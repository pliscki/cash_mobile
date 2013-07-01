package projetofinal.ftec.gui;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Contato;
import projetofinal.ftec.modelo.Contato.Contatos;
import projetofinal.ftec.persistencia.ContatoDAO;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ContatoView extends ViewContainer {
	
	private Contato contato;
	private TextView tvNome;
	private TextView tvSobrenome;
	private TextView tvTelefone;
	private TextView tvEmail;
	private TextView tvContatoTipo;
	private Long id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Bundle extras = getIntent().getExtras();
		
		id = null;
		
		if (extras != null) {
			id = extras.getLong(Contatos._ID);						
		}
		super.onCreate(savedInstanceState);
		
		setTitle(R.string.cadastro_contato);		
	}
	
	@Override
	public void initComps(LayoutInflater factory, ScrollView scrollView) {
		// TODO Auto-generated method stub
		super.initComps(factory, scrollView);
		
		View viewContato = factory.inflate(R.layout.contato_view, null);
        scrollView.addView(viewContato);
		
		tvNome = (TextView) findViewById(R.id.contato_view_tvNome);
		tvSobrenome = (TextView) findViewById(R.id.contato_view_tvSobrenome);
		tvContatoTipo = (TextView) findViewById(R.id.contato_view_tvContatotipo);
		tvTelefone = (TextView) findViewById(R.id.contato_view_tvTelefone);
		tvEmail = (TextView) findViewById(R.id.contato_view_tvEmail);
		
	}
	
	@Override
	public void atualizar() {
		// TODO Auto-generated method stub
		super.atualizar();
		if (id != null) {
			contato = ContatoDAO.buscarContato(id);
			if (contato != null) {
				tvNome.setText(contato.getNome());
				tvSobrenome.setText(contato.getSobrenome());
				tvContatoTipo.setText(contato.getContatoTipo().getDescricao());
				tvTelefone.setText(contato.getTelefone());
				tvEmail.setText(contato.getEmail());
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
		if (contato != null) {
			Intent it = new Intent(this, ContatoEditar.class);
			it.putExtra(Contatos._ID, contato.getId());
			
			startActivityForResult(it, 1);
		}
	}
	
	@Override
	public void promptDeletar(String mensagem, String titulo) {
		mensagem = getResources().getString(R.string.msg_excluir_contato);
		super.promptDeletar(mensagem, titulo);
	}
	
	@Override
	public void deletar() {
		int count;
		// TODO Auto-generated method stub
		super.deletar();
		if (contato != null) {
			try {
			count = ContatoDAO.deletar(contato.getId());
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


