import java.util.*;
public class Vector {
	private final int dimension;
	private double[] components;
	private double length;
	public Vector(int n) {
		this.dimension = n;
		components = new double[n];
		length = 0;
	}
	public Vector(int n, double[] comps)  throws IllegalArgumentException {
		this.dimension = n;
		components = new double[n];
		int i = 0;
		for (double comp : comps) {
			this.components[i] = comp;
			i++;
			length += comp*comp;
		}
		length = Math.sqrt(length);
	}
	
	public double getComponent(int dim) throws IllegalArgumentException {
		if (dim > this.dimension || dim < 0) throw new IllegalArgumentException("Not a valid component");
		
		return components[dim-1];
	}
	
	public void changeComponent(int dim, double set) throws IllegalArgumentException {
		if (dim > this.dimension || dim < 0) throw new IllegalArgumentException("Not a valid component");
		double removedComponent = components[dim-1];
		length = Math.sqrt(length*length - removedComponent*removedComponent + set*set);
		components[dim-1] = set;
	}
	
	public double getLength() {
		return this.length;
	}
	public int getDimension() {
		return this.dimension;
	}
	public static Vector add(List<Vector> list) {
		int dim = list.get(0).getDimension();
		double[] addedComps = new double[dim];
		for (Vector l : list) {
			if (l.getDimension() != dim) throw new IllegalArgumentException("Not a valid component");
			for (int i = 0; i < dim; i++) {
				addedComps[i] += l.getComponent(i+1);
			}
			
		}
		return new Vector(dim,addedComps);
	}
	public static Vector addtwo(Vector v1, Vector v2) {
		if (v1.getDimension() != v2.getDimension()) throw new IllegalArgumentException("Not same dimension");
		
		double[] addedComps = new double[v1.getDimension()];
		for (int i = 0; i < v1.getDimension();i++) {
			addedComps[i] = v1.getComponent(i+1) + v2.getComponent(i+1);
		}
		return new Vector(v1.getDimension(),addedComps);
	}
	
	public static void scalarMultChanges(double scale,Vector v) {
		for (int i = 0; i < v.getDimension();i++) {
			v.changeComponent(i+1, scale);
		}
	}
	public static Vector scalarMultDoesntChange(double scale, Vector v) {
		Vector ret = new Vector(v.getDimension());
		
		for (int i = 1; i <= v.getDimension();i++ ) {
			ret.changeComponent(i, scale*v.getComponent(i));
		}
		return ret;
	}
	public static double dotproduct(Vector v1, Vector v2) {
		if (v1.getDimension() != v2.getDimension()) throw new IllegalArgumentException("Not same dimension");
		
		int dim = v1.getDimension();
		int product = 0;
		for (int i = 1; i <= dim; i++) {
			product += v1.getComponent(i)*v2.getComponent(i);
		}
		return product;
	}
	
	public static Vector subtracttwo(Vector v1, Vector v2) {
		return Vector.addtwo(v1, Vector.scalarMultDoesntChange(-1, v2));
	}
	
	public static double getAngle(Vector v1, Vector v2) {
		double dot = Vector.dotproduct(v1, v2);
		return Math.acos(dot/(v1.getLength()*v2.getLength()));
	}
	//proj_(u)_[v]
	public static Vector projection(Vector u, Vector v) {
		double scalar = Vector.dotproduct(u, v)/(u.getLength()*u.getLength());
		return Vector.scalarMultDoesntChange(scalar, u);
	}
	
	public static List<Vector> orthogonalize(List<Vector> l) {
		if (l.size() > l.get(0).getDimension()) throw new IllegalArgumentException("Can't orthogonalize, too many vectors");
		int dim = l.get(0).getDimension();
		List<Vector> normalized = new ArrayList<Vector>();
		
		for (Vector v : l) {
			if (v.getDimension() != dim) throw new IllegalArgumentException("Not same dimension");
			normalized.add(Vector.scalarMultDoesntChange(1/v.getLength(), v));
		}
		List<Vector> ret = new ArrayList<Vector>();
		ret.add(normalized.get(0));
		
		for (int i = 1; i < normalized.size();i++) {
			Vector toAdd = normalized.get(i);
			for (Vector proj : ret) {
				toAdd = Vector.subtracttwo(toAdd, projection(proj,toAdd));
			}
			ret.add(toAdd);
			
		}
		return ret;
	}
	public static void printVector(Vector v) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 1; i <= v.getDimension();i++) {
			sb.append(v.getComponent(i) + ",");
		}
		sb.append("]");
		System.out.println(sb.toString());
	}
	
	public static void main(String[] args) {
		double[] d = {1,1,1};
		double[] d2 = {2,1,0};
		double[] d3 = {5,1,3};
		Vector v1 = new Vector(3,d);
		System.out.println(v1.getLength());
		Vector v2 = new Vector(3,d2);
		Vector v3 = new Vector(3,d3);
		List<Vector> l = new ArrayList<Vector>();
		l.add(v1);
		l.add(v2);
		l.add(v3);
		List<Vector> o = orthogonalize(l);
		
		System.out.println(dotproduct(o.get(0),o.get(2)));
	
}
}
