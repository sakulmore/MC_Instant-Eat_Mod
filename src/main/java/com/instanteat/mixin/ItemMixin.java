package com.instanteat.mixin;

import com.instanteat.InstantEatMod;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {

    private static final ThreadLocal<Boolean> IS_MULTI_CONSUMING = ThreadLocal.withInitial(() -> false);

    @Inject(method = "getUseDuration", at = @At("HEAD"), cancellable = true)
    public void makeConsumptionInstant(ItemStack itemStack, LivingEntity user, CallbackInfoReturnable<Integer> cir) {
        ItemUseAnimation action = itemStack.getUseAnimation();
        if (action == ItemUseAnimation.EAT || action == ItemUseAnimation.DRINK) {
            cir.setReturnValue(1);
        }
    }

    @Inject(method = "finishUsingItem", at = @At("RETURN"), cancellable = true)
    public void onFinishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> cir) {
        if (InstantEatMod.config != null && !InstantEatMod.config.restoreFullHunger) {
            return;
        }

        if (IS_MULTI_CONSUMING.get()) return;

        if (livingEntity instanceof Player player && stack.getUseAnimation() == ItemUseAnimation.EAT) {
            ItemStack currentStack = cir.getReturnValue();
            Item originalItem = stack.getItem();

            IS_MULTI_CONSUMING.set(true);
            try {
                while (player.getFoodData().needsFood()
                        && !currentStack.isEmpty()
                        && currentStack.getItem() == originalItem) {

                    currentStack = currentStack.getItem().finishUsingItem(currentStack, level, player);
                }
            } finally {
                IS_MULTI_CONSUMING.set(false);
            }

            cir.setReturnValue(currentStack);
        }
    }
}