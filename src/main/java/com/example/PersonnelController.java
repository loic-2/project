package com.example;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.example.modele.Personnel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class PersonnelController extends NavigationController implements Initializable{

    private ObservableList<Personnel> personnels = FXCollections.observableArrayList();

    @FXML
    private TextField rechercher;

    @FXML
    private Label username;

    @FXML
    private TableView<Personnel> TablePersonnel;

    @FXML
    private TableColumn<Personnel, String> colLogin;

    @FXML
    private TableColumn<Personnel, String> colMatricule;

    @FXML
    private TableColumn<Personnel, String> colMdp;

    @FXML
    private TableColumn<Personnel, String> colNom;

    @FXML
    private TableColumn<Personnel, String> colRole;

    @FXML
    private TableColumn<Personnel, String> colTelephone;

    private String newValue;

    /**
     * Permet d'ajouter
     * @throws Exception
     */
    @FXML
    private void addpersonnel() throws Exception
    {
        App.popUpLaunch("ajout_personnel");
        afficherPersonnel();
    }

    /**
     * Permet de supprimer un personnel du systeme
     * @param event
     * @throws SQLException
     */
    @FXML
    void supprimer(MouseEvent event) throws SQLException {

        Personnel personnel= TablePersonnel.getSelectionModel().getSelectedItem();
        if (personnel !=null) {
            
            String mdp=personnel.getMdp();
            String login=personnel.getLogin();

            PreparedStatement ps= App.getConnection().prepareStatement("DELETE FROM Personnel WHERE "+
            "Login_Personnel=? AND MotDePasse_Personnel=?");
            ps.setString(1, login);
            ps.setString(2, mdp);
            ps.executeUpdate();
            afficherPersonnel();

            App.ajouterHistorique("Suppression du personnel "+personnel.getNom());
        }

    }

    
    /**
     * Permet de recherher un personnel dans le systeme
     */
    void rechercher() {

        FilteredList<Personnel> filteredList= new FilteredList<>(personnels);
        rechercher.textProperty().addListener((observable,oldValue,newValue)-> {
            filteredList.setPredicate(personnel-> {

                PersonnelController.this.newValue = newValue;
                if (newValue==null || PersonnelController.this.newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter=newValue.toLowerCase();

                if (personnel.getNom().toLowerCase().indexOf(lowerCaseFilter) !=-1) {

                    return true;
                    
                } else if (personnel.getLogin().toLowerCase().indexOf(lowerCaseFilter) !=-1) {
                    return true;
                    
                } else if (personnel.getRole().toLowerCase().indexOf(lowerCaseFilter) !=-1) {
                    return true;
                }{
                    return false;
                    
                }
                
            });
        });

        SortedList<Personnel> sortedList= new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(TablePersonnel.comparatorProperty());
        TablePersonnel.setItems(sortedList);

    }

    /**
     * Permet d'afficher les differents personnels de l'application
     */
    public void afficherPersonnel(){

        personnels.clear();
        try (ResultSet rs = App.getConnection().createStatement().executeQuery("SELECT * FROM Personnel")) {

            while (rs.next()) {

                personnels.add(new Personnel(rs.getString("Nom_Personnel"), rs.getString("Login_Personnel"),
                 rs.getString("Role_Personnel"), rs.getString("Telephone_Personnel"), rs.getString("DateAjout_Personnel"), rs.getString("MotDePasse_Personnel")));
                
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        
        TablePersonnel.setItems(personnels);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        username.setText(LoginController.loginUser);
        
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
        colMdp.setCellValueFactory(new PropertyValueFactory<>("mdp"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colMatricule.setCellValueFactory(new PropertyValueFactory<>("dateAjout"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        afficherPersonnel();
        rechercher();

        
    }
    
}
