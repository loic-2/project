package com.example;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

public class AjoutMedicamentController extends NavigationController implements Initializable
{

    private ObservableList<String> fList= FXCollections.observableArrayList();

    private int value;

    @FXML
    private Button btnAnnuler;

    @FXML
    private Button btnEnregistrer;

    @FXML
    private DatePicker dateExpiration;

    @FXML
    private ComboBox<String> fournisseur;

    @FXML
    private TextField nomMedicament;

    @FXML
    private TextField prixMedicament;

    @FXML
    private TextField quantite;

    @FXML
    private TextField referenceMedicament;

    @FXML
    private HBox hbox;

    @FXML
    private Pane modifPane;

    @FXML
    private Pane annulPane;

    @FXML
    private Pane enregPane;

    /**
     * Permet de modifier un medicament
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
    void annuler(MouseEvent event) {

        nomMedicament.setText("");
        referenceMedicament.setText("");
        quantite.setText("");
        prixMedicament.setText("");

    }

    /**
     * Permet d'enregistrer un medicament
     * @param event
     * @throws SQLException
     */
    @FXML
    void enregistrer(MouseEvent event) throws SQLException {

        PreparedStatement ps2 =App.getConnection().prepareStatement("SELECT COUNT(*) FROM Medicament WHERE Nom_Medicament=? AND Reference_Medicament=?");
        ps2.setString(1,nomMedicament.getText().toString());
        ps2.setInt(2,Integer.parseInt(referenceMedicament.getText().toString()));
        ResultSet rs1= ps2.executeQuery();
        rs1.next();
        
        if (rs1.getInt(1) == 0) {
            
            PreparedStatement ps1=App.getConnection().prepareStatement("SELECT Identifiant_Fabricant FROM Fabricant WHERE Nom_Fabricant=?");
            ps1.setString(1, fournisseur.getValue().toString());
            ResultSet rs= ps1.executeQuery();
    
            while (rs.next()) {
    
                value=rs.getInt("Identifiant_Fabricant");
                
            }
    
            PreparedStatement ps =App.getConnection().prepareStatement("INSERT INTO Medicament(Nom_Medicament,Reference_Medicament,"+
            "Prix_Medicament,Quantite_Medicament,DateExpiration_Medicament,Identifiant_Fabricant,DateAjout_Medicament) VALUES(?,?,?,?,?,?,NOW())");
    
            ps.setString(1,nomMedicament.getText().toString());
            ps.setInt(2,Integer.parseInt(referenceMedicament.getText().toString()));
            ps.setInt(4,Integer.parseInt(quantite.getText().toString()));
            ps.setFloat(3,Float.parseFloat(prixMedicament.getText().toString()));
            ps.setInt(6,value);
            ps.setString(5,dateExpiration.getValue().toString());
            ps.executeUpdate();
    
            App.ajouterHistorique("Ajout du medicament "+nomMedicament.getText().toString());
            App.successDialog("Succes", "Le medicament "+nomMedicament.getText().toString()+
            " a ete ajoute avec succes", AlertType.INFORMATION);
            annuler(event);

        } else {
            errorLogin.setText("Ce medicament est deja present");
            App.ajouterHistorique("Echec de l'ajout d'un medicament ");
            panerror.setVisible(true);
        }

    }

    @FXML
    private void validationMedicament() throws Exception
    {
        
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        panerror.setVisible(false);
        hbox.getChildren().remove(modifPane);
        
        try {
            ResultSet rs= App.getConnection().createStatement().executeQuery("SELECT Nom_Fabricant FROM Fabricant");

            while (rs.next()) {

                fList.add(rs.getString("Nom_Fabricant"));
                
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        fournisseur.getItems().addAll(fList);
        
    }
    
}
