package com.example.dhtl.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dhtl.R;
import com.example.dhtl.firebase.FirebaseDatabaseHelper;
import com.example.dhtl.models.Staff;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class StaffsActivity extends AppCompatActivity {
    ImageView imgAvatar;
    TextView txtName, edtID;
    EditText edtName, edtPosition, edtEmail, edtPhone, edtDepartmentID;
    Button btnBack;
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

        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
        txtName = (TextView) findViewById(R.id.txtName);
        edtID = (TextView) findViewById(R.id.edtID);
        edtName = (EditText) findViewById(R.id.edtName);
        edtPosition = (EditText) findViewById(R.id.edtPosition);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtDepartmentID = (EditText) findViewById(R.id.edtDepartmentID);
        btnBack = (Button) findViewById(R.id.btnBack);

        String staffID = getIntent().getStringExtra("staffID");
        if (staffID != null) {
            getStaffDetails(staffID);
        }
        btnBack.setOnClickListener(v -> finish());
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
                    edtDepartmentID.setText(staff.getDepartmentID());
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