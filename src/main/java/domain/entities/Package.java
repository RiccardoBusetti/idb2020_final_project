package domain.entities;

public class Package {
    private String startDate;
    private String endDate;
    private Integer roomNo;
    private String street;
    private Integer streetNo;
    private Integer postalCode;
    private Integer costPerNight;
    private Integer paymentMethod;

    public Package(String startDate, String endDate, Integer roomNo, String street, Integer streetNo, Integer postalCode, Integer costPerNight, Integer paymentMethod) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.roomNo = roomNo;
        this.street = street;
        this.streetNo = streetNo;
        this.postalCode = postalCode;
        this.costPerNight = costPerNight;
        this.paymentMethod = paymentMethod;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public Integer getCostPerNight() {
        return costPerNight;
    }

    public void setCostPerNight(Integer costPerNight) {
        this.costPerNight = costPerNight;
    }

    public Integer getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Integer paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        return "Package{" +
                "startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", roomNo=" + roomNo +
                ", street='" + street + '\'' +
                ", streetNo=" + streetNo +
                ", postalCode=" + postalCode +
                ", costPerNight=" + costPerNight +
                ", paymentMethod=" + paymentMethod +
                '}';
    }
}
