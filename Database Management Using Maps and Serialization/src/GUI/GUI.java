package GUI;

import Account.Account;
import Account.SavingAccount;
import Account.SpendingAccount;
import Bank.Bank;
import Bank.BankProc;
import Person.Person;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;


public class GUI extends Application implements EventHandler<ActionEvent> {
    private Stage persoanaStage,contStage;
    private Button persoana, cont;
    public static String err="";
    public BankProc control = new Bank();

    private void mainStage(){//pentru initializarea variabilelor
        deserialize();
        persoana = new Button("Persoana");
        cont = new Button("Cont");
        persoana.setOnAction(this);
        cont.setOnAction(this);
    }

    @Override
    public void start(Stage mainStage) {//throws Exception {

        mainStage();//initializam variabilele de clasa
        mainStage.setTitle("Database Management");
        mainStage.setResizable(false);
        HBox hBox = new HBox();
        Scene scene = new Scene(hBox,300,150);
        hBox.getChildren().addAll(persoana,cont);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);

        mainStage.setScene(scene);
        mainStage.show();
        mainStage.setOnCloseRequest(windowEvent -> {
            serialize();
            System.exit(0);
        });//se inchide programul
    }

    private Stage constructPersoanaStage() throws Exception {
        Stage stage = new Stage();
        stage.setTitle("Persoane");

        TableView x=((Bank) control).getTable("Person");
        ObservableList<TableColumn> columns = x.getColumns();

        TableColumn nume=columns.get(1);
        TableColumn varsta=columns.get(2);

        TextArea errClient= new TextArea();
        errClient.setEditable(false);
        errClient.setBackground(Background.EMPTY);


        TextField cnpT=new TextField();
        TextField ageT = new TextField();
        TextField numeT = new TextField();


        Label cnpL = new Label("cnp: ");
        Label ageL = new Label("age: ");
        Label numeL = new Label("nume: ");


        Button insert = new Button("insert");
        Button delete = new Button("delete");

        Button show = new Button("show");
        x.setEditable(true);

        nume.setCellFactory(TextFieldTableCell.forTableColumn());
        varsta.setCellFactory(TextFieldTableCell.forTableColumn());

        nume.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent>() {
            @Override
            public void handle(TableColumn.CellEditEvent event) {
               Person pers = (Person) x.getSelectionModel().getSelectedItem();
               try{
                   if(!hasNum(event.getNewValue().toString()))throw new Exception();
                   x.refresh();
                   errClient.appendText("Nu se poate intrdouce numele\n");
               }catch(Exception e){
                   pers.setNume(event.getNewValue().toString());
               }
            }
        });

        varsta.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent>() {
            @Override
            public void handle(TableColumn.CellEditEvent event) {
                Person pers = (Person) x.getSelectionModel().getSelectedItem();
                String x1 = pers.getVarsta();
                try {
                    int n= Integer.parseInt(event.getNewValue().toString());
                    pers.setVarsta(event.getNewValue().toString());
                    if(n<0)throw new Exception();
                }catch(Exception e){
                    pers.setVarsta(x1);
                    errClient.appendText("Nu se poate introduce varsta\n");
                    x.refresh();
                }
            }
        });

        insert.setOnAction(event -> {
            err="";
            String[] cl= new String[3];
            cl[1]=ageT.getText();
            cl[2]=numeT.getText();
            for(int i=1;i<cl.length;i++)if(cl[i]==null || cl[i].equals(""))return;
            int n,cnp;
            try{
                n=Integer.parseInt(ageT.getText());
                cnp=Integer.parseInt(cnpT.getText());
                if(hasNum(numeT.getText()))throw new Exception();
            }catch (Exception e){
                errClient.appendText("Eroare la introducerea datelor\n");
                return;
            }
            if(n<0)return;
            control.add(new Person(cnp,cl[2],n));
            errClient.appendText(err);
        });


        delete.setOnAction(event -> {
            err="";
            String[] cl= new String[5];
            cl[0]=cnpT.getText();
            int n;
            try{
                n=Integer.parseInt(cnpT.getText());
            }catch (Exception e){
                errClient.appendText("Eroare la introducerea datelor\n");
                return;
            }
            if(n<0)return;
            control.remove(new Person(n));
            errClient.appendText(err);
        });


        show.setOnAction(event -> {
            x.setItems(control.printBank((byte)1));
        });

        GridPane grid = new GridPane();
        grid.add(cnpL,0,0);
        grid.add(cnpT,1,0);
        grid.add(ageL,0,1);
        grid.add(ageT,1,1);
        grid.add(numeL,0,2);
        grid.add(numeT,1,2);

        grid.setVgap(5);
        grid.setHgap(5);

        VBox vBox =new VBox();

        HBox hBox = new HBox();
        hBox.getChildren().addAll(insert,delete,show);
        hBox.setSpacing(5);

        vBox.getChildren().addAll(grid,hBox,x,errClient);
        vBox.setPadding(new Insets(10,10,10,10));

        Scene scene =new Scene(vBox,610,520);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(windowEvent -> persoanaStage=null);

        return stage;
    }

    private Stage constructContStage() throws Exception {
        Stage stage = new Stage();
        stage.setTitle("Conturi");
        ChoiceBox acc = new ChoiceBox();
        acc.getItems().addAll("SavingAccount", "SpendingAccount");
        acc.setValue("SavingAccount");

        TableView x=((Bank) control).getTable("Account");

        TextArea errProdus= new TextArea();
        errProdus.setEditable(false);
        errProdus.setBackground(Background.EMPTY);


        TextField baniT = new TextField();
        TextField cnpT = new TextField();
        TextField ibanT = new TextField();

        Label choice = new Label("tip: ");
        Label baniL = new Label("bani: ");
        Label cnpL = new Label("cnp: ");
        Label ibanL = new Label("iban: ");

        Button insert = new Button("insert");
        Button retragere = new Button("retragere");
        Button delete = new Button("delete");
        Button depunere = new Button("depunere");
        Button show = new Button("show");

        GridPane grid = new GridPane();
        grid.add(cnpL,0,0);
        grid.add(cnpT,1,0);
        grid.add(ibanL,0,1);
        grid.add(ibanT,1,1);
        grid.add(baniL,0,2);
        grid.add(baniT,1,2);
        grid.add(choice,0,3);
        grid.add(acc,1,3);

        insert.setOnAction(event -> {
            err="";
            String[] cl= new String[3];
            cl[1]=baniT.getText();
            cl[2]=cnpT.getText();
            for(int i=1;i<cl.length;i++)if(cl[i]==null || cl[i].equals(""))return;
            int n,n2;
            try{
                n2=Integer.parseInt(baniT.getText());
                n=Integer.parseInt(cnpT.getText());
            }catch (Exception e){
                errProdus.appendText("Eroare la introducerea datelor\n");
                return;
            }
            if(n<0 ||n2<0)return;
            if(acc.getValue().toString().equals("SavingAccount"))control.add(new SavingAccount(n ,n2));
            if(acc.getValue().toString().equals("SpendingAccount"))control.add(new SpendingAccount(n ,n2));
            errProdus.appendText(err);
        });

        delete.setOnAction(event -> {
            err="";
            int n;
            try{
                n=Integer.parseInt(ibanT.getText());
                if(n<0)throw new Exception();
            }catch (Exception e){
                errProdus.appendText("Eroare la introducerea datelor\n");
                return;
            }
            control.remove(new SpendingAccount(n));
            errProdus.appendText(err);
        });

        retragere.setOnAction(event -> {
            err="";
            int n,n1;
            try{
                n=Integer.parseInt(ibanT.getText());
                n1=Integer.parseInt(baniT.getText());
                if(n<0 || n1<0)throw new Exception();
            }catch (Exception e){
                errProdus.appendText("Eroare la introducerea datelor\n");
                return;
            }
            control.withdraw(n,n1);
            x.refresh();
            errProdus.appendText(err);
        });

        depunere.setOnAction(event -> {
            err="";
            int n,n1;
            try{
                n=Integer.parseInt(ibanT.getText());
                n1=Integer.parseInt(baniT.getText());
                if(n<0 || n1<0)throw new Exception();
            }catch (Exception e){
                errProdus.appendText("Eroare la introducerea datelor\n");
                return;
            }
            control.addMoney(n,n1);
            x.refresh();
            errProdus.appendText(err);
        });

        show.setOnAction(event -> {
            x.setItems(control.printBank((byte)2));
        });


        grid.setVgap(5);
        grid.setHgap(5);

        VBox vBox =new VBox();

        HBox hBox = new HBox();
        hBox.getChildren().addAll(insert,delete,depunere,retragere,show);
        hBox.setSpacing(5);

        vBox.getChildren().addAll(grid,hBox,x,errProdus);
        vBox.setPadding(new Insets(10,10,10,10));

        Scene scene =new Scene(vBox,610,520);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(windowEvent -> contStage=null);


        return stage;
    }

    @Override
    public void handle(ActionEvent event) {//de aici pornesc simularea de la butonul RUN
        if(event.getSource()==persoana) {
            if(persoanaStage==null) {
                try {
                    persoanaStage = constructPersoanaStage();
                } catch (Exception e) {

                }
            }
        }
        if(event.getSource()==cont) {
            if(contStage==null) {
                try {
                    contStage = constructContStage();
                } catch (Exception e) {

                }
            }
        }
    }

    private void serialize(){
        try {
            FileOutputStream fileOut = new FileOutputStream("bank.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(control);
            out.close();
            fileOut.close();
            System.out.printf("Serializarea s-a facut in bank.ser");
        } catch (Exception i) {
            System.out.println("Eroare la salvarea fisierului");
        }
    }

    private void deserialize(){
        try {
            FileInputStream fileIn = new FileInputStream("bank.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            control = (Bank) in.readObject();
            in.close();
            fileIn.close();
            Account.setId(((Bank)control).maxIban());
            ((Bank)control).addObs();
            System.out.println("Fisierul a fost incarcat");
        } catch (Exception i) {
            System.out.println("Eroare la incaracarea fisierului\nFisierul va fi creat");
        }

    }

    private boolean hasNum(String c){
        for(int i=0;i<10;i++) if(c.contains(String.valueOf(i)))return true;
        return false;
    }

    public static void main(String[] args){
        launch(args);
    }
}