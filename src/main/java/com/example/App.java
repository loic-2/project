package com.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    static Stage primaryStage;
    static Stage popUpStage;
/**
 * methode de lancement de l'application
 */
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage= stage;
        scene = new Scene(loadFXML("login"));
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }

    /**
     * 
     * @param fxml
     * @throws IOException
     */
    static void setRoot(String fxml) throws IOException {
        
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * Permet de naviguer entre les differentes scenes de l'application
     * @param fxml
     * @throws IOException
     */
    static void setOurScene(String fxml) throws IOException
    {
        scene= new Scene(loadFXML(fxml));
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    /**
     * Permet l'affichage des differentes fenetres modales
     * @param fxml
     * @throws IOException
     */
    static void popUpLaunch(String fxml) throws IOException{
        popUpStage = new Stage();
        Scene popUpScene= new Scene(loadFXML(fxml));
        popUpScene.setFill(Color.TRANSPARENT);
        popUpStage.setScene(popUpScene);
        popUpStage.initModality(Modality.APPLICATION_MODAL);    
        popUpStage.initStyle(StageStyle.TRANSPARENT);
        popUpStage.showAndWait();
    }

    /**
     * Ferme une fenetre modale
     * @throws IOException
     */
    static void closeStage() throws IOException
    {
        popUpStage.close();
    }

    /**
     * permet de notifier l'utilisateur de la reussite de son action (ajout, suppression, etc...)
     * @param message
     */
    static void succes(String message)
    {
       // Notifications notificationsBuilder= Notifications.create();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * Permet de se connecter au SGBD MYSQL
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException{

        try {
            
            Connection con= DriverManager.getConnection("jdbc:mysql://localhost/upharm","loic","1234");
            return con;
        } catch (Exception e) {
            App.successDialog("Echec", "La tentative de connexion au serveur a echoue"+
            "veillez reessayer",AlertType.ERROR);
        }
            return null;
    }

    public static ButtonType successDialog(String title,String message,AlertType alertType)
    {
        Alert alert= new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> optional= alert.showAndWait();
        return optional.get();
    }

    /**
     * Permet d'enregistrer un nouveau historique
     * @param action
     * @throws SQLException
     */
    static void ajouterHistorique(String action) throws SQLException
    {

        PreparedStatement ps= App.getConnection().prepareStatement("SELECT Identifiant_Personnel FROM Personnel WHERE Login_Personnel=?");
        ps.setString(1, LoginController.loginUser);
        ResultSet resultSet = ps.executeQuery();

        resultSet.next();
        int id = resultSet.getInt("Identifiant_Personnel");

        PreparedStatement ps2 =App.getConnection().prepareStatement("INSERT INTO Historique(Action_Historique,Identifiant_Personnel,"+
        "DateRealisation_Historique) VALUES(?,?,NOW())");

        ps2.setString(1,action);
        ps2.setInt(2,id);
        ps2.executeUpdate();
    }

    public static void main(String[] args) {
        launch();
    }

}