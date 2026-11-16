package com.academia.api.controller;

import com.academia.api.repository.ProfessorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:academia-test;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false"
})
class ProfessorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProfessorRepository repository;

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
}