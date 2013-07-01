package projetofinal.ftec.gui;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Alarme;
import projetofinal.ftec.modelo.Alarme.Alarmes;
import projetofinal.ftec.modelo.Sistema;
import projetofinal.ftec.persistencia.AlarmeDAO;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TimePicker;
import android.widget.Toast;


public class AlarmeEditar extends Activity {
	private EditText etDescricao;
	private TimePicker tpHoraInicial;
	private TimePicker tpHoraFinal;
	private EditText etIntervalo;
	private EditText etDiasAntesVencimento;
	private CheckBox cbAtivo;
	private Button btnSalvar;
	private Button btnCancelar;
	private Long id;
	private Alarme alarme;
	private Intent resultIntent;
	@Override
	protected void onCreate(Bundle icicle) {		 
		
		super.onCreate(icicle);		
		setContentView(R.layout.edit_container);
		setTitle(R.string.novo_alarme);	
        
		initComps();		
			
		Bundle extras = getIntent().getExtras();
		
		id = null;
		
		if (extras != null) {
			id = extras.getLong(Alarmes._ID);
			if (id != null) {
				Alarme alarme = AlarmeDAO.buscarAlarme(id);
				if (alarme != null) {
					etDescricao.setText(alarme.getDescricao());
					tpHoraInicial.setCurrentHour(alarme.getHora_inicial());
					tpHoraInicial.setCurrentMinute(alarme.getMin_inicial());
					tpHoraFinal.setCurrentHour(alarme.getHora_final());
					tpHoraFinal.setCurrentMinute(alarme.getMin_final());
					etIntervalo.setText(Integer.toString(alarme.getIntervalo()));
					etDiasAntesVencimento.setText(Integer.toString(alarme.getDias_antes_vencimento()));
					cbAtivo.setChecked(alarme.getAtivo().compareTo("S") == 0);					
					setTitle(R.string.editar_alarme);
				}
			}			
		}
		
		btnCancelar.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();				
			}
		});
		btnSalvar.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				salvar();		
				
			}			
			
		});
	}
	private void initComps() {
		ScrollView scrollView = (ScrollView) findViewById(R.id.edit_scroll);
		LayoutInflater factory = LayoutInflater.from(this);
        View viewAlarme = factory.inflate(R.layout.alarme_editar, null);
        scrollView.addView(viewAlarme);
		etDescricao = (EditText) findViewById(R.id.alarme_etDescricao);
		tpHoraInicial = (TimePicker) findViewById(R.id.alarme_tpHorainicial);
		tpHoraFinal = (TimePicker) findViewById(R.id.alarme_tpHorafinal);
		etIntervalo = (EditText) findViewById(R.id.alarme_etIntervalo);
		etDiasAntesVencimento = (EditText) findViewById(R.id.alarme_etDias_antes_vencimento);
		cbAtivo = (CheckBox) findViewById(R.id.alarme_cbAtivo);
		
		cbAtivo.setChecked(true);
		tpHoraInicial.setIs24HourView(true);
		tpHoraFinal.setIs24HourView(true);
		
		btnCancelar = (Button) findViewById(R.id.btnCancelar);
		btnSalvar = (Button) findViewById(R.id.btnSalvar);
		
	}
	
	public void salvar() {
		alarme = new Alarme();
		if (id != null) {
			alarme.setId(id);
		}
		
		alarme.setDescricao(etDescricao.getText().toString());
		alarme.setHora_inicial(tpHoraInicial.getCurrentHour());
		alarme.setMin_inicial(tpHoraInicial.getCurrentMinute());
		alarme.setHora_final(tpHoraFinal.getCurrentHour());
		alarme.setMin_final(tpHoraFinal.getCurrentMinute());
		
		
		if (etIntervalo.getText().toString().compareTo("") != 0) {
			alarme.setIntervalo(Integer.parseInt(etIntervalo.getText().toString()));
		}
		if (etDiasAntesVencimento.getText().toString().compareTo("") != 0) {
			alarme.setDias_antes_vencimento(Integer.parseInt(etDiasAntesVencimento.getText().toString()));
		}
		if (cbAtivo.isChecked()) {
			alarme.setAtivo("S");
		} else {
			alarme.setAtivo("N");
		}
		
		Sistema sis = Sistema.getSistema();
		if (sis != null && sis.getUsuarioLogado() != null) {
			alarme.setUsuario(sis.getUsuarioLogado());
		}
		try {
			Long idAlarme = AlarmeDAO.salvar(alarme);			
			resultIntent = new Intent();
			resultIntent.putExtra("id", idAlarme);
			setResult(RESULT_OK, resultIntent);				
			finish();
		} catch (Exception e) {
			
			Toast.makeText(this, this.getResources().getString(Integer.parseInt(e.getMessage())), Toast.LENGTH_SHORT).show();
		}	
		
	}		
	

}
