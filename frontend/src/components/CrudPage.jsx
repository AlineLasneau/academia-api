import { useCallback, useEffect, useMemo, useState } from 'react';
import { ApiError, request } from '../api.js';
import * as fmt from '../formatos.js';
import { IconeAlerta, IconeBusca, IconeEditar, IconeExcluir, IconeMais } from './Icones.jsx';
import { Confirmacao, Modal, Pessoa, SeloAtivo, SeloStatus } from './Ui.jsx';

const FONTE_ALUNOS = { caminho: '/alunos', rotulo: 'nomeCompleto' };

function valoresIniciais(campos, item) {
  return Object.fromEntries(
    campos.map((c) => [c.nome, item ? String(item[c.nome] ?? '') : c.padrao ?? '']),
  );
}

function montarPayload(campos, valores) {
  return Object.fromEntries(
    campos.map((c) => {
      const v = valores[c.nome];
      if (v === '' || v == null) return [c.nome, null];
      if (c.tipo === 'number' || c.fonte) return [c.nome, Number(v)];
      return [c.nome, v];
    }),
  );
}

export default function CrudPage({ recurso, notificar }) {
  const [itens, setItens] = useState([]);
  const [carregando, setCarregando] = useState(true);
  const [erroLista, setErroLista] = useState(null);
  const [opcoes, setOpcoes] = useState({});
  const [filtroAluno, setFiltroAluno] = useState('');
  const [busca, setBusca] = useState('');

  const [formAberto, setFormAberto] = useState(false);
  const [editandoId, setEditandoId] = useState(null);
  const [valores, setValores] = useState({});
  const [errosCampos, setErrosCampos] = useState({});
  const [erroForm, setErroForm] = useState(null);
  const [salvando, setSalvando] = useState(false);

  const [paraExcluir, setParaExcluir] = useState(null);
  const [excluindo, setExcluindo] = useState(false);

  // Carrega as listas usadas nos selects (alunos, planos, professores).
  const carregarOpcoes = useCallback(async () => {
    const fontes = recurso.campos.filter((c) => c.fonte);
    if (recurso.filtroPorAluno && !fontes.some((c) => c.nome === 'alunoId')) {
      fontes.push({ nome: 'alunoId', fonte: FONTE_ALUNOS });
    }
    const resultado = {};
    await Promise.all(
      fontes.map(async (c) => {
        const lista = await request(c.fonte.caminho);
        resultado[c.nome] = lista.map((x) => ({ valor: String(x.id), rotulo: x[c.fonte.rotulo] }));
      }),
    );
    setOpcoes(resultado);
  }, [recurso]);

  const carregarItens = useCallback(async () => {
    setCarregando(true);
    setErroLista(null);
    try {
      const caminho = filtroAluno ? `${recurso.caminho}/aluno/${filtroAluno}` : recurso.caminho;
      setItens(await request(caminho));
    } catch (e) {
      setErroLista(e.message);
    } finally {
      setCarregando(false);
    }
  }, [recurso.caminho, filtroAluno]);

  useEffect(() => {
    carregarOpcoes().catch((e) => setErroLista(e.message));
  }, [carregarOpcoes]);

  useEffect(() => {
    carregarItens();
  }, [carregarItens]);

  function rotuloReferencia(campo, id) {
    return opcoes[campo]?.find((o) => o.valor === String(id))?.rotulo ?? `#${id}`;
  }

  // Texto puro de cada célula: usado na busca.
  function textoCelula(coluna, item) {
    const v = item[coluna.campo];
    if (coluna.formato?.startsWith('referencia')) return rotuloReferencia(coluna.campo, v);
    if (coluna.formato === 'status') return fmt.ROTULO_STATUS[v] ?? v;
    if (coluna.formato === 'data') return fmt.data(v);
    if (coluna.formato === 'ativo') return v ? 'ativo' : 'inativo';
    return v ?? '';
  }

  const itensVisiveis = useMemo(() => {
    const termo = fmt.normalizar(busca.trim());
    if (!termo) return itens;
    return itens.filter((item) =>
      recurso.colunas.some((c) => fmt.normalizar(textoCelula(c, item)).includes(termo)),
    );
    // textoCelula usa opcoes, por isso ela está nas dependências.
  }, [itens, busca, opcoes, recurso.colunas]);

  function celula(coluna, item) {
    const v = item[coluna.campo];
    switch (coluna.formato) {
      case 'pessoa':
        return <Pessoa nome={v} />;
      case 'referencia-pessoa':
        return <Pessoa nome={rotuloReferencia(coluna.campo, v)} />;
      case 'referencia':
        return rotuloReferencia(coluna.campo, v);
      case 'data':
        return <span className="texto-suave">{fmt.data(v)}</span>;
      case 'moeda':
        return <span className="numero">{fmt.moeda(v)}</span>;
      case 'numero':
        return <span className="numero">{fmt.numero(v)}</span>;
      case 'ativo':
        return <SeloAtivo ativo={v} />;
      case 'status':
        return <SeloStatus status={v} />;
      case 'longo':
        return v ? <span className="texto-longo" title={v}>{v}</span> : <span className="texto-suave">—</span>;
      default:
        return v == null || v === '' ? <span className="texto-suave">—</span> : String(v);
    }
  }

  function abrirFormulario(item = null) {
    setEditandoId(item?.id ?? null);
    setValores(valoresIniciais(recurso.campos, item));
    setErrosCampos({});
    setErroForm(null);
    setFormAberto(true);
  }

  function fecharFormulario() {
    if (!salvando) setFormAberto(false);
  }

  async function salvar(evento) {
    evento.preventDefault();
    setSalvando(true);
    setErrosCampos({});
    setErroForm(null);
    try {
      const body = montarPayload(recurso.campos, valores);
      if (editandoId) {
        await request(`${recurso.caminho}/${editandoId}`, { method: 'PUT', body });
      } else {
        await request(recurso.caminho, { method: 'POST', body });
      }
      setFormAberto(false);
      notificar(editandoId ? 'Alterações salvas.' : 'Cadastro realizado.');
      await carregarItens();
    } catch (e) {
      if (e instanceof ApiError && Object.keys(e.campos).length > 0) {
        setErrosCampos(e.campos);
        setErroForm('Verifique os campos destacados.');
      } else {
        setErroForm(e.message);
      }
    } finally {
      setSalvando(false);
    }
  }

  async function confirmarExclusao() {
    setExcluindo(true);
    try {
      await request(`${recurso.caminho}/${paraExcluir.id}`, { method: 'DELETE' });
      notificar('Registro excluído.');
      setParaExcluir(null);
      await carregarItens();
    } catch (e) {
      setParaExcluir(null);
      notificar(e.message, 'erro');
    } finally {
      setExcluindo(false);
    }
  }

  const Icone = recurso.icone;
  const total = itens.length;

  return (
    <section>
      <header className="cabecalho-pagina">
        <div className="cabecalho-titulo">
          <span className="cabecalho-icone"><Icone tamanho={22} /></span>
          <div>
            <h1>
              {recurso.titulo}
              {!carregando && !erroLista && <span className="contador">{total}</span>}
            </h1>
            <p className="subtitulo">{recurso.descricao}</p>
          </div>
        </div>
        <button type="button" className="btn btn-primario" onClick={() => abrirFormulario()}>
          <IconeMais tamanho={16} /> Novo
        </button>
      </header>

      <div className="cartao">
        <div className="barra-ferramentas">
          <label className="busca">
            <IconeBusca tamanho={16} />
            <input
              type="search"
              placeholder="Buscar…"
              value={busca}
              onChange={(e) => setBusca(e.target.value)}
              aria-label={`Buscar em ${recurso.titulo}`}
            />
          </label>
          {recurso.filtroPorAluno && (
            <select
              className="select-filtro"
              value={filtroAluno}
              onChange={(e) => setFiltroAluno(e.target.value)}
              aria-label="Filtrar por aluno"
            >
              <option value="">Todos os alunos</option>
              {(opcoes.alunoId ?? []).map((o) => (
                <option key={o.valor} value={o.valor}>{o.rotulo}</option>
              ))}
            </select>
          )}
        </div>

        {erroLista ? (
          <div className="estado estado-erro">
            <IconeAlerta tamanho={28} />
            <p>{erroLista}</p>
            <button type="button" className="btn" onClick={carregarItens}>Tentar novamente</button>
          </div>
        ) : carregando ? (
          <div className="skeleton-lista" aria-label="Carregando">
            {[0, 1, 2, 3].map((i) => <div key={i} className="skeleton-linha" />)}
          </div>
        ) : itens.length === 0 ? (
          <div className="estado">
            <span className="estado-icone"><Icone tamanho={28} /></span>
            <p className="estado-titulo">Nenhum registro por aqui ainda</p>
            <p>Comece cadastrando o primeiro.</p>
            <button type="button" className="btn btn-primario" onClick={() => abrirFormulario()}>
              <IconeMais tamanho={16} /> Cadastrar
            </button>
          </div>
        ) : itensVisiveis.length === 0 ? (
          <div className="estado">
            <p className="estado-titulo">Nada encontrado para “{busca}”</p>
            <button type="button" className="btn" onClick={() => setBusca('')}>Limpar busca</button>
          </div>
        ) : (
          <div className="tabela-container">
            <table>
              <thead>
                <tr>
                  {recurso.colunas.map((c) => <th key={c.campo}>{c.rotulo}</th>)}
                  <th className="col-acoes"><span className="sr-only">Ações</span></th>
                </tr>
              </thead>
              <tbody>
                {itensVisiveis.map((item) => (
                  <tr key={item.id}>
                    {recurso.colunas.map((c) => <td key={c.campo}>{celula(c, item)}</td>)}
                    <td className="col-acoes">
                      <div className="acoes">
                        <button
                          type="button"
                          className="btn-icone"
                          onClick={() => abrirFormulario(item)}
                          aria-label="Editar"
                          title="Editar"
                        >
                          <IconeEditar tamanho={16} />
                        </button>
                        <button
                          type="button"
                          className="btn-icone btn-icone-perigo"
                          onClick={() => setParaExcluir(item)}
                          aria-label="Excluir"
                          title="Excluir"
                        >
                          <IconeExcluir tamanho={16} />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {formAberto && (
        <Modal
          titulo={editandoId ? `Editar ${recurso.singular}` : `Novo cadastro`}
          subtitulo={recurso.titulo}
          aoFechar={fecharFormulario}
        >
          <form noValidate onSubmit={salvar}>
            {erroForm && (
              <div className="alerta-erro" role="alert">
                <IconeAlerta tamanho={16} /> {erroForm}
              </div>
            )}
            <div className="grade-campos">
              {recurso.campos.map((campo, i) => (
                <Campo
                  key={campo.nome}
                  campo={campo}
                  valor={valores[campo.nome]}
                  erro={errosCampos[campo.nome]}
                  opcoes={campo.opcoes ?? opcoes[campo.nome] ?? []}
                  autoFocus={i === 0}
                  onChange={(v) => setValores((atual) => ({ ...atual, [campo.nome]: v }))}
                />
              ))}
            </div>
            <div className="modal-acoes">
              <button type="button" className="btn" onClick={fecharFormulario} disabled={salvando}>
                Cancelar
              </button>
              <button type="submit" className="btn btn-primario" disabled={salvando}>
                {salvando ? 'Salvando…' : 'Salvar'}
              </button>
            </div>
          </form>
        </Modal>
      )}

      {paraExcluir && (
        <Confirmacao
          titulo="Excluir registro?"
          mensagem="Esta ação não pode ser desfeita."
          rotuloConfirmar="Excluir"
          ocupado={excluindo}
          aoConfirmar={confirmarExclusao}
          aoCancelar={() => setParaExcluir(null)}
        />
      )}
    </section>
  );
}

function Campo({ campo, valor, erro, opcoes, autoFocus, onChange }) {
  const id = `campo-${campo.nome}`;
  const comum = {
    id,
    value: valor ?? '',
    autoFocus,
    'aria-invalid': Boolean(erro),
    'aria-describedby': erro ? `${id}-erro` : undefined,
    onChange: (e) => onChange(e.target.value),
  };

  let controle;
  if (campo.tipo === 'select') {
    controle = (
      <select {...comum}>
        {!campo.padrao && <option value="">Selecione…</option>}
        {opcoes.map((o) => <option key={o.valor} value={o.valor}>{o.rotulo}</option>)}
      </select>
    );
  } else if (campo.tipo === 'textarea') {
    controle = <textarea rows={3} {...comum} />;
  } else {
    controle = <input type={campo.tipo ?? 'text'} min={campo.min} step={campo.passo} {...comum} />;
  }

  return (
    <div className={`campo ${campo.largo ? 'campo-largo' : ''} ${erro ? 'campo-com-erro' : ''}`}>
      <label htmlFor={id}>
        {campo.rotulo}
        {campo.obrigatorio && <span className="obrigatorio" aria-hidden="true"> *</span>}
      </label>
      {controle}
      {erro && <small id={`${id}-erro`} className="mensagem-erro">{erro}</small>}
    </div>
  );
}
