package br.com.seuapp.controller;

import br.com.seuapp.dao.CursoDao;
import br.com.seuapp.model.Curso;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class CursoController {

    @FXML private TextField txtNome;
    @FXML private TextField txtCargaHoraria;
    @FXML private Button btnSalvar;
    @FXML private Button btnEditar;
    @FXML private Button btnExcluir;

    @FXML private TableView<Curso> tableCursos;
    @FXML private TableColumn<Curso, String> colNome;
    @FXML private TableColumn<Curso, Integer> colCarga;

    private final CursoDao cursoDao = new CursoDao();
    private final ObservableList<Curso> listaCursos = FXCollections.observableArrayList();

    /** quando != null, estamos em modo “editar” */
    private Curso editing = null;

    @FXML
    public void initialize() {
        // 1) configuração das colunas
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCarga.setCellValueFactory(new PropertyValueFactory<>("cargaHoraria"));

        // 2) vincula e carrega tabela
        tableCursos.setItems(listaCursos);
        refreshTable();

        // 3) listener de seleção
        tableCursos.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            boolean has = sel != null;
            btnEditar.setDisable(!has);
            btnExcluir.setDisable(!has);
        });

        // 4) ações dos botões
        btnSalvar .setOnAction(e -> onSalvar());
        btnEditar .setOnAction(e -> onEditar());
        btnExcluir.setOnAction(e -> onExcluir());

        // estado inicial
        btnEditar.setDisable(true);
        btnExcluir.setDisable(true);
    }

    private void refreshTable() {
        listaCursos.setAll(cursoDao.findAll(Curso.class));
    }

    private void clearForm() {
        editing = null;
        txtNome.clear();
        txtCargaHoraria.clear();
        btnSalvar.setText("Salvar");
        tableCursos.getSelectionModel().clearSelection();
    }

    private void onSalvar() {
        String nome = txtNome.getText().trim();
        String carga = txtCargaHoraria.getText().trim();

        if (nome.isEmpty() || carga.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Preencha todos os campos.", ButtonType.OK)
                    .showAndWait();
            return;
        }

        int ch;
        try {
            ch = Integer.parseInt(carga);
        } catch (NumberFormatException ex) {
            new Alert(Alert.AlertType.ERROR, "Carga Horária inválida.", ButtonType.OK)
                    .showAndWait();
            return;
        }

        if (editing == null) {
            // CREATE
            Curso c = new Curso();
            c.setNome(nome);
            c.setCargaHoraria(ch);
            cursoDao.create(c);
        } else {
            // UPDATE
            editing.setNome(nome);
            editing.setCargaHoraria(ch);
            cursoDao.update(editing);
        }

        refreshTable();
        clearForm();
    }

    private void onEditar() {
        Curso sel = tableCursos.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        editing = sel;
        txtNome.setText(sel.getNome());
        txtCargaHoraria.setText(String.valueOf(sel.getCargaHoraria()));
        btnSalvar.setText("Atualizar");
    }

    private void onExcluir() {
        Curso sel = tableCursos.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Excluir curso \"" + sel.getNome() + "\"?",
                ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES) {
                cursoDao.delete(sel);
                refreshTable();
                clearForm();
            }
        });
    }
}
