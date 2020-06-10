package com.sgaraba.library.web.rest;

import com.sgaraba.library.LibraryApp;
import com.sgaraba.library.domain.Radio;
import com.sgaraba.library.repository.RadioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RadioResource} REST controller.
 */
@SpringBootTest(classes = LibraryApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RadioResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private RadioRepository radioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRadioMockMvc;

    private Radio radio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Radio createEntity(EntityManager em) {
        Radio radio = new Radio()
            .description(DEFAULT_DESCRIPTION);
        return radio;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Radio createUpdatedEntity(EntityManager em) {
        Radio radio = new Radio()
            .description(UPDATED_DESCRIPTION);
        return radio;
    }

    @BeforeEach
    public void initTest() {
        radio = createEntity(em);
    }

    @Test
    @Transactional
    public void createRadio() throws Exception {
        int databaseSizeBeforeCreate = radioRepository.findAll().size();
        // Create the Radio
        restRadioMockMvc.perform(post("/api/radios")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(radio)))
            .andExpect(status().isCreated());

        // Validate the Radio in the database
        List<Radio> radioList = radioRepository.findAll();
        assertThat(radioList).hasSize(databaseSizeBeforeCreate + 1);
        Radio testRadio = radioList.get(radioList.size() - 1);
        assertThat(testRadio.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createRadioWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = radioRepository.findAll().size();

        // Create the Radio with an existing ID
        radio.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRadioMockMvc.perform(post("/api/radios")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(radio)))
            .andExpect(status().isBadRequest());

        // Validate the Radio in the database
        List<Radio> radioList = radioRepository.findAll();
        assertThat(radioList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = radioRepository.findAll().size();
        // set the field null
        radio.setDescription(null);

        // Create the Radio, which fails.


        restRadioMockMvc.perform(post("/api/radios")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(radio)))
            .andExpect(status().isBadRequest());

        List<Radio> radioList = radioRepository.findAll();
        assertThat(radioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRadios() throws Exception {
        // Initialize the database
        radioRepository.saveAndFlush(radio);

        // Get all the radioList
        restRadioMockMvc.perform(get("/api/radios?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(radio.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getRadio() throws Exception {
        // Initialize the database
        radioRepository.saveAndFlush(radio);

        // Get the radio
        restRadioMockMvc.perform(get("/api/radios/{id}", radio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(radio.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }
    @Test
    @Transactional
    public void getNonExistingRadio() throws Exception {
        // Get the radio
        restRadioMockMvc.perform(get("/api/radios/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRadio() throws Exception {
        // Initialize the database
        radioRepository.saveAndFlush(radio);

        int databaseSizeBeforeUpdate = radioRepository.findAll().size();

        // Update the radio
        Radio updatedRadio = radioRepository.findById(radio.getId()).get();
        // Disconnect from session so that the updates on updatedRadio are not directly saved in db
        em.detach(updatedRadio);
        updatedRadio
            .description(UPDATED_DESCRIPTION);

        restRadioMockMvc.perform(put("/api/radios")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRadio)))
            .andExpect(status().isOk());

        // Validate the Radio in the database
        List<Radio> radioList = radioRepository.findAll();
        assertThat(radioList).hasSize(databaseSizeBeforeUpdate);
        Radio testRadio = radioList.get(radioList.size() - 1);
        assertThat(testRadio.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingRadio() throws Exception {
        int databaseSizeBeforeUpdate = radioRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRadioMockMvc.perform(put("/api/radios")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(radio)))
            .andExpect(status().isBadRequest());

        // Validate the Radio in the database
        List<Radio> radioList = radioRepository.findAll();
        assertThat(radioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRadio() throws Exception {
        // Initialize the database
        radioRepository.saveAndFlush(radio);

        int databaseSizeBeforeDelete = radioRepository.findAll().size();

        // Delete the radio
        restRadioMockMvc.perform(delete("/api/radios/{id}", radio.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Radio> radioList = radioRepository.findAll();
        assertThat(radioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
