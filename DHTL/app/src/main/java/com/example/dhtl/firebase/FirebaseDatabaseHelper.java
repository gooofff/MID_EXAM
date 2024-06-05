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

    public void addStaff(String id, String name, String position, String email, String phone, String departmentID, OnCompleteListener<Void> onCompleteListener) {
        Map<String, Object> staff = new HashMap<>();
        staff.put("id", id);
        staff.put("name", name);
        staff.put("position", position);
        staff.put("email", email);
        staff.put("phone", phone);
        staff.put("departmentID", departmentID);

        staffsRef.child(id).setValue(staff).addOnCompleteListener(onCompleteListener);
    }

}
