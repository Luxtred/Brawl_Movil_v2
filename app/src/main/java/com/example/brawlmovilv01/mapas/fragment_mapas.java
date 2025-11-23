package com.example.brawlmovilv01.mapas;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brawlmovilv01.R;
import com.example.brawlmovilv01.datos.api.ApiClient;
import com.example.brawlmovilv01.datos.api.ApiService;
import com.example.brawlmovilv01.datos.modelos.Mapa;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class fragment_mapas extends Fragment implements MapaAdapter.OnMapaClickListener {

    private RecyclerView recyclerView;
    private MapaAdapter adapter;
    private ProgressBar progressBar;
    private static Parcelable recyclerViewStateMapas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mapas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerView();
        fetchMapas();
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_mapas);
        progressBar = view.findViewById(R.id.progress_bar_mapas);
    }

    private void setupRecyclerView() {
        adapter = new MapaAdapter(getContext(), new ArrayList<>(), this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);
    }

    private void fetchMapas() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);

        ApiService apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
        Call<List<Mapa>> call = apiService.getMapas(0, 100);

        call.enqueue(new Callback<List<Mapa>>() {
            @Override
            public void onResponse(@NonNull Call<List<Mapa>> call, @NonNull Response<List<Mapa>> response) {
                if (progressBar != null) progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    adapter.setMapaList(response.body());

                    if (recyclerViewStateMapas != null && recyclerView.getLayoutManager() != null) {
                        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewStateMapas);
                    }
                } else {
                    mostrarError("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Mapa>> call, @NonNull Throwable t) {
                if (progressBar != null) progressBar.setVisibility(View.GONE);
                Log.e("fragment_mapas", "Error API", t);
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
        args.putInt("MODO_ID_RELACIONADO", mapa.getIdMode());
        args.putString("MODO_ICONO", ""); // Valor por defecto
        args.putString("MODO_COLOR", "#808080"); // Gris por defecto

        detalleFragment.setArguments(args);

        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content_container, detalleFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (recyclerView != null && recyclerView.getLayoutManager() != null) {
            recyclerViewStateMapas = recyclerView.getLayoutManager().onSaveInstanceState();
        }
    }

    private void mostrarError(String mensaje) {
        if (getContext() != null) {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
        }
    }
}