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

import com.sgaraba.library.domain.Band;
import com.sgaraba.library.domain.*; // for static metamodels
import com.sgaraba.library.repository.BandRepository;
import com.sgaraba.library.service.dto.BandCriteria;

/**
 * Service for executing complex queries for {@link Band} entities in the database.
 * The main input is a {@link BandCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Band} or a {@link Page} of {@link Band} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BandQueryService extends QueryService<Band> {

    private final Logger log = LoggerFactory.getLogger(BandQueryService.class);

    private final BandRepository bandRepository;

    public BandQueryService(BandRepository bandRepository) {
        this.bandRepository = bandRepository;
    }

    /**
     * Return a {@link List} of {@link Band} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Band> findByCriteria(BandCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Band> specification = createSpecification(criteria);
        return bandRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Band} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Band> findByCriteria(BandCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Band> specification = createSpecification(criteria);
        return bandRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BandCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Band> specification = createSpecification(criteria);
        return bandRepository.count(specification);
    }

    /**
     * Function to convert {@link BandCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Band> createSpecification(BandCriteria criteria) {
        Specification<Band> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Band_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Band_.name));
            }
            if (criteria.getBandMeter() != null) {
                specification = specification.and(buildSpecification(criteria.getBandMeter(), Band_.bandMeter));
            }
            if (criteria.getInUse() != null) {
                specification = specification.and(buildSpecification(criteria.getInUse(), Band_.inUse));
            }
            if (criteria.getCommandRunningBand() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCommandRunningBand(), Band_.commandRunningBand));
            }
            if (criteria.getCommandId() != null) {
                specification = specification.and(buildSpecification(criteria.getCommandId(),
                    root -> root.join(Band_.commands, JoinType.LEFT).get(Command_.id)));
            }
        }
        return specification;
    }
}
