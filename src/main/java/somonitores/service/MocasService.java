package somonitores.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import somonitores.dto.MocasDTO;
import somonitores.entity.MocasEntity;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class MocasService {

    private static final String UNIDADE_MAE = "ESTACA BETIM";

    @Inject
    EntityManager entityManager;

    @Transactional
    public List<MocasDTO> buscaTodasMocas() throws Exception {
        try {
            List<MocasEntity> mocasEntities = entityManager.createQuery(
                            "SELECT m FROM MocasEntity m ORDER BY m.unidade ASC, m.nome ASC",
                            MocasEntity.class)
                    .getResultList();

            if (mocasEntities.isEmpty()) {
                throw new Exception("Nenhuma moça encontrada");
            }

            return mocasEntities.stream()
                    .filter(Objects::nonNull)
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Erro ao buscar moças: " + e.getMessage());
        }
    }

    @Transactional
    public List<MocasDTO> buscaMocasPorUnidade(String unidade) throws Exception {
        String unidadeTratada = unidade.trim();

        // "Estaca Betim" é a unidade mãe (soma de todas as alas/ramos) e não existe como
        // linha própria na tabela — pedir por ela equivale a trazer todos os registros.
        if (UNIDADE_MAE.equalsIgnoreCase(unidadeTratada)) {
            return buscaTodasMocas();
        }

        String jpql = "SELECT m FROM MocasEntity m " +
                "WHERE UPPER(m.unidade) = UPPER(:unidade) " +
                "ORDER BY m.nome ASC";

        List<MocasEntity> mocasEntities = entityManager.createQuery(jpql, MocasEntity.class)
                .setParameter("unidade", unidadeTratada)
                .getResultList();

        return mocasEntities.stream()
                .filter(Objects::nonNull)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private MocasDTO mapToDTO(MocasEntity entity) {
        return MocasDTO.builder()
                .id(entity.getId())
                .unidade(entity.getUnidade())
                .nome(entity.getNome())
                .idade(entity.getIdade())
                .recomendacaoBatisterio(entity.getRecomendacaoBatisterio())
                .build();
    }
}
