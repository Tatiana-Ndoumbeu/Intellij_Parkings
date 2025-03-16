
-- Table for Vehicules
CREATE TABLE Vehicule (
                          id INT(20) NOT NULL AUTO_INCREMENT,
                          num_plaque VARCHAR(32) NOT NULL,
                          type VARCHAR(32) NOT NULL,
                          marque VARCHAR(64) NOT NULL,
                          id_place VARCHAR(50) NOT NULL,
                          PRIMARY KEY (id),
                          UNIQUE(id_place),
                          FOREIGN KEY(id_place) REFERENCES PlaceDeParking(id_place)

);

-- SQL statements for Vehicules
-- SELECT_ALL_VEHICULES: Retrieve all vehicules.
-- INSERT_VEHICULE: Insert a new vehicule.
-- Example:
-- SELECT t.num_plaque, t.type, t.marque FROM Vehicule t;
-- INSERT INTO Vehicule (num_plaque, type, marque) VALUES (?, ?, ?);



-- Table for Abonnements
CREATE TABLE Abonnement (
                            id_abonnement VARCHAR(36) NOT NULL,  -- e.g., UUID
                            typeAbonnement VARCHAR(32) NOT NULL,
                            prix DOUBLE NOT NULL,
                            dateDebut DATETIME NOT NULL,
                            dateFin DATETIME NOT NULL,
                            statutAbonnement VARCHAR(16) NOT NULL,
                            PRIMARY KEY (id_abonnement)
);

-- SQL statements for Abonnements
-- SELECT_ALL_ABONNEMENTS: Retrieve all abonnements.
-- INSERT_ABONNEMENT: Insert a new abonnement.
-- Example:
-- SELECT t.id_abonnement, t.typeAbonnement, t.prix FROM Abonnement t;
-- INSERT INTO Abonnement (id_abonnement, typeAbonnement, prix, dateDebut, dateFin, statutAbonnement) VALUES (?, ?, ?, ?, ?, ?);



-- Table for Personnes
CREATE TABLE Personne (
                          id_personne VARCHAR(36) NOT NULL,  -- e.g., UUID
                          mail VARCHAR(128) NOT NULL,
                          nom VARCHAR(64) NOT NULL,
                          prenom VARCHAR(64) NOT NULL,
                          tel VARCHAR(32),
                          code_postal VARCHAR(16),
                          PRIMARY KEY (id_personne)
);

-- SQL statements for Personnes
-- SELECT_ALL_PERSONNES: Retrieve all personnes.
-- INSERT_PERSONNE: Insert a new personne.
-- Example:
-- SELECT t.id_personne, t.mail, t.nom, t.prenom, t.tel, t.code_postal FROM Personne t;
-- INSERT INTO Personne (mail, nom, prenom, tel, code_postal) VALUES (?, ?, ?, ?, ?);



-- Table for PlaceDeParking
CREATE TABLE PlaceDeParking (
                                id_place VARCHAR(36) NOT NULL,  -- e.g., UUID
                                emplacement VARCHAR(128) NOT NULL,
                                type_place VARCHAR(32) NOT NULL,
                                statut_place VARCHAR(16) NOT NULL,
                                PRIMARY KEY (id_place)
);

CREATE TABLE LocalLaverie(
                             NumLocalL INT,
                             disponibilite BOOLEAN,
                             id int(20) NOT NULL AUTO_INCREMENT,
                             PRIMARY KEY(id)
);


-- SQL statements for PlaceDeParking
-- SELECT_ALL_PLACE_DE_PARKING: Retrieve all parking places.
-- INSERT_PLACE_DE_PARKING: Insert a new parking place.
-- Example:
-- SELECT t.id_place, t.type_place, t.statut_place, t.emplacement FROM PlaceDeParking t;
-- INSERT INTO PlaceDeParking (type_place, statut_place, emplacement) VALUES (?, ?, ?);