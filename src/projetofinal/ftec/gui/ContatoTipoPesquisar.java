package projetofinal.ftec.gui;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.ContatoTipo;
import projetofinal.ftec.modelo.ContatoTipo.ContatoTipos;
import projetofinal.ftec.persistencia.ContatoTipoDAO;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

public class ContatoTipoPesquisar extends PesquisaContainer {
	private Bundle extras;
	private Intent resultIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		extras = getIntent().getExtras();		
		
		Cursor cursor = ContatoTipoDAO.getCursor();
		String[] colunas = new String[] {ContatoTipos.DESCRICAO};
		int[] to = new int[] {android.R.id.text1};
		int layoutNativo = android.R.layout.simple_list_item_1;
		int contentView = R.layout.list_main;
		
		setCursor(cursor);
		setColunas(colunas);
		setTo(to);
		setLayoutNativo(layoutNativo);
		setLocalContentView(contentView);
		setHintEditPesquisa(R.string.pesquisar_contato_tipo);
		
		super.onCreate(savedInstanceState);
		
		setTitle(R.string.consultar_contato_tipo);
	}
	
	@Override
	public void novo() {
		// TODO Auto-generated method stub
		super.novo();
		Intent it = new Intent(this, ContatoTipoEditar.class);
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
				
				resultIntent.putExtra(ContatoTipos._ID, c.getLong(c.getColumnIndexOrThrow(ContatoTipos._ID)));
				
				ContatoTipoPesquisar.this.setResult(RESULT_OK, resultIntent);
				ContatoTipoPesquisar.this.finish();
			} else {			
				Intent it = new Intent(this, ContatoTipoView.class);
				it.putExtra(ContatoTipos._ID, c.getLong(c.getColumnIndexOrThrow(ContatoTipos._ID)));			
				
				startActivity(it);
			}
		}
		
	}
	
	@Override
	public void pesquisaItem(String texto) {
		// TODO Auto-generated method stub
		super.pesquisaItem(texto);
		Cursor cursor = ContatoTipoDAO.findLike(ContatoTipo.colunas, ContatoTipos.DESCRICAO, texto, ContatoTipos.DESCRICAO);
		setCursor(cursor);
		iniciarAdaptador(cursor);
	}

}
