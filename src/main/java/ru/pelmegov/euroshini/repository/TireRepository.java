package ru.pelmegov.euroshini.repository;

import ru.pelmegov.euroshini.domain.Tire;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Tire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TireRepository extends JpaRepository<Tire, Long>, JpaSpecificationExecutor<Tire> {
    @Query("SELECT SUM(tire.count) from Tire tire")
    int getTireCount();
}
