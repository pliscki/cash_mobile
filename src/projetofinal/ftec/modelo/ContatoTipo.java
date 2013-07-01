package projetofinal.ftec.modelo;

import projetofinal.ftec.R;
import projetofinal.ftec.persistencia.ContatoTipoDAO;
import projetofinal.ftec.persistencia.UsuarioDAO;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class ContatoTipo {
	public static String[] colunas = new String[] { ContatoTipos._ID, ContatoTipos.DESCRICAO, ContatoTipos.USUARIO_ID 	};	
	public static final String AUTHORITY = "projetofinal.ftec.modelo.provider.contatoTipo";
	private long id;
	private String descricao;
	private Usuario usuario;	
		
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

	public ContatoTipo() {
		
	}
	
	public ContatoTipo(String descricao, Usuario usuario) {
		super();
		this.descricao = descricao;
		this.usuario = usuario;
	}
	
	public ContatoTipo(long id, String descricao, Usuario usuario) {
		super();
		this.id = id;
		this.descricao = descricao;
		this.usuario = usuario;	
	}
	
	public static final class ContatoTipos implements BaseColumns {
		private ContatoTipos() {			
		}
		
		public static final Uri CONTENT_URI = Uri.parse("content:// " + AUTHORITY + "/contatoTipos");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.contatoTipos";
		public static final String DEFAULT_SORT_ORDER = "contato_tipo._id ASC";
		public static final String DESCRICAO = "descricao";
		public static final String USUARIO_ID = "usuario_id";
		//NOMES UTILIZADOS NOS SELECTS
		public static final String CONTATO_TIPO_ID = ContatoTipoDAO.getNomeTabela() + "._id";
		public static final String CONTATO_TIPO_DESCRICAO = ContatoTipoDAO.getNomeTabela() + ".descricao";
		public static final String CONTATO_TIPO_USUARIO_ID = ContatoTipoDAO.getNomeTabela() + ".usuario_id";
		
		public static final String GET_TIPO_ID = ContatoTipoDAO.getNomeTabela() + "_id";
		public static final String GET_TIPO_DESCRICAO = ContatoTipoDAO.getNomeTabela() + "_descricao";
		public static final String GET_TIPO_USUARIO_ID = ContatoTipoDAO.getNomeTabela() + "_usuario_id";
		public static Uri getUriId(long id) {
			Uri uriContatoTipo = ContentUris.withAppendedId(ContatoTipos.CONTENT_URI, id);
			return uriContatoTipo;
		}
		
	}
	
	public String toString() {
		return "Tipo de Contato: " + descricao;
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
		
		ContatoTipo contatoTipo = ContatoTipoDAO.buscarContatoTipoPorDescricao(this.descricao);
		if (contatoTipo != null && ( this.id == 0 || this.id != contatoTipo.getId()) ) {
			throw new Exception(Integer.toString(R.string.contato_tipo_duplicado));
		}
		
		return true;
	}
	
	public void trim() {
		this.descricao = this.descricao.trim();
	}
	
	
}
