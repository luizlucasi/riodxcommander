package com.sgaraba.library.repository;

import com.sgaraba.library.domain.Modem;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Modem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModemRepository extends JpaRepository<Modem, Long> {
}
