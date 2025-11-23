package com.example.brawlmovilv01.mapas;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.brawlmovilv01.R;
import com.example.brawlmovilv01.datos.api.ApiClient;
import com.example.brawlmovilv01.datos.api.ApiService;
import com.example.brawlmovilv01.datos.modelos.Mapa;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class fragment_mapas_mode extends Fragment implements MapaAdapter.OnMapaClickListener {

    private RecyclerView recyclerView;
    private MapaAdapter adapter;
    private ProgressBar progressBar;

    private TextView tvTitle;
    private CardView cardModeHeader;
    private ImageView ivModeIconHeader;

    private int modoId;
    private String modoNombre;
    private String modoIconoUrl;
    private String modoColorFondo;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mapas_mode, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initArgs();
        initViews(view);
        setupHeader();
        setupRecyclerView();

        if (modoId != -1) {
            fetchMapasPorModo(modoId);
        } else {
            mostrarError("Error: Modo no identificado");
        }
    }

    private void initArgs() {
        Bundle args = getArguments();
        if (args != null) {
            modoId = args.getInt("MODO_ID", -1);
            modoNombre = args.getString("MODO_NOMBRE", "Mapas");
            modoIconoUrl = args.getString("MODO_ICONO_URL", null);
            modoColorFondo = args.getString("MODO_COLOR_FONDO", null);
        } else {
            modoId = -1;
            modoNombre = "Error";
        }
    }

    private void initViews(View view) {
        tvTitle = view.findViewById(R.id.tv_title_modo_mapa);
        cardModeHeader = view.findViewById(R.id.card_mode_header);
        ivModeIconHeader = view.findViewById(R.id.iv_mode_icon_header);
        recyclerView = view.findViewById(R.id.recycler_view_modo_mapa);
        progressBar = view.findViewById(R.id.progress_bar_modo_mapa);
    }

    private void setupHeader() {
        tvTitle.setText(modoNombre.toUpperCase(Locale.ROOT));

        if (getContext() != null && modoIconoUrl != null && !modoIconoUrl.isEmpty()) {
            Glide.with(this)
                    .load(modoIconoUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(ivModeIconHeader);
        } else {
            ivModeIconHeader.setImageResource(R.drawable.placeholder);
        }

        int color = Color.LTGRAY;
        if (modoColorFondo != null && !modoColorFondo.isEmpty()) {
            try {
                color = Color.parseColor(modoColorFondo);
            } catch (IllegalArgumentException e) {

            }
        }
        cardModeHeader.setCardBackgroundColor(color);
    }

    private void setupRecyclerView() {
        adapter = new MapaAdapter(getContext(), new ArrayList<>(), this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);
    }

    private void fetchMapasPorModo(int id) {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);

        ApiService apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
        Call<List<Mapa>> call = apiService.getMapasByModo(id);

        call.enqueue(new Callback<List<Mapa>>() {
            @Override
            public void onResponse(@NonNull Call<List<Mapa>> call, @NonNull Response<List<Mapa>> response) {
                if (progressBar != null) progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    adapter.setMapaList(response.body());
                } else {
                    mostrarError("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Mapa>> call, @NonNull Throwable t) {
                if (progressBar != null) progressBar.setVisibility(View.GONE);
                Log.e("fragment_mapas_mode", "Error API", t);
                mostrarError("Error de conexión");
            }
        });
    }

    @Override
    public void onMapaClick(Mapa mapa) {
        Fragment detalleFragment = new fragment_mapa_detail();
        Bundle args = new Bundle();

        args.putInt("MAPA_ID", mapa.getIdMap());
        args.putString("MAPA_NOMBRE", mapa.getNombre());
        args.putString("MAPA_IMAGEN", mapa.getImagenUrlMap());
        args.putString("MODO_ICONO", this.modoIconoUrl);
        args.putString("MODO_COLOR", this.modoColorFondo);

        detalleFragment.setArguments(args);

        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content_container, detalleFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void mostrarError(String mensaje) {
        if (getContext() != null) {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
        }
    }
}