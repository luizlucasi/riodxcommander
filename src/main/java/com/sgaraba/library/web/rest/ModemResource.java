package com.sgaraba.library.web.rest;

import com.sgaraba.library.domain.Modem;
import com.sgaraba.library.repository.ModemRepository;
import com.sgaraba.library.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.sgaraba.library.domain.Modem}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ModemResource {

    private final Logger log = LoggerFactory.getLogger(ModemResource.class);

    private static final String ENTITY_NAME = "modem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ModemRepository modemRepository;

    public ModemResource(ModemRepository modemRepository) {
        this.modemRepository = modemRepository;
    }

    /**
     * {@code POST  /modems} : Create a new modem.
     *
     * @param modem the modem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new modem, or with status {@code 400 (Bad Request)} if the modem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/modems")
    public ResponseEntity<Modem> createModem(@RequestBody Modem modem) throws URISyntaxException {
        log.debug("REST request to save Modem : {}", modem);
        if (modem.getId() != null) {
            throw new BadRequestAlertException("A new modem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Modem result = modemRepository.save(modem);
        return ResponseEntity.created(new URI("/api/modems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /modems} : Updates an existing modem.
     *
     * @param modem the modem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated modem,
     * or with status {@code 400 (Bad Request)} if the modem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the modem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/modems")
    public ResponseEntity<Modem> updateModem(@RequestBody Modem modem) throws URISyntaxException {
        log.debug("REST request to update Modem : {}", modem);
        if (modem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Modem result = modemRepository.save(modem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, modem.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /modems} : get all the modems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of modems in body.
     */
    @GetMapping("/modems")
    public List<Modem> getAllModems() {
        log.debug("REST request to get all Modems");
        return modemRepository.findAll();
    }

    /**
     * {@code GET  /modems/:id} : get the "id" modem.
     *
     * @param id the id of the modem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the modem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/modems/{id}")
    public ResponseEntity<Modem> getModem(@PathVariable Long id) {
        log.debug("REST request to get Modem : {}", id);
        Optional<Modem> modem = modemRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(modem);
    }

    /**
     * {@code DELETE  /modems/:id} : delete the "id" modem.
     *
     * @param id the id of the modem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/modems/{id}")
    public ResponseEntity<Void> deleteModem(@PathVariable Long id) {
        log.debug("REST request to delete Modem : {}", id);
        modemRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
