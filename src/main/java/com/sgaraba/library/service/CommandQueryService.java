package com.sgaraba.library.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.sgaraba.library.domain.Command;
import com.sgaraba.library.domain.*; // for static metamodels
import com.sgaraba.library.repository.CommandRepository;
import com.sgaraba.library.service.dto.CommandCriteria;

/**
 * Service for executing complex queries for {@link Command} entities in the database.
 * The main input is a {@link CommandCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Command} or a {@link Page} of {@link Command} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CommandQueryService extends QueryService<Command> {

    private final Logger log = LoggerFactory.getLogger(CommandQueryService.class);

    private final CommandRepository commandRepository;

    public CommandQueryService(CommandRepository commandRepository) {
        this.commandRepository = commandRepository;
    }

    /**
     * Return a {@link List} of {@link Command} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Command> findByCriteria(CommandCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Command> specification = createSpecification(criteria);
        return commandRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Command} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Command> findByCriteria(CommandCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Command> specification = createSpecification(criteria);
        return commandRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CommandCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Command> specification = createSpecification(criteria);
        return commandRepository.count(specification);
    }

    /**
     * Function to convert {@link CommandCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Command> createSpecification(CommandCriteria criteria) {
        Specification<Command> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Command_.id));
            }
            if (criteria.getCall() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCall(), Command_.call));
            }
            if (criteria.getInUse() != null) {
                specification = specification.and(buildSpecification(criteria.getInUse(), Command_.inUse));
            }
            if (criteria.getOperatorId() != null) {
                specification = specification.and(buildSpecification(criteria.getOperatorId(),
                    root -> root.join(Command_.operator, JoinType.LEFT).get(Operator_.id)));
            }
            if (criteria.getBandId() != null) {
                specification = specification.and(buildSpecification(criteria.getBandId(),
                    root -> root.join(Command_.bands, JoinType.LEFT).get(Band_.id)));
            }
        }
        return specification;
    }
}
