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
@Table(name = "Cursos") // definimos nombre de la tabla

public class Curso implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    //RELACIÓN N:1 con Profesor

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor_fk") //Con esto referenciamos a la tabla profe
    private Profesor profesor;

    //RELACIÓN N:M con Estudiante
    // "mappedBy" indica que la entidad "Estudiante" gestionará la tabla de unión.
    @ManyToMany(mappedBy = "cursos")
    private Set<Estudiante> estudiantes = new HashSet<>();


}
