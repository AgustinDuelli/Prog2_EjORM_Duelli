package service;

import connections.HibernateUtil;
import entities.Posts;
import org.hibernate.Session;

import java.util.List;

public class PostsService {

    public Posts getPost(Long id) {
        try(Session session = HibernateUtil.getSession().getSessionFactory().openSession()) {
            return session.get(Posts.class, id);
        } catch (Exception e) {
            System.out.println("Error al obtener el post de la base de datos");
            System.out.println("Error: \n" + e.getMessage());
        }
        return null;
    }

    public List<Object[]> cantidadDeComentariosPost(){
        List<Object[]> postCantidadComentariosList = null;
        try (Session session = HibernateUtil.getSession().getSessionFactory().openSession()) {
          postCantidadComentariosList = session.createQuery("select p.titulo, size(p.comentarios) from Posts p").list();
        } catch (Exception e) {
            System.out.println("Error al obtener la cantidad de comentarios por post");
            System.out.println("Error: \n" + e.getMessage());
        }
        return postCantidadComentariosList;
    }

    public List<Posts> listarSinComentarios() {
        List<Posts> posts;
        try (Session session = HibernateUtil.getSession().getSessionFactory().openSession()) {
            posts = session.createQuery("from Posts p where size(p.comentarios) = 0 ORDER BY fechaPublicacion desc", Posts.class).list();
        } catch (Exception e) {
            System.out.println("Error al obtener los posts sin comentarios");
            System.out.println("Error: \n" + e.getMessage());
            return null;
        }
        return posts;
    }
}
