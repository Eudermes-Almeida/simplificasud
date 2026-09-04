package somonitores.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import somonitores.dto.MembrosAdultosSolteirosDTO;
import somonitores.entity.MembrosAdultosSolteirosEntity;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class MembrosAdultosSolteirosService {

    private static final String UNIDADE_MAE = "ESTACA BETIM";

    @Inject
    EntityManager entityManager;

    @Transactional
    public List<MembrosAdultosSolteirosDTO> buscaTodosMembrosAdultosSolteiros() throws Exception {
        try {
            List<MembrosAdultosSolteirosEntity> membrosAdultosSolteirosEntities = entityManager.createQuery(
                            "SELECT m FROM MembrosAdultosSolteirosEntity m ORDER BY m.unidade ASC, m.nome ASC",
                            MembrosAdultosSolteirosEntity.class)
                    .getResultList();

            if (membrosAdultosSolteirosEntities.isEmpty()) {
                throw new Exception("Nenhum membro adulto solteiro encontrado");
            }

            return membrosAdultosSolteirosEntities.stream()
                    .filter(Objects::nonNull)
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Erro ao buscar membros adultos solteiros: " + e.getMessage());
        }
    }

    @Transactional
    public List<MembrosAdultosSolteirosDTO> buscaMembrosAdultosSolteirosPorUnidade(String unidade) throws Exception {
        String unidadeTratada = unidade.trim();

        // "Estaca Betim" é a unidade mãe (soma de todas as alas/ramos) e não existe como
        // linha própria na tabela — pedir por ela equivale a trazer todos os registros.
        if (UNIDADE_MAE.equalsIgnoreCase(unidadeTratada)) {
            return buscaTodosMembrosAdultosSolteiros();
        }

        String jpql = "SELECT m FROM MembrosAdultosSolteirosEntity m " +
                "WHERE UPPER(m.unidade) = UPPER(:unidade) " +
                "ORDER BY m.nome ASC";

        List<MembrosAdultosSolteirosEntity> membrosAdultosSolteirosEntities = entityManager.createQuery(jpql, MembrosAdultosSolteirosEntity.class)
                .setParameter("unidade", unidadeTratada)
                .getResultList();

        return membrosAdultosSolteirosEntities.stream()
                .filter(Objects::nonNull)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private MembrosAdultosSolteirosDTO mapToDTO(MembrosAdultosSolteirosEntity entity) {
        return MembrosAdultosSolteirosDTO.builder()
                .id(entity.getId())
                .unidade(entity.getUnidade())
                .nome(entity.getNome())
                .sexo(entity.getSexo())
                .idade(entity.getIdade())
                .estadocivil(entity.getEstadocivil())
                .recomendacaotemplo(entity.getRecomendacaotemplo())
                .paismissao(entity.getPaismissao())
                .chamados(entity.getChamados())
                .build();
    }
}
