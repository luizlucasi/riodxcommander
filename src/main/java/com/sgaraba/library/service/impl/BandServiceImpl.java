package com.sgaraba.library.service.impl;

import com.sgaraba.library.service.BandService;
import com.sgaraba.library.domain.Band;
import com.sgaraba.library.repository.BandRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Band}.
 */
@Service
@Transactional
public class BandServiceImpl implements BandService {

    private final Logger log = LoggerFactory.getLogger(BandServiceImpl.class);

    private final BandRepository bandRepository;

    public BandServiceImpl(BandRepository bandRepository) {
        this.bandRepository = bandRepository;
    }

    /**
     * Save a band.
     *
     * @param band the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Band save(Band band) {
        log.debug("Request to save Band : {}", band);
        return bandRepository.save(band);
    }

    /**
     * Get all the bands.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Band> findAll() {
        log.debug("Request to get all Bands");
        return bandRepository.findAll();
    }

    /**
     * Get one band by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Band> findOne(Long id) {
        log.debug("Request to get Band : {}", id);
        return bandRepository.findById(id);
    }

    /**
     * Delete the band by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Band : {}", id);
        bandRepository.deleteById(id);
    }
}
