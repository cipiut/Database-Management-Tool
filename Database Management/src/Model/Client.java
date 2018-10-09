package Model;

import DataAcess.AbstractDAO;

public class Client extends AbstractDAO<Client> {
    private int idClient;
    private int age;
    private String nume;
    private String adresa;
    private String email;

    public Client(int age, String nume, String adresa, String email) {
        this.age = age;
        this.nume = nume;
        this.adresa = adresa;
        this.email = email;
    }

    public Client() {

    }

    public int getIdClient() {
        return idClient;
    }

    public int getAge() {
        return age;
    }

    public String getNume() {
        return nume;
    }

    public String getAdresa() {
        return adresa;
    }

    public String getEmail() {
        return email;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}