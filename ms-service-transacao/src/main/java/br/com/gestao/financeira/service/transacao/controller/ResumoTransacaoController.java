package br.com.gestao.financeira.service.transacao.controller;

import br.com.gestao.financeira.service.transacao.service.RelatorioService;
import br.com.gestao.financeira.service.transacao.service.ResumoTransacaoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/transacoes/resumo")
@SecurityRequirement(name = "bearer-key")
public class ResumoTransacaoController {

    private final ResumoTransacaoService service;
    private final RelatorioService relatorioService;

    public ResumoTransacaoController(ResumoTransacaoService service, RelatorioService relatorioService) {
        this.service = service;
        this.relatorioService = relatorioService;
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

    @GetMapping("/{id}/relatorio/excel")
    public ResponseEntity<InputStreamResource> downloadExcel(@PathVariable(name = "id") Long usuarioId) throws IOException {
        ByteArrayInputStream in = relatorioService.gerarExcel(usuarioId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=resumo_transacoes.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }

    @GetMapping("/{id}/relatorio/pdf")
    public ResponseEntity<InputStreamResource> downloadPdf(@PathVariable(name = "id") Long usuarioId) {
        ByteArrayInputStream in = relatorioService.gerarPdf(usuarioId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=resumo_transacoes.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(in));
    }
}

