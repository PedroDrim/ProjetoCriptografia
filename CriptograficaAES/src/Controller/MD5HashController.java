package Controller;

import Exceptions.HashCreatorException;
import model.HashManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Gerador de hashes do tipo 'MD5'
 */
public class MD5HashController implements HashManager{

    private String MD5 = "MD5";

    /**
     * Metodo responsavel por gerar o hash de uma mensagem
     * @param mensagem mensagem a ser calculado o hash
     * @return hash da correspondente mensagem
     */
    @Override
    public String gerarHash(byte[] mensagem) {
        String hash;

        try {
            MessageDigest messageDigest = MessageDigest.getInstance(MD5);
            messageDigest.update(mensagem);
            hash = Base64.getEncoder().encodeToString(messageDigest.digest());

        } catch (NoSuchAlgorithmException e) {
            throw new HashCreatorException("Nao foi possivel gerar o hash da mensagem.");
        }

        return hash;
    }

    /**
     * Metodo responsavel por validar um hash
     * @param mensagemOriginal mensagem a ser validada
     * @param supostoHash hash supostamente 'verdadeiro'
     * @return 'true', caso verdadeito e 'false', caso falso
     */
    @Override
    public boolean validarHash(byte[] mensagemOriginal, String supostoHash) {

        String hashOriginal = gerarHash(mensagemOriginal);
        return supostoHash.equals(hashOriginal);
    }
}
