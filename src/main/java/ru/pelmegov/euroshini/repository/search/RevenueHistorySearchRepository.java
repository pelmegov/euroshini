package ru.pelmegov.euroshini.repository.search;

import ru.pelmegov.euroshini.domain.RevenueHistory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RevenueHistory entity.
 */
public interface RevenueHistorySearchRepository extends ElasticsearchRepository<RevenueHistory, Long> {
}
