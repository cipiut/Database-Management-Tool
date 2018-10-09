package Model;

import DataAcess.AbstractDAO;

public class Produs extends AbstractDAO<Produs> {
    private int idProdus;
    private String nume;
    private int cantitate;
    private int pret;

    public Produs( String nume, int cantitate, int pret) {
        this.nume = nume;
        this.cantitate = cantitate;
        this.pret = pret;
    }

    public Produs() {
    }

    public int getIdProdus() {
        return idProdus;
    }

    public String getNume() {
        return nume;
    }

    public int getCantitate() {
        return cantitate;
    }

    public int getPret() {
        return pret;
    }

    public void setIdProdus(int idProdus) {
        this.idProdus = idProdus;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setCantitate(int cantitate) {
        this.cantitate = cantitate;
    }

    public void setPret(int pret) {
        this.pret = pret;
    }
}