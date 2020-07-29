package testSimulation;

import model.Hotel;
import model.SiteTouristique;
import persistence.JdbcConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersistenceHotel {


    public static void main(String[] args) {
        //afficher();
        ArrayList<SiteTouristique> rep ;
        rep = requete();
        for(int i=0;i<rep.size();i++){
            System.out.println(rep.get(i));
        }


    }


    public static void afficher() {
        String req = "SELECT * FROM SiteTouristique";
        try {
            Connection dbConnection = JdbcConnection.getConnection();
           /* Statement stmt = dbConnection.createStatement();
            ResultSet rs;*/
            PreparedStatement preparedStatement = dbConnection.prepareStatement(req);
            ResultSet rs = preparedStatement.executeQuery();
            // rs = stmt.executeQuery("SELECT * FROM SiteTouristique");
            //On récupère les MetaData
            ResultSetMetaData resultMeta = rs.getMetaData();
            //On affiche le nom des colonnes
            for(int i = 1; i <= resultMeta.getColumnCount(); i++)
                System.out.print("\t" + resultMeta.getColumnName(i).toUpperCase() + "\t ");
            System.out.println("\n----------------------------------------------");

            int i =0;

            while ( rs.next() ) {
                String nom = rs.getString("nom");
                String type = rs.getString("type");
                float longitude = rs.getFloat("longitude");
                float latitude = rs.getFloat("latitude");
                System.out.println( nom+ "\t" +type + "\t" + longitude + "\t" + latitude+"\t");
            }
            preparedStatement.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());

        }
    }

    public static ArrayList<SiteTouristique> requete() {
        String req = "SELECT * FROM SiteTouristique";
        //ArrayList<SiteTouristique> site = new ArrayList<SiteTouristique>();
        ArrayList<SiteTouristique> site = new ArrayList();
        try {
            Connection dbConnection = JdbcConnection.getConnection();
            PreparedStatement preparedStatement = dbConnection.prepareStatement(req);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                SiteTouristique siteT = new SiteTouristique();
                siteT.setNom(rs.getString("nom"));
                siteT.setLongitude(rs.getDouble("longitude"));
                siteT.setLatitude(rs.getDouble("latitude"));
                site.add(siteT);
            }
            preparedStatement.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return site;
    }

    public static  void distanceHotelSite()  {

        List<Hotel> hotels = new ArrayList();
        try {

            String selectAddressQuery = "SELECT * FROM Hotel";
            Connection dbConnection = JdbcConnection.getConnection();
            PreparedStatement preparedStatement = dbConnection.prepareStatement(selectAddressQuery);

            ResultSet rs = preparedStatement.executeQuery();

            while ( rs.next() ) {

                Hotel hotel = new Hotel();
                hotel.setNom(rs.getString("nom"));
                hotel.setLongitude(rs.getFloat("longitude"));
                hotel.setLatitude(rs.getFloat("latitude"));
                //System.out.println(hotel.getNom()+"   " +hotel.getLatitude());

                hotels.add(hotel);
            }
            //preparedStatement.close();
        }catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());

        }
        System.out.println(hotels);
    }



}
