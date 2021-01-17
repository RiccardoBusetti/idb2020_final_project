package domain.dao;

import data.postgres.DBConnection;
import domain.SQL;
import domain.entities.Package;
import domain.entities.PaymentMethod;
import domain.entities.Room;

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
    public void insertPackage(Package _package) {
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
            insertOffers.setInt(1, _package.getPaymentMethod());
            insertOffers.setDate(2, Date.valueOf(_package.getStartDate()));
            insertOffers.setInt(3, _package.getRoomNo());
            insertOffers.setString(4, _package.getStreet());
            insertOffers.setInt(5, _package.getStreetNo());
            insertOffers.setInt(6, _package.getPostalCode());
            insertOffers.executeUpdate();
        });
    }

    @Override
    public List<Room> selectRooms() {
        List<Room> rooms = new ArrayList<>();

        connection.makeQuery((db) -> {
            rooms.addAll(toList(db.createStatement().executeQuery(Queries.SELECT_ROOMS),
                    (rs) -> new Room(
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
            paymentMethods.addAll(toList(db.createStatement().executeQuery(Queries.SELECT_PAYMENT_METHODS),
                    (rs) -> new PaymentMethod(
                            rs.getInt("code"),
                            rs.getString("Name")
                    )));
        });

        return paymentMethods;
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
        private static final String SELECT_ROOMS = "SELECT * FROM Room";
        private static final String SELECT_PAYMENT_METHODS = "SELECT * FROM PaymentMethod";
    }
}
