package fr.mobile.horod.models;

import java.io.Serializable;

public class Fields implements Serializable {
    private double tarifhor;
    private String adresse;
    private double[] geo_point_2d;
    private String arrondt;

    public double getTarifhor() {
        return tarifhor;
    }

    public void setTarifhor(double tarifhor) {
        this.tarifhor = tarifhor;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public double[] getGeo_point_2d() {
        return geo_point_2d;
    }

    public void setGeo_point_2d(double[] geo_point_2d) {
        this.geo_point_2d = geo_point_2d;
    }

    public String getArrondt() {
        return arrondt;
    }

    public void setArrondt(String arrondt) {
        this.arrondt = arrondt;
    }
}
