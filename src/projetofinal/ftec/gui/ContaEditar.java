package projetofinal.ftec.gui;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Conta;
import projetofinal.ftec.modelo.Conta.Contas;
import projetofinal.ftec.modelo.Sistema;
import projetofinal.ftec.persistencia.ContaDAO;
import projetofinal.ftec.utils.CustomDecimalUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class ContaEditar extends Activity {
	private EditText etDescricao;	
	private Button btnSalvar;
	private Button btnCancelar;
	private LinearLayout lSaldoInicial;
	private ImageButton imgBtnMaisMenos;
	private EditText etSaldoInicial;
	private EditText EtSaldoInicialDec;
	private Long id;
	private Conta conta;
	private Intent resultIntent;
	private String tipo_movimento;
	@Override
	protected void onCreate(Bundle icicle) {		 
		
		super.onCreate(icicle);		
		setContentView(R.layout.edit_container);
		setTitle(R.string.nova_conta);	
        
		initComps();		
			
		Bundle extras = getIntent().getExtras();
		
		id = null;
		tipo_movimento = "R";
		
		if (extras != null) {
			id = extras.getLong(Contas._ID);
			if (id != null) {
				Conta conta = ContaDAO.buscarConta(id);
				if (conta != null) {
					etDescricao.setText(conta.getDescricao());
					lSaldoInicial.setVisibility(View.GONE);
					setTitle(R.string.editar_conta);
				}
			}			
		}
		
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
		
		imgBtnMaisMenos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (tipo_movimento.compareTo("R") == 0) {
					imgBtnMaisMenos.setImageDrawable(getResources().getDrawable(R.drawable.ic_btn_round_minus));
					tipo_movimento = "P";
				} else {
					imgBtnMaisMenos.setImageDrawable(getResources().getDrawable(R.drawable.ic_btn_round_plus));
					tipo_movimento = "R";
				}
			}
		});
	}
	private void initComps() {
		ScrollView scrollView = (ScrollView) findViewById(R.id.edit_scroll);
		LayoutInflater factory = LayoutInflater.from(this);
        View viewConta = factory.inflate(R.layout.conta_editar, null);
        scrollView.addView(viewConta);
		
		etDescricao = (EditText) findViewById(R.id.conta_etDescricao);
		etSaldoInicial = (EditText) findViewById(R.id.conta_etSaldoInicial);
		EtSaldoInicialDec = (EditText) findViewById(R.id.conta_etSaldoInicial_decimal);
		btnCancelar = (Button) findViewById(R.id.btnCancelar);
		btnSalvar = (Button) findViewById(R.id.btnSalvar);
		imgBtnMaisMenos = (ImageButton) findViewById(R.id.conta_btn_mais_menos);
		lSaldoInicial = (LinearLayout) findViewById(R.id.conta_linear_saldo_inicial);
		
	}
	
	public void salvar() {
		conta = new Conta();
		if (id != null) {
			conta.setId(id);
		}
		conta.setDescricao(etDescricao.getText().toString());			
		Sistema sis = Sistema.getSistema();
		if (sis != null && sis.getUsuarioLogado() != null) {
			conta.setUsuario(sis.getUsuarioLogado());
		}
		
		conta.setSaldoInicial(CustomDecimalUtils.toDouble(etSaldoInicial.getText().toString(), EtSaldoInicialDec.getText().toString()));
		conta.setTipoMovimentoInicial(tipo_movimento);
		conta.setCtx(this);
		try {
			Long idConta = ContaDAO.salvar(conta);
			resultIntent = new Intent();
			resultIntent.putExtra("id", idConta);
			setResult(RESULT_OK, resultIntent);				
			finish();
		} catch (Exception e) {
			Toast.makeText(this, this.getResources().getString(Integer.parseInt(e.getMessage())), Toast.LENGTH_SHORT).show();
		}	
		
	}
	
	

}
