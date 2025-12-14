package modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Estudiantes") // definimos nombre de la tabla

public class Estudiante implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    // Relación N:M con Curso
    // Estudiante es el propietario de la relación, por lo que define la tabla de unión
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "estudiante_curso", //Esto es el nomre de la tabla intermedia que Hibernate creará
            joinColumns = @JoinColumn(name = "estudiante_fk"), //columna que referencia a Estudiantes (propietario)
            inverseJoinColumns = @JoinColumn(name = "curso_fk") // Columna que referencia a Cursos (el lado inverso)
    )
    private Set<Curso> cursos = new HashSet<>();
}
