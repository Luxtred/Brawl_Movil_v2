package com.example.brawlmovilv01.datos.api;

import com.example.brawlmovilv01.Sesion.LoginResponse;
import com.example.brawlmovilv01.builds.build_api.Build;
import com.example.brawlmovilv01.builds.build_api.BuildCreate;
import com.example.brawlmovilv01.builds.build_api.BuildPut;
import com.example.brawlmovilv01.cuenta.cuenta_api.Cuenta;
import com.example.brawlmovilv01.cuenta.cuenta_api.CuentaSet;
import com.example.brawlmovilv01.datos.modelos.Brawler;
import com.example.brawlmovilv01.datos.modelos.BrawlerStats;
import com.example.brawlmovilv01.datos.modelos.Gadget;
import com.example.brawlmovilv01.datos.modelos.Gear;
import com.example.brawlmovilv01.datos.modelos.Hypercharge;
import com.example.brawlmovilv01.datos.modelos.Mapa;
import com.example.brawlmovilv01.datos.modelos.Modo;
import com.example.brawlmovilv01.datos.modelos.StarPower;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // --- CUENTAS ---

    @POST("cuentas")
    Call<Cuenta> crearCuenta(@Body CuentaSet cuentaRequest);

    @FormUrlEncoded
    @POST("token")
    Call<LoginResponse> login(
            @Field("username") String username,
            @Field("password") String password
    );

    @GET("cuentas")
    Call<List<Cuenta>> getCuentas(@Query("skip") int skip, @Query("limit") int limit);


    // --- BUILDS ---

    @POST("builds")
    Call<Build> crearBuild(@Body BuildCreate buildRequest);

    @GET("builds")
    Call<List<Build>> getBuilds(
            @Query("skip") int skip,
            @Query("limit") int limit,
            @Query("id_brawler") Integer idBrawler,
            @Query("id_cuenta") Integer idCuenta
    );

    @PUT("builds/{id_build}")
    Call<Build> actualizarBuild(
            @Path("id_build") int idBuild,
            @Query("id_cuenta") int idCuenta,
            @Body BuildPut buildPut
    );

    @DELETE("builds/{build_id}")
    Call<Void> eliminarBuild(
            @Path("build_id") int buildId,
            @Query("id_cuenta") int cuentaId
    );


    // --- BRAWLERS ---

    @GET("brawlers")
    Call<List<Brawler>> getBrawlers();

    @GET("brawlers/{brawler_id}")
    Call<Brawler> getBrawlerById(@Path("brawler_id") int brawlerId);

    @GET("brawlers/{brawler_id}/gadgets")
    Call<List<Gadget>> getGadgetsForBrawler(@Path("brawler_id") int brawlerId);

    @GET("brawlers/{brawler_id}/starpowers")
    Call<List<StarPower>> getStarPowersForBrawler(@Path("brawler_id") int brawlerId);

    @GET("brawlers/{brawler_id}/gears")
    Call<List<Gear>> getGearsForBrawler(@Path("brawler_id") int brawlerId);

    @GET("brawlers/{brawler_id}/hypercharges")
    Call<List<Hypercharge>> getHyperchargesForBrawler(@Path("brawler_id") int brawlerId);


    // --- MODOS Y MAPAS ---

    @GET("modos")
    Call<List<Modo>> getModos(@Query("skip") int skip, @Query("limit") int limit);

    @GET("modos/{mode_id}")
    Call<Modo> getModoById(@Path("mode_id") int modeId);

    @GET("mapas")
    Call<List<Mapa>> getMapas(@Query("skip") int skip, @Query("limit") int limit);

    @GET("mapas/{map_id}")
    Call<Mapa> getMapaById(@Path("map_id") int mapId);

    @GET("modos/{mode_id}/mapas")
    Call<List<Mapa>> getMapasByModo(@Path("mode_id") int modeId);

    @GET("mapas/{id}/stats")
    Call<List<BrawlerStats>> getMapStats(@Path("id") int mapaId);


    // --- COMPONENTES GLOBALES ---

    @GET("gadgets")
    Call<List<Gadget>> getAllGadgets(@Query("skip") int skip, @Query("limit") int limit);

    @GET("starpowers")
    Call<List<StarPower>> getAllStarPowers(@Query("skip") int skip, @Query("limit") int limit);

}