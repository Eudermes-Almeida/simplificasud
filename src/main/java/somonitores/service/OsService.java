package somonitores.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import somonitores.dto.ClienteDTO;
import somonitores.dto.OsDTO;
import somonitores.entity.ClienteEntity;
import somonitores.entity.OsEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class OsService {
    @Inject
    EntityManager entityManager;

    @Transactional
    public OsDTO buscarOsPeloId(Long os) throws Exception {
        try {
            OsEntity osEntity = entityManager.find(OsEntity.class, os);

            if (osEntity == null) {
                return null;
            }

            return mapToDTO(osEntity);
        } catch (NoResultException e) {
            throw new Exception("Ordem de Servico não encontrada");
        }
    }

    @Transactional
    public List<OsDTO> buscarOsPeloCodcli(Long codcli) {
        if (codcli == null) {
            return Collections.emptyList();
        }

        String query = "SELECT obj FROM OsEntity obj WHERE obj.codcli = :codcli";
        List<OsEntity> osEntities = entityManager.createQuery(query, OsEntity.class)
                .setParameter("codcli", codcli)
                .getResultList();

        return osEntities.stream()
                .filter(Objects::nonNull) // Filtra entidades nulas
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<OsDTO> buscarOsPorRazaoSocial(String rzsocial) {
        // Criação da consulta JPQL
        String jpql = "SELECT obj FROM OsEntity obj " +
                "WHERE UPPER(obj.rzsocial) LIKE UPPER(CONCAT('%', :rzsocial, '%'))";

        TypedQuery<OsEntity> query = entityManager.createQuery(jpql, OsEntity.class);
        query.setParameter("rzsocial", rzsocial);

        List<OsEntity> ordens = query.getResultList();

        // Mapeia a lista de ClienteEntity para ClienteDTO
        return ordens.stream()
                .filter(Objects::nonNull) // Filtra entidades nulas
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<OsDTO> buscarOsPorNomeFantasia(String fantasia) {
        // Criação da consulta JPQL
        String jpql = "SELECT obj FROM OsEntity obj " +
                "WHERE UPPER(obj.fantasia) LIKE UPPER(CONCAT('%', :fantasia, '%'))";

        TypedQuery<OsEntity> query = entityManager.createQuery(jpql, OsEntity.class);
        query.setParameter("fantasia", fantasia);

        List<OsEntity> ordens = query.getResultList();

        // Mapeia a lista de ClienteEntity para ClienteDTO
        return ordens.stream()
                .filter(Objects::nonNull) // Filtra entidades nulas
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    @Transactional
    public List<OsDTO> buscarOsPelaDataEntrada(String dataString) {

        if (dataString == null || dataString.isEmpty()) {
            return Collections.emptyList();
        }

        // Definindo o formato da data que será recebido
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate data;

        try {
            // Convertendo a string para LocalDate
            data = LocalDate.parse(dataString, formatter);
        } catch (Exception e) {
            // Se a conversão falhar, retorna uma lista vazia
            return Collections.emptyList();
        }

        // Convertendo LocalDate para o formato 'YYYY-MM-DD' para a consulta
        String dataConsulta = data.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Consulta nativa SQL para buscar as ordens de serviço pela data
        String sql = "SELECT * FROM OS$ WHERE CONVERT(DATE, data, 103) = ?";
        List<OsEntity> osEntities = entityManager.createNativeQuery(sql, OsEntity.class)
                .setParameter(1, dataConsulta)
                .getResultList();

        return osEntities.stream()
                .filter(Objects::nonNull) // Filtra entidades nulas
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<OsDTO> buscarOsPelaDataFechamento(String fechamentoString) {

        if (fechamentoString == null || fechamentoString.isEmpty()) {
            return Collections.emptyList();
        }

        // Definindo o formato da data que será recebido
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate data;

        try {
            // Convertendo a string para LocalDate
            data = LocalDate.parse(fechamentoString, formatter);
        } catch (Exception e) {
            // Se a conversão falhar, retorna uma lista vazia
            return Collections.emptyList();
        }

        // Convertendo LocalDate para o formato 'YYYY-MM-DD' para a consulta
        String dataConsulta = data.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Consulta nativa SQL para buscar as ordens de serviço pela data
        String sql = "SELECT * FROM OS$ WHERE CONVERT(DATE, fechamento, 103) = ?";
        List<OsEntity> osEntities = entityManager.createNativeQuery(sql, OsEntity.class)
                .setParameter(1, dataConsulta)
                .getResultList();

        return osEntities.stream()
                .filter(Objects::nonNull) // Filtra entidades nulas
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<OsDTO> buscarUltimasOs(int limite) {

        if (limite <= 0 || limite > 1000) {
            return Collections.emptyList();
        }

        try {
            String query = "SELECT obj FROM OsEntity obj ORDER BY obj.codcli DESC";
            List<OsEntity> osEntities = entityManager.createQuery(query, OsEntity.class)
                    .setMaxResults(limite) // Define o limite diretamente na consulta
                    .getResultList();

            return osEntities.stream()
                    .filter(Objects::nonNull) // Filtra entidades nulas
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // Tratar exceção (log, rethrow, etc.)
            e.printStackTrace(); // Exemplo de tratamento simples
            return Collections.emptyList();
        }
    }

    public Long buscarUltimaOsCadastrada() {
        String jpql = "SELECT MAX(obj.os) FROM OsEntity obj";
        Long maiorId = entityManager.createQuery(jpql, Long.class).getSingleResult();
        return maiorId; // Retorna apenas o maior ID
    }
    public List<OsDTO> buscarOsPorSerial(String serial) {
        // Criação da consulta JPQL
        String jpql = "SELECT obj FROM OsEntity obj " +
                "WHERE UPPER(obj.serial) LIKE UPPER(CONCAT('%', :serial, '%'))";

        TypedQuery<OsEntity> query = entityManager.createQuery(jpql, OsEntity.class);
        query.setParameter("serial", serial);

        List<OsEntity> ordens = query.getResultList();

        // Mapeia a lista de ClienteEntity para ClienteDTO
        return ordens.stream()
                .filter(Objects::nonNull) // Filtra entidades nulas
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public OsEntity criarOs(OsEntity os) {
        if (os == null) {
            throw new IllegalArgumentException("Cliente não pode ser nulo");
        }
        entityManager.persist(os);
        return os; // Retorna o cliente persistido
    }

    @Transactional
    public OsEntity atualizarOs(Long os, OsEntity osAtualizado) throws Exception {
        if (os == null || osAtualizado == null) {
            throw new IllegalArgumentException("Código da ordem de servico não podem ser nulos");
        }

        // Busca a ordem de servico pelo id
        OsEntity osExistente = entityManager.find(OsEntity.class, os);
        if (osExistente == null) {
            throw new Exception("Ordem de servico não encontrada com o código: " + os);
        }

        // Atualiza os campos da ordem de servico existente:
        //OBSERVAÇÃO IMPORTANTE: NO PUT NÃO ATUALZA O ID
        osExistente.setData(osAtualizado.getData());
        osExistente.setHora(osAtualizado.getHora());
        osExistente.setFantasia(osAtualizado.getFantasia());
        osExistente.setRzsocial(osAtualizado.getRzsocial());
        osExistente.setCodcli(osAtualizado.getCodcli());
        osExistente.setAtendente(osAtualizado.getAtendente());
        osExistente.setStatus(osAtualizado.getStatus());
        osExistente.setPrioridade(osAtualizado.getPrioridade());
        osExistente.setEquipamento(osAtualizado.getEquipamento());
        osExistente.setMarca(osAtualizado.getMarca());
        osExistente.setModelo(osAtualizado.getModelo());
        osExistente.setTopologia(osAtualizado.getTopologia());
        osExistente.setCor(osAtualizado.getCor());
        osExistente.setSerial(osAtualizado.getSerial());
        osExistente.setPatrimonio(osAtualizado.getPatrimonio());
        osExistente.setObs1(osAtualizado.getObs1());
        osExistente.setObs2(osAtualizado.getObs2());
        osExistente.setDefeito1(osAtualizado.getDefeito1());
        osExistente.setDefeito2(osAtualizado.getDefeito2());
        osExistente.setDefeito3(osAtualizado.getDefeito3());
        osExistente.setTarefa1(osAtualizado.getTarefa1());
        osExistente.setTarefa2(osAtualizado.getTarefa2());
        osExistente.setTarefa3(osAtualizado.getTarefa3());
        osExistente.setBaterias(osAtualizado.getBaterias());
        osExistente.setMaterial2(osAtualizado.getMaterial2());
        osExistente.setMaterial3(osAtualizado.getMaterial3());
        osExistente.setOutrositens(osAtualizado.getOutrositens());
        osExistente.setQt1(osAtualizado.getQt1());
        osExistente.setQt2(osAtualizado.getQt2());
        osExistente.setQt3(osAtualizado.getQt3());
        osExistente.setQtoutrositens(osAtualizado.getQtoutrositens());
        osExistente.setQtdeslocamento(osAtualizado.getQtdeslocamento());
        osExistente.setValorunit1(osAtualizado.getValorunit1());
        osExistente.setValorunit2(osAtualizado.getValorunit2());
        osExistente.setValorunit3(osAtualizado.getValorunit3());
        osExistente.setValorunitoutrositens(osAtualizado.getValorunitoutrositens());
        osExistente.setValorunitariodeslocamento(osAtualizado.getValorunitariodeslocamento());
        osExistente.setValortotal1(osAtualizado.getValortotal1());
        osExistente.setValortotal2(osAtualizado.getValortotal2());
        osExistente.setValortotal3(osAtualizado.getValortotal3());
        osExistente.setValortotaloutrositens(osAtualizado.getValortotaloutrositens());
        osExistente.setValortotaldeslocamento(osAtualizado.getValortotaldeslocamento());
        osExistente.setValorassistencia(osAtualizado.getValorassistencia());
        osExistente.setTotalbruto(osAtualizado.getTotalbruto());
        osExistente.setValordesconto(osAtualizado.getValordesconto());
        osExistente.setDesconto(osAtualizado.getDesconto());
        osExistente.setTotaliq(osAtualizado.getTotaliq());
        osExistente.setFechamento(osAtualizado.getFechamento());
        osExistente.setTecnico(osAtualizado.getTecnico());
        osExistente.setGarantia(osAtualizado.getGarantia());
        osExistente.setDataorca(osAtualizado.getDataorca());
        osExistente.setOsoriginal(osAtualizado.getOsoriginal());
        osExistente.setVenda(osAtualizado.getVenda());
        osExistente.setModelofake(osAtualizado.getModelofake());
        osExistente.setObs3(osAtualizado.getObs3());
        osExistente.setJaimp(osAtualizado.getJaimp());
        osExistente.setTop10(osAtualizado.getTop10());
        osExistente.setDataretro(osAtualizado.getDataretro());

        // O EntityManager irá automaticamente atualizar a entidade existente
        return osExistente; // Retorna o cliente atualizado
    }

    @Transactional
    public void deletarOs(Long os) throws Exception {
        if (os == null) {
            throw new IllegalArgumentException("Numero da OS não pode ser nulo");
        }

        // Busca a Ordem de servico pelo Id
        OsEntity osExistente = entityManager.find(OsEntity.class, os);
        if (osExistente == null) {
            throw new Exception("Cliente não encontrado com o código: " + os);
        }

        // Remove o cliente
        entityManager.remove(osExistente);
    }
    
    private OsDTO mapToDTO(OsEntity entity) {
        return OsDTO.builder()
                .os(entity.getOs())
                .data(entity.getData())
                .hora(entity.getHora())
                .fantasia(entity.getFantasia())
                .rzsocial(entity.getRzsocial())
                .codcli(entity.getCodcli())
                .atendente(entity.getAtendente())
                .status(entity.getStatus())
                .prioridade(entity.getPrioridade())
                .equipamento(entity.getEquipamento())
                .marca(entity.getMarca())
                .modelo(entity.getModelo())
                .topologia(entity.getTopologia())
                .cor(entity.getCor())
                .serial(entity.getSerial())
                .patrimonio(entity.getPatrimonio())
                .obs1(entity.getObs1())
                .obs2(entity.getObs2())
                .defeito1(entity.getDefeito1())
                .defeito2(entity.getDefeito2())
                .defeito3(entity.getDefeito3())
                .tarefa1(entity.getTarefa1())
                .tarefa2(entity.getTarefa2())
                .tarefa3(entity.getTarefa3())
                .baterias(entity.getBaterias())
                .material2(entity.getMaterial2())
                .material3(entity.getMaterial3())
                .outrositens(entity.getOutrositens())
                .qt1(entity.getQt1())
                .qt2(entity.getQt2())
                .qt3(entity.getQt3())
                .qtoutrositens(entity.getQtoutrositens())
                .qtdeslocamento(entity.getQtdeslocamento())
                .valorunit1(entity.getValorunit1())
                .valorunit2(entity.getValorunit2())
                .valorunit3(entity.getValorunit3())
                .valorunitoutrositens(entity.getValorunitoutrositens())
                .valorunitariodeslocamento(entity.getValorunitariodeslocamento())
                .valortotal1(entity.getValortotal1())
                .valortotal2(entity.getValortotal2())
                .valortotal3(entity.getValortotal3())
                .valortotaloutrositens(entity.getValortotaloutrositens())
                .valortotaldeslocamento(entity.getValortotaldeslocamento())
                .valorassistencia(entity.getValorassistencia())
                .totalbruto(entity.getTotalbruto())
                .valordesconto(entity.getValordesconto())
                .desconto(entity.getDesconto())
                .totaliq(entity.getTotaliq())
                .fechamento(entity.getFechamento())
                .tecnico(entity.getTecnico())
                .garantia(entity.getGarantia())
                .dataorca(entity.getDataorca())
                .osoriginal(entity.getOsoriginal())
                .venda(entity.getVenda())
                .modelofake(entity.getModelofake())
                .obs3(entity.getObs3())
                .jaimp(entity.getJaimp())
                .top10(entity.getTop10())
                .dataretro(entity.getDataretro())
                .build();
    }

}
