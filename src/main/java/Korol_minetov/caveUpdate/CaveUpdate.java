package Korol_minetov.caveUpdate;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
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

    @Override
    public void onDisable() {
        getLogger().info("end of industrialization");
    }
}
