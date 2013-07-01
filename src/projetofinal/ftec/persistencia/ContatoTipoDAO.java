package projetofinal.ftec.persistencia;

import java.util.ArrayList;
import java.util.List;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Categoria.Categorias;
import projetofinal.ftec.modelo.ContatoTipo;
import projetofinal.ftec.modelo.ContatoTipo.ContatoTipos;
import projetofinal.ftec.modelo.Sistema;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

public class ContatoTipoDAO extends BaseDAO{
	private static final String NOME_TABELA = "contato_tipo";	
	
	public static long salvar(ContatoTipo contatoTipo) throws Exception {
		long id = contatoTipo.getId();
		
		contatoTipo.trim();
		
		try {
			contatoTipo.check();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}					
		
		if (id != 0) {
			atualizar(contatoTipo);
		} else {
			id = inserir(contatoTipo);
		}
		return id;
		
	}
	
	public static long inserir(ContatoTipo contatoTipo) {
		ContentValues values = new ContentValues ();
		values.put(ContatoTipos.DESCRICAO, contatoTipo.getDescricao());
		values.put(ContatoTipos.USUARIO_ID, contatoTipo.getUsuario().getId());
		
		long id = inserir(NOME_TABELA, values);
		return id;		
	}
	
	public static int atualizar(ContatoTipo contatoTipo) {
		ContentValues values = new ContentValues();
		values.put(ContatoTipos.DESCRICAO, contatoTipo.getDescricao());
		values.put(ContatoTipos.USUARIO_ID, contatoTipo.getUsuario().getId());
		String _id = String.valueOf(contatoTipo.getId());
		String where = ContatoTipos._ID + "=?";
		String[] whereArgs = new String[] { _id };
		int count = atualizar(NOME_TABELA, values, where, whereArgs);
		return count;
	}
	
	public static int deletar(long id) throws SQLiteConstraintException {
		int count;
		String where 		= ContatoTipos._ID + "=?";
		String _id 			= String.valueOf(id);
		String[] whereArgs 	= new String[] { _id };
		try {
			count 			= deletar(NOME_TABELA, where, whereArgs);
		} catch(SQLiteConstraintException e) {
			throw new SQLiteConstraintException(Integer.toString(R.string.msg_falha_excluir_contato_tipo));
		}
		return count;		
	}
	
	public static ContatoTipo buscarContatoTipo(long id) {
		Sistema sis = Sistema.getSistema();
		SQLiteDatabase db = sis.getConexaoBanco();		
		Cursor c = db.query(true, NOME_TABELA, ContatoTipo.colunas, ContatoTipos._ID + "=" + id, null, null, null, null, null);
		if (c.getCount() > 0) {
			c.moveToFirst();						
			return setDadosContatoTipo(c);			
		}
		return null;
	}
	
	public static Cursor getCursor() {
		return getCursor(NOME_TABELA, ContatoTipo.colunas);		
	}
	
	public static List<ContatoTipo> listarContatoTipo() {		
		Cursor c = getCursor();
		List<ContatoTipo> contatoTipos = new ArrayList<ContatoTipo>();		
		if (c.moveToFirst()) {			
			do {
				contatoTipos.add(setDadosContatoTipo(c));
			} while (c.moveToNext());
		} 
		return contatoTipos;
	}
	
	public static ContatoTipo buscarContatoTipoPorDescricao(String descricao) {
		ContatoTipo contatoTipo = null;
		try {
			Sistema sis = Sistema.getSistema();
			SQLiteDatabase db = sis.getConexaoBanco();			
			Cursor c = db.query(NOME_TABELA, ContatoTipo.colunas, ContatoTipos.DESCRICAO + "='" + descricao + "'", null, null, null, null);
			if (c.moveToNext()) {
				contatoTipo = setDadosContatoTipo(c);								
			}
		} catch (SQLException e) {
			return null;
		}
		return contatoTipo;
	}	
	
	public static ContatoTipo buscarContatoTipoUsuario(String descricao, Long usuario_id) {
		ContatoTipo contatoTipo = null;
		try {
			Sistema sis = Sistema.getSistema();
			SQLiteDatabase db = sis.getConexaoBanco();			
			Cursor c = db.query(NOME_TABELA, ContatoTipo.colunas, ContatoTipos.DESCRICAO + "='" + descricao + "'" + " and " +
															      ContatoTipos.USUARIO_ID + "=" + usuario_id + "", null, null, null, null);
			if (c.moveToNext()) {
				contatoTipo = setDadosContatoTipo(c);								
			}
		} catch (SQLException e) {
			return null;
		}
		return contatoTipo;
	}
	
	public static ContatoTipo setDadosContatoTipo(Cursor c) {
		ContatoTipo contatoTipo = new ContatoTipo();		
		contatoTipo.setId(c.getLong(c.getColumnIndex(ContatoTipos._ID)));
		contatoTipo.setDescricao(c.getString(c.getColumnIndex(ContatoTipos.DESCRICAO)));
		contatoTipo.setUsuario(UsuarioDAO.buscarUsuario(c.getLong(c.getColumnIndex(Categorias.USUARIO_ID))));		
		return contatoTipo;		
	}
	
	public static Cursor findLike(String[] colunas, String origem, String destino, String orderBy) {
		return findLike(NOME_TABELA, colunas, origem, destino, orderBy); 
		
	}

	public static String getNomeTabela() {
		return NOME_TABELA;
	}
	
}
