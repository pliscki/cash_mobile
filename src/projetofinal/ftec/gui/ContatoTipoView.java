package projetofinal.ftec.gui;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.ContatoTipo;
import projetofinal.ftec.modelo.ContatoTipo.ContatoTipos;
import projetofinal.ftec.persistencia.ContatoTipoDAO;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ContatoTipoView extends ViewContainer {
	
	private ContatoTipo contatoTipo;
	private TextView tvDescricao;
	private Long id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Bundle extras = getIntent().getExtras();
		
		id = null;
		
		if (extras != null) {
			id = extras.getLong(ContatoTipos._ID);						
		}
		super.onCreate(savedInstanceState);
		
		setTitle(R.string.cadastro_contato_tipo);		
	}
	
	@Override
	public void initComps(LayoutInflater factory, ScrollView scrollView) {
		// TODO Auto-generated method stub
		super.initComps(factory, scrollView);
		
		View viewContatoTipo = factory.inflate(R.layout.contato_tipo_view, null);
        scrollView.addView(viewContatoTipo);
		
		tvDescricao = (TextView) findViewById(R.id.contato_tipo_view_tvDescricao);		
		
	}
	
	@Override
	public void atualizar() {
		// TODO Auto-generated method stub
		super.atualizar();
		if (id != null) {
			contatoTipo = ContatoTipoDAO.buscarContatoTipo(id);
			if (contatoTipo != null) {
				tvDescricao.setText(contatoTipo.getDescricao());
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
		if (contatoTipo != null) {
			Intent it = new Intent(this, ContatoTipoEditar.class);
			it.putExtra(ContatoTipos._ID, contatoTipo.getId());
			
			startActivityForResult(it, 1);
		}
	}
	
	@Override
	public void promptDeletar(String mensagem, String titulo) {
		mensagem = getResources().getString(R.string.msg_excluir_contato_tipo);
		super.promptDeletar(mensagem, titulo);
	}
	
	@Override
	public void deletar() {
		int count;
		// TODO Auto-generated method stub
		super.deletar();
		if (contatoTipo != null) {
			try {
			count = ContatoTipoDAO.deletar(contatoTipo.getId());
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


