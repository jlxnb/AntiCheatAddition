package de.photon.aacadditionpro.util.violationlevels;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public abstract class ViolationManagement
{
    /**
     * A {@link List} of {@link Threshold}s which is guaranteed to be sorted.
     */
    protected final ThresholdList thresholds;

    /**
     * The module id of the handler
     */
    protected final String moduleId;

    /**
     * Create a new {@link ViolationManagement}
     *
     * @param moduleId the module id of the module this {@link ViolationManagement} is being used by.
     */
    public ViolationManagement(final String moduleId)
    {
        this.moduleId = moduleId;
        this.thresholds = ThresholdList.loadThresholds(moduleId + ".thresholds");
    }

    public static Flag flagFromPlayer(Player player)
    {
        return new Flag(player);
    }

    public static Flag flagFromPlayers(Collection<Player> players)
    {
        return new Flag(players);
    }

    /**
     * Flags a {@link Player} according to the options set in the {@link Flag}.
     */
    public abstract void flag(final Flag flag);

    /**
     * @param uuid the {@link UUID} of the {@link Player} whose vl should be returned.
     *
     * @return the vl of the given uuid.
     */
    public abstract int getVL(final UUID uuid);

    /**
     * Sets the vl of a player.
     *
     * @param player the {@link Player} whose vl should be set
     * @param newVl  the new vl of the player.
     */
    public abstract void setVL(final Player player, final int newVl);

    /**
     * Adds an {@link Integer} to the vl of a player. The number can be negative, this will decrease the vl then.
     *
     * @param player the {@link Player} whose vl should be set
     * @param vl     the vl to be added to the current vl.
     */
    protected abstract void addVL(final Player player, final int vl);

    /**
     * Used to execute the commands that are defined in the config section CHECK_NAME.thresholds
     *
     * @param player the {@link Player} that should be punished and that should be used to apply the placeholders
     * @param fromVl the last vl of the player before the addition and the searching-range for command.
     */
    protected abstract void punishPlayer(final Player player, final int fromVl, final int toVl);

    /**
     * Has options for the flagging process.
     * {@link Flag} contains the player
     */
    protected static class Flag
    {
        protected Player player;
        protected Collection<Player> team;
        protected int addedVl = 1;
        protected int cancelVl = -1;
        protected Runnable onCancel = null;
        protected Runnable eventNotCancelled = null;

        private Flag(Player player)
        {
            this.player = player;
        }

        private Flag(Collection<Player> team)
        {
            this.team = team;
        }

        public Flag setAddedVl(int addedVl)
        {
            this.addedVl = addedVl;
            return this;
        }

        public Flag setCancelAction(int cancelVl, Runnable onCancel)
        {
            this.cancelVl = cancelVl;
            this.onCancel = onCancel;
            return this;
        }

        public Flag setEventNotCancelledAction(Runnable eventNotCancelled)
        {
            this.eventNotCancelled = eventNotCancelled;
            return this;
        }
    }
}
