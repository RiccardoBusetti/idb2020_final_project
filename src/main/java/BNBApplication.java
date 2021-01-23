import domain.dao.BNBDao;
import domain.entities.Package;
import domain.entities.*;
import utils.ConsoleUtils;
import utils.UUIDUtils;

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
                "Creates a new package available for booking.",
                new InsertPackage()
        ),
        BOOK_PACKAGE(
                "Book a package",
                "Books a given package for a date interval.",
                new BookPackage()
        ),
        SELECT_PACKAGES_WITH_POSTAL_CODE(
                "Select a package by postal code",
                "Retrieves the packages with a given postal code.",
                new PackageWithGivenPostalCode()
        ),
        SELECT_BOOKINGS_OVERLAPPING_WITH(
                "Select overlapping bookings by time interval",
                "Retrieves all the bookings that overlap with a given starting and ending date.",
                new SelectBookingsOverlappingWith()
        ),
        MAKE_REVIEW(
                "Make a review",
                "Inserts a review in a booking.",
                new MakeReview()
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
            return title.toUpperCase() + ": " + description.toLowerCase();
        }
    }

    public static final class InsertPackage implements Function<BNBDao, Boolean> {
        @Override
        public Boolean apply(BNBDao dao) {
            Room selectedRoom = ConsoleUtils.promptIndexedSelection(
                    "SELECT A ROOM: ",
                    "THERE ARE NO ROOMS AVAILABLE",
                    dao.selectRooms());
            if (selectedRoom == null) return false;

            PaymentMethod selectedPaymentMethod = ConsoleUtils.promptIndexedSelection(
                    "SELECT A PAYMENT METHOD: ",
                    "THERE ARE NO PAYMENT METHODS AVAILABLE",
                    dao.selectPaymentMethods());
            if (selectedPaymentMethod == null) return false;

            dao.insertPackage(new Package(
                    ConsoleUtils.promptDate("INSERT A STARTING DATE: "),
                    ConsoleUtils.promptDate("INSERT AN ENDING DATE: "),
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
            Package selectedPackage = ConsoleUtils.promptIndexedSelection(
                    "SELECT A PACKAGE: ",
                    "THERE ARE NO PACKAGES AVAILABLE",
                    dao.selectPackages());
            if (selectedPackage == null) return false;

            Customer selectedCustomer = ConsoleUtils.promptIndexedSelection(
                    "SELECT A CUSTOMER: ",
                    "THERE ARE NO CUSTOMERS AVAILABLE",
                    dao.selectCustomers());
            if (selectedCustomer == null) return false;

            PaymentMethod selectedPaymentMethod =
                    ConsoleUtils.promptIndexedSelection(
                            "SELECT A PAYMENT METHOD: ",
                            "THERE ARE NO SELECTED PAYMENT METHODS AVAILABLE",
                            dao.selectPaymentMethodsOfPackage(selectedPackage));
            if (selectedPaymentMethod == null) return false;

            dao.insertBooking(
                    new Booking(
                            UUIDUtils.generateUUIDv4(),
                            ConsoleUtils.promptDate("SELECT A STARTING DATE: "),
                            ConsoleUtils.promptDate("SELECT AN ENDING DATE: ")
                    ),
                    selectedCustomer,
                    selectedPackage,
                    selectedPaymentMethod
            );

            return true;
        }
    }

    private static final class PackageWithGivenPostalCode implements Function<BNBDao, Boolean> {
        @Override
        public Boolean apply(BNBDao dao) {
            int postalCode = ConsoleUtils.promptInteger("SELECT A POSTAL CODE: ");

            List<Package> packages = dao.selectPackagesWithPostalCode(postalCode);

            ConsoleUtils.showList(
                    "PACKAGES WITH GIVEN POSTAL CODE ARE THE FOLLOWING: ",
                    "NO PACKAGES WERE FOUND WITH GIVEN POSTAL CODE.\n",
                    packages);

            return true;
        }
    }

    private static final class SelectBookingsOverlappingWith implements Function<BNBDao, Boolean> {
        @Override
        public Boolean apply(BNBDao dao) {
            Package selectedPackage = ConsoleUtils.promptIndexedSelection(
                    "SELECT A PACKAGE: ",
                    "THERE ARE NO PACKAGES AVAILABLE",
                    dao.selectPackages());
            if (selectedPackage == null) return false;

            String startDate = ConsoleUtils.promptDate("SELECT A STARTING DATE: ");
            String endDate = ConsoleUtils.promptDate("SELECT AN ENDING DATE: ");

            List<Booking> bookings = dao.getDatesWherePackageIsBooked(selectedPackage, startDate, endDate);

            ConsoleUtils.showList(
                    "BOOKINGS WITH GIVEN PACKAGE AND GIVEN START AND END DATE ARE THE FOLLOWING: ",
                    "NO BOOKING WITH GIVEN PACKAGE WITH GIVEN DATES.",
                    bookings);

            return true;
        }
    }

    private static final class MakeReview implements Function<BNBDao, Boolean> {
        @Override
        public Boolean apply(BNBDao dao) {
            Booking selectedBooking = ConsoleUtils.promptIndexedSelection(
                    "SELECT A BOOKING: ",
                    "THERE ARE NO BOOKINGS AVAILABLE",
                    dao.selectBooking());
            if (selectedBooking == null) return false;

            String review = ConsoleUtils.promptString("WRITE A REVIEW: ");
            int stars = ConsoleUtils.promptInteger("STARS (0-5):");

            try {
                dao.insertReview(selectedBooking, review, stars);
                ConsoleUtils.show("BOOKING REVIEWED SUCCESSFULLY");
            } catch (RuntimeException e) {
                ConsoleUtils.show("BOOKING REVIEW NOT INSERTED BECAUSE " + e.getMessage());
            }

            return true;
        }
    }
}
