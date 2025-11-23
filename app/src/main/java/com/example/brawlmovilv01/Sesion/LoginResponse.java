package com.example.brawlmovilv01.Sesion;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("token_type")
    private String tokenType;

    @SerializedName("nombre_perfil")
    private String nombrePerfil;

    @SerializedName("id_cuenta")
    private int idCuenta;

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getNombrePerfil() {
        return nombrePerfil;
    }

    public int getIdCuenta() {
        return idCuenta;
    }
}