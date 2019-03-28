package cn.design;

public class AddressBook implements Comparable {
    private String name;
    private String phoneNumber;
    private String address;
    private String emailAddress;
    private String remarks;

    public AddressBook() {

    }

    public AddressBook(String name, String phoneNumber, String address, String emailAddress, String remarks) {
        super();
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.emailAddress = emailAddress;
        this.remarks = remarks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "AddressBook{" + "name='" + name + '\'' + ", phoneNumber='" + phoneNumber + '\'' + ", address='"
                + address + '\'' + ", emailAddress='" + emailAddress + '\'' + ", remarks='" + remarks + '\'' + '}';
    }

    @Override
    public int compareTo(Object o) {// 大的排后面
        AddressBook addressBook = (AddressBook) o;
        if (this.name.compareTo(addressBook.name) > 0) {
            return 1;
        } else {
            return -1;
        }
    }
}
