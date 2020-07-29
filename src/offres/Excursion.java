package offres;

import lucene.MIXTEContainer;
import lucene.SQLContainer;
import lucene.TraitementRequete;
import model.Hotel;
import model.SiteTouristique;
import model.Transport;
import persistence.JdbcConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Excursion {

    private List<SiteTouristique> siteTouristiques = new LinkedList<SiteTouristique>();

    private Hotel hotel;

    public List<SiteTouristique> getSiteTouristiques() {
        return siteTouristiques;
    }

    public Excursion(List<SiteTouristique> siteTouristiques) {
        this.siteTouristiques = siteTouristiques;
    }

    public Excursion() {
    }

    public void initExcursionSimple(String req, double prix) throws Exception {
        this.initSiteTouristiquesSqlContainer(req);
        this.initHotel(prix);
    }

    public void initExcursionMixte(String req, double prix) throws Exception {
        this.initSiteTouristiquesMixteContainer(req);
        this.initHotel(prix);
    }


    public void addSiteTouristiques(SiteTouristique siteTouristique){
        this.siteTouristiques.add(siteTouristique);
        //siteTouristiques.add(siteTouristique);
    }

    public void setSiteTouristiques(List<SiteTouristique> siteTouristiques) {
        this.siteTouristiques = siteTouristiques;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    /*public double calculDistance(double x1, double y1, double x2, double y2){
        return Math.sqrt((x1-x2) + (y1-y2));
    }*/




    public double calcul_distance(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);
        double earthRadius = 6371.01; //Kilometers
        return earthRadius * Math.acos(Math.sin(lat1)*Math.sin(lat2) + Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon1 - lon2));
        //return Math.acos(Math.sin(lat1)*Math.sin(lat2) + Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon1 - lon2));
    }

    public double distance_excursion(Hotel hotel){
        double somme = 0;
        somme += calcul_distance(hotel.getLatitude(), hotel.getLongitude(),
                siteTouristiques.get(0).getLatitude(), siteTouristiques.get(0).getLongitude());
        for(int i=0; i < (siteTouristiques.size())-1; i++){
             somme += calcul_distance(siteTouristiques.get(i).getLatitude(),siteTouristiques.get(i).getLongitude(),
                    siteTouristiques.get(i+1).getLatitude(), siteTouristiques.get(i+1).getLongitude());
        }
        int last = (siteTouristiques.size())-1;
        somme += calcul_distance(hotel.getLatitude(), hotel.getLongitude(),
                siteTouristiques.get(last).getLatitude(), siteTouristiques.get(last).getLongitude());
        return somme;
    }

    public void initSiteTouristiquesSqlContainer(String req) throws Exception {
        TraitementRequete tr = new TraitementRequete();
        tr.execute_request(req);
        for(int i=0; i<tr.getSqlContainers().size(); i++){
            SQLContainer sql = tr.getSqlContainers().get(i);
            SiteTouristique site = new SiteTouristique(sql.getNom(),
                    sql.getType(),sql.getCoordonnesX(),
                    sql.getCoordonnesY(), sql.getIle());
            this.addSiteTouristiques(site);
        }
    }

    public void initSiteTouristiquesMixteContainer(String req) throws Exception {
        TraitementRequete tr = new TraitementRequete();
        tr.execute_request(req);
        for(int i=0; i<tr.getMixteContainers().size(); i++){
            MIXTEContainer sql = tr.getMixteContainers().get(i);
            SiteTouristique site = new SiteTouristique(sql.getNom(),
                    sql.getType(),sql.getCoordonnesX(),
                    sql.getCoordonnesY(), sql.getIle());
            this.addSiteTouristiques(site);
        }
    }

    public void initHotel(double prix) throws SQLException {
        List<Hotel> hotels = new LinkedList<Hotel>();
        String selectAddressQuery = "SELECT * FROM Hotel WHERE prix <"+prix;
        Connection dbConnection = JdbcConnection.getConnection();
        PreparedStatement preparedStatement = dbConnection.prepareStatement(selectAddressQuery);
        ResultSet rs = preparedStatement.executeQuery();
        while ( rs.next() ) {
            Hotel hotel = new Hotel();
            hotel.setNom(rs.getString("nom"));
            hotel.setLongitude(rs.getDouble("longitude"));
            hotel.setLatitude(rs.getDouble("latitude"));
            //System.out.println(hotel.getNom()+"   " +hotel.getLatitude());
            hotels.add(hotel);
        }
        preparedStatement.close();

        //remplir le tableau avec la distance de chaque hotel et les differentes sites
        double tab[] = new double[hotels.size()];
        for(int i=0; i < hotels.size(); i++){
            double distance = 0;
            distance = distance_excursion(hotels.get(i));
            tab[i] = distance;
        }

        //recuperer l'indice de l'hotel qui a la plus petite distance
        double min = tab[0];
        int indice = 0 ;
        for (int j=1; j < tab.length; j++){
            if(tab[j] < min) {
                min = tab[j];
                indice = j;
            }
        }
        this.hotel = hotels.get(indice);//l'hotel ayant la plus petite distance
    }

    public double evaluate_score(List<MIXTEContainer> sites, Hotel h, int nb_jours, int densite, double budgetMin,
                                 double budgetMax, double pertinenceMax){
        double pertinence_score=0;
        double price_score=0;
        double confort_score=0;

        //partie pertinence coefficient 3, max=10
        double score = sites.get(0).getScore() + sites.get(1).getScore();
        if (score == pertinenceMax){
            pertinence_score=10;
        }
        else if (score >= (pertinenceMax-(pertinenceMax*0.1)) && score <= pertinenceMax){ //entre 90 et 100% du scoreMax
            pertinence_score=9;
        }
        else if (score >= (pertinenceMax-(pertinenceMax*0.2)) && score <= pertinenceMax){ //entre 80 et 90% du scoreMax
            pertinence_score=8;
        }
        else if (score >= (pertinenceMax-(pertinenceMax*0.3)) && score <= pertinenceMax){ //entre 70 et 80% du scoreMax
            pertinence_score=7;
        }
        else if (score >= (pertinenceMax-(pertinenceMax*0.4)) && score <= pertinenceMax){ //entre 60 et 70% du scoreMax
            pertinence_score=6;
        }
        else if (score >= (pertinenceMax-(pertinenceMax*0.5)) && score <= pertinenceMax){ //entre 50 et 60% du scoreMax
            pertinence_score=5;
        }
        else if (score >= (pertinenceMax-(pertinenceMax*0.6)) && score <= pertinenceMax){ //entre 40 et 50% du scoreMax
            pertinence_score=4;
        }
        else if (score >= (pertinenceMax-(pertinenceMax*0.7)) && score <= pertinenceMax){ //entre 30 et 40% du scoreMax
            pertinence_score=3;
        }
        else if (score >= (pertinenceMax-(pertinenceMax*0.8)) && score <= pertinenceMax){ //entre 20 et 30% du scoreMax
            pertinence_score=2;
        }
        else if (score >= (pertinenceMax-(pertinenceMax*0.9)) && score <= pertinenceMax){ //entre 10 et 20% du scoreMax
            pertinence_score=1;
        }
        else if (score <= (pertinenceMax-(pertinenceMax*0.9)) && score >= 0){ //entre 0 et 10% du scoreMax
            pertinence_score=0.5;
        }
        else{pertinence_score=0;}

        //partie prix coefficient2, max=10

        Transport current = new Transport();
        double distance;
        double prix_transport;
        double prix_distance;
        double prix_excursion=0;

        // Hotel --> Site0
        if (h.getIle() == sites.get(0).getIle()){
            current.setType_transport("Autobus");
        }
        else current.setType_transport("Bateau");
        prix_transport = current.getPrix();
        distance = calcul_distance(h.getLatitude(),h.getLongitude(),sites.get(0).getCoordonnesX(),
                sites.get(0).getCoordonnesY());
        prix_distance = distance * prix_transport;
        prix_excursion += prix_distance;

        // Site0 --> Site1
        if (sites.get(0).getIle() == sites.get(1).getIle()){
            current.setType_transport("Autobus");
        }
        else current.setType_transport("Bateau");
        prix_transport = current.getPrix();
        distance = calcul_distance(sites.get(0).getCoordonnesX(),sites.get(0).getCoordonnesY(),
                sites.get(1).getCoordonnesX(), sites.get(1).getCoordonnesY());
        prix_distance = distance * prix_transport;
        prix_excursion += prix_distance;


        // Site1 --> Hotel
        if (h.getIle() == sites.get(1).getIle()){
            current.setType_transport("Autobus");
        }
        else current.setType_transport("Bateau");
        prix_transport = current.getPrix();
        distance = calcul_distance(h.getLatitude(),h.getLongitude(),sites.get(1).getCoordonnesX(),
                sites.get(1).getCoordonnesY());
        prix_distance = distance * prix_transport;
        prix_excursion += prix_distance;

        double budget_excursionsMin = budgetMin - h.getPrix()*nb_jours;
        budget_excursionsMin -= 0.1 * budget_excursionsMin; //marge d'erreur de 10%
        double budget_excursionsMax = budgetMax - h.getPrix()*nb_jours;
        budget_excursionsMax += 0.1 * budget_excursionsMax; //marge d'erreur de 10%
        double budget_excursionMin = budget_excursionsMin/densite;
        double budget_excursionMax = budget_excursionsMax/densite;

        if(prix_excursion >= budget_excursionMin && prix_excursion <= budget_excursionMax){
            price_score = 10;
        }
        else if(prix_excursion >= (budget_excursionMin-(0.1*budget_excursionMin)) &&
                prix_excursion <= (budget_excursionMax+(0.1*budget_excursionMax))) {
            price_score = 5;
        }
        else if(prix_excursion >= (budget_excursionMin-(0.2*budget_excursionMin)) &&
                prix_excursion <= (budget_excursionMax+(0.2*budget_excursionMax))) {
            price_score = 2.5;
        }
        else{
            price_score = 0;
        }

        //partie confort coefficient1, max=10

        return ((3*pertinence_score) + (2*price_score) + confort_score)/6;
    }

    public double calculate_prix_excursion(){
        double prix_total = 0;
        Hotel h = this.getHotel();
        List<SiteTouristique> sites = this.getSiteTouristiques();

        Transport current = new Transport();
        double distance;
        double prix_transport;
        double prix_distance;
        double prix_excursion=0;

        // Hotel --> Site0
        if (h.getIle() == sites.get(0).getIle()){
            current.setType_transport("Autobus");
            current.setPrix(1);
        }
        else {
            current.setType_transport("Bateau");
            current.setPrix(1.5);
        }
        prix_transport = current.getPrix();
        distance = calcul_distance(h.getLatitude(),h.getLongitude(),sites.get(0).getLongitude(),
                sites.get(0).getLatitude());
        prix_distance = (distance/17)/10 * prix_transport;
        prix_excursion += prix_distance;

        // Site0 --> Site1
        if (sites.get(0).getIle() == sites.get(1).getIle()){
            current.setType_transport("Autobus");
            current.setPrix(1);
        }
        else {
            current.setType_transport("Bateau");
            current.setPrix(1.5);
        }
        prix_transport = current.getPrix();
        distance = calcul_distance(sites.get(0).getLongitude(),sites.get(0).getLatitude(),
                sites.get(1).getLongitude(), sites.get(1).getLatitude());
        prix_distance = (distance/17)/10 * prix_transport;
        prix_excursion += prix_distance;


        // Site1 --> Hotel
        if (h.getIle() == sites.get(1).getIle()){
            current.setType_transport("Autobus");
            current.setPrix(1);
        }
        else {
            current.setType_transport("Bateau");
            current.setPrix(1.5);
        }
        prix_transport = current.getPrix();
        distance = calcul_distance(h.getLatitude(),h.getLongitude(),sites.get(1).getLongitude(),
                sites.get(1).getLatitude());
        prix_distance = (distance/17)/10 * prix_transport;
        prix_excursion += prix_distance;

        prix_total += prix_excursion;
        return prix_total;
    }

}
