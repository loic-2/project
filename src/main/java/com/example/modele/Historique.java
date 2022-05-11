package com.example.modele;

public class Historique {

    private String personnel;
    private String dateAction;
    private String details;

    public Historique(String personnel, String dateAction, String details) {
        this.personnel = personnel;
        this.dateAction = dateAction;
        this.details = details;
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
     * @return String return the dateAction
     */
    public String getDateAction() {
        return dateAction;
    }

    /**
     * @param dateAction the dateAction to set
     */
    public void setDateAction(String dateAction) {
        this.dateAction = dateAction;
    }

    /**
     * @return String return the details
     */
    public String getDetails() {
        return details;
    }

    /**
     * @param details the details to set
     */
    public void setDetails(String details) {
        this.details = details;
    }

}
