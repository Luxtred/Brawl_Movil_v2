package com.example.brawlmovilv01.builds;

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
import com.example.brawlmovilv01.Sesion.SessionManager;
import com.example.brawlmovilv01.builds.build_api.Build;

import java.util.List;

public class BuildAdapter extends RecyclerView.Adapter<BuildAdapter.ViewHolder> {

    public interface OnItemActionListener {
        void onEdit(Build build);
        void onDelete(Build build);
    }

    private final Context context;
    private final List<Build> buildList;
    private final OnItemActionListener listener;
    private final int currentUserId;

    public BuildAdapter(Context context, List<Build> buildList, OnItemActionListener listener) {
        this.context = context;
        this.buildList = buildList;
        this.listener = listener;

        SessionManager session = new SessionManager(context);
        this.currentUserId = session.fetchUserId();
    }

    public BuildAdapter(Context context, List<Build> buildList) {
        this(context, buildList, null);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_build_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Build build = buildList.get(position);

        if (build.getCuenta() != null) {
            holder.tvAutor.setText("AUTOR: " + build.getCuenta().getNombrePerfil());
        } else {
            holder.tvAutor.setText("AUTOR: DESCONOCIDO");
        }

        boolean isOwner = build.getCuenta() != null && build.getCuenta().getIdCuenta() == currentUserId;

        if (isOwner && listener != null) {
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);

            holder.btnEdit.setOnClickListener(v -> listener.onEdit(build));
            holder.btnDelete.setOnClickListener(v -> listener.onDelete(build));
        } else {
            holder.btnEdit.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
        }

        loadImage(build.getBrawlerPin(), holder.ivPin);

        String urlGadget = (build.getGadget() != null) ? build.getGadget().getImagenG() : null;
        loadImage(urlGadget, holder.ivGadget);

        String urlSP = (build.getStarpower() != null) ? build.getStarpower().getImagenS() : null;
        loadImage(urlSP, holder.ivStarPower);

        String urlGear1 = (build.getGear1() != null) ? build.getGear1().getImagenGs() : null;
        loadImage(urlGear1, holder.ivGear1);

        String urlGear2 = (build.getGear2() != null) ? build.getGear2().getImagenGs() : null;
        loadImage(urlGear2, holder.ivGear2);

        String urlHyper = (build.getHypercharge() != null) ? build.getHypercharge().getImagenH() : null;
        loadImage(urlHyper, holder.ivHyper);
    }

    private void loadImage(String url, ImageView imageView) {
        if (url != null && !url.isEmpty()) {
            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.placeholder);
        }
    }

    @Override
    public int getItemCount() {
        return buildList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAutor;
        ImageView ivGadget, ivStarPower, ivGear1, ivGear2, ivHyper, ivPin;
        ImageView btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAutor = itemView.findViewById(R.id.tv_build_author);
            ivGadget = itemView.findViewById(R.id.iv_build_gadget);
            ivStarPower = itemView.findViewById(R.id.iv_build_starpower);
            ivGear1 = itemView.findViewById(R.id.iv_build_gear1);
            ivGear2 = itemView.findViewById(R.id.iv_build_gear2);
            ivHyper = itemView.findViewById(R.id.iv_build_hyper);
            ivPin = itemView.findViewById(R.id.iv_brawler_pin);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}