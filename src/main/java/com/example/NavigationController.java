package com.example;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class NavigationController {

    @FXML
    protected Pane panerror;

    @FXML
    protected Label errorLogin;

    @FXML
    protected Label message;

    @FXML
    protected AnchorPane succesDialog;


    /**
     * Permet de fermer l'application
     * @throws Exception
     */
    @FXML
    protected void fermerApplication() throws Exception
    {
        Platform.exit();
    }

    /**
     * Permet de fermer la boite de dialogue
     * @throws Exception
     */
    @FXML
    protected void fermerPopUp() throws Exception
    {
        App.closeStage();
    }

    /**
     * Permet d'aller au dashboard
     * @throws Exception
     */
    @FXML
    protected void switchToDashboard() throws Exception
    {
        App.setRoot("dashboard");
    }

    /**
     * Permet d'aller sur la fenetre des medicament
     * @throws Exception
     */
    @FXML
    protected void switchToMedicament() throws Exception
    {
        App.setRoot("medicament");
    }

    /**
     * Permet d'aller au niveau de la scene facture
     * @throws Exception
     */
    @FXML
    protected void switchToFacture() throws Exception
    {
        App.setRoot("facture");
    }

    /**
     * Permet d'aller au niveau de la fenetre fournisseur
     * @throws Exception
     */
    @FXML
    protected void switchToFournisseur() throws Exception
    {
        App.setRoot("fournisseur");
    }

    /**
     * Permet d'aller au niveau de la fenetre personnel
     * @throws Exception
     */
    @FXML
    protected void switchToPersonnel() throws Exception
    {
        App.setRoot("personnel");
    }

    /**
     * Permet d'aller au niveau de la fenetre historique
     * @throws Exception
     */
    @FXML
    protected void switchToHistorique() throws Exception
    {
        App.setRoot("historique");
    }

    @FXML
    protected void switchToNotification() throws Exception
    {
        
    }

    /**
     * Permet de se  deconnecter de l'application
     * @throws Exception
     */
    @FXML
    protected void deconnexion() throws Exception
    {

            if (App.successDialog("Deconnexion", "Voulez-vous vraiment vous deconnecter?",
            AlertType.CONFIRMATION) == ButtonType.OK) {
                App.setOurScene("login");
                App.ajouterHistorique("Deconnexion du systeme"); 
            }
            
    }

    /**
     * Permet de masquer le message d'erreur
     * @param event
     */
    @FXML
    void remove(MouseEvent event) {

        panerror.setVisible(false);

    }
    
}
