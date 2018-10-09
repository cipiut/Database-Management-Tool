package Presentation;

import BusinessLayer.Business;
import Model.Client;
import Model.Comanda;
import Model.Produs;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class GUI extends Application implements EventHandler<ActionEvent> {
    private Stage clientStage,produsStage,comandaStage;
    private Button client, produs, comanda;
    public static String err="";

    private void mainStage(){//pentru initializarea variabilelor
        client = new Button("Client");
        produs = new Button("Produs");
        comanda = new Button("Comanda");

        client.setOnAction(this);
        produs.setOnAction(this);
        comanda.setOnAction(this);
    }

    @Override
    public void start(Stage mainStage) throws Exception {

        mainStage();//initializam variabilele de clasa
        mainStage.setTitle("Database Management");
        mainStage.setResizable(false);
        HBox hBox = new HBox();
        Scene scene = new Scene(hBox,320,150);
        hBox.getChildren().addAll(client,produs,comanda);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);

        mainStage.setScene(scene);
        mainStage.show();
        mainStage.setOnCloseRequest(windowEvent -> System.exit(0));//se inchide programul cu tot cu thread si timer
    }

    private Stage constructClientStage(){
        Stage stage = new Stage();
        stage.setTitle("Tabela Client");
        Business control = new Business(new Client());
        TableView x=control.getTable();

        TextArea errClient= new TextArea();
        errClient.setEditable(false);
        errClient.setBackground(Background.EMPTY);


        TextField idClientT=new TextField();
        TextField ageT = new TextField();
        TextField numeT = new TextField();
        TextField adresaT = new TextField();
        TextField emailT = new TextField();

        Label idClientL = new Label("idClient: ");
        Label ageL = new Label("age: ");
        Label numeL = new Label("nume: ");
        Label adresaL = new Label("adresa: ");
        Label emailL = new Label("email: ");

        Button insert = new Button("insert");
        Button find = new Button("find by id");
        Button delete = new Button("delete");
        Button update = new Button("update");
        Button show = new Button("show");

        insert.setOnAction(event -> {
            err="";
            String[] cl= new String[5];
            cl[1]=ageT.getText();
            cl[2]=numeT.getText();
            cl[4]=emailT.getText();
            cl[3]=adresaT.getText();
            for(int i=1;i<cl.length;i++)if(cl[i]==null || cl[i].equals(""))return;
            int n;
            try{
                n=Integer.parseInt(ageT.getText());
            }catch (Exception e){
                errClient.appendText("Eroare la introducerea datelor\n");
                return;
            }
            if(n<0)return;
            control.insert(new Client(n,cl[2],cl[3],cl[4]));
            errClient.appendText(err);
        });

        find.setOnAction(event -> {
            err="";
            String[] cl= new String[5];
            cl[0]=idClientT.getText();
            int n;
            try{
                n=Integer.parseInt(idClientT.getText());
            }catch (Exception e){
                errClient.appendText("Eroare la introducerea datelor\n");
                return;
            }
            if(n<0)return;
            x.setItems(control.findById(n));
            errClient.appendText(err);
        });

        delete.setOnAction(event -> {
            err="";
            String[] cl= new String[5];
            cl[0]=idClientT.getText();
            int n;
            try{
                n=Integer.parseInt(idClientT.getText());
            }catch (Exception e){
                errClient.appendText("Eroare la introducerea datelor\n");
                return;
            }
            if(n<0)return;
            control.delete(n);
            errClient.appendText(err);
        });

        update.setOnAction(event -> {
            err="";
            int k=0,s=0;
            String[] cl= new String[5];
            String[] cl1= new String[5];
            cl1[0]="idClient";
            cl1[1]="age";
            cl1[2]="nume";
            cl1[3]="adresa";
            cl1[4]="email";
            String[] values= new String[5];
            String[] fields= new String[5];
            cl[0]=idClientT.getText();
            cl[1]=ageT.getText();
            cl[2]=numeT.getText();
            cl[3]=adresaT.getText();
            cl[4]=emailT.getText();
            for(int i=1;i<cl.length;i++){
                if(cl[i]!=null && !cl[i].equals("")){
                    values[k]=cl[i];
                    fields[k++]=cl1[i];
                    s++;
                }
            }
            int n=0,n1=0;
            try{
                n=Integer.parseInt(idClientT.getText());
                if(!cl[1].equals(""))n1=Integer.parseInt(ageT.getText());
                if(n<0 || n1<0 || s==0)return;
            }catch (Exception e){
                errClient.appendText("Eroare la introducerea datelor\n");
                return;
            }
            if(n<0 || n1<0)return;
            control.update(n,fields,values);
            errClient.appendText(err);
        });

        show.setOnAction(event -> {
            x.setItems(control.findAll());
        });

        GridPane grid = new GridPane();
        grid.add(idClientL,0,0);
        grid.add(idClientT,1,0);
        grid.add(ageL,0,1);
        grid.add(ageT,1,1);
        grid.add(numeL,0,2);
        grid.add(numeT,1,2);
        grid.add(adresaL,0,3);
        grid.add(adresaT,1,3);
        grid.add(emailL,0,4);
        grid.add(emailT,1,4);

        grid.setVgap(5);
        grid.setHgap(5);

        VBox vBox =new VBox();

        HBox hBox = new HBox();
        hBox.getChildren().addAll(insert,find,delete,update,show);
        hBox.setSpacing(5);

        vBox.getChildren().addAll(grid,hBox,x,errClient);
        vBox.setPadding(new Insets(10,10,10,10));

        Scene scene =new Scene(vBox,610,520);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(windowEvent -> clientStage=null);

        return stage;
    }

    private Stage constructProdusStage(){
        Stage stage = new Stage();
        stage.setTitle("Tabela Produs");

        Business control = new Business(new Produs());

        TableView x=control.getTable();

        TextArea errProdus= new TextArea();
        errProdus.setEditable(false);
        errProdus.setBackground(Background.EMPTY);


        TextField idProdusT=new TextField();
        TextField numeT = new TextField();
        TextField cantitateT = new TextField();
        TextField pretT = new TextField();

        Label idProdusL = new Label("idProdus: ");
        Label numeL = new Label("nume: ");
        Label cantitateL = new Label("cantitate: ");
        Label pretL = new Label("pret: ");

        Button insert = new Button("insert");
        Button find = new Button("find by id");
        Button delete = new Button("delete");
        Button update = new Button("update");
        Button show = new Button("show");

        GridPane grid = new GridPane();
        grid.add(idProdusL,0,0);
        grid.add(idProdusT,1,0);
        grid.add(numeL,0,1);
        grid.add(numeT,1,1);
        grid.add(cantitateL,0,2);
        grid.add(cantitateT,1,2);
        grid.add(pretL,0,3);
        grid.add(pretT,1,3);

        insert.setOnAction(event -> {
            err="";
            String[] cl= new String[5];
            cl[1]=numeT.getText();
            cl[2]=cantitateT.getText();
            cl[3]=pretT.getText();
            if(cl[1]==null || cl[1].equals(""))return;
            int n,n1;
            try{
                n=Integer.parseInt(cantitateT.getText());
                n1=Integer.parseInt(pretT.getText());
            }catch (Exception e){
                errProdus.appendText("Eroare la introducerea datelor\n");
                return;
            }
            if(n<0 || n1<0)return;
            control.insert(new Produs(cl[1],n,n1));
            errProdus.appendText(err);
        });

        find.setOnAction(event -> {
            err="";
            String[] cl= new String[5];
            cl[0]=idProdusT.getText();
            int n;
            try{
                n=Integer.parseInt(idProdusT.getText());
            }catch (Exception e){
                errProdus.appendText("Eroare la introducerea datelor\n");
                return;
            }
            if(n<0)return;
            x.setItems(control.findById(n));
            errProdus.appendText(err);
        });

        delete.setOnAction(event -> {
            err="";
            String[] cl= new String[5];
            cl[0]=idProdusT.getText();
            int n;
            try{
                n=Integer.parseInt(idProdusT.getText());
                if(n<0)throw new Exception();
            }catch (Exception e){
                errProdus.appendText("Eroare la introducerea datelor\n");
                return;
            }
            control.delete(n);
            errProdus.appendText(err);
        });

        update.setOnAction(event -> {
            err="";
            int k=0,s=0;
            String[] cl= new String[5];
            String[] cl1= new String[5];
            cl1[0]="idProdus";
            cl1[1]="nume";
            cl1[2]="cantitate";
            cl1[3]="pret";
            String[] values= new String[5];
            String[] fields= new String[5];
            cl[0]=idProdusT.getText();
            cl[1]=numeT.getText();
            cl[2]=cantitateT.getText();
            cl[3]=pretT.getText();
            for(int i=1;i<cl.length;i++){
                if(cl[i]!=null && !cl[i].equals("")){
                    values[k]=cl[i];
                    fields[k++]=cl1[i];
                    s++;
                }
            }
            int n=0,n1=0,n2=0;
            try{
                n=Integer.parseInt(idProdusT.getText());
                if(!cl[2].equals(""))n1=Integer.parseInt(cantitateT.getText());
                if(!cl[3].equals(""))n2=Integer.parseInt(pretT.getText());
                if(n<0 || n1<0 || n2<0 || s==0)return;
            }catch (Exception e){
                errProdus.appendText("Eroare la introducerea datelor\n");
                return;
            }
            control.update(n,fields,values);
            errProdus.appendText(err);
        });

        show.setOnAction(event -> {
            x.setItems(control.findAll());
        });


        grid.setVgap(5);
        grid.setHgap(5);

        VBox vBox =new VBox();

        HBox hBox = new HBox();
        hBox.getChildren().addAll(insert,find,delete,update,show);
        hBox.setSpacing(5);

        vBox.getChildren().addAll(grid,hBox,x,errProdus);
        vBox.setPadding(new Insets(10,10,10,10));

        Scene scene =new Scene(vBox,610,520);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(windowEvent -> produsStage=null);


        return stage;
    }

    private Stage constructComandaStage(){
        Stage stage = new Stage();
        stage.setTitle("Tabela Comanda");

        Business control = new Business(new Comanda());

        TableView x=control.getTable();

        TextArea errComanda= new TextArea();
        errComanda.setEditable(false);
        errComanda.setBackground(Background.EMPTY);

        TextField idComandaT=new TextField();
        TextField idClientT = new TextField();
        TextField idProdusT = new TextField();
        TextField cantitateT = new TextField();

        Label idComandaL = new Label("idComanda: ");
        Label idClientL = new Label("idClient: ");
        Label idProdusL = new Label("idProdus: ");
        Label cantitateL = new Label("cantitate: ");

        Button insert = new Button("insert");
        Button find = new Button("find by id");
        Button delete = new Button("delete");
        Button update = new Button("update");
        Button show = new Button("show");
        Button factura = new Button("factura");


        GridPane grid = new GridPane();
        grid.add(idComandaL,0,0);
        grid.add(idComandaT,1,0);
        grid.add(idClientL,0,1);
        grid.add(idClientT,1,1);
        grid.add(idProdusL,0,2);
        grid.add(idProdusT,1,2);
        grid.add(cantitateL,0,3);
        grid.add(cantitateT,1,3);

        grid.setVgap(5);
        grid.setHgap(5);

        insert.setOnAction(event -> {
            err="";
            int n,n1,n2;
            try{
                n=Integer.parseInt(cantitateT.getText());
                n1=Integer.parseInt(idProdusT.getText());
                n2=Integer.parseInt(idClientT.getText());
                if(n<0 || n1<0 ||n2<0)return;
            }catch (Exception e){
                errComanda.appendText("Eroare la introducerea datelor\n");
                return;
            }
            control.insert(new Comanda(n2,n1,n));
            errComanda.appendText(err);
        });

        find.setOnAction(event -> {
            err="";
            String[] cl= new String[5];
            cl[0]=idComandaT.getText();
            int n;
            try{
                n=Integer.parseInt(idComandaT.getText());
            }catch (Exception e){
                errComanda.appendText("Eroare la introducerea datelor\n");
                return;
            }
            if(n<0)return;
            x.setItems(control.findById(n));
            errComanda.appendText(err);
        });

        delete.setOnAction(event -> {
            err="";
            String[] cl= new String[5];
            cl[0]=idComandaT.getText();
            int n;
            try{
                n=Integer.parseInt(idComandaT.getText());
            }catch (Exception e){
                errComanda.appendText("Eroare la introducerea datelor\n");
                return;
            }
            if(n<0)return;
            control.delete(n);
            errComanda.appendText(err);
        });

        update.setOnAction(event -> {
            err="";
            int k=0,s=0;
            String[] cl= new String[5];
            String[] cl1= new String[5];
            cl1[0]="idComanda";
            cl1[1]="idClient";
            cl1[2]="idProdus";
            cl1[3]="cantitate";
            String[] values= new String[5];
            String[] fields= new String[5];
            cl[0]=idComandaT.getText();
            cl[1]=idClientT.getText();
            cl[2]=idProdusT.getText();
            cl[3]=cantitateT.getText();
            for(int i=1;i<cl.length;i++){
                if(cl[i]!=null && !cl[i].equals("")){
                    values[k]=cl[i];
                    fields[k++]=cl1[i];
                    s++;
                }
            }
            int n=0,n1=0,n2=0,n3=0;
            try{
                n=Integer.parseInt(idComandaT.getText());
                if(cl[1]!=null && !cl[1].equals(""))n3=Integer.parseInt(cl[1]);
                if(cl[2]!=null && !cl[2].equals(""))n1=Integer.parseInt(cl[2]);
                if(cl[3]!=null && !cl[3].equals(""))n2=Integer.parseInt(cl[3]);
                if(n<0 || n1<0 ||n2<0||n3<0 || s==0)throw new Exception();
            }catch (Exception e){
                errComanda.appendText("Eroare la introducerea datelor\n");
                return;
            }
            control.update(n,fields,values);
            errComanda.appendText(err);
        });

        show.setOnAction(event -> {
            x.setItems(control.findAll());
        });

        factura.setOnAction(event -> {
            err="";
            int n=0;
            try{
                n=Integer.parseInt(idComandaT.getText());
                if(n<0)throw new Exception();
            }catch (Exception e){
                errComanda.appendText("Eroare la introducerea datelor\n");
                return;
            }
            control.factura(n);
            errComanda.appendText(err);
        });

        VBox vBox =new VBox();

        HBox hBox = new HBox();
        hBox.getChildren().addAll(insert,find,delete,update,show,factura);
        hBox.setSpacing(5);

        vBox.getChildren().addAll(grid,hBox,x,errComanda);
        vBox.setPadding(new Insets(10,10,10,10));

        Scene scene =new Scene(vBox,610,520);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(windowEvent -> comandaStage=null);

        return stage;
    }

    @Override
    public void handle(ActionEvent event) {//de aici pornesc simularea de la butonul RUN
        if(event.getSource()==client) {
            if(clientStage==null) {
                clientStage = constructClientStage();
            }
        }
        if(event.getSource()==produs) {
            if(produsStage==null) {
                produsStage = constructProdusStage();
            }
        }
        if(event.getSource()==comanda) {
            if(comandaStage==null){
                comandaStage= constructComandaStage();
            }
        }
    }

    public static void main(String[] args){
        launch(args);
    }
}