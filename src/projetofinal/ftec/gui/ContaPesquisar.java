package projetofinal.ftec.gui;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Conta;
import projetofinal.ftec.modelo.Conta.Contas;
import projetofinal.ftec.persistencia.ContaDAO;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;

public class ContaPesquisar extends PesquisaContainer {
	private Bundle extras;
	private Intent resultIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		extras = getIntent().getExtras();
		
		Cursor cursor = ContaDAO.getContaSaldo();
		
		setContentView(R.layout.list_main_simples);
		
		String[] colunas = new String[] {Contas.DESCRICAO, "saldo_conta"};
		int[] to = new int[] {android.R.id.text1, android.R.id.text2};
		int layoutNativo = android.R.layout.simple_list_item_2;
		int contentView = R.layout.list_main;
		
		setCursor(cursor);
		setColunas(colunas);
		setTo(to);
		setLayoutNativo(layoutNativo);
		setLocalContentView(contentView);
		setHintEditPesquisa(R.string.pesquisar_conta);
		
		super.onCreate(savedInstanceState);
		
		setTitle(R.string.consultar_contas);
	}
	
	@Override
	public void novo() {
		// TODO Auto-generated method stub
		super.novo();
		Intent it = new Intent(this, ContaEditar.class);
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
				
				resultIntent.putExtra(Contas._ID, c.getLong(c.getColumnIndexOrThrow(Contas._ID)));
				
				ContaPesquisar.this.setResult(RESULT_OK, resultIntent);
				ContaPesquisar.this.finish();
			} else {
				Intent it = new Intent(this, ContaView.class);
				it.putExtra(Contas._ID, c.getLong(c.getColumnIndexOrThrow(Contas._ID)));			
				
				startActivity(it);
			}
		}
		
	}
	
	@Override
	public void pesquisaItem(String texto) {
		// TODO Auto-generated method stub
		super.pesquisaItem(texto);
		Cursor cursor = ContaDAO.findLike(Conta.colunas, Contas.DESCRICAO, texto, Contas.DESCRICAO);
		setCursor(cursor);
		iniciarAdaptador(cursor);
	}


}
