package minegame159.meteorclient.modules.combat;

//Created by squidoodly 08/10/2020

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import minegame159.meteorclient.events.TickEvent;
import minegame159.meteorclient.friends.FriendManager;
import minegame159.meteorclient.modules.Category;
import minegame159.meteorclient.modules.ToggleModule;
import minegame159.meteorclient.settings.BoolSetting;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AutoTrap extends ToggleModule {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> turnOff = sgGeneral.add(new BoolSetting.Builder()
            .name("turn-off")
            .description("Turns off when placed.")
            .defaultValue(false)
            .build()
    );

    public AutoTrap(){
        super(Category.Combat, "auto-trap", "Traps people in an obsidian cage.");
    }

    private PlayerEntity target = null;
    private BlockPos targetPos;
    private int obsidianSlot;
    private int prevSlot;

    @EventHandler
    private final Listener<TickEvent> onTick = new Listener<>(event -> {
        obsidianSlot = -1;
        target = null;
        for(int i = 0; i < 9; i++){
            if (mc.player.inventory.getStack(i).getItem() == Blocks.OBSIDIAN.asItem()){
                obsidianSlot = i;
                break;
            }
        }
        if (obsidianSlot == -1) return;
        for(PlayerEntity player : mc.world.getPlayers()){
            if (player == mc.player || !FriendManager.INSTANCE.attack(player)) continue;
            if (target == null){
                target = player;
            }else if (mc.player.distanceTo(target) > mc.player.distanceTo(player)){
                target = player;
            }
        }
        if (target == null) return;
        if (mc.player.distanceTo(target) < 4){
            prevSlot = mc.player.inventory.selectedSlot;
            mc.player.inventory.selectedSlot = obsidianSlot;
            targetPos = target.getBlockPos().up();
            if(mc.world.getBlockState(targetPos.add(1, 0, 0)).getMaterial().isReplaceable()){
                mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(target.getPos().add(1, 0, 0), Direction.UP, targetPos.add(1, 0, 0), false));
                mc.player.swingHand(Hand.MAIN_HAND);
            }
            if(mc.world.getBlockState(targetPos.add(-1, 0, 0)).getMaterial().isReplaceable()){
                mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(target.getPos().add(-1, 0, 0), Direction.UP, targetPos.add(-1, 0, 0), false));
                mc.player.swingHand(Hand.MAIN_HAND);
            }
            if(mc.world.getBlockState(targetPos.add(0, 0, 1)).getMaterial().isReplaceable()){
                mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(target.getPos().add(0, 0, 1), Direction.UP, targetPos.add(0, 0, 1), false));
                mc.player.swingHand(Hand.MAIN_HAND);
            }
            if(mc.world.getBlockState(targetPos.add(0, 0, -1)).getMaterial().isReplaceable()){
                mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(target.getPos().add(0, 0, -1), Direction.UP, targetPos.add(0, 0, -1), false));
                mc.player.swingHand(Hand.MAIN_HAND);
            }
            if(mc.world.getBlockState(targetPos.add(0, 1, 0)).getMaterial().isReplaceable()){
                mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(target.getPos().add(0, 1, 0), Direction.UP, targetPos.add(0, 1, 0), false));
                mc.player.swingHand(Hand.MAIN_HAND);
            }
            if(mc.world.getBlockState(targetPos.add(0, -2, 0)).getMaterial().isReplaceable()){
                mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(target.getPos().add(0, -2, 0), Direction.UP, targetPos.add(0, 1, 0), false));
                mc.player.swingHand(Hand.MAIN_HAND);
            }
            //Auto toggle
            if (turnOff.get()) toggle();

            mc.player.inventory.selectedSlot = prevSlot;
        }
    });
}