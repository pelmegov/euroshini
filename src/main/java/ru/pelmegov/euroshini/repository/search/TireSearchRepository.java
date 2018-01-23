package ru.pelmegov.euroshini.repository.search;

import ru.pelmegov.euroshini.domain.Tire;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Tire entity.
 */
public interface TireSearchRepository extends ElasticsearchRepository<Tire, Long> {
}
