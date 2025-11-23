package com.example.brawlmovilv01.cuenta;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.brawlmovilv01.MainActivity;
import com.example.brawlmovilv01.R;
import com.example.brawlmovilv01.Sesion.SessionManager;

public class fragment_menuLateral extends DialogFragment {

    private SessionManager sessionManager;
    private MenuLateralListener listener;

    public interface MenuLateralListener {
        void onBuildsClicked();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (MenuLateralListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " debe implementar MenuLateralListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.TemaMenuLateral);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            Window window = dialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();

            params.gravity = Gravity.START;
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;

            window.setAttributes(params);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu_lateral, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(requireContext());

        TextView tvUsername = view.findViewById(R.id.tv_menu_username);
        TextView btnBuilds = view.findViewById(R.id.btn_menu_builds);
        TextView btnLogout = view.findViewById(R.id.btn_menu_logout);

        String userName = sessionManager.fetchUserName();
        tvUsername.setText(userName != null ? userName : "Usuario");

        btnBuilds.setOnClickListener(v -> {
            dismiss();
            if (listener != null) {
                listener.onBuildsClicked();
            }
        });

        btnLogout.setOnClickListener(v -> {
            dismiss();
            sessionManager.logoutUser();

            Toast.makeText(getContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}