package ru.pelmegov.euroshini.service.mapper.impl;

import org.springframework.stereotype.Service;
import ru.pelmegov.euroshini.domain.SaleHistory;
import ru.pelmegov.euroshini.service.dto.SaleHistoryDTO;
import ru.pelmegov.euroshini.service.mapper.SaleHistoryMapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SaleHistoryMapperImpl implements SaleHistoryMapper {

    @Override
    public SaleHistoryDTO toDto(SaleHistory saleHistory) {
        return new SaleHistoryDTO(saleHistory);
    }

    @Override
    public SaleHistory toEntity(SaleHistoryDTO saleHistoryDTO) {
        if (saleHistoryDTO == null) {
            return null;
        } else {
            SaleHistory saleHistory = new SaleHistory();
            saleHistory.setId(saleHistoryDTO.getId());
            saleHistory.setCount(saleHistoryDTO.getCount());
            saleHistory.setPrice(saleHistoryDTO.getPrice());
            saleHistory.setSum(saleHistoryDTO.getSum());
            saleHistory.setTire(saleHistoryDTO.getTire());
            return saleHistory;
        }
    }

    @Override
    public List<SaleHistory> toEntity(List<SaleHistoryDTO> dtoList) {
        return dtoList.stream()
            .filter(Objects::nonNull)
            .map(this::toEntity)
            .collect(Collectors.toList());
    }

    @Override
    public List<SaleHistoryDTO> toDto(List<SaleHistory> entityList) {
        return entityList.stream()
            .filter(Objects::nonNull)
            .map(this::toDto)
            .collect(Collectors.toList());
    }

}
