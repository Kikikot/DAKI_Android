package com.androidstudy.com.daki;

import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import objetos.*;

public class GestorDDBB implements Runnable {

    private Conexion db;
    private Conexion dbNotificaciones;

    private int numberOfNotifications = 0;
    private boolean working = true;

    private final String dbHost = "d791cb50-1163-484d-9954-a41c017ec13a.mysql.sequelizer.com";
    private final String dbName = "dbd791cb501163484d9954a41c017ec13a";
    private final String dbUser = "shzplpupurazfjjc";
    private final String dbPass = "a4tCx5f3edcbDc8w7wUNjZwJjfvtokPGKyZirhJasqKmnsVyknr7QApiFSssGUN3";

    private final float relacion = (21.75f*549.61f)/(1000000f*22.0969f);

    public GestorDDBB(){
        new Thread(this, "GestorDDBB").start();
    }

    public void desconectar() {
        working=false;
        db.desconectar();
        dbNotificaciones.desconectar();
    }

    public boolean conecta(String user, String pass) {
        String consulta = "select count(*) from usuarios where alias='"+user+"' and pass='"+pass+"'";
        boolean hayRespuesta = db.consulta(consulta);
        if (hayRespuesta){
            String respuesta = db.getString();
            if (respuesta.equals("1")) return true;
        }
        return false;
    }

    public String[] getDatosCortosUsuario(String usuario) {
        String consulta = "select alias, email, p.poblacion, CONCAT(nombre, ' ', apellidos), direccion "
                + "from usuarios u, poblaciones p "
                + "where alias='"+usuario+"' and u.poblacion=p.id";
        if (db.consulta(consulta)) return db.getDatosPrimeraFila();
        return null;
    }

    @Override
    public void run() {
        db = new Conexion(Conexion.TipoDDBB.MySQL, dbHost, dbName, dbUser, dbPass);
        dbNotificaciones = new Conexion(Conexion.TipoDDBB.MySQL, dbHost, dbName, dbUser, dbPass);
        while (working){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            uploadNumberNotifications();
        }
    }

    public int getNumberOfNotifications(){
        return numberOfNotifications;
    }

    private void uploadNumberNotifications() {
        //TODO
    }

    public String[] getProvincias() {
        String consulta = "select provincia from provincias order by provincia";
        if (db.consulta(consulta)) return db.getDatosColumna(0);
        return null;
    }

    public String[] getLocalidades(String provincia) {
        String consulta = "select poblacion from poblaciones po, provincias pr "
                + "where po.provincia=pr.id and pr.provincia='"+provincia+"' order by poblacion";
        if (db.consulta(consulta)) return db.getDatosColumna(0);
        return null;
    }

    public boolean existeUsuario(String user) {
        String consulta = "select count(*) from usuarios where alias='"+user+"'";
        if (db.consulta(consulta)){
            if (db.getString().equals("1")) return true;
        }
        return false;
    }

    public boolean existeMail(String mail) {
        String consulta = "select count(*) from usuarios where email='"+mail+"'";
        if (db.consulta(consulta)){
            if (db.getString().equals("1")) return true;
        }
        return false;
    }

    public void registrarUsuario(String alias, String pass, String mail, String poblacion,
                                 String nombre, String apellidos, String direccion, String cp) {
        String orden = "insert into usuarios(alias, pass, email, direccion, cp, poblacion, nombre, apellidos) "
                + "values('"+alias+"', '"+pass+"', '"+mail+"', '"+direccion+"',"
                + " "+cp+", (select id from poblaciones where poblacion='"+poblacion+"'), '"+nombre+"', '"+apellidos+"')";
        db.modifica(orden);
    }

    public void enviarMensajeAnonimo(String asunto, String mensaje, String correo) {
        String orden = "insert into mensajes_usuarios(asunto, mensaje, mail) "
                + "values('"+asunto+"', '"+mensaje+"', '"+correo+"')";
        db.modifica(orden);
    }

    public void enviarMensaje(String usuario, String asunto, String mensaje, String correo) {
        String idUsuario = selectIdUsuario(usuario);
        String orden = "insert into mensajes_usuarios(usuario, asunto, mensaje, mail) "
                + "values("+idUsuario+", '"+asunto+"', '"+mensaje+"', '"+correo+"')";
        db.modifica(orden);
    }

    public String[] getDatosLargoUsuario(String usuario){
        String consulta = "select u.pass, u.email, u.direccion, u.cp, p.id, p.poblacion, u.nombre, "
                + "u.apellidos from usuarios u, poblaciones p "
                + "where alias='"+usuario+"' and p.id = u.poblacion";
        if (db.consulta(consulta)) return db.getDatosPrimeraFila();
        return null;
    }

    private String selectIdUsuario(String usuario) {
        return "(select id from usuarios where alias = '"+usuario+"')";
    }

    public String getProvinciaDePoblacion(int idPoblacion) {
        String consulta = "select pr.provincia from provincias pr, poblaciones po where po.provincia = pr.id and po.id = "+idPoblacion;
        if (db.consulta(consulta)) return db.getString();
        return null;
    }

    public void actualizarUsuario(String usuario, String pass, String mail, String poblacion,
                                  String nombre, String apellidos, String direccion, String cp) {
        String orden = "update usuarios set pass='"+pass+"', email='"+mail+"', direccion='"+direccion+"', cp="+cp+", "
                + "poblacion=(select id from poblaciones where poblacion='"+poblacion+"'), "
                + "nombre='"+nombre+"', apellidos='"+apellidos+"' where alias='"+usuario+"'";
        db.modifica(orden);
    }

    public Usuario getUsuario(String user) {
        String consulta = "select id, pass, email, direccion, cp, poblacion, nombre, apellidos "
                + "from usuarios where alias = '"+user+"'";
        if (db.consulta(consulta)){
            String[] pre = db.getDatosPrimeraFila();
            Usuario usuario = new Usuario(Integer.parseInt(pre[0]), user, pre[1], pre[2], pre[3],
                    Integer.parseInt(pre[4]), Integer.parseInt(pre[5]), pre[6], pre[7]);
            return usuario;
        }
        return null;
    }

    public PrevisualizacionSorteo[] getSorteosPorKm(String usuario) {
        String[] idXY = getIdLocalizado(usuario);
        String consulta = construyeConsultaPreSorteos(idXY[0], idXY[1], idXY[2]);
        consulta = consulta + "order by distancia limit 20";
        return getSorteosDeConsulta(consulta);
    }

    public PrevisualizacionSorteo[] getSorteosPorValor(String usuario) {
        String[] idXY = getIdLocalizado(usuario);
        String consulta = construyeConsultaPreSorteos(idXY[0], idXY[1], idXY[2]);
        consulta = consulta + "order by valor desc, distancia limit 20";
        return getSorteosDeConsulta(consulta);
    }

    public PrevisualizacionSorteo[] getSorteosPorRecientes(String usuario) {
        String[] idXY = getIdLocalizado(usuario);
        String consulta = construyeConsultaPreSorteos(idXY[0], idXY[1], idXY[2]);
        consulta = consulta + "order by fecha_limite desc, distancia limit 20";
        return getSorteosDeConsulta(consulta);
    }

    public PrevisualizacionSorteo[] getSorteosPorComercio(String usuario, String tipo) {
        String[] idXY = getIdLocalizado(usuario);
        String consulta = construyeConsultaPreSorteos(idXY[0], idXY[1], idXY[2]);

        String idTipo = "select id from tipos_de_negocio where tipo='"+tipo+"'";

        consulta = consulta + " and s.id in (select sorteo from sorteos_x_patrocinador "
                + "where patrocinador in (select patrocinador "
                + "from tipo_de_negocio_x_patrocinador "
                + "where negocio=("+idTipo+"))) "
                + "order by distancia limit 20";
        return getSorteosDeConsulta(consulta);
    }

    private String[] getIdLocalizado(String usuario) {
        String consulta = "select u.id, p.x, p.y from usuarios u, poblaciones p where "
                + "u.alias='"+usuario+"' and p.id = "
                + "(select poblacion from usuarios where alias = '"+usuario+"')";
        db.consulta(consulta);
        return db.getDatosPrimeraFila();
    }

    private String construyeConsultaPreSorteos(String id, String a, String b) {
        String totalTickets = "select count(*) from tickets where participa_en = s.id";

        String verdes = "select participa_en from tickets where propietario = "+id
                + " union distinct "
                + "select sorteo from sorteos_seguidos_x_usuario where usuario = "+id;

        String distanciaMenor = "select round(min(sqrt(((x)-("+a+"))*((x)-("+a+"))+((y)-("+b+"))*((y)-("+b+"))))*"+relacion+", 2) from poblaciones where id in ("
                + "select poblacion from patrocinadores where id in ("
                + "select patrocinador from sorteos_x_patrocinador where sorteo = s.id"
                + ")"
                + ")";

        String consulta = "select s.id, s.valor, ("+totalTickets+") as tickets, ("+distanciaMenor+") as distancia, imagen, s.id in ("+verdes+") as ojeado "
                + "from sorteos s where s.id in (select sorteo from sorteos_x_patrocinador group by sorteo) "
                + "and s.estado = (select id from estados_sorteos where estado='ACTIVO') ";
        return consulta;
    }

    private PrevisualizacionSorteo[] getSorteosDeConsulta(String consulta) {
        db.consulta(consulta);
        String[][] datos = db.getDatos();
        PrevisualizacionSorteo[] sorteos = new PrevisualizacionSorteo[datos.length];
        for (int i=0; i<sorteos.length; i++){
            String[] pre = datos[i];
            boolean ojeado = pre[5].equals("1");
            try {
                URL url = new URL(pre[4]);
                sorteos[i] = new PrevisualizacionSorteo(url, pre[0], pre[3], pre[1], pre[2], ojeado);
            } catch (MalformedURLException e) {
                sorteos[i] = new PrevisualizacionSorteo(pre[0], pre[3], pre[1], pre[2], ojeado);
            }
        }
        return sorteos;
    }

    public PrevisualizacionSorteo[] getSorteosParticipando(String usuario) {
        String[] idXY = getIdLocalizado(usuario);
        String consulta = construyeConsultaMisSorteos(idXY[0], idXY[1], idXY[2]);
        consulta = consulta + " and s.id in (select participa_en  from tickets where propietario = "+idXY[0]+")";
        return getSorteosDeConsulta(consulta);
    }

    public PrevisualizacionSorteo[] getSorteosSiguiendo(String usuario) {
        String[] idXY = getIdLocalizado(usuario);
        String consulta = construyeConsultaMisSorteos(idXY[0], idXY[1], idXY[2]);
        consulta = consulta + " and s.id in (select sorteo from sorteos_seguidos_x_usuario where usuario = "+idXY[0]+")";
        return getSorteosDeConsulta(consulta);
    }

    private String construyeConsultaMisSorteos(String id, String a, String b) {
        String totalTickets = "select count(*) from tickets where participa_en = s.id";

        String distanciaMenor = "select round(min(sqrt(((x)-("+a+"))*((x)-("+a+"))+((y)-("+b+"))*((y)-("+b+"))))*"+relacion+", 2) from poblaciones where id in ("
                + "select poblacion from patrocinadores where id in ("
                + "select patrocinador from sorteos_x_patrocinador where sorteo = s.id"
                + ")"
                + ")";

        String consulta = "select s.id, s.valor, ("+totalTickets+") as tickets, ("+distanciaMenor+") as distancia, imagen, 1 as ojeado "
                + "from sorteos s where s.estado = (select id from estados_sorteos where estado='ACTIVO') ";
        return consulta;
    }

    public String[] getRestoDatosDelSorteo(String idSorteo, String usuario) {
        // titulo, tickets_de_usuario, fecha_sorteo, total_comantarios, descripcion, condiciones
        String consulta = "select titulo, "
                + "(select count(*) from tickets where participa_en="+idSorteo+" and "
                + "propietario=(select id from usuarios where alias ='"+usuario+"')), "
                + "fecha_sorteo, "
                + "(select count(*) from comentarios where sorteo="+idSorteo+"), "
                + "descripcion, "
                + "condiciones, "
                + "id in (select sorteo from sorteos_seguidos_x_usuario where "
                + "usuario=(select id from usuarios where alias ='"+usuario+"' )"
                + "and sorteo="+idSorteo+"), "
                + "ticket_minimo "
                + "from sorteos where id="+idSorteo;
        if(db.consulta(consulta)) return db.getDatosPrimeraFila();
        return null;
    }

    public boolean insertarComentario(Comentario comentario) {
        String orden = "insert into comentarios(autor, fecha, sorteo, comentario) "
                + "values((select id from usuarios where alias='"+comentario.getAutor()+"'), sysdate(), "
                + comentario.getSorteo()+", '"+comentario.getComentario()+"')";

        System.out.println(orden);
        if (db.modifica(orden)){
            orden = "select id, fecha from comentarios "
                    + "where sorteo="+comentario.getSorteo()+" and comentario='"+comentario.getComentario()+"' "
                    + "and autor=(select id from usuarios where alias='"+comentario.getAutor()+"') "
                    + "and fecha=(select max(fecha) from comentarios "
                    + "where sorteo="+comentario.getSorteo()+" "
                    + "and comentario='"+comentario.getComentario()+"' "
                    + "and autor=(select id from usuarios where alias='"+comentario.getAutor()+"')"
                    + ")";
            if (db.consulta(orden)){
                String[] data = db.getDatosPrimeraFila();
                comentario.setId(data[0]);
                comentario.setFecha(data[1]);
            }
            return true;
        }
        return false;
    }

    public ArrayList<Comentario> buscarNuevosComentarios(ArrayList<String> ids, String idSorteo) {
        ArrayList<Comentario> comentarios = new ArrayList<Comentario>();

        String losIds = montarIds(ids);
        String consulta = "select id, (select alias from usuarios where id=c.autor), fecha, sorteo, comentario "
                + "from comentarios c "
                + "where sorteo="+idSorteo+losIds+" order by fecha asc";

        System.out.println(consulta);

        if (db.consulta(consulta)){
            String[][] respuesta = db.getDatos();
            for (int i=0; i<respuesta.length; i++){
                String[] temp = respuesta[i];
                System.out.println(temp[4]);
                comentarios.add(new Comentario(temp[0], temp[1], temp[2], temp[3], temp[4]));
            }
        }

        return comentarios;
    }

    private String montarIds(ArrayList<String> ids) {
        String respuesta = "";
        if (ids.size()>0){
            respuesta = " and id not in (";
            for (int i=0; i<ids.size()-1; i++) respuesta = respuesta + ids.get(i) + ", ";
            respuesta = respuesta + ids.get(ids.size()-1)+")";
        }
        return respuesta;
    }

    public ArrayList<Patrocinador> getPatrocinadores(String idSorteo, String usuario) {
        ArrayList<Patrocinador> patrocinadores = new ArrayList<Patrocinador>();

        String consultaLocalizacion = "select x, y from poblaciones where id=(select poblacion from usuarios where alias='"+usuario+"')";

        db.consulta(consultaLocalizacion);
        String[] xy = db.getDatosPrimeraFila();
        String a = xy[0], b =xy[1];

        String selectDistancia = "(select round(min(sqrt(((x)-("+a+"))*((x)-("+a+"))+((y)-("+b+"))*((y)-("+b+"))))*"+relacion+", 2) "
                + "from poblaciones where id = p.poblacion)";

        String consulta = "select id, nombre, contacto, telf, email, direccion, "+selectDistancia+", imagen from patrocinadores p "
                + "where id in (select patrocinador from sorteos_x_patrocinador where sorteo="+idSorteo+")";
        db.consulta(consulta);
        String[][] datos = db.getDatos();
        for (int i=0; i<datos.length; i++){
            String[] temp = datos[i];
            String consultaTipos = "select tipo from tipos_de_negocio where id in (select negocio "
                    + "from tipo_de_negocio_x_patrocinador where patrocinador = "+temp[0]+")";
            db.consulta(consultaTipos);
            String[] tipos = db.getDatosColumna(0);
            patrocinadores.add(new Patrocinador(temp[0], temp[1], tipos, temp[2], temp[4], temp[3], temp[5], temp[6], temp[7]));
        }

        return patrocinadores;
    }

    public boolean empezarASeguirSorteo(String idSorteo, String usuario) {
        String orden = "insert into sorteos_seguidos_x_usuario(usuario, sorteo) "
                + "values((select id from usuarios where alias='"+usuario+"'), "+idSorteo+")";
        return db.modifica(orden);
    }

    public boolean dejarDeSeguirSorteo(String idSorteo, String usuario) {
        String orden = "delete from sorteos_seguidos_x_usuario where sorteo="+idSorteo+" "
                + "and usuario=(select id from usuarios where alias='"+usuario+"')";
        return db.modifica(orden);
    }

    public String[] getNombrePatrocinadores(String idSorteo) {
        String consulta = "select nombre from patrocinadores where id in (select patrocinador "
                + "from sorteos_x_patrocinador where sorteo="+idSorteo+")";
        if (db.consulta(consulta)) return db.getDatosColumna(0);
        return null;
    }

    public boolean registrarTicket(String idSorteo, String ticket, String fecha,
                                   float importe, String patrocinador, String usuario) {
        String orden = "insert into tickets(ticket, propietario, patrocinador, participa_en, valor, fecha_compra) "
                + "values('"+ticket+"', "
                + "(select id from usuarios where alias='"+usuario+"'), "
                + "(select id from patrocinadores where nombre='"+patrocinador+"'), "
                + idSorteo+", "
                + "round("+importe+", 2), "
                + "'"+fecha+"'"
                + ")";

        System.out.println(orden);

        return db.modifica(orden);
    }

    public Presorteo getDatosCortosSorteo(String id, String usuario) {
        System.out.println("'"+id+"'");

        String a = "select x from poblaciones where id = (select poblacion "
                + "from usuarios where alias='"+usuario+"')";

        String b = "select y from poblaciones where id = (select poblacion "
                + "from usuarios where alias='"+usuario+"')";

        String dist = "select round(min(sqrt(((x)-("+a+"))*((x)-("+a+"))+((y)-("+b+"))*((y)-("+b+"))))*"+relacion+", 2) "
                + "from poblaciones where id in ("
                + "select poblacion from patrocinadores where id in ("
                + "select patrocinador from sorteos_x_patrocinador where sorteo = "+id+"))";

        String totalT = "select count(*) from tickets where participa_en = "+id;

        String consulta = "select valor, imagen, ("+dist+"), ("+totalT+") from sorteos where id="+id;

        System.out.println(consulta);

        db.consulta(consulta);
        String[] datos = db.getDatosPrimeraFila();
        return new Presorteo(datos[0], datos[1], datos[2], datos[3]);
    }

    public TicketGroup getTicketsDeSorteo(String id, String usuario) {
        String patrocinador = "select nombre from patrocinadores where id=t.patrocinador";

        String consulta = "select ticket, ("+patrocinador+") as patrocinador, valor, fecha_compra "
                + "from tickets t where participa_en="+id+" and propietario="+
                "(select id from usuarios where alias='"+usuario+"') "
                + "order by patrocinador, ticket";

        db.consulta(consulta);
        String[][] datos = db.getDatos();
        ArrayList<Ticket> tickets = new ArrayList<Ticket>();
        for (int i=0; i<datos.length; i++){
            String[] t = datos[i];
            tickets.add(new Ticket(t[0], usuario, t[1], id, t[3], t[2]));
        }
        TicketGroup grupo = new TicketGroup(id, tickets);
        return grupo;
    }

    public ArrayList<TicketGroup> getTicketsUsuario(String usuario) {
        String idPropietario = "select id from usuarios where alias='"+usuario+"'";

        String consulta = "select participa_en from tickets "
                + "where propietario=("+idPropietario+") group by participa_en;";

        db.consulta(consulta);
        String[] sorteos = db.getDatosColumna(0);

        ArrayList<TicketGroup> grupos = new ArrayList<TicketGroup>();
        for (int i=0; i<sorteos.length; i++){
            grupos.add(getTicketsDeSorteo(sorteos[i], usuario));
        }
        return grupos;
    }

    public synchronized ArrayList<Notificacion> buscaNuevasNotificaciones(
            ArrayList<String> idsNotificaciones, String usuario) {
        String condicion = "";

        if (idsNotificaciones.size()!=0){
            condicion = " and id not in (";
            for (int i=0; i<idsNotificaciones.size()-1; i++){
                condicion = condicion + idsNotificaciones.get(i) + ", ";
            }
            condicion = condicion + idsNotificaciones.get(idsNotificaciones.size()-1) +")";
        }

        String idUsuario = "select id from usuarios where alias='"+usuario+"'";

        String consulta = "select id, usuario, mensaje, estado, asunto from notificaciones_usuarios "
                + "where usuario = ("+idUsuario+")"+condicion+" order by id";

        dbNotificaciones.consulta(consulta);
        String[][] datos = dbNotificaciones.getDatos();
        ArrayList<Notificacion> respuesta = new ArrayList<Notificacion>();
        for (int i=0; i<datos.length; i++){
            String[] t = datos[i];
            respuesta.add(new Notificacion(t[0], t[1], t[2], t[3], t[4]));
        }
        return respuesta;
    }

    public void actualizaNotificacion(Notificacion notificacion) {
        String orden = "update notificaciones_usuarios set estado = "+notificacion.getEstado()+" where id = "+notificacion.getId();
        db.modifica(orden);
    }

    public boolean borrarNotificacion(Notificacion notificacion) {
        String orden = "delete from notificaciones_usuarios where id="+notificacion.getId();
        return db.modifica(orden);
    }

    public String[] getTiposComercio() {
        String consulta = "select tipo from tipos_de_negocio order by tipo";

        db.consulta(consulta);

        return db.getDatosColumna(0);
    }
}
