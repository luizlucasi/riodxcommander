package com.sgaraba.library.web.rest;

import com.sgaraba.library.domain.Band;
import com.sgaraba.library.service.BandService;
import com.sgaraba.library.web.rest.errors.BadRequestAlertException;
import com.sgaraba.library.service.dto.BandCriteria;
import com.sgaraba.library.service.BandQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.sgaraba.library.domain.Band}.
 */
@RestController
@RequestMapping("/api")
public class BandResource {

    private final Logger log = LoggerFactory.getLogger(BandResource.class);

    private static final String ENTITY_NAME = "band";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BandService bandService;

    private final BandQueryService bandQueryService;

    public BandResource(BandService bandService, BandQueryService bandQueryService) {
        this.bandService = bandService;
        this.bandQueryService = bandQueryService;
    }

    /**
     * {@code POST  /bands} : Create a new band.
     *
     * @param band the band to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new band, or with status {@code 400 (Bad Request)} if the band has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bands")
    public ResponseEntity<Band> createBand(@RequestBody Band band) throws URISyntaxException {
        log.debug("REST request to save Band : {}", band);
        if (band.getId() != null) {
            throw new BadRequestAlertException("A new band cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Band result = bandService.save(band);
        return ResponseEntity.created(new URI("/api/bands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bands} : Updates an existing band.
     *
     * @param band the band to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated band,
     * or with status {@code 400 (Bad Request)} if the band is not valid,
     * or with status {@code 500 (Internal Server Error)} if the band couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bands")
    public ResponseEntity<Band> updateBand(@RequestBody Band band) throws URISyntaxException {
        log.debug("REST request to update Band : {}", band);
        if (band.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Band result = bandService.save(band);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, band.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /bands} : get all the bands.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bands in body.
     */
    @GetMapping("/bands")
    public ResponseEntity<List<Band>> getAllBands(BandCriteria criteria) {
        log.debug("REST request to get Bands by criteria: {}", criteria);
        List<Band> entityList = bandQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /bands/count} : count all the bands.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/bands/count")
    public ResponseEntity<Long> countBands(BandCriteria criteria) {
        log.debug("REST request to count Bands by criteria: {}", criteria);
        return ResponseEntity.ok().body(bandQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /bands/:id} : get the "id" band.
     *
     * @param id the id of the band to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the band, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bands/{id}")
    public ResponseEntity<Band> getBand(@PathVariable Long id) {
        log.debug("REST request to get Band : {}", id);
        Optional<Band> band = bandService.findOne(id);
        return ResponseUtil.wrapOrNotFound(band);
    }

    /**
     * {@code DELETE  /bands/:id} : delete the "id" band.
     *
     * @param id the id of the band to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bands/{id}")
    public ResponseEntity<Void> deleteBand(@PathVariable Long id) {
        log.debug("REST request to delete Band : {}", id);
        bandService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
