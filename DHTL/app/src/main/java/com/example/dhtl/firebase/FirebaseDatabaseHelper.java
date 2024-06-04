package com.example.dhtl.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class FirebaseDatabaseHelper {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference staffsRef = database.getReference("DHTL/staffs");
    DatabaseReference departmentsRef = database.getReference("DHTL/departments");

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

}
