import java.util.HashMap;
import java.util.Map;

// Excepción personalizada: cuenta no encontrada
class CuentaNoEncontradaException extends Exception {
    public CuentaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}

// Excepción personalizada: saldo insuficiente
class SaldoInsuficienteException extends Exception {
    public SaldoInsuficienteException(String mensaje) {
        super(mensaje);
    }
}

// Clase abstracta CuentaBancaria
abstract class CuentaBancaria {
    protected String numeroCuenta;
    protected double saldo;

    public CuentaBancaria(String numeroCuenta, double saldoInicial) {
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldoInicial;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void depositar(double monto) {
        if (monto > 0) {
            saldo += monto;
        }
    }

    public abstract void retirar(double monto) throws SaldoInsuficienteException;
}

// Cuenta corriente
class CuentaCorriente extends CuentaBancaria {
    public CuentaCorriente(String numeroCuenta, double saldoInicial) {
        super(numeroCuenta, saldoInicial);
    }

    @Override
    public void retirar(double monto) throws SaldoInsuficienteException {
        if (monto > saldo) {
            throw new SaldoInsuficienteException("Saldo insuficiente en cuenta corriente.");
        }
        saldo -= monto;
    }
}

// Cuenta de ahorro
class CuentaAhorro extends CuentaBancaria {
    public CuentaAhorro(String numeroCuenta, double saldoInicial) {
        super(numeroCuenta, saldoInicial);
    }

    @Override
    public void retirar(double monto) throws SaldoInsuficienteException {
        if (monto > saldo) {
            throw new SaldoInsuficienteException("Saldo insuficiente en cuenta de ahorro.");
        }
        saldo -= monto;
    }
}

// Clase principal: Banco
public class BancoApp {
    private Map<String, CuentaBancaria> cuentas;

    public BancoApp() {
        cuentas = new HashMap<>();
    }

    public void crearCuenta(CuentaBancaria cuenta) {
        cuentas.put(cuenta.getNumeroCuenta(), cuenta);
    }

    public void eliminarCuenta(String numeroCuenta) throws CuentaNoEncontradaException {
        if (!cuentas.containsKey(numeroCuenta)) {
            throw new CuentaNoEncontradaException("La cuenta " + numeroCuenta + " no existe.");
        }
        cuentas.remove(numeroCuenta);
    }

    public void depositar(String numeroCuenta, double monto) throws CuentaNoEncontradaException {
        CuentaBancaria cuenta = obtenerCuenta(numeroCuenta);
        cuenta.depositar(monto);
    }

    public void retirar(String numeroCuenta, double monto)
            throws CuentaNoEncontradaException, SaldoInsuficienteException {
        CuentaBancaria cuenta = obtenerCuenta(numeroCuenta);
        cuenta.retirar(monto);
    }

    public double calcularSaldoTotal() {
        double total = 0;
        for (CuentaBancaria cuenta : cuentas.values()) {
            total += cuenta.getSaldo();
        }
        return total;
    }

    private CuentaBancaria obtenerCuenta(String numeroCuenta) throws CuentaNoEncontradaException {
        CuentaBancaria cuenta = cuentas.get(numeroCuenta);
        if (cuenta == null) {
            throw new CuentaNoEncontradaException("Cuenta no encontrada: " + numeroCuenta);
        }
        return cuenta;
    }

    // Método main para probar la funcionalidad
    public static void main(String[] args) {
        BancoApp banco = new BancoApp();

        banco.crearCuenta(new CuentaCorriente("001", 500));
        banco.crearCuenta(new CuentaAhorro("002", 1000));

        try {
            banco.depositar("001", 200);
            banco.retirar("002", 300);
            System.out.println("Saldo total en el banco: " + banco.calcularSaldoTotal());
            banco.eliminarCuenta("001");
        } catch (CuentaNoEncontradaException | SaldoInsuficienteException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}