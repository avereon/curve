package com.avereon.curve.math;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class Intersection {

	public enum Type {
		NONE,
		PARALLEL,
		COINCIDENT,
		SAME,
		INTERSECTION
	}

	private final Type type;

	private final double[][] points;

	protected Intersection( Type status, double[]... points ) {
		this.type = status;
		this.points = points;
	}

	public Type getType() {
		return type;
	}

	public double[][] getPoints() {
		return points;
	}

	boolean contains( double[] point ) {
		for( double[] check : points ) {
			if( Arrays.equals( point, check ) ) return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return type.name() + Arrays.stream( points ).map( p -> " " + Arrays.toString( p ) ).collect( Collectors.joining() );
	}

	@Override
	public boolean equals( Object object ) {
		if( !(object instanceof Intersection) ) return false;
		Intersection that = (Intersection)object;

		if( this.type != that.type ) return false;
		if( this.points.length != that.points.length ) return false;

		for( double[] point : this.points ) {
			boolean found = false;
			for( double[] check : that.points ) {
				if( Arrays.equals( point, check ) ) {
					found = true;
					break;
				}
			}
			if( !found ) return false;
		}

		return true;
	}

}
