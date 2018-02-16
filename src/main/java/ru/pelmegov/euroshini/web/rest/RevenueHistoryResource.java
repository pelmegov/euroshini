package ru.pelmegov.euroshini.web.rest;

import com.codahale.metrics.annotation.Timed;
import ru.pelmegov.euroshini.domain.RevenueHistory;

import ru.pelmegov.euroshini.repository.RevenueHistoryRepository;
import ru.pelmegov.euroshini.repository.TireRepository;
import ru.pelmegov.euroshini.repository.search.RevenueHistorySearchRepository;
import ru.pelmegov.euroshini.web.rest.errors.BadRequestAlertException;
import ru.pelmegov.euroshini.web.rest.util.HeaderUtil;
import ru.pelmegov.euroshini.web.rest.util.PaginationUtil;
import ru.pelmegov.euroshini.service.dto.RevenueHistoryDTO;
import ru.pelmegov.euroshini.service.mapper.RevenueHistoryMapper;
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

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing RevenueHistory.
 */
@RestController
@RequestMapping("/api")
public class RevenueHistoryResource {

    private final Logger log = LoggerFactory.getLogger(RevenueHistoryResource.class);

    private static final String ENTITY_NAME = "revenueHistory";

    private final RevenueHistoryRepository revenueHistoryRepository;

    private final RevenueHistoryMapper revenueHistoryMapper;

    private final RevenueHistorySearchRepository revenueHistorySearchRepository;

    public RevenueHistoryResource(RevenueHistoryRepository revenueHistoryRepository, RevenueHistoryMapper revenueHistoryMapper, RevenueHistorySearchRepository revenueHistorySearchRepository) {
        this.revenueHistoryRepository = revenueHistoryRepository;
        this.revenueHistoryMapper = revenueHistoryMapper;
        this.revenueHistorySearchRepository = revenueHistorySearchRepository;
    }

    /**
     * POST  /revenue-histories : Create a new revenueHistory.
     *
     * @param revenueHistoryDTO the revenueHistoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new revenueHistoryDTO, or with status 400 (Bad Request) if the revenueHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/revenue-histories")
    @Timed
    public ResponseEntity<RevenueHistoryDTO> createRevenueHistory(@RequestBody RevenueHistoryDTO revenueHistoryDTO) throws URISyntaxException {
        log.debug("REST request to save RevenueHistory : {}", revenueHistoryDTO);
        if (revenueHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new revenueHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RevenueHistory revenueHistory = revenueHistoryMapper.toEntity(revenueHistoryDTO);
        revenueHistory = revenueHistoryRepository.save(revenueHistory);
        RevenueHistoryDTO result = revenueHistoryMapper.toDto(revenueHistory);
        revenueHistorySearchRepository.save(revenueHistory);
        return ResponseEntity.created(new URI("/api/revenue-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /revenue-histories : Updates an existing revenueHistory.
     *
     * @param revenueHistoryDTO the revenueHistoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated revenueHistoryDTO,
     * or with status 400 (Bad Request) if the revenueHistoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the revenueHistoryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/revenue-histories")
    @Timed
    public ResponseEntity<RevenueHistoryDTO> updateRevenueHistory(@RequestBody RevenueHistoryDTO revenueHistoryDTO) throws URISyntaxException {
        log.debug("REST request to update RevenueHistory : {}", revenueHistoryDTO);
        if (revenueHistoryDTO.getId() == null) {
            return createRevenueHistory(revenueHistoryDTO);
        }
        RevenueHistory revenueHistory = revenueHistoryMapper.toEntity(revenueHistoryDTO);
        revenueHistory = revenueHistoryRepository.save(revenueHistory);
        RevenueHistoryDTO result = revenueHistoryMapper.toDto(revenueHistory);
        revenueHistorySearchRepository.save(revenueHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, revenueHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /revenue-histories : get all the revenueHistories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of revenueHistories in body
     */
    @GetMapping("/revenue-histories")
    @Timed
    public ResponseEntity<List<RevenueHistoryDTO>> getAllRevenueHistories(Pageable pageable) {
        log.debug("REST request to get a page of RevenueHistories");
        Page<RevenueHistory> page = revenueHistoryRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/revenue-histories");
        return new ResponseEntity<>(revenueHistoryMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /revenue-histories/:id : get the "id" revenueHistory.
     *
     * @param id the id of the revenueHistoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the revenueHistoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/revenue-histories/{id}")
    @Timed
    public ResponseEntity<RevenueHistoryDTO> getRevenueHistory(@PathVariable Long id) {
        log.debug("REST request to get RevenueHistory : {}", id);
        RevenueHistory revenueHistory = revenueHistoryRepository.findOne(id);
        RevenueHistoryDTO revenueHistoryDTO = revenueHistoryMapper.toDto(revenueHistory);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(revenueHistoryDTO));
    }

    /**
     * DELETE  /revenue-histories/:id : delete the "id" revenueHistory.
     *
     * @param id the id of the revenueHistoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/revenue-histories/{id}")
    @Timed
    public ResponseEntity<Void> deleteRevenueHistory(@PathVariable Long id) {
        log.debug("REST request to delete RevenueHistory : {}", id);
        revenueHistoryRepository.delete(id);
        revenueHistorySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/revenue-histories?query=:query : search for the revenueHistory corresponding
     * to the query.
     *
     * @param query the query of the revenueHistory search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/revenue-histories")
    @Timed
    public ResponseEntity<List<RevenueHistoryDTO>> searchRevenueHistories(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of RevenueHistories for query {}", query);
        Page<RevenueHistory> page = revenueHistorySearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/revenue-histories");
        return new ResponseEntity<>(revenueHistoryMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

}
