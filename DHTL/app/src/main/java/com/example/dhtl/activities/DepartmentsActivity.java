package com.example.dhtl.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
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
import com.example.dhtl.models.Department;
import com.example.dhtl.models.Staff;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DepartmentsActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    ImageView imgLogo;
    TextView txtName, edtID;
    EditText edtName, edtEmail, edtWebsite, edtAddress, edtPhone, edtParentID;
    Spinner spinnerParentID;
    FirebaseDatabaseHelper dbHelper;
    Button btnDelete, btnUpdate, btnBack;
    ArrayList<Department> departments = new ArrayList<>();
    String selectedDepartmentID;
    Uri selectedImageUri;

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

        imgLogo = findViewById(R.id.imgLogo);
        txtName = findViewById(R.id.txtName);
        edtID = findViewById(R.id.edtID);
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtWebsite = findViewById(R.id.edtWebsite);
        edtAddress =  findViewById(R.id.edtAddress);
        edtPhone =  findViewById(R.id.edtPhone);
        spinnerParentID = findViewById(R.id.spinnerParentID);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnBack = findViewById(R.id.btnBack);
        dbHelper = new FirebaseDatabaseHelper();

        loadDepartmentsAndSetSelection();

        String departmentID = getIntent().getStringExtra("departmentID");
        if (departmentID != null) {
            getDepartmentDetails(departmentID);
        }

        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDepartment();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DepartmentsActivity.this)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa đơn vị này không?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String departmentID = getIntent().getStringExtra("departmentID");
                                if (departmentID != null) {
                                    deleteDepartment(departmentID);
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

        spinnerParentID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        }
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
                    if (department.getWebsite().equals("null")) {
                        edtWebsite.setText("Không có");
                    }
                    else {
                        edtWebsite.setText(department.getWebsite());
                    }
                    edtAddress.setText(department.getAddress());
                    edtPhone.setText(department.getPhone());
                    if (department.getParentID() == null || department.getParentID().equals("null")) {
                        selectedDepartmentID = "null";
                    }
                    else {
                        selectedDepartmentID = department.getParentID();
                    }

                    if (department.getLogo() != null && !department.getLogo().isEmpty()) {
                        try {
                            byte[] decodedString = Base64.decode(department.getLogo(), Base64.NO_WRAP);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            imgLogo.setImageBitmap(decodedByte);
                        } catch (IllegalArgumentException e) {
                            // Xử lý lỗi giải mã Base64 không hợp lệ ở đây (ví dụ: hiển thị một ảnh mặc định)
                            e.printStackTrace();
                            imgLogo.setImageResource(R.drawable.ic_user);
                        }
                    } else {
                        imgLogo.setImageResource(R.drawable.ic_user); // Ảnh mặc định nếu không có ảnh
                    }
                    loadDepartmentsAndSetSelection();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
                Toast.makeText(DepartmentsActivity.this, "Lỗi khi tải dữ liệu đơn vị", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDepartmentsAndSetSelection() {
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

                ArrayAdapter<Department> adapter = new ArrayAdapter<>(DepartmentsActivity.this, android.R.layout.simple_spinner_item, departments);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerParentID.setAdapter(adapter);

                setSpinnerSelection();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DepartmentsActivity.this, "Lỗi khi tải dữ liệu departments", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSpinnerSelection() {
        for (int i = 0; i < departments.size(); i++) {
            if (departments.get(i).getDepartmentID().equals(selectedDepartmentID)) {
                spinnerParentID.setSelection(i);
                break;
            }
        }
    }

    private void updateDepartment() {
        String id = edtID.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String website = edtWebsite.getText().toString().trim();
        if (website.equals("Không có")) {
            website = "null";
        }
        String address = edtAddress.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String departmentID = selectedDepartmentID;

        if (id.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()|| departmentID.isEmpty() ) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        String logoBase64 = "";
        if (selectedImageUri != null) {
            logoBase64 = encodeImageToBase64();
        }
        else {
            // Lấy avatar hiện tại từ ImageView nếu không có ảnh mới được chọn
            BitmapDrawable drawable = (BitmapDrawable) imgLogo.getDrawable();
            if (drawable != null) {
                logoBase64 = encodeImageToBase64();
            }
        }

        dbHelper.updateDepartment(id, name, email, website, address, phone, departmentID, logoBase64, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(DepartmentsActivity.this, "Cập nhật đơn vị thành công", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);  // Thiết lập kết quả thành công
                    finish();  // Đóng Activity
                } else {
                    Toast.makeText(DepartmentsActivity.this, "Cập nhật đơn vị thất bại", Toast.LENGTH_SHORT).show();
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

    private void deleteDepartment(String departmentID) {
        dbHelper.deleteDepartment(departmentID, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(DepartmentsActivity.this, "Xóa đơn vị thành công", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(DepartmentsActivity.this, "Xóa đơn vị thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}