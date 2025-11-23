package com.example.brawlmovilv01.datos.modelos;
import com.google.gson.annotations.SerializedName;

public class Hypercharge {

    @SerializedName("id_hypercharge")
    private int idHypercharge;

    @SerializedName("nombre_h")
    private String nombreH;

    @SerializedName("descripcion_h")
    private String descripcionH;

    @SerializedName("imagenH")
    private String imagenH;

    @SerializedName("id_brawler")
    private int idBrawler;

    public int getIdHypercharge() { return idHypercharge; }
    public String getNombreH() { return nombreH; }
    public String getDescripcionH() { return descripcionH; }
    public String getImagenH() { return imagenH; }
    public int getIdBrawler() { return idBrawler; }
}