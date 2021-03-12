package com.avereon.curve.math;

import org.junit.jupiter.api.Test;

import static com.avereon.curve.match.Matchers.near;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

public class PolynomialTest {

	@Test
	void testConstants() {
		assertThat( Polynomial.LN2, is( 0.6931471805599453 ) );
		assertThat( Polynomial.LN10, is( 2.302585092994046 ) );
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
		assertThat( new Polynomial( 0 ).getDegree(), is( 0 ) );
		assertThat( new Polynomial( 2 ).getDegree(), is( 0 ) );
		assertThat( new Polynomial( 1.0, 2.0, 3.0 ).getDegree(), is( 2 ) );
	}

	@Test
	void testGetCoefficients() {
		assertThat( new Polynomial( 1, 2, 3 ).getCoefficients(), is( new double[]{ 1, 2, 3 } ) );
		assertThat( new Polynomial( 1, 0, 0 ).getCoefficients(), is( new double[]{ 1, 0, 0 } ) );
		assertThat( new Polynomial( 0, 0, 1 ).getCoefficients(), is( new double[]{ 1 } ) );
	}

	@Test
	void testEvaluate() {
		assertThat( new Polynomial( 2 ).evaluate( -1 ), is( 2.0 ) );
		assertThat( new Polynomial( 2 ).evaluate( 0 ), is( 2.0 ) );
		assertThat( new Polynomial( 2 ).evaluate( 1 ), is( 2.0 ) );

		assertThat( new Polynomial( 2, 0 ).evaluate( -1 ), is( -2.0 ) );
		assertThat( new Polynomial( 2, 0 ).evaluate( 0 ), is( 0.0 ) );
		assertThat( new Polynomial( 2, 0 ).evaluate( 1 ), is( 2.0 ) );

		assertThat( new Polynomial( 1, 2, 3 ).evaluate( 4 ), is( 27.0 ) );

		assertThat( new Polynomial( 5 ).evaluate( 1 ), is( 5.0 ) );
		assertThat( new Polynomial( 5 ).evaluate( 2 ), is( 5.0 ) );

		assertThat( new Polynomial( -3, 2 ).evaluate( 1 ), is( -1.0 ) );
		assertThat( new Polynomial( -3, 2 ).evaluate( 2 ), is( -4.0 ) );

		assertThat( new Polynomial( 3, 2, 1 ).evaluate( 2 ), is( 17.0 ) );
		assertThat( new Polynomial( 3, 2, 1 ).evaluate( -2 ), is( 9.0 ) );
	}

	@Test
	void testAdd() {
		assertThat( new Polynomial( 1 ).add( new Polynomial( 2 ) ), is( new Polynomial( 3 ) ) );
		assertThat( new Polynomial( 1, -2 ).add( new Polynomial( -3, 4 ) ), is( new Polynomial( -2, 2 ) ) );
	}

	@Test
	void testMultiply() {
		assertThat( new Polynomial( 1, 1 ).multiply( new Polynomial( 1, 1 ) ), is( new Polynomial( 1, 2, 1 ) ) );
		assertThat( new Polynomial( 1, 2, 1 ).multiply( new Polynomial( 2 ) ), is( new Polynomial( 2, 4, 2 ) ) );
		assertThat( new Polynomial( 1 ).multiply( new Polynomial( 2 ) ), is( new Polynomial( 2 ) ) );
		assertThat( new Polynomial( 1, -2 ).multiply( new Polynomial( -3, 4 ) ), is( new Polynomial( -3, 10, -8 ) ) );
	}

	@Test
	void testDivide() {
		Polynomial a = new Polynomial( 2 );
		a.divide( 2 );
		assertThat( a, is( new Polynomial( 1 ) ) );

		Polynomial b = new Polynomial( 1, 2, 3 );
		b.divide( 2 );
		assertThat( b, is( new Polynomial( 0.5, 1, 1.5 ) ) );
	}

	@Test
	void testSimplify() {
		Polynomial a = new Polynomial( 1e-12, 1, 0 );
		assertThat( a.getDegree(), is( 1 ) );

		Polynomial b = new Polynomial( 1e-11, 1, 0 );
		assertThat( b.getDegree(), is( 2 ) );

		Polynomial c = new Polynomial( 1 + Double.MIN_VALUE, 1, 0 );
		assertThat( c.getDegree(), is( 2 ) );
	}

	@Test
	void testGetDerivative() {
		assertThat( new Polynomial( 2, 0, 0 ).getDerivative(), is( new Polynomial( 4, 0 ) ) );
	}

	//	@Test
	//	@Test void testGetLinearRoot() {
	//		assertThat( new double[] { 0.0 }, new Polynomial( -2, 0 ).getRoots() );
	//		assertThat( new double[] { -0.0 }, new Polynomial( 2, 0 ).getRoots() );
	//		assertThat( new double[] { 1.0 }, new Polynomial( 1, -1 ).getRoots() );
	//		assertThat( new double[] { 0.5 }, new Polynomial( 2, -1 ).getRoots() );
	//		assertThat( new double[] { -2.0 }, new Polynomial( 1, 2 ).getRoots() );
	//	}
	//
	//	@Test
	//	@Test void testGetQuadricRoots() {
	//		assertThat( new double[] { 1 }, new Polynomial( 1, -2, 1 ).getRoots() );
	//		assertThat( new double[] { -1 }, new Polynomial( 1, 2, 1 ).getRoots() );
	//		assertThat( new double[] { 1, 2 }, new Polynomial( 1, -3, 2 ).getRoots() );
	//		assertThat( new double[] { 0, 3 }, new Polynomial( 1, -3, 0 ).getRoots() );
	//
	//		// Test x^2.
	//		assertThat( new double[] { 0 }, new Polynomial( 1, 0, 0 ).getRoots() );
	//
	//		// Test X^2-10+25.
	//		assertThat( new double[] { 5.0 }, new Polynomial( 1, -10, 25 ).getRoots() );
	//
	//		// Test x^2 + 1.
	//		assertThat( new double[] {}, new Polynomial( 1, 0, 1 ).getRoots() );
	//	}
	//
	//	@Test
	//	@Test void testGetCubicRoots() {
	//		assertThat( new double[] { 1, 2, 3 }, new Polynomial( 1, -6, 11, -6 ).getRoots(), 1e-15 );
	//		assertThat( new double[] { -3, -2, -1 }, new Polynomial( 1, 6, 11, 6 ).getRoots(), 1e-15 );
	//
	//		// Test x^3.
	//		assertThat( new double[] { 0 }, new Polynomial( 1, 0, 0, 0 ).getRoots() );
	//	}
	//
	//	@Test
	//	@Test void testGetQuarticRoots() {
	//		assertThat( new double[] { 1, 2, 3, 4 }, new Polynomial( 1, -10, 35, -50, 24 ).getRoots() );
	//		assertThat( new double[] { -4, -3, -2, -1 }, new Polynomial( 1, 10, 35, 50, 24 ).getRoots() );
	//		assertThat( new double[] { 1, 2 }, new Polynomial( 1, -6, 13, -12, 4 ).getRoots() );
	//
	//		// Text x^4.
	//		assertThat( new double[] { 0 }, new Polynomial( 1, 0, 0, 0, 0 ).getRoots() );
	//
	//		// Test x^4 + 1.
	//		assertThat( new double[] {}, new Polynomial( 1, 0, 0, 0, 1 ).getRoots() );
	//
	//		// Test x^4 - 1.
	//		assertThat( new double[] { -1, 1 }, new Polynomial( 1, 0, 0, 0, -1 ).getRoots() );
	//	}

	@Test
	void testGetRootsLinear() {
		double[] roots = new Polynomial( 2, -1 ).getRoots();
		assertThat( roots[ 0 ], is( 0.5 ) );
		assertThat( roots.length, is( 1 ) );
	}

	@Test
	void testGetRootsQuadricWith0Roots() {
		double[] roots = new Polynomial( 1, 0, 1 ).getRoots();
		assertThat( roots.length, is( 0 ) );
	}

	@Test
	void testGetRootsQuadricWith1Roots() {
		double[] roots = new Polynomial( 1, 0, 0 ).getRoots();
		assertThat( roots[ 0 ], is( 0.0 ) );
		assertThat( roots.length, is( 1 ) );
	}

	@Test
	void testGetRootsQuadricWith2Roots() {
		double[] roots = new Polynomial( 1, 0, -1 ).getRoots();
		assertThat( roots[ 0 ], is( -1.0 ) );
		assertThat( roots[ 1 ], is( 1.0 ) );
		assertThat( roots.length, is( 2 ) );
	}

	@Test
	void testGetRootsCubic() {
		double[] roots = new Polynomial( 1, 0, 0, 0 ).getRoots();
		assertThat( roots[ 0 ], is( 0.0 ) );
		assertThat( roots.length, is( 1 ) );

		roots = new Polynomial( -1, 0, 0, 1 ).getRoots();
		assertThat( roots[ 0 ], is( 1.0 ) );
		assertThat( roots.length, is( 1 ) );
	}

	@Test
	void testGetRootsCubicWith1Roots() {
		double[] roots = new Polynomial( 1, 0, 0, 0 ).getRoots();
		assertThat( roots[ 0 ], is( 0.0 ) );
		assertThat( roots.length, is( 1 ) );

		roots = new Polynomial( 1, 0, 1, -2 ).getRoots();
		assertThat( roots[ 0 ], near( 1.0 ) );
		assertThat( roots.length, is( 1 ) );

		roots = new Polynomial( 1, -3, 3, -1 ).getRoots();
		assertThat( roots[ 0 ], near( 1.0 ) );
		assertThat( roots.length, is( 1 ) );

		roots = new Polynomial( 1, -9, +27, -27 ).getRoots();
		assertThat( roots[ 0 ], near( 3.0 ) );
		assertThat( roots.length, is( 1 ) );
	}

	@Test
	void testGetRootsCubicWith2Roots() {
		double[] roots = new Polynomial( 1, 0, -3, 2 ).getRoots();
		assertThat( roots[ 0 ], is( -2.0 ) );
		assertThat( roots[ 1 ], is( 1.0 ) );
		assertThat( roots.length, is( 2 ) );

		roots = new Polynomial( 1, -1, 0, 0 ).getRoots();
		assertThat( roots[ 0 ], is( 1.0 ) );
		assertThat( roots[ 1 ], is( 0.0 ) );
		assertThat( roots.length, is( 2 ) );
	}

	@Test
	void testGetRootsCubicWith3Roots() {
		double[] roots = new Polynomial( 1, 0, -1, 0 ).getRoots();
		assertThat( roots[ 0 ], is( -1.0 ) );
		assertThat( roots[ 1 ], near( 0.0 ) );
		assertThat( roots[ 2 ], is( 1.0 ) );
		assertThat( roots.length, is( 3 ) );
	}

	@Test
	void testGetRootsQuarticWith0Roots() {
		double[] roots = new Polynomial( 1, 0, 0, 0, 1 ).getRoots();
		assertThat( roots.length, is( 0 ) );
	}

	@Test
	void testGetRootsQuarticWith1Roots() {
		double[] roots = new Polynomial( 1, 0, 0, 0, 0 ).getRoots();
		assertThat( roots.length, is( 1 ) );
		assertThat( roots[ 0 ], is( 0.0 ) );
	}

	@Test
	void testGetRootsQuarticWith2Roots() {
		double[] roots = new Polynomial( 1, 0, 0, 0, -1 ).getRoots();
		assertThat( roots[ 0 ], is( -1.0 ) );
		assertThat( roots[ 1 ], is( 1.0 ) );
		assertThat( roots.length, is( 2 ) );
	}

	@Test
	void testGetRootsQuarticWith3Roots() {
		double[] roots = new Polynomial( 1, 0, -4, 0, 0 ).getRoots();
		assertThat( roots[ 0 ], is( -2.0 ) );
		assertThat( roots[ 1 ], near( 0.0 ) );
		assertThat( roots[ 2 ], is( 2.0 ) );
		assertThat( roots.length, is( 3 ) );
	}

	@Test
	void testGetRootsQuarticWith4Roots() {
		double[] roots = new Polynomial( 3, 6, -123, -126, 1080 ).getRoots();
		assertThat( roots[ 0 ], near( -6.0 ) );
		assertThat( roots[ 1 ], near( 5.0 ) );
		assertThat( roots[ 2 ], near( -4.0 ) );
		assertThat( roots[ 3 ], near( 3.0 ) );
		assertThat( roots.length, is( 4 ) );
	}

	@Test
	void testEquals() {
		assertEquals( new Polynomial( 1 ), new Polynomial( 1 ) );
		assertEquals( new Polynomial( 2, 1 ), new Polynomial( 2, 1 ) );
		assertEquals( new Polynomial( 1, 2, 3 ), new Polynomial( 1, 2, 3 ) );

		assertNotEquals( new Polynomial( 2 ), new Polynomial( 1 ) );
		assertNotEquals( new Polynomial( 1, 2 ), new Polynomial( 1 ) );
		assertNotEquals( new Polynomial( 2, 1 ), new Polynomial( 1 ) );
		assertNotEquals( new Polynomial( 1 ), new Polynomial( 2 ) );
		assertNotEquals( new Polynomial( 2, 1 ), new Polynomial( 3, 1 ) );
		assertNotEquals( new Polynomial( 1, 2, 3 ), new Polynomial( 3, 2, 1 ) );
	}

	@Test
	void testToString() {
		assertThat( new Polynomial( 2 ).toString(), is( "2.0" ) );
		assertThat( new Polynomial( -1, 2 ).toString(), is( "-1.0x + 2.0" ) );
		assertThat( new Polynomial( 3, 0, 2 ).toString(), is( "3.0x^2 + 2.0" ) );
		assertThat( new Polynomial( 3, -7, 0 ).toString(), is( "3.0x^2 - 7.0x" ) );
		assertThat( new Polynomial( -3, -7, 4 ).toString(), is( "-3.0x^2 - 7.0x + 4.0" ) );
		assertThat( new Polynomial( -5, 0, 3, -0, -2 ).toString(), is( "-5.0x^4 + 3.0x^2 - 2.0" ) );
	}

}
