/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CasosPruebaYanet_2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import java.lang.reflect.Field;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import sistemaAutogestion.IObligatorio;
import sistemaAutogestion.Retorno;
import sistemaAutogestion.Sistema;

/**
 *
 * @author yanete
 */
public class Test_Obligatorio2_Func_3_10 {
      private IObligatorio s;

    @Before
    public void setUp() {
        s = new Sistema();
        s.crearSistemaDeGestion();
    }


    // ===================== CASOS =====================

    @Test
    public void ok_SinUsuarios_RetornaVacio() {
        Retorno r = s.usuarioMayor();
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals("", r.getValorString());
    }

    @Test
    public void ok_TodosConMismaCantidad_EligeCedulaMenor() {
        // Todos con 0 alquileres (empate) -> debe elegir la cédula menor
        String c1 = "01234567"; // menor
        String c2 = "12345678";
        String c3 = "22345678";

        assertEquals(Retorno.Resultado.OK, s.registrarUsuario(c2, "B").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarUsuario(c3, "C").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarUsuario(c1, "A").getResultado());

        Retorno r = s.usuarioMayor();
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals(c1, r.getValorString());
    }

    @Test
    public void ok_DesempataPorCantidad_yLuegoPorCedula() {
        String c1 = String.format("%08d", 20000000 + 1); // 20000001
        String c2 = String.format("%08d", 20000000 + 2); // 20000002
        String c3 = String.format("%08d", 20000000 + 3); // 20000003

        // Registrar usuarios
        assertEquals(Retorno.Resultado.OK, s.registrarUsuario(c1, "U1").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarUsuario(c2, "U2").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarUsuario(c3, "U3").getResultado());

        // Simular cantidades de alquileres: c2 = 5, c3 = 5, c1 = 3
        try {
            Sistema sys = (Sistema) s;
            Field fUsuarios = Sistema.class.getDeclaredField("usuarios");
            fUsuarios.setAccessible(true);
            Object lista = fUsuarios.get(sys); // ListaSE<Usuario>

            int len = (int) lista.getClass().getMethod("longitud").invoke(lista);
            for (int i = 0; i < len; i++) {
                Object u = lista.getClass().getMethod("obtener", int.class).invoke(lista, i); // Usuario
                String ced = (String) u.getClass().getMethod("getCedula").invoke(u);

                if (ced.equals(c1)) {
                    u.getClass().getMethod("setCantAlquileres", int.class).invoke(u, 3);
                } else if (ced.equals(c2)) {
                    u.getClass().getMethod("setCantAlquileres", int.class).invoke(u, 5);
                } else if (ced.equals(c3)) {
                    u.getClass().getMethod("setCantAlquileres", int.class).invoke(u, 5);
                }
            }
        } catch (Exception e) {
            fail("Fallo ajustando cantAlquileres por reflexión: " + e);
        }

        // Ganador entre c2 y c3 debe ser la cédula menor
        Retorno r = s.usuarioMayor();
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals(c2.compareTo(c3) < 0 ? c2 : c3, r.getValorString());
    }
}
