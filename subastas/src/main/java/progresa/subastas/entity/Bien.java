package progresa.subastas.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bien")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "valor_inicial", nullable = false)
    private Double valorInicial;

    @Column(name = "estado")
    private String estado; // DISPONIBLE, EN_SUBASTA, ADJUDICADO

    @OneToOne(mappedBy = "bien")
    @JsonIgnore
    private Subasta subasta;

    public Bien(String descripcion, Double valorInicial) {
        this.descripcion = descripcion;
        this.valorInicial = valorInicial;
        this.estado = "DISPONIBLE";
    }
}
