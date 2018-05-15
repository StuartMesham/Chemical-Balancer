
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author stu
 */
public class Compound implements Unit {

    private int coefficient = 1;
    private final ArrayList<Unit> subUnits = new ArrayList<>();

    public void addSubUnit(Unit subUnit) {
        this.subUnits.add(subUnit);
    }

    @Override
    public String getHtml() {
        String s = "";

        if (getCoefficient() > 1) {
            s += '(';
        }
        for (Unit unit : subUnits) {
            s += unit.getHtml();
        }
        if (getCoefficient() > 1) {
            s += ')' + "<sub>" + getCoefficient() + "</sub>";
        }
        return s;
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
    public int getElementCount(String symbol) {
        int count = 0;
        for (Unit unit : subUnits) {
            count += unit.getElementCount(symbol);
        }
        count *= getCoefficient();
        return count;
    }
}
