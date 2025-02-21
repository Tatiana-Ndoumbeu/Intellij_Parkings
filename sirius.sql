CREATE TABLE LocalLaverie(
   NumLocalL INT,
   disponibilite BOOLEAN,
   id int(20) NOT NULL AUTO_INCREMENT,
   PRIMARY KEY(id)
);


CREATE TABLE Mécanicien(
   id_mecanicien VARCHAR(50),
   nom VARCHAR(50) NOT NULL,
   prenom VARCHAR(50),
   telephone INT NOT NULL,
   Diponibilité BOOLEAN,
   Spécialité VARCHAR(50),
   mail VARCHAR(50),
   PRIMARY KEY(id_mecanicien)
);

CREATE TABLE Place_de_parking(
   id_place VARCHAR(50),
   typePlace VARCHAR(50),
   statutPlace VARCHAR(50) NOT NULL,
   emplacement VARCHAR(50),
   PRIMARY KEY(id_place)
);

CREATE TABLE LocalTechnique(
   IdLocalT INT,
   Diponibilté DATE,
   PRIMARY KEY(IdLocalT)
);

CREATE TABLE Véhicule(
   num_plaque VARCHAR(50),
   type VARCHAR(50) NOT NULL,
   marque VARCHAR(50),
   id_place VARCHAR(50) NOT NULL,
   PRIMARY KEY(num_plaque),
   UNIQUE(id_place),
   FOREIGN KEY(id_place) REFERENCES Place_de_parking(id_place)
);

CREATE TABLE Usager(
   id_usager INT AUTO_INCREMENT,
   nom VARCHAR(100) NOT NULL,
   prenom VARCHAR(100),
   mail VARCHAR(255),
   codePostal INT,
   telephone INT,
   pays VARCHAR(50),
   num_plaque VARCHAR(50) NOT NULL,
   PRIMARY KEY(id_usager),
   UNIQUE(mail),
   FOREIGN KEY(num_plaque) REFERENCES Véhicule(num_plaque)
);

CREATE TABLE Service(
   id_service VARCHAR(50),
   date_service DATETIME,
   type_service VARCHAR(50) NOT NULL,
   Libellé VARCHAR(50),
   id_usager INT NOT NULL,
   PRIMARY KEY(id_service),
   FOREIGN KEY(id_usager) REFERENCES Usager(id_usager)
);

CREATE TABLE Abonnement(
   id_abonnement VARCHAR(50),
   typeAbonnement VARCHAR(50) NOT NULL,
   prix DECIMAL(15,2) NOT NULL,
   date_debut DATETIME NOT NULL,
   date_fin DATETIME NOT NULL,
   statutAbonnement VARCHAR(50) NOT NULL,
   id_usager INT NOT NULL,
   PRIMARY KEY(id_abonnement),
   UNIQUE(id_usager),
   FOREIGN KEY(id_usager) REFERENCES Usager(id_usager)
);

CREATE TABLE Paiement(
   id_paiement VARCHAR(50),
   montant DECIMAL NOT NULL,
   date_paiement DATETIME NOT NULL,
   moyen_paiement VARCHAR(50) NOT NULL,
   id_abonnement VARCHAR(50) NOT NULL,
   PRIMARY KEY(id_paiement),
   FOREIGN KEY(id_abonnement) REFERENCES Abonnement(id_abonnement)
);

CREATE TABLE Reservation(
   id_reservation VARCHAR(50),
   date_reservation DATE NOT NULL,
   heure TIME,
   date_entree DATETIME,
   date_sortie DATETIME,
   id_usager INT NOT NULL,
   id_paiement VARCHAR(50) NOT NULL,
   PRIMARY KEY(id_reservation),
   FOREIGN KEY(id_usager) REFERENCES Usager(id_usager),
   FOREIGN KEY(id_paiement) REFERENCES Paiement(id_paiement)
);

CREATE TABLE Concerner(
   id_place VARCHAR(50),
   id_reservation VARCHAR(50),
   PRIMARY KEY(id_place, id_reservation),
   FOREIGN KEY(id_place) REFERENCES Place_de_parking(id_place),
   FOREIGN KEY(id_reservation) REFERENCES Reservation(id_reservation)
);

CREATE TABLE Faire_appel(
   id INT,
   id_service VARCHAR(50),
   IdLocalT INT,
   PRIMARY KEY(id, id_service, IdLocalT),
   FOREIGN KEY(id) REFERENCES LocalLaverie(id),
   FOREIGN KEY(id_service) REFERENCES Service(id_service),
   FOREIGN KEY(IdLocalT) REFERENCES LocalTechnique(IdLocalT)
);

CREATE TABLE Rendre(
   id_mecanicien VARCHAR(50),
   id_service VARCHAR(50),
   PRIMARY KEY(id_mecanicien, id_service),
   FOREIGN KEY(id_mecanicien) REFERENCES Mécanicien(id_mecanicien),
   FOREIGN KEY(id_service) REFERENCES Service(id_service)
);
INSERT INTO LocalLaverie (NumLocalL, disponibilite) VALUES (433, true);