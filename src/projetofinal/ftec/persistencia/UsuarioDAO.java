package projetofinal.ftec.persistencia;

import java.util.ArrayList;
import java.util.List;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Sistema;
import projetofinal.ftec.modelo.Usuario;
import projetofinal.ftec.modelo.Usuario.Usuarios;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

public class UsuarioDAO extends BaseDAO{
	private static final String NOME_TABELA = "usuario";	
	
	public static long salvar(Usuario usuario) throws Exception {
		long id = usuario.getId();
		
		usuario.trim();
		
		try {
			usuario.check();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}					
		
		if (id != 0) {
			atualizar(usuario);
		} else {
			id = inserir(usuario);
		}
		return id;
		
	}
	
	public static long inserir(Usuario usuario) {
		ContentValues values = new ContentValues ();
		values.put(Usuarios.LOGIN, usuario.getLogin());
		values.put(Usuarios.SENHA, usuario.getSenha());
		values.put(Usuarios.NOME_EXIBICAO, usuario.getNome_exibicao());
		values.put(Usuarios.LOGIN_AUTOMATICO, usuario.getLogin_automatico());
		values.put(Usuarios.EMAIL, usuario.getEmail());
		long id = inserir(NOME_TABELA, values);
		return id;		
	}
	
	public static int atualizar(Usuario usuario) {
		ContentValues values = new ContentValues();
		values.put(Usuarios.LOGIN, usuario.getLogin());
		values.put(Usuarios.SENHA, usuario.getSenha());
		values.put(Usuarios.NOME_EXIBICAO, usuario.getNome_exibicao());
		values.put(Usuarios.LOGIN_AUTOMATICO, usuario.getLogin_automatico());
		values.put(Usuarios.EMAIL, usuario.getEmail());
		String _id = String.valueOf(usuario.getId());
		String where = Usuarios._ID + "=?";
		String[] whereArgs = new String[] { _id };
		int count = atualizar(NOME_TABELA, values, where, whereArgs);
		return count;
	}
	
	public static int deletar(long id) throws SQLiteConstraintException {
		int count;
		String where 		= Usuarios._ID + "=?";
		String _id 			= String.valueOf(id);
		String[] whereArgs 	= new String[] { _id };
		try {
			count 			= deletar(NOME_TABELA, where, whereArgs);
		} catch(SQLiteConstraintException e) {
			throw new SQLiteConstraintException(Integer.toString(R.string.msg_falha_excluir_usuario));
		}
		return count;		
	}
	
	public static Usuario buscarUsuario(long id) {
		Sistema sis = Sistema.getSistema();
		SQLiteDatabase db = sis.getConexaoBanco();		
		Cursor c = db.query(true, NOME_TABELA, Usuario.colunas, Usuarios._ID + "=" + id, null, null, null, null, null);
		if (c.getCount() > 0) {
			c.moveToFirst();						
			return setDadosUsuario(c);			
		}
		return null;
	}
	
	public static Cursor getCursor() {
		return getCursor(NOME_TABELA, Usuario.colunas);		
	}
	
	public static List<Usuario> listarUsuario() {		
		Cursor c = getCursor();
		List<Usuario> usuarios = new ArrayList<Usuario>();		
		if (c.moveToFirst()) {			
			do {
				usuarios.add(setDadosUsuario(c));
			} while (c.moveToNext());
		} 
		return usuarios;
	}
	
	public static Usuario buscarUsuarioPorLogin(String login) {
		Usuario usuario = null;
		try {
			Sistema sis = Sistema.getSistema();
			SQLiteDatabase db = sis.getConexaoBanco();			
			Cursor c = db.query(NOME_TABELA, Usuario.colunas, Usuarios.LOGIN + "='" + login + "'", null, null, null, null);
			if (c.moveToNext()) {
				usuario = setDadosUsuario(c);								
			}
		} catch (SQLException e) {
			return null;
		}
		return usuario;
	}	
	
	public static Usuario buscarUsuarioPorLoginSenha(String login, String senha) {
		Usuario usuario = null;
		try {
			Sistema sis = Sistema.getSistema();
			SQLiteDatabase db = sis.getConexaoBanco();			
			Cursor c = db.query(NOME_TABELA, Usuario.colunas, Usuarios.LOGIN + "='" + login + "'" + " and " +
															  Usuarios.SENHA + "='" + senha + "'", null, null, null, null);
			if (c.moveToNext()) {
				usuario = setDadosUsuario(c);								
			}
		} catch (SQLException e) {
			return null;
		}
		return usuario;
	}		
	
	public static Usuario setDadosUsuario(Cursor c) {
		Usuario usuario = new Usuario();		
		usuario.setId(c.getLong(c.getColumnIndex(Usuarios._ID)));
		usuario.setLogin(c.getString(c.getColumnIndex(Usuarios.LOGIN)));
		usuario.setSenha(c.getString(c.getColumnIndex(Usuarios.SENHA)));
		usuario.setNome_exibicao(c.getString(c.getColumnIndex(Usuarios.NOME_EXIBICAO)));
		usuario.setLogin_automatico(c.getString(c.getColumnIndex(Usuarios.LOGIN_AUTOMATICO)));
		usuario.setEmail(c.getString(c.getColumnIndexOrThrow(Usuarios.EMAIL)));
		return usuario;		
	}
	
	public static Cursor findLike(String[] colunas, String origem, String destino, String orderBy) {  
		return findLike(NOME_TABELA, colunas, origem, destino, orderBy);		
	}

	public static String getNomeTabela() {
		return NOME_TABELA;
	}
	
}
