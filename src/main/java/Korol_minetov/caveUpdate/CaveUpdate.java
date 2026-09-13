package Korol_minetov.caveUpdate;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
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
    public boolean ispech(ItemStack pech){
        if(pech==null||!pech.hasItemMeta()){
            return false;
        }
        ItemMeta meta=pech.getItemMeta();
        if (meta==null){
            return false;
        }
        if (meta.hasCustomModelData()&&meta.getCustomModelData()==6768){
            return true;
        }
        return false;
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
    public void onBlockPlace(BlockPlaceEvent event) {
       ItemStack pechka=event.getItemInHand();
       if(ispech(pechka)){
           Block block=event.getBlockPlaced();
           Location locate=block.getLocation().add(0.5,0.5,0.5);
           block.getWorld().spawn(locate,ItemDisplay.class,display->{

           });
       }
    }
    @EventHandler
    public void oninteract(PlayerInteractEvent Event){
        if (Event.getAction()==Action.LEFT_CLICK_BLOCK&&Event.getClickedBlock()!=null){
            if (isbur(Event.getItem())){
                za1cekyndydo.put(Event.getPlayer().getUniqueId(),Event.getBlockFace());
            }
        }
    }
    @EventHandler(priority=EventPriority.HIGH,ignoreCancelled = true)
    public void burlamat3x3(BlockBreakEvent Event){
        Block center=Event.getBlock();
        if (skameika_zapasnih.contains(center)) return;
        Player player=Event.getPlayer();
        ItemStack item=player.getInventory().getItemInMainHand();
        if (!isbur(item)) return;
        BlockFace posledni_vzglyad=za1cekyndydo.getOrDefault(player.getUniqueId(),BlockFace.UP);

        int minX=-1,maxX=1,minY=-1,maxY=1,minZ=-1,maxZ=1;
        if (posledni_vzglyad==BlockFace.UP||posledni_vzglyad==BlockFace.DOWN){
            minY=0;
            maxY=0;
        }
        else if(posledni_vzglyad==BlockFace.EAST||posledni_vzglyad==BlockFace.WEST){
            minX=0;
            maxX=0;
        }
        else if(posledni_vzglyad==BlockFace.NORTH||posledni_vzglyad==BlockFace.SOUTH){
            minZ=0;
            maxZ=0;
        }
        List<String> nigrist=getConfig().getStringList("bur.blacklisted-blocks");
        for(int x=minX;x<=maxX;x++){
            for(int y=minY;y<=maxY;y++){
                for (int z=minZ;z<=maxZ;z++){
                    if (x==0&&y==0&&z==0){continue;}
                    getLogger().info("rabotat");
                    Block zhertva=center.getRelative(x,y,z);
                    if (zhertva.getType().isAir()||zhertva.getType().getHardness()<0){continue;}
                    if (nigrist.contains(zhertva.getType().name())){continue;}
                    getLogger().info("rabotat3");
                    skameika_zapasnih.add(zhertva);
                    zhertva.breakNaturally();
                    skameika_zapasnih.remove(zhertva);
                }
            }
        }
    }
    @Override
    public void onDisable() {
        getLogger().info("end of industrialization");
    }
}
