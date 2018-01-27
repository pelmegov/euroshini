package ru.pelmegov.euroshini.repository.search;

import ru.pelmegov.euroshini.domain.SaleHistory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SaleHistory entity.
 */
public interface SaleHistorySearchRepository extends ElasticsearchRepository<SaleHistory, Long> {
}
