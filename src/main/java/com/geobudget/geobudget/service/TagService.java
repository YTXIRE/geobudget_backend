package com.geobudget.geobudget.service;

import com.geobudget.geobudget.dto.tag.*;
import com.geobudget.geobudget.entity.*;
import com.geobudget.geobudget.repository.*;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final TagTransactionRepository tagTransactionRepository;
    private final TransactionRepository transactionRepository;

    public List<TagDto> getTagsByUser(Long userId) {
        return tagRepository.findByUserId(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public TagDto getTagById(Long id, Long userId) {
        return tagRepository.findByIdAndUserId(id, userId)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Тег не найден"));
    }

    @Transactional
    public TagDto createTag(CreateTagRequest request, Long userId) {
        if (tagRepository.existsByNameAndUserId(request.getName(), userId)) {
            throw new RuntimeException("Тег с таким названием уже существует");
        }

        Tag tag = Tag.builder()
                .name(request.getName())
                .color(request.getColor())
                .icon(request.getIcon())
                .userId(userId)
                .build();

        tag = tagRepository.save(tag);
        return toDto(tag);
    }

    @Transactional
    public TagDto updateTag(Long id, UpdateTagRequest request, Long userId) {
        Tag tag = tagRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Тег не найден"));

        if (request.getName() != null && !request.getName().isBlank()) {
            if (!request.getName().equals(tag.getName()) &&
                tagRepository.existsByNameAndUserId(request.getName(), userId)) {
                throw new RuntimeException("Тег с таким названием уже существует");
            }
            tag.setName(request.getName());
        }
        if (request.getColor() != null) {
            tag.setColor(request.getColor());
        }
        if (request.getIcon() != null) {
            tag.setIcon(request.getIcon());
        }

        tag = tagRepository.save(tag);
        return toDto(tag);
    }

    @Transactional
    public void deleteTag(Long id, Long userId) {
        Tag tag = tagRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Тег не найден"));

        tagTransactionRepository.deleteByTagIdAndUserId(id, userId);
        tagRepository.delete(tag);
    }

    @Transactional(readOnly = true)
    public TagStatsResponse getTagStats(Long userId, LocalDateTime from, LocalDateTime to, int limit) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new IllegalArgumentException("from must be before or equal to to");
        }

        List<Transaction> transactions = transactionRepository.findAll(buildTransactionSpec(userId, from, to));
        if (transactions.isEmpty()) {
            return TagStatsResponse.builder()
                    .summary(TagStatsResponse.TagStatsSummary.builder()
                            .taggedTransactions(0L)
                            .taggedAmount(BigDecimal.ZERO)
                            .uniqueTags(0L)
                            .build())
                    .tags(List.of())
                    .build();
        }

        List<Long> transactionIds = transactions.stream()
                .map(Transaction::getId)
                .toList();
        Map<Long, Transaction> transactionById = transactions.stream()
                .collect(Collectors.toMap(Transaction::getId, transaction -> transaction));
        Map<Long, Tag> tagsById = tagRepository.findByUserId(userId).stream()
                .collect(Collectors.toMap(Tag::getId, tag -> tag));

        List<TagTransaction> links = tagTransactionRepository.findByUserIdAndTransactionIdIn(userId, transactionIds);
        Map<Long, TagAggregate> aggregates = new HashMap<>();
        Set<Long> taggedTransactionIds = links.stream()
                .map(TagTransaction::getTransactionId)
                .collect(Collectors.toSet());

        for (TagTransaction link : links) {
            Transaction transaction = transactionById.get(link.getTransactionId());
            Tag tag = tagsById.get(link.getTagId());
            if (transaction == null || tag == null) {
                continue;
            }

            BigDecimal amount = transaction.getBaseAmount() != null
                    ? transaction.getBaseAmount()
                    : transaction.getAmount();
            TagAggregate aggregate = aggregates.computeIfAbsent(tag.getId(), ignored -> new TagAggregate(tag));
            aggregate.transactionsCount += 1;
            aggregate.totalAmount = aggregate.totalAmount.add(amount != null ? amount.abs() : BigDecimal.ZERO);
        }

        List<TagStatsResponse.TagStatItem> items = aggregates.values().stream()
                .sorted((left, right) -> {
                    int amountCompare = right.totalAmount.compareTo(left.totalAmount);
                    if (amountCompare != 0) {
                        return amountCompare;
                    }
                    return Long.compare(right.transactionsCount, left.transactionsCount);
                })
                .limit(Math.max(limit, 1))
                .map(aggregate -> TagStatsResponse.TagStatItem.builder()
                        .id(aggregate.tag.getId())
                        .name(aggregate.tag.getName())
                        .color(aggregate.tag.getColor())
                        .icon(aggregate.tag.getIcon())
                        .transactionsCount(aggregate.transactionsCount)
                        .totalAmount(aggregate.totalAmount)
                        .build())
                .toList();

        BigDecimal taggedAmount = taggedTransactionIds.stream()
                .map(transactionById::get)
                .filter(java.util.Objects::nonNull)
                .map(transaction -> transaction.getBaseAmount() != null ? transaction.getBaseAmount() : transaction.getAmount())
                .filter(java.util.Objects::nonNull)
                .map(BigDecimal::abs)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return TagStatsResponse.builder()
                .summary(TagStatsResponse.TagStatsSummary.builder()
                        .taggedTransactions((long) taggedTransactionIds.size())
                        .taggedAmount(taggedAmount)
                        .uniqueTags((long) aggregates.size())
                        .build())
                .tags(items)
                .build();
    }

    @Transactional
    public void addTagsToTransaction(Long transactionId, List<Long> tagIds, Long userId) {
        tagTransactionRepository.deleteByTransactionIdAndUserId(transactionId, userId);

        for (Long tagId : tagIds) {
            TagTransaction tt = TagTransaction.builder()
                    .tagId(tagId)
                    .transactionId(transactionId)
                    .userId(userId)
                    .build();
            tagTransactionRepository.save(tt);
        }
    }

    public List<TagDto> getTagsByTransaction(Long transactionId, Long userId) {
        return tagTransactionRepository.findByTransactionIdAndUserId(transactionId, userId)
                .stream()
                .map(tt -> tagRepository.findById(tt.getTagId()))
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private TagDto toDto(Tag tag) {
        return TagDto.builder()
                .id(tag.getId())
                .name(tag.getName())
                .color(tag.getColor())
                .icon(tag.getIcon())
                .build();
    }

    private Specification<Transaction> buildTransactionSpec(Long userId, LocalDateTime from, LocalDateTime to) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("userId"), userId));
            predicates.add(cb.isFalse(root.get("isDeleted")));
            if (from != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("occurredAt"), from));
            }
            if (to != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("occurredAt"), to));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    private static class TagAggregate {
        private final Tag tag;
        private long transactionsCount = 0;
        private BigDecimal totalAmount = BigDecimal.ZERO;

        private TagAggregate(Tag tag) {
            this.tag = tag;
        }
    }
}
