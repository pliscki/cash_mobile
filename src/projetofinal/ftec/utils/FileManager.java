package projetofinal.ftec.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class FileManager{	
		
	public static String  carregarArquivoAsset(String arquivo, Context ctx) {
		String conteudo = "";
		try {					
			AssetManager am = ctx.getAssets();
			InputStream in = am.open(arquivo);
			if (true) {
				int tamanho = in.available();
				byte bytes[] = new byte[tamanho];
				in.read(bytes);
				conteudo = new String(bytes);				
			}		
			
		} catch(FileNotFoundException e) {			
			Log.e("DEBUG", "Arquivo não encontrado: " + e.getMessage(), e);
		} catch(IOException e) {
			Log.e("DEBUG", e.getMessage(), e);
		} finally {
			return conteudo;
		}
	}

}
