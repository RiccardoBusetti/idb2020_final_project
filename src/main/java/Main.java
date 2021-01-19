import data.postgres.DBConnection;
import domain.dao.SQLBNBDao;

public class Main {

    public static void main(String[] args) {
        DBConnection.connect((connection -> {
            new BNBApplication(new SQLBNBDao(connection)).start();
        }));
    }
}
