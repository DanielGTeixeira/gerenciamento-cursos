package br.com.seuapp.controller;

import br.com.seuapp.dao.CursoDao;
import br.com.seuapp.dao.DisciplinaDao;
import br.com.seuapp.dao.ProfessorDao;
import br.com.seuapp.model.Curso;
import br.com.seuapp.model.Disciplina;
import br.com.seuapp.model.Professor;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.cell.PropertyValueFactory;


import java.util.ArrayList;
import java.util.List;

public class DisciplinaController {

    @FXML private TextField txtNome;
    @FXML private TextArea  txtDescricao;
    @FXML private ComboBox<Curso>       cbCurso;
    @FXML private ListView<Professor>   lvProfessores;

    @FXML private Button btnSalvar;
    @FXML private Button btnEditar;
    @FXML private Button btnExcluir;

    @FXML private TableView<Disciplina> tableDisciplinas;
    @FXML private TableColumn<Disciplina, String> colNome;
    @FXML private TableColumn<Disciplina, String> colDescricao;
    @FXML private TableColumn<Disciplina, String> colCurso;

    private final DisciplinaDao disciplinaDao = new DisciplinaDao();
    private final CursoDao       cursoDao     = new CursoDao();
    private final ProfessorDao   professorDao = new ProfessorDao();

    private final ObservableList<Disciplina> listaDisc        = FXCollections.observableArrayList();
    private final ObservableList<Curso>      listaCursos      = FXCollections.observableArrayList();
    private final ObservableList<Professor>  listaProfessores = FXCollections.observableArrayList();

    /** quando != null, estamos em modo “editar” */
    private Disciplina editing = null;

    @FXML
    public void initialize() {
        // popula combobox e listview
        listaCursos.setAll(cursoDao.findAll(Curso.class));
        cbCurso.setItems(listaCursos);

        listaProfessores.setAll(professorDao.findAll(Professor.class));
        lvProfessores.setItems(listaProfessores);
        lvProfessores.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // configura colunas da tabela
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colCurso.setCellValueFactory(c -> {
            String n = c.getValue().getCurso()!=null
                    ? c.getValue().getCurso().getNome()
                    : "";
            return new ReadOnlyStringWrapper(n);
        });

        // vincula e carrega tabela
        tableDisciplinas.setItems(listaDisc);
        refreshTable();

        // listener seleção
        tableDisciplinas.getSelectionModel().selectedItemProperty()
                .addListener((obs,o,n) -> {
                    boolean has = n!=null;
                    btnEditar.setDisable(!has);
                    btnExcluir.setDisable(!has);
                });

        // ações
        btnSalvar .setOnAction(e -> onSalvar());
        btnEditar .setOnAction(e -> onEditar());
        btnExcluir.setOnAction(e -> onExcluir());

        // estado inicial
        btnEditar.setDisable(true);
        btnExcluir.setDisable(true);
    }

    private void refreshTable() {
        listaDisc.setAll(disciplinaDao.findAll(Disciplina.class));
    }

    private void clearForm() {
        editing = null;
        txtNome.clear();
        txtDescricao.clear();
        cbCurso.getSelectionModel().clearSelection();
        lvProfessores.getSelectionModel().clearSelection();
        btnSalvar.setText("Salvar");
        tableDisciplinas.getSelectionModel().clearSelection();
    }

    private void onSalvar() {
        String nome = txtNome.getText().trim();
        String desc = txtDescricao.getText().trim();
        Curso curso = cbCurso.getValue();
        List<Professor> profs = new ArrayList<>(lvProfessores.getSelectionModel().getSelectedItems());

        if (nome.isEmpty() || desc.isEmpty() || curso==null || profs.isEmpty()) {
            new Alert(Alert.AlertType.WARNING,
                    "Preencha todos os campos e selecione pelo menos 1 professor.",
                    ButtonType.OK).showAndWait();
            return;
        }

        if (editing==null) {
            // CREATE
            Disciplina d = new Disciplina();
            d.setNome(nome);
            d.setDescricao(desc);
            d.setCurso(curso);
            d.setProfessores(profs);
            disciplinaDao.create(d);
        } else {
            // UPDATE
            editing.setNome(nome);
            editing.setDescricao(desc);
            editing.setCurso(curso);
            editing.setProfessores(profs);
            disciplinaDao.update(editing);
        }

        refreshTable();
        clearForm();
    }

    private void onEditar() {
        Disciplina sel = tableDisciplinas.getSelectionModel().getSelectedItem();
        if (sel==null) return;
        editing = sel;

        txtNome.setText(sel.getNome());
        txtDescricao.setText(sel.getDescricao());
        cbCurso.setValue(sel.getCurso());

        // seleciona professores
        lvProfessores.getSelectionModel().clearSelection();
        for (Professor p : sel.getProfessores()) {
            lvProfessores.getSelectionModel().select(p);
        }

        btnSalvar.setText("Atualizar");
    }

    private void onExcluir() {
        Disciplina sel = tableDisciplinas.getSelectionModel().getSelectedItem();
        if (sel==null) return;
        Alert c = new Alert(Alert.AlertType.CONFIRMATION,
                "Excluir disciplina \""+sel.getNome()+"\"?",
                ButtonType.YES, ButtonType.NO);
        c.showAndWait().ifPresent(bt -> {
            if (bt==ButtonType.YES) {
                disciplinaDao.delete(sel);
                refreshTable();
                clearForm();
            }
        });
    }
}
