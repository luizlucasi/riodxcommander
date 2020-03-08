package com.sgaraba.library.web.rest;

import com.sgaraba.library.domain.Command;
import com.sgaraba.library.service.CommandService;
import com.sgaraba.library.web.rest.errors.BadRequestAlertException;
import com.sgaraba.library.service.dto.CommandCriteria;
import com.sgaraba.library.service.CommandQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.sgaraba.library.domain.Command}.
 */
@RestController
@RequestMapping("/api")
public class CommandResource {

    private final Logger log = LoggerFactory.getLogger(CommandResource.class);

    private static final String ENTITY_NAME = "command";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommandService commandService;

    private final CommandQueryService commandQueryService;

    public CommandResource(CommandService commandService, CommandQueryService commandQueryService) {
        this.commandService = commandService;
        this.commandQueryService = commandQueryService;
    }

    /**
     * {@code POST  /commands} : Create a new command.
     *
     * @param command the command to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new command, or with status {@code 400 (Bad Request)} if the command has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/commands")
    public ResponseEntity<Command> createCommand(@Valid @RequestBody Command command) throws URISyntaxException {
        log.debug("REST request to save Command : {}", command);
        if (command.getId() != null) {
            throw new BadRequestAlertException("A new command cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Command result = commandService.save(command);
        return ResponseEntity.created(new URI("/api/commands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /commands} : Updates an existing command.
     *
     * @param command the command to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated command,
     * or with status {@code 400 (Bad Request)} if the command is not valid,
     * or with status {@code 500 (Internal Server Error)} if the command couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/commands")
    public ResponseEntity<Command> updateCommand(@Valid @RequestBody Command command) throws URISyntaxException {
        log.debug("REST request to update Command : {}", command);
        if (command.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Command result = commandService.save(command);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, command.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /commands} : get all the commands.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commands in body.
     */
    @GetMapping("/commands")
    public ResponseEntity<List<Command>> getAllCommands(CommandCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Commands by criteria: {}", criteria);
        Page<Command> page = commandQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /commands/count} : count all the commands.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/commands/count")
    public ResponseEntity<Long> countCommands(CommandCriteria criteria) {
        log.debug("REST request to count Commands by criteria: {}", criteria);
        return ResponseEntity.ok().body(commandQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /commands/:id} : get the "id" command.
     *
     * @param id the id of the command to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the command, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/commands/{id}")
    public ResponseEntity<Command> getCommand(@PathVariable Long id) {
        log.debug("REST request to get Command : {}", id);
        Optional<Command> command = commandService.findOne(id);
        return ResponseUtil.wrapOrNotFound(command);
    }

    /**
     * {@code DELETE  /commands/:id} : delete the "id" command.
     *
     * @param id the id of the command to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/commands/{id}")
    public ResponseEntity<Void> deleteCommand(@PathVariable Long id) {
        log.debug("REST request to delete Command : {}", id);
        commandService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
