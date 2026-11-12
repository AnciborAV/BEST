package ru.aav.opora.config.liquibase;

import java.sql.SQLException;
import liquibase.CatalogAndSchema.CatalogAndSchemaCase;
import liquibase.database.DatabaseConnection;
import liquibase.database.core.H2Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;

/** Согласует имена таблиц Liquibase с H2 в режиме DATABASE_TO_LOWER=TRUE. */
public class LowercaseH2Database extends H2Database {

    public LowercaseH2Database() {
        unquotedObjectsAreUppercased = false;
    }

    @Override
    public CatalogAndSchemaCase getSchemaAndCatalogCase() {
        return CatalogAndSchemaCase.LOWER_CASE;
    }

    @Override
    public int getPriority() {
        return super.getPriority() + 1;
    }

    @Override
    public boolean isCorrectDatabaseImplementation(
        DatabaseConnection connection
    ) throws DatabaseException {
        if (
            !super.isCorrectDatabaseImplementation(connection) ||
            !(connection instanceof JdbcConnection jdbc)
        ) {
            return false;
        }
        try {
            return jdbc
                .getWrappedConnection()
                .getMetaData()
                .storesLowerCaseIdentifiers();
        } catch (SQLException exception) {
            throw new DatabaseException(exception);
        }
    }
}
