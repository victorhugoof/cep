package db.migration;

import br.com.github.victorhugoof.api.cep.enums.OrigemCep;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import static org.apache.commons.lang3.StringUtils.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Slf4j
public class V6__ImportBaseCeps extends BaseJavaMigration {
    private static final String FILE = "/data/base-ceps-normalizada.csv";
    private static final Integer BULK_SIZE = 200;

    @Override
    public void migrate(Context context) throws Exception {
        var saved = new AtomicInteger();
        var ceps = new ArrayList<CepArquivo>();

        percorreLinhas(linha -> {
            try {
                ceps.add(new CepArquivo(linha));
            } catch (Exception e) {
                log.error("Erro ao parsear a linha '{}'. Erro: {}", linha, e.getMessage());
            }

            if (ceps.size() == BULK_SIZE) {
                insert(context.getConnection(), ceps);
                saved.addAndGet(ceps.size());
                ceps.clear();
            }
        }, true);

        if (!ceps.isEmpty()) {
            insert(context.getConnection(), ceps);
            ceps.clear();
        }
    }

    private void insert(Connection db, List<CepArquivo> data) {
        var query = new StringBuilder();
        query.append(" INSERT INTO cep (cep, bairro, complemento, logradouro, latitude, longitude, origem, cidade_ibge, created_at, updated_at) ");
        query.append(" VALUES ");

        for (var i = 0; i < data.size(); i++) {
            query.append("        (?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW()) ");
            if (i < data.size() - 1) {
                query.append(",");
            }
        }

        query.append(" ON CONFLICT (cep) ");
        query.append("     DO UPDATE SET bairro = excluded.bairro, ");
        query.append("                   complemento = excluded.complemento, ");
        query.append("                   logradouro = excluded.logradouro, ");
        query.append("                   latitude = excluded.latitude, ");
        query.append("                   longitude = excluded.longitude, ");
        query.append("                   origem = excluded.origem, ");
        query.append("                   cidade_ibge = excluded.cidade_ibge, ");
        query.append("                   updated_at = excluded.updated_at ");

        try (var ps = db.prepareStatement(query.toString())) {
            var psCount = 1;

            for (var dto : data) {
                ps.setInt(psCount++, dto.getCep());
                ps.setString(psCount++, dto.getBairro());
                ps.setString(psCount++, dto.getComplemento());
                ps.setString(psCount++, dto.getLogradouro());
                ps.setBigDecimal(psCount++, dto.getLatitude());
                ps.setBigDecimal(psCount++, dto.getLongitude());
                ps.setString(psCount++, dto.getOrigem().name());
                ps.setInt(psCount++, dto.getCidadeIbge());
            }

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private void percorreLinhas(Consumer<String> linhaConsumer, boolean skipHeader) throws IOException {
        try (var br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream(FILE))))) {
            var i = -1;

            String linha;
            while ((linha = br.readLine()) != null) {
                i++;
                if (i == 0 && skipHeader) {
                    continue;
                }

                linhaConsumer.accept(linha);
            }
        }
    }

    @Data
    private static class CepArquivo {
        private final Integer cep;
        private final String bairro;
        private final String complemento;
        private final String logradouro;
        private final Integer cidadeIbge;
        private final BigDecimal latitude;
        private final BigDecimal longitude;
        private final OrigemCep origem;

        CepArquivo(String linha) {
            var dados = linha.split(";");
            this.cep = Integer.parseInt(Objects.requireNonNull(getInfo(dados, 0)));
            this.bairro = getInfo(dados, 1);
            this.complemento = getInfo(dados, 2);
            this.logradouro = getInfo(dados, 3);
            this.cidadeIbge = Integer.parseInt(Objects.requireNonNull(getInfo(dados, 4)));
            this.latitude = NumberUtils.createBigDecimal(firstNonBlank(getInfo(dados, 5)));
            this.longitude = NumberUtils.createBigDecimal(firstNonBlank(getInfo(dados, 6)));
            this.origem = OrigemCep.valueOf(getInfo(dados, 7));
        }

        private static String getInfo(String[] dados, int index) {
            return dados.length > index ? dados[index].trim() : null;
        }

    }
}
