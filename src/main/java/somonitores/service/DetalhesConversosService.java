package somonitores.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import somonitores.dto.DetalhesConversosDTO;
import somonitores.entity.DetalhesConversosEntity;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class DetalhesConversosService {

    private static final String UNIDADE_MAE = "ESTACA BETIM";

    @Inject
    EntityManager entityManager;

    @Transactional
    public List<DetalhesConversosDTO> buscaTodosDetalhesConversos() throws Exception {
        try {
            List<DetalhesConversosEntity> detalhesEntities = entityManager.createQuery(
                            "SELECT d FROM DetalhesConversosEntity d ORDER BY d.unidade ASC, d.nome ASC",
                            DetalhesConversosEntity.class)
                    .getResultList();

            if (detalhesEntities.isEmpty()) {
                throw new Exception("Nenhum detalhe de converso encontrado");
            }

            return detalhesEntities.stream()
                    .filter(Objects::nonNull)
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Erro ao buscar detalhes de conversos: " + e.getMessage());
        }
    }

    @Transactional
    public List<DetalhesConversosDTO> buscaDetalhesConversosPorUnidade(String unidade) throws Exception {
        String unidadeTratada = unidade.trim();

        // "Estaca Betim" é a unidade mãe (soma de todas as alas/ramos) e não existe como
        // linha própria na tabela — pedir por ela equivale a trazer todos os registros.
        if (UNIDADE_MAE.equalsIgnoreCase(unidadeTratada)) {
            return buscaTodosDetalhesConversos();
        }

        String jpql = "SELECT d FROM DetalhesConversosEntity d " +
                "WHERE UPPER(d.unidade) = UPPER(:unidade) " +
                "ORDER BY d.nome ASC";

        List<DetalhesConversosEntity> detalhesEntities = entityManager.createQuery(jpql, DetalhesConversosEntity.class)
                .setParameter("unidade", unidadeTratada)
                .getResultList();

        return detalhesEntities.stream()
                .filter(Objects::nonNull)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private DetalhesConversosDTO mapToDTO(DetalhesConversosEntity entity) {
        return DetalhesConversosDTO.builder()
                .id(entity.getId())
                .unidade(entity.getUnidade())
                .nome(entity.getNome())
                .sexo(entity.getSexo())
                .idade(entity.getIdade())
                .ativo(entity.getAtivo())
                .temChamado(entity.getTemChamado())
                .ministradora(entity.getMinistradora())
                .ministrador(entity.getMinistrador())
                .recomendacao(entity.getRecomendacao())
                .sacerdocio(entity.getSacerdocio())
                .build();
    }
}
