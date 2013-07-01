package projetofinal.ftec.gui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Conta.Contas;
import projetofinal.ftec.modelo.Contato.Contatos;
import projetofinal.ftec.modelo.Previsto.Previstos;
import projetofinal.ftec.modelo.Realizado.Realizados;
import projetofinal.ftec.persistencia.ContaDAO;
import projetofinal.ftec.persistencia.ContatoDAO;
import projetofinal.ftec.persistencia.PrevistoDAO;
import projetofinal.ftec.persistencia.RealizadoDAO;
import projetofinal.ftec.services.MonitorContas;
import projetofinal.ftec.utils.CustomDateUtils;
import projetofinal.ftec.utils.CustomDecimalUtils;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class PrevistoPesquisar extends ListActivity {
	
	private Cursor cursor;
	private PrevistoAdapter adaptador;
	private Bundle extras;
	private int ordemSelecionada;
	protected static final int CRIAR = 1;
	protected static final int FILTRAR = 2;
	protected static final int ORDENAR = 3;
	protected static final int DIALOG_ORDENAR = 4;
	protected static final int INTENT_FILTROS = 5;
	protected static final int DIALOG_ACOES = 6;
	protected static final int DLG_DELETAR = 7;
	
	private Calendar dtVencIni;
	private Calendar dtVencFin;
	private Calendar dtEmisIni;
	private Calendar dtEmisFin;
	private long categoria_id;
	private long conta_id;
	private long contato_id;
	private Cursor cursorLista;
	private String tituloDialogAcao;
	private boolean baixados;
	private boolean pendentes;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		extras = getIntent().getExtras();
		
		if (extras != null && 
			extras.containsKey("ORIGEM")) {
			if (extras.getString("ORIGEM").compareTo(MonitorContas.MONITOR_CONTAS) == 0) {
				NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				nm.cancel(R.string.app_name);
				setTitle(R.string.notificacao_movimento_previsto);
			}
		}
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.list_main_simples);
		
		dtVencIni = Calendar.getInstance();
		dtVencFin = Calendar.getInstance();
		dtVencFin.add(Calendar.MONTH, 1);
		dtVencIni.add(Calendar.MONTH, -1);
		pendentes = true;
		openQuery();
	}
	
	
	public void iniciarAdaptador(Cursor c) {
		if (cursor != null) {
			stopManagingCursor(cursor);
		}
		if (c != null) {
			startManagingCursor(c);
			adaptador = new PrevistoAdapter(this, c);
			setListAdapter(adaptador);
		}
	}
	
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		cursorLista = (Cursor) adaptador.getItem(position);
		
		tituloDialogAcao = getResources().getString(R.string.opcoes);
		if (cursorLista != null) { 
			tituloDialogAcao = cursorLista.getString(cursorLista.getColumnIndexOrThrow(Previstos.GET_DESCRICAO)) + " - " +
					getResources().getString(R.string.sigla_moeda) + " " + CustomDecimalUtils.format(cursorLista.getDouble(cursorLista.getColumnIndexOrThrow(Previstos.GET_VAL_PREVISTO)));
			
		}
		showDialog(DIALOG_ACOES);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, CRIAR, 0, R.string.criar).setIcon(android.R.drawable.ic_menu_add);
		menu.add(0, FILTRAR, 0, R.string.filtrar).setIcon(android.R.drawable.ic_menu_search);
		menu.add(0, ORDENAR, 0, R.string.ordenar).setIcon(android.R.drawable.ic_menu_sort_by_size);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {		
			case CRIAR:
				novo();
				break;
			case ORDENAR:
				showDialog(DIALOG_ORDENAR);
				break;
			case FILTRAR:
				Intent it = new Intent(PrevistoPesquisar.this, PrevistoFiltrar.class);
				if (dtEmisIni != null && dtEmisFin != null) {
					it.putExtra("EMIS_INI", dtEmisIni.getTimeInMillis());
					it.putExtra("EMIS_FIN", dtEmisFin.getTimeInMillis());
				}
				if (dtVencIni != null && dtVencFin != null) {
					it.putExtra("VENC_INI", dtVencIni.getTimeInMillis());
					it.putExtra("VENC_FIN", dtVencFin.getTimeInMillis());
				}
				if (categoria_id != 0) {
					it.putExtra("CATEGORIA_ID", categoria_id);
				}
				if (conta_id != 0) {
					it.putExtra("CONTA_ID", conta_id);
				}
				if (contato_id != 0) {
					it.putExtra("CONTATO_ID", contato_id);
				}
				it.putExtra("BAIXADOS", baixados);
				it.putExtra("PENDENTES", pendentes);
				startActivityForResult(it, INTENT_FILTROS);
				break;
		}
		return true;
	}
	
	public void novo() {
		Intent it = new Intent(this, PrevistoEditar.class);
		startActivity(it);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
	        case DIALOG_ORDENAR:
	        	return new AlertDialog.Builder(PrevistoPesquisar.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.ordenar_por)
                .setSingleChoiceItems(R.array.ordem_previsto, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	ordemSelecionada = whichButton;
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	openQuery();
                    }
                })
                .setNegativeButton(R.string.cancelar, null)
               .create();
	        case DIALOG_ACOES:
	        	return new AlertDialog.Builder(PrevistoPesquisar.this)
	        	.setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(tituloDialogAcao)
                .setItems(R.array.acoes_previsto, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						aplicarAcao(which);
					}
				})
                .setNegativeButton(R.string.cancelar, null)
               .create();
	        case DLG_DELETAR:
	        	return new AlertDialog.Builder(PrevistoPesquisar.this)
	        	.setIcon(android.R.drawable.ic_dialog_alert)
	        	.setTitle(R.string.excluir)
	        	.setMessage(R.string.msg_excluir_previsto)
	        	.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						deletar();
					}
				})
				.setNegativeButton(R.string.cancelar, null)
				.create();
	    }
	    return null;
	}
	
	
	protected void aplicarAcao(int acao) {
		Intent it;
		switch (acao) {
		case 0:
			it = new Intent(PrevistoPesquisar.this, BaixarTitulo.class);
			it.putExtra(Previstos._ID, cursorLista.getLong(cursorLista.getColumnIndexOrThrow(Previstos.PREVISTO_ID)));
			startActivity(it);
			break;
		case 1:
			it = new Intent(PrevistoPesquisar.this, PrevistoView.class);
			it.putExtra(Previstos._ID, cursorLista.getLong(cursorLista.getColumnIndexOrThrow(Previstos.PREVISTO_ID)));
			startActivity(it);
			break;
		case 2:
			it = new Intent(PrevistoPesquisar.this, PrevistoEditar.class);
			it.putExtra(Previstos._ID, cursorLista.getLong(cursorLista.getColumnIndexOrThrow(Previstos.PREVISTO_ID)));
			startActivity(it);
			break;
		case 3:
			showDialog(DLG_DELETAR);
			break;
		case 4:
			it = new Intent(PrevistoPesquisar.this, PrevistoEditar.class);
			it.putExtra(Previstos._ID, cursorLista.getLong(cursorLista.getColumnIndexOrThrow(Previstos.PREVISTO_ID)));
			it.putExtra("MODO", "duplicar");
			startActivity(it);
			break;
		}
	}

	public void openQuery() {
		SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
		qBuilder.setTables(getTabelas());
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Previstos.PREVISTO_ID, Previstos.PREVISTO_ID);
		map.put(Previstos.PREVISTO_DESCRICAO, Previstos.PREVISTO_DESCRICAO + " as " + Previstos.GET_DESCRICAO);
		map.put(Previstos.PREVISTO_DT_VENCIMENTO, Previstos.PREVISTO_DT_VENCIMENTO + " as " + Previstos.GET_DT_VENCIMENTO);
		map.put(Previstos.PREVISTO_VAL_PREVISTO, Previstos.PREVISTO_VAL_PREVISTO + " as " + Previstos.GET_VAL_PREVISTO);
		map.put(Contatos.CONTATO_NOME, Contatos.CONTATO_NOME + " as " + Contatos.GET_NOME);
		map.put(Contatos.CONTATO_ID, Contatos.CONTATO_ID + " as " + Contatos.GET_ID); 
		qBuilder.setProjectionMap(map); 
		cursor = PrevistoDAO.query(qBuilder, getProjection(), montarWhere(), null, null, null, getOrderBy());
		
		iniciarAdaptador(cursor);
	}
	
	public String montarWhere() {
		String whereString = "";
		
		if (extras.containsKey(Previstos.TIPO_MOVIMENTO)) {
			whereString = Previstos.PREVISTO_TIPO_MOVIMENTO + " = " + "'" + extras.getString(Previstos.TIPO_MOVIMENTO) + "'";
		} 
		 
		if (dtVencIni != null && dtVencFin != null) {
			if (whereString.compareTo("") != 0) {
				whereString += " AND ";
				if (CustomDateUtils.compararDataDMY(dtVencIni, dtVencFin) == 0) {
					whereString += Previstos.PREVISTO_DT_VENCIMENTO + " = '" + CustomDateUtils.toSQLDate(dtVencIni) + "'";
				} else {
					whereString += Previstos.PREVISTO_DT_VENCIMENTO + " >= '" + CustomDateUtils.toSQLDate(dtVencIni) + "' AND " +
								   Previstos.PREVISTO_DT_VENCIMENTO + " <= '" + CustomDateUtils.toSQLDate(dtVencFin) + "'";
				}
			}	 
		} 
		if (dtEmisIni != null && dtEmisFin != null) {
			if (whereString.compareTo("") != 0) {
				whereString += " AND ";
				if (CustomDateUtils.compararDataDMY(dtEmisIni, dtEmisFin) == 0) {
					whereString += Previstos.PREVISTO_DT_EMISSAO + " = '" + CustomDateUtils.toSQLDate(dtEmisIni) + "'";
				} else {
					whereString += Previstos.PREVISTO_DT_EMISSAO + " >= '" + CustomDateUtils.toSQLDate(dtEmisIni) + "' AND " +
								   Previstos.PREVISTO_DT_EMISSAO + " <= '" + CustomDateUtils.toSQLDate(dtEmisFin) + "'";
				}
			}
		}
		if (categoria_id != 0) {
			if (whereString.compareTo("") != 0) {
				whereString += " AND ";
			}
			whereString += Previstos.PREVISTO_CATEGORIA_ID + " = " + categoria_id;
		}
		if (conta_id != 0) {
			if (whereString.compareTo("") != 0) {
				whereString += " AND ";
			}
			whereString += Previstos.PREVISTO_CONTA_ID + " = " + conta_id;
		}
		if (contato_id != 0) {
			if (whereString.compareTo("") != 0) {
				whereString += " AND ";
			}
			whereString += Previstos.PREVISTO_CONTATO_ID + " = " + contato_id;
		}
		if (baixados ^ pendentes) {
			if (whereString.compareTo("") != 0) {
				whereString += " AND ";
			}
			if (pendentes) {
				whereString += " not ";
			}
			
			whereString += " exists ( select 1 from " + RealizadoDAO.getNomeTabela() + " where " +
				Realizados.REALIZADO_PREVISTO_ID + " = " + Previstos.PREVISTO_ID + " ) "; 	
		}
		
		if (extras != null &&
			extras.containsKey("LISTA_PREVISTOS")) {
			whereString = "";
			
			ArrayList<String> listaPrevistos = new ArrayList<String>();
			listaPrevistos = extras.getStringArrayList("LISTA_PREVISTOS");
			
			for (int i = 0;i<listaPrevistos.size();i++) {
				if (whereString.compareTo("") != 0) {
					whereString += " OR ";
				}
				whereString += Previstos.PREVISTO_ID + " = " + listaPrevistos.get(i);
			}
			
			if (whereString.compareTo("") != 0) {
				whereString = " ( " + whereString + " ) ";
			}
			
		}
		
		if (baixados ^ pendentes) {
			if (whereString.compareTo("") != 0) {
				whereString += " AND ";
			}
			if (pendentes) {
				whereString += " not ";
			}
			
			whereString += " exists ( select 1 from " + RealizadoDAO.getNomeTabela() + " where " +
				Realizados.REALIZADO_PREVISTO_ID + " = " + Previstos.PREVISTO_ID + " ) "; 	
		}
		
		return whereString;
	}
	
	public String getOrderBy() {
		if (ordemSelecionada == 0) {
			return Previstos.PREVISTO_DT_VENCIMENTO;
		} else if (ordemSelecionada == 1) {
			return Contatos.CONTATO_NOME;
		} else if (ordemSelecionada == 2) {
			return Previstos.PREVISTO_DESCRICAO;
		} else if (ordemSelecionada == 3) {
			return Previstos.PREVISTO_DT_EMISSAO;
		} else if (ordemSelecionada == 4) {
			return Contas.CONTA_DESCRICAO;
		}
		
		return "";
	} 
	
	public String[] getProjection() {
		ArrayList<String> projection = new ArrayList<String>();
		projection.add(Previstos.PREVISTO_DESCRICAO);
		projection.add(Previstos.PREVISTO_DT_VENCIMENTO);
		projection.add(Previstos.PREVISTO_VAL_PREVISTO);
		projection.add(Contatos.CONTATO_NOME);
		projection.add(Previstos.PREVISTO_ID);
		projection.add(Contatos.CONTATO_ID);
		
		String[] retorno = (String[]) projection.toArray(new String[projection.size()]);
		
		return retorno;
	}
	
	public String getTabelas() {
		String tabelas;
		tabelas = PrevistoDAO.getNomeTabela() + PrevistoDAO.getRelacao(ContatoDAO.getNomeTabela()) +
				PrevistoDAO.getRelacao(ContaDAO.getNomeTabela());;
	    return tabelas;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case INTENT_FILTROS:
				Bundle extras = data.getExtras();
				if (extras != null) {
					if (extras.containsKey("EMIS_INI") && extras.containsKey("EMIS_FIN") &&
						extras.getLong("EMIS_INI") != 0 && extras.getLong("EMIS_FIN") != 0) {
						dtEmisIni.setTimeInMillis(extras.getLong("EMIS_INI"));
						dtEmisFin.setTimeInMillis(extras.getLong("EMIS_FIN"));
					} else {
						dtEmisIni = null;
						dtEmisFin = null;
					}
					if (extras.containsKey("VENC_INI") && extras.containsKey("VENC_FIN") &&
						extras.getLong("VENC_INI") != 0 && extras.getLong("VENC_FIN") != 0) {
						dtVencIni.setTimeInMillis(extras.getLong("VENC_INI"));
						dtVencFin.setTimeInMillis(extras.getLong("VENC_FIN"));
					} else {
						dtVencIni = null;
						dtVencFin = null;
					}
					if (extras.containsKey("CATEGORIA_ID")) {
						categoria_id = extras.getLong("CATEGORIA_ID");
					}
					if (extras.containsKey("CONTA_ID")) {
						conta_id = extras.getLong("CONTA_ID");
					}
					if (extras.containsKey("CONTATO_ID")) {
						contato_id = extras.getLong("CONTATO_ID");
					}
					if (extras.containsKey("BAIXADOS")) {
						baixados = extras.getBoolean("BAIXADOS");
					}
					if (extras.containsKey("PENDENTES")) {
						pendentes = extras.getBoolean("PENDENTES");
					}
					openQuery(); 
		    }
			default:
				break;
			}
		}
	}
	
	public void deletar() {
		int count;
		
		if (cursor != null) {
			try {
			count = PrevistoDAO.deletar(cursor.getLong(cursor.getColumnIndexOrThrow(Previstos.PREVISTO_ID)));
			} catch (SQLiteConstraintException e) {
				Toast.makeText(this, getResources().getString(Integer.parseInt(e.getMessage())), Toast.LENGTH_SHORT).show();
				return;
			}
			if ( count == 1 ) {
				openQuery();
			}
		}
	}


}
