package edu.ezip.ing1.pds.uiUtils;

import edu.ezip.ing1.pds.api.AdminRepository;
import edu.ezip.ing1.pds.business.dto.Admin;

public class RegisterAdminUseCase {
    private final AdminRepository repository;

    public RegisterAdminUseCase(AdminRepository repository) {
        this.repository = repository;
    }

    public boolean register(String name, String email, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) return false;
        Admin admin = new Admin(name, email, password,"");
        return repository.save(admin);
    }
}
