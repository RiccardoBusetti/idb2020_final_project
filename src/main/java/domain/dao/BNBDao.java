package domain.dao;

import domain.entities.Package;
import domain.entities.PaymentMethod;
import domain.entities.Room;

import java.util.List;

public interface BNBDao {

    void insertPackage(Package _package);

    List<Room> selectRooms();

    List<PaymentMethod> selectPaymentMethods();
}
