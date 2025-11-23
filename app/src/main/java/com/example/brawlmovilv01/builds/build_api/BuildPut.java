package com.example.brawlmovilv01.builds.build_api;

import com.google.gson.annotations.SerializedName;

public class BuildPut {

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

    public BuildPut(int idBrawler, Integer idStarPower, Integer idGadgets, Integer idGears1, Integer idGears2, Integer idHypercharge) {
        this.idBrawler = idBrawler;
        this.idStarPower = idStarPower;
        this.idGadgets = idGadgets;
        this.idGears1 = idGears1;
        this.idGears2 = idGears2;
        this.idHypercharge = idHypercharge;
    }
}