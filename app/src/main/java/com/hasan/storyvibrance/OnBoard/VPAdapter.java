package com.hasan.storyvibrance.OnBoard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hasan.storyvibrance.R;

import java.util.ArrayList;

public class VPAdapter extends RecyclerView.Adapter<VPAdapter.ViewHolder> {
    private ArrayList<ViewPagerItem> viewPagerItemArrayList;
    Context context;

    public VPAdapter(Context context, ArrayList<ViewPagerItem> viewPagerItemArrayList) {
        this.viewPagerItemArrayList = viewPagerItemArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public VPAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.onboarding_viewpager_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VPAdapter.ViewHolder holder, int position) {
        ViewPagerItem viewPagerItem = viewPagerItemArrayList.get(position);
        holder.tvHeading.setText(viewPagerItem.getHeading());
        holder.tvDesc.setText(viewPagerItem.getDescription());
    }

    @Override
    public int getItemCount() {
        return viewPagerItemArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHeading, tvDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHeading = itemView.findViewById(R.id.tvHeading);
            tvDesc = itemView.findViewById(R.id.tvDesc);
        }
    }
}
