package com.example.modele;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import com.example.App;

import org.controlsfx.control.Notifications;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.util.Duration;

public class Controle extends ScheduledService{


    /**
     * Permet le controle des dates d'expiration et l'envoi de notifications
     */
    private void controle() {

        ResultSet rs;
        try {
            rs = App.getConnection().createStatement().executeQuery("SELECT * FROM Medicament");
            while (rs.next()) {
            
            LocalDate date= LocalDate.now();
            DateTimeFormatter dateFormat=DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date1= LocalDate.parse(rs.getString("dateExpiration_Medicament"), dateFormat);
            //LocalDate date2= date.minus(20,ChronoUnit.DAYS);
            
            Period period= Period.between(date, date1);
            //Period period1= Period.between(date, date2);
            if (date.isAfter(date1) || date1.isEqual(date) ) {
                System.out.println(date1);
                Platform.runLater(()->{
                    Notifications notifications= Notifications.create();
                                                 notifications.darkStyle()
                                                              .hideAfter(Duration.seconds(15))
                                                              .text("Medicament")
                                                              .show();
                });
            } else if (period.getDays()<=20 && period.getYears()==0 && period.getMonths()==0 && date1.isAfter(date)) {
                System.out.println(date1);
                Platform.runLater(()->{
                    Notifications notifications= Notifications.create();
                                                 notifications.darkStyle()
                                                              .hideAfter(Duration.seconds(15))
                                                              .text("Medicament")
                                                              .show();
                });
                
            }else {
                
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
               
                e.printStackTrace();
            }
        }
    } catch (SQLException e) {
        
        e.printStackTrace();
    }

    }

    @Override
    protected Task createTask() {
       
        return new Task<Void>() {

            @Override
            protected Void call() throws Exception {
               System.out.println("En cours");
                controle();
                return null;
            }
            
        };
    }
    
}
