package projetofinal.ftec.persistencia;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Alarme;
import projetofinal.ftec.modelo.Alarme.Alarmes;
import projetofinal.ftec.modelo.Categoria.Categorias;
import projetofinal.ftec.modelo.Conta.Contas;
import projetofinal.ftec.modelo.Contato.Contatos;
import projetofinal.ftec.modelo.Previsto;
import projetofinal.ftec.modelo.Previsto.Previstos;
import projetofinal.ftec.modelo.Realizado.Realizados;
import projetofinal.ftec.modelo.Sistema;
import projetofinal.ftec.utils.CustomDateUtils;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

public class PrevistoDAO extends BaseDAO {
	private static final String NOME_TABELA = "previsto";	
	
	public static long salvar(Previsto previsto) throws Exception {
		long id = previsto.getId();
		
		previsto.trim();
		
		try {
			previsto.check();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}					
		
		if (id != 0) {
			atualizar(previsto);
		} else {
			id = inserir(previsto);
		}
		return id;
		
	}
	
	public static long inserir(Previsto previsto) {
		ContentValues values = new ContentValues ();
		values.put(Previstos.DESCRICAO, previsto.getDescricao());
		values.put(Previstos.USUARIO_ID, previsto.getUsuario().getId());
		if (previsto.getConta() != null) {
			values.put(Previstos.CONTA_ID, previsto.getConta().getId());
		} else {
			values.putNull(Previstos.CONTA_ID);
		}
		if (previsto.getContato() != null) {
			values.put(Previstos.CONTATO_ID, previsto.getContato().getId());
		} else {
			values.putNull(Previstos.CONTATO_ID);
		}
		if (previsto.getCategoria() != null) { 
			values.put(Previstos.CATEGORIA_ID, previsto.getCategoria().getId());
		} else {
			values.putNull(Previstos.CATEGORIA_ID);
		}
		if (previsto.getAlarme() != null) {
			values.put(Previstos.ALARME_ID, previsto.getAlarme().getId());
		} else {
			values.putNull(Previstos.ALARME_ID);
		}
		values.put(Previstos.DT_EMISSAO, CustomDateUtils.toSQLDate(previsto.getDt_emissao()));
		values.put(Previstos.DT_VENCIMENTO, CustomDateUtils.toSQLDate(previsto.getDt_vencimento()));
		values.put(Previstos.DT_ULT_PAGAMENTO, CustomDateUtils.toSQLDate(previsto.getDt_ult_pagamento()));
		values.put(Previstos.VAL_PREVISTO, previsto.getVal_previsto());
		values.put(Previstos.VAL_SALDO, previsto.getVal_saldo());
		values.put(Previstos.TIPO_MOVIMENTO, previsto.getTipo_movimento());
		values.put(Previstos.PAGAMENTO_AUTOMATICO, previsto.getPagamento_automatico());
		
		
		long id = inserir(NOME_TABELA, values);
		return id;		
	} 
	
	public static int atualizar(Previsto previsto) {
		ContentValues values = new ContentValues();
		values.put(Previstos.DESCRICAO, previsto.getDescricao());
		values.put(Previstos.USUARIO_ID, previsto.getUsuario().getId());
		if (previsto.getConta() != null) {
			values.put(Previstos.CONTA_ID, previsto.getConta().getId());
		} else {
			values.putNull(Previstos.CONTA_ID);
		}
		if (previsto.getContato() != null) {
			values.put(Previstos.CONTATO_ID, previsto.getContato().getId());
		} else {
			values.putNull(Previstos.CONTATO_ID);
		}
		if (previsto.getCategoria() != null) { 
			values.put(Previstos.CATEGORIA_ID, previsto.getCategoria().getId());
		} else {
			values.putNull(Previstos.CATEGORIA_ID);
		}
		if (previsto.getAlarme() != null) {
			values.put(Previstos.ALARME_ID, previsto.getAlarme().getId());
		} else {
			values.putNull(Previstos.ALARME_ID);
		}
		values.put(Previstos.DT_EMISSAO, CustomDateUtils.toSQLDate(previsto.getDt_emissao()));
		values.put(Previstos.DT_VENCIMENTO, CustomDateUtils.toSQLDate(previsto.getDt_vencimento()));
		values.put(Previstos.DT_ULT_PAGAMENTO, CustomDateUtils.toSQLDate(previsto.getDt_ult_pagamento()));
		values.put(Previstos.VAL_PREVISTO, previsto.getVal_previsto());
		values.put(Previstos.VAL_SALDO, previsto.getVal_saldo());
		values.put(Previstos.TIPO_MOVIMENTO, previsto.getTipo_movimento());
		values.put(Previstos.PAGAMENTO_AUTOMATICO, previsto.getPagamento_automatico());
		String _id = String.valueOf(previsto.getId());
		String where = Previstos._ID + "=?";
		String[] whereArgs = new String[] { _id };
		int count = atualizar(NOME_TABELA, values, where, whereArgs);
		return count;
	}
	
	public static int deletar(long id) throws SQLiteConstraintException {
		int count;
		String where 		= Previstos._ID + "=?";
		String _id 			= String.valueOf(id);
		String[] whereArgs 	= new String[] { _id };
		try {
			count 			= deletar(NOME_TABELA, where, whereArgs);
		} catch(SQLiteConstraintException e) {
			throw new SQLiteConstraintException(Integer.toString(R.string.msg_falha_excluir_previsto));
		}
		return count;		
	}
	
	public static Previsto buscarPrevisto(long id) {
		Sistema sis = Sistema.getSistema();
		SQLiteDatabase db = sis.getConexaoBanco();		
		Cursor c = db.query(true, NOME_TABELA, Previsto.colunas, Previstos._ID + "=" + id, null, null, null, null, null);		
		if (c.getCount() > 0) {
			c.moveToFirst();						
			return setDadosPrevisto(c);			
		}
		return null;
	}
	
	public static Cursor getCursor() {
		return getCursor(NOME_TABELA, Previsto.colunas);		
	}
	
	public static List<Previsto> listarPrevisto() {		
		Cursor c = getCursor();
		List<Previsto> previstos = new ArrayList<Previsto>();		
		if (c.moveToFirst()) {			
			do {
				previstos.add(setDadosPrevisto(c));
			} while (c.moveToNext());
		} 
		return previstos;
	}
		
	
	public static Previsto buscarPrevistoPorUsuario(String descricao, Long usuario_id) {
		Previsto previsto = null;
		try {
			Sistema sis = Sistema.getSistema();
			SQLiteDatabase db = sis.getConexaoBanco();			
			Cursor c = db.query(NOME_TABELA, Previsto.colunas, Previstos.DESCRICAO + "='" + descricao + "'" + " and " +
										     Previstos.USUARIO_ID + "=" + usuario_id + "", null, null, null, null);
			if (c.moveToNext()) {
				previsto = setDadosPrevisto(c);								
			}
		} catch (SQLException e) {
			return null;
		}
		return previsto;
	}	
	
	public static Previsto setDadosPrevisto(Cursor c) {
		Previsto previsto = new Previsto();		
		previsto.setId(c.getLong(c.getColumnIndex(Previstos._ID)));
		previsto.setDescricao(c.getString(c.getColumnIndex(Previstos.DESCRICAO)));
		previsto.setUsuario(UsuarioDAO.buscarUsuario(c.getLong(c.getColumnIndex(Previstos.USUARIO_ID))));
		previsto.setConta(ContaDAO.buscarConta(c.getLong(c.getColumnIndexOrThrow(Previstos.CONTA_ID))));
		previsto.setContato(ContatoDAO.buscarContato(c.getLong(c.getColumnIndexOrThrow(Previstos.CONTATO_ID))));
		previsto.setCategoria(CategoriaDAO.buscarCategoria(c.getLong(c.getColumnIndexOrThrow(Previstos.CATEGORIA_ID))));
		previsto.setAlarme(AlarmeDAO.buscarAlarme(c.getLong(c.getColumnIndexOrThrow(Previstos.ALARME_ID))));
		try {
			previsto.setDt_emissao(CustomDateUtils.toCalendar(c.getString(c.getColumnIndexOrThrow(Previstos.DT_EMISSAO))));
			previsto.setDt_vencimento(CustomDateUtils.toCalendar(c.getString(c.getColumnIndexOrThrow(Previstos.DT_VENCIMENTO))));
			previsto.setDt_ult_pagamento(CustomDateUtils.toCalendar(c.getString(c.getColumnIndexOrThrow(Previstos.DT_ULT_PAGAMENTO))));
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		previsto.setVal_previsto(c.getDouble(c.getColumnIndexOrThrow(Previstos.VAL_PREVISTO)));
		previsto.setVal_saldo(c.getDouble(c.getColumnIndexOrThrow(Previstos.VAL_SALDO)));
		previsto.setTipo_movimento(c.getString(c.getColumnIndexOrThrow(Previstos.TIPO_MOVIMENTO)));
		previsto.setPagamento_automatico(c.getString(c.getColumnIndexOrThrow(Previstos.PAGAMENTO_AUTOMATICO)));
		
		//TODO setar campos de tipo data
		return previsto;		
	}
	
	public static Cursor findLike(String[] colunas, String origem, String destino, String orderBy) {		 
		return findLike(NOME_TABELA, colunas, origem, destino, orderBy);		
	}
	
	public static Cursor getPrevistoPendente(Alarme alarme) {
		int dias_antes = 0;
		long id_alarme = 0;
		if ( alarme != null) {
			dias_antes = alarme.getDias_antes_vencimento();
			id_alarme = alarme.getId();
		}
		Calendar dataCorte = Calendar.getInstance();
		dataCorte.add(Calendar.DATE, dias_antes);
		SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
		qBuilder.setTables(getNomeTabela());
		return query(qBuilder, Previsto.colunas, 
				Previstos.ALARME_ID 		+ " = " 	+ Long.toString(id_alarme) 				+ "  AND " +
 				Previstos.DT_VENCIMENTO 	+ " <= '" 	+ CustomDateUtils.toSQLDate(dataCorte) 	+ "' AND " + 
				" not exists ( select 1 from " + RealizadoDAO.getNomeTabela() + " where " +
				Realizados.REALIZADO_PREVISTO_ID + " = " + Previstos.PREVISTO_ID + " )", null, null, null, null);
	}
	
	public static Cursor getPrevistoPagamentoAuto() {
		Calendar dataVencimento = Calendar.getInstance();
		SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
		qBuilder.setTables(getNomeTabela());
		return query(qBuilder, Previsto.colunas, 
 				Previstos.DT_VENCIMENTO 	+ " <= '" 	+ CustomDateUtils.toSQLDate(dataVencimento) 	+ "' AND " +
 				Previstos.PAGAMENTO_AUTOMATICO + " = 'S' AND " +
				" not exists ( select 1 from " + RealizadoDAO.getNomeTabela() + " where " +
				Realizados.REALIZADO_PREVISTO_ID + " = " + Previstos.PREVISTO_ID + " )", null, null, null, null);
		
	}

	public static String getNomeTabela() {
		return NOME_TABELA;
	}
	
	public static void replicarPrevisto(Previsto previstoBase, int quantidade, int intervalo)  throws Exception{
		ArrayList<Previsto> previsto = new ArrayList<Previsto>();
		
		for (int i = 0;i<quantidade;i++) {
			
			previsto.add(i, previstoBase);

			if (intervalo == 0) {
				previsto.get(i).getDt_vencimento().add(Calendar.DATE, 1 );
			} else if (intervalo == 1) {
				previsto.get(i).getDt_vencimento().add(Calendar.DATE, 7);
			} else if (intervalo == 2) {
				previsto.get(i).getDt_vencimento().add(Calendar.MONTH, 1);
			} else if (intervalo == 3) {
				previsto.get(i).getDt_vencimento().add(Calendar.YEAR, 1);
			}
			try {
				salvar(previsto.get(i));
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
		}
		
	}
	
	public static String getRelacao(String tabela) {
		if (tabela == "usuario") {
			return " INNER JOIN " + UsuarioDAO.getNomeTabela() + " ON " + Alarmes.ALARME_ID + " = " + Previstos.PREVISTO_USUARIO_ID + " ";
		} else if (tabela == "conta") {
			return " INNER JOIN " + ContaDAO.getNomeTabela() + " ON " + Contas.CONTA_ID + " = " + Previstos.PREVISTO_CONTA_ID + " ";			
		} else if (tabela == "contato") {
			return " LEFT OUTER JOIN " + ContatoDAO.getNomeTabela() + " ON "  + Contatos.CONTATO_ID + " = " + Previstos.PREVISTO_CONTATO_ID + " ";
		} else if (tabela == "categoria") {
			return " LEFT OUTER JOIN " + CategoriaDAO.getNomeTabela() + " ON " + Categorias.CATEGORIA_ID + " = " + Previstos.PREVISTO_CATEGORIA_ID + " ";
		} else if (tabela == "alarme") {
			return " LEFT OUTER JOIN " + AlarmeDAO.getNomeTabela() + " ON " + Alarmes.ALARME_ID + " = " + Previstos.ALARME_ID + " ";
		}
		
		return "";
	}
	
	
	
}
