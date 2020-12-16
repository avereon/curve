package com.avereon.curve.math;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static com.avereon.curve.match.Matchers.near;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OrientationTest {

	private static final double PI_OVER_2 = Math.PI / 2;

	@Test
	void testConstructor() {
		Orientation orientation = new Orientation();
		assertOrientationValues( orientation, Vector.of(), Vector.of( 0, 0, 1 ), Vector.of( 0, 1, 0 ), 0, 0, 0 );
	}

	@Test
	void testConstructorWithOriginAndNormal() {
		Orientation orientation = new Orientation( Vector.of(), Vector.UNIT_Y );
		assertOrientationValues( orientation, Vector.of(), Vector.UNIT_Y, Vector.reverse( Vector.UNIT_Z ), 1e-15 );

		orientation = new Orientation( Vector.of(), Vector.UNIT_X );
		assertOrientationValues( orientation, Vector.of(), Vector.UNIT_X, Vector.UNIT_Y );
	}

	@Test
	void testConstructorWithOriginAndPose() {
		Orientation orientation = new Orientation( Vector.of( 1, 2, 3 ), Vector.of( 0, 1, 0 ), Vector.of( 0, 0, 1 ) );
		assertOrientationValues( orientation, Vector.of( 1, 2, 3 ), Vector.of( 0, 1, 0 ), Vector.of( 0, 0, 1 ), PI_OVER_2, -0.0, Math.PI );
	}

	@Test
	void testConstructorWithOriginAndRotationAngles() {
		Orientation orientation = new Orientation( Vector.of( 1, 2, 3 ), PI_OVER_2, 0.0, Math.PI );
		assertOrientationValues( orientation, Vector.of( 1, 2, 3 ), Vector.of( 0, 1, 0 ), Vector.of( 0, 0, 1 ), PI_OVER_2, 0.0, Math.PI, 1e-15 );
	}

	@Test
	void testSetWithOrientation() {
		Orientation orientation1 = new Orientation();
		Orientation orientation2 = new Orientation( Vector.of( 1, 2, 3 ), Vector.of( 4, 5, 6 ), Vector.of( 7, 8, 9 ) );
		assertOrientationValues( orientation1, Vector.of(), Vector.of( 0, 0, 1 ), Vector.of( 0, 1, 0 ) );

		orientation1.set( orientation2 );
		assertEquals( orientation2, orientation1, 1e-16 );
	}

	@Test
	void testSetWithVectors() {
		double[] origin = Vector.of( 1, 2, 3 );
		double[] normal = Vector.of( 4, 5, 6 );
		double[] rotate = Vector.of( 7, 8, 9 );
		Orientation orientation = new Orientation();
		assertOrientationValues( orientation, Vector.of(), Vector.of( 0, 0, 1 ), Vector.of( 0, 1, 0 ) );

		orientation.set( origin, normal, rotate );
		assertEquals( new Orientation( Vector.of( 1, 2, 3 ), Vector.of( 4, 5, 6 ), Vector.of( 7, 8, 9 ) ), orientation, 1e-16 );
	}

	@Test
	void testSetOrigin() {
		Orientation orientation = new Orientation();
		orientation.setOrigin( Vector.of( 1, 2, 3 ) );
		assertOrientationValues( orientation, Vector.of( 1, 2, 3 ), Vector.of( 0, 0, 1 ), Vector.of( 0, 1, 0 ), 0, 0, 0 );
	}

	@Test
	void testGetPose() {
		Orientation orientation = new Orientation( Vector.of( 1, 2, 3 ), Vector.of( 4, 5, 6 ), Vector.of( 7, 8, 9 ) );
		assertOrientationValues( orientation.getPose(), Vector.of(), Vector.of( 4, 5, 6 ), Vector.of( 7, 8, 9 ) );
	}

	@Test
	void testSetPose() {
		Orientation orientation = new Orientation();
		orientation.setPose( Vector.of( 0, 1, 0 ), Vector.of( 0, 0, 1 ) );
		assertOrientationValues( orientation, Vector.of(), Vector.of( 0, 1, 0 ), Vector.of( 0, 0, 1 ), PI_OVER_2, -0.0, Math.PI );
	}

	@Test
	void testSetRotationAngles() {
		Orientation orientation = new Orientation();
		orientation.setRotationAngles( PI_OVER_2, -0.0, Math.PI );
		assertOrientationValues( orientation, Vector.of(), Vector.of( 0, 1, 0 ), Vector.of( 0, 0, 1 ), PI_OVER_2, 0.0, Math.PI, 1e-15 );
	}

	@Test
	void testTransform() {
		Orientation orientation = new Orientation();
		orientation.transform( Transform.translation( 1, 2, 3 ).combine( Transform.rotation( Vector.of( 1, 0, 0 ), PI_OVER_2 ) ) );
		assertEquals( new Orientation( Vector.of( 1, 2, 3 ), Vector.of( 0, -1, 0 ), Vector.of( 0, 0, 1 ) ), orientation, 1e-16 );
	}

	@Test
	void testTransformOrigin() {
		Orientation orientation = new Orientation();
		orientation.transformOrigin( Transform.translation( 1, 2, 3 ) );
		assertEquals( new Orientation( Vector.of( 1, 2, 3 ) ), orientation );
	}

	@Test
	void testTransformAxes() {
		Orientation orientation = new Orientation();
		orientation.transformAxes( Transform.rotation( Vector.of( 1, 0, 0 ), PI_OVER_2 ) );
		assertEquals( new Orientation( Vector.of( 0, 0, 0 ), Vector.of( 0, -1, 0 ), Vector.of( 0, 0, 1 ) ), orientation, 1e-16 );
	}

	@Test
	void testTransformWithNegativeYAxis() {
		Orientation orientation = new Orientation( Vector.of( 0, 0, 0 ), Vector.of( 0, -1, 0 ), Vector.of( 0, 0, 1 ) );
		Transform transform = orientation.getLocalToTargetTransform();
		assertThat( transform.times( Vector.of( 0, 1, 0 ) ), is( Vector.of( 0, 0, 1 ) ) );
	}

	@Test
	void testGetLocalToWorldTransform() {
		Orientation orientation = new Orientation();
		assertThat( orientation.getLocalToTargetTransform(), is( Transform.identity() ) );

		orientation.transform( Transform.translation( 1, 2, 3 ) );
		assertThat( orientation.getLocalToTargetTransform(), is( Transform.translation( 1, 2, 3 ) ) );
	}

	@Test
	void testGetWorldToLocalTransform() {
		Orientation orientation = new Orientation();
		assertThat( orientation.getTargetToLocalTransform(), is( Transform.identity() ) );

		orientation.transform( Transform.translation( 1, 2, 3 ) );
		assertThat( orientation.getTargetToLocalTransform(), is( Transform.translation( -1, -2, -3 ) ) );
	}

	//	@Test void testIsModifiedByOrigin() throws Exception {
	//		Orientation orientation = new Orientation();
	//		assertFalse( orientation.isModified() );
	//
	//		orientation.setOrigin( Vector.of( 1, 1, 1 ) );
	//		assertTrue( orientation.isModified() );
	//
	//		orientation.setOrigin( Vector.of( 0, 0, 0 ) );
	//		assertFalse( orientation.isModified() );
	//
	//		orientation.setOrigin( Vector.of( 1, 1, 1 ) );
	//		assertTrue( orientation.isModified() );
	//
	//		orientation.setModified( false );
	//		assertFalse( orientation.isModified() );
	//	}
	//
	//	@Test void testIsModifiedByNormal() throws Exception {
	//		Orientation orientation = new Orientation();
	//		assertFalse( orientation.isModified() );
	//
	//		orientation.setNormal( Vector.of( 1, 1, 1 ) );
	//		assertTrue( orientation.isModified() );
	//
	//		orientation.setNormal( Vector.of( 0, 0, 1 ) );
	//		assertFalse( orientation.isModified() );
	//	}
	//
	//	@Test void testIsModifiedByRotate() throws Exception {
	//		Orientation orientation = new Orientation();
	//		assertFalse( orientation.isModified() );
	//
	//		orientation.setRotate( Vector.of( 1, 1, 1 ) );
	//		assertTrue( orientation.isModified() );
	//
	//		orientation.setRotate( Vector.of( 0, 1, 0 ) );
	//		assertFalse( orientation.isModified() );
	//	}
	//
	//	@Test void testIsModifiedByOrientation() throws Exception {
	//		Orientation orientation = new Orientation();
	//		assertFalse( orientation.isModified() );
	//
	//		orientation.setPose( Vector.of( 1, 1, 1 ), Vector.of( -1, 1, 1 ) );
	//		assertTrue( orientation.isModified() );
	//
	//		orientation.setPose( Vector.of( 0, 0, 1 ), Vector.of( 0, 1, 0 ) );
	//		assertFalse( orientation.isModified() );
	//	}
	//
	//	@Test void testIsModifiedByRotationAngles() throws Exception {
	//		Orientation orientation = new Orientation();
	//		assertFalse( orientation.isModified() );
	//
	//		orientation.setRotationAngles( 1, 2, 3 );
	//		assertTrue( orientation.isModified() );
	//
	//		orientation.setRotationAngles( 0, 0, 0 );
	//		assertFalse( orientation.isModified() );
	//	}
	//
	//	@Test void testModifiedByTransform() throws Exception {
	//		Orientation orientation = new Orientation();
	//		assertFalse( orientation.isModified() );
	//
	//		orientation.getLocalToTargetTransform();
	//		assertFalse( orientation.isModified() );
	//	}
	//
	//	@Test void testDataChangedEventHandling() throws Exception {
	//		Orientation orientation = new Orientation();
	//
	//		DataWatcher watcher = new DataWatcher();
	//		orientation.addDataListener( watcher );
	//		assertFalse( orientation.isModified() );
	//		watcher.assertEventCounts( 0, 0, 0, 0, 0 );
	//		watcher.reset();
	//
	//		orientation.set( new Orientation( Vector.of( 0, 1, 2 ), Vector.of( 3, 4, 5 ), Vector.of( 6, 7, 8 ) ) );
	//		watcher.assertEventCounts( 1, 1, 3, 0, 0 );
	//		watcher.reset();
	//
	//		orientation.set( new Orientation( Vector.of( 0, 1, 2 ), Vector.of( 3, 4, 5 ), Vector.of( 6, 7, 8 ) ) );
	//		watcher.assertEventCounts( 0, 0, 0, 0, 0 );
	//		watcher.reset();
	//
	//		orientation.set( new Orientation( Vector.of( 8, 7, 6 ), Vector.of( 5, 4, 3 ), Vector.of( 2, 1, 0 ) ) );
	//		watcher.assertEventCounts( 1, 0, 3, 0, 0 );
	//		watcher.reset();
	//	}

	@Test
	void testClone() {
		Orientation orientation = new Orientation( Vector.of( 1, 2, 3 ), Vector.of( 0, 1, 0 ), Vector.of( 0, 0, 1 ) );
		Orientation clone = orientation.clone();
		assertOrientationValues( clone, Vector.of( 1, 2, 3 ), Vector.of( 0, 1, 0 ), Vector.of( 0, 0, 1 ), PI_OVER_2, -0.0, Math.PI );
	}

	@Test
	void testEquals() {
		Orientation orientation1 = new Orientation();
		Orientation orientation2 = new Orientation();
		assertEquals( orientation2, orientation1 );
		assertEquals( orientation1, orientation2 );

		Orientation orientation3 = new Orientation( Vector.of( 0, 1, 2 ), Vector.of( 3, 4, 5 ), Vector.of( 6, 7, 8 ) );
		Orientation orientation4 = new Orientation( Vector.of( 0, 1, 2 ), Vector.of( 3, 4, 5 ), Vector.of( 6, 7, 8 ) );
		assertEquals( orientation4, orientation3 );
		assertEquals( orientation3, orientation4 );

		assertNotEquals( orientation1, orientation3 );
		assertNotEquals( orientation3, orientation1 );
	}

	//	@Test void testEqualsUsingAttributes() throws Exception {
	//		Orientation orientation1 = new Orientation();
	//		Orientation orientation2 = new Orientation();
	//		assertTrue( orientation1.equalsUsingAttributes( orientation2 ) );
	//		assertTrue( orientation2.equalsUsingAttributes( orientation1 ) );
	//
	//		Orientation orientation3 = new Orientation( Vector.of( 0, 1, 2 ), Vector.of( 3, 4, 5 ), Vector.of( 6, 7, 8 ) );
	//		Orientation orientation4 = new Orientation( Vector.of( 0, 1, 2 ), Vector.of( 3, 4, 5 ), Vector.of( 6, 7, 8 ) );
	//		assertTrue( orientation3.equalsUsingAttributes( orientation4 ) );
	//		assertTrue( orientation4.equalsUsingAttributes( orientation3 ) );
	//	}

	@Test
	void testHashCode() {
		Orientation orientation1 = new Orientation();
		Orientation orientation2 = new Orientation();
		assertThat( orientation1.hashCode(), is( orientation2.hashCode() ) );

		Orientation orientation3 = new Orientation( Vector.of( 0, 1, 2 ), Vector.of( 3, 4, 5 ), Vector.of( 6, 7, 8 ) );
		Orientation orientation4 = new Orientation( Vector.of( 0, 1, 2 ), Vector.of( 3, 4, 5 ), Vector.of( 6, 7, 8 ) );
		assertThat( orientation3.hashCode(), is( orientation4.hashCode() ) );
	}

	public static void assertEquals( Orientation expected, Orientation actual ) {
		assertEquals( expected, actual, 0.0 );
	}

	public static void assertEquals( Orientation expected, Orientation actual, double tolerance ) {
		try {
			assertThat( actual.getOrigin(), near( expected.getOrigin(), tolerance ) );
			assertThat( actual.getNormal(), near( expected.getNormal(), tolerance ) );
			assertThat( actual.getRotate(), near( expected.getRotate(), tolerance ) );
		} catch( AssertionError error ) {
			throw new AssertionError( "expected: " + expected + " was: " + actual );
		}
	}

//	public static void assertGeometricallyEquals( Orientation expected, Orientation actual ) {
//		Geometry.areSamePoint( expected.getOrigin(), actual.getOrigin() );
//		Geometry.areSamePoint( expected.getNormal(), actual.getNormal() );
//		Geometry.areSamePoint( expected.getRotate(), actual.getRotate() );
//	}

	public static void assertOrientationValues( Orientation orientation, double[] origin, double[] normal, double[] rotate ) {
		assertOrientationValues( orientation, origin, normal, rotate, 0.0 );
	}

	public static void assertOrientationValues( Orientation orientation, double[] origin, double[] normal, double[] rotate, double tolerance ) {
		assertNotNull( orientation );
		try {
			assertThat( orientation.getOrigin(), near( Vector.of( origin[ 0 ], origin[ 1 ], origin[ 2 ] ), tolerance ) );
			assertThat( orientation.getNormal(), near( Vector.of( normal[ 0 ], normal[ 1 ], normal[ 2 ] ), tolerance ) );
			assertThat( orientation.getRotate(), near( Vector.of( rotate[ 0 ], rotate[ 1 ], rotate[ 2 ] ), tolerance ) );
		} catch( AssertionFailedError error ) {
			throw new AssertionError( "expected: " + new Orientation( origin, normal, rotate ) + " was: " + orientation );
		}
	}

	public static void assertOrientationValues( Orientation orientation, double[] position, double[] z, double[] up, double xrotate, double yrotate, double zrotate ) {
		assertOrientationValues( orientation, position, z, up, xrotate, yrotate, zrotate, 0.0 );
	}

	public static void assertOrientationValues( Orientation orientation, double[] position, double[] z, double[] up, double xrotate, double yrotate, double zrotate, double tolerance ) {
		assertNotNull( orientation );
		try {
			assertThat( orientation.getOrigin(), near( position, tolerance ) );
			assertThat( orientation.getNormal(), near( z, tolerance ) );
			assertThat( orientation.getRotate(), near( up, tolerance ) );
			assertThat( orientation.getXRotation(), is( xrotate ) );
			assertThat( orientation.getYRotation(), is( yrotate ) );
			assertThat( orientation.getZRotation(), is( zrotate ) );
		} catch( AssertionFailedError error ) {
			throw new AssertionError( "expected: " + new Orientation( position, z, up ) + " was: " + orientation );
		}
	}

}
