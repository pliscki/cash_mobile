package projetofinal.ftec.gui;

import projetofinal.ftec.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;

public class MenuProcessos extends Activity implements OnClickListener{
	
	private static final int PREVISTO = 1;
	private static final int REALIZADO = 2;
	private static final int TRANSFERENCIA = 3;
	
	private int[] botoes  = {R.drawable.ic_menu_previsto, R.drawable.ic_menu_realizado_2, R.drawable.ic_menu_transferencia};
	private int[] labels  = {R.string.previstos, R.string.realizados, R.string.transferencias};
	private int[] id_menu = {PREVISTO, REALIZADO, TRANSFERENCIA};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.menu);	
		GridView grid = (GridView) findViewById(R.id.grd_menu);
		grid.setAdapter(new AdaptadorMenu(this, botoes, labels, id_menu, new GridView.LayoutParams(200,160)));
		
	}
	
	@Override
	public void onClick(View v) {
		Intent it;
		
		switch (v.getId()) {			
		case PREVISTO:
			it = new Intent(MenuProcessos.this, PrevistoPesqContainer.class);
			startActivity(it);			
			break;
		case REALIZADO:
			it = new Intent(MenuProcessos.this, RealizadoPesqContainer.class);
			startActivity(it);
			break;
		case TRANSFERENCIA:
			it = new Intent(MenuProcessos.this, TransferenciaContas.class);
			startActivity(it);
			break;
		default:
			break;
		}
		
	}

}
