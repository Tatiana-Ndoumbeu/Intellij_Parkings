
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

CREATE TABLE Abonnement (
                            id_abonnement VARCHAR(36) NOT NULL,  -- e.g., UUID
                            typeAbonnement VARCHAR(32) NOT NULL,
                            prix DOUBLE NOT NULL,
                            dateDebut DATETIME NOT NULL,
                            dateFin DATETIME NOT NULL,
                            statutAbonnement VARCHAR(16) NOT NULL,
                            id_personne VARCHAR(36) NOT NULL,
                            PRIMARY KEY (id_abonnement),
                            FOREIGN KEY (id_personne)
);

CREATE TABLE Personne (
                          id_personne VARCHAR(36) NOT NULL,  -- e.g., UUID
                          mail VARCHAR(128) NOT NULL,
                          nom VARCHAR(64) NOT NULL,
                          prenom VARCHAR(64) NOT NULL,
                          tel VARCHAR(32),
                          code_postal VARCHAR(16),
                          PRIMARY KEY (id_personne)
                              ADD CONSTRAINT unique_mail UNIQUE (mail);
);

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

CREATE TABLE LocalTechnique(
                             numLocalT INT,
                             disponibilite BOOLEAN,
                             id_LT int(20) NOT NULL AUTO_INCREMENT,
                             PRIMARY KEY(id_LT)
);
CREATE TABLE Mecanicien (
                            id_mecanicien INT NOT NULL AUTO_INCREMENT,
                            nom VARCHAR(50) NOT NULL,
                            prenom VARCHAR(50),
                            telephone VARCHAR(20) NOT NULL,
                            disponibilite BOOLEAN,
                            specialite VARCHAR(50),
                            mail VARCHAR(50),
                            PRIMARY KEY (id_mecanicien)
                            ADD CONSTRAINT unique_tel UNIQUE (telephone);

);
CREATE TABLE Reservation_local (
                                   id INT AUTO_INCREMENT PRIMARY KEY,
                                   numero_local VARCHAR(50) NOT NULL,
                                   date_debut DATE NOT NULL,
                                   date_fin DATE NOT NULL,
                                   heure_entree TIME NOT NULL ,
                                   heure_sortie TIME NOT NULL
                                   type_local VARCHAR(100) NOT NULL,
                                   reservationLocalId VARCHAR(50) NOT NULL
);

CREATE TABLE ArchivePaiement (
                                 id INT PRIMARY KEY AUTO_INCREMENT,
                                 nom VARCHAR(100),
                                 prenom VARCHAR(100),
                                 service VARCHAR(255),
                                 date_paiement DATE,
                                 montant DECIMAL(10, 2)
);

ALTER TABLE Abonnement
    -> ADD CONSTRAINT drop_abonnement_personne
    -> FOREIGN KEY (id_personne)
    -> REFERENCES Personne(id_personne)
    -> ON DELETE CASCADE;

ALTER TABLE Reservation_local
    ADD CONSTRAINT drop_resaLocal_personne
        FOREIGN KEY (id_personne)
            REFERENCES Personne(id_personne)
            ON DELETE CASCADE;
