package br.com.seuapp.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;

public class MainController {

    @FXML
    private MenuItem menuCursos;
    @FXML
    private MenuItem menuDisciplinas;
    @FXML
    private MenuItem menuProfessores;
    @FXML
    private MenuItem menuTurmas;
    @FXML
    private StackPane bodyPane;

    @FXML
    public void initialize() {
        menuCursos.setOnAction(e -> loadView("/fxml/CursoForm.fxml"));
        menuDisciplinas.setOnAction(e -> loadView("/fxml/DisciplinaForm.fxml"));
        menuProfessores.setOnAction(e -> loadView("/fxml/ProfessorForm.fxml"));
        menuTurmas.setOnAction(e -> loadView("/fxml/TurmaForm.fxml"));

        // carrega a tela inicial
        loadView("/fxml/CursoForm.fxml");
    }

    private void loadView(String fxmlPath) {
        try {
            Parent node = FXMLLoader.load(getClass().getResource(fxmlPath));
            bodyPane.getChildren().setAll(node);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
