package JUnit;

import Account.SpendingAccount;
import Bank.Bank;
import Person.Person;
import Bank.BankProc;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Testare {

    private BankProc bank=new Bank();

    @Test
    public void addPerson(){
        Person p = new Person(1,"asf",123);
        bank.add(p);
        assertEquals(bank.printBank((byte)1).size(),1);
    }

    @Test
    public void addAccount(){
        Person p = new Person(1,"asf",123);
        bank.add(p);
        SpendingAccount p1 = new SpendingAccount(1,3);
        bank.add(p1);
        assertEquals(bank.printBank((byte)2).size(),1);
    }

    @Test
    public void removePerson(){
        bank=new Bank();
        Person p = new Person(1,"asf",123);
        bank.remove(p);
        assertEquals(bank.printBank((byte)1).size(),0);
    }

    @Test
    public void removeAccount(){
        bank=new Bank();
        SpendingAccount p = new SpendingAccount(1);
        bank.remove(p);
        assertEquals(bank.printBank((byte)2).size(),0);
    }

} 