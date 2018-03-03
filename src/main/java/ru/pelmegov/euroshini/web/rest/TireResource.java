package ru.pelmegov.euroshini.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.elasticsearch.common.util.CollectionUtils;
import org.springframework.data.domain.PageImpl;
import ru.pelmegov.euroshini.domain.Tire;
import ru.pelmegov.euroshini.service.TireService;
import ru.pelmegov.euroshini.web.rest.errors.BadRequestAlertException;
import ru.pelmegov.euroshini.web.rest.util.HeaderUtil;
import ru.pelmegov.euroshini.web.rest.util.PaginationUtil;
import ru.pelmegov.euroshini.service.dto.TireDTO;
import ru.pelmegov.euroshini.service.dto.TireCriteria;
import ru.pelmegov.euroshini.service.TireQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Tire.
 */
@RestController
@RequestMapping("/api")
public class TireResource {

    private final Logger log = LoggerFactory.getLogger(TireResource.class);

    private static final String ENTITY_NAME = "tire";

    private final TireService tireService;

    private final TireQueryService tireQueryService;

    public TireResource(TireService tireService, TireQueryService tireQueryService) {
        this.tireService = tireService;
        this.tireQueryService = tireQueryService;
    }

    /**
     * POST  /tires : Create a new tire.
     *
     * @param tireDTO the tireDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tireDTO, or with status 400 (Bad Request) if the tire has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tires")
    @Timed
    public ResponseEntity<TireDTO> createTire(@RequestBody TireDTO tireDTO) throws URISyntaxException {
        log.debug("REST request to save Tire : {}", tireDTO);
        if (tireDTO.getId() != null) {
            throw new BadRequestAlertException("A new tire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TireDTO result = tireService.save(tireDTO);
        return ResponseEntity.created(new URI("/api/tires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tires : Updates an existing tire.
     *
     * @param tireDTO the tireDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tireDTO,
     * or with status 400 (Bad Request) if the tireDTO is not valid,
     * or with status 500 (Internal Server Error) if the tireDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tires")
    @Timed
    public ResponseEntity<TireDTO> updateTire(@RequestBody TireDTO tireDTO) throws URISyntaxException {
        log.debug("REST request to update Tire : {}", tireDTO);
        if (tireDTO.getId() == null) {
            return createTire(tireDTO);
        }
        TireDTO result = tireService.save(tireDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tireDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tires : get all the tires.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of tires in body
     */
    @GetMapping("/tires")
    @Timed
    public ResponseEntity<List<TireDTO>> getAllTires(TireCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Tires by criteria: {}", criteria);
        Page<TireDTO> page = tireQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tires");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tires/:id : get the "id" tire.
     *
     * @param id the id of the tireDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tireDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tires/{id}")
    @Timed
    public ResponseEntity<TireDTO> getTire(@PathVariable Long id) {
        log.debug("REST request to get Tire : {}", id);
        TireDTO tireDTO = tireService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tireDTO));
    }

    /**
     * DELETE  /tires/:id : delete the "id" tire.
     *
     * @param id the id of the tireDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tires/{id}")
    @Timed
    public ResponseEntity<Void> deleteTire(@PathVariable Long id) {
        log.debug("REST request to delete Tire : {}", id);
        tireService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/tires?query=:query : search for the tire corresponding
     * to the query.
     *
     * @param query the query of the tire search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/tires")
    @Timed
    public ResponseEntity<List<TireDTO>> searchTires(TireCriteria criteria, @RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Tires for query {}", query);
        List<TireDTO> elasticResult = tireService.search(query);
        List<TireDTO> tireQueryResult = tireQueryService.findByCriteria(criteria);
        tireQueryResult.retainAll(elasticResult);
        Page page = new PageImpl(tireQueryResult, pageable, tireQueryResult.size());
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/tires");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     +     * GET /tires/count : get tires count
     +     * @return tires count
     +     */
    @GetMapping("/tires/count")
    @Timed
    public ResponseEntity<Integer> getTireCount() {
        log.debug("REST request to get a tireCount");
        return new ResponseEntity<>(1, HttpStatus.OK);
    }

}
