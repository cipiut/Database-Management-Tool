package Model;

import DataAcess.AbstractDAO;

public class Comanda extends AbstractDAO<Comanda> {
    private int idComanda;
    private int idClient;
    private int idProdus;
    private int cantitate;

    public Comanda( int idClient, int idProdus,int cantitate) {
        this.idClient = idClient;
        this.idProdus = idProdus;
        this.cantitate = cantitate;
    }

    public Comanda() {
    }

    public void setIdComanda(int idComanda) {
        this.idComanda = idComanda;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public void setIdProdus(int idProdus) {
        this.idProdus = idProdus;
    }

    public void setCantitate(int cantitate) {
        this.cantitate = cantitate;
    }

    public int getIdComanda() {
        return idComanda;
    }

    public int getIdClient() {
        return idClient;
    }

    public int getIdProdus() {
        return idProdus;
    }

    public int getCantitate() {
        return cantitate;
    }

}