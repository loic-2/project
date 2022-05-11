package com.example;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.example.modele.Medicament;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class MedicamentController extends NavigationController implements Initializable{

    private ObservableList<Medicament> medicaments= FXCollections.observableArrayList();

    private String value;

    @FXML
    private TextField rechercher;

    @FXML
    private Label username;

    @FXML
    private TableColumn<Medicament, String> colAction;

    @FXML
    private TableColumn<Medicament, String> colExpiration;

    @FXML
    private TableColumn<Medicament, String> colFounisseur;

    @FXML
    private TableColumn<Medicament, Integer> colId;

    @FXML
    private TableColumn<Medicament, String> colNom;

    @FXML
    private TableColumn<Medicament, Float> colPrix;

    @FXML
    private TableColumn<Medicament, Integer> colQuantite;

    @FXML
    private TableView<Medicament> tableMedicament;

    private String newValue;

    /**
     * Permet d'afficher la boite d'ajout d'un medicament
     * @throws Exception
     */
    @FXML
    private void addmedicament() throws Exception
    {
        App.popUpLaunch("ajout_medicament");
        afficherMedicament();
    }

    /**
     * permet de rechercher un medicament 
     */
    void rechercher() {

        FilteredList<Medicament> filteredList= new FilteredList<>(medicaments);
        rechercher.textProperty().addListener((observable,oldValue,newValue)-> {
            filteredList.setPredicate(medicament-> {
                
                MedicamentController.this.newValue = newValue;
                if (newValue==null || MedicamentController.this.newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter=newValue.toLowerCase();

                if (medicament.getNomMedicament().toLowerCase().indexOf(lowerCaseFilter) !=-1) {

                    return true;
                    
                } else if (medicament.getFournisseur().toLowerCase().indexOf(lowerCaseFilter) !=-1) {
                    return true;
                    
                }{
                    return false;
                    
                }
                
            });
        });

        SortedList<Medicament> sortedList= new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableMedicament.comparatorProperty());
        tableMedicament.setItems(sortedList);

    }

    /**
     * Permet de afficher les differents medicaments de la base de la bd
     */
    private void afficherMedicament() {

        medicaments.clear();
        try (ResultSet rs = App.getConnection().createStatement().executeQuery("SELECT * FROM Medicament")) {

            while (rs.next()) {

                int idFournisseur= rs.getInt("Identifiant_Fabricant");
                PreparedStatement ps= App.getConnection().prepareStatement("SELECT Nom_Fabricant FROM Fabricant WHERE Identifiant_Fabricant=?");
                ps.setInt(1, idFournisseur);
                ResultSet name = ps.executeQuery();

                while (name.next()) {

                    value=name.getString("Nom_Fabricant");
                    
                }



                medicaments.add(new Medicament(rs.getString("Nom_Medicament"), rs.getInt("Quantite_Medicament"), rs.getString("DateExpiration_Medicament"),
                 rs.getFloat("Prix_Medicament"), rs.getInt("Reference_Medicament"), value));
                
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableMedicament.setItems(medicaments);
    }

    /**
     * Permet de supprimer un medicament de la bd
     * @param event
     * @throws SQLException
     */
    @FXML
    void supprimer(MouseEvent event) throws SQLException {

        Medicament medicament= tableMedicament.getSelectionModel().getSelectedItem();
        if (medicament !=null) {
            
            String nom=medicament.getNomMedicament();
            int reference=medicament.getReferenceMedicament();

            PreparedStatement ps= App.getConnection().prepareStatement("DELETE FROM Medicament WHERE "+
            "Reference_Medicament=? AND Nom_Medicament=?");
            ps.setInt(1, reference);
            ps.setString(2, nom);
            ps.executeUpdate();
            afficherMedicament();

            App.ajouterHistorique("Suppression du medicament "+nom);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        username.setText(LoginController.loginUser);
        
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomMedicament"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("nomMedicament"));
        colExpiration.setCellValueFactory(new PropertyValueFactory<>("dateExpiration"));
        colId.setCellValueFactory(new PropertyValueFactory<>("referenceMedicament"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colFounisseur.setCellValueFactory(new PropertyValueFactory<>("fournisseur"));
        colQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));

        afficherMedicament();
        rechercher();
        
    }

    
   
}