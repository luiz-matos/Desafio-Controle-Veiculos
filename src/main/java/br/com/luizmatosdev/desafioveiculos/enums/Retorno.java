package br.com.luizmatosdev.desafioveiculos.enums;

import lombok.Getter;

@Getter
public enum Retorno {
    SUCESSO(0, "Sucesso"),
    PLACA_VEICULO_JA_EXISTE(-1, "Placa do veículo já existe"),
    VEICULO_NAO_EXISTENTE(-2, "Veículo não encontrado"),
    ERRO_WS(-80, "Erro de comunicação externa"),
    CAMPO_INVALIDO_OU_OBRIGATORIO(-90, "Campo inválido ou obrigatório");

    private final int codigo;
    private final String descricao;

    Retorno(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
}
