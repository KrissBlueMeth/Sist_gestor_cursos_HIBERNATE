package modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Profesores") // definimos nombre de la tabla
@EqualsAndHashCode(exclude = "cursos")
public class Profesor implements Serializable {

    // Clave Primaria (PK)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Atributos Simples
    private String nombre;

    //Relación 1:M con Curso
    @OneToMany(mappedBy = "profesor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Curso> cursos = new ArrayList<>();

//Este constructor lo añado manual porque en el mainapp me salia error al usar el @data
    public Profesor(String nombre) {
        this.nombre = nombre;
    }

}