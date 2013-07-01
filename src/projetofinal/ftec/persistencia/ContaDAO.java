package projetofinal.ftec.persistencia;

import java.util.ArrayList;
import java.util.List;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Conta;
import projetofinal.ftec.modelo.Conta.Contas;
import projetofinal.ftec.modelo.Realizado.Realizados;
import projetofinal.ftec.modelo.Sistema;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

public class ContaDAO extends BaseDAO {
	private static final String NOME_TABELA = "conta";	
	
	public static long salvar(Conta conta) throws Exception {
		long id = conta.getId();
		
		conta.trim();
		
		try {
			conta.check();
			if (id != 0) {
				atualizar(conta);
			} else {
				id = inserir(conta);
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}					
		
		
		return id;
		
	}
	
	public static long inserir(Conta conta) throws Exception {
		ContentValues values = new ContentValues ();
		values.put(Contas.DESCRICAO, conta.getDescricao());
		values.put(Contas.USUARIO_ID, conta.getUsuario().getId());
		
		long id = inserir(NOME_TABELA, values);
		if (id > 0 && conta.getSaldoInicial() > 0) {
			conta.setId(id);
			try {
				conta.gerarSaldoInicial();
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
			
		}
		return id;		
	} 
	
	public static int atualizar(Conta conta) {
		ContentValues values = new ContentValues();
		values.put(Contas.DESCRICAO, conta.getDescricao());
		values.put(Contas.USUARIO_ID, conta.getUsuario().getId());
		String _id = String.valueOf(conta.getId());
		String where = Contas._ID + "=?";
		String[] whereArgs = new String[] { _id };
		int count = atualizar(NOME_TABELA, values, where, whereArgs);
		return count;
	}
	
	public static int deletar(long id) throws SQLiteConstraintException {
		int count;
		String where 		= Contas._ID + "=?";
		String _id 			= String.valueOf(id);
		String[] whereArgs 	= new String[] { _id };
		try {
			count 			= deletar(NOME_TABELA, where, whereArgs);
		} catch(SQLiteConstraintException e) {
			throw new SQLiteConstraintException(Integer.toString(R.string.msg_falha_excluir_conta));
		}
		return count;		
	}
	
	public static Conta buscarConta(long id) {
		Sistema sis = Sistema.getSistema();
		SQLiteDatabase db = sis.getConexaoBanco();		
		Cursor c = db.query(true, NOME_TABELA, Conta.colunas, Contas._ID + "=" + id, null, null, null, null, null);
		if (c.getCount() > 0) {
			c.moveToFirst();						
			return setDadosConta(c);			
		}
		return null;
	}
	
	public static Cursor getCursor() {
		return getCursor(NOME_TABELA, Conta.colunas);		
	}
	
	public static List<Conta> listarConta() {		
		Cursor c = getCursor();
		List<Conta> contas = new ArrayList<Conta>();		
		if (c.moveToFirst()) {			
			do {
				contas.add(setDadosConta(c));
			} while (c.moveToNext());
		} 
		return contas;
	}
	
	public static Conta buscarContaPorDescricao(String descricao) {
		Conta conta = null;
		try {
			Sistema sis = Sistema.getSistema();
			SQLiteDatabase db = sis.getConexaoBanco();			
			Cursor c = db.query(NOME_TABELA, Conta.colunas, Contas.DESCRICAO + "='" + descricao + "'", null, null, null, null);
			if (c.moveToNext()) {
				conta = setDadosConta(c);								
			}
		} catch (SQLException e) {
			return null;
		}
		return conta;
	}	
	
	public static Conta buscarContaUsuario(String descricao, Long usuario_id) {
		Conta conta = null;
		try {
			Sistema sis = Sistema.getSistema();
			SQLiteDatabase db = sis.getConexaoBanco();			
			Cursor c = db.query(NOME_TABELA, Conta.colunas, Contas.DESCRICAO + "='" + descricao + "'" + " and " +
															Contas.USUARIO_ID + "=" + usuario_id + "", null, null, null, null);
			if (c.moveToNext()) {
				conta = setDadosConta(c);								
			}
		} catch (SQLException e) {
			return null;
		}
		return conta;
	}	
	
	public static Conta setDadosConta(Cursor c) {
		Conta conta = new Conta();		
		conta.setId(c.getLong(c.getColumnIndex(Contas._ID)));
		conta.setDescricao(c.getString(c.getColumnIndex(Contas.DESCRICAO)));
		conta.setUsuario(UsuarioDAO.buscarUsuario(c.getLong(c.getColumnIndex(Contas.USUARIO_ID))));		
		return conta;		
	}
	
	public static Cursor findLike(String[] colunas, String origem, String destino, String orderBy) {		 
		return findLike(NOME_TABELA, colunas, origem, destino, orderBy);		
	}

	public static String getNomeTabela() {
		return NOME_TABELA;
	}
	
	public static Cursor getContaSaldo() {
		SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
		qBuilder.setTables(getNomeTabela() + getRelacao(RealizadoDAO.getNomeTabela()));
		return query(qBuilder, 
				new String[] {Contas.CONTA_ID, 
							  Contas.CONTA_DESCRICAO, 
							  " 'Saldo: R$ ' || coalesce(sum(" + Realizados.REALIZADO_VAL_MOVIMENTO + " * (case " + 
							  Realizados.REALIZADO_TIPO_MOVIMENTO + " when 'P' then -1 else 1 end)), 0) as saldo_conta"} ,
							  null, null, Contas.CONTA_ID, null, null);
	}
	
	public static String getRelacao(String tabela) {
		if (tabela == "realizado") {
			return " LEFT OUTER JOIN " + RealizadoDAO.getNomeTabela() + " ON "  + Realizados.CONTA_ID + " = " + Contas.CONTA_ID + " ";
		}
		
		return "";
	}
	
	
	
}
