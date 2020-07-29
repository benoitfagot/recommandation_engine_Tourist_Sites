package lucene;
import java.io.*;
import java.nio.file.*;

import javafx.util.Pair;
import persistence.JdbcConnection;

import java.io.PrintWriter;
import java.sql.*;
import java.util.*;
import java.util.Map.Entry;

public class TraitementRequete {

    private Lucene lucene_traitement;
    private List<SQLContainer> sqlContainers = new LinkedList<>();
    private List<MIXTEContainer> mixteContainers = new LinkedList<>();

    public TraitementRequete(){}

    public TraitementRequete(Lucene lucene){
        this.lucene_traitement=lucene;
    }


    public Lucene getLucene_traitement() {
        return lucene_traitement;
    }

    public void setLucene_traitement(Lucene lucene_traitement) {
        this.lucene_traitement = lucene_traitement;
    }

    public List<SQLContainer> getSqlContainers() {
        return sqlContainers;
    }

    public List<MIXTEContainer> getMixteContainers() {
        return mixteContainers;
    }

    public void addSQLContainers(SQLContainer container) {
        this.sqlContainers.add(container);
    }

    public void addMIXTEContainers(MIXTEContainer container) {
        this.mixteContainers.add(container);
    }

    //exemple de requête attendue
    //SELECT nom from Site
    //where type='historique'
    //with sculpture Renaissance
    public void execute_request(String requete) throws Exception{
        Connection dbConnection = JdbcConnection.getConnection();
        int id_site;
        String id;
        String nom;
        String type;
        String ile;
        double longitude;
        double latitude;
        boolean idDemande=false;
        boolean nomDemande=false;
        boolean typeDemande=false;
        boolean longitudeDemande=false;
        boolean latitudeDemande=false;
        boolean ileDemande=false;

        if (requete.contains("with")==true){ //requête mixte BDE
            String[] parts = requete.split("with");
            String requete_sql = parts[0];
            String requete_lucene = parts[1];
            if(requete_sql.contains("id_site")!=true) { //si la requête ne contient pas la clé, rajouter la clé
                requete_sql = requete_sql.substring(6);
                requete_sql = "SELECT id_site," + requete_sql;
            }
            PreparedStatement preparedStatement = dbConnection.prepareStatement(requete_sql);
            ResultSet rs = preparedStatement.executeQuery();
            Lucene lucene_traitement= new Lucene(requete_lucene);
            this.setLucene_traitement(lucene_traitement);


            //TRAITEMENT DE LA PARTIE TEXTUELLE
            this.getLucene_traitement().lucene_main(); //cela va remplir l'arrayList de l'attribut lucene_traitement
            //avec les résultats de la requête sous forme de paires (nom, score)


            //TRAITEMENT POUR CHAQUE RESULTAT DE LA PARTIE SQL
            String[] words = requete_sql.split("\\W+");
            for(int b=0;b<words.length;b++){
                if(words[b]!="SELECT" && words[b]!="FROM"){
                    switch(words[b]){
                        case "nom": nomDemande=true;
                            break;
                        case "type": typeDemande=true;
                            break;
                        case "longitude":longitudeDemande=true;
                            break;
                        case "latitude":latitudeDemande=true;
                            break;
                        case "ile":ileDemande=true;
                        default:break;
                    }
                }
                if(words[b]=="FROM")break;
            }
            while ( rs.next() ) {
                SQLContainer container = new SQLContainer();
                id_site = rs.getInt("id_site");
                id = String.valueOf(id_site);
                container.setId(id_site);
                if(nomDemande==true){
                    nom=rs.getString("nom");
                    container.setNom(nom);
                }
                if(typeDemande==true){
                    type=rs.getString("type");
                    container.setType(type);
                }
                if(longitudeDemande==true){
                    longitude=rs.getDouble("longitude");
                    container.setCoordonnesX(longitude);
                }
                if(latitudeDemande==true){
                    latitude=rs.getDouble("latitude");
                    container.setCoordonnesY(latitude);
                }
                if(ileDemande==true){
                    ile=rs.getString("ile");
                    container.setIle(ile);
                }
                this.getSqlContainers().add(container);

                for(int i=0;i<this.getLucene_traitement().getResults().size();i++){
                    if (id.equals(this.getLucene_traitement().getResults().get(i).getValue())){ //Si la clé SQL correspond à la clé du repertoire R
                        Couple couple = new Couple(this.getLucene_traitement().getResults().get(i).getKey(),
                                this.getLucene_traitement().getResults().get(i).getValue()); //Alors le résultat appartient à la requête et on le stocke
                        MIXTEContainer container1 = new MIXTEContainer(container);
                        container1.setScore(couple.getKey());
                        this.getMixteContainers().add(container1);
                        //this.getTm().put(couple.getKey(),couple.getValue());
                    }
                }
            }
            //TRIER LES RESULTATS DANS L ORDRE DECROISSANT DE SCORE LUCENE
            Collections.sort(this.getMixteContainers());
            MIXTEContainer current;
            for(int q=0;q<this.getMixteContainers().size();q++){
                current=this.getMixteContainers().get(q);
                System.out.println(current.toString());
            }

            preparedStatement.close();
        }
        else{ //requête simple SQL
            PreparedStatement preparedStatement = dbConnection.prepareStatement(requete);
            ResultSet rs = preparedStatement.executeQuery();
            String[] words = requete.split("\\W+");
            for(int b=0;b<words.length;b++){
                if(words[b]!="SELECT" && words[b]!="FROM"){
                    switch(words[b]){
                        case "id_site": idDemande=true;
                            break;
                        case "nom": nomDemande=true;
                            break;
                        case "type": typeDemande=true;
                            break;
                        case "longitude":longitudeDemande=true;
                            break;
                        case "latitude":latitudeDemande=true;
                            break;
                        case "ile":ileDemande=true;
                        default:break;
                    }
                }
                if(words[b]=="FROM")break;
            }
            while ( rs.next() ) {
                SQLContainer container = new SQLContainer();
                if (idDemande == true) {
                    id_site = rs.getInt("id_site");
                    container.setId(id_site);
                }
                if (nomDemande == true) {
                    nom = rs.getString("nom");
                    container.setNom(nom);
                }
                if (typeDemande == true) {
                    type = rs.getString("type");
                    container.setType(type);
                }
                if (longitudeDemande == true) {
                    longitude = rs.getDouble("longitude");
                    container.setCoordonnesX(longitude);
                }
                if (latitudeDemande == true) {
                    latitude = rs.getDouble("latitude");
                    container.setCoordonnesY(latitude);
                }
                if (ileDemande == true) {
                    ile = rs.getString(("ile"));
                    container.setIle(ile);
                }
                this.getSqlContainers().add(container);
            }
            SQLContainer current;
            for(int q=0;q<this.getSqlContainers().size();q++){
                current=this.getSqlContainers().get(q);
                System.out.println(current.toString());
            }
            preparedStatement.close();

        }


    }

}