package br.com.gestao.financeira.api.controller;

import br.com.gestao.financeira.api.service.ResumoTransacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transacoes/resumo")
public class ResumoTransacaoController {

    private final ResumoTransacaoService service;

    public ResumoTransacaoController(ResumoTransacaoService service) {
        this.service = service;
    }

    @GetMapping("/{id}/categoria")
    public ResponseEntity<?> porCategoria(@PathVariable(name = "id") Long usuarioId) {
        return ResponseEntity.ok(service.resumoPorCategoria(usuarioId));
    }

    @GetMapping("/{id}/dia")
    public ResponseEntity<?> porDia(@PathVariable(name = "id") Long usuarioId) {
        return ResponseEntity.ok(service.resumoPorDia(usuarioId));
    }

    @GetMapping("/{id}/mes")
    public ResponseEntity<?> porMes(@PathVariable(name = "id") Long usuarioId) {
        return ResponseEntity.ok(service.resumoPorMes(usuarioId));
    }
}

