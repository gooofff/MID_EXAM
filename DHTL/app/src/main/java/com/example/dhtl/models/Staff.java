package com.example.dhtl.models;

public class Staff {
    private String staffID, name, position, email, phone, avatar, departmentID;

    public Staff() {

    }

    public Staff(String staffID, String name, String position, String email, String phone, String avatar, String departmentID) {
        this.staffID = staffID;
        this.name = name;
        this.position = position;
        this.email = email;
        this.phone = phone;
        this.avatar = avatar;
        this.departmentID = departmentID;
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(String departmentID) {
        this.departmentID = departmentID;
    }
}
