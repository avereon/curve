package com.avereon.curve.math;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

public class PolynomialTest {

	private static final Offset<Double> tolerance = Offset.offset( 1e-12 );

	@Test
	void testConstants() {
		assertThat( Polynomial.LN2 ).isEqualTo( 0.6931471805599453 );
		assertThat( Polynomial.LN10 ).isEqualTo( 2.302585092994046 );
	}

	@Test
	void testPolynomialWithNull() {
		try {
			new Polynomial( (double[])null );
			fail();
		} catch( NullPointerException ignored ) {}
	}

	@Test
	void testPolynomialWithZeroCoefficients() {
		try {
			new Polynomial();
			fail();
		} catch( IllegalArgumentException ignored ) {}
	}

	@Test
	void testGetDegree() {
		assertThat( new Polynomial( 0 ).getDegree() ).isEqualTo( 0 );
		assertThat( new Polynomial( 2 ).getDegree() ).isEqualTo( 0 );
		assertThat( new Polynomial( 1.0, 2.0, 3.0 ).getDegree() ).isEqualTo( 2 );
	}

	@Test
	void testGetCoefficients() {
		assertThat( new Polynomial( 1, 2, 3 ).getCoefficients() ).isEqualTo( new double[]{ 1, 2, 3 } );
		assertThat( new Polynomial( 1, 0, 0 ).getCoefficients() ).isEqualTo( new double[]{ 1, 0, 0 } );
		assertThat( new Polynomial( 0, 0, 1 ).getCoefficients() ).isEqualTo( new double[]{ 1 } );
	}

	@Test
	void testEvaluate() {
		assertThat( new Polynomial( 2 ).evaluate( -1 ) ).isEqualTo( 2.0 );
		assertThat( new Polynomial( 2 ).evaluate( 0 ) ).isEqualTo( 2.0 );
		assertThat( new Polynomial( 2 ).evaluate( 1 ) ).isEqualTo( 2.0 );

		assertThat( new Polynomial( 2, 0 ).evaluate( -1 ) ).isEqualTo( -2.0 );
		assertThat( new Polynomial( 2, 0 ).evaluate( 0 ) ).isEqualTo( 0.0 );
		assertThat( new Polynomial( 2, 0 ).evaluate( 1 ) ).isEqualTo( 2.0 );

		assertThat( new Polynomial( 1, 2, 3 ).evaluate( 4 ) ).isEqualTo( 27.0 );

		assertThat( new Polynomial( 5 ).evaluate( 1 ) ).isEqualTo( 5.0 );
		assertThat( new Polynomial( 5 ).evaluate( 2 ) ).isEqualTo( 5.0 );

		assertThat( new Polynomial( -3, 2 ).evaluate( 1 ) ).isEqualTo( -1.0 );
		assertThat( new Polynomial( -3, 2 ).evaluate( 2 ) ).isEqualTo( -4.0 );

		assertThat( new Polynomial( 3, 2, 1 ).evaluate( 2 ) ).isEqualTo( 17.0 );
		assertThat( new Polynomial( 3, 2, 1 ).evaluate( -2 ) ).isEqualTo( 9.0 );
	}

	@Test
	void testAdd() {
		assertThat( new Polynomial( 1 ).add( new Polynomial( 2 ) ) ).isEqualTo( new Polynomial( 3 ) );
		assertThat( new Polynomial( 1, -2 ).add( new Polynomial( -3, 4 ) ) ).isEqualTo( new Polynomial( -2, 2 ) );
	}

	@Test
	void testMultiply() {
		assertThat( new Polynomial( 1, 1 ).multiply( new Polynomial( 1, 1 ) ) ).isEqualTo( new Polynomial( 1, 2, 1 ) );
		assertThat( new Polynomial( 1, 2, 1 ).multiply( new Polynomial( 2 ) ) ).isEqualTo( new Polynomial( 2, 4, 2 ) );
		assertThat( new Polynomial( 1 ).multiply( new Polynomial( 2 ) ) ).isEqualTo( new Polynomial( 2 ) );
		assertThat( new Polynomial( 1, -2 ).multiply( new Polynomial( -3, 4 ) ) ).isEqualTo( new Polynomial( -3, 10, -8 ) );
	}

	@Test
	void testDivide() {
		Polynomial a = new Polynomial( 2 );
		a.divide( 2 );
		assertThat( a ).isEqualTo( new Polynomial( 1 ) );

		Polynomial b = new Polynomial( 1, 2, 3 );
		b.divide( 2 );
		assertThat( b ).isEqualTo( new Polynomial( 0.5, 1, 1.5 ) );
	}

	@Test
	void testSimplify() {
		Polynomial a = new Polynomial( 1e-12, 1, 0 );
		assertThat( a.getDegree() ).isEqualTo( 1 );

		Polynomial b = new Polynomial( 1e-11, 1, 0 );
		assertThat( b.getDegree() ).isEqualTo( 2 );

		Polynomial c = new Polynomial( 1 + Double.MIN_VALUE, 1, 0 );
		assertThat( c.getDegree() ).isEqualTo( 2 );
	}

	@Test
	void testGetDerivative() {
		assertThat( new Polynomial( 2, 0, 0 ).getDerivative() ).isEqualTo( new Polynomial( 4, 0 ) );
	}

	@Test
	void testBisection() {
		double value = Math.sqrt( 0.5 );
		assertThat( new Polynomial( 2, 0, -1 ).bisection( -2, -1 ) ).isNaN();
		assertThat( new Polynomial( 2, 0, -1 ).bisection( -1, 0 ) ).isCloseTo( -value, Offset.offset( 10 * Polynomial.TOLERANCE ) );
		assertThat( new Polynomial( 2, 0, -1 ).bisection( 0, 1 ) ).isCloseTo( value, Offset.offset( 10 * Polynomial.TOLERANCE ) );
		assertThat( new Polynomial( 2, 0, -1 ).bisection( 1, 2 ) ).isNaN();
	}

	@Test
	void testGetRootsLinear() {
		double[] roots = new Polynomial( 2, -1 ).getRoots();
		assertThat( roots[ 0 ] ).isEqualTo( 0.5 );
		assertThat( roots.length ).isEqualTo( 1 );
	}

	@Test
	void testGetRootsQuadricWith0Roots() {
		double[] roots = new Polynomial( 1, 0, 1 ).getRoots();
		assertThat( roots.length ).isEqualTo( 0 );
	}

	@Test
	void testGetRootsQuadricWith1Roots() {
		double[] roots = new Polynomial( 1, 0, 0 ).getRoots();
		assertThat( roots[ 0 ] ).isEqualTo( 0.0 );
		assertThat( roots.length ).isEqualTo( 1 );
	}

	@Test
	void testGetRootsQuadricWith2Roots() {
		double[] roots = new Polynomial( 1, 0, -1 ).getRoots();
		assertThat( roots[ 0 ] ).isEqualTo( -1.0 );
		assertThat( roots[ 1 ] ).isEqualTo( 1.0 );
		assertThat( roots.length ).isEqualTo( 2 );

		roots = new Polynomial( 1, -1, 0.09 ).getRoots();
		assertThat( roots[ 0 ] ).isCloseTo( 0.1, tolerance );
		assertThat( roots[ 1 ] ).isCloseTo( 0.9, tolerance );
		assertThat( roots.length ).isEqualTo( 2 );
	}

	@Test
	void testGetRootsCubic() {
		double[] roots = new Polynomial( 1, 0, 0, 0 ).getRoots();
		assertThat( roots[ 0 ] ).isEqualTo( 0.0 );
		assertThat( roots.length ).isEqualTo( 1 );

		roots = new Polynomial( -1, 0, 0, 1 ).getRoots();
		assertThat( roots[ 0 ] ).isEqualTo( 1.0 );
		assertThat( roots.length ).isEqualTo( 1 );
	}

	@Test
	void testGetRootsCubicWith1Roots() {
		double[] roots = new Polynomial( 1, 0, 0, 0 ).getRoots();
		assertThat( roots[ 0 ] ).isEqualTo( 0.0 );
		assertThat( roots.length ).isEqualTo( 1 );

		roots = new Polynomial( 1, 0, 1, -2 ).getRoots();
		assertThat( roots[ 0 ] ).isCloseTo( 1.0, tolerance );
		assertThat( roots.length ).isEqualTo( 1 );

		roots = new Polynomial( 1, -3, 3, -1 ).getRoots();
		assertThat( roots[ 0 ] ).isCloseTo( 1.0, tolerance );
		assertThat( roots.length ).isEqualTo( 1 );

		roots = new Polynomial( 1, -9, +27, -27 ).getRoots();
		assertThat( roots[ 0 ] ).isCloseTo( 3.0, tolerance );
		assertThat( roots.length ).isEqualTo( 1 );
	}

	@Test
	void testGetRootsCubicWith2Roots() {
		double[] roots = new Polynomial( 1, 0, -3, 2 ).getRoots();
		assertThat( roots[ 0 ] ).isEqualTo( -2.0 );
		assertThat( roots[ 1 ] ).isEqualTo( 1.0 );
		assertThat( roots.length ).isEqualTo( 2 );

		roots = new Polynomial( 1, -1, 0, 0 ).getRoots();
		assertThat( roots[ 0 ] ).isEqualTo( 1.0 );
		assertThat( roots[ 1 ] ).isEqualTo( 0.0 );
		assertThat( roots.length ).isEqualTo( 2 );
	}

	@Test
	void testGetRootsCubicWith3Roots() {
		double[] roots = new Polynomial( 1, 0, -1, 0 ).getRoots();
		assertThat( roots[ 0 ] ).isEqualTo( -1.0 );
		assertThat( roots[ 1 ] ).isCloseTo( 0.0, tolerance );
		assertThat( roots[ 2 ] ).isEqualTo( 1.0 );
		assertThat( roots.length ).isEqualTo( 3 );
	}

	@Test
	void testGetRootsQuarticWith0Roots() {
		double[] roots = new Polynomial( 1, 0, 0, 0, 1 ).getRoots();
		assertThat( roots.length ).isEqualTo( 0 );
	}

	@Test
	void testGetRootsQuarticWith1Roots() {
		double[] roots = new Polynomial( 1, 0, 0, 0, 0 ).getRoots();
		assertThat( roots.length ).isEqualTo( 1 );
		assertThat( roots[ 0 ] ).isEqualTo( 0.0 );
	}

	@Test
	void testGetRootsQuarticWith2Roots() {
		double[] roots = new Polynomial( 1, 0, 0, 0, -1 ).getRoots();
		assertThat( roots[ 0 ] ).isEqualTo( -1.0 );
		assertThat( roots[ 1 ] ).isEqualTo( 1.0 );
		assertThat( roots.length ).isEqualTo( 2 );
	}

	@Test
	void testGetRootsQuarticWith3Roots() {
		double[] roots = new Polynomial( 1, 0, -4, 0, 0 ).getRoots();
		assertThat( roots[ 0 ] ).isEqualTo( -2.0 );
		assertThat( roots[ 1 ] ).isCloseTo( 0.0, tolerance );
		assertThat( roots[ 2 ] ).isEqualTo( 2.0 );
		assertThat( roots.length ).isEqualTo( 3 );
	}

	@Test
	void testGetRootsQuarticWith4Roots() {
		double[] roots = new Polynomial( 3, 6, -123, -126, 1080 ).getRoots();
		assertThat( roots[ 0 ] ).isCloseTo( -6.0, tolerance );
		assertThat( roots[ 1 ] ).isCloseTo( 5.0, tolerance );
		assertThat( roots[ 2 ] ).isCloseTo( -4.0, tolerance );
		assertThat( roots[ 3 ] ).isCloseTo( 3.0, tolerance );
		assertThat( roots.length ).isEqualTo( 4 );
	}

	@Test
	void testEquals() {
		assertThat( new Polynomial( 1 ) ).isEqualTo( new Polynomial( 1 ) );
		assertThat( new Polynomial( 2, 1 ) ).isEqualTo( new Polynomial( 2, 1 ) );
		assertThat( new Polynomial( 1, 2, 3 ) ).isEqualTo( new Polynomial( 1, 2, 3 ) );

		assertThat( new Polynomial( 2 ) ).isNotEqualTo( new Polynomial( 1 ) );
		assertThat( new Polynomial( 1, 2 ) ).isNotEqualTo( new Polynomial( 1 ) );
		assertThat( new Polynomial( 2, 1 ) ).isNotEqualTo( new Polynomial( 1 ) );
		assertThat( new Polynomial( 1 ) ).isNotEqualTo( new Polynomial( 2 ) );
		assertThat( new Polynomial( 2, 1 ) ).isNotEqualTo( new Polynomial( 3, 1 ) );
		assertThat( new Polynomial( 1, 2, 3 ) ).isNotEqualTo( new Polynomial( 3, 2, 1 ) );
	}

	@Test
	void testToString() {
		assertThat( new Polynomial( 2 ).toString() ).isEqualTo( "2.0" );
		assertThat( new Polynomial( -1, 2 ).toString() ).isEqualTo( "-1.0x + 2.0" );
		assertThat( new Polynomial( 3, 0, 2 ).toString() ).isEqualTo( "3.0x^2 + 2.0" );
		assertThat( new Polynomial( 3, -7, 0 ).toString() ).isEqualTo( "3.0x^2 - 7.0x" );
		assertThat( new Polynomial( -3, -7, 4 ).toString() ).isEqualTo( "-3.0x^2 - 7.0x + 4.0" );
		assertThat( new Polynomial( -5, 0, 3, -0, -2 ).toString() ).isEqualTo( "-5.0x^4 + 3.0x^2 - 2.0" );
	}

}
