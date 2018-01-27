package ru.pelmegov.euroshini.service.dto;


import ru.pelmegov.euroshini.domain.SalePoint;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the SalePoint entity.
 */
public class SalePointDTO implements Serializable {

    private Long id;

    private String name;

    public SalePointDTO() {
        // Empty constructor needed for Jackson.
    }

    public SalePointDTO(SalePoint salePoint) {
        this.id = salePoint.getId();
        this.name = salePoint.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SalePointDTO salePointDTO = (SalePointDTO) o;
        if(salePointDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), salePointDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SalePointDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
