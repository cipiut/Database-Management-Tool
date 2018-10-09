package Person;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

public class Person implements Serializable, Observer {
    private int cnp;
    private String nume;
    private String varsta;

    public int hashCode(){
        return cnp;
    }

    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || obj.getClass()!= this.getClass()) return false;
        Person p = (Person) obj;
        return (p.cnp == this.cnp);
    }

    public Person(int cnp, String nume,int varsta) {
        this.cnp = cnp;
        this.varsta = String.valueOf(varsta);
        this.nume = nume;
    }

    public Person(int cnp) {
        this.cnp = cnp;
    }

    public int getCnp() {
        return cnp;
    }

    public String getVarsta() {
        return varsta;
    }

    public String getNume() {
        return nume;
    }

    public void setVarsta(String varsta) {
        this.varsta = varsta;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println(cnp +" "+nume+". " + arg);
    }
}