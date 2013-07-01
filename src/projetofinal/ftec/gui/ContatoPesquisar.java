package projetofinal.ftec.gui;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Contato;
import projetofinal.ftec.modelo.Contato.Contatos;
import projetofinal.ftec.persistencia.ContatoDAO;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

public class ContatoPesquisar extends PesquisaContainer {
	private Bundle extras;
	private Intent resultIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		extras = getIntent().getExtras();
		
		Cursor cursor = ContatoDAO.getCursor();
		String[] colunas = new String[] {Contatos.NOME};
		int[] to = new int[] {android.R.id.text1};
		int layoutNativo = android.R.layout.simple_list_item_1;
		int contentView = R.layout.list_main;
		
		setCursor(cursor);
		setColunas(colunas);
		setTo(to);
		setLayoutNativo(layoutNativo);
		setLocalContentView(contentView);
		setHintEditPesquisa(R.string.pesquisar_contato);
		
		super.onCreate(savedInstanceState);
		
		
		
		setTitle(R.string.consultar_contatos);
	}
	
	@Override
	public void novo() {
		// TODO Auto-generated method stub
		super.novo();
		Intent it = new Intent(this, ContatoEditar.class);
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
				
				resultIntent.putExtra(Contatos._ID, c.getLong(c.getColumnIndexOrThrow(Contatos._ID)));
				
				ContatoPesquisar.this.setResult(RESULT_OK, resultIntent);
				ContatoPesquisar.this.finish();
			} else {
				Intent it = new Intent(this, ContatoView.class);
				it.putExtra(Contatos._ID, c.getLong(c.getColumnIndexOrThrow(Contatos._ID)));			
				
				startActivity(it);
			}
		}
		
	}
	
	@Override
	public void pesquisaItem(String texto) {
		// TODO Auto-generated method stub
		super.pesquisaItem(texto);
		Cursor cursor = ContatoDAO.findLike(Contato.colunas, Contatos.NOME, texto, Contatos.NOME);
		setCursor(cursor);
		iniciarAdaptador(cursor);
	}

}
