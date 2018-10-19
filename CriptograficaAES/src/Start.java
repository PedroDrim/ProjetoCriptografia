import Controller.KeyWalletFileController;
import model.KeyWallet;
import model.Menu;

/**
 * Classe principal do sistema
 */
public class Start {

    /**
     * Metodo de inicializacao
     * @param args parametros de linha de comando
     */
    public static void main (String[] args) {

        String remetentePrefix = "ana";
        String destinatarioPrefix = "jose";
        String inputOriginalFile = "hp.jpg";
        String outputEncriptedFile = "message.zip";
        String outputFile = "lovecraft";

        Menu menu = new Menu();

        menu.pairWallet(remetentePrefix, destinatarioPrefix);
        menu.criarMessageFile(inputOriginalFile, outputEncriptedFile, remetentePrefix);
        menu.reverterMessageFile(outputEncriptedFile,outputFile , destinatarioPrefix);
    }

}
