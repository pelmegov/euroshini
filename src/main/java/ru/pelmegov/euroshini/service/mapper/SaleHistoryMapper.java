package ru.pelmegov.euroshini.service.mapper;

import ru.pelmegov.euroshini.domain.*;
import ru.pelmegov.euroshini.service.dto.SaleHistoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SaleHistory and its DTO SaleHistoryDTO.
 */
@Mapper(componentModel = "spring", uses = {TireMapper.class})
public interface SaleHistoryMapper extends EntityMapper<SaleHistoryDTO, SaleHistory> {

    @Mapping(source = "tire", target = "tire")
    SaleHistoryDTO toDto(SaleHistory saleHistory);

    @Mappings({
        @Mapping(source = "tire", target = "tire"),
        @Mapping(source = "createdBy", target = "createdBy"),
        @Mapping(source = "createdDate", target = "createdDate"),
        @Mapping(source = "lastModifiedBy", target = "lastModifiedBy"),
        @Mapping(source = "lastModifiedDate", target = "lastModifiedDate"),
    })
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
