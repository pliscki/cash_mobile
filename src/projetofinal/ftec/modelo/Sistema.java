package projetofinal.ftec.modelo;

import java.util.Calendar;
import java.util.List;

import projetofinal.ftec.R;
import projetofinal.ftec.persistencia.AlarmeDAO;
import projetofinal.ftec.persistencia.DbHelper;
import projetofinal.ftec.persistencia.PrevistoDAO;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class Sistema {	
	private static Sistema sis;
	private SQLiteDatabase db;
	private DbHelper dbHelper;
	private Usuario usuarioLogado;
	
	public  Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public  void setUsuarioLogado(Usuario usuarioLogado) {
		if (usuarioLogado != null) {
			this.usuarioLogado = usuarioLogado;
		}
	}

	public static Sistema getSistema(){
		if (sis == null) {
			synchronized (Sistema.class) {
				sis = new Sistema();				
			}
		}
		return sis;
	}
	
	public boolean iniciarAplicacao(Context ctx) {
		dbHelper = DbHelper.getDbHelper(ctx);
		db = dbHelper.getDataBase(ctx);
		if (db == null) {			
			return false;
		}		 
		return true;
	}
	
	public SQLiteDatabase getConexaoBanco() {
		return db;
	}
	
	public void setConexaoBanco(SQLiteDatabase db) {
		this.db = db;
	}
	
	public void fechaConexaoBanco(Context ctx) {
		iniciarAlarmes(ctx);
		
		if (db != null) {
			db.close();
		} 
		if (dbHelper != null) {
			dbHelper.fechar();
		}
		
		db = null;
		dbHelper = null;
		
	}
	
	public void iniciarAlarmes(Context ctx) {
		List<Alarme> alarmes = AlarmeDAO.listarAlarme();
		
		Calendar horaAlarme = Calendar.getInstance();
		Intent it = new Intent("MONITOR_PREVISTO");
		PendingIntent op = PendingIntent.getService(ctx, 0, it, 0);
		
		AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
		am.cancel(op);
		
		for (int i = 0; i<alarmes.size();i++) {
			if (alarmes.get(i).getAtivo().compareTo("S") == 0) {
				horaAlarme.add(Calendar.MINUTE, alarmes.get(i).getIntervalo());
				am.setInexactRepeating(AlarmManager.RTC_WAKEUP,horaAlarme.getTimeInMillis(), alarmes.get(i).getId() * 60 * 1000, op);
			}
		}
		
	}
	
	public void executarPagamentoAutomatico(Context ctx) {
		Cursor c = PrevistoDAO.getPrevistoPagamentoAuto();
		Log.w("DEBUG", "pagamento automatico");
		if (c.moveToFirst()) {
			do {
				Previsto previsto = PrevistoDAO.setDadosPrevisto(c);
				try {
					previsto.baixarTitulo(previsto.getConta(), previsto.getVal_previsto(), previsto.getDt_vencimento());
				} catch (Exception e) {
					Log.e(ctx.getResources().getString(R.string.app_name), e.getMessage());
					e.printStackTrace();
				}
			} while (c.moveToNext());
		}
	}
}
