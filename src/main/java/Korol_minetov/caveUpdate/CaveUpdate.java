package Korol_minetov.caveUpdate;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class CaveUpdate extends JavaPlugin implements Listener, CommandExecutor, TabCompleter{
    private NamespacedKey burger;
    private NamespacedKey pechka_grechkey;
    private NamespacedKey pechka_blocka_grechkey;

    private final Map<UUID, BlockFace> za1cekyndydo=new HashMap<>();
    private final Set<Block> skameika_zapasnih=new HashSet<>();

    @Override
    public void onEnable() {
        getLogger().info("time for industrialization");
        saveDefaultConfig();
        burger=new NamespacedKey(this,"is_buritmozhno");
        pechka_grechkey=new NamespacedKey(this,"is_xz");
        pechka_blocka_grechkey=new NamespacedKey(this,"is_pechitmozhno");
        getServer().getPluginManager().registerEvents(this,this);
    }
    public boolean isbur(ItemStack bur){
        if (bur==null||!bur.hasItemMeta()){
            return false;
        }
        ItemMeta meta=bur.getItemMeta();
        if (meta==null){
            return false;
        }
        if (meta.hasCustomModelData()&&meta.getCustomModelData()==6767){
            return true;
        }
        return false;
    }
    @EventHandler
    public void oninteract(PlayerInteractEvent Event){

    }
    @Override
    public void onDisable() {
        getLogger().info("end of industrialization");
    }
}
