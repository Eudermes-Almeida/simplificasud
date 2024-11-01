package somonitores.resource;

import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import somonitores.dto.ClienteDTO;
import somonitores.entity.ClienteEntity;
import somonitores.service.ClienteService;
import java.sql.SQLException;

import java.sql.SQLException;
import java.util.List;

@Path("/cliente")
public class ClienteResource {

        @Inject
        ClienteService clienteService;

        @Path("/{codcli}")
        @GET
//@Authenticated
        @Tag(name = "Busca Clientes", description = "Busca Clientes por codcli")
//@SecurityRequirement(name = OpenAPIFilter.SSO_SCHEMA)
//@SecurityRequirement(name = OpenAPIFilter.LOGIN_SCHEMA)
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        @APIResponses(value = {
                @APIResponse(responseCode = "400", description = "Requisição recebida não é válida", content = @Content(schema = @Schema(implementation = ClienteDTO.class))),
                @APIResponse(responseCode = "401", description = "Não autenticado", content = @Content(schema = @Schema(implementation = ClienteDTO.class))),
                @APIResponse(responseCode = "403", description = "Token inválido", content = @Content(schema = @Schema(implementation = ClienteDTO.class))),
                @APIResponse(responseCode = "404", description = "Requisição não retornou dados", content = @Content(schema = @Schema(implementation = ClienteDTO.class))),
                //@APIResponse(responseCode = "500", description = Constants.INTERNAL_SERVER_ERROR_MESSAGE),
        })
//@RequestInterceptor(alias = "Detalhar Simulação ")
        @Operation(summary = "Busca Cliente por id", description = "Buscar Cliente por codcli")
        public Response buscaClientePorCodcli(@PathParam("codcli") Long codcli) {
                try {
                        ClienteDTO clienteDTO = clienteService.buscaClientePorCodCli(codcli);

                        if (clienteDTO == null) {
                                return Response.status(Response.Status.NOT_FOUND)
                                        .entity("Cliente não encontrado para o ID: " + codcli)
                                        .build();
                        }

                        return Response.ok(clienteDTO).build();
                } catch (Exception e) {
                        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                .entity("Erro ao buscar cliente: " + e.getMessage())
                                .build();
                }
        }

        @Path("/clientes")
        @GET
        @Tag(name = "Busca Clientes", description = "Busca todos os Clientes")
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        @APIResponses(value = {
                @APIResponse(responseCode = "204", description = "Nenhum cliente encontrado", content = @Content(schema = @Schema(implementation = ClienteDTO.class))),
                @APIResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ClienteDTO.class))),
        })
        @Operation(summary = "Busca todos os Clientes", description = "Buscar todos os Clientes")
        public List<ClienteDTO> buscaTodosClientes() throws Exception {

                               List<ClienteDTO> clientesDTO = clienteService.buscaTodosClientes();

                return clientesDTO;
        }

        @GET
        @Path("/cidade")
        @Produces(MediaType.APPLICATION_JSON)
        @Tag(name = "Busca Clientes por Cidade", description = "Busca Clientes por Cidade")
        public Response buscarClientesPorCidade(@QueryParam("cidade") String cidade) {
                try {
                        List<ClienteDTO> clientes = clienteService.buscarClientesPorCidade(cidade);

                        if (clientes.isEmpty()) {
                                return Response.status(Response.Status.NOT_FOUND)
                                        .entity("Nenhum cliente encontrado para a cidade: " + cidade)
                                        .build();
                        }

                        return Response.ok(clientes).build();
                } catch (Exception e) {
                        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                .entity("Erro ao buscar clientes: " + e.getMessage())
                                .build();
                }
        }

        @GET
        @Path("/fantasia")
        @Produces(MediaType.APPLICATION_JSON)
        @Tag(name = "Busca Clientes pelo Nome Fantasia", description = "Busca Clientes por parte do nome Fantasia")
        public Response buscarClientesPorNomeFantasia(@QueryParam("nomefantasia") String nomefantasia) {
                try {
                        List<ClienteDTO> clientes = clienteService.buscarClientesPorNomeFantasia(nomefantasia);

                        if (clientes.isEmpty()) {
                                return Response.status(Response.Status.NOT_FOUND)
                                        .entity("Nenhum cliente encontrado para o nome fantasia: " + nomefantasia)
                                        .build();
                        }

                        return Response.ok(clientes).build();
                } catch (Exception e) {
                        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                .entity("Erro ao buscar clientes: " + e.getMessage())
                                .build();
                }
        }

                @GET
                @Path("/razaosocial")
                @Produces(MediaType.APPLICATION_JSON)
                @Tag(name = "Busca Clientes por parte da Razao Social", description = "Busca Cliente por parte da Razao Social")
                public Response buscarClientesPorRazaoSocial(@QueryParam("razaosocial") String razaosocial) {
                        try {
                                List<ClienteDTO> clientes = clienteService.buscarClientesPorRazaoSocial(razaosocial);

                                if (clientes.isEmpty()) {
                                        return Response.status(Response.Status.NOT_FOUND)
                                                .entity("Nenhum cliente encontrado para Razao Social : " + razaosocial)
                                                .build();
                                }

                                return Response.ok(clientes).build();
                        } catch (Exception e) {
                                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                        .entity("Erro ao buscar clientes: " + e.getMessage())
                                        .build();
                        }

        }
        @GET
        @Path("/ultimocliente")
        @Produces(MediaType.APPLICATION_JSON)
        @Tag(name = "Busca o CodCli do ultimo Cliente cadastrado", description = "Busca o CodCli do ultimo Cliente cadastrado")
        public Response buscarMaiorId() {
                try {
                        Long maiorId = clienteService.buscarUltimoCliente();

                        if (maiorId == null) {
                                return Response.status(Response.Status.NOT_FOUND)
                                        .entity("Nenhum cliente encontrado.")
                                        .build();
                        }

                        return Response.ok(maiorId).build(); // Retorna apenas o maior ID
                } catch (Exception e) {
                        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                .entity("Erro ao buscar maior ID: " + e.getMessage())
                                .build();
                }
        }
        @POST
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        @Tag(name = "Persiste um novo cliente no banco de dados", description = "Persiste um novo cliente no banco de dados")
        public Response criarCliente(ClienteDTO clienteDTO) {
                try {
                        // DTO para Entity:
                        ClienteEntity clienteEntity = mapToEntity(clienteDTO);

                        // Salva o cliente usando o serviço
                        ClienteEntity clientePersistido = clienteService.salvarCliente(clienteEntity);

                        return Response.status(Response.Status.CREATED)
                                .entity(clientePersistido) // Retorna o DTO criado com status 201
                                .build();
                } catch (Exception e) {

                        return Response.status(Response.Status.BAD_REQUEST)
                                //Se chegar um id repetido no banco de dados, ele não aceita a persistência e dá esta excessão
                                .entity("Erro ao criar cliente: já existe um cliente com o mesmo CODCLI " + e.getMessage())
                                .build();
                        }

                }


        public static ClienteEntity mapToEntity(ClienteDTO dto) {
                if (dto == null) {
                        return null; // Retorna null se o DTO for nulo
                }

                ClienteEntity entity = new ClienteEntity();
                entity.setCodcli(dto.getId());
                entity.setNomefantasia(dto.getNomefantasia().toUpperCase());
                entity.setRazaosocial(dto.getRazaosocial().toUpperCase());
                entity.setTipo(dto.getTipo().toUpperCase());
                entity.setCpfcnpj(dto.getCpfcnpj().toUpperCase());
                entity.setContato(dto.getContato().toUpperCase());
                entity.setRuanum(dto.getRuanum().toUpperCase());
                entity.setNum(dto.getNum().toUpperCase());
                entity.setBairro(dto.getBairro().toUpperCase());
                entity.setCidade(dto.getCidade().toUpperCase());
                entity.setCep(dto.getCep().toUpperCase());
                entity.setFonefixo(dto.getFonefixo().toUpperCase());
                entity.setFone(dto.getFone().toUpperCase());
                entity.setCelular2(dto.getCelular2().toUpperCase());
                entity.setEmail(dto.getEmail().toUpperCase());
                entity.setSegmento(dto.getSegmento().toUpperCase());
                entity.setInscestadual(dto.getInscestadual().toUpperCase());

                return entity;
        }

        @PUT
        @Path("/clientes/{codcli}")
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        @Tag(name = "Atualiza um cliente existente", description = "Atualiza os dados de um cliente existente no banco de dados")
        public Response atualizarCliente(@PathParam("codcli") Long codcli, ClienteDTO clienteDTO) {
                try {

                        // transforma o DTO em Entity:
                        ClienteEntity clienteEntity = mapToEntity(clienteDTO);

                        // Atualiza o cliente usando o serviço
                        ClienteEntity cliente = clienteService.atualizarCliente(codcli, clienteEntity);

                        return Response.ok(cliente) // Retorna o cliente atualizado com status 200
                                .build();
                } catch (Exception e) {
                        // Verifica se a mensagem de erro contém informações sobre não encontrado
                        if (e.getMessage() != null && e.getMessage().contains("Cliente não encontrado")) {
                                return Response.status(Response.Status.NOT_FOUND)
                                        .entity("Cliente não encontrado com o código: " + codcli)
                                        .build();
                        }
                        return Response.status(Response.Status.BAD_REQUEST)
                                .entity("Erro ao atualizar cliente: " + e.getMessage())
                                .build();
                }
        }



        @DELETE
        @Path("/clientes/{codcli}")
        @Produces(MediaType.APPLICATION_JSON)
        @Tag(name = "Remove um cliente existente", description = "Remove um cliente existente do banco de dados")
        public Response deletarCliente(@PathParam("codcli") Long codcli) {
                try {
                        // Deleta o cliente usando o serviço
                        clienteService.deletarCliente(codcli);

                        return Response.noContent().build(); // Retorna 204 No Content
                } catch (Exception e) {
                        // Verifica se a mensagem de erro contém informações sobre não encontrado
                        if (e.getMessage() != null && e.getMessage().contains("Cliente não encontrado")) {
                                return Response.status(Response.Status.NOT_FOUND)
                                        .entity("Cliente não encontrado com o código: " + codcli)
                                        .build();
                        }
                        return Response.status(Response.Status.BAD_REQUEST)
                                .entity("Erro ao deletar cliente: " + e.getMessage())
                                .build();
                }
        }


}


