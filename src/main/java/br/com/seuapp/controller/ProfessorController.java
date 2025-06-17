package br.com.seuapp.controller;

import br.com.seuapp.dao.ProfessorDao;
import br.com.seuapp.model.Professor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.cell.PropertyValueFactory;


public class ProfessorController {

    @FXML private TextField txtNome;
    @FXML private TextField txtEmail;
    @FXML private TextField txtFormacao;

    @FXML private Button btnSalvar;
    @FXML private Button btnEditar;
    @FXML private Button btnExcluir;

    @FXML private TableView<Professor> tableProfessores;
    @FXML private TableColumn<Professor, String> colNome;
    @FXML private TableColumn<Professor, String> colEmail;
    @FXML private TableColumn<Professor, String> colFormacao;

    private final ProfessorDao profDao = new ProfessorDao();
    private final ObservableList<Professor> profs = FXCollections.observableArrayList();

    /** se não é null, estamos editando este professor */
    private Professor editing = null;

    @FXML
    public void initialize() {
        // configura colunas
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colFormacao.setCellValueFactory(new PropertyValueFactory<>("formacao"));

        // vincula lista à tabela
        tableProfessores.setItems(profs);
        refreshTable();

        // listener de seleção
        tableProfessores.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, sel) -> {
                    boolean has = sel != null;
                    btnEditar.setDisable(!has);
                    btnExcluir.setDisable(!has);
                });

        // ações
        btnSalvar.setOnAction(e -> onSalvar());
        btnEditar.setOnAction(e -> onEditar());
        btnExcluir.setOnAction(e -> onExcluir());

        // estado inicial
        btnEditar.setDisable(true);
        btnExcluir.setDisable(true);
    }

    private void refreshTable() {
        profs.setAll(profDao.findAll(Professor.class));
    }

    private void clearForm() {
        editing = null;
        txtNome.clear();
        txtEmail.clear();
        txtFormacao.clear();
        btnSalvar.setText("Salvar");
        tableProfessores.getSelectionModel().clearSelection();
    }

    private void onSalvar() {
        String nome = txtNome.getText().trim();
        String email = txtEmail.getText().trim();
        String form = txtFormacao.getText().trim();

        if (nome.isEmpty() || email.isEmpty() || form.isEmpty()) {
            new Alert(Alert.AlertType.WARNING,
                    "Preencha todos os campos.", ButtonType.OK)
                    .showAndWait();
            return;
        }

        if (editing == null) {
            // CREATE
            Professor p = new Professor();
            p.setNome(nome);
            p.setEmail(email);
            p.setFormacao(form);
            profDao.create(p);
        } else {
            // UPDATE
            editing.setNome(nome);
            editing.setEmail(email);
            editing.setFormacao(form);
            profDao.update(editing);
        }

        refreshTable();
        clearForm();
    }

    private void onEditar() {
        Professor sel = tableProfessores.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        editing = sel;
        txtNome.setText(sel.getNome());
        txtEmail.setText(sel.getEmail());
        txtFormacao.setText(sel.getFormacao());
        btnSalvar.setText("Atualizar");
    }

    private void onExcluir() {
        Professor sel = tableProfessores.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Excluir professor \"" + sel.getNome() + "\"?",
                ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES) {
                profDao.delete(sel);
                refreshTable();
                clearForm();
            }
        });
    }
}
