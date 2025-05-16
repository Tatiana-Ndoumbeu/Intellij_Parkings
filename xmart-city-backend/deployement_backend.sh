#!/bin/bash

VM_USER="toto"                      # Utilisateur de la VM
VM_HOST="172.31.253.72"              # Adresse de la VM
VM_PATH="/home/toto/Sirius"         # Dossier où stocker le JAR sur la VM
REMOTE_PORT=45066                    #port utilisé
LOCAL_JAR="target/xmart-zity-backend-1.0-SNAPSHOT-jar-with-dependencies.jar"

echo "1- Compilation du backend en local"
mvn clean package -f /Users/user/Desktop/Travail/Sirius/Intellij_Parkings/pom.xml

echo "2- Transfert du JAR vers la VM"
scp "$LOCAL_JAR" "$VM_USER@$VM_HOST:$VM_PATH/xmart-zity-backend-1.0-SNAPSHOT-jar-with-dependencies.jar"

echo "3- Arrêt d'un eventuel ancien processus backend sur la VM"
ssh "$VM_USER@$VM_HOST" "if lsof -t -i tcp:$REMOTE_PORT > /dev/null; then kill \$(lsof -t -i tcp:$REMOTE_PORT); echo 'Processus tué'; else echo 'Aucun processus à arrêter'; fi"

echo "4- Démarrage du backend sur la VM"
ssh "$VM_USER@$VM_HOST" "java -jar $VM_PATH/xmart-zity-backend-1.0-SNAPSHOT-jar-with-dependencies.jar > $VM_PATH/logs.txt 2>&1 &"

echo "TOPP Backend déployé avec succès sur $VM_HOST"