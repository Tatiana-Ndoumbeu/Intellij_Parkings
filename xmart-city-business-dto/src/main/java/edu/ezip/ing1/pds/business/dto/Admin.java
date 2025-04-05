package edu.ezip.ing1.pds.business.dto;

public class Admin {
    private String id;
    private String name;
    private String email;
    private String password;

    public Admin(String name, String email, String password, String id) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }
    public Admin() {
        this.id = "";
        this.name = "";
        this.email = "";
        this.password = "";
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getId() { return id; }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setId(String id) {}
}