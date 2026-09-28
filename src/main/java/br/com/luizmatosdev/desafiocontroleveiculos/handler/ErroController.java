package br.com.luizmatosdev.desafiocontroleveiculos.handler;

import br.com.luizmatosdev.desafiocontroleveiculos.enums.Retorno;
import br.com.luizmatosdev.desafiocontroleveiculos.service.ResponseService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Substitui o /error padrão do Spring Boot, para o erro que escapa dos handlers também vir no envelope. */
@Hidden
@RestController
public class ErroController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<ResponseService<Void>> erro(HttpServletRequest request) {
        Object atributo = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int status = atributo instanceof Integer codigo ? codigo : 500;
        Retorno retorno = Retorno.doStatus(status);
        return ResponseEntity.status(status).body(ResponseService.build(retorno.getCodigo(), retorno.getDescricao()));
    }
}
