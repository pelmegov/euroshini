package ru.pelmegov.euroshini.web.rest;

import com.codahale.metrics.annotation.Timed;
import ru.pelmegov.euroshini.domain.SalePoint;

import ru.pelmegov.euroshini.repository.SalePointRepository;
import ru.pelmegov.euroshini.repository.search.SalePointSearchRepository;
import ru.pelmegov.euroshini.web.rest.errors.BadRequestAlertException;
import ru.pelmegov.euroshini.web.rest.util.HeaderUtil;
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

    private final SalePointSearchRepository salePointSearchRepository;

    public SalePointResource(SalePointRepository salePointRepository, SalePointSearchRepository salePointSearchRepository) {
        this.salePointRepository = salePointRepository;
        this.salePointSearchRepository = salePointSearchRepository;
    }

    /**
     * POST  /sale-points : Create a new salePoint.
     *
     * @param salePoint the salePoint to create
     * @return the ResponseEntity with status 201 (Created) and with body the new salePoint, or with status 400 (Bad Request) if the salePoint has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sale-points")
    @Timed
    public ResponseEntity<SalePoint> createSalePoint(@RequestBody SalePoint salePoint) throws URISyntaxException {
        log.debug("REST request to save SalePoint : {}", salePoint);
        if (salePoint.getId() != null) {
            throw new BadRequestAlertException("A new salePoint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SalePoint result = salePointRepository.save(salePoint);
        salePointSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/sale-points/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sale-points : Updates an existing salePoint.
     *
     * @param salePoint the salePoint to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated salePoint,
     * or with status 400 (Bad Request) if the salePoint is not valid,
     * or with status 500 (Internal Server Error) if the salePoint couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sale-points")
    @Timed
    public ResponseEntity<SalePoint> updateSalePoint(@RequestBody SalePoint salePoint) throws URISyntaxException {
        log.debug("REST request to update SalePoint : {}", salePoint);
        if (salePoint.getId() == null) {
            return createSalePoint(salePoint);
        }
        SalePoint result = salePointRepository.save(salePoint);
        salePointSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, salePoint.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sale-points : get all the salePoints.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of salePoints in body
     */
    @GetMapping("/sale-points")
    @Timed
    public List<SalePoint> getAllSalePoints() {
        log.debug("REST request to get all SalePoints");
        return salePointRepository.findAll();
        }

    /**
     * GET  /sale-points/:id : get the "id" salePoint.
     *
     * @param id the id of the salePoint to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the salePoint, or with status 404 (Not Found)
     */
    @GetMapping("/sale-points/{id}")
    @Timed
    public ResponseEntity<SalePoint> getSalePoint(@PathVariable Long id) {
        log.debug("REST request to get SalePoint : {}", id);
        SalePoint salePoint = salePointRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(salePoint));
    }

    /**
     * DELETE  /sale-points/:id : delete the "id" salePoint.
     *
     * @param id the id of the salePoint to delete
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
    public List<SalePoint> searchSalePoints(@RequestParam String query) {
        log.debug("REST request to search SalePoints for query {}", query);
        return StreamSupport
            .stream(salePointSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
