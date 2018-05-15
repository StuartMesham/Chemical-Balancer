
import java.util.Objects;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author stu
 */
public class Element implements Unit {
    int coefficient;
    private final String symbol;

    public Element(String symbol, int coefficient) {
        this.symbol = symbol;
        this.coefficient = coefficient;
    }
    
    public Element(String symbol) {
        this(symbol, 1);
    }
    
    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return symbol + getCoefficient();
    }

    @Override
    public void setCoefficient(int coefficient) {
        this.coefficient = coefficient;
    }

    @Override
    public int getCoefficient() {
        return coefficient;
    }

    @Override
    public String getHtml() {
        if (coefficient > 1) {
            return symbol + "<sub>" + getCoefficient() + "</sub>";
        }
        
        return symbol;
    }

    @Override
    public int getElementCount(String symbol) {
        if (this.symbol.equals(symbol)) {
            return getCoefficient();
        }
        return 0;
    }
}
