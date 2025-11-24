package com.example.brawlmovilv01.brawlers;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.brawlmovilv01.datos.modelos.Brawler;
import com.example.brawlmovilv01.R;

import java.util.ArrayList;
import java.util.List;

public class BrawlerAdapter extends RecyclerView.Adapter<BrawlerAdapter.BrawlerViewHolder> implements Filterable {

    public interface OnBrawlerClickListener {
        void onBrawlerClick(int brawlerId);
    }

    private final OnBrawlerClickListener clickListener;
    private final Context context;
    private List<Brawler> brawlerList;
    private List<Brawler> brawlerListFull;
    private final int defaultColor;

    public BrawlerAdapter(Context context, List<Brawler> brawlerList, OnBrawlerClickListener clickListener) {
        this.context = context;
        this.brawlerList = brawlerList;
        this.brawlerListFull = new ArrayList<>(brawlerList);
        this.defaultColor = Color.LTGRAY;
        this.clickListener = clickListener;
    }

    public void setBrawlerList(List<Brawler> brawlerList) {
        this.brawlerList = brawlerList;
        this.brawlerListFull = new ArrayList<>(brawlerList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BrawlerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_brawler, parent, false);
        return new BrawlerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrawlerViewHolder holder, int position) {
        Brawler brawler = brawlerList.get(position);

        holder.tvBrawlerName.setText(brawler.getNombre());

        Glide.with(context)
                .load(brawler.getImagen1())
                .placeholder(R.drawable.placeholder)
                .into(holder.ivBrawlerImage);

        String colorHex = brawler.getColorRareza();
        try {
            if (colorHex != null && !colorHex.isEmpty()) {
                int color = Color.parseColor(colorHex);
                holder.cardBrawlerBackground.setCardBackgroundColor(color);
            } else {
                holder.cardBrawlerBackground.setCardBackgroundColor(defaultColor);
            }
        } catch (IllegalArgumentException e) {
            Log.e("BrawlerAdapter", "Color inválido: " + colorHex);
            holder.cardBrawlerBackground.setCardBackgroundColor(defaultColor);
        }

        holder.itemView.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION && clickListener != null) {
                clickListener.onBrawlerClick(brawlerList.get(currentPosition).getIdBrawler());
            }
        });
    }

    @Override
    public int getItemCount() {
        return brawlerList.size();
    }

    @Override
    public Filter getFilter() {
        return brawlerFilter;
    }

    private final Filter brawlerFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Brawler> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(brawlerListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Brawler brawler : brawlerListFull) {
                    if (brawler.getNombre().toLowerCase().startsWith(filterPattern)) {
                        filteredList.add(brawler);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            brawlerList.clear();
            brawlerList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public static class BrawlerViewHolder extends RecyclerView.ViewHolder {
        CardView cardBrawlerBackground;
        ImageView ivBrawlerImage;
        TextView tvBrawlerName;

        public BrawlerViewHolder(@NonNull View itemView) {
            super(itemView);
            cardBrawlerBackground = itemView.findViewById(R.id.card_brawler_background);
            ivBrawlerImage = itemView.findViewById(R.id.iv_brawler_image);
            tvBrawlerName = itemView.findViewById(R.id.tv_brawler_name);
        }
    }
}