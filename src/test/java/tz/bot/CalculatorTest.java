package tz.bot;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    private Calculator calculator;
    private int firstNumber;
    private int secondNumber;

    @BeforeEach
    public void init(){
        calculator=new Calculator();
        firstNumber=10;
        secondNumber=5;
    }

    @Test
    void add() {
        int result= calculator.add(firstNumber,secondNumber);
        assertNotEquals(10,result);
    }

    @Test
    void subtract() {
    int result=calculator.subtract(firstNumber,secondNumber);
    assertEquals(5,result);

    }

    @Test
    void divide(){

        assertThrows(ArithmeticException.class,()->calculator.divide(8,0));
    }

    @AfterEach
    public void tearDown(){
        calculator=null;
    }
}