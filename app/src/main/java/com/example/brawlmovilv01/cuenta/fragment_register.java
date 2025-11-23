package com.example.brawlmovilv01.cuenta;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.brawlmovilv01.R;
import com.example.brawlmovilv01.cuenta.cuenta_api.Cuenta;
import com.example.brawlmovilv01.cuenta.cuenta_api.CuentaSet;
import com.example.brawlmovilv01.datos.api.ApiClient;
import com.example.brawlmovilv01.datos.api.ApiService;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class fragment_register extends Fragment {

    private EditText etEmail, etProfileName, etPassword, etPasswordConfirm;
    private Button btnSubmitRegister;
    private TextInputLayout tilEmail, tilProfileName, tilPassword, tilPasswordConfirm;

    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiService = ApiClient.getClient(requireContext()).create(ApiService.class);

        initViews(view);
        setupListeners();
    }

    private void initViews(View view) {
        tilEmail = view.findViewById(R.id.til_email_reg);
        tilProfileName = view.findViewById(R.id.til_profile_name);
        tilPassword = view.findViewById(R.id.til_password_reg);
        tilPasswordConfirm = view.findViewById(R.id.til_password_confirm);

        etEmail = view.findViewById(R.id.et_email_reg);
        etProfileName = view.findViewById(R.id.et_profile_name);
        etPassword = view.findViewById(R.id.et_password_reg);
        etPasswordConfirm = view.findViewById(R.id.et_password_confirm);
        btnSubmitRegister = view.findViewById(R.id.btn_submit_register);
    }

    private void setupListeners() {
        btnSubmitRegister.setOnClickListener(v -> validarRegistro());
    }

    private void validarRegistro() {
        etEmail.setError(null);
        etProfileName.setError(null);
        tilPassword.setError(null);
        tilPasswordConfirm.setError(null);

        String email = etEmail.getText().toString().trim();
        String profileName = etProfileName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String passwordConfirm = etPasswordConfirm.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Campo obligatorio");
            etEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Correo inválido");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(profileName)) {
            etProfileName.setError("Campo obligatorio");
            etProfileName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            tilPassword.setError("Campo obligatorio");
            etPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            tilPassword.setError("Mínimo 6 caracteres");
            etPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(passwordConfirm)) {
            tilPasswordConfirm.setError("Campo obligatorio");
            etPasswordConfirm.requestFocus();
            return;
        }

        if (!password.equals(passwordConfirm)) {
            tilPasswordConfirm.setError("Las contraseñas no coinciden");
            etPasswordConfirm.requestFocus();
            return;
        }

        performRegister(profileName, email, password);
    }

    private void performRegister(String nombre, String email, String password) {
        CuentaSet cuentaRequest = new CuentaSet(nombre, email, password);

        apiService.crearCuenta(cuentaRequest).enqueue(new Callback<Cuenta>() {
            @Override
            public void onResponse(@NonNull Call<Cuenta> call, @NonNull Response<Cuenta> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "¡Cuenta creada! Inicia sesión.", Toast.LENGTH_LONG).show();
                    if (getActivity() != null) {
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                } else {
                    handleRegisterError(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Cuenta> call, @NonNull Throwable t) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void handleRegisterError(Response<Cuenta> response) {
        String errorMsg = "El correo o nombre ya existen.";
        try {
            if (response.errorBody() != null) {
                String errorBody = response.errorBody().string();
                if (errorBody.contains("correo")) {
                    etEmail.setError("Correo ya registrado");
                    etEmail.requestFocus();
                    return;
                } else if (errorBody.contains("nombre")) {
                    etProfileName.setError("Nombre ya en uso");
                    etProfileName.requestFocus();
                    return;
                }
            }
        } catch (Exception e) {

        }

        if (getContext() != null) {
            Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
        }
    }
}