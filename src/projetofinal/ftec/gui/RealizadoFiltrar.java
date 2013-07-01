package projetofinal.ftec.gui;

import java.util.Calendar;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Categoria;
import projetofinal.ftec.modelo.Categoria.Categorias;
import projetofinal.ftec.modelo.Conta;
import projetofinal.ftec.modelo.Conta.Contas;
import projetofinal.ftec.modelo.Contato;
import projetofinal.ftec.modelo.Contato.Contatos;
import projetofinal.ftec.persistencia.CategoriaDAO;
import projetofinal.ftec.persistencia.ContaDAO;
import projetofinal.ftec.persistencia.ContatoDAO;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class RealizadoFiltrar extends Activity implements OnClickListener {
	private Contato contato;
	private Categoria categoria;
	private Conta conta;
	private Bundle extras;
	private Button btnMovIni;
	private Button btnMovFin;
	private Button btnOk;
	private Button btnCancelar;
	TextView tvConta;
	TextView tvCategoria;
	TextView tvContato;
	private ImageButton imgBtnContato;
	private ImageButton imgBtnConta;
	private ImageButton imgBtnCategoria;
	LinearLayout lConta;
	LinearLayout lCategoria;
	LinearLayout lContato;
	
	private static final int PESQUISA_CONTA = 1;
	private static final int PESQUISA_CONTATO = 2;
	private static final int PESQUISA_CATEGORIA = 3;
	private static final int DIALOG_MOV_INI = 4;
	private static final int DIALOG_MOV_FIN = 5;
	private static final int BTN_OK = 101;
	private static final int BTN_CANCELAR = 102;
	private static final int BTN_MOV_INI = 103;
	private static final int BTN_MOV_FIN = 104;
	private static final int LINEAR_CONTATO = 107;
	private static final int LINEAR_CATEGORIA = 108;
	private static final int LINEAR_CONTA = 109;
	private static final int LIMPAR_FILTROS = 110;
	private static final int BTN_DESELECT_CONTA = 111;
	private static final int BTN_DESELECT_CATEGORIA = 112;
	private static final int BTN_DESELECT_CONTATO = 113;
	
	private Calendar dtMovIni;
	private Calendar dtMovFin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_container);
		setTitle(R.string.adicionar_filtros);
		extras = getIntent().getExtras();
		
		initComps();
		setValores();
	}
	
	private void initComps() {
		ScrollView scrollView = (ScrollView) findViewById(R.id.edit_scroll);
		LayoutInflater factory = LayoutInflater.from(this);
        View viewRealizado = factory.inflate(R.layout.realizado_filtros, null);
        scrollView.addView(viewRealizado);
        btnMovIni = (Button) findViewById(R.id.realizado_filtro_btnMovInicial);
        btnMovFin = (Button) findViewById(R.id.realizado_filtro_btnMovFinal);
        btnOk = (Button) findViewById(R.id.btnSalvar);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        tvCategoria = (TextView) findViewById(R.id.consulta_tvCategoria);
        tvConta = (TextView) findViewById(R.id.consulta_tvConta);
        tvContato = (TextView) findViewById(R.id.consulta_tvContato);
        lCategoria = (LinearLayout) findViewById(R.id.consulta_linear_categoria);
        lConta = (LinearLayout) findViewById(R.id.consulta_linear_conta);
        lContato = (LinearLayout) findViewById(R.id.consulta_linear_contato);
        imgBtnCategoria = (ImageButton) findViewById(R.id.consulta_btn_categoria_deselec);
		imgBtnConta = (ImageButton) findViewById(R.id.consulta_btn_conta_deselec);
		imgBtnContato = (ImageButton) findViewById(R.id.consulta_btn_contato_deselec);
        
        btnOk.setId(BTN_OK);
        btnCancelar.setId(BTN_CANCELAR);
        btnMovIni.setId(BTN_MOV_INI);
        btnMovFin.setId(BTN_MOV_FIN);
        lCategoria.setId(LINEAR_CATEGORIA);
        lConta.setId(LINEAR_CONTA);
        lContato.setId(LINEAR_CONTATO);
        imgBtnCategoria.setId(BTN_DESELECT_CATEGORIA);
        imgBtnConta.setId(BTN_DESELECT_CONTA);
        imgBtnContato.setId(BTN_DESELECT_CONTATO);
        
        btnOk.setText(R.string.ok);
        btnCancelar.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        btnMovIni.setOnClickListener(this);
        btnMovFin.setOnClickListener(this);
        lCategoria.setOnClickListener(this);
        lConta.setOnClickListener(this);
        lContato.setOnClickListener(this);
        imgBtnCategoria.setOnClickListener(this);
        imgBtnConta.setOnClickListener(this);
        imgBtnContato.setOnClickListener(this);
		
	}
	
	private void setValores() {
		dtMovIni = Calendar.getInstance();
        dtMovFin = Calendar.getInstance();
        
        dtMovIni.add(Calendar.MONTH, -1);        
		
        if (extras != null) {
			if (extras.containsKey("MOV_INI")) {
				dtMovIni.setTimeInMillis(extras.getLong("MOV_INI"));
			}
			if (extras.containsKey("MOV_FIN")) {
				dtMovFin.setTimeInMillis(extras.getLong("MOV_FIN"));
			}
			if (extras.containsKey("CONTA_ID")) {
				conta = ContaDAO.buscarConta(extras.getLong("CONTA_ID"));
				if (conta != null) {
					tvConta.setText(conta.getDescricao());
					imgBtnConta.setVisibility(View.GONE);
				}
			}
			if (extras.containsKey("CONTATO_ID")) {
				contato = ContatoDAO.buscarContato(extras.getLong("CONTATO_ID"));
				if (contato != null) {
					tvContato.setText(contato.getNome());
					imgBtnContato.setVisibility(View.GONE);
				}
			}
			if (extras.containsKey("CATEGORIA_ID")) {
				categoria = CategoriaDAO.buscarCategoria(extras.getLong("CATEGORIA_ID"));
				if (categoria != null) {
					tvCategoria.setText(categoria.getDescricao());
					imgBtnCategoria.setVisibility(View.GONE);
				}
			}
        }
		btnMovIni.setText(DateFormat.format("dd/MM/yyyy", dtMovIni.getTime()));
		btnMovFin.setText(DateFormat.format("dd/MM/yyyy", dtMovFin.getTime()));
	}
	
	@Override
	public void onClick(View v) {
		Intent it = null;
		Log.w("DEBUG", Integer.toString(v.getId()));
		switch (v.getId()) {
			case BTN_CANCELAR:
				setResult(RESULT_CANCELED);
				finish();
				break;
			case BTN_OK:
				if (validarFiltros()) {
					retornarFiltro();
				}
				break;
			case BTN_MOV_INI:
				showDialog(DIALOG_MOV_INI);
				break;
			case BTN_MOV_FIN:
				showDialog(DIALOG_MOV_FIN);
				break;
			case LINEAR_CATEGORIA:
				it = new Intent(RealizadoFiltrar.this, CategoriaPesquisar.class);
				it.putExtra("MODO", "consulta");
				startActivityForResult(it, PESQUISA_CATEGORIA);
				break;
			case LINEAR_CONTA:
				it = new Intent(RealizadoFiltrar.this, ContaPesquisar.class);
				it.putExtra("MODO", "consulta");
				startActivityForResult(it, PESQUISA_CONTA);
				break;
			case LINEAR_CONTATO:
				it = new Intent(RealizadoFiltrar.this, ContatoPesquisar.class);
				it.putExtra("MODO", "consulta");
				startActivityForResult(it, PESQUISA_CONTATO);
				break;
			case BTN_DESELECT_CATEGORIA:
				categoria = null;
				tvCategoria.setText("");
				imgBtnCategoria.setVisibility(View.GONE);
				break;
			case BTN_DESELECT_CONTA:
				conta = null;
				tvConta.setText("");
				imgBtnConta.setVisibility(View.GONE);
				break;
			case BTN_DESELECT_CONTATO:
				contato = null;
				tvContato.setText("");
				imgBtnContato.setVisibility(View.GONE);
				break;				
		}
	}
	
	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
		   switch(requestCode) { 
		      case (PESQUISA_CONTATO) : {
		    	  if (data.getLongExtra(Contatos._ID, 0) != 0) {
		    		  contato = ContatoDAO.buscarContato(data.getLongExtra(Contatos._ID, 0));
		    		  if (contato != null) {
		    			  tvContato.setText(contato.getNome());
		    			  imgBtnContato.setVisibility(View.VISIBLE);
		    		  }
		    	  }
		    	  break;
		      }
		      case (PESQUISA_CONTA) : {
		    	  if (data.getLongExtra(Contas._ID, 0) != 0) {
		    		  conta = ContaDAO.buscarConta(data.getLongExtra(Contas._ID, 0));
		    		  if (conta != null) {
		    			  tvConta.setText(conta.getDescricao());
		    			  imgBtnConta.setVisibility(View.VISIBLE);
		    		  }
		    	  }
		    	  break;
		      }
		      case (PESQUISA_CATEGORIA) : {
		    	  if (data.getLongExtra(Categorias._ID, 0) != 0) {
		    		  categoria = CategoriaDAO.buscarCategoria(data.getLongExtra(Categorias._ID, 0));
		    		  if (categoria != null) {
		    			  tvCategoria.setText(categoria.getDescricao());
		    			  imgBtnCategoria.setVisibility(View.VISIBLE);
		    		  }
		    	  }
		    	  break;
		      }
		   }
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
	        case DIALOG_MOV_INI:
	        	return new DatePickerDialog(this,
                        mDateSetListenerMovIni,
                        dtMovIni.get(Calendar.YEAR), dtMovIni.get(Calendar.MONTH), dtMovIni.get(Calendar.DATE));
	        case DIALOG_MOV_FIN:
	            return new DatePickerDialog(this,
	                        mDateSetListenerMovFin,
	                        dtMovFin.get(Calendar.YEAR), dtMovFin.get(Calendar.MONTH), dtMovFin.get(Calendar.DATE));
	    }
	    return null;
	}
	
	protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DIALOG_MOV_INI:
            	((DatePickerDialog) dialog).updateDate(dtMovIni.get(Calendar.YEAR), dtMovIni.get(Calendar.MONTH), dtMovIni.get(Calendar.DATE));
                break;
            case DIALOG_MOV_FIN:
                ((DatePickerDialog) dialog).updateDate(dtMovFin.get(Calendar.YEAR), dtMovFin.get(Calendar.MONTH), dtMovFin.get(Calendar.DATE));
                break;
        }
    }
	
	private DatePickerDialog.OnDateSetListener mDateSetListenerMovIni =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                        int dayOfMonth) {
                	dtMovIni.set(year, monthOfYear, dayOfMonth);
                    updateDisplay(DIALOG_MOV_INI);
                }
            };
	private DatePickerDialog.OnDateSetListener mDateSetListenerMovFin =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                        int dayOfMonth) {
                	dtMovFin.set(year, monthOfYear, dayOfMonth);
                    updateDisplay(DIALOG_MOV_FIN);
                }
            };
            
    private void updateDisplay(int id) {
    	switch (id) {
		case DIALOG_MOV_INI:
			btnMovIni.setText(DateFormat.format("dd/MM/yyyy", dtMovIni.getTime()));
			break;
		case DIALOG_MOV_FIN:
			btnMovFin.setText(DateFormat.format("dd/MM/yyyy", dtMovFin.getTime()));
			break;
		default:
			btnMovIni.setText(DateFormat.format("dd/MM/yyyy", dtMovIni.getTime()));
			btnMovFin.setText(DateFormat.format("dd/MM/yyyy", dtMovFin.getTime()));
			break;
		}
    }
    
    private boolean validarFiltros() {
    	if (dtMovIni == null ^ dtMovFin == null) {
    		Toast.makeText(this, getResources().getString(R.string.filtro_movimento_invalido) , Toast.LENGTH_SHORT).show();
    		return false;
    	} else if (dtMovIni != null && dtMovFin != null){
    		if (dtMovIni.compareTo(dtMovFin) > 0) {
    			Toast.makeText(this,getResources().getString(R.string.mov_inicial_menor_final) , Toast.LENGTH_SHORT).show();
    			return false;
    		}
    	}
    	return true;
    }
    
    private void retornarFiltro(){
    	Intent resultIntent = new Intent();
    	if (dtMovIni!= null && dtMovFin != null) {
    		resultIntent.putExtra("MOV_INI", dtMovIni.getTimeInMillis());
    		resultIntent.putExtra("MOV_FIN", dtMovFin.getTimeInMillis());
    	} else {
    		resultIntent.putExtra("MOV_INI", 0);
    		resultIntent.putExtra("MOV_FIN", 0);
    	}
    	if (contato != null) {
    		resultIntent.putExtra("CONTATO_ID", contato.getId());
    	} else {
    		resultIntent.putExtra("CONTATO_ID", 0);
    	}
    	if (conta != null) {
    		resultIntent.putExtra("CONTA_ID", conta.getId());
    	} else {
    		resultIntent.putExtra("CONTA_ID", 0);
    	}
    	if (categoria != null) {
    		resultIntent.putExtra("CATEGORIA_ID", categoria.getId());
    	} else {
    		resultIntent.putExtra("CATEGORIA_ID", 0);
    	}
    	
		setResult(RESULT_OK, resultIntent);				
		finish();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(0, LIMPAR_FILTROS, 0, R.string.limpar_filtros).setIcon(android.R.drawable.ic_menu_close_clear_cancel);
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
		case LIMPAR_FILTROS:
			extras = null;
			categoria = null;
			conta = null;
			contato = null;
			tvCategoria.setText("");
			tvConta.setText("");
			tvContato.setText("");
			imgBtnCategoria.setVisibility(View.GONE);
			imgBtnConta.setVisibility(View.GONE);
			imgBtnContato.setVisibility(View.GONE);
			setValores();
			break;

		default:
			break;
		}
    	return true;
    }

}
