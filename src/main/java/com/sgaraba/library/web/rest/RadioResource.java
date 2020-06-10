package com.sgaraba.library.web.rest;

import com.sgaraba.library.domain.Radio;
import com.sgaraba.library.repository.RadioRepository;
import com.sgaraba.library.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.sgaraba.library.domain.Radio}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RadioResource {

    private final Logger log = LoggerFactory.getLogger(RadioResource.class);

    private static final String ENTITY_NAME = "radio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RadioRepository radioRepository;

    public RadioResource(RadioRepository radioRepository) {
        this.radioRepository = radioRepository;
    }

    /**
     * {@code POST  /radios} : Create a new radio.
     *
     * @param radio the radio to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new radio, or with status {@code 400 (Bad Request)} if the radio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/radios")
    public ResponseEntity<Radio> createRadio(@Valid @RequestBody Radio radio) throws URISyntaxException {
        log.debug("REST request to save Radio : {}", radio);
        if (radio.getId() != null) {
            throw new BadRequestAlertException("A new radio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Radio result = radioRepository.save(radio);
        return ResponseEntity.created(new URI("/api/radios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /radios} : Updates an existing radio.
     *
     * @param radio the radio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated radio,
     * or with status {@code 400 (Bad Request)} if the radio is not valid,
     * or with status {@code 500 (Internal Server Error)} if the radio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/radios")
    public ResponseEntity<Radio> updateRadio(@Valid @RequestBody Radio radio) throws URISyntaxException {
        log.debug("REST request to update Radio : {}", radio);
        if (radio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Radio result = radioRepository.save(radio);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, radio.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /radios} : get all the radios.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of radios in body.
     */
    @GetMapping("/radios")
    public List<Radio> getAllRadios() {
        log.debug("REST request to get all Radios");
        return radioRepository.findAll();
    }

    /**
     * {@code GET  /radios/:id} : get the "id" radio.
     *
     * @param id the id of the radio to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the radio, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/radios/{id}")
    public ResponseEntity<Radio> getRadio(@PathVariable Long id) {
        log.debug("REST request to get Radio : {}", id);
        Optional<Radio> radio = radioRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(radio);
    }

    /**
     * {@code DELETE  /radios/:id} : delete the "id" radio.
     *
     * @param id the id of the radio to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/radios/{id}")
    public ResponseEntity<Void> deleteRadio(@PathVariable Long id) {
        log.debug("REST request to delete Radio : {}", id);
        radioRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
