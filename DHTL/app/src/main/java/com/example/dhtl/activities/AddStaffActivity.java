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

public class AddStaffActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    ImageView imgAvatar;
    EditText edtID, edtName, edtPhone, edtPosition, edtEmail;
    TextView txtAdd;
    Spinner spinnerDepartmentID;
    Button btn_Add, btn_Back;
    FirebaseDatabaseHelper dbHelper;
    ArrayList<Department> departments = new ArrayList<>();
    String selectedDepartmentID;
    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_staff);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new FirebaseDatabaseHelper();

        imgAvatar = findViewById(R.id.imgAvatar);
        edtID = findViewById(R.id.edtID);
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtPosition = findViewById(R.id.edtPosition);
        edtEmail = findViewById(R.id.edtEmail);
        txtAdd = findViewById(R.id.txtAdd);
        spinnerDepartmentID = findViewById(R.id.spinnerDepartmentID);
        btn_Add = findViewById(R.id.btn_Add);
        btn_Back = findViewById(R.id.btn_Back);

        loadDepartments();

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        spinnerDepartmentID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Department selectedDepartment = (Department) parent.getItemAtPosition(position);
                selectedDepartmentID = selectedDepartment.getDepartmentID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedDepartmentID = null;
            }
        });
        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStaff();
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
            imgAvatar.setImageURI(selectedImageUri);
            txtAdd.setVisibility(View.GONE);
        }
    }

    private void addStaff() {
        String id = edtID.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        String position = edtPosition.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String departmentID = selectedDepartmentID;

        if (id.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty() || departmentID == null || departmentID.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        String avatarBase64 = "";
        if (selectedImageUri != null) {
            avatarBase64 = encodeImageToBase64();
        }

        dbHelper.addStaff(id, name, position, email, phone, departmentID, avatarBase64, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AddStaffActivity.this, "Thêm nhân viên thành công", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);  // Thiết lập kết quả thành công
                    finish();  // Đóng Activity
                } else {
                    Toast.makeText(AddStaffActivity.this, "Thêm nhân viên thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String encodeImageToBase64() {
        BitmapDrawable drawable = (BitmapDrawable) imgAvatar.getDrawable();
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
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String departmentID = snapshot.getKey();
                    String departmentName = snapshot.child("name").getValue(String.class);
                    if (departmentID != null && departmentName != null) {
                        departments.add(new Department(departmentID, departmentName));
                    }
                }

                ArrayAdapter<Department> adapter = new ArrayAdapter<>(AddStaffActivity.this, android.R.layout.simple_spinner_item, departments);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerDepartmentID.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddStaffActivity.this, "Lỗi khi tải dữ liệu departments", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
