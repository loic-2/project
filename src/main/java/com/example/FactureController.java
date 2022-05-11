package com.example;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.example.modele.Facture;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class FactureController extends NavigationController implements Initializable{

    private ObservableList<Facture> factures= FXCollections.observableArrayList();

    private String value;

    @FXML
    private TextField rechercher;

    @FXML
    private TableColumn<Facture, String> ColPersonnel;

    @FXML
    private TableColumn<Facture, String> colDate;

    @FXML
    private TableColumn<Facture, Float> colMtTotal;

    @FXML
    private TableColumn<Facture, Integer> colNum;

    @FXML
    private TableView<Facture> tableFacture;

    @FXML
    private Label username;

    private String newValue;

    
    /** 
     * @throws Exception
     */
    @FXML
    private void addFacture() throws Exception
    {
        App.popUpLaunch("modele_facture");
        afficherFacture();
    }

   /**
    * Permet d'effectuer une recherche
    */ 
    void rechercher() {

        FilteredList<Facture> filteredList= new FilteredList<>(factures);
        rechercher.textProperty().addListener((observable,oldValue,newValue)-> {
            filteredList.setPredicate(facture-> {
                
                FactureController.this.newValue = newValue;
                if (newValue==null || FactureController.this.newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter=newValue.toLowerCase();

                if (facture.getPersonnel().toLowerCase().indexOf(lowerCaseFilter) !=-1) {

                    return true;
                    
                }{
                    return false;
                    
                }
                
            });
        });

        SortedList<Facture> sortedList= new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableFacture.comparatorProperty());
        tableFacture.setItems(sortedList);

    }

    /**
     * Affiche les factures enregistres dans le systeme
     */
    public void afficherFacture(){

        factures.clear();

        try (ResultSet rs = App.getConnection().createStatement().executeQuery("SELECT * FROM Facture")) {

            while (rs.next()) {

                int idPersonnel= rs.getInt("Identifiant_Personnel");
                PreparedStatement ps= App.getConnection().prepareStatement("SELECT Nom_Personnel FROM Personnel WHERE Identifiant_Personnel=?");
                ps.setInt(1, idPersonnel);
                ResultSet name = ps.executeQuery();

                while (name.next()) {

                    value=name.getString("Nom_Personnel");
                    
                }



                factures.add(new Facture(rs.getString("DateEtablissement_Facture"),value,
                 rs.getFloat("MontantTotal_Facture"), rs.getInt("Numero_Facture")));
                
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableFacture.setItems(factures);
    }

    
    /** 
     * Permet de supprimer une facture
     * @param event
     * @throws SQLException
     */
    @FXML
    void supprimer(MouseEvent event) throws SQLException {

        Facture facture= tableFacture.getSelectionModel().getSelectedItem();
        if (facture !=null) {
            
            int numero=facture.getNumFacture();

            PreparedStatement ps= App.getConnection().prepareStatement("DELETE FROM Facture WHERE "+
            "Numero_Facture=?");
            ps.setInt(1, numero);
            ps.executeUpdate();
            afficherFacture();

            App.ajouterHistorique("Suppression de la facture numero "+numero);
        }

    }

    
    /** 
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        username.setText(LoginController.loginUser);
        tableFacture.setEditable(true);
        colNum.setCellValueFactory(new PropertyValueFactory<>("numFacture"));
        colMtTotal.setCellValueFactory(new PropertyValueFactory<>("mtTotal"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateEtablissement"));
        ColPersonnel.setCellValueFactory(new PropertyValueFactory<>("personnel"));

        afficherFacture();
        rechercher();
        
    }
    
}
