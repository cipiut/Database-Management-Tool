package Account;

import java.io.Serializable;

public class SpendingAccount extends Account implements Serializable{

    public SpendingAccount(int cnp,double bani) {
        super(cnp,bani);
        tip="Spending";
        this.bani = bani;
        Account.id++;
    }

    public SpendingAccount(int iban) {
        super(iban);
    }
}