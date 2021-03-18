package com.avereon.curve.math;

import java.nio.DoubleBuffer;

public class Transform {

	private final double[][] m = new double[ 4 ][ 4 ];

	private DoubleBuffer matrix;

	private boolean negate;

	public Transform( double[][] m ) {
		this( m[ 0 ][ 0 ],
			m[ 0 ][ 1 ],
			m[ 0 ][ 2 ],
			m[ 0 ][ 3 ],
			m[ 1 ][ 0 ],
			m[ 1 ][ 1 ],
			m[ 1 ][ 2 ],
			m[ 1 ][ 3 ],
			m[ 2 ][ 0 ],
			m[ 2 ][ 1 ],
			m[ 2 ][ 2 ],
			m[ 2 ][ 3 ],
			m[ 3 ][ 0 ],
			m[ 3 ][ 1 ],
			m[ 3 ][ 2 ],
			m[ 3 ][ 3 ]
		);
	}

	/**
	 * Create a new transform.
	 */
	public Transform(
		double m00, double m01, double m02, double m03, double m10, double m11, double m12, double m13, double m20, double m21, double m22, double m23, double m30, double m31, double m32, double m33
	) {
		m[ 0 ][ 0 ] = m00;
		m[ 0 ][ 1 ] = m01;
		m[ 0 ][ 2 ] = m02;
		m[ 0 ][ 3 ] = m03;
		m[ 1 ][ 0 ] = m10;
		m[ 1 ][ 1 ] = m11;
		m[ 1 ][ 2 ] = m12;
		m[ 1 ][ 3 ] = m13;
		m[ 2 ][ 0 ] = m20;
		m[ 2 ][ 1 ] = m21;
		m[ 2 ][ 2 ] = m22;
		m[ 2 ][ 3 ] = m23;
		m[ 3 ][ 0 ] = m30;
		m[ 3 ][ 1 ] = m31;
		m[ 3 ][ 2 ] = m32;
		m[ 3 ][ 3 ] = m33;
	}

	public boolean isMirror() {
		// FIXME This really should be the x-scale and y-scale have different signs
		// This can use a scale of the unit vectors to determine the signum for each axis
		double[] w = applyXY( Vector.of( 1, 1, 0 ) );
		double x = Math.signum( w[ 0 ] );
		double y = Math.signum( w[ 1 ] );
		return x != y;
	}

	/**
	 * Multiply this transform by a vector. Both the input and output vectors are
	 * assumed to be in homogeneous coordinates, and to have an implicit fourth
	 * element equal to 1.
	 */
	// TODO Rename to apply?
	public final double[] apply( double[] vector ) {
		double w = m[ 3 ][ 0 ] * vector[ 0 ] + m[ 3 ][ 1 ] * vector[ 1 ] + m[ 3 ][ 2 ] * vector[ 2 ] + m[ 3 ][ 3 ];
		return Vector.of( (m[ 0 ][ 0 ] * vector[ 0 ] + m[ 0 ][ 1 ] * vector[ 1 ] + m[ 0 ][ 2 ] * vector[ 2 ] + m[ 0 ][ 3 ]) / w,
			(m[ 1 ][ 0 ] * vector[ 0 ] + m[ 1 ][ 1 ] * vector[ 1 ] + m[ 1 ][ 2 ] * vector[ 2 ] + m[ 1 ][ 3 ]) / w,
			(m[ 2 ][ 0 ] * vector[ 0 ] + m[ 2 ][ 1 ] * vector[ 1 ] + m[ 2 ][ 2 ] * vector[ 2 ] + m[ 2 ][ 3 ]) / w
		);
	}

	/**
	 * This method is identical to times(), except that the specified vector is
	 * assumed to be a direction vector. The implicit fourth element is assumed to
	 * be 0.
	 * <p>
	 * This is faster than times() when a direction vector can be used.
	 */
	// TODO Rename to apply?
	public final double[] applyDirection( double[] vector ) {
		return Vector.of( m[ 0 ][ 0 ] * vector[ 0 ] + m[ 0 ][ 1 ] * vector[ 1 ] + m[ 0 ][ 2 ] * vector[ 2 ],
			m[ 1 ][ 0 ] * vector[ 0 ] + m[ 1 ][ 1 ] * vector[ 1 ] + m[ 1 ][ 2 ] * vector[ 2 ],
			m[ 2 ][ 0 ] * vector[ 0 ] + m[ 2 ][ 1 ] * vector[ 1 ] + m[ 2 ][ 2 ] * vector[ 2 ]
		);
	}

	/**
	 * This method is identical to times(), except that it only calculates the x
	 * and y components of the output vector. This can save several operations
	 * when the z component is not of interest.
	 */
	// TODO Rename to apply?
	public final double[] applyXY( double[] vector ) {
		double w = m[ 3 ][ 0 ] * vector[ 0 ] + m[ 3 ][ 1 ] * vector[ 1 ] + m[ 3 ][ 2 ] * vector[ 2 ] + m[ 3 ][ 3 ];
		return Vector.of( (m[ 0 ][ 0 ] * vector[ 0 ] + m[ 0 ][ 1 ] * vector[ 1 ] + m[ 0 ][ 2 ] * vector[ 2 ] + m[ 0 ][ 3 ]) / w,
			(m[ 1 ][ 0 ] * vector[ 0 ] + m[ 1 ][ 1 ] * vector[ 1 ] + m[ 1 ][ 2 ] * vector[ 2 ] + m[ 1 ][ 3 ]) / w
		);
	}

	/**
	 * This method is identical to times(), except that it only calculates the z
	 * component of the output vector. This is useful, for example, for
	 * determining whether a point lies in front or behind the camera.
	 */
	// TODO Rename to apply?
	public final double applyZ( double[] vector ) {
		double w = m[ 3 ][ 0 ] * vector[ 0 ] + m[ 3 ][ 1 ] * vector[ 1 ] + m[ 3 ][ 2 ] * vector[ 2 ] + m[ 3 ][ 3 ];
		return (m[ 2 ][ 0 ] * vector[ 0 ] + m[ 2 ][ 1 ] * vector[ 1 ] + m[ 2 ][ 2 ] * vector[ 2 ] + m[ 2 ][ 3 ]) / w;
	}

	/**
	 * Create a new transform by combining two transforms.
	 */
	public final Transform combine( Transform transform ) {
		double[][] a = this.m;
		double[][] b = transform.m;
		double[][] c = new double[ 4 ][ 4 ];

		c[ 0 ][ 0 ] = a[ 0 ][ 0 ] * b[ 0 ][ 0 ] + a[ 0 ][ 1 ] * b[ 1 ][ 0 ] + a[ 0 ][ 2 ] * b[ 2 ][ 0 ] + a[ 0 ][ 3 ] * b[ 3 ][ 0 ];
		c[ 0 ][ 1 ] = a[ 0 ][ 0 ] * b[ 0 ][ 1 ] + a[ 0 ][ 1 ] * b[ 1 ][ 1 ] + a[ 0 ][ 2 ] * b[ 2 ][ 1 ] + a[ 0 ][ 3 ] * b[ 3 ][ 1 ];
		c[ 0 ][ 2 ] = a[ 0 ][ 0 ] * b[ 0 ][ 2 ] + a[ 0 ][ 1 ] * b[ 1 ][ 2 ] + a[ 0 ][ 2 ] * b[ 2 ][ 2 ] + a[ 0 ][ 3 ] * b[ 3 ][ 2 ];
		c[ 0 ][ 3 ] = a[ 0 ][ 0 ] * b[ 0 ][ 3 ] + a[ 0 ][ 1 ] * b[ 1 ][ 3 ] + a[ 0 ][ 2 ] * b[ 2 ][ 3 ] + a[ 0 ][ 3 ] * b[ 3 ][ 3 ];
		c[ 1 ][ 0 ] = a[ 1 ][ 0 ] * b[ 0 ][ 0 ] + a[ 1 ][ 1 ] * b[ 1 ][ 0 ] + a[ 1 ][ 2 ] * b[ 2 ][ 0 ] + a[ 1 ][ 3 ] * b[ 3 ][ 0 ];
		c[ 1 ][ 1 ] = a[ 1 ][ 0 ] * b[ 0 ][ 1 ] + a[ 1 ][ 1 ] * b[ 1 ][ 1 ] + a[ 1 ][ 2 ] * b[ 2 ][ 1 ] + a[ 1 ][ 3 ] * b[ 3 ][ 1 ];
		c[ 1 ][ 2 ] = a[ 1 ][ 0 ] * b[ 0 ][ 2 ] + a[ 1 ][ 1 ] * b[ 1 ][ 2 ] + a[ 1 ][ 2 ] * b[ 2 ][ 2 ] + a[ 1 ][ 3 ] * b[ 3 ][ 2 ];
		c[ 1 ][ 3 ] = a[ 1 ][ 0 ] * b[ 0 ][ 3 ] + a[ 1 ][ 1 ] * b[ 1 ][ 3 ] + a[ 1 ][ 2 ] * b[ 2 ][ 3 ] + a[ 1 ][ 3 ] * b[ 3 ][ 3 ];
		c[ 2 ][ 0 ] = a[ 2 ][ 0 ] * b[ 0 ][ 0 ] + a[ 2 ][ 1 ] * b[ 1 ][ 0 ] + a[ 2 ][ 2 ] * b[ 2 ][ 0 ] + a[ 2 ][ 3 ] * b[ 3 ][ 0 ];
		c[ 2 ][ 1 ] = a[ 2 ][ 0 ] * b[ 0 ][ 1 ] + a[ 2 ][ 1 ] * b[ 1 ][ 1 ] + a[ 2 ][ 2 ] * b[ 2 ][ 1 ] + a[ 2 ][ 3 ] * b[ 3 ][ 1 ];
		c[ 2 ][ 2 ] = a[ 2 ][ 0 ] * b[ 0 ][ 2 ] + a[ 2 ][ 1 ] * b[ 1 ][ 2 ] + a[ 2 ][ 2 ] * b[ 2 ][ 2 ] + a[ 2 ][ 3 ] * b[ 3 ][ 2 ];
		c[ 2 ][ 3 ] = a[ 2 ][ 0 ] * b[ 0 ][ 3 ] + a[ 2 ][ 1 ] * b[ 1 ][ 3 ] + a[ 2 ][ 2 ] * b[ 2 ][ 3 ] + a[ 2 ][ 3 ] * b[ 3 ][ 3 ];
		c[ 3 ][ 0 ] = a[ 3 ][ 0 ] * b[ 0 ][ 0 ] + a[ 3 ][ 1 ] * b[ 1 ][ 0 ] + a[ 3 ][ 2 ] * b[ 2 ][ 0 ] + a[ 3 ][ 3 ] * b[ 3 ][ 0 ];
		c[ 3 ][ 1 ] = a[ 3 ][ 0 ] * b[ 0 ][ 1 ] + a[ 3 ][ 1 ] * b[ 1 ][ 1 ] + a[ 3 ][ 2 ] * b[ 2 ][ 1 ] + a[ 3 ][ 3 ] * b[ 3 ][ 1 ];
		c[ 3 ][ 2 ] = a[ 3 ][ 0 ] * b[ 0 ][ 2 ] + a[ 3 ][ 1 ] * b[ 1 ][ 2 ] + a[ 3 ][ 2 ] * b[ 2 ][ 2 ] + a[ 3 ][ 3 ] * b[ 3 ][ 2 ];
		c[ 3 ][ 3 ] = a[ 3 ][ 0 ] * b[ 0 ][ 3 ] + a[ 3 ][ 1 ] * b[ 1 ][ 3 ] + a[ 3 ][ 2 ] * b[ 2 ][ 3 ] + a[ 3 ][ 3 ] * b[ 3 ][ 3 ];

		Transform combined = new Transform( c );
		combined.negate = this.negate | transform.negate;
		return (new Transform( c ));
	}

	/**
	 * Get the inverse transform.
	 */
	public final Transform inverse() {
		if( determinant() == 0 ) throw new ArithmeticException( "Determinant cannot be zero." );

		double[][] b = new double[ 4 ][ 4 ];

		double t14 = m[ 0 ][ 0 ] * m[ 1 ][ 1 ];
		double t15 = m[ 2 ][ 2 ] * m[ 3 ][ 3 ];
		double t17 = m[ 2 ][ 3 ] * m[ 3 ][ 2 ];
		double t19 = m[ 0 ][ 0 ] * m[ 2 ][ 1 ];
		double t20 = m[ 1 ][ 2 ] * m[ 3 ][ 3 ];
		double t22 = m[ 1 ][ 3 ] * m[ 3 ][ 2 ];
		double t24 = m[ 0 ][ 0 ] * m[ 3 ][ 1 ];
		double t25 = m[ 1 ][ 2 ] * m[ 2 ][ 3 ];
		double t27 = m[ 1 ][ 3 ] * m[ 2 ][ 2 ];
		double t29 = m[ 1 ][ 0 ] * m[ 0 ][ 1 ];
		double t32 = m[ 1 ][ 0 ] * m[ 2 ][ 1 ];
		double t33 = m[ 0 ][ 2 ] * m[ 3 ][ 3 ];
		double t35 = m[ 0 ][ 3 ] * m[ 3 ][ 2 ];
		double t37 = m[ 1 ][ 0 ] * m[ 3 ][ 1 ];
		double t38 = m[ 0 ][ 2 ] * m[ 2 ][ 3 ];
		double t40 = m[ 0 ][ 3 ] * m[ 2 ][ 2 ];
		double t42 = t14 * t15 - t14 * t17 - t19 * t20 + t19 * t22 + t24 * t25 - t24 * t27 - t29 * t15 + t29 * t17 + t32 * t33 - t32 * t35 - t37 * t38 + t37 * t40;
		double t43 = m[ 2 ][ 0 ] * m[ 0 ][ 1 ];
		double t46 = m[ 2 ][ 0 ] * m[ 1 ][ 1 ];
		double t49 = m[ 2 ][ 0 ] * m[ 3 ][ 1 ];
		double t50 = m[ 0 ][ 2 ] * m[ 1 ][ 3 ];
		double t52 = m[ 0 ][ 3 ] * m[ 1 ][ 2 ];
		double t54 = m[ 3 ][ 0 ] * m[ 0 ][ 1 ];
		double t57 = m[ 3 ][ 0 ] * m[ 1 ][ 1 ];
		double t60 = m[ 3 ][ 0 ] * m[ 2 ][ 1 ];
		double t63 = t43 * t20 - t43 * t22 - t46 * t33 + t46 * t35 + t49 * t50 - t49 * t52 - t54 * t25 + t54 * t27 + t57 * t38 - t57 * t40 - t60 * t50 + t60 * t52;
		double t65 = 1 / (t42 + t63);
		double t71 = m[ 0 ][ 2 ] * m[ 2 ][ 1 ];
		double t73 = m[ 0 ][ 3 ] * m[ 2 ][ 1 ];
		double t75 = m[ 0 ][ 2 ] * m[ 3 ][ 1 ];
		double t77 = m[ 0 ][ 3 ] * m[ 3 ][ 1 ];
		double t81 = m[ 0 ][ 1 ] * m[ 1 ][ 2 ];
		double t83 = m[ 0 ][ 1 ] * m[ 1 ][ 3 ];
		double t85 = m[ 0 ][ 2 ] * m[ 1 ][ 1 ];
		double t87 = m[ 0 ][ 3 ] * m[ 1 ][ 1 ];
		double t101 = m[ 1 ][ 0 ] * m[ 2 ][ 2 ];
		double t103 = m[ 1 ][ 0 ] * m[ 2 ][ 3 ];
		double t105 = m[ 2 ][ 0 ] * m[ 1 ][ 2 ];
		double t107 = m[ 2 ][ 0 ] * m[ 1 ][ 3 ];
		double t109 = m[ 3 ][ 0 ] * m[ 1 ][ 2 ];
		double t111 = m[ 3 ][ 0 ] * m[ 1 ][ 3 ];
		double t115 = m[ 0 ][ 0 ] * m[ 2 ][ 2 ];
		double t117 = m[ 0 ][ 0 ] * m[ 2 ][ 3 ];
		double t119 = m[ 2 ][ 0 ] * m[ 0 ][ 2 ];
		double t121 = m[ 2 ][ 0 ] * m[ 0 ][ 3 ];
		double t123 = m[ 3 ][ 0 ] * m[ 0 ][ 2 ];
		double t125 = m[ 3 ][ 0 ] * m[ 0 ][ 3 ];
		double t129 = m[ 0 ][ 0 ] * m[ 1 ][ 2 ];
		double t131 = m[ 0 ][ 0 ] * m[ 1 ][ 3 ];
		double t133 = m[ 1 ][ 0 ] * m[ 0 ][ 2 ];
		double t135 = m[ 1 ][ 0 ] * m[ 0 ][ 3 ];

		b[ 0 ][ 0 ] = (m[ 1 ][ 1 ] * m[ 2 ][ 2 ] * m[ 3 ][ 3 ] - m[ 1 ][ 1 ] * m[ 2 ][ 3 ] * m[ 3 ][ 2 ] - m[ 2 ][ 1 ] * m[ 1 ][ 2 ] * m[ 3 ][ 3 ] + m[ 2 ][ 1 ] * m[ 1 ][ 3 ] * m[ 3 ][ 2 ] + m[ 3 ][ 1 ] * m[ 1 ][ 2 ] * m[ 2 ][ 3 ] - m[ 3 ][ 1 ] * m[ 1 ][ 3 ] * m[ 2 ][ 2 ]) * t65;
		b[ 0 ][ 1 ] = -(m[ 0 ][ 1 ] * m[ 2 ][ 2 ] * m[ 3 ][ 3 ] - m[ 0 ][ 1 ] * m[ 2 ][ 3 ] * m[ 3 ][ 2 ] - t71 * m[ 3 ][ 3 ] + t73 * m[ 3 ][ 2 ] + t75 * m[ 2 ][ 3 ] - t77 * m[ 2 ][ 2 ]) * t65;
		b[ 0 ][ 2 ] = (t81 * m[ 3 ][ 3 ] - t83 * m[ 3 ][ 2 ] - t85 * m[ 3 ][ 3 ] + t87 * m[ 3 ][ 2 ] + t75 * m[ 1 ][ 3 ] - t77 * m[ 1 ][ 2 ]) * t65;
		b[ 0 ][ 3 ] = -(t81 * m[ 2 ][ 3 ] - t83 * m[ 2 ][ 2 ] - t85 * m[ 2 ][ 3 ] + t87 * m[ 2 ][ 2 ] + t71 * m[ 1 ][ 3 ] - t73 * m[ 1 ][ 2 ]) * t65;
		b[ 1 ][ 0 ] = -(t101 * m[ 3 ][ 3 ] - t103 * m[ 3 ][ 2 ] - t105 * m[ 3 ][ 3 ] + t107 * m[ 3 ][ 2 ] + t109 * m[ 2 ][ 3 ] - t111 * m[ 2 ][ 2 ]) * t65;
		b[ 1 ][ 1 ] = (t115 * m[ 3 ][ 3 ] - t117 * m[ 3 ][ 2 ] - t119 * m[ 3 ][ 3 ] + t121 * m[ 3 ][ 2 ] + t123 * m[ 2 ][ 3 ] - t125 * m[ 2 ][ 2 ]) * t65;
		b[ 1 ][ 2 ] = -(t129 * m[ 3 ][ 3 ] - t131 * m[ 3 ][ 2 ] - t133 * m[ 3 ][ 3 ] + t135 * m[ 3 ][ 2 ] + t123 * m[ 1 ][ 3 ] - t125 * m[ 1 ][ 2 ]) * t65;
		b[ 1 ][ 3 ] = (t129 * m[ 2 ][ 3 ] - t131 * m[ 2 ][ 2 ] - t133 * m[ 2 ][ 3 ] + t135 * m[ 2 ][ 2 ] + t119 * m[ 1 ][ 3 ] - t121 * m[ 1 ][ 2 ]) * t65;
		b[ 2 ][ 0 ] = (t32 * m[ 3 ][ 3 ] - t103 * m[ 3 ][ 1 ] - t46 * m[ 3 ][ 3 ] + t107 * m[ 3 ][ 1 ] + t57 * m[ 2 ][ 3 ] - t111 * m[ 2 ][ 1 ]) * t65;
		b[ 2 ][ 1 ] = -(t19 * m[ 3 ][ 3 ] - t117 * m[ 3 ][ 1 ] - t43 * m[ 3 ][ 3 ] + t121 * m[ 3 ][ 1 ] + t54 * m[ 2 ][ 3 ] - t125 * m[ 2 ][ 1 ]) * t65;
		b[ 2 ][ 2 ] = (t14 * m[ 3 ][ 3 ] - t131 * m[ 3 ][ 1 ] - t29 * m[ 3 ][ 3 ] + t135 * m[ 3 ][ 1 ] + t54 * m[ 1 ][ 3 ] - t125 * m[ 1 ][ 1 ]) * t65;
		b[ 2 ][ 3 ] = -(t14 * m[ 2 ][ 3 ] - t131 * m[ 2 ][ 1 ] - t29 * m[ 2 ][ 3 ] + t135 * m[ 2 ][ 1 ] + t43 * m[ 1 ][ 3 ] - t121 * m[ 1 ][ 1 ]) * t65;
		b[ 3 ][ 0 ] = -(t32 * m[ 3 ][ 2 ] - t101 * m[ 3 ][ 1 ] - t46 * m[ 3 ][ 2 ] + t105 * m[ 3 ][ 1 ] + t57 * m[ 2 ][ 2 ] - t109 * m[ 2 ][ 1 ]) * t65;
		b[ 3 ][ 1 ] = (t19 * m[ 3 ][ 2 ] - t115 * m[ 3 ][ 1 ] - t43 * m[ 3 ][ 2 ] + t119 * m[ 3 ][ 1 ] + t54 * m[ 2 ][ 2 ] - t123 * m[ 2 ][ 1 ]) * t65;
		b[ 3 ][ 2 ] = -(t14 * m[ 3 ][ 2 ] - t129 * m[ 3 ][ 1 ] - t29 * m[ 3 ][ 2 ] + t133 * m[ 3 ][ 1 ] + t54 * m[ 1 ][ 2 ] - t123 * m[ 1 ][ 1 ]) * t65;
		b[ 3 ][ 3 ] = (t14 * m[ 2 ][ 2 ] - t129 * m[ 2 ][ 1 ] - t29 * m[ 2 ][ 2 ] + t133 * m[ 2 ][ 1 ] + t43 * m[ 1 ][ 2 ] - t119 * m[ 1 ][ 1 ]) * t65;

		return new Transform( b );
	}

	/**
	 * This method is used to load the transform using the OpenGL API.
	 *
	 * @return The transform as a DoubleBuffer
	 */
	public final DoubleBuffer getMatrix() {
		if( matrix == null ) {
			matrix = DoubleBuffer.wrap( new double[]{ m[ 0 ][ 0 ], m[ 1 ][ 0 ], m[ 2 ][ 0 ], m[ 3 ][ 0 ], m[ 0 ][ 1 ], m[ 1 ][ 1 ], m[ 2 ][ 1 ], m[ 3 ][ 1 ], m[ 0 ][ 2 ], m[ 1 ][ 2 ], m[ 2 ][ 2 ], m[ 3 ][ 2 ], m[ 0 ][ 3 ], m[ 1 ][ 3 ], m[ 2 ][ 3 ], m[ 3 ][ 3 ] } );
		}
		return matrix;
	}

	/**
	 * Create an identity transform.
	 */
	public static Transform identity() {
		return new Transform( 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0 );
	}

	/**
	 * Create a transform to scale a vector.
	 */
	public static Transform scale( double scaleX, double scaleY, double scaleZ ) {
		return new Transform( scaleX, 0.0, 0.0, 0.0, 0.0, scaleY, 0.0, 0.0, 0.0, 0.0, scaleZ, 0.0, 0.0, 0.0, 0.0, 1.0 );
	}

	/**
	 * Create transform to scale using a specified origin.
	 *
	 * @param origin The origin
	 * @param scaleX The x scale
	 * @param scaleY The y scale
	 * @param scaleZ The z scale
	 * @return The transformation with origin and scale
	 */
	public static Transform scale( double[] origin, double scaleX, double scaleY, double scaleZ ) {
		Transform transform = identity();
		transform = transform.combine( translation( origin ) );
		transform = transform.combine( scale( scaleX, scaleY, scaleZ ) );
		transform = transform.combine( translation( Vector.reverse( origin ) ) );
		return transform;
	}

	/**
	 * Create a translation transform.
	 *
	 * @param offset The vector of cartesian offsets
	 * @return The translation transform
	 */
	public static Transform translation( double[] offset ) {
		return translation( offset[ 0 ], offset[ 1 ], offset[ 2 ] );
	}

	/**
	 * Create a translation transform.
	 */
	public static Transform translation( double deltaX, double deltaY, double deltaZ ) {
		return new Transform( 1.0, 0.0, 0.0, deltaX, 0.0, 1.0, 0.0, deltaY, 0.0, 0.0, 1.0, deltaZ, 0.0, 0.0, 0.0, 1.0 );
	}

	/**
	 * Create a transform to rotate around an axis.
	 *
	 * @param axis The axis around which to rotate
	 * @param angle The rotation angle in radians
	 */
	public static Transform rotation( double[] axis, double angle ) {
		if( Vector.magnitude( axis ) == 0 || angle == 0.0 ) return Transform.identity();

		double cos = Math.cos( angle );
		double sin = Math.sin( angle );
		double theta = 1.0 - cos;

		// Normalize the axis
		axis = Vector.normalize( axis );

		return new Transform( theta * axis[ 0 ] * axis[ 0 ] + cos,
			theta * axis[ 0 ] * axis[ 1 ] - sin * axis[ 2 ],
			theta * axis[ 0 ] * axis[ 2 ] + sin * axis[ 1 ],
			0.0,
			theta * axis[ 0 ] * axis[ 1 ] + sin * axis[ 2 ],
			theta * axis[ 1 ] * axis[ 1 ] + cos,
			theta * axis[ 1 ] * axis[ 2 ] - sin * axis[ 0 ],
			0.0,
			theta * axis[ 0 ] * axis[ 2 ] - sin * axis[ 1 ],
			theta * axis[ 1 ] * axis[ 2 ] + sin * axis[ 0 ],
			theta * axis[ 2 ] * axis[ 2 ] + cos,
			0.0,
			0.0,
			0.0,
			0.0,
			1.0
		);
	}

	/**
	 * Create a transform to rotate around an axis located at an origin.
	 *
	 * @param origin The origin of the axis.
	 * @param axis The axis around which to rotate.
	 * @param angle The rotation angle in radians.
	 * @return A transform to rotate around the axis
	 */
	public static Transform rotation( double[] origin, double[] axis, double angle ) {
		Transform transform = identity();
		transform = transform.combine( translation( origin ) );
		transform = transform.combine( rotation( axis, angle ) );
		transform = transform.combine( translation( Vector.reverse( origin ) ) );
		return transform;
	}

	/**
	 * Create a transform to rotate a vector around the X axis.
	 *
	 * @param angle The rotation angle in radians.
	 */
	public static Transform xrotation( double angle ) {
		double cos = Math.cos( angle );
		double sin = Math.sin( angle );
		return new Transform( 1.0, 0.0, 0.0, 0.0, 0.0, cos, -sin, 0.0, 0.0, sin, cos, 0.0, 0.0, 0.0, 0.0, 1.0 );
	}

	/**
	 * Create a transform to rotate a vector around the Y axis.
	 *
	 * @param angle The rotation angle in radians.
	 */
	public static Transform yrotation( double angle ) {
		double cos = Math.cos( angle );
		double sin = Math.sin( angle );

		return new Transform( cos, 0.0, sin, 0.0, 0.0, 1.0, 0.0, 0.0, -sin, 0.0, cos, 0.0, 0.0, 0.0, 0.0, 1.0 );
	}

	/**
	 * Create a transform to rotate a vector around the Z axis.
	 *
	 * @param angle The rotation angle in radians.
	 */
	public static Transform zrotation( double angle ) {
		double cos = Math.cos( angle );
		double sin = Math.sin( angle );

		return new Transform( cos, -sin, 0.0, 0.0, sin, cos, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0 );
	}

	/**
	 * Create a mirror transform using three vectors to define a mirror plane. Two
	 * points are used to define a 2D mirror axis and the third point is a normal.
	 *
	 * @param a Axis point one.
	 * @param b Axis point two.
	 * @param n Axis normal.
	 * @return A mirror transform.
	 */
	public static Transform mirror( double[] a, double[] b, double[] n ) {
		if( a == null || b == null || n == null ) return null;

		Orientation orientation = new Orientation( a, n, Vector.subtract( a, b ) );
		Transform local = orientation.getTargetToLocalTransform();

		Transform transform = Transform.identity();
		transform = transform.combine( orientation.getLocalToTargetTransform() );
		transform = transform.combine( Transform.translation( local.apply( a ) ) );
		transform = transform.combine( Transform.scale( -1, 1, 1 ) );
		transform = transform.combine( Transform.translation( Vector.reverse( local.apply( a ) ) ) );
		transform = transform.combine( orientation.getTargetToLocalTransform() );

		return transform;
	}

	/**
	 * Create a transform to convert from the target orientation to the local
	 * orientation. The vectors specified define the local orientation. This
	 * transform transforms coordinates such that the origin vector origin is
	 * translated to the origin, the direction vector normal lies along the
	 * positive z axis, and the direction vector rotate lies the positive y half
	 * of the y-z plane.
	 */
	public static Transform localTransform( double[] origin, double[] normal, double[] rotate ) {
		double[] zrotate = Vector.scale( normal, 1.0 / Vector.magnitude( normal ) );
		double[] xrotate = Vector.normalize( Vector.cross( rotate, normal ) );
		double[] yrotate = Vector.cross( zrotate, xrotate );
		return new Transform( xrotate[ 0 ],
			xrotate[ 1 ],
			xrotate[ 2 ],
			-(xrotate[ 0 ] * origin[ 0 ] + xrotate[ 1 ] * origin[ 1 ] + xrotate[ 2 ] * origin[ 2 ]),
			yrotate[ 0 ],
			yrotate[ 1 ],
			yrotate[ 2 ],
			-(yrotate[ 0 ] * origin[ 0 ] + yrotate[ 1 ] * origin[ 1 ] + yrotate[ 2 ] * origin[ 2 ]),
			zrotate[ 0 ],
			zrotate[ 1 ],
			zrotate[ 2 ],
			-(zrotate[ 0 ] * origin[ 0 ] + zrotate[ 1 ] * origin[ 1 ] + zrotate[ 2 ] * origin[ 2 ]),
			0.0,
			0.0,
			0.0,
			1.0
		);
	}

	/**
	 * Create a transform to convert from the local orientation to the target
	 * orientation. The vectors specified define the local orientation. This
	 * transform transforms coordinates such that the z axis lies along the
	 * direction of normal, the y axis lies in the direction of rotate, and the
	 * origin is translated to the point origin.
	 */
	public static Transform targetTransform( double[] origin, double[] normal, double[] rotate ) {
		double[] zrotate = Vector.scale( normal, 1.0 / Vector.magnitude( normal ) );
		double[] xrotate = Vector.normalize( Vector.cross( rotate, normal ) );
		double[] yrotate = Vector.cross( zrotate, xrotate );
		return new Transform( xrotate[ 0 ],
			yrotate[ 0 ],
			zrotate[ 0 ],
			origin[ 0 ],
			xrotate[ 1 ],
			yrotate[ 1 ],
			zrotate[ 1 ],
			origin[ 1 ],
			xrotate[ 2 ],
			yrotate[ 2 ],
			zrotate[ 2 ],
			origin[ 2 ],
			0.0,
			0.0,
			0.0,
			1.0
		);
	}

	/**
	 * Create a transform to convert between camera orientation and orthographic
	 * projection using the specified clipping planes assuming that the camera is
	 * at (0,0,0).
	 *
	 * @param left The left clipping plane.
	 * @param right The right clipping plane.
	 * @param bottom The bottom clipping plane.
	 * @param top The top clipping plane.
	 * @param near The near clipping plane.
	 * @param far The far clipping plane.
	 * @return An orthographic transform.
	 */
	public static Transform ortho( double left, double right, double bottom, double top, double near, double far ) {
		double a = 2.0 / (right - left);
		double b = -(right + left) / (right - left);

		double c = 2.0 / (top - bottom);
		double d = -(top + bottom) / (top - bottom);

		double e = -2.0 / (far - near);
		double f = -(far + near) / (far - near);

		return new Transform( a, 0, 0, b, 0, c, 0, d, 0, 0, e, f, 0, 0, 0, 1 );
	}

	/**
	 * Create a transform to convert between camera orientation and perspective
	 * projection using the specified clipping planes assuming that the camera is
	 * at (0,0,0).
	 *
	 * @param left The left clipping plane.
	 * @param right The right clipping plane.
	 * @param bottom The bottom clipping plane.
	 * @param top The top clipping plane.
	 * @param near The near clipping plane.
	 * @param far The far clipping plane.
	 * @return A perspective transform.
	 */
	public static Transform frustrum( double left, double right, double bottom, double top, double near, double far ) {
		double a = (2 * near) / (right - left);
		double b = (right + left) / (right - left);

		double c = (2 * near) / (top - bottom);
		double d = (top + bottom) / (top - bottom);

		double e = -(far + near) / (far - near);
		double f = -(2 * far * near) / (far - near);

		return new Transform( a, 0, b, 0, 0, c, d, 0, 0, 0, e, f, 0, 0, -1, 0 );
	}

	/**
	 * Create a transform to implement a perspective projection. The center of
	 * projection is at (0, 0, -distance), and the projection plane is given by z
	 * = 1.
	 */
	public static Transform perspective( double distance ) {
		double a = 1.0 / ((distance + 1.0) * (distance + 1.0));
		return new Transform( 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, a, 1.0 - a, 0.0, 0.0, a, 1.0 - a );
	}

	public final double[][] getMatrixArray() {
		return m;
	}

	@Override
	public boolean equals( Object object ) {
		if( !(object instanceof Transform) ) return false;
		Transform transform = (Transform)object;
		return (m[ 0 ][ 0 ] == transform.m[ 0 ][ 0 ] && m[ 0 ][ 1 ] == transform.m[ 0 ][ 1 ] && m[ 0 ][ 2 ] == transform.m[ 0 ][ 2 ] && m[ 0 ][ 3 ] == transform.m[ 0 ][ 3 ] && m[ 1 ][ 0 ] == transform.m[ 1 ][ 0 ] && m[ 1 ][ 1 ] == transform.m[ 1 ][ 1 ] && m[ 1 ][ 2 ] == transform.m[ 1 ][ 2 ] && m[ 1 ][ 3 ] == transform.m[ 1 ][ 3 ] && m[ 2 ][ 0 ] == transform.m[ 2 ][ 0 ] && m[ 2 ][ 1 ] == transform.m[ 2 ][ 1 ] && m[ 2 ][ 2 ] == transform.m[ 2 ][ 2 ] && m[ 2 ][ 3 ] == transform.m[ 2 ][ 3 ] && m[ 3 ][ 0 ] == transform.m[ 3 ][ 0 ] && m[ 3 ][ 1 ] == transform.m[ 3 ][ 1 ] && m[ 3 ][ 2 ] == transform.m[ 3 ][ 2 ] && m[ 3 ][ 3 ] == transform.m[ 3 ][ 3 ]);
	}

	@Override
	public int hashCode() {
		double value = m[ 0 ][ 0 ] + m[ 0 ][ 1 ] + m[ 0 ][ 2 ] + m[ 0 ][ 3 ] + m[ 1 ][ 0 ] + m[ 1 ][ 1 ] + m[ 1 ][ 2 ] + m[ 1 ][ 3 ] + m[ 2 ][ 0 ] + m[ 2 ][ 1 ] + m[ 2 ][ 2 ] + m[ 2 ][ 3 ] + m[ 3 ][ 0 ] + m[ 3 ][ 1 ] + m[ 3 ][ 2 ] + m[ 3 ][ 3 ];
		return Float.floatToIntBits( (float)value );
	}

	@Override
	public String toString() {
		return toJson();
	}

	private String toJson() {
		StringBuilder builder = new StringBuilder();

		builder.append( "[\n" );
		for( int rowIndex = 0; rowIndex < 4; rowIndex++ ) {
			builder.append( "  " );
			for( int columnIndex = 0; columnIndex < 4; columnIndex++ ) {
				builder.append( m[ rowIndex ][ columnIndex ] );
				if( columnIndex < 3 ) builder.append( ", " );
			}
			if( rowIndex < 3 ) builder.append( "," );
			builder.append( "\n" );
		}
		builder.append( "]\n" );

		return builder.toString();
	}

	/**
	 * Calculate the matrix determinant.
	 *
	 * @return The matrix determinant.
	 */
	private double determinant() {
		double s1 = m[ 0 ][ 0 ] * m[ 1 ][ 1 ] * m[ 2 ][ 2 ] * m[ 3 ][ 3 ] - m[ 0 ][ 0 ] * m[ 1 ][ 1 ] * m[ 2 ][ 3 ] * m[ 3 ][ 2 ] - m[ 0 ][ 0 ] * m[ 2 ][ 1 ] * m[ 1 ][ 2 ] * m[ 3 ][ 3 ] + m[ 0 ][ 0 ] * m[ 2 ][ 1 ] * m[ 1 ][ 3 ] * m[ 3 ][ 2 ] + m[ 0 ][ 0 ] * m[ 3 ][ 1 ] * m[ 1 ][ 2 ] * m[ 2 ][ 3 ] - m[ 0 ][ 0 ] * m[ 3 ][ 1 ] * m[ 1 ][ 3 ] * m[ 2 ][ 2 ] - m[ 1 ][ 0 ] * m[ 0 ][ 1 ] * m[ 2 ][ 2 ] * m[ 3 ][ 3 ] + m[ 1 ][ 0 ] * m[ 0 ][ 1 ] * m[ 2 ][ 3 ] * m[ 3 ][ 2 ] + m[ 1 ][ 0 ] * m[ 2 ][ 1 ] * m[ 0 ][ 2 ] * m[ 3 ][ 3 ] - m[ 1 ][ 0 ] * m[ 2 ][ 1 ] * m[ 0 ][ 3 ] * m[ 3 ][ 2 ] - m[ 1 ][ 0 ] * m[ 3 ][ 1 ] * m[ 0 ][ 2 ] * m[ 2 ][ 3 ] + m[ 1 ][ 0 ] * m[ 3 ][ 1 ] * m[ 0 ][ 3 ] * m[ 2 ][ 2 ];
		return s1 + m[ 2 ][ 0 ] * m[ 0 ][ 1 ] * m[ 1 ][ 2 ] * m[ 3 ][ 3 ] - m[ 2 ][ 0 ] * m[ 0 ][ 1 ] * m[ 1 ][ 3 ] * m[ 3 ][ 2 ] - m[ 2 ][ 0 ] * m[ 1 ][ 1 ] * m[ 0 ][ 2 ] * m[ 3 ][ 3 ] + m[ 2 ][ 0 ] * m[ 1 ][ 1 ] * m[ 0 ][ 3 ] * m[ 3 ][ 2 ] + m[ 2 ][ 0 ] * m[ 3 ][ 1 ] * m[ 0 ][ 2 ] * m[ 1 ][ 3 ] - m[ 2 ][ 0 ] * m[ 3 ][ 1 ] * m[ 0 ][ 3 ] * m[ 1 ][ 2 ] - m[ 3 ][ 0 ] * m[ 0 ][ 1 ] * m[ 1 ][ 2 ] * m[ 2 ][ 3 ] + m[ 3 ][ 0 ] * m[ 0 ][ 1 ] * m[ 1 ][ 3 ] * m[ 2 ][ 2 ] + m[ 3 ][ 0 ] * m[ 1 ][ 1 ] * m[ 0 ][ 2 ] * m[ 2 ][ 3 ] - m[ 3 ][ 0 ] * m[ 1 ][ 1 ] * m[ 0 ][ 3 ] * m[ 2 ][ 2 ] - m[ 3 ][ 0 ] * m[ 2 ][ 1 ] * m[ 0 ][ 2 ] * m[ 1 ][ 3 ] + m[ 3 ][ 0 ] * m[ 2 ][ 1 ] * m[ 0 ][ 3 ] * m[ 1 ][ 2 ];
	}

}
