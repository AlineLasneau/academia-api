package com.academia.api.controller;

import com.academia.api.model.Aluno;
import com.academia.api.repository.AlunoRepository;
import com.academia.api.repository.AvaliacaoFisicaRepository;

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
class AvaliacaoFisicaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private AvaliacaoFisicaRepository avaliacaoRepository;

    private Long cadastrarAluno() {
        Aluno aluno = new Aluno();
        aluno.setNomeCompleto("Maria Silva");
        aluno.setCpf("12345678901");
        aluno.setEmail("maria@email.com");
        aluno.setTelefone("21999999999");
        aluno.setDataNascimento(LocalDate.of(2000, 5, 15));

        return alunoRepository.save(aluno).getId();
    }

    private String avaliacaoJson(Long alunoId) {
        return """
                {
                    "alunoId": %d,
                    "dataAvaliacao": "2026-11-16",
                    "peso": 70.5,
                    "altura": 1.70,
                    "percentualGordura": 20.5,
                    "observacoes": "Avaliação inicial"
                }
                """.formatted(alunoId);
    }

    @Test
    void deveCadastrarAvaliacaoFisica() throws Exception {
        Long alunoId = cadastrarAluno();

        mockMvc.perform(post("/avaliacoes-fisicas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(avaliacaoJson(alunoId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.alunoId").value(alunoId))
                .andExpect(jsonPath("$.peso").value(70.5))
                .andExpect(jsonPath("$.altura").value(1.70));
    }

    @Test
    void naoDeveCadastrarAvaliacaoSemPeso() throws Exception {
        Long alunoId = cadastrarAluno();

        String json = """
                {
                    "alunoId": %d,
                    "dataAvaliacao": "2026-11-16",
                    "peso": null,
                    "altura": 1.70,
                    "percentualGordura": 20.5
                }
                """.formatted(alunoId);

        mockMvc.perform(post("/avaliacoes-fisicas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void naoDeveCadastrarAvaliacaoComDataFutura() throws Exception {
        Long alunoId = cadastrarAluno();

        String json = """
                {
                    "alunoId": %d,
                    "dataAvaliacao": "2099-12-10",
                    "peso": 70.5,
                    "altura": 1.70,
                    "percentualGordura": 20.5
                }
                """.formatted(alunoId);

        mockMvc.perform(post("/avaliacoes-fisicas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void naoDeveCadastrarAvaliacaoComAlunoInexistente()
            throws Exception {

        mockMvc.perform(post("/avaliacoes-fisicas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(avaliacaoJson(999999L)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveListarAvaliacoes() throws Exception {
        Long alunoId = cadastrarAluno();

        mockMvc.perform(post("/avaliacoes-fisicas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(avaliacaoJson(alunoId)));

        mockMvc.perform(get("/avaliacoes-fisicas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].peso").value(70.5));
    }

    @Test
    void deveListarAvaliacoesPorAluno() throws Exception {
        Long alunoId = cadastrarAluno();

        mockMvc.perform(post("/avaliacoes-fisicas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(avaliacaoJson(alunoId)));

        mockMvc.perform(get(
                        "/avaliacoes-fisicas/aluno/" + alunoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].alunoId").value(alunoId));
    }

    @Test
    void deveBuscarAvaliacaoPorId() throws Exception {
        Long alunoId = cadastrarAluno();

        mockMvc.perform(post("/avaliacoes-fisicas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(avaliacaoJson(alunoId)));

        Long id = avaliacaoRepository.findAll().get(0).getId();

        mockMvc.perform(get("/avaliacoes-fisicas/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void deveAtualizarAvaliacao() throws Exception {
        Long alunoId = cadastrarAluno();

        mockMvc.perform(post("/avaliacoes-fisicas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(avaliacaoJson(alunoId)));

        Long id = avaliacaoRepository.findAll().get(0).getId();

        String atualizado = """
                {
                    "alunoId": %d,
                    "dataAvaliacao": "2026-11-16",
                    "peso": 68.0,
                    "altura": 1.70,
                    "percentualGordura": 18.0,
                    "observacoes": "Segunda avaliação"
                }
                """.formatted(alunoId);

        mockMvc.perform(put("/avaliacoes-fisicas/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(atualizado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.peso").value(68.0))
                .andExpect(jsonPath("$.percentualGordura").value(18.0));
    }

    @Test
    void deveExcluirAvaliacao() throws Exception {
        Long alunoId = cadastrarAluno();

        mockMvc.perform(post("/avaliacoes-fisicas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(avaliacaoJson(alunoId)));

        Long id = avaliacaoRepository.findAll().get(0).getId();

        mockMvc.perform(delete("/avaliacoes-fisicas/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/avaliacoes-fisicas/" + id))
                .andExpect(status().isNotFound());
    }
}