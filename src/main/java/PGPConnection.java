import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PGPConnection {

    private static final String URL = Dotenv.load().get("DB_URL");
    private static final String USERNAME = Dotenv.load().get("DB_USERNAME");
    private static final String PASSWORD = Dotenv.load().get("DB_PASSWORD");

    private final Connection connection;

    public static Connection newConnection() {
        return new PGPConnection().connection;
    }

    public PGPConnection() {
        this.connection = getConnection();
    }

    private Connection getConnection() {
        try {
            loadDriver();
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Error while connecting to the database: " + e.getSQLState());
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
