package progresa.subastas.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "subasta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subasta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDateTime fechaFin;

    @Column(name = "precio_base", nullable = false)
    private Double precioBase;

    @Column(name = "estado")
    private String estado; // ABIERTA, CERRADA, ADJUDICADA, DESIERTA

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bien_id", nullable = false)
    private Bien bien;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subasta")
    @JsonManagedReference
    private Set<Puja> pujas = new HashSet<>();

    @OneToOne(mappedBy = "subasta")
    @JsonIgnore
    private Adjudicacion adjudicacion;

    public Subasta(LocalDateTime fechaInicio, LocalDateTime fechaFin, Double precioBase, Bien bien) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.precioBase = precioBase;
        this.bien = bien;
        this.estado = "ABIERTA";
        this.pujas = new HashSet<>();
    }
    public Double obtenerPujaMasAlta() {
        Double maximo = this.precioBase;
        if (pujas != null) {
            for (Puja p : pujas) {
                if (p.getImporte() > maximo) {
                    maximo = p.getImporte();
                }
            }
        }
        return maximo;
    }
}
