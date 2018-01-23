package ru.pelmegov.euroshini.web.rest;

import com.codahale.metrics.annotation.Timed;
import ru.pelmegov.euroshini.domain.Tire;

import ru.pelmegov.euroshini.repository.TireRepository;
import ru.pelmegov.euroshini.repository.search.TireSearchRepository;
import ru.pelmegov.euroshini.web.rest.errors.BadRequestAlertException;
import ru.pelmegov.euroshini.web.rest.util.HeaderUtil;
import ru.pelmegov.euroshini.web.rest.util.PaginationUtil;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

    private final TireRepository tireRepository;

    private final TireSearchRepository tireSearchRepository;

    public TireResource(TireRepository tireRepository, TireSearchRepository tireSearchRepository) {
        this.tireRepository = tireRepository;
        this.tireSearchRepository = tireSearchRepository;
    }

    /**
     * POST  /tires : Create a new tire.
     *
     * @param tire the tire to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tire, or with status 400 (Bad Request) if the tire has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tires")
    @Timed
    public ResponseEntity<Tire> createTire(@RequestBody Tire tire) throws URISyntaxException {
        log.debug("REST request to save Tire : {}", tire);
        if (tire.getId() != null) {
            throw new BadRequestAlertException("A new tire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tire result = tireRepository.save(tire);
        tireSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/tires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tires : Updates an existing tire.
     *
     * @param tire the tire to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tire,
     * or with status 400 (Bad Request) if the tire is not valid,
     * or with status 500 (Internal Server Error) if the tire couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tires")
    @Timed
    public ResponseEntity<Tire> updateTire(@RequestBody Tire tire) throws URISyntaxException {
        log.debug("REST request to update Tire : {}", tire);
        if (tire.getId() == null) {
            return createTire(tire);
        }
        Tire result = tireRepository.save(tire);
        tireSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tire.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tires : get all the tires.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tires in body
     */
    @GetMapping("/tires")
    @Timed
    public ResponseEntity<List<Tire>> getAllTires(Pageable pageable) {
        log.debug("REST request to get a page of Tires");
        Page<Tire> page = tireRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tires");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tires/:id : get the "id" tire.
     *
     * @param id the id of the tire to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tire, or with status 404 (Not Found)
     */
    @GetMapping("/tires/{id}")
    @Timed
    public ResponseEntity<Tire> getTire(@PathVariable Long id) {
        log.debug("REST request to get Tire : {}", id);
        Tire tire = tireRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tire));
    }

    /**
     * DELETE  /tires/:id : delete the "id" tire.
     *
     * @param id the id of the tire to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tires/{id}")
    @Timed
    public ResponseEntity<Void> deleteTire(@PathVariable Long id) {
        log.debug("REST request to delete Tire : {}", id);
        tireRepository.delete(id);
        tireSearchRepository.delete(id);
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
    public ResponseEntity<List<Tire>> searchTires(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Tires for query {}", query);
        Page<Tire> page = tireSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/tires");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
