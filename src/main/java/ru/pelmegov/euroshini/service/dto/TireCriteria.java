package ru.pelmegov.euroshini.service.dto;

import java.io.Serializable;
import ru.pelmegov.euroshini.domain.enumeration.Technology;
import ru.pelmegov.euroshini.domain.enumeration.Season;
import ru.pelmegov.euroshini.domain.enumeration.Manufacturer;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;

/**
 * Criteria class for the Tire entity. This class is used in TireResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /tires?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TireCriteria implements Serializable {
    /**
     * Class for filtering Technology
     */
    public static class TechnologyFilter extends Filter<Technology> {
    }

    /**
     * Class for filtering Season
     */
    public static class SeasonFilter extends Filter<Season> {
    }

    /**
     * Class for filtering Manufacturer
     */
    public static class ManufacturerFilter extends Filter<Manufacturer> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter mark;

    private StringFilter model;

    private DoubleFilter radius;

    private TechnologyFilter technology;

    private StringFilter index;

    private StringFilter releaseYear;

    private BooleanFilter isStrong;

    private SeasonFilter season;

    private ManufacturerFilter manufacturer;

    private BigDecimalFilter price;

    private IntegerFilter count;

    private LongFilter salePointId;

    public TireCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getMark() {
        return mark;
    }

    public void setMark(StringFilter mark) {
        this.mark = mark;
    }

    public StringFilter getModel() {
        return model;
    }

    public void setModel(StringFilter model) {
        this.model = model;
    }

    public DoubleFilter getRadius() {
        return radius;
    }

    public void setRadius(DoubleFilter radius) {
        this.radius = radius;
    }

    public TechnologyFilter getTechnology() {
        return technology;
    }

    public void setTechnology(TechnologyFilter technology) {
        this.technology = technology;
    }

    public StringFilter getIndex() {
        return index;
    }

    public void setIndex(StringFilter index) {
        this.index = index;
    }

    public StringFilter getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(StringFilter releaseYear) {
        this.releaseYear = releaseYear;
    }

    public BooleanFilter getIsStrong() {
        return isStrong;
    }

    public void setIsStrong(BooleanFilter isStrong) {
        this.isStrong = isStrong;
    }

    public SeasonFilter getSeason() {
        return season;
    }

    public void setSeason(SeasonFilter season) {
        this.season = season;
    }

    public ManufacturerFilter getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(ManufacturerFilter manufacturer) {
        this.manufacturer = manufacturer;
    }

    public BigDecimalFilter getPrice() {
        return price;
    }

    public void setPrice(BigDecimalFilter price) {
        this.price = price;
    }

    public IntegerFilter getCount() {
        return count;
    }

    public void setCount(IntegerFilter count) {
        this.count = count;
    }

    public LongFilter getSalePointId() {
        return salePointId;
    }

    public void setSalePointId(LongFilter salePointId) {
        this.salePointId = salePointId;
    }

    @Override
    public String toString() {
        return "TireCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (mark != null ? "mark=" + mark + ", " : "") +
            (model != null ? "model=" + model + ", " : "") +
            (radius != null ? "radius=" + radius + ", " : "") +
            (technology != null ? "technology=" + technology + ", " : "") +
            (index != null ? "index=" + index + ", " : "") +
            (releaseYear != null ? "releaseYear=" + releaseYear + ", " : "") +
            (isStrong != null ? "isStrong=" + isStrong + ", " : "") +
            (season != null ? "season=" + season + ", " : "") +
            (manufacturer != null ? "manufacturer=" + manufacturer + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (count != null ? "count=" + count + ", " : "") +
            (salePointId != null ? "salePointId=" + salePointId + ", " : "") +
            "}";
    }

}
