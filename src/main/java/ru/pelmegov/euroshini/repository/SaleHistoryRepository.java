package ru.pelmegov.euroshini.repository;

import ru.pelmegov.euroshini.domain.SaleHistory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.Instant;
import java.util.List;

/**
 * Spring Data JPA repository for the SaleHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SaleHistoryRepository extends JpaRepository<SaleHistory, Long> {

    List<SaleHistory> findByCreatedDateAfterAndCreatedDateBefore(Instant after, Instant before);

}
