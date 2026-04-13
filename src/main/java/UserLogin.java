import java.util.Scanner;

public class UserLogin {

    public UserLogin(){
        scannerLogIn();
        System.out.println("Bienvenido");
    }
    public UserLogin(String username, String password){
        validateLogIn(username, password, scannerLogIn());
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
    private void validateLogIn(String username,String password, String[] inputs){
        if (inputs[0].equals(username) && inputs[1].equals(password)){
            System.out.println("Bienvenido "+ username);
        } else {
            System.out.println("Credenciales incorrectas, intente nuevamente"+ System.lineSeparator());
            validateLogIn(username,password,inputs);
        }
    }
}
