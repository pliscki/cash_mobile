package projetofinal.ftec.gui;

import projetofinal.ftec.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;

public class MenuRelatorios extends Activity implements OnClickListener{
	
	private static final int BALANCO = 1;
	private static final int GERAL = 2;

	private int[] botoes  = {R.drawable.ic_menu_balancete, R.drawable.ic_menu_relatorio};
	private int[] labels  = {R.string.balanco, R.string.relatorio_geral};
	private int[] id_menu = {BALANCO, GERAL};
	
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
		
		case BALANCO:
			it = new Intent(MenuRelatorios.this, RelatorioBalanco.class);
			startActivity(it);
			break;
		case GERAL:
			it = new Intent(MenuRelatorios.this, RltGeralContainer.class);
			startActivity(it);
			break;
		}
		
		
	}

}
