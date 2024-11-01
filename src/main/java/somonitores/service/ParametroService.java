package somonitores.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import somonitores.dto.ClienteDTO;
import somonitores.dto.ParametroDTO;
import somonitores.entity.ClienteEntity;
import somonitores.entity.OsEntity;
import somonitores.entity.ParametroEntity;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class ParametroService {

    @Inject
    EntityManager entityManager;

    @Transactional
    public List<ParametroDTO> buscaTodosParametros() throws Exception {
        try {

            List<ParametroEntity> parametroEntities = entityManager.createQuery("SELECT c FROM ParametroEntity c ORDER BY c.id DESC", ParametroEntity.class).getResultList();

            if (parametroEntities.isEmpty()) {

                throw new Exception("Nenhum parametro encontrado");
            }

            return parametroEntities.stream()
                    .filter(Objects::nonNull) // Filtra entidades nulas
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Erro ao buscar parametros: " + e.getMessage());
        }
    }




    @Transactional
    public List<ParametroDTO> buscaParametroPorNome(String parametro) {
        if (parametro == null || parametro.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String sql = null;

        System.out.println(parametro + "**********************************************");

        if (parametro.equals("CIDADE")) {
            sql = "SELECT * FROM PARAMETROS WHERE parametro = 'CIDADE'";
        }

        if (parametro.equals("SEGMENTO")) {
            sql = "SELECT * FROM PARAMETROS WHERE parametro = 'SEGMENTO'";
        }

        if (parametro.equals("ATENDENTE")) {
            sql = "SELECT * FROM PARAMETROS WHERE parametro = 'ATENDENTES'";
        }

        if (parametro.equals("DEFEITOCLIENTE")) {
            sql = "SELECT * FROM PARAMETROS WHERE parametro = 'DEFEITOS CLIENTES'";
        }

        if (parametro.equals("EQUIPAMENTO")) {
            sql = "SELECT * FROM PARAMETROS WHERE parametro = 'EQUIPAMENTO'";
        }

        if (parametro.equals("MARCANOBREAK")) {
            sql = "SELECT * FROM PARAMETROS WHERE parametro = 'MARCA NOBREAK'";
        }

        if (parametro.equals("MODELONOBREAK")) {
            sql = "SELECT * FROM PARAMETROS WHERE parametro = 'MODELO NOBREAK'";
        }

        if (parametro.equals("TOPOLOGIANOBREAK")) {
            sql = "SELECT * FROM PARAMETROS WHERE parametro = 'TOPOLOGIA NOBREAK'";
        }

        if (parametro.equals("MARCAMONITOR")) {
            sql = "SELECT * FROM PARAMETROS WHERE parametro = 'MARCA MONITOR'";
        }

        if (parametro.equals("MODELOMONITOR")) {
            sql = "SELECT * FROM PARAMETROS WHERE parametro = 'MODELO MONITOR'";
        }

        if (parametro.equals("TOPOLOGIAMONITOR")) {
            sql = "SELECT * FROM PARAMETROS WHERE parametro = 'TOPOLOGIA MONITOR'";
        }

        if (parametro.equals("MARCABATERIA")) {
            sql = "SELECT * FROM PARAMETROS WHERE parametro = 'MARCA BATERIA'";
        }

        if (parametro.equals("MODELOBATERIA")) {
            sql = "SELECT * FROM PARAMETROS WHERE parametro = 'MODELO BATERIA'";
        }

        if (parametro.equals("TOPOLOGIABATERIA")) {
            sql = "SELECT * FROM PARAMETROS WHERE parametro = 'TOPOLOGIA BATERIA'";
        }

        if (parametro.equals("MARCAESTABILIZADOR")) {
            sql = "SELECT * FROM PARAMETROS WHERE parametro = 'MARCA ESTABILIZADOR'";
        }

        if (parametro.equals("MODELOESTABILIZADOR")) {
            sql = "SELECT * FROM PARAMETROS WHERE parametro = 'MODELO ESTABILIZADOR'";
        }

        if (parametro.equals("TOPOLOGIAESTABILIZADOR")) {
            sql = "SELECT * FROM PARAMETROS WHERE parametro = 'TOPOLOGIA ESTABILIZADOR'";
        }

        if (parametro.equals("MARCAACESSORIO")) {
            sql = "SELECT * FROM PARAMETROS WHERE parametro = 'MARCA ACESSÓRIO'";
        }

        if (parametro.equals("MODELOACESSORIO")) {
            sql = "SELECT * FROM PARAMETROS WHERE parametro = 'MODELO ACESSORIO'";
        }

        if (parametro.equals("TOPOLOGIAACESSORIO")) {
            sql = "SELECT * FROM PARAMETROS WHERE parametro = 'TOPOLOGIA ACESSORIO'";
        }

        if (parametro.equals("COR")) {
            sql = "SELECT * FROM PARAMETROS WHERE parametro = 'COR'";
        }

        if (parametro.equals("STATUS")) {
            sql = "SELECT * FROM PARAMETROS WHERE parametro = 'STATUS'";
        }

        if (parametro.equals("TECNICO")) {
            sql = "SELECT * FROM PARAMETROS WHERE parametro = 'TECNICO'";
        }

        if (parametro.equals("TAREFAEXECUTADA")) {
            sql = "SELECT * FROM PARAMETROS WHERE parametro = 'TAREFA EXECUTADA'";
        }

        if (parametro.equals("BATERIA")) {
            sql = "SELECT * FROM PARAMETROS WHERE parametro = 'BATERIAS'";
        }

        if (parametro.equals("MATERIAL")) {
            sql = "SELECT * FROM PARAMETROS WHERE parametro = 'MATERIAIS'";
        }

        // Executando a consulta SQL nativa
        List<ParametroEntity> parametroEntities = entityManager.createNativeQuery(sql, ParametroEntity.class)
                .getResultList();

        // Mapeando as entidades para DTO
        return parametroEntities.stream()
                .filter(Objects::nonNull) // Filtra entidades nulas
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ParametroEntity criarParametro(ParametroEntity parametro) {
        if (parametro == null) {
            throw new IllegalArgumentException("Cliente não pode ser nulo");
        }
        entityManager.persist(parametro);
        return parametro; // Retorna o cliente persistido
    }

    @Transactional
    public void deletarParametro(Long id) throws Exception {
        if (id == null) {
            throw new IllegalArgumentException("Código do parametro não pode ser nulo");
        }
        // Busca o cliente existente
        ParametroEntity parametroExistente = entityManager.find(ParametroEntity.class, id);
        if (parametroExistente == null) {
            throw new Exception("Parametro não encontrado com o código: " + id);
        }

        entityManager.remove(parametroExistente);
    }

    @Transactional
    public ParametroEntity atualizarParametro(Long id, ParametroEntity parametroAtualizado) throws Exception {
        if (id == null || parametroAtualizado == null) {
            throw new IllegalArgumentException("Código do paramentro e parametro não podem ser nulos");
        }

        // Busca o parametro existente
        ParametroEntity parametroexistente = entityManager.find(ParametroEntity.class, id);
        if (parametroexistente == null) {
            throw new Exception("Parametro não encontrado com o código: " + id);
        }
        // Atualiza os campos do cliente existente
        parametroexistente.setParametro(parametroAtualizado.getParametro());
        parametroexistente.setValor(parametroAtualizado.getValor());

        // O EntityManager irá automaticamente atualizar a entidade existente
        return parametroexistente; // Retorna o cliente atualizado
    }


    private ParametroDTO mapToDTO(ParametroEntity entity) {
        return ParametroDTO.builder()
                .id(entity.getId())
                .parametro(entity.getParametro())
                .valor(entity.getValor())
                .build();
    }
}

