package com.etho.pm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.etho.pm.IntegrationTest;
import com.etho.pm.domain.Nominee;
import com.etho.pm.domain.Policy;
import com.etho.pm.domain.Users;
import com.etho.pm.domain.enumeration.PolicyStatus;
import com.etho.pm.repository.PolicyRepository;
import com.etho.pm.service.criteria.PolicyCriteria;
import com.etho.pm.service.dto.PolicyDTO;
import com.etho.pm.service.mapper.PolicyMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PolicyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PolicyResourceIT {

    private static final Long DEFAULT_POLICY_AMOUNT = 1L;
    private static final Long UPDATED_POLICY_AMOUNT = 2L;
    private static final Long SMALLER_POLICY_AMOUNT = 1L - 1L;

    private static final Long DEFAULT_INSTALMENT_AMOUNT = 1L;
    private static final Long UPDATED_INSTALMENT_AMOUNT = 2L;
    private static final Long SMALLER_INSTALMENT_AMOUNT = 1L - 1L;

    private static final Long DEFAULT_TERM = 1L;
    private static final Long UPDATED_TERM = 2L;
    private static final Long SMALLER_TERM = 1L - 1L;

    private static final Long DEFAULT_INSTALMENT_PERIOD = 1L;
    private static final Long UPDATED_INSTALMENT_PERIOD = 2L;
    private static final Long SMALLER_INSTALMENT_PERIOD = 1L - 1L;

    private static final Long DEFAULT_INSTALMENT_DATE = 1L;
    private static final Long UPDATED_INSTALMENT_DATE = 2L;
    private static final Long SMALLER_INSTALMENT_DATE = 1L - 1L;

    private static final PolicyStatus DEFAULT_STATUS = PolicyStatus.OPEN;
    private static final PolicyStatus UPDATED_STATUS = PolicyStatus.INFORCE;

    private static final Instant DEFAULT_DATE_START = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_START = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_END = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_END = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MATURITY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MATURITY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UIN_NO = "AAAAAAAAAA";
    private static final String UPDATED_UIN_NO = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/policies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private PolicyMapper policyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPolicyMockMvc;

    private Policy policy;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Policy createEntity(EntityManager em) {
        Policy policy = new Policy()
            .policyAmount(DEFAULT_POLICY_AMOUNT)
            .instalmentAmount(DEFAULT_INSTALMENT_AMOUNT)
            .term(DEFAULT_TERM)
            .instalmentPeriod(DEFAULT_INSTALMENT_PERIOD)
            .instalmentDate(DEFAULT_INSTALMENT_DATE)
            .status(DEFAULT_STATUS)
            .dateStart(DEFAULT_DATE_START)
            .dateEnd(DEFAULT_DATE_END)
            .maturityDate(DEFAULT_MATURITY_DATE)
            .uinNo(DEFAULT_UIN_NO)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return policy;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Policy createUpdatedEntity(EntityManager em) {
        Policy policy = new Policy()
            .policyAmount(UPDATED_POLICY_AMOUNT)
            .instalmentAmount(UPDATED_INSTALMENT_AMOUNT)
            .term(UPDATED_TERM)
            .instalmentPeriod(UPDATED_INSTALMENT_PERIOD)
            .instalmentDate(UPDATED_INSTALMENT_DATE)
            .status(UPDATED_STATUS)
            .dateStart(UPDATED_DATE_START)
            .dateEnd(UPDATED_DATE_END)
            .maturityDate(UPDATED_MATURITY_DATE)
            .uinNo(UPDATED_UIN_NO)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        return policy;
    }

    @BeforeEach
    public void initTest() {
        policy = createEntity(em);
    }

    @Test
    @Transactional
    void createPolicy() throws Exception {
        int databaseSizeBeforeCreate = policyRepository.findAll().size();
        // Create the Policy
        PolicyDTO policyDTO = policyMapper.toDto(policy);
        restPolicyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(policyDTO)))
            .andExpect(status().isCreated());

        // Validate the Policy in the database
        List<Policy> policyList = policyRepository.findAll();
        assertThat(policyList).hasSize(databaseSizeBeforeCreate + 1);
        Policy testPolicy = policyList.get(policyList.size() - 1);
        assertThat(testPolicy.getPolicyAmount()).isEqualTo(DEFAULT_POLICY_AMOUNT);
        assertThat(testPolicy.getInstalmentAmount()).isEqualTo(DEFAULT_INSTALMENT_AMOUNT);
        assertThat(testPolicy.getTerm()).isEqualTo(DEFAULT_TERM);
        assertThat(testPolicy.getInstalmentPeriod()).isEqualTo(DEFAULT_INSTALMENT_PERIOD);
        assertThat(testPolicy.getInstalmentDate()).isEqualTo(DEFAULT_INSTALMENT_DATE);
        assertThat(testPolicy.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPolicy.getDateStart()).isEqualTo(DEFAULT_DATE_START);
        assertThat(testPolicy.getDateEnd()).isEqualTo(DEFAULT_DATE_END);
        assertThat(testPolicy.getMaturityDate()).isEqualTo(DEFAULT_MATURITY_DATE);
        assertThat(testPolicy.getUinNo()).isEqualTo(DEFAULT_UIN_NO);
        assertThat(testPolicy.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testPolicy.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void createPolicyWithExistingId() throws Exception {
        // Create the Policy with an existing ID
        policy.setId(1L);
        PolicyDTO policyDTO = policyMapper.toDto(policy);

        int databaseSizeBeforeCreate = policyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPolicyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(policyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Policy in the database
        List<Policy> policyList = policyRepository.findAll();
        assertThat(policyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateStartIsRequired() throws Exception {
        int databaseSizeBeforeTest = policyRepository.findAll().size();
        // set the field null
        policy.setDateStart(null);

        // Create the Policy, which fails.
        PolicyDTO policyDTO = policyMapper.toDto(policy);

        restPolicyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(policyDTO)))
            .andExpect(status().isBadRequest());

        List<Policy> policyList = policyRepository.findAll();
        assertThat(policyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateEndIsRequired() throws Exception {
        int databaseSizeBeforeTest = policyRepository.findAll().size();
        // set the field null
        policy.setDateEnd(null);

        // Create the Policy, which fails.
        PolicyDTO policyDTO = policyMapper.toDto(policy);

        restPolicyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(policyDTO)))
            .andExpect(status().isBadRequest());

        List<Policy> policyList = policyRepository.findAll();
        assertThat(policyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMaturityDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = policyRepository.findAll().size();
        // set the field null
        policy.setMaturityDate(null);

        // Create the Policy, which fails.
        PolicyDTO policyDTO = policyMapper.toDto(policy);

        restPolicyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(policyDTO)))
            .andExpect(status().isBadRequest());

        List<Policy> policyList = policyRepository.findAll();
        assertThat(policyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastModifiedIsRequired() throws Exception {
        int databaseSizeBeforeTest = policyRepository.findAll().size();
        // set the field null
        policy.setLastModified(null);

        // Create the Policy, which fails.
        PolicyDTO policyDTO = policyMapper.toDto(policy);

        restPolicyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(policyDTO)))
            .andExpect(status().isBadRequest());

        List<Policy> policyList = policyRepository.findAll();
        assertThat(policyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastModifiedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = policyRepository.findAll().size();
        // set the field null
        policy.setLastModifiedBy(null);

        // Create the Policy, which fails.
        PolicyDTO policyDTO = policyMapper.toDto(policy);

        restPolicyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(policyDTO)))
            .andExpect(status().isBadRequest());

        List<Policy> policyList = policyRepository.findAll();
        assertThat(policyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPolicies() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList
        restPolicyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(policy.getId().intValue())))
            .andExpect(jsonPath("$.[*].policyAmount").value(hasItem(DEFAULT_POLICY_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].instalmentAmount").value(hasItem(DEFAULT_INSTALMENT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].term").value(hasItem(DEFAULT_TERM.intValue())))
            .andExpect(jsonPath("$.[*].instalmentPeriod").value(hasItem(DEFAULT_INSTALMENT_PERIOD.intValue())))
            .andExpect(jsonPath("$.[*].instalmentDate").value(hasItem(DEFAULT_INSTALMENT_DATE.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].dateStart").value(hasItem(DEFAULT_DATE_START.toString())))
            .andExpect(jsonPath("$.[*].dateEnd").value(hasItem(DEFAULT_DATE_END.toString())))
            .andExpect(jsonPath("$.[*].maturityDate").value(hasItem(DEFAULT_MATURITY_DATE.toString())))
            .andExpect(jsonPath("$.[*].uinNo").value(hasItem(DEFAULT_UIN_NO)))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
    }

    @Test
    @Transactional
    void getPolicy() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get the policy
        restPolicyMockMvc
            .perform(get(ENTITY_API_URL_ID, policy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(policy.getId().intValue()))
            .andExpect(jsonPath("$.policyAmount").value(DEFAULT_POLICY_AMOUNT.intValue()))
            .andExpect(jsonPath("$.instalmentAmount").value(DEFAULT_INSTALMENT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.term").value(DEFAULT_TERM.intValue()))
            .andExpect(jsonPath("$.instalmentPeriod").value(DEFAULT_INSTALMENT_PERIOD.intValue()))
            .andExpect(jsonPath("$.instalmentDate").value(DEFAULT_INSTALMENT_DATE.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.dateStart").value(DEFAULT_DATE_START.toString()))
            .andExpect(jsonPath("$.dateEnd").value(DEFAULT_DATE_END.toString()))
            .andExpect(jsonPath("$.maturityDate").value(DEFAULT_MATURITY_DATE.toString()))
            .andExpect(jsonPath("$.uinNo").value(DEFAULT_UIN_NO))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY));
    }

    @Test
    @Transactional
    void getPoliciesByIdFiltering() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        Long id = policy.getId();

        defaultPolicyShouldBeFound("id.equals=" + id);
        defaultPolicyShouldNotBeFound("id.notEquals=" + id);

        defaultPolicyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPolicyShouldNotBeFound("id.greaterThan=" + id);

        defaultPolicyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPolicyShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPoliciesByPolicyAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where policyAmount equals to DEFAULT_POLICY_AMOUNT
        defaultPolicyShouldBeFound("policyAmount.equals=" + DEFAULT_POLICY_AMOUNT);

        // Get all the policyList where policyAmount equals to UPDATED_POLICY_AMOUNT
        defaultPolicyShouldNotBeFound("policyAmount.equals=" + UPDATED_POLICY_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPoliciesByPolicyAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where policyAmount not equals to DEFAULT_POLICY_AMOUNT
        defaultPolicyShouldNotBeFound("policyAmount.notEquals=" + DEFAULT_POLICY_AMOUNT);

        // Get all the policyList where policyAmount not equals to UPDATED_POLICY_AMOUNT
        defaultPolicyShouldBeFound("policyAmount.notEquals=" + UPDATED_POLICY_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPoliciesByPolicyAmountIsInShouldWork() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where policyAmount in DEFAULT_POLICY_AMOUNT or UPDATED_POLICY_AMOUNT
        defaultPolicyShouldBeFound("policyAmount.in=" + DEFAULT_POLICY_AMOUNT + "," + UPDATED_POLICY_AMOUNT);

        // Get all the policyList where policyAmount equals to UPDATED_POLICY_AMOUNT
        defaultPolicyShouldNotBeFound("policyAmount.in=" + UPDATED_POLICY_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPoliciesByPolicyAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where policyAmount is not null
        defaultPolicyShouldBeFound("policyAmount.specified=true");

        // Get all the policyList where policyAmount is null
        defaultPolicyShouldNotBeFound("policyAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPoliciesByPolicyAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where policyAmount is greater than or equal to DEFAULT_POLICY_AMOUNT
        defaultPolicyShouldBeFound("policyAmount.greaterThanOrEqual=" + DEFAULT_POLICY_AMOUNT);

        // Get all the policyList where policyAmount is greater than or equal to UPDATED_POLICY_AMOUNT
        defaultPolicyShouldNotBeFound("policyAmount.greaterThanOrEqual=" + UPDATED_POLICY_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPoliciesByPolicyAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where policyAmount is less than or equal to DEFAULT_POLICY_AMOUNT
        defaultPolicyShouldBeFound("policyAmount.lessThanOrEqual=" + DEFAULT_POLICY_AMOUNT);

        // Get all the policyList where policyAmount is less than or equal to SMALLER_POLICY_AMOUNT
        defaultPolicyShouldNotBeFound("policyAmount.lessThanOrEqual=" + SMALLER_POLICY_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPoliciesByPolicyAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where policyAmount is less than DEFAULT_POLICY_AMOUNT
        defaultPolicyShouldNotBeFound("policyAmount.lessThan=" + DEFAULT_POLICY_AMOUNT);

        // Get all the policyList where policyAmount is less than UPDATED_POLICY_AMOUNT
        defaultPolicyShouldBeFound("policyAmount.lessThan=" + UPDATED_POLICY_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPoliciesByPolicyAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where policyAmount is greater than DEFAULT_POLICY_AMOUNT
        defaultPolicyShouldNotBeFound("policyAmount.greaterThan=" + DEFAULT_POLICY_AMOUNT);

        // Get all the policyList where policyAmount is greater than SMALLER_POLICY_AMOUNT
        defaultPolicyShouldBeFound("policyAmount.greaterThan=" + SMALLER_POLICY_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPoliciesByInstalmentAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where instalmentAmount equals to DEFAULT_INSTALMENT_AMOUNT
        defaultPolicyShouldBeFound("instalmentAmount.equals=" + DEFAULT_INSTALMENT_AMOUNT);

        // Get all the policyList where instalmentAmount equals to UPDATED_INSTALMENT_AMOUNT
        defaultPolicyShouldNotBeFound("instalmentAmount.equals=" + UPDATED_INSTALMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPoliciesByInstalmentAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where instalmentAmount not equals to DEFAULT_INSTALMENT_AMOUNT
        defaultPolicyShouldNotBeFound("instalmentAmount.notEquals=" + DEFAULT_INSTALMENT_AMOUNT);

        // Get all the policyList where instalmentAmount not equals to UPDATED_INSTALMENT_AMOUNT
        defaultPolicyShouldBeFound("instalmentAmount.notEquals=" + UPDATED_INSTALMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPoliciesByInstalmentAmountIsInShouldWork() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where instalmentAmount in DEFAULT_INSTALMENT_AMOUNT or UPDATED_INSTALMENT_AMOUNT
        defaultPolicyShouldBeFound("instalmentAmount.in=" + DEFAULT_INSTALMENT_AMOUNT + "," + UPDATED_INSTALMENT_AMOUNT);

        // Get all the policyList where instalmentAmount equals to UPDATED_INSTALMENT_AMOUNT
        defaultPolicyShouldNotBeFound("instalmentAmount.in=" + UPDATED_INSTALMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPoliciesByInstalmentAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where instalmentAmount is not null
        defaultPolicyShouldBeFound("instalmentAmount.specified=true");

        // Get all the policyList where instalmentAmount is null
        defaultPolicyShouldNotBeFound("instalmentAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPoliciesByInstalmentAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where instalmentAmount is greater than or equal to DEFAULT_INSTALMENT_AMOUNT
        defaultPolicyShouldBeFound("instalmentAmount.greaterThanOrEqual=" + DEFAULT_INSTALMENT_AMOUNT);

        // Get all the policyList where instalmentAmount is greater than or equal to UPDATED_INSTALMENT_AMOUNT
        defaultPolicyShouldNotBeFound("instalmentAmount.greaterThanOrEqual=" + UPDATED_INSTALMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPoliciesByInstalmentAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where instalmentAmount is less than or equal to DEFAULT_INSTALMENT_AMOUNT
        defaultPolicyShouldBeFound("instalmentAmount.lessThanOrEqual=" + DEFAULT_INSTALMENT_AMOUNT);

        // Get all the policyList where instalmentAmount is less than or equal to SMALLER_INSTALMENT_AMOUNT
        defaultPolicyShouldNotBeFound("instalmentAmount.lessThanOrEqual=" + SMALLER_INSTALMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPoliciesByInstalmentAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where instalmentAmount is less than DEFAULT_INSTALMENT_AMOUNT
        defaultPolicyShouldNotBeFound("instalmentAmount.lessThan=" + DEFAULT_INSTALMENT_AMOUNT);

        // Get all the policyList where instalmentAmount is less than UPDATED_INSTALMENT_AMOUNT
        defaultPolicyShouldBeFound("instalmentAmount.lessThan=" + UPDATED_INSTALMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPoliciesByInstalmentAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where instalmentAmount is greater than DEFAULT_INSTALMENT_AMOUNT
        defaultPolicyShouldNotBeFound("instalmentAmount.greaterThan=" + DEFAULT_INSTALMENT_AMOUNT);

        // Get all the policyList where instalmentAmount is greater than SMALLER_INSTALMENT_AMOUNT
        defaultPolicyShouldBeFound("instalmentAmount.greaterThan=" + SMALLER_INSTALMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPoliciesByTermIsEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where term equals to DEFAULT_TERM
        defaultPolicyShouldBeFound("term.equals=" + DEFAULT_TERM);

        // Get all the policyList where term equals to UPDATED_TERM
        defaultPolicyShouldNotBeFound("term.equals=" + UPDATED_TERM);
    }

    @Test
    @Transactional
    void getAllPoliciesByTermIsNotEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where term not equals to DEFAULT_TERM
        defaultPolicyShouldNotBeFound("term.notEquals=" + DEFAULT_TERM);

        // Get all the policyList where term not equals to UPDATED_TERM
        defaultPolicyShouldBeFound("term.notEquals=" + UPDATED_TERM);
    }

    @Test
    @Transactional
    void getAllPoliciesByTermIsInShouldWork() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where term in DEFAULT_TERM or UPDATED_TERM
        defaultPolicyShouldBeFound("term.in=" + DEFAULT_TERM + "," + UPDATED_TERM);

        // Get all the policyList where term equals to UPDATED_TERM
        defaultPolicyShouldNotBeFound("term.in=" + UPDATED_TERM);
    }

    @Test
    @Transactional
    void getAllPoliciesByTermIsNullOrNotNull() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where term is not null
        defaultPolicyShouldBeFound("term.specified=true");

        // Get all the policyList where term is null
        defaultPolicyShouldNotBeFound("term.specified=false");
    }

    @Test
    @Transactional
    void getAllPoliciesByTermIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where term is greater than or equal to DEFAULT_TERM
        defaultPolicyShouldBeFound("term.greaterThanOrEqual=" + DEFAULT_TERM);

        // Get all the policyList where term is greater than or equal to UPDATED_TERM
        defaultPolicyShouldNotBeFound("term.greaterThanOrEqual=" + UPDATED_TERM);
    }

    @Test
    @Transactional
    void getAllPoliciesByTermIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where term is less than or equal to DEFAULT_TERM
        defaultPolicyShouldBeFound("term.lessThanOrEqual=" + DEFAULT_TERM);

        // Get all the policyList where term is less than or equal to SMALLER_TERM
        defaultPolicyShouldNotBeFound("term.lessThanOrEqual=" + SMALLER_TERM);
    }

    @Test
    @Transactional
    void getAllPoliciesByTermIsLessThanSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where term is less than DEFAULT_TERM
        defaultPolicyShouldNotBeFound("term.lessThan=" + DEFAULT_TERM);

        // Get all the policyList where term is less than UPDATED_TERM
        defaultPolicyShouldBeFound("term.lessThan=" + UPDATED_TERM);
    }

    @Test
    @Transactional
    void getAllPoliciesByTermIsGreaterThanSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where term is greater than DEFAULT_TERM
        defaultPolicyShouldNotBeFound("term.greaterThan=" + DEFAULT_TERM);

        // Get all the policyList where term is greater than SMALLER_TERM
        defaultPolicyShouldBeFound("term.greaterThan=" + SMALLER_TERM);
    }

    @Test
    @Transactional
    void getAllPoliciesByInstalmentPeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where instalmentPeriod equals to DEFAULT_INSTALMENT_PERIOD
        defaultPolicyShouldBeFound("instalmentPeriod.equals=" + DEFAULT_INSTALMENT_PERIOD);

        // Get all the policyList where instalmentPeriod equals to UPDATED_INSTALMENT_PERIOD
        defaultPolicyShouldNotBeFound("instalmentPeriod.equals=" + UPDATED_INSTALMENT_PERIOD);
    }

    @Test
    @Transactional
    void getAllPoliciesByInstalmentPeriodIsNotEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where instalmentPeriod not equals to DEFAULT_INSTALMENT_PERIOD
        defaultPolicyShouldNotBeFound("instalmentPeriod.notEquals=" + DEFAULT_INSTALMENT_PERIOD);

        // Get all the policyList where instalmentPeriod not equals to UPDATED_INSTALMENT_PERIOD
        defaultPolicyShouldBeFound("instalmentPeriod.notEquals=" + UPDATED_INSTALMENT_PERIOD);
    }

    @Test
    @Transactional
    void getAllPoliciesByInstalmentPeriodIsInShouldWork() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where instalmentPeriod in DEFAULT_INSTALMENT_PERIOD or UPDATED_INSTALMENT_PERIOD
        defaultPolicyShouldBeFound("instalmentPeriod.in=" + DEFAULT_INSTALMENT_PERIOD + "," + UPDATED_INSTALMENT_PERIOD);

        // Get all the policyList where instalmentPeriod equals to UPDATED_INSTALMENT_PERIOD
        defaultPolicyShouldNotBeFound("instalmentPeriod.in=" + UPDATED_INSTALMENT_PERIOD);
    }

    @Test
    @Transactional
    void getAllPoliciesByInstalmentPeriodIsNullOrNotNull() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where instalmentPeriod is not null
        defaultPolicyShouldBeFound("instalmentPeriod.specified=true");

        // Get all the policyList where instalmentPeriod is null
        defaultPolicyShouldNotBeFound("instalmentPeriod.specified=false");
    }

    @Test
    @Transactional
    void getAllPoliciesByInstalmentPeriodIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where instalmentPeriod is greater than or equal to DEFAULT_INSTALMENT_PERIOD
        defaultPolicyShouldBeFound("instalmentPeriod.greaterThanOrEqual=" + DEFAULT_INSTALMENT_PERIOD);

        // Get all the policyList where instalmentPeriod is greater than or equal to UPDATED_INSTALMENT_PERIOD
        defaultPolicyShouldNotBeFound("instalmentPeriod.greaterThanOrEqual=" + UPDATED_INSTALMENT_PERIOD);
    }

    @Test
    @Transactional
    void getAllPoliciesByInstalmentPeriodIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where instalmentPeriod is less than or equal to DEFAULT_INSTALMENT_PERIOD
        defaultPolicyShouldBeFound("instalmentPeriod.lessThanOrEqual=" + DEFAULT_INSTALMENT_PERIOD);

        // Get all the policyList where instalmentPeriod is less than or equal to SMALLER_INSTALMENT_PERIOD
        defaultPolicyShouldNotBeFound("instalmentPeriod.lessThanOrEqual=" + SMALLER_INSTALMENT_PERIOD);
    }

    @Test
    @Transactional
    void getAllPoliciesByInstalmentPeriodIsLessThanSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where instalmentPeriod is less than DEFAULT_INSTALMENT_PERIOD
        defaultPolicyShouldNotBeFound("instalmentPeriod.lessThan=" + DEFAULT_INSTALMENT_PERIOD);

        // Get all the policyList where instalmentPeriod is less than UPDATED_INSTALMENT_PERIOD
        defaultPolicyShouldBeFound("instalmentPeriod.lessThan=" + UPDATED_INSTALMENT_PERIOD);
    }

    @Test
    @Transactional
    void getAllPoliciesByInstalmentPeriodIsGreaterThanSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where instalmentPeriod is greater than DEFAULT_INSTALMENT_PERIOD
        defaultPolicyShouldNotBeFound("instalmentPeriod.greaterThan=" + DEFAULT_INSTALMENT_PERIOD);

        // Get all the policyList where instalmentPeriod is greater than SMALLER_INSTALMENT_PERIOD
        defaultPolicyShouldBeFound("instalmentPeriod.greaterThan=" + SMALLER_INSTALMENT_PERIOD);
    }

    @Test
    @Transactional
    void getAllPoliciesByInstalmentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where instalmentDate equals to DEFAULT_INSTALMENT_DATE
        defaultPolicyShouldBeFound("instalmentDate.equals=" + DEFAULT_INSTALMENT_DATE);

        // Get all the policyList where instalmentDate equals to UPDATED_INSTALMENT_DATE
        defaultPolicyShouldNotBeFound("instalmentDate.equals=" + UPDATED_INSTALMENT_DATE);
    }

    @Test
    @Transactional
    void getAllPoliciesByInstalmentDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where instalmentDate not equals to DEFAULT_INSTALMENT_DATE
        defaultPolicyShouldNotBeFound("instalmentDate.notEquals=" + DEFAULT_INSTALMENT_DATE);

        // Get all the policyList where instalmentDate not equals to UPDATED_INSTALMENT_DATE
        defaultPolicyShouldBeFound("instalmentDate.notEquals=" + UPDATED_INSTALMENT_DATE);
    }

    @Test
    @Transactional
    void getAllPoliciesByInstalmentDateIsInShouldWork() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where instalmentDate in DEFAULT_INSTALMENT_DATE or UPDATED_INSTALMENT_DATE
        defaultPolicyShouldBeFound("instalmentDate.in=" + DEFAULT_INSTALMENT_DATE + "," + UPDATED_INSTALMENT_DATE);

        // Get all the policyList where instalmentDate equals to UPDATED_INSTALMENT_DATE
        defaultPolicyShouldNotBeFound("instalmentDate.in=" + UPDATED_INSTALMENT_DATE);
    }

    @Test
    @Transactional
    void getAllPoliciesByInstalmentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where instalmentDate is not null
        defaultPolicyShouldBeFound("instalmentDate.specified=true");

        // Get all the policyList where instalmentDate is null
        defaultPolicyShouldNotBeFound("instalmentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPoliciesByInstalmentDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where instalmentDate is greater than or equal to DEFAULT_INSTALMENT_DATE
        defaultPolicyShouldBeFound("instalmentDate.greaterThanOrEqual=" + DEFAULT_INSTALMENT_DATE);

        // Get all the policyList where instalmentDate is greater than or equal to UPDATED_INSTALMENT_DATE
        defaultPolicyShouldNotBeFound("instalmentDate.greaterThanOrEqual=" + UPDATED_INSTALMENT_DATE);
    }

    @Test
    @Transactional
    void getAllPoliciesByInstalmentDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where instalmentDate is less than or equal to DEFAULT_INSTALMENT_DATE
        defaultPolicyShouldBeFound("instalmentDate.lessThanOrEqual=" + DEFAULT_INSTALMENT_DATE);

        // Get all the policyList where instalmentDate is less than or equal to SMALLER_INSTALMENT_DATE
        defaultPolicyShouldNotBeFound("instalmentDate.lessThanOrEqual=" + SMALLER_INSTALMENT_DATE);
    }

    @Test
    @Transactional
    void getAllPoliciesByInstalmentDateIsLessThanSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where instalmentDate is less than DEFAULT_INSTALMENT_DATE
        defaultPolicyShouldNotBeFound("instalmentDate.lessThan=" + DEFAULT_INSTALMENT_DATE);

        // Get all the policyList where instalmentDate is less than UPDATED_INSTALMENT_DATE
        defaultPolicyShouldBeFound("instalmentDate.lessThan=" + UPDATED_INSTALMENT_DATE);
    }

    @Test
    @Transactional
    void getAllPoliciesByInstalmentDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where instalmentDate is greater than DEFAULT_INSTALMENT_DATE
        defaultPolicyShouldNotBeFound("instalmentDate.greaterThan=" + DEFAULT_INSTALMENT_DATE);

        // Get all the policyList where instalmentDate is greater than SMALLER_INSTALMENT_DATE
        defaultPolicyShouldBeFound("instalmentDate.greaterThan=" + SMALLER_INSTALMENT_DATE);
    }

    @Test
    @Transactional
    void getAllPoliciesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where status equals to DEFAULT_STATUS
        defaultPolicyShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the policyList where status equals to UPDATED_STATUS
        defaultPolicyShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllPoliciesByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where status not equals to DEFAULT_STATUS
        defaultPolicyShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the policyList where status not equals to UPDATED_STATUS
        defaultPolicyShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllPoliciesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultPolicyShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the policyList where status equals to UPDATED_STATUS
        defaultPolicyShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllPoliciesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where status is not null
        defaultPolicyShouldBeFound("status.specified=true");

        // Get all the policyList where status is null
        defaultPolicyShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllPoliciesByDateStartIsEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where dateStart equals to DEFAULT_DATE_START
        defaultPolicyShouldBeFound("dateStart.equals=" + DEFAULT_DATE_START);

        // Get all the policyList where dateStart equals to UPDATED_DATE_START
        defaultPolicyShouldNotBeFound("dateStart.equals=" + UPDATED_DATE_START);
    }

    @Test
    @Transactional
    void getAllPoliciesByDateStartIsNotEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where dateStart not equals to DEFAULT_DATE_START
        defaultPolicyShouldNotBeFound("dateStart.notEquals=" + DEFAULT_DATE_START);

        // Get all the policyList where dateStart not equals to UPDATED_DATE_START
        defaultPolicyShouldBeFound("dateStart.notEquals=" + UPDATED_DATE_START);
    }

    @Test
    @Transactional
    void getAllPoliciesByDateStartIsInShouldWork() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where dateStart in DEFAULT_DATE_START or UPDATED_DATE_START
        defaultPolicyShouldBeFound("dateStart.in=" + DEFAULT_DATE_START + "," + UPDATED_DATE_START);

        // Get all the policyList where dateStart equals to UPDATED_DATE_START
        defaultPolicyShouldNotBeFound("dateStart.in=" + UPDATED_DATE_START);
    }

    @Test
    @Transactional
    void getAllPoliciesByDateStartIsNullOrNotNull() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where dateStart is not null
        defaultPolicyShouldBeFound("dateStart.specified=true");

        // Get all the policyList where dateStart is null
        defaultPolicyShouldNotBeFound("dateStart.specified=false");
    }

    @Test
    @Transactional
    void getAllPoliciesByDateEndIsEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where dateEnd equals to DEFAULT_DATE_END
        defaultPolicyShouldBeFound("dateEnd.equals=" + DEFAULT_DATE_END);

        // Get all the policyList where dateEnd equals to UPDATED_DATE_END
        defaultPolicyShouldNotBeFound("dateEnd.equals=" + UPDATED_DATE_END);
    }

    @Test
    @Transactional
    void getAllPoliciesByDateEndIsNotEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where dateEnd not equals to DEFAULT_DATE_END
        defaultPolicyShouldNotBeFound("dateEnd.notEquals=" + DEFAULT_DATE_END);

        // Get all the policyList where dateEnd not equals to UPDATED_DATE_END
        defaultPolicyShouldBeFound("dateEnd.notEquals=" + UPDATED_DATE_END);
    }

    @Test
    @Transactional
    void getAllPoliciesByDateEndIsInShouldWork() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where dateEnd in DEFAULT_DATE_END or UPDATED_DATE_END
        defaultPolicyShouldBeFound("dateEnd.in=" + DEFAULT_DATE_END + "," + UPDATED_DATE_END);

        // Get all the policyList where dateEnd equals to UPDATED_DATE_END
        defaultPolicyShouldNotBeFound("dateEnd.in=" + UPDATED_DATE_END);
    }

    @Test
    @Transactional
    void getAllPoliciesByDateEndIsNullOrNotNull() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where dateEnd is not null
        defaultPolicyShouldBeFound("dateEnd.specified=true");

        // Get all the policyList where dateEnd is null
        defaultPolicyShouldNotBeFound("dateEnd.specified=false");
    }

    @Test
    @Transactional
    void getAllPoliciesByMaturityDateIsEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where maturityDate equals to DEFAULT_MATURITY_DATE
        defaultPolicyShouldBeFound("maturityDate.equals=" + DEFAULT_MATURITY_DATE);

        // Get all the policyList where maturityDate equals to UPDATED_MATURITY_DATE
        defaultPolicyShouldNotBeFound("maturityDate.equals=" + UPDATED_MATURITY_DATE);
    }

    @Test
    @Transactional
    void getAllPoliciesByMaturityDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where maturityDate not equals to DEFAULT_MATURITY_DATE
        defaultPolicyShouldNotBeFound("maturityDate.notEquals=" + DEFAULT_MATURITY_DATE);

        // Get all the policyList where maturityDate not equals to UPDATED_MATURITY_DATE
        defaultPolicyShouldBeFound("maturityDate.notEquals=" + UPDATED_MATURITY_DATE);
    }

    @Test
    @Transactional
    void getAllPoliciesByMaturityDateIsInShouldWork() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where maturityDate in DEFAULT_MATURITY_DATE or UPDATED_MATURITY_DATE
        defaultPolicyShouldBeFound("maturityDate.in=" + DEFAULT_MATURITY_DATE + "," + UPDATED_MATURITY_DATE);

        // Get all the policyList where maturityDate equals to UPDATED_MATURITY_DATE
        defaultPolicyShouldNotBeFound("maturityDate.in=" + UPDATED_MATURITY_DATE);
    }

    @Test
    @Transactional
    void getAllPoliciesByMaturityDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where maturityDate is not null
        defaultPolicyShouldBeFound("maturityDate.specified=true");

        // Get all the policyList where maturityDate is null
        defaultPolicyShouldNotBeFound("maturityDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPoliciesByUinNoIsEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where uinNo equals to DEFAULT_UIN_NO
        defaultPolicyShouldBeFound("uinNo.equals=" + DEFAULT_UIN_NO);

        // Get all the policyList where uinNo equals to UPDATED_UIN_NO
        defaultPolicyShouldNotBeFound("uinNo.equals=" + UPDATED_UIN_NO);
    }

    @Test
    @Transactional
    void getAllPoliciesByUinNoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where uinNo not equals to DEFAULT_UIN_NO
        defaultPolicyShouldNotBeFound("uinNo.notEquals=" + DEFAULT_UIN_NO);

        // Get all the policyList where uinNo not equals to UPDATED_UIN_NO
        defaultPolicyShouldBeFound("uinNo.notEquals=" + UPDATED_UIN_NO);
    }

    @Test
    @Transactional
    void getAllPoliciesByUinNoIsInShouldWork() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where uinNo in DEFAULT_UIN_NO or UPDATED_UIN_NO
        defaultPolicyShouldBeFound("uinNo.in=" + DEFAULT_UIN_NO + "," + UPDATED_UIN_NO);

        // Get all the policyList where uinNo equals to UPDATED_UIN_NO
        defaultPolicyShouldNotBeFound("uinNo.in=" + UPDATED_UIN_NO);
    }

    @Test
    @Transactional
    void getAllPoliciesByUinNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where uinNo is not null
        defaultPolicyShouldBeFound("uinNo.specified=true");

        // Get all the policyList where uinNo is null
        defaultPolicyShouldNotBeFound("uinNo.specified=false");
    }

    @Test
    @Transactional
    void getAllPoliciesByUinNoContainsSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where uinNo contains DEFAULT_UIN_NO
        defaultPolicyShouldBeFound("uinNo.contains=" + DEFAULT_UIN_NO);

        // Get all the policyList where uinNo contains UPDATED_UIN_NO
        defaultPolicyShouldNotBeFound("uinNo.contains=" + UPDATED_UIN_NO);
    }

    @Test
    @Transactional
    void getAllPoliciesByUinNoNotContainsSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where uinNo does not contain DEFAULT_UIN_NO
        defaultPolicyShouldNotBeFound("uinNo.doesNotContain=" + DEFAULT_UIN_NO);

        // Get all the policyList where uinNo does not contain UPDATED_UIN_NO
        defaultPolicyShouldBeFound("uinNo.doesNotContain=" + UPDATED_UIN_NO);
    }

    @Test
    @Transactional
    void getAllPoliciesByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultPolicyShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the policyList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultPolicyShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllPoliciesByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultPolicyShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the policyList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultPolicyShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllPoliciesByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultPolicyShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the policyList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultPolicyShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllPoliciesByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where lastModified is not null
        defaultPolicyShouldBeFound("lastModified.specified=true");

        // Get all the policyList where lastModified is null
        defaultPolicyShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllPoliciesByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultPolicyShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the policyList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultPolicyShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllPoliciesByLastModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where lastModifiedBy not equals to DEFAULT_LAST_MODIFIED_BY
        defaultPolicyShouldNotBeFound("lastModifiedBy.notEquals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the policyList where lastModifiedBy not equals to UPDATED_LAST_MODIFIED_BY
        defaultPolicyShouldBeFound("lastModifiedBy.notEquals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllPoliciesByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultPolicyShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the policyList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultPolicyShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllPoliciesByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where lastModifiedBy is not null
        defaultPolicyShouldBeFound("lastModifiedBy.specified=true");

        // Get all the policyList where lastModifiedBy is null
        defaultPolicyShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllPoliciesByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultPolicyShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the policyList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultPolicyShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllPoliciesByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultPolicyShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the policyList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultPolicyShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllPoliciesByNomineeIsEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);
        Nominee nominee;
        if (TestUtil.findAll(em, Nominee.class).isEmpty()) {
            nominee = NomineeResourceIT.createEntity(em);
            em.persist(nominee);
            em.flush();
        } else {
            nominee = TestUtil.findAll(em, Nominee.class).get(0);
        }
        em.persist(nominee);
        em.flush();
        policy.addNominee(nominee);
        policyRepository.saveAndFlush(policy);
        Long nomineeId = nominee.getId();

        // Get all the policyList where nominee equals to nomineeId
        defaultPolicyShouldBeFound("nomineeId.equals=" + nomineeId);

        // Get all the policyList where nominee equals to (nomineeId + 1)
        defaultPolicyShouldNotBeFound("nomineeId.equals=" + (nomineeId + 1));
    }

    @Test
    @Transactional
    void getAllPoliciesByUsersIsEqualToSomething() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);
        Users users;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            users = UsersResourceIT.createEntity(em);
            em.persist(users);
            em.flush();
        } else {
            users = TestUtil.findAll(em, Users.class).get(0);
        }
        em.persist(users);
        em.flush();
        policy.setUsers(users);
        policyRepository.saveAndFlush(policy);
        Long usersId = users.getId();

        // Get all the policyList where users equals to usersId
        defaultPolicyShouldBeFound("usersId.equals=" + usersId);

        // Get all the policyList where users equals to (usersId + 1)
        defaultPolicyShouldNotBeFound("usersId.equals=" + (usersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPolicyShouldBeFound(String filter) throws Exception {
        restPolicyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(policy.getId().intValue())))
            .andExpect(jsonPath("$.[*].policyAmount").value(hasItem(DEFAULT_POLICY_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].instalmentAmount").value(hasItem(DEFAULT_INSTALMENT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].term").value(hasItem(DEFAULT_TERM.intValue())))
            .andExpect(jsonPath("$.[*].instalmentPeriod").value(hasItem(DEFAULT_INSTALMENT_PERIOD.intValue())))
            .andExpect(jsonPath("$.[*].instalmentDate").value(hasItem(DEFAULT_INSTALMENT_DATE.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].dateStart").value(hasItem(DEFAULT_DATE_START.toString())))
            .andExpect(jsonPath("$.[*].dateEnd").value(hasItem(DEFAULT_DATE_END.toString())))
            .andExpect(jsonPath("$.[*].maturityDate").value(hasItem(DEFAULT_MATURITY_DATE.toString())))
            .andExpect(jsonPath("$.[*].uinNo").value(hasItem(DEFAULT_UIN_NO)))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));

        // Check, that the count call also returns 1
        restPolicyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPolicyShouldNotBeFound(String filter) throws Exception {
        restPolicyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPolicyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPolicy() throws Exception {
        // Get the policy
        restPolicyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPolicy() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        int databaseSizeBeforeUpdate = policyRepository.findAll().size();

        // Update the policy
        Policy updatedPolicy = policyRepository.findById(policy.getId()).get();
        // Disconnect from session so that the updates on updatedPolicy are not directly saved in db
        em.detach(updatedPolicy);
        updatedPolicy
            .policyAmount(UPDATED_POLICY_AMOUNT)
            .instalmentAmount(UPDATED_INSTALMENT_AMOUNT)
            .term(UPDATED_TERM)
            .instalmentPeriod(UPDATED_INSTALMENT_PERIOD)
            .instalmentDate(UPDATED_INSTALMENT_DATE)
            .status(UPDATED_STATUS)
            .dateStart(UPDATED_DATE_START)
            .dateEnd(UPDATED_DATE_END)
            .maturityDate(UPDATED_MATURITY_DATE)
            .uinNo(UPDATED_UIN_NO)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        PolicyDTO policyDTO = policyMapper.toDto(updatedPolicy);

        restPolicyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, policyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(policyDTO))
            )
            .andExpect(status().isOk());

        // Validate the Policy in the database
        List<Policy> policyList = policyRepository.findAll();
        assertThat(policyList).hasSize(databaseSizeBeforeUpdate);
        Policy testPolicy = policyList.get(policyList.size() - 1);
        assertThat(testPolicy.getPolicyAmount()).isEqualTo(UPDATED_POLICY_AMOUNT);
        assertThat(testPolicy.getInstalmentAmount()).isEqualTo(UPDATED_INSTALMENT_AMOUNT);
        assertThat(testPolicy.getTerm()).isEqualTo(UPDATED_TERM);
        assertThat(testPolicy.getInstalmentPeriod()).isEqualTo(UPDATED_INSTALMENT_PERIOD);
        assertThat(testPolicy.getInstalmentDate()).isEqualTo(UPDATED_INSTALMENT_DATE);
        assertThat(testPolicy.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPolicy.getDateStart()).isEqualTo(UPDATED_DATE_START);
        assertThat(testPolicy.getDateEnd()).isEqualTo(UPDATED_DATE_END);
        assertThat(testPolicy.getMaturityDate()).isEqualTo(UPDATED_MATURITY_DATE);
        assertThat(testPolicy.getUinNo()).isEqualTo(UPDATED_UIN_NO);
        assertThat(testPolicy.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testPolicy.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void putNonExistingPolicy() throws Exception {
        int databaseSizeBeforeUpdate = policyRepository.findAll().size();
        policy.setId(count.incrementAndGet());

        // Create the Policy
        PolicyDTO policyDTO = policyMapper.toDto(policy);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPolicyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, policyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(policyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Policy in the database
        List<Policy> policyList = policyRepository.findAll();
        assertThat(policyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPolicy() throws Exception {
        int databaseSizeBeforeUpdate = policyRepository.findAll().size();
        policy.setId(count.incrementAndGet());

        // Create the Policy
        PolicyDTO policyDTO = policyMapper.toDto(policy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPolicyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(policyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Policy in the database
        List<Policy> policyList = policyRepository.findAll();
        assertThat(policyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPolicy() throws Exception {
        int databaseSizeBeforeUpdate = policyRepository.findAll().size();
        policy.setId(count.incrementAndGet());

        // Create the Policy
        PolicyDTO policyDTO = policyMapper.toDto(policy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPolicyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(policyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Policy in the database
        List<Policy> policyList = policyRepository.findAll();
        assertThat(policyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePolicyWithPatch() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        int databaseSizeBeforeUpdate = policyRepository.findAll().size();

        // Update the policy using partial update
        Policy partialUpdatedPolicy = new Policy();
        partialUpdatedPolicy.setId(policy.getId());

        partialUpdatedPolicy.term(UPDATED_TERM).dateEnd(UPDATED_DATE_END).uinNo(UPDATED_UIN_NO).lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restPolicyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPolicy.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPolicy))
            )
            .andExpect(status().isOk());

        // Validate the Policy in the database
        List<Policy> policyList = policyRepository.findAll();
        assertThat(policyList).hasSize(databaseSizeBeforeUpdate);
        Policy testPolicy = policyList.get(policyList.size() - 1);
        assertThat(testPolicy.getPolicyAmount()).isEqualTo(DEFAULT_POLICY_AMOUNT);
        assertThat(testPolicy.getInstalmentAmount()).isEqualTo(DEFAULT_INSTALMENT_AMOUNT);
        assertThat(testPolicy.getTerm()).isEqualTo(UPDATED_TERM);
        assertThat(testPolicy.getInstalmentPeriod()).isEqualTo(DEFAULT_INSTALMENT_PERIOD);
        assertThat(testPolicy.getInstalmentDate()).isEqualTo(DEFAULT_INSTALMENT_DATE);
        assertThat(testPolicy.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPolicy.getDateStart()).isEqualTo(DEFAULT_DATE_START);
        assertThat(testPolicy.getDateEnd()).isEqualTo(UPDATED_DATE_END);
        assertThat(testPolicy.getMaturityDate()).isEqualTo(DEFAULT_MATURITY_DATE);
        assertThat(testPolicy.getUinNo()).isEqualTo(UPDATED_UIN_NO);
        assertThat(testPolicy.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testPolicy.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void fullUpdatePolicyWithPatch() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        int databaseSizeBeforeUpdate = policyRepository.findAll().size();

        // Update the policy using partial update
        Policy partialUpdatedPolicy = new Policy();
        partialUpdatedPolicy.setId(policy.getId());

        partialUpdatedPolicy
            .policyAmount(UPDATED_POLICY_AMOUNT)
            .instalmentAmount(UPDATED_INSTALMENT_AMOUNT)
            .term(UPDATED_TERM)
            .instalmentPeriod(UPDATED_INSTALMENT_PERIOD)
            .instalmentDate(UPDATED_INSTALMENT_DATE)
            .status(UPDATED_STATUS)
            .dateStart(UPDATED_DATE_START)
            .dateEnd(UPDATED_DATE_END)
            .maturityDate(UPDATED_MATURITY_DATE)
            .uinNo(UPDATED_UIN_NO)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restPolicyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPolicy.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPolicy))
            )
            .andExpect(status().isOk());

        // Validate the Policy in the database
        List<Policy> policyList = policyRepository.findAll();
        assertThat(policyList).hasSize(databaseSizeBeforeUpdate);
        Policy testPolicy = policyList.get(policyList.size() - 1);
        assertThat(testPolicy.getPolicyAmount()).isEqualTo(UPDATED_POLICY_AMOUNT);
        assertThat(testPolicy.getInstalmentAmount()).isEqualTo(UPDATED_INSTALMENT_AMOUNT);
        assertThat(testPolicy.getTerm()).isEqualTo(UPDATED_TERM);
        assertThat(testPolicy.getInstalmentPeriod()).isEqualTo(UPDATED_INSTALMENT_PERIOD);
        assertThat(testPolicy.getInstalmentDate()).isEqualTo(UPDATED_INSTALMENT_DATE);
        assertThat(testPolicy.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPolicy.getDateStart()).isEqualTo(UPDATED_DATE_START);
        assertThat(testPolicy.getDateEnd()).isEqualTo(UPDATED_DATE_END);
        assertThat(testPolicy.getMaturityDate()).isEqualTo(UPDATED_MATURITY_DATE);
        assertThat(testPolicy.getUinNo()).isEqualTo(UPDATED_UIN_NO);
        assertThat(testPolicy.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testPolicy.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingPolicy() throws Exception {
        int databaseSizeBeforeUpdate = policyRepository.findAll().size();
        policy.setId(count.incrementAndGet());

        // Create the Policy
        PolicyDTO policyDTO = policyMapper.toDto(policy);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPolicyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, policyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(policyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Policy in the database
        List<Policy> policyList = policyRepository.findAll();
        assertThat(policyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPolicy() throws Exception {
        int databaseSizeBeforeUpdate = policyRepository.findAll().size();
        policy.setId(count.incrementAndGet());

        // Create the Policy
        PolicyDTO policyDTO = policyMapper.toDto(policy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPolicyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(policyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Policy in the database
        List<Policy> policyList = policyRepository.findAll();
        assertThat(policyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPolicy() throws Exception {
        int databaseSizeBeforeUpdate = policyRepository.findAll().size();
        policy.setId(count.incrementAndGet());

        // Create the Policy
        PolicyDTO policyDTO = policyMapper.toDto(policy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPolicyMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(policyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Policy in the database
        List<Policy> policyList = policyRepository.findAll();
        assertThat(policyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePolicy() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        int databaseSizeBeforeDelete = policyRepository.findAll().size();

        // Delete the policy
        restPolicyMockMvc
            .perform(delete(ENTITY_API_URL_ID, policy.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Policy> policyList = policyRepository.findAll();
        assertThat(policyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
