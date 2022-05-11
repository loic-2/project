package com.example;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import com.example.modele.Achat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class ModeleFactureController extends NavigationController implements Initializable
{

    private ObservableList<String> fList= FXCollections.observableArrayList();
    private ObservableList<Achat> achats=FXCollections.observableArrayList();
    private final PrinterJob printerJob= PrinterJob.createPrinterJob();

    private static float MontantTotal_Facture=0;

    @FXML
    private Label numFacture;

    @FXML
    private Label MtTotal;

    @FXML
    private TableColumn<Achat, String> colMedicament;

    @FXML
    private TableColumn<Achat, Float> colPrixUnitaire;

    @FXML
    private TableColumn<Achat, Float> colPrixtotal;

    @FXML
    private TableColumn<Achat, Integer> colQte;

    @FXML
    private Label dateEtablissement;

    @FXML
    private ComboBox<String> medicament;

    @FXML
    private TextField nomClient;


    @FXML
    private TextField quantite;

    @FXML
    private TableView<Achat> tableAchat;

    /**
     * Permet d'imprimer une facture
     * @param event
     * @throws SQLException
     */
    @FXML
    void imprimer(MouseEvent event) throws SQLException {

        if (printerJob != null) {
            printerJob.showPrintDialog(App.popUpStage);
        }
        App.ajouterHistorique("Impression de la facture numero "+numFacture.getText().toString());

    }

    /**
     * Permet d'ajouter un medicament a la facture
     * @param event
     * @throws SQLException
     */
    @FXML
    void ajouterMedicament(MouseEvent event) throws SQLException {

        PreparedStatement ps= App.getConnection().prepareStatement("SELECT Prix_Medicament, Quantite_Medicament, Reference_Medicament FROM Medicament WHERE"+
        " Nom_Medicament=? ORDER BY DateEXpiration_Medicament ASC LIMIT 1");
        ps.setString(1, medicament.getValue().toString());
        ResultSet rs = ps.executeQuery();
        rs.next();
        int qte=Integer.parseInt(quantite.getText().toString());
        if (qte <= (rs.getInt("Quantite_Medicament")-10)) {

            float prixUnite=rs.getFloat("Prix_Medicament");
            MontantTotal_Facture+=prixUnite*qte;
            achats.addAll(new Achat(medicament.getValue().toString(), qte, prixUnite, prixUnite*qte)); 
            tableAchat.setItems(achats);
            MtTotal.setText(Float.toString(MontantTotal_Facture));
            quantite.setText("");
            
        }else{
            errorLogin.setText("Impossible: Stock insuffisant");
            panerror.setVisible(true);
        }

    }

    /**
     * Permet d'enregistrer une facture 
     * @param event
     * @throws SQLException
     */
    @FXML
    void enregistrer(MouseEvent event) throws SQLException {

        PreparedStatement ps1 =App.getConnection().prepareStatement("SELECT Identifiant_Personnel FROM Personnel WHERE Login_Personnel=?");
        ps1.setString(1, LoginController.loginUser);
        ResultSet rs = ps1.executeQuery();
        rs.next();

        for (Achat achat : achats) {
            //Recuperation de la reference du produit
            String medicament=achat.getMedicament();
            PreparedStatement ps2= App.getConnection().prepareStatement("SELECT Prix_Medicament, Quantite_Medicament, Reference_Medicament FROM Medicament WHERE"+
            " Nom_Medicament=? ORDER BY DateEXpiration_Medicament ASC LIMIT 1");
            ps2.setString(1, medicament);
            ResultSet rs2 = ps2.executeQuery();
            rs2.next();

            //Modification du stock
            int qte=achat.getQuantite();
            PreparedStatement ps = App.getConnection().prepareStatement("UPDATE Medicament SET "+
            "Quantite_Medicament= Quantite_Medicament-? WHERE Nom_Medicament=? AND Reference_Medicament=?");
            ps.setInt(1, qte);
            ps.setString(2, medicament);
            ps.setInt(3, rs2.getInt("Reference_Medicament"));
            ps.executeUpdate();
        }

        //Ajout de la facture a la table des factures
        PreparedStatement ps =App.getConnection().prepareStatement("INSERT INTO Facture(Numero_Facture,MontantTotal_Facture,"+
        "DateEtablissement_Facture, Identifiant_Personnel) VALUES(?,?,NOW(),?)");
        ps.setString(1,numFacture.getText().toString());
        ps.setFloat(2,MontantTotal_Facture);
        ps.setInt(3,rs.getInt("Identifiant_Personnel"));
        ps.executeUpdate();

        achats.clear();
        tableAchat.setItems(achats);
        MontantTotal_Facture=0;
        MtTotal.setText(Float.toString(MontantTotal_Facture));

        App.ajouterHistorique("Ajout de la facture numero "+numFacture.getText().toString());

    }

    /**
     * Permet de retirer un medicament de la facture
     * @param event
     * @throws SQLException
     */
    @FXML
    void retirerMedicament(MouseEvent event) throws SQLException {

        Achat achat= tableAchat.getSelectionModel().getSelectedItem();
        if (achat !=null) {
            
            float mt=achat.getPrixTotal();
            achats.remove(achat);
            tableAchat.setItems(achats);
            MontantTotal_Facture-=mt;
            MtTotal.setText(Float.toString(MontantTotal_Facture));
        }

    }

    @FXML
    private void validationFacture() throws Exception
    {
        
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        MtTotal.setText("0");
        panerror.setVisible(false);
        Date date= new Date();
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");
        String NowDate= simpleDateFormat.format(date);
        dateEtablissement.setText(NowDate);

        colMedicament.setCellValueFactory(new PropertyValueFactory<>("medicament"));
        colQte.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colPrixUnitaire.setCellValueFactory(new PropertyValueFactory<>("prixUnitaire"));
        colPrixtotal.setCellValueFactory(new PropertyValueFactory<>("prixTotal"));
        tableAchat.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
       
        try {
            ResultSet rs = App.getConnection().createStatement().executeQuery("SELECT count(*) FROM Facture");
            rs.next();
            numFacture.setText(Integer.toString( rs.getInt(1)+1));

            ResultSet rs1= App.getConnection().createStatement().executeQuery("SELECT Nom_Medicament FROM Medicament");

            while (rs1.next()) {

                fList.add(rs1.getString("Nom_Medicament"));
                
            }

        } catch (SQLException e) {
            
            e.printStackTrace();
        }
        
        medicament.getItems().addAll(fList);
        
    }
    
}
