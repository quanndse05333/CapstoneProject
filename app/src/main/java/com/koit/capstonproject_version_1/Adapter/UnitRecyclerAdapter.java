package com.koit.capstonproject_version_1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;

import java.util.ArrayList;

public class UnitRecyclerAdapter  extends RecyclerView.Adapter<UnitRecyclerAdapter.ViewHolder>{
    ArrayList<Unit> unitArrayList;
    Context context;

    public UnitRecyclerAdapter() {
    }

    public UnitRecyclerAdapter(ArrayList<Unit> unitArrayList, Context context) {
        this.unitArrayList = unitArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.custom_product_unit_layout,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvUnitName.setText("1 " + unitArrayList.get(position).getUnitName());
        holder.tvUnitPrice.setText(unitArrayList.get(position).getUnitPrice() + " VNĐ");
    }

    @Override
    public int getItemCount() {
        return unitArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUnitName, tvUnitPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUnitName = (TextView) itemView.findViewById(R.id.tvUnitName);
            tvUnitPrice = (TextView) itemView.findViewById(R.id.tvUnitPrice);
        }
    }
}
