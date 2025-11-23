package com.example.brawlmovilv01.builds;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brawlmovilv01.R;
import com.example.brawlmovilv01.builds.SelectionAdapter;
import com.example.brawlmovilv01.Sesion.SessionManager;
import com.example.brawlmovilv01.builds.build_api.Build;
import com.example.brawlmovilv01.builds.build_api.BuildCreate;
import com.example.brawlmovilv01.builds.build_api.BuildPut;
import com.example.brawlmovilv01.datos.api.ApiClient;
import com.example.brawlmovilv01.datos.api.ApiService;
import com.example.brawlmovilv01.datos.modelos.Gadget;
import com.example.brawlmovilv01.datos.modelos.Gear;
import com.example.brawlmovilv01.datos.modelos.Hypercharge;
import com.example.brawlmovilv01.datos.modelos.StarPower;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class fragment_build_create extends Fragment {

    private static final String ARG_BRAWLER_ID = "brawler_id";
    private static final String ARG_BUILD_ID = "build_id";
    private static final String ARG_PRE_SP = "pre_sp";
    private static final String ARG_PRE_GADGET = "pre_gadget";
    private static final String ARG_PRE_GEAR1 = "pre_gear1";
    private static final String ARG_PRE_GEAR2 = "pre_gear2";
    private static final String ARG_PRE_HYPER = "pre_hyper";

    private int brawlerId;
    private int buildId = -1;
    private ApiService apiService;
    private SessionManager sessionManager;

    private Integer selectedSpId = null;
    private Integer selectedGadgetId = null;
    private Integer selectedGear1Id = null;
    private Integer selectedGear2Id = null;
    private Integer selectedHyperId = null;

    private RecyclerView rvSP, rvGadget, rvGears, rvHyper;
    private Button btnGuardar;

    public static fragment_build_create newInstance(int brawlerId, int buildId, Integer sp, Integer gd, Integer g1, Integer g2, Integer hc) {
        fragment_build_create fragment = new fragment_build_create();
        Bundle args = new Bundle();
        args.putInt(ARG_BRAWLER_ID, brawlerId);
        args.putInt(ARG_BUILD_ID, buildId);
        if (sp != null) args.putInt(ARG_PRE_SP, sp);
        if (gd != null) args.putInt(ARG_PRE_GADGET, gd);
        if (g1 != null) args.putInt(ARG_PRE_GEAR1, g1);
        if (g2 != null) args.putInt(ARG_PRE_GEAR2, g2);
        if (hc != null) args.putInt(ARG_PRE_HYPER, hc);
        fragment.setArguments(args);
        return fragment;
    }

    public static fragment_build_create newInstance(int brawlerId) {
        return newInstance(brawlerId, -1, null, null, null, null, null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            brawlerId = getArguments().getInt(ARG_BRAWLER_ID);
            buildId = getArguments().getInt(ARG_BUILD_ID, -1);
            if (getArguments().containsKey(ARG_PRE_SP)) selectedSpId = getArguments().getInt(ARG_PRE_SP);
            if (getArguments().containsKey(ARG_PRE_GADGET)) selectedGadgetId = getArguments().getInt(ARG_PRE_GADGET);
            if (getArguments().containsKey(ARG_PRE_GEAR1)) selectedGear1Id = getArguments().getInt(ARG_PRE_GEAR1);
            if (getArguments().containsKey(ARG_PRE_GEAR2)) selectedGear2Id = getArguments().getInt(ARG_PRE_GEAR2);
            if (getArguments().containsKey(ARG_PRE_HYPER)) selectedHyperId = getArguments().getInt(ARG_PRE_HYPER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_build_create, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(requireContext());
        apiService = ApiClient.getClient(requireContext()).create(ApiService.class);

        initViews(view);

        if (buildId != -1) {
            btnGuardar.setText("ACTUALIZAR BUILD");
        }
        btnGuardar.setOnClickListener(v -> guardarBuild());

        cargarDatosBrawler();
    }

    private void initViews(View view) {
        rvSP = view.findViewById(R.id.rv_select_starpower);
        rvGadget = view.findViewById(R.id.rv_select_gadget);
        rvGears = view.findViewById(R.id.rv_select_gears);
        rvHyper = view.findViewById(R.id.rv_select_hyper);
        btnGuardar = view.findViewById(R.id.btn_save_build);

        rvSP.setLayoutManager(crearLayoutCentrado());
        rvGadget.setLayoutManager(crearLayoutCentrado());
        rvGears.setLayoutManager(crearLayoutCentrado());
        rvHyper.setLayoutManager(crearLayoutCentrado());
    }

    private FlexboxLayoutManager crearLayoutCentrado() {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.CENTER);
        layoutManager.setAlignItems(AlignItems.CENTER);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        return layoutManager;
    }

    private void cargarDatosBrawler() {
        apiService.getStarPowersForBrawler(brawlerId).enqueue(new Callback<List<StarPower>>() {
            @Override public void onResponse(@NonNull Call<List<StarPower>> call, @NonNull Response<List<StarPower>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<SelectionAdapter.ItemSeleccionable> items = new ArrayList<>();
                    for (StarPower sp : response.body()) {
                        SelectionAdapter.ItemSeleccionable item = new SelectionAdapter.ItemSeleccionable(sp.getIdStarPower(), sp.getImagenS());
                        if (selectedSpId != null && sp.getIdStarPower() == selectedSpId) item.isSelected = true;
                        items.add(item);
                    }
                    rvSP.setAdapter(new SelectionAdapter(getContext(), items, false, ids -> selectedSpId = ids.isEmpty() ? null : ids.get(0)));
                }
            }
            @Override public void onFailure(@NonNull Call<List<StarPower>> call, @NonNull Throwable t) {}
        });

        apiService.getGadgetsForBrawler(brawlerId).enqueue(new Callback<List<Gadget>>() {
            @Override public void onResponse(@NonNull Call<List<Gadget>> call, @NonNull Response<List<Gadget>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<SelectionAdapter.ItemSeleccionable> items = new ArrayList<>();
                    for (Gadget g : response.body()) {
                        SelectionAdapter.ItemSeleccionable item = new SelectionAdapter.ItemSeleccionable(g.getIdGadgets(), g.getImagenG());
                        if (selectedGadgetId != null && g.getIdGadgets() == selectedGadgetId) item.isSelected = true;
                        items.add(item);
                    }
                    rvGadget.setAdapter(new SelectionAdapter(getContext(), items, false, ids -> selectedGadgetId = ids.isEmpty() ? null : ids.get(0)));
                }
            }
            @Override public void onFailure(@NonNull Call<List<Gadget>> call, @NonNull Throwable t) {}
        });

        apiService.getGearsForBrawler(brawlerId).enqueue(new Callback<List<Gear>>() {
            @Override public void onResponse(@NonNull Call<List<Gear>> call, @NonNull Response<List<Gear>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<SelectionAdapter.ItemSeleccionable> items = new ArrayList<>();
                    for (Gear g : response.body()) {
                        SelectionAdapter.ItemSeleccionable item = new SelectionAdapter.ItemSeleccionable(g.getIdGears(), g.getImagenGs());
                        if ((selectedGear1Id != null && g.getIdGears() == selectedGear1Id) || (selectedGear2Id != null && g.getIdGears() == selectedGear2Id)) {
                            item.isSelected = true;
                        }
                        items.add(item);
                    }
                    rvGears.setAdapter(new SelectionAdapter(getContext(), items, true, ids -> {
                        selectedGear1Id = (ids.size() > 0) ? ids.get(0) : null;
                        selectedGear2Id = (ids.size() > 1) ? ids.get(1) : null;
                    }));
                }
            }
            @Override public void onFailure(@NonNull Call<List<Gear>> call, @NonNull Throwable t) {}
        });

        apiService.getHyperchargesForBrawler(brawlerId).enqueue(new Callback<List<Hypercharge>>() {
            @Override public void onResponse(@NonNull Call<List<Hypercharge>> call, @NonNull Response<List<Hypercharge>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<SelectionAdapter.ItemSeleccionable> items = new ArrayList<>();
                    for (Hypercharge h : response.body()) {
                        SelectionAdapter.ItemSeleccionable item = new SelectionAdapter.ItemSeleccionable(h.getIdHypercharge(), h.getImagenH());
                        if (selectedHyperId != null && h.getIdHypercharge() == selectedHyperId) item.isSelected = true;
                        items.add(item);
                    }
                    rvHyper.setAdapter(new SelectionAdapter(getContext(), items, false, ids -> selectedHyperId = ids.isEmpty() ? null : ids.get(0)));
                }
            }
            @Override public void onFailure(@NonNull Call<List<Hypercharge>> call, @NonNull Throwable t) {}
        });
    }

    private void guardarBuild() {
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(getContext(), "Sesión expirada", Toast.LENGTH_SHORT).show();
            return;
        }
        int idCuenta = sessionManager.fetchUserId();

        if (buildId == -1) {
            BuildCreate request = new BuildCreate(idCuenta, brawlerId, selectedSpId, selectedGadgetId, selectedGear1Id, selectedGear2Id, selectedHyperId);
            apiService.crearBuild(request).enqueue(generarCallback("Build creada"));
        } else {
            BuildPut request = new BuildPut(brawlerId, selectedSpId, selectedGadgetId, selectedGear1Id, selectedGear2Id, selectedHyperId);
            apiService.actualizarBuild(buildId, idCuenta, request).enqueue(generarCallback("Build actualizada"));
        }
    }

    private Callback<Build> generarCallback(String mensajeExito) {
        return new Callback<Build>() {
            @Override
            public void onResponse(@NonNull Call<Build> call, @NonNull Response<Build> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), mensajeExito, Toast.LENGTH_SHORT).show();
                    if (getParentFragmentManager() != null) {
                        getParentFragmentManager().popBackStack();
                    }
                } else {
                    try {
                        String err = response.errorBody() != null ? response.errorBody().string() : "Error";
                        Log.e("API_ERROR", err);
                        Toast.makeText(getContext(), "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {}
                }
            }

            @Override
            public void onFailure(@NonNull Call<Build> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error de red", Toast.LENGTH_SHORT).show();
            }
        };
    }
}