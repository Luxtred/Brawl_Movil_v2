package com.example.brawlmovilv01.datos.modelos;
import com.google.gson.annotations.SerializedName;

public class StarPower {

    @SerializedName("id_starPower")
    private int idStarPower;

    @SerializedName("nombre_s")
    private String nombreS;

    @SerializedName("descripcion_s")
    private String descripcionS;

    @SerializedName("imagenS")
    private String imagenS;

    @SerializedName("id_brawler")
    private int idBrawler;

    public int getIdStarPower() { return idStarPower; }
    public String getNombreS() { return nombreS; }
    public String getDescripcionS() { return descripcionS; }
    public String getImagenS() { return imagenS; }
    public int getIdBrawler() { return idBrawler; }
}