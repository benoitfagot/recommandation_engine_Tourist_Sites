-- phpMyAdmin SQL Dump
-- version 4.6.6deb5
-- https://www.phpmyadmin.net/
--
-- Client :  localhost:3306
-- Généré le :  Lun 14 Janvier 2019 à 15:29
-- Version du serveur :  5.7.24-0ubuntu0.18.04.1
-- Version de PHP :  7.1.26-1+ubuntu18.04.1+deb.sury.org+1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `ileDesCanaries`
--

-- --------------------------------------------------------

--
-- Structure de la table `Hotel`
--

CREATE TABLE `Hotel` (
  `id_hotel` int(11) NOT NULL,
  `nom` varchar(50) DEFAULT NULL,
  `longitude` float DEFAULT NULL,
  `latitude` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `SiteTouristique`
--

CREATE TABLE `SiteTouristique` (
  `id_site` int(11) NOT NULL,
  `nom` varchar(50) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `longitude` float DEFAULT NULL,
  `latitude` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `Transport`
--

CREATE TABLE `Transport` (
  `id_transport` int(11) NOT NULL,
  `type_transport` varchar(50) DEFAULT NULL,
  `prix` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Index pour les tables exportées
--

--
-- Index pour la table `Hotel`
--
ALTER TABLE `Hotel`
  ADD PRIMARY KEY (`id_hotel`);

--
-- Index pour la table `SiteTouristique`
--
ALTER TABLE `SiteTouristique`
  ADD PRIMARY KEY (`id_site`);

--
-- Index pour la table `Transport`
--
ALTER TABLE `Transport`
  ADD PRIMARY KEY (`id_transport`);

--
-- AUTO_INCREMENT pour les tables exportées
--

--
-- AUTO_INCREMENT pour la table `Hotel`
--
ALTER TABLE `Hotel`
  MODIFY `id_hotel` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `SiteTouristique`
--
ALTER TABLE `SiteTouristique`
  MODIFY `id_site` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `Transport`
--
ALTER TABLE `Transport`
  MODIFY `id_transport` int(11) NOT NULL AUTO_INCREMENT;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
