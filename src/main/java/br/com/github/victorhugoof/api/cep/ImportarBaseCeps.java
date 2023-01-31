package br.com.github.victorhugoof.api.cep;

import br.com.github.victorhugoof.api.cep.enums.Estado;
import static br.com.github.victorhugoof.api.cep.helper.CepUtils.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

@Slf4j
public class ImportarBaseCeps {

    public static void main(String[] args) throws Exception {
        var searchList = new ArrayList<Integer>();
        try (var br = new BufferedReader(new FileReader("base-ceps.tsv"))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                var cep = new CepArquivo(linha);
                if (Estado.SC.equals(cep.estado)) {
                    searchList.add(cep.cep);
                }
            }
        }

        searchList.forEach(cep -> {
            try {
                search(parseCep(cep));
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void search(String numCep) {
        try {
            var client = HttpClient.newBuilder().build();

            var request = HttpRequest.newBuilder(new URI("http://localhost:8585/api-cep/cep/%s".formatted(numCep)))
                    .GET()
                    .build();

            log.info("[{}] {} {}", numCep, request.method(), request.uri());
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("[{}] {}: {}", numCep, response.statusCode(), response.body());
        } catch (Exception e) {
            log.error("[{}] {}", numCep, e.getMessage(), e);
        }
    }

    @Data
    private static class CepArquivo {

        private final Integer cep;
        private final String cidade;
        private final Estado estado;
        private final String bairro;
        private final String logradouro;
        private final String complemento;

        CepArquivo(String linha) {
            var dados = linha.split("\t");
            var cepStr = dados[0];
            var cidadeEstado = dados[1];
            var cidadeEstadoSeparatorIndex = cidadeEstado.lastIndexOf("/");

            this.cep = Integer.parseInt(cepStr);
            this.cidade = cidadeEstado.substring(0, cidadeEstadoSeparatorIndex);
            this.estado = Estado.find(cidadeEstado.substring(cidadeEstadoSeparatorIndex + 1, cidadeEstadoSeparatorIndex + 3));
            this.bairro = getInfo(dados, 2);
            this.logradouro = getInfo(dados, 3);
            this.complemento = getInfo(dados, 4);
        }

        private static String getInfo(String[] dados, int index) {
            return dados.length > index ? dados[index] : null;
        }
    }
}
