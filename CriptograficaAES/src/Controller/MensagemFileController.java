package Controller;

import Exceptions.CustomFileException;
import model.FileManager;
import model.HashManager;
import model.KeyManager;
import model.KeyWallet;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;

/**
 * Classe responsavel por gerenciar a criptografia da mensagem
 */
public class MensagemFileController implements FileManager {

    private final int ENCRYPT_BLOCK_SIZE = 100;
    private final int DECRYPT_BLOCK_SIZE = 128;

    /**
     * Metodo para criptografar um arquivo
     * @param arquivoOriginal Nome do arquivo a ser criptografado
     * @param arquivoCriptografado Nome do resultado criptografado
     * @param keyWallet 'KeyWallet' contendo chaves privada do remetente e publica do destinatario (resp.)
     */

    @Override
    public void criptografar(String arquivoOriginal, String arquivoCriptografado, KeyWallet keyWallet) {

        try {
            FileInputStream fileInputStream = new FileInputStream(arquivoOriginal);
            FileOutputStream fileOutputStream = new FileOutputStream(arquivoCriptografado);

            PasswordManager passwordManager = new PasswordManager();
            KeyManager symmetricManager = new SymmetricKeyController();
            KeyManager asymetricManager = new AsymmetricKeyController();
            HashManager hashManager = new MD5HashController();

            String fileExtension = arquivoOriginal.split("\\.")[1];

            byte[] blocoZero = passwordManager.generateAESKey();

            Key[] symKeyVector = symmetricManager.gerarChave(blocoZero);

            byte[] mensagem = fileInputStream.readAllBytes();
            byte[] extensioBytes = Base64.getEncoder().encode(fileExtension.getBytes());
            String hash = hashManager.gerarHash(mensagem);

            // Hash de validacao (assimetrica / Base64) -> minha chave privada
            byte[] hashCriptografadoAsym = asymetricManager.criptografar(hash.getBytes(), keyWallet.getChavePrivadaRemetente());

            // Chave simetrica (assimetrica) -> minha chave privada
            byte[] chaveSimetricaCriptografada = asymetricManager.criptografar(symKeyVector[0].getEncoded(), keyWallet.getChavePrivadaRemetente());

            // Extensao do arquivo (simetrica / Base64)
            byte[] fileExtensionCriptografadaSym = symmetricManager.criptografar(extensioBytes, symKeyVector[0]);

            // Conteudo do arquivo (simetrica)
            byte[] mensagemCriptografadaSym = symmetricManager.criptografar(mensagem, symKeyVector[0]);

            int totalLength = hashCriptografadoAsym.length + fileExtensionCriptografadaSym.length + mensagemCriptografadaSym.length + chaveSimetricaCriptografada.length;

            // Concatenacao de array de bytes
            byte[] join = new byte[totalLength];
            int localLength = 0;

            System.arraycopy(hashCriptografadoAsym, 0, join, localLength, hashCriptografadoAsym.length);
            localLength += hashCriptografadoAsym.length;

            System.arraycopy(chaveSimetricaCriptografada, 0, join, localLength, chaveSimetricaCriptografada.length);
            localLength += chaveSimetricaCriptografada.length;

            System.arraycopy(fileExtensionCriptografadaSym, 0, join, localLength, fileExtensionCriptografadaSym.length);
            localLength += fileExtensionCriptografadaSym.length;

            System.arraycopy(mensagemCriptografadaSym, 0, join, localLength, mensagemCriptografadaSym.length);

            // Array Split
            int maxRodadas = totalLength / this.ENCRYPT_BLOCK_SIZE;
            if(totalLength % this.ENCRYPT_BLOCK_SIZE > 0)
                maxRodadas++;

            for(int rodada = 0; rodada < maxRodadas; rodada++) {
                int start = rodada * this.ENCRYPT_BLOCK_SIZE;
                int end = ((rodada + 1) * this.ENCRYPT_BLOCK_SIZE) - 1;

                if(rodada == maxRodadas - 1)
                    end = totalLength;

                byte[] sub = Arrays.copyOfRange(join, start, end + 1);
                byte[] subAsym = asymetricManager.criptografar(sub, keyWallet.getChavePublicaDestinatario());

                fileOutputStream.write(subAsym);
            }

            fileInputStream.close();
            fileOutputStream.close();

        } catch (IOException e) {
            throw new CustomFileException("Nao foi possivel criptografar o arquivo.");
        }

    }

    /**
     * Metodo para descriptografar um arquivo
     * @param arquivoCriptografado Nome do arquivo a ser descriptografado
     * @param arquivoDescriptografado Nome do resultado descriptografado
     * @param keyWallet 'KeyWallet' contendo chaves privada do destinatario e publica do remetente (resp.)
     */
    @Override
    public void descriptografar(String arquivoCriptografado, String arquivoDescriptografado, KeyWallet keyWallet) {
        try {

            FileInputStream fileInputStream = new FileInputStream(arquivoCriptografado);

            KeyManager symmetricManager = new SymmetricKeyController();
            KeyManager asymetricManager = new AsymmetricKeyController();
            HashManager hashManager = new MD5HashController();

            byte[] bytesEncriptados = fileInputStream.readAllBytes();

            // Array Split
            int totalLength = bytesEncriptados.length;
            int maxRodadas = totalLength / this.DECRYPT_BLOCK_SIZE;

            if(totalLength % this.DECRYPT_BLOCK_SIZE > 0)
                maxRodadas++;

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            for(int rodada = 0; rodada < maxRodadas; rodada++) {

                int start = rodada * this.DECRYPT_BLOCK_SIZE;
                int end = ((rodada + 1) * this.DECRYPT_BLOCK_SIZE) - 1;

                if(rodada == maxRodadas - 1)
                    end = totalLength - 1;

                byte[] sub = Arrays.copyOfRange(bytesEncriptados, start, end + 1);
                byte[] subDesc = asymetricManager.descriptografar(sub, keyWallet.getChavePrivadaRemetente());

                byteArrayOutputStream.write(subDesc);
            }

            byte[] bytesDesencriptados = byteArrayOutputStream.toByteArray();

            // Obtendo hash
            byte[] hashBytesEncrypt = Arrays.copyOfRange(bytesDesencriptados, 0, 128);
            byte[] hashBytes = asymetricManager.descriptografar(hashBytesEncrypt, keyWallet.getChavePublicaDestinatario());
            String supostoHash = new String(hashBytes);

            // Obtendo chave simetrica
            byte[] symmetricKeyBytesEncrypt = Arrays.copyOfRange(bytesDesencriptados, 128, 256);
            byte[] symmetricKeyBytes = asymetricManager.descriptografar(symmetricKeyBytesEncrypt, keyWallet.getChavePublicaDestinatario());
            Key symmetricKey = SymmetricKeyController.bytesToKey(symmetricKeyBytes);

            // Obtendo extensao do arquivo
            byte[] extensionBytesEncrypt = Arrays.copyOfRange(bytesDesencriptados, 256, 280);
            byte[] extensionBytes = symmetricManager.descriptografar(extensionBytesEncrypt, symmetricKey);
            extensionBytes = Base64.getDecoder().decode(extensionBytes);
            String fileExtension = new String(extensionBytes);

            // Obtendo mensagem
            byte[] mensagemBytesEncrypt = Arrays.copyOfRange(bytesDesencriptados, 280, bytesDesencriptados.length - 1);
            byte[] mensagemBytes = symmetricManager.descriptografar(mensagemBytesEncrypt, symmetricKey);

            if(!hashManager.validarHash(mensagemBytes, supostoHash))
                throw new CustomFileException("Nao foi possivel descriptografar o arquivo.");

            String outputFileName = arquivoDescriptografado + "." + fileExtension;
            FileOutputStream fileOutputStream = new FileOutputStream(outputFileName);

            fileOutputStream.write(mensagemBytes);

            byteArrayOutputStream.close();
            fileInputStream.close();
            fileOutputStream.close();

        } catch (IOException e) {
            throw new CustomFileException("Nao foi possivel descriptografar o arquivo.");
        }

    }
}
