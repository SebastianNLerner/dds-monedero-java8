package dds.monedero.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

public class MonederoTest {
  private Cuenta cuenta;

  @Before
  public void init() {
    cuenta = new Cuenta();
  }

  @Test
  public void Poner_1500_en_la_cuenta() {
    cuenta.poner(1500);
    assertEquals(1500, cuenta.getSaldo(),0.0);
  }

  @Test(expected = MontoNegativoException.class)
  public void PonerMontoNegativo() {
    cuenta.poner(-1500);
  }

  @Test
  public void Poner_Tres_Depositos_Seguidos() {
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);
    assertEquals(1500+1900+456, cuenta.getSaldo(),0.0);
  }

  @Test(expected = MaximaCantidadDepositosException.class)
  public void No_Se_Puede_Poner_Mas_De_Tres_Depositos() {
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);
    cuenta.poner(245);
  }

  @Test(expected = SaldoMenorException.class)
  public void No_Se_Puede_Extraer_Mas_Que_El_Saldo() {
    cuenta.setSaldo(90);
    cuenta.sacar(1001);
  }

  @Test(expected = MaximoExtraccionDiarioException.class)
  public void No_Se_Puede_Extraer_Mas_De_1000() {
    cuenta.setSaldo(5000);
    cuenta.sacar(1001);
  }

  @Test(expected = MontoNegativoException.class)
  public void No_Se_Puede_Extraer_Monto_Negativo() {
    cuenta.sacar(-500);
  }

}