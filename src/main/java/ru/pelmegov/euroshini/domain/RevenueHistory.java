package ru.pelmegov.euroshini.domain;


import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.Objects;

/**
 * A RevenueHistory.
 */
@Entity
@Table(name = "revenue_history")
@Document(indexName = "revenuehistory")
public class RevenueHistory extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "count")
    private Integer count;

    @ManyToOne
    private Tire tire;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public RevenueHistory count(Integer count) {
        this.count = count;
        return this;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Tire getTire() {
        return tire;
    }

    public RevenueHistory tire(Tire tire) {
        this.tire = tire;
        return this;
    }

    public void setTire(Tire tire) {
        this.tire = tire;
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
        RevenueHistory revenueHistory = (RevenueHistory) o;
        if (revenueHistory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), revenueHistory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RevenueHistory{" +
            "id=" + getId() +
            ", count=" + getCount() +
            "}";
    }
}
