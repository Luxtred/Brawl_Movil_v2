package com.example.brawlmovilv01.datos.modelos;
import com.google.gson.annotations.SerializedName;

public class Gear {

    @SerializedName("id_gears")
    private int idGears;

    @SerializedName("nombre_ge")
    private String nombreGe;

    @SerializedName("tipo")
    private String tipo;

    @SerializedName("imagenGs")
    private String imagenGs;

    @SerializedName("id_brawler")
    private int idBrawler;

    public int getIdGears() { return idGears; }
    public String getNombreGe() { return nombreGe; }
    public String getTipo() { return tipo; }
    public String getImagenGs() { return imagenGs; }
    public int getIdBrawler() { return idBrawler; }
}