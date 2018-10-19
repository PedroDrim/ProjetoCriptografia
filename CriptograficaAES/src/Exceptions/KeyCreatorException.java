package Exceptions;

/**
 * Excecao customizada para gerenciar a criacao de chaves de criptografia
 */
public class KeyCreatorException extends RuntimeException {

    /**
     * Construtor publico
     * @param mensagem mensagem de erro
     */
    public KeyCreatorException(String mensagem) {
        super(mensagem);
    }
}
