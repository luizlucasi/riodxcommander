package com.sgaraba.library.repository;

import com.sgaraba.library.domain.Band;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Band entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BandRepository extends JpaRepository<Band, Long>, JpaSpecificationExecutor<Band> {

}
