package lucene;

import java.io.*;
import java.nio.file.*;

//import javafx.util.Pair;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import persistence.JdbcConnection;

import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

public class Lucene {

    private ArrayList<Couple> results = new ArrayList<Couple>();
    private String requete;

    public Lucene(String requete){
        this.requete = requete;
    }

    public void addResults(Couple couple) {
        this.results.add(couple);
    }

    public ArrayList<Couple> getResults() {
        return results;
    }

    public String getRequete() {
        return requete;
    }

    public void setRequete(String requete) {
        this.requete = requete;
    }

    public void lucene_main() throws Exception{
        String path = "/home/doublefuckall/IdeaProjects/projet-agp-m1/tmp/"; //path des fichiers TXT
        int amount = createExtendedData("SiteTouristique","nom", path);
        //int amount = 16;
        System.out.println("nombre de docs à indexer "+(amount-1));
        //System.out.println(new File(".").getAbsoluteFile()); //obtenir le path absolu

        int MAX_RESULTS = 100; //nombre max de réponses retournées

        // 1. Specifier l'analyseur pour le texte.
        //    Le même analyseur est utilisé pour l'indexation et la recherche
        Analyzer analyseur = new StandardAnalyzer();

        // 2. Creation de l'index
//	    Directory index = new RAMDirectory();  //création index en mémoire
        Path indexpath = FileSystems.getDefault().getPath("/home/doublefuckall/IdeaProjects/projet-agp-m1/tmp/index"); //localisation index
        Directory index = FSDirectory.open(indexpath);  //création index sur disque

        IndexWriterConfig config = new IndexWriterConfig(analyseur);
        IndexWriter w = new IndexWriter(index, config);

        Document[] documents = new Document[amount];
        for(int p=1;p<amount;p++) documents[p] = new Document();
        String requete = "SELECT id_site FROM SiteTouristique";
        Connection dbConnection = JdbcConnection.getConnection();
        PreparedStatement preparedStatement = dbConnection.prepareStatement(requete);
        ResultSet rs = preparedStatement.executeQuery();

        int id;
        int i = 1;
        while (rs.next()) {

            id = rs.getInt("id_site");
            // 3. Indexation des documents
            //    Ici on indexe seulement un fichier
            File f = new File(path + id + ".txt");
            documents[i].add(new Field("nom", f.getName(), TextField.TYPE_STORED));
            documents[i].add(new Field("contenu", new FileReader(f), TextField.TYPE_NOT_STORED));
            w.addDocument(documents[i]);
            i++;
        }
        preparedStatement.close();


        w.close(); //on ferme le index writer après l'indexation de tous les documents

        // 4. Interroger l'index
        DirectoryReader ireader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(ireader); //l'objet qui fait la recherche dans l'index
        //String reqstr = "volcan AND île~2";
        String reqstr = this.requete;

        //Parsing de la requete en un objet Query
        //  "contenu" est le champ interrogé par defaut si aucun champ n'est precisé
        QueryParser qp = new QueryParser("contenu", analyseur);
        Query req = qp.parse(reqstr);

        TopDocs resultats = searcher.search(req, MAX_RESULTS); //recherche

        // 6. Affichage resultats
        System.out.println(resultats.totalHits + " documents correspondent");
        String name;
        float score;
        for(i=0; i<resultats.scoreDocs.length; i++) {
            int docId = resultats.scoreDocs[i].doc;
            Document d = searcher.doc(docId);
            System.out.println(d.get("nom") + ": score " + resultats.scoreDocs[i].score );
            name = d.get("nom");
            score = resultats.scoreDocs[i].score;
            String[] parts = name.split("\\."); //supprimer le '.txt' afin de ne récupérer que le nom du site
            name = parts[0];
            Couple couple = new Couple(score,name);
            this.addResults(couple);
        }

        // fermeture seulement quand il n'y a plus besoin d'acceder aux resultats
        ireader.close();
    }

    /*POUR FAIRE DES TEST
       public static void main(String[] args) throws Exception{
           String path = "/home/doublefuckall/IdeaProjects/projet-agp-m1/tmp/"; //path des fichiers TXT
           int amount = createExtendedData("SiteTouristique","nom", path);
           System.out.println("amount "+amount);
           System.out.println(new File(".").getAbsoluteFile()); //obtenir le path absolu

           int MAX_RESULTS = 100; //nombre max de réponses retournées

           // 1. Specifier l'analyseur pour le texte.
           //    Le même analyseur est utilisé pour l'indexation et la recherche
           Analyzer analyseur = new StandardAnalyzer();

           // 2. Creation de l'index
   //	    Directory index = new RAMDirectory();  //création index en mémoire
           Path indexpath = FileSystems.getDefault().getPath("/home/doublefuckall/IdeaProjects/projet-agp-m1/tmp/index"); //localisation index
           Directory index = FSDirectory.open(indexpath);  //création index sur disque

           IndexWriterConfig config = new IndexWriterConfig(analyseur);
           IndexWriter w = new IndexWriter(index, config);

           Document[] documents = new Document[amount];
           for(int p=1;p<amount;p++) documents[p] = new Document();
               String requete = "SELECT nom FROM SiteTouristique";
               Connection dbConnection = JdbcConnection.getConnection();
               PreparedStatement preparedStatement = dbConnection.prepareStatement(requete);
               ResultSet rs = preparedStatement.executeQuery();

               String id;
               int i = 1;
               while (rs.next()) {

                   id = rs.getString("nom");
                   // 3. Indexation des documents
                   //    Ici on indexe seulement un fichier
                   File f = new File(path + id + ".txt");
                   documents[i].add(new Field("nom", f.getName(), TextField.TYPE_STORED));
                   documents[i].add(new Field("contenu", new FileReader(f), TextField.TYPE_NOT_STORED));
                   w.addDocument(documents[i]);
                   i++;
               }
               preparedStatement.close();


           w.close(); //on ferme le index writer après l'indexation de tous les documents

           // 4. Interroger l'index
           DirectoryReader ireader = DirectoryReader.open(index);
           IndexSearcher searcher = new IndexSearcher(ireader); //l'objet qui fait la recherche dans l'index
           String reqstr = "volcan AND île~2";

           //Parsing de la requete en un objet Query
           //  "contenu" est le champ interrogé par defaut si aucun champ n'est precisé
           QueryParser qp = new QueryParser("contenu", analyseur);
           Query req = qp.parse(reqstr);

           TopDocs resultats = searcher.search(req, MAX_RESULTS); //recherche

           // 6. Affichage resultats
           System.out.println(resultats.totalHits + " documents correspondent");
           String name;
           for(i=0; i<resultats.scoreDocs.length; i++) {
               int docId = resultats.scoreDocs[i].doc;
               Document d = searcher.doc(docId);
               System.out.println(d.get("nom") + ": score " + resultats.scoreDocs[i].score );
           }

           // fermeture seulement quand il n'y a plus besoin d'acceder aux resultats
           ireader.close();
       }
   */
    /*
    Créer les fichiers contenant les descriptions
     */
    public static void insertTextDescription(int id, String description, String path) throws Exception{
        PrintWriter writer = new PrintWriter(path + id +".txt", "UTF-8");
        writer.println(description);
        writer.close();
    }

    /*
    Créer la BD etendue contenant les descriptions des Sites
     */
    public static int createExtendedData(String table, String key, String path) {
        int i =1;
        try {

            String req = "SELECT id_site FROM SiteTouristique";

            Connection dbConnection = JdbcConnection.getConnection();
            PreparedStatement preparedStatement = dbConnection.prepareStatement(req);
            ResultSet rs = preparedStatement.executeQuery();

            int id;

            while ( rs.next() ) {
                id = rs.getInt("id_site");
                switch(i){
                    case 1: insertTextDescription(id, "la Gomera lors d'une excursion d'une journée au départ du sud de Ténérife, et découvrez ses sites, sa culture et sa gastronomie avec un guide local ! Havre de paix au paysage spectaculaire, l'île est souvent considérée comme le joyau de l'archipel des Canaries. Découvrez des monuments tels que la maison de Colomb dans la capitale de San Sebastian, regardez la vue depuis le Mirador de Las Carboneras, dégustez un déjeuner de 2 plats dans un village pittoresque, puis assistez à une démonstration de « silbo gomero » (langue sifflée locale).", path);
                        break;
                    case 2: insertTextDescription(id,"Partez à la découverte des villages et des paysages qui entourent le Mont Teide et la vallée de Masca à Tenerife, au cours d'une excursion d'une journée ! Après avoir quitté le sud de Tenerife, dirigez-vous vers le Parc national du Teide, classé au patrimoine mondial de l'UNESCO, pour découvrir des panoramas volcaniques d'un autre monde. Vous traverserez Vilaflor, l'un des villages les plus hauts d'Espagne, ferez un arrêt pour photographier le célèbre dragonnier, puis vous bénéficierez de beaucoup de temps libre pour explorer Garachico et Masca - deux magnifiques villages des Canaries nichés dans les collines.", path);
                        break;
                    case 3: insertTextDescription(id,"Les salines de Janubio, à Lanzarote, sont les plus étendues des Îles Canaries et constituent un paysage humain d'une grande valeur architecturale et culturelle. De nos jours, elles continuent de produire l'or blanc qui a été essentiel à l'économie de l'île jusqu'à quelques décennies seulement. Elles se trouvent dans une lagune créée par une éruption volcanique qui a été à l'origine d'une barrière de lave qui fait face à la mer, et ont été classées Site d'Intérêt Scientifique pour la présence d'oiseaux migrateurs.", path);
                        break;
                    case 4: insertTextDescription(id,"La visite astronomique est l'excursion idéale à Teide pour les amateurs d’astronomie. Cette visite offre le transport depuis différents points sur l'île de Ténérife. Au cours de cette activité, le guide starlight vous accompagnera à tout instant. Votre visite astronomique de Teide inclut une visite guidée dans l'après-midi du plus grand observatoire solaire du monde, l’Observatoire de Teide et, après le coucher du soleil, une observation des étoiles grâce à un télescope longue portée sous le ciel clair et pur du parc national du Teide, le parc national le plus visité en Europe. L’excursion est limitée à 20 voyageurs maximum.", path);
                        break;
                    case 5: insertTextDescription(id,"Les Dunes de Maspalomas représentent un espace naturel unique sur les Îles Canaries pour leur beauté et la variété de l'écosystème qu'elle héberge. Protégé par le gouvernement des Canaries comme réserve naturelle spéciale, cet espace de 400 hectares comprend une plage splendide, un champ de vives dunes de sable organique, un bois de palmiers et un lac saumâtre. Ce mélange de désert et d'oasis se trouve sur la côte de l'extrême Sud de Gran Canaria et est entouré des bâtiments hôteliers de renommée de la grande station balnéaire de Maspalomas.", path);
                        break;
                    case 6: insertTextDescription(id,"La montagne de Tindaya se trouve au Nord-ouest de Fuerteventura, tout près de La Oliva. Les anciens aborigènes de Fuerteventura considéraient Tindaya comme une montagne sacrée à laquelle ils attribuaient également des propriétés magiques, comme le montrent les plus de 300 gravures d'une grande valeur archéologique qui s'y trouvent. Cette montagne solitaire de 400 mètres d'altitude, que le temps a sculptée, dénote dans ce paysage aride et plat, typique de Fuerteventura.", path);
                        break;
                    case 7: insertTextDescription(id,"Dans le sud de l'île de La Palma se trouvent ces salines, qui ont été classés Site d'Intérêt Scientifique pour être le lieu où se posent de nombreux oiseaux migrateurs. C'est l'un des lieux les plus visités de l'île. Ici, la terre, l'eau et l'air s'allient pour faire des salines de Fuencaliente un paysage humain d'une valeur immense, où les contrastes entre le blanc du sel, le noir de la terre volcanique et le bleu de la mer composent un cadre d'une grande beauté. La palette de couleurs de la nature continue de peindre les salines dans un spectacle à ne pas manquer.", path);
                        break;
                    case 8: insertTextDescription(id,"C'était durant le XVIIIème siècle que les premiers pétroglyphes des Îles Canaries se trouvaient à Belmaco, au sud-est de La Palma. Depuis lors, le lieu est devenu un point de référence pour toutes les personnes intéressées par le passé de l'archipel. Dix grottes naturelles et une station de gravures rupestres – desquelles il existe diverses interprétations – forment un ensemble archéologique, aujourd'hui classé Monument Historique Artistique. Le parc archéologique comprend de nombreux services qui aident le visiteur. Il est conseillé de laisser sa voiture au parking et de se diriger en premier lieu au centre d'interprétation intéressant pour ensuite rejoindre le chemin agréable qui conduit jusqu'aux différents sites, chacun marqué de panneaux d'informations. En terminant la visite, il est possible de trouver dans la boutique du parc une bonne démonstration de l'artisanat palmero", path);
                        break;
                    case 9: insertTextDescription(id,"De forteresse en ruines au Musée International d'Art Contemporain, grâce au génie de l'artiste César Manrique, le château de San José est un fort où l'avant-gardisme et la gastronomie se savourent tout en admirant la mer inimitable de Lanzarote. Sur ses murs de pierre du XVIIIème siècle sont accrochées des œuvres de Tápies, Miró, Alechinsky, Millares, et bien d'autres. Situé à l'est de l'île, ce centre artistique, dans lequel on entre en traversant un pont levis, est un musée, un restaurant et, sans aucun doute, un lieu unique et emblématique de la ville.", path);
                        break;
                    case 10: insertTextDescription(id,"Le ravin de Guiniguada traverse un lieu qui est sans aucun doute si merveilleux qu'il ne laisse personne indifférent : le Jardin Botanique Viera y Clavijo, le plus vieux d'Espagne et l'un des plus beaux du monde. Se balader dans ses 27 hectares permet de découvrir la variété florale dont l'archipel canarien peut être fier. Situé dans la capitale de Grande Canarie, au nord-est de l'île, il abrite des espèces végétales exotiques et incroyables, et plus de 500 plantes endémiques, dont certaines sont en voie d'extinction.", path);
                        break;
                    case 11: insertTextDescription(id,"La Casa Lercaro est située dans le nord de Tenerife, plus spécialement à La Laguna, ville classée au Patrimoine de l'Humanité. Ce palais vieux de plus de quatre siècles abrite le Musée de l'Histoire de Tenerife, bien qu'il soit difficile de savoir qui, des expositions permanentes qu'on peut admirer en son intérieur ou de l'édifice en lui-même, est le plus intéressant. Grâce à sa position au cœur historique de La Laguna, la Casa Lercaro est connue comme étant un lieu parfait pour une promenade tranquille.", path);
                        break;
                    case 12: insertTextDescription(id,"Le Jardin d'Acclimatation de La Orotava, dans le Port de la Cruz, est l'un des meilleurs endroits du nord de Tenerife pour se promener dans la nature. Les amoureux de botanique y découvriront de nombreuses espèces végétales, notamment d'origine tropicale : à certains moments, le visiteur aura même la sensation d'être dans la jungle. Également connu sous le nom de Jardin Botanique du Port de la Cruz, il a été fondé il y a plus de 200 ans.", path);
                        break;
                    case 13: insertTextDescription(id,"La Réserve naturelle intégrale de Mencáfete se trouve à l'Ouest de l'île de El Hierro, à l'intérieur du Parque Rural de La Frontera et près de Sabinosa. Les secrets et les trésors qui s'y cachent en font une enclave spéciale pour les amoureux de la nature : des espèces animales en danger d'extinction, comme les anoures jusqu'au bois humide de sabines et de monteverde typiques de Fuerteventura. Les grottes qui se faufilent entre la laurisilva hébergent des chauve-souris et des chats sauvages.", path);
                        break;
                    case 14: insertTextDescription(id,"Découvrir le charme d'un petit village entre les ravins. Les montagnes de toute beauté que l'on aperçoit de la route menant à Fataga annoncent déjà que le charme du petit village du Sud de Gran Canaria n'est pas que pittoresque. Ce recoin placide et délicieux de petites maisons blanches bien conservées, de petites rues étroites et d'une multitude de fleurs, décorent ce village perché dans les hauteurs d'une vallée éponyme et entouré de ravins, de pins et d'une palmeraie fournie et unique.", path);
                        break;
                    case 15: insertTextDescription(id,"Dans le centre de l'île de La Palma, à El Paso, se trouve le Musée de la Soie. Dans celui-ci se perpétue la tradition d'élaboration de la soie, depuis l'élevage du ver jusqu'au tissage et la broderie. Situé dans un sublime édifice historique de la ville, il comprend l'Atelier de las Hilanderas, où le visiteur peut apprendre comment ces artisans travaillent, suivant un processus disparu depuis longtemps dans le reste de l'Europe. Ce musée montre au public le processus minutieux qui est suivi pour obtenir un matériau aussi délicat que la soie palmera, qui requiert la collaboration de plusieurs spécialistes pour la transformer en produit final. Des anciens métiers à tisser qui fonctionnent encore dans la maison sortent des œuvres d'art authentiques élaborées à la main et pouvant ensuite être achetées dans la boutique du musée. Ce trésor de la tradition de La Palma est ouvert du lundi au vendredi.", path);
                        break;
                    default:break;
                }
                i++;
            }
            preparedStatement.close();
        } catch (Exception e) {
            System.err.println(" an exception! ");
            System.err.println(e.getMessage());

        }
        return i;
    }

}
