package Bank;

import Account.Account;
import Person.Person;
import javafx.collections.ObservableList;

public interface BankProc {
    /**
     * Adauga un cont in baza de date
     * @pre a!=null
     * @post isWellFormed();
     */
    void add(Account a);

    /**
     * Sterge un cont din baza de date
     * @pre a!=null
     * @post isWellFormed();
     */
    void remove(Account a);

    /**
     * Adauga o noua persoana in baza de date
     * @pre a!=null
     * @post isWellFormed();
     */
    void add(Person a);

    /**
     * Sterge o persoana din baza de date
     * @pre a!=null
     * @post isWellFormed();
     */
    void remove(Person a);

    /**
     * Addauga bani intr-un cont
     * @pre bani>=0
     * @post isWellFormed();
     */
    void addMoney(int iban,int bani);

    /**
     * Sterge bani dintr-un cont
     * @pre bani>=0 ;
     * @post isWellFormed();
     */
    void withdraw(int iban,int bani);

    /**
     * Returneaza baza de date pentru Persoana sau Cont
     * @pre n==1 || n==2
     */
    ObservableList printBank(byte n);
}