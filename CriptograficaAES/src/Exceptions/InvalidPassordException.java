package Exceptions;

/**
 * Excecao customizada para gerenciar a criacao de vetores de inicializacao
 */
public class InvalidPassordException extends RuntimeException {

    /**
     * Construtor publico
     * @param message mensagem de erro
     */
    public InvalidPassordException(String message) {
        super(message);
    }
}
