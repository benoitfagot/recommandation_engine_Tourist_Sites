package calculate;

import lucene.MIXTEContainer;
import lucene.TraitementRequete;
import model.Hotel;
import model.SiteTouristique;
import model.Transport;
import offres.Confort;
import offres.Excursion;

import javax.sound.sampled.Mixer;
import java.util.*;

public class ChoisirExcursion {

    private List<MIXTEContainer> combinaisonsSitesMixtes= new LinkedList<MIXTEContainer>();
    private List<Excursion>excursionsChoisies= new LinkedList<Excursion>();
    //private int nbActivites;


    public double calcul_distance(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);
        double earthRadius = 6371.01; //Kilometers
        return earthRadius * Math.acos(Math.sin(lat1)*Math.sin(lat2) + Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon1 - lon2));
        //return Math.acos(Math.sin(lat1)*Math.sin(lat2) + Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon1 - lon2));
    }    /**
     * Méthode pour calculer les combinaisons des sites possibles
     *
     */
    public void calculerCombinaisonSites(List<MIXTEContainer> sites, int size, int nbActivites, int i, int index, List<MIXTEContainer> combinaison){

        // Current combination is ready to be printed, print it
        if (index == nbActivites)
        {
           // Excursion excursion = new Excursion();
            for (int j=0; j<nbActivites; j++){
                combinaisonsSitesMixtes.add(combinaison.get(j));
                //excursion.addSiteTouristiques(combinaison.get(j));
            }
            return;

        }

        // When no more elements are there to put in data[]
        if (i >= size)
            return;
        // current is included, put next at next location
        combinaison.add(index, sites.get(i));

        calculerCombinaisonSites(sites, size, nbActivites, i+1,index+1 ,combinaison );

        // current is excluded, replace it with next (Note that
        // i+1 is passed, but index is not changed)
        calculerCombinaisonSites(sites, size, nbActivites, i+1,index ,combinaison );

    }

    public List<MIXTEContainer> getCombinaisonsSitesMixtes(List<MIXTEContainer> sites, int nbActivites) {

        List<MIXTEContainer> combi= new LinkedList<MIXTEContainer>();
        calculerCombinaisonSites(sites, sites.size(),nbActivites, 0, 0, combi);
        return this.combinaisonsSitesMixtes;
    }


    /***
     * Fonction qui évalue cahque combinaison en fonction des critères de l'utilisateur et renvoie un score
     */

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
            current.setPrix(1);
        }
        else {
            current.setType_transport("Bateau");
            current.setPrix(1.5);
        }
        prix_transport = current.getPrix();
        distance = calcul_distance(h.getLatitude(),h.getLongitude(),sites.get(0).getCoordonnesX(),
                sites.get(0).getCoordonnesY());
        prix_distance = distance * prix_transport;
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
        distance = calcul_distance(sites.get(0).getCoordonnesX(),sites.get(0).getCoordonnesY(),
                sites.get(1).getCoordonnesX(), sites.get(1).getCoordonnesY());
        prix_distance = distance * prix_transport;
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
        Confort confort = new Confort();
        confort_score=confort.calculConfortExcursion(sites,h);
        //System.out.print("score"+confort_score);
        //retourner le score d'une requêtes
        return ((3*pertinence_score) + (2*price_score) + confort_score)/6;
    }


    /**
     * Fonction qui renvoie la meilleure combinaison de sitesmixtes
     */
   List<MIXTEContainer> combinaisonChoisie (List<MIXTEContainer> combinaisonSites, int nbActivites, Hotel h, int nbJours, int densite,double budgetMin, double budgetMax,double pertinenceMax){

       double score;
        List<CombinaisonScore> combiScores = new LinkedList<CombinaisonScore>();
       for (int k=0; k<combinaisonsSitesMixtes.size()/nbActivites; k++) {
           List<MIXTEContainer> combinaison= new LinkedList<MIXTEContainer>();
           for (int l = 0; l < nbActivites; l++) {
               combinaison.add(combinaisonsSitesMixtes.get(nbActivites*k+l)); //Pour chaque combinaison je calcule son score
           }
           // on calcule le score de chaque combi et on le stocke dans la liste
           score=evaluate_score(combinaison,h,nbJours,densite, budgetMin, budgetMax,pertinenceMax);
           CombinaisonScore combiScore = new CombinaisonScore(combinaison, score);
           combiScores.add(combiScore);
       }
       //Trier la liste des combinaisons choisie en fonction du score???????????????
       //Collections.sort(combiScores);
       double max=combiScores.get(0).getScore();
       int indice = 0;
       for(int w=1;w<combiScores.size();w++){
           CombinaisonScore c = combiScores.get(w);
           if (c.getScore()>max){
               max = c.getScore();
               indice = w;
           }
       }
       //Retourner la première combinaison
       return combiScores.get(indice).getCombinaison();

    }

    /**
     * Fonction qui transforme une combinaison de sitesMixte en sitesTouristique et les insère dans une excursion
     * @param excursion
     * @param sitesMixtes
     * @throws Exception
     */

    public void insertSitesMixtesExcursion(Excursion excursion, List<MIXTEContainer> sitesMixtes) {

        for(int i=0; i<sitesMixtes.size(); i++){
            MIXTEContainer siteMixte = sitesMixtes.get(i);
            SiteTouristique siteTouristique = new SiteTouristique(siteMixte.getNom(),
                    siteMixte.getType(),siteMixte.getCoordonnesX(),
                    siteMixte.getCoordonnesY(), siteMixte.getIle());
            excursion.addSiteTouristiques(siteTouristique);
        }
    }


    /**
     * Fonction qui permet de calculer les combinaisons choisies en fonction des critères de l'utilisateur
     */

    public void calculateExcurionsChoisie(List<MIXTEContainer> sites, int nbActivites,Hotel h, int nbJours, int densite,double budgetMin, double budgetMax,double pertinenceMax) {
        List<MIXTEContainer> combi= new LinkedList<MIXTEContainer>();
        // Parcourir toutes les combinaisons possibles
        calculerCombinaisonSites(sites, sites.size(),2, 0, 0, combi);
        while (! combinaisonsSitesMixtes.isEmpty()){
            List<MIXTEContainer> combinaisonChoisie = new LinkedList<MIXTEContainer>();
            combinaisonChoisie=combinaisonChoisie(combinaisonsSitesMixtes, nbActivites, h, nbJours, densite,budgetMin, budgetMax, pertinenceMax);
            Excursion excursionChoisie = new Excursion();
            insertSitesMixtesExcursion(excursionChoisie, combinaisonChoisie);
            excursionsChoisies.add(excursionChoisie);
            for (int i=0; i<combinaisonChoisie.size();i++) {
                sites.remove(combinaisonChoisie.get(i));
            }
            combinaisonsSitesMixtes.clear();
            calculerCombinaisonSites(sites, sites.size(),2, 0, 0, combi);
        }

    }



    // Getters & Setters

    public List<Excursion> getExcursionsChoisies(List<MIXTEContainer> sites, int nbActivites,Hotel h, int nbJours, int densite,double budgetMin, double budgetMax) {


        List<Excursion> excursionChoisiesDensite= new LinkedList<Excursion>();
        double pertinenceMax=sites.get(0).getScore();

        calculateExcurionsChoisie( sites, nbActivites,h, nbJours, densite,budgetMin, budgetMax,pertinenceMax);
        for (int k=0; k<densite; k++) {
            excursionChoisiesDensite.add(excursionsChoisies.get(k));
        }

        return excursionChoisiesDensite;
    }



   /* public List<Excursion> getexcursionsChoisies(List<SiteTouristique> sites,int nbActivites) {
        calculateExcurionsChoisie(sites,nbActivites);
        return excursionsChoisies;
    }*/

    public void setExcursionspertinenetes(List<Excursion> excursionspertinnentes) {
        this.excursionsChoisies = excursionspertinnentes;
    }

    public static void main (String[] args) throws Exception {
        // Petit test de la fonction
        List<MIXTEContainer> sites = new LinkedList<MIXTEContainer>();
        ChoisirExcursion cl = new ChoisirExcursion();
        String requete="SELECT nom, type, longitude, latitude FROM SiteTouristique with île~2";

        /*List<MIXTEContainer> sites = new LinkedList<MIXTEContainer>();
        int nbActivites= 2;

        sites.add(new MIXTEContainer("Un"));
        sites.add(new MIXTEContainer("Deux"));
        sites.add(new MIXTEContainer("Trois"));
        sites.add(new MIXTEContainer("Quatre"));
        sites.add(new MIXTEContainer("Cinq"));
        sites.add(new MIXTEContainer("Six"));
        sites.add(new MIXTEContainer("Sept"));
        sites.add(new MIXTEContainer("Huit"));
        sites.add(new MIXTEContainer("Neuf"));
        sites.add(new MIXTEContainer("Dix"));
        sites.add(new MIXTEContainer("Onze"));
        sites.add(new MIXTEContainer("Douze"));
        sites.add(new MIXTEContainer("Treize"));
        sites.add(new MIXTEContainer("Quatorze"));
        sites.add(new MIXTEContainer("Quinze"));
        //Hotel hotel = new Hotel();*/


        //Récupérer les listes des sites MIXTE containers à partir de la requete
        TraitementRequete tr = new TraitementRequete();

        //Là ou on va stocker les résultats
        List<Excursion> excursionsChoisies = new LinkedList<Excursion>();




        Hotel hotel = new Hotel(); //Comment génèrer un hotel?
        int nbActivites=2;
        int nbJours=7;
        int densite=3; //// Définir la densité en fonction du critère du client
        double budgetMin=0;
        double budgetMax=100;
        tr.execute_request(requete);
        sites=tr.getMixteContainers();
        excursionsChoisies=cl.getExcursionsChoisies(sites, nbActivites, hotel,nbJours,densite, budgetMin,budgetMax);


        // Tester les combinaison
       /* List<MIXTEContainer>combinaisons= new LinkedList<MIXTEContainer>();
        combinaisons=cl.getCombinaisonsSitesMixtes(sites, nbActivites);

        //Afficher les combinaison des sitesMIXTES
        for (int k=0; k<combinaisons.size()/nbActivites; k++){
            //List<MIXTEContainer> array=combinaisons.get(k).getSiteTouristiques();
            for (int l=0;l<nbActivites; l++){
                System.out.println(combinaisons.get(nbActivites*k+l).getNom());
            }
            System.out.println("\n");
        }

      // Tester les combinaison Choisies
        List<Excursion>excursions = cl.getExcursionsChoisies(sites, nbActivites);
        //cl.calculateExcurionsChoisie(sites,nbActivites);*/
        //Afficher toutes les excursions choisies
      /* for (int k=0; k<excursionsChoisies.size(); k++){
            List<SiteTouristique> sitesExcursion=excursionsChoisies.get(k).getSiteTouristiques();
           //System.out.println("\n");
            for (int l=0;l<sitesExcursion.size(); l++){
                //System.out.println(""+sitesExcursion.get(l).getNom());
            }
            //System.out.println("\n");
        }
        // Collections.sort(cl.combinaisonsexcursions,new Excursion().getPrice());
    */

    }










}
