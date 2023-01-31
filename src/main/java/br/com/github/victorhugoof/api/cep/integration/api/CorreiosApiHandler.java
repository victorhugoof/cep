package br.com.github.victorhugoof.api.cep.integration.api;

import br.com.github.victorhugoof.api.cep.enums.Estado;
import br.com.github.victorhugoof.api.cep.enums.OrigemCep;
import static br.com.github.victorhugoof.api.cep.helper.CepUtils.*;
import br.com.github.victorhugoof.api.cep.integration.CepApi;
import br.com.github.victorhugoof.api.cep.integration.CepApiHandler;
import br.com.github.victorhugoof.api.cep.integration.CidadeApi;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.soap.MessageFactory;
import static java.util.Objects.*;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Slf4j
@Component
public class CorreiosApiHandler implements CepApiHandler {

    private static final String API_URL = "https://apps.correios.com.br/SigepMasterJPA/AtendeClienteService/AtendeCliente";
    private static final String SOAP_ACTION = "http://cliente.bean.master.sigep.bsb.correios.com.br/AtendeCliente/consultaCEP";
    private final WebClient webClient;

    @Getter
    @Value("${cep.correios-api.habilitado}")
    private boolean habilitado;

    @Getter
    @Value("${cep.correios-api.ordem}")
    private Integer ordem;

    public CorreiosApiHandler(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    @Override
    public Mono<CepApi> findCepApi(Integer numCep) {
        log.info("Buscando no CorreiosApi");
        return webClient.post()
                .uri(API_URL)
                .header("SOAPAction", SOAP_ACTION)
                .contentType(MediaType.APPLICATION_XML)
                .bodyValue(getXmlBody(numCep))
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(this::getResponse)
                .map(ConsultaCepResponse::getCep)
                .filter(it -> nonNull(it) && numCep.equals(parseCep(it.getCep())))
                .onErrorResume(e -> {
                    log.debug(e.getMessage());
                    return Mono.empty();
                })
                .mapNotNull(this::toCepApi);
    }

    private CepApi toCepApi(CorreiosCepApiModel cep) {
        return CepApi.builder()
                .cep(parseCep(cep.getCep()))
                .bairro(cep.getBairro())
                .logradouro(cep.getEnd())
                .complemento(null)
                .latitude(null)
                .longitude(null)
                .cidade(CidadeApi.builder()
                        .ibge(null)
                        .nome(cep.getCidade())
                        .estado(Estado.find(cep.getUf()))
                        .build())
                .origem(OrigemCep.CORREIOS_API)
                .build();
    }

    private Mono<ConsultaCepResponse> getResponse(String body) {
        return Mono.fromSupplier(() -> {
            try (InputStream is = new ByteArrayInputStream(body.getBytes())) {
                var message = MessageFactory.newInstance().createMessage(null, is);
                var unmarshaller = JAXBContext.newInstance(ConsultaCepResponse.class).createUnmarshaller();

                return (ConsultaCepResponse) unmarshaller.unmarshal(message.getSOAPBody().extractContentAsDocument());
            } catch (Throwable e) {
                log.debug(e.getMessage());
                return null;
            }
        });
    }

    private String getXmlBody(Integer cep) {
        return "<soapenv:Envelope" //
                + " xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"" //
                + " xmlns:cli=\"http://cliente.bean.master.sigep.bsb.correios.com.br/\">" //
                + " <soapenv:Header/>" //
                + " <soapenv:Body>" //
                + "     <cli:consultaCEP>" //
                + "         <cep>" + parseCep(cep) + "</cep>" //
                + "     </cli:consultaCEP>" //
                + " </soapenv:Body>" //
                + "</soapenv:Envelope>";
    }

    @Data
    @XmlRootElement(name = "consultaCEPResponse", namespace = "http://cliente.bean.master.sigep.bsb.correios.com.br/")
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class ConsultaCepResponse {

        @XmlElement(name = "return")
        private CorreiosCepApiModel cep;
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class CorreiosCepApiModel {

        private String cep;
        private String uf;
        private String bairro;
        private String cidade;
        private String end;
        private String complemento;
        private String complemento2;
    }

}
