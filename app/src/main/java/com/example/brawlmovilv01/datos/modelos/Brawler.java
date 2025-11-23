package com.example.brawlmovilv01.datos.modelos;
import com.google.gson.annotations.SerializedName;

public class Brawler {

    @SerializedName("id_brawler")
    private int idBrawler;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("rareza")
    private String rareza;

    @SerializedName("color_rareza")
    private String colorRareza;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("imagen1")
    private String imagen1;

    public int getIdBrawler() { return idBrawler; }
    public String getNombre() { return nombre; }
    public String getRareza() { return rareza; }
    public String getColorRareza() { return colorRareza; }
    public String getDescripcion() { return descripcion; }
    public String getImagen1() { return imagen1; }
}