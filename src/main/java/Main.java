import java.sql.*;

public class Main {

    public static void main(String[] args) throws Exception {
        Connection connection = PGPConnection.newConnection();

        ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Building");
        while (rs.next()) {
            System.out.println(rs.getString("street"));
        }
    }
}
