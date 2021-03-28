/**
 * A three-dimensional point class.
 */
public class Point3d {
    /** X coordinate of the point */
    private double xCoord;

    /** Y coordinate of the point */
    private double yCoord;

    /** Z coordinate of the point */
    private double zCoord;

    /** Constructor to initialize point to (x, y, z) value. */
    public Point3d(double x, double y, double z) {
        xCoord = x;
        yCoord = y;
        zCoord = z;
    }

    /** No-argument constructor:  defaults to a point at the origin. */
    public Point3d() {
        // Call three-argument constructor and specify the origin.
        this(0, 0, 0);
    }

    /** Compare two points for equality. */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Point3d) {
            Point3d pt = (Point3d)o;

            //Double 은 primitive type 이므로 ==으로 비교 가능
            return getX() == pt.getX() && getY() == pt.getY() && getZ() == pt.getZ();
        }
        return false;
    }

    /** Compute the straight-line distance between two points. */
    public double distanceTo(Point3d pt) {

        double distance = 0.0;
        double diffX = pt.getX() - getX();
        double diffY = pt.getY() - getY();
        double diffZ = pt.getZ() - getZ();

        distance = Math.pow(diffX, 2) + Math.pow(diffY, 2) + Math.pow(diffZ, 2);
        distance = Math.sqrt(distance);

        return distance;
    }

    /** Return the X coordinate of the point. */
    public double getX() {
        return xCoord;
    }

    /** Return the Y coordinate of the point. */
    public double getY() {
        return yCoord;
    }

    /** Return the Z coordinate of the point. */
    public double getZ() {
        return zCoord;
    }

    /** Set the X coordinate of the point. */
    public void setX(double val) {
        xCoord = val;
    }

    /** Set the Y coordinate of the point. */
    public void setY(double val) {
        yCoord = val;
    }

    /** Set the Z coordinate of the point. */
    public void setZ(double val) {
        zCoord = val;
    }
}
