package com.sgaraba.library.web.rest;

import com.sgaraba.library.LibraryApp;
import com.sgaraba.library.domain.Command;
import com.sgaraba.library.domain.Operator;
import com.sgaraba.library.domain.Band;
import com.sgaraba.library.repository.CommandRepository;
import com.sgaraba.library.service.CommandService;
import com.sgaraba.library.web.rest.errors.ExceptionTranslator;
import com.sgaraba.library.service.dto.CommandCriteria;
import com.sgaraba.library.service.CommandQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.sgaraba.library.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CommandResource} REST controller.
 */
@SpringBootTest(classes = LibraryApp.class)
public class CommandResourceIT {

    private static final String DEFAULT_CALL = "AAAAAAAAAA";
    private static final String UPDATED_CALL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IN_USE = false;
    private static final Boolean UPDATED_IN_USE = true;

    @Autowired
    private CommandRepository commandRepository;

    @Mock
    private CommandRepository commandRepositoryMock;

    @Mock
    private CommandService commandServiceMock;

    @Autowired
    private CommandService commandService;

    @Autowired
    private CommandQueryService commandQueryService;

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

    private MockMvc restCommandMockMvc;

    private Command command;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CommandResource commandResource = new CommandResource(commandService, commandQueryService);
        this.restCommandMockMvc = MockMvcBuilders.standaloneSetup(commandResource)
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
    public static Command createEntity(EntityManager em) {
        Command command = new Command()
            .call(DEFAULT_CALL)
            .inUse(DEFAULT_IN_USE);
        return command;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Command createUpdatedEntity(EntityManager em) {
        Command command = new Command()
            .call(UPDATED_CALL)
            .inUse(UPDATED_IN_USE);
        return command;
    }

    @BeforeEach
    public void initTest() {
        command = createEntity(em);
    }

    @Test
    @Transactional
    public void createCommand() throws Exception {
        int databaseSizeBeforeCreate = commandRepository.findAll().size();

        // Create the Command
        restCommandMockMvc.perform(post("/api/commands")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(command)))
            .andExpect(status().isCreated());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeCreate + 1);
        Command testCommand = commandList.get(commandList.size() - 1);
        assertThat(testCommand.getCall()).isEqualTo(DEFAULT_CALL);
        assertThat(testCommand.isInUse()).isEqualTo(DEFAULT_IN_USE);
    }

    @Test
    @Transactional
    public void createCommandWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = commandRepository.findAll().size();

        // Create the Command with an existing ID
        command.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommandMockMvc.perform(post("/api/commands")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(command)))
            .andExpect(status().isBadRequest());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCallIsRequired() throws Exception {
        int databaseSizeBeforeTest = commandRepository.findAll().size();
        // set the field null
        command.setCall(null);

        // Create the Command, which fails.

        restCommandMockMvc.perform(post("/api/commands")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(command)))
            .andExpect(status().isBadRequest());

        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCommands() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList
        restCommandMockMvc.perform(get("/api/commands?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(command.getId().intValue())))
            .andExpect(jsonPath("$.[*].call").value(hasItem(DEFAULT_CALL)))
            .andExpect(jsonPath("$.[*].inUse").value(hasItem(DEFAULT_IN_USE.booleanValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllCommandsWithEagerRelationshipsIsEnabled() throws Exception {
        CommandResource commandResource = new CommandResource(commandServiceMock, commandQueryService);
        when(commandServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restCommandMockMvc = MockMvcBuilders.standaloneSetup(commandResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restCommandMockMvc.perform(get("/api/commands?eagerload=true"))
        .andExpect(status().isOk());

        verify(commandServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllCommandsWithEagerRelationshipsIsNotEnabled() throws Exception {
        CommandResource commandResource = new CommandResource(commandServiceMock, commandQueryService);
            when(commandServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restCommandMockMvc = MockMvcBuilders.standaloneSetup(commandResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restCommandMockMvc.perform(get("/api/commands?eagerload=true"))
        .andExpect(status().isOk());

            verify(commandServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getCommand() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get the command
        restCommandMockMvc.perform(get("/api/commands/{id}", command.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(command.getId().intValue()))
            .andExpect(jsonPath("$.call").value(DEFAULT_CALL))
            .andExpect(jsonPath("$.inUse").value(DEFAULT_IN_USE.booleanValue()));
    }


    @Test
    @Transactional
    public void getCommandsByIdFiltering() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        Long id = command.getId();

        defaultCommandShouldBeFound("id.equals=" + id);
        defaultCommandShouldNotBeFound("id.notEquals=" + id);

        defaultCommandShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCommandShouldNotBeFound("id.greaterThan=" + id);

        defaultCommandShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCommandShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCommandsByCallIsEqualToSomething() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList where call equals to DEFAULT_CALL
        defaultCommandShouldBeFound("call.equals=" + DEFAULT_CALL);

        // Get all the commandList where call equals to UPDATED_CALL
        defaultCommandShouldNotBeFound("call.equals=" + UPDATED_CALL);
    }

    @Test
    @Transactional
    public void getAllCommandsByCallIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList where call not equals to DEFAULT_CALL
        defaultCommandShouldNotBeFound("call.notEquals=" + DEFAULT_CALL);

        // Get all the commandList where call not equals to UPDATED_CALL
        defaultCommandShouldBeFound("call.notEquals=" + UPDATED_CALL);
    }

    @Test
    @Transactional
    public void getAllCommandsByCallIsInShouldWork() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList where call in DEFAULT_CALL or UPDATED_CALL
        defaultCommandShouldBeFound("call.in=" + DEFAULT_CALL + "," + UPDATED_CALL);

        // Get all the commandList where call equals to UPDATED_CALL
        defaultCommandShouldNotBeFound("call.in=" + UPDATED_CALL);
    }

    @Test
    @Transactional
    public void getAllCommandsByCallIsNullOrNotNull() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList where call is not null
        defaultCommandShouldBeFound("call.specified=true");

        // Get all the commandList where call is null
        defaultCommandShouldNotBeFound("call.specified=false");
    }
                @Test
    @Transactional
    public void getAllCommandsByCallContainsSomething() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList where call contains DEFAULT_CALL
        defaultCommandShouldBeFound("call.contains=" + DEFAULT_CALL);

        // Get all the commandList where call contains UPDATED_CALL
        defaultCommandShouldNotBeFound("call.contains=" + UPDATED_CALL);
    }

    @Test
    @Transactional
    public void getAllCommandsByCallNotContainsSomething() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList where call does not contain DEFAULT_CALL
        defaultCommandShouldNotBeFound("call.doesNotContain=" + DEFAULT_CALL);

        // Get all the commandList where call does not contain UPDATED_CALL
        defaultCommandShouldBeFound("call.doesNotContain=" + UPDATED_CALL);
    }


    @Test
    @Transactional
    public void getAllCommandsByInUseIsEqualToSomething() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList where inUse equals to DEFAULT_IN_USE
        defaultCommandShouldBeFound("inUse.equals=" + DEFAULT_IN_USE);

        // Get all the commandList where inUse equals to UPDATED_IN_USE
        defaultCommandShouldNotBeFound("inUse.equals=" + UPDATED_IN_USE);
    }

    @Test
    @Transactional
    public void getAllCommandsByInUseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList where inUse not equals to DEFAULT_IN_USE
        defaultCommandShouldNotBeFound("inUse.notEquals=" + DEFAULT_IN_USE);

        // Get all the commandList where inUse not equals to UPDATED_IN_USE
        defaultCommandShouldBeFound("inUse.notEquals=" + UPDATED_IN_USE);
    }

    @Test
    @Transactional
    public void getAllCommandsByInUseIsInShouldWork() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList where inUse in DEFAULT_IN_USE or UPDATED_IN_USE
        defaultCommandShouldBeFound("inUse.in=" + DEFAULT_IN_USE + "," + UPDATED_IN_USE);

        // Get all the commandList where inUse equals to UPDATED_IN_USE
        defaultCommandShouldNotBeFound("inUse.in=" + UPDATED_IN_USE);
    }

    @Test
    @Transactional
    public void getAllCommandsByInUseIsNullOrNotNull() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList where inUse is not null
        defaultCommandShouldBeFound("inUse.specified=true");

        // Get all the commandList where inUse is null
        defaultCommandShouldNotBeFound("inUse.specified=false");
    }

    @Test
    @Transactional
    public void getAllCommandsByOperatorIsEqualToSomething() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);
        Operator operator = OperatorResourceIT.createEntity(em);
        em.persist(operator);
        em.flush();
        command.setOperator(operator);
        commandRepository.saveAndFlush(command);
        Long operatorId = operator.getId();

        // Get all the commandList where operator equals to operatorId
        defaultCommandShouldBeFound("operatorId.equals=" + operatorId);

        // Get all the commandList where operator equals to operatorId + 1
        defaultCommandShouldNotBeFound("operatorId.equals=" + (operatorId + 1));
    }


    @Test
    @Transactional
    public void getAllCommandsByBandIsEqualToSomething() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);
        Band band = BandResourceIT.createEntity(em);
        em.persist(band);
        em.flush();
        command.addBand(band);
        commandRepository.saveAndFlush(command);
        Long bandId = band.getId();

        // Get all the commandList where band equals to bandId
        defaultCommandShouldBeFound("bandId.equals=" + bandId);

        // Get all the commandList where band equals to bandId + 1
        defaultCommandShouldNotBeFound("bandId.equals=" + (bandId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCommandShouldBeFound(String filter) throws Exception {
        restCommandMockMvc.perform(get("/api/commands?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(command.getId().intValue())))
            .andExpect(jsonPath("$.[*].call").value(hasItem(DEFAULT_CALL)))
            .andExpect(jsonPath("$.[*].inUse").value(hasItem(DEFAULT_IN_USE.booleanValue())));

        // Check, that the count call also returns 1
        restCommandMockMvc.perform(get("/api/commands/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCommandShouldNotBeFound(String filter) throws Exception {
        restCommandMockMvc.perform(get("/api/commands?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCommandMockMvc.perform(get("/api/commands/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCommand() throws Exception {
        // Get the command
        restCommandMockMvc.perform(get("/api/commands/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommand() throws Exception {
        // Initialize the database
        commandService.save(command);

        int databaseSizeBeforeUpdate = commandRepository.findAll().size();

        // Update the command
        Command updatedCommand = commandRepository.findById(command.getId()).get();
        // Disconnect from session so that the updates on updatedCommand are not directly saved in db
        em.detach(updatedCommand);
        updatedCommand
            .call(UPDATED_CALL)
            .inUse(UPDATED_IN_USE);

        restCommandMockMvc.perform(put("/api/commands")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCommand)))
            .andExpect(status().isOk());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate);
        Command testCommand = commandList.get(commandList.size() - 1);
        assertThat(testCommand.getCall()).isEqualTo(UPDATED_CALL);
        assertThat(testCommand.isInUse()).isEqualTo(UPDATED_IN_USE);
    }

    @Test
    @Transactional
    public void updateNonExistingCommand() throws Exception {
        int databaseSizeBeforeUpdate = commandRepository.findAll().size();

        // Create the Command

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommandMockMvc.perform(put("/api/commands")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(command)))
            .andExpect(status().isBadRequest());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCommand() throws Exception {
        // Initialize the database
        commandService.save(command);

        int databaseSizeBeforeDelete = commandRepository.findAll().size();

        // Delete the command
        restCommandMockMvc.perform(delete("/api/commands/{id}", command.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
