package projetofinal.ftec.gui;

import java.util.Calendar;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Alarme;
import projetofinal.ftec.modelo.Alarme.Alarmes;
import projetofinal.ftec.modelo.Categoria;
import projetofinal.ftec.modelo.Categoria.Categorias;
import projetofinal.ftec.modelo.Conta;
import projetofinal.ftec.modelo.Conta.Contas;
import projetofinal.ftec.modelo.Contato;
import projetofinal.ftec.modelo.Contato.Contatos;
import projetofinal.ftec.modelo.Previsto;
import projetofinal.ftec.modelo.Previsto.Previstos;
import projetofinal.ftec.modelo.Sistema;
import projetofinal.ftec.persistencia.AlarmeDAO;
import projetofinal.ftec.persistencia.CategoriaDAO;
import projetofinal.ftec.persistencia.ContaDAO;
import projetofinal.ftec.persistencia.ContatoDAO;
import projetofinal.ftec.persistencia.PrevistoDAO;
import projetofinal.ftec.utils.CustomDecimalUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class PrevistoEditar extends Activity {
	private EditText etDescricao;
	private EditText etValPrevisto;
	private EditText etValPrevistoDec;
	private EditText etRepetir;
	private RadioGroup rgTipoMovimento;
	private RadioButton rbPagar;
	private RadioButton rbReceber;
	private Button btnDtVencimento;
	private Button btnDtEmissao;
	private Button btnRepetir;
	private CheckBox cbPagamentoAuto;
	private TextView tvContato;
	private TextView tvConta;
	private TextView tvCategoria;
	private TextView tvAlarme;
	private ImageButton imgBtnContato;
	private ImageButton imgBtnConta;
	private ImageButton imgBtnCategoria;
	private ImageButton imgBtnAlarme;
	
	private LinearLayout lContato;
	private LinearLayout lConta;
	private LinearLayout lCategoria;
	private LinearLayout lAlarme;
	private LinearLayout lRepetir;
	private LinearLayout lValPrevisto;
	private Button btnSalvar;
	private Button btnCancelar;
	private Long id;
	private Intent resultIntent;
	private static final int PESQUISA_CONTA = 1;
	private static final int PESQUISA_CONTATO = 2;
	private static final int PESQUISA_CATEGORIA = 3;
	private static final int PESQUISA_ALARME = 4;
	private static final int DIALOG_REPETIR = 5;
	private static final int DIALOG_VENCIMENTO = 6;
	private static final int DIALOG_EMISSAO = 7;
	private Previsto previsto;
	private Contato contato;
	private Conta conta;
	private Categoria categoria;
	private Alarme alarme;
	private Calendar dtVenc;
	private Calendar dtEmis;
	private String[] optIntervalo;
	private int intervaloSelecionado;
	@Override
	protected void onCreate(Bundle icicle) {		 
		
		super.onCreate(icicle);		
		setContentView(R.layout.edit_container);
		setTitle(R.string.lancar_previsto);	
        
		initComps();		
			
		Bundle extras = getIntent().getExtras();
		
		id = null;
		if (extras != null && extras.containsKey(Previstos._ID)) {
			id = extras.getLong(Previstos._ID);
			if (id != null) {
				previsto = PrevistoDAO.buscarPrevisto(id);
				if (previsto != null) {
					etDescricao.setText(previsto.getDescricao());
					etValPrevisto.setText(CustomDecimalUtils.getIntPart(previsto.getVal_previsto()));
					etValPrevistoDec.setText(CustomDecimalUtils.getDecPart(previsto.getVal_previsto()));
					if (previsto.getAlarme() != null) {
						tvAlarme.setText(previsto.getAlarme().getDescricao());
						alarme = previsto.getAlarme(); 
						imgBtnAlarme.setVisibility(View.VISIBLE);
						
					}
					if (previsto.getConta() != null) {
						tvConta.setText(previsto.getConta().getDescricao());
						conta = previsto.getConta();
						imgBtnConta.setVisibility(View.VISIBLE);
					}
					if (previsto.getContato() != null) {
						tvContato.setText(previsto.getContato().getNome());
						contato = previsto.getContato();
						imgBtnContato.setVisibility(View.VISIBLE);
					}
					if (previsto.getCategoria() != null) {
						tvCategoria.setText(previsto.getCategoria().getDescricao());
						categoria = previsto.getCategoria();
						imgBtnCategoria.setVisibility(View.VISIBLE);
					}
					dtEmis = previsto.getDt_emissao();
					dtVenc = previsto.getDt_vencimento();
					
					rbPagar.setChecked(previsto.getTipo_movimento().compareTo("P") == 0);
					rbReceber.setChecked(previsto.getTipo_movimento().compareTo("R") == 0);
					cbPagamentoAuto.setChecked(previsto.getPagamento_automatico().compareTo("S") == 0);
					setTitle(R.string.editar_previsto);
					
					//Desabilitar campos para edicao
					lRepetir.setVisibility(View.GONE);
					rgTipoMovimento.setVisibility(View.GONE);
					
				}
			}			
		}
		if (previsto == null) {
			dtEmis = Calendar.getInstance();
			dtVenc = Calendar.getInstance();
			rbPagar.setChecked(true);
		} else if (extras.containsKey("MODO") &&
				extras.getString("MODO").compareTo("duplicar") == 0) {
			previsto.setId(0);
			lRepetir.setVisibility(View.VISIBLE);
			cbPagamentoAuto.setVisibility(View.VISIBLE);
			rgTipoMovimento.setVisibility(View.VISIBLE);
			dtEmis = Calendar.getInstance();
			dtVenc = Calendar.getInstance();
		}
		updateDisplay();
	}
	private void initComps() {
		ScrollView scrollView = (ScrollView) findViewById(R.id.edit_scroll);
		LayoutInflater factory = LayoutInflater.from(this);
        View viewPrevisto = factory.inflate(R.layout.previsto_editar, null);
        scrollView.addView(viewPrevisto);
        
        lValPrevisto = (LinearLayout)	findViewById(R.id.previsto_val_previsto);
		
		etDescricao = (EditText) findViewById(R.id.previsto_etDescricao);
		etValPrevisto = (EditText) lValPrevisto.findViewById(R.id.edit_text_inteiro);
		etValPrevistoDec = (EditText) lValPrevisto.findViewById(R.id.edit_text_decimal);
		etRepetir = (EditText) findViewById(R.id.previsto_etRepetir);
		btnDtVencimento = (Button) findViewById(R.id.previsto_btn_vencimento);
		btnDtEmissao = (Button) findViewById(R.id.previsto_btn_emissao);
		btnRepetir = (Button) findViewById(R.id.previsto_btn_repetir);
		cbPagamentoAuto = (CheckBox) findViewById(R.id.previsto_cbPagamentoAuto);
		rgTipoMovimento = (RadioGroup) findViewById(R.id.previsto_rgTipo_movimento);
		rbPagar = (RadioButton) findViewById(R.id.previsto_rbPagar);
		rbReceber = (RadioButton) findViewById(R.id.previsto_rbReceber);
		tvAlarme = (TextView) findViewById(R.id.consulta_tvAlarme);
		tvCategoria = (TextView) findViewById(R.id.consulta_tvCategoria);
		tvConta = (TextView) findViewById(R.id.consulta_tvConta);
		tvContato = (TextView) findViewById(R.id.consulta_tvContato);
		optIntervalo = getResources().getStringArray(R.array.intervalo_periodo);
		imgBtnAlarme = (ImageButton) findViewById(R.id.consulta_btn_alarme_deselec);
		imgBtnCategoria = (ImageButton) findViewById(R.id.consulta_btn_categoria_deselec);
		imgBtnConta = (ImageButton) findViewById(R.id.consulta_btn_conta_deselec);
		imgBtnContato = (ImageButton) findViewById(R.id.consulta_btn_contato_deselec);
        
		btnCancelar = (Button) findViewById(R.id.btnCancelar);
		btnSalvar = (Button) findViewById(R.id.btnSalvar);
		lContato = (LinearLayout) findViewById(R.id.consulta_linear_contato);
		lConta = (LinearLayout) findViewById(R.id.consulta_linear_conta);
		lCategoria = (LinearLayout) findViewById(R.id.consulta_linear_categoria);
		lAlarme = (LinearLayout) findViewById(R.id.consulta_linear_alarme);
		lRepetir = (LinearLayout) findViewById(R.id.previsto_linear_repetir);
		
		intervaloSelecionado = 2;
		btnRepetir.setText(optIntervalo[intervaloSelecionado]);
		
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
		
		btnRepetir.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_REPETIR);	
			}
		});
		
		btnDtEmissao.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_EMISSAO);
			}
		});
		btnDtVencimento.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_VENCIMENTO);
			}
		});
		
		lContato.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent it = new Intent(PrevistoEditar.this, ContatoPesquisar.class);
				it.putExtra("MODO", "consulta");
				startActivityForResult(it, PESQUISA_CONTATO);
			}
		});
		
		lConta.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent it = new Intent(PrevistoEditar.this, ContaPesquisar.class);
				it.putExtra("MODO", "consulta");
				startActivityForResult(it, PESQUISA_CONTA);
			}
		});
		
		lCategoria.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent it = new Intent(PrevistoEditar.this, CategoriaPesquisar.class);
				it.putExtra("MODO", "consulta");
				startActivityForResult(it, PESQUISA_CATEGORIA);
			}
		});
		
		lAlarme.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent it = new Intent(PrevistoEditar.this, AlarmePesquisar.class);
				it.putExtra("MODO", "consulta");
				startActivityForResult(it, PESQUISA_ALARME);
			}
		});
		imgBtnAlarme.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alarme = null;
				tvAlarme.setText("");
				imgBtnAlarme.setVisibility(View.GONE);
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
		if (previsto == null) {
			previsto = new Previsto();
		}
		int qtdRepetir = 0;
		
		previsto.setDescricao(etDescricao.getText().toString());
		
		previsto.setVal_previsto(CustomDecimalUtils.toDouble(etValPrevisto.getText().toString(), etValPrevistoDec.getText().toString()));
		
		if (rbPagar.isChecked()) {
			previsto.setTipo_movimento("P");
		} else {
			previsto.setTipo_movimento("R");
		}
		
		if (cbPagamentoAuto.isChecked()) {
			previsto.setPagamento_automatico("S");
		} else {
			previsto.setPagamento_automatico("N");
		}
		
		previsto.setDt_emissao(dtEmis);
		previsto.setDt_vencimento(dtVenc);
		
		previsto.setConta(conta);
		previsto.setContato(contato);
		previsto.setCategoria(categoria);
		previsto.setAlarme(alarme);
		
		if (etRepetir.getText().toString().compareTo("") != 0) {
			qtdRepetir = Integer.parseInt(etRepetir.getText().toString());
		}
		
		Sistema sis = Sistema.getSistema();
		if (sis != null && sis.getUsuarioLogado() != null) {
			previsto.setUsuario(sis.getUsuarioLogado());
		}
		try {
			Long idPrevisto = PrevistoDAO.salvar(previsto);
			if (idPrevisto != 0) {
				PrevistoDAO.replicarPrevisto(previsto, qtdRepetir, intervaloSelecionado);
			}
			resultIntent = new Intent();
			resultIntent.putExtra("id", idPrevisto);
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
		    			  cbPagamentoAuto.setChecked(categoria.getPagamento_automatico().compareTo("S") == 0);
		    			  imgBtnCategoria.setVisibility(View.VISIBLE);
		    		  }
		    	  }
		    	  break;
		      }
		      case (PESQUISA_ALARME) : {
		    	  if (data.getLongExtra(Alarmes._ID, 0) != 0) {
		    		  alarme = AlarmeDAO.buscarAlarme(data.getLongExtra(Alarmes._ID, 0));
		    		  if (alarme != null) {
		    			  tvAlarme.setText(alarme.getDescricao());
		    			  imgBtnAlarme.setVisibility(View.VISIBLE);
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
	        case DIALOG_EMISSAO:
	        	return new DatePickerDialog(this,
                        mDateSetListenerEmis,
                        dtEmis.get(Calendar.YEAR), dtEmis.get(Calendar.MONTH), dtEmis.get(Calendar.DATE));
	        case DIALOG_VENCIMENTO:
	            return new DatePickerDialog(this,
	                        mDateSetListenerVenc,
	                        dtVenc.get(Calendar.YEAR), dtVenc.get(Calendar.MONTH), dtVenc.get(Calendar.DATE));
	        case DIALOG_REPETIR:
	        	return new AlertDialog.Builder(PrevistoEditar.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.selecionar_intervalo)
                .setSingleChoiceItems(R.array.intervalo_periodo, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	intervaloSelecionado = whichButton;
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	btnRepetir.setText(optIntervalo[intervaloSelecionado]);
                    }
                })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
               .create();
	    }
	    return null;
	}
	
	protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DIALOG_EMISSAO:
            	((DatePickerDialog) dialog).updateDate(dtEmis.get(Calendar.YEAR), dtEmis.get(Calendar.MONTH), dtEmis.get(Calendar.DATE));
                break;
            case DIALOG_VENCIMENTO:
                ((DatePickerDialog) dialog).updateDate(dtVenc.get(Calendar.YEAR), dtVenc.get(Calendar.MONTH), dtVenc.get(Calendar.DATE));
                break;
        }
    }
	
	private DatePickerDialog.OnDateSetListener mDateSetListenerEmis =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                        int dayOfMonth) {
                	dtEmis.set(year, monthOfYear, dayOfMonth);
                    updateDisplay();
                }
            };
    private DatePickerDialog.OnDateSetListener mDateSetListenerVenc =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                        int dayOfMonth) {
                	dtVenc.set(year, monthOfYear, dayOfMonth);
                    updateDisplay();
                }
            };   
    private void updateDisplay() {
        btnDtEmissao.setText(DateFormat.format("EEEE, dd/MM/yyyy", dtEmis));
        btnDtVencimento.setText(DateFormat.format("EEEE, dd/MM/yyyy", dtVenc));
    }            
}
