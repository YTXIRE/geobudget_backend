package com.geobudget.geobudget.service;

import com.geobudget.geobudget.dto.IconDto;
import com.geobudget.geobudget.dto.IconGroupDto;
import com.geobudget.geobudget.entity.Icon;
import com.geobudget.geobudget.entity.IconGroup;
import com.geobudget.geobudget.repository.IconGroupRepository;
import com.geobudget.geobudget.repository.IconRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class IconService {
    private final IconGroupRepository iconGroupRepository;
    private final IconRepository iconRepository;

    public List<IconGroupDto> getAllIcons() {
        Iterable<IconGroup> groups = iconGroupRepository.findAll();
        return StreamSupport.stream(groups.spliterator(), false)
                .map(group -> {
                    List<Icon> icons = iconRepository.findByGroupId(group.getId());
                    List<IconDto> iconDtos = icons.stream()
                            .map(i -> IconDto.builder()
                                    .id(i.getId())
                                    .name(i.getName())
                                    .codePoint(i.getCodePoint())
                                    .build())
                            .toList();
                    return IconGroupDto.builder()
                            .id(group.getId())
                            .name(group.getName())
                            .icons(iconDtos)
                            .build();
                })
                .toList();
    }
}
