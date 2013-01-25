/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cosmicleap.actors;

import com.imi.Cosmic;
import com.imi.cosmic.Entity;
import com.imi.cosmic.ProcessorComponent;
import com.imi.cosmic.ProcessorComponentUpdate;
import com.imi.utils.CosmicDebug;
import com.imi.utils.DeltaUpdatable;
import com.imi.vecmath.Vector3f;
import cosmicleap.LeapListener;

/**
 *
 * @author Lou Hayt
 */
public class LeapHand implements DeltaUpdatable {
    
    Entity entity = new Entity("LeapHand");
    
    Vector3f [] fingerPos = new Vector3f[5];
    
    Vector3f camPos = new Vector3f();
    Vector3f camFwd = new Vector3f();
    Vector3f marker = new Vector3f();

    public LeapHand() {
        for(int i = 0; i < fingerPos.length; i++)
        {
            fingerPos[i] = new Vector3f();
            CosmicDebug.createDebugTrackingSphere(fingerPos[i], null, 0.1f);
        }
        
        ProcessorComponent pc = new ProcessorComponentUpdate(this, true);
        entity.addComponent(ProcessorComponent.class, pc);
    }
    
    @Override
    public void update(float deltaTime) {
        
        // Update position in front of camera
        Cosmic.getMainCamera().getCameraPosition(camPos);
        Cosmic.getMainCamera().getCameraForward(camFwd);
        camFwd.scale(-2.0f);
        camFwd.y -= 1.0f;
        camPos.add(camFwd);               
        
        // Move position with sensor offset
        if (LeapListener.markerPosition[0] == null)
            return;
        for(int i = 0; i < 5; i++)
        {
            marker.set(LeapListener.markerPosition[i]);
            Cosmic.getMainCamera().transformPoint(marker);
            marker.scale(0.0025f);
    //        System.out.println("Offset: " + marker);
            fingerPos[i].set(camPos);
            fingerPos[i].add(marker);
        }
    }

    public Entity getEntity() {
        return entity;
    }
    
}
