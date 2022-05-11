package com.example;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;


import org.controlsfx.control.NotificationPane;
import org.controlsfx.control.Notifications;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class LoginController extends NavigationController implements Initializable
{
    static String loginUser;

   

    @FXML
    private Button BtnConnexion;

    @FXML
    private TextField login;

    @FXML
    private PasswordField motDePasse;



    /**
     * Permet de se connecter a l'application
     * @throws Exception
     */
    @FXML
    private void seConnecter() throws Exception
    {
        PreparedStatement ps= App.getConnection().prepareStatement("SELECT COUNT(*) FROM Personnel WHERE "+
        "Login_Personnel=? AND MotDePasse_Personnel=?");
        ps.setString(1, login.getText().toString());
        ps.setString(2, motDePasse.getText().toString());
        ResultSet rs= ps.executeQuery();
        rs.next();
        if (rs.getInt(1)==1) {

            loginUser=login.getText().toString();
            App.ajouterHistorique("Connexion au systeme");
            //Platform.runLater(new Controle());
            Notifications notifications= Notifications.create();
                    notifications.title("Bienvenue "+LoginController.loginUser)
                                 .darkStyle().hideCloseButton().text("UPHARM")
                                 .hideAfter(Duration.seconds(30))
                                 .position(Pos.TOP_CENTER)
                                 .showConfirm();
            App.setOurScene("dashboard");

            
        } else {

            errorLogin.setText("Echec de la connexion");
            panerror.setVisible(true);
            
        }
    }




    
    /** 
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        panerror.setVisible(false);
        
    }
    

}
