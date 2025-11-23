package com.example.brawlmovilv01.builds.build_api;

import com.google.gson.annotations.SerializedName;

public class BuildCreate {

    @SerializedName("id_cuenta")
    private int idCuenta;

    @SerializedName("id_brawler")
    private int idBrawler;

    @SerializedName("id_starPower")
    private Integer idStarPower;

    @SerializedName("id_gadgets")
    private Integer idGadgets;

    @SerializedName("id_gears_1")
    private Integer idGears1;

    @SerializedName("id_gears_2")
    private Integer idGears2;

    @SerializedName("id_hypercharge")
    private Integer idHypercharge;

    public BuildCreate(int idCuenta, int idBrawler, Integer idStarPower, Integer idGadgets, Integer idGears1, Integer idGears2, Integer idHypercharge) {
        this.idCuenta = idCuenta;
        this.idBrawler = idBrawler;
        this.idStarPower = idStarPower;
        this.idGadgets = idGadgets;
        this.idGears1 = idGears1;
        this.idGears2 = idGears2;
        this.idHypercharge = idHypercharge;
    }
}