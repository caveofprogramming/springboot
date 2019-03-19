package com.caveofprogramming.tests;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.caveofprogramming.model.entity.Interest;
import com.caveofprogramming.model.entity.Profile;
import com.caveofprogramming.model.entity.SiteUser;
import com.caveofprogramming.service.InterestService;
import com.caveofprogramming.service.ProfileService;
import com.caveofprogramming.service.UserService;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations = "classpath:test.properties")

public class BulkTests {

	private static final String namesFile = "/com/caveofprogramming/tests/data/names.txt";
	private static final String interestsFile = "/com/caveofprogramming/tests/data/hobbies.txt";
	private static final int NUM_USERS = 40;

	@Autowired
	private UserService userService;

	@Autowired
	private ProfileService profileService;

	@Autowired
	private InterestService interestService;

	private SiteUser[] testUsers = { new SiteUser("test@example.com", "testtest", "test", "test"),
			new SiteUser("johnxyz@example.com", "hellohello", "John", "Purcell", "ROLE_ADMIN"),
			new SiteUser("carollhunter@example.com", "passcaroll", "Caroll", "Hunter"),
			new SiteUser("lacimark@example.com", "passlaci", "Laci", "Mark"),
			new SiteUser("dollieun@example.com", "passdollie", "Dollie", "Un") };

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

	//@Ignore
	@Test
	public void createTestData() throws IOException {

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
