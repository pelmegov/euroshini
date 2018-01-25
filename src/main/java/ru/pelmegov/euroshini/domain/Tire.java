package ru.pelmegov.euroshini.domain;


import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import ru.pelmegov.euroshini.domain.enumeration.Season;

import ru.pelmegov.euroshini.domain.enumeration.Manufacturer;

import ru.pelmegov.euroshini.domain.enumeration.Technology;

/**
 * A Tire.
 */
@Entity
@Table(name = "tire")
@Document(indexName = "tire")
public class Tire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "is_strong")
    private Boolean isStrong;

    @Column(name = "radius")
    private Double radius;

    @Column(name = "release_year")
    private String releaseYear;

    @Column(name = "jhi_size")
    private String size;

    @Column(name = "mark")
    private String mark;

    @Column(name = "model")
    private String model;

    @Column(name = "jhi_index")
    private String index;

    @Column(name = "price", precision=10, scale=2)
    private BigDecimal price;

    @Column(name = "count")
    private Integer count;

    @Enumerated(EnumType.STRING)
    @Column(name = "season")
    private Season season;

    @Enumerated(EnumType.STRING)
    @Column(name = "manufacturer")
    private Manufacturer manufacturer;

    @Enumerated(EnumType.STRING)
    @Column(name = "technology")
    private Technology technology;

    @ManyToOne
    private SalePoint salePoint;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Tire title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
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
            ", title='" + getTitle() + "'" +
            ", isStrong='" + isIsStrong() + "'" +
            ", radius=" + getRadius() +
            ", releaseYear='" + getReleaseYear() + "'" +
            ", size='" + getSize() + "'" +
            ", mark='" + getMark() + "'" +
            ", model='" + getModel() + "'" +
            ", index='" + getIndex() + "'" +
            ", price=" + getPrice() +
            ", count=" + getCount() +
            ", season='" + getSeason() + "'" +
            ", manufacturer='" + getManufacturer() + "'" +
            ", technology='" + getTechnology() + "'" +
            "}";
    }
}
