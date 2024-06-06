package com.example.dhtl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dhtl.R;
import com.example.dhtl.adapters.StaffsAdapter;
import com.example.dhtl.firebase.FirebaseDatabaseHelper;
import com.example.dhtl.models.Staff;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static final int ADD_STAFF_REQUEST_CODE = 1;
    public static final int UPDATE_STAFF_REQUEST_CODE = 2;
    Button btn_Departments;
    TextView txt_Add;
    ImageView img_Add;
    RecyclerView recyclerView;
    private FirebaseDatabaseHelper databaseHelper;
    private List<Staff> staffs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_Departments = findViewById(R.id.btn_Departments);
        txt_Add = findViewById(R.id.txt_Add);
        img_Add = findViewById(R.id.img_Add);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseHelper = new FirebaseDatabaseHelper();
        staffs = new ArrayList<>();

        btn_Departments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DepartmentsView.class);
                startActivity(intent);
            }
        });

        txt_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddStaffActivity.class);
                startActivityForResult(intent, ADD_STAFF_REQUEST_CODE);
            }
        });

        img_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddStaffActivity.class);
                startActivityForResult(intent, ADD_STAFF_REQUEST_CODE);
            }
        });

        loadStaffs();
    }

    private void loadStaffs() {
        databaseHelper.getStaffsReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                staffs.clear();
                for (DataSnapshot tsnapshot : snapshot.getChildren()) {
                    Staff staff = tsnapshot.getValue(Staff.class);
                    staffs.add(staff);
                }
                // Sắp xếp danh sách người dùng theo tên
                Collections.sort(staffs, (u1, u2) -> u1.getName().compareToIgnoreCase(u2.getName()));
                // Hiển thị danh sách
                getStaffs(staffs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("RealtimeDatabase", "Error getting data: ", error.toException());
            }
        });
    }

    private void getStaffs(List<Staff> staffs){
        StaffsAdapter adapter = new StaffsAdapter(MainActivity.this, staffs);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // Tải lại danh sách nhân viên
            loadStaffs();
        }
    }
}