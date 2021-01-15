import data.postgres.DBConnection;
import domain.dao.SQLBNBDao;

public class Main {

    public static void main(String[] args) {
        DBConnection.connect((connection -> {
            BNBApplication application = new BNBApplication(new SQLBNBDao(connection));
            application.insertPackage();
        }));
    }
}
