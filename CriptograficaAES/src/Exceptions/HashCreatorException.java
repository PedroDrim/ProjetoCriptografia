package Exceptions;

/**
 * Excecao customizada para gerenciar a criacao de hashes
 */
public class HashCreatorException extends RuntimeException{

    /**
     * Construtor publico
     * @param mensagem mensagem de erro
     */
    public HashCreatorException(String mensagem) {
        super(mensagem);
    }
}
