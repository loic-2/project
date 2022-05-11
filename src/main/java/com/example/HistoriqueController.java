package com.example;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.example.modele.Historique;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class HistoriqueController extends NavigationController implements Initializable
 {

    private ObservableList<Historique> historiques= FXCollections.observableArrayList();

    @FXML
    private Label username;

    @FXML
    private TableView<Historique> TableHistorique;

    @FXML
    private TableColumn<Historique, String> colDateAction;

    @FXML
    private TableColumn<Historique, String> colDetails;

    @FXML
    private TableColumn<Historique, String> colPersonnel;

    @FXML
    private TextField rechercher;

    /*PrinterJob printerJob = PrinterJob.createPrinterJob();
    if(printerJob.showPrintDialog(primaryStage.getOwner()) && printerJob.printPage(yourNode))
       printerJob.endJob();*/

       /**
        * Permet de supprimer un historique
        * @param event
        * @throws SQLException
        */
    @FXML
    void supprimer(MouseEvent event) throws SQLException {

        Historique historique= TableHistorique.getSelectionModel().getSelectedItem();
        if (historique != null) {
            
            String nom=historique.getPersonnel();
            String date=historique.getDateAction();

            PreparedStatement ps= App.getConnection().prepareStatement("SELECT Identifiant_Personnel FROM Personnel WHERE Login_Personnel=?");
            ps.setString(1, nom);
            ResultSet name = ps.executeQuery();

            name.next();
            int value = name.getInt("Identifiant_Personnel");

            PreparedStatement ps1= App.getConnection().prepareStatement("DELETE FROM Historique WHERE "+
            "Identifiant_Personnel=? AND DateRealisation_Historique=?");
            ps1.setInt(1, value);
            ps1.setString(2, date);
            ps1.executeUpdate();
            afficherHistorique();

            App.ajouterHistorique("A supprimer l'historique du "+date);
            afficherHistorique();
        }

    }

    /**
     * Permet d'afficher les differents historiques
     */
    private void afficherHistorique()
    {

        historiques.clear();
        try (ResultSet rs = App.getConnection().createStatement().executeQuery("SELECT * FROM Historique ORDER BY DateRealisation_Historique DESC")) {

            while (rs.next()) {

                int idPersonnel=rs.getInt("Identifiant_Personnel");
                PreparedStatement ps= App.getConnection().prepareStatement("SELECT Login_Personnel "+
                " FROM Personnel WHERE Identifiant_Personnel=?");
                ps.setInt(1, idPersonnel);
                ResultSet rSet=ps.executeQuery();
                rSet.next();

                historiques.add(new Historique(rSet.getString("Login_Personnel"), rs.getString("DateRealisation_Historique"),
                 rs.getString("Action_Historique")));
                
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        TableHistorique.setItems(historiques);

    }

    /**
     * Permet de rechercher un historique precis
     */
    void rechercher() {

        FilteredList<Historique> filteredList= new FilteredList<>(historiques);
        rechercher.textProperty().addListener((observable,oldValue,newValue)-> {
            filteredList.setPredicate(historique-> {
                
                if (newValue==null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter=newValue.toLowerCase();

                if (historique.getPersonnel().toLowerCase().indexOf(lowerCaseFilter) !=-1) {

                    return true;
                    
                } else if (historique.getDetails().toLowerCase().indexOf(lowerCaseFilter) !=-1) {
                    return true;
                    
                }{
                    return false;
                    
                }
                
            });
        });

        SortedList<Historique> sortedList= new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(TableHistorique.comparatorProperty());
        TableHistorique.setItems(sortedList);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        username.setText(LoginController.loginUser);

        colDetails.setCellValueFactory(new PropertyValueFactory<>("details"));
        colDateAction.setCellValueFactory(new PropertyValueFactory<>("dateAction"));
        colPersonnel.setCellValueFactory(new PropertyValueFactory<>("personnel"));

        afficherHistorique();
        rechercher();
    }
    
}
