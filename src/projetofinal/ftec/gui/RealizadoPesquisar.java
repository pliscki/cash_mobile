package projetofinal.ftec.gui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Conta.Contas;
import projetofinal.ftec.modelo.Contato.Contatos;
import projetofinal.ftec.modelo.Realizado.Realizados;
import projetofinal.ftec.persistencia.ContaDAO;
import projetofinal.ftec.persistencia.ContatoDAO;
import projetofinal.ftec.persistencia.RealizadoDAO;
import projetofinal.ftec.utils.CustomDateUtils;
import projetofinal.ftec.utils.CustomDecimalUtils;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
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

public class RealizadoPesquisar extends ListActivity {
	
	private Cursor cursor;
	private RealizadoAdapter adaptador;
	private Bundle extras;
	private int ordemSelecionada;
	protected static final int CRIAR = 1;
	protected static final int FILTRAR = 2;
	protected static final int ORDENAR = 3;
	protected static final int DIALOG_ORDENAR = 4;
	protected static final int INTENT_FILTROS = 5;
	protected static final int DIALOG_ACOES = 6;
	protected static final int DLG_DELETAR = 7;
	
	private Calendar dtMovIni;
	private Calendar dtMovFin;
	private long categoria_id;
	private long conta_id;
	private long contato_id;
	private String tituloDialogAcao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		extras = getIntent().getExtras();
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.list_main_simples);
		
		dtMovIni = Calendar.getInstance();
		dtMovFin = Calendar.getInstance();
		dtMovIni.add(Calendar.MONTH, -1);
		openQuery();
	}
	
	public void iniciarAdaptador(Cursor c) {
		if (cursor != null) {
			stopManagingCursor(cursor);
		}
		if (c != null) {
			startManagingCursor(c);
			adaptador = new RealizadoAdapter(this, c);
			setListAdapter(adaptador);
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		cursor = (Cursor) adaptador.getItem(position);
		
		tituloDialogAcao = getResources().getString(R.string.opcoes);
		if (cursor != null) { 
			tituloDialogAcao = cursor.getString(cursor.getColumnIndexOrThrow(Realizados.GET_DESCRICAO)) + " - " +
					getResources().getString(R.string.sigla_moeda) + " " + CustomDecimalUtils.format(cursor.getDouble(cursor.getColumnIndexOrThrow(Realizados.GET_VAL_MOVIMENTO)));
			
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
				Intent it = new Intent(RealizadoPesquisar.this, RealizadoFiltrar.class);
				if (dtMovIni != null && dtMovFin != null) {
					it.putExtra("MOV_INI", dtMovIni.getTimeInMillis());
					it.putExtra("MOV_FIN", dtMovFin.getTimeInMillis());
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
				startActivityForResult(it, INTENT_FILTROS);
				break;
		}
		return true;
	}
	
	public void novo() {
		Intent it = new Intent(this, RealizadoEditar.class);
		startActivity(it);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
	        case DIALOG_ORDENAR:
	        	return new AlertDialog.Builder(RealizadoPesquisar.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.ordenar_por)
                .setSingleChoiceItems(R.array.ordem_realizado, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	ordemSelecionada = whichButton;
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	openQuery();
                    }
                })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
               .create();
	        case DIALOG_ACOES:
	        	return new AlertDialog.Builder(RealizadoPesquisar.this)
	        	.setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(tituloDialogAcao)
                .setItems(R.array.acoes_realizado, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						aplicarAcao(which);
					}
				})
                .setNegativeButton(R.string.cancelar, null)
               .create();
	        case DLG_DELETAR:
	        	return new AlertDialog.Builder(RealizadoPesquisar.this)
	        	.setIcon(android.R.drawable.ic_dialog_alert)
	        	.setTitle(R.string.excluir)
	        	.setMessage(R.string.msg_excluir_realizado)
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
	
	
	public void openQuery() {
		SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
		qBuilder.setTables(getTabelas());
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Realizados.REALIZADO_DESCRICAO, Realizados.REALIZADO_DESCRICAO + " as " + Realizados.GET_DESCRICAO);
		map.put(Realizados.REALIZADO_DT_MOVIMENTO, Realizados.REALIZADO_DT_MOVIMENTO + " as " + Realizados.GET_DT_MOVIMENTO);
		map.put(Realizados.REALIZADO_VAL_MOVIMENTO, Realizados.REALIZADO_VAL_MOVIMENTO + " as " + Realizados.GET_VAL_MOVIMENTO);
		map.put(Realizados.REALIZADO_ID, Realizados.REALIZADO_ID);
		map.put(Contatos.CONTATO_NOME, Contatos.CONTATO_NOME + " as " + Contatos.GET_NOME);
		map.put(Contatos.CONTATO_ID, Contatos.CONTATO_ID + " as " + Contatos.GET_ID);
		qBuilder.setProjectionMap(map);
		cursor = RealizadoDAO.query(qBuilder, getProjection(), montarWhere(), null, null, null, getOrderBy());
		iniciarAdaptador(cursor);
	}
	
	public String montarWhere() {
		String whereString = "";
		
		if (extras.containsKey(Realizados.TIPO_MOVIMENTO)) {
			whereString = Realizados.REALIZADO_TIPO_MOVIMENTO + " = " + "'" + extras.getString((Realizados.TIPO_MOVIMENTO)) + "'";
		}
		
		if (dtMovIni != null && dtMovFin != null) {
			if (whereString.compareTo("") != 0) {
				whereString += " AND ";
				if (CustomDateUtils.compararDataDMY(dtMovIni, dtMovFin) == 0) {
					whereString += Realizados.REALIZADO_DT_MOVIMENTO + " = '" + CustomDateUtils.toSQLDate(dtMovIni) + "'";
				} else {
					whereString += Realizados.REALIZADO_DT_MOVIMENTO + " >= '" + CustomDateUtils.toSQLDate(dtMovIni) + "' AND " +
							Realizados.REALIZADO_DT_MOVIMENTO + " <= '" + CustomDateUtils.toSQLDate(dtMovFin) + "'";
				}
			}	 
		} 
		if (categoria_id != 0) {
			if (whereString.compareTo("") != 0) {
				whereString += " AND ";
			}
			whereString += Realizados.REALIZADO_CATEGORIA_ID + " = " + categoria_id;
		}
		if (conta_id != 0) {
			if (whereString.compareTo("") != 0) {
				whereString += " AND ";
			}
			whereString += Realizados.REALIZADO_CONTA_ID + " = " + conta_id;
		}
		if (contato_id != 0) {
			if (whereString.compareTo("") != 0) {
				whereString += " AND ";
			}
			whereString += Realizados.REALIZADO_CONTATO_ID + " = " + contato_id;
		}
		return whereString;
	}
	
	public String getOrderBy() {
		if (ordemSelecionada == 0) {
			return Realizados.REALIZADO_DT_MOVIMENTO + " desc";
		} else if (ordemSelecionada == 1) {
			return Contatos.CONTATO_NOME;
		} else if (ordemSelecionada == 2) {
			return Realizados.REALIZADO_DESCRICAO;
		} else if (ordemSelecionada == 3) {
			return Contas.CONTA_DESCRICAO;
		}
		
		return "";
	} 
	
	public String[] getProjection() {
		ArrayList<String> projection = new ArrayList<String>();
		projection.add(Realizados.REALIZADO_DESCRICAO);
		projection.add(Realizados.REALIZADO_DT_MOVIMENTO);
		projection.add(Realizados.REALIZADO_VAL_MOVIMENTO);
		projection.add(Contatos.CONTATO_NOME);
		projection.add(Realizados.REALIZADO_ID);
		projection.add(Contatos.CONTATO_ID);
		
		String[] retorno = (String[]) projection.toArray(new String[projection.size()]);
		
		return retorno;
	}
	
	public String getTabelas() {
		String tabelas;
		tabelas = RealizadoDAO.getNomeTabela() + RealizadoDAO.getRelacao(ContatoDAO.getNomeTabela()) +
				RealizadoDAO.getRelacao(ContaDAO.getNomeTabela());;
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
					if (extras.containsKey("MOV_INI") && extras.containsKey("MOV_FIN") &&
						extras.getLong("MOV_INI") != 0 && extras.getLong("MOV_FIN") != 0) {
						dtMovIni.setTimeInMillis(extras.getLong("MOV_INI"));
						dtMovFin.setTimeInMillis(extras.getLong("MOV_FIN"));
					} else {
						dtMovIni = null;
						dtMovIni = null;
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
					openQuery();
				}
				break;

			default:
				break;
			}
		}
	}	
	
	protected void aplicarAcao(int acao) {
		Intent it;
		switch (acao) {
		case 0:
			it = new Intent(RealizadoPesquisar.this, RealizadoView.class);
			it.putExtra(Realizados._ID, cursor.getLong(cursor.getColumnIndexOrThrow(Realizados.REALIZADO_ID)));
			startActivity(it);
			break;
		case 1:
			it = new Intent(RealizadoPesquisar.this, RealizadoEditar.class);
			it.putExtra(Realizados._ID, cursor.getLong(cursor.getColumnIndexOrThrow(Realizados.REALIZADO_ID)));
			startActivity(it);
			break;
		case 2:
			showDialog(DLG_DELETAR);
			break;	
		case 3:
			it = new Intent(RealizadoPesquisar.this, RealizadoEditar.class);
			it.putExtra(Realizados._ID, cursor.getLong(cursor.getColumnIndexOrThrow(Realizados.REALIZADO_ID)));
			it.putExtra("MODO", "duplicar");
			startActivity(it);
			break;
		}
	}
	
	public void deletar() {
		int count;
		
		if (cursor != null) {
			try {
			count = RealizadoDAO.deletar(cursor.getLong(cursor.getColumnIndexOrThrow(Realizados.REALIZADO_ID)));
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
