package br.com.luizmatosdev.desafiocontroleveiculos.enums;

import lombok.Getter;

@Getter
public enum Retorno {
    SUCESSO(0, "Sucesso"),
    PLACA_VEICULO_JA_EXISTE(-1, "Placa do veículo já existe"),
    VEICULO_NAO_EXISTENTE(-2, "Veículo não encontrado"),
    NAO_AUTENTICADO(-70, "Login necessário, ou token inválido ou vencido"),
    ACESSO_NEGADO(-71, "O perfil do usuário não tem acesso a esta rota"),
    ERRO_WS(-80, "Erro de comunicação externa"),
    CAMPO_INVALIDO_OU_OBRIGATORIO(-90, "Campo inválido ou obrigatório"),
    REQUISICAO_INVALIDA(-91, "Requisição inválida"),
    ROTA_NAO_ENCONTRADA(-92, "Rota não encontrada"),
    ERRO_INTERNO(-99, "Erro interno");

    private final int codigo;
    private final String descricao;

    Retorno(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    /** Retorno dos erros que o Spring trata sozinho, escolhido pelo status HTTP. */
    public static Retorno doStatus(int status) {
        return switch (status) {
            case 401 -> NAO_AUTENTICADO;
            case 403 -> ACESSO_NEGADO;
            case 404 -> ROTA_NAO_ENCONTRADA;
            default -> status < 500 ? REQUISICAO_INVALIDA : ERRO_INTERNO;
        };
    }
}
