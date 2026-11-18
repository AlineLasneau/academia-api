
package com.academia.api.controller;

import com.academia.api.model.Aluno;
import com.academia.api.model.Plano;
import com.academia.api.repository.AlunoRepository;
import com.academia.api.repository.PlanoRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PlanoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlanoRepository planoRepository;

    @Autowired
    private AlunoRepository alunoRepository;

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
                .andExpect(status().isCreated())
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
                .andExpect(status().isCreated());

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
                .andExpect(status().isCreated());

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
                .andExpect(status().isCreated());

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
                .andExpect(status().isCreated());

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


    @Test
    void deveManterPlanoInativoAoAtualizar() throws Exception {

        Plano plano = new Plano();
        plano.setNome("Plano Antigo");
        plano.setDuracaoMeses(1);
        plano.setValor(new BigDecimal("79.90"));
        plano.setAtivo(false);
        Long id = planoRepository.save(plano).getId();

        String planoAtualizado = """
            {
                "nome": "Plano Antigo Renomeado",
                "descricao": "Descontinuado",
                "duracaoMeses": 1,
                "valor": 79.90
            }
            """;

        mockMvc.perform(put("/planos/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(planoAtualizado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Plano Antigo Renomeado"))
                .andExpect(jsonPath("$.ativo").value(false));
    }

    @Test
    void naoDeveExcluirPlanoComMensalidadeVinculada() throws Exception {

        Plano plano = new Plano();
        plano.setNome("Plano Mensal");
        plano.setDuracaoMeses(1);
        plano.setValor(new BigDecimal("99.90"));
        Long planoId = planoRepository.save(plano).getId();

        Aluno aluno = new Aluno();
        aluno.setNomeCompleto("Aluno Teste");
        aluno.setCpf("12345678901");
        aluno.setEmail("teste@teste.com");
        aluno.setTelefone("21999999999");
        aluno.setDataNascimento(LocalDate.of(2000, 1, 1));
        Long alunoId = alunoRepository.save(aluno).getId();

        String mensalidadeJson = """
            {
                "alunoId": %d,
                "planoId": %d,
                "valor": 99.90,
                "dataVencimento": "2026-12-10"
            }
            """.formatted(alunoId, planoId);

        mockMvc.perform(post("/mensalidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mensalidadeJson))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/planos/" + planoId))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.erro").value(
                        "O plano possui mensalidades vinculadas "
                                + "e não pode ser excluído."));
    }

    @Test
    void deveRetornarMensagemQuandoPlanoNaoExistir() throws Exception {
        mockMvc.perform(get("/planos/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value("Plano não encontrado."));
    }
}
