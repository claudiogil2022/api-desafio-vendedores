package com.desafio.infrastructure.web.exceptions;

import com.desafio.application.dtos.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException ex) {
        log.error("Erro de runtime: ", ex);
        String friendlyMessage = getFriendlyMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(friendlyMessage));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Argumento inválido: ", ex);
        String friendlyMessage = getFriendlyMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(friendlyMessage));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleJsonParseException(HttpMessageNotReadableException ex) {
        log.error("Erro no formato JSON: ", ex);
        log.error("Causa raiz: ", ex.getCause());
        String mensagem = "❌ Formato de dados inválido. Verifique se todos os campos estão preenchidos corretamente.";
        
        // Melhor diagnóstico do erro
        if (ex.getCause() != null) {
            String causaMessage = ex.getCause().getMessage();
            if (causaMessage != null) {
                if (causaMessage.contains("TipoContratacao")) {
                    mensagem = "❌ Tipo de contratação inválido. Use: CLT, OUTSOURCING ou PESSOA_JURIDICA";
                } else if (causaMessage.contains("LocalDate")) {
                    mensagem = "❌ Formato de data inválido. Use: yyyy-MM-dd (exemplo: 1990-01-15)";
                } else if (causaMessage.contains("Cannot deserialize")) {
                    mensagem = "❌ Erro na estrutura JSON. Verifique a sintaxe e tipos de dados.";
                }
            }
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(mensagem));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        log.error("Erro de validação: ", ex);
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = getFriendlyValidationMessage(fieldName, error.getDefaultMessage());
            errors.put(fieldName, errorMessage);
        });
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.<Map<String, String>>builder()
                        .success(false)
                        .message("⚠️ Dados incompletos ou inválidos. Verifique os campos destacados:")
                        .data(errors)
                        .timestamp(java.time.LocalDateTime.now().toString())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        log.error("Erro interno: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("🔧 Ops! Algo deu errado. Nossa equipe técnica foi notificada."));
    }

    private String getFriendlyMessage(String originalMessage) {
        if (originalMessage == null) return "❌ Erro desconhecido";
        
        // Mensagens específicas de documento
        if (originalMessage.contains("Documento inválido")) {
            return "❌ CPF ou CNPJ inválido. Verifique se digitou corretamente.";
        }
        if (originalMessage.contains("Pessoa Física deve ter CPF")) {
            return "❌ Para CLT ou Terceirizado, informe um CPF válido (11 dígitos).";
        }
        if (originalMessage.contains("Pessoa Jurídica deve ter CNPJ")) {
            return "❌ Para Pessoa Jurídica, informe um CNPJ válido (14 dígitos).";
        }
        
        // Mensagens de duplicidade
        if (originalMessage.contains("Já existe um vendedor com este documento")) {
            return "⚠️ CPF/CNPJ já cadastrado. Cada vendedor deve ter um documento único.";
        }
        if (originalMessage.contains("Já existe um vendedor com este email")) {
            return "⚠️ E-mail já cadastrado. Cada vendedor deve ter um e-mail único.";
        }
        
        // Mensagens de filial
        if (originalMessage.contains("Filial não encontrada")) {
            return "❌ Filial inválida. Selecione uma filial válida.";
        }
        if (originalMessage.contains("Filial não está ativa")) {
            return "⚠️ Filial inativa. Não é possível cadastrar vendedores nesta filial.";
        }
        
        // Mensagens de processamento
        if (originalMessage.contains("Processamento não encontrado")) {
            return "❌ Processamento não encontrado. Verifique o ID informado.";
        }
        
        // Mensagem padrão
        return "❌ " + originalMessage;
    }

    private String getFriendlyValidationMessage(String fieldName, String originalMessage) {
        switch (fieldName) {
            case "nome":
                return "📝 Nome é obrigatório e deve ter pelo menos 2 caracteres";
            case "email":
                return "📧 E-mail é obrigatório e deve ter um formato válido";
            case "documento":
                return "🆔 CPF ou CNPJ é obrigatório";
            case "dataNascimento":
                return "📅 Data de nascimento é obrigatória";
            case "filialId":
                return "🏢 Filial é obrigatória";
            case "tipoContratacao":
                return "💼 Tipo de contratação é obrigatório (CLT, Terceirizado ou Pessoa Jurídica)";
            default:
                return originalMessage;
        }
    }
}
