package projetofinal.ftec.persistencia;

import java.util.ArrayList;
import java.util.List;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Contato;
import projetofinal.ftec.modelo.Contato.Contatos;
import projetofinal.ftec.modelo.Sistema;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

public class ContatoDAO extends BaseDAO {
	private static final String NOME_TABELA = "contato";	
	
	public static long salvar(Contato contato) throws Exception {
		long id = contato.getId();
		
		contato.trim();
		
		try {
			contato.check();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}					
		
		if (id != 0) {
			atualizar(contato);
		} else {
			id = inserir(contato);
		}
		return id;
		
	}
	
	public static long inserir(Contato contato) {
		ContentValues values = new ContentValues ();
		values.put(Contatos.NOME, contato.getNome());
		values.put(Contatos.SOBRENOME, contato.getSobrenome());
		values.put(Contatos.TELEFONE, contato.getTelefone());
		values.put(Contatos.EMAIL, contato.getEmail());
		values.put(Contatos.USUARIO_ID, contato.getUsuario().getId());
		if (contato.getContatoTipo() != null) {
			values.put(Contatos.CONTATO_TIPO, contato.getContatoTipo().getId());
		} else {
			values.putNull(Contatos.CONTATO_TIPO);
		}
		
		
		long id = inserir(NOME_TABELA, values);
		return id;		
	} 
	
	public static int atualizar(Contato contato) {
		ContentValues values = new ContentValues();
		values.put(Contatos.NOME, contato.getNome());
		values.put(Contatos.SOBRENOME, contato.getSobrenome());
		values.put(Contatos.TELEFONE, contato.getTelefone());
		values.put(Contatos.EMAIL, contato.getEmail());
		values.put(Contatos.USUARIO_ID, contato.getUsuario().getId());
		if (contato.getContatoTipo() != null) {
			values.put(Contatos.CONTATO_TIPO, contato.getContatoTipo().getId());
		} else {
			values.putNull(Contatos.CONTATO_TIPO);
		}
		
		String _id = String.valueOf(contato.getId());
		String where = Contatos._ID + "=?";
		String[] whereArgs = new String[] { _id };
		int count = atualizar(NOME_TABELA, values, where, whereArgs);
		return count;
	}
	
	public static int deletar(long id) throws SQLiteConstraintException {
		int count;
		String where 		= Contatos._ID + "=?";
		String _id 			= String.valueOf(id);
		String[] whereArgs 	= new String[] { _id };
		try {
			count 			= deletar(NOME_TABELA, where, whereArgs);
		} catch(SQLiteConstraintException e) {
			throw new SQLiteConstraintException(Integer.toString(R.string.msg_falha_excluir_contato));
		}
		return count;		
	}
	
	public static Contato buscarContato(long id) {
		Sistema sis = Sistema.getSistema();
		SQLiteDatabase db = sis.getConexaoBanco();		
		Cursor c = db.query(true, NOME_TABELA, Contato.colunas, Contatos._ID + "=" + id, null, null, null, null, null);
		if (c.getCount() > 0) {
			c.moveToFirst();						
			return setDadosContato(c);			
		}
		return null;
	}
	
	public static Cursor getCursor() {
		return getCursor(NOME_TABELA, Contato.colunas);		
	}
	
	public static List<Contato> listarContato() {		
		Cursor c = getCursor();
		List<Contato> contatos = new ArrayList<Contato>();		
		if (c.moveToFirst()) {			
			do {
				contatos.add(setDadosContato(c));
			} while (c.moveToNext());
		} 
		return contatos;
	}
	
	public static Contato buscarContatoPorNome(String nome) {
		Contato contato = null;
		try {
			Sistema sis = Sistema.getSistema();
			SQLiteDatabase db = sis.getConexaoBanco();			
			Cursor c = db.query(NOME_TABELA, Contato.colunas, Contatos.NOME + "='" + nome + "'", null, null, null, null);
			if (c.moveToNext()) {
				contato = setDadosContato(c);								
			}
		} catch (SQLException e) {
			return null;
		}
		return contato;
	}	
	
	public static Contato buscarContatoUsuario(String nome, Long usuario_id) {
		Contato contato = null;
		try {
			Sistema sis = Sistema.getSistema();
			SQLiteDatabase db = sis.getConexaoBanco();			
			Cursor c = db.query(NOME_TABELA, Contato.colunas, Contatos.NOME + "='" + nome + "'" + " and " +
															Contatos.USUARIO_ID + "=" + usuario_id + "", null, null, null, null);
			if (c.moveToNext()) {
				contato = setDadosContato(c);								
			}
		} catch (SQLException e) {
			return null;
		}
		return contato;
	}	
	
	public static Contato setDadosContato(Cursor c) {
		Contato contato = new Contato();
		contato.setId(c.getLong(c.getColumnIndexOrThrow(Contatos._ID)));
		contato.setNome(c.getString(c.getColumnIndexOrThrow(Contatos.NOME)));
		contato.setSobrenome(c.getString(c.getColumnIndexOrThrow(Contatos.SOBRENOME)));
		contato.setTelefone(c.getString(c.getColumnIndexOrThrow(Contatos.TELEFONE)));
		contato.setEmail(c.getString(c.getColumnIndexOrThrow(Contatos.EMAIL)));
		contato.setUsuario(UsuarioDAO.buscarUsuario(c.getLong(c.getColumnIndex(Contatos.USUARIO_ID))));
		contato.setContatoTipo(ContatoTipoDAO.buscarContatoTipo(c.getLong(c.getColumnIndexOrThrow(Contatos.CONTATO_TIPO))));
		return contato;		
	}
	
	public static Cursor findLike(String[] colunas, String origem, String destino, String orderBy) {		 
		return findLike(NOME_TABELA, colunas, origem, destino, orderBy);		
	}

	public static String getNomeTabela() {
		return NOME_TABELA;
	}
	
	
	
}
