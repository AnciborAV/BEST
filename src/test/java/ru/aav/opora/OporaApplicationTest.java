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

    @Test
    void restartsWithExistingLowercaseH2Database(
        @org.junit.jupiter.api.io.TempDir java.nio.file.Path directory
    ) {
        String url = "jdbc:h2:file:" + directory.resolve("restart")
            + ";MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH";
        String[] arguments = {
            "--spring.datasource.url=" + url,
            "--spring.datasource.username=sa",
            "--spring.datasource.password=",
            "--spring.main.banner-mode=off"
        };
        // Первый запуск создаёт служебные таблицы Liquibase и тестовые данные.
        try (var first = new org.springframework.boot.builder.SpringApplicationBuilder(OporaApplication.class)
                .web(org.springframework.boot.WebApplicationType.NONE).run(arguments)) {
            var database = first.getBean(JdbcTemplate.class);
            database.execute("CREATE TABLE restart_marker (id INTEGER PRIMARY KEY)");
            database.update("INSERT INTO restart_marker(id) VALUES (1)");
        }
        // Второй запуск должен узнать существующие таблицы и сохранить данные.
        try (var second = new org.springframework.boot.builder.SpringApplicationBuilder(OporaApplication.class)
                .web(org.springframework.boot.WebApplicationType.NONE).run(arguments)) {
            var database = second.getBean(JdbcTemplate.class);
            assertThat(database.queryForObject("SELECT COUNT(*) FROM restart_marker", Long.class)).isEqualTo(1L);
            assertThat(database.queryForObject("SELECT COUNT(*) FROM databasechangelog", Long.class)).isZero();
        }
    }
}
