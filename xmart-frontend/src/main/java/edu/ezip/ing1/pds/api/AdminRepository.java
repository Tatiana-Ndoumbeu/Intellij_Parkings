package edu.ezip.ing1.pds.api;

import edu.ezip.ing1.pds.business.dto.Admin;

public interface AdminRepository {
    boolean save(Admin admin);
    Admin findByEmail(String email);
    public boolean login(String email, String password);
}
