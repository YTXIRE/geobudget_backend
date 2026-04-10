package com.geobudget.geobudget.service;

import com.geobudget.geobudget.dto.GroupDto;
import com.geobudget.geobudget.entity.Category;
import com.geobudget.geobudget.entity.Group;
import com.geobudget.geobudget.exception.GroupHasDependenciesException;
import com.geobudget.geobudget.repository.CategoryRepository;
import com.geobudget.geobudget.repository.GroupRepository;
import com.geobudget.geobudget.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    public List<GroupDto> getGroupsForUser(Long userId) {
        return groupRepository.findByUserIdIsNullOrUserId(userId).stream()
                .map(this::mapToDto)
                .toList();
    }

    public GroupDto getGroupById(Long id, Long userId) {
        Group group = groupRepository.findByIdAndUserIdIsNullOrUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        return mapToDto(group);
    }

    public GroupDto createGroup(GroupDto dto, Long userId) {
        if (groupRepository.existsByNameAndUserId(dto.getName(), userId)) {
            throw new RuntimeException("Group with this name already exists");
        }
        
        Group group = Group.builder()
                .name(dto.getName())
                .userId(userId)
                .isSystem(false)
                .icon(dto.getIcon())
                .build();
        
        return mapToDto(groupRepository.save(group));
    }

    public GroupDto updateGroup(Long id, GroupDto dto, Long userId) {
        Group group = groupRepository.findByIdAndUserIdIsNullOrUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        
        if (Boolean.TRUE.equals(group.getIsSystem())) {
            throw new RuntimeException("Cannot edit system group");
        }
        
        if (!group.getName().equals(dto.getName())) {
            if (groupRepository.existsByNameAndUserId(dto.getName(), userId)) {
                throw new RuntimeException("Group with this name already exists");
            }
        }
        
        group.setName(dto.getName());
        group.setIcon(dto.getIcon());
        return mapToDto(groupRepository.save(group));
    }

    @Transactional
    public void deleteGroup(Long id, Long userId) {
        Group group = groupRepository.findByIdAndUserIdIsNullOrUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        
        if (Boolean.TRUE.equals(group.getIsSystem())) {
            throw new RuntimeException("Cannot delete system group");
        }
        
        List<Category> categories = categoryRepository.findByGroupId(id);
        int categoryCount = categories.size();
        
        if (categoryCount > 0) {
            List<Long> categoryIds = categories.stream().map(Category::getId).toList();
            long transactionCount = transactionRepository.countByCategoryIdIn(categoryIds);
            
            List<String> categoryNames = categories.stream()
                    .map(Category::getName)
                    .limit(5)
                    .toList();
            String namesStr = String.join(", ", categoryNames);
            if (categoryCount > 5) {
                namesStr += " и ещё " + (categoryCount - 5);
            }
            
            throw new GroupHasDependenciesException(categoryCount, (int) transactionCount, namesStr);
        }
        
        groupRepository.delete(group);
    }

    private GroupDto mapToDto(Group group) {
        return GroupDto.builder()
                .id(group.getId())
                .name(group.getName())
                .userId(group.getUserId())
                .isSystem(group.getIsSystem())
                .createdAt(group.getCreatedAt())
                .updatedAt(group.getUpdatedAt())
                .icon(group.getIcon())
                .build();
    }
}
