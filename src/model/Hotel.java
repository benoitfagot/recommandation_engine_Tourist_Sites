package model;

public class Hotel {

    private String nom;
    private double longitude;
    private double latitude;
    private double prix;
    private String ile;

    public Hotel(String nom, double longitude, double latitude, double prix, String ile) {
        this.nom = nom;
        this.longitude = longitude;
        this.latitude = latitude;
        this.prix = prix;
        this.ile = ile;
    }

    public Hotel() {
    }


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public String getIle() {
        return ile;
    }

    public void setIle(String ile) {
        this.ile = ile;
    }
}
