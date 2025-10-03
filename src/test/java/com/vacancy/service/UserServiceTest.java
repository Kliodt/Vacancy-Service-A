package com.vacancy.service;

import com.vacancy.exceptions.RequestException;
import com.vacancy.model.dto.UserDto;
import com.vacancy.model.dto.UserVacancyResponseDto;
import com.vacancy.model.entities.Organization;
import com.vacancy.model.entities.User;
import com.vacancy.model.entities.UserVacancyResponse;
import com.vacancy.model.entities.Vacancy;
import com.vacancy.repository.OrganizationRepository;
import com.vacancy.repository.UserRepository;
import com.vacancy.repository.UserVacancyResponseRepository;
import com.vacancy.repository.VacancyRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.Page;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceTest {

    @LocalServerPort
    private Integer port;
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17");

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    VacancyRepository vacancyRepository;
    @Autowired
    OrganizationRepository organizationRepository;
    @Autowired
    UserVacancyResponseRepository responseRepository;

    private User testUser;
    private Vacancy testVacancy;
    private Organization testOrganization;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.url", postgres::getJdbcUrl);
        registry.add("spring.flyway.user", postgres::getUsername);
        registry.add("spring.flyway.password", postgres::getPassword);
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        responseRepository.deleteAll();
        vacancyRepository.deleteAll();
        userRepository.deleteAll();
        organizationRepository.deleteAll();

        testOrganization = new Organization("TestOrg", "org@example.com");
        testOrganization = organizationRepository.save(testOrganization);

        testUser = new User("testUser", "test@example.com");
        testUser.setCvLink("http://cv.example.com");
        testUser = userRepository.save(testUser);

        testVacancy = new Vacancy("Java Developer", "Java Developer position");
        testVacancy.setOrganization(testOrganization);
        testVacancy = vacancyRepository.save(testVacancy);
    }

    @Test
    void getAllUsersTest() {
        userRepository.save(new User("user1", "user1@example.com"));
        userRepository.save(new User("user2", "user2@example.com"));

        Page<User> result = userService.getAllUsers(0, 2);

        assertEquals(2, result.getContent().size());
        assertEquals(3, result.getTotalElements());
    }

    @Test
    void getAllUsers_shouldLimitPageSize() {
        Page<User> result = userService.getAllUsers(0, 100);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void getUserByIdTest() {
        User result = userService.getUserById(testUser.getId());
        
        assertEquals(testUser.getId(), result.getId());
        assertEquals("testUser", result.getNickname());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void getUserById_shouldThrowException_whenNotFound() {
        assertThrows(RequestException.class, () -> {
            userService.getUserById(999L);
        });
    }

    @Test
    void createUserTest() {
        User tempUser = new User("newUser", "new@example.com");
        tempUser.setCvLink("http://newcv.example.com");
        UserDto userDto = new UserDto(tempUser);

        User result = userService.createUser(userDto);

        assertTrue(result.getId() > 0);
        assertEquals("newUser", result.getNickname());
        assertEquals("new@example.com", result.getEmail());
    }

    @Test
    void createUser_shouldThrowException_whenEmailExists() {
        User tempUser = new User("anotherUser", "test@example.com");
        UserDto userDto = new UserDto(tempUser);

        assertThrows(RequestException.class, () -> {
            userService.createUser(userDto);
        });
    }

    @Test
    void updateUserTest() {
        User tempUser = new User("updatedUser", "updated@example.com");
        tempUser.setCvLink("http://updated.example.com");
        UserDto userDto = new UserDto(tempUser);

        User result = userService.updateUser(testUser.getId(), userDto);

        assertEquals("updatedUser", result.getNickname());
        assertEquals("updated@example.com", result.getEmail());
        assertEquals("http://updated.example.com", result.getCvLink());
    }

    @Test
    void updateUser_shouldThrowException_whenNotFound() {
        User tempUser = new User("updatedUser", "updated@example.com");
        UserDto userDto = new UserDto(tempUser);

        assertThrows(RequestException.class, () -> {
            userService.updateUser(999L, userDto);
        });
    }

    @Test
    void deleteUserTest() {
        Long userId = testUser.getId();
        
        userService.deleteUser(userId);

        assertFalse(userRepository.existsById(userId));
    }

    @Test
    void getUserFavoritesTest() {
        testUser.getFavoriteList().add(testVacancy);
        userRepository.save(testUser);

        List<Vacancy> result = userService.getUserFavorites(testUser.getId());

        assertEquals(1, result.size());
        assertEquals(testVacancy.getId(), result.get(0).getId());
    }

    @Test
    void getUserFavorites_shouldReturnEmpty_whenNoFavorites() {
        List<Vacancy> result = userService.getUserFavorites(testUser.getId());
        
        assertTrue(result.isEmpty());
    }

    @Test
    void getUserResponsesTest() {
        UserVacancyResponse response = new UserVacancyResponse(testUser, testVacancy);
        responseRepository.save(response);

        List<UserVacancyResponseDto> result = userService.getUserResponses(testUser.getId());

        assertEquals(1, result.size());
        assertEquals(testUser.getId(), result.get(0).getUserId());
        assertEquals(testVacancy.getId(), result.get(0).getVacancyId());
    }

    @Test
    void getUserResponses_shouldReturnEmpty_whenNoResponses() {
        List<UserVacancyResponseDto> result = userService.getUserResponses(testUser.getId());
        
        assertTrue(result.isEmpty());
    }
}