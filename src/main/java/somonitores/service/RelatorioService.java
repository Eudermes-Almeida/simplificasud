package somonitores.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import somonitores.dto.ComissaoDTO;
import somonitores.dto.OsDTO;
import somonitores.dto.SomatoriaDTO;
import somonitores.entity.OsEntity;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class RelatorioService {

    @Inject
    EntityManager entityManager;


    @Transactional
    public List<OsDTO> faturamentoEmIntervalo(String dataInicioString, String dataFimString, String busca) {
        if (dataInicioString == null || dataFimString == null || dataInicioString.isEmpty() || dataFimString.isEmpty()) {
            return Collections.emptyList();
        }

        String sql = null;

        if (busca.equals("faturamentoTotal")) {
            sql = "SELECT * FROM OS$ " +
                    "WHERE fechamento BETWEEN CONVERT(datetime, ?, 120) AND CONVERT(datetime, ?, 120) " +
                    "ORDER BY fechamento DESC";
        }

        if (busca.equals("faturamentoNobreak")) {
            sql = "SELECT * FROM OS$ o " +
                    "WHERE fechamento BETWEEN CONVERT(datetime, ?, 120) AND CONVERT(datetime, ?, 120) " +
                    "AND EQUIPAMENTO = 'NOBREAK' " +
                    "ORDER BY fechamento DESC";
        }

        if (busca.equals("faturamentoManutencaoNobreak")) {
            sql = "SELECT * FROM OS$ o " +
                    "WHERE fechamento BETWEEN CONVERT(datetime, ?, 120) AND CONVERT(datetime, ?, 120) " +
                    "AND EQUIPAMENTO = 'NOBREAK' " + "AND VENDA = 'NÃO' " +
                    "ORDER BY fechamento DESC";
        }

        if (busca.equals("faturamentoManutencaoNobreakAbaixo3kva")) {
            sql = "SELECT * FROM OS$ o " +
                    "WHERE fechamento BETWEEN CONVERT(datetime, ?, 120) AND CONVERT(datetime, ?, 120) " +
                    "AND EQUIPAMENTO = 'NOBREAK' " +
                    "AND VENDA = 'NÃO' " +
                    "AND (MODELO = '500 VA' OR MODELO = '600 VA' OR MODELO = '700 VA' " +
                    "OR MODELO = '800 VA' OR MODELO = '1 KVA' OR MODELO = '1,2 KVA' " +
                    "OR MODELO = '1,3 KVA' OR MODELO = '1,4 KVA' OR MODELO = '1,5 KVA' " +
                    "OR MODELO = '1,8 KVA' OR MODELO = '2 KVA' OR MODELO = '2,2 KVA' " +
                    "OR MODELO = '2,4 KVA' OR MODELO = '3 KVA' OR MODELO = '3,2 KVA' " +
                    "OR MODELO = '3,5 KVA' OR MODELO = '3/4 HP' OR MODELO = '1/2 HP' " +
                    "OR MODELO = '3,200 KVA' OR MODELO = '600 KVA')" +
                    "ORDER BY fechamento DESC";
        }

        if (busca.equals("faturamentoManutencaoNobreakAcima3kva")) {
            sql = "SELECT * FROM OS$ o " +
                    "WHERE fechamento BETWEEN CONVERT(datetime, ?, 120) AND CONVERT(datetime, ?, 120) " +
                    "AND EQUIPAMENTO = 'NOBREAK' " +
                    "AND VENDA = 'NÃO' " +
                    "AND (MODELO = '4 kVA' " +
                    "OR MODELO = '5 kVA' OR MODELO = '6 KVA' OR MODELO = '8 KVA' " +
                    "OR MODELO = '10 KVA' OR MODELO = '12 KVA' OR MODELO = '15 KVA' " +
                    "OR MODELO = '20 KVA' OR MODELO = '30 KVA')" +
                    "ORDER BY fechamento DESC";
        }

        if (busca.equals("vendaNobreakNovo")) {
            sql = "SELECT * FROM OS$ o " +
                    "WHERE fechamento BETWEEN CONVERT(datetime, ?, 120) AND CONVERT(datetime, ?, 120) " +
                    "AND EQUIPAMENTO = 'NOBREAK' " +
                    "AND VENDA = 'NOVO' " +
                    "ORDER BY fechamento DESC";
        }

        if (busca.equals("vendaNobreakSemiNovo")) {
            sql = "SELECT * FROM OS$ o " +
                    "WHERE fechamento BETWEEN CONVERT(datetime, ?, 120) AND CONVERT(datetime, ?, 120) " +
                    "AND EQUIPAMENTO = 'NOBREAK' " +
                    "AND VENDA = 'SEMI NOVO' " +
                    "ORDER BY fechamento DESC";
        }

        if (busca.equals("nobreakInviavel")) {
            sql = "SELECT * FROM OS$ o " +
                    "WHERE fechamento BETWEEN CONVERT(datetime, ?, 120) AND CONVERT(datetime, ?, 120) " +
                    "AND EQUIPAMENTO = 'NOBREAK' " +
                    "AND TAREFA1 = 'INVIÁVEL' " +
                    "ORDER BY fechamento DESC";
        }

        if (busca.equals("nobreakOrcamentoNaoAprovado")) {
            sql = "SELECT * FROM OS$ o " +
                    "WHERE fechamento BETWEEN CONVERT(datetime, ?, 120) AND CONVERT(datetime, ?, 120) " +
                    "AND EQUIPAMENTO = 'NOBREAK' " +
                    "AND TAREFA1 = 'ORC NAO APROVADO' " +
                    "ORDER BY fechamento DESC";
        }

        if (busca.equals("nobreakGarantiaLoja")) {
            sql = "SELECT * FROM OS$ o " +
                    "WHERE fechamento BETWEEN CONVERT(datetime, ?, 120) AND CONVERT(datetime, ?, 120) " +
                    "AND EQUIPAMENTO = 'NOBREAK' " +
                    "AND GARANTIA = 'LOJA' " +
                    "ORDER BY fechamento DESC";
        }

        if (busca.equals("nobreakGarantiaFabrica")) {
            sql = "SELECT * FROM OS$ o " +
                    "WHERE fechamento BETWEEN CONVERT(datetime, ?, 120) AND CONVERT(datetime, ?, 120) " +
                    "AND EQUIPAMENTO = 'NOBREAK' " +
                    "AND GARANTIA = 'FABRICA' " +
                    "ORDER BY fechamento DESC";
        }

        if (busca.equals("vendaBateriaAvulsa")) {

            System.out.println("*********************entrou no if venda bateria avulsa**************");
            sql = "SELECT * FROM OS$ o " +
                    "WHERE fechamento BETWEEN CONVERT(datetime, ?, 120) AND CONVERT(datetime, ?, 120) " +
                    "AND EQUIPAMENTO = 'BATERIA' " +
                    "AND TAREFA1 = 'VENDA BATERIA' " +
                    "ORDER BY fechamento DESC";
        }

        if (busca.equals("vendaBateriaAvulsa")) {
            sql = "SELECT * FROM OS$ o " +
                    "WHERE fechamento BETWEEN CONVERT(datetime, ?, 120) AND CONVERT(datetime, ?, 120) " +
                    //"AND EQUIPAMENTO = 'BATERIA' " +
                    "AND TAREFA1 = 'VENDA BATERIA' " +
                    "ORDER BY fechamento DESC";
        }

        if (busca.equals("vendaSucataBateria")) {
            sql = "SELECT * FROM OS$ o " +
                    "WHERE fechamento BETWEEN CONVERT(datetime, ?, 120) AND CONVERT(datetime, ?, 120) " +
                    //"AND EQUIPAMENTO = 'BATERIA' " +
                    "AND TAREFA1 = 'VENDA SUCATA BATERIA' " +
                    "ORDER BY fechamento DESC";
        }

        if (busca.equals("faturamentoMonitor")) {
            sql = "SELECT * FROM OS$ o " +
                    "WHERE fechamento BETWEEN CONVERT(datetime, ?, 120) AND CONVERT(datetime, ?, 120) " +
                    "AND EQUIPAMENTO = 'MONITOR' " +
                    "ORDER BY fechamento DESC";
        }

        if (busca.equals("faturamentoManutencaoMonitor")) {
            sql = "SELECT * FROM OS$ o " +
                    "WHERE fechamento BETWEEN CONVERT(datetime, ?, 120) AND CONVERT(datetime, ?, 120) " +
                    "AND EQUIPAMENTO = 'MONITOR' " +
                    "ORDER BY fechamento DESC";
        }

        if (busca.equals("vendaMonitorSemiNovo")) {
            sql = "SELECT * FROM OS$ o " +
                    "WHERE fechamento BETWEEN CONVERT(datetime, ?, 120) AND CONVERT(datetime, ?, 120) " +
                    "AND EQUIPAMENTO = 'MONITOR' " +
                    "AND VENDA = 'SEMI NOVO' " +
                    "ORDER BY fechamento DESC";
        }

        if (busca.equals("monitorInviavel")) {
            sql = "SELECT * FROM OS$ o " +
                    "WHERE fechamento BETWEEN CONVERT(datetime, ?, 120) AND CONVERT(datetime, ?, 120) " +
                    "AND EQUIPAMENTO = 'MONITOR' " +
                    "AND TAREFA1 = 'INVIÁVEL' " +
                    "ORDER BY fechamento DESC";
        }

        if (busca.equals("monitorOrcamentoNaoAprovado")) {
            sql = "SELECT * FROM OS$ o " +
                    "WHERE fechamento BETWEEN CONVERT(datetime, ?, 120) AND CONVERT(datetime, ?, 120) " +
                    "AND EQUIPAMENTO = 'MONITOR' " +
                    "AND TAREFA1 = 'ORC NAO APROVADO' " +
                    "ORDER BY fechamento DESC";
        }

        if (busca.equals("monitorGarantiaLoja")) {
            sql = "SELECT * FROM OS$ o " +
                    "WHERE fechamento BETWEEN CONVERT(datetime, ?, 120) AND CONVERT(datetime, ?, 120) " +
                    "AND EQUIPAMENTO = 'MONITOR' " +
                    "AND GARANTIA = 'LOJA' " +
                    "ORDER BY fechamento DESC";
        }

        if (busca.equals("faturamentoOutroEquipamento")) {
            sql = "SELECT * FROM OS$ o " +
                    "WHERE fechamento BETWEEN CONVERT(datetime, ?, 120) AND CONVERT(datetime, ?, 120) " +
                    "AND EQUIPAMENTO NOT IN ('MONITOR', 'NOBREAK', 'BATERIA', 'SUCATA BATERIA', 'BATERIAS USADAS', " +
                    "'BANCO BATERIAS', 'BANCO DE BATERIAS', 'MODULO BATERIA', 'MODULO DE BATERIA') " +
                    "ORDER BY fechamento DESC";

        }

        if (busca.equals("entradaEquipamento")) {
            sql = "SELECT * FROM OS$ o " +
                    "WHERE data BETWEEN CONVERT(datetime, ?, 120) AND CONVERT(datetime, ?, 120) " +
                    "ORDER BY o.os DESC";
        }

        if (busca.equals("orcamentoNoPeriodo")) {
            sql = "SELECT * FROM OS$ o " +
                    "WHERE dataorca BETWEEN CONVERT(datetime, ?, 120) AND CONVERT(datetime, ?, 120) "+
                    "ORDER BY o.os DESC";

        }

        if (busca.equals("comissaoTecnicos")) {
            sql = "SELECT * FROM OS$ o " +
                    "WHERE dataorca BETWEEN CONVERT(datetime, ?, 120) AND CONVERT(datetime, ?, 120) ";

        }


        if (busca.equals("tecnicoEudermes")) {
            sql = "SELECT * FROM OS$ o " +
                    "WHERE fechamento BETWEEN CONVERT(datetime, ?, 120) AND CONVERT(datetime, ?, 120) " +
                    "AND TECNICO = 'EUDERMES' " +
                    "AND STATUS = 'FECHADA' " +
                    "ORDER BY fechamento DESC";
        }

        if (busca.equals("tecnicoVinicius")) {
            sql = "SELECT * FROM OS$ o " +
                    "WHERE fechamento BETWEEN CONVERT(datetime, ?, 120) AND CONVERT(datetime, ?, 120) " +
                    "AND TECNICO = 'VINÍCIUS' " +
                    "AND STATUS = 'FECHADA' " +
                    "ORDER BY fechamento DESC";
        }

        if (busca.equals("tecnicoLeonardo")) {
            sql = "SELECT * FROM OS$ o " +
                    "WHERE fechamento BETWEEN CONVERT(datetime, ?, 120) AND CONVERT(datetime, ?, 120) " +
                    "AND TECNICO = 'LEONARDO' " +
                    "AND STATUS = 'FECHADA' " +
                    "ORDER BY fechamento DESC";
        }

        if (busca.equals("tecnicoRafael")) {
            sql = "SELECT * FROM OS$ o " +
                    "WHERE fechamento BETWEEN CONVERT(datetime, ?, 120) AND CONVERT(datetime, ?, 120) " +
                    "AND TECNICO = 'RAFAEL' " +
                    "AND STATUS = 'FECHADA' " +
                    "ORDER BY fechamento DESC";
        }

        if (busca.equals("tecnicoGabriel")) {
            sql = "SELECT * FROM OS$ o " +
                    "WHERE fechamento BETWEEN CONVERT(datetime, ?, 120) AND CONVERT(datetime, ?, 120) " +
                    "AND TECNICO = 'GABRIEL' " +
                    "AND STATUS = 'FECHADA' " +
                    "ORDER BY fechamento DESC";
        }

        List<OsEntity> osEntities = entityManager.createNativeQuery(sql, OsEntity.class)
                .setParameter(1, dataInicioString)
                .setParameter(2, dataFimString)
                .getResultList();

        // Somando os valores da coluna TOTALIQ
        BigDecimal totalIQ = osEntities.stream()
                .filter(Objects::nonNull) // Filtra entidades nulas
                .map(osEntity -> {
                    // Converte o valor de TOTALIQ de varchar para BigDecimal
                    String totalIQString = osEntity.getTotaliq();
                    if (totalIQString != null) {
                        // Remove espaços em branco e substitui vírgulas por pontos
                        totalIQString = totalIQString.trim().replace(",", ".");
                        try {
                            return new BigDecimal(totalIQString);
                        } catch (NumberFormatException e) {
                            // Se a conversão falhar, retorna BigDecimal.ZERO
                            return BigDecimal.ZERO;
                        }
                    }
                    return BigDecimal.ZERO; // Retorna zero se a string for nula
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Soma os valores

        // pegando a quantidade de ordens de servicos
        int qtOs = osEntities.size();

        // Mapeando as entidades para DTO e incluindo totalIQSum
        List<OsDTO> osDTOs = osEntities.stream()
                .filter(Objects::nonNull) // Filtra entidades nulas
                .map(entity -> mapToDTO(entity, totalIQ, qtOs)) // Passa totalIQSum
                .collect(Collectors.toList());

        return osDTOs;
    }

    @Transactional
    public List<OsDTO> buscaOsPorStatus(String status) {
        if (status == null) {
            return Collections.emptyList();
        }

        String sql = null;

        if (status.equals("aberta")) {
            sql = "SELECT * FROM OS$ " +
                    "WHERE status = 'ABERTA' OR status = 'EUDERMES' OR status = 'GARRADO'"  +
                    "ORDER BY OS DESC";
        }

        if (status.equals("passarOrcamento")) {
            sql = "SELECT * FROM OS$ " +
                    "WHERE status = 'PASSAR ORÇ'"  +
                    "ORDER BY OS DESC";
        }

        if (status.equals("aguardandoAprovacao")) {
            sql = "SELECT * FROM OS$ " +
                    "WHERE status = 'AGUARDANDO APROV'"  +
                    "ORDER BY OS DESC";
        }

        if (status.equals("aprovadas")) {
            sql = "SELECT * FROM OS$ " +
                    "WHERE status = 'APROVADO ORÇ'"  +
                    "ORDER BY OS DESC";
        }

        if (status.equals("naoAprovadas")) {
            sql = "SELECT * FROM OS$ " +
                    "WHERE status = 'NÃO APROVADO'"  +
                    "ORDER BY OS DESC";
        }
        if (status.equals("baixadas")) {
            sql = "SELECT * FROM OS$ " +
                    "WHERE status = 'BAIXA'"  +
                    "ORDER BY OS DESC";
        }

        if (status.equals("pronta")) {
            sql = "SELECT * FROM OS$ " +
                    "WHERE status = 'PRONTO'"  +
                    "ORDER BY OS DESC";
        }

        if (status.equals("garantiaLojaAindaNoLaboratorio")) {
            sql = "SELECT * FROM OS$ " +
                    "WHERE status NOT IN ('FECHADA') AND garantia = 'LOJA'" +
                   "ORDER BY OS DESC";
        }

        if (status.equals("garantiaFabricaAindaNoLaboratorio")) {
            sql = "SELECT * FROM OS$ " +
                    "WHERE status NOT IN ('FECHADA') AND garantia = 'FABRICA'" +
                    "ORDER BY OS DESC";
        }



        if (status.equals("emAnalise")) {
            sql = "SELECT * FROM OS$ " +
                    "WHERE status = 'ANALISE'" +
                    "ORDER BY OS DESC";
        }

        if (status.equals("osImpressaMasNaoFechada")) {
            sql = "SELECT * FROM OS$ " +
                    "WHERE status NOT IN ('FECHADA') AND jaimp = 'IMP'" +
                    "ORDER BY OS DESC";
        }




        List<OsEntity> osEntities = entityManager.createNativeQuery(sql, OsEntity.class)
                //.setParameter(1, dataInicioString)
                //.setParameter(2, dataFimString)
                .getResultList();

        // Somando os valores da coluna TOTALIQ
        BigDecimal totalIQ = osEntities.stream()
                .filter(Objects::nonNull) // Filtra entidades nulas
                .map(osEntity -> {
                    // Converte o valor de TOTALIQ de varchar para BigDecimal
                    String totalIQString = osEntity.getTotaliq();
                    if (totalIQString != null) {
                        // Remove espaços em branco e substitui vírgulas por pontos
                        totalIQString = totalIQString.trim().replace(",", ".");
                        try {
                            return new BigDecimal(totalIQString);
                        } catch (NumberFormatException e) {
                            // Se a conversão falhar, retorna BigDecimal.ZERO
                            return BigDecimal.ZERO;
                        }
                    }
                    return BigDecimal.ZERO; // Retorna zero se a string for nula
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Soma os valores

        // pegando a quantidade de ordens de servicos
        int qtOs = osEntities.size();

        // Mapeando as entidades para DTO e incluindo totalIQSum
        List<OsDTO> osDTOs = osEntities.stream()
                .filter(Objects::nonNull) // Filtra entidades nulas
                .map(entity -> mapToDTO(entity, totalIQ, qtOs)) // Passa totalIQSum
                .collect(Collectors.toList());

        return osDTOs;

    }

    @Transactional
    public List<OsDTO> buscaOsPorStatusECodcli(String status, Long codcli) {
        if (status == null || codcli == null) {
            return Collections.emptyList();
        }

        String sql = null;

       if (status.equals("aguardandoAprovacao")) {
            sql = "SELECT * FROM OS$ WHERE status = 'AGUARDANDO APROV' AND codcli = :codcli ORDER BY OS DESC";
        }

        // Executando a consulta com parâmetro codcli
        List<OsEntity> osEntities = entityManager.createNativeQuery(sql, OsEntity.class)
                .setParameter("codcli", codcli)  // Passando o parâmetro codcli
                .getResultList();

        // Somando os valores da coluna TOTALIQ
        BigDecimal totalIQ = osEntities.stream()
                .filter(Objects::nonNull)
                .map(osEntity -> {
                    String totalIQString = osEntity.getTotaliq();
                    if (totalIQString != null) {
                        totalIQString = totalIQString.trim().replace(",", ".");
                        try {
                            return new BigDecimal(totalIQString);
                        } catch (NumberFormatException e) {
                            return BigDecimal.ZERO;
                        }
                    }
                    return BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Quantidade de ordens de serviço
        int qtOs = osEntities.size();

        // Mapeando as entidades para DTO e incluindo totalIQSum
        List<OsDTO> osDTOs = osEntities.stream()
                .filter(Objects::nonNull)
                .map(entity -> mapToDTO(entity, totalIQ, qtOs))
                .collect(Collectors.toList());

        return osDTOs;
    }

    @Transactional
    public List<OsDTO> buscaOsPorStatusRemoto() {
        String sql = "SELECT * FROM OS$ WHERE STATUS = 'REPROVADA REMOTA' OR STATUS = 'APROVADA REMOTA' ORDER BY OS DESC";

        // Executando a consulta para buscar as entidades
        List<OsEntity> osEntities = entityManager.createNativeQuery(sql, OsEntity.class)
                .getResultList();

        // Caso nenhuma ordem de serviço seja encontrada
        if (osEntities.isEmpty()) {
            return Collections.emptyList();
        }

        // Quantidade de ordens de serviço
        int qtOs = osEntities.size();

        // Somando os valores da coluna TOTALIQ
        BigDecimal totalIQ = osEntities.stream()
                .filter(Objects::nonNull)
                .map(osEntity -> {
                    String totalIQString = osEntity.getTotaliq();
                    if (totalIQString != null) {
                        totalIQString = totalIQString.trim().replace(",", ".");
                        try {
                            return new BigDecimal(totalIQString);
                        } catch (NumberFormatException e) {
                            return BigDecimal.ZERO;
                        }
                    }
                    return BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Mapeando as entidades para DTOs e incluindo totalIQSum
        List<OsDTO> osDTOs = osEntities.stream()
                .filter(Objects::nonNull)
                .map(entity -> mapToDTO(entity, totalIQ, qtOs))
                .collect(Collectors.toList());

        return osDTOs;
    }




    @Transactional
    public ComissaoDTO calculaComissaoTecnicos(String dataInicioString, String dataFimString, String busca) {
        String sql = null;

        if (busca.equals("comissaoTecnicos")) {
            sql = "SELECT * FROM OS$ " +
                    "WHERE fechamento BETWEEN CONVERT(datetime, ?, 120) AND CONVERT(datetime, ?, 120) " +
                    "ORDER BY fechamento ASC";
        }

        List<OsEntity> osEntities = entityManager.createNativeQuery(sql, OsEntity.class)
                .setParameter(1, dataInicioString)
                .setParameter(2, dataFimString)
                .getResultList();

        // Somando os valores da coluna TOTALIQ
        BigDecimal totalIQ = osEntities.stream()
                .filter(Objects::nonNull) // Filtra entidades nulas
                .map(osEntity -> {
                    // Converte o valor de TOTALIQ de varchar para BigDecimal
                    String totalIQString = osEntity.getTotaliq();
                    if (totalIQString != null) {
                        // Remove espaços em branco e substitui vírgulas por pontos
                        totalIQString = totalIQString.trim().replace(",", ".");
                        try {
                            return new BigDecimal(totalIQString);
                        } catch (NumberFormatException e) {
                            // Se a conversão falhar, retorna BigDecimal.ZERO
                            return BigDecimal.ZERO;
                        }
                    }
                    return BigDecimal.ZERO; // Retorna zero se a string for nula
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Soma os valores

        // pegando a quantidade de ordens de servicos
        int qtOs = osEntities.size();

        // Mapeando as entidades para DTO e incluindo totalIQSum
        List<OsDTO> osDTOs = osEntities.stream()
                .filter(Objects::nonNull) // Filtra entidades nulas
                .map(entity -> mapToDTO(entity, totalIQ, qtOs)) // Passa totalIQSum
                .collect(Collectors.toList());

        // BUSCA O VALOR DO FATURAMENTO TOTAL //////////////////////////////////
        BigDecimal faturamentoTotal = osDTOs.isEmpty() ? BigDecimal.ZERO : osDTOs.get(0).getSomatoriaDTO().getSomatotaliq();

        System.out.println("***************** faturamento total *********************** " + faturamentoTotal);

        // BUSCA VALOR DE VENDA DE NOBREAK NOVO ////////////////////////////
        busca ="vendaNobreakNovo";

        BigDecimal valorVendaNobreak = BigDecimal.ZERO;
        try {
            List<OsDTO> vendaNobreakNovo = faturamentoEmIntervalo(dataInicioString, dataFimString, busca);

            if (!vendaNobreakNovo.isEmpty()) {
                valorVendaNobreak = vendaNobreakNovo.get(0).getSomatoriaDTO().getSomatotaliq();
            }
        } catch (Exception e) {
            valorVendaNobreak = BigDecimal.ZERO;
        }
        System.out.println("****************valor venda nobreak novo******* " + valorVendaNobreak);

        // BUSCA VALOR DE VENDA DE BATERIAS AVULSAS ////////////////////////////
        busca ="vendaBateriaAvulsa";

        BigDecimal valorVendaBateria = BigDecimal.ZERO;
        try {
            List<OsDTO> vendaBateria = faturamentoEmIntervalo(dataInicioString, dataFimString, busca);

            if (!vendaBateria.isEmpty()) {
                valorVendaBateria = vendaBateria.get(0).getSomatoriaDTO().getSomatotaliq();
            }
        } catch (Exception e) {
            valorVendaBateria = BigDecimal.ZERO;
        }
        System.out.println("****************valor venda Bateria Avulsa******* " + valorVendaBateria);

        // CALCULO PERCENTUAL COMISSÃO
        BigDecimal percentual;
        if (faturamentoTotal.compareTo(new BigDecimal("40000")) < 0) {
            percentual = BigDecimal.ZERO; // Se faturamentoTotal for menor que 40000
        } else if (faturamentoTotal.compareTo(new BigDecimal("50000")) < 0) {
            percentual = new BigDecimal("0.005"); // Se faturamentoTotal for maior que 40000 e menor que 50000
        } else {
            percentual = new BigDecimal("0.01"); // Se faturamentoTotal for maior que 50000
        }

        System.out.println("***********percentual de comissão ********" + percentual);

        // CALCULO VALOR COMISSIONAL:
        BigDecimal valorComissional = faturamentoTotal.subtract(valorVendaBateria.add(valorVendaNobreak));

        System.out.println("*********valor comissional " + valorComissional);

        // CALCULO DA COMISSÃO INDIVIDUAL:
        BigDecimal comissaoIndividual = valorComissional.multiply(percentual).setScale(2, RoundingMode.HALF_UP);

        System.out.println("***************COMISSAO IND " + comissaoIndividual);

        return ComissaoDTO.builder()
                .comissao(comissaoIndividual)
                .valorComissional(valorComissional)
                .percentual(percentual)
                .build();
    }



    private OsDTO mapToDTO(OsEntity entity, BigDecimal totalIQ, int qtOs) {
        SomatoriaDTO somatoriaDTO = SomatoriaDTO.builder()
                .somatotaliq(totalIQ)
                .qtOs(qtOs)
                .build();

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
                .somatoriaDTO(somatoriaDTO)
                .build();
    }

}
