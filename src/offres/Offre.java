package offres;

import lucene.TraitementRequete;
import model.Hotel;
import offres.ClientParameters;
import offres.Excursion;
import persistence.JdbcConnection;
import  calculate.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.*;

public class Offre {
    private List<Excursion> excursions = new LinkedList<Excursion>();
    private int prix;
    private Hotel hotel;

    public Offre(){}

    public List<Excursion> getExcursions() {
        return excursions;
    }

    public void setExcursions(List<Excursion> excursions) {
        this.excursions = excursions;
    }

    public void addExcursions (Excursion exc){
        this.excursions.add(exc);
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    @Override
    public String toString() {
        return "Offre{" +
                "excursions=" + excursions +
                ", prix=" + prix +
                ", hotel=" + hotel +
                '}';
    }

    public List<Offre> createOffre(ClientParameters cp) throws Exception{

        ChoisirExcursion calculator = new ChoisirExcursion();
        List<Offre> offres = new LinkedList<Offre>();
        List<Hotel> hotels = new LinkedList<Hotel>();
        int nbActivites=2;


        //OBTENIR LES 3 HOTELS LES PLUS CHERS DANS LE BUDGET/2


        String price_req = "SELECT * FROM Hotel WHERE prix BETWEEN " + (cp.getPrixMin()/2)/cp.getNbJours() + " AND "+ (cp.getPrixMax()/2)/cp.getNbJours() +" ORDER BY prix DESC LIMIT 3";
        //System.out.println(price_req);
        Connection dbConnection = JdbcConnection.getConnection();
        PreparedStatement preparedStatement = dbConnection.prepareStatement(price_req);
        ResultSet rs = preparedStatement.executeQuery();
        while ( rs.next() ) {
            Hotel h1=new Hotel(rs.getString("nom"), rs.getFloat("longitude"),
                    rs.getFloat("latitude"),rs.getDouble("prix"),
                    rs.getString("ile"));
            hotels.add(h1);
        }




        //transformer les mots clés en requête Lucene
        String req = cp.getKeywords();
        req = req.replace("et","AND");
        req = req.replace("ou", "OR");
        //traiter les virgules
        req = "("+req;
        req = req.replace(",",")AND(");
        req = req+")";

        //traitement requête
        TraitementRequete tr = new TraitementRequete();
        String full_req="";
        /*if(cp.getKeywords().contains("historique") && !cp.getKeywords().contains("activité")){
            full_req = "SELECT nom, type, longitude, latitude, ile FROM SiteTouristique" +
        }*/
        full_req += "SELECT nom, type, longitude, latitude, ile FROM SiteTouristique with "+req;
        tr.execute_request(full_req);

        // Pour chaque hotel je crée une offre
        for (int j=0; j<hotels.size();j++){
            //System.out.println("\n");
            List <Excursion> excursionsChoisies = new LinkedList<>();
            excursionsChoisies=calculator.getExcursionsChoisies(tr.getMixteContainers() ,nbActivites, hotels.get(j),cp.getNbJours(),cp.getDensite(), cp.getPrixMin(),cp.getPrixMax());
            Offre offre= new Offre();
            offre.setHotel(hotels.get(j));
            offre.setExcursions(excursionsChoisies);
            offres.add(offre);
        }
        return offres;
    }
}