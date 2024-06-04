package com.example.dhtl.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dhtl.R;
import com.example.dhtl.models.Department;

import java.util.List;

public class DepartmentsAdapter extends RecyclerView.Adapter<DepartmentsAdapter.DepartmentViewHolder> {
    private List<Department> departments;
    public DepartmentsAdapter(List<Department> departments) {
        this.departments = departments;
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
        // Load ảnh đại diện, bạn có thể sử dụng thư viện như Glide hoặc Picasso
        // Glide.with(holder.imageUser.getContext()).load(user.image).into(holder.imageUser);
    }

    @Override
    public int getItemCount() {
        return departments.size();
    }

    public static class DepartmentViewHolder extends RecyclerView.ViewHolder {
        ImageView imgStaff;
        TextView textName;
        public DepartmentViewHolder(@NonNull View itemView) {
            super(itemView);
            imgStaff = itemView.findViewById(R.id.imgView);
            textName = itemView.findViewById(R.id.textName);
        }
    }
}
