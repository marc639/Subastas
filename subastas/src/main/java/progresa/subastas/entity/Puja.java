package progresa.subastas.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "puja")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Puja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "importe", nullable = false)
    private Double importe;

    @Column(name = "fecha_puja")
    private LocalDateTime fechaPuja;

    @ManyToOne
    @JoinColumn(name = "subasta_id", nullable = false)
    @JsonBackReference
    private Subasta subasta;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public Puja(Double importe, Subasta subasta, Usuario usuario) {
        this.importe = importe;
        this.subasta = subasta;
        this.usuario = usuario;
        this.fechaPuja = LocalDateTime.now();
    }
}
