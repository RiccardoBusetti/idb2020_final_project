import domain.dao.BNBDao;
import domain.entities.Package;
import domain.entities.PaymentMethod;
import domain.entities.Room;
import utils.ConsoleUtils;

import java.util.List;

public class BNBApplication {

    private final BNBDao dao;

    public BNBApplication(BNBDao dao) {
        this.dao = dao;
    }

    public void insertPackage() {
        List<Room> rooms = dao.selectRooms();
        Room selectedRoom = rooms.get(
                ConsoleUtils.indexedSelection("SELECT A ROOM: ", rooms));

        List<PaymentMethod> paymentMethod = dao.selectPaymentMethods();
        PaymentMethod selectedPaymentMethod = paymentMethod.get(
                ConsoleUtils.indexedSelection("SELECT A PAYMENT METHOD: ", paymentMethod));

        dao.insertPackage(new Package(
                "2020-05-01",
                "2020-06-01",
                selectedRoom.getRoomNo(),
                selectedRoom.getStreet(),
                selectedRoom.getStreetNo(),
                selectedRoom.getPostalCode(),
                0,
                selectedPaymentMethod.getCode()
        ));
    }
}
