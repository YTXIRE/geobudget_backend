package com.geobudget.geobudget.service;

import com.geobudget.geobudget.dto.transaction.TransactionCreateRequest;
import com.geobudget.geobudget.dto.transaction.TransactionSummaryResponse;
import com.geobudget.geobudget.dto.transaction.TransactionUpdateRequest;
import com.geobudget.geobudget.entity.Category;
import com.geobudget.geobudget.entity.Transaction;
import com.geobudget.geobudget.repository.CategoryRepository;
import com.geobudget.geobudget.repository.TransactionRepository;
import com.geobudget.geobudget.repository.TransactionSummaryProjection;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private TransactionService transactionService;

    private TransactionCreateRequest incomeRequest;

    @BeforeEach
    void setUp() {
        incomeRequest = TransactionCreateRequest.builder()
                .type("income")
                .amount(new BigDecimal("15000.00"))
                .categoryId(2L)
                .description("Salary")
                .occurredAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createIncome_success() {
        Transaction saved = Transaction.builder()
                .id(1L)
                .userId(7L)
                .type("income")
                .amount(new BigDecimal("15000.00"))
                .category(Category.builder().id(2L).transactionType("income").type("system").build())
                .description("Salary")
                .occurredAt(incomeRequest.getOccurredAt())
                .createdAt(LocalDateTime.now())
                .build();

        when(categoryRepository.findById(2L)).thenReturn(Optional.of(Category.builder().id(2L).transactionType("income").type("system").build()));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(saved);

        var response = transactionService.create(7L, incomeRequest);

        assertEquals(1L, response.getId());
        assertEquals("income", response.getType());
        assertEquals(new BigDecimal("15000.00"), response.getAmount());
        assertNotNull(response.getCreatedAt());
    }

    @Test
    void createWithoutCategory_failsValidation() {
        TransactionCreateRequest expenseRequest = TransactionCreateRequest.builder()
                .type("expense")
                .amount(new BigDecimal("100.00"))
                .occurredAt(LocalDateTime.now())
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transactionService.create(7L, expenseRequest));

        assertEquals("categoryId is required", ex.getMessage());
    }

    @Test
    void getAll_filtersByTypeAndDates() {
        LocalDateTime from = LocalDateTime.now().minusDays(10);
        LocalDateTime to = LocalDateTime.now();

        when(transactionRepository.findAll(any(Specification.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of()));

        when(categoryRepository.findById(2L)).thenReturn(Optional.of(Category.builder().id(2L).transactionType("income").type("system").build()));

        transactionService.getAll(7L, "income", from, to, 2L, null, null, PageRequest.of(0, 20));

        verify(transactionRepository).findAll(any(Specification.class), eq(PageRequest.of(0, 20)));
    }

    @Test
    void update_foreignCategoryIsNotAccessible() {
        Transaction transaction = Transaction.builder().id(11L).userId(7L).type("income").amount(BigDecimal.ONE).occurredAt(LocalDateTime.now()).build();
        Category foreignCategory = Category.builder().id(5L).type("user").userId(99L).build();

        TransactionUpdateRequest updateRequest = TransactionUpdateRequest.builder()
                .type("expense")
                .amount(new BigDecimal("10.00"))
                .categoryId(5L)
                .description("Coffee")
                .occurredAt(LocalDateTime.now())
                .build();

        when(transactionRepository.findByIdAndUserIdAndIsDeletedFalse(11L, 7L)).thenReturn(Optional.of(transaction));
        when(categoryRepository.findById(5L)).thenReturn(Optional.of(foreignCategory));

        assertThrows(EntityNotFoundException.class, () -> transactionService.update(7L, 11L, updateRequest));
    }

    @Test
    void create_whenTypeMismatchWithCategory_failsValidation() {
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(Category.builder().id(2L).transactionType("expense").type("system").build()));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transactionService.create(7L, incomeRequest));

        assertEquals("Transaction type does not match category type", ex.getMessage());
    }

    @Test
    void create_whenOnlyLatitudeProvided_failsValidation() {
        TransactionCreateRequest request = TransactionCreateRequest.builder()
                .type("income")
                .amount(new BigDecimal("5000.00"))
                .categoryId(2L)
                .occurredAt(LocalDateTime.now())
                .latitude(new BigDecimal("55.75"))
                .build();

        when(categoryRepository.findById(2L)).thenReturn(Optional.of(Category.builder().id(2L).transactionType("income").type("system").build()));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transactionService.create(7L, request));

        assertEquals("latitude and longitude must be provided together", ex.getMessage());
    }

    @Test
    void softDelete_setsIsDeletedTrue() {
        Transaction transaction = Transaction.builder().id(11L).userId(7L).type("income").amount(BigDecimal.ONE).occurredAt(LocalDateTime.now()).isDeleted(false).build();
        when(transactionRepository.findByIdAndUserIdAndIsDeletedFalse(11L, 7L)).thenReturn(Optional.of(transaction));

        transactionService.softDelete(7L, 11L);

        assertEquals(true, transaction.getIsDeleted());
        verify(transactionRepository).save(transaction);
    }

    @Test
    void summary_returnsBalance() {
        TransactionSummaryProjection projection = new TransactionSummaryProjection() {
            @Override
            public BigDecimal getIncome() {
                return new BigDecimal("250000.00");
            }

            @Override
            public BigDecimal getExpense() {
                return new BigDecimal("187540.50");
            }
        };

        when(transactionRepository.getSummary(eq(7L), any(), any())).thenReturn(projection);

        TransactionSummaryResponse response = transactionService.getSummary(7L, LocalDateTime.now().minusDays(1), LocalDateTime.now());

        assertEquals(new BigDecimal("250000.00"), response.getIncome());
        assertEquals(new BigDecimal("187540.50"), response.getExpense());
        assertEquals(new BigDecimal("62459.50"), response.getBalance());
    }
}
