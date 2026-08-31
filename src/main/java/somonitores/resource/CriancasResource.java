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
import somonitores.dto.CriancasDTO;
import somonitores.service.CriancasService;

import java.util.List;

@Path("/criancas")
public class CriancasResource {

    @Inject
    CriancasService criancasService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Busca Crianças", description = "Busca Crianças por unidade")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Crianças encontradas (pode ser lista vazia se a unidade não tiver registros)", content = @Content(schema = @Schema(implementation = CriancasDTO.class))),
            @APIResponse(responseCode = "400", description = "Parâmetro 'unidade' não informado", content = @Content(schema = @Schema(implementation = CriancasDTO.class))),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = CriancasDTO.class))),
    })
    @Operation(summary = "Busca Crianças por unidade", description = "Busca as crianças de uma unidade específica. Informar 'Estaca Betim' retorna os dados de todas as unidades, já que a estaca é a soma de todas as alas/ramos.")
    public Response buscaCriancasPorUnidade(@QueryParam("unidade") String unidade) {
        try {
            if (unidade == null || unidade.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("O parâmetro 'unidade' é obrigatório")
                        .build();
            }

            List<CriancasDTO> criancas = criancasService.buscaCriancasPorUnidade(unidade);

            return Response.ok(criancas).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar crianças: " + e.getMessage())
                    .build();
        }
    }

}
