package com.geobudget.geobudget.service;

import com.geobudget.geobudget.dto.GroupDto;
import com.geobudget.geobudget.entity.Group;
import com.geobudget.geobudget.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;

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
        return mapToDto(groupRepository.save(group));
    }

    public void deleteGroup(Long id, Long userId) {
        Group group = groupRepository.findByIdAndUserIdIsNullOrUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        
        if (Boolean.TRUE.equals(group.getIsSystem())) {
            throw new RuntimeException("Cannot delete system group");
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
                .build();
    }
}
