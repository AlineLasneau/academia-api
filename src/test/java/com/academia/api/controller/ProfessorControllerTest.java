package com.academia.api.controller;

import com.academia.api.model.Aluno;
import com.academia.api.repository.AlunoRepository;
import com.academia.api.repository.ProfessorRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProfessorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProfessorRepository repository;

    @Autowired
    private AlunoRepository alunoRepository;

    private String professorJson(String nome, String cpf, String email) {
        return """
                {
                    "nomeCompleto": "%s",
                    "cpf": "%s",
                    "email": "%s",
                    "telefone": "21999999999",
                    "especialidade": "Musculação"
                }
                """.formatted(nome, cpf, email);
    }

    @Test
    void deveCadastrarProfessor() throws Exception {
        mockMvc.perform(post("/professores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(professorJson(
                                "Carlos Silva", "11111111111",
                                "carlos@email.com")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nomeCompleto")
                        .value("Carlos Silva"))
                .andExpect(jsonPath("$.especialidade")
                        .value("Musculação"));
    }

    @Test
    void naoDeveCadastrarProfessorSemNome() throws Exception {
        mockMvc.perform(post("/professores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(professorJson(
                                "", "11111111111",
                                "carlos@email.com")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveListarProfessores() throws Exception {
        mockMvc.perform(post("/professores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(professorJson(
                        "Carlos Silva", "11111111111",
                        "carlos@email.com")));

        mockMvc.perform(get("/professores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomeCompleto")
                        .value("Carlos Silva"));
    }

    @Test
    void deveBuscarProfessorPorId() throws Exception {
        mockMvc.perform(post("/professores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(professorJson(
                        "Carlos Silva", "11111111111",
                        "carlos@email.com")));

        Long id = repository.findAll().get(0).getId();

        mockMvc.perform(get("/professores/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void deveRetornar404ParaProfessorInexistente() throws Exception {
        mockMvc.perform(get("/professores/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveAtualizarProfessor() throws Exception {
        mockMvc.perform(post("/professores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(professorJson(
                        "Carlos Silva", "11111111111",
                        "carlos@email.com")));

        Long id = repository.findAll().get(0).getId();

        mockMvc.perform(put("/professores/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(professorJson(
                                "Carlos Souza", "11111111111",
                                "carlos@email.com")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeCompleto")
                        .value("Carlos Souza"));
    }

    @Test
    void deveExcluirProfessor() throws Exception {
        mockMvc.perform(post("/professores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(professorJson(
                        "Carlos Silva", "11111111111",
                        "carlos@email.com")));

        Long id = repository.findAll().get(0).getId();

        mockMvc.perform(delete("/professores/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/professores/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void naoDeveCadastrarProfessorComCpfDuplicado() throws Exception {
        mockMvc.perform(post("/professores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(professorJson(
                                "Carlos Silva", "11111111111",
                                "carlos@email.com")))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/professores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(professorJson(
                                "Outro Professor", "11111111111",
                                "outro@email.com")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.erro").value("CPF já cadastrado."));
    }

    @Test
    void naoDeveExcluirProfessorComTreinoVinculado() throws Exception {
        mockMvc.perform(post("/professores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(professorJson(
                                "Carlos Silva", "11111111111",
                                "carlos@email.com")))
                .andExpect(status().isCreated());

        Long professorId = repository.findAll().get(0).getId();

        Aluno aluno = new Aluno();
        aluno.setNomeCompleto("Maria Silva");
        aluno.setCpf("12345678901");
        aluno.setEmail("maria@email.com");
        aluno.setTelefone("21999999999");
        aluno.setDataNascimento(LocalDate.of(2000, 5, 15));
        Long alunoId = alunoRepository.save(aluno).getId();

        String treinoJson = """
                {
                    "nome": "Treino A",
                    "alunoId": %d,
                    "professorId": %d
                }
                """.formatted(alunoId, professorId);

        mockMvc.perform(post("/treinos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(treinoJson))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/professores/" + professorId))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.erro").value(
                        "O professor possui treinos vinculados "
                                + "e não pode ser excluído."));
    }
}
