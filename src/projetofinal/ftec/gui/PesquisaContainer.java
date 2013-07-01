package projetofinal.ftec.gui;

import projetofinal.ftec.R;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class PesquisaContainer extends ListActivity {
	private Cursor cursor;
	private String[] colunas;
	private int[] to;
	private int layoutNativo;
	private ListAdapter mAdapter;
	private int localContentView;
	private int hintEditPesquisa;
	protected static final int CRIAR = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(localContentView);
		
        
        ImageButton btnCriar = (ImageButton) findViewById(R.id.btn_criar_item);
		btnCriar.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				novo();				
			}
		});
		
		EditText etPesquisa = (EditText) findViewById(R.id.search_src_text);
		etPesquisa.setHint(hintEditPesquisa);
		etPesquisa.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				pesquisaItem(s.toString());
			}
		});
		
		iniciarAdaptador(cursor);
	}


	protected void onListItemClick(ListView l, View v, int position, long id) {

        //recupera o cursor na posição selecionada
        Cursor c = (Cursor) mAdapter.getItem(position);
        selecionaItem(c);                  
        
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, CRIAR, 0, R.string.criar).setIcon(android.R.drawable.ic_menu_add);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// Clicou no menu
		switch (item.getItemId()) {		
			case CRIAR:	
				novo();
				break;			
		}
		return true;
	}
	
	public void iniciarAdaptador(Cursor c) {
		Cursor oldCursor = getCursor();
		if (oldCursor != null) {
			stopManagingCursor(oldCursor);
		}
		if (c != null) {
			startManagingCursor(c);
			mAdapter = new SimpleCursorAdapter(this, layoutNativo, cursor, colunas, to);
			setListAdapter(mAdapter);
		}
	}
	
	
	public void pesquisaItem(String texto) {
		
	}
	
	public void selecionaItem(Cursor c) {
	
	}
	
	public void novo() {
		
	}
	
	public int getHintEditPesquisa() {
		return hintEditPesquisa;
	}


	public void setHintEditPesquisa(int hintEditPesquisa) {
		this.hintEditPesquisa = hintEditPesquisa;
	}
	
	public int getLocalContentView() {
		return localContentView;
	}

	public void setLocalContentView(int localContentView) {
		this.localContentView = localContentView;
	}

	public Cursor getCursor() {
		return cursor;
	}

	public void setCursor(Cursor cursor) {
		this.cursor = cursor;
	}

	public String[] getColunas() {
		return colunas;
	}

	public void setColunas(String[] colunas) {
		this.colunas = colunas;
	}

	public int[] getTo() {
		return to;
	}

	public void setTo(int[] to) {
		this.to = to;
	}

	public int getLayoutNativo() {
		return layoutNativo;
	}

	public void setLayoutNativo(int layoutNativo) {
		this.layoutNativo = layoutNativo;
	}
	
	
}
