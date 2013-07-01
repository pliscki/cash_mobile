package projetofinal.ftec.modelo;

import java.util.Calendar;

import projetofinal.ftec.R;
import projetofinal.ftec.persistencia.CategoriaDAO;
import projetofinal.ftec.persistencia.ContaDAO;
import projetofinal.ftec.persistencia.ContatoDAO;
import projetofinal.ftec.persistencia.PrevistoDAO;
import projetofinal.ftec.persistencia.RealizadoDAO;
import projetofinal.ftec.persistencia.UsuarioDAO;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class Realizado {
	public static String[] colunas = new String[] {Realizados._ID, Realizados.DESCRICAO, Realizados.CATEGORIA_ID, Realizados.CONTA_ID,
		Realizados.CONTATO_ID, Realizados.PREVISTO_ID, Realizados.DT_MOVIMENTO, Realizados.VAL_MOVIMENTO, Realizados.TIPO_MOVIMENTO, Realizados.USUARIO_ID};	
	public static final String AUTHORITY = "projetofinal.ftec.modelo.provider.realizado";
	private long id;
	private Usuario usuario;
	private Contato contato;
	private Conta conta;
	private Categoria categoria;	
	private String descricao;	
	private Calendar dt_movimento;
	private Double val_movimento;
	private String tipo_movimento;
	private Previsto previsto;

	public Realizado() {
		dt_movimento = Calendar.getInstance();
		val_movimento = 0.0;
	}
	
	public Realizado( Usuario usuario, Contato contato, Conta conta, Categoria categoria, String descricao,
			Calendar dt_movimento, Double val_movimento, String tipo_movimento, Previsto previsto) {
		super();
		this.usuario = usuario;
		this.contato = contato;
		this.conta = conta;
		this.categoria = categoria;
		this.descricao = descricao;
		this.dt_movimento = dt_movimento;
		this.val_movimento = val_movimento;
		this.tipo_movimento = tipo_movimento;
		this.previsto = previsto;
		
	}
	
	public Realizado(long id, Usuario usuario, Contato contato, Conta conta, Categoria categoria, String descricao,
			Calendar dt_movimento, Double val_movimento, String tipo_movimento, Previsto previsto) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.contato = contato;
		this.conta = conta;
		this.categoria = categoria;
		this.descricao = descricao;
		this.dt_movimento = dt_movimento;
		this.val_movimento = val_movimento;
		this.tipo_movimento = tipo_movimento;
		this.previsto = previsto;
	}
	public static final class Realizados implements BaseColumns {
		private Realizados() {			
		}
		
		public static final Uri CONTENT_URI = Uri.parse("content:// " + AUTHORITY + "/realizados");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.realizados";
		public static final String DEFAULT_SORT_ORDER = "realizado._id ASC";
		public static final String DESCRICAO = "descricao";
		public static final String USUARIO_ID = "usuario_id";
		public static final String CONTATO_ID = "contato_id";
		public static final String CONTA_ID = "conta_id";
		public static final String CATEGORIA_ID = "categoria_id";
		public static final String DT_MOVIMENTO = "dt_movimento";
		public static final String VAL_MOVIMENTO = "val_movimento";
		public static final String TIPO_MOVIMENTO = "tipo_movimento";
		public static final String PREVISTO_ID = "previsto_id";
		//NOMES UTILIZADOS NOS SELECTS
		public static final String REALIZADO_ID = RealizadoDAO.getNomeTabela() + "._id";
		public static final String REALIZADO_DESCRICAO = RealizadoDAO.getNomeTabela() + ".descricao";
		public static final String REALIZADO_USUARIO_ID = RealizadoDAO.getNomeTabela() + ".usuario_id";
		public static final String REALIZADO_CONTATO_ID = RealizadoDAO.getNomeTabela() + ".contato_id";
		public static final String REALIZADO_CONTA_ID = RealizadoDAO.getNomeTabela() + ".conta_id";
		public static final String REALIZADO_CATEGORIA_ID = RealizadoDAO.getNomeTabela() + ".categoria_id";
		public static final String REALIZADO_DT_MOVIMENTO = RealizadoDAO.getNomeTabela() + ".dt_movimento";
		public static final String REALIZADO_VAL_MOVIMENTO = RealizadoDAO.getNomeTabela() + ".val_movimento";
		public static final String REALIZADO_TIPO_MOVIMENTO = RealizadoDAO.getNomeTabela() + ".tipo_movimento";
		public static final String REALIZADO_PREVISTO_ID = RealizadoDAO.getNomeTabela() + ".previsto_id";
		
		public static final String GET_ID = RealizadoDAO.getNomeTabela() + "_id";
		public static final String GET_DESCRICAO = RealizadoDAO.getNomeTabela() + "_descricao";
		public static final String GET_USUARIO_ID = RealizadoDAO.getNomeTabela() + "_usuario_id";
		public static final String GET_CONTATO_ID = RealizadoDAO.getNomeTabela() + "_contato_id";
		public static final String GET_CONTA_ID = RealizadoDAO.getNomeTabela() + "_conta_id";
		public static final String GET_CATEGORIA_ID = RealizadoDAO.getNomeTabela() + "_categoria_id";
		public static final String GET_DT_MOVIMENTO = RealizadoDAO.getNomeTabela() + "_dt_movimento";
		public static final String GET_VAL_MOVIMENTO = RealizadoDAO.getNomeTabela() + "_val_movimento";
		public static final String GET_TIPO_MOVIMENTO = RealizadoDAO.getNomeTabela() + "_tipo_movimento";
		public static final String GET_PREVISTO_ID = RealizadoDAO.getNomeTabela() + "_previsto_id";
		
		public static Uri getUriId(long id) {
			Uri uriRealizado = ContentUris.withAppendedId(Realizados.CONTENT_URI, id);
			return uriRealizado;
		}
		
	}
	
	public String toString() {
		return "Descrição: " + descricao;
	}
	
	public boolean check() throws Exception {		
		
		if (this.val_movimento.compareTo(0.0) <= 0 ) {
			throw new Exception(Integer.toString(R.string.val_realizado_invalido));
		}
		
		if (this.dt_movimento == null) {
			throw new Exception(Integer.toString(R.string.data_movimento_obrig));
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
		
		if (this.previsto != null) {
			if (PrevistoDAO.buscarPrevisto(this.previsto.getId()) == null) {
				throw new Exception(Integer.toString(R.string.previsto_invalido));
			}
		}
		
		if (this.contato != null && 
			ContatoDAO.buscarContato(this.contato.getId()) == null) {
			throw new Exception(Integer.toString(R.string.contato_invalido));
		}
		
		if (this.categoria != null &&
			CategoriaDAO.buscarCategoria(this.categoria.getId()) == null) {
			throw new Exception(Integer.toString(R.string.categoria_invalida));
		}
		
		return true;
	}
	
	public void trim() {
		this.descricao = this.descricao.trim();
		this.tipo_movimento = this.tipo_movimento.trim();
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

	public Calendar getDt_movimento() {
		return dt_movimento;
	}

	public void setDt_movimento(Calendar dt_movimento) {
		this.dt_movimento = dt_movimento;
	}

	public Double getVal_movimento() {
		return val_movimento;
	}

	public void setVal_movimento(Double val_movimento) {
		this.val_movimento = val_movimento;
	}

	public String getTipo_movimento() {
		return tipo_movimento;
	}

	public void setTipo_movimento(String tipo_movimento) {
		this.tipo_movimento = tipo_movimento;
	}

	public Previsto getPrevisto() {
		return previsto;
	}

	public void setPrevisto(Previsto previsto) {
		this.previsto = previsto;
	}

	
	
	
}
