#!/bin/bash

LOCAL_FRONT="/Users/user/Desktop/Travail/Sirius/Intellij_Parkings/"         # chemin vers le projet (chacun remplace)

LOCAL_JAR="/Users/user/Desktop/Travail/Sirius/Intellij_Parkings/xmart-frontend/target/xmart-frontend-1.0-SNAPSHOT-jar-with-dependencies.jar" #chemin du .jar créé (chacun remplace aussi)

echo "Compilation du PROJET"
#mvn package "$LOCAL_FRONT"
cd "$LOCAL_FRONT"
mvn package

echo "Démarrage du frontend"
java -jar "$LOCAL_JAR"