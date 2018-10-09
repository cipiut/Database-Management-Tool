package Account;

import java.io.Serializable;
import java.util.Observable;

public abstract class Account extends Observable implements Serializable{
    protected int iban=0;
    protected int cnp;
    protected double bani;
    protected String tip;
    protected static int id=0 ;

    public Account(int cnp, double bani) {
        this.cnp = cnp;
        this.iban=id+1;
    }

    public Account(int iban) {
        this.iban = iban;
    }

    public int getCnp() {
        return cnp;
    }

    public int getIban() {
        return iban;
    }

    public String getTip() {
        return tip;
    }

    public void setBani(double bani) {
        if(this.getTip().equals("Saving"))this.bani = bani+(bani*SavingAccount.DOBANDA);
        else this.bani = bani;
        setChanged();
        notifyObservers("S-a modificat numerarul\nNoul numerar: "+String.format("%.3f",bani));
    }

    public double getBani() {
        return bani;
    }

    public static void setId(int id){
        Account.id=id;
    }

    public void setIban(int iban) {
        this.iban = iban;
    }
}