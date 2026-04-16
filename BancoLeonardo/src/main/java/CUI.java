import java.util.ArrayList;
import java.util.Scanner;

public class CUI {
    // Command User Interface
    // Maneja el diseño y los prints de la interfaz
    private Sucursal sucursal;
    private UserLogin activeUser;
    private DataReader dr;
    private AuditReader ar;

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

    private void printDataList(ArrayList<String> data){
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

    public void clientMenu() {
        String[] optionsMenu = new String[2];
        optionsMenu[0] = "Consultar perfil de cliente";
        optionsMenu[1] = "Volver";

        printDataList(dr.clientelaToListString());
        switch (scanOptionList(optionsMenu)) {
            case 0 -> clientMenu(); //Nada
            case 1 -> mainMenu();
        }
    }

    public void transferMenu() {
        String[] optionsLabel = {"Seleccionar Emisor: ", "Seleccionar Destino: ", "Establecer Monto: ", "Volver"};
        String[] optionsMenu = new String[4];
        for (int i = 0; i < optionsLabel.length; i++) {
            optionsMenu[i] = optionsLabel[i];
        }

        printDataList(ar.auditToListString());
        switch (scanOptionList(optionsMenu)) {
            case 0 -> clientMenu();
            case 1 -> transferMenu();
            case 2 -> newClientMenu();
            case 3 -> mainMenu();
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
//      Se que comentar código está mal pero me ahorro el switch redundante
//        switch (scanOptionList(optionsMenu)) {
//            case 0 -> mainMenu();
//        }
    }


    public void mainMenu(){
        String[] optionsMenu = {"Clientes", "Transferencias", "Añadir Nuevo Cliente", "Balance Total", "Cambiar cuenta"};

        printLogo();
        switch(scanOptionList(optionsMenu)){
            case 0 -> clientMenu();
            case 1 -> transferMenu();
            case 2 -> newClientMenu();
            case 3 -> balMenu();
        }
    }
}
