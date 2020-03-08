package com.sgaraba.library.web.rest;

import com.sgaraba.library.domain.Operator;
import com.sgaraba.library.service.OperatorService;
import com.sgaraba.library.web.rest.errors.BadRequestAlertException;
import com.sgaraba.library.service.dto.OperatorCriteria;
import com.sgaraba.library.service.OperatorQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.sgaraba.library.domain.Operator}.
 */
@RestController
@RequestMapping("/api")
public class OperatorResource {

    private final Logger log = LoggerFactory.getLogger(OperatorResource.class);

    private static final String ENTITY_NAME = "operator";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OperatorService operatorService;

    private final OperatorQueryService operatorQueryService;

    public OperatorResource(OperatorService operatorService, OperatorQueryService operatorQueryService) {
        this.operatorService = operatorService;
        this.operatorQueryService = operatorQueryService;
    }

    /**
     * {@code POST  /operators} : Create a new operator.
     *
     * @param operator the operator to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new operator, or with status {@code 400 (Bad Request)} if the operator has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/operators")
    public ResponseEntity<Operator> createOperator(@Valid @RequestBody Operator operator) throws URISyntaxException {
        log.debug("REST request to save Operator : {}", operator);
        if (operator.getId() != null) {
            throw new BadRequestAlertException("A new operator cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Operator result = operatorService.save(operator);
        return ResponseEntity.created(new URI("/api/operators/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /operators} : Updates an existing operator.
     *
     * @param operator the operator to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated operator,
     * or with status {@code 400 (Bad Request)} if the operator is not valid,
     * or with status {@code 500 (Internal Server Error)} if the operator couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/operators")
    public ResponseEntity<Operator> updateOperator(@Valid @RequestBody Operator operator) throws URISyntaxException {
        log.debug("REST request to update Operator : {}", operator);
        if (operator.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Operator result = operatorService.save(operator);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, operator.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /operators} : get all the operators.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of operators in body.
     */
    @GetMapping("/operators")
    public ResponseEntity<List<Operator>> getAllOperators(OperatorCriteria criteria) {
        log.debug("REST request to get Operators by criteria: {}", criteria);
        List<Operator> entityList = operatorQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /operators/count} : count all the operators.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/operators/count")
    public ResponseEntity<Long> countOperators(OperatorCriteria criteria) {
        log.debug("REST request to count Operators by criteria: {}", criteria);
        return ResponseEntity.ok().body(operatorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /operators/:id} : get the "id" operator.
     *
     * @param id the id of the operator to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the operator, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/operators/{id}")
    public ResponseEntity<Operator> getOperator(@PathVariable Long id) {
        log.debug("REST request to get Operator : {}", id);
        Optional<Operator> operator = operatorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(operator);
    }

    /**
     * {@code DELETE  /operators/:id} : delete the "id" operator.
     *
     * @param id the id of the operator to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/operators/{id}")
    public ResponseEntity<Void> deleteOperator(@PathVariable Long id) {
        log.debug("REST request to delete Operator : {}", id);
        operatorService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
