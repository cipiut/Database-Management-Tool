package Bank;

import Account.Account;
import Account.SavingAccount;
import Person.Person;
import GUI.GUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @invariant isWellFormed();
 */
public class Bank implements BankProc,Serializable{
    private HashMap<Person,List<Account>> bank ;

    public Bank(){
        bank=new HashMap<>();
    }

    @Override
    public void add(Account a) {
        assert a!=null;
        int cnp = a.getCnp();
        for(Map.Entry p : bank.entrySet()){
            if(((Person)p.getKey()).getCnp()==cnp){
                a.addObserver((Person)p.getKey());
                ((LinkedList<Account>) p.getValue()).add(a);
            }
        }
        assert isWellFormed();
    }

    @Override
    public void remove(Account a) {
        assert a!=null;
        int iban = a.getIban();
        for(Map.Entry p : bank.entrySet()){
            for(Account f :(LinkedList<Account>) p.getValue())
            if(f.getIban()==iban){
                ((LinkedList<Account>) p.getValue()).remove(f);
            }
        }
        assert isWellFormed();
    }

    @Override
    public void add(Person a) {
        assert a!=null;
        int cnp = a.getCnp();
        for(Map.Entry p : bank.entrySet())
            if(((Person)p.getKey()).getCnp()==cnp) return;
        bank.put(a,new LinkedList());
        assert isWellFormed();
    }

    @Override
    public void remove(Person a) {
        assert a!=null;
        int cnp = a.getCnp();
        for(Map.Entry p : bank.entrySet())
            if(((Person)p.getKey()).getCnp()==cnp){
                bank.remove(p.getKey());
                return;
            }
        assert isWellFormed();
    }

    @Override
    public void addMoney(int iban, int bani) {
        assert bani>=0;
        for(Map.Entry p : bank.entrySet()) {
            List<Account> c = (LinkedList)p.getValue();
            for (Account f : c)
                if (f.getIban() == iban) {
                    if (f.getTip().equals("Saving") && !((SavingAccount) f).isDepunere()) {
                        f.setBani(f.getBani() + bani);
                        ((SavingAccount) f).setDepunere(true);
                    } else if (f.getTip().equals("Spending")) f.setBani(f.getBani() + bani);
                    else GUI.err = "Nu se poate depune in Cont\n";
                }
        }
        assert isWellFormed();
    }

    @Override
    public void withdraw(int iban ,int bani) {
        assert bani>=0;
        for(Map.Entry p : bank.entrySet()) {
            List<Account> c = (LinkedList)p.getValue();
            for (Account f : c)
                if (f.getIban() == iban) {
                    if (f.getTip().equals("Saving") && !((SavingAccount) f).isRetragere() && f.getBani() >= bani) {
                        f.setBani(f.getBani() - bani);
                        ((SavingAccount) f).setRetragere(true);
                    } else if (f.getTip().equals("Spending") && f.getBani() >= bani) f.setBani(f.getBani() - bani);
                    else GUI.err = "Nu se poate retrage din Cont\n";
                }
        }
        assert isWellFormed();
    }

    @Override
    public ObservableList printBank(byte n) {
        assert n==1 || n==2;
        List list;
        if(n==1) {
            list=new LinkedList<Person>();
            for (Map.Entry p : bank.entrySet()) list.add((Person)p.getKey());
        }
        else if(n==2) {
            list=new LinkedList<Account>();
            for (Map.Entry p : bank.entrySet())
                for(Account f : (LinkedList<Account>)p.getValue())list.add(f);
        }
        else return null;
        return FXCollections.observableList(list);
    }

    public TableView getTable(String c)throws Exception{
        Class cls;
        TableView tab;
        switch (c) {
            case "Person":
                cls = Class.forName("Person.Person");
                tab = new TableView<Person>();
                break;
            case "Account":
                cls = Class.forName("Account.Account");
                tab = new TableView<Account>();
                break;
            default:
                return null;
        }
        Field[] x = cls.getDeclaredFields();
        int i = 0;
        TableColumn[] tableColumns;
        if(c.equals("Person"))tableColumns = new TableColumn[x.length];
        else tableColumns = new TableColumn[x.length-1];
        for (Field s : x) {
            if(s.getName().equals("id"))break;
            tableColumns[i] = new TableColumn(s.getName());
            if (c.equals("Person")) tableColumns[i++].setCellValueFactory(new PropertyValueFactory<Person, String>(s.getName()));
            else if(c.equals("Account"))tableColumns[i++].setCellValueFactory(new PropertyValueFactory<Account, String>(s.getName()));
        }
        tab.getColumns().addAll(tableColumns);
        return tab;
    }

    public int maxIban(){
        int max=0;
        for(Map.Entry p : bank.entrySet()){
            for(Account c : (LinkedList<Account>)p.getValue()){
                if(c.getIban()>max)max=c.getIban();
            }
        }
        return max;
    }

    public void addObs(){
        for(Map.Entry p : bank.entrySet()){
            for(Account a : (LinkedList<Account>) p.getValue()){
                a.addObserver((Person)p.getKey());
            }
        }
    }

    private boolean isWellFormed(){
        for(Map.Entry p : bank.entrySet()){
            for(Account a : (LinkedList<Account>)p.getValue()){
                if(a.getBani()<0 || a.getCnp()!=((Person)p.getKey()).getCnp())return false;
            }
        }
        return true;
    }

}