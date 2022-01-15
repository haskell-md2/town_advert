package haskell_md2.town_adv;

import com.palmergames.bukkit.towny.object.Town;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class Town_adv extends JavaPlugin {

    public static String[] AdvMessage;
    public static List<Town> BlockedTowns = new ArrayList<>();

    public static Plugin plugin;
    public static Economy economy;
    public static VKDriver vk;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        InitializeCfg();
        this.setupEconomy();
        AdvMessage = getConfig().getString("advertising_msg").split("\n");

        getCommand("tprem").setExecutor(new SendAdvert());

        vk = new VKDriver(getConfig().getString("vk_api_key"));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    void InitializeCfg(){
        File config = new File(getDataFolder()+File.separator+"config.yml");
        if(!config.exists()) {
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }
    }


    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

}
