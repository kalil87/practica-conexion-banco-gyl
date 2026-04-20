package leo.AppCUI;

import leo.ModeloBanco.Cliente.Cliente;
import leo.ModeloBanco.Sucursal;
import leo.ModeloBanco.Transferencia.Transferencia;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private final Scanner scanner = new Scanner(System.in);

    public void setSucursalList(ArrayList<Sucursal> sucursalList) {
        this.sucursalList = sucursalList;
        this.sr = new SucursalReader(sucursalList);
    }

    public UserLogin getActiveUser(){
        return activeUser;
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

    public void printSaldo(){
        System.out.println("Tu saldo actual es de: $" + activeUser.getCuenta().getSaldo());
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
        return scanner.nextLine().trim();
    }

    private BigDecimal scanValidateMonto(){
        while (true) {
            String input = scanValidate();
            try {
                return new BigDecimal(input);
            } catch (NumberFormatException error) {
                System.out.println("Error: No es un número.");
                System.out.print("Introduzca un monto válido: ");
            }
        }
    }

    private Cliente scanValidateCliente(){
        Cliente resultado;
        System.out.println("Introduzca usuario del cliente: ");
        resultado = sr.getClienteFromAny(scanValidate());
        while (resultado == null){
            System.out.println("Usuario no encontrado, intente de nuevo");
            resultado = sr.getClienteFromAny(scanValidate());
        }
        return resultado;
    }

    private boolean validateTransferencia(Cliente emisor, Cliente receptor, BigDecimal monto){
        if (emisor == null) {return false;}
        if (receptor == null) {return false;}
        if (monto == null) {return false;}
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {return false;}
        if (emisor.getSaldo().compareTo(monto) < 0) {return false;}
        return true;
    }

    private boolean validateTransferenciaConSaldo(Cliente emisor, Cliente receptor, BigDecimal monto){
        if (!validateTransferencia(emisor, receptor, monto)) {return false;}
        return emisor.getSaldo().compareTo(monto) >= 0;
    }

    private int scanOptionList(String[] options){
        printOptionList(options);

        int userChoice;
        do {
            String input = scanValidate();
            while (!input.matches("\\d+")) {
                System.out.println("Error: No es un número.");
                System.out.print("Elija un comando válido: ");
                input = scanValidate();
            }
            userChoice = Integer.parseInt(input);
            if (userChoice <= 0 || userChoice > options.length) {
                System.out.println("Error: Comando no válido.");
                System.out.print("Elija un comando válido: ");
            }
        } while (userChoice <= 0 || userChoice > options.length);
        return userChoice - 1;
    }

    public void depositMenu(){
        System.out.println("Ingrese Monto: ");
        BigDecimal monto = scanValidateMonto();
        new Transferencia.Builder(true, activeUser.getCuenta(), monto)
                .acreditar(sucursal.auditor);
        System.out.println("Deposito acreditado");
        System.out.println(System.lineSeparator());
        mainMenu();
    }

    public void withdrawMenu(){
        System.out.println("Ingrese Monto: ");
        BigDecimal monto = scanValidateMonto();
        new Transferencia.Builder(false, activeUser.getCuenta(), monto)
                .acreditar(sucursal.auditor);
        System.out.println("Retiro acreditado");
        System.out.println(System.lineSeparator());
        mainMenu();
    }

    public void sucursalMenu(){
        String[] optionsMenu = {"Volver"};

        printDataList(sr.getSucursalesPropiasToListString());
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
        if (activeUser.isAdmin()) {
            adminTransferMenu();
        } else {
            String[] optionsLabel = {"Seleccionar Destinatario: ", "Establecer Monto: ", "Acreditar Transferencia ", "Volver"};
            String[] optionsMenu = new String[4];
            Cliente receptor = null;
            BigDecimal monto = null;
            for (int i = 0; i < optionsLabel.length; i++) {
                optionsMenu[i] = optionsLabel[i];
            }

            printDataList(ar.makeListString(ar.getAllTransferenciasOfCliente(activeUser.getCuenta())));
            int selection;
            do {
                switch (selection = scanOptionList(optionsMenu)) {
                    case 0 -> {
                        receptor = scanValidateCliente();
                        optionsMenu[0] = optionsLabel[0] + receptor.getNombreCompleto();
                    }
                    case 1 -> {
                        monto = scanValidateMonto();
                        optionsMenu[1] = optionsLabel[1] + monto.toString();
                    }
                    case 2 -> {
                        if (validateTransferenciaConSaldo(activeUser.getCuenta(), receptor, monto)) {
                            new Transferencia.Builder(activeUser.getCuenta(), receptor, monto)
                                    .fecha(LocalDateTime.now().toString())
                                    .acreditar(ar.source);
                            if (dr.source.buscarUsername(receptor.getUsername()) == null){
                                Sucursal sucursalExterna = sr.hasCliente(receptor.getUsername());
                                new Transferencia.Builder(activeUser.getCuenta(), receptor, monto)
                                        .fecha(LocalDateTime.now().toString())
                                        .registrar(sucursalExterna.auditor);
                            }
                            System.out.println("Transferencia acreditada exitosamente");
                            selection = 3;
                        } else {
                            System.out.println("Verifique los datos e intente nuevamente");
                        }
                    }
                }
            } while (selection != 3);
            mainMenu();

        }
    }

    public void adminTransferMenu() {
        String[] optionsLabel = {"Seleccionar Emisor: ", "Seleccionar Destino: ", "Establecer Monto: ","Acreditar Transferencia", "Volver"};
        String[] optionsMenu = new String[5];
        Cliente[] optionsValues = new Cliente[2];
        BigDecimal monto = null;
        for (int i = 0; i < optionsLabel.length; i++) {
            optionsMenu[i] = optionsLabel[i];
        }

        printDataList(ar.getAuditToListString());
        int selection;
        do {
            switch (selection = scanOptionList(optionsMenu)) {
                case 0,1 -> {
                    optionsValues[selection] = scanValidateCliente();
                    optionsMenu[selection] = optionsLabel[selection] + optionsValues[selection].getNombreCompleto();
                }
                case 2 -> {
                    monto = scanValidateMonto();
                    optionsMenu[2] = optionsLabel[2] + monto.toString();
                }
                case 3 -> {
                    if (validateTransferencia(optionsValues[0],optionsValues[1], monto)) {
                        new Transferencia.Builder(optionsValues[0],optionsValues[1], monto)
                                .fecha(LocalDateTime.now().toString())
                                .acreditar(ar.source);
                        if (dr.source.buscarUsername(optionsValues[0].getUsername()) == null){
                            Sucursal sucursalExterna = sr.hasCliente(optionsValues[0].getUsername());
                            new Transferencia.Builder(optionsValues[0], optionsValues[1], monto)
                                    .fecha(LocalDateTime.now().toString())
                                    .registrar(sucursalExterna.auditor);
                        }
                        if (dr.source.buscarUsername(optionsValues[1].getUsername()) == null){
                            Sucursal sucursalExterna = sr.hasCliente(optionsValues[1].getUsername());
                            new Transferencia.Builder(optionsValues[0], optionsValues[1], monto)
                                    .fecha(LocalDateTime.now().toString())
                                    .registrar(sucursalExterna.auditor);
                        }
                        System.out.println("Transferencia creada y acreditada exitosamente");
                        selection = 4;
                    } else {
                        System.out.println("Verifique los datos e intente nuevamente");
                    }
                }
            }
        } while (selection != 4);
        mainMenu();
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
        String[] optionsMenu = {"Depósito", "Retiro","Sucursales","Clientes", "Transferencias", "Añadir Nuevo Cliente", "Balance Total", "Cambiar cuenta", "Salir"};

        printLogo();
        printSaldo();
        switch(scanOptionList(optionsMenu)){
            case 0 -> depositMenu();
            case 1 -> withdrawMenu();
            case 2 -> sucursalMenu();
            case 3 -> clientMenu();
            case 4 -> transferMenu();
            case 5 -> newClientMenu();
            case 6 -> balMenu();
            case 7 -> this.activeUser = null;
        }
        return false;
    }

    public boolean mainMenu(){
        if (activeUser.isAdmin()) {return adminMenu();}
        String[] optionsMenu = {"Depósito","Retiro", "Transferencias","Sucursales","Cambiar Cuenta", "Salir"};

        printLogo();
        printSaldo();
        switch(scanOptionList(optionsMenu)){
            case 0 -> depositMenu();
            case 1 -> withdrawMenu();
            case 2 -> transferMenu();
            case 3 -> sucursalMenu();
            case 4 -> this.activeUser = null;
        }
        return false;
    }
}
