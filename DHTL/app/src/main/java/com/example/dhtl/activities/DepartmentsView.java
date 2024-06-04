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
import com.example.dhtl.adapters.DepartmentsAdapter;
import com.example.dhtl.adapters.StaffsAdapter;
import com.example.dhtl.firebase.FirebaseDatabaseHelper;
import com.example.dhtl.models.Department;
import com.example.dhtl.models.Staff;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DepartmentsView extends AppCompatActivity {
    Button btn_Staffs;
    TextView txt_Add;
    ImageView img_Add;
    RecyclerView recyclerView;
    private FirebaseDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_departments_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_Staffs = findViewById(R.id.btn_Staffs);
        txt_Add = findViewById(R.id.txt_Add);
        img_Add = findViewById(R.id.img_Add);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseHelper = new FirebaseDatabaseHelper();
        btn_Staffs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DepartmentsView.this, MainActivity.class);
                startActivity(intent);
            }
        });

        txt_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DepartmentsView.this, AddDepartmentActivity.class);
                startActivity(intent);
            }
        });

        img_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DepartmentsView.this, AddDepartmentActivity.class);
                startActivity(intent);
            }
        });

        databaseHelper.getDepartmentsReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Department> departments = new ArrayList<>();
                for (DataSnapshot tsnapshot : snapshot.getChildren()) {
                    Department department = tsnapshot.getValue(Department.class);
                    departments.add(department);
                }
                // Sắp xếp danh sách người dùng theo tên
                Collections.sort(departments, (u1, u2) -> u1.getName().compareToIgnoreCase(u2.getName()));
                // Hiển thị danh sách
                getDepartments(departments);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("RealtimeDatabase", "Error getting data: ", error.toException());
            }
        });
    }

    private void getDepartments(List<Department> departments){
        DepartmentsAdapter adapter = new DepartmentsAdapter(this, departments);
        recyclerView.setAdapter(adapter);
    }
}