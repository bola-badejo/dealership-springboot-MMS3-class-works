package com.smkngun.dealership;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")        // Use the test yaml + skip the DataLoader.
class DealershipApplicationTests {

	@Test
	void contextLoads() {
	}

}
