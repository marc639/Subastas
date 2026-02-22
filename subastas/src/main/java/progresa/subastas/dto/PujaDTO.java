package progresa.subastas.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PujaDTO {
    private Long usuarioId;
    private Long subastaId;
    private Double importe;
}
