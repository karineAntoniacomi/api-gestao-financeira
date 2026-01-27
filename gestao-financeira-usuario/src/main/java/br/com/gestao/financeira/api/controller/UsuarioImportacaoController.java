package br.com.gestao.financeira.api.controller;

import br.com.gestao.financeira.api.service.UsuarioImportacaoService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/usuarios")
public class UsuarioImportacaoController {

    private final UsuarioImportacaoService service;

    public UsuarioImportacaoController(UsuarioImportacaoService service) {
        this.service = service;
    }

    @PostMapping(value = "/importar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> importar(@RequestPart("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        service.importar(file);
        return ResponseEntity.ok().build();
    }
}
