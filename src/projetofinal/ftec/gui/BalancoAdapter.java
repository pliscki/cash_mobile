package projetofinal.ftec.gui;

import projetofinal.ftec.R;
import projetofinal.ftec.utils.CustomDecimalUtils;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BalancoAdapter extends CursorAdapter{
	private Context context;
	private LayoutInflater layoutInflater;
	
	public BalancoAdapter(Context context, Cursor cursor) {
		super(context, cursor);
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View v = layoutInflater.inflate(R.layout.view_linha_relatorio_balanco, parent, false);
		return v;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView tvDescricao = (TextView) view.findViewById(R.id.rlt_balanco_tvDescricao);
		TextView tvValor = (TextView) view.findViewById(R.id.rlt_balanco_tvValor);
		LinearLayout lLinha = (LinearLayout) view.findViewById(R.id.rlt_balanco_linear);
		
		int nivel = cursor.getInt(cursor.getColumnIndexOrThrow(RelatorioBalanco.NIVEL));
		String descricao = cursor.getString(cursor.getColumnIndexOrThrow(RelatorioBalanco.DESCRICAO));
		String valor = CustomDecimalUtils.format(cursor.getDouble(cursor.getColumnIndexOrThrow(RelatorioBalanco.TOTAL)));
		
		
		
		switch (nivel) {
			case 1:
				lLinha.setBackgroundResource(R.color.azul_claro_1);
				tvDescricao.setTextSize(28);
				tvValor.setTextSize(28);
				tvDescricao.setText(R.string.saldo);
				break;
			case 2:
				lLinha.setBackgroundResource(R.color.azul_escuro_2);
				tvDescricao.setTextSize(25);
				tvValor.setTextSize(25);
				if (descricao.trim().compareTo("P") == 0) {
					tvDescricao.setText(R.string.despesas);
				} else {
					tvDescricao.setText(R.string.receitas);
				}
				break;
			case 3:
				tvDescricao.setTextColor(R.color.branco);
				tvValor.setTextColor(R.color.branco);
				lLinha.setBackgroundResource(R.color.azul_claro_3);
				tvDescricao.setTextSize(20);
				tvValor.setTextSize(20);
				tvDescricao.setText("  " + descricao);
				break;
			case 4:
				tvDescricao.setTextColor(R.color.branco);
				tvValor.setTextColor(R.color.branco);
				lLinha.setBackgroundResource(R.color.azul_claro_2);
				tvDescricao.setTextSize(15);
				tvValor.setTextSize(15);
				if (descricao.trim().compareTo("") == 0) {
					descricao = context.getResources().getString(R.string.nao_definido);
				}
				tvDescricao.setText("     " + descricao);
			break;
		}
		
		tvValor.setText(context.getResources().getString(R.string.sigla_moeda) + " " + valor);
		
	}
	
	@Override
	public boolean isEnabled(int position) {
		return false;
	}

	

}
