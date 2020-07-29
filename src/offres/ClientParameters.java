package offres;

import java.util.LinkedList;
import java.util.List;

public class ClientParameters {

    private double prixMin;
    private double prixMax;
    private double confortMin;
    private double confortLax;
    private int densite;
    private String keywords;
    private int nbJours;

    public ClientParameters(double min, double max, double min2, double max2, int densite, String keywords_list, int nbJours){
        this.prixMin = min;
        this.prixMax = max;
        this.confortMin = min2;
        this.confortLax = max2;
        this.densite = densite;
        this.keywords = keywords_list;
        this.nbJours=nbJours;
    }

    public double getPrixMin() {
        return this.prixMin;
    }

    public void setPrixMin(double prixMin) {
        this.prixMin = prixMin;
    }

    public double getPrixMax() {
        return this.prixMax;
    }

    public void setPrixMax(double prixMax) {
        this.prixMax = prixMax;
    }

    public double getConfortMin() {
        return confortMin;
    }

    public void setConfortMin(double confortMin) {
        this.confortMin = confortMin;
    }

    public double getConfortLax() {
        return confortLax;
    }

    public void setConfortLax(double confortLax) {
        this.confortLax = confortLax;
    }

    public int getDensite() {
        return densite;
    }

    public void setDensité(int densite) {
        this.densite = densite;
    }

    public String getKeywords() {
        return keywords;
    }

    public int getNbJours() {
        return nbJours;
    }

    public void setNbJours(int nbJours) {
        this.nbJours = nbJours;
    }


    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }


}
