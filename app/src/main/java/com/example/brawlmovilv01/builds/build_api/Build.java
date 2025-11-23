package com.example.brawlmovilv01.builds.build_api;

import com.example.brawlmovilv01.cuenta.cuenta_api.Cuenta;
import com.example.brawlmovilv01.datos.modelos.Gadget;
import com.example.brawlmovilv01.datos.modelos.Gear;
import com.example.brawlmovilv01.datos.modelos.Hypercharge;
import com.example.brawlmovilv01.datos.modelos.StarPower;
import com.google.gson.annotations.SerializedName;

public class Build {

    @SerializedName("id_build")
    private int idBuild;

    @SerializedName("id_brawler")
    private int idBrawler;

    @SerializedName("cuenta")
    private Cuenta cuenta;

    @SerializedName("starpower")
    private StarPower starpower;

    @SerializedName("gadget")
    private Gadget gadget;

    @SerializedName("gear1")
    private Gear gear1;

    @SerializedName("gear2")
    private Gear gear2;

    @SerializedName("hypercharge")
    private Hypercharge hypercharge;

    @SerializedName("brawler_pin")
    private String brawlerPin;

    // --- Getters ---
    public int getIdBuild() {
        return idBuild;
    }

    public int getIdBrawler() {
        return idBrawler;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public StarPower getStarpower() {
        return starpower;
    }

    public Gadget getGadget() {
        return gadget;
    }

    public Gear getGear1() {
        return gear1;
    }

    public Gear getGear2() {
        return gear2;
    }

    public Hypercharge getHypercharge() {
        return hypercharge;
    }

    public String getBrawlerPin() {
        return brawlerPin;
    }
}