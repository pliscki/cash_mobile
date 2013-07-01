package projetofinal.ftec.gui;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Sistema;
import projetofinal.ftec.modelo.Usuario.Usuarios;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;

public class MenuCadastros extends Activity implements OnClickListener{
	
	private Sistema sis;
	
	private static final int CONTA = 1;
	private static final int CATEGORIA = 2;
	private static final int USUARIO = 3;
	private static final int CONTATO_TIPO = 4;
	private static final int CONTATO = 5;
	private static final int ALARME = 6;
	
	private int[] botoes  = {R.drawable.ic_menu_conta_2, R.drawable.ic_menu_categoria, R.drawable.ic_menu_contato_tipo, R.drawable.ic_menu_contato, 
			R.drawable.ic_menu_alarme, R.drawable.ic_menu_usuario};
	private int[] labels  = {R.string.contas, R.string.categorias, R.string.contato_tipos,
			R.string.contatos, R.string.alarmes, R.string.usuario};
	private int[] id_menu = {CONTA, CATEGORIA, CONTATO_TIPO, CONTATO, ALARME, USUARIO};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.menu);	
		GridView grid = (GridView) findViewById(R.id.grd_menu);
		grid.setAdapter(new AdaptadorMenu(this, botoes, labels, id_menu, new GridView.LayoutParams(200,160)));
	
		sis = Sistema.getSistema();
	}
	
	@Override
	public void onClick(View v) {
		Intent it;
		switch (v.getId()) {
		
		case CONTA:
			it = new Intent(MenuCadastros.this, ContaPesquisar.class);
			startActivity(it);
			break;
		case CATEGORIA:
			it = new Intent(MenuCadastros.this, CategoriaPesquisar.class);
			startActivity(it);
			break;
		case USUARIO:
			it = new Intent(MenuCadastros.this, UsuarioView.class);
			it.putExtra(Usuarios._ID, sis.getUsuarioLogado().getId());
			startActivity(it);			
			break;	
		case CONTATO_TIPO:
			it = new Intent(MenuCadastros.this, ContatoTipoPesquisar.class);
			startActivity(it);			
			break;	
		case CONTATO:
			it = new Intent(MenuCadastros.this, ContatoPesquisar.class);
			startActivity(it);			
			break;
		case ALARME:
			it = new Intent(MenuCadastros.this, AlarmePesquisar.class);
			startActivity(it);			
			break;
		default:
			break;
		}
		
	}

}
