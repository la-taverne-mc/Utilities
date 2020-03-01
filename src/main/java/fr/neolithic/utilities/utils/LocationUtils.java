package fr.neolithic.utilities.utils;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Campfire;
import org.bukkit.block.data.type.Snow;
import org.bukkit.block.data.type.TrapDoor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LocationUtils {
    private static List<Material> hollowMaterials = Arrays.asList(
        Material.OAK_SAPLING, Material.SPRUCE_SAPLING, Material.BIRCH_SAPLING, 
        Material.JUNGLE_SAPLING, Material.ACACIA_SAPLING, Material.DARK_OAK_SAPLING,
        Material.GRASS, Material.FERN, Material.DEAD_BUSH, Material.DANDELION,
        Material.POPPY, Material.BLUE_ORCHID, Material.ALLIUM, Material.AZURE_BLUET,
        Material.RED_TULIP, Material.ORANGE_TULIP, Material.WHITE_TULIP, Material.PINK_TULIP,
        Material.OXEYE_DAISY, Material.CORNFLOWER, Material.LILY_OF_THE_VALLEY, 
        Material.BROWN_MUSHROOM, Material.RED_MUSHROOM, Material.TORCH, Material.SUNFLOWER,
        Material.LILAC, Material.ROSE_BUSH, Material.PEONY, Material.TALL_GRASS,
        Material.LARGE_FERN, Material.TUBE_CORAL, Material.BRAIN_CORAL, Material.BUBBLE_CORAL,
        Material.FIRE_CORAL, Material.HORN_CORAL, Material.TUBE_CORAL_FAN, Material.BRAIN_CORAL_FAN,
        Material.BUBBLE_CORAL_FAN, Material.FIRE_CORAL_FAN, Material.HORN_CORAL_FAN,
        Material.DEAD_TUBE_CORAL, Material.DEAD_BRAIN_CORAL, Material.DEAD_BUBBLE_CORAL,
        Material.DEAD_FIRE_CORAL, Material.DEAD_HORN_CORAL, Material.DEAD_TUBE_CORAL_FAN,
        Material.DEAD_BRAIN_CORAL_FAN, Material.DEAD_BUBBLE_CORAL_FAN, Material.DEAD_FIRE_CORAL_FAN,
        Material.DEAD_HORN_CORAL_FAN, Material.LADDER, Material.VINE, Material.SCAFFOLDING,
        Material.OAK_SIGN, Material.SPRUCE_SIGN, Material.BIRCH_SIGN, Material.JUNGLE_SIGN,
        Material.ACACIA_SIGN, Material.DARK_OAK_SIGN, Material.OAK_WALL_SIGN,
        Material.SPRUCE_WALL_SIGN, Material.BIRCH_WALL_SIGN, Material.JUNGLE_WALL_SIGN,
        Material.ACACIA_WALL_SIGN, Material.DARK_OAK_WALL_SIGN, Material.WHITE_BANNER,
        Material.ORANGE_BANNER, Material.MAGENTA_BANNER, Material.LIGHT_BLUE_BANNER,
        Material.YELLOW_BANNER, Material.LIME_BANNER, Material.PINK_BANNER, Material.GRAY_BANNER,
        Material.LIGHT_GRAY_BANNER, Material.CYAN_BANNER, Material.PURPLE_BANNER,
        Material.BLUE_BANNER, Material.BROWN_BANNER, Material.GREEN_BANNER, Material.RED_BANNER,
        Material.BLACK_BANNER, Material.POWERED_RAIL, Material.DETECTOR_RAIL, Material.RAIL,
        Material.ACTIVATOR_RAIL, Material.AIR, Material.CAVE_AIR, Material.VOID_AIR,
        Material.ORANGE_CARPET, Material.MAGENTA_CARPET, Material.LIGHT_BLUE_CARPET,
        Material.YELLOW_CARPET, Material.LIME_CARPET, Material.PINK_CARPET, Material.GRAY_CARPET,
        Material.LIGHT_GRAY_CARPET, Material.CYAN_CARPET, Material.PURPLE_CARPET,
        Material.BLUE_CARPET, Material.BROWN_CARPET, Material.GREEN_CARPET, Material.RED_CARPET,
        Material.BLACK_CARPET, Material.WHITE_CARPET, Material.IRON_DOOR, Material.OAK_DOOR,
        Material.SPRUCE_DOOR, Material.BIRCH_DOOR, Material.JUNGLE_DOOR, Material.ACACIA_DOOR,
        Material.DARK_OAK_DOOR, Material.OAK_BUTTON, Material.SPRUCE_BUTTON, Material.BIRCH_BUTTON,
        Material.JUNGLE_BUTTON, Material.ACACIA_BUTTON, Material.DARK_OAK_BUTTON,
        Material.REDSTONE_TORCH, Material.WALL_TORCH, Material.REDSTONE_WALL_TORCH,
        Material.REDSTONE_WIRE, Material.STONE_BUTTON, Material.TRIPWIRE_HOOK, Material.TRIPWIRE,
        Material.STONE_PRESSURE_PLATE, Material.LIGHT_WEIGHTED_PRESSURE_PLATE,
        Material.HEAVY_WEIGHTED_PRESSURE_PLATE, Material.OAK_PRESSURE_PLATE, Material.SPRUCE_PRESSURE_PLATE,
        Material.BIRCH_PRESSURE_PLATE, Material.JUNGLE_PRESSURE_PLATE, Material.ACACIA_PRESSURE_PLATE,
        Material.DARK_OAK_PRESSURE_PLATE
    );

    public static @Nullable Location getNearestSafeLocation(@NotNull Location location) {
        Location newLocation = null;
        Double distance = null;
        int maxHeight = location.getWorld().getMaxHeight();
        int locY = location.getBlockY();

        for (int i = 0; i < 15; i++) {
            for (int y = -i; y < 1 + i; y++) {
                if (locY + y < 0 || locY + y > maxHeight) break;

                for (int x = -i; x < 1 + i; x++) {
                    for (int z = -i; z < 1 + i; z++) {
                        Location testLocation = getApproximativeLocation(location);
                        testLocation.add(x, y, z);
                        double testDistance = testLocation.distance(location);
                        if (isLocationSafe(testLocation) && (distance == null || testDistance < distance)) {
                            distance = testDistance;
                            newLocation = testLocation;
                        }
                    }
                }
            }
        }

        return newLocation;
    }
                

    public static boolean isLocationSafe(@NotNull Location location) {
        Location loc = location.clone();
        loc.setY(closest(new double[] {loc.getBlockY(), loc.getBlockY() + 1, loc.getBlockY() - 1}, loc.getY()));
        Location locAbove = loc.clone();
        Location locUnder = loc.clone();
        locAbove.setY(loc.getY() + 1);
        locUnder.setY(loc.getY() - 1);
        Block blockAbove = locAbove.getBlock();
        Block block = loc.getBlock();
        Block blockUnder = locUnder.getBlock();
        if (isBlockUnsafe(blockUnder)) return false;
        if (isBlockHollow(blockAbove) && isBlockHollow(block) && isBlockSolid(blockUnder)) return true;
        return false;
    }

    public static boolean isBlockSolid(@NotNull Block block) {
        switch (block.getType()) {
            case SNOW:
                Snow snowData = (Snow) block.getBlockData();
                return snowData.getLayers() != 1;

            case OAK_TRAPDOOR:
            case SPRUCE_TRAPDOOR:
            case BIRCH_TRAPDOOR:
            case JUNGLE_TRAPDOOR:
            case ACACIA_TRAPDOOR:
            case DARK_OAK_TRAPDOOR:
            case IRON_TRAPDOOR:
                TrapDoor trapDoorData = (TrapDoor) block.getBlockData();
                return !trapDoorData.isOpen();
            
            case COBBLESTONE_WALL:
            case IRON_BARS:
            case NETHER_BRICK_FENCE:
            case OAK_DOOR:
            case SPRUCE_DOOR:
            case BIRCH_DOOR:
            case JUNGLE_DOOR:
            case ACACIA_DOOR:
            case DARK_OAK_DOOR:
            case IRON_DOOR:
            case OAK_FENCE:
            case SPRUCE_FENCE:
            case BIRCH_FENCE:
            case JUNGLE_FENCE:
            case ACACIA_FENCE:
            case DARK_OAK_FENCE:
            case OAK_FENCE_GATE:
            case SPRUCE_FENCE_GATE:
            case BIRCH_FENCE_GATE:
            case JUNGLE_FENCE_GATE:
            case ACACIA_FENCE_GATE:
            case DARK_OAK_FENCE_GATE:
            case GLASS_PANE:
            case WHITE_STAINED_GLASS_PANE:
            case ORANGE_STAINED_GLASS_PANE:
            case MAGENTA_STAINED_GLASS_PANE:
            case LIGHT_BLUE_STAINED_GLASS_PANE:
            case YELLOW_STAINED_GLASS_PANE:
            case LIME_STAINED_GLASS_PANE:
            case PINK_STAINED_GLASS_PANE:
            case GRAY_STAINED_GLASS_PANE:
            case LIGHT_GRAY_STAINED_GLASS_PANE:
            case CYAN_STAINED_GLASS_PANE:
            case PURPLE_STAINED_GLASS_PANE:
            case BLUE_STAINED_GLASS_PANE:
            case BROWN_STAINED_GLASS_PANE:
            case GREEN_STAINED_GLASS_PANE:
            case RED_STAINED_GLASS_PANE:
            case BLACK_STAINED_GLASS_PANE:
                return false;

            default:
                return block.getType().isSolid();
        }
    }

    public static boolean isBlockHollow(@NotNull Block block) {
        if (block.getType() == Material.SNOW) {
            Snow snowData = (Snow) block.getBlockData();
            return snowData.getLayers() == 1;
        }
        return hollowMaterials.contains(block.getType());
    }

    public static boolean isBlockUnsafe(@NotNull Block block) {
        switch (block.getType()) {
            case CAMPFIRE:
                Campfire campfireData = (Campfire) block.getBlockData();
                return campfireData.isLit();

            case CACTUS:
            case END_PORTAL:
            case FIRE:
            case MAGMA_BLOCK:
            case NETHER_PORTAL:
            case WITHER_ROSE:
                return true;

            default:
                return false;
        }
    }

    public static Location getApproximativeLocation(@NotNull Location location) {
        double x = closest(new double[] {location.getBlockX() - 0.5f, location.getBlockX() + 0.5f}, location.getX());
        double z = closest(new double[] {location.getBlockZ() - 0.5f, location.getBlockZ() + 0.5f}, location.getZ());
        float yaw = closest(new float[] {-180.f, -90.f, 0.f, 90.f, 180.f}, location.getYaw());
        float pitch = closest(new float[] {-90.f, -45.f, 0.f, 45.f, 90.f}, location.getPitch());
        
        return new Location(location.getWorld(), x, location.getBlockY(), z, yaw, pitch);
    }

    public static float closest(@NotNull float[] numbers, @NotNull float number) {
        float distance = Math.abs(numbers[0] - number);
        int idx = 0;

        for (int i = 0; i < numbers.length; i++) {
            float idistance = Math.abs(numbers[i] - number);
            if (idistance < distance) {
                idx = i;
                distance = idistance;
            }
        }

        return numbers[idx];
    }

    public static double closest(@NotNull double[] numbers, @NotNull double number) {
        double distance = Math.abs(numbers[0] - number);
        int idx = 0;

        for (int i = 0; i < numbers.length; i++) {
            double idistance = Math.abs(numbers[i] - number);
            if (idistance < distance) {
                idx = i;
                distance = idistance;
            }
        }

        return numbers[idx];
    }
}
