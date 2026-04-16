package ui;

import modelo.*;
import servicio.OperacionesBancarias;

import java.util.Scanner;

public class Menu {
    private final Banco banco;
    private Sucursal sucursalActiva;
    private Cuenta sesionActiva;
    private Estado estadoActual;
    private final Scanner teclado;

    public Menu(Banco banco) {
        this.banco = banco;
        teclado = new Scanner(System.in);
    }

    public void mostrarMenuBanco() {
        boolean correr = true;

        while (correr) {
            System.out.println("\n-----Bienvenido a nuestro banco-----\n");
            System.out.println("¿A qué sucursal le gustaría acceder? Escriba el nombre que corresponda (Puede escribir S para salir del programa)\n");
            mostrarSucursales();
            String nombreSucursal = teclado.nextLine();

            if (nombreSucursal.equalsIgnoreCase("S")) {
                correr = false;
                System.out.println("\nGracias por visitar nuestro banco\n");
            } else {
                sucursalActiva = banco.buscarSucursal(nombreSucursal);

                if (sucursalActiva == null) {
                    System.out.println("\nOpción inválida\n");
                }

                while (sucursalActiva != null) {
                    try {
                        setEstado();
                        switch (estadoActual) {
                            case INVITADO -> mostrarMenuInvitado();
                            case CLIENTE -> mostrarMenuCliente();
                            case ADMIN -> mostrarMenuAdmin();
                        }
                    } catch (IllegalArgumentException datoInvalido) {
                        System.out.println("Error de dato inválido: " + datoInvalido.getMessage());
                        teclado.nextLine();
                    }  catch (IllegalStateException estadoInvalido) {
                        System.out.println("Error de estado inválido: " + estadoInvalido.getMessage());
                        teclado.nextLine();
                    } catch (RuntimeException tiempoEjecucion) {
                        System.out.println("Error de tiempo de ejecución: " + tiempoEjecucion.getMessage());
                        teclado.nextLine();
                    } catch (Exception error) {
                        System.out.println("Error: " + error.getMessage());
                        teclado.nextLine();
                    }
                }
            }
        }
    }

    private void mostrarMenuInvitado() {
        System.out.println("""
                Ingrese el número que corresponda con la acción que desee realizar:
                1) Registrar cuenta
                2) Iniciar sesión
                3) Salir de la sucursal""");

        int opcion = teclado.nextInt();
        teclado.nextLine();

        switch (opcion) {
            case 1:
                mostrarResultadoMetodo(
                        registrarCuenta(),
                        "\nCuenta creada con éxito. Bienvenido, " + sesionActiva.getNombre() + "\n",
                        "\nYa existe una cuenta creada con el email ingresado\n");
                break;
            case 2:
                mostrarResultadoMetodo(
                        iniciarSesion(),
                        "\nSesión iniciada con éxito. Bienvenido, " + sesionActiva.getNombre() + "\n",
                        "\nEl email y/o el pin ingresados son incorrectos\n");
                break;
            case 3:
                salirSucursal();
                break;
            default:
                System.out.println("\nOpción inválida\n");
        }
    }

    private void mostrarMenuCliente() {
        System.out.println("""
                Ingrese el número que corresponda con la acción que desee realizar:
                1) Depositar dinero
                2) Retirar dinero
                3) Realizar una transferencia
                4) Mostrar datos de la cuenta
                5) Eliminar cuenta
                6) Cerrar sesión
                7) Salir de la sucursal""");

        int opcion = teclado.nextInt();
        teclado.nextLine();

        switch (opcion) {
            case 1:
                double montoDepositado = procesarDeposito();
                System.out.println("\nHa depositado $" + montoDepositado + " en su cuenta\n");
                break;
            case 2:
                double montoRetirado = procesarRetiro();
                System.out.println("\nHa retirado $" + montoRetirado + " de su cuenta\n");
                break;
            case 3:
                mostrarResultadoMetodo(
                        procesarTransferencia(),
                        "\nTransferencia realizada con éxito\n",
                        "\nNo se ha encontrado una cuenta asociada con el email ingresado\n");
                break;
            case 4:
                System.out.println(sesionActiva);
                System.out.println(sesionActiva.armarHistorialTransacciones());
                break;
            case 5:
                mostrarResultadoMetodo(procesarEliminacion(),
                        "\nCuenta eliminada con éxito\n",
                        "\nVolviendo al menú de acciones\n");
                break;
            case 6:
                sesionActiva = null;
                System.out.println("\nSesión cerrada con éxito\n");
                break;
            case 7:
                salirSucursal();
                break;
            default:
                System.out.println("\nOpción inválida\n");
        }
    }

    private void mostrarMenuAdmin() {
        System.out.println("""
                Ingrese el número que corresponda con la acción que desee realizar:
                1) Registrar cuenta
                2) Registrar sucursal
                3) Mostrar datos de una cuenta
                4) Mostrar datos de una sucursal
                5) Mostrar datos del banco
                6) Eliminar una cuenta
                7) Cerrar sesión
                8) Salir de la sucursal""");

        int opcion = teclado.nextInt();
        teclado.nextLine();

        switch (opcion) {
            case 1:
                mostrarResultadoMetodo(
                        registrarCuenta(),
                        "\nCuenta creada con éxito. Bienvenido, " + sesionActiva.getNombre() + "\n",
                        "\nYa existe una cuenta creada con el email ingresado\n");
                break;
            case 2:
                registrarSucursal();
                break;
            case 3:
                mostrarDatosCuenta();
                break;
            case 4:
                mostrarDatosSucursal();
                break;
            case 5:
                mostrarDatosBanco();
                break;
            case 6:
                mostrarResultadoMetodo(
                        procesarEliminacionAdmin(),
                        "\nCuenta eliminada con éxito\n",
                        "\nVolviendo al menú de acciones (si no quiso hacer esto, probablemente ingresó un dato inválido)\n");
                break;
            case 7:
                sesionActiva = null;
                System.out.println("\nSesión cerrada con éxito\n");
                break;
            case 8:
                salirSucursal();
                break;
            default:
                System.out.println("\nOpción inválida\n");
        }
    }

    private boolean registrarCuenta() {
        boolean resultado = true;
        TipoCuenta tipoCuentaNuevo = null;

        System.out.println("\nIngrese su nombre\n");

        String nombreNuevo = teclado.nextLine();

        System.out.println("\nIngrese su email\n");

        String emailNuevo = teclado.nextLine();

        System.out.println("\nIngrese su pin\n");

        int pinNuevo = teclado.nextInt();

        System.out.println("""
                Indique el tipo de cuenta que le gustaría crear:
                1) Caja de ahorro
                2) Cuenta corriente""");

        int opcionCuenta = teclado.nextInt();

        switch (opcionCuenta) {
            case 1 -> tipoCuentaNuevo = TipoCuenta.CAJA_AHORRO;
            case 2 -> tipoCuentaNuevo = TipoCuenta.CUENTA_CORRIENTE;
        }

        sesionActiva = sucursalActiva.crearCuenta(nombreNuevo, emailNuevo, pinNuevo, false, tipoCuentaNuevo);
        if (sesionActiva == null) {
            resultado = false;
        }
        return resultado;
    }

    private void registrarSucursal() {
        System.out.println("\nIngrese el nombre de la nueva sucursal\n");
        String nombreNuevaSucursal = teclado.nextLine();
        banco.crearSucursal(nombreNuevaSucursal);
    }

    private double procesarDeposito() {
        System.out.println("\nIngrese el monto que desea depositar en su cuenta\n");

        double montoDepositado = teclado.nextDouble();

        OperacionesBancarias.depositar(sesionActiva, montoDepositado);

        return montoDepositado;
    }

    private double procesarRetiro() {
        System.out.println("\nIngrese el monto que desea retirar de su cuenta\n");

        double montoRetirado = teclado.nextDouble();

        OperacionesBancarias.retirar(sesionActiva, montoRetirado);

        return montoRetirado;
    }

    private boolean procesarTransferencia() {
        boolean resultado = false;

        System.out.println("\nIngrese el monto que desea transferir desde su cuenta\n");
        double montoTransferido = teclado.nextDouble();

        System.out.println("\nIngrese el email de la persona a la que transferirá el dinero\n");
        String emailTransferido = teclado.nextLine();

        Cuenta cuentaTransferido = banco.buscarCuentaBanco(emailTransferido);

        if (cuentaTransferido != null && !cuentaTransferido.getEmail().equals(sesionActiva.getEmail())) {
            OperacionesBancarias.transferir(sesionActiva, cuentaTransferido, montoTransferido);
            resultado = true;
        }
        return resultado;
    }

    private boolean iniciarSesion() {
        boolean resultado = false;

        System.out.println("\nIngrese su email\n");

        String emailSesion = teclado.nextLine();

        System.out.println("\nIngrese su pin\n");

        int pinSesion = teclado.nextInt();
        Cuenta cuentaSesion = sucursalActiva.buscarCuentaSucursal(emailSesion);

        if (cuentaSesion != null) {
            if (cuentaSesion.getPin() == pinSesion) {
                this.sesionActiva = cuentaSesion;
                resultado = true;
            }
        }
        return resultado;
    }

    private void salirSucursal() {
        sesionActiva = null;
        sucursalActiva = null;
    }

    private void mostrarDatosCuenta() {
        System.out.println("\nIngrese el email de la cuenta cuyos datos desea ver\n");
        String emailCuentaBuscada = teclado.nextLine();

        Cuenta cuentaBuscada = banco.buscarCuentaBanco(emailCuentaBuscada);
        System.out.println(cuentaBuscada);
    }

    private void mostrarDatosSucursal() {
        System.out.println("\nIngrese el nombre de la sucursal cuyos datos desea ver\n");
        mostrarSucursales();
        String nombreSucursalBuscada = teclado.nextLine();

        Sucursal sucursalBuscada = banco.buscarSucursal(nombreSucursalBuscada);

        for (Cuenta cuenta : sucursalBuscada.getCuentas()) {
            System.out.println(cuenta);
        }
    }

    private void mostrarDatosBanco() {
        System.out.println("-----Detalles de las cuentas del banco-----");

        for (Sucursal sucursal : banco.getSucursales()) {
            System.out.println("-----Sucursal " + sucursal.getNombre() + "-----");

            for (Cuenta cuenta : sucursal.getCuentas()) {
                System.out.println(cuenta);
            }
        }
    }

    private void mostrarSucursales() {
        for (int i = 0; i < banco.getSucursales().size(); i++) {
            System.out.println(i + 1 + ") " + banco.getSucursales().get(i).getNombre());
        }
    }

    private void mostrarResultadoMetodo(boolean resultado, String mensajeExito, String mensajeError) {
        if (resultado) {
            System.out.println(mensajeExito);
        } else {
            System.out.println(mensajeError);
        }
    }

    private boolean procesarEliminacion() {
        boolean resultado = false;

        System.out.println("\n¿Está seguro que desea eliminar su cuenta? (Presione Enter para continuar)\n");
        String eliminar = teclado.nextLine();

        if (eliminar.isBlank()) {
            sucursalActiva.eliminarCuenta(sesionActiva);
            sesionActiva = null;
            resultado = true;
        }
        return resultado;
    }

    private boolean procesarEliminacionAdmin() {
        boolean resultado = false;

        System.out.println("\nIngrese el mail de la cuenta que desea eliminar\n");
        String emailCuentaEliminar = teclado.nextLine();
        Cuenta cuentaEliminar = banco.buscarCuentaBanco(emailCuentaEliminar);

        if (cuentaEliminar != null) {
            if (cuentaEliminar != sesionActiva) {
                System.out.println("\nLos datos de la cuenta seleccionada son los siguientes:\n");
                System.out.println(cuentaEliminar);
                System.out.println("¿Está seguro que desea eliminar esta cuenta? (Presione Enter para continuar)");
                String eliminar = teclado.nextLine();

                if (eliminar.isBlank()) {
                    sucursalActiva.eliminarCuenta(cuentaEliminar);
                    resultado = true;
                }
            }
        }
        return resultado;
    }

    private void setEstado() {
        if (sesionActiva == null) {
            estadoActual = Estado.INVITADO;
        } else if (sesionActiva.isAdmin()) {
            estadoActual = Estado.ADMIN;
        } else {
            estadoActual = Estado.CLIENTE;
        }
    }
}