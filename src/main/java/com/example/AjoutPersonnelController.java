package com.example;

import java.io.IOException;
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

public class AjoutPersonnelController extends NavigationController implements Initializable
{

    private ObservableList<String> fList= FXCollections.observableArrayList();

    @FXML
    private Button btnAnnuler;

    @FXML
    private Pane annulPane;

    @FXML
    private Pane enregPane;

    @FXML
    private Pane modifPane;

    @FXML
    private HBox hbox;

    @FXML
    private Button btnEnregistrer;

    @FXML
    private PasswordField confirMdpPersonnel;

    @FXML
    private TextField loginPersonnel;

    @FXML
    private PasswordField mdpPersonnel;

    @FXML
    private TextField nomPersonnel;

    @FXML
    private ComboBox<String> rolePersonnel;

    @FXML
    private TextField telPersonnel;

    /**
     * Permet de modifier un personnel
     * @param event
     */
    @FXML
    void modifier(MouseEvent event) {

    }

    /**
     * Permet d'effacer le contenu des differents champs
     * @param event
     */
    @FXML
    void annuler(MouseEvent event) {

        mdpPersonnel.setText("");
        confirMdpPersonnel.setText("");
        nomPersonnel.setText("");
        telPersonnel.setText("");
        loginPersonnel.setText("");

    }

    /**
     * Permet d'enregistrer un personnel
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    void enregistrer(MouseEvent event) throws IOException, SQLException {

        if (mdpPersonnel.getText().toString().equals(confirMdpPersonnel.getText().toString())) {

            PreparedStatement ps1 =App.getConnection().prepareStatement("SELECT COUNT(*) FROM Personnel WHERE Nom_Personnel=? AND MotDePasse_Personnel=?");
            ps1.setString(1,nomPersonnel.getText().toString());
            ps1.setString(2,mdpPersonnel.getText().toString());
            ResultSet rs= ps1.executeQuery();
            rs.next();

            if (rs.getInt(1) == 0) {

                PreparedStatement ps =App.getConnection().prepareStatement("INSERT INTO Personnel(Nom_Personnel,Login_Personnel,"+
                "Telephone_Personnel,MotDePasse_Personnel,Role_Personnel,DateAjout_Personnel) VALUES(?,?,?,?,?,NOW())");

                ps.setString(1,nomPersonnel.getText().toString());
                ps.setString(2,loginPersonnel.getText().toString());
                ps.setString(4,mdpPersonnel.getText().toString());
                ps.setString(5,rolePersonnel.getValue().toString());
                ps.setString(3,telPersonnel.getText().toString());
                ps.executeUpdate();

                App.ajouterHistorique("Ajout du personnel "+nomPersonnel.getText().toString());
                App.successDialog("Success", "Le personnel "+nomPersonnel.getText().toString()+" a ete ajoute avec"
                +"succes", AlertType.INFORMATION);
                annuler(event);
                
            } else {
                errorLogin.setText("Identifiant deja utilises dans le systeme");
                App.ajouterHistorique("Echec de l'ajout d'un personnel ");
                panerror.setVisible(true);
            }
            
        } else {

            errorLogin.setText("Mot de passe non identique");
            App.ajouterHistorique("Echec de l'ajout d'un personnel ");
            panerror.setVisible(true);
            
        }

    }

    @FXML
    private void validationPersonnel() throws Exception
    {
        
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        panerror.setVisible(false);
        
        try {
            ResultSet rs= App.getConnection().createStatement().executeQuery("SELECT Nom FROM Role");

            while (rs.next()) {

                fList.add(rs.getString("Nom"));
                
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        rolePersonnel.getItems().addAll(fList);
        
    }
    
}
