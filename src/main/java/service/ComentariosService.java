package service;

import connections.HibernateUtil;
import entities.*;
import org.hibernate.Session;

import java.io.*;
import java.util.Calendar;
import java.util.List;


public class ComentariosService {
    public void insertarComentariosEnPost(Posts post) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Comentarios comentario = new Comentarios();
        comentario.setPost(post);
        System.out.println("Introduce el autor del comentario:");
        comentario.setAutor(br.readLine());
        System.out.println("Introduce el comentario:");
        comentario.setComentario(br.readLine());
        comentario.setFechaComentario(Calendar.getInstance().getTime());
        comentario.setPost(post);

        // Guardar comentario en la base de datos
        try (Session session = HibernateUtil.getSession().getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(comentario);
            session.getTransaction().commit();
            System.out.println("Comentario de post guardado!\n");
        } catch (Exception e) {
            System.out.println("Error al guardar el comentario de post actualizado en la base de datos");
            System.out.println("Error: \n" + e.getMessage());
        }
    }

    public List<Comentarios> comentariosDePost (Posts post) {
        List<Comentarios> comentarios;
        try (Session session = HibernateUtil.getSession().getSessionFactory().openSession()) {
            comentarios = session.createQuery("from Comentarios c where c.post.id = :id", Comentarios.class)
                    .setParameter("id", post.getId())
                    .list();
        }
        return comentarios;
    }

}
