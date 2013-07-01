package projetofinal.ftec.modelo;

import projetofinal.ftec.R;
import projetofinal.ftec.persistencia.AlarmeDAO;
import projetofinal.ftec.persistencia.UsuarioDAO;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class Alarme {
	public static String[] colunas = new String[] { Alarmes._ID, Alarmes.DESCRICAO, Alarmes.HORA_INICIAL, Alarmes.MIN_INICIAL, 
		Alarmes.HORA_FINAL, Alarmes.MIN_FINAL,	Alarmes.INTERVALO, Alarmes.ATIVO, Alarmes.USUARIO_ID, Alarmes.DIAS_ANTES_VENCIMENTO };	
	public static final String AUTHORITY = "projetofinal.ftec.modelo.provider.alarme";
	private long id;
	private String descricao;
	private int hora_inicial;
	private int min_inicial;
	private int hora_final;
	private int min_final;
	private int intervalo;
	private int dias_antes_vencimento;
	private String ativo;
	private Usuario usuario;
	
	public Alarme () {
	}
	
	public Alarme (String descricao, int hora_inicial, int min_inicial, int hora_final, int min_final, 
			int intervalo, String ativo, Usuario usuario, int dias_antes_vencimento) {
		super();
		this.descricao = descricao;
		this.hora_inicial = hora_inicial;
		this.min_inicial = min_inicial;
		this.hora_final = hora_final;
		this.min_final = min_final;
		this.intervalo = intervalo;
		this.ativo = ativo;
		this.usuario = usuario;
		this.dias_antes_vencimento = dias_antes_vencimento;
	}
	
	public Alarme (Long id, String descricao, int hora_inicial, int min_inicial, int hora_final, int min_final, 
			int intervalo, String ativo, Usuario usuario, int dias_antes_vencimento) {
		super();
		this.id = id;
		this.descricao = descricao;
		this.hora_inicial = hora_inicial;
		this.min_inicial = min_inicial;
		this.hora_final = hora_final;
		this.min_final = min_final;
		this.intervalo = intervalo;
		this.ativo = ativo;
		this.usuario = usuario;
		this.dias_antes_vencimento = dias_antes_vencimento;
	}
	
	public static final class Alarmes implements BaseColumns {
		private Alarmes() {			
		}
		
		public static final Uri CONTENT_URI = Uri.parse("content:// " + AUTHORITY + "/alarmes");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.alarmes";
		public static final String DEFAULT_SORT_ORDER = "alarme._id ASC";
		
		public static final String DESCRICAO = "descricao";
		
		public static final String HORA_INICIAL = "hora_inicial";
		public static final String MIN_INICIAL = "min_inicial";
		public static final String HORA_FINAL = "hora_final";
		public static final String MIN_FINAL = "min_final";
		public static final String INTERVALO = "intervalo";
		public static final String ATIVO = "ativo";
		public static final String USUARIO_ID = "usuario_id";
		public static final String DIAS_ANTES_VENCIMENTO = "dias_antes_vencimento";
		
		// Nomes utilizados nos selects
		public static final String ALARME_ID = AlarmeDAO.getNomeTabela() + "._id";
		public static final String ALARME_DESCRICAO = AlarmeDAO.getNomeTabela() + ".descricao";
		public static final String ALARME_HORA_INICIAL = AlarmeDAO.getNomeTabela() + ".hora_inicial";
		public static final String ALARME_MIN_INICIAL = AlarmeDAO.getNomeTabela() + ".min_inicial";
		public static final String ALARME_HORA_FINAL = AlarmeDAO.getNomeTabela() + ".hora_final";
		public static final String ALARME_MIN_FINAL = AlarmeDAO.getNomeTabela() + ".min_final";
		public static final String ALARME_INTERVALO = AlarmeDAO.getNomeTabela() + ".intervalo";
		public static final String ALARME_ATIVO = AlarmeDAO.getNomeTabela() + ".ativo";
		public static final String ALARME_USUARIO_ID = AlarmeDAO.getNomeTabela() + ".usuario_id";
		public static final String ALARME_DIAS_ANTES_VENCIMENTO = AlarmeDAO.getNomeTabela() +  ".dias_antes_vencimento";
		
		public static final String GET_ID = AlarmeDAO.getNomeTabela() + "_id";
		public static final String GET_DESCRICAO = AlarmeDAO.getNomeTabela() + "_descricao";
		public static final String GET_HORA_INICIAL = AlarmeDAO.getNomeTabela() + "_hora_inicial";
		public static final String GET_MIN_INICIAL = AlarmeDAO.getNomeTabela() + "_min_inicial";
		public static final String GET_HORA_FINAL = AlarmeDAO.getNomeTabela() + "_hora_final";
		public static final String GET_MIN_FINAL = AlarmeDAO.getNomeTabela() + "_min_final";
		public static final String GET_INTERVALO = AlarmeDAO.getNomeTabela() + "_intervalo";
		public static final String GET_ATIVO = AlarmeDAO.getNomeTabela() + "_ativo";
		public static final String GET_USUARIO_ID = AlarmeDAO.getNomeTabela() + "_usuario_id";
		public static final String GET_ANTES_VENCIMENTO = AlarmeDAO.getNomeTabela() + "_dias_antes_vencimento";
		
		public static Uri getUriId(long id) {
			Uri uriAlarme = ContentUris.withAppendedId(Alarmes.CONTENT_URI, id);
			return uriAlarme;
		}
		
	}
	
	public String toString() {
		return "Alarme: " + descricao;
	}
	
	public boolean check() throws Exception {
		
		if (this.descricao.compareTo("") == 0) {
			throw new Exception(Integer.toString(R.string.descricao_obrig));
		}		
		
		if (this.usuario == null) {
			throw new Exception(Integer.toString(R.string.usuario_obrig));
		} else if (UsuarioDAO.buscarUsuario(this.usuario.getId()) == null) {
			throw new Exception(Integer.toString(R.string.usuario_invalido));
		}
		
		if ((this.hora_inicial + this.min_inicial) >= (this.hora_final + this.min_final)) {
			throw new Exception(Integer.toString(R.string.hora_inicial_menor_final));
		}	
		
		if (this.intervalo <= 0) {
			throw new Exception(Integer.toString(R.string.intervalo_invalido));
		}
		
		return true;
	}
	
	public void trim() {
		this.descricao = this.descricao.trim();
		this.ativo = this.ativo.trim();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getHora_inicial() {
		return hora_inicial;
	}

	public void setHora_inicial(int hora_inicial) {
		this.hora_inicial = hora_inicial;
	}	
	
	public int getMin_inicial() {
		return min_inicial;
	}

	public void setMin_inicial(int min_inicial) {
		this.min_inicial = min_inicial;
	}
	

	public int getHora_final() {
		return hora_final;
	}

	public void setHora_final(int hora_final) {
		this.hora_final = hora_final;
	}

	public int getMin_final() {
		return min_final;
	}

	public void setMin_final(int min_final) {
		this.min_final = min_final;
	}

	public int getIntervalo() {
		return intervalo;
	}

	public void setIntervalo(int intervalo) {
		this.intervalo = intervalo;
	}

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public int getDias_antes_vencimento() {
		return dias_antes_vencimento;
	}

	public void setDias_antes_vencimento(int dias_antes_vencimento) {
		this.dias_antes_vencimento = dias_antes_vencimento;
	}
	
	
}
