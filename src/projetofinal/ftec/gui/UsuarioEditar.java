package projetofinal.ftec.gui;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Usuario;
import projetofinal.ftec.modelo.Usuario.Usuarios;
import projetofinal.ftec.persistencia.UsuarioDAO;
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

public class UsuarioEditar extends Activity {
	private EditText etLogin;
	private EditText etSenha;
	private EditText etNomExibicao;
	private EditText etSenhaRepetir;
	private CheckBox cbLoginAuto;
	private EditText etEmail;
	private Button btnSalvar;
	private Button btnCancelar;
	private Long id;
	private Usuario usuario;
	private Intent resultIntent;
	@Override
	protected void onCreate(Bundle icicle) {		 
		
		super.onCreate(icicle);		
		setContentView(R.layout.edit_container);
		setTitle(R.string.cadastrar_usuario);	
        
		initComps();		
			
		Bundle extras = getIntent().getExtras();
		
		id = null;
		
		if (extras != null) {
			id = extras.getLong(Usuarios._ID);
			if (id != null) {
				Usuario usuario = UsuarioDAO.buscarUsuario(id);
				if (usuario != null) {
					etLogin.setText(usuario.getLogin());
					etSenha.setText(usuario.getSenha());
					etNomExibicao.setText(usuario.getNome_exibicao());
					cbLoginAuto.setChecked(usuario.getLogin_automatico().compareTo("S") == 0);
					etEmail.setText(usuario.getEmail());
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
        View viewUsuario = factory.inflate(R.layout.usuario_editar, null);
        scrollView.addView(viewUsuario);
		
		etLogin = (EditText) findViewById(R.id.usuar_etLogin);
		etSenha = (EditText) findViewById(R.id.usuar_etSenha);
		etNomExibicao = (EditText) findViewById(R.id.usuar_etNomExibicao);
		etSenhaRepetir = (EditText) findViewById(R.id.usuar_etRepetirSenha);
		cbLoginAuto = (CheckBox) findViewById(R.id.usuar_cbLoginAuto);
		etEmail = (EditText) findViewById(R.id.usuar_etEmail);
		btnCancelar = (Button) findViewById(R.id.btnCancelar);
		btnSalvar = (Button) findViewById(R.id.btnSalvar);		
	}
	
	public void salvar() {
		usuario = new Usuario();
		if (id != null) {
			usuario.setId(id);
		}
		usuario.setLogin(etLogin.getText().toString());
		usuario.setSenha(etSenha.getText().toString());
		usuario.setSenhaRepetir(etSenhaRepetir.getText().toString());
		usuario.setNome_exibicao(etNomExibicao.getText().toString());
		usuario.setEmail(etEmail.getText().toString());
		if (cbLoginAuto.isChecked()) {
			usuario.setLogin_automatico("S");
		} else {
			usuario.setLogin_automatico("N");
		}		
		
		try {
			Long idUsuario = UsuarioDAO.salvar(usuario);			
			resultIntent = new Intent();
			resultIntent.putExtra(Usuarios._ID, idUsuario);
			setResult(RESULT_OK, resultIntent);				
			finish();
		} catch (Exception e) {
			Toast.makeText(this, this.getResources().getString(Integer.parseInt(e.getMessage())), Toast.LENGTH_SHORT).show();
		}	
		
	}
	
	

}
