package com.example.dhtl.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dhtl.R;
import com.example.dhtl.models.Staff;

import java.util.List;

public class StaffsAdapter extends RecyclerView.Adapter<StaffsAdapter.StaffViewHolder> {
    private List<Staff> staffs;
    public StaffsAdapter(List<Staff> staffs) {
        this.staffs = staffs;
    }

    @NonNull
    @Override
    public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new StaffViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder holder, int position) {
        Staff staff = staffs.get(position);
        holder.textName.setText(staff.getName());
        // Load ảnh đại diện, bạn có thể sử dụng thư viện như Glide hoặc Picasso
        // Glide.with(holder.imageUser.getContext()).load(user.image).into(holder.imageUser);
    }

    @Override
    public int getItemCount() {
        return staffs.size();
    }

    public static class StaffViewHolder extends RecyclerView.ViewHolder {
        ImageView imgStaff;
        TextView textName;
        public StaffViewHolder(@NonNull View itemView) {
            super(itemView);
            imgStaff = itemView.findViewById(R.id.imgView);
            textName = itemView.findViewById(R.id.textName);
        }
    }
}
