package com.example.brawlmovilv01.modos;

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
import com.example.brawlmovilv01.datos.modelos.Modo;
import com.example.brawlmovilv01.mapas.fragment_mapas_mode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class fragment_modos extends Fragment implements ModoAdapter.OnModoClickListener {

    private RecyclerView recyclerView;
    private ModoAdapter adapter;
    private ProgressBar progressBar;
    private static Parcelable recyclerViewStateModos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_modos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerView();
        fetchModos();
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_modos);
        progressBar = view.findViewById(R.id.progress_bar_modos);
    }

    private void setupRecyclerView() {
        adapter = new ModoAdapter(getContext(), new ArrayList<>(), this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(adapter);
    }

    private void fetchModos() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);

        ApiService apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
        Call<List<Modo>> call = apiService.getModos(0, 100);

        call.enqueue(new Callback<List<Modo>>() {
            @Override
            public void onResponse(@NonNull Call<List<Modo>> call, @NonNull Response<List<Modo>> response) {
                if (progressBar != null) progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    adapter.setModoList(response.body());

                    if (recyclerViewStateModos != null && recyclerView.getLayoutManager() != null) {
                        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewStateModos);
                    }
                } else {
                    mostrarError("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Modo>> call, @NonNull Throwable t) {
                if (progressBar != null) progressBar.setVisibility(View.GONE);
                Log.e("fragment_modos", "Error API", t);
                mostrarError("Error de conexión");
            }
        });
    }

    @Override
    public void onModoClick(Modo modo) {
        Fragment mapaModoFragment = new fragment_mapas_mode();
        Bundle args = new Bundle();
        args.putInt("MODO_ID", modo.getIdMode());
        args.putString("MODO_NOMBRE", modo.getNameM());
        args.putString("MODO_ICONO_URL", modo.getImagenIcono());
        args.putString("MODO_COLOR_FONDO", modo.getColorFondo());

        mapaModoFragment.setArguments(args);

        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content_container, mapaModoFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (recyclerView != null && recyclerView.getLayoutManager() != null) {
            recyclerViewStateModos = recyclerView.getLayoutManager().onSaveInstanceState();
        }
    }

    private void mostrarError(String mensaje) {
        if (getContext() != null) {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
        }
    }
}