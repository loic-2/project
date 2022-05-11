package com.example.modele;

public class Achat {

    private String medicament;
    private int quantite;
    private float prixUnitaire;
    private float prixTotal;

    public Achat(String medicament, int quantite, float prixUnitaire, float prixTotal) {
        this.medicament = medicament;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.prixTotal = prixTotal;
    }
    

    /**
     * @return String return the medicament
     */
    public String getMedicament() {
        return medicament;
    }

    /**
     * @param medicament the medicament to set
     */
    public void setMedicament(String medicament) {
        this.medicament = medicament;
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
     * @return float return the prixUnitaire
     */
    public float getPrixUnitaire() {
        return prixUnitaire;
    }

    /**
     * @param prixUnitaire the prixUnitaire to set
     */
    public void setPrixUnitaire(float prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    /**
     * @return float return the prixTotal
     */
    public float getPrixTotal() {
        return prixTotal;
    }

    /**
     * @param prixTotal the prixTotal to set
     */
    public void setPrixTotal(float prixTotal) {
        this.prixTotal = prixTotal;
    }

}
