import { useCallback, useState } from 'react';
import CrudPage from './components/CrudPage.jsx';
import Dashboard from './components/Dashboard.jsx';
import { IconeInicio } from './components/Icones.jsx';
import { Toast } from './components/Ui.jsx';
import { recursos } from './recursos.js';

const INICIO = { id: 'inicio', titulo: 'Visão geral', icone: IconeInicio };
const SECOES = [INICIO, ...recursos];

export default function App() {
  const [ativoId, setAtivoId] = useState(INICIO.id);
  const [aviso, setAviso] = useState(null);

  const notificar = useCallback((texto, tipo = 'sucesso') => {
    setAviso({ texto, tipo, chave: Date.now() });
  }, []);
  const fecharAviso = useCallback(() => setAviso(null), []);

  const recurso = recursos.find((r) => r.id === ativoId);

  return (
    <div className="layout">
      <aside className="lateral">
        <div className="marca">
          <span className="marca-logo" aria-hidden="true">A</span>
          <span className="marca-nome">Academia</span>
        </div>
        <nav aria-label="Seções" className="menu">
          {SECOES.map((s) => {
            const Icone = s.icone;
            const ativo = s.id === ativoId;
            return (
              <button
                key={s.id}
                type="button"
                className={`menu-item ${ativo ? 'menu-item-ativo' : ''}`}
                aria-current={ativo ? 'page' : undefined}
                onClick={() => setAtivoId(s.id)}
              >
                <Icone tamanho={18} />
                <span>{s.titulo}</span>
              </button>
            );
          })}
        </nav>
      </aside>

      <main className="conteudo">
        {/* key força a remontagem ao trocar de seção, limpando o estado. */}
        {recurso ? (
          <CrudPage key={recurso.id} recurso={recurso} notificar={notificar} />
        ) : (
          <Dashboard irPara={setAtivoId} />
        )}
      </main>

      <Toast key={aviso?.chave} aviso={aviso} aoFechar={fecharAviso} />
    </div>
  );
}
