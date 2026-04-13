import java.util.Scanner;

public class CUI {
    // Command User Interface
    // Maneja el diseño y los prints de la interfaz
    public void printLogo(){
        String logo= """
                          ▒▒▒▒                         \s
                          ▒▒░░▒▒▒▒▒▒▒▒▒▒▒▒             \s
                          ▒▒▒▒░░░░░░░░░░░░▒▒▒▒         \s
                        ▒▒░░░░░░░░░░░░░░░░░░░░▒▒       \s
                      ▒▒░░▓▓░░░░░░░░░░░░░░░░░░░░▒▒     \s
                  ▒▒▒▒░░░░░░░░░░░░░░░░░░░░░░░░░░▒▒     \s
                  ▒▒░░░░░░░░░░░░░░░░░░░░░░░░░░░░▒▒  ▒▒ \s
                  ▒▒░░░░░░░░░░░░░░░░░░░░░░░░░░░░▒▒▒▒   \s
                    ▒▒░░░░░░░░░░░░░░░░░░░░░░░░░░▒▒     \s
                      ▒▒▒▒░░░░░░░░░░░░░░░░░░░░▒▒       \s
                          ▒▒░░░░░░░░░░░░░░░░▒▒         \s
                          ▒▒▒▒░░░░░░░░░░░░▒▒▒▒         \s
                          ▒▒░░▒▒▒▒▒▒▒▒▒▒▒▒░░▒▒         \s
                          ▒▒▒▒▒▒        ▒▒▒▒▒▒         \s
                
                ░░BANCO CONSOLA DE TALLER POO GYL 2026░░
                """;
        System.out.println(logo);
    }

    private void printOptionList(String[] options){
        System.out.printf("Elija un comando:%n");
        for (int i = 0; i < options.length; i++) {
            System.out.println(i+1 + " - " + options[i]);
        }
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

        switch (scanOptionList(optionsMenu)) {
            case 0 -> clientMenu(); //Nada
            case 1 -> mainMenu();
        }
    }

    public void transferMenu() {
        String[] optionsMenu = new String[4];
        optionsMenu[0] = "Seleccionar Emisor: ";
        optionsMenu[1] = "Seleccionar Destino: ";
        optionsMenu[2] = "Establecer Monto: ";
        optionsMenu[3] = "Volver";

        printLogo();
        switch (scanOptionList(optionsMenu)) {
            case 0 -> clientMenu();
            case 1 -> transferMenu();
            case 2 -> newClientMenu();
            case 3 -> mainMenu();
        }
    }

    public void newClientMenu() {
        String[] optionsMenu = new String[5];
        optionsMenu[0] = "Nombre: ";
        optionsMenu[1] = "Edad: ";
        optionsMenu[2] = "Dirección: ";
        optionsMenu[3] = "Crear cuenta";
        optionsMenu[4] = "Volver";

        printLogo();
        switch (scanOptionList(optionsMenu)) {
            case 0 -> clientMenu();
            case 1 -> transferMenu();
            case 2 -> newClientMenu();
            case 3 -> balMenu();
            case 4 -> mainMenu();
        }
    }


    public void balMenu() {
        String[] optionsMenu = new String[4];
        optionsMenu[0] = "Consultar Lista de Clientes";
        optionsMenu[1] = "Realizar Transferencia";
        optionsMenu[2] = "Añadir Nuevo Cliente";
        optionsMenu[3] = "Balance de todas las cuentas";

        printLogo();
        switch (scanOptionList(optionsMenu)) {
            case 0 -> clientMenu();
            case 1 -> transferMenu();
            case 2 -> newClientMenu();
            case 3 -> balMenu();
        }
    }


    public void mainMenu(){
        String[] optionsMenu = new String[4];
        optionsMenu[0] = "Clientes";
        optionsMenu[1] = "Transferencias";
        optionsMenu[2] = "Añadir Nuevo Cliente";
        optionsMenu[3] = "Balance Total";

        printLogo();
        switch(scanOptionList(optionsMenu)){
            case 0 -> clientMenu();
            case 1 -> transferMenu();
            case 2 -> newClientMenu();
            case 3 -> balMenu();
        };
    }
}
