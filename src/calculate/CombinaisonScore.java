package calculate;

import lucene.MIXTEContainer;

import java.util.LinkedList;
import java.util.List;

public class CombinaisonScore implements Comparable<CombinaisonScore> {

    private List<MIXTEContainer> combinaison = new LinkedList<MIXTEContainer>();
    private double score;


    //Constructor

    public CombinaisonScore(List<MIXTEContainer> combinaison, double score) {
        this.combinaison = combinaison;
        this.score = score;
    }


    //Getters & Setters


    public List<MIXTEContainer> getCombinaison() {
        return combinaison;
    }

    public void setCombinaison(List<MIXTEContainer> combinaison) {
        this.combinaison = combinaison;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public int compareTo(CombinaisonScore o) {

            if(this.score < o.getScore()) return 1;
            if(this.score > o.getScore()) return -1;
            else                   return 0;
    }



}
