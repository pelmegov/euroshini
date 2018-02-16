package ru.pelmegov.euroshini.repository;

import ru.pelmegov.euroshini.domain.RevenueHistory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the RevenueHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RevenueHistoryRepository extends JpaRepository<RevenueHistory, Long> {

}
