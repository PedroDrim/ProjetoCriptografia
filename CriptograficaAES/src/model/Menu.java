package model;

import Controller.AsymmetricKeyController;
import Controller.KeyWalletFileController;
import Controller.MensagemFileController;

import java.security.Key;

/**
 * Classe de menu
 */
public class Menu {

    /**
     * Metodo para criptografar um arquivo
     * @param inputOriginalFile Nome do arquivo a ser criptografado
     * @param outputEncriptedFile Nome do resultado criptografado
     * @param keyWalletPrefix Padrao das chaves privada do remetente e publica do destinatario (resp.)
     */
    public void criarMessageFile(String inputOriginalFile, String outputEncriptedFile, String keyWalletPrefix) {
        FileManager fileManager = new MensagemFileController();

        String rk0 = keyWalletPrefix + ".rk0";
        String dk1 = keyWalletPrefix + ".dk1";
        KeyWallet keyWallet = KeyWalletFileController.importKeyWallet(rk0, dk1);

        fileManager.criptografar(inputOriginalFile, outputEncriptedFile, keyWallet);
    }

    /**
     * Metodo para descriptografar um arquivo criptografado anteriormente por esse algoritmo
     * @param inputEncriptedFile Nome do arquivo criptografado a ser descriptografado
     * @param outputOriginalFile Nome do resultado descriptografado
     * @param keyWalletPrefix Padrao das chaves privada do destinatario e publica do remetente (resp.)
     */
    public void reverterMessageFile(String inputEncriptedFile, String outputOriginalFile, String keyWalletPrefix) {
        FileManager fileManager = new MensagemFileController();

        String rk0 = keyWalletPrefix + ".rk0";
        String dk1 = keyWalletPrefix + ".dk1";
        KeyWallet keyWallet = KeyWalletFileController.importKeyWallet(rk0, dk1);

        fileManager.descriptografar(inputEncriptedFile, outputOriginalFile, keyWallet);
    }

    /**
     * Metodo para gerar e exportar um pair de 'KeyWallet'
     * @param remetentePrefix prefixo do remetente
     * @param destinatarioPrefix prefixo do destinatario
     */
    public void pairWallet(String remetentePrefix, String destinatarioPrefix) {
        KeyWallet[] wallets = this.generatePairWallet();
        KeyWalletFileController.exportKeyWallet(wallets[0], remetentePrefix);
        KeyWalletFileController.exportKeyWallet(wallets[1], destinatarioPrefix);

    }

    /**
     * Metodo para gerar um conjunto de 'KeyWallet' para o remetente e o destinatario (resp.)
     * @return conjunto de 'KeyWallet' para o remetente e o destinatario (resp.)
     */
    private KeyWallet[] generatePairWallet() {
        KeyManager keyManager = new AsymmetricKeyController();
        Key[] keysA = keyManager.gerarChave(null);

        keyManager = new AsymmetricKeyController();
        Key[] keysB = keyManager.gerarChave(null);

        KeyWallet[] keyWallets = new KeyWallet[2];
        keyWallets[0] = new KeyWallet(keysA[0], keysB[1]);
        keyWallets[1] = new KeyWallet(keysB[0], keysA[1]);

        return keyWallets;
    }

    /**
     * Metodo para gerar um 'KeyWallet' propria
     * @return uma 'KeyWallet' propria
     */
    public KeyWallet generateSelfWallet() {
        KeyManager keyManager = new AsymmetricKeyController();
        Key[] keys = keyManager.gerarChave(null);

        KeyWallet keyWallet = new KeyWallet(keys[0], keys[1]);
        return keyWallet;
    }
}
