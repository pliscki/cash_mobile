package projetofinal.ftec.gui;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Previsto.Previstos;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class PrevistoPesqContainer extends TabActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		final TabHost tabHost = getTabHost();
		
		LayoutInflater factory = LayoutInflater.from(this);
        View tabReceber  = factory.inflate(R.layout.view_cabecalho_tab, null);
        View tabPagar  = factory.inflate(R.layout.view_cabecalho_tab, null);
        TextView tvReceber = (TextView) tabReceber.findViewById(R.id.tv_tab_indicator);
        TextView tvPagar = (TextView) tabPagar.findViewById(R.id.tv_tab_indicator);
        ImageView imgReceber = (ImageView) tabReceber.findViewById(R.id.img_tab_indicator);
        ImageView imgPagar = (ImageView) tabPagar.findViewById(R.id.img_tab_indicator);
        
        tvReceber.setText(R.string.receber);
        tvPagar.setText(R.string.pagar);
		
		Intent itReceber = new Intent(this, PrevistoPesquisar.class);
		itReceber.putExtra(Previstos.TIPO_MOVIMENTO, "R");
        tabHost.addTab(tabHost.newTabSpec("tab1")
                .setIndicator(tabReceber)
                .setContent(itReceber));

        Intent itPagar = new Intent(this, PrevistoPesquisar.class);
        itPagar.putExtra(Previstos.TIPO_MOVIMENTO, "P");
        tabHost.addTab(tabHost.newTabSpec("tab2")
        		.setIndicator(tabPagar)
                .setContent(itPagar));
        
        setTitle(R.string.consultar_previsto);
	}	

}
