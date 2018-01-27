package ru.pelmegov.euroshini.web.rest;

import com.codahale.metrics.annotation.Timed;
import ru.pelmegov.euroshini.domain.SalePoint;

import ru.pelmegov.euroshini.repository.SalePointRepository;
import ru.pelmegov.euroshini.repository.search.SalePointSearchRepository;
import ru.pelmegov.euroshini.web.rest.errors.BadRequestAlertException;
import ru.pelmegov.euroshini.web.rest.util.HeaderUtil;
import ru.pelmegov.euroshini.service.dto.SalePointDTO;
import ru.pelmegov.euroshini.service.mapper.SalePointMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing SalePoint.
 */
@RestController
@RequestMapping("/api")
public class SalePointResource {

    private final Logger log = LoggerFactory.getLogger(SalePointResource.class);

    private static final String ENTITY_NAME = "salePoint";

    private final SalePointRepository salePointRepository;

    private final SalePointMapper salePointMapper;

    private final SalePointSearchRepository salePointSearchRepository;

    public SalePointResource(SalePointRepository salePointRepository, SalePointMapper salePointMapper, SalePointSearchRepository salePointSearchRepository) {
        this.salePointRepository = salePointRepository;
        this.salePointMapper = salePointMapper;
        this.salePointSearchRepository = salePointSearchRepository;
    }

    /**
     * POST  /sale-points : Create a new salePoint.
     *
     * @param salePointDTO the salePointDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new salePointDTO, or with status 400 (Bad Request) if the salePoint has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sale-points")
    @Timed
    public ResponseEntity<SalePointDTO> createSalePoint(@RequestBody SalePointDTO salePointDTO) throws URISyntaxException {
        log.debug("REST request to save SalePoint : {}", salePointDTO);
        if (salePointDTO.getId() != null) {
            throw new BadRequestAlertException("A new salePoint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SalePoint salePoint = salePointMapper.toEntity(salePointDTO);
        salePoint = salePointRepository.save(salePoint);
        SalePointDTO result = salePointMapper.toDto(salePoint);
        salePointSearchRepository.save(salePoint);
        return ResponseEntity.created(new URI("/api/sale-points/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sale-points : Updates an existing salePoint.
     *
     * @param salePointDTO the salePointDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated salePointDTO,
     * or with status 400 (Bad Request) if the salePointDTO is not valid,
     * or with status 500 (Internal Server Error) if the salePointDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sale-points")
    @Timed
    public ResponseEntity<SalePointDTO> updateSalePoint(@RequestBody SalePointDTO salePointDTO) throws URISyntaxException {
        log.debug("REST request to update SalePoint : {}", salePointDTO);
        if (salePointDTO.getId() == null) {
            return createSalePoint(salePointDTO);
        }
        SalePoint salePoint = salePointMapper.toEntity(salePointDTO);
        salePoint = salePointRepository.save(salePoint);
        SalePointDTO result = salePointMapper.toDto(salePoint);
        salePointSearchRepository.save(salePoint);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, salePointDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sale-points : get all the salePoints.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of salePoints in body
     */
    @GetMapping("/sale-points")
    @Timed
    public List<SalePointDTO> getAllSalePoints() {
        log.debug("REST request to get all SalePoints");
        List<SalePoint> salePoints = salePointRepository.findAll();
        return salePointMapper.toDto(salePoints);
        }

    /**
     * GET  /sale-points/:id : get the "id" salePoint.
     *
     * @param id the id of the salePointDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the salePointDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sale-points/{id}")
    @Timed
    public ResponseEntity<SalePointDTO> getSalePoint(@PathVariable Long id) {
        log.debug("REST request to get SalePoint : {}", id);
        SalePoint salePoint = salePointRepository.findOne(id);
        SalePointDTO salePointDTO = salePointMapper.toDto(salePoint);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(salePointDTO));
    }

    /**
     * DELETE  /sale-points/:id : delete the "id" salePoint.
     *
     * @param id the id of the salePointDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sale-points/{id}")
    @Timed
    public ResponseEntity<Void> deleteSalePoint(@PathVariable Long id) {
        log.debug("REST request to delete SalePoint : {}", id);
        salePointRepository.delete(id);
        salePointSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/sale-points?query=:query : search for the salePoint corresponding
     * to the query.
     *
     * @param query the query of the salePoint search
     * @return the result of the search
     */
    @GetMapping("/_search/sale-points")
    @Timed
    public List<SalePointDTO> searchSalePoints(@RequestParam String query) {
        log.debug("REST request to search SalePoints for query {}", query);
        return StreamSupport
            .stream(salePointSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(salePointMapper::toDto)
            .collect(Collectors.toList());
    }

}
