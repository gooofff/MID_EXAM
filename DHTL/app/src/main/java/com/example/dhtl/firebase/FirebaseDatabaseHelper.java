package com.example.dhtl.firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirebaseDatabaseHelper {
    FirebaseDatabase database;
    DatabaseReference staffsRef;
    DatabaseReference departmentsRef;

    public FirebaseDatabaseHelper() {
        database = FirebaseDatabase.getInstance();
        staffsRef = database.getReference("DHTL/staffs");
        departmentsRef = database.getReference("DHTL/departments");
    }

    public DatabaseReference getStaffsReference() {
        return staffsRef;
    }
    public DatabaseReference getDepartmentsReference() {
        return departmentsRef;
    }

    public void addStaff(String id, String name, String position, String email, String phone, String departmentID, String avatar, OnCompleteListener<Void> onCompleteListener) {
        Map<String, Object> staff = new HashMap<>();
        staff.put("staffID", id);
        staff.put("name", name);
        staff.put("position", position);
        staff.put("email", email);
        staff.put("phone", phone);
        staff.put("departmentID", departmentID);
        staff.put("avatar", avatar);

        staffsRef.child(id).setValue(staff).addOnCompleteListener(onCompleteListener);
    }

    public void addDepartment(String id, String name, String email, String website, String address, String phone, String departmentID, OnCompleteListener<Void> onCompleteListener) {
        Map<String, Object> department = new HashMap<>();
        department.put("departmentID", id);
        department.put("name", name);
        department.put("email", email);
        department.put("website", website);
        department.put("address", address);
        department.put("phone", phone);
        department.put("parentID", departmentID);

        departmentsRef.child(id).setValue(department).addOnCompleteListener(onCompleteListener);
    }

    public void updateStaff(String id, String name, String position, String email, String phone, String departmentID, String avatarBase64, OnCompleteListener<Void> onCompleteListener) {
        Map<String, Object> staff = new HashMap<>();
        staff.put("staffID", id);
        staff.put("name", name);
        staff.put("position", position);
        staff.put("email", email);
        staff.put("phone", phone);
        staff.put("departmentID", departmentID);
        staff.put("avatar", avatarBase64);

        staffsRef.child(id).setValue(staff).addOnCompleteListener(onCompleteListener);
    }

    public void deleteStaff(String staffID, OnCompleteListener<Void> onCompleteListener) {
        staffsRef.child(staffID).removeValue().addOnCompleteListener(onCompleteListener);
    }
}
