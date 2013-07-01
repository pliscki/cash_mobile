package projetofinal.ftec.gui;


import projetofinal.ftec.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;


public class AdaptadorMenu extends BaseAdapter {
	private Context ctx;
	private final int[] botoes;
	private final int[] labels;
	private final int[] id_menu;
	private final android.view.ViewGroup.LayoutParams params;
	public AdaptadorMenu(Context c, int[] botoes, int[] labels, int[] id_menu,
			android.view.ViewGroup.LayoutParams params) {
		this.botoes = botoes;
		this.labels = labels;
		this.ctx	= c;
		this.params = params;
		this.id_menu = id_menu;
	}

	public int getCount() {
		return botoes.length;
	}

	public Object getItem(int posicao) {
		return posicao; 
	}

	public long getItemId(int posicao) {
		return posicao;
	}

	public View getView(int posicao, View convertView, ViewGroup parent) {
 		Button btn = new Button(ctx);		
		Resources res = ctx.getResources();		
		Drawable drb  = res.getDrawable(botoes[posicao]);
		drb.setBounds(0, 0, 90, 90);
		btn.setBackgroundColor(res.getColor(R.color.transparente));
		btn.setCompoundDrawables(null, drb, null, null);		
		btn.setLayoutParams(params);
		btn.setText(ctx.getResources().getString(labels[posicao]));
		btn.setTextSize(14);
		btn.setTextColor(res.getColor(R.color.branco));
		btn.setId(id_menu[posicao]);
		btn.setOnClickListener((OnClickListener) ctx);
		return btn;
	}

}
