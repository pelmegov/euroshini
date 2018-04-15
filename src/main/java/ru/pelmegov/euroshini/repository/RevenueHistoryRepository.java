package ru.pelmegov.euroshini.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pelmegov.euroshini.domain.RevenueHistory;

import java.time.Instant;
import java.util.List;

/**
 * Spring Data JPA repository for the RevenueHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RevenueHistoryRepository extends JpaRepository<RevenueHistory, Long> {

    List<RevenueHistory> findByCreatedDateAfterAndCreatedDateBefore(Instant after, Instant before);

}
