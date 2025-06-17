package br.com.seuapp.model;

import javax.persistence.*;

@Entity
@Table(name = "turma")
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String semestre;

    @Column(nullable = false)
    private String horario;

    @ManyToOne
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    public Turma() {
        // construtor padrão
    }


    // Construtor auxiliar (sem ID)
    public Turma(String semestre, String horario, Disciplina disciplina, Professor professor) {
        this.semestre   = semestre;
        this.horario    = horario;
        this.disciplina = disciplina;
        this.professor  = professor;
    }


    // getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSemestre() { return semestre; }
    public void setSemestre(String semestre) { this.semestre = semestre; }

    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }

    public Disciplina getDisciplina() { return disciplina; }
    public void setDisciplina(Disciplina disciplina) { this.disciplina = disciplina; }

    public Professor getProfessor() { return professor; }
    public void setProfessor(Professor professor) { this.professor = professor; }
}
