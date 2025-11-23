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

    // --- 1. Interfaz de Clic (Movida al inicio) ---
    public interface OnBrawlerClickListener {
        void onBrawlerClick(int brawlerId);
    }

    // --- 2. Campos (Variables) ---
    private final OnBrawlerClickListener clickListener;
    private final Context context;
    private List<Brawler> brawlerList;
    private List<Brawler> brawlerListFull;
    private final int defaultColor;

    // --- 3. Constructor ---
    public BrawlerAdapter(Context context, List<Brawler> brawlerList, OnBrawlerClickListener clickListener) {
        this.context = context;
        this.brawlerList = brawlerList;
        this.brawlerListFull = new ArrayList<>(brawlerList);
        this.defaultColor = Color.LTGRAY;
        this.clickListener = clickListener; // Asigna el listener
    }

    // --- 4. Métodos Públicos (Actualizar lista) ---
    public void setBrawlerList(List<Brawler> brawlerList) {
        this.brawlerList = brawlerList;
        this.brawlerListFull = new ArrayList<>(brawlerList);
        notifyDataSetChanged();
    }

    // --- 5. Métodos Sobrescritos del Adapter ---
    @NonNull
    @Override
    public BrawlerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_brawler, parent, false);
        return new BrawlerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrawlerViewHolder holder, int position) {
        Brawler brawler = brawlerList.get(position);

        // 1. Poner el nombre
        holder.tvBrawlerName.setText(brawler.getNombre());

        // 2. Cargar la imagen con Glide (Respetando tu placeholder)
        Glide.with(context)
                .load(brawler.getImagen1()) // URL de la API
                .placeholder(R.drawable.placeholder) // <-- Usando tu placeholder
                .into(holder.ivBrawlerImage);

        // 3. Aplicar el color de rareza
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

        // 4. Asignar el OnClickListener
        holder.itemView.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION && clickListener != null) {
                // Usando tu método de modelo .getIdBrawler()
                clickListener.onBrawlerClick(brawlerList.get(currentPosition).getIdBrawler());
            }
        });
    }

    @Override
    public int getItemCount() {
        return brawlerList.size();
    }

    // --- 6. Implementación del Filtro ---
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
                    // Lógica 'startsWith'
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

    // --- 7. ViewHolder (Movido al final) ---
    public static class BrawlerViewHolder extends RecyclerView.ViewHolder {
        CardView cardBrawlerBackground;
        ImageView ivBrawlerImage;
        TextView tvBrawlerName;

        public BrawlerViewHolder(@NonNull View itemView) {
            super(itemView);
            // Referencias a los IDs del item_brawler.xml
            cardBrawlerBackground = itemView.findViewById(R.id.card_brawler_background);
            ivBrawlerImage = itemView.findViewById(R.id.iv_brawler_image);
            tvBrawlerName = itemView.findViewById(R.id.tv_brawler_name);
        }
    }
}