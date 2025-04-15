package edu.ezip.ing1.pds.api;

import edu.ezip.ing1.pds.business.dto.*;


import java.io.IOException;


public interface LocalLaverieRepository {
    Boolean insert(LocalLaverie local) throws InterruptedException, IOException;
    LocalLaveries select() throws InterruptedException, IOException;
    LocalLaveries selectDispo() throws InterruptedException, IOException;
    Boolean update(LocalLaverie local) throws InterruptedException, IOException;
    Boolean delete(LocalLaverie local) throws InterruptedException, IOException;

}