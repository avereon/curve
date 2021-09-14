package com.avereon.curve.math;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class Intersection3DTest {

	@Test
	void testIntersectLinePlane() {
		// Null checks.
		assertThat( Intersection3D.intersectLinePlane( new double[]{ 0, 0, 0 }, new double[]{ 0, 0, 0 }, new double[]{ 0, 0, 0 }, new double[]{ 0, 0, 1 } ), is( new Intersection3D( Intersection.Type.NONE ) ) );
		assertThat( Intersection3D.intersectLinePlane( new double[]{ -1, 0, 0 }, new double[]{ 1, 0, 0 }, new double[]{ 0, 0, 0 }, new double[]{ 0, 0, 1 } ), is( new Intersection3D( Intersection.Type.NONE ) ) );
		assertThat( Intersection3D.intersectLinePlane( new double[]{ -1, 2, 0 }, new double[]{ 1, 2, 0 }, new double[]{ 0, 0, 0 }, new double[]{ 0, 0, 1 } ), is( new Intersection3D( Intersection.Type.NONE ) ) );
		assertThat( Intersection3D.intersectLinePlane( new double[]{ -1, 2, 3 }, new double[]{ 1, 2, 3 }, new double[]{ 0, 0, 0 }, new double[]{ 0, 0, 1 } ), is( new Intersection3D( Intersection.Type.NONE ) ) );

		// Trivial plane with non-trivial line.
		assertThat( Intersection3D.intersectLinePlane( new double[]{ 0, 0, -1 }, new double[]{ 0, 0, 1 }, new double[]{ 0, 0, 0 }, new double[]{ 0, 0, 1 } ), is( new Intersection3D( Intersection.Type.INTERSECTION, new double[]{ 0, 0, 0 } ) ) );
		assertThat( Intersection3D.intersectLinePlane( new double[]{ -1, -1, -1 }, new double[]{ 1, 1, 1 }, new double[]{ 0, 0, 0 }, new double[]{ 0, 0, 1 } ), is( new Intersection3D( Intersection.Type.INTERSECTION, new double[]{ 0, 0, 0 } ) ) );
		assertThat( Intersection3D.intersectLinePlane( new double[]{ 0, 0, 2 }, new double[]{ 0, 0, 1 }, new double[]{ 0, 0, 0 }, new double[]{ 0, 0, 1 } ), is( new Intersection3D( Intersection.Type.INTERSECTION, new double[]{ 0, 0, 0 } ) ) );

		// Non-trivial plane with trivial line.
		assertThat( Intersection3D.intersectLinePlane( new double[]{ 1, 2, -1 }, new double[]{ 1, 2, 1 }, new double[]{ 1, 2, 3 }, new double[]{ 1, 1, 1 } ), is( new Intersection3D( Intersection.Type.INTERSECTION, new double[]{ 1, 2, 3 } ) ) );
	}

	@Test
	void testIntersectPlanePlane() {
		// TODO Test Intersection3D.intersectPlanePlane
	}
}
