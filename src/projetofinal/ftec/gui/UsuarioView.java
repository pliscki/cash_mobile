package projetofinal.ftec.gui;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Usuario;
import projetofinal.ftec.modelo.Usuario.Usuarios;
import projetofinal.ftec.persistencia.UsuarioDAO;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class UsuarioView extends ViewContainer {
	
	private Usuario usuario;
	private TextView tvLogin;
	private TextView tvNomExibicao;
	private TextView tvLoginAutomatico;
	private TextView tvEmail;
	private Long id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setTitle(R.string.cadastro_usuario);
		
		Bundle extras = getIntent().getExtras();
		
		id = null;
		
		if (extras != null) {
			id = extras.getLong(Usuarios._ID);
		}
		super.onCreate(savedInstanceState);		
	}
	
	@Override
	public void initComps(LayoutInflater factory, ScrollView scrollView) {
		// TODO Auto-generated method stub
		super.initComps(factory, scrollView);
		
		View viewConta = factory.inflate(R.layout.usuario_view, null);
        scrollView.addView(viewConta);
		
		tvLogin 		  = (TextView) findViewById(R.id.usuario_view_tvLogin);	
		tvNomExibicao 	  = (TextView) findViewById(R.id.usuario_view_tvNome_exibicao);
		tvLoginAutomatico = (TextView) findViewById(R.id.usuario_view_tvLogin_automatico);		
		tvEmail 		  = (TextView) findViewById(R.id.usuario_view_tvEmail);
	}
	
	@Override
	public void atualizar() {
		// TODO Auto-generated method stub
		super.atualizar();
		if (id != null) {
			usuario = UsuarioDAO.buscarUsuario(id);
			if (usuario != null) {
				tvLogin.setText(usuario.getLogin());
				tvNomExibicao.setText(usuario.getNome_exibicao());
				tvEmail.setText(usuario.getEmail());
				if (usuario.getLogin_automatico().compareTo("S") == 0) {
					tvLoginAutomatico.setText(R.string.sim);
				} else {
					tvLoginAutomatico.setText(R.string.nao);
				}
			}
			else {
				finish();
			}				
		}
	}
	
	@Override
	public void editar() {
		// TODO Auto-generated method stub
		super.editar();
		if (usuario != null) {
			Intent it = new Intent(this, UsuarioEditar.class);
			it.putExtra(Usuarios._ID, usuario.getId());
			startActivityForResult(it, 1);
		}
	}
	
	@Override
	public void promptDeletar(String mensagem, String titulo) {
		mensagem = getResources().getString(R.string.msg_excluir_usuario);
		super.promptDeletar(mensagem, titulo);
	}
	
	@Override
	public void deletar() {
		int count;
		// TODO Auto-generated method stub
		super.deletar();
		if (usuario != null) {
			try {
			count = UsuarioDAO.deletar(usuario.getId());
			} catch (SQLiteConstraintException e) {
				Toast.makeText(this, getResources().getString(Integer.parseInt(e.getMessage())), Toast.LENGTH_SHORT).show();
				return;
			}
			if ( count == 1 ) {
				finish();
			}
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			atualizar();
		}
	}

}


