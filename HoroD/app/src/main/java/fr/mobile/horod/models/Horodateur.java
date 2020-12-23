package fr.mobile.horod.models;

public class Horodateur {
    private double tarif;
    private String adresse;

    public Horodateur(double tarif, String adresse) {
        this.tarif = tarif;
        this.adresse = adresse;
    }

    public double getTarif() {
        return tarif;
    }

    public void setTarif(double tarif) {
        this.tarif = tarif;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
}
