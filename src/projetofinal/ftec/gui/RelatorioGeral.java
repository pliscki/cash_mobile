package projetofinal.ftec.gui;

import java.util.Calendar;
import java.util.HashMap;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Categoria.Categorias;
import projetofinal.ftec.modelo.Conta.Contas;
import projetofinal.ftec.modelo.Contato.Contatos;
import projetofinal.ftec.modelo.ContatoTipo.ContatoTipos;
import projetofinal.ftec.modelo.Realizado.Realizados;
import projetofinal.ftec.modelo.Sistema;
import projetofinal.ftec.persistencia.CategoriaDAO;
import projetofinal.ftec.persistencia.ContaDAO;
import projetofinal.ftec.persistencia.ContatoDAO;
import projetofinal.ftec.persistencia.ContatoTipoDAO;
import projetofinal.ftec.persistencia.RealizadoDAO;
import projetofinal.ftec.utils.CustomDateUtils;
import projetofinal.ftec.utils.CustomDecimalUtils;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class RelatorioGeral extends ListActivity {
	private Cursor cursor;
	private Cursor cursorGeral;
	private GeralAdapter adaptador;
	private SQLiteDatabase db;
	private LinearLayout lCabecalho;
	private LinearLayout lRodape;
	private ImageButton btnProximo;
	private ImageButton btnAnterior;
	private Button btnPeriodo;
	private Button btnRodape;
	private Bundle extras;
	
	public static final String DESCRICAO = "descricao";
	public static final String TOTAL_DESCRICAO = "total_descricao";
	public static final String NOME_TABELA = "temp_rlt_geral";
	public static final String TOTAL_GERAL = "total_geral";
	
	private static final int ANO = 0;
	private static final int MES = 1;
	private static final int PERSONALIZADO = 2;
	
	private static final int CONTA = 0;
	private static final int CATEGORIA = 1;
	private static final int CONTATO = 2;
	private static final int CONTATO_TIPO = 3;
	
	private static final int DIALOG_PERIODO = 1;
	private static final int INTENT_FILTROS = 2;
	private static final int DIALOG_AGRUPAR = 3;
	
	private static final int MENU_AGRUPAR = 10;
	
	private Calendar dtMovIni;
	private Calendar dtMovFin;
	private long categoria_id;
	private long conta_id;
	private long contato_id;
	
	private int tipoPeriodo;
	private int tipoAgrupamento;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		extras = getIntent().getExtras();
		
		Sistema sis = Sistema.getSistema();
		db = sis.getConexaoBanco();
		
		setContentView(R.layout.list_main_simples);
		
		setTitle(R.string.relatorio_geral);
		
		initComps();
		
		tipoPeriodo = ANO;
		tipoAgrupamento = CATEGORIA;
		iniciarFiltro();
		
		openQuery();
		updateDisplay();
	}
	
	private void openQuery() {
		setTotalGeral();
		String[] colunas = new String[] {"rowid", DESCRICAO, TOTAL_DESCRICAO};
		criarTempTable();
		SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
		qBuilder.setTables(NOME_TABELA);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("rowid", "rowid as _id");
		map.put(DESCRICAO, DESCRICAO);
		map.put(TOTAL_DESCRICAO, TOTAL_DESCRICAO);
		qBuilder.setProjectionMap(map);
		cursor = RealizadoDAO.query(qBuilder, colunas, null, null, null, null, TOTAL_DESCRICAO + " desc ");
		iniciarAdaptador(cursor);
	}
	
	private void criarTempTable() {
		String campoDescricao = Categorias.CATEGORIA_DESCRICAO;
		String tabela = CategoriaDAO.getNomeTabela();
		String agruparPor  = Categorias.CATEGORIA_ID;
		
		String sqlCreateTemp;
		String sqlTotalGeral;
		
		switch (tipoAgrupamento) {
		case  CONTA:
			campoDescricao = Contas.CONTA_DESCRICAO;
			tabela = ContaDAO.getNomeTabela();
			agruparPor = Contas.CONTA_ID;
			break;
		case CATEGORIA:
			campoDescricao = Categorias.CATEGORIA_DESCRICAO;
			tabela = CategoriaDAO.getNomeTabela();
			agruparPor = Categorias.CATEGORIA_ID;
			break;
		case CONTATO:
			campoDescricao = Contatos.CONTATO_NOME;
			tabela = ContatoDAO.getNomeTabela();
			agruparPor = Contatos.CONTATO_ID;
			break;
		case CONTATO_TIPO:
			campoDescricao = ContatoTipos.CONTATO_TIPO_DESCRICAO;
			tabela = ContatoTipoDAO.getNomeTabela();
			agruparPor = ContatoTipos.CONTATO_TIPO_ID;
			break;
		}
		
		try {
			db.execSQL("drop table if exists "  + NOME_TABELA);
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
		}
		sqlCreateTemp = "create temporary table " + NOME_TABELA + " as ";
		
		
		if (tipoAgrupamento != CONTATO_TIPO) {
			sqlTotalGeral = "select coalesce(" + campoDescricao + ", '" + getResources().getString(R.string.nao_definido) + "') as " + DESCRICAO +
					", sum(" + Realizados.REALIZADO_VAL_MOVIMENTO + ") as " + TOTAL_DESCRICAO +
					" from " + RealizadoDAO.getNomeTabela() + RealizadoDAO.getRelacao(tabela) +  
					" where " + getWhereQuery() +
					" group by " + agruparPor;
		} else {
			sqlTotalGeral = "select coalesce(" + campoDescricao + ", '" + getResources().getString(R.string.nao_definido) + "') as " + DESCRICAO +
					", sum(" + Realizados.REALIZADO_VAL_MOVIMENTO + ") as " + TOTAL_DESCRICAO +
					" from " + RealizadoDAO.getNomeTabela() + RealizadoDAO.getRelacao(ContatoDAO.getNomeTabela()) +
					" left outer join " + tabela +  " on " + Contatos.CONTATO_CONTATO_TIPO + " = " + ContatoTipos.CONTATO_TIPO_ID + 
					" where " + getWhereQuery() +
					" group by " + agruparPor;
		}
		
		
		try {
			db.execSQL(sqlCreateTemp + 
					   sqlTotalGeral + 
					   " order by " + DESCRICAO);
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
		}
	
	}
	
	private void setTotalGeral() {
		String[] colunas = new String[] {" sum(" + Realizados.REALIZADO_VAL_MOVIMENTO + ") as " + TOTAL_GERAL};
		SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
		qBuilder.setTables(RealizadoDAO.getNomeTabela());
		
		cursorGeral = RealizadoDAO.query(qBuilder, colunas, getWhereQuery(), null, null, null, null);
	}
	
	private String getWhereQuery() {
		String where = "";
		
		if (CustomDateUtils.compararDataDMY(dtMovIni, dtMovFin) == 0) {
			where = Realizados.REALIZADO_DT_MOVIMENTO + " = '" + CustomDateUtils.toSQLDate(dtMovIni) + "' ";
		} else {
			where = Realizados.REALIZADO_DT_MOVIMENTO + " >= '" + CustomDateUtils.toSQLDate(dtMovIni) + "' AND " +
								Realizados.REALIZADO_DT_MOVIMENTO + " < '" + CustomDateUtils.toSQLDate(dtMovFin) + "' "; 
		}
		
		if (extras.containsKey(Realizados.TIPO_MOVIMENTO)) {
			if (where.compareTo("") != 0) {
				where += " AND ";
			}
			
			where += Realizados.REALIZADO_TIPO_MOVIMENTO + " = " + "'" + extras.getString((Realizados.TIPO_MOVIMENTO)) + "'";
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
			adaptador = new GeralAdapter(this, c);
			if (cursorGeral != null &&
				cursorGeral.moveToFirst()) {
				adaptador.setTotalGeral(cursorGeral.getDouble(cursorGeral.getColumnIndexOrThrow(TOTAL_GERAL)));
			}
			setListAdapter(adaptador);
		}
	}
	
	private void initComps() {
		lCabecalho = (LinearLayout) findViewById(R.id.list_simples_linear_header);
		lRodape = (LinearLayout) findViewById(R.id.list_simples_linear_footer);
		LayoutInflater factory = LayoutInflater.from(this);
		btnRodape = new Button(this);
		btnRodape.setTextSize(20);
		btnRodape.setGravity(Gravity.RIGHT);
		btnRodape.setEnabled(false);
		View barraNavegacao = factory.inflate(R.layout.barra_navegacao, null);
		lCabecalho.addView(barraNavegacao, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		lRodape.addView(btnRodape, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		
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
		
		if (cursorGeral != null &&
		    cursorGeral.moveToFirst()) {
			btnRodape.setText(getResources().getString(R.string.total_geral) + ": " + 
							  getResources().getString(R.string.sigla_moeda) + " " +
							  CustomDecimalUtils.format(cursorGeral.getDouble(cursorGeral.getColumnIndexOrThrow(TOTAL_GERAL))));
		} else {
			btnRodape.setText(getResources().getString(R.string.total_geral) + ": " + 
					  getResources().getString(R.string.sigla_moeda) + " " +
					  CustomDecimalUtils.format(0.0));
		}
		
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
			Intent it = new Intent(RelatorioGeral.this, RealizadoFiltrar.class);
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
		openQuery();
		updateDisplay();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_PERIODO:
			return new AlertDialog.Builder(RelatorioGeral.this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle(R.string.tipo_intervalo)
            .setItems(R.array.tipos_periodo, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					tipoPeriodo = which;
					iniciarFiltro();
					openQuery();
					updateDisplay();
				}
			})
            .setNegativeButton(R.string.cancelar, null)
           .create();
		case DIALOG_AGRUPAR:
			return new AlertDialog.Builder(RelatorioGeral.this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle(R.string.agrupar_por)
            .setItems(R.array.agrupar_por, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case CONTA:
						tipoAgrupamento = CONTA;
						break;
					case CONTATO:
						tipoAgrupamento = CONTATO;
						break;
					case CATEGORIA:
						tipoAgrupamento = CATEGORIA;
						break;
					case CONTATO_TIPO:
						tipoAgrupamento = CONTATO_TIPO;
						break;
					}
					iniciarFiltro();
					openQuery();
					updateDisplay();
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_AGRUPAR, 0, R.string.agrupar_por).setIcon(android.R.drawable.ic_menu_share);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {		
			case MENU_AGRUPAR:
				showDialog(DIALOG_AGRUPAR);
				break;
		}
		return true;
	}
}
