package projetofinal.ftec.gui;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Previsto;
import projetofinal.ftec.modelo.Previsto.Previstos;
import projetofinal.ftec.persistencia.PrevistoDAO;
import projetofinal.ftec.utils.CustomDecimalUtils;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class PrevistoView extends ViewContainer {
	
	private Previsto previsto;
	private TextView tvConta;
	private TextView tvTipoMovimento;
	private TextView tvContato;
	private TextView tvValPrevisto;
	private TextView tvDtVencimento;
	private TextView tvDtEmissao;
 	private TextView tvDescricao;
	private TextView tvCategoria;
	private TextView tvPagamentoAuto;
	private TextView tvAlarme;
	private long id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Bundle extras = getIntent().getExtras();
		
		id = 0;
		
		if (extras != null) {
			id = extras.getLong(Previstos._ID);						
		}
		super.onCreate(savedInstanceState);
		
		setTitle(R.string.lancamento_previsto);		
	}
	
	@Override
	public void initComps(LayoutInflater factory, ScrollView scrollView) {
		// TODO Auto-generated method stub
		super.initComps(factory, scrollView);
		
		View viewRealizado = factory.inflate(R.layout.previsto_view, null);
        scrollView.addView(viewRealizado);
		
		tvCategoria = (TextView) findViewById(R.id.previsto_view_tvCategoria);
		tvConta = (TextView) findViewById(R.id.previsto_view_tvConta);
		tvContato = (TextView) findViewById(R.id.previsto_view_tvContato);
		tvDescricao = (TextView) findViewById(R.id.previsto_view_tvDescricao);
		tvDtEmissao = (TextView) findViewById(R.id.previsto_view_tvDtEmissao);
		tvDtVencimento = (TextView) findViewById(R.id.previsto_view_tvDtVencimento);
		tvTipoMovimento = (TextView) findViewById(R.id.previsto_view_tvTipoMovimento);
		tvValPrevisto = (TextView) findViewById(R.id.previsto_view_tvValPrevisto);
		tvPagamentoAuto = (TextView) findViewById(R.id.previsto_view_tvPagamentoAutomatico);
		tvAlarme = (TextView) findViewById(R.id.previsto_view_tvAlarme);
	}
	
	@Override
	public void atualizar() {
		// TODO Auto-generated method stub
		super.atualizar();
		if (id != 0) {
			previsto = PrevistoDAO.buscarPrevisto(id);
			if (previsto != null) {
				tvDescricao.setText(previsto.getDescricao());
				tvDtEmissao.setText(DateFormat.format("EEEE dd/MM/yyyy", previsto.getDt_emissao()));
				tvDtVencimento.setText(DateFormat.format("EEEE dd/MM/yyyy", previsto.getDt_vencimento()));
				
				tvValPrevisto.setText(CustomDecimalUtils.format(previsto.getVal_previsto()));
				if (previsto.getCategoria() != null) {
					tvCategoria.setText(previsto.getCategoria().getDescricao());
				}
				if (previsto.getConta() != null) {
					tvConta.setText(previsto.getConta().getDescricao());
				}
				if (previsto.getContato() != null) {
					tvContato.setText(previsto.getContato().getNome());
				}
				if (previsto.getAlarme() != null) {
					tvAlarme.setText(previsto.getAlarme().getDescricao());
				}
				if (previsto.getTipo_movimento().compareTo("P") == 0) {
					tvTipoMovimento.setText(R.string.pagar);
				} else {
					tvTipoMovimento.setText(R.string.receber);
				}
				if (previsto.getPagamento_automatico().compareTo("S") == 0) {
					tvPagamentoAuto.setText(R.string.sim);
				} else {
					tvPagamentoAuto.setText(R.string.nao);
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
		if (previsto != null) {
			Intent it = new Intent(this, PrevistoEditar.class);
			it.putExtra(Previstos._ID, previsto.getId());
			
			startActivityForResult(it, 1);
		}
	}
	
	@Override
	public void promptDeletar(String mensagem, String titulo) {
		mensagem = getResources().getString(R.string.msg_excluir_previsto);
		super.promptDeletar(mensagem, titulo);
	}
	
	@Override
	public void deletar() {
		int count;
		// TODO Auto-generated method stub
		super.deletar();
		if (previsto != null) {
			try {
			count = PrevistoDAO.deletar(previsto.getId());
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


