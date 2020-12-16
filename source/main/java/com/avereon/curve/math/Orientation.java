package com.avereon.curve.math;

import java.util.Arrays;

public class Orientation {

	private double[] origin;

	private double[] normal;

	private double[] rotate;

	private Transform toLocal;

	private Transform toTarget;

	private double xrotation;

	private double yrotation;

	private double zrotation;

	public Orientation() {
		this( Vector.of(), Vector.UNIT_Z, Vector.UNIT_Y );
	}

	public Orientation( double[] origin ) {
		this( origin, Vector.UNIT_Z, Vector.UNIT_Y );
	}

	public Orientation( double[] origin, double[] normal ) {
		double[] axis = Vector.cross( Vector.UNIT_Z, normal );
		double angle = Geometry.getAngle( Vector.UNIT_Z, normal );
		double[] rotate = Transform.rotation( axis, angle ).times( Vector.UNIT_Y );
		set( origin, normal, rotate );
	}

	public Orientation( double[] origin, double[] normal, double[] rotate ) {
		set( origin, normal, rotate );
	}

	public Orientation( double[] origin, double xrotation, double yrotation, double zrotation ) {
		this( origin );
		setRotationAngles( xrotation, yrotation, zrotation );
	}

	public final double[] getOrigin() {
		return origin;
	}

	public final void setOrigin( double[] origin ) {
		set( origin, getNormal(), getRotate() );
	}

	public final double[] getNormal() {
		return normal;
	}

	public final void setNormal( double[] normal ) {
		set( getOrigin(), normal, getRotate() );
	}

	/**
	 * This is a separate way to set the Z direction that also includes correcting
	 * the up direction to maintain the same relationship between the Z direction
	 * and the up direction.
	 *
	 * @param normal The orientation normal
	 */
	public final void setNormalCorrectRotate( double[] normal ) {
		if( Arrays.equals( getNormal(), normal ) ) return;
		if( Vector.magnitude( normal ) <= 0.0 ) throw new IllegalArgumentException( "Normal magnitude cannot be zero." );

		double[] oldNormal = getNormal();
		double[] axis = Vector.cross( oldNormal, normal );
		double angle = Geometry.getAngle( oldNormal, normal );

		Transform transform = Transform.rotation( axis, angle );
		double[] rotate = transform.timesDirection( getRotate() );

		set( getOrigin(), normal, rotate );
	}

	public final double[] getRotate() {
		return rotate;
	}

	public final void setRotate( double[] rotate ) {
		set( getOrigin(), getNormal(), rotate );
	}

	public final void set( Orientation orientation ) {
		set( orientation.getOrigin(), orientation.getNormal(), orientation.getRotate() );
	}

	/**
	 * Get the pose of this orientation. The pose is an orientation with the same
	 * normal and rotate vectors but with the origin set to zero.
	 *
	 * @return The orientation pose
	 */
	public final Orientation getPose() {
		return new Orientation( Vector.of(), getNormal(), getRotate() );
	}

	/**
	 * Set the pose (normal and rotate) without changing the origin.
	 *
	 * @param normal The normal of the orientation.
	 * @param rotate The rotate of the orientation.
	 */
	public final void setPose( double[] normal, double[] rotate ) {
		set( getOrigin(), normal, rotate );
	}

	/**
	 * Set all three orientation vectors.
	 *
	 * @param origin The orientation origin
	 * @param normal The orientation normal
	 * @param rotate The orientation rotate
	 */
	public final void set( double[] origin, double[] normal, double[] rotate ) {
		this.origin = origin;
		this.normal = normal;
		this.rotate = rotate;

		calculateRotationAngles( normal, rotate );
		toLocal = toTarget = null;
	}

	/**
	 * Set the rotation angles.
	 *
	 * @param xrotation The rotation angle around the x axis in radians.
	 * @param yrotation The rotation angle around the y axis in radians.
	 * @param zrotation The rotation angle around the z axis in radians.
	 */
	public final void setRotationAngles( double xrotation, double yrotation, double zrotation ) {
		this.xrotation = xrotation;
		this.yrotation = yrotation;
		this.zrotation = zrotation;

		Transform transform = Transform.yrotation( -yrotation ).combine( Transform.xrotation( -xrotation ) ).combine( Transform.zrotation( -zrotation ) );
		set( getOrigin(), transform.times( Vector.UNIT_Z ), transform.times( Vector.UNIT_Y ) );
		toLocal = toTarget = null;
	}

	/**
	 * Transform this orientation by applying a transform to its position and
	 * axes.
	 *
	 * @param transform The orientation transform
	 */
	public final void transform( Transform transform ) {
		set( transform.times( getOrigin() ), transform.timesDirection( getNormal() ), transform.timesDirection( getRotate() ) );
	}

	/**
	 * Transform this orientation by applying a transform to its position.
	 *
	 * @param transform The orientation transform
	 */
	public final void transformOrigin( Transform transform ) {
		set( transform.times( getOrigin() ), getNormal(), getRotate() );
	}

	/**
	 * Transform this orientation by applying a transform to its axes.
	 *
	 * @param transform The orientation transform
	 */
	public final void transformAxes( Transform transform ) {
		set( getOrigin(), transform.timesDirection( getNormal() ), transform.timesDirection( getRotate() ) );
	}

	/**
	 * Return a transform which will transform coordinates from the local
	 * orientation to the target orientation.
	 *
	 * @return A transform to convert to the target orientation.
	 */
	public final Transform getLocalToTargetTransform() {
		if( toTarget == null ) toTarget = Transform.targetTransform( getOrigin(), getNormal(), getRotate() );
		return toTarget;
	}

	/**
	 * Return a transform which will transform coordinates from the target
	 * orientation to the local orientation.
	 *
	 * @return A transform to convert to the local orientation.
	 */
	public final Transform getTargetToLocalTransform() {
		if( toLocal == null ) toLocal = Transform.localTransform( getOrigin(), getNormal(), getRotate() );
		return toLocal;
	}

	@Override
	public final Orientation clone() {
		return new Orientation( Vector.of( getOrigin() ), Vector.of( getNormal() ), Vector.of( getRotate() ) );
	}

	@Override
	public final String toString() {
		return toJson();
	}

	@Override
	public int hashCode() {
		double[] origin = getOrigin();
		double[] normal = getNormal();
		double[] rotate = getRotate();

		return Vector.hash( origin ) ^ Vector.hash( normal ) ^ Vector.hash( rotate );
	}

	@Override
	public boolean equals( Object object ) {
		if( !(object instanceof Orientation) ) return false;
		Orientation that = (Orientation)object;
		return Arrays.equals( this.origin, that.origin ) && Arrays.equals( this.normal, that.normal ) && Arrays.equals( this.rotate, that.rotate );
	}

	public String toJson() {
		StringBuilder builder = new StringBuilder();

		builder.append( "[\n" );
		builder.append( "  origin:").append( Vector.toString( getOrigin() ) ).append( ",\n" );
		builder.append( "  normal:").append( Vector.toString( getNormal() ) ).append( ",\n" );
		builder.append( "  rotate:").append( Vector.toString( getRotate() ) ).append( "\n" );
		builder.append( "]\n" );

		return builder.toString();
	}

	public static Orientation fromThreePoints( double[] origin, double[] xaxis, double[] point ) {
		double[] normal = Vector.cross( Vector.minus( origin, xaxis ), Vector.minus( origin, point ) );
		double[] rotate = Vector.cross( Vector.minus( origin, xaxis ), normal );
		return new Orientation( origin, normal, rotate );
	}

	public static boolean areGeometricallyEqual( Orientation a, Orientation b ) {
		boolean o = Geometry.areSamePoint( a.getOrigin(), b.getOrigin() );
		boolean n = Geometry.areSamePoint( a.getNormal(), b.getNormal() );
		boolean r = Geometry.areSamePoint( a.getRotate(), b.getRotate() );
		return o & n & r;
	}

	double getXRotation() {
		return xrotation;
	}

	double getYRotation() {
		return yrotation;
	}

	double getZRotation() {
		return zrotation;
	}

	private void calculateRotationAngles( double[] normal, double[] rotate ) {
		double length;
		double[] vector;
		Transform transform;

		if( normal[ 0 ] == 0.0 && normal[ 2 ] == 0.0 ) {
			length = Math.sqrt( rotate[ 0 ] * rotate[ 0 ] + rotate[ 2 ] * rotate[ 2 ] );
			yrotation = Math.acos( rotate[ 2 ] / length );
			if( Double.isNaN( yrotation ) ) yrotation = Math.acos( rotate[ 2 ] > 0.0 ? 1.0 : -1.0 );
			if( normal[ 1 ] > 0.0 ) yrotation *= -1.0;
		} else {
			length = Math.sqrt( normal[ 0 ] * normal[ 0 ] + normal[ 2 ] * normal[ 2 ] );
			yrotation = Math.acos( normal[ 2 ] / length );
			if( Double.isNaN( yrotation ) ) yrotation = Math.acos( normal[ 2 ] > 0.0 ? 1.0 : -1.0 );
			if( normal[ 0 ] > 0.0 ) yrotation *= -1.0;
		}
		transform = Transform.yrotation( yrotation );
		vector = transform.times( normal );
		length = Vector.magnitude( normal );
		xrotation = Math.acos( vector[ 2 ] / length );
		if( Double.isNaN( xrotation ) ) xrotation = Math.acos( vector[ 2 ] > 0.0 ? 1.0 : -1.0 );
		if( vector[ 1 ] < 0.0 ) xrotation *= -1.0;
		transform = Transform.xrotation( xrotation ).combine( transform );
		vector = transform.times( rotate );
		length = Math.sqrt( vector[ 0 ] * vector[ 0 ] + vector[ 1 ] * vector[ 1 ] );
		zrotation = Math.acos( vector[ 1 ] / length );
		if( Double.isNaN( zrotation ) ) zrotation = Math.acos( vector[ 1 ] > 0.0 ? 1.0 : -1.0 );
		if( vector[ 0 ] < 0.0 ) zrotation *= -1.0;
	}

}
