
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author stu
 */
public class Interface extends javax.swing.JFrame {

    DecimalFormat df = new DecimalFormat("#.######");

    private ArrayList<Unit> reactants = new ArrayList<>();
    private ArrayList<Unit> products = new ArrayList<>();
    private ArrayList<String> elementNames = new ArrayList<>();

    private double[][] system;

    private double[] variables;

    /**
     * Creates new form Interface
     */
    public Interface() {
        initComponents();
    }

    private void displayUnbalancedEquation() {
        String equation = "";
        for (int i = 0; i < reactants.size(); i++) {
            equation += reactants.get(i).getHtml();

            if (i < reactants.size() - 1) {
                equation += " + ";
            }
        }

        equation += " ⇌ ";

        for (int i = 0; i < products.size(); i++) {
            equation += products.get(i).getHtml();

            if (i < products.size() - 1) {
                equation += " + ";
            }
        }

        jTextPane1.setText(equation);
    }

    private void displayBalancedEquation() {
        int variableNumber = 0;

        String equation = "";
        for (int i = 0; i < reactants.size(); i++) {

            if (variables[variableNumber] != 1) {
                equation += df.format(variables[variableNumber]);
            }
            variableNumber++;

            equation += reactants.get(i).getHtml();

            if (i < reactants.size() - 1) {
                equation += " + ";
            }
        }

        equation += " ⇌ ";

        for (int i = 0; i < products.size(); i++) {

            if (variables[variableNumber] != 1) {
                equation += df.format(variables[variableNumber]);
            }
            variableNumber++;

            equation += products.get(i).getHtml();

            if (i < products.size() - 1) {
                equation += " + ";
            }
        }

        jTextPane1.setText(equation);
    }

    private void parseEquationString(String equationString) {
        reactants.clear();
        products.clear();

        String s = equationString.replaceAll("\\s+", "");

        String[] halves = s.split("=");

        for (int i = 0; i < halves.length; i++) {
            String[] smolecules = halves[i].split("\\+");

            for (String str : smolecules) {
                if (i == 0) {
                    reactants.add(parseCompoundString(str));
                } else {
                    products.add(parseCompoundString(str));
                }
            }
        }
    }

    private Compound parseCompoundString(String s) {
        System.out.println("Finding compound: " + s);
        Compound compound = new Compound();

        int i = 0;

        while (i < s.length()) {
            System.out.println("s.charAt(i) = " + s.charAt(i));
            if (Character.isUpperCase(s.charAt(i))) {

                //Extract element name
                String elementName = s.charAt(i) + "";
                i++;
                while (i < s.length() && Character.isLowerCase(s.charAt(i))) {
                    elementName += s.charAt(i);
                    i++;
                }

                //Extract element coefficient
                String stringCoefficient = "";

                while (i < s.length() && Character.isDigit(s.charAt(i))) {
                    stringCoefficient += s.charAt(i);
                    i++;
                }

                int coefficient = 1;

                if (stringCoefficient.length() > 0) {
                    coefficient = Integer.parseInt(stringCoefficient);
                }

                //Add element
                compound.addSubUnit(new Element(elementName, coefficient));
                if (!elementNames.contains(elementName)) {
                    elementNames.add(elementName);
                }

            } else if (s.charAt(i) == '(') {
                //Extract compound
                int end = s.lastIndexOf(")");

                Compound subCompound = parseCompoundString(s.substring(i + 1, end));

                i = end + 1;

                //Extract compound coefficient
                String stringCoefficient = "";

                while (i < s.length() && Character.isDigit(s.charAt(i))) {
                    stringCoefficient += s.charAt(i);
                    i++;
                }

                int coefficient = 1;

                if (stringCoefficient.length() > 0) {
                    coefficient = Integer.parseInt(stringCoefficient);
                }

                subCompound.setCoefficient(coefficient);

                compound.addSubUnit(subCompound);
            }
        }
        System.out.println("Found the compound!");
        return compound;
    }

    private void createEquationsSystem() {
        system = new double[elementNames.size()][reactants.size() + products.size() + 1];

        for (int i = 0; i < system.length; i++) {
            for (int j = 0; j < system[0].length - 1; j++) {
                if (j < reactants.size()) {
                    system[i][j] = reactants.get(j).getElementCount(elementNames.get(i));
                } else {
                    system[i][j] = -products.get(j - reactants.size()).getElementCount(elementNames.get(i));
                }
            }
            system[i][system[0].length - 1] = 0;
        }
    }

    private void solveEquations() {
        System.out.println("Part 1:");
        for (int i = 0; i < system.length && i < system[0].length - 1; i++) {
            //make system[i][i] the pivot point

            if (system[i][i] == 0) {
                boolean found = false;
                //Find another row to add to row i so that row i isn't 0
                for (int j = i + 1; j < system.length; j++) {
                    if (system[j][i] != 0) {
                        //Add row j to row i
                        for (int k = 0; k < system[0].length; k++) {
                            system[i][k] += system[j][k];
                        }
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    continue;
                }
            }

            for (int i2 = i + 1; i2 < system.length; i2++) {
                //make system[i2][i] = 0
                double n = system[i2][i];

                if (n == 0) {
                    continue;
                }

                //multiply row i2 by system[i][i]
                for (int j = 0; j < system[i2].length; j++) {
                    system[i2][j] *= system[i][i];
                }

                //subtract row i from row i2 until system[i2][i] = 0
                for (int j = 0; j < system[0].length; j++) {
                    system[i2][j] -= system[i][j] * n;
                }
            }
            printSystem();
        }
    }

    private void printSystem() {
        System.out.println("---PRINTING SYSTEM:---");
        for (double[] i : system) {
            for (double j : i) {
                System.out.print(String.format("%5s", j + ""));
            }
            System.out.print("\n");
        }
    }

    private boolean solveVariables() {
        variables = new double[system[0].length - 1];
        
        int defaultValue = 1;

        valueLoop:
        while (true) {
            for (int i = variables.length - 1; i > -1; i--) {
                if (i > system.length - 1 || system[i][i] == 0) {
                    variables[i] = defaultValue;
                } else {
                    variables[i] = system[i][variables.length];
                    for (int j = i + 1; j < variables.length; j++) {
                        variables[i] -= variables[j] * system[i][j];
                    }
                    
                    variables[i] /= system[i][i];
                    
                    if (variables[i] < 0) {
                        return false;
                    }
                    
                    if (variables[i] % 1 != 0) {
                        System.out.println("Default value " + defaultValue + " does not result in integers");
                        defaultValue++;
                        continue valueLoop;
                    }
                }
            }
            break;
        }
        
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(480, 340));

        jTextField1.setText("P2I4 + P4 + H2O + K = PH4I + K(H3PO4)2");

        jButton1.setText("Balance");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextPane1.setEditable(false);
        jTextPane1.setContentType("text/html"); // NOI18N
        jScrollPane1.setViewportView(jTextPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        parseEquationString(jTextField1.getText());
        displayUnbalancedEquation();
        
        createEquationsSystem();
        printSystem();
        
        solveEquations();
        
        if(solveVariables()){
        displayBalancedEquation();
        } else {
            JOptionPane.showMessageDialog(this, "Cannot solve with positive integers", "Invalid equation", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Interface().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
}
