package projetofinal.ftec.modelo;

import java.util.Calendar;

import projetofinal.ftec.R;
import projetofinal.ftec.persistencia.AlarmeDAO;
import projetofinal.ftec.persistencia.CategoriaDAO;
import projetofinal.ftec.persistencia.ContaDAO;
import projetofinal.ftec.persistencia.ContatoDAO;
import projetofinal.ftec.persistencia.PrevistoDAO;
import projetofinal.ftec.persistencia.RealizadoDAO;
import projetofinal.ftec.persistencia.UsuarioDAO;
import projetofinal.ftec.utils.CustomDateUtils;
import android.content.ContentUris;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

public class Previsto {
	public static String[] colunas = new String[] { Previstos._ID, Previstos.DESCRICAO, Previstos.ALARME_ID, Previstos.CONTATO_ID,
		Previstos.CONTA_ID, Previstos.CATEGORIA_ID, Previstos.DT_EMISSAO, Previstos.DT_VENCIMENTO, Previstos.VAL_PREVISTO,
		Previstos.DT_ULT_PAGAMENTO, Previstos.TIPO_MOVIMENTO, Previstos.VAL_SALDO, Previstos.PAGAMENTO_AUTOMATICO, Previstos.USUARIO_ID};
	public static final String AUTHORITY = "projetofinal.ftec.modelo.provider.previsto";
	private long id;
	private Usuario usuario;
	private Alarme alarme;
	private Contato contato;
	private Conta conta;
	private Categoria categoria;	
	private String descricao;	
	private Calendar dt_emissao;
	private Calendar dt_vencimento;
	private Double val_previsto;
	private Calendar dt_ult_pagamento;
	private String tipo_movimento;
	private Double val_saldo;
	private String pagamento_automatico;	

	public Previsto() {
		dt_emissao = Calendar.getInstance();
		dt_ult_pagamento = Calendar.getInstance();
		dt_vencimento = Calendar.getInstance();
		val_previsto = 0.0;
		val_saldo = 0.0;
	}
	
	public Previsto( Usuario usuario, Alarme alarme, Contato contato,
			Conta conta, Categoria categoria, String descricao,
			Calendar dt_emissao, Calendar dt_vencimento, Double val_previsto,
			Calendar dt_ult_pagamento, String tipo_movimento, Double val_saldo,
			String pagamento_automatico) {
		super();
		this.usuario = usuario;
		this.alarme = alarme;
		this.contato = contato;
		this.conta = conta;
		this.categoria = categoria;
		this.descricao = descricao;
		this.dt_emissao = dt_emissao;
		this.dt_vencimento = dt_vencimento;
		this.val_previsto = val_previsto;
		this.dt_ult_pagamento = dt_ult_pagamento;
		this.tipo_movimento = tipo_movimento;
		this.val_saldo = val_saldo;
		this.pagamento_automatico = pagamento_automatico;
	}
	
	public Previsto(long id, Usuario usuario, Alarme alarme, Contato contato,
			Conta conta, Categoria categoria, String descricao,
			Calendar dt_emissao, Calendar dt_vencimento, Double val_previsto,
			Calendar dt_ult_pagamento, String tipo_movimento, Double val_saldo,
			String pagamento_automatico) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.alarme = alarme;
		this.contato = contato;
		this.conta = conta;
		this.categoria = categoria;
		this.descricao = descricao;
		this.dt_emissao = dt_emissao;
		this.dt_vencimento = dt_vencimento;
		this.val_previsto = val_previsto;
		this.dt_ult_pagamento = dt_ult_pagamento;
		this.tipo_movimento = tipo_movimento;
		this.val_saldo = val_saldo;
		this.pagamento_automatico = pagamento_automatico;
	}
	public static final class Previstos implements BaseColumns {
		private Previstos() {			
		}
		
		public static final Uri CONTENT_URI = Uri.parse("content:// " + AUTHORITY + "/previstos");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.previstos";
		public static final String DEFAULT_SORT_ORDER = "previsto._id ASC";
		public static final String DESCRICAO = "descricao";
		public static final String ALARME_ID = "alarme_id";
		public static final String USUARIO_ID = "usuario_id";
		public static final String CONTATO_ID = "contato_id";
		public static final String CONTA_ID = "conta_id";
		public static final String CATEGORIA_ID = "categoria_id";
		public static final String DT_EMISSAO = "dt_emissao";
		public static final String DT_VENCIMENTO = "dt_vencimento";
		public static final String VAL_PREVISTO = "val_previsto";
		public static final String DT_ULT_PAGAMENTO = "dt_ult_pagamento";
		public static final String TIPO_MOVIMENTO = "tipo_movimento";
		public static final String VAL_SALDO = "val_saldo";
		public static final String PAGAMENTO_AUTOMATICO = "pagamento_automatico";
		//NOMES UTILIZADOS NOS SELECTS
		public static final String PREVISTO_ID = PrevistoDAO.getNomeTabela() + "._id";
		public static final String PREVISTO_DESCRICAO = PrevistoDAO.getNomeTabela() + ".descricao";
		public static final String PREVISTO_ALARME_ID = PrevistoDAO.getNomeTabela() + ".alarme_id";
		public static final String PREVISTO_USUARIO_ID = PrevistoDAO.getNomeTabela() + ".usuario_id";
		public static final String PREVISTO_CONTATO_ID = PrevistoDAO.getNomeTabela() + ".contato_id";
		public static final String PREVISTO_CONTA_ID = PrevistoDAO.getNomeTabela() + ".conta_id";
		public static final String PREVISTO_CATEGORIA_ID = PrevistoDAO.getNomeTabela() + ".categoria_id";
		public static final String PREVISTO_DT_EMISSAO = PrevistoDAO.getNomeTabela() + ".dt_emissao";
		public static final String PREVISTO_DT_VENCIMENTO = PrevistoDAO.getNomeTabela() + ".dt_vencimento";
		public static final String PREVISTO_VAL_PREVISTO = PrevistoDAO.getNomeTabela() + ".val_previsto";
		public static final String PREVISTO_DT_ULT_PAGAMENTO = PrevistoDAO.getNomeTabela() + ".dt_ult_pagamento";
		public static final String PREVISTO_TIPO_MOVIMENTO = PrevistoDAO.getNomeTabela() + ".tipo_movimento";
		public static final String PREVISTO_VAL_SALDO = PrevistoDAO.getNomeTabela() + ".val_saldo";
		public static final String PREVISTO_PAGAMENTO_AUTOMATICO = PrevistoDAO.getNomeTabela() + ".pagamento_automatico";
		//NOMES UTILIZADOS PARA BUSCAR OS CAMPOS DA QUERY
		public static final String GET_ID = PrevistoDAO.getNomeTabela() + "_id";
		public static final String GET_DESCRICAO = PrevistoDAO.getNomeTabela() + "_descricao";
		public static final String GET_ALARME_ID = PrevistoDAO.getNomeTabela() + "_alarme_id";
		public static final String GET_USUARIO_ID = PrevistoDAO.getNomeTabela() + "_usuario_id";
		public static final String GET_CONTATO_ID = PrevistoDAO.getNomeTabela() + "_contato_id";
		public static final String GET_CONTA_ID = PrevistoDAO.getNomeTabela() + "_conta_id";
		public static final String GET_CATEGORIA_ID = PrevistoDAO.getNomeTabela() + "_categoria_id";
		public static final String GET_DT_EMISSAO = PrevistoDAO.getNomeTabela() + "_dt_emissao";
		public static final String GET_DT_VENCIMENTO = PrevistoDAO.getNomeTabela() + "_dt_vencimento";
		public static final String GET_VAL_PREVISTO = PrevistoDAO.getNomeTabela() + "_val_previsto";
		public static final String GET_DT_ULT_PAGAMENTO = PrevistoDAO.getNomeTabela() + "_dt_ult_pagamento";
		public static final String GET_TIPO_MOVIMENTO = PrevistoDAO.getNomeTabela() + "_tipo_movimento";
		public static final String GET_VAL_SALDO = PrevistoDAO.getNomeTabela() + "_val_saldo";
		public static final String GET_PAGAMENTO_AUTOMATICO = PrevistoDAO.getNomeTabela() + "_pagamento_automatico";		
		public static Uri getUriId(long id) {
			Uri uriPrevisto = ContentUris.withAppendedId(Previstos.CONTENT_URI, id);
			return uriPrevisto;
		}
		
	}
	
	public String toString() {
		return "Descrição: " + descricao;
	}
	
	public boolean check() throws Exception {
		

		if (this.id != 0 && RealizadoDAO.buscarRealizadoPorPrevisto(this.id) != null) {
			throw new Exception(Integer.toString(R.string.lancamento_previsto_ja_baixado));
		}
		
		if (this.val_previsto.compareTo(0.0) <= 0 ) {
			throw new Exception(Integer.toString(R.string.val_previsto_invalido));
		}
		
		if (CustomDateUtils.compararDataDMY(this.dt_emissao.getTimeInMillis(), this.dt_vencimento.getTimeInMillis()) > 0) {
			throw new Exception(Integer.toString(R.string.dt_vencimento_invalida));
		}		
		
		if (this.usuario == null) {
			throw new Exception(Integer.toString(R.string.usuario_obrig));
		} else if (UsuarioDAO.buscarUsuario(this.usuario.getId()) == null) {
			throw new Exception(Integer.toString(R.string.usuario_invalido));
		}		
		
		if (this.conta == null) {
			throw new Exception(Integer.toString(R.string.conta_obrig));
		} else if (ContaDAO.buscarConta(this.conta.getId()) == null) {
			throw new Exception(Integer.toString(R.string.conta_invalida));
		}
		
		if (this.contato != null && 
			ContatoDAO.buscarContato(this.contato.getId()) == null) {
			throw new Exception(Integer.toString(R.string.contato_invalido));
		}
		
		if (this.categoria != null &&
			CategoriaDAO.buscarCategoria(this.categoria.getId()) == null) {
			throw new Exception(Integer.toString(R.string.categoria_invalida));
		}
		
		if (this.alarme != null &&
			AlarmeDAO.buscarAlarme(this.alarme.getId()) == null) {
			throw new Exception(Integer.toString(R.string.alarme_invalido));
		}
		
		return true;
	}
	
	public void trim() {
		this.descricao = this.descricao.trim();
		this.pagamento_automatico = this.pagamento_automatico.trim();
		this.tipo_movimento = this.tipo_movimento.trim();
	}
	
	public long baixarTitulo(Conta conta, Double valMovimento, Calendar dtMovimento) throws Exception {
		
		Sistema sis = Sistema.getSistema();
		SQLiteDatabase db = sis.getConexaoBanco();
		
		try {
			db.beginTransaction();
			
			Previsto previsto = PrevistoDAO.buscarPrevisto(this.getId());
			if (previsto == null) {
				throw new Exception(Integer.toString(R.string.previsto_invalido));
			}
			
			if (RealizadoDAO.buscarRealizadoPorPrevisto(previsto.getId()) != null) {
				throw new Exception(Integer.toString(R.string.previsto_ja_baixado));
			}
		
			if (conta == null) {
				throw new Exception(Integer.toString(R.string.conta_obrig));
			}
			if (valMovimento.compareTo(0.0) <= 0) {
				throw new Exception(Integer.toString(R.string.val_realizado_invalido));
			}
			
			if (dtMovimento == null) {
				throw new Exception(Integer.toString(R.string.data_movimento_obrig));
			} 
			
			Realizado realizado = new Realizado(previsto.getUsuario(), previsto.getContato(), conta, previsto.getCategoria(), previsto.getDescricao(),
					dtMovimento, valMovimento, previsto.getTipo_movimento(), previsto  );
			long idRealizado = RealizadoDAO.salvar(realizado);
			if (idRealizado <= 0) {
				throw new Exception(Integer.toString(R.string.falha_baixar_titulo));
			}
			
			db.setTransactionSuccessful();
			
			return idRealizado;
		
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			db.endTransaction();
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Alarme getAlarme() {
		return alarme;
	}

	public void setAlarme(Alarme alarme) {
		this.alarme = alarme;
	}

	public Contato getContato() {
		return contato;
	}

	public void setContato(Contato contato) {
		this.contato = contato;
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Calendar getDt_emissao() {
		return dt_emissao;
	}

	public void setDt_emissao(Calendar dt_emissao) {
		this.dt_emissao = dt_emissao;
	}

	public Calendar getDt_vencimento() {
		return dt_vencimento;
	}

	public void setDt_vencimento(Calendar dt_vencimento) {
		this.dt_vencimento = dt_vencimento;
	}

	public Double getVal_previsto() {
		return val_previsto;
	}

	public void setVal_previsto(Double val_previsto) {
		this.val_previsto = val_previsto;
	}

	public Calendar getDt_ult_pagamento() {
		return dt_ult_pagamento;
	}

	public void setDt_ult_pagamento(Calendar dt_ult_pagamento) {
		this.dt_ult_pagamento = dt_ult_pagamento;
	}

	public String getTipo_movimento() {
		return tipo_movimento;
	}

	public void setTipo_movimento(String tipo_movimento) {
		this.tipo_movimento = tipo_movimento;
	}

	public Double getVal_saldo() {
		return val_saldo;
	}

	public void setVal_saldo(Double val_saldo) {
		this.val_saldo = val_saldo;
	}

	public String getPagamento_automatico() {
		return pagamento_automatico;
	}

	public void setPagamento_automatico(String pagamento_automatico) {
		this.pagamento_automatico = pagamento_automatico;
	}
	
	
}
