package com.example;


import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

public class AjoutFournisseurController extends NavigationController implements Initializable
{

    @FXML
    private TextField adresseFournisseur;

    @FXML
    private Button btnAnnuler;

    @FXML
    private Button btnEnregistrer;

    @FXML
    private TextField nomFournisseur;

    @FXML
    private HBox hbox;

    @FXML
    private Pane annulPane;

    @FXML
    private Pane enregPane;

    @FXML
    private Pane modifPane;

    @FXML
    private TextField numeroFournisseur;

    /**
     * Permet de modifier un founisseur
     * @param event
     */
    @FXML
    void modifier(MouseEvent event) {

    }


    /**
     * Permet de renitialiser les differents champs
     * @param event
     */
    @FXML
    void annuler(ActionEvent event) {

        nomFournisseur.setText("");
        numeroFournisseur.setText("");
        adresseFournisseur.setText("");

    }

    /**
     * Permet d'enregistrer un fournisseur
     * @param event
     * @throws Exception
     */
    @FXML
    void enregistrer(ActionEvent event) throws Exception {

        if (validationFournisseur(nomFournisseur,50) && validationFournisseur(numeroFournisseur,9)
        && validationFournisseur(adresseFournisseur, 8)) {

            PreparedStatement ps1 =App.getConnection().prepareStatement("SELECT COUNT(*) FROM Fabricant WHERE Nom_Fabricant=? AND Adresse_Fabricant=?");
            ps1.setString(1,nomFournisseur.getText().toString());
            ps1.setString(2,adresseFournisseur.getText().toString());
            ResultSet rs= ps1.executeQuery();
            rs.next();

            if (rs.getInt(1) == 0) {
                
                PreparedStatement ps =App.getConnection().prepareStatement("INSERT INTO Fabricant(Nom_Fabricant,Adresse_Fabricant,"+
                "Telephone_Fabricant) VALUES(?,?,?)");

                ps.setString(1,nomFournisseur.getText().toString());
                ps.setString(2,adresseFournisseur.getText().toString());
                ps.setString(3,numeroFournisseur.getText().toString());
                ps.executeUpdate();
                
                App.ajouterHistorique("Ajout du fournisseur "+nomFournisseur.getText().toString());
                App.successDialog("Succes", "Le medicament "+nomFournisseur.getText().toString()+
                " a ete ajoute avec succes", AlertType.INFORMATION);
                annuler(event);

            } else {
                errorLogin.setText("Impossible, ce fournisseur existe deja");
                App.ajouterHistorique("Erreur lors de l'ajaout d'un fournisseur");
                panerror.setVisible(true);
            }
            
        } else {
            
            errorLogin.setText("Erreur lors de l'enregistrement, verifier les champs");
            App.ajouterHistorique("Erreur lors de l'ajaout d'un fournisseur");
            panerror.setVisible(true);

        }
        

    }

    /**
     * Permet la validation du contenu d'un champ
     * @param textField
     * @param val
     * @return
     * @throws Exception
     */
    @FXML
    private Boolean validationFournisseur(TextField textField,int val) throws Exception
    {
        String control= textField.getText().toString();
        
        if (control.isEmpty() && control.contains("%") && control.length()>val) {
            return false;
        } 
        return true;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    

        panerror.setVisible(false);
        
    }
    
}
