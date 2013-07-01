package projetofinal.ftec.gui;

import java.util.Calendar;
import java.util.HashMap;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Categoria.Categorias;
import projetofinal.ftec.modelo.Conta.Contas;
import projetofinal.ftec.modelo.Realizado.Realizados;
import projetofinal.ftec.modelo.Sistema;
import projetofinal.ftec.persistencia.CategoriaDAO;
import projetofinal.ftec.persistencia.ContaDAO;
import projetofinal.ftec.persistencia.RealizadoDAO;
import projetofinal.ftec.utils.CustomDateUtils;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class RelatorioBalanco extends ListActivity {
	private Cursor cursor;
	private BalancoAdapter adaptador;
	private SQLiteDatabase db;
	private LinearLayout lCabecalho;
	private ImageButton btnProximo;
	private ImageButton btnAnterior;
	private Button btnPeriodo;
	
	public static final String NIVEL = "nivel";
	public static final String TIPO_MOVIMENTO = "tipo_movimento";
	public static final String CONTA_ID = "conta_id";
	public static final String DESCRICAO = "descricao";
	public static final String TOTAL = "total";
	public static final String NOME_TABELA = "temp_balanco";
	
	private static final int ANO = 0;
	private static final int MES = 1;
	private static final int PERSONALIZADO = 2;
	
	private static final int DIALOG_PERIODO = 1;
	private static final int INTENT_FILTROS = 2;
	
	private Calendar dtMovIni;
	private Calendar dtMovFin;
	private long categoria_id;
	private long conta_id;
	private long contato_id;
	
	private int tipoPeriodo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Sistema sis = Sistema.getSistema();
		db = sis.getConexaoBanco();
		
		setContentView(R.layout.list_main_simples);
		
		setTitle(R.string.relatorio_balanco);
		
		initComps();
		
		tipoPeriodo = ANO;
		iniciarFiltro();
		
		openQuery();
	}
	
	private void openQuery() {
		String[] colunas = new String[] {"rowid", NIVEL, DESCRICAO, TIPO_MOVIMENTO, CONTA_ID, TOTAL};
		criarTempTable();
		SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
		qBuilder.setTables(NOME_TABELA);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("rowid", "rowid as _id");
		map.put(NIVEL, NIVEL);
		map.put(DESCRICAO, DESCRICAO);
		map.put(TIPO_MOVIMENTO, TIPO_MOVIMENTO);
		map.put(CONTA_ID, CONTA_ID);
		map.put(TOTAL, TOTAL);
		qBuilder.setProjectionMap(map);
		cursor = RealizadoDAO.query(qBuilder, colunas, null, null, null, null, null);
		iniciarAdaptador(cursor);
	}
	
	private void criarTempTable() {
		String sqlCreateTemp;
		String sqlTotalGeral;
		String sqlTotalMovimento;
		String sqlTotalConta;
		String sqlTotalCategoria;
		
		try {
			db.execSQL("drop table if exists "  + NOME_TABELA);
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
		}
		sqlCreateTemp = "create temporary table " + NOME_TABELA + " as ";
		
		sqlTotalGeral = " select 1 as " + NIVEL + ", '' as " + TIPO_MOVIMENTO +" , " +
				"0 as " + CONTA_ID + " , '' as " + DESCRICAO +", " + 
				" sum(" + Realizados.REALIZADO_VAL_MOVIMENTO + " * (case " + TIPO_MOVIMENTO + " when 'P' then -1 else 1 end)" + 
				") as " + TOTAL +
				" from " + RealizadoDAO.getNomeTabela()  + getWhereQuery();
		
		sqlTotalMovimento = " select 2 as " + NIVEL + ", " + Realizados.REALIZADO_TIPO_MOVIMENTO + " as " + TIPO_MOVIMENTO +" , " +
				"0 as " + CONTA_ID + " , " + Realizados.REALIZADO_TIPO_MOVIMENTO + " as " + DESCRICAO +", " + 
				" sum(" + Realizados.REALIZADO_VAL_MOVIMENTO + ") as " + TOTAL +
				" from " + RealizadoDAO.getNomeTabela() + getWhereQuery() +
				" group by " + NIVEL + ", " + DESCRICAO + ", " + TIPO_MOVIMENTO + " ";
		
		sqlTotalConta = " select 3 as " + NIVEL + ", " + Realizados.REALIZADO_TIPO_MOVIMENTO + " as " + TIPO_MOVIMENTO +" , " +
				Contas.CONTA_ID + " as " + CONTA_ID + ", " + Contas.CONTA_DESCRICAO + " as " + DESCRICAO + ", " +
				" sum(" + Realizados.REALIZADO_VAL_MOVIMENTO + ") as " + TOTAL +
				" from " + RealizadoDAO.getNomeTabela() + " " + RealizadoDAO.getRelacao(ContaDAO.getNomeTabela()) +  getWhereQuery() + 
				" group by " + NIVEL + ", " + CONTA_ID + ", " + TIPO_MOVIMENTO + " ";
		
		sqlTotalCategoria = " select 4 as " + NIVEL + ", " + Realizados.REALIZADO_TIPO_MOVIMENTO + " as " + TIPO_MOVIMENTO +" , " +
				Contas.CONTA_ID + " as " + CONTA_ID + ", coalesce(" + Categorias.CATEGORIA_DESCRICAO + ",'') as " + DESCRICAO + ", " +
				" sum(" + Realizados.REALIZADO_VAL_MOVIMENTO + ") as " + TOTAL +
				" from " + RealizadoDAO.getNomeTabela() + " " + RealizadoDAO.getRelacao(ContaDAO.getNomeTabela()) + " " +
				RealizadoDAO.getRelacao(CategoriaDAO.getNomeTabela()) +  getWhereQuery() +				
				" group by " + NIVEL + ", " + Categorias.CATEGORIA_ID + ", " + TIPO_MOVIMENTO + ", " + CONTA_ID + " ";
		
		try {
			db.execSQL(sqlCreateTemp + 
					   sqlTotalGeral     + " union " +	 
					   sqlTotalMovimento + " union " +
					   sqlTotalConta 	 + " union " + 
					   sqlTotalCategoria + 
					   " order by " + TIPO_MOVIMENTO + ", " + CONTA_ID + ", " + NIVEL + ", " + DESCRICAO);
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
		}
	
	}
	
	private String getWhereQuery() {
		String where = "";
		
		if (CustomDateUtils.compararDataDMY(dtMovIni, dtMovFin) == 0) {
			where = " where " + Realizados.REALIZADO_DT_MOVIMENTO + " = '" + CustomDateUtils.toSQLDate(dtMovIni) + "' ";
		} else {
			where = " where " + Realizados.REALIZADO_DT_MOVIMENTO + " >= '" + CustomDateUtils.toSQLDate(dtMovIni) + "' AND " +
								Realizados.REALIZADO_DT_MOVIMENTO + " < '" + CustomDateUtils.toSQLDate(dtMovFin) + "' "; 
		}
		if (categoria_id != 0) {
			if (where.compareTo("") != 0) {
				where += " AND ";
			}
			where += Realizados.REALIZADO_CATEGORIA_ID + " = " + categoria_id;
		}
		if (conta_id != 0) {
			if (where.compareTo("") != 0) {
				where += " AND ";
			}
			where += Realizados.REALIZADO_CONTA_ID + " = " + conta_id;
		}
		if (contato_id != 0) {
			if (where.compareTo("") != 0) {
				where += " AND ";
			}
			where += Realizados.REALIZADO_CONTATO_ID + " = " + contato_id;
		}
		
		return where;
	}
	
	private void iniciarAdaptador(Cursor c) {
		if (cursor != null) {
			stopManagingCursor(cursor);
		}
		if (c != null) {
			startManagingCursor(c);
			adaptador = new BalancoAdapter(this, c);
			setListAdapter(adaptador);
		}
	}
	
	private void initComps() {
		lCabecalho = (LinearLayout) findViewById(R.id.list_simples_linear_header);
		LayoutInflater factory = LayoutInflater.from(this);
		View barraNavegacao = factory.inflate(R.layout.barra_navegacao, null);
		lCabecalho.addView(barraNavegacao, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		
		btnAnterior = (ImageButton) findViewById(R.id.bar_btn_anterior);
		btnProximo = (ImageButton) findViewById(R.id.bar_btn_proximo);
		btnPeriodo = (Button) findViewById(R.id.bar_btn_periodo);
		
		btnAnterior.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alteraPeriodo(-1);
			}
		});
		btnProximo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alteraPeriodo(1);
			}
		});
		btnPeriodo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_PERIODO);
			}
		});
		
	}
	
	private void updateDisplay() {
		btnAnterior.setEnabled(true);
		btnProximo.setEnabled(true);
		
		switch (tipoPeriodo) {
		case MES:
			btnPeriodo.setText(DateFormat.format("MMMM - yyyy", dtMovIni));
			break;
		case ANO:
			btnPeriodo.setText(DateFormat.format("yyyy", dtMovIni));
			break;
		case PERSONALIZADO:
			btnAnterior.setEnabled(false);
			btnProximo.setEnabled(false);
			btnPeriodo.setText(R.string.personalizado);
			Intent it = new Intent(RelatorioBalanco.this, RealizadoFiltrar.class);
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
	}
	
	private void iniciarFiltro() {
		
		if (tipoPeriodo != PERSONALIZADO) {
			
			dtMovFin = Calendar.getInstance();
			dtMovIni = Calendar.getInstance();
			
			dtMovIni.set(Calendar.DATE, 1);
			dtMovFin.set(Calendar.DATE, 1);
			
			switch (tipoPeriodo) {
			case MES:
				dtMovFin.add(Calendar.MONTH, 1);
				break;
			case ANO:
				dtMovIni.set(Calendar.MONTH, 0);
				dtMovFin.set(Calendar.MONTH, 0);
				dtMovFin.add(Calendar.YEAR, 1);
				break;
			}
		}		
		updateDisplay();
		
	}
	
	private void alteraPeriodo(int valor) {
		switch (tipoPeriodo) {
		case MES:
			dtMovIni.add(Calendar.MONTH, valor);
			dtMovFin.add(Calendar.MONTH, valor);
			break;
		case ANO:
			dtMovIni.add(Calendar.YEAR, valor);
			dtMovFin.add(Calendar.YEAR, valor);
			break;
		}
		updateDisplay();
		openQuery();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_PERIODO:
			return new AlertDialog.Builder(RelatorioBalanco.this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle(R.string.tipo_intervalo)
            .setItems(R.array.tipos_periodo, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					tipoPeriodo = which;
					iniciarFiltro();
					openQuery();
				}
			})
            .setNegativeButton(R.string.cancelar, null)
           .create();
		}
		return null;
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
						// Incrementar em +1 o dia pois o SQL é montado com a comparacao "<" e nao "<="
						dtMovFin.add(Calendar.DATE, 1);
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
}
