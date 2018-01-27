package ru.pelmegov.euroshini.service.mapper;

import ru.pelmegov.euroshini.domain.*;
import ru.pelmegov.euroshini.service.dto.SalePointDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SalePoint and its DTO SalePointDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SalePointMapper extends EntityMapper<SalePointDTO, SalePoint> {


    @Mapping(target = "tires", ignore = true)
    SalePoint toEntity(SalePointDTO salePointDTO);

    default SalePoint fromId(Long id) {
        if (id == null) {
            return null;
        }
        SalePoint salePoint = new SalePoint();
        salePoint.setId(id);
        return salePoint;
    }
}
