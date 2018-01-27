package ru.pelmegov.euroshini.service.mapper.impl;

import org.springframework.stereotype.Service;
import ru.pelmegov.euroshini.domain.Tire;
import ru.pelmegov.euroshini.service.dto.TireDTO;
import ru.pelmegov.euroshini.service.mapper.TireMapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TireMapperImpl implements TireMapper {

    @Override
    public TireDTO toDto(Tire entity) {
        return new TireDTO(entity);
    }

    @Override
    public Tire toEntity(TireDTO tireDTO) {
        if (tireDTO == null) {
            return null;
        } else {
            Tire tire = new Tire();
            tire.setId(tireDTO.getId());
            tire.setCount(tireDTO.getCount());
            tire.setIndex(tireDTO.getIndex());
            tire.setIsStrong(tireDTO.isIsStrong());
            tire.setModel(tireDTO.getModel());
            tire.setMark(tireDTO.getMark());
            tire.setPrice(tireDTO.getPrice());
            tire.setManufacturer(tireDTO.getManufacturer());
            tire.setRadius(tireDTO.getRadius());
            tire.setReleaseYear(tireDTO.getReleaseYear());
            tire.setTitle(tireDTO.getTitle());
            tire.setTechnology(tireDTO.getTechnology());
            tire.setSeason(tireDTO.getSeason());
            tire.setSize(tireDTO.getSize());
            tire.setSalePoint(tireDTO.getSalePoint());
            return tire;
        }
    }

    @Override
    public List<Tire> toEntity(List<TireDTO> dtoList) {
        return dtoList.stream()
            .filter(Objects::nonNull)
            .map(this::toEntity)
            .collect(Collectors.toList());
    }

    @Override
    public List<TireDTO> toDto(List<Tire> entityList) {
        return entityList.stream()
            .filter(Objects::nonNull)
            .map(this::toDto)
            .collect(Collectors.toList());
    }

}
