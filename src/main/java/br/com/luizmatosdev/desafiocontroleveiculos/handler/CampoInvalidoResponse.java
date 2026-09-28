package br.com.luizmatosdev.desafiocontroleveiculos.handler;

import br.com.luizmatosdev.desafiocontroleveiculos.dto.InputErrorDTO;
import java.util.List;

public record CampoInvalidoResponse(List<InputErrorDTO> fields) {}
