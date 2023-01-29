package br.com.github.victorhugoof.cep.controller;

import br.com.github.victorhugoof.cep.model.CepCompleto;
import br.com.github.victorhugoof.cep.service.SearchCepService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@Validated
@RestController
@RequestMapping("/ceps")
@RequiredArgsConstructor
public class CepController {

    private final SearchCepService searchCepService;

    @GetMapping("/{cep}")
    public Mono<ResponseEntity<CepCompleto>> findByCep(
            @PathVariable @Valid @Size(min = 8, max = 8) @Pattern(regexp = "[0-9]+", message = "deve conter somente números") String cep,
            @RequestParam(required = false) boolean force
    ) {
        return searchCepService.searchCep(cep, force)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
}
