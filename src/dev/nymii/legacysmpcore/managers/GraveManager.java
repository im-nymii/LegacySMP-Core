package dev.nymii.legacysmpcore.managers;

import dev.nymii.legacysmpcore.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.time.Duration;

public class GraveManager implements Listener {

    private static final Material GRAVE_MATERIAL = Material.STONE_BRICK_WALL;

    private final NamespacedKey graveKey;

    public GraveManager(Main plugin) {
        this.graveKey = new NamespacedKey(plugin, "grave");
    }

    public void createGrave(Player player, Location deathLocation) {
        World world = deathLocation.getWorld();
        if (world == null) {
            return;
        }

        Block block = deathLocation.getBlock();
        block.setType(GRAVE_MATERIAL, false);

        Location standLoc = block.getLocation().add(0.5, 0.3, 0.5);
        String name = player.getName();

        ArmorStand stand = world.spawn(standLoc, ArmorStand.class);
        stand.setInvisible(true);
        stand.setMarker(true);
        stand.setGravity(false);
        stand.setInvulnerable(true);
        stand.setSmall(true);
        stand.setBasePlate(false);
        stand.setPersistent(true);
        stand.setCanPickupItems(false);
        stand.customName(Component.text(name, NamedTextColor.WHITE));
        stand.setCustomNameVisible(true);
        stand.getPersistentDataContainer().set(graveKey, PersistentDataType.BYTE, (byte) 1);
    }

    private boolean isGraveBlock(Block block) {
        if (block.getType() != GRAVE_MATERIAL) {
            return false;
        }

        Location center = block.getLocation().add(0.5, 0.5, 0.5);
        for (Entity entity : block.getWorld().getNearbyEntities(center, 0.6, 1.5, 0.6)) {
            if (entity instanceof ArmorStand
                    && entity.getPersistentDataContainer().has(graveKey, PersistentDataType.BYTE)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAllowedShovel(Material material) {
        return material == Material.IRON_SHOVEL
                || material == Material.DIAMOND_SHOVEL
                || material == Material.NETHERITE_SHOVEL;
    }

    private void removeGrave(Block block) {
        World world = block.getWorld();
        Location center = block.getLocation().add(0.5, 0.5, 0.5);

        for (Entity entity : world.getNearbyEntities(center, 0.6, 1.5, 0.6)) {
            if (entity instanceof ArmorStand
                    && entity.getPersistentDataContainer().has(graveKey, PersistentDataType.BYTE)) {
                entity.remove();
            }
        }
        block.setType(Material.AIR, false);

        world.spawnParticle(Particle.BLOCK, center, 40, 0.3, 0.5, 0.3, 0.1,
                Material.DIRT.createBlockData());
        world.spawnParticle(Particle.BLOCK, center, 40, 0.3, 0.5, 0.3, 0.1,
                Material.STONE.createBlockData());

        world.playSound(center, Sound.BLOCK_MUD_BREAK, 1.0f, 1.0f);
        world.playSound(center, Sound.BLOCK_STONE_BREAK, 1.0f, 1.0f);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        Block block = event.getClickedBlock();
        if (block == null || !isGraveBlock(block)) {
            return;
        }

        event.setCancelled(true);

        Player player = event.getPlayer();
        Material tool = player.getInventory().getItemInMainHand().getType();

        if (isAllowedShovel(tool)) {
            removeGrave(block);

            player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 200, 0, false, true));
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 0, false, true));

            player.showTitle(Title.title(
                    Component.text("Grave removed", NamedTextColor.GREEN),
                    Component.empty(),
                    Title.Times.times(
                            Duration.ofMillis(150),
                            Duration.ofSeconds(1),
                            Duration.ofMillis(400))
            ));
        } else {
            player.showTitle(Title.title(
                    Component.text("Protected Grave", NamedTextColor.RED),
                    Component.text("Requires an iron, diamond or netherite shovel",
                            NamedTextColor.GRAY),
                    Title.Times.times(
                            Duration.ofMillis(150),
                            Duration.ofSeconds(2),
                            Duration.ofMillis(400))
            ));
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (isGraveBlock(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        if (isGraveBlock(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        event.blockList().removeIf(this::isGraveBlock);
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        event.blockList().removeIf(this::isGraveBlock);
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        if (event.getBlocks().stream().anyMatch(this::isGraveBlock)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        if (event.getBlocks().stream().anyMatch(this::isGraveBlock)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        if (isGraveBlock(event.getToBlock())) {
            event.setCancelled(true);
        }
    }
}

