
package com.academia.api.controller;

import com.academia.api.repository.PlanoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
class PlanoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlanoRepository planoRepository;

    @Test
    void deveCadastrarPlanoComSucesso() throws Exception {

        String planoJson = """
                {
                    "nome": "Plano Mensal",
                    "descricao": "Acesso completo à academia",
                    "duracaoMeses": 1,
                    "valor": 99.90,
                    "ativo": true
                }
                """;

        mockMvc.perform(post("/planos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(planoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Plano Mensal"))
                .andExpect(jsonPath("$.duracaoMeses").value(1))
                .andExpect(jsonPath("$.valor").value(99.90));
    }

    @Test
    void naoDeveCadastrarPlanoSemNome() throws Exception {

        String planoJson = """
                {
                    "nome": "",
                    "descricao": "Plano de teste",
                    "duracaoMeses": 1,
                    "valor": 99.90,
                    "ativo": true
                }
                """;

        mockMvc.perform(post("/planos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(planoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nome").value("O nome do plano é obrigatório"));
    }


    @Test
    void deveListarTodosOsPlanos() throws Exception {

        String planoJson = """
            {
                "nome": "Plano Trimestral",
                "descricao": "Plano de três meses",
                "duracaoMeses": 3,
                "valor": 249.90,
                "ativo": true
            }
            """;

        mockMvc.perform(post("/planos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(planoJson))
                .andExpect(status().isOk());

        mockMvc.perform(get("/planos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome")
                        .value("Plano Trimestral"))
                .andExpect(jsonPath("$[0].duracaoMeses")
                        .value(3));
    }


    @Test
    void deveBuscarPlanoPorId() throws Exception {

        String planoJson = """
            {
                "nome": "Plano Anual",
                "descricao": "Acesso por um ano",
                "duracaoMeses": 12,
                "valor": 899.90,
                "ativo": true
            }
            """;

        mockMvc.perform(post("/planos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(planoJson))
                .andExpect(status().isOk());

        Long id = planoRepository.findAll().get(0).getId();

        mockMvc.perform(get("/planos/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nome").value("Plano Anual"));
    }


    @Test
    void deveRetornar404QuandoPlanoNaoExistir() throws Exception {

        mockMvc.perform(get("/planos/999999"))
                .andExpect(status().isNotFound());
    }


    @Test
    void deveAtualizarPlano() throws Exception {

        String planoOriginal = """
            {
                "nome": "Plano Mensal",
                "descricao": "Plano básico",
                "duracaoMeses": 1,
                "valor": 99.90,
                "ativo": true
            }
            """;

        // Cadastra o plano
        mockMvc.perform(post("/planos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(planoOriginal))
                .andExpect(status().isOk());

        // Recupera o ID gerado
        Long id = planoRepository.findAll().get(0).getId();

        String planoAtualizado = """
            {
                "nome": "Plano Premium",
                "descricao": "Acesso completo",
                "duracaoMeses": 12,
                "valor": 899.90,
                "ativo": true
            }
            """;

        // Atualiza o plano
        mockMvc.perform(put("/planos/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(planoAtualizado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Plano Premium"))
                .andExpect(jsonPath("$.duracaoMeses").value(12))
                .andExpect(jsonPath("$.valor").value(899.90));
    }

    @Test
    void deveExcluirPlano() throws Exception {

        String planoJson = """
            {
                "nome": "Plano Teste",
                "descricao": "Plano para exclusão",
                "duracaoMeses": 1,
                "valor": 99.90,
                "ativo": true
            }
            """;

        mockMvc.perform(post("/planos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(planoJson))
                .andExpect(status().isOk());

        Long id = planoRepository.findAll().get(0).getId();

        mockMvc.perform(delete("/planos/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/planos/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void naoDeveAtualizarPlanoInexistente() throws Exception {

        String planoJson = """
        {
            "nome": "Plano Premium",
            "descricao": "Acesso completo",
            "duracaoMeses": 12,
            "valor": 99.90,
            "ativo": true
        }
        """;

        mockMvc.perform(put("/planos/999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(planoJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro")
                        .value("Plano não encontrado."));
    }

    @Test
    void naoDeveExcluirPlanoInexistente() throws Exception {

        mockMvc.perform(delete("/planos/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro")
                        .value("Plano não encontrado."));
    }


}
