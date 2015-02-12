package objetos;

public class Usuario {
	
	private int id;
	private String alias;
	private String pass;
	private String email;
	private String direccion;
	private int cp;
	private int poblacion;
	private String nombre;
	private String apellidos;
	
	public Usuario(){
	}

	public Usuario(int id, String alias, String pass, String email, String direccion,
			int cp, int poblacion, String nombre, String apellidos) {
		this.id = id;
		this.alias = alias;
		this.pass = pass;
		this.email = email;
		this.direccion = direccion;
		this.cp = cp;
		this.poblacion = poblacion;
		this.nombre = nombre;
		this.apellidos = apellidos;
	}
	
	public int getId() {
		return id;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public int getCp() {
		return cp;
	}

	public void setCp(int cp) {
		this.cp = cp;
	}

	public int getPoblacion() {
		return poblacion;
	}

	public void setPoblacion(int poblacion) {
		this.poblacion = poblacion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getAlias() {
		return alias;
	}
	
	
}
