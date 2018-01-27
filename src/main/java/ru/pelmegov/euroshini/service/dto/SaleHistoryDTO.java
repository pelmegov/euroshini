package ru.pelmegov.euroshini.service.dto;


import ru.pelmegov.euroshini.domain.SaleHistory;
import ru.pelmegov.euroshini.domain.Tire;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the SaleHistory entity.
 */
public class SaleHistoryDTO implements Serializable {

    private Long id;

    private Integer count;

    private BigDecimal price;

    private BigDecimal sum;

    private Tire tire;

    private String createdBy;

    private Instant createdDate;

    public SaleHistoryDTO() {
        // Empty constructor needed for Jackson.
    }

    public SaleHistoryDTO(SaleHistory saleHistory) {
        this.id = saleHistory.getId();
        this.count = saleHistory.getCount();
        this.price = saleHistory.getPrice();
        this.sum = saleHistory.getSum();
        this.tire = saleHistory.getTire();
        this.createdBy = saleHistory.getCreatedBy();
        this.createdDate = saleHistory.getCreatedDate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public Tire getTire() {
        return tire;
    }

    public void setTire(Tire tire) {
        this.tire = tire;
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

        SaleHistoryDTO saleHistoryDTO = (SaleHistoryDTO) o;
        if(saleHistoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), saleHistoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SaleHistoryDTO{" +
            "id=" + getId() +
            ", count=" + getCount() +
            ", price=" + getPrice() +
            ", sum=" + getSum() +
            "}";
    }
}
