package com.sgaraba.library.web.rest;

import com.sgaraba.library.LibraryApp;
import com.sgaraba.library.domain.Band;
import com.sgaraba.library.domain.Command;
import com.sgaraba.library.repository.BandRepository;
import com.sgaraba.library.service.BandService;
import com.sgaraba.library.web.rest.errors.ExceptionTranslator;
import com.sgaraba.library.service.dto.BandCriteria;
import com.sgaraba.library.service.BandQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.sgaraba.library.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sgaraba.library.domain.enumeration.BandMeter;
/**
 * Integration tests for the {@link BandResource} REST controller.
 */
@SpringBootTest(classes = LibraryApp.class)
public class BandResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BandMeter DEFAULT_BAND_METER = BandMeter.BAND160;
    private static final BandMeter UPDATED_BAND_METER = BandMeter.BAND80;

    private static final Boolean DEFAULT_IN_USE = false;
    private static final Boolean UPDATED_IN_USE = true;

    private static final String DEFAULT_COMMAND_RUNNING_BAND = "AAAAAAAAAA";
    private static final String UPDATED_COMMAND_RUNNING_BAND = "BBBBBBBBBB";

    @Autowired
    private BandRepository bandRepository;

    @Autowired
    private BandService bandService;

    @Autowired
    private BandQueryService bandQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restBandMockMvc;

    private Band band;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BandResource bandResource = new BandResource(bandService, bandQueryService);
        this.restBandMockMvc = MockMvcBuilders.standaloneSetup(bandResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Band createEntity(EntityManager em) {
        Band band = new Band()
            .name(DEFAULT_NAME)
            .bandMeter(DEFAULT_BAND_METER)
            .inUse(DEFAULT_IN_USE)
            .commandRunningBand(DEFAULT_COMMAND_RUNNING_BAND);
        return band;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Band createUpdatedEntity(EntityManager em) {
        Band band = new Band()
            .name(UPDATED_NAME)
            .bandMeter(UPDATED_BAND_METER)
            .inUse(UPDATED_IN_USE)
            .commandRunningBand(UPDATED_COMMAND_RUNNING_BAND);
        return band;
    }

    @BeforeEach
    public void initTest() {
        band = createEntity(em);
    }

    @Test
    @Transactional
    public void createBand() throws Exception {
        int databaseSizeBeforeCreate = bandRepository.findAll().size();

        // Create the Band
        restBandMockMvc.perform(post("/api/bands")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(band)))
            .andExpect(status().isCreated());

        // Validate the Band in the database
        List<Band> bandList = bandRepository.findAll();
        assertThat(bandList).hasSize(databaseSizeBeforeCreate + 1);
        Band testBand = bandList.get(bandList.size() - 1);
        assertThat(testBand.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBand.getBandMeter()).isEqualTo(DEFAULT_BAND_METER);
        assertThat(testBand.isInUse()).isEqualTo(DEFAULT_IN_USE);
        assertThat(testBand.getCommandRunningBand()).isEqualTo(DEFAULT_COMMAND_RUNNING_BAND);
    }

    @Test
    @Transactional
    public void createBandWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bandRepository.findAll().size();

        // Create the Band with an existing ID
        band.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBandMockMvc.perform(post("/api/bands")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(band)))
            .andExpect(status().isBadRequest());

        // Validate the Band in the database
        List<Band> bandList = bandRepository.findAll();
        assertThat(bandList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBands() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        // Get all the bandList
        restBandMockMvc.perform(get("/api/bands?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(band.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].bandMeter").value(hasItem(DEFAULT_BAND_METER.toString())))
            .andExpect(jsonPath("$.[*].inUse").value(hasItem(DEFAULT_IN_USE.booleanValue())))
            .andExpect(jsonPath("$.[*].commandRunningBand").value(hasItem(DEFAULT_COMMAND_RUNNING_BAND)));
    }
    
    @Test
    @Transactional
    public void getBand() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        // Get the band
        restBandMockMvc.perform(get("/api/bands/{id}", band.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(band.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.bandMeter").value(DEFAULT_BAND_METER.toString()))
            .andExpect(jsonPath("$.inUse").value(DEFAULT_IN_USE.booleanValue()))
            .andExpect(jsonPath("$.commandRunningBand").value(DEFAULT_COMMAND_RUNNING_BAND));
    }


    @Test
    @Transactional
    public void getBandsByIdFiltering() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        Long id = band.getId();

        defaultBandShouldBeFound("id.equals=" + id);
        defaultBandShouldNotBeFound("id.notEquals=" + id);

        defaultBandShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBandShouldNotBeFound("id.greaterThan=" + id);

        defaultBandShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBandShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllBandsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        // Get all the bandList where name equals to DEFAULT_NAME
        defaultBandShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the bandList where name equals to UPDATED_NAME
        defaultBandShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllBandsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        // Get all the bandList where name not equals to DEFAULT_NAME
        defaultBandShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the bandList where name not equals to UPDATED_NAME
        defaultBandShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllBandsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        // Get all the bandList where name in DEFAULT_NAME or UPDATED_NAME
        defaultBandShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the bandList where name equals to UPDATED_NAME
        defaultBandShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllBandsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        // Get all the bandList where name is not null
        defaultBandShouldBeFound("name.specified=true");

        // Get all the bandList where name is null
        defaultBandShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllBandsByNameContainsSomething() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        // Get all the bandList where name contains DEFAULT_NAME
        defaultBandShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the bandList where name contains UPDATED_NAME
        defaultBandShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllBandsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        // Get all the bandList where name does not contain DEFAULT_NAME
        defaultBandShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the bandList where name does not contain UPDATED_NAME
        defaultBandShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllBandsByBandMeterIsEqualToSomething() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        // Get all the bandList where bandMeter equals to DEFAULT_BAND_METER
        defaultBandShouldBeFound("bandMeter.equals=" + DEFAULT_BAND_METER);

        // Get all the bandList where bandMeter equals to UPDATED_BAND_METER
        defaultBandShouldNotBeFound("bandMeter.equals=" + UPDATED_BAND_METER);
    }

    @Test
    @Transactional
    public void getAllBandsByBandMeterIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        // Get all the bandList where bandMeter not equals to DEFAULT_BAND_METER
        defaultBandShouldNotBeFound("bandMeter.notEquals=" + DEFAULT_BAND_METER);

        // Get all the bandList where bandMeter not equals to UPDATED_BAND_METER
        defaultBandShouldBeFound("bandMeter.notEquals=" + UPDATED_BAND_METER);
    }

    @Test
    @Transactional
    public void getAllBandsByBandMeterIsInShouldWork() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        // Get all the bandList where bandMeter in DEFAULT_BAND_METER or UPDATED_BAND_METER
        defaultBandShouldBeFound("bandMeter.in=" + DEFAULT_BAND_METER + "," + UPDATED_BAND_METER);

        // Get all the bandList where bandMeter equals to UPDATED_BAND_METER
        defaultBandShouldNotBeFound("bandMeter.in=" + UPDATED_BAND_METER);
    }

    @Test
    @Transactional
    public void getAllBandsByBandMeterIsNullOrNotNull() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        // Get all the bandList where bandMeter is not null
        defaultBandShouldBeFound("bandMeter.specified=true");

        // Get all the bandList where bandMeter is null
        defaultBandShouldNotBeFound("bandMeter.specified=false");
    }

    @Test
    @Transactional
    public void getAllBandsByInUseIsEqualToSomething() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        // Get all the bandList where inUse equals to DEFAULT_IN_USE
        defaultBandShouldBeFound("inUse.equals=" + DEFAULT_IN_USE);

        // Get all the bandList where inUse equals to UPDATED_IN_USE
        defaultBandShouldNotBeFound("inUse.equals=" + UPDATED_IN_USE);
    }

    @Test
    @Transactional
    public void getAllBandsByInUseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        // Get all the bandList where inUse not equals to DEFAULT_IN_USE
        defaultBandShouldNotBeFound("inUse.notEquals=" + DEFAULT_IN_USE);

        // Get all the bandList where inUse not equals to UPDATED_IN_USE
        defaultBandShouldBeFound("inUse.notEquals=" + UPDATED_IN_USE);
    }

    @Test
    @Transactional
    public void getAllBandsByInUseIsInShouldWork() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        // Get all the bandList where inUse in DEFAULT_IN_USE or UPDATED_IN_USE
        defaultBandShouldBeFound("inUse.in=" + DEFAULT_IN_USE + "," + UPDATED_IN_USE);

        // Get all the bandList where inUse equals to UPDATED_IN_USE
        defaultBandShouldNotBeFound("inUse.in=" + UPDATED_IN_USE);
    }

    @Test
    @Transactional
    public void getAllBandsByInUseIsNullOrNotNull() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        // Get all the bandList where inUse is not null
        defaultBandShouldBeFound("inUse.specified=true");

        // Get all the bandList where inUse is null
        defaultBandShouldNotBeFound("inUse.specified=false");
    }

    @Test
    @Transactional
    public void getAllBandsByCommandRunningBandIsEqualToSomething() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        // Get all the bandList where commandRunningBand equals to DEFAULT_COMMAND_RUNNING_BAND
        defaultBandShouldBeFound("commandRunningBand.equals=" + DEFAULT_COMMAND_RUNNING_BAND);

        // Get all the bandList where commandRunningBand equals to UPDATED_COMMAND_RUNNING_BAND
        defaultBandShouldNotBeFound("commandRunningBand.equals=" + UPDATED_COMMAND_RUNNING_BAND);
    }

    @Test
    @Transactional
    public void getAllBandsByCommandRunningBandIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        // Get all the bandList where commandRunningBand not equals to DEFAULT_COMMAND_RUNNING_BAND
        defaultBandShouldNotBeFound("commandRunningBand.notEquals=" + DEFAULT_COMMAND_RUNNING_BAND);

        // Get all the bandList where commandRunningBand not equals to UPDATED_COMMAND_RUNNING_BAND
        defaultBandShouldBeFound("commandRunningBand.notEquals=" + UPDATED_COMMAND_RUNNING_BAND);
    }

    @Test
    @Transactional
    public void getAllBandsByCommandRunningBandIsInShouldWork() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        // Get all the bandList where commandRunningBand in DEFAULT_COMMAND_RUNNING_BAND or UPDATED_COMMAND_RUNNING_BAND
        defaultBandShouldBeFound("commandRunningBand.in=" + DEFAULT_COMMAND_RUNNING_BAND + "," + UPDATED_COMMAND_RUNNING_BAND);

        // Get all the bandList where commandRunningBand equals to UPDATED_COMMAND_RUNNING_BAND
        defaultBandShouldNotBeFound("commandRunningBand.in=" + UPDATED_COMMAND_RUNNING_BAND);
    }

    @Test
    @Transactional
    public void getAllBandsByCommandRunningBandIsNullOrNotNull() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        // Get all the bandList where commandRunningBand is not null
        defaultBandShouldBeFound("commandRunningBand.specified=true");

        // Get all the bandList where commandRunningBand is null
        defaultBandShouldNotBeFound("commandRunningBand.specified=false");
    }
                @Test
    @Transactional
    public void getAllBandsByCommandRunningBandContainsSomething() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        // Get all the bandList where commandRunningBand contains DEFAULT_COMMAND_RUNNING_BAND
        defaultBandShouldBeFound("commandRunningBand.contains=" + DEFAULT_COMMAND_RUNNING_BAND);

        // Get all the bandList where commandRunningBand contains UPDATED_COMMAND_RUNNING_BAND
        defaultBandShouldNotBeFound("commandRunningBand.contains=" + UPDATED_COMMAND_RUNNING_BAND);
    }

    @Test
    @Transactional
    public void getAllBandsByCommandRunningBandNotContainsSomething() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        // Get all the bandList where commandRunningBand does not contain DEFAULT_COMMAND_RUNNING_BAND
        defaultBandShouldNotBeFound("commandRunningBand.doesNotContain=" + DEFAULT_COMMAND_RUNNING_BAND);

        // Get all the bandList where commandRunningBand does not contain UPDATED_COMMAND_RUNNING_BAND
        defaultBandShouldBeFound("commandRunningBand.doesNotContain=" + UPDATED_COMMAND_RUNNING_BAND);
    }


    @Test
    @Transactional
    public void getAllBandsByCommandIsEqualToSomething() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);
        Command command = CommandResourceIT.createEntity(em);
        em.persist(command);
        em.flush();
        band.addCommand(command);
        bandRepository.saveAndFlush(band);
        Long commandId = command.getId();

        // Get all the bandList where command equals to commandId
        defaultBandShouldBeFound("commandId.equals=" + commandId);

        // Get all the bandList where command equals to commandId + 1
        defaultBandShouldNotBeFound("commandId.equals=" + (commandId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBandShouldBeFound(String filter) throws Exception {
        restBandMockMvc.perform(get("/api/bands?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(band.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].bandMeter").value(hasItem(DEFAULT_BAND_METER.toString())))
            .andExpect(jsonPath("$.[*].inUse").value(hasItem(DEFAULT_IN_USE.booleanValue())))
            .andExpect(jsonPath("$.[*].commandRunningBand").value(hasItem(DEFAULT_COMMAND_RUNNING_BAND)));

        // Check, that the count call also returns 1
        restBandMockMvc.perform(get("/api/bands/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBandShouldNotBeFound(String filter) throws Exception {
        restBandMockMvc.perform(get("/api/bands?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBandMockMvc.perform(get("/api/bands/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingBand() throws Exception {
        // Get the band
        restBandMockMvc.perform(get("/api/bands/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBand() throws Exception {
        // Initialize the database
        bandService.save(band);

        int databaseSizeBeforeUpdate = bandRepository.findAll().size();

        // Update the band
        Band updatedBand = bandRepository.findById(band.getId()).get();
        // Disconnect from session so that the updates on updatedBand are not directly saved in db
        em.detach(updatedBand);
        updatedBand
            .name(UPDATED_NAME)
            .bandMeter(UPDATED_BAND_METER)
            .inUse(UPDATED_IN_USE)
            .commandRunningBand(UPDATED_COMMAND_RUNNING_BAND);

        restBandMockMvc.perform(put("/api/bands")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedBand)))
            .andExpect(status().isOk());

        // Validate the Band in the database
        List<Band> bandList = bandRepository.findAll();
        assertThat(bandList).hasSize(databaseSizeBeforeUpdate);
        Band testBand = bandList.get(bandList.size() - 1);
        assertThat(testBand.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBand.getBandMeter()).isEqualTo(UPDATED_BAND_METER);
        assertThat(testBand.isInUse()).isEqualTo(UPDATED_IN_USE);
        assertThat(testBand.getCommandRunningBand()).isEqualTo(UPDATED_COMMAND_RUNNING_BAND);
    }

    @Test
    @Transactional
    public void updateNonExistingBand() throws Exception {
        int databaseSizeBeforeUpdate = bandRepository.findAll().size();

        // Create the Band

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBandMockMvc.perform(put("/api/bands")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(band)))
            .andExpect(status().isBadRequest());

        // Validate the Band in the database
        List<Band> bandList = bandRepository.findAll();
        assertThat(bandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBand() throws Exception {
        // Initialize the database
        bandService.save(band);

        int databaseSizeBeforeDelete = bandRepository.findAll().size();

        // Delete the band
        restBandMockMvc.perform(delete("/api/bands/{id}", band.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Band> bandList = bandRepository.findAll();
        assertThat(bandList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
