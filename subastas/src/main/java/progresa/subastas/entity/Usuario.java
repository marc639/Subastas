package progresa.subastas.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "nif", nullable = false, unique = true)
    private String nif;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
    @JsonIgnore
    private Set<Puja> pujas = new HashSet<>();

    public Usuario(String nombre, String email, String nif) {
        this.nombre = nombre;
        this.email = email;
        this.nif = nif;
    }
}
