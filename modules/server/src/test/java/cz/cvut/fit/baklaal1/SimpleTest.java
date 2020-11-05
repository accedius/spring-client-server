package cz.cvut.fit.baklaal1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SimpleTest {
    @Test
    public void passingTest() {
        assertEquals(123,123);
    }
}
