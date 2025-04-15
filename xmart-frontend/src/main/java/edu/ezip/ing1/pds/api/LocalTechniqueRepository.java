package edu.ezip.ing1.pds.api;

import edu.ezip.ing1.pds.business.dto.LocalLaverie;
import edu.ezip.ing1.pds.business.dto.LocalLaveries;
import edu.ezip.ing1.pds.business.dto.LocalTechnique;
import edu.ezip.ing1.pds.business.dto.LocalTechniques;



import java.io.IOException;

public interface LocalTechniqueRepository {

    Boolean insert(LocalTechnique local) throws InterruptedException, IOException;
    LocalTechniques select() throws InterruptedException, IOException;
    LocalTechniques selectDispo() throws InterruptedException, IOException;
    Boolean update(LocalTechnique local) throws InterruptedException, IOException;
    Boolean delete(LocalTechnique local) throws InterruptedException, IOException;

}