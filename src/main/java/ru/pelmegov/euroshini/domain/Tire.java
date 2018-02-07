package ru.pelmegov.euroshini.domain;


import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import ru.pelmegov.euroshini.domain.enumeration.Technology;

import ru.pelmegov.euroshini.domain.enumeration.Season;

import ru.pelmegov.euroshini.domain.enumeration.Manufacturer;

/**
 * A Tire.
 */
@Entity
@Table(name = "tire")
@Document(indexName = "tire")
public class Tire extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "mark")
    private String mark;

    @Column(name = "model")
    private String model;

    @Column(name = "radius")
    private Double radius;

    @Column(name = "jhi_size")
    private String size;

    @Enumerated(EnumType.STRING)
    @Column(name = "technology")
    private Technology technology;

    @Column(name = "jhi_index")
    private String index;

    @Column(name = "release_year")
    private String releaseYear;

    @Column(name = "is_strong")
    private Boolean isStrong;

    @Enumerated(EnumType.STRING)
    @Column(name = "season")
    private Season season;

    @Enumerated(EnumType.STRING)
    @Column(name = "manufacturer")
    private Manufacturer manufacturer;

    @Column(name = "price", precision=10, scale=2)
    private BigDecimal price;

    @Column(name = "count")
    private Integer count;

    @ManyToOne
    private SalePoint salePoint;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMark() {
        return mark;
    }

    public Tire mark(String mark) {
        this.mark = mark;
        return this;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getModel() {
        return model;
    }

    public Tire model(String model) {
        this.model = model;
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getRadius() {
        return radius;
    }

    public Tire radius(Double radius) {
        this.radius = radius;
        return this;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public String getSize() {
        return size;
    }

    public Tire size(String size) {
        this.size = size;
        return this;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Technology getTechnology() {
        return technology;
    }

    public Tire technology(Technology technology) {
        this.technology = technology;
        return this;
    }

    public void setTechnology(Technology technology) {
        this.technology = technology;
    }

    public String getIndex() {
        return index;
    }

    public Tire index(String index) {
        this.index = index;
        return this;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public Tire releaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
        return this;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Boolean isIsStrong() {
        return isStrong;
    }

    public Tire isStrong(Boolean isStrong) {
        this.isStrong = isStrong;
        return this;
    }

    public void setIsStrong(Boolean isStrong) {
        this.isStrong = isStrong;
    }

    public Season getSeason() {
        return season;
    }

    public Tire season(Season season) {
        this.season = season;
        return this;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public Tire manufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
        return this;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Tire price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public Tire count(Integer count) {
        this.count = count;
        return this;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public SalePoint getSalePoint() {
        return salePoint;
    }

    public Tire salePoint(SalePoint salePoint) {
        this.salePoint = salePoint;
        return this;
    }

    public void setSalePoint(SalePoint salePoint) {
        this.salePoint = salePoint;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tire tire = (Tire) o;
        if (tire.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tire.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Tire{" +
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
