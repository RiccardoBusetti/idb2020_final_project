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
        ),
        EXIT("Exit the program", "Exits the program.");

        private final String title;
        private final String description;
        private final Function<BNBDao, Boolean> block;

        Query(String title, String description) {
            this.title = title;
            this.description = description;
            this.block = null;
        }

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
                    "There are no rooms available.\n",
                    dao.selectRooms());
            if (selectedRoom == null) return false;

            PaymentMethod selectedPaymentMethod = ConsoleUtils.promptIndexedSelection(
                    "SELECT A PAYMENT METHOD: ",
                    "There are no payment methods available.\n",
                    dao.selectPaymentMethods());
            if (selectedPaymentMethod == null) return false;

            try {
                dao.insertPackage(new Package(
                        ConsoleUtils.promptDate("INSERT A STARTING DATE: "),
                        ConsoleUtils.promptDate("INSERT AN ENDING DATE: "),
                        selectedRoom.getRoomNo(),
                        selectedRoom.getStreet(),
                        selectedRoom.getStreetNo(),
                        selectedRoom.getPostalCode(),
                        ConsoleUtils.promptInteger("INSERT THE COST PER NIGHT: ")
                ), selectedPaymentMethod);
                ConsoleUtils.show("Package inserted successfully.\n");
            } catch (RuntimeException e) {
                ConsoleUtils.show("Package couldn't be inserted because: " + e.getMessage() + "\n");
            }

            return true;
        }
    }

    private static final class BookPackage implements Function<BNBDao, Boolean> {
        @Override
        public Boolean apply(BNBDao dao) {
            Customer selectedCustomer = ConsoleUtils.promptIndexedSelection(
                    "SELECT A CUSTOMER: ",
                    "There are no customers available.\n",
                    dao.selectCustomers());
            if (selectedCustomer == null) return false;

            Package selectedPackage = ConsoleUtils.promptIndexedSelection(
                    "SELECT A PACKAGE: ",
                    "There are no packages available.\n",
                    dao.selectPackages());
            if (selectedPackage == null) return false;

            Service selectedExtraService = ConsoleUtils.promptIndexedSelection("" +
                            "SELECT EXTRA SERVICE (OPTIONAL): ",
                    "There are no extra services available.\n",
                    dao.selectExtraServicesOfPackage(selectedPackage));
            if (selectedExtraService == null) return false;

            PaymentMethod selectedPaymentMethod =
                    ConsoleUtils.promptIndexedSelection(
                            "SELECT A PAYMENT METHOD: ",
                            "There are no payment methods available.\n",
                            dao.selectPaymentMethodsOfPackage(selectedPackage));
            if (selectedPaymentMethod == null) return false;

            try {
                dao.insertBooking(
                        new Booking(
                                UUIDUtils.generateUUIDv4(),
                                ConsoleUtils.promptDate("SELECT A STARTING DATE: "),
                                ConsoleUtils.promptDate("SELECT AN ENDING DATE: ")
                        ),
                        selectedCustomer,
                        selectedPackage,
                        selectedPaymentMethod,
                        selectedExtraService
                );
                ConsoleUtils.show("Booking inserted successfully.\n");
            } catch (RuntimeException e) {
                ConsoleUtils.show("Booking couldn't be inserted because: " + e.getMessage() + "\n");
            }

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
                    "There are no packages with a given postal code.\n",
                    packages);

            return true;
        }
    }

    private static final class SelectBookingsOverlappingWith implements Function<BNBDao, Boolean> {
        @Override
        public Boolean apply(BNBDao dao) {
            Package selectedPackage = ConsoleUtils.promptIndexedSelection(
                    "SELECT A PACKAGE: ",
                    "There are no packages available.\n",
                    dao.selectPackages());
            if (selectedPackage == null) return false;

            String startDate = ConsoleUtils.promptDate("SELECT A STARTING DATE: ");
            String endDate = ConsoleUtils.promptDate("SELECT AN ENDING DATE: ");

            List<Booking> bookings = dao.getDatesWherePackageIsBooked(selectedPackage, startDate, endDate);

            ConsoleUtils.showList(
                    "THESE ARE THE BOOKINGS THAT OVERLAP WITH YOUR DATES: ",
                    "There are no bookings in this period.\n",
                    bookings);

            return true;
        }
    }

    private static final class MakeReview implements Function<BNBDao, Boolean> {
        @Override
        public Boolean apply(BNBDao dao) {
            Booking selectedBooking = ConsoleUtils.promptIndexedSelection(
                    "SELECT A BOOKING: ",
                    "There are no bookings available.\n",
                    dao.selectBooking());
            if (selectedBooking == null) return false;

            String review = ConsoleUtils.promptString("WRITE A REVIEW: ");
            int stars = ConsoleUtils.promptInteger("STARS (0-5): ");

            try {
                dao.insertReview(selectedBooking, review, stars);
                ConsoleUtils.show("Booking reviewed successfully.\n");
            } catch (RuntimeException e) {
                ConsoleUtils.show("Booking review couldn't be inserted because: " + e.getMessage() + "\n");
            }

            return true;
        }
    }
}
