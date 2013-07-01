package projetofinal.ftec.gui;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Realizado;
import projetofinal.ftec.modelo.Realizado.Realizados;
import projetofinal.ftec.persistencia.RealizadoDAO;
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

public class RealizadoView extends ViewContainer {
	
	private Realizado realizado;
	private TextView tvConta;
	private TextView tvTipoMovimento;
	private TextView tvContato;
	private TextView tvValMovimento;
	private TextView tvDtTransacao;
	private TextView tvDescricao;
	private TextView tvCategoria;
	private long id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Bundle extras = getIntent().getExtras();
		
		id = 0;
		
		if (extras != null) {
			id = extras.getLong(Realizados._ID);						
		}
		super.onCreate(savedInstanceState);
		
		setTitle(R.string.lancamento_realizado);		
	}
	
	@Override
	public void initComps(LayoutInflater factory, ScrollView scrollView) {
		// TODO Auto-generated method stub
		super.initComps(factory, scrollView);
		
		View viewRealizado = factory.inflate(R.layout.realizado_view, null);
        scrollView.addView(viewRealizado);
		
		tvCategoria = (TextView) findViewById(R.id.realizado_view_tvCategoria);
		tvConta = (TextView) findViewById(R.id.realizado_view_tvConta);
		tvContato = (TextView) findViewById(R.id.realizado_view_tvContato);
		tvDescricao = (TextView) findViewById(R.id.realizado_view_tvDescricao);
		tvDtTransacao = (TextView) findViewById(R.id.realizado_view_tvDtMovimento);
		tvTipoMovimento = (TextView) findViewById(R.id.realizado_view_tvTipoMovimento);
		tvValMovimento = (TextView) findViewById(R.id.realizado_view_tvValMovimento);
		
	}
	
	@Override
	public void atualizar() {
		// TODO Auto-generated method stub
		super.atualizar();
		if (id != 0) {
			realizado = RealizadoDAO.buscarRealizado(id);
			if (realizado != null) {
				tvDescricao.setText(realizado.getDescricao());
				tvDtTransacao.setText(DateFormat.format("EEEE dd/MM/yyyy", realizado.getDt_movimento()));
				tvValMovimento.setText(CustomDecimalUtils.format(realizado.getVal_movimento()));
				if (realizado.getCategoria() != null) {
					tvCategoria.setText(realizado.getCategoria().getDescricao());
				}
				if (realizado.getConta() != null) {
					tvConta.setText(realizado.getConta().getDescricao());
				}
				if (realizado.getContato() != null) {
					tvContato.setText(realizado.getContato().getNome());
				}
				if (realizado.getTipo_movimento().compareTo("P") == 0) {
					tvTipoMovimento.setText(R.string.pago);
				} else {
					tvTipoMovimento.setText(R.string.recebido);
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
		if (realizado != null) {
			Intent it = new Intent(this, RealizadoEditar.class);
			it.putExtra(Realizados._ID, realizado.getId());
			
			startActivityForResult(it, 1);
		}
	}
	
	@Override
	public void promptDeletar(String mensagem, String titulo) {
		mensagem = getResources().getString(R.string.msg_excluir_realizado);
		super.promptDeletar(mensagem, titulo);
	}
	
	@Override
	public void deletar() {
		int count;
		// TODO Auto-generated method stub
		super.deletar();
		if (realizado != null) {
			try {
			count = RealizadoDAO.deletar(realizado.getId());
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


