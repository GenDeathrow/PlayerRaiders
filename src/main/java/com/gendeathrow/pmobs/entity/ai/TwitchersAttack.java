package com.gendeathrow.pmobs.entity.ai;

import com.gendeathrow.pmobs.entity.New.EntityRaiderBase;

import net.minecraft.entity.ai.EntityAIAttackMelee;

public class TwitchersAttack extends EntityAIAttackMelee{

    private final EntityRaiderBase raider;
    private int raiseArmTicks;

    public TwitchersAttack(EntityRaiderBase entityRaiderBase, double speedIn, boolean longMemoryIn)
    {
        super(entityRaiderBase, speedIn, longMemoryIn);
        this.raider = entityRaiderBase;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        super.startExecuting();
        this.raiseArmTicks = 0;
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        super.resetTask();
        this.raider.setArmsRaised(false);
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        super.updateTask();
        ++this.raiseArmTicks;

        if (this.raiseArmTicks >= 5 && this.attackTick < 10)
        {
            this.raider.setArmsRaised(true);
        }
        else
        {
            this.raider.setArmsRaised(false);
        }
    }
}
