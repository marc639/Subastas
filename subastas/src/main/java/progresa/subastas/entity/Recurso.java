package progresa.subastas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "recurso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Recurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "motivo", nullable = false)
    private String motivo;

    @Column(name = "fecha_recurso")
    private LocalDateTime fechaRecurso;

    @Column(name = "estado")
    private String estado; // PENDIENTE, RESUELTO, DESESTIMADO

    @ManyToOne
    @JoinColumn(name = "adjudicacion_id", nullable = false)
    private Adjudicacion adjudicacion;

    public Recurso(String motivo, Adjudicacion adjudicacion) {
        this.motivo = motivo;
        this.adjudicacion = adjudicacion;
        this.fechaRecurso = LocalDateTime.now();
        this.estado = "PENDIENTE";
    }
}
