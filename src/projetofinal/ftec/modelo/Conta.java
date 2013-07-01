package projetofinal.ftec.modelo;

import java.util.Calendar;

import projetofinal.ftec.R;
import projetofinal.ftec.persistencia.ContaDAO;
import projetofinal.ftec.persistencia.RealizadoDAO;
import projetofinal.ftec.persistencia.UsuarioDAO;
import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.provider.BaseColumns;

public class Conta {
	public static String[] colunas = new String[] { Contas._ID, Contas.DESCRICAO, Contas.USUARIO_ID 	};	
	public static final String AUTHORITY = "projetofinal.ftec.modelo.provider.conta";
	private long id;
	private String descricao;
	private Usuario usuario;
	private Double saldoInicial;
	private String tipoMovimentoInicial;
	private Context ctx;
		
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Double getSaldoInicial() {
		return saldoInicial;
	}

	public void setSaldoInicial(Double saldoInicial) {
		this.saldoInicial = saldoInicial;
	}

	public Context getCtx() {
		return ctx;
	}

	public void setCtx(Context ctx) {
		this.ctx = ctx;
	}

	public String getTipoMovimentoInicial() {
		return tipoMovimentoInicial;
	}

	public void setTipoMovimentoInicial(String tipoMovimentoInicial) {
		this.tipoMovimentoInicial = tipoMovimentoInicial;
	}

	public Conta() {
		
	}
	
	public Conta(String descricao, Usuario usuario, Double saldoInicial) {
		super();
		this.descricao 	= descricao;
		this.usuario = usuario;
		this.saldoInicial = saldoInicial;
	}
	
	public Conta(long id, String descricao, Usuario usuario, Double saldoInicial) {
		super();
		this.id = id;
		this.descricao = descricao;
		this.usuario = usuario;
		this.saldoInicial = saldoInicial;
	}
	
	public static final class Contas implements BaseColumns {
		private Contas() {			
		}
		
		public static final Uri CONTENT_URI = Uri.parse("content:// " + AUTHORITY + "/contas");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.contas";
		public static final String DEFAULT_SORT_ORDER = "conta._id ASC";
		public static final String DESCRICAO = "descricao";
		public static final String USUARIO_ID = "usuario_id";
		//nomes utilizados nos selects
		public static final String CONTA_ID = ContaDAO.getNomeTabela() + "._id";
		public static final String CONTA_DESCRICAO = ContaDAO.getNomeTabela() + ".descricao";
		public static final String CONTA_USUARIO_ID = ContaDAO.getNomeTabela() + ".usuario_id";
		
		public static final String GET_ID = ContaDAO.getNomeTabela() + "_id";
		public static final String GET_DESCRICAO = ContaDAO.getNomeTabela() + "_descricao";
		public static final String GET_USUARIO_ID = ContaDAO.getNomeTabela() + "_usuario_id";		
		public static Uri getUriId(long id) {
			Uri uriConta = ContentUris.withAppendedId(Contas.CONTENT_URI, id);
			return uriConta;
		}
		
	}
	
	public String toString() {
		return "Descrição: " + descricao;
	}
	
	public boolean check() throws Exception {		
		
		if (this.descricao.compareTo("") == 0) {
			throw new Exception(Integer.toString(R.string.descricao_obrig));
		}
		if (this.usuario == null) {
			throw new Exception(Integer.toString(R.string.usuario_obrig));
		} else if (UsuarioDAO.buscarUsuario(this.usuario.getId()) == null) {
			throw new Exception(Integer.toString(R.string.usuario_invalido));
		}		
		
		Conta conta = ContaDAO.buscarContaPorDescricao(this.descricao);
		if (conta != null && ( this.id == 0 || this.id != conta.getId()) ) {
			throw new Exception(Integer.toString(R.string.conta_duplicada));
		}
		
		return true;
	}
	
	public void trim() {
		this.descricao = this.descricao.trim();
	}

	public void gerarSaldoInicial() throws Exception {
		Realizado realizado = new Realizado();
		realizado.setConta(this);
		if (this.ctx != null) {
			realizado.setDescricao(ctx.getResources().getString(R.string.descricao_saldo_inicial));
		}
		realizado.setDt_movimento(Calendar.getInstance());
		realizado.setVal_movimento(this.saldoInicial);
		realizado.setTipo_movimento(this.tipoMovimentoInicial);
		realizado.setUsuario(this.usuario);
		
		try {
			long idRealizado = RealizadoDAO.salvar(realizado);
			if (idRealizado <= 0) {
				throw new Exception(Integer.toString(R.string.falha_criar_saldo_inicial));
			}
		} catch (Exception e) {
			throw new Exception(Integer.toString(R.string.falha_criar_saldo_inicial));
		}
		
		
		
	}
	
	
}
