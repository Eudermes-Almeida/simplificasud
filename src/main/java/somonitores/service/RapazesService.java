package somonitores.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import somonitores.dto.RapazesDTO;
import somonitores.entity.RapazesEntity;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class RapazesService {

    private static final String UNIDADE_MAE = "ESTACA BETIM";

    @Inject
    EntityManager entityManager;

    @Transactional
    public List<RapazesDTO> buscaTodosRapazes() throws Exception {
        try {
            List<RapazesEntity> rapazesEntities = entityManager.createQuery(
                            "SELECT r FROM RapazesEntity r ORDER BY r.unidade ASC, r.nome ASC",
                            RapazesEntity.class)
                    .getResultList();

            if (rapazesEntities.isEmpty()) {
                throw new Exception("Nenhum rapaz encontrado");
            }

            return rapazesEntities.stream()
                    .filter(Objects::nonNull)
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Erro ao buscar rapazes: " + e.getMessage());
        }
    }

    @Transactional
    public List<RapazesDTO> buscaRapazesPorUnidade(String unidade) throws Exception {
        String unidadeTratada = unidade.trim();

        // "Estaca Betim" é a unidade mãe (soma de todas as alas/ramos) e não existe como
        // linha própria na tabela — pedir por ela equivale a trazer todos os registros.
        if (UNIDADE_MAE.equalsIgnoreCase(unidadeTratada)) {
            return buscaTodosRapazes();
        }

        String jpql = "SELECT r FROM RapazesEntity r " +
                "WHERE UPPER(r.unidade) = UPPER(:unidade) " +
                "ORDER BY r.nome ASC";

        List<RapazesEntity> rapazesEntities = entityManager.createQuery(jpql, RapazesEntity.class)
                .setParameter("unidade", unidadeTratada)
                .getResultList();

        return rapazesEntities.stream()
                .filter(Objects::nonNull)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private RapazesDTO mapToDTO(RapazesEntity entity) {
        return RapazesDTO.builder()
                .id(entity.getId())
                .unidade(entity.getUnidade())
                .nome(entity.getNome())
                .idade(entity.getIdade())
                .sacerdocio(entity.getSacerdocio())
                .build();
    }
}
