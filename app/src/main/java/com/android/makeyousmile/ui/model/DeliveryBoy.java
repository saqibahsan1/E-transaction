package com.android.makeyousmile.ui.model;

public class DeliveryBoy {

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }



    private String email;
    private String name;
    private String address;
    private String cnic;
    private String contactNumber;
    private String liecence;
    private String vechicle;

    public String getLiecence() {
        return liecence;
    }

    public void setLiecence(String liecence) {
        this.liecence = liecence;
    }

    public String getVechicle() {
        return vechicle;
    }

    public void setVechicle(String vechicle) {
        this.vechicle = vechicle;
    }
}
