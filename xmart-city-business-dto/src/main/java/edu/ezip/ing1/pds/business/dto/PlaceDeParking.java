package edu.ezip.ing1.pds.business.dto;


public class PlaceDeParking {
    private String idPlace;
    private String typePlace;
    private String statutPlace;
    private String emplacement;

    public PlaceDeParking(String idPlace, String typePlace, String statutPlace, String emplacement) {
        this.idPlace = idPlace;
        this.typePlace = typePlace;
        this.statutPlace = statutPlace;
        this.emplacement = emplacement;
    }
    public PlaceDeParking() {}

    public String getIdPlace() {
        return idPlace;
    }

    public void setIdPlace(String idPlace) {
        this.idPlace = idPlace;
    }

    public String getTypePlace() {
        return typePlace;
    }

    public void setTypePlace(String typePlace) {
        this.typePlace = typePlace;
    }

    public String getStatutPlace() {
        return statutPlace;
    }

    public void setStatutPlace(String statutPlace) {
        this.statutPlace = statutPlace;
    }

    public String getEmplacement() {
        return emplacement;
    }

    public void setEmplacement(String emplacement) {
        this.emplacement = emplacement;
    }

    @Override
    public String toString() {
        return "PlaceDeParking{" +
                "idPlace='" + idPlace + '\'' +
                ", typePlace='" + typePlace + '\'' +
                ", statutPlace='" + statutPlace + '\'' +
                ", emplacement='" + emplacement + '\'' +
                '}';
    }
}
