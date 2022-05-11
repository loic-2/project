package com.example.modele;

public class Facture {

    private String dateEtablissement;
    private String personnel;
    private float mtTotal;
    private int numFacture;

    public Facture(String dateEtablissement, String personnel, float mtTotal, int numFacture) {
        this.dateEtablissement = dateEtablissement;
        this.personnel = personnel;
        this.mtTotal = mtTotal;
        this.numFacture = numFacture;
    }


    /**
     * @return String return the dateEtablissement
     */
    public String getDateEtablissement() {
        return dateEtablissement;
    }

    /**
     * @param dateEtablissement the dateEtablissement to set
     */
    public void setDateEtablissement(String dateEtablissement) {
        this.dateEtablissement = dateEtablissement;
    }

    /**
     * @return String return the personnel
     */
    public String getPersonnel() {
        return personnel;
    }

    /**
     * @param personnel the personnel to set
     */
    public void setPersonnel(String personnel) {
        this.personnel = personnel;
    }

    /**
     * @return float return the mtTotal
     */
    public float getMtTotal() {
        return mtTotal;
    }

    /**
     * @param mtTotal the mtTotal to set
     */
    public void setMtTotal(float mtTotal) {
        this.mtTotal = mtTotal;
    }

    /**
     * @return int return the numFacture
     */
    public int getNumFacture() {
        return numFacture;
    }

    /**
     * @param numFacture the numFacture to set
     */
    public void setNumFacture(int numFacture) {
        this.numFacture = numFacture;
    }

}
