package ru.aav.opora;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Test the real deployment profile without overriding its database settings.
@SpringBootTest
@ActiveProfiles("render")
@AutoConfigureMockMvc
class RenderProfileTest {
    @Autowired private MockMvc mvc;
    @Autowired private Environment environment;

    @Test
    void servesPublicPagesWithoutExternalDatabase() throws Exception {
        assertThat(environment.getProperty("spring.datasource.url"))
            .startsWith("jdbc:h2:mem:opora-render;");
        assertThat(environment.getProperty("server.address")).isEqualTo("0.0.0.0");
        for (String path : new String[] {"/", "/stability", "/hardship", "/direction",
                "/practices", "/css/reading-size.css", "/css/practices.css", "/js/practices.js"}) {
            mvc.perform(get(path)).andExpect(status().isOk());
        }
    }
}
