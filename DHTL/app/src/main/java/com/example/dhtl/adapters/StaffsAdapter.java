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
import com.example.dhtl.activities.MainActivity;
import com.example.dhtl.activities.StaffsActivity;
import com.example.dhtl.models.Staff;

import java.util.List;

public class StaffsAdapter extends RecyclerView.Adapter<StaffsAdapter.StaffViewHolder> {
    private List<Staff> staffs;
    private Context context;
    public StaffsAdapter(Context context, List<Staff> staffs) {
        this.staffs = staffs;
        this.context = context;
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

        // Giải mã chuỗi Base64 và hiển thị ảnh
        if (staff.getAvatar() != null && !staff.getAvatar().isEmpty()) {
            try {
                byte[] decodedString = Base64.decode(staff.getAvatar(), Base64.NO_WRAP);
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
            Intent intent = new Intent(context, StaffsActivity.class);
            intent.putExtra("staffID", staff.getStaffID());
            ((MainActivity) context).startActivityForResult(intent, MainActivity.UPDATE_STAFF_REQUEST_CODE);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return staffs.size();
    }

    public void filterList(List<Staff> filteredList) {
        staffs = filteredList;
        notifyDataSetChanged();
    }

    public static class StaffViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView textName;
        CardView cardView;
        public StaffViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            textName = itemView.findViewById(R.id.textName);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
