package io.github.relaciones.web.rest;

import io.github.relaciones.Application;
import io.github.relaciones.domain.Relation;
import io.github.relaciones.repository.RelationRepository;
import io.github.relaciones.web.rest.dto.RelationDTO;
import io.github.relaciones.web.rest.mapper.RelationMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the RelationResource REST controller.
 *
 * @see RelationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class RelationResourceTest {

    private static final String DEFAULT_CODE = "A";
    private static final String UPDATED_CODE = "B";
    private static final String DEFAULT_DATE = "AAAAA";
    private static final String UPDATED_DATE = "BBBBB";
    private static final String DEFAULT_AUTHOR = "A";
    private static final String UPDATED_AUTHOR = "B";
    private static final String DEFAULT_TITLE = "A";
    private static final String UPDATED_TITLE = "B";
    private static final String DEFAULT_TESTIMONIAL = "AAAAA";
    private static final String UPDATED_TESTIMONIAL = "BBBBB";
    private static final String DEFAULT_DIGITAL_SAMPLE_URL = "AAAAA";
    private static final String UPDATED_DIGITAL_SAMPLE_URL = "BBBBB";

    private static final byte[] DEFAULT_DIGITAL_SAMPLE_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DIGITAL_SAMPLE_FILE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_DIGITAL_SAMPLE_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DIGITAL_SAMPLE_FILE_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_DIGITAL_SAMPLE_FILE2 = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DIGITAL_SAMPLE_FILE2 = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_DIGITAL_SAMPLE_FILE2_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DIGITAL_SAMPLE_FILE2_CONTENT_TYPE = "image/png";

    @Inject
    private RelationRepository relationRepository;

    @Inject
    private RelationMapper relationMapper;

    //@Inject
    //private RelationSearchRepository relationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRelationMockMvc;

    private Relation relation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RelationResource relationResource = new RelationResource();
        ReflectionTestUtils.setField(relationResource, "relationRepository", relationRepository);
        ReflectionTestUtils.setField(relationResource, "relationMapper", relationMapper);
        //ReflectionTestUtils.setField(relationResource, "relationSearchRepository", relationSearchRepository);
        this.restRelationMockMvc = MockMvcBuilders.standaloneSetup(relationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        relation = new Relation();
        relation.setCode(DEFAULT_CODE);
        relation.setDate(DEFAULT_DATE);
        relation.setAuthor(DEFAULT_AUTHOR);
        relation.setTitle(DEFAULT_TITLE);
        relation.setTestimonial(DEFAULT_TESTIMONIAL);
        relation.setDigitalSampleUrl(DEFAULT_DIGITAL_SAMPLE_URL);
        relation.setDigitalSampleFile(DEFAULT_DIGITAL_SAMPLE_FILE);
        relation.setDigitalSampleFileContentType(DEFAULT_DIGITAL_SAMPLE_FILE_CONTENT_TYPE);
        relation.setDigitalSampleFile2(DEFAULT_DIGITAL_SAMPLE_FILE2);
        relation.setDigitalSampleFile2ContentType(DEFAULT_DIGITAL_SAMPLE_FILE2_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createRelation() throws Exception {
        int databaseSizeBeforeCreate = relationRepository.findAll().size();

        // Create the Relation
        RelationDTO relationDTO = relationMapper.relationToRelationDTO(relation);

        restRelationMockMvc.perform(post("/api/relations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(relationDTO)))
                .andExpect(status().isCreated());

        // Validate the Relation in the database
        List<Relation> relations = relationRepository.findAll();
        assertThat(relations).hasSize(databaseSizeBeforeCreate + 1);
        Relation testRelation = relations.get(relations.size() - 1);
        assertThat(testRelation.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testRelation.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testRelation.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testRelation.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testRelation.getTestimonial()).isEqualTo(DEFAULT_TESTIMONIAL);
        assertThat(testRelation.getDigitalSampleUrl()).isEqualTo(DEFAULT_DIGITAL_SAMPLE_URL);
        assertThat(testRelation.getDigitalSampleFile()).isEqualTo(DEFAULT_DIGITAL_SAMPLE_FILE);
        assertThat(testRelation.getDigitalSampleFileContentType()).isEqualTo(DEFAULT_DIGITAL_SAMPLE_FILE_CONTENT_TYPE);
        assertThat(testRelation.getDigitalSampleFile2()).isEqualTo(DEFAULT_DIGITAL_SAMPLE_FILE2);
        assertThat(testRelation.getDigitalSampleFile2ContentType()).isEqualTo(DEFAULT_DIGITAL_SAMPLE_FILE2_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = relationRepository.findAll().size();
        // set the field null
        relation.setCode(null);

        // Create the Relation, which fails.
        RelationDTO relationDTO = relationMapper.relationToRelationDTO(relation);

        restRelationMockMvc.perform(post("/api/relations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(relationDTO)))
                .andExpect(status().isBadRequest());

        List<Relation> relations = relationRepository.findAll();
        assertThat(relations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = relationRepository.findAll().size();
        // set the field null
        relation.setDate(null);

        // Create the Relation, which fails.
        RelationDTO relationDTO = relationMapper.relationToRelationDTO(relation);

        restRelationMockMvc.perform(post("/api/relations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(relationDTO)))
                .andExpect(status().isBadRequest());

        List<Relation> relations = relationRepository.findAll();
        assertThat(relations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAuthorIsRequired() throws Exception {
        int databaseSizeBeforeTest = relationRepository.findAll().size();
        // set the field null
        relation.setAuthor(null);

        // Create the Relation, which fails.
        RelationDTO relationDTO = relationMapper.relationToRelationDTO(relation);

        restRelationMockMvc.perform(post("/api/relations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(relationDTO)))
                .andExpect(status().isBadRequest());

        List<Relation> relations = relationRepository.findAll();
        assertThat(relations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = relationRepository.findAll().size();
        // set the field null
        relation.setTitle(null);

        // Create the Relation, which fails.
        RelationDTO relationDTO = relationMapper.relationToRelationDTO(relation);

        restRelationMockMvc.perform(post("/api/relations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(relationDTO)))
                .andExpect(status().isBadRequest());

        List<Relation> relations = relationRepository.findAll();
        assertThat(relations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRelations() throws Exception {
        // Initialize the database
        relationRepository.saveAndFlush(relation);

        // Get all the relations
        restRelationMockMvc.perform(get("/api/relations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(relation.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR.toString())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].testimonial").value(hasItem(DEFAULT_TESTIMONIAL.toString())))
                .andExpect(jsonPath("$.[*].digitalSampleUrl").value(hasItem(DEFAULT_DIGITAL_SAMPLE_URL.toString())))
                .andExpect(jsonPath("$.[*].digitalSampleFileContentType").value(hasItem(DEFAULT_DIGITAL_SAMPLE_FILE_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].digitalSampleFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_DIGITAL_SAMPLE_FILE))))
                .andExpect(jsonPath("$.[*].digitalSampleFile2ContentType").value(hasItem(DEFAULT_DIGITAL_SAMPLE_FILE2_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].digitalSampleFile2").value(hasItem(Base64Utils.encodeToString(DEFAULT_DIGITAL_SAMPLE_FILE2))));
    }

    @Test
    @Transactional
    public void getRelation() throws Exception {
        // Initialize the database
        relationRepository.saveAndFlush(relation);

        // Get the relation
        restRelationMockMvc.perform(get("/api/relations/{id}", relation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(relation.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.testimonial").value(DEFAULT_TESTIMONIAL.toString()))
            .andExpect(jsonPath("$.digitalSampleUrl").value(DEFAULT_DIGITAL_SAMPLE_URL.toString()))
            .andExpect(jsonPath("$.digitalSampleFileContentType").value(DEFAULT_DIGITAL_SAMPLE_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.digitalSampleFile").value(Base64Utils.encodeToString(DEFAULT_DIGITAL_SAMPLE_FILE)))
            .andExpect(jsonPath("$.digitalSampleFile2ContentType").value(DEFAULT_DIGITAL_SAMPLE_FILE2_CONTENT_TYPE))
            .andExpect(jsonPath("$.digitalSampleFile2").value(Base64Utils.encodeToString(DEFAULT_DIGITAL_SAMPLE_FILE2)));
    }

    @Test
    @Transactional
    public void getNonExistingRelation() throws Exception {
        // Get the relation
        restRelationMockMvc.perform(get("/api/relations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRelation() throws Exception {
        // Initialize the database
        relationRepository.saveAndFlush(relation);

		int databaseSizeBeforeUpdate = relationRepository.findAll().size();

        // Update the relation
        relation.setCode(UPDATED_CODE);
        relation.setDate(UPDATED_DATE);
        relation.setAuthor(UPDATED_AUTHOR);
        relation.setTitle(UPDATED_TITLE);
        relation.setTestimonial(UPDATED_TESTIMONIAL);
        relation.setDigitalSampleUrl(UPDATED_DIGITAL_SAMPLE_URL);
        relation.setDigitalSampleFile(UPDATED_DIGITAL_SAMPLE_FILE);
        relation.setDigitalSampleFileContentType(UPDATED_DIGITAL_SAMPLE_FILE_CONTENT_TYPE);
        relation.setDigitalSampleFile2(UPDATED_DIGITAL_SAMPLE_FILE2);
        relation.setDigitalSampleFile2ContentType(UPDATED_DIGITAL_SAMPLE_FILE2_CONTENT_TYPE);
        RelationDTO relationDTO = relationMapper.relationToRelationDTO(relation);

        restRelationMockMvc.perform(put("/api/relations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(relationDTO)))
                .andExpect(status().isOk());

        // Validate the Relation in the database
        List<Relation> relations = relationRepository.findAll();
        assertThat(relations).hasSize(databaseSizeBeforeUpdate);
        Relation testRelation = relations.get(relations.size() - 1);
        assertThat(testRelation.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testRelation.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRelation.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testRelation.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testRelation.getTestimonial()).isEqualTo(UPDATED_TESTIMONIAL);
        assertThat(testRelation.getDigitalSampleUrl()).isEqualTo(UPDATED_DIGITAL_SAMPLE_URL);
        assertThat(testRelation.getDigitalSampleFile()).isEqualTo(UPDATED_DIGITAL_SAMPLE_FILE);
        assertThat(testRelation.getDigitalSampleFileContentType()).isEqualTo(UPDATED_DIGITAL_SAMPLE_FILE_CONTENT_TYPE);
        assertThat(testRelation.getDigitalSampleFile2()).isEqualTo(UPDATED_DIGITAL_SAMPLE_FILE2);
        assertThat(testRelation.getDigitalSampleFile2ContentType()).isEqualTo(UPDATED_DIGITAL_SAMPLE_FILE2_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void deleteRelation() throws Exception {
        // Initialize the database
        relationRepository.saveAndFlush(relation);

		int databaseSizeBeforeDelete = relationRepository.findAll().size();

        // Get the relation
        restRelationMockMvc.perform(delete("/api/relations/{id}", relation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Relation> relations = relationRepository.findAll();
        assertThat(relations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
