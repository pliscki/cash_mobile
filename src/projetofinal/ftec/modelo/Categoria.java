package projetofinal.ftec.modelo;

import projetofinal.ftec.R;
import projetofinal.ftec.persistencia.CategoriaDAO;
import projetofinal.ftec.persistencia.UsuarioDAO;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class Categoria {
	public static String[] colunas = new String[] { Categorias._ID, Categorias.DESCRICAO, Categorias.USUARIO_ID, Categorias.PAGAMENTO_AUTOMATICO };	
	public static final String AUTHORITY = "projetofinal.ftec.modelo.provider.categoria";
	private long id;
	private String descricao;
	private Usuario usuario;
	private String pagamento_automatico;		

	public Categoria() {
		
	}
	
	public Categoria(String descricao, Usuario usuario, String pagamento_automatico) {
		super();
		this.descricao 	= descricao;
		this.usuario = usuario;
		this.pagamento_automatico = pagamento_automatico;
	}
	
	public Categoria(long id, String descricao, Usuario usuario, String pagamento_automatico) {
		super();
		this.id = id;
		this.descricao = descricao;
		this.usuario = usuario;
		this.pagamento_automatico = pagamento_automatico;
	}
	
	public static final class Categorias implements BaseColumns {
		private Categorias() {			
		}
		
		public static final Uri CONTENT_URI = Uri.parse("content:// " + AUTHORITY + "/categorias");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.categorias";
		public static final String DEFAULT_SORT_ORDER = "categoria._id ASC";
		public static final String DESCRICAO = "descricao";
		public static final String USUARIO_ID = "usuario_id";
		public static final String PAGAMENTO_AUTOMATICO = "pagamento_automatico";
		//nomes utilizados nos selects
		public static final String CATEGORIA_ID = CategoriaDAO.getNomeTabela() + "._id";
		public static final String CATEGORIA_DESCRICAO = CategoriaDAO.getNomeTabela() + ".descricao";
		public static final String CATEGORIA_USUARIO_ID = CategoriaDAO.getNomeTabela() + ".usuario_id";
		public static final String CATEGORIA_PAGAMENTO_AUTOMATICO = CategoriaDAO.getNomeTabela() + ".pagamento_automatico";
		
		public static final String GET_ID = CategoriaDAO.getNomeTabela() + "_id";
		public static final String GET_DESCRICAO = CategoriaDAO.getNomeTabela() + "_descricao";
		public static final String GET_USUARIO_ID = CategoriaDAO.getNomeTabela() + "_usuario_id";
		public static final String GET_PAGAMENTO_AUTOMATICO = CategoriaDAO.getNomeTabela() + "_pagamento_automatico";
		public static Uri getUriId(long id) {
			Uri uriCategoria = ContentUris.withAppendedId(Categorias.CONTENT_URI, id);
			return uriCategoria;
		}
		
	}
	
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
	
	public String getPagamento_automatico() {
		return pagamento_automatico;
	}

	public void setPagamento_automatico(String pagamento_automatico) {
		this.pagamento_automatico = pagamento_automatico;
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
		
		Categoria categoria = CategoriaDAO.buscarCategoriaPorDescricao(this.descricao);
		if (categoria != null && ( this.id == 0 || this.id != categoria.getId()) ) {
			throw new Exception(Integer.toString(R.string.categoria_duplicada));
		}
		
		return true;
	}
	
	public void trim() {
		this.descricao = this.descricao.trim();
	}
	
	
}
