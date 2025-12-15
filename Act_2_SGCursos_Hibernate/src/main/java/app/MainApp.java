package app;

import dao.CursoDAOGenericoImpl;
import dao.EstudianteDAOGenericoImpl;
import dao.ProfesorDAOGenericoImpl;
import util.HibernateUtil;
import modelo.Curso;
import modelo.Profesor;
import modelo.Estudiante;

public class MainApp {

    // Instanciamos las implementaciones DAO.
    // Estas son variables de instancia, por eso necesitamos 'app.insertarDatos()' en el main.
    private final ProfesorDAOGenericoImpl profesorDAO = new ProfesorDAOGenericoImpl();
    private final CursoDAOGenericoImpl cursoDAO = new CursoDAOGenericoImpl();
    private final EstudianteDAOGenericoImpl estudianteDAO = new EstudianteDAOGenericoImpl();

    public static void main(String[] args) {
        // aqui creo la instancia
        MainApp app = new MainApp();

        // 1. Inserción de los datos iniciales
        app.insertarDatos();

        // 2. Ejecución de las consultas requeridas
        app.mostrarConsultas();

        // Buena práctica: cerrar la SessionFactory al finalizar la aplicación
        HibernateUtil.shutdown();
    }

    private void insertarDatos() {
        System.out.println("--- 1. INSERTANDO PROFESOR Y CURSOS ---");

        Profesor prof1 = new Profesor("Dr. Smith");
        Curso c1 = new Curso("Hibernate Básico");
        Curso c2 = new Curso("MySQL Avanzado");

        // Esto es para hacer la BIDIRECCIONALIDAD MANUALMENTE:
        // Lado Uno (Profesor) y Lado Muchos (Curso) sincronizados:
        prof1.getCursos().add(c1);
        prof1.getCursos().add(c2);
        c1.setProfesor(prof1);
        c2.setProfesor(prof1);

        profesorDAO.save(prof1);
        System.out.println("Profesor guardado con ID: " + prof1.getId());

        System.out.println("\n--- 2. INSERTANDO ESTUDIANTES Y ASIGNANDO A CURSOS ---");

        Estudiante e1 = new Estudiante("Alice Johnson");
        Estudiante e2 = new Estudiante("Bob Williams");
        Estudiante e3 = new Estudiante("Charlie Brown");

        // igual que antes para la bideccionalidad sin metodos "extra" de ayuda, para Many-to-Many:

        // e1 en c1
        e1.getCursos().add(c1);
        c1.getEstudiantes().add(e1);

        // e2 en c1
        e2.getCursos().add(c1);
        c1.getEstudiantes().add(e2);

        // e3 en c2 y c1
        e3.getCursos().add(c2);
        c2.getEstudiantes().add(e3);
        e3.getCursos().add(c1);
        c1.getEstudiantes().add(e3);

        // Como Curso no tiene un metodo de persistencia directo aquí,
        // dependemos de que Hibernate persista los cambios a c1 y c2 a través de las relaciones
        // y de que las llamadas a estudianteDAO.save(eN) guarden a los estudiantes.

        estudianteDAO.save(e1);
        estudianteDAO.save(e2);
        estudianteDAO.save(e3);

        System.out.println("3 Estudiantes guardados y asignados.");
    }

    private void mostrarConsultas() {
        System.out.println("\n=============================================");
        System.out.println("--- CONSULTAS REQUERIDAS ---");

        // Asumimos ID=1 para las primeras entidades insertadas
        Long targetProfesorId = 1L;
        Long targetCursoId = 1L;

        // Requisito A: Muestra los cursos de un profesor
        Profesor profRecuperado = profesorDAO.getById(targetProfesorId);

        if (profRecuperado != null) {
            System.out.println("\n-> Cursos impartidos por " + profRecuperado.getNombre() + ":");
            // Al acceder a getCursos(), Hibernate ejecuta la consulta para llenar la lista
            profRecuperado.getCursos().forEach(curso ->
                    System.out.println("  - " + curso.getNombre())
            );
        }

        // Requisito B: Muestra los estudiantes inscritos en un curso
        Curso cursoRecuperado = cursoDAO.getById(targetCursoId);

        if (cursoRecuperado != null) {
            System.out.println("\n-> Estudiantes inscritos en el curso " + cursoRecuperado.getNombre() + ":");
            // Al acceder a getEstudiantes(), Hibernate consulta la tabla intermedia
            cursoRecuperado.getEstudiantes().forEach(estudiante ->
                    System.out.println("  - " + estudiante.getNombre())
            );
        }
        System.out.println("=============================================");
    }
}