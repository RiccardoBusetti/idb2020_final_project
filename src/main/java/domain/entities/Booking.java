package domain.entities;

import java.awt.print.Book;

public class Booking {
    private String uuid;
    private String startDate;
    private String endDate;
    private String reviewMessage;
    private int reviewStars;

    public Booking(String uuid, String startDate, String endDate) {
        this.uuid = uuid;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Booking(String uuid, String startDate, String endDate, String reviewMessage, int reviewStars) {
        this.uuid = uuid;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reviewMessage = reviewMessage;
        this.reviewStars = reviewStars;
    }

    public String getUUID() {
        return uuid;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
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

    public String getReviewMessage() {
        return reviewMessage;
    }

    public void setReviewMessage(String reviewMessage) {
        this.reviewMessage = reviewMessage;
    }

    public int getReviewStars() {
        return reviewStars;
    }

    public void setReviewStars(int reviewStars) {
        this.reviewStars = reviewStars;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "uuid='" + uuid + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", reviewMessage='" + reviewMessage + '\'' +
                ", reviewStars=" + reviewStars +
                '}';
    }
}
