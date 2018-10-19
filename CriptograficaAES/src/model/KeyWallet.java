package model;

import java.security.Key;

public class KeyWallet {

    private Key chavePublicaDestinatario;
    private Key chavePrivadaRemetente;

    public KeyWallet(Key chavePrivadaRemetente, Key chavePublicaDestinatario) {

        this.chavePublicaDestinatario = chavePublicaDestinatario;
        this.chavePrivadaRemetente = chavePrivadaRemetente;
    }

    public Key getChavePublicaDestinatario() {
        return this.chavePublicaDestinatario;
    }

    public Key getChavePrivadaRemetente() {
        return this.chavePrivadaRemetente;
    }

}
