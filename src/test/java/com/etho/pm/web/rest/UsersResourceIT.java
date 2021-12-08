package com.etho.pm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.etho.pm.IntegrationTest;
import com.etho.pm.domain.Address;
import com.etho.pm.domain.Policy;
import com.etho.pm.domain.Users;
import com.etho.pm.domain.UsersType;
import com.etho.pm.domain.enumeration.StatusInd;
import com.etho.pm.repository.UsersRepository;
import com.etho.pm.service.criteria.UsersCriteria;
import com.etho.pm.service.dto.UsersDTO;
import com.etho.pm.service.mapper.UsersMapper;
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
 * Integration tests for the {@link UsersResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UsersResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_BIRTH_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BIRTH_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_USER_TYPE_ID = 1L;
    private static final Long UPDATED_USER_TYPE_ID = 2L;
    private static final Long SMALLER_USER_TYPE_ID = 1L - 1L;

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final StatusInd DEFAULT_STATUS = StatusInd.A;
    private static final StatusInd UPDATED_STATUS = StatusInd.I;

    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    private static final String DEFAULT_MOBILE_NO = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_NO = "BBBBBBBBBB";

    private static final String DEFAULT_ONE_TIME_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_ONE_TIME_PASSWORD = "BBBBBBBBBB";

    private static final Instant DEFAULT_OTP_EXPIRY_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_OTP_EXPIRY_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUsersMockMvc;

    private Users users;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Users createEntity(EntityManager em) {
        Users users = new Users()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .birthDate(DEFAULT_BIRTH_DATE)
            .userTypeId(DEFAULT_USER_TYPE_ID)
            .username(DEFAULT_USERNAME)
            .password(DEFAULT_PASSWORD)
            .email(DEFAULT_EMAIL)
            .imageUrl(DEFAULT_IMAGE_URL)
            .status(DEFAULT_STATUS)
            .activated(DEFAULT_ACTIVATED)
            .mobileNo(DEFAULT_MOBILE_NO)
            .oneTimePassword(DEFAULT_ONE_TIME_PASSWORD)
            .otpExpiryTime(DEFAULT_OTP_EXPIRY_TIME)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return users;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Users createUpdatedEntity(EntityManager em) {
        Users users = new Users()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .userTypeId(UPDATED_USER_TYPE_ID)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .email(UPDATED_EMAIL)
            .imageUrl(UPDATED_IMAGE_URL)
            .status(UPDATED_STATUS)
            .activated(UPDATED_ACTIVATED)
            .mobileNo(UPDATED_MOBILE_NO)
            .oneTimePassword(UPDATED_ONE_TIME_PASSWORD)
            .otpExpiryTime(UPDATED_OTP_EXPIRY_TIME)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        return users;
    }

    @BeforeEach
    public void initTest() {
        users = createEntity(em);
    }

    @Test
    @Transactional
    void createUsers() throws Exception {
        int databaseSizeBeforeCreate = usersRepository.findAll().size();
        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);
        restUsersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usersDTO)))
            .andExpect(status().isCreated());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeCreate + 1);
        Users testUsers = usersList.get(usersList.size() - 1);
        assertThat(testUsers.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testUsers.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testUsers.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testUsers.getUserTypeId()).isEqualTo(DEFAULT_USER_TYPE_ID);
        assertThat(testUsers.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testUsers.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testUsers.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUsers.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testUsers.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testUsers.getActivated()).isEqualTo(DEFAULT_ACTIVATED);
        assertThat(testUsers.getMobileNo()).isEqualTo(DEFAULT_MOBILE_NO);
        assertThat(testUsers.getOneTimePassword()).isEqualTo(DEFAULT_ONE_TIME_PASSWORD);
        assertThat(testUsers.getOtpExpiryTime()).isEqualTo(DEFAULT_OTP_EXPIRY_TIME);
        assertThat(testUsers.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testUsers.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void createUsersWithExistingId() throws Exception {
        // Create the Users with an existing ID
        users.setId(1L);
        UsersDTO usersDTO = usersMapper.toDto(users);

        int databaseSizeBeforeCreate = usersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUsersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usersDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBirthDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = usersRepository.findAll().size();
        // set the field null
        users.setBirthDate(null);

        // Create the Users, which fails.
        UsersDTO usersDTO = usersMapper.toDto(users);

        restUsersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usersDTO)))
            .andExpect(status().isBadRequest());

        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = usersRepository.findAll().size();
        // set the field null
        users.setUsername(null);

        // Create the Users, which fails.
        UsersDTO usersDTO = usersMapper.toDto(users);

        restUsersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usersDTO)))
            .andExpect(status().isBadRequest());

        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = usersRepository.findAll().size();
        // set the field null
        users.setPassword(null);

        // Create the Users, which fails.
        UsersDTO usersDTO = usersMapper.toDto(users);

        restUsersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usersDTO)))
            .andExpect(status().isBadRequest());

        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = usersRepository.findAll().size();
        // set the field null
        users.setActivated(null);

        // Create the Users, which fails.
        UsersDTO usersDTO = usersMapper.toDto(users);

        restUsersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usersDTO)))
            .andExpect(status().isBadRequest());

        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastModifiedIsRequired() throws Exception {
        int databaseSizeBeforeTest = usersRepository.findAll().size();
        // set the field null
        users.setLastModified(null);

        // Create the Users, which fails.
        UsersDTO usersDTO = usersMapper.toDto(users);

        restUsersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usersDTO)))
            .andExpect(status().isBadRequest());

        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastModifiedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = usersRepository.findAll().size();
        // set the field null
        users.setLastModifiedBy(null);

        // Create the Users, which fails.
        UsersDTO usersDTO = usersMapper.toDto(users);

        restUsersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usersDTO)))
            .andExpect(status().isBadRequest());

        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUsers() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList
        restUsersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(users.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].userTypeId").value(hasItem(DEFAULT_USER_TYPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].mobileNo").value(hasItem(DEFAULT_MOBILE_NO)))
            .andExpect(jsonPath("$.[*].oneTimePassword").value(hasItem(DEFAULT_ONE_TIME_PASSWORD)))
            .andExpect(jsonPath("$.[*].otpExpiryTime").value(hasItem(DEFAULT_OTP_EXPIRY_TIME.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
    }

    @Test
    @Transactional
    void getUsers() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get the users
        restUsersMockMvc
            .perform(get(ENTITY_API_URL_ID, users.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(users.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.userTypeId").value(DEFAULT_USER_TYPE_ID.intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.booleanValue()))
            .andExpect(jsonPath("$.mobileNo").value(DEFAULT_MOBILE_NO))
            .andExpect(jsonPath("$.oneTimePassword").value(DEFAULT_ONE_TIME_PASSWORD))
            .andExpect(jsonPath("$.otpExpiryTime").value(DEFAULT_OTP_EXPIRY_TIME.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY));
    }

    @Test
    @Transactional
    void getUsersByIdFiltering() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        Long id = users.getId();

        defaultUsersShouldBeFound("id.equals=" + id);
        defaultUsersShouldNotBeFound("id.notEquals=" + id);

        defaultUsersShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUsersShouldNotBeFound("id.greaterThan=" + id);

        defaultUsersShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUsersShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUsersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where firstName equals to DEFAULT_FIRST_NAME
        defaultUsersShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the usersList where firstName equals to UPDATED_FIRST_NAME
        defaultUsersShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where firstName not equals to DEFAULT_FIRST_NAME
        defaultUsersShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the usersList where firstName not equals to UPDATED_FIRST_NAME
        defaultUsersShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultUsersShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the usersList where firstName equals to UPDATED_FIRST_NAME
        defaultUsersShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where firstName is not null
        defaultUsersShouldBeFound("firstName.specified=true");

        // Get all the usersList where firstName is null
        defaultUsersShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where firstName contains DEFAULT_FIRST_NAME
        defaultUsersShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the usersList where firstName contains UPDATED_FIRST_NAME
        defaultUsersShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where firstName does not contain DEFAULT_FIRST_NAME
        defaultUsersShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the usersList where firstName does not contain UPDATED_FIRST_NAME
        defaultUsersShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where lastName equals to DEFAULT_LAST_NAME
        defaultUsersShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the usersList where lastName equals to UPDATED_LAST_NAME
        defaultUsersShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where lastName not equals to DEFAULT_LAST_NAME
        defaultUsersShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the usersList where lastName not equals to UPDATED_LAST_NAME
        defaultUsersShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultUsersShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the usersList where lastName equals to UPDATED_LAST_NAME
        defaultUsersShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where lastName is not null
        defaultUsersShouldBeFound("lastName.specified=true");

        // Get all the usersList where lastName is null
        defaultUsersShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where lastName contains DEFAULT_LAST_NAME
        defaultUsersShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the usersList where lastName contains UPDATED_LAST_NAME
        defaultUsersShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where lastName does not contain DEFAULT_LAST_NAME
        defaultUsersShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the usersList where lastName does not contain UPDATED_LAST_NAME
        defaultUsersShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByBirthDateIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where birthDate equals to DEFAULT_BIRTH_DATE
        defaultUsersShouldBeFound("birthDate.equals=" + DEFAULT_BIRTH_DATE);

        // Get all the usersList where birthDate equals to UPDATED_BIRTH_DATE
        defaultUsersShouldNotBeFound("birthDate.equals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllUsersByBirthDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where birthDate not equals to DEFAULT_BIRTH_DATE
        defaultUsersShouldNotBeFound("birthDate.notEquals=" + DEFAULT_BIRTH_DATE);

        // Get all the usersList where birthDate not equals to UPDATED_BIRTH_DATE
        defaultUsersShouldBeFound("birthDate.notEquals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllUsersByBirthDateIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where birthDate in DEFAULT_BIRTH_DATE or UPDATED_BIRTH_DATE
        defaultUsersShouldBeFound("birthDate.in=" + DEFAULT_BIRTH_DATE + "," + UPDATED_BIRTH_DATE);

        // Get all the usersList where birthDate equals to UPDATED_BIRTH_DATE
        defaultUsersShouldNotBeFound("birthDate.in=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllUsersByBirthDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where birthDate is not null
        defaultUsersShouldBeFound("birthDate.specified=true");

        // Get all the usersList where birthDate is null
        defaultUsersShouldNotBeFound("birthDate.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByUserTypeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where userTypeId equals to DEFAULT_USER_TYPE_ID
        defaultUsersShouldBeFound("userTypeId.equals=" + DEFAULT_USER_TYPE_ID);

        // Get all the usersList where userTypeId equals to UPDATED_USER_TYPE_ID
        defaultUsersShouldNotBeFound("userTypeId.equals=" + UPDATED_USER_TYPE_ID);
    }

    @Test
    @Transactional
    void getAllUsersByUserTypeIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where userTypeId not equals to DEFAULT_USER_TYPE_ID
        defaultUsersShouldNotBeFound("userTypeId.notEquals=" + DEFAULT_USER_TYPE_ID);

        // Get all the usersList where userTypeId not equals to UPDATED_USER_TYPE_ID
        defaultUsersShouldBeFound("userTypeId.notEquals=" + UPDATED_USER_TYPE_ID);
    }

    @Test
    @Transactional
    void getAllUsersByUserTypeIdIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where userTypeId in DEFAULT_USER_TYPE_ID or UPDATED_USER_TYPE_ID
        defaultUsersShouldBeFound("userTypeId.in=" + DEFAULT_USER_TYPE_ID + "," + UPDATED_USER_TYPE_ID);

        // Get all the usersList where userTypeId equals to UPDATED_USER_TYPE_ID
        defaultUsersShouldNotBeFound("userTypeId.in=" + UPDATED_USER_TYPE_ID);
    }

    @Test
    @Transactional
    void getAllUsersByUserTypeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where userTypeId is not null
        defaultUsersShouldBeFound("userTypeId.specified=true");

        // Get all the usersList where userTypeId is null
        defaultUsersShouldNotBeFound("userTypeId.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByUserTypeIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where userTypeId is greater than or equal to DEFAULT_USER_TYPE_ID
        defaultUsersShouldBeFound("userTypeId.greaterThanOrEqual=" + DEFAULT_USER_TYPE_ID);

        // Get all the usersList where userTypeId is greater than or equal to UPDATED_USER_TYPE_ID
        defaultUsersShouldNotBeFound("userTypeId.greaterThanOrEqual=" + UPDATED_USER_TYPE_ID);
    }

    @Test
    @Transactional
    void getAllUsersByUserTypeIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where userTypeId is less than or equal to DEFAULT_USER_TYPE_ID
        defaultUsersShouldBeFound("userTypeId.lessThanOrEqual=" + DEFAULT_USER_TYPE_ID);

        // Get all the usersList where userTypeId is less than or equal to SMALLER_USER_TYPE_ID
        defaultUsersShouldNotBeFound("userTypeId.lessThanOrEqual=" + SMALLER_USER_TYPE_ID);
    }

    @Test
    @Transactional
    void getAllUsersByUserTypeIdIsLessThanSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where userTypeId is less than DEFAULT_USER_TYPE_ID
        defaultUsersShouldNotBeFound("userTypeId.lessThan=" + DEFAULT_USER_TYPE_ID);

        // Get all the usersList where userTypeId is less than UPDATED_USER_TYPE_ID
        defaultUsersShouldBeFound("userTypeId.lessThan=" + UPDATED_USER_TYPE_ID);
    }

    @Test
    @Transactional
    void getAllUsersByUserTypeIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where userTypeId is greater than DEFAULT_USER_TYPE_ID
        defaultUsersShouldNotBeFound("userTypeId.greaterThan=" + DEFAULT_USER_TYPE_ID);

        // Get all the usersList where userTypeId is greater than SMALLER_USER_TYPE_ID
        defaultUsersShouldBeFound("userTypeId.greaterThan=" + SMALLER_USER_TYPE_ID);
    }

    @Test
    @Transactional
    void getAllUsersByUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where username equals to DEFAULT_USERNAME
        defaultUsersShouldBeFound("username.equals=" + DEFAULT_USERNAME);

        // Get all the usersList where username equals to UPDATED_USERNAME
        defaultUsersShouldNotBeFound("username.equals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllUsersByUsernameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where username not equals to DEFAULT_USERNAME
        defaultUsersShouldNotBeFound("username.notEquals=" + DEFAULT_USERNAME);

        // Get all the usersList where username not equals to UPDATED_USERNAME
        defaultUsersShouldBeFound("username.notEquals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllUsersByUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where username in DEFAULT_USERNAME or UPDATED_USERNAME
        defaultUsersShouldBeFound("username.in=" + DEFAULT_USERNAME + "," + UPDATED_USERNAME);

        // Get all the usersList where username equals to UPDATED_USERNAME
        defaultUsersShouldNotBeFound("username.in=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllUsersByUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where username is not null
        defaultUsersShouldBeFound("username.specified=true");

        // Get all the usersList where username is null
        defaultUsersShouldNotBeFound("username.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByUsernameContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where username contains DEFAULT_USERNAME
        defaultUsersShouldBeFound("username.contains=" + DEFAULT_USERNAME);

        // Get all the usersList where username contains UPDATED_USERNAME
        defaultUsersShouldNotBeFound("username.contains=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllUsersByUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where username does not contain DEFAULT_USERNAME
        defaultUsersShouldNotBeFound("username.doesNotContain=" + DEFAULT_USERNAME);

        // Get all the usersList where username does not contain UPDATED_USERNAME
        defaultUsersShouldBeFound("username.doesNotContain=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllUsersByPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where password equals to DEFAULT_PASSWORD
        defaultUsersShouldBeFound("password.equals=" + DEFAULT_PASSWORD);

        // Get all the usersList where password equals to UPDATED_PASSWORD
        defaultUsersShouldNotBeFound("password.equals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUsersByPasswordIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where password not equals to DEFAULT_PASSWORD
        defaultUsersShouldNotBeFound("password.notEquals=" + DEFAULT_PASSWORD);

        // Get all the usersList where password not equals to UPDATED_PASSWORD
        defaultUsersShouldBeFound("password.notEquals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUsersByPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where password in DEFAULT_PASSWORD or UPDATED_PASSWORD
        defaultUsersShouldBeFound("password.in=" + DEFAULT_PASSWORD + "," + UPDATED_PASSWORD);

        // Get all the usersList where password equals to UPDATED_PASSWORD
        defaultUsersShouldNotBeFound("password.in=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUsersByPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where password is not null
        defaultUsersShouldBeFound("password.specified=true");

        // Get all the usersList where password is null
        defaultUsersShouldNotBeFound("password.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByPasswordContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where password contains DEFAULT_PASSWORD
        defaultUsersShouldBeFound("password.contains=" + DEFAULT_PASSWORD);

        // Get all the usersList where password contains UPDATED_PASSWORD
        defaultUsersShouldNotBeFound("password.contains=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUsersByPasswordNotContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where password does not contain DEFAULT_PASSWORD
        defaultUsersShouldNotBeFound("password.doesNotContain=" + DEFAULT_PASSWORD);

        // Get all the usersList where password does not contain UPDATED_PASSWORD
        defaultUsersShouldBeFound("password.doesNotContain=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUsersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where email equals to DEFAULT_EMAIL
        defaultUsersShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the usersList where email equals to UPDATED_EMAIL
        defaultUsersShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsersByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where email not equals to DEFAULT_EMAIL
        defaultUsersShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the usersList where email not equals to UPDATED_EMAIL
        defaultUsersShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultUsersShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the usersList where email equals to UPDATED_EMAIL
        defaultUsersShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where email is not null
        defaultUsersShouldBeFound("email.specified=true");

        // Get all the usersList where email is null
        defaultUsersShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByEmailContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where email contains DEFAULT_EMAIL
        defaultUsersShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the usersList where email contains UPDATED_EMAIL
        defaultUsersShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where email does not contain DEFAULT_EMAIL
        defaultUsersShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the usersList where email does not contain UPDATED_EMAIL
        defaultUsersShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsersByImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where imageUrl equals to DEFAULT_IMAGE_URL
        defaultUsersShouldBeFound("imageUrl.equals=" + DEFAULT_IMAGE_URL);

        // Get all the usersList where imageUrl equals to UPDATED_IMAGE_URL
        defaultUsersShouldNotBeFound("imageUrl.equals=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllUsersByImageUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where imageUrl not equals to DEFAULT_IMAGE_URL
        defaultUsersShouldNotBeFound("imageUrl.notEquals=" + DEFAULT_IMAGE_URL);

        // Get all the usersList where imageUrl not equals to UPDATED_IMAGE_URL
        defaultUsersShouldBeFound("imageUrl.notEquals=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllUsersByImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where imageUrl in DEFAULT_IMAGE_URL or UPDATED_IMAGE_URL
        defaultUsersShouldBeFound("imageUrl.in=" + DEFAULT_IMAGE_URL + "," + UPDATED_IMAGE_URL);

        // Get all the usersList where imageUrl equals to UPDATED_IMAGE_URL
        defaultUsersShouldNotBeFound("imageUrl.in=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllUsersByImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where imageUrl is not null
        defaultUsersShouldBeFound("imageUrl.specified=true");

        // Get all the usersList where imageUrl is null
        defaultUsersShouldNotBeFound("imageUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByImageUrlContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where imageUrl contains DEFAULT_IMAGE_URL
        defaultUsersShouldBeFound("imageUrl.contains=" + DEFAULT_IMAGE_URL);

        // Get all the usersList where imageUrl contains UPDATED_IMAGE_URL
        defaultUsersShouldNotBeFound("imageUrl.contains=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllUsersByImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where imageUrl does not contain DEFAULT_IMAGE_URL
        defaultUsersShouldNotBeFound("imageUrl.doesNotContain=" + DEFAULT_IMAGE_URL);

        // Get all the usersList where imageUrl does not contain UPDATED_IMAGE_URL
        defaultUsersShouldBeFound("imageUrl.doesNotContain=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllUsersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where status equals to DEFAULT_STATUS
        defaultUsersShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the usersList where status equals to UPDATED_STATUS
        defaultUsersShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllUsersByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where status not equals to DEFAULT_STATUS
        defaultUsersShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the usersList where status not equals to UPDATED_STATUS
        defaultUsersShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllUsersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultUsersShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the usersList where status equals to UPDATED_STATUS
        defaultUsersShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllUsersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where status is not null
        defaultUsersShouldBeFound("status.specified=true");

        // Get all the usersList where status is null
        defaultUsersShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByActivatedIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where activated equals to DEFAULT_ACTIVATED
        defaultUsersShouldBeFound("activated.equals=" + DEFAULT_ACTIVATED);

        // Get all the usersList where activated equals to UPDATED_ACTIVATED
        defaultUsersShouldNotBeFound("activated.equals=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    void getAllUsersByActivatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where activated not equals to DEFAULT_ACTIVATED
        defaultUsersShouldNotBeFound("activated.notEquals=" + DEFAULT_ACTIVATED);

        // Get all the usersList where activated not equals to UPDATED_ACTIVATED
        defaultUsersShouldBeFound("activated.notEquals=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    void getAllUsersByActivatedIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where activated in DEFAULT_ACTIVATED or UPDATED_ACTIVATED
        defaultUsersShouldBeFound("activated.in=" + DEFAULT_ACTIVATED + "," + UPDATED_ACTIVATED);

        // Get all the usersList where activated equals to UPDATED_ACTIVATED
        defaultUsersShouldNotBeFound("activated.in=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    void getAllUsersByActivatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where activated is not null
        defaultUsersShouldBeFound("activated.specified=true");

        // Get all the usersList where activated is null
        defaultUsersShouldNotBeFound("activated.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByMobileNoIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where mobileNo equals to DEFAULT_MOBILE_NO
        defaultUsersShouldBeFound("mobileNo.equals=" + DEFAULT_MOBILE_NO);

        // Get all the usersList where mobileNo equals to UPDATED_MOBILE_NO
        defaultUsersShouldNotBeFound("mobileNo.equals=" + UPDATED_MOBILE_NO);
    }

    @Test
    @Transactional
    void getAllUsersByMobileNoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where mobileNo not equals to DEFAULT_MOBILE_NO
        defaultUsersShouldNotBeFound("mobileNo.notEquals=" + DEFAULT_MOBILE_NO);

        // Get all the usersList where mobileNo not equals to UPDATED_MOBILE_NO
        defaultUsersShouldBeFound("mobileNo.notEquals=" + UPDATED_MOBILE_NO);
    }

    @Test
    @Transactional
    void getAllUsersByMobileNoIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where mobileNo in DEFAULT_MOBILE_NO or UPDATED_MOBILE_NO
        defaultUsersShouldBeFound("mobileNo.in=" + DEFAULT_MOBILE_NO + "," + UPDATED_MOBILE_NO);

        // Get all the usersList where mobileNo equals to UPDATED_MOBILE_NO
        defaultUsersShouldNotBeFound("mobileNo.in=" + UPDATED_MOBILE_NO);
    }

    @Test
    @Transactional
    void getAllUsersByMobileNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where mobileNo is not null
        defaultUsersShouldBeFound("mobileNo.specified=true");

        // Get all the usersList where mobileNo is null
        defaultUsersShouldNotBeFound("mobileNo.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByMobileNoContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where mobileNo contains DEFAULT_MOBILE_NO
        defaultUsersShouldBeFound("mobileNo.contains=" + DEFAULT_MOBILE_NO);

        // Get all the usersList where mobileNo contains UPDATED_MOBILE_NO
        defaultUsersShouldNotBeFound("mobileNo.contains=" + UPDATED_MOBILE_NO);
    }

    @Test
    @Transactional
    void getAllUsersByMobileNoNotContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where mobileNo does not contain DEFAULT_MOBILE_NO
        defaultUsersShouldNotBeFound("mobileNo.doesNotContain=" + DEFAULT_MOBILE_NO);

        // Get all the usersList where mobileNo does not contain UPDATED_MOBILE_NO
        defaultUsersShouldBeFound("mobileNo.doesNotContain=" + UPDATED_MOBILE_NO);
    }

    @Test
    @Transactional
    void getAllUsersByOneTimePasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where oneTimePassword equals to DEFAULT_ONE_TIME_PASSWORD
        defaultUsersShouldBeFound("oneTimePassword.equals=" + DEFAULT_ONE_TIME_PASSWORD);

        // Get all the usersList where oneTimePassword equals to UPDATED_ONE_TIME_PASSWORD
        defaultUsersShouldNotBeFound("oneTimePassword.equals=" + UPDATED_ONE_TIME_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUsersByOneTimePasswordIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where oneTimePassword not equals to DEFAULT_ONE_TIME_PASSWORD
        defaultUsersShouldNotBeFound("oneTimePassword.notEquals=" + DEFAULT_ONE_TIME_PASSWORD);

        // Get all the usersList where oneTimePassword not equals to UPDATED_ONE_TIME_PASSWORD
        defaultUsersShouldBeFound("oneTimePassword.notEquals=" + UPDATED_ONE_TIME_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUsersByOneTimePasswordIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where oneTimePassword in DEFAULT_ONE_TIME_PASSWORD or UPDATED_ONE_TIME_PASSWORD
        defaultUsersShouldBeFound("oneTimePassword.in=" + DEFAULT_ONE_TIME_PASSWORD + "," + UPDATED_ONE_TIME_PASSWORD);

        // Get all the usersList where oneTimePassword equals to UPDATED_ONE_TIME_PASSWORD
        defaultUsersShouldNotBeFound("oneTimePassword.in=" + UPDATED_ONE_TIME_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUsersByOneTimePasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where oneTimePassword is not null
        defaultUsersShouldBeFound("oneTimePassword.specified=true");

        // Get all the usersList where oneTimePassword is null
        defaultUsersShouldNotBeFound("oneTimePassword.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByOneTimePasswordContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where oneTimePassword contains DEFAULT_ONE_TIME_PASSWORD
        defaultUsersShouldBeFound("oneTimePassword.contains=" + DEFAULT_ONE_TIME_PASSWORD);

        // Get all the usersList where oneTimePassword contains UPDATED_ONE_TIME_PASSWORD
        defaultUsersShouldNotBeFound("oneTimePassword.contains=" + UPDATED_ONE_TIME_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUsersByOneTimePasswordNotContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where oneTimePassword does not contain DEFAULT_ONE_TIME_PASSWORD
        defaultUsersShouldNotBeFound("oneTimePassword.doesNotContain=" + DEFAULT_ONE_TIME_PASSWORD);

        // Get all the usersList where oneTimePassword does not contain UPDATED_ONE_TIME_PASSWORD
        defaultUsersShouldBeFound("oneTimePassword.doesNotContain=" + UPDATED_ONE_TIME_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUsersByOtpExpiryTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where otpExpiryTime equals to DEFAULT_OTP_EXPIRY_TIME
        defaultUsersShouldBeFound("otpExpiryTime.equals=" + DEFAULT_OTP_EXPIRY_TIME);

        // Get all the usersList where otpExpiryTime equals to UPDATED_OTP_EXPIRY_TIME
        defaultUsersShouldNotBeFound("otpExpiryTime.equals=" + UPDATED_OTP_EXPIRY_TIME);
    }

    @Test
    @Transactional
    void getAllUsersByOtpExpiryTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where otpExpiryTime not equals to DEFAULT_OTP_EXPIRY_TIME
        defaultUsersShouldNotBeFound("otpExpiryTime.notEquals=" + DEFAULT_OTP_EXPIRY_TIME);

        // Get all the usersList where otpExpiryTime not equals to UPDATED_OTP_EXPIRY_TIME
        defaultUsersShouldBeFound("otpExpiryTime.notEquals=" + UPDATED_OTP_EXPIRY_TIME);
    }

    @Test
    @Transactional
    void getAllUsersByOtpExpiryTimeIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where otpExpiryTime in DEFAULT_OTP_EXPIRY_TIME or UPDATED_OTP_EXPIRY_TIME
        defaultUsersShouldBeFound("otpExpiryTime.in=" + DEFAULT_OTP_EXPIRY_TIME + "," + UPDATED_OTP_EXPIRY_TIME);

        // Get all the usersList where otpExpiryTime equals to UPDATED_OTP_EXPIRY_TIME
        defaultUsersShouldNotBeFound("otpExpiryTime.in=" + UPDATED_OTP_EXPIRY_TIME);
    }

    @Test
    @Transactional
    void getAllUsersByOtpExpiryTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where otpExpiryTime is not null
        defaultUsersShouldBeFound("otpExpiryTime.specified=true");

        // Get all the usersList where otpExpiryTime is null
        defaultUsersShouldNotBeFound("otpExpiryTime.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultUsersShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the usersList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultUsersShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllUsersByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultUsersShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the usersList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultUsersShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllUsersByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultUsersShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the usersList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultUsersShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllUsersByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where lastModified is not null
        defaultUsersShouldBeFound("lastModified.specified=true");

        // Get all the usersList where lastModified is null
        defaultUsersShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultUsersShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the usersList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultUsersShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllUsersByLastModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where lastModifiedBy not equals to DEFAULT_LAST_MODIFIED_BY
        defaultUsersShouldNotBeFound("lastModifiedBy.notEquals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the usersList where lastModifiedBy not equals to UPDATED_LAST_MODIFIED_BY
        defaultUsersShouldBeFound("lastModifiedBy.notEquals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllUsersByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultUsersShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the usersList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultUsersShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllUsersByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where lastModifiedBy is not null
        defaultUsersShouldBeFound("lastModifiedBy.specified=true");

        // Get all the usersList where lastModifiedBy is null
        defaultUsersShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultUsersShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the usersList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultUsersShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllUsersByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultUsersShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the usersList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultUsersShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllUsersByUsersTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);
        UsersType usersType;
        if (TestUtil.findAll(em, UsersType.class).isEmpty()) {
            usersType = UsersTypeResourceIT.createEntity(em);
            em.persist(usersType);
            em.flush();
        } else {
            usersType = TestUtil.findAll(em, UsersType.class).get(0);
        }
        em.persist(usersType);
        em.flush();
        users.setUsersType(usersType);
        usersRepository.saveAndFlush(users);
        Long usersTypeId = usersType.getId();

        // Get all the usersList where usersType equals to usersTypeId
        defaultUsersShouldBeFound("usersTypeId.equals=" + usersTypeId);

        // Get all the usersList where usersType equals to (usersTypeId + 1)
        defaultUsersShouldNotBeFound("usersTypeId.equals=" + (usersTypeId + 1));
    }

    @Test
    @Transactional
    void getAllUsersByPolicyIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);
        Policy policy;
        if (TestUtil.findAll(em, Policy.class).isEmpty()) {
            policy = PolicyResourceIT.createEntity(em);
            em.persist(policy);
            em.flush();
        } else {
            policy = TestUtil.findAll(em, Policy.class).get(0);
        }
        em.persist(policy);
        em.flush();
        users.addPolicy(policy);
        usersRepository.saveAndFlush(users);
        Long policyId = policy.getId();

        // Get all the usersList where policy equals to policyId
        defaultUsersShouldBeFound("policyId.equals=" + policyId);

        // Get all the usersList where policy equals to (policyId + 1)
        defaultUsersShouldNotBeFound("policyId.equals=" + (policyId + 1));
    }

    @Test
    @Transactional
    void getAllUsersByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);
        Address address;
        if (TestUtil.findAll(em, Address.class).isEmpty()) {
            address = AddressResourceIT.createEntity(em);
            em.persist(address);
            em.flush();
        } else {
            address = TestUtil.findAll(em, Address.class).get(0);
        }
        em.persist(address);
        em.flush();
        users.addAddress(address);
        usersRepository.saveAndFlush(users);
        Long addressId = address.getId();

        // Get all the usersList where address equals to addressId
        defaultUsersShouldBeFound("addressId.equals=" + addressId);

        // Get all the usersList where address equals to (addressId + 1)
        defaultUsersShouldNotBeFound("addressId.equals=" + (addressId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUsersShouldBeFound(String filter) throws Exception {
        restUsersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(users.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].userTypeId").value(hasItem(DEFAULT_USER_TYPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].mobileNo").value(hasItem(DEFAULT_MOBILE_NO)))
            .andExpect(jsonPath("$.[*].oneTimePassword").value(hasItem(DEFAULT_ONE_TIME_PASSWORD)))
            .andExpect(jsonPath("$.[*].otpExpiryTime").value(hasItem(DEFAULT_OTP_EXPIRY_TIME.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));

        // Check, that the count call also returns 1
        restUsersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUsersShouldNotBeFound(String filter) throws Exception {
        restUsersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUsersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUsers() throws Exception {
        // Get the users
        restUsersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUsers() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        int databaseSizeBeforeUpdate = usersRepository.findAll().size();

        // Update the users
        Users updatedUsers = usersRepository.findById(users.getId()).get();
        // Disconnect from session so that the updates on updatedUsers are not directly saved in db
        em.detach(updatedUsers);
        updatedUsers
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .userTypeId(UPDATED_USER_TYPE_ID)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .email(UPDATED_EMAIL)
            .imageUrl(UPDATED_IMAGE_URL)
            .status(UPDATED_STATUS)
            .activated(UPDATED_ACTIVATED)
            .mobileNo(UPDATED_MOBILE_NO)
            .oneTimePassword(UPDATED_ONE_TIME_PASSWORD)
            .otpExpiryTime(UPDATED_OTP_EXPIRY_TIME)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        UsersDTO usersDTO = usersMapper.toDto(updatedUsers);

        restUsersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usersDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usersDTO))
            )
            .andExpect(status().isOk());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
        Users testUsers = usersList.get(usersList.size() - 1);
        assertThat(testUsers.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testUsers.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUsers.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testUsers.getUserTypeId()).isEqualTo(UPDATED_USER_TYPE_ID);
        assertThat(testUsers.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testUsers.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testUsers.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUsers.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testUsers.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUsers.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testUsers.getMobileNo()).isEqualTo(UPDATED_MOBILE_NO);
        assertThat(testUsers.getOneTimePassword()).isEqualTo(UPDATED_ONE_TIME_PASSWORD);
        assertThat(testUsers.getOtpExpiryTime()).isEqualTo(UPDATED_OTP_EXPIRY_TIME);
        assertThat(testUsers.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testUsers.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void putNonExistingUsers() throws Exception {
        int databaseSizeBeforeUpdate = usersRepository.findAll().size();
        users.setId(count.incrementAndGet());

        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usersDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUsers() throws Exception {
        int databaseSizeBeforeUpdate = usersRepository.findAll().size();
        users.setId(count.incrementAndGet());

        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUsers() throws Exception {
        int databaseSizeBeforeUpdate = usersRepository.findAll().size();
        users.setId(count.incrementAndGet());

        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usersDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUsersWithPatch() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        int databaseSizeBeforeUpdate = usersRepository.findAll().size();

        // Update the users using partial update
        Users partialUpdatedUsers = new Users();
        partialUpdatedUsers.setId(users.getId());

        partialUpdatedUsers
            .lastName(UPDATED_LAST_NAME)
            .userTypeId(UPDATED_USER_TYPE_ID)
            .password(UPDATED_PASSWORD)
            .email(UPDATED_EMAIL)
            .imageUrl(UPDATED_IMAGE_URL)
            .activated(UPDATED_ACTIVATED)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsers))
            )
            .andExpect(status().isOk());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
        Users testUsers = usersList.get(usersList.size() - 1);
        assertThat(testUsers.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testUsers.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUsers.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testUsers.getUserTypeId()).isEqualTo(UPDATED_USER_TYPE_ID);
        assertThat(testUsers.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testUsers.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testUsers.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUsers.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testUsers.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testUsers.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testUsers.getMobileNo()).isEqualTo(DEFAULT_MOBILE_NO);
        assertThat(testUsers.getOneTimePassword()).isEqualTo(DEFAULT_ONE_TIME_PASSWORD);
        assertThat(testUsers.getOtpExpiryTime()).isEqualTo(DEFAULT_OTP_EXPIRY_TIME);
        assertThat(testUsers.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testUsers.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void fullUpdateUsersWithPatch() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        int databaseSizeBeforeUpdate = usersRepository.findAll().size();

        // Update the users using partial update
        Users partialUpdatedUsers = new Users();
        partialUpdatedUsers.setId(users.getId());

        partialUpdatedUsers
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .userTypeId(UPDATED_USER_TYPE_ID)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .email(UPDATED_EMAIL)
            .imageUrl(UPDATED_IMAGE_URL)
            .status(UPDATED_STATUS)
            .activated(UPDATED_ACTIVATED)
            .mobileNo(UPDATED_MOBILE_NO)
            .oneTimePassword(UPDATED_ONE_TIME_PASSWORD)
            .otpExpiryTime(UPDATED_OTP_EXPIRY_TIME)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsers))
            )
            .andExpect(status().isOk());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
        Users testUsers = usersList.get(usersList.size() - 1);
        assertThat(testUsers.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testUsers.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUsers.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testUsers.getUserTypeId()).isEqualTo(UPDATED_USER_TYPE_ID);
        assertThat(testUsers.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testUsers.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testUsers.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUsers.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testUsers.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUsers.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testUsers.getMobileNo()).isEqualTo(UPDATED_MOBILE_NO);
        assertThat(testUsers.getOneTimePassword()).isEqualTo(UPDATED_ONE_TIME_PASSWORD);
        assertThat(testUsers.getOtpExpiryTime()).isEqualTo(UPDATED_OTP_EXPIRY_TIME);
        assertThat(testUsers.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testUsers.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingUsers() throws Exception {
        int databaseSizeBeforeUpdate = usersRepository.findAll().size();
        users.setId(count.incrementAndGet());

        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, usersDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUsers() throws Exception {
        int databaseSizeBeforeUpdate = usersRepository.findAll().size();
        users.setId(count.incrementAndGet());

        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUsers() throws Exception {
        int databaseSizeBeforeUpdate = usersRepository.findAll().size();
        users.setId(count.incrementAndGet());

        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(usersDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUsers() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        int databaseSizeBeforeDelete = usersRepository.findAll().size();

        // Delete the users
        restUsersMockMvc
            .perform(delete(ENTITY_API_URL_ID, users.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
