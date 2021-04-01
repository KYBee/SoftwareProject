/**
 * This class stores the basic state necessary for the A* algorithm to compute a
 * path across a map.  This state includes a collection of "open waypoints" and
 * another collection of "closed waypoints."  In addition, this class provides
 * the basic operations that the A* pathfinding algorithm needs to perform its
 * processing.
 **/
import java.util.*;

public class AStarState
{
    /** This is a reference to the map that the A* algorithm is navigating. **/
    private Map2D map;

    /** This is a map that stores Waypoints which should be traverse*/
    private Map<Location, Waypoint> openWaypoints;

    /** This is a map that stores Waypoints which already been measured*/
    private Map<Location, Waypoint> closedWaypoints;


    /**
     * Initialize a new state object for the A* pathfinding algorithm to use.
     **/
    public AStarState(Map2D map)
    {
        if (map == null)
            throw new NullPointerException("map cannot be null");

        this.map = map;
        // initialize openWaypoints, closedWaypoints by assigning new Hashmap
        this.openWaypoints = new HashMap<>();
        this.closedWaypoints = new HashMap<>();
    }

    /** Returns the map that the A* pathfinder is navigating. **/
    public Map2D getMap()
    {
        return map;
    }

    /**
     * This method scans through all open waypoints, and returns the waypoint
     * with the minimum total cost.  If there are no open waypoints, this method
     * returns <code>null</code>.
     **/

    /** 210401자 교수님의 줌 세션을 듣고 수업시간에 배운 Comparator를 사용해서 getMinOpenWaypoint를 해결해보기 위해서
     * 제가 구현한 방법과는 다른 방법으로 코드를 한 번 더 짜보았습니다. 기존의 코드는 아래에 주석처리 했습니다.
     *
    public Waypoint getMinOpenWaypoint()
    {
        // first check whether the openWaypoint is empty or not. if empty, return null.
        if (openWaypoints.isEmpty())
            return null;
        // if not empty, compare each Waypoint's total cost(float).
        else {
            Waypoint minWaypoint = null;
            for (Waypoint w : openWaypoints.values()) {
                if (minWaypoint == null || Float.compare(w.getTotalCost(), minWaypoint.getTotalCost()) < 0)
                    minWaypoint = w;
            }
            return minWaypoint;
        }
    }
     */

    /**아래는 새로 구현한 코드입니다.*/

    /** 전달받은 HashMap과 Comparator으로 최소값을 return 합니다. */
    static Waypoint min(Map<Location, Waypoint> coll, Comparator<Waypoint> comp) {
        List<Waypoint> sortedWaypoint = new LinkedList<>(coll.values());
        Collections.sort(sortedWaypoint, comp);
        return sortedWaypoint.get(0);
    }

    public Waypoint getMinOpenWaypoint() {
        if (openWaypoints.isEmpty()) return null;

        return min(openWaypoints, new Comparator<Waypoint>() {
            public int compare(Waypoint w1, Waypoint w2) {
                return Double.compare(w1.getTotalCost(), w2.getTotalCost());
            }
        });
    }

    /**
     * This method adds a waypoint to (or potentially updates a waypoint already
     * in) the "open waypoints" collection.  If there is not already an open
     * waypoint at the new waypoint's location then the new waypoint is simply
     * added to the collection.  However, if there is already a waypoint at the
     * new waypoint's location, the new waypoint replaces the old one ONLY
     * IF the new waypoint's "previous cost" value is less than the current
     * waypoint's "previous cost" value.
     **/

    public boolean addOpenWaypoint(Waypoint newWP)
    {
        Location target = newWP.getLocation();

        if (openWaypoints.containsKey(target)) {
            // add new key, value set <Location, Waypoint> only when new Waypoint has lower value of previous cost(float)
            if (Float.compare(newWP.getPreviousCost(), openWaypoints.get(target).getPreviousCost()) < 0) {
                // new Waypoints added
                openWaypoints.put(target, newWP);
                return true;
            }
        }
        else{
            // new Waypoints added
            openWaypoints.put(target, newWP);
            return true;
        }

        return false;
    }


    /** Returns the current number of open waypoints. **/
    public int numOpenWaypoints()
    {
        return openWaypoints.size();
    }


    /**
     * This method moves the waypoint at the specified location from the
     * open list to the closed list.
     **/
    public void closeWaypoint(Location loc)
    {
        closedWaypoints.put(loc, openWaypoints.remove(loc));
    }

    /**
     * Returns true if the collection of closed waypoints contains a waypoint
     * for the specified location.
     **/
    public boolean isLocationClosed(Location loc)
    {
        return closedWaypoints.containsKey(loc);
    }
}
