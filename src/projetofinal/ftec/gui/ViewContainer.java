package projetofinal.ftec.gui;

import projetofinal.ftec.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;

public class ViewContainer extends Activity {
	protected static final int EDITAR = 1;
	protected static final int DELETAR = 2;
	private static final int CONFIRMA_EXCLUSAO = 999;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.view_container);
		ScrollView scrollView = (ScrollView) findViewById(R.id.view_scroll);
		LayoutInflater factory = LayoutInflater.from(this);
		initComps(factory, scrollView);
		atualizar();
	}
	
	public void initComps(LayoutInflater factory, ScrollView scrollView) {
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, EDITAR, 0, R.string.editar).setIcon(android.R.drawable.ic_menu_edit);
		menu.add(0, DELETAR, 0, R.string.deletar).setIcon(android.R.drawable.ic_menu_delete);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// Clicou no menu
		switch (item.getItemId()) {		
			case EDITAR:	
				editar();
				break;
			case DELETAR:
				promptDeletar("", "");
				break;
		}
		return true;
	}

	public void promptDeletar(String mensagem, String titulo) {
		// TODO Auto-generated method stub
		if (mensagem.compareTo("") == 0) {
			mensagem = getResources().getString(R.string.msg_excluir_padrao);
		}
		if (titulo.compareTo("") == 0) {
			titulo = getResources().getString(R.string.excluir);
		}
		Intent it = new Intent(this, DialogsAlerta.class);
		it.putExtra("TIPO_DIALOG", "question");
		it.putExtra("mensagem", mensagem);
		it.putExtra("titulo", titulo);
		startActivityForResult(it, CONFIRMA_EXCLUSAO);
	}

	public void deletar() {
		// TODO Auto-generated method stub
		
	}

	public void editar() {
		// TODO Auto-generated method stub
		
	}
	
	public void atualizar() {
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {     
		super.onActivityResult(requestCode, resultCode, data); 
		switch(requestCode) { 
			case (CONFIRMA_EXCLUSAO) : {
				if (resultCode == Activity.RESULT_OK) {
				   deletar();
		    	}
		        break; 
		    }
		      
	    } 
	}	

}
