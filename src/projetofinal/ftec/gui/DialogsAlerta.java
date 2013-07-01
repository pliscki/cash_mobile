package projetofinal.ftec.gui;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Usuario.Usuarios;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class DialogsAlerta extends Activity {
	private Bundle extras;
	private Intent resultIntent;
	private static final int DIALOG_LOGIN = 1;
	private static final int DIALOG_QUESTION = 2;
	
	@Override
    protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_LOGIN:
			 LayoutInflater factory = LayoutInflater.from(this);
             final View viewLogin = factory.inflate(R.layout.login_usuario, null);
             
			 final EditText etLogin = (EditText) viewLogin.findViewById(R.id.edit_etLogin);
			 final EditText etSenha = (EditText) viewLogin.findViewById(R.id.edit_etSenha);			
			
            
            if (extras.getString(Usuarios.LOGIN).compareTo("") != 0) {
            	etLogin.setText(extras.getString(Usuarios.LOGIN));
            }
            if (extras.getString(Usuarios.SENHA).compareTo("") != 0) {
            	etLogin.setText(extras.getString(Usuarios.SENHA));
            }
            return new AlertDialog.Builder(DialogsAlerta.this)
                .setTitle(R.string.usuario_login)
                .setView(viewLogin)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	resultIntent = new Intent();
                    	Log.e("DEBUG", etLogin.getText().toString());
                    	Log.e("DEBUG", etLogin.getText().toString());

						resultIntent.putExtra(Usuarios.LOGIN, etLogin.getText().toString());    						
						resultIntent.putExtra(Usuarios.SENHA, etSenha.getText().toString());
						
						DialogsAlerta.this.setResult(RESULT_OK, resultIntent);
						DialogsAlerta.this.finish();                   	
                    }
                })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	DialogsAlerta.this.setResult(RESULT_CANCELED);
                    	DialogsAlerta.this.finish();
                    }
                })
                .create();
		case DIALOG_QUESTION:
			String msg = new String (extras.getString("mensagem"));
			String msgTitulo = new String (extras.getString("titulo")); 
			 
			return new AlertDialog.Builder(DialogsAlerta.this)
            .setTitle(msgTitulo)
            .setMessage(msg)
            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
					DialogsAlerta.this.setResult(RESULT_OK, resultIntent);
					DialogsAlerta.this.finish();                   	
                }
            })
            .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	DialogsAlerta.this.setResult(RESULT_CANCELED);
                	DialogsAlerta.this.finish();
                }
            })
            .create();
		default:			
			return null;
		}
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		this.setResult(RESULT_CANCELED);
	}
	
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.menu);
		
		extras = getIntent().getExtras();		
		
		if (extras.getString("TIPO_DIALOG").compareTo("login") == 0 ) {
			showDialog(DIALOG_LOGIN);
		} else if (extras.getString("TIPO_DIALOG").compareTo("question") == 0) {
			showDialog(DIALOG_QUESTION);
		}
	}

}
