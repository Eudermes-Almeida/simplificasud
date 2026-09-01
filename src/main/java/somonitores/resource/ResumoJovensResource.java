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
import somonitores.dto.ResumoJovensDTO;
import somonitores.service.ResumoJovensService;

import java.util.List;

@Path("/resumojovens")
public class ResumoJovensResource {

    @Inject
    ResumoJovensService resumoJovensService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Busca Resumo Jovens", description = "Busca Resumo Jovens por unidade")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Resumo de jovens encontrado (pode ser lista vazia se a unidade não tiver registros)", content = @Content(schema = @Schema(implementation = ResumoJovensDTO.class))),
            @APIResponse(responseCode = "400", description = "Parâmetro 'unidade' não informado", content = @Content(schema = @Schema(implementation = ResumoJovensDTO.class))),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ResumoJovensDTO.class))),
    })
    @Operation(summary = "Busca Resumo Jovens por unidade", description = "Busca o resumo de jovens de uma unidade específica. Informar 'Estaca Betim' retorna os dados de todas as unidades, já que a estaca é a soma de todas as alas/ramos.")
    public Response buscaResumoJovensPorUnidade(@QueryParam("unidade") String unidade) {
        try {
            if (unidade == null || unidade.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("O parâmetro 'unidade' é obrigatório")
                        .build();
            }

            List<ResumoJovensDTO> resumoJovens = resumoJovensService.buscaResumoJovensPorUnidade(unidade);

            return Response.ok(resumoJovens).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar resumo de jovens: " + e.getMessage())
                    .build();
        }
    }

}
