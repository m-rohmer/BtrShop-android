package io.btrshop.products.domain.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by charlie on 6/01/17.
 *
 * The purpose of this class, is to produce an object we can use for the post service {@link io.btrshop.data.source.api.ProductsService}
 */
public class BeaconJson {

    @SerializedName("dist")
    private double dist;

    @SerializedName("uuid")
    private String uuid;

    @SerializedName("closeCoef")
    private float closeCoef;

    @SerializedName("farCoef")
    private float farCoef;


    /**
     * Constructor.
     *
     * @param uuid
     * @param distance
     */
    public BeaconJson(String uuid, double distance, float closeCoef, float farCoef) {
        this.uuid = uuid;
        this.dist = distance/1000;
        this.closeCoef = closeCoef;
        this.farCoef = farCoef;
    }

    public double getDist() {
        return dist;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public float getCloseCoef() {
        return closeCoef;
    }

    public void setCloseCoef(float closeCoef) {
        this.closeCoef = closeCoef;
    }

    public float getFarCoef() {
        return farCoef;
    }

    public void setFarCoef(float farCoef) {
        this.farCoef = farCoef;
    }
}
