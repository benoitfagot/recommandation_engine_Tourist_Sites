package lucene;

public class SQLContainer {
    private int id = 0;
    private String nom = null;
    private String type = null;
    private double coordonnesX = 0;
    private double coordonnesY = 0;
    private String ile= null;


    public SQLContainer(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public double getCoordonnesX() {
        return coordonnesX;
    }

    public void setCoordonnesX(double coordonnesX) {
        this.coordonnesX = coordonnesX;
    }

    public double getCoordonnesY() {
        return coordonnesY;
    }

    public void setCoordonnesY(double coordonnesY) {
        this.coordonnesY = coordonnesY;
    }

    public String getIle() {
        return ile;
    }

    public void setIle(String ile) {
        this.ile = ile;
    }

    public String toString(){
        String str="";
        if(this.getId()!=0)str+=this.getId()+" -- ";
        String nom="";
        if(this.getNom()!=null) nom=this.getNom()+" -- ";
        String type="";
        if(this.getType()!=null) type=this.getType()+" -- ";
        String coordonnesX="";
        if(this.getCoordonnesX()!=0) coordonnesX+=this.getCoordonnesX()+" -- ";
        String coordonnesY="";
        if(this.getCoordonnesY()!=0) coordonnesY+=this.getCoordonnesY()+" -- ";
        String ile="";
        if(this.getIle()!=null) ile+=this.getIle()+" -- ";
        str+=nom + type + coordonnesX + coordonnesY + ile;
        return str;
    }
}
