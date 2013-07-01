package projetofinal.ftec.gui;

import java.text.ParseException;
import java.util.Calendar;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Contato.Contatos;
import projetofinal.ftec.modelo.Realizado.Realizados;
import projetofinal.ftec.utils.CustomDateUtils;
import projetofinal.ftec.utils.CustomDecimalUtils;
import android.content.Context;
import android.database.Cursor;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;


public class RealizadoAdapter extends CursorAdapter {
	private Context context;
	private LayoutInflater layoutInflater;
	
	public RealizadoAdapter(Context context, Cursor cursor) {
		super(context, cursor);
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View v = layoutInflater.inflate(R.layout.previsto_linha_consulta, parent, false);
		return v;
	}
	
	@Override
	public void bindView(View v, Context ctx, Cursor c) {
		
		TextView tvDescricao = (TextView) v.findViewById(R.id.previsto_consulta_tvDescricao);
		TextView tvValor = (TextView) v.findViewById(R.id.previsto_consulta_tvValor);
		TextView tvContato = (TextView) v.findViewById(R.id.previsto_consulta_tvContato);
		TextView tvVencimento = (TextView) v.findViewById(R.id.previsto_consulta_tvVencimento);
		
		
		tvDescricao.setText(c.getString(c.getColumnIndexOrThrow(Realizados.GET_DESCRICAO)));
		tvValor.setText(v.getContext().getString(R.string.sigla_moeda) + " " + 
					CustomDecimalUtils.format(c.getDouble(c.getColumnIndexOrThrow(Realizados.GET_VAL_MOVIMENTO))));
		tvContato.setText("");
		tvContato.setText(c.getString(c.getColumnIndexOrThrow(Contatos.GET_NOME)));
		
		Calendar datVencimento = Calendar.getInstance();
		
		try {
			datVencimento = CustomDateUtils.toCalendar(c.getString(c.getColumnIndexOrThrow(Realizados.GET_DT_MOVIMENTO)));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		tvVencimento.setText(v.getContext().getString(R.string.data) + ": " +
				DateFormat.format("dd/MM/yyyy", datVencimento.getTime())); 
		
			
	}
}
