package projetofinal.ftec.gui;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Alarme;
import projetofinal.ftec.modelo.Alarme.Alarmes;
import projetofinal.ftec.persistencia.AlarmeDAO;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

public class AlarmePesquisar extends PesquisaContainer {
	private Bundle extras;
	private Intent resultIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		extras = getIntent().getExtras();
		
		Cursor cursor = AlarmeDAO.getCursor();
		String[] colunas = new String[] {Alarmes.DESCRICAO};
		int[] to = new int[] {android.R.id.text1};
		int layoutNativo = android.R.layout.simple_list_item_1;
		int contentView = R.layout.list_main;
		
		setCursor(cursor);
		setColunas(colunas);
		setTo(to);
		setLayoutNativo(layoutNativo);
		setLocalContentView(contentView);
		setHintEditPesquisa(R.string.pesquisar_alarme);
		
		super.onCreate(savedInstanceState);
		
		
		
		setTitle(R.string.consultar_alarmes);
	}
	
	@Override
	public void novo() {
		// TODO Auto-generated method stub
		super.novo();
		Intent it = new Intent(this, AlarmeEditar.class);
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
				
				resultIntent.putExtra(Alarmes._ID, c.getLong(c.getColumnIndexOrThrow(Alarmes._ID)));
				
				AlarmePesquisar.this.setResult(RESULT_OK, resultIntent);
				AlarmePesquisar.this.finish();
			} else {
				Intent it = new Intent(this, AlarmeView.class);
				it.putExtra(Alarmes._ID, c.getLong(c.getColumnIndexOrThrow(Alarmes._ID)));			
				
				startActivity(it);
			}
		}
		
	}
	
	@Override
	public void pesquisaItem(String texto) {
		// TODO Auto-generated method stub
		super.pesquisaItem(texto);
		Cursor cursor = AlarmeDAO.findLike(Alarme.colunas, Alarmes.DESCRICAO, texto, Alarmes.DESCRICAO);
		setCursor(cursor);
		iniciarAdaptador(cursor);
	}

}
