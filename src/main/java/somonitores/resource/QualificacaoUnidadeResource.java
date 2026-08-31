package somonitores.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import somonitores.dto.QualificacaoUnidadeDTO;
import somonitores.service.QualificacaoUnidadeService;

import java.util.List;

@Path("/qualificacaounidade")
public class QualificacaoUnidadeResource {

    @Inject
    QualificacaoUnidadeService qualificacaoUnidadeService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Busca Qualificação de Unidade", description = "Busca Qualificação de Unidade por unidade")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Qualificações de unidade encontradas (pode ser lista vazia se a unidade não tiver registros)", content = @Content(schema = @Schema(implementation = QualificacaoUnidadeDTO.class))),
            @APIResponse(responseCode = "400", description = "Parâmetro 'unidade' não informado", content = @Content(schema = @Schema(implementation = QualificacaoUnidadeDTO.class))),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = QualificacaoUnidadeDTO.class))),
    })
    @Operation(summary = "Busca Qualificação de Unidade por unidade", description = "Busca a qualificação de uma unidade específica. Informar 'Estaca Betim' retorna os dados de todas as unidades, já que a estaca é a soma de todas as alas/ramos.")
    public Response buscaQualificacoesUnidadePorUnidade(@QueryParam("unidade") String unidade) {
        try {
            if (unidade == null || unidade.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("O parâmetro 'unidade' é obrigatório")
                        .build();
            }

            List<QualificacaoUnidadeDTO> qualificacoes = qualificacaoUnidadeService.buscaQualificacoesUnidadePorUnidade(unidade);

            return Response.ok(qualificacoes).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar qualificações de unidade: " + e.getMessage())
                    .build();
        }
    }

}
