import java.util.Date;

public class Sucursal {
    private String nombre;
    private String direccion;
    private Date inauguracion;

    private InterfaceClientela registro = new RegistroClientela();

    private Sucursal(Builder builder) {
        this.nombre = builder.nombre;
        this.direccion = builder.direccion;
        this.inauguracion = builder.inauguracion;
        this.registro = builder.registro;
    }

    public class Builder {
        private final String nombre;
        private final String direccion;
        private final Date inauguracion;
        private final InterfaceClientela registro;

        public Builder(String nombre, String direccion, Date inauguracion, InterfaceClientela registro) {
            this.nombre = nombre;
            this.direccion = direccion;
            this.inauguracion = inauguracion;
            this.registro = registro;
        }

        public Sucursal build() {
            // WIP:
            // return ListaSucursales.cargar(new Sucursal(this));
            return new Sucursal(this);
    }

}
