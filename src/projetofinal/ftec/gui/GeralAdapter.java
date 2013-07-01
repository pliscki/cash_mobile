package projetofinal.ftec.gui;

import projetofinal.ftec.R;
import projetofinal.ftec.utils.CustomDecimalUtils;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GeralAdapter extends CursorAdapter{
	private Context context;
	private LayoutInflater layoutInflater;
	private Double totalGeral;
	
	public GeralAdapter(Context context, Cursor cursor) {
		super(context, cursor);
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View v = layoutInflater.inflate(R.layout.view_linha_relatorio_geral, parent, false);
		return v;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView tvDescricao = (TextView) view.findViewById(R.id.rlt_geral_tvDescricao);
		TextView tvPorcentagem = (TextView) view.findViewById(R.id.rlt_geral_tvPorcentagem);
		ProgressBar barraProgresso = (ProgressBar) view.findViewById(R.id.rlt_geral_barra);
		
		
		tvDescricao.setText(cursor.getString(cursor.getColumnIndexOrThrow(RelatorioGeral.DESCRICAO)) + " " +
							view.getResources().getString(R.string.sigla_moeda) + " " +
							CustomDecimalUtils.format(cursor.getDouble(cursor.getColumnIndexOrThrow(RelatorioGeral.TOTAL_DESCRICAO))));
		Double percentual = 0.0;
		try {
			percentual = cursor.getDouble(cursor.getColumnIndexOrThrow(RelatorioGeral.TOTAL_DESCRICAO)) / totalGeral * 100;
		} catch (ArithmeticException e) {
		}
		
		tvPorcentagem.setText(CustomDecimalUtils.format(CustomDecimalUtils.FORMATO_PERCENT, percentual) + " %");
		barraProgresso.setMax(100);
		barraProgresso.setProgress(Integer.parseInt(CustomDecimalUtils.getIntPart(percentual)));
	}
	
	@Override
	public boolean isEnabled(int position) {
		return false;
	}

	public Double getTotalGeral() {
		return totalGeral;
	}

	public void setTotalGeral(Double totalGeral) {
		this.totalGeral = totalGeral;
	}

	

}
