package domain.dao;

import data.postgres.DBConnection;
import domain.SQL;
import domain.entities.Package;
import domain.entities.*;
import org.postgresql.util.PGobject;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLBNBDao implements BNBDao {

    private final DBConnection connection;

    public SQLBNBDao(DBConnection connection) {
        this.connection = connection;
    }

    @Override
    public void insertPackage(Package _package, PaymentMethod paymentMethod) {
        connection.makeTransaction(true, true, (db) -> {
            PreparedStatement insertPackage = db.prepareStatement(Queries.INSERT_PACKAGE);
            insertPackage.setDate(1, Date.valueOf(_package.getStartDate()));
            insertPackage.setInt(2, _package.getRoomNo());
            insertPackage.setString(3, _package.getStreet());
            insertPackage.setInt(4, _package.getStreetNo());
            insertPackage.setInt(5, _package.getPostalCode());
            insertPackage.setDate(6, Date.valueOf(_package.getEndDate()));
            insertPackage.setInt(7, _package.getCostPerNight());
            insertPackage.executeUpdate();

            PreparedStatement insertOffers = db.prepareStatement(Queries.INSERT_OFFERS);
            insertOffers.setInt(1, paymentMethod.getCode());
            insertOffers.setDate(2, Date.valueOf(_package.getStartDate()));
            insertOffers.setInt(3, _package.getRoomNo());
            insertOffers.setString(4, _package.getStreet());
            insertOffers.setInt(5, _package.getStreetNo());
            insertOffers.setInt(6, _package.getPostalCode());
            insertOffers.executeUpdate();
        });
    }

    @Override
    public void insertBooking(Booking booking, Customer customer, Package _package, PaymentMethod paymentMethod, Service service) {
        connection.makeTransaction(true, true, (db) -> {
            PGobject uuid = new PGobject();
            uuid.setType("uuid");
            uuid.setValue(booking.getUUID());

            PreparedStatement insertBooking = db.prepareStatement(Queries.INSERT_BOOKING);
            insertBooking.setObject(1, uuid);
            insertBooking.setDate(2, Date.valueOf(booking.getStartDate()));
            insertBooking.setDate(3, Date.valueOf(booking.getEndDate()));
            insertBooking.executeUpdate();

            PreparedStatement insertBooks = db.prepareStatement(Queries.INSERT_BOOKS);
            insertBooks.setObject(1, uuid);
            insertBooks.setString(2, customer.getMail());
            insertBooks.executeUpdate();

            PreparedStatement insertThe = db.prepareStatement(Queries.INSERT_THE);
            insertThe.setObject(1, uuid);
            insertThe.setDate(2, Date.valueOf(_package.getStartDate()));
            insertThe.setInt(3, _package.getRoomNo());
            insertThe.setString(4, _package.getStreet());
            insertThe.setInt(5, _package.getStreetNo());
            insertThe.setInt(6, _package.getPostalCode());
            insertThe.executeUpdate();

            PreparedStatement insertWith = db.prepareStatement(Queries.INSERT_WITH);
            insertWith.setObject(1, uuid);
            insertWith.setInt(2, paymentMethod.getCode());
            insertWith.executeUpdate();

            PreparedStatement insertWithExtra = db.prepareStatement(Queries.INSERT_WITH_EXTRA);
            insertWithExtra.setInt(1, service.getId());
            insertWithExtra.setObject(2, uuid);
            insertWithExtra.executeUpdate();
        });
    }

    @Override
    public void insertReview(Booking booking, String review, int stars) {
        connection.makeTransaction(true, true, (db) -> {
            PreparedStatement insertReview = db.prepareStatement(Queries.MAKE_A_REVIEW);
            insertReview.setString(1, review);
            insertReview.setInt(2, stars);
            PGobject uuid = new PGobject();
            uuid.setType("uuid");
            uuid.setValue(booking.getUUID());
            insertReview.setObject(3, uuid);
            insertReview.executeUpdate();
        });
    }

    @Override
    public List<Room> selectRooms() {
        List<Room> rooms = new ArrayList<>();

        connection.makeQuery((db) -> {
            ResultSet resultSet = db.createStatement()
                    .executeQuery(Queries.SELECT_ROOMS);

            rooms.addAll(toList(resultSet, (rs) -> new Room(
                    rs.getInt("roomNo"),
                    rs.getString("B_street"),
                    rs.getInt("B_streetNo"),
                    rs.getInt("B_postalCode"),
                    rs.getInt("maxPeople"),
                    rs.getInt("m2")
            )));
        });

        return rooms;
    }

    @Override
    public List<PaymentMethod> selectPaymentMethods() {
        List<PaymentMethod> paymentMethods = new ArrayList<>();

        connection.makeQuery((db) -> {
            ResultSet resultSet = db.createStatement()
                    .executeQuery(Queries.SELECT_PAYMENT_METHODS);

            paymentMethods.addAll(toList(resultSet, (rs) -> new PaymentMethod(
                    rs.getInt("code"),
                    rs.getString("Name")
            )));
        });

        return paymentMethods;
    }

    @Override
    public List<PaymentMethod> selectPaymentMethodsOfPackage(Package _package) {
        List<PaymentMethod> paymentMethods = new ArrayList<>();

        connection.makeQuery((db) -> {
            PreparedStatement selectPaymentMethodsOfPackage = db.prepareStatement(Queries.SELECT_PAYMENT_METHODS_OF_PACKAGE);
            selectPaymentMethodsOfPackage.setDate(1, Date.valueOf(_package.getStartDate()));
            selectPaymentMethodsOfPackage.setInt(2, _package.getRoomNo());
            selectPaymentMethodsOfPackage.setString(3, _package.getStreet());
            selectPaymentMethodsOfPackage.setInt(4, _package.getStreetNo());
            selectPaymentMethodsOfPackage.setInt(5, _package.getPostalCode());

            paymentMethods.addAll(toList(selectPaymentMethodsOfPackage.executeQuery(), (rs) -> new PaymentMethod(
                    rs.getInt("code"),
                    rs.getString("Name")
            )));
        });

        return paymentMethods;
    }

    @Override
    public List<Customer> selectCustomers() {
        List<Customer> customers = new ArrayList<>();

        connection.makeQuery((db) -> {
            ResultSet resultSet = db.createStatement()
                    .executeQuery(Queries.SELECT_CUSTOMERS);

            customers.addAll(toList(resultSet, (rs) -> new Customer(
                    rs.getString("ssn"),
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getString("mail")
            )));
        });

        return customers;
    }

    @Override
    public List<Package> selectPackages() {
        List<Package> packages = new ArrayList<>();

        connection.makeQuery((db) -> {
            ResultSet resultSet = db.createStatement()
                    .executeQuery(Queries.SELECT_PACKAGES);

            packages.addAll(toList(resultSet, (rs) ->
                    new Package(
                            rs.getString("startDate"),
                            rs.getString("endDate"),
                            rs.getInt("R_roomNo"),
                            rs.getString("R_B_street"),
                            rs.getInt("R_B_streetNo"),
                            rs.getInt("R_B_postalCode"),
                            rs.getInt("costPerNight")
                    )
            ));
        });

        return packages;
    }

    @Override
    public List<Package> selectPackagesWithPostalCode(int postalCode) {
        List<Package> packages = new ArrayList<>();

        connection.makeQuery((db) -> {
            PreparedStatement packagesWithDate = db.prepareStatement(Queries.SELECT_PACKAGES_WITH_POSTAL_CODE);
            packagesWithDate.setInt(1, postalCode);

            packages.addAll(toList(packagesWithDate.executeQuery(), (rs) -> new Package(
                    rs.getString("startDate"),
                    rs.getString("endDate"),
                    rs.getInt("R_roomNo"),
                    rs.getString("R_B_street"),
                    rs.getInt("R_B_streetNo"),
                    rs.getInt("R_B_postalCode"),
                    rs.getInt("costPerNight")
            )));
        });

        return packages;
    }

    @Override
    public List<Booking> getDatesWherePackageIsBooked(Package _package, String startDate, String endDate) {
        List<Booking> bookings = new ArrayList<>();

        connection.makeQuery((db) -> {
            PreparedStatement bookingsWithDates = db.prepareStatement(Queries.SELECT_BOOKINGS_WITH_GIVEN_DATES);
            bookingsWithDates.setDate(1, Date.valueOf(_package.getStartDate()));
            bookingsWithDates.setInt(2, _package.getRoomNo());
            bookingsWithDates.setString(3, _package.getStreet());
            bookingsWithDates.setInt(4, _package.getStreetNo());
            bookingsWithDates.setInt(5, _package.getPostalCode());
            bookingsWithDates.setDate(6, Date.valueOf(startDate));
            bookingsWithDates.setDate(7, Date.valueOf(endDate));

            bookings.addAll(toList(bookingsWithDates.executeQuery(), (rs) -> new Booking(
                    rs.getString("uuid"),
                    rs.getString("startDate"),
                    rs.getString("endDate")

            )));
        });

        return bookings;
    }

    @Override
    public List<Booking> selectBooking() {
        List<Booking> bookings = new ArrayList<>();

        connection.makeQuery((db) -> {
            ResultSet resultSet = db.createStatement()
                    .executeQuery(Queries.SELECT_BOOKINGS);

            bookings.addAll(toList(resultSet, (rs) ->
                    new Booking(
                            rs.getString("uuid"),
                            rs.getString("startDate"),
                            rs.getString("endDate"),
                            rs.getString("reviewMessage"),
                            rs.getInt("reviewStars")
                    )));
        });

        return bookings;
    }

    @Override
    public List<Service> selectExtraServicesOfPackage(Package _package) {
        List<Service> extraServices = new ArrayList<>();

        connection.makeQuery((db) -> {
            PreparedStatement resultSet = db.prepareStatement(Queries.SELECT_EXTRA_SERVICES_OF_PACKAGE);
            resultSet.setDate(1, Date.valueOf(_package.getStartDate()));
            resultSet.setInt(2, _package.getRoomNo());
            resultSet.setString(3, _package.getStreet());
            resultSet.setInt(4, _package.getStreetNo());
            resultSet.setInt(5, _package.getPostalCode());

            extraServices.addAll(toList(resultSet.executeQuery(), (rs) -> new Service(
                    rs.getInt("id"),
                    rs.getString("name")
            )));
        });

        return extraServices;
    }

    private <T> List<T> toList(ResultSet resultSet, SQL.SQLMapper<ResultSet, T> mapper) throws SQLException {
        List<T> elements = new ArrayList<>();

        while (resultSet.next()) {
            elements.add(mapper.map(resultSet));
        }

        return elements;
    }

    private static final class Queries {
        private static final String INSERT_PACKAGE = "INSERT INTO Package VALUES (?, ?, ?, ?, ?, ?, ?)";
        private static final String INSERT_OFFERS = "INSERT INTO Offers VALUES (?, ?, ?, ?, ?, ?)";
        private static final String INSERT_BOOKING = "INSERT INTO Booking VALUES (?, ?, ?)";
        private static final String INSERT_BOOKS = "INSERT INTO Books VALUES (?, ?)";
        private static final String INSERT_THE = "INSERT INTO The VALUES (?, ?, ?, ?, ?, ?);";
        private static final String INSERT_WITH = "INSERT INTO With_ VALUES (?, ?)";
        private static final String INSERT_WITH_EXTRA = "INSERT INTO WithExtra VALUES (?, ?)";
        private static final String SELECT_ROOMS = "SELECT * FROM Room";
        private static final String SELECT_PAYMENT_METHODS = "SELECT * FROM PaymentMethod";
        private static final String SELECT_PAYMENT_METHODS_OF_PACKAGE = "SELECT PM.code, PM.name\n" +
                "FROM Package AS P\n" +
                "JOIN Offers AS O ON P.startDate = O.Pac_startDate\n" +
                "\tAND P.R_roomNo = O.Pac_R_roomNo\n" +
                "\tAND P.R_B_street = O.Pac_R_B_street\n" +
                "\tAND P.R_B_streetNo = O.Pac_R_B_streetNo\n" +
                "\tAND P.R_B_postalCode = O.Pac_R_B_postalCode\n" +
                "JOIN PaymentMethod AS PM ON O.Pay_code = PM.code\n" +
                "WHERE P.startDate = ?\n" +
                "\tAND P.R_roomNo = ?\n" +
                "\tAND P.R_B_street = ?\n" +
                "\tAND P.R_B_streetNo = ?\n" +
                "\tAND P.R_B_postalCode = ?";
        private static final String SELECT_CUSTOMERS = "SELECT ssn, name, surname, mail\n" +
                "FROM Person AS P\n" +
                "JOIN ISA_C_P AS I ON P.ssn = I.P_ssn\n" +
                "JOIN Customer AS C ON I.C_mail = C.mail";
        private static final String SELECT_PACKAGES = "SELECT * FROM Package";
        private static final String SELECT_PACKAGES_WITH_POSTAL_CODE = "SELECT *\n" +
                "FROM Package\n" +
                "WHERE (? = r_b_postalCode)";
        private static final String SELECT_BOOKINGS_WITH_GIVEN_DATES = "SELECT *\n" +
                "FROM Booking AS B JOIN The AS T on B.uuid = T.B_uuid\n" +
                "WHERE T.P_startDate = ?\n" +
                "\tAND T.P_R_roomNo = ?\n" +
                "\tAND T.P_R_B_street = ?\n" +
                "\tAND T.P_R_B_streetNo = ?\n" +
                "\tAND T.P_R_B_postalCode = ?\n" +
                "\tAND NOT ((B.endDate <= ?) OR (? <= B.startDate))";
        private static final String MAKE_A_REVIEW = "UPDATE Booking\n" +
                "SET reviewMessage = ?, reviewStars = ?\n" +
                "WHERE uuid = ?";
        private static final String SELECT_BOOKINGS = "SELECT * FROM Booking";
        private static final String SELECT_EXTRA_SERVICES_OF_PACKAGE = "SELECT S.id, S.name\n" +
                "FROM Package AS P\n" +
                "JOIN Room AS R ON P.R_roomNo = R.roomNo\n" +
                "AND P.R_B_street = R.B_street\n" +
                "AND P.R_B_streetNo = R.B_streetNo\n" +
                "AND P.R_B_postalCode = R.B_postalCode\n" +
                "JOIN HasExtra AS H ON R.B_street = H.R_B_street\n" +
                "AND R.B_streetNo = H.R_B_streetNo\n" +
                "AND R.B_postalCode = H.R_B_postalCode\n" +
                "JOIN Service AS S ON H.S_id = S.id\n" +
                "WHERE P.startDate = ? \n" +
                "AND P.R_roomNo = ? \n" +
                "AND P.R_B_street = ? \n" +
                "AND P.R_B_streetNo = ? \n" +
                "AND P.R_B_postalCode = ?";
    }
}