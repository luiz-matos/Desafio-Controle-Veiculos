package br.com.luizmatosdev.desafioveiculos.service;

import br.com.luizmatosdev.desafioveiculos.enums.Retorno;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"message", "data"})
@Getter
public class ResponseService<T> {
    private final T data;
    private final Message message;

    private ResponseService(T data, Message message) {
        this.message = message;
        this.data = data;
    }

    public static <E> ResponseService<E> build(E data) {
        return new ResponseService<>(data, new Message(Retorno.SUCESSO.getCodigo(), Retorno.SUCESSO.getDescricao()));
    }

    public static <E> ResponseService<E> build(E data, Integer code, String message) {
        return new ResponseService<>(data, new Message(code, message));
    }

    public static ResponseService<Void> build(Integer code, String message) {
        return new ResponseService<>(null, new Message(code, message));
    }

    private record Message(Integer codigo, String descricao) {}
}
