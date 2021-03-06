package com.minecolonies.coremod.colony.buildings;

import com.minecolonies.api.util.InventoryUtils;
import com.minecolonies.coremod.colony.Colony;
import com.minecolonies.coremod.colony.ColonyView;
import com.minecolonies.coremod.colony.buildings.views.AbstractBuildingView;
import com.minecolonies.coremod.tileentities.TileEntityRack;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * Contains basic methods that all Huts will need.
 */
public abstract class AbstractBuildingHut extends AbstractBuilding
{

    /**
     * Simple constructor, just calls super.
     *
     * @param c The colony that this building belongs too.
     * @param l The location of this building.
     */
    public AbstractBuildingHut(@NotNull final Colony c, final BlockPos l)
    {
        super(c, l);
    }

    /**
     * Returns the max amount of inhabitants.
     *
     * @return Max inhabitants.
     */
    public int getMaxInhabitants()
    {
        return 1;
    }

    /**
     * Get count of item matching a predicate in the hut.
     * @param predicate the predicate.
     * @param stop the amount it should stop counting.
     * @param world the world the building is in.
     * @return the amount.
     */
    public int getCountOfPredicateInHut(final Predicate<ItemStack> predicate, final int stop, @NotNull final World world)
    {
        int amount = InventoryUtils.getItemCountInItemHandler(new InvWrapper(getTileEntity()), predicate);

        if(amount >= stop)
        {
            return amount;
        }

        for (final BlockPos pos : getAdditionalCountainers())
        {
            final TileEntity entity = world.getTileEntity(pos);

            if(entity instanceof TileEntityRack)
            {
                amount += ((TileEntityRack) entity).getItemCount(predicate);
            }
            else if(entity instanceof TileEntityChest)
            {
                amount += InventoryUtils.getItemCountInItemHandler(new InvWrapper((TileEntityChest) entity), predicate);
            }

            if(amount >= stop)
            {
                return amount;
            }
        }
        return amount;
    }

    /**
     * BuildingHut view for the client.
     */
    public static class View extends AbstractBuildingView
    {
        /**
         * Constructor for the BuildingHut view.
         *
         * @param c ColonyView associated with this building.
         * @param l The location of this building.
         */
        protected View(final ColonyView c, @NotNull final BlockPos l)
        {
            super(c, l);
        }
    }
}
