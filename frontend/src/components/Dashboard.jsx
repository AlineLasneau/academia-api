import { useCallback, useEffect, useState } from 'react';
import { request } from '../api.js';
import * as fmt from '../formatos.js';
import {
  IconeAlerta,
  IconeAlunos,
  IconeInicio,
  IconeMensalidades,
  IconePlanos,
  IconeProfessores,
} from './Icones.jsx';
import { Pessoa, SeloStatus } from './Ui.jsx';

const soma = (lista) => lista.reduce((t, m) => t + Number(m.valor ?? 0), 0);

function Indicador({ icone: Icone, cor, rotulo, valor, detalhe, aoClicar }) {
  return (
    <button type="button" className={`indicador indicador-${cor}`} onClick={aoClicar}>
      <span className="indicador-icone"><Icone tamanho={20} /></span>
      <span className="indicador-rotulo">{rotulo}</span>
      <span className="indicador-valor">{valor}</span>
      {detalhe && <span className="indicador-detalhe">{detalhe}</span>}
    </button>
  );
}

export default function Dashboard({ irPara }) {
  const [dados, setDados] = useState(null);
  const [erro, setErro] = useState(null);

  const carregar = useCallback(async () => {
    setErro(null);
    try {
      const [alunos, professores, planos, mensalidades] = await Promise.all([
        request('/alunos'),
        request('/professores'),
        request('/planos'),
        request('/mensalidades'),
      ]);
      setDados({ alunos, professores, planos, mensalidades });
    } catch (e) {
      setErro(e.message);
    }
  }, []);

  useEffect(() => {
    carregar();
  }, [carregar]);

  const cabecalho = (
    <header className="cabecalho-pagina">
      <div className="cabecalho-titulo">
        <span className="cabecalho-icone"><IconeInicio tamanho={22} /></span>
        <div>
          <h1>Visão geral</h1>
          <p className="subtitulo">Resumo da academia hoje, {fmt.data(fmt.hojeISO())}.</p>
        </div>
      </div>
    </header>
  );

  if (erro) {
    return (
      <section>
        {cabecalho}
        <div className="cartao estado estado-erro">
          <IconeAlerta tamanho={28} />
          <p>{erro}</p>
          <button type="button" className="btn" onClick={carregar}>Tentar novamente</button>
        </div>
      </section>
    );
  }

  if (!dados) {
    return (
      <section>
        {cabecalho}
        <div className="grade-indicadores">
          {[0, 1, 2, 3].map((i) => <div key={i} className="indicador skeleton-bloco" />)}
        </div>
      </section>
    );
  }

  const { alunos, professores, planos, mensalidades } = dados;
  const nomeAluno = Object.fromEntries(alunos.map((a) => [a.id, a.nomeCompleto]));
  const hoje = fmt.hojeISO();

  const pagas = mensalidades.filter((m) => m.status === 'PAGO');
  const pendentes = mensalidades.filter((m) => m.status === 'PENDENTE');
  const atrasadas = mensalidades.filter((m) => m.status === 'ATRASADO');
  const emAberto = [...pendentes, ...atrasadas];

  const proximos = [...emAberto]
    .sort((a, b) => a.dataVencimento.localeCompare(b.dataVencimento))
    .slice(0, 6);

  const recentes = [...alunos]
    .sort((a, b) => (b.dataMatricula ?? '').localeCompare(a.dataMatricula ?? '') || b.id - a.id)
    .slice(0, 5);

  const total = mensalidades.length || 1;
  const fatias = [
    { rotulo: 'Pagas', qtd: pagas.length, valor: soma(pagas), cor: 'verde' },
    { rotulo: 'Pendentes', qtd: pendentes.length, valor: soma(pendentes), cor: 'ambar' },
    { rotulo: 'Atrasadas', qtd: atrasadas.length, valor: soma(atrasadas), cor: 'vermelho' },
  ];

  const ativos = (lista) => lista.filter((x) => x.ativo).length;

  return (
    <section>
      {cabecalho}

      <div className="grade-indicadores">
        <Indicador
          icone={IconeAlunos}
          cor="azul"
          rotulo="Alunos ativos"
          valor={ativos(alunos)}
          detalhe={`${alunos.length} cadastrados`}
          aoClicar={() => irPara('alunos')}
        />
        <Indicador
          icone={IconeProfessores}
          cor="roxo"
          rotulo="Professores"
          valor={ativos(professores)}
          detalhe={`${professores.length} cadastrados`}
          aoClicar={() => irPara('professores')}
        />
        <Indicador
          icone={IconePlanos}
          cor="ciano"
          rotulo="Planos ativos"
          valor={ativos(planos)}
          detalhe={`${planos.length} cadastrados`}
          aoClicar={() => irPara('planos')}
        />
        <Indicador
          icone={IconeMensalidades}
          cor={atrasadas.length ? 'vermelho' : 'verde'}
          rotulo="A receber"
          valor={fmt.moeda(soma(emAberto))}
          detalhe={
            atrasadas.length
              ? `${atrasadas.length} ${atrasadas.length === 1 ? 'atrasada' : 'atrasadas'}`
              : 'Nenhuma atrasada'
          }
          aoClicar={() => irPara('mensalidades')}
        />
      </div>

      <div className="grade-paineis">
        <div className="cartao painel">
          <div className="painel-topo">
            <h2>Próximos vencimentos</h2>
            <button type="button" className="link" onClick={() => irPara('mensalidades')}>Ver todas</button>
          </div>
          {proximos.length === 0 ? (
            <p className="painel-vazio">Nenhuma mensalidade em aberto.</p>
          ) : (
            <ul className="lista">
              {proximos.map((m) => {
                const vencida = m.dataVencimento < hoje;
                return (
                  <li key={m.id} className="lista-item">
                    <Pessoa nome={nomeAluno[m.alunoId] ?? `Aluno #${m.alunoId}`} />
                    <span className={`lista-data ${vencida ? 'texto-perigo' : 'texto-suave'}`}>
                      {vencida ? 'Venceu ' : 'Vence '}{fmt.data(m.dataVencimento)}
                    </span>
                    <span className="numero lista-valor">{fmt.moeda(m.valor)}</span>
                    <SeloStatus status={m.status} />
                  </li>
                );
              })}
            </ul>
          )}
        </div>

        <div className="coluna-paineis">
          <div className="cartao painel">
            <div className="painel-topo">
              <h2>Mensalidades</h2>
              <span className="texto-suave">{mensalidades.length} no total</span>
            </div>
            <div className="barra-segmentada" role="img" aria-label="Distribuição das mensalidades por situação">
              {fatias.map((f) =>
                f.qtd > 0 ? (
                  <span
                    key={f.rotulo}
                    className={`segmento segmento-${f.cor}`}
                    style={{ width: `${(f.qtd / total) * 100}%` }}
                    title={`${f.rotulo}: ${f.qtd}`}
                  />
                ) : null,
              )}
            </div>
            <ul className="legenda">
              {fatias.map((f) => (
                <li key={f.rotulo}>
                  <span className={`legenda-ponto segmento-${f.cor}`} />
                  <span>{f.rotulo}</span>
                  <strong>{f.qtd}</strong>
                  <span className="numero texto-suave">{fmt.moeda(f.valor)}</span>
                </li>
              ))}
            </ul>
          </div>

          <div className="cartao painel">
            <div className="painel-topo">
              <h2>Matrículas recentes</h2>
              <button type="button" className="link" onClick={() => irPara('alunos')}>Ver alunos</button>
            </div>
            {recentes.length === 0 ? (
              <p className="painel-vazio">Nenhum aluno cadastrado.</p>
            ) : (
              <ul className="lista">
                {recentes.map((a) => (
                  <li key={a.id} className="lista-item">
                    <Pessoa nome={a.nomeCompleto} />
                    <span className="texto-suave lista-data">{fmt.data(a.dataMatricula)}</span>
                  </li>
                ))}
              </ul>
            )}
          </div>
        </div>
      </div>
    </section>
  );
}
