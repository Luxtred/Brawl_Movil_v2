package com.example.brawlmovilv01.datos.modelos;
import com.google.gson.annotations.SerializedName;

public class Mapa {

    @SerializedName("id_map")
    private int idMap;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("imagen_url_map")
    private String imagenUrlMap;

    @SerializedName("id_mode")
    private int idMode;

    public int getIdMap() { return idMap; }
    public String getNombre() { return nombre; }
    public String getImagenUrlMap() { return imagenUrlMap; }
    public int getIdMode() { return idMode; }
}