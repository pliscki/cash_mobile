package projetofinal.ftec.modelo;

import projetofinal.ftec.R;
import projetofinal.ftec.persistencia.UsuarioDAO;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class Usuario {
	public static String[] colunas = new String[] { Usuarios._ID, Usuarios.LOGIN, Usuarios.SENHA, 
		Usuarios.NOME_EXIBICAO, Usuarios.LOGIN_AUTOMATICO, Usuarios.EMAIL 	};	
	public static final String AUTHORITY = "projetofinal.ftec.modelo.provider.usuario";
	private long id;
	private String login;
	private String senha;
	private String senhaRepetir;
	private String nome_exibicao;
	private String login_automatico;
	private String email;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNome_exibicao() {
		return nome_exibicao;
	}

	public void setNome_exibicao(String nome_exibicao) {
		this.nome_exibicao = nome_exibicao;
	}

	public String getLogin_automatico() {
		return login_automatico;
	}

	public void setLogin_automatico(String login_automatico) {
		this.login_automatico = login_automatico;
	}

	public String getSenhaRepetir() {
		return senhaRepetir;
	}

	public void setSenhaRepetir(String senhaRepetir) {
		this.senhaRepetir = senhaRepetir;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Usuario() {
		
	}
	
	public Usuario(String login, String senha, String nome_exibicao, String login_automatico, String email) {
		super();
		this.login = login;
		this.senha = senha;
		this.nome_exibicao = nome_exibicao;
		this.login_automatico = login_automatico;
		this.email = email;
	}
	
	public Usuario(long id, String login, String senha, String nome_exibicao, String login_automatico, String email) {
		super();
		this.id = id;
		this.login = login;
		this.senha = senha;
		this.nome_exibicao = nome_exibicao;
		this.login_automatico = login_automatico;
		this.email = email;
	}
	
	public static final class Usuarios implements BaseColumns {
		private Usuarios() {			
		}
		
		public static final Uri CONTENT_URI = Uri.parse("content:// " + AUTHORITY + "/usuarios");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.usuarios";
		public static final String DEFAULT_SORT_ORDER = "usuario._id ASC";
		public static final String LOGIN = "login";
		public static final String SENHA = "senha";
		public static final String NOME_EXIBICAO = "nome_exibicao";
		public static final String LOGIN_AUTOMATICO = "login_automatico";
		public static final String EMAIL = "email";
		//NOMES UTILIZADOS NOS SELECTS
		public static final String USUARIO_ID = UsuarioDAO.getNomeTabela() + "._id";
		public static final String USUARIO_LOGIN = UsuarioDAO.getNomeTabela() + ".login";
		public static final String USUARIO_SENHA = UsuarioDAO.getNomeTabela() + ".senha";
		public static final String USUARIO_NOME_EXIBICAO = UsuarioDAO.getNomeTabela() + ".nome_exibicao";
		public static final String USUARIO_LOGIN_AUTOMATICO = UsuarioDAO.getNomeTabela() + ".login_automatico";
		public static final String USUARIO_EMAIL = UsuarioDAO.getNomeTabela() + ".email";
		
		public static final String GET_ID = UsuarioDAO.getNomeTabela() + "_id";
		public static final String GET_LOGIN = UsuarioDAO.getNomeTabela() + "_login";
		public static final String GET_SENHA = UsuarioDAO.getNomeTabela() + "_senha";
		public static final String GET_NOME_EXIBICAO = UsuarioDAO.getNomeTabela() + "_nome_exibicao";
		public static final String GET_LOGIN_AUTOMATICO = UsuarioDAO.getNomeTabela() + "_login_automatico";
		public static final String GET_EMAIL = UsuarioDAO.getNomeTabela() + ".email";
		
		public static Uri getUriId(long id) {
			Uri uriUsuario = ContentUris.withAppendedId(Usuarios.CONTENT_URI, id);
			return uriUsuario;
		}
		
	}
	
	public String toString() {
		return "Login: " + login + ",Nome de exibição: " + nome_exibicao;
	}
	
	public boolean check() throws Exception {
		
		if (this.login.compareTo("") == 0) {
			throw new Exception(Integer.toString(R.string.usuario_obrig));
		}
		if (this.senha.compareTo("") == 0) {
			throw new Exception(Integer.toString(R.string.senha_obrig));
		}
		if (this.senhaRepetir.compareTo("") == 0 )
			throw new Exception(Integer.toString(R.string.repetir_senha_obrig));
		if (this.id == 0 && UsuarioDAO.buscarUsuarioPorLogin(this.login) != null) {
			throw new Exception(Integer.toString(R.string.usuario_duplicado));
		}
		if (!this.senha.equals(this.senhaRepetir.toString())) {
			throw new Exception(Integer.toString(R.string.senha_repetida_errada));
		}
		
		return true;
	}
	
	public void trim() {
		this.login         = this.login.trim();
		this.nome_exibicao = this.nome_exibicao.trim();
		this.senha 		   = this.senha.trim();
		this.email		   = this.email.trim();
	}
	
	
}
