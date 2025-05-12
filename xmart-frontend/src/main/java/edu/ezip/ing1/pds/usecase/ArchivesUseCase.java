package edu.ezip.ing1.pds.usecase;

import edu.ezip.ing1.pds.api.ArchivesRepository;
import edu.ezip.ing1.pds.business.dto.ArchivesPaiement;
import edu.ezip.ing1.pds.business.dto.ArchivesPaiements;


import java.io.IOException;

public class ArchivesUseCase {
    private final ArchivesRepository archiveRepository;

    public ArchivesUseCase(ArchivesRepository archiveRepository) {
        this.archiveRepository = archiveRepository;
    }

    public boolean createArchive(ArchivesPaiement archive) throws InterruptedException, IOException {

        return archiveRepository.insert(archive);
    }
    public ArchivesPaiements afficherArchives() throws InterruptedException, IOException {
        return archiveRepository.select();
    }

}
