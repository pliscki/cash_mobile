package projetofinal.ftec.services;

import java.util.ArrayList;
import java.util.Calendar;

import projetofinal.ftec.R;
import projetofinal.ftec.gui.MainMenu;
import projetofinal.ftec.modelo.Alarme;
import projetofinal.ftec.modelo.Previsto.Previstos;
import projetofinal.ftec.modelo.Sistema;
import projetofinal.ftec.persistencia.AlarmeDAO;
import projetofinal.ftec.persistencia.PrevistoDAO;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;

public class MonitorContas extends Service implements Runnable {
	public static final String MONITOR_CONTAS = "monitor_contas";
	private Sistema sis;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		new Thread(this).start();
	}

	@Override
	public void run() {
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		sis = Sistema.getSistema();
		if (sis.iniciarAplicacao(this)) {
			sis.executarPagamentoAutomatico(this);
			verificarContasPendentes();
		}
		stopSelf();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	private void verificarContasPendentes() {
		Calendar horaAtual = Calendar.getInstance();
		Alarme alarme;
		ArrayList<String> listaPrevistos = new ArrayList<String>();
		
		Cursor cursorAlarme = AlarmeDAO.buscarAlarmesAtivos();
		if (cursorAlarme.moveToFirst()) {
			alarme = AlarmeDAO.setDadosAlarme(cursorAlarme);
			int minInicial = (alarme.getHora_inicial() * 60) + alarme.getMin_inicial();
			int minFinal = (alarme.getHora_final() * 60 ) + alarme.getMin_final();
			int minAtual = (horaAtual.get(Calendar.HOUR_OF_DAY) * 60) + horaAtual.get(Calendar.MINUTE);
			if (minInicial <= minAtual &&
				minFinal >= minAtual) {
				do {
					Cursor cursorPrevisto = PrevistoDAO.getPrevistoPendente(alarme);
					if (cursorPrevisto.moveToFirst()) {
						do {
							if (!listaPrevistos.contains(Long.toString(cursorPrevisto.getLong(cursorPrevisto.getColumnIndexOrThrow(Previstos._ID))))) {
								listaPrevistos.add(Long.toString(cursorPrevisto.getLong(cursorPrevisto.getColumnIndexOrThrow(Previstos._ID))));
							}
						} while (cursorPrevisto.moveToNext());
					}
					cursorPrevisto.close();
				} while (cursorAlarme.moveToNext());
				cursorAlarme.close();
			}
		}
		
		if (!listaPrevistos.isEmpty()) {
			CharSequence titulo = Integer.toString(listaPrevistos.size()) + " " + getResources().getString(R.string.notificacao_movimento_previsto);
			CharSequence mensagem = getResources().getString(R.string.clique_para_visualizar);
			CharSequence tickerText = getResources().getString(R.string.notificacao_movimento_previsto);
			Intent itPrevisto = new Intent(MonitorContas.this, MainMenu.class);
			Bundle extras = new Bundle();
			
			extras.putStringArrayList("LISTA_PREVISTOS", listaPrevistos);
			extras.putString("ORIGEM", MONITOR_CONTAS);
			itPrevisto.replaceExtras(extras);
			
			
			criarNotificacao(this, tickerText, titulo, mensagem, itPrevisto);
		}
	}
	
	protected void criarNotificacao(Context context, CharSequence mensagemBarraStatus, CharSequence titulo,CharSequence mensagem, Intent it) {
		// Recupera o serviço do NotificationManager
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nm.cancel(R.string.app_name);

		Notification n = new Notification(R.drawable.ic_launcher, mensagemBarraStatus, System.currentTimeMillis());

		// PendingIntent para executar a Activity se o usuário selecionar a
		// notificação
		PendingIntent p = PendingIntent.getActivity(this, 0, it, PendingIntent.FLAG_UPDATE_CURRENT);

		// Informações
		n.setLatestEventInfo(this, titulo, mensagem, p);

		// Precisa de permissão: <uses-permission
		// android:name="android.permission.VIBRATE" />
		// espera 100ms e vibra por 250ms, depois espera por 100 ms e vibra por
		// 500ms.
		n.vibrate = new long[] { 100, 250, 100, 500 };

		// id (numero único) que identifica esta notificação
		nm.notify(R.string.app_name, n);
	}

}
