package lol.pyr.znpcsplus.npc;

import com.ticxo.modelengine.api.nms.entity.wrapper.LookController;

public class NpcLookController implements LookController {

    private final ModeledNpcImpl npc;

    public NpcLookController(ModeledNpcImpl npc) {
        this.npc = npc;
    }

    @Override
    public void lookAt(double x, double y, double z) {

    }

    @Override
    public void setPitch(float pitch) {

    }

    @Override
    public void setHeadYaw(float yaw) {

    }

    @Override
    public void setBodyYaw(float yaw) {

    }
}
