package edu.ezip.ing1.pds.api;

import edu.ezip.ing1.pds.business.dto.Mecanicien;
import edu.ezip.ing1.pds.business.dto.Mecaniciens;


import java.io.IOException;

public interface MecanicienRepository {


    Boolean insert(Mecanicien mecanicien) throws InterruptedException, IOException;
    Mecaniciens select() throws InterruptedException, IOException;
}