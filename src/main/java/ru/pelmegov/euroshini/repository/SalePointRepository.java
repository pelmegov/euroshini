package ru.pelmegov.euroshini.repository;

import ru.pelmegov.euroshini.domain.SalePoint;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SalePoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalePointRepository extends JpaRepository<SalePoint, Long> {

}
