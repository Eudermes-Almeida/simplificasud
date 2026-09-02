package somonitores.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
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
import somonitores.dto.CadastroCredenciaisRequestDTO;
import somonitores.dto.IdentificacaoRequestDTO;
import somonitores.dto.IdentificacaoResponseDTO;
import somonitores.dto.LideresDTO;
import somonitores.entity.LideresEntity;
import somonitores.service.LideresService;

import java.util.List;
import java.util.Optional;

// Endpoint de LEITURA apenas (lista líderes por unidade, campos não-sensíveis). Os
// endpoints do fluxo de autenticação propriamente dito (identificação por
// nascimento+registromembro, cadastro de login/senha, login) ainda não existem aqui --
// ver memória project-raiox-auth-design para o desenho combinado, próximo passo.
@Path("/lideres")
public class LideresResource {

    @Inject
    LideresService lideresService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Busca Líderes", description = "Busca líderes (bispados, presidências de ramo, presidência da estaca) por unidade")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Líderes encontrados (pode ser lista vazia se a unidade não tiver registros)", content = @Content(schema = @Schema(implementation = LideresDTO.class))),
            @APIResponse(responseCode = "400", description = "Parâmetro 'unidade' não informado", content = @Content(schema = @Schema(implementation = LideresDTO.class))),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = LideresDTO.class))),
    })
    @Operation(summary = "Busca líderes por unidade", description = "Busca os líderes de uma unidade específica. Informar 'Estaca Betim' retorna os líderes de todas as unidades. Campos sensíveis (registromembro, data de nascimento, senha) não são retornados por este endpoint.")
    public Response buscaLideresPorUnidade(@QueryParam("unidade") String unidade) {
        try {
            if (unidade == null || unidade.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("O parâmetro 'unidade' é obrigatório")
                        .build();
            }

            List<LideresDTO> lideres = lideresService.buscaLideresPorUnidade(unidade);

            return Response.ok(lideres).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar líderes: " + e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/identificar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Identificação de Líder", description = "Primeiro passo do primeiro acesso: identifica o líder por nascimento + últimos 4 caracteres do registro de membro")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Líder encontrado e ainda sem login/senha cadastrados", content = @Content(schema = @Schema(implementation = IdentificacaoResponseDTO.class))),
            @APIResponse(responseCode = "400", description = "Dados de identificação não informados"),
            @APIResponse(responseCode = "404", description = "Não encontrado, ou já cadastrado anteriormente — mesma mensagem para os dois casos, de propósito"),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor"),
    })
    @Operation(summary = "Identifica um líder pelo nascimento + registro de membro", description = "Não distingue 'não encontrado' de 'já cadastrado' na resposta, para não revelar qual dos dois motivos causou a falha.")
    public Response identificar(IdentificacaoRequestDTO request) {
        try {
            if (request == null || request.getNascimento() == null
                    || request.getUltimosQuatroRegistro() == null || request.getUltimosQuatroRegistro().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Informe a data de nascimento e os últimos 4 caracteres do registro de membro.")
                        .build();
            }

            Optional<LideresEntity> encontrado = lideresService.buscaPorNascimentoERegistroMembro(
                    request.getNascimento(), request.getUltimosQuatroRegistro());

            boolean jaCadastrado = encontrado.isPresent()
                    && encontrado.get().getLogin() != null
                    && !encontrado.get().getLogin().isBlank();

            if (encontrado.isEmpty() || jaCadastrado) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Procure a Presidência da Estaca.")
                        .build();
            }

            LideresEntity lider = encontrado.get();
            IdentificacaoResponseDTO resposta = IdentificacaoResponseDTO.builder()
                    .id(lider.getId())
                    .nome(lider.getNome())
                    .build();

            return Response.ok(resposta).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao identificar líder: " + e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/cadastrar-credenciais")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Cadastro de Credenciais", description = "Segundo passo do primeiro acesso: grava login e senha escolhidos pelo líder já identificado")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Login e senha cadastrados com sucesso"),
            @APIResponse(responseCode = "400", description = "Login/senha não informados ou senha muito curta"),
            @APIResponse(responseCode = "404", description = "Id de líder inexistente"),
            @APIResponse(responseCode = "409", description = "Login já em uso por outro líder, ou este líder já tinha se cadastrado antes"),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor"),
    })
    @Operation(summary = "Cadastra login e senha de um líder já identificado", description = "Só é aceito uma única vez por líder — uma vez que login/senha existem, uma nova tentativa retorna 409, sem reabrir cadastro.")
    public Response cadastrarCredenciais(CadastroCredenciaisRequestDTO request) {
        try {
            if (request == null || request.getId() == null
                    || request.getLogin() == null || request.getLogin().trim().isEmpty()
                    || request.getSenha() == null || request.getSenha().trim().length() < 4) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Informe login e uma senha com pelo menos 4 caracteres.")
                        .build();
            }

            Optional<LideresEntity> liderOpt = lideresService.buscaLiderPorId(request.getId());
            if (liderOpt.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Procure a Presidência da Estaca.")
                        .build();
            }

            LideresEntity lider = liderOpt.get();
            if (lider.getLogin() != null && !lider.getLogin().isBlank()) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("Procure a Presidência da Estaca.")
                        .build();
            }

            if (lideresService.loginJaExiste(request.getLogin())) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("Este login já está em uso, escolha outro.")
                        .build();
            }

            lideresService.cadastrarCredenciais(request.getId(), request.getLogin(), request.getSenha());

            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao cadastrar credenciais: " + e.getMessage())
                    .build();
        }
    }

}
