package Account;

import java.io.Serializable;

public class SavingAccount extends Account implements Serializable {
    private boolean retragere;
    private boolean depunere;
    protected static final double DOBANDA=(double) 8/100;

    public SavingAccount(int cnp,double bani) {
        super(cnp,bani);
        tip="Saving";
        this.bani = bani+(bani*SavingAccount.DOBANDA);
        if(bani>0)depunere=true;
        Account.id++;
    }

    public boolean isRetragere() {
        return retragere;
    }

    public boolean isDepunere() {
        return depunere;
    }

    public void setRetragere(boolean retragere) {
        this.retragere = retragere;
    }

    public void setDepunere(boolean depunere) {
        this.depunere = depunere;
    }
}