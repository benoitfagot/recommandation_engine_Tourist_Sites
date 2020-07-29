DROP TABLE Hotel;
DROP TABLE SiteTouristique;
DROP TABLE Transport;

CREATE TABLE Hotel(
	id_hotel INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	nom VARCHAR(50) NOT NULL, 
	longitude DOUBLE NOT NULL,
	latitude DOUBLE NOT NULL,
	prix DOUBLE NOT NULL,
	ile VARCHAR(50) NOT NULL

);

CREATE TABLE SiteTouristique(
	id_site INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	nom VARCHAR(50) NOT NULL,
	type VARCHAR(50) NOT NULL,
	longitude DOUBLE NOT NULL,
	latitude DOUBLE NOT NULL,
	ile VARCHAR(50) NOT NULL

);

CREATE Table Transport(
	id_transport INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	type_transport VARCHAR(50) NOT NULL,
	prix DOUBLE NOT NULL,
	vitesse INT NOT NULL
);


INSERT INTO SiteTouristique(nom, type, longitude, latitude, ile) VALUES('Grande Canarie', 'activité', 28.56, 10.554, 'Tenerife');
INSERT INTO SiteTouristique(nom, type, longitude, latitude, ile) VALUES('Teide', 'historique', 45.2, 25.66, 'Lanzarote');
INSERT INTO SiteTouristique(nom, type, longitude, latitude, ile) VALUES('Parc national du Teide','historique', 26.254, 15.885, 'Fuerteventura');
INSERT INTO SiteTouristique(nom, type, longitude, latitude, ile) VALUES('Parc national de Timanfaya','activité', 25.255, 13.56,'La Gomera');
INSERT INTO SiteTouristique(nom, type, longitude, latitude, ile) VALUES('Playa Blanca','historique', 44.25, 35.54, 'La Palma');
INSERT INTO SiteTouristique(nom, type, longitude, latitude, ile) VALUES('Loro Parque','activité', 15.69, 8.65, 'Tenerife');
INSERT INTO SiteTouristique(nom, type, longitude, latitude, ile) VALUES('Playa de las américas','activité', 9.685, 6.666, 'Grande canarie');
INSERT INTO SiteTouristique(nom, type, longitude, latitude, ile) VALUES('Roque Nublo','historique', 25.26, 20.222, 'La Palma');
INSERT INTO SiteTouristique(nom, type, longitude, latitude, ile) VALUES('Lago Martiánez','activité', 15.2556, 6.256,'Tenerife' );
INSERT INTO SiteTouristique(nom, type, longitude, latitude, ile) VALUES('Roque Cinchado','historique', 34.25, 19.455, 'Fuerteventura');
INSERT INTO SiteTouristique(nom, type, longitude, latitude, ile) VALUES('Auditorium de Santa Cruz','activité', 14.268, 14.66, 'Grande canarie');
INSERT INTO SiteTouristique(nom, type, longitude, latitude, ile) VALUES('Jungle park','historique', 41.66, 35.455, 'Fuerteventura');
INSERT INTO SiteTouristique(nom, type, longitude, latitude, ile) VALUES('Téléphérique du Teide','historique', 45.225, 25.256,'La Gomera');
INSERT INTO SiteTouristique(nom, type, longitude, latitude, ile) VALUES('Playa de Maspalomas','activité', 12.636, 7.566, 'Lanzarote');
INSERT INTO SiteTouristique(nom, type, longitude, latitude, ile) VALUES('Casa de Colón','historique', 7.586, 4.669, 'La Palma');

INSERT INTO Hotel(nom, longitude, latitude, prix, ile) VALUES('Hotel THe Volcan Lanzarote', 15.26, 8.56, 50, 'Lanzarote');
INSERT INTO Hotel(nom, longitude, latitude, prix, ile) VALUES('Hotel Grand Teguise Playa', 25.25, 15.65, 80, 'Tenerife');
INSERT INTO Hotel(nom, longitude, latitude, prix, ile) VALUES('Hesperia Lanzarote', 8.69, 4.26, 150, 'Lanzarote');
INSERT INTO Hotel(nom, longitude, latitude, prix, ile) VALUES('Hotel Hacienda De Abajo', 35.26, 25.23, 55, 'Tenerife');
INSERT INTO Hotel(nom, longitude, latitude, prix, ile) VALUES('Hotel Jardin Tecina', 45.69, 34.74, 150, 'La Gomera');
INSERT INTO Hotel(nom, longitude, latitude, prix, ile) VALUES('Princesa Yaiza Suite Hotel Resort', 10.9, 8.35, 69, 'Fuerteventura');
INSERT INTO Hotel(nom, longitude, latitude, prix, ile)VALUES('VIK Suite Hotel Risco del Gato', 20.9, 18.69, 70, '');
INSERT INTO Hotel(nom, longitude, latitude, prix, ile) VALUES('Colon Guanahani - Adrian Hoteles', 15.26, 12.69, 95, '');
INSERT INTO Hotel(nom, longitude, latitude, prix, ile) VALUES('Sol Fuerteventura Jandia By Melia', 14.14, 9.65, 147, 'Fuerteventura');
INSERT INTO Hotel(nom, longitude, latitude, prix, ile) VALUES('NH Imperial Playa', 18.69, 15.312, 120, 'La Gomera');
INSERT INTO Hotel(nom, longitude, latitude, prix, ile) VALUES('Parque San Antonio', 14.36, 9.65, 196, 'Fuerteventura');
INSERT INTO Hotel(nom, longitude, latitude, prix, ile) VALUES('H10 Rubicon Palace', 17.698, 7.96, 200, 'La Gomera');
INSERT INTO Hotel(nom, longitude, latitude, prix, ile) VALUES('Hotel Alta Montana', 36.28, 25.36, 320, 'La Palma');
INSERT INTO Hotel(nom, longitude, latitude, prix, ile) VALUES('Atlantic Garden Beach Mate', 47.5, 31.25, 300, 'Lanzarote');
INSERT INTO Hotel(nom, longitude, latitude, prix, ile) VALUES('Hotel Rural Finca Salamanca', 14.6, 11.58, 167, 'Tenerife');
INSERT INTO Hotel(nom, longitude, latitude, prix, ile) VALUES('Hotel The Corralejo Beach', 12.69, 9.63, 350, 'La Gomera');
INSERT INTO Hotel(nom, longitude, latitude, prix, ile) VALUES('Hotel Costa Caleta', 41.23, 24.69, 398, 'La Palma');
INSERT INTO Hotel(nom, longitude, latitude, prix, ile) VALUES('LABRANDA Alyssa Suite Hotel', 19.36, 14.247, 185, 'La Palma');

INSERT INTO Transport(type_transport, prix, vitesse) VALUES('Bateau', 50, 70);
INSERT INTO Transport(type_transport, prix, vitesse) VALUES('Autobus', 30, 50);

