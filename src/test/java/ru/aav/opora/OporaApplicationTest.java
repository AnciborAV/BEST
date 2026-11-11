package ru.aav.opora;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:opora-test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1",
    "spring.datasource.username=sa",
    "spring.datasource.password="
})
@AutoConfigureMockMvc
class OporaApplicationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JdbcTemplate jdbc;

    @Test
    void rendersPublicHomeUsingThymeleaf() throws Exception {
        mvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("home"))
            .andExpect(content().string(containsString("<title>ОПОРА</title>")))
            .andExpect(content().string(containsString("Пережить. Осмыслить. Создавать.")))
            .andExpect(content().string(not(containsString("th:text"))));
    }

    @Test
    void startsWithOwnChangelogAndWithoutBookstoreRoutesOrTables() throws Exception {
        Long migrations = jdbc.queryForObject("SELECT COUNT(*) FROM databasechangelog", Long.class);
        Long tables = jdbc.queryForObject("""
            SELECT COUNT(*) FROM information_schema.tables
            WHERE table_schema='public' AND table_name IN ('books', 'store_users', 'store_orders')
            """, Long.class);
        assertThat(migrations).isZero();
        assertThat(tables).isZero();
        mvc.perform(get("/books")).andExpect(status().isNotFound());
    }
}
