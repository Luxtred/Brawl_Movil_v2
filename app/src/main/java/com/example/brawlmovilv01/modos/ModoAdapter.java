package com.example.brawlmovilv01.modos;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.brawlmovilv01.R;
import com.example.brawlmovilv01.datos.modelos.Modo;

import java.util.List;

public class ModoAdapter extends RecyclerView.Adapter<ModoAdapter.ModoViewHolder> {

    public interface OnModoClickListener {
        void onModoClick(Modo modo);
    }

    private final Context context;
    private List<Modo> modoList;
    private final OnModoClickListener clickListener;
    private final int defaultColor;

    public ModoAdapter(Context context, List<Modo> modoList, OnModoClickListener listener) {
        this.context = context;
        this.modoList = modoList;
        this.clickListener = listener;
        this.defaultColor = Color.LTGRAY;
    }

    public void setModoList(List<Modo> modoList) {
        this.modoList = modoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ModoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_modo, parent, false);
        return new ModoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModoViewHolder holder, int position) {
        Modo modo = modoList.get(position);

        Glide.with(context)
                .load(modo.getImagenIcono())
                .placeholder(R.drawable.placeholder)
                .into(holder.ivModeIcon);

        String colorHex = modo.getColorFondo();
        try {
            if (colorHex != null && !colorHex.isEmpty()) {
                holder.cardModeBackground.setCardBackgroundColor(Color.parseColor(colorHex));
            } else {
                holder.cardModeBackground.setCardBackgroundColor(defaultColor);
            }
        } catch (IllegalArgumentException e) {
            holder.cardModeBackground.setCardBackgroundColor(defaultColor);
        }

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onModoClick(modo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modoList.size();
    }

    public static class ModoViewHolder extends RecyclerView.ViewHolder {
        CardView cardModeBackground;
        ImageView ivModeIcon;

        public ModoViewHolder(@NonNull View itemView) {
            super(itemView);
            cardModeBackground = itemView.findViewById(R.id.card_mode_background);
            ivModeIcon = itemView.findViewById(R.id.iv_mode_icon);
        }
    }
}