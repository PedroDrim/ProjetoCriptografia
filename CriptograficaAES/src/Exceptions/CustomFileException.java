package Exceptions;

/**
 * Excecao customizada para gerenciar a criacao de arquivos
 */
public class CustomFileException extends RuntimeException{

    /**
     * Construtor publico
     * @param mensagem mensagem de erro
     */
    public CustomFileException(String mensagem) {
        super(mensagem);
    }

}
