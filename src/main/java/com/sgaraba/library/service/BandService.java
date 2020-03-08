package com.sgaraba.library.service;

import com.sgaraba.library.domain.Band;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Band}.
 */
public interface BandService {

    /**
     * Save a band.
     *
     * @param band the entity to save.
     * @return the persisted entity.
     */
    Band save(Band band);

    /**
     * Get all the bands.
     *
     * @return the list of entities.
     */
    List<Band> findAll();

    /**
     * Get the "id" band.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Band> findOne(Long id);

    /**
     * Delete the "id" band.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
