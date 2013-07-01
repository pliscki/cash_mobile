package projetofinal.ftec.modelo;

import projetofinal.ftec.R;
import projetofinal.ftec.persistencia.ContatoDAO;
import projetofinal.ftec.persistencia.ContatoTipoDAO;
import projetofinal.ftec.persistencia.UsuarioDAO;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class Contato {
	public static String[] colunas = new String[] { Contatos._ID, Contatos.NOME, Contatos.SOBRENOME, Contatos.TELEFONE,
		Contatos.EMAIL, Contatos.USUARIO_ID, Contatos.CONTATO_TIPO 	};	
	public static final String AUTHORITY = "projetofinal.ftec.modelo.provider.contato";
	private long id;
	private String nome;
	private String sobrenome;
	private String telefone;
	private String email;	
	private Usuario usuario;
	private ContatoTipo contatoTipo;	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public ContatoTipo getContatoTipo() {
		return contatoTipo;
	}

	public void setContatoTipo(ContatoTipo contatoTipo) {
		this.contatoTipo = contatoTipo;
	}

	public Contato() {
		
	}
	
	public Contato(String nome, String telefone, String email, String sobrenome, Usuario usuario, ContatoTipo contatoTipo) {
		super();
		this.nome = nome;
		this.sobrenome = sobrenome;
		this.telefone = telefone;
		this.email = email;
		this.usuario = usuario;
		this.contatoTipo = contatoTipo;
	}
	
	public Contato(Long id, String nome, String telefone, String email, String sobrenome, Usuario usuario, ContatoTipo contatoTipo) {
		super();
		this.id = id;
		this.nome = nome;
		this.sobrenome = sobrenome;
		this.telefone = telefone;
		this.email = email;
		this.usuario = usuario;
		this.contatoTipo = contatoTipo;
	}
	
	public static final class Contatos implements BaseColumns {
		private Contatos() {			
		}
		
		public static final Uri CONTENT_URI = Uri.parse("content:// " + AUTHORITY + "/contatos");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.contatos";
		public static final String DEFAULT_SORT_ORDER = "contato._id ASC";
		public static final String NOME = "nome";
		public static final String USUARIO_ID = "usuario_id";
		public static final String SOBRENOME = "sobrenome";
		public static final String TELEFONE = "telefone";
		public static final String EMAIL = "email";
		public static final String CONTATO_TIPO = "contato_tipo_id";
		//NOMES UTILIZADOS NOS SELECTS
		public static final String CONTATO_ID = ContatoDAO.getNomeTabela() + "._id";
		public static final String CONTATO_NOME = ContatoDAO.getNomeTabela() + ".nome";
		public static final String CONTATO_USUARIO_ID = ContatoDAO.getNomeTabela() + ".usuario_id";
		public static final String CONTATO_SOBRENOME = ContatoDAO.getNomeTabela() + ".sobrenome";
		public static final String CONTATO_TELEFONE = ContatoDAO.getNomeTabela() + ".telefone";
		public static final String CONTATO_EMAIL = ContatoDAO.getNomeTabela() + ".email";
		public static final String CONTATO_CONTATO_TIPO = ContatoDAO.getNomeTabela() + ".contato_tipo_id";
		
		public static final String GET_ID = ContatoDAO.getNomeTabela() + "_id";
		public static final String GET_NOME = ContatoDAO.getNomeTabela() + "_nome";
		public static final String GET_USUARIO_ID = ContatoDAO.getNomeTabela() + "_usuario_id";
		public static final String GET_SOBRENOME = ContatoDAO.getNomeTabela() + "_sobrenome";
		public static final String GET_TELEFONE = ContatoDAO.getNomeTabela() + "_telefone";
		public static final String GET_EMAIL = ContatoDAO.getNomeTabela() + "_email";
		public static final String GET_CONTATO_TIPO = ContatoDAO.getNomeTabela() + "_contato_tipo_id";
		public static Uri getUriId(long id) {
			Uri uriConta = ContentUris.withAppendedId(Contatos.CONTENT_URI, id);
			return uriConta;
		}
		
	}
	
	public String toString() {
		return "Nome: " + nome + " Sobrenome: " + sobrenome;
	}
	
	public boolean check() throws Exception {		
		
		if (this.nome.compareTo("") == 0) {
			throw new Exception(Integer.toString(R.string.nome_obrig));
		}
		if (this.usuario == null) {
			throw new Exception(Integer.toString(R.string.usuario_obrig));
		} else if (UsuarioDAO.buscarUsuario(this.usuario.getId()) == null) {
			throw new Exception(Integer.toString(R.string.usuario_invalido));
		}
		
		if (this.contatoTipo == null) {
			throw new Exception(Integer.toString(R.string.contato_tipo_obrig));
		} else if (ContatoTipoDAO.buscarContatoTipo(this.contatoTipo.getId()) == null) {
			throw new Exception(Integer.toString(R.string.contato_tipo_invalido));
		}
		
		Contato contato = ContatoDAO.buscarContatoPorNome(this.nome);
		if (contato != null && ( this.id == 0 || this.id != contato.getId()) ) {
			throw new Exception(Integer.toString(R.string.contato_duplicado));
		} 
		
		return true;
	}
	
	public void trim() {
		this.nome = this.nome.trim();
		this.sobrenome = this.sobrenome.trim();
		this.telefone = this.telefone.trim();
		this.email = this.email.trim();
	}
	
	
}
