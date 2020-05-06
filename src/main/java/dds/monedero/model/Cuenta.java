package dds.monedero.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

public class Cuenta {

  private double saldo = 0;
  private List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta() {
  }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  public void poner(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }

    if (getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }

    agregarMovimiento(LocalDate.now(),cuanto,true);
  }

  public void sacar(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
    if (saldo < cuanto) {
      throw new SaldoMenorException("No puede sacar mas de " + saldo + " $");
    }
    double limite = 1000 - getMontoExtraidoA(LocalDate.now());
    if (cuanto > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
          + " diarios, límite: " + limite);
    }
    agregarMovimiento(LocalDate.now(),cuanto,false);
  }

  public void agregarMovimiento(LocalDate fecha, double cuanto, boolean esDeposito) {
	  this.saldo = calcularValor(esDeposito,cuanto);
	  movimientos.add(new Movimiento(fecha, cuanto, esDeposito));
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return movimientos.stream()
        .filter(movimiento -> !movimiento.noFueDepositado(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }
  
  public double getSaldo() {
	  return saldo;
  }

	  public double calcularValor(boolean esDeposito,double monto) {
	    if (esDeposito) {
	      return saldo + monto;
	    } else {
	      return saldo - monto;
	    }
	  }

}
