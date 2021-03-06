package domain.dao;

import domain.entities.Package;
import domain.entities.*;

import java.util.List;

public interface BNBDao {

    void insertPackage(Package _package, PaymentMethod paymentMethod) throws RuntimeException;

    void insertBooking(Booking booking, Customer customer, Package _package, PaymentMethod paymentMethod, Service service) throws RuntimeException;

    void insertReview(Booking booking, String review, int stars) throws RuntimeException;

    List<Room> selectRooms();

    List<PaymentMethod> selectPaymentMethods();

    List<PaymentMethod> selectPaymentMethodsOfPackage(Package _package);

    List<Customer> selectCustomers();

    List<Package> selectPackages();

    List<Booking> selectBooking();

    List<Package> selectPackagesWithPostalCode(int postalCode);

    List<Booking> getDatesWherePackageIsBooked(Package _package, String startDate, String endDate);

    List<Service> selectExtraServicesOfPackage(Package _package);
}
