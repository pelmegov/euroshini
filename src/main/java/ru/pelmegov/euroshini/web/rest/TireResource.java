package ru.pelmegov.euroshini.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Joiner;
import com.google.common.collect.Streams;
import com.google.common.primitives.Doubles;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pelmegov.euroshini.domain.Tire;
import ru.pelmegov.euroshini.repository.TireRepository;
import ru.pelmegov.euroshini.repository.search.TireSearchRepository;
import ru.pelmegov.euroshini.service.dto.TireDTO;
import ru.pelmegov.euroshini.service.mapper.TireMapper;
import ru.pelmegov.euroshini.web.rest.errors.BadRequestAlertException;
import ru.pelmegov.euroshini.web.rest.util.HeaderUtil;
import ru.pelmegov.euroshini.web.rest.util.PaginationUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

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

    private final TireMapper tireMapper;

    private final TireSearchRepository tireSearchRepository;

    public TireResource(TireRepository tireRepository, TireMapper tireMapper, TireSearchRepository tireSearchRepository) {
        this.tireRepository = tireRepository;
        this.tireMapper = tireMapper;
        this.tireSearchRepository = tireSearchRepository;
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
        Tire tire = tireMapper.toEntity(tireDTO);
        tire = tireRepository.saveAndFlush(tire);
        TireDTO result = tireMapper.toDto(tire);
        tireSearchRepository.save(tire);
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
        Tire tire = tireMapper.toEntity(tireDTO);
        tire = tireRepository.save(tire);
        TireDTO result = tireMapper.toDto(tire);
        tireSearchRepository.save(tire);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tireDTO.getId().toString()))
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
    public ResponseEntity<List<TireDTO>> getAllTires(Pageable pageable) {
        log.debug("REST request to get a page of Tires");
        Page<Tire> page = tireRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tires");
        return new ResponseEntity<>(tireMapper.toDto(page.getContent()), headers, HttpStatus.OK);
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
        Tire tire = tireRepository.findOne(id);
        TireDTO tireDTO = tireMapper.toDto(tire);
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
    public ResponseEntity<List<TireDTO>> searchTires(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Tires for query {}", query);
        Set<String> subQueries = new HashSet<>(Arrays.asList(query.split(" ")));

        Set<Tire> sizeSearch = sizeSearch(subQueries, pageable);
        Set<Tire> radiusSearch = radiusSearch(subQueries, pageable);

        Page<Tire> page = tireSearchRepository.search(queryStringQuery(Joiner.on(" ").join(subQueries)).escape(true), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/tires");

        HashSet<Tire> result = new HashSet<>(page.getContent());
        result.addAll(radiusSearch);
        result.addAll(sizeSearch);

        return new ResponseEntity<>(tireMapper.toDto(new ArrayList<>(result)), headers, HttpStatus.OK);
    }

    private Set<Tire> radiusSearch(Set<String> subQueries, Pageable pageable) {
        Set<Tire> result = new HashSet<>();
        subQueries.stream()
            .filter(subQuery -> Doubles.tryParse(subQuery) != null)
            .forEach(subQuery -> result.addAll(tireSearchRepository.search(matchQuery("radius", subQuery), pageable).getContent()));
        return result;
    }

    private Set<Tire> sizeSearch(Set<String> subQueries, Pageable pageable) {
        Set<Tire> result = new HashSet<>();

        Iterator<String> iterator = subQueries.iterator();
        Streams.stream(iterator)
            .filter(subQuery -> subQuery.matches(".*[/].*"))
            .forEach(subQuery -> {
                result.addAll(tireSearchRepository.search(matchPhraseQuery("size", subQuery), pageable).getContent());
                iterator.remove();
            });
        return result;
    }

}
