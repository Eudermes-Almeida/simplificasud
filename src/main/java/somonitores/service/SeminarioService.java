package somonitores.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import somonitores.dto.SeminarioDTO;
import somonitores.entity.SeminarioEntity;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class SeminarioService {

    private static final String UNIDADE_MAE = "ESTACA BETIM";

    @Inject
    EntityManager entityManager;

    @Transactional
    public List<SeminarioDTO> buscaTodosSeminario() throws Exception {
        try {
            List<SeminarioEntity> seminarioEntities = entityManager.createQuery(
                            "SELECT s FROM SeminarioEntity s ORDER BY s.unidade ASC, s.nome ASC",
                            SeminarioEntity.class)
                    .getResultList();

            if (seminarioEntities.isEmpty()) {
                throw new Exception("Nenhum registro de seminário encontrado");
            }

            return seminarioEntities.stream()
                    .filter(Objects::nonNull)
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Erro ao buscar registros de seminário: " + e.getMessage());
        }
    }

    @Transactional
    public List<SeminarioDTO> buscaSeminarioPorUnidade(String unidade) throws Exception {
        String unidadeTratada = unidade.trim();

        // "Estaca Betim" é a unidade mãe (soma de todas as alas/ramos) e não existe como
        // linha própria na tabela — pedir por ela equivale a trazer todos os registros.
        if (UNIDADE_MAE.equalsIgnoreCase(unidadeTratada)) {
            return buscaTodosSeminario();
        }

        String jpql = "SELECT s FROM SeminarioEntity s " +
                "WHERE UPPER(s.unidade) = UPPER(:unidade) " +
                "ORDER BY s.nome ASC";

        List<SeminarioEntity> seminarioEntities = entityManager.createQuery(jpql, SeminarioEntity.class)
                .setParameter("unidade", unidadeTratada)
                .getResultList();

        return seminarioEntities.stream()
                .filter(Objects::nonNull)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private SeminarioDTO mapToDTO(SeminarioEntity entity) {
        return SeminarioDTO.builder()
                .id(entity.getId())
                .unidade(entity.getUnidade())
                .nome(entity.getNome())
                .sexo(entity.getSexo())
                .idade(entity.getIdade())
                .percentualFrequencia(entity.getPercentualFrequencia())
                .dataUltimaPresenca(entity.getDataUltimaPresenca())
                .build();
    }
}
