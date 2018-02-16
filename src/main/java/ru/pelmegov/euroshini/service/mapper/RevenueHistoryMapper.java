package ru.pelmegov.euroshini.service.mapper;

import ru.pelmegov.euroshini.domain.*;
import ru.pelmegov.euroshini.service.dto.RevenueHistoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity RevenueHistory and its DTO RevenueHistoryDTO.
 */
@Mapper(componentModel = "spring", uses = {TireMapper.class})
public interface RevenueHistoryMapper extends EntityMapper<RevenueHistoryDTO, RevenueHistory> {

    @Mapping(source = "tire", target = "tire")
    RevenueHistoryDTO toDto(RevenueHistory revenueHistory);

    @Mappings({
        @Mapping(source = "tire", target = "tire"),
        @Mapping(source = "createdBy", target = "createdBy"),
        @Mapping(source = "createdDate", target = "createdDate"),
        @Mapping(source = "lastModifiedBy", target = "lastModifiedBy"),
        @Mapping(source = "lastModifiedDate", target = "lastModifiedDate"),
    })
    RevenueHistory toEntity(RevenueHistoryDTO revenueHistoryDTO);

    default RevenueHistory fromId(Long id) {
        if (id == null) {
            return null;
        }
        RevenueHistory revenueHistory = new RevenueHistory();
        revenueHistory.setId(id);
        return revenueHistory;
    }
}
