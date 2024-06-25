import entities.Comentarios;
import entities.Posts;
import service.PostsService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Generar while para consultas
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("\nIntroduce una consulta:");
            System.out.println("1. Insertar comentarios a un post");
            System.out.println("2. Comentarios de un post");
            System.out.println("3. Cantidad de comentarios por post");
            System.out.println("4. Posts sin comentarios");
            System.out.println("5. Salir\n");
            System.out.println("Opción: ");
            try {
                int opcion = Integer.parseInt(br.readLine());
                switch (opcion) {
                    case 1:
                        insertarComentariosEnPost(br);
                        break;
                    case 2:
                        comentariosDePost(br);
                        break;
                    case 3:
                        cantidadComentariosPost(br);
                        break;
                    case 4:
                        postsSinComentarios();
                        break;
                    case 5:
                        System.out.println("Saliendo...");
                        br.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Opción no válida");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

        }
    }

    private static void insertarComentariosEnPost(BufferedReader br) throws IOException {
        Posts post = new PostsService().getPost(leerId(br));
        if (post != null) {
            System.out.println("Post encontrado:\n" + post.getTitulo());
            new service.ComentariosService().insertarComentariosEnPost(post);
        } else {
            System.out.println("Post no encontrado\n");
        }
    }

    private static void comentariosDePost(BufferedReader br) throws IOException {
        Posts post = new PostsService().getPost(leerId(br));
        if (post != null) {
            List<Comentarios> comentarios = new service.ComentariosService().comentariosDePost(post);
            System.out.println("Comentarios del post:" + post.getTitulo());
            comentarios.forEach(c -> {
                System.out.println("Comentario" + comentarios.indexOf(c) + ":");
                System.out.println("Autor: " + c.getAutor());
                System.out.println("Comentario: " + c.getComentario());
                System.out.println("Fecha: " + c.getFechaComentario());
                System.out.println("--\n");
            });
        } else {
            System.out.println("Post no encontrado");
        }
    }

    private static void cantidadComentariosPost (BufferedReader br) throws IOException{
        List<Object[]> postCantidadComentarios = new PostsService().cantidadDeComentariosPost();
        postCantidadComentarios.forEach(p -> {
            System.out.println("Post: " + p[0]);
            System.out.println("    Cantidad de comentarios: " + p[1]);
            System.out.println("--\n");
        });

    }


    private static void postsSinComentarios() {
        List<Posts> posts = new PostsService().listarSinComentarios();
        if (posts.isEmpty()) {
            System.out.println("No hay posts sin comentarios");
        } else {
            System.out.println("Posts sin comentarios:");
            posts.forEach(p -> {
                System.out.println("ID: " + p.getId());
                System.out.println("Titulo: " + p.getTitulo());
                System.out.println("Contenido: " + p.getContenido());
                System.out.println("Fecha de publicación: " + p.getFechaPublicacion());
                System.out.println("--\n");
            });
        }
    }

    // Helpers
    private static Long leerId(BufferedReader br) throws IOException {
        System.out.println("Introduce el id del post:");
        Long idPost = Long.parseLong(br.readLine());
        return idPost;
    }

}
