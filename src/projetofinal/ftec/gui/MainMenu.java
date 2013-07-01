package projetofinal.ftec.gui;


import java.util.List;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Sistema;
import projetofinal.ftec.modelo.Usuario;
import projetofinal.ftec.modelo.Usuario.Usuarios;
import projetofinal.ftec.persistencia.UsuarioDAO;
import projetofinal.ftec.services.MonitorContas;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;


public class MainMenu extends TabActivity {
	private static final int USUARIO_CADASTRO = 1;
	private static final int USUARIO_LOGIN = 2;
	private Bundle extras;
	
	private Sistema sis;
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);		
				
		sis = Sistema.getSistema();
		
		if (!sis.iniciarAplicacao(this)){
			this.encerrar();
		} 
		
		loginUsuario();
	}
	
	public void loginUsuario() {
		List<Usuario>  listaUsuario = UsuarioDAO.listarUsuario();	
		if (listaUsuario.size() == 0) {
			Intent it = new Intent(this, UsuarioEditar.class);			
			startActivityForResult(it, USUARIO_CADASTRO);						
		} else {			
			for (int i = 0; i < listaUsuario.size(); i++) {				
				if (listaUsuario.get(i).getLogin_automatico().compareTo("S") == 0) {
					sis.setUsuarioLogado(listaUsuario.get(i));
				}			
			}
			if (sis.getUsuarioLogado() == null) {
				// sempre o primeiro da lista
				Intent it = new Intent(this, DialogsAlerta.class);
				it.putExtra("TIPO_DIALOG", "login");
				it.putExtra(Usuarios.LOGIN, listaUsuario.get(0).getLogin());
				it.putExtra(Usuarios.SENHA, "");
				startActivityForResult(it, USUARIO_LOGIN);
			} else {
				iniciar();
			}
		}		
	}
	
	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {     
	   super.onActivityResult(requestCode, resultCode, data); 
	   switch(requestCode) { 
	      case (USUARIO_CADASTRO) : {	    	  
	         if (resultCode == Activity.RESULT_OK) {    	  
	    	    if (data.getLongExtra(Usuarios._ID, 0) != 0) {
	    		   sis.setUsuarioLogado(UsuarioDAO.buscarUsuario(data.getLongExtra(Usuarios._ID, 0)));	    		   
	    		   this.iniciar();
	    	    } 
	         } else {
	        	 MainMenu.this.encerrar();
	         }
	      break; 
	      } 
	      case (USUARIO_LOGIN) : {
	         if (resultCode == Activity.RESULT_OK) {	        	
	            sis.setUsuarioLogado(UsuarioDAO.buscarUsuarioPorLoginSenha(data.getStringExtra(Usuarios.LOGIN), data.getStringExtra(Usuarios.SENHA)));
	            if (sis.getUsuarioLogado() == null) {	            		            	
	            	loginUsuario();
	            } else {
	            	this.iniciar();
	            }
	    	 } else {
	    		 MainMenu.this.encerrar();
	    	 }
	      }
	   } 
	}
	
	public void encerrar() {
		this.finish();
	}
	
	public void iniciar() {
		sis.executarPagamentoAutomatico(this);
		
		String nomeExibicao;
		
		final TabHost tabHost = getTabHost();
		
		LayoutInflater factory = LayoutInflater.from(this);
        View tabPrincipal  = factory.inflate(R.layout.view_cabecalho_tab, null);
        View tabCadastros  = factory.inflate(R.layout.view_cabecalho_tab, null);
        View tabRelatorios = factory.inflate(R.layout.view_cabecalho_tab, null);
        TextView tvPrincipal = (TextView) tabPrincipal.findViewById(R.id.tv_tab_indicator);
        TextView tvCadastros = (TextView) tabCadastros.findViewById(R.id.tv_tab_indicator);
        TextView tvRelatorios = (TextView) tabRelatorios.findViewById(R.id.tv_tab_indicator);
        ImageView imgPrincipal = (ImageView) tabPrincipal.findViewById(R.id.img_tab_indicator);
        ImageView imgCadastros = (ImageView) tabCadastros.findViewById(R.id.img_tab_indicator);
        ImageView imgRelatorios = (ImageView) tabRelatorios.findViewById(R.id.img_tab_indicator);
        
        tvPrincipal.setText(R.string.principal);
        tvCadastros.setText(R.string.cadastros);
        tvRelatorios.setText(R.string.relatorios);

		Intent itMenuProcessos = new Intent(this, MenuProcessos.class);
        tabHost.addTab(tabHost.newTabSpec("tab1")
        		.setIndicator(tabPrincipal)
                .setContent(itMenuProcessos));

        Intent itMenuCadastros = new Intent(this, MenuCadastros.class);
        tabHost.addTab(tabHost.newTabSpec("tab2")
        		.setIndicator(tabCadastros)
                .setContent(itMenuCadastros));
        
        Intent itMenuRelat = new Intent(this, MenuRelatorios.class);
        tabHost.addTab(tabHost.newTabSpec("tab3")
        		.setIndicator(tabRelatorios)
                .setContent(itMenuRelat));
		
		 if (sis.getUsuarioLogado().getNome_exibicao().compareTo("") == 0) {
			 nomeExibicao = sis.getUsuarioLogado().getLogin();
		} else {
			nomeExibicao = sis.getUsuarioLogado().getNome_exibicao();
		}
		setTitle(this.getResources().getString(R.string.hello) + ", " + nomeExibicao);
		
		extras = getIntent().getExtras();
		if (extras != null &&
			extras.containsKey("ORIGEM")) {
			if (extras.getString("ORIGEM").compareTo(MonitorContas.MONITOR_CONTAS) == 0 ) {
				Intent it = new Intent(MainMenu.this, PrevistoPesquisar.class);
				
				it.putExtras(extras);
				startActivity(it);
			}
		}
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		sis.fechaConexaoBanco(this);
		System.exit(0);
	}
	
	/*
	public String getUsername(){
	    AccountManager manager = AccountManager.get(this); 
	    Account[] accounts = manager.getAccountsByType("com.google"); 
	    List<String> possibleEmails = new LinkedList<String>();

	    for (Account account : accounts) {
	      // TODO: Check possibleEmail against an email regex or treat
	      // account.name as an email address only for certain account.type values.
	      possibleEmails.add(account.name);
	    }

	    if(!possibleEmails.isEmpty() && possibleEmails.get(0) != null){
	        String email = possibleEmails.get(0);
	        String[] parts = email.split("@");
	        if(parts.length > 0 && parts[0] != null)
	            return parts[0];
	        else
	            return null;
	    }else
	        return null;
	} */

}
