package com.example.brawlmovilv01.builds;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brawlmovilv01.R;
import com.example.brawlmovilv01.Sesion.SessionManager;
import com.example.brawlmovilv01.builds.build_api.Build;
import com.example.brawlmovilv01.datos.api.ApiClient;
import com.example.brawlmovilv01.datos.api.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class fragment_build extends Fragment {

    private static final String ARG_BRAWLER_ID = "brawler_id";
    private int brawlerId = 0;
    private static Parcelable recyclerViewState;

    private RecyclerView recyclerView;
    private BuildAdapter adapter;
    private List<Build> listaBuilds;
    private ApiService apiService;
    private SessionManager sessionManager;

    public static fragment_build newInstance(int brawlerId) {
        fragment_build fragment = new fragment_build();
        Bundle args = new Bundle();
        args.putInt(ARG_BRAWLER_ID, brawlerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            brawlerId = getArguments().getInt(ARG_BRAWLER_ID, 0);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_build, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(requireContext());
        // CORRECCIÓN: Uso del nuevo ApiClient seguro
        apiService = ApiClient.getClient(requireContext()).create(ApiService.class);

        initViews(view);
        setupRecyclerView();
        setupCreateButton(view);
        cargarBuilds();
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.rv_builds);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listaBuilds = new ArrayList<>();

        adapter = new BuildAdapter(getContext(), listaBuilds, new BuildAdapter.OnItemActionListener() {
            @Override
            public void onEdit(Build build) {
                irAEditarBuild(build);
            }

            @Override
            public void onDelete(Build build) {
                mostrarConfirmacionBorrar(build);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void setupCreateButton(View view) {
        CardView btnCrearBuild = view.findViewById(R.id.card_create_build);
        btnCrearBuild.setOnClickListener(v -> {
            if (brawlerId != 0) {
                // Modo Crear Nuevo (sin ID de build)
                fragment_build_create createFragment = fragment_build_create.newInstance(brawlerId);
                navigateToFragment(createFragment);
            } else {
                Toast.makeText(getContext(), "Selecciona un Brawler primero", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void navigateToFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.main_content_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void mostrarConfirmacionBorrar(Build build) {
        if (getContext() == null) return;

        new AlertDialog.Builder(getContext())
                .setTitle("Eliminar Build")
                .setMessage("¿Estás seguro de que deseas eliminar esta build permanentemente?")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarBuildAPI(build))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarBuildAPI(Build build) {
        int myUserId = sessionManager.fetchUserId();

        apiService.eliminarBuild(build.getIdBuild(), myUserId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Build eliminada", Toast.LENGTH_SHORT).show();
                    cargarBuilds();
                } else {
                    Toast.makeText(getContext(), "Error al eliminar: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void irAEditarBuild(Build build) {
        Integer spId = (build.getStarpower() != null) ? build.getStarpower().getIdStarPower() : null;
        Integer gdId = (build.getGadget() != null) ? build.getGadget().getIdGadgets() : null;
        Integer g1Id = (build.getGear1() != null) ? build.getGear1().getIdGears() : null;
        Integer g2Id = (build.getGear2() != null) ? build.getGear2().getIdGears() : null;
        Integer hcId = (build.getHypercharge() != null) ? build.getHypercharge().getIdHypercharge() : null;
        fragment_build_create editFragment = fragment_build_create.newInstance(
                build.getIdBrawler(),
                build.getIdBuild(),
                spId, gdId, g1Id, g2Id, hcId
        );

        navigateToFragment(editFragment);
    }

    private void cargarBuilds() {
        Integer filtroBrawler = (brawlerId != 0) ? brawlerId : null;
        Integer filtroCuenta = null;

        if (brawlerId == 0) {
            if (sessionManager.isLoggedIn()) {
                filtroCuenta = sessionManager.fetchUserId();
            } else {
                return;
            }
        }

        apiService.getBuilds(0, 100, filtroBrawler, filtroCuenta).enqueue(new Callback<List<Build>>() {
            @Override
            public void onResponse(@NonNull Call<List<Build>> call, @NonNull Response<List<Build>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaBuilds.clear();
                    listaBuilds.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    if (recyclerViewState != null && recyclerView.getLayoutManager() != null) {
                        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                    }
                }
            }
            @Override public void onFailure(@NonNull Call<List<Build>> call, @NonNull Throwable t) {}
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (recyclerView != null && recyclerView.getLayoutManager() != null) {
            recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
        }
    }
}