package com.charlotte.passhub.controller;

import com.charlotte.passhub.model.Password;
import com.charlotte.passhub.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/passwords")
@Tag(name = "Password Management", description = "APIs para gerenciamento de senhas")
public class PasswordController {

    @Autowired
    private PasswordService passwordService;

    @GetMapping
    @Operation(summary = "Listar todas as senhas",
            description = "Retorna uma lista com todas as senhas cadastradas do usuário autenticado")
    @ApiResponse(responseCode = "200", description = "Lista de senhas retornada com sucesso")
    public List<Password> getAllPasswords() {
        return passwordService.findAll();
    }

    @PostMapping
    @Operation(summary = "Adicionar nova senha",
            description = "Cria e armazena uma nova senha")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Senha criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    public Password addPassword(@RequestBody Password password) {
        return passwordService.save(password);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar senha por ID",
            description = "Retorna uma senha específica com base no ID fornecido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Senha encontrada"),
            @ApiResponse(responseCode = "404", description = "Senha não encontrada")
    })
    public Password getPasswordById(
            @Parameter(description = "ID da senha a ser buscada", required = true, example = "uuid-123")
            @PathVariable String id) {
        return passwordService.findById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar senha",
            description = "Atualiza os dados de uma senha existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Senha atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Senha não encontrada")
    })
    public Password updatePassword(
            @Parameter(description = "ID da senha a ser atualizada", required = true, example = "uuid-123")
            @PathVariable String id,
            @RequestBody Password password) {
        password.setId(id);
        return passwordService.save(password);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir senha",
            description = "Remove uma senha do sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Senha excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Senha não encontrada")
    })
    public void deletePassword(
            @Parameter(description = "ID da senha a ser excluída", required = true, example = "uuid-123")
            @PathVariable String id) {
        passwordService.deleteById(id);
    }
}