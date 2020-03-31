package com.example.treinamentoandroidavancado;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void calculadora_somar(){
        Calculadora calculadora = new Calculadora(5, 10);
        assertEquals(15, calculadora.somar());
    }

    @Test
    public void calculadora_multiplicar(){
        Calculadora calculadora = new Calculadora(5, 10);
        assertEquals(50, calculadora.multiplicar());
    }
}
