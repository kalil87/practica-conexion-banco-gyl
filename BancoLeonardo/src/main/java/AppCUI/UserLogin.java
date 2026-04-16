import ModeloBanco.*;
import ModeloBanco.Cliente.Cliente;
import ServicioDataBase.DataBaseInjector;

import java.util.Scanner;

public class UserLogin {
    private String user;
    private String pass;
    private boolean admin;
    private Sucursal sucursal;
    private Cliente cuenta;

    public UserLogin(){
        scannerLogIn();
        System.out.println("Bienvenido");
    }
    public UserLogin(String username, String password){
        validateLogIn(username, password, scannerLogIn());
        this.user = username;
        this.pass = password;
        this.admin = true;
    }
    public UserLogin(DataBaseInjector database){
        validateLogInDB(database,scannerLogIn());
        this.admin = validateAdminPerms(cuenta);
    }

    public String getUser(){
        return user;
    }

    public String getPass() {
        return pass; //Why?
    }

    public Cliente getCuenta() {
        return cuenta;
    }

    public boolean isAdmin() {
        return admin;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    private String[] scannerLogIn(){
        Scanner sc = new Scanner(System.in);
        String[] inputs = new String[2];
        System.out.println("Introduzca su usuario:");
        inputs[0] = sc.nextLine();
        System.out.println("Introduzca su contraseña:");
        inputs[1] = sc.nextLine();

        return inputs;
    }
    private boolean validateLogIn(String username,String password, String[] inputs){
        if (inputs[0].equals(username) && inputs[1].equals(password)){
            System.out.println("Bienvenido "+ username);
            return true;
        } else {
            System.out.println("Credenciales incorrectas, intente nuevamente"+ System.lineSeparator());
            validateLogIn(username,password,scannerLogIn());
        }
        return false; //Por si salgo del bucle de validación
    }

    private boolean validateLogInDB(DataBaseInjector db, String[] inputs){
        for (Sucursal iSucursal : db.getSucursalList()){
            Cliente iCliente = iSucursal.registro.buscarUsername(inputs[0]);
            if (iCliente != null){
                if (iCliente.getPassword().equals(inputs[1])){
                    System.out.println("Bienvenido " + iCliente.getNombreCompleto());
                    this.sucursal = iSucursal;
                    this.cuenta = iCliente;
                    return true;
                }
                break; //Si existe usuario pero la contraseña está mal, entonces dejo de buscar
            }
        }
        System.out.println("Credenciales incorrectas, intente nuevamente" + System.lineSeparator());
        validateLogInDB(db, scannerLogIn());

        return false;
    }


    private boolean validateAdminPerms(Cliente check){
        return check.getPermisos().equals("ADMIN");
    }

}
