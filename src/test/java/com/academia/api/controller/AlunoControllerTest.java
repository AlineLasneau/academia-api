package com.academia.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import com.academia.api.repository.AlunoRepository;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AlunoRepository alunoRepository;

    @Test
    void deveCadastrarAlunoComSucesso() throws Exception {

        String alunoJson = """
                {
                    "nomeCompleto": "Maria Silva",
                    "cpf": "12345678900",
                    "email": "maria@email.com",
                    "telefone": "11999999999",
                    "dataNascimento": "2000-05-15"
                }
                """;

        mockMvc.perform(post("/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(alunoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nomeCompleto").value("Maria Silva"))
                .andExpect(jsonPath("$.email").value("maria@email.com"));
    }

    @Test
    void naoDeveCadastrarAlunoSemNome() throws Exception {

        String alunoJson = """
                {
                    "nomeCompleto": "",
                    "cpf": "12345678900",
                    "email": "maria@email.com",
                    "telefone": "11999999999",
                    "dataNascimento": "2000-05-15"
                }
                """;

        mockMvc.perform(post("/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(alunoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nomeCompleto")
                        .value("O nome é obrigatório"));
    }

    @Test
    void deveListarTodosOsAlunos() throws Exception {

        String alunoJson = """
            {
                "nomeCompleto": "Carlos Souza",
                "cpf": "98765432100",
                "email": "carlos@email.com",
                "telefone": "11988888888",
                "dataNascimento": "1998-08-20"
            }
            """;

        mockMvc.perform(post("/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(alunoJson))
                .andExpect(status().isOk());

        mockMvc.perform(get("/alunos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomeCompleto")
                        .value("Carlos Souza"))
                .andExpect(jsonPath("$[0].email")
                        .value("carlos@email.com"));
    }

    @Test
    void deveBuscarAlunoPorId() throws Exception {

        String alunoJson = """
            {
                "nomeCompleto": "Ana Oliveira",
                "cpf": "11122233344",
                "email": "ana@email.com",
                "telefone": "11977777777",
                "dataNascimento": "1999-03-10"
            }
            """;

        mockMvc.perform(post("/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(alunoJson))
                .andExpect(status().isOk());

        Long id = alunoRepository.findAll().get(0).getId();

        mockMvc.perform(get("/alunos/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nomeCompleto")
                        .value("Ana Oliveira"))
                .andExpect(jsonPath("$.email")
                        .value("ana@email.com"));
    }


    @Test
    void deveRetornar404QuandoAlunoNaoExistir() throws Exception {

        mockMvc.perform(get("/alunos/999999"))
                .andExpect(status().isNotFound());
    }


    @Test
    void deveAtualizarAluno() throws Exception {

        String alunoOriginal = """
            {
                "nomeCompleto": "Joao Silva",
                "cpf": "22233344455",
                "email": "joao@email.com",
                "telefone": "11966666666",
                "dataNascimento": "1997-04-12"
            }
            """;

        mockMvc.perform(post("/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(alunoOriginal))
                .andExpect(status().isOk());

        Long id = alunoRepository.findAll().get(0).getId();

        String alunoAtualizado = """
            {
                "nomeCompleto": "Joao Silva Atualizado",
                "cpf": "22233344455",
                "email": "joao.novo@email.com",
                "telefone": "11955555555",
                "dataNascimento": "1997-04-12"
            }
            """;

        mockMvc.perform(put("/alunos/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(alunoAtualizado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeCompleto")
                        .value("Joao Silva Atualizado"))
                .andExpect(jsonPath("$.email")
                        .value("joao.novo@email.com"));
    }

    @Test
    void deveExcluirAluno() throws Exception {

        String alunoJson = """
            {
                "nomeCompleto": "Pedro Santos",
                "cpf": "33344455566",
                "email": "pedro@email.com",
                "telefone": "11944444444",
                "dataNascimento": "1996-06-20"
            }
            """;

        mockMvc.perform(post("/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(alunoJson))
                .andExpect(status().isOk());

        Long id = alunoRepository.findAll().get(0).getId();

        mockMvc.perform(delete("/alunos/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/alunos/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void naoDeveCadastrarAlunoComCpfDuplicado() throws Exception {

        String primeiroAluno = """
        {
            "nomeCompleto": "Maria Silva",
            "cpf": "12345678900",
            "email": "maria@email.com",
            "telefone": "11999999999",
            "dataNascimento": "2000-05-15"
        }
        """;

        String segundoAluno = """
        {
            "nomeCompleto": "Joao Silva",
            "cpf": "12345678900",
            "email": "joao@email.com",
            "telefone": "11888888888",
            "dataNascimento": "1999-04-10"
        }
        """;

        mockMvc.perform(post("/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(primeiroAluno))
                .andExpect(status().isOk());

        mockMvc.perform(post("/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(segundoAluno))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.erro")
                        .value("CPF ou e-mail já cadastrado."));
    }

    @Test
    void naoDeveCadastrarAlunoComEmailDuplicado() throws Exception {

        String primeiroAluno = """
        {
            "nomeCompleto": "Maria Silva",
            "cpf": "12345678900",
            "email": "maria@email.com",
            "telefone": "11999999999",
            "dataNascimento": "2000-05-15"
        }
        """;

        String segundoAluno = """
        {
            "nomeCompleto": "Joao Silva",
            "cpf": "98765432100",
            "email": "maria@email.com",
            "telefone": "11888888888",
            "dataNascimento": "1999-04-10"
        }
        """;

        mockMvc.perform(post("/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(primeiroAluno))
                .andExpect(status().isOk());

        mockMvc.perform(post("/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(segundoAluno))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.erro")
                        .value("CPF ou e-mail já cadastrado."));
    }

    @Test
    void deveAtualizarAlunoMantendoCpfEEmail() throws Exception {

        String alunoOriginal = """
        {
            "nomeCompleto": "Maria Silva",
            "cpf": "12345678900",
            "email": "maria@email.com",
            "telefone": "11999999999",
            "dataNascimento": "2000-05-15"
        }
        """;

        mockMvc.perform(post("/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(alunoOriginal))
                .andExpect(status().isOk());

        Long id = alunoRepository.findAll().get(0).getId();

        String alunoAtualizado = """
        {
            "nomeCompleto": "Maria Silva Atualizada",
            "cpf": "12345678900",
            "email": "maria@email.com",
            "telefone": "11777777777",
            "dataNascimento": "2000-05-15"
        }
        """;

        mockMvc.perform(put("/alunos/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(alunoAtualizado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeCompleto")
                        .value("Maria Silva Atualizada"))
                .andExpect(jsonPath("$.cpf")
                        .value("12345678900"))
                .andExpect(jsonPath("$.email")
                        .value("maria@email.com"));
    }


}
