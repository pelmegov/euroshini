package ru.pelmegov.euroshini.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import ru.pelmegov.euroshini.domain.Tire;

import ru.pelmegov.euroshini.domain.*; // for static metamodels
import ru.pelmegov.euroshini.domain.enumeration.*; // for static metamodels

import ru.pelmegov.euroshini.repository.TireRepository;
import ru.pelmegov.euroshini.repository.search.TireSearchRepository;
import ru.pelmegov.euroshini.service.dto.TireCriteria;

import ru.pelmegov.euroshini.service.dto.TireDTO;
import ru.pelmegov.euroshini.service.mapper.TireMapper;

/**
 * Service for executing complex queries for Tire entities in the database.
 * The main input is a {@link TireCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TireDTO} or a {@link Page} of {@link TireDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TireQueryService extends QueryService<Tire> {

    private final Logger log = LoggerFactory.getLogger(TireQueryService.class);

    private final TireRepository tireRepository;

    private final TireMapper tireMapper;

    private final TireSearchRepository tireSearchRepository;

    public TireQueryService(TireRepository tireRepository, TireMapper tireMapper, TireSearchRepository tireSearchRepository) {
        this.tireRepository = tireRepository;
        this.tireMapper = tireMapper;
        this.tireSearchRepository = tireSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TireDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TireDTO> findByCriteria(TireCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Tire> specification = createSpecification(criteria);
        return tireMapper.toDto(tireRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TireDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TireDTO> findByCriteria(TireCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Tire> specification = createSpecification(criteria);
        final Page<Tire> result = tireRepository.findAll(specification, page);
        return result.map(tireMapper::toDto);
    }

    /**
     * Function to convert TireCriteria to a {@link Specifications}
     */
    private Specifications<Tire> createSpecification(TireCriteria criteria) {
        Specifications<Tire> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Tire_.id));
            }
            if (criteria.getMark() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMark(), Tire_.mark));
            }
            if (criteria.getModel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModel(), Tire_.model));
            }
            if (criteria.getRadius() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRadius(), Tire_.radius));
            }
            if (criteria.getTechnology() != null) {
                specification = specification.and(buildSpecification(criteria.getTechnology(), Tire_.technology));
            }
            if (criteria.getIndex() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIndex(), Tire_.index));
            }
            if (criteria.getReleaseYear() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReleaseYear(), Tire_.releaseYear));
            }
            if (criteria.getIsStrong() != null) {
                specification = specification.and(buildSpecification(criteria.getIsStrong(), Tire_.isStrong));
            }
            if (criteria.getSeason() != null) {
                specification = specification.and(buildSpecification(criteria.getSeason(), Tire_.season));
            }
            if (criteria.getManufacturer() != null) {
                specification = specification.and(buildSpecification(criteria.getManufacturer(), Tire_.manufacturer));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Tire_.price));
            }
            if (criteria.getCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCount(), Tire_.count));
            }
            if (criteria.getSalePointId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getSalePointId(), Tire_.salePoint, SalePoint_.id));
            }
        }
        return specification;
    }

}
