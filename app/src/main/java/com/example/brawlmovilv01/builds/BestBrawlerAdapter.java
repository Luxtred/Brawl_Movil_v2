package com.example.brawlmovilv01.builds;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.brawlmovilv01.R;
import com.example.brawlmovilv01.datos.modelos.BrawlerStats;

import java.util.List;
import java.util.Locale;

public class BestBrawlerAdapter extends RecyclerView.Adapter<BestBrawlerAdapter.ViewHolder> {

    private final Context context;
    private final List<BrawlerStats> brawlerList;

    public BestBrawlerAdapter(Context context, List<BrawlerStats> brawlerList) {
        this.context = context;
        this.brawlerList = brawlerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_best_brawler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BrawlerStats stats = brawlerList.get(position);

        holder.tvName.setText(stats.getNombre());
        holder.tvWinRate.setText(String.format(Locale.getDefault(), "%.1f%%", stats.getWinRate()));
        holder.tvUseRate.setText(String.format(Locale.getDefault(), "%.1f%%", stats.getUseRate()));

        holder.progressWin.setProgress((int) stats.getWinRate());
        holder.progressUse.setProgress((int) (stats.getUseRate() * 5));

        Glide.with(context)
                .load(stats.getImagenUrl())
                .placeholder(R.drawable.placeholder)
                .into(holder.ivIcon);
    }

    @Override
    public int getItemCount() {
        return brawlerList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvName, tvWinRate, tvUseRate;
        ProgressBar progressWin, progressUse;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_brawler_icon);
            tvName = itemView.findViewById(R.id.tv_brawler_name);
            tvWinRate = itemView.findViewById(R.id.tv_win_rate);
            tvUseRate = itemView.findViewById(R.id.tv_use_rate);
            progressWin = itemView.findViewById(R.id.progress_win);
            progressUse = itemView.findViewById(R.id.progress_use);
        }
    }
}