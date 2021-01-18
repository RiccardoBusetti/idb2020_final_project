import domain.dao.BNBDao;
import domain.entities.Package;
import domain.entities.PaymentMethod;
import domain.entities.Room;
import utils.ConsoleUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BNBApplication {

    private final BNBDao dao;

    public BNBApplication(BNBDao dao) {
        this.dao = dao;
    }

    public void start() {
        promptMenu();
    }

    private void promptMenu() {
        ConsoleUtils.promptIndexedSelection(
                "SELECT A FUNCTION: ",
                Arrays.stream(Query.values()).collect(Collectors.toList())
        ).block.apply(dao);
    }

    public enum Query {
        INSERT_PACKAGE(
                "Insert a new package",
                "Inserts a new package into the database.",
                new InsertPackage()
        ),
        BOOK_PACKAGE(
                "Book a package",
                "Inserts a booking for a package.",
                new BookPackage()
        );

        private final String title;
        private final String description;
        private final Function<BNBDao, Boolean> block;

        Query(String title, String description, Function<BNBDao, Boolean> block) {
            this.title = title;
            this.description = description;
            this.block = block;
        }

        @Override
        public String toString() {
            return title.toUpperCase() + ": " + description;
        }
    }

    public static final class InsertPackage implements Function<BNBDao, Boolean> {
        @Override
        public Boolean apply(BNBDao dao) {
            List<Room> rooms = dao.selectRooms();
            Room selectedRoom =
                    ConsoleUtils.promptIndexedSelection("SELECT A ROOM: ", rooms);

            List<PaymentMethod> paymentMethod = dao.selectPaymentMethods();
            PaymentMethod selectedPaymentMethod =
                    ConsoleUtils.promptIndexedSelection("SELECT A PAYMENT METHOD: ", paymentMethod);

            String startDate = ConsoleUtils.promptString("INSERT A STARTING DATE: ");
            String endingDate = ConsoleUtils.promptString("INSERT AN ENDING DATE: ");

            dao.insertPackage(new Package(
                    startDate,
                    endingDate,
                    selectedRoom.getRoomNo(),
                    selectedRoom.getStreet(),
                    selectedRoom.getStreetNo(),
                    selectedRoom.getPostalCode(),
                    0
            ), selectedPaymentMethod);

            return true;
        }
    }

    private static final class BookPackage implements Function<BNBDao, Boolean> {
        @Override
        public Boolean apply(BNBDao dao) {
            // TODO: implement the booking of a package.
            return true;
        }
    }
}
