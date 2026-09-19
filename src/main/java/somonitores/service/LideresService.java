package somonitores.service;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import somonitores.dto.LideresDTO;
import somonitores.dto.LiderAdminDTO;
import somonitores.entity.LideresEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class LideresService {

    private static final String UNIDADE_MAE = "ESTACA BETIM";

    @Inject
    EntityManager entityManager;

    @Transactional
    public List<LideresDTO> buscaTodosLideres() throws Exception {
        try {
            List<LideresEntity> lideresEntities = entityManager.createQuery(
                            "SELECT l FROM LideresEntity l ORDER BY l.unidade ASC, l.nome ASC",
                            LideresEntity.class)
                    .getResultList();

            if (lideresEntities.isEmpty()) {
                throw new Exception("Nenhum líder encontrado");
            }

            return lideresEntities.stream()
                    .filter(Objects::nonNull)
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Erro ao buscar líderes: " + e.getMessage());
        }
    }

    @Transactional
    public List<LideresDTO> buscaLideresPorUnidade(String unidade) throws Exception {
        String unidadeTratada = unidade.trim();

        // "Estaca Betim" é a única unidade que já existe como linha própria nesta tabela
        // (os 5 líderes de escopo Estaca), mas pedir por ela deve trazer TODOS os líderes
        // (inclusive os de escopo Ala), não só esses 5 — mesmo padrão das outras 10 tabelas.
        if (UNIDADE_MAE.equalsIgnoreCase(unidadeTratada)) {
            return buscaTodosLideres();
        }

        String jpql = "SELECT l FROM LideresEntity l " +
                "WHERE UPPER(l.unidade) = UPPER(:unidade) " +
                "ORDER BY l.nome ASC";

        List<LideresEntity> lideresEntities = entityManager.createQuery(jpql, LideresEntity.class)
                .setParameter("unidade", unidadeTratada)
                .getResultList();

        return lideresEntities.stream()
                .filter(Objects::nonNull)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // --- Suporte ao fluxo de autenticação (identificação/login) ---
    // Métodos abaixo trabalham direto com a Entity (nunca com o DTO "seguro" acima),
    // já que precisam ler/gravar registromembro e senha_hash — campos que o DTO
    // deliberadamente não expõe pela API de leitura.

    @Transactional
    public Optional<LideresEntity> buscaPorNascimentoERegistroMembro(LocalDate nascimento, String ultimosQuatroRegistro) {
        List<LideresEntity> candidatos = entityManager.createQuery(
                        "SELECT l FROM LideresEntity l WHERE l.nascimento = :nascimento",
                        LideresEntity.class)
                .setParameter("nascimento", nascimento)
                .getResultList();

        return candidatos.stream()
                .filter(l -> terminaCom(l.getRegistromembro(), ultimosQuatroRegistro))
                .findFirst();
    }

    @Transactional
    public Optional<LideresEntity> buscaLiderPorId(Long id) {
        return Optional.ofNullable(entityManager.find(LideresEntity.class, id));
    }

    @Transactional
    public Optional<LideresEntity> buscaPorLogin(String login) {
        return entityManager.createQuery(
                        "SELECT l FROM LideresEntity l WHERE UPPER(l.login) = UPPER(:login)",
                        LideresEntity.class)
                .setParameter("login", login.trim())
                .getResultList()
                .stream()
                .findFirst();
    }

    @Transactional
    public boolean loginJaExiste(String login) {
        Long total = entityManager.createQuery(
                        "SELECT COUNT(l) FROM LideresEntity l WHERE UPPER(l.login) = UPPER(:login)",
                        Long.class)
                .setParameter("login", login.trim())
                .getSingleResult();
        return total > 0;
    }

    // Único ponto do projeto que grava senha -- sempre em hash (BCrypt), nunca em texto
    // puro. Não valida duplicidade de login nem se o registro já estava cadastrado; isso
    // é responsabilidade do Resource, que decide o status HTTP de cada caso antes de chamar
    // este método.
    @Transactional
    public void cadastrarCredenciais(Long id, String login, String senhaPlana) {
        LideresEntity entity = entityManager.find(LideresEntity.class, id);
        entity.setLogin(login.trim());
        entity.setSenhaHash(BcryptUtil.bcryptHash(senhaPlana));
    }

    // --- CRUD administrativo de perfis (tela restrita ao escopo Master) ---
    // Só aqui a API grava nome/registromembro/unidade/escopo/nascimento/chamado -- os
    // outros pontos de escrita (cadastrarCredenciais acima) só tocam login/senha_hash.

    @Transactional
    public Optional<LiderAdminDTO> buscaAdminPorRegistroMembro(String registromembro) {
        return entityManager.createQuery(
                        "SELECT l FROM LideresEntity l WHERE l.registromembro = :registromembro",
                        LideresEntity.class)
                .setParameter("registromembro", registromembro.trim())
                .getResultList()
                .stream()
                .findFirst()
                .map(this::mapToAdminDTO);
    }

    // Usado na criação (idAtual=null) e na edição (idAtual = id do próprio registro, que
    // não deve contar como conflito consigo mesmo).
    @Transactional
    public boolean registroMembroEmUsoPorOutro(String registromembro, Long idAtual) {
        String jpql = "SELECT COUNT(l) FROM LideresEntity l WHERE l.registromembro = :registromembro"
                + (idAtual != null ? " AND l.id <> :idAtual" : "");
        var query = entityManager.createQuery(jpql, Long.class)
                .setParameter("registromembro", registromembro.trim());
        if (idAtual != null) {
            query.setParameter("idAtual", idAtual);
        }
        return query.getSingleResult() > 0;
    }

    @Transactional
    public LiderAdminDTO criarLider(LiderAdminDTO dto) {
        LideresEntity entity = LideresEntity.builder()
                .nome(dto.getNome().trim())
                .registromembro(dto.getRegistromembro().trim())
                .unidade(dto.getUnidade().trim())
                .escopo(dto.getEscopo().trim())
                .nascimento(dto.getNascimento())
                .chamado(dto.getChamado())
                .build();
        entityManager.persist(entity);
        return mapToAdminDTO(entity);
    }

    @Transactional
    public Optional<LiderAdminDTO> atualizarLider(Long id, LiderAdminDTO dto) {
        LideresEntity entity = entityManager.find(LideresEntity.class, id);
        if (entity == null) {
            return Optional.empty();
        }
        entity.setNome(dto.getNome().trim());
        entity.setRegistromembro(dto.getRegistromembro().trim());
        entity.setUnidade(dto.getUnidade().trim());
        entity.setEscopo(dto.getEscopo().trim());
        entity.setNascimento(dto.getNascimento());
        entity.setChamado(dto.getChamado());
        return Optional.of(mapToAdminDTO(entity));
    }

    // "Remover acesso": não apaga a linha (mantém histórico/nome/registromembro), só
    // esvazia o escopo. A coluna é NOT NULL, então usa string vazia como sentinela de
    // "sem acesso" -- AuthResource.login rejeita login quando o escopo está em branco.
    @Transactional
    public boolean revogarAcesso(Long id) {
        LideresEntity entity = entityManager.find(LideresEntity.class, id);
        if (entity == null) {
            return false;
        }
        entity.setEscopo("");
        return true;
    }

    private LiderAdminDTO mapToAdminDTO(LideresEntity entity) {
        return LiderAdminDTO.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .registromembro(entity.getRegistromembro())
                .unidade(entity.getUnidade())
                .escopo(entity.getEscopo())
                .nascimento(entity.getNascimento())
                .chamado(entity.getChamado())
                .build();
    }

    private boolean terminaCom(String registromembro, String ultimosQuatro) {
        if (registromembro == null || ultimosQuatro == null) {
            return false;
        }
        String valor = registromembro.trim();
        String sufixo = ultimosQuatro.trim();
        return valor.length() >= sufixo.length()
                && valor.substring(valor.length() - sufixo.length()).equalsIgnoreCase(sufixo);
    }

    private LideresDTO mapToDTO(LideresEntity entity) {
        return LideresDTO.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .unidade(entity.getUnidade())
                .escopo(entity.getEscopo())
                .chamado(entity.getChamado())
                .login(entity.getLogin())
                .build();
    }
}
