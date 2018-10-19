package model;

import java.security.Key;

/**
 * Interface responsavel por gerenciar a criacao de chaves para criptografia
 */
public interface KeyManager {

    /**
     * Metodo responsavel por gerar chaves criptografadas
     * @param primeiroBloco vetor de inicializacao das chaves
     * @return chaves geradas
     */
    Key[] gerarChave(byte[] primeiroBloco);

    /**
     * Metodo responsavel por descriptografar uma mensagem criptografada anteriormente
     * @param mensagemCritografada mensagem a ser descriptografada
     * @param key chave para descriptografar
     * @return mensagem descriptografada
     */
    byte[] descriptografar(byte[] mensagemCritografada, Key key);

    /**
     * Metodo responsavel por criptografar uma mensagem
     * @param mensagem mensagem a ser criptografada
     * @param key chave para criptografar
     * @return mensagem criptografada
     */
    byte[] criptografar(byte[] mensagem, Key key);
}
