package domain.dao;

import domain.entities.Package;
import domain.entities.*;

import java.util.List;

public interface BNBDao {

    void insertPackage(Package _package, PaymentMethod paymentMethod);

    void insertBooking(Booking booking, Customer customer, Package _package, PaymentMethod paymentMethod);

    List<Room> selectRooms();

    List<PaymentMethod> selectPaymentMethods();

    List<PaymentMethod> selectPaymentMethodsOfPackage(Package _package);

    List<Customer> selectCustomers();

    List<Package> selectPackages();

    List<Package> selectPackagesWithGiveDate(String startDate, String endDate);
}
