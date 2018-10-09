package BusinessLayer;
import DataAcess.AbstractDAO;
import Model.Client;
import Model.Comanda;
import Model.Produs;
import Presentation.GUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Business {
    private AbstractDAO dao;

    public Business(AbstractDAO dao){
        this.dao=dao;
    }

    /**
     * Functia getTable apeleaza functia de generare a tabelului din AbstractDao si imi aduce
     * tabelul cu coloanele din baza de date
     * @return TableView tabelul creat pentru GUI
     */
    public TableView getTable(){
        return dao.tabel();
    }

    /**
     * Functia findAll apeleaza functia findAll pentru a face un Select* From dao
     * @return ObservableList lista cu toate elementele returnate din tabela
     */
    public ObservableList findAll(){
        return FXCollections.observableList(dao.findAll());
    }

    /**
     * Functia findById apeleaza functia findById pentru a face un Select* From dao Where idDao = id
     * @param  id paramettrul care ne arata dupa ce id sa facem extragerea din tabela
     * @return ObservableList lista cu toate elementele returnate din tabela
     */
    public ObservableList findById(int id){
        List t = new ArrayList();
        t.add(dao.findById(id));
        return FXCollections.observableList(t);
    }

    /**
     * Functia ma ajuta sa inserez in tabela dao . Avem nevoie de un Obiect de tip AbstractDao ca parametru
     * Mai de parte se apeleaza functia insert din AbstractDao .
     * In cazul in care avem un obiect de tip comanda trebuie sa executam un update pe tabela de Produs
     * @param o parametru abstract pentru reflexie in funtia insert din AbstractDao
     */
    public void insert(AbstractDAO o){
        if(dao instanceof Comanda){
            Comanda c= (Comanda) o;
            AbstractDAO p = new Produs().findById(c.getIdProdus());
            AbstractDAO cl = new Client().findById(c.getIdClient());
            if(p==null) {
                GUI.err="EroareId Produs inexistent\n";
                return;
            }
            if(cl==null){
                GUI.err="EroareId Client inexistent\n";
                return;
            }
            Produs pr=(Produs)p;

            int cantitateP =pr.getCantitate();
            int cantitateC =c.getCantitate();

            if(cantitateP-cantitateC<0){
                GUI.err="Eroare Cantitate depasita\n";
                return;
            }
            String[] f={"cantitate"};
            String[] val={String.valueOf(cantitateP-cantitateC)};
            p.update(c.getIdProdus(),f,val);
            dao.insert(c);
        }
        else dao.insert(o);
    }


    /**
     * Functia de update ma ajuta sa actualizez date din tabele. Are trei paramentri
     * Funcita apeleaza din AbstractDao functia de update.
     * In cazul in care avem un dao de tip comanda trebuie actualizate si coloana cantitate si cea de
     * idProdus .
     * @param id imi indentifica id-ul obiectului care trebuie actualizat
     * @param fields imi arata numele Field-urilor care trebuie actualizate
     * @param values imi arata cu ce valori se vor actualiza coloanele
     */
    public void update(int id ,String[] fields, String[] values){
        if(dao.findById(id)==null) {
            GUI.err="Eroare element inexistent\n";
            return;
        }
        if(dao instanceof Comanda){
            String v;
            AbstractDAO com = ((Comanda) dao).findById(id);
            AbstractDAO pDao = new Produs().findById(((Comanda) com).getIdProdus());
            int poz=cauta(fields,"idProdus");
            if(cauta(fields,"idProdus")!=-1 && cauta(fields,"cantitate")!=-1){
                update(id,new String[]{"idProdus"},new String[]{values[cauta(fields,"idProdus")]});
                update(id,new String[]{"cantitate"},new String[]{values[cauta(fields,"cantitate")]});
                return;
            }
            if(poz!=-1){
                if(pDao.findById(Integer.valueOf(values[poz]))==null){
                    GUI.err="Eroare IdProdus inexistent \n";
                    return;
                }
                Produs aux =(Produs)pDao.findById(((Comanda) com).getIdProdus());
                v=String.valueOf(((Comanda) com).getCantitate()+aux.getCantitate());
                pDao.update(aux.getIdProdus(),new String[]{"cantitate"},new String[]{v});
                aux=(Produs)pDao.findById(Integer.valueOf(values[poz]));
                if(aux.getCantitate()-((Comanda) com).getCantitate()<0){
                    GUI.err="Eroare Cantitate depasita\n";
                    return;
                }
                v=String.valueOf(aux.getCantitate()-((Comanda) com).getCantitate());
                pDao.update(aux.getIdProdus(),new String[]{"cantitate"},new String[]{v});
            }
            poz=cauta(fields,"cantitate");
            if(poz!=-1){
                Produs aux =(Produs) pDao.findById(((Comanda) com).getIdProdus());
                int rez=aux.getCantitate()+(((Comanda) com).getCantitate()-Integer.valueOf(values[poz]));
                if(rez<0){
                    GUI.err="Eroare Cantitate depasita\n";
                    return;
                }
                pDao.update(aux.getIdProdus(),new String[]{"cantitate"}, new String[]{String.valueOf(rez)});
            }
            dao.update(id,fields,values);
        }
        else if(dao.findById(id)!=null)dao.update(id,fields,values);
    }

    /**
     * Functia de delete imi sterge din tabela elementul cu idDao id.
     * Pentru a sterge elemente din comanda se va reactualiza la valoarea initiala
     * celelate cantitati din tabela Produs
     * @param id imi indentifica id-ul obiectului care trebuie actualizat
     */
    public void delete(int id){
        if(dao.findById(id)==null) {
            GUI.err="Eroare element inexistent\n";
            return;
        }
        if(dao instanceof Comanda){
            Comanda com = (Comanda) dao.findById(id);
            Produs pDao=new Produs().findById(com.getIdProdus());
            pDao.update(pDao.getIdProdus(),new String[]{String.valueOf("cantitate")},
                    new String[]{String.valueOf(pDao.getCantitate()+com.getCantitate())});
        }
        if(dao.findById(id)==null){
            GUI.err="Eroare delete inexistent\n";
            return;
        }
        dao.delete(id);
    }

    /**
     * Functia de factura este cea care imi arata totalul de plata al unei comnzi sub forma de chitanta
     * Functia in interior creaza un fisier de tip .txt in care se adauga totalul de plata si produsul cumparat
     * @param id imi indentifica id-ul comenzi care trebuie facturata
     */
    public void factura(int id){
        String file="Comanda"+id+".txt";
        Comanda t = (Comanda) dao.findById(id);
        if(t==null){
            GUI.err="Eroare nu exista aceasta comanda\n";
            return;
        }
        Produs pDao = new Produs().findById(t.getIdProdus());
        Client cDao = new Client().findById(t.getIdClient());
        String op = t.getCantitate()+"  X  "+ pDao.getPret();
        String rez = "TOTAL : "+(t.getCantitate()*pDao.getPret());
        String line = "------------------------------";
        String client ="Nume: "+ cDao.getNume() +"\nAdresa: "+cDao.getAdresa()+"\n\n";
        String str="            Factura\n\n"+client+pDao.getNume();
        for(int i=op.length()+pDao.getNume().length();i<line.length();i++)str+=" ";
        str+=(op+"\n"+line+"-\n");
        for(int i=rez.length();i<line.length();i++)str+=" ";
        str+=rez;
        byte[] strToBytes = str.getBytes();
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(strToBytes);
            out.close();
        } catch (IOException e) {

        }
        GUI.err="Factura generata cu succes\n";
    }

    private int cauta(String[] x,String y){
        for(int i=0;i<x.length;i++)if(y.equals(x[i]))return i;
        return -1;
    }

}