package com.avereon.curve.math;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MatrixTest {

	@Test
	public void testSolve() {
		double[][] matrix = { { 2, 1, -1, 8 }, { -3, -1, 2, -11 }, { -2, 1, 2, -3 } };

		Matrix.gauss( matrix );

		assertThat( matrix[ 0 ][ 3 ] ).isEqualTo(2, Offset.offset(1e-12));
		assertThat( matrix[ 1 ][ 3 ] ).isEqualTo( 3, Offset.offset(1e-12) );
		assertThat( matrix[ 2 ][ 3 ] ).isEqualTo( -1, Offset.offset(1e-12) );
	}

	@Test
	public void testSolveWithExtras() {
		double[][] matrix = { { 2, 1, -1, 8, 3 }, { -3, -1, 2, -11, 7 }, { -2, 1, 2, -3, -2 }, { -1, 6, 7, -2, -3 } };

		Matrix.gauss( matrix, 1, 1 );

		assertThat( matrix[ 0 ][ 3 ] ).isEqualTo(2, Offset.offset(1e-12));
		assertThat( matrix[ 1 ][ 3 ] ).isEqualTo( 3, Offset.offset(1e-12) );
		assertThat( matrix[ 2 ][ 3 ] ).isEqualTo( -1, Offset.offset(1e-12) );

		assertThat( matrix[ 0 ][ 4 ] ).isEqualTo( 35, Offset.offset(1e-12) );
		assertThat( matrix[ 1 ][ 4 ] ).isEqualTo( -22, Offset.offset(1e-12) );
		assertThat( matrix[ 2 ][ 4 ] ).isEqualTo( 45, Offset.offset(1e-12) );
	}

	@Test
	public void testSwap() {
		double[][] matrix = { { 2, 1, -1, 8 }, { -3, -1, 2, -11 }, { -2, 1, 2, -3 } };

		Matrix.swap( matrix, 0, 2 );

		assertThat( matrix[ 0 ][ 0 ] ).isEqualTo( -2, Offset.offset( 1E-12 ) );
		assertThat( matrix[ 0 ][ 1 ] ).isEqualTo( 1, Offset.offset( 1E-12 ) );
		assertThat( matrix[ 0 ][ 2 ] ).isEqualTo( 2, Offset.offset( 1E-12 ) );
		assertThat( matrix[ 0 ][ 3 ] ).isEqualTo( -3, Offset.offset( 1E-12 ) );

		assertThat( matrix[ 2 ][ 0 ] ).isEqualTo( 2, Offset.offset( 1E-12 ) );
		assertThat( matrix[ 2 ][ 1 ] ).isEqualTo( 1, Offset.offset( 1E-12 ) );
		assertThat( matrix[ 2 ][ 2 ] ).isEqualTo( -1, Offset.offset( 1E-12 ) );
		assertThat( matrix[ 2 ][ 3 ] ).isEqualTo( 8, Offset.offset( 1E-12 ) );
	}

	@Test
	public void testMultiply() {
		double[][] matrix = { { 2, 1, -1, 8 }, { -3, -1, 2, -11 }, { -2, 1, 2, -3 } };

		Matrix.multiply( matrix, 0, 2 );

		assertThat( matrix[ 0 ][ 0 ] ).isEqualTo( 4, Offset.offset( 1E-12 ) );
		assertThat( matrix[ 0 ][ 1 ] ).isEqualTo( 2, Offset.offset( 1E-12 ) );
		assertThat( matrix[ 0 ][ 2 ] ).isEqualTo( -2, Offset.offset( 1E-12 ) );
		assertThat( matrix[ 0 ][ 3 ] ).isEqualTo( 16, Offset.offset( 1E-12 ) );
	}

	@Test
	public void testCombine() {
		double[][] matrix = { { 2, 1, -1, 8 }, { -3, -1, 2, -11 }, { -2, 1, 2, -3 } };

		Matrix.combine( matrix, 0, 1, 2 );

		assertThat( matrix[ 0 ][ 0 ] ).isEqualTo( 8, Offset.offset( 1E-12 ) );
		assertThat( matrix[ 0 ][ 1 ] ).isEqualTo( 3, Offset.offset( 1E-12 ) );
		assertThat( matrix[ 0 ][ 2 ] ).isEqualTo( -5, Offset.offset( 1E-12 ) );
		assertThat( matrix[ 0 ][ 3 ] ).isEqualTo( 30, Offset.offset( 1E-12 ) );
	}

}
