package com.example.brawlmovilv01.mapas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.brawlmovilv01.R;
import com.example.brawlmovilv01.datos.modelos.Mapa;

import java.util.List;

public class MapaAdapter extends RecyclerView.Adapter<MapaAdapter.MapaViewHolder> {

    public interface OnMapaClickListener {
        void onMapaClick(Mapa mapa);
    }

    private final Context context;
    private List<Mapa> mapaList;
    private final OnMapaClickListener listener;

    public MapaAdapter(Context context, List<Mapa> mapaList, OnMapaClickListener listener) {
        this.context = context;
        this.mapaList = mapaList;
        this.listener = listener;
    }

    public void setMapaList(List<Mapa> mapaList) {
        this.mapaList = mapaList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MapaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mapa, parent, false);
        return new MapaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MapaViewHolder holder, int position) {
        Mapa mapa = mapaList.get(position);

        holder.tvMapName.setText(mapa.getNombre());

        Glide.with(context)
                .load(mapa.getImagenUrlMap())
                .placeholder(R.drawable.placeholder)
                .into(holder.ivMapImage);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMapaClick(mapa);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mapaList.size();
    }

    public static class MapaViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMapImage;
        TextView tvMapName;

        public MapaViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMapImage = itemView.findViewById(R.id.iv_map_image);
            tvMapName = itemView.findViewById(R.id.tv_map_name);
        }
    }
}