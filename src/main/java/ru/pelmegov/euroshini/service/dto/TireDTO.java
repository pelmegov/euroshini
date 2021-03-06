package ru.pelmegov.euroshini.service.dto;


import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import ru.pelmegov.euroshini.domain.SalePoint;
import ru.pelmegov.euroshini.domain.enumeration.Technology;
import ru.pelmegov.euroshini.domain.enumeration.Season;
import ru.pelmegov.euroshini.domain.enumeration.Manufacturer;

/**
 * A DTO for the Tire entity.
 */
public class TireDTO implements Serializable {

    private Long id;

    private String mark;

    private String model;

    private Double radius;

    private String size;

    private Technology technology;

    private String index;

    private String releaseYear;

    private Boolean isStrong;

    private Season season;

    private Manufacturer manufacturer;

    private BigDecimal price;

    private Integer count;

    private SalePoint salePoint;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Technology getTechnology() {
        return technology;
    }

    public void setTechnology(Technology technology) {
        this.technology = technology;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Boolean isIsStrong() {
        return isStrong;
    }

    public void setIsStrong(Boolean isStrong) {
        this.isStrong = isStrong;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public SalePoint getSalePoint() {
        return salePoint;
    }

    public void setSalePoint(SalePoint salePoint) {
        this.salePoint = salePoint;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TireDTO tireDTO = (TireDTO) o;
        if(tireDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tireDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TireDTO{" +
            "id=" + getId() +
            ", mark='" + getMark() + "'" +
            ", model='" + getModel() + "'" +
            ", radius=" + getRadius() +
            ", size='" + getSize() + "'" +
            ", technology='" + getTechnology() + "'" +
            ", index='" + getIndex() + "'" +
            ", releaseYear='" + getReleaseYear() + "'" +
            ", isStrong='" + isIsStrong() + "'" +
            ", season='" + getSeason() + "'" +
            ", manufacturer='" + getManufacturer() + "'" +
            ", price=" + getPrice() +
            ", count=" + getCount() +
            "}";
    }
}
