package com.example.brawlmovilv01.brawlers;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.brawlmovilv01.R;
import com.example.brawlmovilv01.builds.fragment_build;
import com.example.brawlmovilv01.datos.api.ApiClient;
import com.example.brawlmovilv01.datos.api.ApiService;
import com.example.brawlmovilv01.datos.modelos.Brawler;
import com.example.brawlmovilv01.datos.modelos.Gadget;
import com.example.brawlmovilv01.datos.modelos.Gear;
import com.example.brawlmovilv01.datos.modelos.Hypercharge;
import com.example.brawlmovilv01.datos.modelos.StarPower;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class fragment_brawler_detail extends Fragment {

    private static final String ARG_BRAWLER_ID = "brawler_id";
    private int brawlerId;
    private ApiService apiService;

    private TextView tvBrawlerName, tvBrawlerRareza, tvGearCount;
    private ImageView ivBrawlerImage, ivStarPower1, ivStarPower2, ivGadget1, ivGadget2, ivHypercharge;
    private MaterialCardView cvBrawlerImageBg;
    private Button btnBuilds;

    public static fragment_brawler_detail newInstance(int brawlerId) {
        fragment_brawler_detail fragment = new fragment_brawler_detail();
        Bundle args = new Bundle();
        args.putInt(ARG_BRAWLER_ID, brawlerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            brawlerId = getArguments().getInt(ARG_BRAWLER_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_brawler_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        apiService = ApiClient.getClient(requireContext()).create(ApiService.class);

        setupListeners();
        fetchAllBrawlerData();
    }

    private void initViews(View view) {
        tvBrawlerName = view.findViewById(R.id.tv_brawler_name_detail);
        tvBrawlerRareza = view.findViewById(R.id.tv_brawler_rareza_detail);
        ivBrawlerImage = view.findViewById(R.id.iv_brawler_image_detail);
        cvBrawlerImageBg = view.findViewById(R.id.cv_brawler_image_bg);
        ivStarPower1 = view.findViewById(R.id.iv_star_power_1);
        ivStarPower2 = view.findViewById(R.id.iv_star_power_2);
        ivGadget1 = view.findViewById(R.id.iv_gadget_1);
        ivGadget2 = view.findViewById(R.id.iv_gadget_2);
        ivHypercharge = view.findViewById(R.id.iv_hypercharge);
        tvGearCount = view.findViewById(R.id.tv_gear_count);
        btnBuilds = view.findViewById(R.id.btn_builds);
    }

    private void setupListeners() {
        btnBuilds.setOnClickListener(v -> {
            fragment_build listFragment = fragment_build.newInstance(brawlerId);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.main_content_container, listFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void fetchAllBrawlerData() {
        if (brawlerId == 0) return;

        loadBrawlerInfo();
        loadStarPowers();
        loadGadgets();
        loadHypercharge();
        loadGears();
    }

    private void loadBrawlerInfo() {
        apiService.getBrawlerById(brawlerId).enqueue(new Callback<Brawler>() {
            @Override
            public void onResponse(@NonNull Call<Brawler> call, @NonNull Response<Brawler> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Brawler brawler = response.body();
                    tvBrawlerName.setText(brawler.getNombre());
                    tvBrawlerRareza.setText("RAREZA: " + brawler.getRareza());

                    if (getContext() != null) {
                        Glide.with(getContext())
                                .load(brawler.getImagen1())
                                .placeholder(R.drawable.placeholder)
                                .into(ivBrawlerImage);

                        try {
                            int color = Color.parseColor(brawler.getColorRareza());
                            cvBrawlerImageBg.setCardBackgroundColor(color);

                            int strokeWidthPx = (int) (6 * getResources().getDisplayMetrics().density);
                            cvBrawlerImageBg.setStrokeWidth(strokeWidthPx);
                            cvBrawlerImageBg.setStrokeColor(getDarkerColor(color));

                        } catch (Exception e) {

                        }
                    }
                }
            }
            @Override public void onFailure(@NonNull Call<Brawler> call, @NonNull Throwable t) {}
        });
    }

    private void loadStarPowers() {
        apiService.getStarPowersForBrawler(brawlerId).enqueue(new Callback<List<StarPower>>() {
            @Override
            public void onResponse(@NonNull Call<List<StarPower>> call, @NonNull Response<List<StarPower>> response) {
                if (response.isSuccessful() && response.body() != null && getContext() != null) {
                    List<StarPower> list = response.body();
                    if (list.size() > 0) Glide.with(getContext()).load(list.get(0).getImagenS()).into(ivStarPower1);
                    if (list.size() > 1) Glide.with(getContext()).load(list.get(1).getImagenS()).into(ivStarPower2);
                }
            }
            @Override public void onFailure(@NonNull Call<List<StarPower>> call, @NonNull Throwable t) {}
        });
    }

    private void loadGadgets() {
        apiService.getGadgetsForBrawler(brawlerId).enqueue(new Callback<List<Gadget>>() {
            @Override
            public void onResponse(@NonNull Call<List<Gadget>> call, @NonNull Response<List<Gadget>> response) {
                if (response.isSuccessful() && response.body() != null && getContext() != null) {
                    List<Gadget> list = response.body();
                    if (list.size() > 0) Glide.with(getContext()).load(list.get(0).getImagenG()).into(ivGadget1);
                    if (list.size() > 1) Glide.with(getContext()).load(list.get(1).getImagenG()).into(ivGadget2);
                }
            }
            @Override public void onFailure(@NonNull Call<List<Gadget>> call, @NonNull Throwable t) {}
        });
    }

    private void loadHypercharge() {
        apiService.getHyperchargesForBrawler(brawlerId).enqueue(new Callback<List<Hypercharge>>() {
            @Override
            public void onResponse(@NonNull Call<List<Hypercharge>> call, @NonNull Response<List<Hypercharge>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty() && getContext() != null) {
                    ivHypercharge.setVisibility(View.VISIBLE);
                    Glide.with(getContext()).load(response.body().get(0).getImagenH()).into(ivHypercharge);
                } else {
                    ivHypercharge.setVisibility(View.GONE);
                }
            }
            @Override public void onFailure(@NonNull Call<List<Hypercharge>> call, @NonNull Throwable t) {}
        });
    }

    private void loadGears() {
        apiService.getGearsForBrawler(brawlerId).enqueue(new Callback<List<Gear>>() {
            @Override
            public void onResponse(@NonNull Call<List<Gear>> call, @NonNull Response<List<Gear>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tvGearCount.setText(String.valueOf(response.body().size()));
                } else {
                    tvGearCount.setText("0");
                }
            }
            @Override public void onFailure(@NonNull Call<List<Gear>> call, @NonNull Throwable t) {
                tvGearCount.setText("-");
            }
        });
    }

    private int getDarkerColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
        return Color.HSVToColor(hsv);
    }
}