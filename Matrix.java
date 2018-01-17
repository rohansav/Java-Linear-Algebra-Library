import java.lang.reflect.Method;
import java.util.*;
public class Matrix {
	private final int numRows;
	private final int numCols;
	private double[][] matrix;
	public Matrix(int rows, int cols) {
		this.numRows = rows;
		this.numCols = cols;
		matrix = new double[rows][cols];
	}
	public Matrix(double[][] a) {
		this.numRows = a.length;
		this.numCols = a[0].length;
		matrix = new double[numRows][numCols];
		for (int i = 0; i < numRows; i++) {
			for (int j = 0 ; j < numCols;j++) {
				matrix[i][j] = a[i][j];
			}
		}
	}
	
	public int rows() {return numRows;}
	public int cols() {return numCols;}
	public double getComp(int i, int j) {
		if (i <1 || j < 1 || i > numRows || j > numCols) throw new IllegalArgumentException("This doesn't exist!");
		
		return matrix[i-1][j-1];
	}
	
	public static Matrix scalarMult(double scale, Matrix m) {
		double[][] d = new double[m.rows()][m.cols()];
		for (int row = 1; row <=m.rows();row++) {
			for (int col = 1; col <=m.cols();col++) {
				d[row-1][col-1] = scale*m.getComp(row, col);
			}
		}
		return new Matrix(d);
	}
	
	public static Matrix addtwo(Matrix m1, Matrix m2) {
		if (m1.rows() != m2.rows() || m1.cols() != m2.cols()) throw new IllegalArgumentException("Not same size");
		double[][] d = new double[m1.rows()][m1.cols()];
		for (int row = 1; row <=m1.rows();row++) {
			for (int col = 1; col <=m1.cols();col++) {
				d[row-1][col-1] = m1.getComp(row, col) + m2.getComp(row, col);
			}
		}
		return new Matrix(d);
	}
	
	public static List<Vector> getRowVectors(Matrix m) {
		List<Vector> l = new ArrayList<Vector>();
		for (int i = 1; i <= m.rows();i++) {
			double[] vec = new double[m.cols()];
			for (int j = 1; j <= m.cols();j++) {
				vec[j-1] = m.getComp(i, j);
			}
			l.add(new Vector(vec.length,vec));
		}
		return l;
	}
	
	public static List<Vector> getColumnVectors(Matrix m) {
		List<Vector> l = new ArrayList<Vector>();
		for (int i = 1; i <= m.cols();i++) {
			double[] vec = new double[m.cols()];
			for(int j = 1; j <= m.rows();j++) {
				vec[j-1] = m.getComp(j, i);
			}
			l.add(new Vector(vec.length,vec));
		}
		return l;
	}
	public static Matrix multM1xM2(Matrix m1, Matrix m2) {
		if (m1.cols() != m2.rows()) throw new IllegalArgumentException("Can't multiply");
		List<Vector> firstRows = getRowVectors(m1);
		List<Vector> secondCols = getColumnVectors(m2);
		double[][] d = new double[m1.rows()][m2.cols()];
		
		for (int i = 0; i < m1.rows();i++) {
			for (int j = 0; j < m2.cols();j++) {
				d[i][j] = Vector.dotproduct(firstRows.get(i), secondCols.get(j));
			}
		}
		return new Matrix(d);
	}
	
	public static double determinant(Matrix m) {
		return recurseDeterminant(m);
	}
	
	private static double recurseDeterminant(Matrix m) {
		if (m.rows() == 1) return m.getComp(1, 1);
		
		
		double ret = 0;
		double smalldet = 0;
		for (int i = 1; i <= m.cols();i++) {
			
			double[][] newMatrix = new double[m.rows()-1][m.cols()-1];
			int colCount = 0;
			int rowCount = 0;
			for (int x = 2; x <=m.rows();x++) {
				for (int y = 1; y <= m.cols();y++) {
					
					if (y != i) {
						
					newMatrix[rowCount][colCount] = m.getComp(x, y);
					if (colCount != newMatrix.length-1) colCount++;
					else {
						colCount = 0;
						rowCount++;
					}
					}
				}
			}
			smalldet += Math.pow(-1, 1+i)*m.getComp(1, i)*recurseDeterminant(new Matrix(newMatrix));
			
			ret = smalldet;
		}
		return ret;
	}
	
	
	
	public static void main(String[] args) {
		double[][] d = {{1,2,3},
						{0,4,6},
						{0,2,7}};
			for (Method m : Vector.class.getDeclaredMethods()) {
				System.out.println(m.getName());
			}
		}
	}

