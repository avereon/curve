package com.avereon.curve.math;

public class Matrix {

	public static final void gauss( double[][] matrix ) {
		gauss( matrix, 0, 0 );
	}

	public static final void gauss( double[][] matrix, int extrarows ) {
		gauss( matrix, extrarows, 0 );
	}

	public static final void gauss( double[][] matrix, int extrarows, int extracols ) {
		int m = matrix.length - extrarows;
		if( m == 0 ) return;
		int n = matrix[ 0 ].length - extracols;
		if( n == 0 ) return;

		int i = 0;
		int j = 0;
		int maxIndex = 0;
		double value = 0;
		double maxValue = 0;
		while( i < m & j < n ) {
			maxIndex = i;
			maxValue = matrix[ i ][ j ];

			for( int k = i + 1; k < m; k++ ) {
				value = matrix[ k ][ j ];
				if( Math.abs( value ) > Math.abs( maxValue ) ) {
					maxIndex = k;
					maxValue = value;
				}
			}

			if( Math.abs( maxValue ) > 0 ) {
				swap( matrix, i, maxIndex );
				multiply( matrix, i, 1 / maxValue );
				for( int u = 0; u < m; u++ ) {
					if( u != i ) {
						combine( matrix, u, i, matrix[ u ][ j ] );
					}
				}
				i++;
			}
			j++;
		}
	}

	public static final void swap( double[][] matrix, int source, int target ) {
		double[] row = matrix[ source ];
		matrix[ source ] = matrix[ target ];
		matrix[ target ] = row;
	}

	public static final void multiply( double[][] matrix, int row, double value ) {
		int n = matrix[ row ].length;
		for( int i = 0; i < n; i++ ) {
			matrix[ row ][ i ] *= value;
		}
	}

	public static final void combine( double[][] matrix, int target, int source, double value ) {
		int n = matrix[ source ].length;
		for( int i = 0; i < n; i++ ) {
			matrix[ target ][ i ] = matrix[ target ][ i ] - matrix[ source ][ i ] * value;
		}
	}

}
