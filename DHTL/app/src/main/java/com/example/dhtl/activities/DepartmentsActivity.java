package com.example.dhtl.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dhtl.R;
import com.example.dhtl.firebase.FirebaseDatabaseHelper;
import com.example.dhtl.models.Department;
import com.example.dhtl.models.Staff;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class DepartmentsActivity extends AppCompatActivity {
    ImageButton imgLogo;
    TextView txtName;
    EditText edtID, edtName, edtEmail, edtWebsite, edtAddress, edtPhone, edtParentID;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_departments);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imgLogo = (ImageButton) findViewById(R.id.imgLogo);
        txtName = (TextView) findViewById(R.id.txtName);
        edtID = (EditText) findViewById(R.id.edtID);
        edtName = (EditText) findViewById(R.id.edtName);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtWebsite = (EditText) findViewById(R.id.edtWebsite);
        edtAddress = (EditText) findViewById(R.id.edtAddress);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtParentID = (EditText) findViewById(R.id.edtParentID);
        btnBack = (Button) findViewById(R.id.btnBack);

        String departmentID = getIntent().getStringExtra("departmentID");
        if (departmentID != null) {
            getDepartmentDetails(departmentID);
        }
        btnBack.setOnClickListener(v -> finish());

    }

    private void getDepartmentDetails(String staffID) {
        FirebaseDatabaseHelper databaseHelper = new FirebaseDatabaseHelper();
        databaseHelper.getDepartmentsReference().child(staffID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Department department = snapshot.getValue(Department.class);
                if (department != null) {
                    txtName.setText(department.getName());
                    edtID.setText(department.getDepartmentID());
                    edtName.setText(department.getName());
                    edtEmail.setText(department.getEmail());
                    edtWebsite.setText(department.getWebsite());
                    edtAddress.setText(department.getAddress());
                    edtPhone.setText(department.getPhone());
                    edtParentID.setText(department.getParentID());
//                    Glide.with(StaffDetailActivity.this).load(staff.getAvatar()).into(imgStaff);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }
}