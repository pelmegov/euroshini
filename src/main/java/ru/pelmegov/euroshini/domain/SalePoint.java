package ru.pelmegov.euroshini.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A SalePoint.
 */
@Entity
@Table(name = "sale_point")
@Document(indexName = "salepoint")
public class SalePoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "salePoint")
    @JsonIgnore
    private Set<Tire> tires = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public SalePoint name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Tire> getTires() {
        return tires;
    }

    public SalePoint tires(Set<Tire> tires) {
        this.tires = tires;
        return this;
    }

    public SalePoint addTire(Tire tire) {
        this.tires.add(tire);
        tire.setSalePoint(this);
        return this;
    }

    public SalePoint removeTire(Tire tire) {
        this.tires.remove(tire);
        tire.setSalePoint(null);
        return this;
    }

    public void setTires(Set<Tire> tires) {
        this.tires = tires;
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
        SalePoint salePoint = (SalePoint) o;
        if (salePoint.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), salePoint.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SalePoint{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
