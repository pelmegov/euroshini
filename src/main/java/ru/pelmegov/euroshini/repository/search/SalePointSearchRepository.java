package ru.pelmegov.euroshini.repository.search;

import ru.pelmegov.euroshini.domain.SalePoint;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SalePoint entity.
 */
public interface SalePointSearchRepository extends ElasticsearchRepository<SalePoint, Long> {
}
