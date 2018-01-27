package ru.pelmegov.euroshini.service.mapper;

import ru.pelmegov.euroshini.domain.*;
import ru.pelmegov.euroshini.service.dto.SaleHistoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SaleHistory and its DTO SaleHistoryDTO.
 */
@Mapper(componentModel = "spring", uses = {TireMapper.class})
public interface SaleHistoryMapper extends EntityMapper<SaleHistoryDTO, SaleHistory> {

    @Mapping(source = "tire.id", target = "tireId")
    SaleHistoryDTO toDto(SaleHistory saleHistory);

    @Mapping(source = "tireId", target = "tire")
    SaleHistory toEntity(SaleHistoryDTO saleHistoryDTO);

    default SaleHistory fromId(Long id) {
        if (id == null) {
            return null;
        }
        SaleHistory saleHistory = new SaleHistory();
        saleHistory.setId(id);
        return saleHistory;
    }
}
