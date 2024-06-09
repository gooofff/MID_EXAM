package com.example.dhtl.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dhtl.R;
import com.example.dhtl.activities.DepartmentsActivity;
import com.example.dhtl.activities.DepartmentsView;
import com.example.dhtl.activities.MainActivity;
import com.example.dhtl.activities.StaffsActivity;
import com.example.dhtl.models.Department;
import com.example.dhtl.models.Staff;

import java.util.List;

public class DepartmentsAdapter extends RecyclerView.Adapter<DepartmentsAdapter.DepartmentViewHolder> {
    private List<Department> departments;
    private Context context;
    public DepartmentsAdapter(Context context, List<Department> departments) {
        this.departments = departments;
        this.context = context;
    }

    @NonNull
    @Override
    public DepartmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new DepartmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentViewHolder holder, int position) {
        Department department = departments.get(position);
        holder.textName.setText(department.getName());

        // Giải mã chuỗi Base64 và hiển thị ảnh
        if (department.getLogo() != null && !department.getLogo().isEmpty()) {
            try {
                byte[] decodedString = Base64.decode(department.getLogo(), Base64.NO_WRAP);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.imgAvatar.setImageBitmap(decodedByte);
            } catch (IllegalArgumentException e) {
                // Xử lý lỗi giải mã Base64 không hợp lệ ở đây (ví dụ: hiển thị một ảnh mặc định)
                e.printStackTrace();
                holder.imgAvatar.setImageResource(R.drawable.ic_user);
            }
        } else {
            holder.imgAvatar.setImageResource(R.drawable.ic_user); // Ảnh mặc định nếu không có ảnh
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DepartmentsActivity.class);
            intent.putExtra("departmentID", department.getDepartmentID());
            ((DepartmentsView) context).startActivityForResult(intent, DepartmentsView.UPDATE_DEPARTMENT_REQUEST_CODE);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return departments.size();
    }

    public void filterList(List<Department> filteredList) {
        departments = filteredList;
        notifyDataSetChanged();
    }

    public static class DepartmentViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView textName;
        CardView cardView;
        public DepartmentViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            textName = itemView.findViewById(R.id.textName);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
