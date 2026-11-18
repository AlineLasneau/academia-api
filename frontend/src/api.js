const BASE = '/api';

const MENSAGEM_SEM_CONEXAO =
  'Não foi possível conectar à API. Verifique se o backend está rodando na porta 8080.';

export class ApiError extends Error {
  constructor(status, corpo) {
    super(corpo?.erro ?? mensagemPadrao(status));
    this.status = status;
    // Erros de validação (400) chegam como { campo: mensagem }.
    this.campos = status === 400 && corpo && !corpo.erro ? corpo : {};
  }
}

function mensagemPadrao(status) {
  if (status === 400) return 'Verifique os campos destacados.';
  if (status === 404) return 'Registro não encontrado.';
  if (status >= 500) return MENSAGEM_SEM_CONEXAO;
  return `Erro inesperado (${status}).`;
}

export async function request(caminho, { method = 'GET', body } = {}) {
  let resposta;
  try {
    resposta = await fetch(BASE + caminho, {
      method,
      headers: body ? { 'Content-Type': 'application/json' } : undefined,
      body: body ? JSON.stringify(body) : undefined,
    });
  } catch {
    throw new ApiError(0, { erro: MENSAGEM_SEM_CONEXAO });
  }

  if (resposta.status === 204) return null;

  const texto = await resposta.text();
  let dados = null;
  try {
    dados = texto ? JSON.parse(texto) : null;
  } catch {
    dados = null;
  }

  if (!resposta.ok) throw new ApiError(resposta.status, dados);
  return dados;
}
