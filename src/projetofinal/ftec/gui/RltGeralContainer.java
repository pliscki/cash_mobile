package projetofinal.ftec.gui;

import projetofinal.ftec.R;
import projetofinal.ftec.modelo.Realizado.Realizados;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class RltGeralContainer extends TabActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		final TabHost tabHost = getTabHost();
		
		LayoutInflater factory = LayoutInflater.from(this);
        View tabRecebido  = factory.inflate(R.layout.view_cabecalho_tab, null);
        View tabPago  = factory.inflate(R.layout.view_cabecalho_tab, null);
        TextView tvRecebido = (TextView) tabRecebido.findViewById(R.id.tv_tab_indicator);
        TextView tvPago = (TextView) tabPago.findViewById(R.id.tv_tab_indicator);
        ImageView imgRecebido = (ImageView) tabRecebido.findViewById(R.id.img_tab_indicator);
        ImageView imgPago = (ImageView) tabPago.findViewById(R.id.img_tab_indicator);
        
        tvRecebido.setText(R.string.recebido);
        tvPago.setText(R.string.pago);
		
		Intent itReceber = new Intent(this, RelatorioGeral.class);
		itReceber.putExtra(Realizados.TIPO_MOVIMENTO, "R");
        tabHost.addTab(tabHost.newTabSpec("tab1")
                .setIndicator(tabRecebido)
                .setContent(itReceber));

        Intent itPagar = new Intent(this, RelatorioGeral.class);
        itPagar.putExtra(Realizados.TIPO_MOVIMENTO, "P");
        tabHost.addTab(tabHost.newTabSpec("tab2")
        		.setIndicator(tabPago)
                .setContent(itPagar));
        
        setTitle(R.string.relatorio_geral);
	}	

}
