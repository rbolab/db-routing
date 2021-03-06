package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhmdsApp;

import com.mycompany.myapp.domain.Fund;
import com.mycompany.myapp.repository.FundRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FundResource REST controller.
 *
 * @see FundResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhmdsApp.class)
public class FundResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final String DEFAULT_CURRENCY = "AAAAA";
    private static final String UPDATED_CURRENCY = "BBBBB";

    @Inject
    private FundRepository fundRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restFundMockMvc;

    private Fund fund;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FundResource fundResource = new FundResource();
        ReflectionTestUtils.setField(fundResource, "fundRepository", fundRepository);
        this.restFundMockMvc = MockMvcBuilders.standaloneSetup(fundResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fund createEntity(EntityManager em) {
        Fund fund = new Fund()
                .name(DEFAULT_NAME)
                .currency(DEFAULT_CURRENCY);
        return fund;
    }

    @Before
    public void initTest() {
        fund = createEntity(em);
    }

    @Test
    @Transactional
    public void createFund() throws Exception {
        int databaseSizeBeforeCreate = fundRepository.findAll().size();

        // Create the Fund

        restFundMockMvc.perform(post("/api/funds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fund)))
                .andExpect(status().isCreated());

        // Validate the Fund in the database
        List<Fund> funds = fundRepository.findAll();
        assertThat(funds).hasSize(databaseSizeBeforeCreate + 1);
        Fund testFund = funds.get(funds.size() - 1);
        assertThat(testFund.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFund.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
    }

    @Test
    @Transactional
    public void getAllFunds() throws Exception {
        // Initialize the database
        fundRepository.saveAndFlush(fund);

        // Get all the funds
        restFundMockMvc.perform(get("/api/funds?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(fund.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())));
    }

    @Test
    @Transactional
    public void getFund() throws Exception {
        // Initialize the database
        fundRepository.saveAndFlush(fund);

        // Get the fund
        restFundMockMvc.perform(get("/api/funds/{id}", fund.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fund.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFund() throws Exception {
        // Get the fund
        restFundMockMvc.perform(get("/api/funds/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFund() throws Exception {
        // Initialize the database
        fundRepository.saveAndFlush(fund);
        int databaseSizeBeforeUpdate = fundRepository.findAll().size();

        // Update the fund
        Fund updatedFund = fundRepository.findOne(fund.getId());
        updatedFund
                .name(UPDATED_NAME)
                .currency(UPDATED_CURRENCY);

        restFundMockMvc.perform(put("/api/funds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFund)))
                .andExpect(status().isOk());

        // Validate the Fund in the database
        List<Fund> funds = fundRepository.findAll();
        assertThat(funds).hasSize(databaseSizeBeforeUpdate);
        Fund testFund = funds.get(funds.size() - 1);
        assertThat(testFund.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFund.getCurrency()).isEqualTo(UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    public void deleteFund() throws Exception {
        // Initialize the database
        fundRepository.saveAndFlush(fund);
        int databaseSizeBeforeDelete = fundRepository.findAll().size();

        // Get the fund
        restFundMockMvc.perform(delete("/api/funds/{id}", fund.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Fund> funds = fundRepository.findAll();
        assertThat(funds).hasSize(databaseSizeBeforeDelete - 1);
    }
}
