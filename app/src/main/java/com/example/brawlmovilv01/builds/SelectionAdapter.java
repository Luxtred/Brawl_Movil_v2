package com.example.brawlmovilv01.builds;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.brawlmovilv01.R;

import java.util.ArrayList;
import java.util.List;

public class SelectionAdapter extends RecyclerView.Adapter<SelectionAdapter.ViewHolder> {

    public static class ItemSeleccionable {
        public int id;
        public String urlImagen;
        public boolean isSelected = false;

        public ItemSeleccionable(int id, String url) {
            this.id = id;
            this.urlImagen = url;
        }
    }

    public interface OnItemSelectedListener {
        void onSelectionChanged(List<Integer> selectedIds);
    }

    private final Context context;
    private final List<ItemSeleccionable> items;
    private final boolean multiSelect;
    private final int maxSelection;
    private final OnItemSelectedListener listener;

    public SelectionAdapter(Context context, List<ItemSeleccionable> items, boolean multiSelect, OnItemSelectedListener listener) {
        this.context = context;
        this.items = items;
        this.multiSelect = multiSelect;
        this.maxSelection = multiSelect ? 2 : 1;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_selection_component, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemSeleccionable item = items.get(position);

        Glide.with(context)
                .load(item.urlImagen)
                .placeholder(R.drawable.placeholder)
                .into(holder.ivIcon);

        holder.bgSelection.setVisibility(item.isSelected ? View.VISIBLE : View.INVISIBLE);

        holder.itemView.setOnClickListener(v -> toggleSelection(position));
    }

    private void toggleSelection(int position) {
        ItemSeleccionable item = items.get(position);

        if (multiSelect) {
            if (item.isSelected) {
                item.isSelected = false;
            } else {
                int count = 0;
                for (ItemSeleccionable i : items) {
                    if (i.isSelected) count++;
                }

                if (count < maxSelection) {
                    item.isSelected = true;
                } else {
                    return;
                }
            }
        } else {
            for (ItemSeleccionable i : items) {
                i.isSelected = false;
            }
            item.isSelected = true;
        }

        notifyDataSetChanged();

        if (listener != null) {
            List<Integer> selectedIds = new ArrayList<>();
            for (ItemSeleccionable i : items) {
                if (i.isSelected) selectedIds.add(i.id);
            }
            listener.onSelectionChanged(selectedIds);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        View bgSelection;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_component_icon);
            bgSelection = itemView.findViewById(R.id.view_selection_bg);
        }
    }
}