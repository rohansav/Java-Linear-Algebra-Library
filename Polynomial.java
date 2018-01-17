import java.util.*;
public class Polynomial {
	public List<PolyNode> terms;
	
	private static class PolyNode {
		private int degree;
		private double coefficient;
		public PolyNode(int d, double co) {
			this.degree = d;
			this.coefficient = co;
		}
		public int getdegree() {
			return degree;
		}
		public double getCoeff() {
			return this.coefficient;
		}
		public void changeDegreeByInt(int n) {
			degree += n;
		}
		public void changeCoeff(double n) {
			coefficient = n;
		}
	}
	
	public int getDegree() {
		return terms.size()-1;
	}
	
	public Polynomial() {
		terms = new ArrayList<PolyNode>();
		terms.add(new PolyNode(0,0));
	}
	public Polynomial(int n) {
		terms = new ArrayList<PolyNode>();
		for (int i = 0; i <= n; i++) {
			if (i != n) terms.add(new PolyNode(i,0));
			else terms.add(new PolyNode(n,1));
		}
		
	}
	
	public Polynomial(List<Double> l) {
		terms = new ArrayList<PolyNode>();
		for (int i = 0; i < l.size();i++) {
			terms.add(new PolyNode(i,l.get(i)));
		}
	}
	
	public Polynomial(double[] arr) {
		terms = new ArrayList<PolyNode>();
		for (int i = 0; i < arr.length;i++) {
			terms.add(new PolyNode(i,arr[i]));
		}
	}
	public static Polynomial addtwo(Polynomial p1, Polynomial p2) {
		List<Double> newPoly= new ArrayList<Double>();
		
		for (int i = 0; i <= Math.max(p1.getDegree(), p2.getDegree());i++) {
			if (i > p1.getDegree()) {
				newPoly.add(p2.terms.get(i).getCoeff());
			}
			else if (i > p2.getDegree()) {
				
				newPoly.add(p1.terms.get(i).getCoeff());
			}
			else 
				//if (i <= p1.getDegree() && i <= p2.getDegree()) {
				newPoly.add(p2.terms.get(i).getCoeff() + p1.terms.get(i).getCoeff());
				//}
		}
		
		return new Polynomial(newPoly);
	}
	
	public static Polynomial scalarMult(Polynomial p, double scale) {
		List<Double> ret = new ArrayList<Double>();
		for(PolyNode pn : p.terms) {
			ret.add(scale*pn.getCoeff());
		}
		return new Polynomial(ret);
	}
	
	public static Polynomial subtract(Polynomial p1, Polynomial p2) {
		return addtwo(p1,scalarMult(p2,-1));
	}
	public static Polynomial derivative(Polynomial p) {
		List<Double> newPoly = new ArrayList<Double>();
		for (int i = 1; i <= p.getDegree();i++) {
			newPoly.add(p.terms.get(i).getCoeff()*i);
		}
		return new Polynomial(newPoly);
	}
	
	public static Polynomial integralZeroConstant(Polynomial p) {
		List<Double> newPoly = new ArrayList<Double>();
		newPoly.add(0.0);
		for (PolyNode pn : p.terms) {
			newPoly.add(pn.getCoeff()/(pn.getdegree()+1));
		}
		return new Polynomial(newPoly);
	}
	
	public static double evaluateAtNumber(Polynomial p, double d) {
		double ret = 0;
		for (int i = 0; i <= p.getDegree();i++) {
			ret += p.terms.get(i).getCoeff()*powerTo(d,i);
		}
		return ret;
	}
	
	public static double evaluateAtOne(Polynomial p) {
		double ret = 0;
		for (int i = 0; i <= p.getDegree();i++) {
			ret += p.terms.get(i).getCoeff();
		}
		return ret;
	}
	//TO FIX
	public static double evaluateAtNegativeOne(Polynomial p) {
		double ret = 0;
		for (int i = 0; i <= p.getDegree();i++) {
			if (i%2 == 0) ret += p.terms.get(0).getCoeff();
			else ret -= p.terms.get(i).getCoeff();
		}
		return ret;
	}
	public static Polynomial multiplyPoly(Polynomial p1, Polynomial p2) {
		Map<Integer,Double> newPoly = new HashMap<Integer,Double>();
		for (PolyNode pn1 : p1.terms) {
			for (PolyNode pn2 : p2.terms) {
				int degree = pn1.getdegree() + pn2.getdegree();
				if (newPoly.containsKey(degree)) {
					double temp = newPoly.get(degree);
					newPoly.replace(degree, temp + pn1.getCoeff()*pn2.getCoeff());
				}
				else {
					newPoly.put(degree, pn1.getCoeff()*pn2.getCoeff());
				}
			}
		}
		double[] ret = new double[p1.getDegree() + p2.getDegree()+1];
		
		for (Map.Entry<Integer, Double> e : newPoly.entrySet()) {
			ret[e.getKey()] = e.getValue();
		}
		return new Polynomial(ret);
	}
	
	public static double dotProductStandard(Polynomial p1, Polynomial p2) {
		Polynomial integrated = integralZeroConstant(multiplyPoly(p1,p2));
		
		return evaluateAtOne(integrated) - evaluateAtNumber(integrated,-1);
	}
	
	public static Polynomial projection(Polynomial u, Polynomial v) {
		double scale = dotProductStandard(u,v)/dotProductStandard(u,u);
		return scalarMult(u,scale);
	}
	public static List<Polynomial> nLegendre(int n) {
		 List<Polynomial> ret = new ArrayList<Polynomial>();
		 ret.add(new Polynomial(0));
		 int numVectors = n-1;
		 int degreecurr = 1;
		 while (numVectors > 0) {
			 Polynomial temp = new Polynomial(degreecurr);
			 for (Polynomial p : ret) {
				 temp = subtract(temp,projection(p,temp));
			 }
			 ret.add(temp);
			 degreecurr++;
			 numVectors--;
		 }
		 
		 return ret;
	}
	
	public static double powerTo(double x,int n) {
		double ret = 1;
		while (true) {
			int parity = n%2;
			n /=2;
			
		if (parity == 1) {
			ret *=x;
		}
		if (n == 0) break;
		x *=x;
		
		}
		return ret;
	}
	public static void printPoly(Polynomial p) {
		String s = "";
		for (PolyNode pn : p.terms) {
			s += pn.getCoeff() + "*x^" + pn.getdegree() + " + ";
		}
		System.out.println(s);
	}
	public static void main(String[] args) {
		Polynomial p1 = new Polynomial(2);
		printPoly(derivative(p1));
		
		
	}
}
