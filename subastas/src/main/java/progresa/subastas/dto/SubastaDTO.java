package progresa.subastas.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SubastaDTO {
    private Long bienId;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Double precioBase;
}
