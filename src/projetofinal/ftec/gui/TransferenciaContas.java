package projetofinal.ftec.gui;

import java.util.Calendar;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Categoria;
import projetofinal.ftec.modelo.Categoria.Categorias;
import projetofinal.ftec.modelo.Conta;
import projetofinal.ftec.modelo.Conta.Contas;
import projetofinal.ftec.modelo.Realizado;
import projetofinal.ftec.modelo.Sistema;
import projetofinal.ftec.persistencia.CategoriaDAO;
import projetofinal.ftec.persistencia.ContaDAO;
import projetofinal.ftec.persistencia.RealizadoDAO;
import projetofinal.ftec.utils.CustomDecimalUtils;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

public class TransferenciaContas extends Activity {
	private Conta contaOrigem;
	private Conta contaDestino;
	private Categoria categoria;
	private LinearLayout lContaOrigem;
	private LinearLayout lContaDestino;
	private LinearLayout lValTransferencia;
	private LinearLayout lCategoria;
	private TextView tvContaOrigem;
	private TextView tvContaDestino;
	private TextView tvLabelContaOrigem;
	private TextView tvLabelContaDestino;
	private TextView tvCategoria;
	private Button btnDtTransferencia;
	private Button btnSalvar;
	private Button btnCancelar;
	private EditText etValTransferencia;
	private EditText etvalTransferenciaDec;
	private EditText etDescricao;
	private Calendar dtTransferencia;
	private ImageButton imgBtnContaOrigem;
	private ImageButton imgBtnContaDestino;
	private ImageButton imgBtnCategoria;
	
	private static final int DLG_DATA_TRANSF = 1;
	private static final int PESQUISA_CONTA_ORIGEM = 2;
	private static final int PESQUISA_CONTA_DESTINO = 3;
	private static final int PESQUISA_CATEGORIA = 4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.edit_container);
		setTitle(R.string.transferencia_entre_contas);	
        
		initComps();
		
		dtTransferencia = Calendar.getInstance();
		
		updateDisplay();
	}
	
	private void initComps() {
		ScrollView scrollView = (ScrollView) findViewById(R.id.edit_scroll);
		LayoutInflater factory = LayoutInflater.from(this);
        View viewTransferencia = factory.inflate(R.layout.transferencia_contas, null);
        scrollView.addView(viewTransferencia);
        
        lCategoria = (LinearLayout) findViewById(R.id.consulta_linear_categoria);
        lContaDestino = (LinearLayout) findViewById(R.id.transferencia_conta_destino);
        lContaOrigem = (LinearLayout) findViewById(R.id.transferencia_conta_origem);
        lValTransferencia = (LinearLayout) findViewById(R.id.transferencia_val_transferencia);
        
        tvContaDestino = (TextView) lContaDestino.findViewById(R.id.consulta_tvConta);
        tvLabelContaDestino = (TextView) lContaDestino.findViewById(R.id.consulta_label_conta);
        imgBtnContaDestino = (ImageButton) lContaDestino.findViewById(R.id.consulta_btn_conta_deselec);
        tvContaOrigem = (TextView) lContaOrigem.findViewById(R.id.consulta_tvConta);
        tvLabelContaOrigem = (TextView) lContaOrigem.findViewById(R.id.consulta_label_conta);
        imgBtnContaOrigem = (ImageButton) lContaOrigem.findViewById(R.id.consulta_btn_conta_deselec);
        tvCategoria = (TextView) findViewById(R.id.consulta_tvCategoria);
        btnDtTransferencia = (Button) findViewById(R.id.transferencia_btn_dtMovimento);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        etDescricao = (EditText) findViewById(R.id.transferencia_tvDescricao);
        etValTransferencia = (EditText) lValTransferencia.findViewById(R.id.edit_text_inteiro);
        etvalTransferenciaDec = (EditText) lValTransferencia.findViewById(R.id.edit_text_decimal);
        imgBtnCategoria = (ImageButton) findViewById(R.id.consulta_btn_categoria_deselec);
        
        tvLabelContaDestino.setText(R.string.para_conta);
        tvLabelContaOrigem.setText(R.string.da_conta);
        
        btnSalvar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				salvar();
			}
		});
        btnCancelar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				setResult(RESULT_CANCELED);
				finish();
			}
		});
        
        btnDtTransferencia.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DLG_DATA_TRANSF);
			}
		});
        
        lContaDestino.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent it = new Intent(TransferenciaContas.this, ContaPesquisar.class);
				it.putExtra("MODO", "consulta");
				startActivityForResult(it, PESQUISA_CONTA_DESTINO);
			}
		});
        lContaOrigem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent it = new Intent(TransferenciaContas.this, ContaPesquisar.class);
				it.putExtra("MODO", "consulta");
				startActivityForResult(it, PESQUISA_CONTA_ORIGEM);
			}
		});
        lCategoria.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent it = new Intent(TransferenciaContas.this, CategoriaPesquisar.class);
				it.putExtra("MODO", "consulta");
				startActivityForResult(it, PESQUISA_CATEGORIA);	
			}
		});
        imgBtnContaOrigem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				contaOrigem = null;
				tvContaOrigem.setText("");
				imgBtnContaOrigem.setVisibility(View.GONE);
			}
		});
        imgBtnContaDestino.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				contaDestino = null;
				tvContaDestino.setText("");
				imgBtnContaDestino.setVisibility(View.GONE);
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
        
	}
	
	private void salvar() {
		Sistema sis = Sistema.getSistema();
		SQLiteDatabase db = sis.getConexaoBanco();
		Realizado realizadoOrigem = new Realizado(sis.getUsuarioLogado(), null, contaOrigem, categoria, 
				etDescricao.getText().toString(), dtTransferencia, 
				CustomDecimalUtils.toDouble(etValTransferencia.getText().toString(), etvalTransferenciaDec.getText().toString()),
				"P", null);
		Realizado realizadoDestino = new Realizado(sis.getUsuarioLogado(), null, contaDestino, categoria, 
				etDescricao.getText().toString(), dtTransferencia, 
				CustomDecimalUtils.toDouble(etValTransferencia.getText().toString(), etvalTransferenciaDec.getText().toString()),
				"R", null);
		
		try {
			db.beginTransaction();
			
			if (realizadoDestino.getConta() != null && realizadoOrigem.getConta() != null &&
				realizadoDestino.getConta().getId() == realizadoOrigem.getConta().getId()) {
				throw new Exception(Integer.toString(R.string.conta_origem_diferente_conta_destino));
			}
			
			long idRealizadoOrigem = RealizadoDAO.salvar(realizadoOrigem);
			long idRealizadoDestino = RealizadoDAO.salvar(realizadoDestino);
			
			if (idRealizadoOrigem <= 0 || idRealizadoDestino <= 0) {
				throw new Exception(Integer.toString(R.string.falha_transferencia_contas));
			}
			
			
			db.setTransactionSuccessful();
			setResult(RESULT_OK);
			finish();
		} catch (Exception e) {
			Toast.makeText(this, this.getResources().getString(Integer.parseInt(e.getMessage())), Toast.LENGTH_SHORT).show();
		} finally {
			db.endTransaction();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			   switch(requestCode) {
			      case (PESQUISA_CONTA_ORIGEM) : {
			    	  if (data.getLongExtra(Contas._ID, 0) != 0) {
			    		  contaOrigem = ContaDAO.buscarConta(data.getLongExtra(Contas._ID, 0));
			    		  if (contaOrigem != null) {
			    			  tvContaOrigem.setText(contaOrigem.getDescricao());
			    			  imgBtnContaOrigem.setVisibility(View.VISIBLE);
			    		  }
			    	  }
			    	  break;
			      }
			      case (PESQUISA_CONTA_DESTINO) : {
			    	  if (data.getLongExtra(Contas._ID, 0) != 0) {
			    		  contaDestino = ContaDAO.buscarConta(data.getLongExtra(Contas._ID, 0));
			    		  if (contaDestino != null) {
			    			  tvContaDestino.setText(contaDestino.getDescricao());
			    			  imgBtnContaDestino.setVisibility(View.VISIBLE);
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
	        case DLG_DATA_TRANSF:
	        	return new DatePickerDialog(this,
	        			mDateSetListenerTrans,
                        dtTransferencia.get(Calendar.YEAR), dtTransferencia.get(Calendar.MONTH), dtTransferencia.get(Calendar.DATE));
	    }
	    return null;
	}
	
	protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DLG_DATA_TRANSF:
            	((DatePickerDialog) dialog).updateDate(dtTransferencia.get(Calendar.YEAR), dtTransferencia.get(Calendar.MONTH), dtTransferencia.get(Calendar.DATE));
                break;
        }
    }
	
	private DatePickerDialog.OnDateSetListener mDateSetListenerTrans =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                        int dayOfMonth) {
                	dtTransferencia.set(year, monthOfYear, dayOfMonth);
                    updateDisplay();
                }
            };
	
	private void updateDisplay() {
		btnDtTransferencia.setText(DateFormat.format("EEEEE, dd/MM/yyyy", dtTransferencia));
	}
	
}
