#!/bin/bash

# Define variables
SERVER_USER="toto"
SERVER_IP="172.31.253.72"
SERVER_PASSWORD="toto"
IMAGE_NAME="ghcr.io/tatiana-ndoumbeu/xmart-backend:latest"
CONTAINER_NAME="xmart-backend"

echo "Deploying backend on $SERVER_IP..."

# Extract the server's SSH key fingerprint (first time only)
HOST_KEY_FINGERPRINT="ssh-ed25519 255 SHA256:guTFbOTzWrPCdunEVjvOrCxwNmLUWfPpAYfXvj8mp5E"

# Use plink with the host key
plink -batch -ssh -pw "$SERVER_PASSWORD" -hostkey "$HOST_KEY_FINGERPRINT" "$SERVER_USER@$SERVER_IP" << EOF
    echo "Switching to root user..."
    echo "$SERVER_PASSWORD" | sudo -S bash -c '

    echo "Stopping and removing existing container..."
    docker stop $CONTAINER_NAME || true
    docker rm $CONTAINER_NAME || true

    echo "Pulling the latest Docker image..."
    docker pull $IMAGE_NAME

    echo "Running the updated backend container..."
    docker run -d --name $CONTAINER_NAME -p 45065:45065 $IMAGE_NAME

    echo "Deployment completed successfully!"
    '
EOF
