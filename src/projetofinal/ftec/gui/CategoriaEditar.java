package projetofinal.ftec.gui;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Categoria;
import projetofinal.ftec.modelo.Categoria.Categorias;
import projetofinal.ftec.modelo.Sistema;
import projetofinal.ftec.persistencia.CategoriaDAO;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

public class CategoriaEditar extends Activity {
	private EditText etDescricao;
	private CheckBox cbPagamentoAuto;
	private Button btnSalvar;
	private Button btnCancelar;
	private Long id;
	private Categoria categoria;
	private Intent resultIntent;
	@Override
	protected void onCreate(Bundle icicle) {		 
		
		super.onCreate(icicle);		
		setContentView(R.layout.edit_container);
		setTitle(R.string.nova_categoria);	
        
		initComps();		
			
		Bundle extras = getIntent().getExtras();
		
		id = null;
		
		if (extras != null) {
			id = extras.getLong(Categorias._ID);
			if (id != null) {
				Categoria categoria = CategoriaDAO.buscarCategoria(id);
				if (categoria != null) {
					etDescricao.setText(categoria.getDescricao());
					cbPagamentoAuto.setChecked(categoria.getPagamento_automatico().compareTo("S") == 0);
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
	}
	private void initComps() {
		ScrollView scrollView = (ScrollView) findViewById(R.id.edit_scroll);
		LayoutInflater factory = LayoutInflater.from(this);
        View viewCategoria = factory.inflate(R.layout.categoria_editar, null);
        scrollView.addView(viewCategoria);
		
		etDescricao = (EditText) findViewById(R.id.categoria_etDescricao);		
		btnCancelar = (Button) findViewById(R.id.btnCancelar);
		btnSalvar = (Button) findViewById(R.id.btnSalvar);
		cbPagamentoAuto = (CheckBox) findViewById(R.id.categoria_cbPagamentoAuto);
	}
	
	public void salvar() {
		categoria = new Categoria();
		if (id != null) {
			categoria.setId(id);
		}
		categoria.setDescricao(etDescricao.getText().toString());
		if (cbPagamentoAuto.isChecked()) {
			categoria.setPagamento_automatico("S");
		} else {
			categoria.setPagamento_automatico("N");
		}
		Sistema sis = Sistema.getSistema();
		if (sis != null && sis.getUsuarioLogado() != null) {
			categoria.setUsuario(sis.getUsuarioLogado());
		}
		try {
			Long idCategoria = CategoriaDAO.salvar(categoria);			
			resultIntent = new Intent();
			resultIntent.putExtra("id", idCategoria);
			setResult(RESULT_OK, resultIntent);				
			finish();
		} catch (Exception e) {
			Toast.makeText(this, this.getResources().getString(Integer.parseInt(e.getMessage())), Toast.LENGTH_SHORT).show();
		}	
		
	}
	
	

}
