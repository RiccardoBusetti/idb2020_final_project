package domain.entities;

public class Customer extends Person {
    private String mail;

    public Customer(String ssn, String name, String surname, String mail) {
        super(ssn, name, surname);
        this.mail = mail;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "mail='" + mail + '\'' +
                '}';
    }
}
