package com.sgaraba.library.web.rest;

import com.sgaraba.library.LibraryApp;
import com.sgaraba.library.domain.Operator;
import com.sgaraba.library.repository.OperatorRepository;
import com.sgaraba.library.service.OperatorService;
import com.sgaraba.library.web.rest.errors.ExceptionTranslator;
import com.sgaraba.library.service.dto.OperatorCriteria;
import com.sgaraba.library.service.OperatorQueryService;

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

/**
 * Integration tests for the {@link OperatorResource} REST controller.
 */
@SpringBootTest(classes = LibraryApp.class)
public class OperatorResourceIT {

    private static final String DEFAULT_CALL = "AAAAAAAAAA";
    private static final String UPDATED_CALL = "BBBBBBBBBB";

    @Autowired
    private OperatorRepository operatorRepository;

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private OperatorQueryService operatorQueryService;

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

    private MockMvc restOperatorMockMvc;

    private Operator operator;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OperatorResource operatorResource = new OperatorResource(operatorService, operatorQueryService);
        this.restOperatorMockMvc = MockMvcBuilders.standaloneSetup(operatorResource)
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
    public static Operator createEntity(EntityManager em) {
        Operator operator = new Operator()
            .call(DEFAULT_CALL);
        return operator;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operator createUpdatedEntity(EntityManager em) {
        Operator operator = new Operator()
            .call(UPDATED_CALL);
        return operator;
    }

    @BeforeEach
    public void initTest() {
        operator = createEntity(em);
    }

    @Test
    @Transactional
    public void createOperator() throws Exception {
        int databaseSizeBeforeCreate = operatorRepository.findAll().size();

        // Create the Operator
        restOperatorMockMvc.perform(post("/api/operators")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(operator)))
            .andExpect(status().isCreated());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeCreate + 1);
        Operator testOperator = operatorList.get(operatorList.size() - 1);
        assertThat(testOperator.getCall()).isEqualTo(DEFAULT_CALL);
    }

    @Test
    @Transactional
    public void createOperatorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = operatorRepository.findAll().size();

        // Create the Operator with an existing ID
        operator.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOperatorMockMvc.perform(post("/api/operators")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(operator)))
            .andExpect(status().isBadRequest());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCallIsRequired() throws Exception {
        int databaseSizeBeforeTest = operatorRepository.findAll().size();
        // set the field null
        operator.setCall(null);

        // Create the Operator, which fails.

        restOperatorMockMvc.perform(post("/api/operators")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(operator)))
            .andExpect(status().isBadRequest());

        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOperators() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList
        restOperatorMockMvc.perform(get("/api/operators?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operator.getId().intValue())))
            .andExpect(jsonPath("$.[*].call").value(hasItem(DEFAULT_CALL)));
    }
    
    @Test
    @Transactional
    public void getOperator() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get the operator
        restOperatorMockMvc.perform(get("/api/operators/{id}", operator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(operator.getId().intValue()))
            .andExpect(jsonPath("$.call").value(DEFAULT_CALL));
    }


    @Test
    @Transactional
    public void getOperatorsByIdFiltering() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        Long id = operator.getId();

        defaultOperatorShouldBeFound("id.equals=" + id);
        defaultOperatorShouldNotBeFound("id.notEquals=" + id);

        defaultOperatorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOperatorShouldNotBeFound("id.greaterThan=" + id);

        defaultOperatorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOperatorShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllOperatorsByCallIsEqualToSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where call equals to DEFAULT_CALL
        defaultOperatorShouldBeFound("call.equals=" + DEFAULT_CALL);

        // Get all the operatorList where call equals to UPDATED_CALL
        defaultOperatorShouldNotBeFound("call.equals=" + UPDATED_CALL);
    }

    @Test
    @Transactional
    public void getAllOperatorsByCallIsNotEqualToSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where call not equals to DEFAULT_CALL
        defaultOperatorShouldNotBeFound("call.notEquals=" + DEFAULT_CALL);

        // Get all the operatorList where call not equals to UPDATED_CALL
        defaultOperatorShouldBeFound("call.notEquals=" + UPDATED_CALL);
    }

    @Test
    @Transactional
    public void getAllOperatorsByCallIsInShouldWork() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where call in DEFAULT_CALL or UPDATED_CALL
        defaultOperatorShouldBeFound("call.in=" + DEFAULT_CALL + "," + UPDATED_CALL);

        // Get all the operatorList where call equals to UPDATED_CALL
        defaultOperatorShouldNotBeFound("call.in=" + UPDATED_CALL);
    }

    @Test
    @Transactional
    public void getAllOperatorsByCallIsNullOrNotNull() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where call is not null
        defaultOperatorShouldBeFound("call.specified=true");

        // Get all the operatorList where call is null
        defaultOperatorShouldNotBeFound("call.specified=false");
    }
                @Test
    @Transactional
    public void getAllOperatorsByCallContainsSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where call contains DEFAULT_CALL
        defaultOperatorShouldBeFound("call.contains=" + DEFAULT_CALL);

        // Get all the operatorList where call contains UPDATED_CALL
        defaultOperatorShouldNotBeFound("call.contains=" + UPDATED_CALL);
    }

    @Test
    @Transactional
    public void getAllOperatorsByCallNotContainsSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where call does not contain DEFAULT_CALL
        defaultOperatorShouldNotBeFound("call.doesNotContain=" + DEFAULT_CALL);

        // Get all the operatorList where call does not contain UPDATED_CALL
        defaultOperatorShouldBeFound("call.doesNotContain=" + UPDATED_CALL);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOperatorShouldBeFound(String filter) throws Exception {
        restOperatorMockMvc.perform(get("/api/operators?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operator.getId().intValue())))
            .andExpect(jsonPath("$.[*].call").value(hasItem(DEFAULT_CALL)));

        // Check, that the count call also returns 1
        restOperatorMockMvc.perform(get("/api/operators/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOperatorShouldNotBeFound(String filter) throws Exception {
        restOperatorMockMvc.perform(get("/api/operators?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOperatorMockMvc.perform(get("/api/operators/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingOperator() throws Exception {
        // Get the operator
        restOperatorMockMvc.perform(get("/api/operators/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOperator() throws Exception {
        // Initialize the database
        operatorService.save(operator);

        int databaseSizeBeforeUpdate = operatorRepository.findAll().size();

        // Update the operator
        Operator updatedOperator = operatorRepository.findById(operator.getId()).get();
        // Disconnect from session so that the updates on updatedOperator are not directly saved in db
        em.detach(updatedOperator);
        updatedOperator
            .call(UPDATED_CALL);

        restOperatorMockMvc.perform(put("/api/operators")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedOperator)))
            .andExpect(status().isOk());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeUpdate);
        Operator testOperator = operatorList.get(operatorList.size() - 1);
        assertThat(testOperator.getCall()).isEqualTo(UPDATED_CALL);
    }

    @Test
    @Transactional
    public void updateNonExistingOperator() throws Exception {
        int databaseSizeBeforeUpdate = operatorRepository.findAll().size();

        // Create the Operator

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperatorMockMvc.perform(put("/api/operators")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(operator)))
            .andExpect(status().isBadRequest());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOperator() throws Exception {
        // Initialize the database
        operatorService.save(operator);

        int databaseSizeBeforeDelete = operatorRepository.findAll().size();

        // Delete the operator
        restOperatorMockMvc.perform(delete("/api/operators/{id}", operator.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
