package testSimulation;
import model.SiteTouristique;
import offres.*;

import java.util.LinkedList;
import java.util.List;

public class TestOffre {



    public static void main (String[] args) throws Exception {


        double prixMin = 200;
        double prixMax = 1000;
        double confortMin = 5;
        double confortLax = 10;
        int densite = 3;
        String keywords = "île~2";
        int nbJours = 7;

        Offre offre = new Offre();
        ClientParameters cp = new ClientParameters(prixMin, prixMax, confortMin, confortLax, densite, keywords, nbJours);
        List<Offre> offres = new LinkedList<Offre>();

        offres = offre.createOffre(cp);

        //Afficher les offres
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");

        System.out.println("Pour vos paramètres :");
        System.out.println("Fourchette prix :["+cp.getPrixMin()+"€ - "+cp.getPrixMax()+"€]  Nb excursions souhaités: "
                +cp.getDensite()+"  Durée séjour: "+cp.getNbJours()+" jours Mots-clé: "+cp.getKeywords());
        System.out.println("Voici les 3 meilleures offres proposées: ");

        for (int i = 0; i < offres.size(); i++) {
            System.out.println("Offre "+(i+1));
            int prix_offre=0;
            prix_offre+=offres.get(i).getHotel().getPrix()*cp.getNbJours();
            System.out.println("Pour l'hôtel "+offres.get(i).getHotel().getNom()+" qui charge "+
                    offres.get(i).getHotel().getPrix()+"€ par nuit, nous vous proposons :");
            for (int k=0; k<offres.get(i).getExcursions().size(); k++){//chaque excursion
                offres.get(i).getExcursions().get(k).setHotel(offres.get(i).getHotel());
                System.out.println("Excursion " + (k+1) +", prix : " + offres.get(i).getExcursions().get(k).calculate_prix_excursion()+"€");
                prix_offre+=offres.get(i).getExcursions().get(k).calculate_prix_excursion();
                List<SiteTouristique> list = offres.get(i).getExcursions().get(k).getSiteTouristiques();
                for (int l=0; l<list.size();l++){
                    System.out.println("    Visiter " +list.get(l).getNom() +" sur l'île "+list.get(l).getIle() + " qui est un site " + list.get(l).getType());
                }
            }
            System.out.println("Le prix total de cet offre est de " + prix_offre+"€");
            System.out.println("\n");
        }
    }

}
