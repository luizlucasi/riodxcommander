package com.sgaraba.library.service.impl;

import com.sgaraba.library.service.OperatorService;
import com.sgaraba.library.domain.Operator;
import com.sgaraba.library.repository.OperatorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Operator}.
 */
@Service
@Transactional
public class OperatorServiceImpl implements OperatorService {

    private final Logger log = LoggerFactory.getLogger(OperatorServiceImpl.class);

    private final OperatorRepository operatorRepository;

    public OperatorServiceImpl(OperatorRepository operatorRepository) {
        this.operatorRepository = operatorRepository;
    }

    /**
     * Save a operator.
     *
     * @param operator the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Operator save(Operator operator) {
        log.debug("Request to save Operator : {}", operator);
        return operatorRepository.save(operator);
    }

    /**
     * Get all the operators.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Operator> findAll() {
        log.debug("Request to get all Operators");
        return operatorRepository.findAll();
    }

    /**
     * Get one operator by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Operator> findOne(Long id) {
        log.debug("Request to get Operator : {}", id);
        return operatorRepository.findById(id);
    }

    /**
     * Delete the operator by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Operator : {}", id);
        operatorRepository.deleteById(id);
    }
}
