package projetofinal.ftec.persistencia;

import projetofinal.ftec.utils.FileManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public final class DbHelper {
	private static final String NOME_BANCO = "ebolso";
	private static final int VERSAO_BANCO = 1;
	private static final String ARQUIVO_CREATE = "scriptDB.sql";
	private static final String ARQUIVO_CARGA = "cargaInicial.sql";
	private static SQLiteHelper dbHelper;
	private static SQLiteDatabase db;
	private static DbHelper dbH;
	
	private DbHelper(Context ctx) {
		String scriptCreate = FileManager.carregarArquivoAsset(ARQUIVO_CREATE, ctx);
		String scriptCarga = FileManager.carregarArquivoAsset(ARQUIVO_CARGA, ctx);
		dbHelper = new SQLiteHelper(ctx, NOME_BANCO, VERSAO_BANCO, scriptCreate, scriptCarga);
	}
	
	public static DbHelper getDbHelper(Context ctx) {
		if (dbH == null) {
			synchronized (DbHelper.class) {
				dbH = new DbHelper(ctx);				
			}
		}
		return dbH;
	}
	
	public SQLiteDatabase getDataBase(Context ctx) {
		if (db == null) {
			db = ctx.openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE, null);
			
			db = dbHelper.getWritableDatabase();
		}		
		return db;
	}	
	
	
	public void fechar() {
		if (dbHelper != null) {
			dbHelper.close();
		}
	}

}
