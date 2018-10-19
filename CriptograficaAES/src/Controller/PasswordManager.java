package Controller;

import Exceptions.InvalidPassordException;

import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Classe responsavel por gerenciar senhas
 */
public class PasswordManager {

    private final String ALGO = "AES";
    private final int SIZE = 128;

    /**
     * Metodo responsavel por gerar um vetor de inicializacao do algoritmo 'AES'
     * @return vetor de inicializacao gerado
     */
    public byte[] generateAESKey() {

        byte[] keyBytes;

        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(this.ALGO);
            keyGen.init(this.SIZE);
            Key key = keyGen.generateKey();
            keyBytes = key.getEncoded();

        } catch (NoSuchAlgorithmException e) {
            throw new InvalidPassordException("Nao foi possivel gerar o bloco inicial");
        }

        return keyBytes;
    }

    /**
     * Metodo para inserir senhas adicionais no vetor de inicializacao
     * @param originalArray vetor de inicializacao
     * @param password senha a ser inserida
     * @return novo vetor de inicializacao
     */
    public byte[] insertPassword(byte[] originalArray, String password) {

        byte[] passwordArray = Base64.getEncoder().encode(password.getBytes());
        byte[] finalPassword = new byte[originalArray.length];

        for(int index = 0; index < originalArray.length; index++) {
            byte b1 = this.validadeByte(originalArray, index);
            byte b2 = this.validadeByte(passwordArray, index);
            finalPassword[index] = (byte)(b1 ^ b2);
        }
;
        return finalPassword;
    }

    /**
     * Metodo para validar os bytes em um vetor
     * @param array vetor a ser validado
     * @param index posicao desejava
     * @return byte de validacao
     */
    private byte validadeByte(byte[] array, int index) {
        return index >= array.length? 0 : array[index];
    }
}
