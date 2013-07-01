package projetofinal.ftec.gui;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Categoria;
import projetofinal.ftec.modelo.Categoria.Categorias;
import projetofinal.ftec.persistencia.CategoriaDAO;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

public class CategoriaPesquisar extends PesquisaContainer {
	private Bundle extras;
	private Intent resultIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		extras = getIntent().getExtras();
		
		Cursor cursor = CategoriaDAO.getCursor();
		String[] colunas = new String[] {Categorias.DESCRICAO};
		int[] to = new int[] {android.R.id.text1};
		int layoutNativo = android.R.layout.simple_list_item_1;
		int contentView = R.layout.list_main;
		
		setCursor(cursor);
		setColunas(colunas);
		setTo(to);
		setLayoutNativo(layoutNativo);
		setLocalContentView(contentView);
		setHintEditPesquisa(R.string.pesquisar_categoria);
		
		super.onCreate(savedInstanceState);
		
		setTitle(R.string.consultar_categorias);
	}		
		
	@Override
	public void novo() {
		// TODO Auto-generated method stub
		super.novo();
		Intent it = new Intent(this, CategoriaEditar.class);
		startActivity(it);
	}
	
	@Override
	public void selecionaItem(Cursor c) {
		// TODO Auto-generated method stub
		super.selecionaItem(c);
		if (c != null) {
			if (extras != null && extras.containsKey("MODO") &&
				extras.getString("MODO").compareTo("consulta") == 0) {
				resultIntent = new Intent();
				
				resultIntent.putExtra(Categorias._ID, c.getLong(c.getColumnIndexOrThrow(Categorias._ID)));
				
				CategoriaPesquisar.this.setResult(RESULT_OK, resultIntent);
				CategoriaPesquisar.this.finish();
			} else {
				Intent it = new Intent(this, CategoriaView.class);
				it.putExtra(Categorias._ID, c.getLong(c.getColumnIndexOrThrow(Categorias._ID)));		
				
				startActivity(it);
			}
		}
		
	}
	
	@Override
	public void pesquisaItem(String texto) {
		// TODO Auto-generated method stub
		super.pesquisaItem(texto);
		if (getCursor() != null) {
			Cursor cursor = CategoriaDAO.findLike(Categoria.colunas, Categorias.DESCRICAO, texto, Categorias.DESCRICAO);
			setCursor(cursor);
			iniciarAdaptador(cursor);
		}
	}

}
