package projetofinal.ftec.gui;

import java.util.Calendar;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Conta;
import projetofinal.ftec.modelo.Contato.Contatos;
import projetofinal.ftec.modelo.Previsto;
import projetofinal.ftec.modelo.Previsto.Previstos;
import projetofinal.ftec.persistencia.ContaDAO;
import projetofinal.ftec.persistencia.PrevistoDAO;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class BaixarTitulo extends Activity {
	
	private Previsto previsto;
	private Conta conta;
	private TextView tvTipoMovimento;
	private TextView tvContato;
	private TextView tvValPrevisto;
	private TextView tvConta;
	private TextView tvValBaixa;
	private EditText etValBaixa;
	private EditText etValBaixaDec;
	private Button btnDtMovimento;
	private Button btnSalvar;
	private Button btnCancelar;
	private LinearLayout lConta;
	private Calendar dtMovimento;
	private LinearLayout lValBaixa;
	private ImageButton imgBtnConta;
	
	
	private static final int PESQUISA_CONTA = 1;
	private static final int DIALOG_MOVIMENTO = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.edit_container);
		setTitle(R.string.baixar_titulo);	
        
		initComps();		
			
		Bundle extras = getIntent().getExtras();
		
		if (extras != null) {
			if (extras.containsKey(Previstos._ID)) {
				previsto = PrevistoDAO.buscarPrevisto(extras.getLong(Previstos._ID));
				if (previsto == null) {
					setResult(RESULT_CANCELED);
					finish();
				} else {
					if (previsto.getConta() != null) {
						tvConta.setText(previsto.getConta().getDescricao());
						conta = previsto.getConta();
						imgBtnConta.setVisibility(View.VISIBLE);
					}
					if (previsto.getContato() != null) {
						tvContato.setText(previsto.getContato().getNome());
					}
					if (previsto.getTipo_movimento().compareTo("P") == 0) {
						tvTipoMovimento.setText(R.string.pagar_a);
						tvValBaixa.setText(R.string.val_pago);
					} else {
						tvTipoMovimento.setText(R.string.receber_de);
						tvValBaixa.setText(R.string.val_recebido);
					}
					tvValPrevisto.setText(CustomDecimalUtils.format(previsto.getVal_previsto()));
					etValBaixa.setText(CustomDecimalUtils.getIntPart(previsto.getVal_previsto()));
					etValBaixaDec.setText(CustomDecimalUtils.getDecPart(previsto.getVal_previsto()));
					dtMovimento = Calendar.getInstance();
					updateDisplay();
				}
			}
			
		}
	}
	
	private void initComps() {
		ScrollView scrollView = (ScrollView) findViewById(R.id.edit_scroll);
		LayoutInflater factory = LayoutInflater.from(this);
        View viewBaixarTitulo = factory.inflate(R.layout.baixar_titulo, null);
        scrollView.addView(viewBaixarTitulo);
        
        lValBaixa = (LinearLayout) findViewById(R.id.baixar_titulo_val_baixa);
        
        tvTipoMovimento = (TextView) findViewById(R.id.baixar_titulo_tvTipoMovimento);
        tvConta = (TextView) findViewById(R.id.consulta_tvConta);
        tvContato = (TextView) findViewById(R.id.baixar_titulo_tvContato);
        tvValBaixa = (TextView) findViewById(R.id.baixar_titulo_tvValorBaixa);
        tvValPrevisto = (TextView) findViewById(R.id.baixar_titulo_tvValPrevisto);
        etValBaixa = (EditText) lValBaixa.findViewById(R.id.edit_text_inteiro);
        etValBaixaDec = (EditText) lValBaixa.findViewById(R.id.edit_text_decimal);
        btnDtMovimento = (Button) findViewById(R.id.baixar_titulo_btn_dt_movimento);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
		btnSalvar = (Button) findViewById(R.id.btnSalvar);
        lConta = (LinearLayout) findViewById(R.id.consulta_linear_conta);
        imgBtnConta = (ImageButton) findViewById(R.id.consulta_btn_conta_deselec);
       
        btnSalvar.setText(R.string.baixar);        
        
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
        
        lConta.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent it = new Intent(BaixarTitulo.this, ContaPesquisar.class);
				it.putExtra("MODO", "consulta");
				startActivityForResult(it, PESQUISA_CONTA);
			}
		});
        
        btnDtMovimento.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_MOVIMENTO);
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
	}
	
	private void salvar() {
    	if (previsto != null) {
    		try {
				Double valMovimento = CustomDecimalUtils.toDouble(etValBaixa.getText().toString(), etValBaixaDec.getText().toString());
				previsto.baixarTitulo(conta, valMovimento, dtMovimento);
				setResult(RESULT_OK);
				finish();
			} catch (Exception e) {
				Toast.makeText(this, this.getResources().getString(Integer.parseInt(e.getMessage())), Toast.LENGTH_SHORT).show();
			}
    	}
    }
	
	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
		   switch(requestCode) { 
		      case (PESQUISA_CONTA) : {
		    	  if (data.getLongExtra(Contatos._ID, 0) != 0) {
		    		  conta = ContaDAO.buscarConta(data.getLongExtra(Contatos._ID, 0));
		    		  if (conta != null) {
		    			  tvConta.setText(conta.getDescricao());
		    			  imgBtnConta.setVisibility(View.VISIBLE);
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
	        case DIALOG_MOVIMENTO:
	        	return new DatePickerDialog(this,
                        mDateSetListenerMov,
                        dtMovimento.get(Calendar.YEAR), dtMovimento.get(Calendar.MONTH), dtMovimento.get(Calendar.DATE));
	    }
	    return null;
	}
	
	protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DIALOG_MOVIMENTO:
            	((DatePickerDialog) dialog).updateDate(dtMovimento.get(Calendar.YEAR), dtMovimento.get(Calendar.MONTH), dtMovimento.get(Calendar.DATE));
                break;
        }
    }
	
	private DatePickerDialog.OnDateSetListener mDateSetListenerMov =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                        int dayOfMonth) {
                	dtMovimento.set(year, monthOfYear, dayOfMonth);
                    updateDisplay();
                }
            };
            
    private void updateDisplay() {
        btnDtMovimento.setText(DateFormat.format("EEEE, dd/MM/yyyy", dtMovimento));
    }            

}
