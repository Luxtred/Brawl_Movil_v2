package com.example.brawlmovilv01.cuenta.cuenta_api;

import com.google.gson.annotations.SerializedName;

public class Cuenta {

    @SerializedName("id_cuenta")
    private int idCuenta;

    @SerializedName("nombre_perfil")
    private String nombrePerfil;

    @SerializedName("correo_electronico")
    private String correoElectronico;

    public int getIdCuenta() {
        return idCuenta;
    }

    public String getNombrePerfil() {
        return nombrePerfil;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }
}