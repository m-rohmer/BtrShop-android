package io.btrshop.products.domain.model;

/**
 * Created by charlie on 2/12/16.
 */

public class BeaconObject {

    private double distance;
    private String UUID;
    private Position position;


    public BeaconObject(double distance, String UUID) {
        this.distance = distance;
        this.UUID = UUID;
    }


    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
