package projetofinal.ftec.gui;

import java.util.Calendar;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Categoria;
import projetofinal.ftec.modelo.Categoria.Categorias;
import projetofinal.ftec.modelo.Conta;
import projetofinal.ftec.modelo.Conta.Contas;
import projetofinal.ftec.modelo.Contato;
import projetofinal.ftec.modelo.Contato.Contatos;
import projetofinal.ftec.modelo.Realizado;
import projetofinal.ftec.modelo.Realizado.Realizados;
import projetofinal.ftec.modelo.Sistema;
import projetofinal.ftec.persistencia.CategoriaDAO;
import projetofinal.ftec.persistencia.ContaDAO;
import projetofinal.ftec.persistencia.ContatoDAO;
import projetofinal.ftec.persistencia.RealizadoDAO;
import projetofinal.ftec.utils.CustomDecimalUtils;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class RealizadoEditar extends Activity {
	private EditText etDescricao;
	private EditText etValMovimento;
	private EditText etValMovimentoDec;
	private RadioGroup rgTipoMovimento;
	private RadioButton rbPagar;
	private RadioButton rbReceber;
	private Button btnDtMovimento;
	private TextView tvContato;
	private TextView tvConta;
	private TextView tvCategoria;
	private ImageButton imgBtnContato;
	private ImageButton imgBtnConta;
	private ImageButton imgBtnCategoria;
	
	private LinearLayout lContato;
	private LinearLayout lConta;
	private LinearLayout lCategoria;
	private LinearLayout lValMovimento;
	private Button btnSalvar;
	private Button btnCancelar;
	private Long id;
	private Intent resultIntent;
	private static final int PESQUISA_CONTA = 1;
	private static final int PESQUISA_CONTATO = 2;
	private static final int PESQUISA_CATEGORIA = 3;
	private static final int DIALOG_DT_MOVIMENTO = 4;
	private Realizado realizado;
	private Contato contato;
	private Conta conta;
	private Categoria categoria;
	private Calendar dtMov;
	@Override
	protected void onCreate(Bundle icicle) {		 
		
		super.onCreate(icicle);		
		setContentView(R.layout.edit_container);
		setTitle(R.string.lancar_realizado);	
        
		initComps();		
			
		Bundle extras = getIntent().getExtras();
		
		id = null;
		realizado = null;
		if (extras != null) {
			id = extras.getLong(Realizados._ID);
			if (id != null) {
				realizado = RealizadoDAO.buscarRealizado(id);
				if (realizado != null) {
					etDescricao.setText(realizado.getDescricao());
					etValMovimento.setText(CustomDecimalUtils.getIntPart(realizado.getVal_movimento()));
					etValMovimentoDec.setText(CustomDecimalUtils.getDecPart(realizado.getVal_movimento()));
					if (realizado.getConta() != null) {
						tvConta.setText(realizado.getConta().getDescricao());
						conta = realizado.getConta();
						imgBtnConta.setVisibility(View.VISIBLE);
					}
					if (realizado.getContato() != null) {
						tvContato.setText(realizado.getContato().getNome());
						contato = realizado.getContato();
						imgBtnContato.setVisibility(View.VISIBLE);
					}
					if (realizado.getCategoria() != null) {
						tvCategoria.setText(realizado.getCategoria().getDescricao());
						categoria = realizado.getCategoria();
						imgBtnCategoria.setVisibility(View.VISIBLE);
					}
					dtMov = realizado.getDt_movimento();
					rbPagar.setChecked(realizado.getTipo_movimento().compareTo("P") == 0);
					rbReceber.setChecked(realizado.getTipo_movimento().compareTo("R") == 0);
					setTitle(R.string.editar_realizado);
					
					//Desabilitar campos para edição
					rgTipoMovimento.setVisibility(View.GONE);
				}
			}			
		}
		if (realizado == null) {
			dtMov = Calendar.getInstance();
			rbPagar.setChecked(true);
		} else if ( extras.containsKey("MODO") &&
				extras.getString("MODO").compareTo("duplicar") == 0) {
			realizado.setId(0);
			rgTipoMovimento.setVisibility(View.VISIBLE);
			dtMov = Calendar.getInstance();
		}
		updateDisplay();
	}
	private void initComps() {
		ScrollView scrollView = (ScrollView) findViewById(R.id.edit_scroll);
		LayoutInflater factory = LayoutInflater.from(this);
        View viewRealizado = factory.inflate(R.layout.realizado_editar, null);
        scrollView.addView(viewRealizado);
        
        lValMovimento = (LinearLayout) findViewById(R.id.realizado_box_val_movimento);
        lContato = (LinearLayout) findViewById(R.id.consulta_linear_contato);
		lConta = (LinearLayout) findViewById(R.id.consulta_linear_conta);
		lCategoria = (LinearLayout) findViewById(R.id.consulta_linear_categoria);
		
		etDescricao = (EditText) findViewById(R.id.realizado_etDescricao);
		
		etValMovimento = (EditText) lValMovimento.findViewById(R.id.edit_text_inteiro);
		etValMovimentoDec = (EditText) lValMovimento.findViewById(R.id.edit_text_decimal);
		btnDtMovimento = (Button) findViewById(R.id.realizado_btn_dt_movimento);
		rgTipoMovimento = (RadioGroup) findViewById(R.id.realizado_rgTipo_movimento);
		rbPagar = (RadioButton) findViewById(R.id.realizado_rbPagar);
		rbReceber = (RadioButton) findViewById(R.id.realizado_rbReceber);
		tvCategoria = (TextView) findViewById(R.id.consulta_tvCategoria);
		tvConta = (TextView) findViewById(R.id.consulta_tvConta);
		tvContato = (TextView) findViewById(R.id.consulta_tvContato);
		imgBtnCategoria = (ImageButton) findViewById(R.id.consulta_btn_categoria_deselec);
		imgBtnConta = (ImageButton) findViewById(R.id.consulta_btn_conta_deselec);
		imgBtnContato = (ImageButton) findViewById(R.id.consulta_btn_contato_deselec);
        
		btnCancelar = (Button) findViewById(R.id.btnCancelar);
		btnSalvar = (Button) findViewById(R.id.btnSalvar);
		
		
		btnCancelar.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();				
			}
		});
		btnSalvar.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				salvar();
			}			
			
		});
		
		btnDtMovimento.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_DT_MOVIMENTO);
			}
		});
		
		lContato.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent it = new Intent(RealizadoEditar.this, ContatoPesquisar.class);
				it.putExtra("MODO", "consulta");
				startActivityForResult(it, PESQUISA_CONTATO);
			}
		});
		
		lConta.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent it = new Intent(RealizadoEditar.this, ContaPesquisar.class);
				it.putExtra("MODO", "consulta");
				startActivityForResult(it, PESQUISA_CONTA);
			}
		});
		
		lCategoria.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent it = new Intent(RealizadoEditar.this, CategoriaPesquisar.class);
				it.putExtra("MODO", "consulta");
				startActivityForResult(it, PESQUISA_CATEGORIA);
			}
		});
		imgBtnCategoria.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				categoria = null;
				tvCategoria.setText("");
				imgBtnCategoria.setVisibility(View.GONE);
			}
		});
		imgBtnConta.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				conta = null;
				tvConta.setText("");
				imgBtnConta.setVisibility(View.GONE);
			}
		});
		imgBtnContato.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				contato = null;
				tvContato.setText("");
				imgBtnContato.setVisibility(View.GONE);
			}
		});
	}
	
	public void salvar() {
		if (realizado == null) {
			realizado = new Realizado();
		}
		realizado.setDescricao(etDescricao.getText().toString());
		realizado.setVal_movimento(CustomDecimalUtils.toDouble(etValMovimento.getText().toString(), etValMovimentoDec.getText().toString()));

		if (rbPagar.isChecked()) {
			realizado.setTipo_movimento("P");
		} else {
			realizado.setTipo_movimento("R");
		}
		
		realizado.setDt_movimento(dtMov); 
		
		realizado.setConta(conta);
		realizado.setContato(contato);
		realizado.setCategoria(categoria);
		
		Sistema sis = Sistema.getSistema();
		if (sis != null && sis.getUsuarioLogado() != null) {
			realizado.setUsuario(sis.getUsuarioLogado());
		}
		try {
			Long idRealizado = RealizadoDAO.salvar(realizado);
			resultIntent = new Intent();
			resultIntent.putExtra("id", idRealizado);
			setResult(RESULT_OK, resultIntent);				
			finish();
		} catch (Exception e) {
			Toast.makeText(this, this.getResources().getString(Integer.parseInt(e.getMessage())), Toast.LENGTH_SHORT).show();
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
	        case DIALOG_DT_MOVIMENTO:
	        	return new DatePickerDialog(this,
                        mDateSetListenerMov,
                        dtMov.get(Calendar.YEAR), dtMov.get(Calendar.MONTH), dtMov.get(Calendar.DATE));
	    }
	    return null;
	}
	
	protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DIALOG_DT_MOVIMENTO:
            	((DatePickerDialog) dialog).updateDate(dtMov.get(Calendar.YEAR), dtMov.get(Calendar.MONTH), dtMov.get(Calendar.DATE));
                break;
        }
    }
	
	private DatePickerDialog.OnDateSetListener mDateSetListenerMov =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                        int dayOfMonth) {
                	dtMov.set(year, monthOfYear, dayOfMonth);
                    updateDisplay();
                }
            };   
    private void updateDisplay() {
        btnDtMovimento.setText(DateFormat.format("EEEE, dd/MM/yyyy", dtMov));
    }            
}
