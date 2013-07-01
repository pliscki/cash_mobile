package projetofinal.ftec.persistencia;

import projetofinal.ftec.modelo.Sistema;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

public class BaseDAO {

	
	protected static long inserir(String nomeTabela, ContentValues valores) {
		Sistema sis = Sistema.getSistema();
		SQLiteDatabase db = sis.getConexaoBanco();		
		long id = db.insert(nomeTabela, "", valores);
		return id;
	}
	
	protected static int atualizar(String nomeTabela, ContentValues valores, String where, String[] whereArgs) {
		Sistema sis = Sistema.getSistema();
		SQLiteDatabase db = sis.getConexaoBanco();		
		int count = db.update(nomeTabela, valores, where, whereArgs);
		return count;
	}	
	
	
	protected static int deletar(String nomeTabela, String where, String[] whereArgs) throws SQLiteConstraintException {
		int count;
		Sistema sis = Sistema.getSistema();
		SQLiteDatabase db = sis.getConexaoBanco();
		try {
			count = db.delete(nomeTabela, where, whereArgs);
		} catch(SQLiteConstraintException e) {
			throw new SQLiteConstraintException(e.getMessage());
		}
		return count;
	}	
		
	protected static Cursor getCursor(String nomeTabela, String[] colunas) {
		try {
			Sistema sis = Sistema.getSistema();
			SQLiteDatabase db = sis.getConexaoBanco();			
			return db.query(nomeTabela, colunas, null, null, null, null, null);
		} catch (SQLException e) {
			return null;
		}		
	}	
	
	public static Cursor query(SQLiteQueryBuilder queryBuilder, String[] projection, String selection,
			String[] selectionArgs, String groupBy, String having, String orderBy) {
		Sistema sis = Sistema.getSistema();
		SQLiteDatabase db = sis.getConexaoBanco();
		Cursor c = queryBuilder.query(db, projection, selection, selectionArgs, groupBy, having, orderBy);
		return c;
	}	
	
	protected static Cursor findLike(String nomeTabela, String[] colunas, String origem, String destino, String orderBy) {
		SQLiteQueryBuilder query = new SQLiteQueryBuilder();
		query.setTables(nomeTabela);
		return query(query, colunas, origem + " like " + "'%" + destino + "%'", null, null, null, orderBy);		
	}
	

}
