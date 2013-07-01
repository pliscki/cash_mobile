package projetofinal.ftec.persistencia;

import java.util.ArrayList;
import java.util.List;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Alarme;
import projetofinal.ftec.modelo.Alarme.Alarmes;
import projetofinal.ftec.modelo.Sistema;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

public class AlarmeDAO extends BaseDAO {
	private static final String NOME_TABELA = "alarme";	
	
	public static long salvar(Alarme alarme) throws Exception {
		long id = alarme.getId();
		
		alarme.trim();
		
		try {
			alarme.check();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}					
		
		if (id != 0) {
			atualizar(alarme);
		} else {
			id = inserir(alarme);
		}
		return id;
		
	}
	
	public static long inserir(Alarme alarme) {
		ContentValues values = new ContentValues ();
		values.put(Alarmes.DESCRICAO, alarme.getDescricao());
		values.put(Alarmes.HORA_INICIAL, alarme.getHora_inicial());
		values.put(Alarmes.MIN_INICIAL, alarme.getMin_inicial());
		values.put(Alarmes.HORA_FINAL, alarme.getHora_final());
		values.put(Alarmes.MIN_FINAL, alarme.getMin_final());
		values.put(Alarmes.INTERVALO, alarme.getIntervalo());
		values.put(Alarmes.ATIVO, alarme.getAtivo());
		values.put(Alarmes.USUARIO_ID, alarme.getUsuario().getId());
		values.put(Alarmes.DIAS_ANTES_VENCIMENTO, alarme.getDias_antes_vencimento());
		long id = inserir(NOME_TABELA, values);
		return id;		
	}
	
	public static int atualizar(Alarme alarme) {
		ContentValues values = new ContentValues();
		values.put(Alarmes.DESCRICAO, alarme.getDescricao());
		values.put(Alarmes.HORA_INICIAL, alarme.getHora_inicial());
		values.put(Alarmes.MIN_INICIAL, alarme.getMin_inicial());
		values.put(Alarmes.HORA_FINAL, alarme.getHora_final());
		values.put(Alarmes.MIN_FINAL, alarme.getMin_final());
		values.put(Alarmes.INTERVALO, alarme.getIntervalo());
		values.put(Alarmes.ATIVO, alarme.getAtivo());
		values.put(Alarmes.USUARIO_ID, alarme.getUsuario().getId());
		values.put(Alarmes.DIAS_ANTES_VENCIMENTO, alarme.getDias_antes_vencimento());
		String _id = String.valueOf(alarme.getId());
		String where = Alarmes._ID + "=?";
		String[] whereArgs = new String[] { _id };
		int count = atualizar(NOME_TABELA, values, where, whereArgs);
		return count;
	}
	
	public static int deletar(long id) throws SQLiteConstraintException {
		int count;
		String where 		= Alarmes._ID + "=?";
		String _id 			= String.valueOf(id);
		String[] whereArgs 	= new String[] { _id };
		try {
			count 			= deletar(NOME_TABELA, where, whereArgs);
		} catch(SQLiteConstraintException e) {
			throw new SQLiteConstraintException(Integer.toString(R.string.msg_falha_excluir_alarme));
		}
		return count;		
	}
	
	public static Alarme buscarAlarme(long id) {
		Sistema sis = Sistema.getSistema();
		SQLiteDatabase db = sis.getConexaoBanco();		
		Cursor c = db.query(true, NOME_TABELA, Alarme.colunas, Alarmes._ID + "=" + id, null, null, null, null, null);
		if (c.getCount() > 0) {
			c.moveToFirst();						
			return setDadosAlarme(c);			
		}
		return null;
	}
	
	public static Cursor buscarAlarmesAtivos() {
		Sistema sis = Sistema.getSistema();
		SQLiteDatabase db = sis.getConexaoBanco();		
		return db.query(true, NOME_TABELA, Alarme.colunas, Alarmes.ALARME_ATIVO + "= 'S' ", null, null, null, null, null);
		
	}
	
	public static Cursor getCursor() {
		return getCursor(NOME_TABELA, Alarme.colunas);		
	}
	
	public static List<Alarme> listarAlarme() {		
		Cursor c = getCursor();
		List<Alarme> alarmes = new ArrayList<Alarme>();		
		if (c.moveToFirst()) {			
			do {
				alarmes.add(setDadosAlarme(c));
			} while (c.moveToNext());
		} 
		return alarmes;
	}	
	
	public static Alarme setDadosAlarme(Cursor c) {
		Alarme alarme = new Alarme();		
		alarme.setId(c.getLong(c.getColumnIndex(Alarmes._ID)));
		alarme.setDescricao(c.getString(c.getColumnIndex(Alarmes.DESCRICAO)));
		alarme.setHora_inicial(c.getInt(c.getColumnIndexOrThrow(Alarmes.HORA_INICIAL)));
		alarme.setMin_inicial(c.getInt(c.getColumnIndexOrThrow(Alarmes.MIN_INICIAL)));
		alarme.setHora_final(c.getInt(c.getColumnIndexOrThrow(Alarmes.HORA_FINAL)));
		alarme.setMin_final(c.getInt(c.getColumnIndexOrThrow(Alarmes.MIN_FINAL)));
		alarme.setIntervalo(c.getInt(c.getColumnIndexOrThrow(Alarmes.INTERVALO)));
		alarme.setAtivo(c.getString(c.getColumnIndexOrThrow(Alarmes.ATIVO)));
		alarme.setDias_antes_vencimento(c.getInt(c.getColumnIndexOrThrow(Alarmes.DIAS_ANTES_VENCIMENTO)));
		return alarme;		
	}
	
	public static Cursor findLike(String[] colunas, String origem, String destino, String orderBy) {
		return findLike(NOME_TABELA, colunas, origem, destino, orderBy);		
	}

	public static String getNomeTabela() {
		return NOME_TABELA;
	}
	
}
