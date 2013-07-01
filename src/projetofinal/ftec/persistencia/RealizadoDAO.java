package projetofinal.ftec.persistencia;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Alarme.Alarmes;
import projetofinal.ftec.modelo.Categoria.Categorias;
import projetofinal.ftec.modelo.Conta.Contas;
import projetofinal.ftec.modelo.Contato.Contatos;
import projetofinal.ftec.modelo.Realizado;
import projetofinal.ftec.modelo.Realizado.Realizados;
import projetofinal.ftec.modelo.Sistema;
import projetofinal.ftec.utils.CustomDateUtils;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

public class RealizadoDAO extends BaseDAO {
	private static final String NOME_TABELA = "realizado";	
	
	public static long salvar(Realizado realizado) throws Exception {
		long id = realizado.getId();
		
		realizado.trim();
		
		try {
			realizado.check();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}					
		
		if (id != 0) {
			atualizar(realizado);
		} else {
			id = inserir(realizado);
		}
		return id;
		
	}
	
	public static long inserir(Realizado realizado) {
		ContentValues values = new ContentValues ();
		values.put(Realizados.DESCRICAO, realizado.getDescricao());
		values.put(Realizados.USUARIO_ID, realizado.getUsuario().getId());
		if (realizado.getConta() != null) {
			values.put(Realizados.CONTA_ID, realizado.getConta().getId());
		} else {
			values.putNull(Realizados.CONTA_ID);
		}
		if (realizado.getContato() != null) {
			values.put(Realizados.CONTATO_ID, realizado.getContato().getId());
		} else {
			values.putNull(Realizados.CONTATO_ID);
		}
		if (realizado.getCategoria() != null) { 
			values.put(Realizados.CATEGORIA_ID, realizado.getCategoria().getId());
		} else {
			values.putNull(Realizados.CATEGORIA_ID);
		}
		values.put(Realizados.DT_MOVIMENTO, CustomDateUtils.toSQLDate(realizado.getDt_movimento()));
		values.put(Realizados.VAL_MOVIMENTO, realizado.getVal_movimento());
		values.put(Realizados.TIPO_MOVIMENTO, realizado.getTipo_movimento());
		if (realizado.getPrevisto() != null) {
			values.put(Realizados.PREVISTO_ID, realizado.getPrevisto().getId());
		} else {
			values.putNull(Realizados.PREVISTO_ID);
		}
		long id = inserir(NOME_TABELA, values);
		return id;		
	} 
	
	public static int atualizar(Realizado realizado) {
		ContentValues values = new ContentValues();
		values.put(Realizados.DESCRICAO, realizado.getDescricao());
		values.put(Realizados.USUARIO_ID, realizado.getUsuario().getId());
		if (realizado.getConta() != null) {
			values.put(Realizados.CONTA_ID, realizado.getConta().getId());
		} else {
			values.putNull(Realizados.CONTA_ID);
		}
		if (realizado.getContato() != null) {
			values.put(Realizados.CONTATO_ID, realizado.getContato().getId());
		} else {
			values.putNull(Realizados.CONTATO_ID);
		}
		if (realizado.getCategoria() != null) { 
			values.put(Realizados.CATEGORIA_ID, realizado.getCategoria().getId());
		} else {
			values.putNull(Realizados.CATEGORIA_ID);
		}
		values.put(Realizados.DT_MOVIMENTO, CustomDateUtils.toSQLDate(realizado.getDt_movimento()));
		values.put(Realizados.VAL_MOVIMENTO, realizado.getVal_movimento());
		values.put(Realizados.TIPO_MOVIMENTO, realizado.getTipo_movimento());
		if (realizado.getPrevisto() != null) {
			values.put(Realizados.PREVISTO_ID, realizado.getPrevisto().getId());
		} else {
			values.putNull(Realizados.PREVISTO_ID);
		}
		String _id = String.valueOf(realizado.getId());
		String where = Realizados._ID + "=?";
		String[] whereArgs = new String[] { _id };
		int count = atualizar(NOME_TABELA, values, where, whereArgs);
		return count;
	}
	
	public static int deletar(long id) throws SQLiteConstraintException {
		int count;
		String where 		= Realizados._ID + "=?";
		String _id 			= String.valueOf(id);
		String[] whereArgs 	= new String[] { _id };
		try {
			count 			= deletar(NOME_TABELA, where, whereArgs);
		} catch(SQLiteConstraintException e) {
			throw new SQLiteConstraintException(Integer.toString(R.string.msg_falha_excluir_realizado));
		}
		return count;		
	}
	
	public static Realizado buscarRealizado(long id) {
		Sistema sis = Sistema.getSistema();
		SQLiteDatabase db = sis.getConexaoBanco();		
		Cursor c = db.query(true, NOME_TABELA, Realizado.colunas, Realizados._ID + "=" + id, null, null, null, null, null);
		if (c.getCount() > 0) {
			c.moveToFirst();						
			return setDadosRealizado(c);			
		}
		return null;
	}
	
	public static Cursor getCursor() {
		return getCursor(NOME_TABELA, Realizado.colunas);		
	}
	
	public static List<Realizado> listarRealizado() {		
		Cursor c = getCursor();
		List<Realizado> realizados = new ArrayList<Realizado>();		
		if (c.moveToFirst()) {			
			do {
				realizados.add(setDadosRealizado(c));
			} while (c.moveToNext());
		} 
		return realizados;
	}
		
	
	public static Realizado buscarRealizadoPorUsuario(String descricao, Long usuario_id) {
		Realizado realizado = null;
		try {
			Sistema sis = Sistema.getSistema();
			SQLiteDatabase db = sis.getConexaoBanco();			
			Cursor c = db.query(NOME_TABELA, Realizado.colunas, Realizados.DESCRICAO + "='" + descricao + "'" + " and " +
											 					Realizados.USUARIO_ID + "=" + usuario_id + "", null, null, null, null);
			if (c.moveToNext()) {
				realizado = setDadosRealizado(c);								
			}
		} catch (SQLException e) {
			return null;
		}
		return realizado;
	}
	
	public static Realizado buscarRealizadoPorPrevisto(Long previsto_id) {
		Realizado realizado = null;
		try {
			Sistema sis = Sistema.getSistema();
			SQLiteDatabase db = sis.getConexaoBanco();			
			Cursor c = db.query(NOME_TABELA, Realizado.colunas, Realizados.PREVISTO_ID + "=" + previsto_id + "", null, null, null, null);
			if (c.moveToNext()) {
				realizado = setDadosRealizado(c);								
			}
		} catch (SQLException e) {
			return null;
		}
		return realizado;
	}
	
	public static Realizado setDadosRealizado(Cursor c) {
		Realizado realizado = new Realizado();		
		realizado.setId(c.getLong(c.getColumnIndex(Realizados._ID)));
		realizado.setDescricao(c.getString(c.getColumnIndex(Realizados.DESCRICAO)));
		realizado.setUsuario(UsuarioDAO.buscarUsuario(c.getLong(c.getColumnIndex(Realizados.USUARIO_ID))));
		realizado.setConta(ContaDAO.buscarConta(c.getLong(c.getColumnIndexOrThrow(Realizados.CONTA_ID))));
		realizado.setContato(ContatoDAO.buscarContato(c.getLong(c.getColumnIndexOrThrow(Realizados.CONTATO_ID))));
		realizado.setCategoria(CategoriaDAO.buscarCategoria(c.getLong(c.getColumnIndexOrThrow(Realizados.CATEGORIA_ID))));
		try {
			realizado.setDt_movimento(CustomDateUtils.toCalendar(c.getString(c.getColumnIndexOrThrow(Realizados.DT_MOVIMENTO))));
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		realizado.setVal_movimento(c.getDouble(c.getColumnIndexOrThrow(Realizados.VAL_MOVIMENTO)));
		realizado.setTipo_movimento(c.getString(c.getColumnIndexOrThrow(Realizados.TIPO_MOVIMENTO)));
		realizado.setPrevisto(PrevistoDAO.buscarPrevisto(c.getLong(c.getColumnIndexOrThrow(Realizados.PREVISTO_ID))));
		
		//TODO setar campos de tipo data
		return realizado;		
	}
	
	public static Cursor findLike(String[] colunas, String origem, String destino, String orderBy) {		 
		return findLike(NOME_TABELA, colunas, origem, destino, orderBy);		
	}

	public static String getNomeTabela() {
		return NOME_TABELA;
	}
	
	public static String getRelacao(String tabela) {
		if (tabela == "usuario") {
			return " INNER JOIN " + UsuarioDAO.getNomeTabela() + " ON " + Alarmes.ALARME_ID + " = " + Realizados.REALIZADO_USUARIO_ID + " ";
		} else if (tabela == "conta") {
			return " INNER JOIN " + ContaDAO.getNomeTabela() + " ON " + Contas.CONTA_ID + " = " + Realizados.REALIZADO_CONTA_ID + " ";			
		} else if (tabela == "contato") {
			return " LEFT OUTER JOIN " + ContatoDAO.getNomeTabela() + " ON "  + Contatos.CONTATO_ID + " = " + Realizados.REALIZADO_CONTATO_ID + " ";
		} else if (tabela == "categoria") {
			return " LEFT OUTER JOIN " + CategoriaDAO.getNomeTabela() + " ON " + Categorias.CATEGORIA_ID + " = " + Realizados.REALIZADO_CATEGORIA_ID + " ";
		}
		
		return "";
	}
	
	
	
}
