package somonitores.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import somonitores.dto.ClienteDTO;
import somonitores.entity.ClienteEntity;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class ClienteService {

    @Inject
    EntityManager entityManager;

    @Transactional
    public ClienteDTO buscaClientePorCodCli(Long codcli) throws Exception {
        try {
            ClienteEntity clienteEntity = entityManager.find(ClienteEntity.class, codcli);

            if (clienteEntity == null) {
                return null;
            }

            return mapToDTO(clienteEntity);
        } catch (NoResultException e) {
            throw new Exception("Cliente não encontrado");
        }
    }

    @Transactional
    public List<ClienteDTO> buscaTodosClientes() throws Exception {
        try {

            List<ClienteEntity> clientesEntities = entityManager.createQuery("SELECT c FROM ClienteEntity c ORDER BY c.id DESC", ClienteEntity.class).getResultList();

            if (clientesEntities.isEmpty()) {

                throw new Exception("Nenhum cliente encontrado");
            }

            return clientesEntities.stream()
                    .filter(Objects::nonNull) // Filtra entidades nulas
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Erro ao buscar clientes: " + e.getMessage());
        }
    }

    @Transactional
        public List<ClienteDTO> buscarClientesPorCidade(String cidade) {
            if (cidade == null || cidade.trim().isEmpty()) {
                return Collections.emptyList();
            }

            String query = "SELECT obj FROM ClienteEntity obj " +
                    "WHERE UPPER(obj.cidade) = UPPER(:cidade)";
            List<ClienteEntity> clientesEntities = entityManager.createQuery(query, ClienteEntity.class)
                    .setParameter("cidade", cidade)
                    .getResultList();

            return clientesEntities.stream()
                    .filter(Objects::nonNull) // Filtra entidades nulas
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        }

    public List<ClienteDTO> buscarClientesPorNomeFantasia(String nomefantasia) {
        // Criação da consulta JPQL
        String jpql = "SELECT obj FROM ClienteEntity obj " +
                "WHERE UPPER(obj.nomefantasia) LIKE UPPER(CONCAT('%', :nomefantasia, '%'))";

        TypedQuery<ClienteEntity> query = entityManager.createQuery(jpql, ClienteEntity.class);
        query.setParameter("nomefantasia", nomefantasia);

        List<ClienteEntity> clientes = query.getResultList();

        // Mapeia a lista de ClienteEntity para ClienteDTO
        return clientes.stream()
                .filter(Objects::nonNull) // Filtra entidades nulas
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    public List<ClienteDTO> buscarClientesPorRazaoSocial(String razaosocial) {
        // Criação da consulta JPQL
        String jpql = "SELECT obj FROM ClienteEntity obj " +
                "WHERE UPPER(obj.razaosocial) LIKE UPPER(CONCAT('%', :razaosocial, '%'))";

        TypedQuery<ClienteEntity> query = entityManager.createQuery(jpql, ClienteEntity.class);
        query.setParameter("razaosocial", razaosocial);

        List<ClienteEntity> clientes = query.getResultList();

        // Mapeia a lista de ClienteEntity para ClienteDTO
        return clientes.stream()
                .filter(Objects::nonNull) // Filtra entidades nulas
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Long buscarUltimoCliente() {
        String jpql = "SELECT MAX(obj.codcli) FROM ClienteEntity obj";
        Long maiorId = entityManager.createQuery(jpql, Long.class).getSingleResult();
        return maiorId; // Retorna apenas o maior ID
    }

    @Transactional
    public ClienteEntity salvarCliente(ClienteEntity cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não pode ser nulo");
        }
        entityManager.persist(cliente);
        return cliente; // Retorna o cliente persistido
    }

    // ANTES DE CRIAR UM NOVO CLIENTE, PRECISA BUSCAR O ID DO ULTIMO CLIENTE INCLUIDO (FAZER ISTO NO FRONT)
//    public Response criarCliente(ClienteEntity cliente) {
//        try {
//            ClienteEntity clientePersistido = salvarCliente(cliente);
//            return Response.status(Response.Status.CREATED)
//                    .entity(clientePersistido)
//                    .build();
//        } catch (Exception e) {
//            e.printStackTrace(); // Loga a exceção
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                    .entity("Erro ao criar cliente: " + e.getMessage())
//                    .build();
 //       }
   // }


    @Transactional
    public ClienteEntity atualizarCliente(Long codcli, ClienteEntity clienteAtualizado) throws Exception {
        if (codcli == null || clienteAtualizado == null) {
            throw new IllegalArgumentException("Código do cliente e cliente não podem ser nulos");
        }

        // Busca o cliente existente
        ClienteEntity clienteExistente = entityManager.find(ClienteEntity.class, codcli);
        if (clienteExistente == null) {
            throw new Exception("Cliente não encontrado com o código: " + codcli);
        }

        // Atualiza os campos do cliente existente
        clienteExistente.setNomefantasia(clienteAtualizado.getNomefantasia());
        clienteExistente.setRazaosocial(clienteAtualizado.getRazaosocial());
        clienteExistente.setTipo(clienteAtualizado.getTipo());
        clienteExistente.setCpfcnpj(clienteAtualizado.getCpfcnpj());
        clienteExistente.setContato(clienteAtualizado.getContato());
        clienteExistente.setRuanum(clienteAtualizado.getRuanum());
        clienteExistente.setNum(clienteAtualizado.getNum());
        clienteExistente.setBairro(clienteAtualizado.getBairro());
        clienteExistente.setCidade(clienteAtualizado.getCidade());
        clienteExistente.setCep(clienteAtualizado.getCep());
        clienteExistente.setFonefixo(clienteAtualizado.getFonefixo());
        clienteExistente.setFone(clienteAtualizado.getFone());
        clienteExistente.setCelular2(clienteAtualizado.getCelular2());
        clienteExistente.setEmail(clienteAtualizado.getEmail());
        clienteExistente.setSegmento(clienteAtualizado.getSegmento());
        clienteExistente.setInscestadual(clienteAtualizado.getInscestadual());

        // O EntityManager irá automaticamente atualizar a entidade existente
        return clienteExistente; // Retorna o cliente atualizado
    }

    @Transactional
    public void deletarCliente(Long codcli) throws Exception {
        if (codcli == null) {
            throw new IllegalArgumentException("Código do cliente não pode ser nulo");
        }

        // Busca o cliente existente
        ClienteEntity clienteExistente = entityManager.find(ClienteEntity.class, codcli);
        if (clienteExistente == null) {
            throw new Exception("Cliente não encontrado com o código: " + codcli);
        }

        // Remove o cliente
        entityManager.remove(clienteExistente);
    }

    private ClienteDTO mapToDTO(ClienteEntity entity) {
        return ClienteDTO.builder()
                .id(entity.getCodcli())
                //.datacriacao(entity.getDatacriacao())
                .nomefantasia(entity.getNomefantasia())
                .razaosocial(entity.getRazaosocial())
                .tipo(entity.getTipo())
                .cpfcnpj(entity.getCpfcnpj())
                .contato(entity.getContato())
                .ruanum(entity.getRuanum())
                .num(entity.getNum())
                .bairro(entity.getBairro())
                .cidade(entity.getCidade())
                .cep(entity.getCep())
                .fonefixo(entity.getFonefixo())
                .fone(entity.getFone())
                .celular2(entity.getCelular2())
                .email(entity.getEmail())
                .segmento(entity.getSegmento())
                .inscestadual(entity.getInscestadual())
                .build();
    }
}

