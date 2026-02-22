package progresa.subastas.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "adjudicacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Adjudicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "importe_final", nullable = false)
    private Double importeFinal;

    @Column(name = "fecha_adjudicacion")
    private LocalDateTime fechaAdjudicacion;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "subasta_id", nullable = false)
    private Subasta subasta;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario adjudicatario;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "adjudicacion")
    @JsonIgnore
    private Set<Recurso> recursos = new HashSet<>();

    public Adjudicacion(Double importeFinal, Subasta subasta, Usuario adjudicatario) {
        this.importeFinal = importeFinal;
        this.subasta = subasta;
        this.adjudicatario = adjudicatario;
        this.fechaAdjudicacion = LocalDateTime.now();
        this.recursos = new HashSet<>();
    }
}
