package leo.AppCUI;

import leo.ModeloBanco.Cliente.Cliente;
import leo.ModeloBanco.Sucursal;
import leo.ModeloBanco.Transferencia.Transferencia;

import java.util.ArrayList;
import java.util.Scanner;

public class CUI {
    // Command User Interface
    // Maneja el diseño y los prints de la interfaz
    private Sucursal sucursal;
    private ArrayList<Sucursal> sucursalList;
    private UserLogin activeUser;
    private DataReader dr;
    private AuditReader ar;
    private SucursalReader sr;

    public void setSucursalList(ArrayList<Sucursal> sucursalList) {
        this.sucursalList = sucursalList;
        this.sr = new SucursalReader(sucursalList);
    }

    public void setActiveUser(UserLogin user){
        this.activeUser = user;
        setSucursal(this.activeUser.getSucursal());
    }

    public void setSucursal(Sucursal sucursal){
        this.sucursal = sucursal;
        this.dr = new DataReader(sucursal.registro);
        this.ar = new AuditReader(sucursal.auditor);
    }

    public void printLogo(){
        System.out.println(sucursal.getLogo());
    }

    private void printOptionList(String[] options){
        System.out.printf("Elija un comando:%n");
        for (int i = 0; i < options.length; i++) {
            System.out.println(i+1 + " - " + options[i]);
        }
    }

    public void printDataList(ArrayList<String> data){
        printDataList(data, 20);
    }

    private void printDataList(ArrayList<String> data, int maxEntries){
        for (int i = 0; i < data.size() && i < maxEntries; i++){
            System.out.println(data.get(i));
        }
        System.out.println(System.lineSeparator());
    }

    private String scanValidate(){
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    private Cliente scanValidateCliente(){
        Cliente resultado;
        System.out.println("Introduzca usuario del cliente: ");
        resultado = dr.source.buscarUsername(scanValidate());
        while (resultado == null){
            System.out.println("Usuario no encontrado, intente de nuevo");
            resultado = dr.source.buscarUsername(scanValidate());
        }
        return resultado;
    }

    private int scanOptionList(String[] options){
        Scanner sc = new Scanner(System.in);
        printOptionList(options);

        int userChoice;
        do {
            while (!sc.hasNextInt()) {
                System.out.println("Error: No es un número.");
                sc.next();
                System.out.print("Elija un comando válido: ");
            }
            userChoice = sc.nextInt();
            if (userChoice <= 0 || userChoice > options.length) {
                System.out.println("Error: Comando no válido.");
                System.out.print("Elija un comando válido: ");
            }
        } while (userChoice <= 0 || userChoice > options.length);
        return userChoice - 1;
    }

    public void sucursalMenu(){
        String[] optionsMenu = {"Volver"};

        printDataList(sr.SucursalesToListString());
        switch (scanOptionList(optionsMenu)) {
            case 0 -> mainMenu();
        }
    }

    public void clientMenu() {
        String[] optionsMenu = {"Volver"};

        printDataList(dr.clientelaToListString());
        switch (scanOptionList(optionsMenu)) {
            case 0 -> mainMenu();
        }
    }

    public void transferMenu() {
        String[] optionsLabel = {"Seleccionar Emisor: ", "Seleccionar Destino: ", "Establecer Monto: ","Acreditar Transferencia", "Volver"};
        String[] optionsMenu = new String[5];
        String[] optionsValues = new String[3];
        for (int i = 0; i < optionsLabel.length; i++) {
            optionsMenu[i] = optionsLabel[i];
        }

        int selection = scanOptionList(optionsMenu);
        while (selection != 3 && selection != 4) {
            optionsValues[selection] = scanValidate();
            optionsMenu[selection] = optionsLabel[selection] + optionsValues[selection];
            printOptionList(optionsMenu);
            selection = scanOptionList(optionsMenu);
        }

        if (selection == 3) {
            //new Transferencia.Builder(dr.source.buscarUsername(scanValidateCliente()));
            System.out.println("Transferencia acreditada con éxito" + System.lineSeparator());
            newClientMenu();
        } else {
            mainMenu();
        }
    }

    public void newClientMenu() {
        String[] optionsLabel = {"Usuario: ", "Contraseña: ", "Nombre: ", "Apellido: ", "Dirección: ", "Crear cuenta", "Volver"};
        String[] optionsMenu = new String[7];
        String[] optionsValues = new String[5];
        for (int i = 0; i < optionsLabel.length; i++) {
            optionsMenu[i] = optionsLabel[i];
        }

        int selection = scanOptionList(optionsMenu);
        while (selection != 5 && selection != 6) {
            optionsValues[selection] = scanValidate();
            optionsMenu[selection] = optionsLabel[selection] + optionsValues[selection];
            printOptionList(optionsMenu);
            selection = scanOptionList(optionsMenu);
        }

        if (selection == 5) {
            new Cliente.Builder(optionsValues[0], optionsValues[1],optionsValues[2],optionsValues[3], optionsValues[4])
                    .build(dr.source);
            System.out.println("Cliente cargado con éxito" + System.lineSeparator());
            newClientMenu();
        } else {
            mainMenu();
        }
    }


    public void balMenu() {
        String[] optionsMenu = {"Volver"};

        printLogo();
        System.out.println("$" + dr.getBalTotal());
        System.out.println("Entre " + dr.getDataMapSize() + " Cuentas");

        scanOptionList(optionsMenu);
        mainMenu();
    }

    public boolean adminMenu(){
        String[] optionsMenu = {"Sucursales","Clientes", "Transferencias", "Añadir Nuevo Cliente", "Balance Total", "Cambiar cuenta", "Salir"};

        printLogo();
        switch(scanOptionList(optionsMenu)){
            case 0 -> sucursalMenu();
            case 1 -> clientMenu();
            case 2 -> transferMenu();
            case 3 -> newClientMenu();
            case 4 -> balMenu();
            case 5 -> {return true;}
        }
        return false;
    }

    public boolean mainMenu(){
        if (activeUser.isAdmin()) {return adminMenu();}
        String[] optionsMenu = {"Sucursales","Clientes", "Transferencias", "Añadir Nuevo Cliente", "Balance Total", "Cambiar cuenta", "Salir"};

        printLogo();
        switch(scanOptionList(optionsMenu)){
            case 0 -> sucursalMenu();
            case 1 -> clientMenu();
            case 2 -> transferMenu();
            case 3 -> newClientMenu();
            case 4 -> balMenu();
            case 5 -> {return true;}
        }
        return false;
    }
}
