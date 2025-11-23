package com.example.brawlmovilv01.datos.modelos;

import com.google.gson.annotations.SerializedName;

public class BrawlerStats {

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("imagenUrl")
    private String imagenUrl;

    @SerializedName("winRate")
    private double winRate;

    @SerializedName("useRate")
    private double useRate;

    public String getNombre() { return nombre; }
    public String getImagenUrl() { return imagenUrl; }
    public double getWinRate() { return winRate; }
    public double getUseRate() { return useRate; }
}