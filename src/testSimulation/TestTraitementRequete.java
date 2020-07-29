package testSimulation;
import lucene.Lucene;
import lucene.TraitementRequete;

public class TestTraitementRequete {
    public static void main(String[] args) throws Exception{
        TraitementRequete tr = new TraitementRequete();
        //tr.execute_request("SELECT type, longitude, latitude FROM SiteTouristique WHERE type=\"touristique\"");
        tr.execute_request("SELECT nom, type, longitude, latitude, ile FROM SiteTouristique with île~2");
    }
}


///POUR QUE LE TEST FONCTIONNE VOUS DEVEZ CREER UN REPERTOIRE /TMP ET RENSEIGNER SON PATH DANS LA CLASSE LUCENE,
///ENSUITE A L INTERIEUR DU REPERTOIRE /TMP CREER UN REPERTOIRE INDEX