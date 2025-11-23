package com.example.brawlmovilv01.datos.modelos;
import com.google.gson.annotations.SerializedName;

public class Gadget {

    @SerializedName("id_gadgets")
    private int idGadgets;

    @SerializedName("nombre_g")
    private String nombreG;

    @SerializedName("descripcion_g")
    private String descripcionG;

    @SerializedName("imagenG")
    private String imagenG;

    @SerializedName("id_brawler")
    private int idBrawler;

    public int getIdGadgets() { return idGadgets; }
    public String getNombreG() { return nombreG; }
    public String getDescripcionG() { return descripcionG; }
    public String getImagenG() { return imagenG; }
    public int getIdBrawler() { return idBrawler; }
}