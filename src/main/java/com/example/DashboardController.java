package com.example;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.example.modele.Controle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.util.Duration;


public class DashboardController extends NavigationController implements Initializable{

    static int warningMedicament=0;
    static int expiredMedicament=0;
    static int goodMedicament=0;
    private int totalMedicament=0;
    private ObservableList<PieChart.Data> pList=FXCollections.observableArrayList();

    @FXML
    private Label username;

    @FXML
    private Label zoomValue;

    @FXML
    private StackedBarChart stackedBarchart;

    @FXML
    private PieChart piechart;

    @FXML
    private Label numFacture;

    @FXML
    private Label numFournisseur;

    @FXML
    private Label numMedicament;

    @FXML
    private Label numPersonnel;

    /**
     * Affiche les donnees du graphe
     * @throws SQLException
     */
    private void dasboard() throws SQLException
    {

        final XYChart.Series<String,Integer> series= new XYChart.Series<>();
        series.setName("Quantite");
        ResultSet rs = App.getConnection().createStatement().executeQuery("SELECT * FROM Medicament");
        while (rs.next()) {
            
            LocalDate date= LocalDate.now();
            DateTimeFormatter dateFormat=DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date1= LocalDate.parse(rs.getString("dateExpiration_Medicament"), dateFormat);
            //LocalDate date2= date.minus(20,ChronoUnit.DAYS);
            series.getData().add(new XYChart.Data<String,Integer>(rs.getString("Nom_Medicament"),
            rs.getInt("Quantite_Medicament")));
            Period period= Period.between(date, date1);
            //Period period1= Period.between(date, date2);
            if (date.isAfter(date1) || date1.isEqual(date) ) {
                expiredMedicament+=1;
                
            } else if (period.getDays()<=20 && period.getYears()==0 && period.getMonths()==0 && date1.isAfter(date)) {
                warningMedicament+=1;
                
            }else {
                goodMedicament+=1;
            }
            
            
        }
        pList.add(new PieChart.Data("Expire", ((float)expiredMedicament/(float)totalMedicament)*100));
        pList.add(new PieChart.Data("Attention", ((float)warningMedicament/(float)totalMedicament)*100));
        pList.add(new PieChart.Data("Bon", ((float)goodMedicament/(float)totalMedicament)*100));
        stackedBarchart.setCategoryGap(50);
        stackedBarchart.getData().add(series);
        //piechart.setData(pList);

        

        
        
    }

    /**
     * Permet d'afficher les differents donnees du dashboard
     * @throws Exception
     */
    public void afficherParametres() throws Exception
    {

        Platform.runLater(()->{
            try {
                ResultSet rs = App.getConnection().createStatement().executeQuery("SELECT count(*) FROM Medicament");
                ResultSet rs2 = App.getConnection().createStatement().executeQuery("SELECT count(*) FROM Fabricant");
                ResultSet rs3 = App.getConnection().createStatement().executeQuery("SELECT count(*) FROM Personnel");
                ResultSet rs4 = App.getConnection().createStatement().executeQuery("SELECT count(*) FROM Facture");
    
                rs.next();
                rs2.next();
                rs3.next();
                rs4.next();
    
                numFacture.setText(Integer.toString(rs4.getInt(1)));
                numFournisseur.setText(Integer.toString(rs2.getInt(1)));
                numMedicament.setText(Integer.toString(rs.getInt(1)));
                numPersonnel.setText(Integer.toString(rs3.getInt(1)));
                totalMedicament=rs2.getInt(1);
                
    
            } catch (SQLException e) {
                
                e.printStackTrace();
            }
        });

    }

    /**
     * Permet d'initialiser le dashbord avec les differents valeurs des parametres du systeme
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ScheduledService<Void> scheduledService= new Controle();
        ScheduledService<Void> scheduledService2= new ScheduledService<Void>() {

            @Override
            protected Task<Void> createTask() {
                
                return new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        afficherParametres();
                        return null;
                    }
                    
                };
            }

            
        };
        try {
            dasboard();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        scheduledService.setDelay(Duration.seconds(5));
        scheduledService.setPeriod(Duration.seconds(120));
        scheduledService2.setDelay(Duration.seconds(0));
        scheduledService2.setPeriod(Duration.seconds(10));
        scheduledService2.start();
        scheduledService.start();
        username.setText(LoginController.loginUser);
        zoomValue.setVisible(false);


        /*for (final PieChart.Data data : piechart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_MOVED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        zoomValue.setTranslateX(e.getSceneX()-zoomValue.getLayoutX());
                        zoomValue.setTranslateY(e.getSceneY()-zoomValue.getLayoutY());
                        zoomValue.setText(String.valueOf((int)data.getPieValue()) + "%");
                        zoomValue.setVisible(true);
                     }
                });
        }*/
        
    }

}
