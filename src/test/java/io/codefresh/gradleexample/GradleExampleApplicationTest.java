package io.codefresh.gradleexample;


import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class GradleExampleApplicationTest {

//	@Test
//	public void contextLoads() {
//		Assertions.
//				assertEquals("Hello World", "Hello "+"World", "Expected correct message");
//	}
}
