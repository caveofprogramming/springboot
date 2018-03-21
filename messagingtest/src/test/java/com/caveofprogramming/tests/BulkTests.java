package com.caveofprogramming.tests;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.caveofprogramming.model.entity.Interest;
import com.caveofprogramming.model.entity.Message;
import com.caveofprogramming.model.entity.Profile;
import com.caveofprogramming.model.entity.SiteUser;
import com.caveofprogramming.service.InterestService;
import com.caveofprogramming.service.MessageService;
import com.caveofprogramming.service.ProfileService;
import com.caveofprogramming.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
@Transactional

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BulkTests {

	private static final String randomTextFile = "/com/caveofprogramming/tests/data/random.txt";
	private static final String namesFile = "/com/caveofprogramming/tests/data/names.txt";
	private static final String interestsFile = "/com/caveofprogramming/tests/data/hobbies.txt";

	private final int NUM_USERS = 100;
	private final int AVERAGE_MESSAGES_PER_USER = 10;
	private final int MAX_MESSAGE_LENGTH = 250;

	@Autowired
	private UserService userService;

	@Autowired
	private ProfileService profileService;

	@Autowired
	private InterestService interestService;

	@Autowired
	private MessageService messageService;

	private SiteUser[] testUsers = { new SiteUser("test@example.com", "testtest", "test", "test"),
			new SiteUser("johnxyz@example.com", "hellohello", "John", "Purcell", "ROLE_ADMIN"),
			new SiteUser("carollhunter@example.com", "passcaroll", "Caroll",
			"Hunter"),
			new SiteUser("lacimark@example.com", "passlaci", "Laci", "Mark"),
			new SiteUser("dollieun@example.com", "passdollie", "Dollie",
			"Un")
	};

	private List<String> loadFile(String filename, int maxLength) throws IOException {

		Path filePath = new ClassPathResource(filename).getFile().toPath();

		Stream<String> stream = Files.lines(filePath);

		// @formatter:off

		List<String> items = stream.filter(line -> !line.isEmpty()).map(line -> line.trim())
				.filter(line -> line.length() <= maxLength)
				.map(line -> line.substring(0, 1).toUpperCase() + line.substring(1).toLowerCase())
				.collect(Collectors.toList());

		// @formatter:on

		stream.close();

		return items;
	}


	@Test
	//@Ignore
	public void createTestBMessages() throws IOException {

		Random random = new Random();

		Path inputFilePath = new ClassPathResource(randomTextFile).getFile().toPath();
		String randomText = new String(Files.readAllBytes(inputFilePath));

		String[] sentences = randomText.split("\\.");

		List<SiteUser> userList = new ArrayList<>();
		// userService.getAll().forEach(userList::add);
		Arrays.stream(testUsers).forEach(u -> userList.add(userService.get(u.getEmail())));

		for (int i = 0; i < userList.size() * AVERAGE_MESSAGES_PER_USER; i++) {
			SiteUser fromUser = userList.get(random.nextInt(userList.size()));
			SiteUser toUser = userList.get(random.nextInt(userList.size()));

			String messageText = "Hi " + toUser.getFirstname() + "! ";

			String sentence = sentences[random.nextInt(sentences.length)];

			if (messageText.length() + sentence.length() < MAX_MESSAGE_LENGTH - 1) {
				messageText += sentence + ".";
			}

			Message message = new Message(toUser, fromUser, messageText);

			messageService.save(message);
		}

	}

	//@Ignore
	@Test
	public void createSpecificUsers() {
		for (SiteUser user : testUsers) {
			SiteUser existingUser = userService.get(user.getEmail());

			if (existingUser != null) {
				continue;
			}

			user.setEnabled(true);

			String role = user.getRole();

			if (role == null) {
				user.setRole("ROLE_USER");
			}
			
			userService.save(user);
			user = userService.get(user.getEmail());

			Profile profile = new Profile(user);
			profileService.save(profile);
		}
	}

	@Ignore
	@Test
	public void createTestAUsers() throws IOException {

		Random random = new Random();

		List<String> names = loadFile(namesFile, 25);
		List<String> interests = loadFile(interestsFile, 25);

		for (int numUsers = 0; numUsers < NUM_USERS; numUsers++) {
			String firstname = names.get(random.nextInt(names.size()));
			String surname = names.get(random.nextInt(names.size()));

			String email = firstname.toLowerCase() + surname.toLowerCase() + "@example.com";

			if (userService.get(email) != null) {
				continue;
			}

			String password = "pass" + firstname.toLowerCase();
			password = password.substring(0, Math.min(15, password.length()));

			assertTrue(password.length() <= 15);

			SiteUser user = new SiteUser(email, password, firstname, surname);
			user.setEnabled(random.nextInt(5) != 0);

			userService.register(user);

			Profile profile = new Profile(user);

			int numberInterests = random.nextInt(7);

			Set<Interest> userInterests = new HashSet<Interest>();

			for (int i = 0; i < numberInterests; i++) {
				String interestText = interests.get(random.nextInt(interests.size()));

				Interest interest = interestService.createIfNotExists(interestText);
				userInterests.add(interest);
			}

			profile.setInterests(userInterests);
			profileService.save(profile);
		}

		assertTrue(true);
	}
}
