package com.example.brawlmovilv01.datos.modelos;
import com.google.gson.annotations.SerializedName;

public class Modo {

    @SerializedName("id_mode")
    private int idMode;

    @SerializedName("name_m")
    private String nameM;

    @SerializedName("description_m")
    private String descriptionM;

    @SerializedName("imagen_icono")
    private String imagenIcono;

    @SerializedName("color_fondo")
    private String colorFondo;

    public int getIdMode() { return idMode; }
    public String getNameM() { return nameM; }
    public String getDescriptionM() { return descriptionM; }
    public String getImagenIcono() { return imagenIcono; }
    public String getColorFondo() { return colorFondo; }
}