import java.util.Scanner;

public class UserLogin {
    private String user;
    private String pass;
    private boolean admin;

    public UserLogin(){
        scannerLogIn();
        System.out.println("Bienvenido");
        validateAdminPerms("admin");
    }
    public UserLogin(String username, String password){
        validateLogIn(username, password, scannerLogIn());
        this.user = username;
        this.pass = password;
        this.admin = validateAdminPerms(username);
    }
    public String getUser(){
        return user;
    }

    public String getPass() {
        return pass; //Why?
    }

    public boolean isAdmin() {
        return admin; //Why?
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
    private boolean validateAdminPerms(String username){
        //Por el momento, todos tienen permiso de administrador
        System.out.println("Tienes permisos de Administrador");
        return true;
    }

}
