package br.com.seuapp.controller;

import br.com.seuapp.dao.DisciplinaDao;
import br.com.seuapp.dao.ProfessorDao;
import br.com.seuapp.dao.TurmaDao;
import br.com.seuapp.model.Disciplina;
import br.com.seuapp.model.Professor;
import br.com.seuapp.model.Turma;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.cell.PropertyValueFactory;


public class TurmaController {

    @FXML private TextField txtSemestre;
    @FXML private TextField txtHorario;
    @FXML private ComboBox<Disciplina> cbDisciplina;
    @FXML private ComboBox<Professor>  cbProfessor;

    @FXML private Button btnSalvar;
    @FXML private Button btnEditar;
    @FXML private Button btnExcluir;

    @FXML private TableView<Turma> tableTurmas;
    @FXML private TableColumn<Turma, String> colSemestre;
    @FXML private TableColumn<Turma, String> colHorario;
    @FXML private TableColumn<Turma, String> colDisciplina;
    @FXML private TableColumn<Turma, String> colProfessor;

    private final TurmaDao      turmaDao      = new TurmaDao();
    private final DisciplinaDao disciplinaDao = new DisciplinaDao();
    private final ProfessorDao  professorDao  = new ProfessorDao();

    private final ObservableList<Disciplina> disciplinas = FXCollections.observableArrayList();
    private final ObservableList<Professor>  professores = FXCollections.observableArrayList();
    private final ObservableList<Turma>      turmas       = FXCollections.observableArrayList();

    /** null = modo "novo"; não-null = modo "editar" */
    private Turma editing = null;

    @FXML
    public void initialize() {
        // load relacionamentos
        disciplinas.setAll(disciplinaDao.findAll(Disciplina.class));
        professores.setAll(professorDao.findAll(Professor.class));
        cbDisciplina.setItems(disciplinas);
        cbProfessor .setItems(professores);

        // colunas da tabela
        colSemestre .setCellValueFactory(new PropertyValueFactory<>("semestre"));
        colHorario  .setCellValueFactory(new PropertyValueFactory<>("horario"));
        colDisciplina.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(cell.getValue().getDisciplina().getNome())
        );
        colProfessor .setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(cell.getValue().getProfessor().getNome())
        );

        // binding da tabela
        tableTurmas.setItems(turmas);
        refreshTable();

        // habilita botões de editar/excluir ao selecionar
        tableTurmas.getSelectionModel().selectedItemProperty().addListener((obs, oldV, sel) -> {
            boolean has = sel != null;
            btnEditar.setDisable(!has);
            btnExcluir.setDisable(!has);
        });

        // configura ações
        btnSalvar.setOnAction(e -> onSalvar());
        btnEditar.setOnAction(e -> onEditar());
        btnExcluir.setOnAction(e -> onExcluir());

        // estado inicial dos botões
        btnEditar.setDisable(true);
        btnExcluir.setDisable(true);
    }

    private void refreshTable() {
        turmas.setAll(turmaDao.findAll(Turma.class));
    }

    private void clearForm() {
        editing = null;
        txtSemestre.clear();
        txtHorario .clear();
        cbDisciplina.getSelectionModel().clearSelection();
        cbProfessor .getSelectionModel().clearSelection();
        btnSalvar.setText("Salvar");
        tableTurmas.getSelectionModel().clearSelection();
    }

    private void onSalvar() {
        String sem = txtSemestre.getText().trim();
        String hor = txtHorario .getText().trim();
        Disciplina d = cbDisciplina.getValue();
        Professor  p = cbProfessor .getValue();

        if (sem.isEmpty() || hor.isEmpty() || d == null || p == null) {
            new Alert(Alert.AlertType.WARNING,
                    "Preencha todos os campos da turma.",
                    ButtonType.OK).showAndWait();
            return;
        }

        if (editing == null) {
            // CREATE
            Turma t = new Turma();
            t.setSemestre(sem);
            t.setHorario(hor);
            t.setDisciplina(d);
            t.setProfessor(p);
            turmaDao.create(t);
        } else {
            // UPDATE
            editing.setSemestre(sem);
            editing.setHorario(hor);
            editing.setDisciplina(d);
            editing.setProfessor(p);
            turmaDao.update(editing);
        }

        refreshTable();
        clearForm();
    }

    private void onEditar() {
        Turma sel = tableTurmas.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        editing = sel;
        txtSemestre.setText(sel.getSemestre());
        txtHorario .setText(sel.getHorario());
        cbDisciplina.getSelectionModel().select(sel.getDisciplina());
        cbProfessor .getSelectionModel().select(sel.getProfessor());
        btnSalvar.setText("Atualizar");
    }

    private void onExcluir() {
        Turma sel = tableTurmas.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Excluir turma do semestre \"" + sel.getSemestre() + "\" ?",
                ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES) {
                turmaDao.delete(sel);
                refreshTable();
                clearForm();
            }
        });
    }
}
