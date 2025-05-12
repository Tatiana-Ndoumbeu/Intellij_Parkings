package edu.ezip.ing1.pds.api;

import edu.ezip.ing1.pds.business.dto.*;


import java.io.IOException;


public interface ArchivesRepository {
    Boolean insert(ArchivesPaiement archive) throws InterruptedException, IOException;
    ArchivesPaiements select() throws InterruptedException, IOException;
    //Boolean delete(ArchivesPaiement archive) throws InterruptedException, IOException;

}