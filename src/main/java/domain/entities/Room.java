package domain.entities;

public class Room {
    private Integer roomNo;
    private String street;
    private Integer streetNo;
    private Integer postalCode;
    private Integer maxPeople;
    private Integer m2;

    public Room(Integer roomNo, String street, Integer streetNo, Integer postalCode, Integer maxPeople, Integer m2) {
        this.roomNo = roomNo;
        this.street = street;
        this.streetNo = streetNo;
        this.postalCode = postalCode;
        this.maxPeople = maxPeople;
        this.m2 = m2;
    }

    public Integer getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(Integer roomNo) {
        this.roomNo = roomNo;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getStreetNo() {
        return streetNo;
    }

    public void setStreetNo(Integer streetNo) {
        this.streetNo = streetNo;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public Integer getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(Integer maxPeople) {
        this.maxPeople = maxPeople;
    }

    public Integer getM2() {
        return m2;
    }

    public void setM2(Integer m2) {
        this.m2 = m2;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomNo=" + roomNo +
                ", street='" + street + '\'' +
                ", streetNo=" + streetNo +
                ", postalCode=" + postalCode +
                ", maxPeople=" + maxPeople +
                ", m2=" + m2 +
                '}';
    }
}
