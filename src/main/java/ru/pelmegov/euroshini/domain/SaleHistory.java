package ru.pelmegov.euroshini.domain;


import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A SaleHistory.
 */
@Entity
@Table(name = "sale_history")
@Document(indexName = "salehistory")
public class SaleHistory extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "count")
    private Integer count;

    @Column(name = "price", precision=10, scale=2)
    private BigDecimal price;

    @Column(name = "sum", precision=10, scale=2)
    private BigDecimal sum;

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

    public SaleHistory count(Integer count) {
        this.count = count;
        return this;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public SaleHistory price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public SaleHistory sum(BigDecimal sum) {
        this.sum = sum;
        return this;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public Tire getTire() {
        return tire;
    }

    public SaleHistory tire(Tire tire) {
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
        SaleHistory saleHistory = (SaleHistory) o;
        if (saleHistory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), saleHistory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SaleHistory{" +
            "id=" + getId() +
            ", count=" + getCount() +
            ", price=" + getPrice() +
            ", sum=" + getSum() +
            "}";
    }
}
