package com.geobudget.geobudget.service;

import com.geobudget.geobudget.dto.ColorDto;
import com.geobudget.geobudget.dto.ColorGroupDto;
import com.geobudget.geobudget.entity.Color;
import com.geobudget.geobudget.entity.ColorGroup;
import com.geobudget.geobudget.repository.ColorGroupRepository;
import com.geobudget.geobudget.repository.ColorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ColorService {
    private final ColorGroupRepository colorGroupRepository;
    private final ColorRepository colorRepository;

    public List<ColorGroupDto> getAllColors() {
        Iterable<ColorGroup> groups = colorGroupRepository.findAll();
        return StreamSupport.stream(groups.spliterator(), false)
                .map(group -> {
                    List<Color> colors = colorRepository.findByGroupId(group.getId());
                    List<ColorDto> colorDtos = colors.stream()
                            .map(c -> ColorDto.builder()
                                    .id(c.getId())
                                    .name(c.getName())
                                    .hex(c.getHex())
                                    .argb(c.getArgb())
                                    .build())
                            .toList();
                    return ColorGroupDto.builder()
                            .id(group.getId())
                            .name(group.getName())
                            .colors(colorDtos)
                            .build();
                })
                .toList();
    }
}
