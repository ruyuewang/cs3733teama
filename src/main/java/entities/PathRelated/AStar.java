package entities.PathRelated;
import entities.MapEdge;
import entities.MapNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import static java.lang.Math.abs;

public class AStar implements  PathGenerator{
    HashMap<MapNode,KnownPoint> checkedPoints;
    PriorityQueue<KnownPoint> queue;
    MapNode start, end;


    @Override
    public boolean verifyLocations() {
        return false;
    }

    @Override
    public Path generatePath(MapNode start, MapNode end) {
        this.start=start;
        this.end=end;
        KnownPoint checking ; // create a temp variable to keep track of which node are we on.

        //Generate Path
        for(checking = new KnownPoint(start,null,0,calDistance(start,end));
            checking.getNode()==end;   // reached end
            checking=queue.poll()) // move forward one step
        {
            putNodesIntoQueue(checking); // put adjacent node into queue.
            checkedPoints.put(checking.getNode(),checking);
            if(queue.peek()==null) { ;} // @TODO need to return an error, since queue is empty but still not got the path
        }
        // Done generating, output the path
        // make it into the format of outputting.
        return formatOutput(collectPath(checking));
    }


    ////////////////////// helper ///////////////////////


    /**
     * use the abs value of coordinates difference to calculate difference.
     * @param n1 start node
     * @param n2 end node
     * @return sum of x coord diff and y coord diff.
     */
    private int calDistance(MapNode n1, MapNode n2)
    {
        int x = abs(n1.getCoordinate().getxCoord() - n2.getCoordinate().getxCoord());
        int y = abs(n1.getCoordinate().getyCoord() - n2.getCoordinate().getxCoord());
        return x+y; //@TODO need to improve this method of estimation.
    }

    /**
     * @param e the edge being checked
     * @param n the node that's known
     * @return the node on the other end of this edge
     */
    private MapNode adjacentNode(MapEdge e , MapNode n )
    {
        if(e.getStart()==n) return e.getEnd();
        else return e.getStart();
    }


    /**
     * Put all the nodes that are linked to checking into the queue.
     * @param checking
     */
    private void putNodesIntoQueue (KnownPoint checking )
    {
        for(MapEdge e :checking.getEdge()) // putting the adjacentNodes into queue
        {
            MapNode nextNode= adjacentNode(e,checking.getNode());  // get the node to be calculated.

            if( !checkedPoints.containsKey(nextNode)) {  // prevent from going to points already been at.
                int newPastCost = checking.getPastCost() + (int) e.getWeight();

                KnownPoint nextPoint = new KnownPoint(nextNode,
                        checking,
                        newPastCost,
                        newPastCost + calDistance(nextNode, end)); // Generate a new Point from checking point to add into queue.
                queue.add(nextPoint); // add into queue
            }
        }
    }

    /**
     *
     * @param a MapNode a
     * @param b MapNode b
     * @return return the edge connecting MapNode a and MapNode b. If not found, return null.
     */
    private MapEdge getEdgeBetweenNodes(MapNode a, MapNode b)
    {
        for (MapEdge mapEdge : a.getEdges()) {
            if ( mapEdge.getStart()==b | mapEdge.getEnd()==b)
                return mapEdge;
        }
        return null;
    }

    /**
     * @param lastPoint the end point the Path
     * @return  Generate the Path from end point and put them in a list.
     */
    private ArrayList<KnownPoint> collectPath(KnownPoint lastPoint)
    {
        ArrayList<KnownPoint> finalPath = new ArrayList<>();
        for (;lastPoint.getLastNode()==null;)
        {
            finalPath.add(lastPoint);
            lastPoint=lastPoint.getLastNode();
        }
        return finalPath;
    }

    /**
     * @param finalPath the reversed path generated from ending location.
     * @return the formatted Path object.
     */
    private Path formatOutput (ArrayList<KnownPoint> finalPath)
    {
        Path output = new Path();
        MapNode currentNode = finalPath.get(finalPath.size()-1).getNode(); // extract the first Node of the list.
        MapNode nextNode;
        for(int i =finalPath.size()-2;i>-1;--i)
        {
            nextNode=finalPath.get(i).getNode(); // extract the second node
            output.addNode(nextNode);             //store the next node
            output.addEdge(getEdgeBetweenNodes(nextNode,currentNode)); // store the edge between them.
            currentNode=nextNode;   // move forward one step.
        }
        return output;
    }
}