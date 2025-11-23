package com.example.brawlmovilv01;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.brawlmovilv01.Sesion.InicioFragment;
import com.example.brawlmovilv01.brawlers.fragment_brawlers;
import com.example.brawlmovilv01.builds.fragment_build;
import com.example.brawlmovilv01.cuenta.fragment_login;
import com.example.brawlmovilv01.cuenta.fragment_menuLateral;
import com.example.brawlmovilv01.Sesion.SessionManager;
import com.example.brawlmovilv01.mapas.fragment_mapas;
import com.example.brawlmovilv01.modos.fragment_modos;

public class MainActivity extends AppCompatActivity implements fragment_menuLateral.MenuLateralListener {

    private ImageView ivHome, ivProfile;
    private LinearLayout btnBrawlers, btnMapas, btnModos;

    private Handler inactivityHandler;
    private Runnable inactivityRunnable;
    private static final long INACTIVITY_TIMEOUT_MS = 5 * 60 * 1000;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupWindow();
        setupInactivityHandler();

        sessionManager = new SessionManager(this);

        initViews();
        setupListeners();

        if (savedInstanceState == null) {
            loadFragment(new InicioFragment());
        }
    }

    private void setupWindow() {
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );
    }

    private void setupInactivityHandler() {
        inactivityHandler = new Handler(Looper.getMainLooper());
        inactivityRunnable = () -> {
            finish();
        };
    }

    private void initViews() {
        ivHome = findViewById(R.id.iv_home);
        ivProfile = findViewById(R.id.iv_profile);
        btnBrawlers = findViewById(R.id.btn_brawlers);
        btnMapas = findViewById(R.id.btn_mapas);
        btnModos = findViewById(R.id.btn_modos);
    }

    private void setupListeners() {
        btnBrawlers.setOnClickListener(v -> loadFragment(new fragment_brawlers()));
        btnMapas.setOnClickListener(v -> loadFragment(new fragment_mapas()));
        btnModos.setOnClickListener(v -> loadFragment(new fragment_modos()));
        ivHome.setOnClickListener(v -> loadFragment(new InicioFragment()));

        ivProfile.setOnClickListener(v -> {
            if (sessionManager.isLoggedIn()) {
                fragment_menuLateral menuDialog = new fragment_menuLateral();
                menuDialog.show(getSupportFragmentManager(), "MenuPerfil");
            } else {
                loadFragment(new fragment_login());
            }
        });
    }

    @Override
    public void onBuildsClicked() {
        loadFragment(fragment_build.newInstance(0));
    }

    @Override
    protected void onStart() {
        super.onStart();
        inactivityHandler.removeCallbacks(inactivityRunnable);
    }

    @Override
    protected void onStop() {
        super.onStop();
        inactivityHandler.postDelayed(inactivityRunnable, INACTIVITY_TIMEOUT_MS);
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_content_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void mostrarToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}