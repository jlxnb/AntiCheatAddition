package de.photon.anticheataddition.commands.subcommands;

import de.photon.anticheataddition.InternalPermission;
import de.photon.anticheataddition.commands.CommandAttributes;
import de.photon.anticheataddition.commands.InternalCommand;
import de.photon.anticheataddition.commands.TabCompleteSupplier;
import de.photon.anticheataddition.modules.ModuleManager;
import de.photon.anticheataddition.util.violationlevels.Flag;
import org.bukkit.command.CommandSender;

import java.util.Queue;

public class SetVlCommand extends InternalCommand
{
    public SetVlCommand()
    {
        super("setvl", CommandAttributes.builder()
                                        .addCommandHelp("This command sets the vl of a player for a certain violation module.",
                                                        "Helpful for testing purposes or to find false positives.",
                                                        "Syntax: /anticheataddition setvl <player> <module_id> <vl>")
                                        .setPermission(InternalPermission.SETVL)
                                        .build(),
              TabCompleteSupplier.builder().allPlayers().constants(ModuleManager.getViolationModuleMap().keySet()));
    }

    @Override
    protected void execute(CommandSender sender, Queue<String> arguments)
    {
        final var player = parsePlayer(sender, arguments.poll());
        if (player == null) return;

        final var module = ModuleManager.getViolationModuleMap().getModule(arguments.poll());
        if (checkNotNullElseSend(module, sender, "The given module_id does not refer to a known module.")) return;

        final var vlString = arguments.poll();
        if (checkNotNullElseSend(vlString, sender, "Please specify the new vl you want to set for the module.")) return;

        final Integer vl = parseIntElseSend(sender, vlString);
        if (vl == null) return;

        // Reset the vl of the player to 0.
        module.getManagement().setVL(player, 0);

        // Actually flag the player for debug messages.
        module.getManagement().flag(Flag.of(player).setAddedVl(vl));
    }
}
