package ru.pelmegov.euroshini.service.dto;


import ru.pelmegov.euroshini.domain.SalePoint;
import ru.pelmegov.euroshini.domain.Tire;
import ru.pelmegov.euroshini.domain.enumeration.Manufacturer;
import ru.pelmegov.euroshini.domain.enumeration.Season;
import ru.pelmegov.euroshini.domain.enumeration.Technology;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the Tire entity.
 */
public class TireDTO implements Serializable {

    private Long id;

    private String title;

    private Boolean isStrong;

    private Double radius;

    private String releaseYear;

    private String size;

    private String mark;

    private String model;

    private String index;

    private Season season;

    private Manufacturer manufacturer;

    private Technology technology;

    private BigDecimal price;

    private Integer count;

    private SalePoint salePoint;

    private String createdBy;

    private Instant createdDate;

    public TireDTO() {
        // Empty constructor needed for Jackson.
    }

    public TireDTO(Tire entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.isStrong = entity.isIsStrong();
        this.radius = entity.getRadius();
        this.releaseYear = entity.getReleaseYear();
        this.size = entity.getSize();
        this.mark = entity.getMark();
        this.model = entity.getModel();
        this.index = entity.getIndex();
        this.season = entity.getSeason();
        this.manufacturer = entity.getManufacturer();
        this.technology = entity.getTechnology();
        this.price = entity.getPrice();
        this.count = entity.getCount();
        this.salePoint = entity.getSalePoint();
        this.createdBy = entity.getCreatedBy();
        this.createdDate = entity.getCreatedDate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean isIsStrong() {
        return isStrong;
    }

    public void setIsStrong(Boolean isStrong) {
        this.isStrong = isStrong;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
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

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
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

    public Technology getTechnology() {
        return technology;
    }

    public void setTechnology(Technology technology) {
        this.technology = technology;
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

    public Boolean getStrong() {
        return isStrong;
    }

    public void setStrong(Boolean strong) {
        isStrong = strong;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TireDTO tireDTO = (TireDTO) o;
        if (tireDTO.getId() == null || getId() == null) {
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
            ", title='" + getTitle() + "'" +
            ", isStrong='" + isIsStrong() + "'" +
            ", radius=" + getRadius() +
            ", releaseYear='" + getReleaseYear() + "'" +
            ", size='" + getSize() + "'" +
            ", mark='" + getMark() + "'" +
            ", model='" + getModel() + "'" +
            ", index='" + getIndex() + "'" +
            ", season='" + getSeason() + "'" +
            ", manufacturer='" + getManufacturer() + "'" +
            ", technology='" + getTechnology() + "'" +
            ", price=" + getPrice() +
            ", count=" + getCount() +
            "}";
    }
}
