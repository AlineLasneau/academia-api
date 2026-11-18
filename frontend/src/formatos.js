const moedaBRL = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' });
const numeroBR = new Intl.NumberFormat('pt-BR', { maximumFractionDigits: 2 });

export const ROTULO_STATUS = { PENDENTE: 'Pendente', PAGO: 'Pago', ATRASADO: 'Atrasado' };

export function moeda(valor) {
  return valor == null ? '—' : moedaBRL.format(valor);
}

export function numero(valor) {
  return valor == null ? '—' : numeroBR.format(valor);
}

// Datas chegam como "AAAA-MM-DD"; formatamos sem passar por Date para
// não sofrer com fuso horário.
export function data(iso) {
  if (!iso) return '—';
  const [ano, mes, dia] = iso.split('-');
  return `${dia}/${mes}/${ano}`;
}

export function hojeISO() {
  const d = new Date();
  const mes = String(d.getMonth() + 1).padStart(2, '0');
  const dia = String(d.getDate()).padStart(2, '0');
  return `${d.getFullYear()}-${mes}-${dia}`;
}

export function iniciais(nome = '') {
  const partes = nome.trim().split(/\s+/).filter(Boolean);
  if (partes.length === 0) return '?';
  const primeira = partes[0][0];
  const ultima = partes.length > 1 ? partes[partes.length - 1][0] : '';
  return (primeira + ultima).toUpperCase();
}

// Cor estável por nome, para o avatar não mudar de cor entre renderizações.
export function corDoNome(nome = '') {
  let hash = 0;
  for (const ch of nome) hash = (hash * 31 + ch.charCodeAt(0)) % 360;
  return hash;
}

export function normalizar(texto) {
  return String(texto ?? '')
    .normalize('NFD')
    .replace(/[̀-ͯ]/g, '')
    .toLowerCase();
}
