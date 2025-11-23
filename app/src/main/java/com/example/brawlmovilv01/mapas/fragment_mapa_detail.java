package com.example.brawlmovilv01.mapas;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.brawlmovilv01.R;
import com.example.brawlmovilv01.builds.BestBrawlerAdapter;
import com.example.brawlmovilv01.datos.api.ApiClient;
import com.example.brawlmovilv01.datos.api.ApiService;
import com.example.brawlmovilv01.datos.modelos.BrawlerStats;
import com.example.brawlmovilv01.datos.modelos.Modo;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class fragment_mapa_detail extends Fragment {

    private ImageView ivModeIcon, ivMapImage;
    private TextView tvMapName;
    private CardView cardHeader;
    private RecyclerView recyclerBrawlers;
    private BestBrawlerAdapter brawlerAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mapa_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerView();
        processArguments();
    }

    private void initViews(View view) {
        ivModeIcon = view.findViewById(R.id.iv_mapa_detail_mode_icon);
        ivMapImage = view.findViewById(R.id.iv_mapa_detail_image);
        tvMapName = view.findViewById(R.id.tv_mapa_detail_name);
        cardHeader = view.findViewById(R.id.card_mapa_detail_header);
        recyclerBrawlers = view.findViewById(R.id.recycler_view_mejores_brawlers);
    }

    private void setupRecyclerView() {
        recyclerBrawlers.setLayoutManager(new GridLayoutManager(getContext(), 3));
    }

    private void processArguments() {
        Bundle args = getArguments();
        if (args == null) return;

        int mapaId = args.getInt("MAPA_ID", -1);
        String nombreMapa = args.getString("MAPA_NOMBRE", "Mapa");
        String imagenMapaUrl = args.getString("MAPA_IMAGEN", "");

        tvMapName.setText(nombreMapa.toUpperCase(Locale.ROOT));

        if (imagenMapaUrl != null && !imagenMapaUrl.isEmpty()) {
            Glide.with(this)
                    .load(imagenMapaUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(ivMapImage);
        }

        String iconoModoUrl = args.getString("MODO_ICONO", "");
        String colorFondo = args.getString("MODO_COLOR", "");
        int idModoRelacionado = args.getInt("MODO_ID_RELACIONADO", -1);

        if (!iconoModoUrl.isEmpty() && !colorFondo.isEmpty()) {
            aplicarEstiloModo(colorFondo, iconoModoUrl);
        } else if (idModoRelacionado != -1) {
            fetchDetallesDelModo(idModoRelacionado);
        } else {
            cardHeader.setCardBackgroundColor(Color.GRAY);
        }

        if (mapaId != -1) {
            fetchMejoresBrawlers(mapaId);
        }
    }

    private void fetchMejoresBrawlers(int mapaId) {
        ApiService apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
        Call<List<BrawlerStats>> call = apiService.getMapStats(mapaId);

        call.enqueue(new Callback<List<BrawlerStats>>() {
            @Override
            public void onResponse(@NonNull Call<List<BrawlerStats>> call, @NonNull Response<List<BrawlerStats>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<BrawlerStats> statsList = response.body();
                    if (!statsList.isEmpty() && getContext() != null) {
                        brawlerAdapter = new BestBrawlerAdapter(getContext(), statsList);
                        recyclerBrawlers.setAdapter(brawlerAdapter);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<BrawlerStats>> call, @NonNull Throwable t) {
                Log.e("MapaDetail", "Fallo conexión stats", t);
            }
        });
    }

    private void fetchDetallesDelModo(int idModo) {
        ApiService apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
        Call<Modo> call = apiService.getModoById(idModo);

        call.enqueue(new Callback<Modo>() {
            @Override
            public void onResponse(@NonNull Call<Modo> call, @NonNull Response<Modo> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Modo modo = response.body();
                    aplicarEstiloModo(modo.getColorFondo(), modo.getImagenIcono());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Modo> call, @NonNull Throwable t) {
                Log.e("MapaDetail", "Error cargando modo", t);
            }
        });
    }

    private void aplicarEstiloModo(String colorHex, String urlIcono) {
        if (getContext() == null) return;

        try {
            if (colorHex != null && !colorHex.isEmpty()) {
                cardHeader.setCardBackgroundColor(Color.parseColor(colorHex));
            } else {
                cardHeader.setCardBackgroundColor(Color.GRAY);
            }
        } catch (Exception e) {
            cardHeader.setCardBackgroundColor(Color.GRAY);
        }

        if (urlIcono != null && !urlIcono.isEmpty()) {
            Glide.with(this)
                    .load(urlIcono)
                    .placeholder(R.drawable.placeholder)
                    .into(ivModeIcon);
        }
    }
}