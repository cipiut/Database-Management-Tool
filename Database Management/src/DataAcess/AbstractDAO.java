package DataAcess;

import Presentation.GUI;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDAO<T> {
    private final Class<T> clasa;

    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.clasa = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Aici se creaza blocul de select in functie de un field (Pentru acest proiect field-ul este intoteauna id)
     * @param field colana dupa care se face cautarea in tabela
     * @return String returneaza comanda pentru Select
     */
    private String select(String field){
        return "Select * From "+clasa.getSimpleName()+" Where "+field+" =";
    }

    /**
     * Aici se creaza blocul de select all
     * @return String returneaza comanda pentru Select *
     */
    private String select(){
        return "Select * From "+clasa.getSimpleName();
    }

    /**
     * Aici se creaza blocul de delete in functie de un field (Pentru acest proiect field-ul este intoteauna id)
     * @param field colana dupa care se face cautarea in tabela
     * @return String returneaza comanda pentru Delete
     */
    private String delete(String field){
        return "DELETE FROM " +clasa.getSimpleName()+
                " WHERE " +field+ " = " ;
    }

    /**
     * Aici se creaza blocul de insert in functie de un obiect trimis ca parametru
     * @param o obiectul care va fi introdus in tabela
     * @return String returneaza comanda pentru Insert
     */
    private String insertS(Object o){
        String sql="INSERT INTO " +o.getClass().getSimpleName()+" (";
        Field[] x = o.getClass().getDeclaredFields();
        Object[] v = new Object[x.length];
        v[0]=1;
        for(int i=1;i<x.length;i++){
            x[i].setAccessible(true);
            try {
                sql+=x[i].getName();
                if(i!=x.length-1)sql+=",";
                else sql+=")\n";
                v[i]=x[i].get(o);
            } catch (Exception e) {
                System.out.println("Nu merge");
            }
        }
        String values="VALUES (";
        for(int i=1;i<x.length;i++) {
            if(x[i].getType().isAssignableFrom(Integer.TYPE))values+=v[i];
            else values+=("'"+v[i]+"'");
            if(i!=x.length-1)values+=",";
            else values+=")";
        }
        return sql+values;
    }

    /**
     * Functia imi returneaza blocul pentru update in functie de id , coloane si valori
     * @param id imi indentifica id-ul obiectului care trebuie actualizat
     * @param col imi arata numele Field-urilor care trebuie actualizate
     * @param val imi arata cu ce valori se vor actualiza coloanele
     * @return String
     */
    private String updateS(int id,String[] col ,String[] val){
        String update="UPDATE "+clasa.getSimpleName();
        String set="\nSet ";
        String where="\nWhere "+ clasa.getDeclaredFields()[0].getName()+" = "+id;

        Field[] x = clasa.getDeclaredFields();
        for(int i=0;i<x.length;i++) {
            for (int j = 0; j < col.length; j++) {
                if (x[i].getName().equals(col[j])) {
                    if(x[i].getType().isAssignableFrom(Integer.TYPE)) set += (col[j] + "=" + val[j]+",");
                    else set += (col[j] + "=" + "'" + val[j] + "'"+",");
                }
            }
        }
        set=set.substring(0,set.length()-1);
        return update + set + where;
    }

    /**
     * Aceasta este functia cu care pot sa scot toate obiectele dintr-o tabela
     * Functia aceasta folosete reflexivitate pentru parcuregerea generica a tabelei
     * @param set parametrul este un ResultSet care imi retine toate rezultatele din tabela
     * @return List<T> toate obiectele din tabela sunt returnate printr-o lista
     */
    private List<T> getT(ResultSet set){
        List<T> allObjects = new ArrayList<>();
        try{
            while (set.next()){
                T object = clasa.newInstance();
                for(Field f: clasa.getDeclaredFields()){
                    Object v = set.getObject(f.getName());
                    PropertyDescriptor p = new PropertyDescriptor(f.getName(),clasa);
                    Method m = p.getWriteMethod();
                    m.invoke(object,v);
                }
                allObjects.add(object);
            }
        } catch (Exception e) {
            System.out.println("Err getT");
        }
        return allObjects;
    }

    /**
     * Functia findById foloseste parametru generic pentru a intra in mai multe tabele si pentru a scoate toate
     * elementele tabelei
     * @param id imi indentifica id-ul obiectului care trebuie gasit
     * @return T un obiect de tip T care poate reprezenta un Client , Comanda sau Produs
     */
    public T findById(int id){
        Connection c = Conexiune.getConnection();
        try{
            ResultSet set;
            PreparedStatement statement = (PreparedStatement) c.prepareStatement(select(clasa.getDeclaredFields()[0].getName())+id);
            set= statement.executeQuery();
            return getT(set).get(0);
        } catch (Exception e) {
            GUI.err="Eroare findByid " +clasa.getSimpleName()+"\n";
            return null;
        }
    }

    /**
     * Functia de mai jos doar apeleaza functia getT pe un Select* pentru a lua toatele elementele
     * din tabela
     * @return List parametru care contine toate obiectele unei tabele de tip T
     */
    public List<T> findAll(){
        Connection c = Conexiune.getConnection();
        try{
            ResultSet set;
            PreparedStatement statement = (PreparedStatement) c.prepareStatement(select());
            set= statement.executeQuery();
            return getT(set);
        } catch (Exception e) {
            GUI.err="Eroare findAll " +clasa.getSimpleName()+"\n";
            return null;
        }
    }

    /**
     * Se realizeaza delete pe elementele din tabela T in functie de un id
     * @param id imi indentifica id-ul obiectului care trebuie sters
     */
    public void delete(int id){
        Connection c = Conexiune.getConnection();
        try{
            PreparedStatement statement = (PreparedStatement) c.prepareStatement(delete(clasa.getDeclaredFields()[0].getName())+id);
            statement.executeUpdate();
        } catch (Exception e) {
            GUI.err="Eroare Delete " +clasa.getSimpleName()+"\n";
            return;
        }
        GUI.err="Success Delete " +clasa.getSimpleName()+"\n";

    }

    /**
     * Se realizeaza operatia de inserare in tabela a unui obiect
     * @param t parametru abstract pentru reflexie in functia insertS
     */
    public void insert(Object t){
        Connection c = Conexiune.getConnection();
        try{
            PreparedStatement statement = (PreparedStatement) c.prepareStatement(insertS(t));
            statement.executeUpdate();
        } catch (Exception e) {
            GUI.err="Eroare Insert " +clasa.getSimpleName()+"\n";
            return;
        }
        GUI.err="Success Insert " +clasa.getSimpleName()+"\n";
    }

    /**
     * Se realizeaza un update pe baza mai multor coloane si valori intr-o anumita tabela
     * @param id imi indentifica id-ul obiectului care trebuie sters
     * @param fields imi arata numele coloanelor care trebuie actualizate
     * @param values imi arata cu ce valori se vor actualiza coloanele
     */
    public void update(int id ,String[] fields, String[] values){
        Connection c = Conexiune.getConnection();
        try{
            PreparedStatement statement = (PreparedStatement) c.prepareStatement(updateS(id,fields,values));
            statement.executeUpdate();
        } catch (Exception e) {
            GUI.err="Eroare Update " +clasa.getSimpleName();
            return;
        }
        GUI.err="Success Update " +clasa.getSimpleName()+"\n";
    }

    /**
     * Prin reflexivitate se returneaza un tabel in functie de parametrul nostru generic
     * @return TableView este tabelul pe care il vedem pe interfata
     */
    public TableView tabel(){
        TableView<T> tab= new TableView<>();
        Field[] x = clasa.getDeclaredFields();
        int i=0;
        TableColumn[] tableColumns =new TableColumn[x.length];
        for(Field s:x){
            tableColumns[i]=new TableColumn(s.getName());
            tableColumns[i++].setCellValueFactory(new PropertyValueFactory<T,String>(s.getName()));
        }
        tab.getColumns().addAll(tableColumns);
        return tab;
    }
}
