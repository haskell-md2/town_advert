package haskell_md2.town_adv;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Town;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SendAdvert implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!args[0].equalsIgnoreCase("selection")) return false;

        Player p = (Player)sender;

        try {
            Town town = TownyAPI.getInstance().getResident(p).getTown();
            if(town.getMayor() == TownyAPI.getInstance().getResident(p)){

                if(Town_adv.BlockedTowns.contains(town)){
                    p.sendMessage(ChatColor.RED + "Задержка");
                    return false;
                }

                if(!_write_off_money(p, Town_adv.plugin.getConfig().getDouble("advertising_price"))){
                    p.sendMessage(ChatColor.RED + "Недостаточно денег");
                    return false;
                }

                Map<String, String> info = new HashMap<String, String>();

                info.put("mayor_name", town.getMayor().getName());
                info.put("town_name", town.getName());
                info.put("nation_name", town.getNation().getName());
                info.put("coffers", String.valueOf(town.getAccount().getCachedBalance()));
                info.put("population_amount", String.valueOf(town.getResidents().size()));

                //vk
                String temp_vk = Town_adv.plugin.getConfig().getString("advertising_msg_vk");
                Pattern pattern_vk = Pattern.compile("[\\{](.*?)[\\}]");
                Matcher matcher_vk = pattern_vk.matcher(temp_vk);

                while (matcher_vk.find()) {
                    temp_vk= temp_vk.replace("{" + matcher_vk.group(1) + "}", info.get(matcher_vk.group(1)));
                }
                Town_adv.vk.CreatePostInGroup(temp_vk, Town_adv.plugin.getConfig().getString("group_id"));
                //-------------------------

                for(Player ps : Bukkit.getOnlinePlayers()) {
                    if(TownyAPI.getInstance().getResident(p).getTown() != town || p == ps) {
                        for (String str : Town_adv.AdvMessage) {

                            Pattern pattern = Pattern.compile("[\\{](.*?)[\\}]");
                            Matcher matcher = pattern.matcher(str);

                            while (matcher.find()) {
                                if (matcher.group(1).equalsIgnoreCase("join_btn")) {
                                    TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&',Town_adv.plugin.getConfig().getString("join_btn")));
                                    message.setClickEvent(new net.md_5.bungee.api.chat.ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/t join "+info.get("town_name")));
                                    ps.spigot().sendMessage((message));

                                    str = "";
                                } else
                                    str = str.replace("{" + matcher.group(1) + "}", info.get(matcher.group(1)));
                            }

                            ps.sendMessage(ChatColor.translateAlternateColorCodes('&', str.trim()));
                        }
                    }

                }
                Town_adv.BlockedTowns.add(town);

                BukkitTask tas = Bukkit.getScheduler().runTaskLater(Town_adv.plugin, new Runnable() {
                    @Override
                    public void run() {
                        Town_adv.BlockedTowns.remove(town);
                    }
                }, 20l * Town_adv.plugin.getConfig().getInt("advertising_delay"));
            }

        } catch (NotRegisteredException e) {
            //e.printStackTrace();
            return false;
        }

        return false;
    }

    private boolean _write_off_money(Player p, Double price){
        if(Town_adv.economy.getBalance(p) < price) return false;
        Town_adv.economy.withdrawPlayer(p, price);
        return true;
    }

}
