package ru.pelmegov.euroshini.service;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import ru.pelmegov.euroshini.domain.Tire;
import ru.pelmegov.euroshini.repository.TireRepository;
import ru.pelmegov.euroshini.repository.search.TireSearchRepository;
import ru.pelmegov.euroshini.service.dto.TireDTO;
import ru.pelmegov.euroshini.service.mapper.TireMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Tire.
 */
@Service
@Transactional
public class TireService {

    private final Logger log = LoggerFactory.getLogger(TireService.class);

    private final TireRepository tireRepository;

    private final TireMapper tireMapper;

    private final TireSearchRepository tireSearchRepository;

    public TireService(TireRepository tireRepository, TireMapper tireMapper, TireSearchRepository tireSearchRepository) {
        this.tireRepository = tireRepository;
        this.tireMapper = tireMapper;
        this.tireSearchRepository = tireSearchRepository;
    }

    /**
     * Save a tire.
     *
     * @param tireDTO the entity to save
     * @return the persisted entity
     */
    public TireDTO save(TireDTO tireDTO) {
        log.debug("Request to save Tire : {}", tireDTO);
        Tire tire = tireMapper.toEntity(tireDTO);
        tire = tireRepository.save(tire);
        TireDTO result = tireMapper.toDto(tire);
        tireSearchRepository.save(tire);
        return result;
    }

    /**
     * Get all the tires.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TireDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tires");
        return tireRepository.findAll(pageable)
            .map(tireMapper::toDto);
    }

    /**
     * Get one tire by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public TireDTO findOne(Long id) {
        log.debug("Request to get Tire : {}", id);
        Tire tire = tireRepository.findOne(id);
        return tireMapper.toDto(tire);
    }

    /**
     * Delete the tire by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Tire : {}", id);
        tireRepository.delete(id);
        tireSearchRepository.delete(id);
    }

    /**
     * Search for the tire corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<TireDTO> search(String query) {
        log.debug("Request to search for a page of Tires for query {}", query);
        query = normalizeQuery(query);
        List<Tire> tires = Lists.newArrayList(tireSearchRepository.search(queryStringQuery(query)).iterator());
        return tireMapper.toDto(tires);
    }

    private String normalizeQuery(String query) {
        StringBuilder resultQuery = new StringBuilder();
        final String[] words = query.split(" ");
        for (String word : words) {
            String currentWord = word;
            if (StringUtils.isNumeric(word)) {
                currentWord = "(" + word + " OR " + new Double(word) + ")";
            }
            if (word.contains("/")) {
                currentWord = word.replace("/", "\\/");
            }
            resultQuery.append(currentWord).append(" ");
        }
        return resultQuery.toString();
    }
}
