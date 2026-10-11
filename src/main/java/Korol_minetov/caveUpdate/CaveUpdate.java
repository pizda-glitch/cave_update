package Korol_minetov.caveUpdate;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Display;
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
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

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
           Block pechka_block=event.getBlockPlaced();
           Location locate=pechka_block.getLocation().add(0.5,0.5,0.5);
           float spin=0f;
           if (pechka_block.getBlockData() instanceof Directional directional){
               BlockFace ygo_vostok=directional.getFacing();
               spin=switch(ygo_vostok){
                   case NORTH -> 180f;
                   case EAST -> -90f;
                   case SOUTH -> 0f;
                   case WEST -> 90f;
                   default -> 0f;
               };
           }
           final float grand_final_Spin=spin;
           pechka_block.getWorld().spawn(locate,ItemDisplay.class,display->{
               ItemStack displitem=pechka;
               display.setItemStack(displitem);
               display.setBillboard(Display.Billboard.FIXED);
               display.getPersistentDataContainer().set(pechka_blocka_grechkey, PersistentDataType.STRING,"superpech");
               display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.FIXED);
               display.setTransformation(new Transformation(
                       new Vector3f(0f,0f,0f),
                       new AxisAngle4f(0f,0f,0f,0f),
                       new Vector3f(0.2501f,0.2501f,0.2501f),
                       new AxisAngle4f(0f,0f,0f,0f)
               ));
               display.setBrightness(new Display.Brightness(10,15));
               display.setRotation(grand_final_Spin,0f);
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
        if (center.getType()== Material.FURNACE){
            Location furnacenter=center.getLocation().add(0.5,0.5,0.5);
            center.getWorld().getNearbyEntities(furnacenter,0.2,0.2,0.2).forEach(entity ->{
                if (entity instanceof ItemDisplay display) {
                    String type=display.getPersistentDataContainer().get(pechka_blocka_grechkey,PersistentDataType.STRING);
                    if ("superpech".equals(type)){
                        display.remove();
                        Event.setDropItems(false);
                        center.getWorld().dropItemNaturally(center.getLocation(),createsuperfurnance());
                    }
                }
            });
        }
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
                    Block zhertva=center.getRelative(x,y,z);
                    if (zhertva.getType().isAir()||zhertva.getType().getHardness()<0){continue;}
                    if (nigrist.contains(zhertva.getType().name())){continue;}
                    skameika_zapasnih.add(zhertva);
                    zhertva.breakNaturally();
                    skameika_zapasnih.remove(zhertva);
                }
            }
        }
    }
    public ItemStack crtmusor_drop(){
        Material material= Material.FURNACE;
        ItemStack item=new ItemStack(material);
        ItemMeta itmmt=item.getItemMeta();
        if (itmmt==null){
            return item;
        }
        itmmt.setCustomModelData(6768);
        itmmt.setDisplayName("&l&4super pechka");
        item.setItemMeta(itmmt);
        return item;
    }
    @Override
    public void onDisable() {
        getLogger().info("end of industrialization");
    }
}
