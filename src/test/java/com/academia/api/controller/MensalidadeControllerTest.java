
package com.academia.api.controller;

import com.academia.api.model.Aluno;
import com.academia.api.model.Plano;
import com.academia.api.repository.AlunoRepository;
import com.academia.api.repository.MensalidadeRepository;
import com.academia.api.repository.PlanoRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MensalidadeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private PlanoRepository planoRepository;

    @Autowired
    private MensalidadeRepository mensalidadeRepository;

    private Long cadastrarAluno() {
        Aluno aluno = new Aluno();
        aluno.setNomeCompleto("Aluno Teste");
        aluno.setCpf("12345678901");
        aluno.setEmail("teste@teste.com");
        aluno.setTelefone("21999999999");
        aluno.setDataNascimento(java.time.LocalDate.of(2000, 1, 1));

        return alunoRepository.save(aluno).getId();
    }

    private Long cadastrarPlano() {
        Plano plano = new Plano();
        plano.setNome("Plano Mensal");
        plano.setDescricao("Plano de teste");
        plano.setDuracaoMeses(12);
        plano.setValor(new java.math.BigDecimal("99.90"));

        return planoRepository.save(plano).getId();
    }

    @Test
    void deveCadastrarMensalidade() throws Exception {
        Long alunoId = cadastrarAluno();
        Long planoId = cadastrarPlano();

        String json = """
                {
                    "alunoId": %d,
                    "planoId": %d,
                    "valor": 99.90,
                    "dataVencimento": "2026-12-10",
                    "status": "PENDENTE"
                }
                """.formatted(alunoId, planoId);

        mockMvc.perform(post("/mensalidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.valor").value(99.90))
                .andExpect(jsonPath("$.status").value("PENDENTE"));
    }

    @Test
    void deveListarMensalidades() throws Exception {
        mockMvc.perform(get("/mensalidades"))
                .andExpect(status().isOk());
    }

    @Test
    void deveBuscarMensalidadePorId() throws Exception {
        Long alunoId = cadastrarAluno();
        Long planoId = cadastrarPlano();

        String json = """
                {
                    "alunoId": %d,
                    "planoId": %d,
                    "valor": 99.90,
                    "dataVencimento": "2026-12-10",
                    "status": "PENDENTE"
                }
                """.formatted(alunoId, planoId);

        mockMvc.perform(post("/mensalidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        Long id = mensalidadeRepository.findAll().get(0).getId();

        mockMvc.perform(get("/mensalidades/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDENTE"));
    }

    @Test
    void deveRetornarErroAoBuscarMensalidadeInexistente() throws Exception {
        mockMvc.perform(get("/mensalidades/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveAtualizarMensalidade() throws Exception {
        Long alunoId = cadastrarAluno();
        Long planoId = cadastrarPlano();

        String mensalidadeOriginal = """
                {
                    "alunoId": %d,
                    "planoId": %d,
                    "valor": 99.90,
                    "dataVencimento": "2026-12-10",
                    "status": "PENDENTE"
                }
                """.formatted(alunoId, planoId);

        mockMvc.perform(post("/mensalidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mensalidadeOriginal))
                .andExpect(status().isCreated());

        Long id = mensalidadeRepository.findAll().get(0).getId();

        String mensalidadeAtualizada = """
                {
                    "alunoId": %d,
                    "planoId": %d,
                    "valor": 149.90,
                    "dataVencimento": "2027-01-10",
                    "dataPagamento": "2026-12-15",
                    "status": "PAGO"
                }
                """.formatted(alunoId, planoId);

        mockMvc.perform(put("/mensalidades/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mensalidadeAtualizada))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor").value(149.90))
                .andExpect(jsonPath("$.status").value("PAGO"))
                .andExpect(jsonPath("$.dataPagamento").value("2026-12-15"));
    }

    @Test
    void deveExcluirMensalidade() throws Exception {
        Long alunoId = cadastrarAluno();
        Long planoId = cadastrarPlano();

        String json = """
                {
                    "alunoId": %d,
                    "planoId": %d,
                    "valor": 99.90,
                    "dataVencimento": "2026-12-10",
                    "status": "PENDENTE"
                }
                """.formatted(alunoId, planoId);

        mockMvc.perform(post("/mensalidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        Long id = mensalidadeRepository.findAll().get(0).getId();

        mockMvc.perform(delete("/mensalidades/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/mensalidades/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornarErroAoCadastrarMensalidadeComDadosObrigatoriosAusentes() throws Exception {
        Long alunoId = cadastrarAluno();
        Long planoId = cadastrarPlano();

        String json = """
                {
                    "alunoId": %d,
                    "planoId": %d,
                    "valor": null,
                    "dataVencimento": null,
                    "status": ""
                }
                """.formatted(alunoId, planoId);

        mockMvc.perform(post("/mensalidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarErroAoCadastrarMensalidadeComStatusInvalido() throws Exception {
        Long alunoId = cadastrarAluno();
        Long planoId = cadastrarPlano();

        String json = """
                {
                    "alunoId": %d,
                    "planoId": %d,
                    "valor": 99.90,
                    "dataVencimento": "2026-12-10",
                    "status": "CANCELADO"
                }
                """.formatted(alunoId, planoId);

        mockMvc.perform(post("/mensalidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarErroAoCadastrarMensalidadeComAlunoInexistente() throws Exception {
        Long planoId = cadastrarPlano();

        String json = """
                {
                    "alunoId": 9999,
                    "planoId": %d,
                    "valor": 99.90,
                    "dataVencimento": "2026-12-10",
                    "status": "PENDENTE"
                }
                """.formatted(planoId);

        mockMvc.perform(post("/mensalidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornarErroAoAtualizarMensalidadeComAlunoInexistente() throws Exception {
        Long alunoId = cadastrarAluno();
        Long planoId = cadastrarPlano();

        String original = """
                {
                    "alunoId": %d,
                    "planoId": %d,
                    "valor": 99.90,
                    "dataVencimento": "2026-12-10",
                    "status": "PENDENTE"
                }
                """.formatted(alunoId, planoId);

        mockMvc.perform(post("/mensalidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(original))
                .andExpect(status().isCreated());

        Long id = mensalidadeRepository.findAll().get(0).getId();

        String atualizado = """
                {
                    "alunoId": 9999,
                    "planoId": %d,
                    "valor": 149.90,
                    "dataVencimento": "2027-01-10",
                    "status": "PAGO"
                }
                """.formatted(planoId);

        mockMvc.perform(put("/mensalidades/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(atualizado))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornarErroAoCadastrarMensalidadeComPlanoInexistente() throws Exception {
        Long alunoId = cadastrarAluno();

        String json = """
                {
                    "alunoId": %d,
                    "planoId": 9999,
                    "valor": 99.90,
                    "dataVencimento": "2026-12-10",
                    "status": "PENDENTE"
                }
                """.formatted(alunoId);

        mockMvc.perform(post("/mensalidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornarErroAoAtualizarMensalidadeComPlanoInexistente() throws Exception {
        Long alunoId = cadastrarAluno();
        Long planoId = cadastrarPlano();

        String original = """
                {
                    "alunoId": %d,
                    "planoId": %d,
                    "valor": 99.90,
                    "dataVencimento": "2026-12-10",
                    "status": "PENDENTE"
                }
                """.formatted(alunoId, planoId);

        mockMvc.perform(post("/mensalidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(original))
                .andExpect(status().isCreated());

        Long id = mensalidadeRepository.findAll().get(0).getId();

        String atualizado = """
                {
                    "alunoId": %d,
                    "planoId": 9999,
                    "valor": 149.90,
                    "dataVencimento": "2027-01-10",
                    "status": "PAGO"
                }
                """.formatted(alunoId);

        mockMvc.perform(put("/mensalidades/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(atualizado))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornarMensagemQuandoMensalidadeNaoExistir() throws Exception {
        mockMvc.perform(get("/mensalidades/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro")
                        .value("Mensalidade não encontrada."));
    }
}
