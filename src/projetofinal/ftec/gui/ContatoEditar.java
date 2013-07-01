package projetofinal.ftec.gui;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Contato;
import projetofinal.ftec.modelo.Contato.Contatos;
import projetofinal.ftec.modelo.ContatoTipo;
import projetofinal.ftec.modelo.ContatoTipo.ContatoTipos;
import projetofinal.ftec.modelo.Sistema;
import projetofinal.ftec.persistencia.ContatoDAO;
import projetofinal.ftec.persistencia.ContatoTipoDAO;
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
import android.widget.TextView;
import android.widget.Toast;

public class ContatoEditar extends Activity {
	private EditText etNome;
	private EditText etSobrenome;
	private TextView tvContatotipo;
	private EditText etTelefone;
	private EditText etEmail;
	private LinearLayout lContatoTipo;
	private ImageButton imgBtnContatoTipo;
	private Button btnSalvar;
	private Button btnCancelar;
	private Long id;
	private Contato contato;
	private ContatoTipo contatoTipo;
	private Intent resultIntent;
	private static final int PESQUISA_CONTATO_TIPO = 1;
	@Override
	protected void onCreate(Bundle icicle) {		 
		
		super.onCreate(icicle);		
		setContentView(R.layout.edit_container);
		setTitle(R.string.novo_contato);	
        
		initComps();		
			
		Bundle extras = getIntent().getExtras();
		
		id = null;
		
		if (extras != null) {
			id = extras.getLong(Contatos._ID);
			if (id != null) {
				Contato contato = ContatoDAO.buscarContato(id);
				if (contato != null) {
					etNome.setText(contato.getNome());
					etSobrenome.setText(contato.getSobrenome());
					if (contato.getContatoTipo() != null) {
						tvContatotipo.setText(contato.getContatoTipo().getDescricao());
						imgBtnContatoTipo.setVisibility(View.VISIBLE);
					}
					contatoTipo = contato.getContatoTipo();
					etTelefone.setText(contato.getTelefone());
					etEmail.setText(contato.getEmail());
					setTitle(R.string.editar_contato);
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
        View viewContato = factory.inflate(R.layout.contato_editar, null);
        scrollView.addView(viewContato);
		
		etNome = (EditText) findViewById(R.id.contato_etNome);
		etSobrenome = (EditText) findViewById(R.id.contato_etSobrenome);
		tvContatotipo = (TextView) findViewById(R.id.consulta_tvContatotipo);
		etTelefone = (EditText) findViewById(R.id.contato_etTelefone);
		etEmail = (EditText) findViewById(R.id.contato_etEmail);
		btnCancelar = (Button) findViewById(R.id.btnCancelar);
		btnSalvar = (Button) findViewById(R.id.btnSalvar);
		lContatoTipo = (LinearLayout) findViewById(R.id.consulta_linear_contatotipo);
		imgBtnContatoTipo = (ImageButton) findViewById(R.id.consulta_btn_contato_tipo_deselec);
		
		lContatoTipo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent it = new Intent(ContatoEditar.this, ContatoTipoPesquisar.class);
				it.putExtra("MODO", "consulta");			
				
				startActivityForResult(it, PESQUISA_CONTATO_TIPO);
				
			}
		});
		imgBtnContatoTipo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				contatoTipo = null;
				tvContatotipo.setText("");
				imgBtnContatoTipo.setVisibility(View.GONE);
			}
		});
	}
	
	public void salvar() {
		contato = new Contato();
		if (id != null) {
			contato.setId(id);
		}
		contato.setNome(etNome.getText().toString());
		contato.setSobrenome(etSobrenome.getText().toString());
		contato.setContatoTipo(contatoTipo);
		contato.setTelefone(etTelefone.getText().toString());
		contato.setEmail(etEmail.getText().toString());
		
		Sistema sis = Sistema.getSistema();
		if (sis != null && sis.getUsuarioLogado() != null) {
			contato.setUsuario(sis.getUsuarioLogado());
		}
		try {
			Long idContato = ContatoDAO.salvar(contato);			
			resultIntent = new Intent();
			resultIntent.putExtra("id", idContato);
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
		      case (PESQUISA_CONTATO_TIPO) : {
		    	  if (data.getLongExtra(ContatoTipos._ID, 0) != 0) {
		    		  contatoTipo = ContatoTipoDAO.buscarContatoTipo(data.getLongExtra(ContatoTipos._ID, 0));
		    		  if (contatoTipo != null) {
		    			  tvContatotipo.setText(contatoTipo.getDescricao());
		    			  imgBtnContatoTipo.setVisibility(View.VISIBLE);
		    		  }
		    	  }
		      }
		   }
		}
	}
	
	

}
