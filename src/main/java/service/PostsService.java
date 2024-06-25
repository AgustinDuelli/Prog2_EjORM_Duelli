package service;

import connections.HibernateUtil;
import entities.Comentarios;
import entities.Posts;
import org.hibernate.Session;

import javax.persistence.criteria.*;
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
          //postCantidadComentariosList = session.createQuery("select p.titulo, size(p.comentarios) from Posts p").list();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);

            Root<Posts> postRoot = criteriaQuery.from(Posts.class);

// Select titulo and size(comentarios) as separate elements in an array
            criteriaQuery.multiselect(postRoot.get("titulo"), criteriaBuilder.size(postRoot.get("comentarios")));

            postCantidadComentariosList = session.createQuery(criteriaQuery).getResultList();

        } catch (Exception e) {
            System.out.println("Error al obtener la cantidad de comentarios por post");
            System.out.println("Error: \n" + e.getMessage());
        }
        return postCantidadComentariosList;
    }

    public List<Posts> listarSinComentarios() {
        List<Posts> posts;
        try (Session session = HibernateUtil.getSession().getSessionFactory().openSession()) {
            //posts = session.createQuery("from Posts p where size(p.comentarios) = 0 ORDER BY fechaPublicacion desc", Posts.class).list();

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Posts> criteriaQuery = criteriaBuilder.createQuery(Posts.class);

            Root<Posts> postRoot = criteriaQuery.from(Posts.class);

            criteriaQuery.select(postRoot);

// Left join comments to potentially include posts with no comments
            Join<Posts, Comentarios> commentsJoin = postRoot.join("comentarios", JoinType.LEFT);

// Filter posts where size of comments collection is 0
            criteriaQuery.where(criteriaBuilder.isEmpty(postRoot.get("comentarios")));

// Order by fechaPublicacion descending
            criteriaQuery.orderBy(criteriaBuilder.desc(postRoot.get("fechaPublicacion")));

            posts = session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            System.out.println("Error al obtener los posts sin comentarios");
            System.out.println("Error: \n" + e.getMessage());
            return null;
        }
        return posts;
    }
}
