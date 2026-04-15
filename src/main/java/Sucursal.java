

public class Sucursal {
    private String nombre;
    private String direccion;
    private String inauguracion;
    private String logo;

    public InterfaceClientela registro = new RegistroClientela();
    public InterfaceTransferencia auditor = new RegistroTransferencia();

    private final String defaultLogo = """
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
                
                ░░SUCURSAL BANCO DE TALLER POO GYL 2026░░
                """;

    public Sucursal(String nombre, String direccion, String inauguracion) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.inauguracion = inauguracion;
        this.logo = this.defaultLogo;
    }

    public Sucursal(String nombre, String direccion, String inauguracion, String logo) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.inauguracion = inauguracion;
        this.logo = logo;
    }


    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getInauguracion() {
        return inauguracion;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String ASCII) {
        this.logo = ASCII;
    }

    public String getDefaultLogo() {
        return defaultLogo;
    }
}
