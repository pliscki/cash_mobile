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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class PrevistoFiltrar extends Activity implements OnClickListener {
	private Contato contato;
	private Categoria categoria;
	private Conta conta;
	private Bundle extras;
	private Button btnVencIni;
	private Button btnVencFin;
	private Button btnEmisIni;
	private Button btnEmisFin;
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
	private CheckBox cbPendente;
	private CheckBox cbBaixado;
	
	private static final int PESQUISA_CONTA = 1;
	private static final int PESQUISA_CONTATO = 2;
	private static final int PESQUISA_CATEGORIA = 3;
	private static final int DIALOG_VENC_INI = 4;
	private static final int DIALOG_VENC_FIN = 5;
	private static final int DIALOG_EMIS_INI = 6;
	private static final int DIALOG_EMIS_FIN = 7;
	private static final int BTN_OK = 101;
	private static final int BTN_CANCELAR = 102;
	private static final int BTN_VENC_INI = 103;
	private static final int BTN_VENC_FIN = 104;
	private static final int BTN_EMIS_INI = 105;
	private static final int BTN_EMIS_FIN = 106;
	private static final int LINEAR_CONTATO = 107;
	private static final int LINEAR_CATEGORIA = 108;
	private static final int LINEAR_CONTA = 109;
	private static final int LIMPAR_FILTROS = 110;
	private static final int BTN_DESELECT_CONTA = 111;
	private static final int BTN_DESELECT_CATEGORIA = 112;
	private static final int BTN_DESELECT_CONTATO = 113;
	
	private Calendar vencIni;
	private Calendar vencFin;
	private Calendar emisIni;
	private Calendar emisFin;
	
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
        View viewPrevisto = factory.inflate(R.layout.previsto_filtros, null);
        scrollView.addView(viewPrevisto);
        btnVencIni = (Button) findViewById(R.id.previsto_filtro_btnVencInicial);
        btnVencFin = (Button) findViewById(R.id.previsto_filtro_btnVencFinal);
        btnEmisIni = (Button) findViewById(R.id.previsto_filtro_btnEmisInicial);
        btnEmisFin = (Button) findViewById(R.id.previsto_filtro_btnEmisFinal);
        btnOk = (Button) findViewById(R.id.btnSalvar);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        tvCategoria = (TextView) findViewById(R.id.consulta_tvCategoria);
        tvConta = (TextView) findViewById(R.id.consulta_tvConta);
        tvContato = (TextView) findViewById(R.id.consulta_tvContato);
        lCategoria = (LinearLayout) findViewById(R.id.consulta_linear_categoria);
        lConta = (LinearLayout) findViewById(R.id.consulta_linear_conta);
        lContato = (LinearLayout) findViewById(R.id.consulta_linear_contato);
        cbBaixado = (CheckBox) findViewById(R.id.previsto_filtro_cbBaixado);
        cbPendente = (CheckBox) findViewById(R.id.previsto_filtro_cbPendente);
        imgBtnCategoria = (ImageButton) findViewById(R.id.consulta_btn_categoria_deselec);
		imgBtnConta = (ImageButton) findViewById(R.id.consulta_btn_conta_deselec);
		imgBtnContato = (ImageButton) findViewById(R.id.consulta_btn_contato_deselec);
        
        btnOk.setId(BTN_OK);
        btnCancelar.setId(BTN_CANCELAR);
        btnVencIni.setId(BTN_VENC_INI);
        btnVencFin.setId(BTN_VENC_FIN);
        btnEmisIni.setId(BTN_EMIS_INI);
        btnEmisFin.setId(BTN_EMIS_FIN);
        lCategoria.setId(LINEAR_CATEGORIA);
        lConta.setId(LINEAR_CONTA);
        lContato.setId(LINEAR_CONTATO);
        imgBtnCategoria.setId(BTN_DESELECT_CATEGORIA);
        imgBtnConta.setId(BTN_DESELECT_CONTA);
        imgBtnContato.setId(BTN_DESELECT_CONTATO);
        
        btnOk.setText(R.string.ok);
        btnCancelar.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        btnVencIni.setOnClickListener(this);
        btnVencFin.setOnClickListener(this);
        btnEmisIni.setOnClickListener(this);
        btnEmisFin.setOnClickListener(this);
        lCategoria.setOnClickListener(this);
        lConta.setOnClickListener(this);
        lContato.setOnClickListener(this);
        imgBtnCategoria.setOnClickListener(this);
        imgBtnConta.setOnClickListener(this);
        imgBtnContato.setOnClickListener(this);
		
	}
	
	private void setValores() {
		vencIni = Calendar.getInstance();
        vencFin = Calendar.getInstance();
        emisIni = Calendar.getInstance();
        emisFin = Calendar.getInstance();
        
        vencFin.add(Calendar.MONTH, 1);        
		
        if (extras != null) {
			if (extras.containsKey("VENC_INI")) {
				vencIni.setTimeInMillis(extras.getLong("VENC_INI"));
			}
			if (extras.containsKey("VENC_FIN")) {
				vencFin.setTimeInMillis(extras.getLong("VENC_FIN"));
			}
			if (extras.containsKey("EMIS_INI")) {
				emisIni.setTimeInMillis(extras.getLong("EMIS_INI"));
				btnEmisIni.setText(DateFormat.format("dd/MM/yyyy", emisIni.getTime()));
			}
			if (extras.containsKey("EMIS_FIN")) {
				emisFin.setTimeInMillis(extras.getLong("VENC_FIN"));
				btnEmisFin.setText(DateFormat.format("dd/MM/yyyy", emisFin.getTime()));
			}
			if (extras.containsKey("CONTA_ID")) {
				conta = ContaDAO.buscarConta(extras.getLong("CONTA_ID"));
				if (conta != null) {
					tvConta.setText(conta.getDescricao());
					imgBtnConta.setVisibility(View.VISIBLE);
				}
			}
			if (extras.containsKey("CONTATO_ID")) {
				contato = ContatoDAO.buscarContato(extras.getLong("CONTATO_ID"));
				if (contato != null) {
					tvContato.setText(contato.getNome());
					imgBtnContato.setVisibility(View.VISIBLE);
				}
			}
			if (extras.containsKey("CATEGORIA_ID")) {
				categoria = CategoriaDAO.buscarCategoria(extras.getLong("CATEGORIA_ID"));
				if (categoria != null) {
					tvCategoria.setText(categoria.getDescricao());
					imgBtnCategoria.setVisibility(View.VISIBLE);
				}
			}
			if (extras.containsKey("PENDENTES")) {
				cbPendente.setChecked(extras.getBoolean("PENDENTES"));
			}
			if (extras.containsKey("BAIXADOS")) {
				cbBaixado.setChecked(extras.getBoolean("BAIXADOS"));
			}
        }
		btnVencIni.setText(DateFormat.format("dd/MM/yyyy", vencIni.getTime()));
		btnVencFin.setText(DateFormat.format("dd/MM/yyyy", vencFin.getTime()));
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
			case BTN_VENC_INI:
				showDialog(DIALOG_VENC_INI);
				break;
			case BTN_VENC_FIN:
				showDialog(DIALOG_VENC_FIN);
				break;
			case BTN_EMIS_INI:
				showDialog(DIALOG_EMIS_INI);
				break;
			case BTN_EMIS_FIN:
				showDialog(DIALOG_EMIS_FIN);
				break;
			case LINEAR_CATEGORIA:
				it = new Intent(PrevistoFiltrar.this, CategoriaPesquisar.class);
				it.putExtra("MODO", "consulta");
				startActivityForResult(it, PESQUISA_CATEGORIA);
				break;
			case LINEAR_CONTA:
				it = new Intent(PrevistoFiltrar.this, ContaPesquisar.class);
				it.putExtra("MODO", "consulta");
				startActivityForResult(it, PESQUISA_CONTA);
				break;
			case LINEAR_CONTATO:
				it = new Intent(PrevistoFiltrar.this, ContatoPesquisar.class);
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
	        case DIALOG_EMIS_INI:
	        	return new DatePickerDialog(this,
                        mDateSetListenerEmisIni,
                        emisIni.get(Calendar.YEAR), emisIni.get(Calendar.MONTH), emisIni.get(Calendar.DATE));
	        case DIALOG_EMIS_FIN:
	            return new DatePickerDialog(this,
	                        mDateSetListenerEmisFin,
	                        emisFin.get(Calendar.YEAR), emisFin.get(Calendar.MONTH), emisFin.get(Calendar.DATE));
	        case DIALOG_VENC_INI:
	            return new DatePickerDialog(this,
	                        mDateSetListenerVencIni,
	                        vencIni.get(Calendar.YEAR), vencIni.get(Calendar.MONTH), vencIni.get(Calendar.DATE));
	        case DIALOG_VENC_FIN:
	            return new DatePickerDialog(this,
	                        mDateSetListenerVencFin,
	                        vencFin.get(Calendar.YEAR), vencFin.get(Calendar.MONTH), vencFin.get(Calendar.DATE));
	    }
	    return null;
	}
	
	protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DIALOG_EMIS_INI:
            	((DatePickerDialog) dialog).updateDate(emisIni.get(Calendar.YEAR), emisIni.get(Calendar.MONTH), emisIni.get(Calendar.DATE));
                break;
            case DIALOG_EMIS_FIN:
                ((DatePickerDialog) dialog).updateDate(emisFin.get(Calendar.YEAR), emisFin.get(Calendar.MONTH), emisFin.get(Calendar.DATE));
                break;
            case DIALOG_VENC_INI:
            	((DatePickerDialog) dialog).updateDate(vencIni.get(Calendar.YEAR), vencIni.get(Calendar.MONTH), vencIni.get(Calendar.DATE));
                break;
            case DIALOG_VENC_FIN:
                ((DatePickerDialog) dialog).updateDate(vencFin.get(Calendar.YEAR), vencFin.get(Calendar.MONTH), vencFin.get(Calendar.DATE));
                break;
        }
    }
	
	private DatePickerDialog.OnDateSetListener mDateSetListenerEmisIni =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                        int dayOfMonth) {
                	emisIni.set(year, monthOfYear, dayOfMonth);
                    updateDisplay(DIALOG_EMIS_INI);
                }
            };
	private DatePickerDialog.OnDateSetListener mDateSetListenerEmisFin =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                        int dayOfMonth) {
                	emisFin.set(year, monthOfYear, dayOfMonth);
                    updateDisplay(DIALOG_EMIS_FIN);
                }
            };            
	private DatePickerDialog.OnDateSetListener mDateSetListenerVencIni =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                        int dayOfMonth) {
                	vencIni.set(year, monthOfYear, dayOfMonth);
                    updateDisplay(DIALOG_VENC_INI);
                }
            };
	private DatePickerDialog.OnDateSetListener mDateSetListenerVencFin =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                        int dayOfMonth) {
                	vencFin.set(year, monthOfYear, dayOfMonth);
                    updateDisplay(DIALOG_VENC_FIN);
                }
            };
            
    private void updateDisplay(int id) {
    	switch (id) {
		case DIALOG_VENC_INI:
			btnVencIni.setText(DateFormat.format("dd/MM/yyyy", vencIni.getTime()));
			break;
		case DIALOG_VENC_FIN:
			btnVencFin.setText(DateFormat.format("dd/MM/yyyy", vencFin.getTime()));
			break;
		case DIALOG_EMIS_INI:
			btnEmisIni.setText(DateFormat.format("dd/MM/yyyy", emisIni.getTime()));
			break;
		case DIALOG_EMIS_FIN:
			btnEmisFin.setText(DateFormat.format("dd/MM/yyyy", emisFin.getTime()));
			break;
		default:
			btnVencIni.setText(DateFormat.format("dd/MM/yyyy", vencIni.getTime()));
			btnVencFin.setText(DateFormat.format("dd/MM/yyyy", vencFin.getTime()));
			btnEmisIni.setText(DateFormat.format("dd/MM/yyyy", emisIni.getTime()));
			btnEmisFin.setText(DateFormat.format("dd/MM/yyyy", emisFin.getTime()));
			break;
		}
    }
    
    private boolean validarFiltros() {
    	if (vencIni == null ^ vencFin == null) {
    		Toast.makeText(this, getResources().getString(R.string.filtro_vencimento_invalido) , Toast.LENGTH_SHORT).show();
    		return false;
    	} else if (vencIni != null && vencFin != null){
    		if (vencIni.compareTo(vencFin) > 0) {
    			Toast.makeText(this,getResources().getString(R.string.venc_inicial_menor_final) , Toast.LENGTH_SHORT).show();
    			return false;
    		}
    	}
    	if (emisIni == null ^ emisFin == null) {
    		Toast.makeText(this, getResources().getString(R.string.filtro_emissao_invalido) , Toast.LENGTH_SHORT).show();
    		return false;
    	} else if (emisIni != null && emisFin != null){
    		if (emisIni.compareTo(emisFin) > 0) {
    			Toast.makeText(this, getResources().getString(R.string.emis_inicial_menor_final) , Toast.LENGTH_SHORT).show();
    			return false;
    		}
    	}
    	return true;
    }
    
    private void retornarFiltro(){
    	Intent resultIntent = new Intent();
    	if (vencIni!= null && vencFin != null) {
    		resultIntent.putExtra("VENC_INI", vencIni.getTimeInMillis());
    		resultIntent.putExtra("VENC_FIN", vencFin.getTimeInMillis());
    	} else {
    		resultIntent.putExtra("VENC_INI", 0);
    		resultIntent.putExtra("VENC_FIN", 0);
    	}
    	if (btnEmisIni.getText().toString().compareTo("") != 0 &&
    		btnEmisFin.getText().toString().compareTo("") != 0 &&
			emisIni != null && vencFin != null) {
    		resultIntent.putExtra("EMIS_INI", emisIni.getTimeInMillis());
    		resultIntent.putExtra("EMIS_FIN", emisFin.getTimeInMillis());
    	} else {
    		resultIntent.putExtra("EMIS_INI", 0);
    		resultIntent.putExtra("EMIS_FIN", 0);
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
    	
    	resultIntent.putExtra("PENDENTES", cbPendente.isChecked());
    	resultIntent.putExtra("BAIXADOS", cbBaixado.isChecked());
    	
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
			btnEmisFin.setText("");
			btnEmisIni.setText("");
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
