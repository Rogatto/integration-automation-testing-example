package com.staxrt.tutorial;

import com.github.javafaker.Faker;
import com.staxrt.tutorial.model.User;
import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import java.util.Objects;
import java.util.logging.Logger;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {TestRunnerSuite.Initializer.class})
@Epic("API xyz")
@Feature("Users")
public class ApplicationTests {

	private static final Logger logger = Logger.getLogger(ApplicationTests.class.getName());

	@Autowired
	public TestRestTemplate restTemplate;

	@LocalServerPort
	public int port;

	public String getRootUrl() {
		return "http://localhost:" + port + "/api/v1";
	}

	private User createUser(){

		Faker faker = new Faker();
		String firstName = faker.name().firstName();
		String lastName = faker.name().lastName();
		String email = faker.name().username() + "@testeeeeeee.com";

		User user = new User();
		user.setEmail(email);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setCreatedBy(firstName);
		user.setUpdatedBy(firstName);

		return user;
	}

	@Test
	@DisplayName("GET ALL Users")
	@Description("Let's test our GET List all users")
	@Severity(SeverityLevel.MINOR)
	public void testGetAllUsers() {

		User user = createUser();
		restTemplate.postForEntity(getRootUrl() + "/users", user, User.class);

		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/users",
				HttpMethod.GET, entity, String.class);


		Allure.addAttachment("Get all users response body", Objects.requireNonNull(response.getBody()));
		Allure.addAttachment("Get all users status code", String.valueOf(response.getStatusCode()));

		logger.info(response.getBody());
		logger.info(String.valueOf(response.getStatusCode()));

		Assert.assertTrue(Objects.requireNonNull(response.getBody()).contains(user.getFirstName()));
		Assert.assertTrue(response.getStatusCode().is2xxSuccessful());

	}

	@Test
	@DisplayName("GET User by Id")
	@Description("Let's test our GET by id user")
	@Severity(SeverityLevel.BLOCKER)
	public void testGetUserById() {

		User user = createUser();
		ResponseEntity<User> postResponse = restTemplate.postForEntity(getRootUrl() + "/users", user, User.class);

		logger.info(String.valueOf(postResponse.getBody()));

		Allure.addAttachment("Get user by id response body", String.valueOf(postResponse.getBody()));

		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<>(null, headers);
		ResponseEntity<User> response = restTemplate.exchange(getRootUrl() + "/users/" + postResponse.getBody().getId(),
				HttpMethod.GET, entity, User.class);

		Assert.assertEquals(Objects.requireNonNull(response.getBody()).getFirstName(), user.getFirstName());
		Assert.assertEquals(response.getBody().getLastName(), user.getLastName());
		Assert.assertEquals(response.getBody().getEmail(), user.getEmail());
		Assert.assertEquals(response.getBody().getCreatedBy(), user.getFirstName());
		Assert.assertEquals(response.getBody().getUpdatedBy(), user.getFirstName());
		Assert.assertNotNull(response.getBody().getCreatedAt());
		Assert.assertNotNull(response.getBody().getUpdatedAt());
		Assert.assertTrue(response.getStatusCode().is2xxSuccessful());
	}

	@Test
	@DisplayName("GET User by Id Not Found")
	@Severity(SeverityLevel.BLOCKER)
	public void testGetUserByIdNotFound() {

		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<>(null, headers);
		ResponseEntity<User> response = restTemplate.exchange(getRootUrl() + "/users/" + "12233243254546565",
				HttpMethod.GET, entity, User.class);

		Allure.addAttachment("Get user within id not exists", String.valueOf(response.getStatusCode()));

		Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}

	@Test
	@DisplayName("POST User with Success")
	@Severity(SeverityLevel.BLOCKER)
	public void testCreateUser() {

		Faker faker = new Faker();
		String firstName = faker.name().firstName();
		String lastName = faker.name().lastName();
		String email = faker.name().username() + "@testeeeeeee.com";

		User user = new User();
		user.setEmail(email);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setCreatedBy(firstName);
		user.setUpdatedBy(firstName);

		ResponseEntity<User> postResponse = restTemplate.postForEntity(getRootUrl() + "/users", user, User.class);

		logger.info("response body from post User: " + postResponse.getBody());
		logger.info("response status code from post User: " + postResponse.getStatusCode());

		Allure.addAttachment("response body from post User", String.valueOf(postResponse.getBody()));
		Allure.addAttachment("response status code from post User", String.valueOf(postResponse.getStatusCode()));

		Assert.assertEquals(Objects.requireNonNull(postResponse.getBody()).getFirstName(), firstName);
		Assert.assertEquals(postResponse.getBody().getLastName(), lastName);
		Assert.assertEquals(postResponse.getBody().getEmail(), email);
		Assert.assertEquals(postResponse.getBody().getCreatedBy(), firstName);
		Assert.assertEquals(postResponse.getBody().getUpdatedBy(), firstName);
		Assert.assertNotNull(postResponse.getBody().getCreatedAt());
		Assert.assertNotNull(postResponse.getBody().getUpdatedAt());
		Assert.assertTrue(postResponse.getStatusCode().is2xxSuccessful());
	}

	@Test
	@DisplayName("PUT User with Success")
	@Severity(SeverityLevel.BLOCKER)
	public void testPutUsers() {

		User user = createUser();
		ResponseEntity<User> postResponse = restTemplate.postForEntity(getRootUrl() + "/users", user, User.class);

		String firstNameBeforeUpdate = user.getFirstName();

		logger.info("response body from POST User: " + postResponse.getBody());

		user.setFirstName("Test1");
		user.setLastName("Test2");

		HttpHeaders headers = new HttpHeaders();
		HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);

		ResponseEntity<User> putResponse = restTemplate.exchange(getRootUrl() + "/users/" + Objects.requireNonNull(postResponse.getBody()).getId(),
				HttpMethod.PUT, requestEntity, User.class);

		logger.info("response body from PUT User: " + putResponse.getBody());

		Assert.assertEquals(Objects.requireNonNull(putResponse.getBody()).getFirstName(), "Test1");
		Assert.assertEquals(putResponse.getBody().getLastName(), "Test2");
		Assert.assertEquals(putResponse.getBody().getEmail(), user.getEmail());
		Assert.assertEquals(putResponse.getBody().getCreatedBy(), firstNameBeforeUpdate);
		Assert.assertEquals(putResponse.getBody().getUpdatedBy(), firstNameBeforeUpdate);
		Assert.assertNotNull(putResponse.getBody().getCreatedAt());
		Assert.assertNotNull(putResponse.getBody().getUpdatedAt());
		Assert.assertTrue(putResponse.getStatusCode().is2xxSuccessful());

	}

	@Test
	@DisplayName("DELETE User with Success")
	@Severity(SeverityLevel.CRITICAL)
	public void testDeletePost() {
		User user = createUser();
		ResponseEntity<User> postResponse = restTemplate.postForEntity(getRootUrl() + "/users", user, User.class);

		logger.info("response body from POST User: " + postResponse.getBody());


		HttpHeaders headers = new HttpHeaders();
		HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);

		ResponseEntity<User> deleteResponse = restTemplate.exchange(getRootUrl() + "/users/" + Objects.requireNonNull(postResponse.getBody()).getId(),
				HttpMethod.DELETE, requestEntity, User.class);

		logger.info("Delete User status code returned: " + deleteResponse.getStatusCode());

		Assert.assertEquals(deleteResponse.getStatusCode(), HttpStatus.NO_CONTENT);

		try {
			restTemplate.getForObject(getRootUrl() + "/users/" + postResponse.getBody().getId(), User.class);
		} catch (final HttpClientErrorException e) {
			Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
		}
	}

}
