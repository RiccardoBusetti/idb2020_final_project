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
        connection.makeTransaction(true, (db) -> {
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
    public void insertBooking(Booking booking, Customer customer, Package _package, PaymentMethod paymentMethod) {
        connection.makeTransaction(true, (db) -> {
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
    }
}
