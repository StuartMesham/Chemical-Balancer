/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author stu
 */
public interface Unit {
    public abstract void setCoefficient(int coefficient);
    public abstract int getCoefficient();
    public abstract String getHtml();
    public abstract int getElementCount(String symbol);
}
