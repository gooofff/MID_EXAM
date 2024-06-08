package com.example.dhtl.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dhtl.R;
import com.example.dhtl.firebase.FirebaseDatabaseHelper;
import com.example.dhtl.models.Staff;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StaffsActivity extends AppCompatActivity {
    ImageView imgAvatar;
    TextView txtName, edtID;
    EditText edtName, edtPosition, edtEmail, edtPhone;
    Spinner spinnerDepartmentID;
    FirebaseDatabaseHelper dbHelper;
    Button btnDelete, btnUpdate, btnBack;
    ArrayList<String> departmentIDs = new ArrayList<>();
    String selectedDepartmentID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_staffs);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imgAvatar = findViewById(R.id.imgAvatar);
        txtName = findViewById(R.id.txtName);
        edtID = findViewById(R.id.edtID);
        edtName = findViewById(R.id.edtName);
        edtPosition = findViewById(R.id.edtPosition);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        spinnerDepartmentID = findViewById(R.id.spinnerDepartmentID);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnBack = findViewById(R.id.btnBack);
        dbHelper = new FirebaseDatabaseHelper();

        loadDepartments();


        String staffID = getIntent().getStringExtra("staffID");
        if (staffID != null) {
            getStaffDetails(staffID);
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStaff();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(StaffsActivity.this)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa nhân viên này không?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String staffID = getIntent().getStringExtra("staffID");
                                if (staffID != null) {
                                    deleteStaff(staffID);
                                }
                            }
                        })
                        .setNegativeButton("Không", null)
                        .show();
            }
        });
        btnBack.setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
        });

        spinnerDepartmentID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDepartmentID = departmentIDs.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedDepartmentID = null;
            }
        });
    }

    private void getStaffDetails(String staffID) {
        FirebaseDatabaseHelper databaseHelper = new FirebaseDatabaseHelper();
        databaseHelper.getStaffsReference().child(staffID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Staff staff = snapshot.getValue(Staff.class);
                if (staff != null) {
                    txtName.setText(staff.getName());
                    edtID.setText(staff.getStaffID());
                    edtName.setText(staff.getName());
                    edtEmail.setText(staff.getEmail());
                    edtPhone.setText(staff.getPhone());
                    edtPosition.setText(staff.getPosition());
                    selectedDepartmentID = staff.getDepartmentID();
//                    Glide.with(StaffDetailActivity.this).load(staff.getAvatar()).into(imgStaff);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
                Toast.makeText(StaffsActivity.this, "Lỗi khi tải dữ liệu nhân viên", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDepartments() {
        dbHelper.getDepartmentsReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                departmentIDs.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String departmentID = snapshot.getKey();
                    departmentIDs.add(departmentID);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(StaffsActivity.this, android.R.layout.simple_spinner_item, departmentIDs);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerDepartmentID.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(StaffsActivity.this, "Lỗi khi tải dữ liệu departments", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStaff() {
        String id = edtID.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        String position = edtPosition.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String departmentID = selectedDepartmentID;

        if (id.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty() || departmentID.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        dbHelper.updateStaff(id, name, position, email, phone, departmentID, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(StaffsActivity.this, "Cập nhật nhân viên thành công", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);  // Thiết lập kết quả thành công
                    finish();  // Đóng Activity
                } else {
                    Toast.makeText(StaffsActivity.this, "Cập nhật nhân viên thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void deleteStaff(String staffID) {
        dbHelper.deleteStaff(staffID, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(StaffsActivity.this, "Xóa nhân viên thành công", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(StaffsActivity.this, "Xóa nhân viên thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}