package projetofinal.ftec.gui;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Conta;
import projetofinal.ftec.modelo.Conta.Contas;
import projetofinal.ftec.persistencia.ContaDAO;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ContaView extends ViewContainer {
	
	private Conta conta;
	private TextView tvDescricao;
	private Long id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Bundle extras = getIntent().getExtras();
		
		id = null;
		
		if (extras != null) {
			id = extras.getLong(Contas._ID);						
		}
		super.onCreate(savedInstanceState);
		
		setTitle(R.string.cadastro_contas);		
	}
	
	@Override
	public void initComps(LayoutInflater factory, ScrollView scrollView) {
		// TODO Auto-generated method stub
		super.initComps(factory, scrollView);
		
		View viewConta = factory.inflate(R.layout.conta_view, null);
        scrollView.addView(viewConta);
		
		tvDescricao = (TextView) findViewById(R.id.conta_view_tvDescricao);		
		
	}
	
	@Override
	public void atualizar() {
		// TODO Auto-generated method stub
		super.atualizar();
		if (id != null) {
			conta = ContaDAO.buscarConta(id);
			if (conta != null) {
				tvDescricao.setText(conta.getDescricao());					
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
		if (conta != null) {
			Intent it = new Intent(this, ContaEditar.class);
			it.putExtra(Contas._ID, conta.getId());
			
			startActivityForResult(it, 1);
		}
	}
	
	@Override
	public void promptDeletar(String mensagem, String titulo) {
		mensagem = getResources().getString(R.string.msg_excluir_conta);
		super.promptDeletar(mensagem, titulo);
	}
	
	@Override
	public void deletar() {
		int count;
		// TODO Auto-generated method stub
		super.deletar();
		if (conta != null) {
			try {
			count = ContaDAO.deletar(conta.getId());
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


