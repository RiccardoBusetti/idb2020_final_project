import domain.dao.BNBDao;
import domain.entities.Package;
import domain.entities.*;
import utils.ConsoleUtils;
import utils.UUIDUtils;

import java.util.Arrays;
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
        while (true) {
            Function<BNBDao, Boolean> block = ConsoleUtils.promptIndexedSelection(
                    "SELECT A FUNCTION: ",
                    Arrays.stream(Query.values()).collect(Collectors.toList())
            ).block;
            if (block == null) return;
            block.apply(dao);
        }
    }

    public enum Query {
        EXIT("Exit the program", "Exits the program.", null),
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
            Room selectedRoom =
                    ConsoleUtils.promptIndexedSelection("SELECT A ROOM: ", dao.selectRooms());

            PaymentMethod selectedPaymentMethod =
                    ConsoleUtils.promptIndexedSelection("SELECT A PAYMENT METHOD: ", dao.selectPaymentMethods());

            dao.insertPackage(new Package(
                    ConsoleUtils.promptString("INSERT A STARTING DATE: "),
                    ConsoleUtils.promptString("INSERT AN ENDING DATE: "),
                    selectedRoom.getRoomNo(),
                    selectedRoom.getStreet(),
                    selectedRoom.getStreetNo(),
                    selectedRoom.getPostalCode(),
                    ConsoleUtils.promptInteger("INSERT THE COST PER NIGHT: ")
            ), selectedPaymentMethod);

            return true;
        }
    }

    private static final class BookPackage implements Function<BNBDao, Boolean> {
        @Override
        public Boolean apply(BNBDao dao) {
            Package selectedPackage =
                    ConsoleUtils.promptIndexedSelection("SELECT A PACKAGE: ", dao.selectPackages());

            Customer selectedCustomer =
                    ConsoleUtils.promptIndexedSelection("SELECT A CUSTOMER: ", dao.selectCustomers());

            PaymentMethod selectedPaymentMethod =
                    ConsoleUtils.promptIndexedSelection("SELECT A PAYMENT METHOD: ", dao.selectPaymentMethodsOfPackage(selectedPackage));

            dao.insertBooking(
                    new Booking(
                            UUIDUtils.generateUUIDv4(),
                            ConsoleUtils.promptString("SELECT A STARTING DATE: "),
                            ConsoleUtils.promptString("SELECT AN ENDING DATE: ")
                    ),
                    selectedCustomer,
                    selectedPackage,
                    selectedPaymentMethod
            );

            return true;
        }
    }
}
