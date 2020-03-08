package com.sgaraba.library.service;

import com.sgaraba.library.domain.Operator;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Operator}.
 */
public interface OperatorService {

    /**
     * Save a operator.
     *
     * @param operator the entity to save.
     * @return the persisted entity.
     */
    Operator save(Operator operator);

    /**
     * Get all the operators.
     *
     * @return the list of entities.
     */
    List<Operator> findAll();

    /**
     * Get the "id" operator.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Operator> findOne(Long id);

    /**
     * Delete the "id" operator.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
