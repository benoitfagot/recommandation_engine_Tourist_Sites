package offres;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//import com.sun.javafx.css.CalculatedValue;

import lucene.MIXTEContainer;
import model.Hotel;
import model.SiteTouristique;
import model.Transport;

public class Confort {
	
	//Transport transport;
	Excursion excursion;
	String moyenTransport;

	private ResultSet rst;
	private Statement st;


	//Constructor


	public Confort() {
	}

	//Méthode pour calculer la distanc entre deux sites
	public double calcul_distance(double lat1, double lon1, double lat2, double lon2) {
		lat1 = Math.toRadians(lat1);
		lon1 = Math.toRadians(lon1);
		lat2 = Math.toRadians(lat2);
		lon2 = Math.toRadians(lon2);
		double earthRadius = 6371.01; //Kilometers
		return earthRadius * Math.acos(Math.sin(lat1)*Math.sin(lat2) + Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon1 - lon2));
		//return Math.acos(Math.sin(lat1)*Math.sin(lat2) + Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon1 - lon2));
	}

	public String getTypeTransportSiteToSite(MIXTEContainer site1, MIXTEContainer site2 ) {
		if ((site1.getIle()) == (site2.getIle())) {
			moyenTransport = "Autobus"; //-1 pour le score 
		}
		else {
			moyenTransport ="Bateau"; //+1 pour le score 
		}
		return moyenTransport;
	}
	
	public String getTypeTransportSiteToHotel(Hotel hotel, MIXTEContainer site ) {
		if (hotel.getIle() == site.getIle()) {
			moyenTransport = "Autobus"; //-1 pour le score 
		}
		else {
			moyenTransport ="Bateau"; //+1 pour le score 
		}
		return moyenTransport;
	}

	// elle me permettre de récupérer la vitesse d'un autobus
	public int getVitesseAutobus() {

		int vitesseAutobus = 0;
		try {
			String query;
			query = "select vitesse from transport where type = autobus ";
			rst = st.executeQuery(query);
			while (rst.next()) {
				vitesseAutobus = rst.getInt("vitesse");
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return vitesseAutobus;
	}

	// elle me permettre de récupérer la vitesse d'un bateau
	public int getVitesseBateau() {
		int vitesseBateau = 0;
		try {
			String query;
			query = "select vitesse from transport where type = bateau ";
			rst = st.executeQuery(query);
			while (rst.next()) {
				vitesseBateau = rst.getInt("vitesse");
			}

		} catch (Exception ex) {
			System.out.println(ex);
		}
		return vitesseBateau;

	}

	// Calculer la duree d'une distance
	public double calculDuree(double distance, String moyenTransport) {
		double duree=1;// pour éviter la division sur 0
		if (moyenTransport == "Autobus") {
			duree = (distance / getVitesseAutobus()) * 3600;
		} else {
			duree = (distance / getVitesseBateau()) * 3600;
		}
		return duree;
	}

	public double evaluateTypeTransport(String moyenTransport) {
		double scoreTypeTransport = 0;
		if (moyenTransport == "Autobus") {
			scoreTypeTransport-- ;
		} else {
			scoreTypeTransport++ ;
		}
		return scoreTypeTransport;
	}
	
	
//	public boolean isConfortable(Hotel h, SiteTouristique s ) {
//		Hotel hotel = excursion.getHotel();
//		List<SiteTouristique> ex = excursion.getSiteTouristiques();
//		 for(int i=0; i < (ex.size())-1; i++){
//			 
//		 }
//		return true;
//	}
	
	
	//pour calculer le confort d'une excursion
	public double calculConfortExcursion(List<MIXTEContainer> sites, Hotel hotel ) {
		double distance=0;
		double dureeTotale=1; // pour éviter la division sur 0
		double scoreTypeTransport=0;

		String transport;
		int last=sites.size()-1;


		//Calculer la distance entre l'hotel et le premier site
		distance =calcul_distance(hotel.getLatitude(), hotel.getLongitude(), sites.get(0).getCoordonnesX(), sites.get(0).getCoordonnesY());
		transport= getTypeTransportSiteToHotel(hotel, sites.get(0));
		dureeTotale+=calculDuree(distance, transport);
		scoreTypeTransport+=evaluateTypeTransport(transport);

		// Calculer la distance entre les sites
		for (int i=0; i<sites.size()-1; i++) {
			distance =calcul_distance(sites.get(i).getCoordonnesX(), sites.get(i).getCoordonnesY(), sites.get(i+1).getCoordonnesX(), sites.get(i+1).getCoordonnesY());
			transport= getTypeTransportSiteToSite(sites.get(i), sites.get(i+1));
			dureeTotale+=calculDuree(distance, transport);
			scoreTypeTransport+=evaluateTypeTransport(transport);
		}

		//Calculer la distance entre le dernier site et l'hotel
		distance=calcul_distance(hotel.getLatitude(), hotel.getLongitude(), sites.get(last).getCoordonnesX(), sites.get(last).getCoordonnesY());
		transport = getTypeTransportSiteToHotel(hotel, sites.get(last));
		dureeTotale+= calculDuree(distance, transport);
		scoreTypeTransport+=evaluateTypeTransport(transport);
		
		//return scoreTypeTransport+(1/dureeTotale) ;
		return (5*scoreTypeTransport)/((sites.size()+1)+(1/1+dureeTotale)*10);
	}



}
