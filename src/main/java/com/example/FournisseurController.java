package com.example;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.example.modele.Fournisseur;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class FournisseurController extends NavigationController implements Initializable{

    private ObservableList<Fournisseur> fournisseurs= FXCollections.observableArrayList();

    @FXML
    private Label username;

    @FXML
    private TextField rechercher;

    @FXML
    private TableView<Fournisseur> TableFournisseur;

    @FXML
    private TableColumn<Fournisseur, String> colAdresse;

    @FXML
    private TableColumn<Fournisseur, String> colLaboratoire;

    @FXML
    private TableColumn<Fournisseur, String> colTelephone;

    private String newValue;


    /**
     * afficher la boite de dialogue d'insertion
     * @throws Exception
     */
    @FXML
    private void addFournisseur() throws Exception
    {
        App.popUpLaunch("ajout_fournisseur");
        afficherFournisseur();
    }

    /**
     * Permet de rechercher un personnel
     */
    void rechercher() {

        FilteredList<Fournisseur> filteredList= new FilteredList<>(fournisseurs);
        rechercher.textProperty().addListener((observable,oldValue,newValue)-> {
            filteredList.setPredicate(fournisseur-> {
                
                FournisseurController.this.newValue = newValue;
                if (newValue==null || FournisseurController.this.newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter=newValue.toLowerCase();

                if (fournisseur.getNom().toLowerCase().indexOf(lowerCaseFilter) !=-1) {

                    return true;
                    
                } else if (fournisseur.getAdresse().toLowerCase().indexOf(lowerCaseFilter) !=-1) {
                    return true;
                    
                }{
                    return false;
                    
                }
                
            });
        });

        SortedList<Fournisseur> sortedList= new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(TableFournisseur.comparatorProperty());
        TableFournisseur.setItems(sortedList);

    }

    /**
     * Permet d'afficher les fournisseurs dans la table
     */
    private void afficherFournisseur()
    {

        fournisseurs.clear();
        try (ResultSet rs = App.getConnection().createStatement().executeQuery("SELECT * FROM Fabricant")) {

            while (rs.next()) {

                fournisseurs.add(new Fournisseur(rs.getString("Nom_Fabricant"), rs.getString("Telephone_Fabricant"),
                 rs.getString("Adresse_Fabricant")));
                
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        TableFournisseur.setItems(fournisseurs);

    }

    /**
     * Permet de supprimer un fournisseur
     * @param event
     * @throws SQLException
     */
    @FXML
    void supprimer(MouseEvent event) throws SQLException {

        Fournisseur fournisseur= TableFournisseur.getSelectionModel().getSelectedItem();
        if (fournisseur != null) {
            
            String nom=fournisseur.getNom();
            String adresse=fournisseur.getAdresse();

            PreparedStatement ps= App.getConnection().prepareStatement("DELETE FROM Fabricant WHERE "+
            "Nom_Fabricant=? AND Adresse_Fabricant=?");
            ps.setString(1, nom);
            ps.setString(2, adresse);
            ps.executeUpdate();
            afficherFournisseur();

            App.ajouterHistorique("Suppression du fournisseur "+nom);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        username.setText(LoginController.loginUser);
        
        colLaboratoire.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("Adresse"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        afficherFournisseur();
        rechercher();
        
    }
    
}
