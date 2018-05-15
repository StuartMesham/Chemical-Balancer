# Chemical-Balancer
![screen shot 2018-05-15 at 19 50 29](https://user-images.githubusercontent.com/28049022/40074453-f57f475a-5879-11e8-90d2-fdb60b1eb71f.png)

The user types in a chemical equation, smashes the "balance" button and then:

1. The user input is parsed into an object oriented representation of the molecules in the equation.
2. The equation is represented as a system of linear simultaneous equations in an augmented matrix.
3. A Gaussian elimination is performed on this matrix to solve the system of equations.
4. The balanced chemical equation is displayed in the output area.
