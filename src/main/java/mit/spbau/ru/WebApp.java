package mit.spbau.ru;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.LruObjectCache;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.DatabaseConnection;
import mit.spbau.ru.data.Drug;
import mit.spbau.ru.data.DrugType;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import static spark.Spark.get;
import static spark.Spark.port;

public class WebApp {
    /**
     * Web settings
     */
    private final static Integer PORT = 8000;
    private final static Integer INTERNAL_ERROR_CODE = 500;
    private final static String ALL_DRUGS_URL = "/drug/all";
    private final static String NEW_DRUG_URL = "/drug/new";

    /**
     * Database settings
     */
    private final static Integer POSTGRES_POTR = 5432;
    private final static String POSTGRES_USER_NAME = "postgres";
    private final static String POSTGRES_PASSWORD = "foobar";
    private final static String JDBC_URL = String.format("jdbc:postgresql://localhost:%d/postgres?user=%s&password=%s", POSTGRES_POTR, POSTGRES_USER_NAME, POSTGRES_PASSWORD);

    private final static Integer MAX_CONNECTION_AGE = 5 * 60 * 1000;
    private final static JdbcPooledConnectionSource connectionSource;

    private final static Integer CACHE_SIZE = 10000;
    private final static LruObjectCache cache = new LruObjectCache(CACHE_SIZE);

    private enum IsolationLevel {
        REPEATABLE_READ("REPEATABLE READ"),
        READ_COMMITTED("READ COMMITTED");

        private final String name;

        IsolationLevel(final String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    static {
        try {
            connectionSource = new JdbcPooledConnectionSource(JDBC_URL);
            connectionSource.setMaxConnectionAgeMillis(MAX_CONNECTION_AGE);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static Route withTry(final Route route) {
        return (request, response) -> {
            try {
                return route.handle(request, response);
            } catch (Throwable e) {
                e.printStackTrace();
                response.status(INTERNAL_ERROR_CODE);
                return e.getMessage();
            }
        };
    }

    private static Object runTransaction(final IsolationLevel level, final Callable<Object> transaction)
            throws SQLException, IOException {
        final DatabaseConnection connection = connectionSource.getReadWriteConnection(null);
        connection.setAutoCommit(false);

        final ProxyDatabaseType databaseType = new ProxyDatabaseType(connectionSource.getDatabaseType());
        final Callable<Object> wrapper = () -> {
            connection.executeStatement("set transaction isolation level " + level, DatabaseConnection.DEFAULT_RESULT_FLAGS);
            final Object result = transaction.call();
            connection.commit(null);
            connection.close();
            return result;
        };

        return TransactionManager.callInTransaction(connection, false, databaseType, wrapper);
    }

    private static Object getAllDrugs(Request request, Response response) throws IOException, SQLException {
        response.type("text/plain");

        final Dao<Drug, Integer> daoDrug = DaoManager.createDao(connectionSource, Drug.class);
        daoDrug.setObjectCache(cache);

        return runTransaction(IsolationLevel.REPEATABLE_READ,
                () -> DaoManager.createDao(connectionSource, Drug.class)
                        .queryForAll()
                        .stream()
                        .map(Drug::toString
                        ).collect(Collectors.joining("\n")));
    }

    private static Object newDrug(Request request, Response response) throws IOException, SQLException {
        final Object result = runTransaction(IsolationLevel.READ_COMMITTED, () -> {
            final String type = request.queryMap("dosage").value();

            final Dao<DrugType, Integer> daoDrugType = DaoManager.createDao(connectionSource, DrugType.class);
            daoDrugType.setObjectCache(cache);

            final List<DrugType> types = daoDrugType
                    .queryBuilder()
                    .where()
                    .eq("type", type)
                    .query();

            if (types.isEmpty()) {
                throw new RuntimeException("Invalid drug type");
            }

            System.out.println(types.get(0).getType());

            final Dao<Drug, Integer> daoDrug = DaoManager.createDao(connectionSource, Drug.class);
            daoDrug.setObjectCache(cache);

            final Drug drug = new Drug(0,
                    request.queryMap("trade_name").value(),
                    request.queryMap("inn").value(),
                    types.get(0).getId(),
                    null,
                    null,
                    null);

            daoDrug.create(drug);
            return true;
        });

        if (Boolean.TRUE.equals(result)) {
            response.redirect(ALL_DRUGS_URL);
            return null;
        }

        response.status(INTERNAL_ERROR_CODE);
        return "Transaction failed";
    }

    public static void main(String[] args) {
        port(PORT);
        get(ALL_DRUGS_URL, WebApp.withTry(WebApp::getAllDrugs));
        get(NEW_DRUG_URL, WebApp.withTry(WebApp::newDrug));
    }
}
