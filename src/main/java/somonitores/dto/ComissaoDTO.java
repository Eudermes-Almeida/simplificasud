package somonitores.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComissaoDTO {
    private BigDecimal percentual;
    private BigDecimal valorComissional;
    private BigDecimal comissao;
}
