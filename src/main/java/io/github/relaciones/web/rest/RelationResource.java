package io.github.relaciones.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.relaciones.domain.Relation;
import io.github.relaciones.domain.User;
import io.github.relaciones.repository.RelationRepository;
import io.github.relaciones.repository.UserRepository;
import io.github.relaciones.security.SecurityUtils;
import io.github.relaciones.web.rest.dto.RelationDTO;
import io.github.relaciones.web.rest.mapper.RelationMapper;
import io.github.relaciones.web.rest.util.HeaderUtil;
import io.github.relaciones.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing Relation.
 */
@RestController
@RequestMapping("/api")
public class RelationResource {

    private final Logger log = LoggerFactory.getLogger(RelationResource.class);

    @Inject
    private RelationRepository relationRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private RelationMapper relationMapper;

    //@Inject
    //private RelationSearchRepository //relationSearchRepository;

    /**
     * POST  /relations -> Create a new relation.
     */
    @RequestMapping(value = "/relations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RelationDTO> createRelation(@Valid @RequestBody RelationDTO relationDTO) throws URISyntaxException {
        log.debug("REST request to save Relation : {}", relationDTO);
        if (relationDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new relation cannot already have an ID").body(null);
        }
        Relation relation = relationMapper.relationDTOToRelation(relationDTO);

        // Set user and date
        /* set current user */
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get();
        relation.setUser(user);
        relation.setCreated(Calendar.getInstance().getTime());

        Relation result = relationRepository.save(relation);
        //relationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/relations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("relation", result.getId().toString()))
            .body(relationMapper.relationToRelationDTO(result));
    }

    /**
     * PUT  /relations -> Updates an existing relation.
     */
    @RequestMapping(value = "/relations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RelationDTO> updateRelation(@Valid @RequestBody RelationDTO relationDTO) throws URISyntaxException {
        log.debug("REST request to update Relation : {}", relationDTO);
        if (relationDTO.getId() == null) {
            return createRelation(relationDTO);
        }
        Relation relation = relationMapper.relationDTOToRelation(relationDTO);

        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get();
        if(!relationRepository.findOne(relationDTO.getId()).getUser().equals(user))
        {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createEntityUpdateAlert("User has not this relation ", relationDTO.getId().toString()))
                .body(relationDTO);
        }

        // Set user and date
        relation.setModified(Calendar.getInstance().getTime());

        Relation result = relationRepository.save(relation);
        //relationSearchRepository.save(relation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("relation", relationDTO.getId().toString()))
            .body(relationMapper.relationToRelationDTO(result));
    }

    /**
     * GET  /relations -> get all the relations.
     */
    @RequestMapping(value = "/relations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<RelationDTO>> getAllRelations(Pageable pageable)
        throws URISyntaxException {
        // TODO get only relation from user
        //Page<Relation> page = relationRepository.findAll(pageable);

        // Show only relations from user
        Page<Relation> page = relationRepository.findByUser_Login(SecurityUtils.getCurrentLogin(), pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/relations");
        return new ResponseEntity<>(page.getContent().stream()
            .map(relationMapper::relationToRelationDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /relations/:id -> get the "id" relation.
     */
    @RequestMapping(value = "/relations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RelationDTO> getRelation(@PathVariable Long id) {
        log.debug("REST request to get Relation : {}", id);
        return Optional.ofNullable(relationRepository.findByIdAndUser_Login(id, SecurityUtils.getCurrentLogin()))
            .map(relationMapper::relationToRelationDTO)
            .map(relationDTO -> new ResponseEntity<>(
                relationDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /relations/:id -> delete the "id" relation.
     */
    @RequestMapping(value = "/relations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRelation(@PathVariable Long id) {
        log.debug("REST request to delete Relation : {}", id);

        // Set user and date
        /* Only owner can delete it relations */
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get();
        if(relationRepository.findOne(id).getUser().equals(user))
        {
            relationRepository.delete(id);
            //relationSearchRepository.delete(id);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("relation", id.toString())).build();
        }

        return ResponseEntity.badRequest().headers(HeaderUtil.createAlert("User has not this relation ", id.toString())).build();
    }

    /**
     * SEARCH  /_search/relations/:query -> search for the relation corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/relations/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<RelationDTO> searchRelations(@PathVariable String query) {

        return StreamSupport
            .stream(relationRepository.search("%" + query + "%", "%" + query + "%", "%" + query + "%", query, "%" + query + "%").spliterator(), false)
            .map(relationMapper::relationToRelationDTO)
            .collect(Collectors.toList());
    }
}
