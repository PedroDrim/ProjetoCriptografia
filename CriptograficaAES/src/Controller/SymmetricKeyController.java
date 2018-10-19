package Controller;

import Exceptions.KeyCreatorException;
import model.KeyManager;

import java.security.Key;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Classe responsavel por gerenciar o uso de chaves simetricas
 */
public class SymmetricKeyController implements KeyManager {

    private static final String ALGO = "AES";

    /**
     * Metodo para converter bytes em chave simetrica
     * @param keyBytes bytes da chave
     * @return chave simetrica convertida
     */
    public static Key bytesToKey(byte[] keyBytes) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, ALGO);
        return secretKeySpec;
    }

    /**
     * Metodo responsavel por gerar chaves criptografadas
     * @param primeiroBloco vetor de inicializacao das chaves
     * @return chaves geradas
     */
    @Override
    public Key[] gerarChave(byte[] primeiroBloco) {
        Key secretKeySpec = new SecretKeySpec(primeiroBloco, this.ALGO);

        Key[] keyVector = new Key[1];
        keyVector[0] = secretKeySpec;
        return keyVector;
    }

    /**
     * Metodo responsavel por criptografar uma mensagem
     * @param mensagem mensagem a ser criptografada
     * @param symmetricKey chave para criptografar
     * @return mensagem criptografada
     */
    @Override
    public byte[] criptografar(byte[] mensagem, Key symmetricKey) {
        byte[] res;

        try {
            Cipher c = Cipher.getInstance(this.ALGO);
            c.init(Cipher.ENCRYPT_MODE, symmetricKey);
            byte[] encVal = c.doFinal(mensagem);
            res = Base64.getEncoder().encode(encVal);
        } catch (Exception e) {
            throw new KeyCreatorException("Nao foi possivel criptografar a mensagem.");
        }

        return res;
    }

    /**
     * Metodo responsavel por descriptografar uma mensagem criptografada anteriormente
     * @param mensagemCritografada mensagem a ser descriptografada
     * @param symmetricKey chave para descriptografar
     * @return mensagem descriptografada
     */
    @Override
    public byte[] descriptografar(byte[] mensagemCritografada, Key symmetricKey) {
        byte[] decValue;

        try {
            Cipher c = Cipher.getInstance(this.ALGO);
            c.init(Cipher.DECRYPT_MODE, symmetricKey);
            byte[] decordedValue = Base64.getDecoder().decode(mensagemCritografada);
            decValue = c.doFinal(decordedValue);
        } catch (Exception e) {
            throw new KeyCreatorException("Nao foi possivel descriptografar a mensagem.");
        }

        return decValue;
    }
}