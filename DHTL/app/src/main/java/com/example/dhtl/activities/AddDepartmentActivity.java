package com.example.dhtl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dhtl.R;
import com.example.dhtl.firebase.FirebaseDatabaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddDepartmentActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    ImageView imgLogo;
    EditText edtDepartmentID, edtName, edtEmail, edtWebsite, edtAddress, edtPhone, edtParentID;
    Button btn_Add, btn_Back;
    FirebaseDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_department);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new FirebaseDatabaseHelper();
        imgLogo = findViewById(R.id.imgLogo);
        edtDepartmentID = findViewById(R.id.edtDepartmentID);
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtWebsite = findViewById(R.id.edtWebsite);
        edtAddress = findViewById(R.id.edtAddress);
        edtPhone = findViewById(R.id.edtPhone);
        edtParentID = findViewById(R.id.edtParentID);
        btn_Add = findViewById(R.id.btn_Add);
        btn_Back = findViewById(R.id.btn_Back);

        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDepartment();
            }
        });
        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddDepartmentActivity.this, DepartmentsView.class);
                startActivity(intent);
            }
        });
    }

    private void addDepartment() {
        String id = edtDepartmentID.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String website = edtWebsite.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String departmentID = edtParentID.getText().toString().trim();

        if (id.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        dbHelper.addDepartment(id, name, email, website, address, phone, departmentID, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AddDepartmentActivity.this, "Thêm đơn vị thành công", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);  // Thiết lập kết quả thành công
                    finish();  // Đóng Activity
                } else {
                    Toast.makeText(AddDepartmentActivity.this, "Thêm đơn vị thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}