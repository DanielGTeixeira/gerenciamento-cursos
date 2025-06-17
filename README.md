# Gerenciamento de Cursos

Um sistema de cadastro e gerenciamento de **Cursos**, **Disciplinas**, **Professores** e **Turmas**, construído em Java 11+, JavaFX 19 e JPA/Hibernate com PostgreSQL.

---

## 💡 Funcionalidades

- **CRUD de Cursos**  
  - Nome e carga horária
  - Lista em tabela

- **CRUD de Disciplinas**  
  - Nome, descrição, curso associado  
  - Seleção de múltiplos professores  
  - Lista em tabela com coluna do curso exibindo seu nome  

- **CRUD de Professores**  
  - Nome, e-mail, formação  
  - Lista em tabela

- **CRUD completo de Turmas**  
  - Semestre, horário, disciplina e professor  
  - Botões “Salvar” / “Editar” / “Excluir”  
  - Tabela exibindo semestre, horário, nome da disciplina e nome do professor  

- **ComboBoxes e ListViews** mostram diretamente o atributo `nome` das entidades, graças a:
  - Sobrescrita de `toString()` em cada modelo (ex.: `return nome;`), ou
  - Uso de `StringConverter` / `ReadOnlyStringWrapper` nas colunas de tabela

---

## 🛠️ Tecnologias

- **Java 11+**  
- **JavaFX 19** (via Maven plugin — `org.openjfx:javafx-maven-plugin`)  
- **JPA / Hibernate 5.6**  
- **PostgreSQL** (jdbc:postgresql)  
- **Maven** para build e dependências  
- **FXML + CSS** para as telas

---

## 🚀 Como rodar

1. **Pré-requisitos**  
   - JDK 11 ou superior instalado  
   - PostgreSQL rodando em `jdbc:postgresql://localhost:5432/gerenciamento_cursos`  
     - Usuário: `postgres` / senha configurada em `persistence.xml`  
     - Crie o banco e deixe o Hibernate gerar as tabelas

2. **Clone**  
   ```bash
   git clone https://seurepositorio.com/gerenciamento-cursos.git
   cd gerenciamento-cursos
