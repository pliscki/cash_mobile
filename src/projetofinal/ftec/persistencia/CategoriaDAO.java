package projetofinal.ftec.persistencia;

import java.util.ArrayList;
import java.util.List;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Categoria;
import projetofinal.ftec.modelo.Categoria.Categorias;
import projetofinal.ftec.modelo.Sistema;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

public class CategoriaDAO extends BaseDAO{
	private static final String NOME_TABELA = "categoria";	
	
	public static long salvar(Categoria categoria) throws Exception {
		long id = categoria.getId();
		
		categoria.trim();
		
		try {
			categoria.check();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}					
		
		if (id != 0) {
			atualizar(categoria);
		} else {
			id = inserir(categoria);
		}
		return id;
		
	}
	
	public static long inserir(Categoria categoria) {
		ContentValues values = new ContentValues ();
		values.put(Categorias.DESCRICAO, categoria.getDescricao());
		values.put(Categorias.USUARIO_ID, categoria.getUsuario().getId());
		values.put(Categorias.PAGAMENTO_AUTOMATICO, categoria.getPagamento_automatico());
		
		long id = inserir(NOME_TABELA, values);
		return id;		
	}
	
	public static int atualizar(Categoria categoria) {
		ContentValues values = new ContentValues();
		values.put(Categorias.DESCRICAO, categoria.getDescricao());
		values.put(Categorias.USUARIO_ID, categoria.getUsuario().getId());
		values.put(Categorias.PAGAMENTO_AUTOMATICO, categoria.getPagamento_automatico());
		String _id = String.valueOf(categoria.getId());
		String where = Categorias._ID + "=?";
		String[] whereArgs = new String[] { _id };
		int count = atualizar(NOME_TABELA, values, where, whereArgs);
		return count;
	}
	
	public static int deletar(long id) throws SQLiteConstraintException {
		int count;
		String where 		= Categorias._ID + "=?";
		String _id 			= String.valueOf(id);
		String[] whereArgs 	= new String[] { _id };
		try {
			count 			= deletar(NOME_TABELA, where, whereArgs);
		} catch (SQLiteConstraintException e) {
			throw new SQLiteConstraintException(Integer.toString(R.string.msg_falha_excluir_categoria));
		}
		return count;		
	}
	
	public static Categoria buscarCategoria(long id) {
		Sistema sis = Sistema.getSistema();
		SQLiteDatabase db = sis.getConexaoBanco();		
		Cursor c = db.query(true, NOME_TABELA, Categoria.colunas, Categorias._ID + "=" + id, null, null, null, null, null);
		if (c.getCount() > 0) {
			c.moveToFirst();						
			return setDadosCategoria(c);			
		}
		return null;
	}
	
	public static Cursor getCursor() {
		return getCursor(NOME_TABELA, Categoria.colunas);		
	}
	
	public static List<Categoria> listarCategoria() {		
		Cursor c = getCursor();
		List<Categoria> s = new ArrayList<Categoria>();		
		if (c.moveToFirst()) {			
			do {
				s.add(setDadosCategoria(c));
			} while (c.moveToNext());
		} 
		return s;
	}
	
	public static Categoria buscarCategoriaPorDescricao(String descricao) {
		Categoria categoria = null;
		try {
			Sistema sis = Sistema.getSistema();
			SQLiteDatabase db = sis.getConexaoBanco();			
			Cursor c = db.query(NOME_TABELA, Categoria.colunas, Categorias.DESCRICAO + "='" + descricao + "'", null, null, null, null);
			if (c.moveToNext()) {
				categoria = setDadosCategoria(c);								
			}
		} catch (SQLException e) {
			return null;
		}
		return categoria;
	}	
	
	public static Categoria buscarCategoriaUsuario(String descricao, Long usuario_id) {
		Categoria categoria = null;
		try {
			Sistema sis = Sistema.getSistema();
			SQLiteDatabase db = sis.getConexaoBanco();			
			Cursor c = db.query(NOME_TABELA, Categoria.colunas, Categorias.DESCRICAO + "='" + descricao + "'" + " and " +
															    Categorias.USUARIO_ID + "=" + usuario_id + "", null, null, null, null);
			if (c.moveToNext()) {
				categoria = setDadosCategoria(c);								
			}
		} catch (SQLException e) {
			return null;
		}
		return categoria;
	}	
	
	public static Categoria setDadosCategoria(Cursor c) {
		Categoria categoria = new Categoria();		
		categoria.setId(c.getLong(c.getColumnIndex(Categorias._ID)));
		categoria.setDescricao(c.getString(c.getColumnIndex(Categorias.DESCRICAO)));
		categoria.setUsuario(UsuarioDAO.buscarUsuario(c.getLong(c.getColumnIndex(Categorias.USUARIO_ID))));
		categoria.setPagamento_automatico(c.getString(c.getColumnIndex(Categorias.PAGAMENTO_AUTOMATICO)));
		return categoria;		
	}
	
	public static Cursor findLike(String[] colunas, String origem, String destino, String orderBy) {
		return findLike(NOME_TABELA, colunas, origem, destino, orderBy);
	}

	public static String getNomeTabela() {
		return NOME_TABELA;
	}
	
}
