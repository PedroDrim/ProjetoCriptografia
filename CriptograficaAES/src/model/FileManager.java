package model;

/**
 * Interface para geracao de arquivos criptografados
 */
public interface FileManager {

    /**
     * Metodo para criptografar um arquivo
     * @param arquivoOriginal Nome do arquivo a ser criptografado
     * @param arquivoCriptografado Nome do resultado criptografado
     * @param keyWallet 'KeyWallet' contendo chaves privada do remetente e publica do destinatario (resp.)
     */
    void criptografar(String arquivoOriginal, String arquivoCriptografado, KeyWallet keyWallet);

    /**
     * Metodo para descriptografar um arquivo
     * @param arquivoCriptografado Nome do arquivo a ser descriptografado
     * @param arquivoDescriptografado Nome do resultado descriptografado
     * @param keyWallet 'KeyWallet' contendo chaves privada do destinatario e publica do remetente (resp.)
     */
    void descriptografar(String arquivoCriptografado, String arquivoDescriptografado, KeyWallet keyWallet);
}
