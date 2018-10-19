package Controller;

import Exceptions.KeyCreatorException;
import model.KeyManager;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Classe responsavel por gerenciar chaves assimetricas
 */
public class AsymmetricKeyController implements KeyManager{

    private static final String ALGO = "RSA";
    private final int initializeSize = 1024;

    /**
     * Metodo para converter bytes em chave publica
     * @param bytes bytes da chave
     * @return chave publica convertida
     */
    public static Key bytesToPublicKey(byte[] bytes) {

        try {
            Key publicKey = KeyFactory.getInstance(ALGO).generatePublic(new X509EncodedKeySpec(bytes));
            return publicKey;

        } catch(Exception e) {
            throw new KeyCreatorException("Nao foi possivel converter a chave.");
        }
    }

    /**
     * Metodo para converter bytes em chave privada
     * @param bytes bytes da chave
     * @return chave privada convertida
     */
    public static Key bytesToPrivateKey(byte[] bytes) {
        try {
            Key privateKey = KeyFactory.getInstance(ALGO).generatePrivate(new PKCS8EncodedKeySpec(bytes));
            return privateKey;

        } catch(Exception e) {
            throw new KeyCreatorException("Nao foi possivel converter a chave.");
        }
    }

    /**
     * Metodo responsavel por gerar chaves criptografadas
     * @param primeiroBloco vetor de inicializacao das chaves
     * @return chaves geradas
     */
    @Override
    public Key[] gerarChave(byte[] primeiroBloco) {

        // TODO: Achar um uso para o 'primeiroBloco'

        Key[] keyVector = new Key[2];

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(this.ALGO);
            keyPairGenerator.initialize(this.initializeSize);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            keyVector[0] = keyPair.getPrivate();
            keyVector[1] = keyPair.getPublic();
        } catch (Exception e) {
            throw new KeyCreatorException("Nao foi possivel criar os pares de chaves.");
        }

        return keyVector;
    }

    /**
     * Metodo responsavel por criptografar uma mensagem
     * @param mensagem mensagem a ser criptografada
     * @param privateKey chave para criptografar
     * @return mensagem criptografada
     */
    @Override
    public byte[] criptografar(byte[] mensagem, Key privateKey) {

        byte[] mensagemCritografada;

        try {
            Cipher c = Cipher.getInstance(this.ALGO);
            c.init(Cipher.ENCRYPT_MODE, privateKey);

            mensagemCritografada = c.doFinal(mensagem);
        } catch (Exception e) {
            e.printStackTrace();
            throw new KeyCreatorException("Nao foi possivel criptografar a mensagem.");
        }

        return mensagemCritografada;
    }

    /**
     * Metodo responsavel por descriptografar uma mensagem criptografada anteriormente
     * @param mensagemCritografada mensagem a ser descriptografada
     * @param publicKey chave para descriptografar
     * @return mensagem descriptografada
     */
    @Override
    public byte[] descriptografar(byte[] mensagemCritografada, Key publicKey) {

        byte[] mensagemOriginal;

        try {
            Cipher c = Cipher.getInstance(this.ALGO);
            c.init(Cipher.DECRYPT_MODE, publicKey);

            mensagemOriginal = c.doFinal(mensagemCritografada);
        } catch (Exception e) {

            e.printStackTrace();
            throw new KeyCreatorException("Nao foi possivel descriptografar a mensagem.");
        }

        return mensagemOriginal;
    }
}
