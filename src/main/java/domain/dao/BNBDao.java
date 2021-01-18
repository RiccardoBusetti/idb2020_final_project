package domain.dao;

import domain.entities.Package;
import domain.entities.*;

import java.util.List;

public interface BNBDao {

    void insertPackage(Package _package, PaymentMethod paymentMethod);

    void insertBooking(Booking booking, Package _package, PaymentMethod paymentMethod);

    List<Room> selectRooms();

    List<PaymentMethod> selectPaymentMethods();

    List<Customer> selectCustomers();
}
