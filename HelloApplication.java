package com.example.calendar_progetto;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
//importazione delle librerie

public class HelloApplication extends Application {

    private YearMonth meseAnnoCorrente; // per tenere traccia del mese e anno attuali
    private GridPane grigliaCalendario; // griglia per visualizzare i giorni
    private ComboBox<String> selezioneMese; // per scegliere il mese
    private ComboBox<Integer> selezioneAnno; // per scegliere l'anno

    @Override
    public void start(Stage palco) {
        // Impostazione del mese e anno correnti
        meseAnnoCorrente = YearMonth.now();

        // Griglia
        grigliaCalendario = new GridPane();
        grigliaCalendario.setAlignment(Pos.CENTER);
        grigliaCalendario.setHgap(10);
        grigliaCalendario.setVgap(10);

        // menù a tendina per il mese
        selezioneMese = new ComboBox<>();
        for (int i = 1; i <= 12; i++) {
            selezioneMese.getItems().add(meseAnnoCorrente.getMonth().getDisplayName(TextStyle.FULL, Locale.ITALIAN));
            meseAnnoCorrente = meseAnnoCorrente.plusMonths(1);
        }
        meseAnnoCorrente = meseAnnoCorrente.minusMonths(12); // Reset del mese all'attuale
        selezioneMese.setValue(meseAnnoCorrente.getMonth().getDisplayName(TextStyle.FULL, Locale.ITALIAN));

        // menù a tendina per l'anno
        selezioneAnno = new ComboBox<>();
        int annoCorrente = LocalDate.now().getYear();
        for (int i = annoCorrente - 10; i <= annoCorrente + 10; i++) {
            selezioneAnno.getItems().add(i);
        }
        selezioneAnno.setValue(annoCorrente);

        // Bottone per aggiornare il calendario
        Button aggiorna = new Button("Aggiorna");
        aggiorna.setOnAction(e -> aggiornaCalendario());

        // selezione mese e anno
        HBox selezione = new HBox(10, selezioneMese, selezioneAnno, aggiorna);
        selezione.setAlignment(Pos.CENTER);

        // Layout principale
        HBox layoutPrincipale = new HBox(20, grigliaCalendario);
        layoutPrincipale.setAlignment(Pos.CENTER);
        Scene scena = new Scene(new HBox(20, selezione, grigliaCalendario), 600, 400);
        palco.setScene(scena);
        palco.setTitle("Calendario Semplice");
        palco.show();

        // mostra calendario iniziale
        aggiornaCalendario();
    }

    private void aggiornaCalendario() {
        // mostra mese e anno selezionati
        int mese = selezioneMese.getSelectionModel().getSelectedIndex() + 1; // Mesi in Java partono da 1
        int anno = selezioneAnno.getValue();
        meseAnnoCorrente = YearMonth.of(anno, mese);

        // pulisce la griglia
        grigliaCalendario.getChildren().clear();

        // aggiunge giorni della settimana
        String[] giorniSettimana = {"Lun", "Mar", "Mer", "Gio", "Ven", "Sab", "Dom"};
        for (int i = 0; i < giorniSettimana.length; i++) {
            grigliaCalendario.add(new Text(giorniSettimana[i]), i, 0);
        }

        // primo giorno del mese
        LocalDate primoGiorno = meseAnnoCorrente.atDay(1);
        int giornoSettimanaInizio = primoGiorno.getDayOfWeek().getValue(); // Lunedì=1, Domenica=7

        // aggiunge i giotni del mese
        int giorniNelMese = meseAnnoCorrente.lengthOfMonth();
        int riga = 1;
        int colonna = giornoSettimanaInizio - 1; // Per allineare con l'indice della griglia

        for (int giorno = 1; giorno <= giorniNelMese; giorno++) {
            grigliaCalendario.add(new Text(String.valueOf(giorno)), colonna, riga);
            colonna++;
            if (colonna == 7) { // Vai a capo ogni settimana
                colonna = 0;
                riga++;
            }
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
