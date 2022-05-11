package com.example.modele;

public class Medicament {

    private String nomMedicament;
    private int quantite;
    private String dateExpiration;
    private float prix;
    private int referenceMedicament;
    private String fournisseur;

    public Medicament(String nomMedicament, int quantite, String dateExpiration, float prix, int referenceMedicament, String fournisseur) {
        this.nomMedicament = nomMedicament;
        this.quantite = quantite;
        this.dateExpiration = dateExpiration;
        this.prix = prix;
        this.referenceMedicament = referenceMedicament;
        this.fournisseur = fournisseur;
    }
    

    /**
     * @return String return the nomMedicament
     */
    public String getNomMedicament() {
        return nomMedicament;
    }

    /**
     * @param nomMedicament the nomMedicament to set
     */
    public void setNomMedicament(String nomMedicament) {
        this.nomMedicament = nomMedicament;
    }

    /**
     * @return int return the quantite
     */
    public int getQuantite() {
        return quantite;
    }

    /**
     * @param quantite the quantite to set
     */
    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    /**
     * @return String return the dateExpiration
     */
    public String getDateExpiration() {
        return dateExpiration;
    }

    /**
     * @param dateExpiration the dateExpiration to set
     */
    public void setDateExpiration(String dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    /**
     * @return float return the prix
     */
    public float getPrix() {
        return prix;
    }

    /**
     * @param prix the prix to set
     */
    public void setPrix(float prix) {
        this.prix = prix;
    }

    /**
     * @return int return the referenceMedicament
     */
    public int getReferenceMedicament() {
        return referenceMedicament;
    }

    /**
     * @param referenceMedicament the referenceMedicament to set
     */
    public void setReferenceMedicament(int referenceMedicament) {
        this.referenceMedicament = referenceMedicament;
    }


    /**
     * @return Fournisseur return the fournisseur
     */
    public String getFournisseur() {
        return fournisseur;
    }

    /**
     * @param fournisseur the fournisseur to set
     */
    public void setFournisseur(String fournisseur) {
        this.fournisseur = fournisseur;
    }

}
