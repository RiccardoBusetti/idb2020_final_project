package data.postgres;

import domain.SQL;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = Dotenv.load().get("DB_URL");
    private static final String USERNAME = Dotenv.load().get("DB_USERNAME");
    private static final String PASSWORD = Dotenv.load().get("DB_PASSWORD");
    private final Connection connection;

    public DBConnection() {
        this.connection = getConnection();
    }

    public static void connect(SQL.SQLScope<DBConnection> block) {
        DBConnection connection = new DBConnection();

        try {
            block.call(connection);
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error while the database connection was open: " + e.getMessage());
        }
    }

    public void makeQuery(SQL.SQLScope<Connection> block) {
        makeQuery(false, block);
    }

    public void makeQuery(boolean overrideException, SQL.SQLScope<Connection> block) {
        try {
            block.call(connection);
        } catch (SQLException e) {
            if (overrideException)
                throw new RuntimeException(e.getMessage());
            else
                System.out.println("Error while making a query: " + e.getMessage());
        }
    }

    public void makeTransaction(boolean setDeferrable, SQL.SQLScope<Connection> block) {
        makeTransaction(false, setDeferrable, block);
    }

    public void makeTransaction(boolean overrideException, boolean setDeferrable, SQL.SQLScope<Connection> block) {
        try {
            connection.setAutoCommit(false);
            if (setDeferrable) deferrablePS().executeUpdate();

            block.call(connection);

            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            rollback();
            if (overrideException)
                throw new RuntimeException(e.getMessage());
            else
                System.out.println("Error while performing a transaction: " + e.getMessage());
        }
    }

    private void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println("Error while performing the rollback: " + e.getMessage());
        }
    }

    private PreparedStatement deferrablePS() throws SQLException {
        return connection.prepareStatement("SET CONSTRAINTS ALL DEFERRED");
    }

    private void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error while closing the connection to the database: " + e.getMessage());
        }
    }

    private Connection getConnection() {
        try {
            loadDriver();
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Error while connecting to the database: " + e.getMessage());
            return null;
        }
    }

    private void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Error while loading the driver.");
        }
    }
}
