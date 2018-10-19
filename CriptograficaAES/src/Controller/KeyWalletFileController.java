package Controller;

import Exceptions.CustomFileException;
import model.KeyWallet;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;

/**
 * Classe responsavel por gerenciar a importacao e exportacao de 'KeyWallet'
 */
public class KeyWalletFileController {

    /**
     * Metodo para exportar uma 'KeyWallet
     * @param keyWallet 'keyWallet' a ser exportada
     * @param outputPrefix prefixo dos resultados
     */
    public static void exportKeyWallet(KeyWallet keyWallet, String outputPrefix) {

        try {
            byte[] privateBytes = keyWallet.getChavePrivadaRemetente().getEncoded();
            byte[] publicBytes = keyWallet.getChavePublicaDestinatario().getEncoded();

            // rk0 -> Remetente Key privada (0)
            String outputPrivate = outputPrefix + ".rk0";
            // dk1 -> Destinatario Key publica (1)
            String outputPublic = outputPrefix + ".dk1";

            FileOutputStream fileOutputStream = new FileOutputStream(outputPrivate);
            fileOutputStream.write(privateBytes);
            fileOutputStream.close();

            fileOutputStream = new FileOutputStream(outputPublic);
            fileOutputStream.write(publicBytes);
            fileOutputStream.close();


        } catch(IOException e) {
            throw new CustomFileException("Nao foi possivel exportar a carteira.");
        }
    }

    /**
     * Metodo para importar uma 'KeyWallet'
     * @param inputPrivate nome do arquivo privado
     * @param inputPublic nome do arquivo publico
     */
    public static KeyWallet importKeyWallet(String inputPrivate, String inputPublic) {

        try {
            FileInputStream fileInputStream = new FileInputStream(inputPrivate);
            byte[] privateKeyBytes = fileInputStream.readAllBytes();
            fileInputStream.close();

            fileInputStream = new FileInputStream(inputPublic);
            byte[] publicKeyBytes = fileInputStream.readAllBytes();
            fileInputStream.close();

            Key privateKey = AsymmetricKeyController.bytesToPrivateKey(privateKeyBytes);
            Key publicKey = AsymmetricKeyController.bytesToPublicKey(publicKeyBytes);

            return new KeyWallet(privateKey, publicKey);

        } catch(IOException e) {
            throw new CustomFileException("Nao foi possivel importar a carteira.");
        }
    }
}
