package net.novelmc.novelengine.command;

import net.novelmc.novelengine.architect.ArchitectList;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.util.NUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Manage an architect", usage = "/<command> <add <player> | remove <player> | info <player>", source = SourceType.BOTH, rank = Rank.TRAINEE)
public class Command_architect
{

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length < 2)
        {
            return false;
        }

        switch (args.length)
        {
            case 2:
            {
                if (args[0].equalsIgnoreCase("add"))
                {
                    if (!Rank.getRank(sender).isAtLeast(Rank.MANAGER))
                    {
                        sender.sendMessage(ChatColor.GRAY + "You must be an Admin to be able to execute this command.");
                        return true;
                    }

                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null)
                    {
                        sender.sendMessage(ChatColor.GRAY + "That player could not be found.");
                        return true;
                    }

                    if (ArchitectList.isArchitect(player))
                    {
                        sender.sendMessage(ChatColor.GRAY + "That player is already an architect.");
                        return true;
                    }

                    if (ArchitectList.isImpostor(player))
                    {
                        NUtil.playerAction(sender, "Re-adding " + player.getName() + " to the architect list", true);

                        if (ArchitectList.isImpostor(player))
                        {
                            ArchitectList.getImpostors().remove(player.getName());
                        }

                        ArchitectList.updateIp(player);
                        return true;
                    }

                    NUtil.playerAction(sender, "Adding " + player.getName() + " to the architect list", true);
                    ArchitectList.addArchitect(player);
                    return true;
                }

                if (args[0].equalsIgnoreCase("remove"))
                {
                    if (!Rank.getRank(sender).isAtLeast(Rank.MANAGER))
                    {
                        sender.sendMessage(ChatColor.GRAY + "You must be an Admin to execute this command.");
                        return true;
                    }

                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null)
                    {
                        sender.sendMessage(ChatColor.GRAY + "That player could not be found.");
                        return true;
                    }

                    if (!ArchitectList.isArchitect(player))
                    {
                        sender.sendMessage(ChatColor.GRAY + "That player is not an architect.");
                        return true;
                    }

                    NUtil.playerAction(sender, "Removing " + player.getName() + " from the architect list", true);
                    ArchitectList.removeArchitect(ArchitectList.getArchitect(player));
                    return true;
                }

                if (args[0].equalsIgnoreCase("info"))
                {
                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null)
                    {
                        sender.sendMessage(ChatColor.GRAY + "That player could not be found.");
                        return true;
                    }

                    if (!ArchitectList.isArchitect(player))
                    {
                        sender.sendMessage(ChatColor.GRAY + "That player is not an architect.");
                        return true;
                    }

                    sender.sendMessage(ChatColor.GRAY + ArchitectList.getArchitect(player).toString());
                    return true;
                }
            }
            default:
            {
                return false;
            }
        }
    }
}
