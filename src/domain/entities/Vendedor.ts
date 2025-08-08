export enum TipoContratacao {
  OUTSOURCING = 'Outsourcing',
  CLT = 'CLT',
  PESSOA_JURIDICA = 'Pessoa Jurídica'
}

export enum StatusProcessamento {
  PENDENTE = 'PENDENTE',
  PROCESSANDO = 'PROCESSANDO',
  CONCLUIDO = 'CONCLUIDO',
  ERRO = 'ERRO'
}

export interface Filial {
  id: string;
  nome: string;
  cnpj: string;
  cidade: string;
  uf: string;
  tipo: string;
  ativo: boolean;
  dataCadastro: Date;
  ultimaAtualizacao: Date;
}

export interface Vendedor {
  id: string;
  matricula: string;
  nome: string;
  dataNascimento?: Date;
  documento: string;
  email: string;
  tipoContratacao: TipoContratacao;
  filialId: string;
  filial?: Filial;
  createdAt: Date;
  updatedAt: Date;
}

export interface ProcessamentoVendedor {
  id: string;
  vendedorId?: string;
  status: StatusProcessamento;
  dadosOriginais: any;
  erro?: string;
  createdAt: Date;
  updatedAt: Date;
}
