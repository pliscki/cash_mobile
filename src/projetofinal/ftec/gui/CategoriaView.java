package projetofinal.ftec.gui;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Categoria;
import projetofinal.ftec.modelo.Categoria.Categorias;
import projetofinal.ftec.persistencia.CategoriaDAO;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class CategoriaView extends ViewContainer {
	
	private Categoria categoria;
	private TextView tvDescricao;
	private TextView tvPagAuto;
	private Long id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Bundle extras = getIntent().getExtras();
		
		id = null;
		
		if (extras != null) {
			id = extras.getLong(Categorias._ID);						
		}
		super.onCreate(savedInstanceState);
		
		setTitle(R.string.cadastro_categorias);		
	}
	
	@Override
	public void initComps(LayoutInflater factory, ScrollView scrollView) {
		// TODO Auto-generated method stub
		super.initComps(factory, scrollView);
		
		View viewCategoria = factory.inflate(R.layout.categoria_view, null);
        scrollView.addView(viewCategoria);
		
		tvDescricao = (TextView) findViewById(R.id.categoria_view_tvDescricao);		
		tvPagAuto = (TextView) findViewById(R.id.categoria_view_tvPag_auto);
	}
	
	@Override
	public void atualizar() {
		// TODO Auto-generated method stub
		super.atualizar();
		if (id != null) {
			categoria = CategoriaDAO.buscarCategoria(id);
			if (categoria != null) {
				tvDescricao.setText(categoria.getDescricao());
				if (categoria.getPagamento_automatico().compareTo("S") == 0) {
					tvPagAuto.setText(R.string.sim);
				} else {
					tvPagAuto.setText(R.string.nao);
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
		if (categoria != null) {
			Intent it = new Intent(this, CategoriaEditar.class);
			it.putExtra(Categorias._ID, categoria.getId());
			
			startActivityForResult(it, 1);
		}
	}
	
	@Override
	public void promptDeletar(String mensagem, String titulo) {
		mensagem = getResources().getString(R.string.msg_excluir_categoria);
		super.promptDeletar(mensagem, titulo);
	}
	
	@Override
	public void deletar() {
		int count;
		// TODO Auto-generated method stub
		super.deletar();
		if (categoria != null) {
			try {
			count = CategoriaDAO.deletar(categoria.getId());
			} catch (SQLiteConstraintException e) {
				Toast.makeText(this, getResources().getString(Integer.parseInt(e.getMessage())) , Toast.LENGTH_SHORT).show();
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


