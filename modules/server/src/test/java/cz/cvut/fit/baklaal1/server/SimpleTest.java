package cz.cvut.fit.baklaal1.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DisplayName("Test class with simple passing test")
public class SimpleTest {
    @Test
    @DisplayName("Test that should pass")
    public void passingTest() {
        assertEquals(1123,123);
    }
}
