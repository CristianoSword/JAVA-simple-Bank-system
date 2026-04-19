import com.banco.api.application.service.TransacaoService;
import com.banco.api.presentation.dto.conta.ContaAbrirRequest;
import com.banco.api.presentation.dto.conta.ContaResponse;
import com.banco.api.presentation.dto.conta.TransferenciaRequest;
import com.banco.api.presentation.dto.transacao.TransacaoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contas")
@RequiredArgsConstructor
public class ContaController {

    private final ContaService contaService;
    private final TransacaoService transacaoService;

    @PostMapping
    public ResponseEntity<ContaResponse> abrir(@Valid @RequestBody ContaAbrirRequest request) {
        Conta conta = contaService.abrir(request.clienteId(), request.tipo());
        return ResponseEntity.status(HttpStatus.CREATED).body(ContaMapper.toResponse(conta));
    }

    @GetMapping("/{numeroConta}")
    public ResponseEntity<ContaResponse> buscarPorNumero(@PathVariable String numeroConta) {
        Conta conta = contaService.buscarPorNumeroConta(numeroConta);
        return ResponseEntity.ok(ContaMapper.toResponse(conta));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<ContaResponse>> listarPorCliente(@PathVariable Long clienteId) {
        List<ContaResponse> contas = contaService.listarPorCliente(clienteId).stream()
                .map(ContaMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(contas);
    }

    @PostMapping("/{numeroConta}/depositar")
    public ResponseEntity<ContaResponse> depositar(@PathVariable String numeroConta,
            @Valid @RequestBody OperacaoRequest request) {
        Conta conta = contaService.depositar(numeroConta, request.valor());
        return ResponseEntity.ok(ContaMapper.toResponse(conta));
    }

    @PostMapping("/{numeroConta}/sacar")
    public ResponseEntity<ContaResponse> sacar(@PathVariable String numeroConta,
            @Valid @RequestBody OperacaoRequest request) {
        Conta conta = contaService.sacar(numeroConta, request.valor());
        return ResponseEntity.ok(ContaMapper.toResponse(conta));
    }

    @PostMapping("/transferir")
    public ResponseEntity<Map<String, ContaResponse>> transferir(@Valid @RequestBody TransferenciaRequest request) {
        contaService.transferir(request.numeroContaOrigem(), request.numeroContaDestino(), request.valor());

        Conta origem = contaService.buscarPorNumeroConta(request.numeroContaOrigem());
        Conta destino = contaService.buscarPorNumeroConta(request.numeroContaDestino());

        return ResponseEntity.ok(Map.of(
                "origem", ContaMapper.toResponse(origem),
                "destino", ContaMapper.toResponse(destino)));
    }

    @PatchMapping("/{numeroConta}/encerrar")
    public ResponseEntity<Void> encerrar(@PathVariable String numeroConta) {
        contaService.encerrar(numeroConta);
        return ResponseEntity.noContent().build();
    }
}
