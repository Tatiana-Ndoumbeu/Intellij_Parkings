package edu.ezip.ing1.pds.Interface;

import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;

import javax.swing.*;

public class AuthApp {
    private final static String LoggingLabel = "FrontEnd";
    private final static String networkConfigFile = "network.yaml";
    public static void main(String[] args) {
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile); // adapte si besoin
        SwingUtilities.invokeLater(() -> new LoginFrame(networkConfig));
    }
}
