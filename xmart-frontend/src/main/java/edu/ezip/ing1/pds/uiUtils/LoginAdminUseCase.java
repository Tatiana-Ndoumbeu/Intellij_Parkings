package edu.ezip.ing1.pds.uiUtils;

import edu.ezip.ing1.pds.api.AdminRepository;

public class LoginAdminUseCase {
    private final AdminRepository repository;

    public LoginAdminUseCase(AdminRepository repository) {
        this.repository = repository;
    }

    public boolean login(String email, String password) {
        return repository.login(email, password);
    }
}
