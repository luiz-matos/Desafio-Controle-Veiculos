package br.com.luizmatosdev.desafioveiculos.handler;

import br.com.luizmatosdev.desafioveiculos.dto.InputErrorDTO;

import java.util.List;

public record CampoInvalidoResponse(List<InputErrorDTO> fields) {
}
