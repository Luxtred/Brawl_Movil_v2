package com.example.brawlmovilv01.brawlers;

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
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brawlmovilv01.R;
import com.example.brawlmovilv01.datos.api.ApiClient;
import com.example.brawlmovilv01.datos.api.ApiService;
import com.example.brawlmovilv01.datos.modelos.Brawler;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class fragment_brawlers extends Fragment implements BrawlerAdapter.OnBrawlerClickListener {

    private RecyclerView recyclerView;
    private BrawlerAdapter adapter;
    private static Parcelable recyclerViewState;
    private SearchView searchView;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_brawlers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerView();
        setupSearchView();
        fetchBrawlers();
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_brawlers);
        searchView = view.findViewById(R.id.search_view);
        progressBar = view.findViewById(R.id.progress_bar);
    }

    private void setupRecyclerView() {
        adapter = new BrawlerAdapter(getContext(), new ArrayList<>(), this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(adapter);
    }

    private void setupSearchView() {
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (adapter != null) {
                        adapter.getFilter().filter(newText);
                    }
                    return true;
                }
            });
        }
    }

    private void fetchBrawlers() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);

        ApiService apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
        Call<List<Brawler>> call = apiService.getBrawlers();

        call.enqueue(new Callback<List<Brawler>>() {
            @Override
            public void onResponse(@NonNull Call<List<Brawler>> call, @NonNull Response<List<Brawler>> response) {
                if (progressBar != null) progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    adapter.setBrawlerList(response.body());

                    if (recyclerViewState != null && recyclerView.getLayoutManager() != null) {
                        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                    }
                } else {
                    mostrarError("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Brawler>> call, @NonNull Throwable t) {
                if (progressBar != null) progressBar.setVisibility(View.GONE);
                Log.e("fragment_brawlers", "Error API", t);
                mostrarError("Error de conexión");
            }
        });
    }

    @Override
    public void onBrawlerClick(int brawlerId) {
        fragment_brawler_detail detailFragment = fragment_brawler_detail.newInstance(brawlerId);

        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content_container, detailFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (recyclerView != null && recyclerView.getLayoutManager() != null) {
            recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
        }
    }

    private void mostrarError(String mensaje) {
        if (getContext() != null) {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
        }
    }
}