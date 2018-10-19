package model;

/**
 * Interface para o gerenciamento de hashes
 */
public interface HashManager {

    /**
     * Metodo responsavel por gerar o hash de uma mensagem
     * @param mensagem mensagem a ser calculado o hash
     * @return hash da correspondente mensagem
     */
    String gerarHash(byte[] mensagem);

    /**
     * Metodo responsavel por validar um hash
     * @param mensagemOriginal mensagem a ser validada
     * @param supostoHash hash supostamente 'verdadeiro'
     * @return 'true', caso verdadeito e 'false', caso falso
     */
    boolean validarHash(byte[] mensagemOriginal, String supostoHash);
}
