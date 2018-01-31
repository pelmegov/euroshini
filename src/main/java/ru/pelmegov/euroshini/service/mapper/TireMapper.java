package ru.pelmegov.euroshini.service.mapper;

import ru.pelmegov.euroshini.domain.*;
import ru.pelmegov.euroshini.service.dto.TireDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Tire and its DTO TireDTO.
 */
@Mapper(componentModel = "spring", uses = {SalePointMapper.class})
public interface TireMapper extends EntityMapper<TireDTO, Tire> {

    @Mapping(source = "salePoint", target = "salePoint")
    TireDTO toDto(Tire tire);

    @Mapping(source = "salePoint", target = "salePoint")
    Tire toEntity(TireDTO tireDTO);

    default Tire fromId(Long id) {
        if (id == null) {
            return null;
        }
        Tire tire = new Tire();
        tire.setId(id);
        return tire;
    }
}
