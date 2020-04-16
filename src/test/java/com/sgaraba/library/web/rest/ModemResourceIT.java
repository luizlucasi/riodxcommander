package com.sgaraba.library.web.rest;

import com.sgaraba.library.LibraryApp;
import com.sgaraba.library.domain.Modem;
import com.sgaraba.library.repository.ModemRepository;

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
 * Integration tests for the {@link ModemResource} REST controller.
 */
@SpringBootTest(classes = LibraryApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class ModemResourceIT {

    private static final Long DEFAULT_CRM_ESTACAO_ID = 1L;
    private static final Long UPDATED_CRM_ESTACAO_ID = 2L;

    private static final Integer DEFAULT_VSAT_ID = 1;
    private static final Integer UPDATED_VSAT_ID = 2;

    private static final Integer DEFAULT_VSAT_UID = 1;
    private static final Integer UPDATED_VSAT_UID = 2;

    private static final String DEFAULT_VSAT_GROUP = "AAAAAAAAAA";
    private static final String UPDATED_VSAT_GROUP = "BBBBBBBBBB";

    private static final Integer DEFAULT_HUB = 1;
    private static final Integer UPDATED_HUB = 2;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_IP_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_IP_ADDRESS = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final String DEFAULT_STATUS_DESC = "AAAAAAAAAA";
    private static final String UPDATED_STATUS_DESC = "BBBBBBBBBB";

    private static final String DEFAULT_OBJECT_OID = "AAAAAAAAAA";
    private static final String UPDATED_OBJECT_OID = "BBBBBBBBBB";

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

    private static final String DEFAULT_LAN_IP_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_LAN_IP_ADDRESS = "BBBBBBBBBB";

    @Autowired
    private ModemRepository modemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restModemMockMvc;

    private Modem modem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Modem createEntity(EntityManager em) {
        Modem modem = new Modem()
            .crmEstacaoId(DEFAULT_CRM_ESTACAO_ID)
            .vsatId(DEFAULT_VSAT_ID)
            .vsatUid(DEFAULT_VSAT_UID)
            .vsatGroup(DEFAULT_VSAT_GROUP)
            .hub(DEFAULT_HUB)
            .name(DEFAULT_NAME)
            .ipAddress(DEFAULT_IP_ADDRESS)
            .status(DEFAULT_STATUS)
            .statusDesc(DEFAULT_STATUS_DESC)
            .objectOID(DEFAULT_OBJECT_OID)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .lanIpAddress(DEFAULT_LAN_IP_ADDRESS);
        return modem;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Modem createUpdatedEntity(EntityManager em) {
        Modem modem = new Modem()
            .crmEstacaoId(UPDATED_CRM_ESTACAO_ID)
            .vsatId(UPDATED_VSAT_ID)
            .vsatUid(UPDATED_VSAT_UID)
            .vsatGroup(UPDATED_VSAT_GROUP)
            .hub(UPDATED_HUB)
            .name(UPDATED_NAME)
            .ipAddress(UPDATED_IP_ADDRESS)
            .status(UPDATED_STATUS)
            .statusDesc(UPDATED_STATUS_DESC)
            .objectOID(UPDATED_OBJECT_OID)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .lanIpAddress(UPDATED_LAN_IP_ADDRESS);
        return modem;
    }

    @BeforeEach
    public void initTest() {
        modem = createEntity(em);
    }

    @Test
    @Transactional
    public void createModem() throws Exception {
        int databaseSizeBeforeCreate = modemRepository.findAll().size();

        // Create the Modem
        restModemMockMvc.perform(post("/api/modems")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(modem)))
            .andExpect(status().isCreated());

        // Validate the Modem in the database
        List<Modem> modemList = modemRepository.findAll();
        assertThat(modemList).hasSize(databaseSizeBeforeCreate + 1);
        Modem testModem = modemList.get(modemList.size() - 1);
        assertThat(testModem.getCrmEstacaoId()).isEqualTo(DEFAULT_CRM_ESTACAO_ID);
        assertThat(testModem.getVsatId()).isEqualTo(DEFAULT_VSAT_ID);
        assertThat(testModem.getVsatUid()).isEqualTo(DEFAULT_VSAT_UID);
        assertThat(testModem.getVsatGroup()).isEqualTo(DEFAULT_VSAT_GROUP);
        assertThat(testModem.getHub()).isEqualTo(DEFAULT_HUB);
        assertThat(testModem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testModem.getIpAddress()).isEqualTo(DEFAULT_IP_ADDRESS);
        assertThat(testModem.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testModem.getStatusDesc()).isEqualTo(DEFAULT_STATUS_DESC);
        assertThat(testModem.getObjectOID()).isEqualTo(DEFAULT_OBJECT_OID);
        assertThat(testModem.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testModem.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testModem.getLanIpAddress()).isEqualTo(DEFAULT_LAN_IP_ADDRESS);
    }

    @Test
    @Transactional
    public void createModemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = modemRepository.findAll().size();

        // Create the Modem with an existing ID
        modem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restModemMockMvc.perform(post("/api/modems")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(modem)))
            .andExpect(status().isBadRequest());

        // Validate the Modem in the database
        List<Modem> modemList = modemRepository.findAll();
        assertThat(modemList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllModems() throws Exception {
        // Initialize the database
        modemRepository.saveAndFlush(modem);

        // Get all the modemList
        restModemMockMvc.perform(get("/api/modems?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(modem.getId().intValue())))
            .andExpect(jsonPath("$.[*].crmEstacaoId").value(hasItem(DEFAULT_CRM_ESTACAO_ID.intValue())))
            .andExpect(jsonPath("$.[*].vsatId").value(hasItem(DEFAULT_VSAT_ID)))
            .andExpect(jsonPath("$.[*].vsatUid").value(hasItem(DEFAULT_VSAT_UID)))
            .andExpect(jsonPath("$.[*].vsatGroup").value(hasItem(DEFAULT_VSAT_GROUP)))
            .andExpect(jsonPath("$.[*].hub").value(hasItem(DEFAULT_HUB)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].ipAddress").value(hasItem(DEFAULT_IP_ADDRESS)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].statusDesc").value(hasItem(DEFAULT_STATUS_DESC)))
            .andExpect(jsonPath("$.[*].objectOID").value(hasItem(DEFAULT_OBJECT_OID)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].lanIpAddress").value(hasItem(DEFAULT_LAN_IP_ADDRESS)));
    }
    
    @Test
    @Transactional
    public void getModem() throws Exception {
        // Initialize the database
        modemRepository.saveAndFlush(modem);

        // Get the modem
        restModemMockMvc.perform(get("/api/modems/{id}", modem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(modem.getId().intValue()))
            .andExpect(jsonPath("$.crmEstacaoId").value(DEFAULT_CRM_ESTACAO_ID.intValue()))
            .andExpect(jsonPath("$.vsatId").value(DEFAULT_VSAT_ID))
            .andExpect(jsonPath("$.vsatUid").value(DEFAULT_VSAT_UID))
            .andExpect(jsonPath("$.vsatGroup").value(DEFAULT_VSAT_GROUP))
            .andExpect(jsonPath("$.hub").value(DEFAULT_HUB))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.ipAddress").value(DEFAULT_IP_ADDRESS))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.statusDesc").value(DEFAULT_STATUS_DESC))
            .andExpect(jsonPath("$.objectOID").value(DEFAULT_OBJECT_OID))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.lanIpAddress").value(DEFAULT_LAN_IP_ADDRESS));
    }

    @Test
    @Transactional
    public void getNonExistingModem() throws Exception {
        // Get the modem
        restModemMockMvc.perform(get("/api/modems/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateModem() throws Exception {
        // Initialize the database
        modemRepository.saveAndFlush(modem);

        int databaseSizeBeforeUpdate = modemRepository.findAll().size();

        // Update the modem
        Modem updatedModem = modemRepository.findById(modem.getId()).get();
        // Disconnect from session so that the updates on updatedModem are not directly saved in db
        em.detach(updatedModem);
        updatedModem
            .crmEstacaoId(UPDATED_CRM_ESTACAO_ID)
            .vsatId(UPDATED_VSAT_ID)
            .vsatUid(UPDATED_VSAT_UID)
            .vsatGroup(UPDATED_VSAT_GROUP)
            .hub(UPDATED_HUB)
            .name(UPDATED_NAME)
            .ipAddress(UPDATED_IP_ADDRESS)
            .status(UPDATED_STATUS)
            .statusDesc(UPDATED_STATUS_DESC)
            .objectOID(UPDATED_OBJECT_OID)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .lanIpAddress(UPDATED_LAN_IP_ADDRESS);

        restModemMockMvc.perform(put("/api/modems")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedModem)))
            .andExpect(status().isOk());

        // Validate the Modem in the database
        List<Modem> modemList = modemRepository.findAll();
        assertThat(modemList).hasSize(databaseSizeBeforeUpdate);
        Modem testModem = modemList.get(modemList.size() - 1);
        assertThat(testModem.getCrmEstacaoId()).isEqualTo(UPDATED_CRM_ESTACAO_ID);
        assertThat(testModem.getVsatId()).isEqualTo(UPDATED_VSAT_ID);
        assertThat(testModem.getVsatUid()).isEqualTo(UPDATED_VSAT_UID);
        assertThat(testModem.getVsatGroup()).isEqualTo(UPDATED_VSAT_GROUP);
        assertThat(testModem.getHub()).isEqualTo(UPDATED_HUB);
        assertThat(testModem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testModem.getIpAddress()).isEqualTo(UPDATED_IP_ADDRESS);
        assertThat(testModem.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testModem.getStatusDesc()).isEqualTo(UPDATED_STATUS_DESC);
        assertThat(testModem.getObjectOID()).isEqualTo(UPDATED_OBJECT_OID);
        assertThat(testModem.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testModem.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testModem.getLanIpAddress()).isEqualTo(UPDATED_LAN_IP_ADDRESS);
    }

    @Test
    @Transactional
    public void updateNonExistingModem() throws Exception {
        int databaseSizeBeforeUpdate = modemRepository.findAll().size();

        // Create the Modem

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModemMockMvc.perform(put("/api/modems")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(modem)))
            .andExpect(status().isBadRequest());

        // Validate the Modem in the database
        List<Modem> modemList = modemRepository.findAll();
        assertThat(modemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteModem() throws Exception {
        // Initialize the database
        modemRepository.saveAndFlush(modem);

        int databaseSizeBeforeDelete = modemRepository.findAll().size();

        // Delete the modem
        restModemMockMvc.perform(delete("/api/modems/{id}", modem.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Modem> modemList = modemRepository.findAll();
        assertThat(modemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
