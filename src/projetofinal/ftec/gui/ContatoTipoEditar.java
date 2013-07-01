package projetofinal.ftec.gui;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.ContatoTipo;
import projetofinal.ftec.modelo.ContatoTipo.ContatoTipos;
import projetofinal.ftec.modelo.Sistema;
import projetofinal.ftec.persistencia.ContatoTipoDAO;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

public class ContatoTipoEditar extends Activity {
	private EditText etDescricao;	
	private Button btnSalvar;
	private Button btnCancelar;
	private Long id;
	private ContatoTipo contatoTipo;
	private Intent resultIntent;
	@Override
	protected void onCreate(Bundle icicle) {		 
		
		super.onCreate(icicle);		
		setContentView(R.layout.edit_container);
		setTitle(R.string.novo_contato_tipo);	
        
		initComps();		
			
		Bundle extras = getIntent().getExtras();
		
		id = null;
		
		if (extras != null) {
			id = extras.getLong(ContatoTipos._ID);
			if (id != null) {
				ContatoTipo contatoTipo = ContatoTipoDAO.buscarContatoTipo(id);
				if (contatoTipo != null) {
					etDescricao.setText(contatoTipo.getDescricao());
					setTitle(R.string.editar_contato_tipo);
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
        View viewConta = factory.inflate(R.layout.contato_tipo_editar, null);
        scrollView.addView(viewConta);
		
		etDescricao = (EditText) findViewById(R.id.contato_tipo_etDescricao);		
		btnCancelar = (Button) findViewById(R.id.btnCancelar);
		btnSalvar = (Button) findViewById(R.id.btnSalvar);		
	}
	
	public void salvar() {
		contatoTipo = new ContatoTipo();
		if (id != null) {
			contatoTipo.setId(id);
		}
		contatoTipo.setDescricao(etDescricao.getText().toString());			
		Sistema sis = Sistema.getSistema();
		if (sis != null && sis.getUsuarioLogado() != null) {
			contatoTipo.setUsuario(sis.getUsuarioLogado());
		}
		try {
			Long idConta = ContatoTipoDAO.salvar(contatoTipo);			
			resultIntent = new Intent();
			resultIntent.putExtra("id", idConta);
			setResult(RESULT_OK, resultIntent);				
			finish();
		} catch (Exception e) {
			Toast.makeText(this, this.getResources().getString(Integer.parseInt(e.getMessage())), Toast.LENGTH_SHORT).show();
		}	
		
	}
	
	

}
