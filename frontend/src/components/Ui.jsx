import { useEffect, useRef } from 'react';
import { corDoNome, iniciais, ROTULO_STATUS } from '../formatos.js';
import { IconeAlerta, IconeFechar, IconeOk } from './Icones.jsx';

export function Avatar({ nome, tamanho = 32 }) {
  return (
    <span
      className="avatar"
      style={{ '--matiz': corDoNome(nome), width: tamanho, height: tamanho }}
      aria-hidden="true"
    >
      {iniciais(nome)}
    </span>
  );
}

export function Pessoa({ nome }) {
  return (
    <span className="pessoa">
      <Avatar nome={nome} />
      <span className="pessoa-nome">{nome}</span>
    </span>
  );
}

export function SeloAtivo({ ativo }) {
  return (
    <span className={`selo ${ativo ? 'selo-verde' : 'selo-cinza'}`}>
      <span className="selo-ponto" />
      {ativo ? 'Ativo' : 'Inativo'}
    </span>
  );
}

const COR_STATUS = { PAGO: 'verde', PENDENTE: 'ambar', ATRASADO: 'vermelho' };

export function SeloStatus({ status }) {
  return (
    <span className={`selo selo-${COR_STATUS[status] ?? 'cinza'}`}>
      <span className="selo-ponto" />
      {ROTULO_STATUS[status] ?? status}
    </span>
  );
}

export function Modal({ titulo, subtitulo, aoFechar, children, largura = 560 }) {
  // Guarda a função mais recente sem reinstalar o listener a cada render.
  const aoFecharRef = useRef(aoFechar);
  aoFecharRef.current = aoFechar;

  useEffect(() => {
    const aoTeclar = (e) => e.key === 'Escape' && aoFecharRef.current();
    document.addEventListener('keydown', aoTeclar);
    document.body.classList.add('sem-rolagem');
    return () => {
      document.removeEventListener('keydown', aoTeclar);
      document.body.classList.remove('sem-rolagem');
    };
  }, []);

  return (
    <div className="modal-fundo" onMouseDown={aoFechar}>
      <div
        className="modal"
        role="dialog"
        aria-modal="true"
        aria-labelledby="modal-titulo"
        style={{ maxWidth: largura }}
        onMouseDown={(e) => e.stopPropagation()}
      >
        <div className="modal-topo">
          <div>
            <h2 id="modal-titulo">{titulo}</h2>
            {subtitulo && <p className="modal-subtitulo">{subtitulo}</p>}
          </div>
          <button type="button" className="btn-icone" onClick={aoFechar} aria-label="Fechar">
            <IconeFechar />
          </button>
        </div>
        {children}
      </div>
    </div>
  );
}

export function Confirmacao({ titulo, mensagem, rotuloConfirmar, ocupado, aoConfirmar, aoCancelar }) {
  return (
    <Modal titulo={titulo} aoFechar={ocupado ? () => {} : aoCancelar} largura={420}>
      <p className="confirmacao-texto">{mensagem}</p>
      <div className="modal-acoes">
        <button type="button" className="btn" onClick={aoCancelar} disabled={ocupado}>
          Cancelar
        </button>
        <button type="button" className="btn btn-perigo-cheio" onClick={aoConfirmar} disabled={ocupado} autoFocus>
          {ocupado ? 'Excluindo…' : rotuloConfirmar}
        </button>
      </div>
    </Modal>
  );
}

export function Toast({ aviso, aoFechar }) {
  useEffect(() => {
    if (!aviso) return undefined;
    const t = setTimeout(aoFechar, 3500);
    return () => clearTimeout(t);
  }, [aviso, aoFechar]);

  if (!aviso) return null;
  const erro = aviso.tipo === 'erro';
  return (
    <div className={`toast ${erro ? 'toast-erro' : 'toast-sucesso'}`} role={erro ? 'alert' : 'status'}>
      {erro ? <IconeAlerta /> : <IconeOk />}
      <span>{aviso.texto}</span>
    </div>
  );
}
