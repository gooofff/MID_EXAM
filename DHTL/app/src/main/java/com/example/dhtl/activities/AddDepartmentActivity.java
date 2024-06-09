package com.example.dhtl.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dhtl.R;
import com.example.dhtl.firebase.FirebaseDatabaseHelper;
import com.example.dhtl.models.Department;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class AddDepartmentActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    ImageView imgLogo;
    EditText edtDepartmentID, edtName, edtEmail, edtWebsite, edtAddress, edtPhone, edtParentID;
    TextView txtAdd;
    Spinner spinnerParentID;
    Button btn_Add, btn_Back;
    FirebaseDatabaseHelper dbHelper;
    ArrayList<Department> departments = new ArrayList<>();
    String selectedDepartmentID;
    Uri selectedImageUri;

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
        txtAdd = findViewById(R.id.txtAdd);
        spinnerParentID = findViewById(R.id.spinnerParentID);
        btn_Add = findViewById(R.id.btn_Add);
        btn_Back = findViewById(R.id.btn_Back);

        loadDepartments();

        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        spinnerParentID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    selectedDepartmentID = "null"; // Không chọn parentID
                } else {
                    Department selectedDepartment = (Department) parent.getItemAtPosition(position);
                    selectedDepartmentID = selectedDepartment.getDepartmentID();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedDepartmentID = null;
            }
        });

        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDepartment();
            }
        });
        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            imgLogo.setImageURI(selectedImageUri);
            txtAdd.setVisibility(View.GONE);
        }
    }

    private void addDepartment() {
        String id = edtDepartmentID.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String website = edtWebsite.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String departmentID = selectedDepartmentID;

        if (id.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (website.isEmpty()) {
            website = "null";
        }

        String logoBase64 = "";
        if (selectedImageUri != null) {
            logoBase64 = encodeImageToBase64();
        }

        dbHelper.addDepartment(id, name, email, website, address, phone, departmentID, logoBase64, new OnCompleteListener<Void>() {
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

    private String encodeImageToBase64() {
        BitmapDrawable drawable = (BitmapDrawable) imgLogo.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void loadDepartments() {
        dbHelper.getDepartmentsReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                departments.clear();
                departments.add(new Department("", "Không có")); // Thêm mục "Không có" vào đầu danh sách
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String departmentID = snapshot.getKey();
                    String departmentName = snapshot.child("name").getValue(String.class);
                    if (departmentID != null && departmentName != null) {
                        departments.add(new Department(departmentID, departmentName));
                    }
                }

                ArrayAdapter<Department> adapter = new ArrayAdapter<>(AddDepartmentActivity.this, android.R.layout.simple_spinner_item, departments);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerParentID.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddDepartmentActivity.this, "Lỗi khi tải dữ liệu departments", Toast.LENGTH_SHORT).show();
            }
        });
    }
}