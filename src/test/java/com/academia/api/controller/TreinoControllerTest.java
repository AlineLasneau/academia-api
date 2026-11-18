package com.academia.api.controller;

import com.academia.api.model.Aluno;
import com.academia.api.model.Professor;
import com.academia.api.repository.AlunoRepository;
import com.academia.api.repository.ProfessorRepository;
import com.academia.api.repository.TreinoRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TreinoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private TreinoRepository treinoRepository;

    private Long cadastrarAluno() {
        Aluno aluno = new Aluno();
        aluno.setNomeCompleto("Maria Silva");
        aluno.setCpf("12345678901");
        aluno.setEmail("maria@email.com");
        aluno.setTelefone("21999999999");
        aluno.setDataNascimento(LocalDate.of(2000, 5, 15));
        return alunoRepository.save(aluno).getId();
    }

    private Long cadastrarProfessor() {
        Professor professor = new Professor();
        professor.setNomeCompleto("Carlos Silva");
        professor.setCpf("98765432100");
        professor.setEmail("carlos@email.com");
        professor.setTelefone("21988888888");
        professor.setEspecialidade("Musculação");
        return professorRepository.save(professor).getId();
    }

    private String treinoJson(Long alunoId, Long professorId,
                              String nome) {
        return """
                {
                    "nome": "%s",
                    "descricao": "Treino de musculação",
                    "alunoId": %d,
                    "professorId": %d
                }
                """.formatted(nome, alunoId, professorId);
    }

    @Test
    void deveCadastrarTreino() throws Exception {
        Long alunoId = cadastrarAluno();
        Long professorId = cadastrarProfessor();

        mockMvc.perform(post("/treinos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(treinoJson(
                                alunoId, professorId, "Treino A")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Treino A"))
                .andExpect(jsonPath("$.alunoId").value(alunoId))
                .andExpect(jsonPath("$.professorId").value(professorId));
    }

    @Test
    void naoDeveCadastrarTreinoSemNome() throws Exception {
        Long alunoId = cadastrarAluno();
        Long professorId = cadastrarProfessor();

        mockMvc.perform(post("/treinos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(treinoJson(
                                alunoId, professorId, "")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void naoDeveCadastrarTreinoComAlunoInexistente() throws Exception {
        Long professorId = cadastrarProfessor();

        mockMvc.perform(post("/treinos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(treinoJson(
                                999999L, professorId, "Treino A")))
                .andExpect(status().isNotFound());
    }

    @Test
    void naoDeveCadastrarTreinoComProfessorInexistente()
            throws Exception {
        Long alunoId = cadastrarAluno();

        mockMvc.perform(post("/treinos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(treinoJson(
                                alunoId, 999999L, "Treino A")))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveListarTreinos() throws Exception {
        Long alunoId = cadastrarAluno();
        Long professorId = cadastrarProfessor();

        mockMvc.perform(post("/treinos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(treinoJson(
                        alunoId, professorId, "Treino A")));

        mockMvc.perform(get("/treinos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Treino A"));
    }

    @Test
    void deveListarTreinosPorAluno() throws Exception {
        Long alunoId = cadastrarAluno();
        Long professorId = cadastrarProfessor();

        mockMvc.perform(post("/treinos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(treinoJson(
                        alunoId, professorId, "Treino A")));

        mockMvc.perform(get("/treinos/aluno/" + alunoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].alunoId").value(alunoId));
    }

    @Test
    void deveBuscarTreinoPorId() throws Exception {
        Long alunoId = cadastrarAluno();
        Long professorId = cadastrarProfessor();

        mockMvc.perform(post("/treinos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(treinoJson(
                        alunoId, professorId, "Treino A")));

        Long id = treinoRepository.findAll().get(0).getId();

        mockMvc.perform(get("/treinos/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void deveAtualizarTreino() throws Exception {
        Long alunoId = cadastrarAluno();
        Long professorId = cadastrarProfessor();

        mockMvc.perform(post("/treinos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(treinoJson(
                        alunoId, professorId, "Treino A")));

        Long id = treinoRepository.findAll().get(0).getId();

        mockMvc.perform(put("/treinos/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(treinoJson(
                                alunoId, professorId, "Treino B")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Treino B"));
    }

    @Test
    void deveExcluirTreino() throws Exception {
        Long alunoId = cadastrarAluno();
        Long professorId = cadastrarProfessor();

        mockMvc.perform(post("/treinos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(treinoJson(
                        alunoId, professorId, "Treino A")));

        Long id = treinoRepository.findAll().get(0).getId();

        mockMvc.perform(delete("/treinos/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/treinos/" + id))
                .andExpect(status().isNotFound());
    }
}