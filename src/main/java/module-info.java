module es.juliogtrenard.examen_dein_julio_gonzalez {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens es.juliogtrenard.examen_dein_julio_gonzalez to javafx.fxml;
    exports es.juliogtrenard.examen_dein_julio_gonzalez;
    exports es.juliogtrenard.examen_dein_julio_gonzalez.controladores;
    opens es.juliogtrenard.examen_dein_julio_gonzalez.controladores to javafx.fxml;
    exports es.juliogtrenard.examen_dein_julio_gonzalez.modelos;
    exports es.juliogtrenard.examen_dein_julio_gonzalez.db;
    exports es.juliogtrenard.examen_dein_julio_gonzalez.dao;
}