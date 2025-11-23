package com.example.brawlmovilv01.cuenta.cuenta_api;

import com.google.gson.annotations.SerializedName;

public class CuentaSet {

    @SerializedName("nombre_perfil")
    private String nombrePerfil;

    @SerializedName("correo_electronico")
    private String correoElectronico;

    @SerializedName("contrasena")
    private String contrasena;

    public CuentaSet(String nombrePerfil, String correoElectronico, String contrasena) {
        this.nombrePerfil = nombrePerfil;
        this.correoElectronico = correoElectronico;
        this.contrasena = contrasena;
    }
}