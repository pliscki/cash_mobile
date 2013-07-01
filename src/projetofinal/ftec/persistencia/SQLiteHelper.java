package projetofinal.ftec.persistencia;

import projetofinal.ftec.utils.FileManager;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {
	private String scriptSQLCreate;
	private String scriptCargaInicial;
	private Context ctx;

	public SQLiteHelper(Context context, String nomeBanco, int versaoBanco, String scriptSQLCreate,
			 String scriptCargaInicial) {
		super(context, nomeBanco, null, versaoBanco);
		this.scriptSQLCreate 	= scriptSQLCreate;
		this.scriptCargaInicial = scriptCargaInicial;
		this.ctx = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String[] script = scriptSQLCreate.split("\\;");
		try {		
			for (int i = 0;i<script.length;i++) {
				db.execSQL(script[i]);
			}
		} catch (SQLException e) {
			Log.e("DEBUG", e.getMessage());
		}	
		
		String[] scriptCarga = scriptCargaInicial.split("\\;");
		try {		
			for (int i = 0;i<scriptCarga.length;i++) {
				db.execSQL(scriptCarga[i]);
			}
		} catch (SQLException e) {
			Log.e("DEBUG", e.getMessage());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int versaoAntiga, int novaVersao) {
		String scriptUpdate;
		for (int iCta = versaoAntiga + 1;iCta<=novaVersao;iCta++) {
			scriptUpdate = FileManager.carregarArquivoAsset("scriptUpdate_v" + iCta + ".sql", ctx);
			String[] script = scriptUpdate.split("\\;");
			try {		
				for (int i = 0;i<script.length;i++) {
					db.execSQL(script[i] + ";");
				}
			} catch (SQLException e) {
				Log.e("DEBUG", e.getMessage());
			}
		}
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		super.onOpen(db);
		
		db.execSQL(" PRAGMA foreign_keys = ON ");
	}

}
