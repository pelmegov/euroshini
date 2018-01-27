package ru.pelmegov.euroshini.web.rest;

import com.codahale.metrics.annotation.Timed;
import ru.pelmegov.euroshini.domain.SaleHistory;

import ru.pelmegov.euroshini.repository.SaleHistoryRepository;
import ru.pelmegov.euroshini.repository.search.SaleHistorySearchRepository;
import ru.pelmegov.euroshini.web.rest.errors.BadRequestAlertException;
import ru.pelmegov.euroshini.web.rest.util.HeaderUtil;
import ru.pelmegov.euroshini.web.rest.util.PaginationUtil;
import ru.pelmegov.euroshini.service.dto.SaleHistoryDTO;
import ru.pelmegov.euroshini.service.mapper.SaleHistoryMapper;
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
 * REST controller for managing SaleHistory.
 */
@RestController
@RequestMapping("/api")
public class SaleHistoryResource {

    private final Logger log = LoggerFactory.getLogger(SaleHistoryResource.class);

    private static final String ENTITY_NAME = "saleHistory";

    private final SaleHistoryRepository saleHistoryRepository;

    private final SaleHistoryMapper saleHistoryMapper;

    private final SaleHistorySearchRepository saleHistorySearchRepository;

    public SaleHistoryResource(SaleHistoryRepository saleHistoryRepository, SaleHistoryMapper saleHistoryMapper, SaleHistorySearchRepository saleHistorySearchRepository) {
        this.saleHistoryRepository = saleHistoryRepository;
        this.saleHistoryMapper = saleHistoryMapper;
        this.saleHistorySearchRepository = saleHistorySearchRepository;
    }

    /**
     * POST  /sale-histories : Create a new saleHistory.
     *
     * @param saleHistoryDTO the saleHistoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new saleHistoryDTO, or with status 400 (Bad Request) if the saleHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sale-histories")
    @Timed
    public ResponseEntity<SaleHistoryDTO> createSaleHistory(@RequestBody SaleHistoryDTO saleHistoryDTO) throws URISyntaxException {
        log.debug("REST request to save SaleHistory : {}", saleHistoryDTO);
        if (saleHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new saleHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SaleHistory saleHistory = saleHistoryMapper.toEntity(saleHistoryDTO);
        saleHistory = saleHistoryRepository.save(saleHistory);
        SaleHistoryDTO result = saleHistoryMapper.toDto(saleHistory);
        saleHistorySearchRepository.save(saleHistory);
        return ResponseEntity.created(new URI("/api/sale-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sale-histories : Updates an existing saleHistory.
     *
     * @param saleHistoryDTO the saleHistoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated saleHistoryDTO,
     * or with status 400 (Bad Request) if the saleHistoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the saleHistoryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sale-histories")
    @Timed
    public ResponseEntity<SaleHistoryDTO> updateSaleHistory(@RequestBody SaleHistoryDTO saleHistoryDTO) throws URISyntaxException {
        log.debug("REST request to update SaleHistory : {}", saleHistoryDTO);
        if (saleHistoryDTO.getId() == null) {
            return createSaleHistory(saleHistoryDTO);
        }
        SaleHistory saleHistory = saleHistoryMapper.toEntity(saleHistoryDTO);
        saleHistory = saleHistoryRepository.save(saleHistory);
        SaleHistoryDTO result = saleHistoryMapper.toDto(saleHistory);
        saleHistorySearchRepository.save(saleHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, saleHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sale-histories : get all the saleHistories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of saleHistories in body
     */
    @GetMapping("/sale-histories")
    @Timed
    public ResponseEntity<List<SaleHistoryDTO>> getAllSaleHistories(Pageable pageable) {
        log.debug("REST request to get a page of SaleHistories");
        Page<SaleHistory> page = saleHistoryRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sale-histories");
        return new ResponseEntity<>(saleHistoryMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /sale-histories/:id : get the "id" saleHistory.
     *
     * @param id the id of the saleHistoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the saleHistoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sale-histories/{id}")
    @Timed
    public ResponseEntity<SaleHistoryDTO> getSaleHistory(@PathVariable Long id) {
        log.debug("REST request to get SaleHistory : {}", id);
        SaleHistory saleHistory = saleHistoryRepository.findOne(id);
        SaleHistoryDTO saleHistoryDTO = saleHistoryMapper.toDto(saleHistory);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(saleHistoryDTO));
    }

    /**
     * DELETE  /sale-histories/:id : delete the "id" saleHistory.
     *
     * @param id the id of the saleHistoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sale-histories/{id}")
    @Timed
    public ResponseEntity<Void> deleteSaleHistory(@PathVariable Long id) {
        log.debug("REST request to delete SaleHistory : {}", id);
        saleHistoryRepository.delete(id);
        saleHistorySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/sale-histories?query=:query : search for the saleHistory corresponding
     * to the query.
     *
     * @param query the query of the saleHistory search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/sale-histories")
    @Timed
    public ResponseEntity<List<SaleHistoryDTO>> searchSaleHistories(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SaleHistories for query {}", query);
        Page<SaleHistory> page = saleHistorySearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/sale-histories");
        return new ResponseEntity<>(saleHistoryMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

}
