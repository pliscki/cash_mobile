package projetofinal.ftec.gui;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Alarme;
import projetofinal.ftec.modelo.Alarme.Alarmes;
import projetofinal.ftec.persistencia.AlarmeDAO;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmeView extends ViewContainer {
	
	private Alarme alarme;
	private TextView tvDescricao;
	private TextView tvPeriodoAtividade;
	private TextView tvIntervalo;
	private TextView tvDiasAntesVencimento;
	private TextView tvAtivo;
	private Long id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Bundle extras = getIntent().getExtras();
		
		id = null;
		
		if (extras != null) {
			id = extras.getLong(Alarmes._ID);						
		}
		super.onCreate(savedInstanceState);
		
		setTitle(R.string.cadastro_alarmes);		
	}
	
	@Override
	public void initComps(LayoutInflater factory, ScrollView scrollView) {
		// TODO Auto-generated method stub
		super.initComps(factory, scrollView);
		
		View viewAlarme = factory.inflate(R.layout.alarme_view, null);
        scrollView.addView(viewAlarme);
		
		tvDescricao = (TextView) findViewById(R.id.alarme_view_tvDescricao);
		tvPeriodoAtividade = (TextView) findViewById(R.id.alarme_view_tvPeriodoAtividade);
		tvIntervalo = (TextView) findViewById(R.id.alarme_view_tvIntervalo);
		tvDiasAntesVencimento = (TextView) findViewById(R.id.alarme_view_tvDias_antes_vencimento);
		tvAtivo = (TextView) findViewById(R.id.alarme_view_tvAtivo);
		
	}
	
	@Override
	public void atualizar() {
		// TODO Auto-generated method stub
		super.atualizar();
		if (id != null) {
			alarme = AlarmeDAO.buscarAlarme(id);
			if (alarme != null) {
				tvDescricao.setText(alarme.getDescricao());
				tvPeriodoAtividade.setText(String.format("%02d", alarme.getHora_inicial()) + ":" +
										   String.format("%02d", alarme.getMin_inicial()) + " às " +
										   String.format("%02d", alarme.getHora_final()) + ":" +
										   String.format("%02d", alarme.getMin_final()));
				tvIntervalo.setText(Integer.toString(alarme.getIntervalo()) + " " + getResources().getString(R.string.minutos));
				tvDiasAntesVencimento.setText(Integer.toString(alarme.getDias_antes_vencimento()) + " " + getResources().getString(R.string.dias));
				
				if (alarme.getAtivo().compareTo("S") == 0) {
					tvAtivo.setText(R.string.sim);
				} else {
					tvAtivo.setText(R.string.nao);
				}
			}
			else {
				finish();
			}
				
		}
	}
	
	@Override
	public void editar() {
		// TODO Auto-generated method stub
		super.editar();
		if (alarme != null) {
			Intent it = new Intent(this, AlarmeEditar.class);
			it.putExtra(Alarmes._ID, alarme.getId());
			
			startActivityForResult(it, 1);
		}
	}
	
	@Override
	public void promptDeletar(String mensagem, String titulo) {
		mensagem = getResources().getString(R.string.msg_excluir_alarme);
		super.promptDeletar(mensagem, titulo);
	}
	
	@Override
	public void deletar() {
		int count;
		// TODO Auto-generated method stub
		super.deletar();
		if (alarme != null) {
			try {
			count = AlarmeDAO.deletar(alarme.getId());
			} catch (SQLiteConstraintException e) {
				Toast.makeText(this, getResources().getString(Integer.parseInt(e.getMessage())), Toast.LENGTH_SHORT).show();
				return;
			}
			if ( count == 1 ) {
				finish();
			}
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			atualizar();
		}
	}

}


