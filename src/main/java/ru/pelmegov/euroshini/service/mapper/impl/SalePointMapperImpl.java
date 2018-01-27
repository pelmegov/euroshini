package ru.pelmegov.euroshini.service.mapper.impl;

import org.springframework.stereotype.Service;
import ru.pelmegov.euroshini.domain.SalePoint;
import ru.pelmegov.euroshini.service.dto.SalePointDTO;
import ru.pelmegov.euroshini.service.mapper.SalePointMapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SalePointMapperImpl implements SalePointMapper {

    @Override
    public SalePointDTO toDto(SalePoint entity) {
        return new SalePointDTO(entity);
    }

    @Override
    public SalePoint toEntity(SalePointDTO salePointDTO) {
        if (salePointDTO == null) {
            return null;
        } else {
            SalePoint salePoint = new SalePoint();
            salePoint.setId(salePointDTO.getId());
            salePoint.setName(salePointDTO.getName());
            return salePoint;
        }
    }

    @Override
    public List<SalePoint> toEntity(List<SalePointDTO> dtoList) {
        return dtoList.stream()
            .filter(Objects::nonNull)
            .map(this::toEntity)
            .collect(Collectors.toList());
    }

    @Override
    public List<SalePointDTO> toDto(List<SalePoint> entityList) {
        return entityList.stream()
            .filter(Objects::nonNull)
            .map(this::toDto)
            .collect(Collectors.toList());
    }

}
