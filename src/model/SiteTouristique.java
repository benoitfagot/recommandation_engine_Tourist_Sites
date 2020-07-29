package model;

public class SiteTouristique {
    private String nom;
    private String type;
    private double longitude;
    private double latitude;
    private String ile;

    public SiteTouristique(String nom, String type, double longitude, double latitude, String ile) {
        this.nom = nom;
        this.type = type;
        this.longitude = longitude;
        this.latitude = latitude;
        this.ile = ile;
    }

    public SiteTouristique() {
    }

    //constructeur temporaire juste pour le test

    public SiteTouristique(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getIle() {
        return ile;
    }

    public void setIle(String ile) {
        this.ile = ile;
    }

    public double calcul_distance(SiteTouristique s1, SiteTouristique s2) {
        double lat1 = s1.getLatitude();
        double lat2 = s2.getLatitude();
        double lon1 = s1.getLongitude();
        double lon2 = s2.getLongitude();
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);
        double earthRadius = 6371.01; //Kilometers
        return earthRadius * Math.acos(Math.sin(lat1)*Math.sin(lat2) + Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon1 - lon2));
        //return Math.acos(Math.sin(lat1)*Math.sin(lat2) + Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon1 - lon2));
    }
}
