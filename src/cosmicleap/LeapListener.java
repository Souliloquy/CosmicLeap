/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cosmicleap;

import com.imi.utils.CosmicDebug;
import com.imi.vecmath.Vector3f;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Vector;

/**
 *
 * @author Lou Hayt
 */
public class LeapListener extends Listener {
    
    static Controller controller = null;
    static Listener listener = null;
    
    public static final Vector3f [] markerPosition = new Vector3f[5];
    
    public static  void startLeapMotion() {
        if (controller != null)
        {
            // TODO If not currently a listener then add
        }
        else
        {
            listener = new LeapListener();
            controller = new Controller();
            // Have the sample listener receive events from the controller
            controller.addListener(listener);
            
            for(int i = 0; i < 5; i++) {
                markerPosition[i] = new Vector3f();
            }
        }
    }
    
    public static void stopLeapMotion() {
        if (controller != null) {
            controller.removeListener(listener);
        }
    }
    
    @Override
    public void onFrame(Controller controller) {
        // Get the most recent frame and report some basic information
        Frame frame = controller.frame();
        System.out.println("Frame id: " + frame.id()
                         + ", timestamp: " + frame.timestamp()
                         + ", hands: " + frame.hands().count()
                         + ", fingers: " + frame.fingers().count()
                         + ", tools: " + frame.tools().count());

        if (!frame.hands().empty()) {
            // Get the first hand
            Hand hand = frame.hands().get(0);

            // Check if the hand has any fingers
            FingerList fingers = hand.fingers();
            if (!fingers.empty()) {
                        
                // Calculate the hand's average finger tip position
                Vector avgPos = Vector.zero();
                int index = 0;
                for (Finger finger : fingers) {
                    avgPos = avgPos.plus(finger.tipPosition());
                    
                    Vector pos = fingers.iterator().next().tipPosition();
                    markerPosition[index++].set(pos.getX(), pos.getY(), pos.getZ());
                }
                avgPos = avgPos.divide(fingers.count());
//                System.out.println("Hand has " + fingers.count()
//                                 + " fingers, average finger tip position: " + avgPos);
            }

            // Get the hand's sphere radius and palm position
//            System.out.println("Hand sphere radius: " + hand.sphereRadius()
//                             + " mm, palm position: " + hand.palmPosition());

            // Get the hand's normal vector and direction
            Vector normal = hand.palmNormal();
            Vector direction = hand.direction();

            // Calculate the hand's pitch, roll, and yaw angles
//            System.out.println("Hand pitch: " + Math.toDegrees(direction.pitch()) + " degrees, "
//                             + "roll: " + Math.toDegrees(normal.roll()) + " degrees, "
//                             + "yaw: " + Math.toDegrees(direction.yaw()) + " degrees\n");
        }
    }
    
    @Override
    public void onInit(Controller controller) {
        System.out.println("Leap Motion - Initialized");
    }

    @Override
    public void onConnect(Controller controller) {
        System.out.println("Leap Motion - Connected");
    }

    @Override
    public void onDisconnect(Controller controller) {
        System.out.println("Leap Motion - Disconnected");
    }

    @Override
    public void onExit(Controller controller) {
        System.out.println("Leap Motion - Exited");
    }
}
