package com.example.dhtl.models;

public class Department {
    private String departmentID, name, email, website, logo, address, phone, parentID;

    public Department() {

    }

    public Department(String departmentID, String name, String email, String website, String logo, String address, String phone, String parentID) {
        this.departmentID = departmentID;
        this.name = name;
        this.email = email;
        this.website = website;
        this.logo = logo;
        this.address = address;
        this.phone = phone;
        this.parentID = parentID;
    }

    public String getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(String departmentID) {
        this.departmentID = departmentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }
}
