// Ícones em SVG inline (traço no estilo Lucide), sem dependências externas.

function Icone({ children, tamanho = 18, ...props }) {
  return (
    <svg
      width={tamanho}
      height={tamanho}
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
      aria-hidden="true"
      {...props}
    >
      {children}
    </svg>
  );
}

export const IconeInicio = (p) => (
  <Icone {...p}>
    <rect x="3" y="3" width="7" height="9" rx="1" />
    <rect x="14" y="3" width="7" height="5" rx="1" />
    <rect x="14" y="12" width="7" height="9" rx="1" />
    <rect x="3" y="16" width="7" height="5" rx="1" />
  </Icone>
);

export const IconeAlunos = (p) => (
  <Icone {...p}>
    <path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2" />
    <circle cx="9" cy="7" r="4" />
    <path d="M22 21v-2a4 4 0 0 0-3-3.87" />
    <path d="M16 3.13a4 4 0 0 1 0 7.75" />
  </Icone>
);

export const IconePlanos = (p) => (
  <Icone {...p}>
    <rect x="2" y="5" width="20" height="14" rx="2" />
    <path d="M2 10h20" />
  </Icone>
);

export const IconeMensalidades = (p) => (
  <Icone {...p}>
    <path d="M4 2v20l2-1 2 1 2-1 2 1 2-1 2 1 2-1 2 1V2l-2 1-2-1-2 1-2-1-2 1-2-1-2 1Z" />
    <path d="M16 8h-6a2 2 0 1 0 0 4h4a2 2 0 1 1 0 4H8" />
    <path d="M12 17.5v-11" />
  </Icone>
);

export const IconeProfessores = (p) => (
  <Icone {...p}>
    <path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2" />
    <circle cx="9" cy="7" r="4" />
    <path d="m16 11 2 2 4-4" />
  </Icone>
);

export const IconeTreinos = (p) => (
  <Icone {...p}>
    <path d="M6.5 6.5h11" />
    <path d="M6.5 17.5h11" />
    <path d="M12 6.5v11" />
    <rect x="2" y="4" width="4.5" height="5" rx="1" />
    <rect x="17.5" y="4" width="4.5" height="5" rx="1" />
    <rect x="2" y="15" width="4.5" height="5" rx="1" />
    <rect x="17.5" y="15" width="4.5" height="5" rx="1" />
  </Icone>
);

export const IconeAvaliacoes = (p) => (
  <Icone {...p}>
    <path d="M22 12h-4l-3 9L9 3l-3 9H2" />
  </Icone>
);

export const IconeMais = (p) => (
  <Icone {...p}>
    <path d="M12 5v14" />
    <path d="M5 12h14" />
  </Icone>
);

export const IconeEditar = (p) => (
  <Icone {...p}>
    <path d="M12 20h9" />
    <path d="M16.5 3.5a2.12 2.12 0 0 1 3 3L7 19l-4 1 1-4Z" />
  </Icone>
);

export const IconeExcluir = (p) => (
  <Icone {...p}>
    <path d="M3 6h18" />
    <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6" />
    <path d="M8 6V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2" />
  </Icone>
);

export const IconeBusca = (p) => (
  <Icone {...p}>
    <circle cx="11" cy="11" r="8" />
    <path d="m21 21-4.3-4.3" />
  </Icone>
);

export const IconeFechar = (p) => (
  <Icone {...p}>
    <path d="M18 6 6 18" />
    <path d="m6 6 12 12" />
  </Icone>
);

export const IconeAlerta = (p) => (
  <Icone {...p}>
    <circle cx="12" cy="12" r="10" />
    <path d="M12 8v4" />
    <path d="M12 16h.01" />
  </Icone>
);

export const IconeOk = (p) => (
  <Icone {...p}>
    <circle cx="12" cy="12" r="10" />
    <path d="m9 12 2 2 4-4" />
  </Icone>
);
