package net.arsik.jcraft_sf.common.attack;

import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.NonNull;
import net.arna.jcraft.api.attack.MoveType;
import net.arna.jcraft.api.attack.StateContainer;
import net.arna.jcraft.api.attack.enums.BlockableType;
import net.arna.jcraft.api.attack.moves.AbstractGrabAttack;
import net.arna.jcraft.api.attack.moves.AbstractMove;
import net.arna.jcraft.common.util.JUtils;
import net.arna.jcraft.platform.JComponentPlatformUtils;
import net.arsik.jcraft_sf.common.stand.StoneFreeEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Cocoon (S2) - a blockable, punishable grab. Wraps one arm around the target and
 * holds them in place near the attacker for as long as they remain stunned (DAZED).
 * The actual "does the stun still hold" check and the break -> knockdown transition
 * live in {@link StoneFreeEntity}'s tick loop, since that's addon-side logic that
 * doesn't need to touch core JCraft classes.
 */
public final class CocoonAttack extends AbstractGrabAttack<CocoonAttack, StoneFreeEntity, StoneFreeEntity.State> {

    public CocoonAttack(final int cooldown, final int windup, final int duration, final float moveDistance,
                        final float damage, final int stun, final float hitboxSize, final float knockback,
                        final float offset, final AbstractMove<?, ? super StoneFreeEntity> hitMove,
                        final int grabDuration, final double grabOffset) {
        super(cooldown, windup, duration, moveDistance, damage, stun, hitboxSize, knockback, offset,
                hitMove, StateContainer.of(StoneFreeEntity.State.COCOON), grabDuration, grabOffset);

        withBlockableType(BlockableType.BLOCKABLE);
    }

    @Override
    public @NotNull Set<LivingEntity> perform(StoneFreeEntity attacker, LivingEntity user) {
        Set<LivingEntity> targets = super.perform(attacker, user);

        if (!targets.isEmpty()) {
            for (LivingEntity target : targets) {
                // Re-anchor the grab position to the PLAYER (user) instead of the Stand entity
                JComponentPlatformUtils.getGrab(target).startGrab(user, getGrabDuration(), getGrabOffset());
            }

            // Clear the move lock so you can attack immediately
            JUtils.cancelMoves(attacker);
        }

        return targets;
    }

    @Override
    public @NonNull MoveType<CocoonAttack> getMoveType() {
        return Type.INSTANCE;
    }

    @Override
    protected @NonNull CocoonAttack getThis() {
        return this;
    }

    @Override
    public @NonNull CocoonAttack copy() {
        return copyExtras(new CocoonAttack(getCooldown(), getWindup(), getDuration(), getMoveDistance(), getDamage(),
                getStun(), getHitboxSize(), getKnockback(), getOffset(), getHitMove(), getGrabDuration(), getGrabOffset()));
    }

    public static class Type extends AbstractGrabAttack.Type<CocoonAttack> {
        public static final Type INSTANCE = new Type();

        @Override
        protected @NonNull App<RecordCodecBuilder.Mu<CocoonAttack>, CocoonAttack> buildCodec(RecordCodecBuilder.Instance<CocoonAttack> instance) {
            return instance.group(extras(), attackExtras(), cooldown(), windup(), duration(), moveDistance(), damage(),
                            stun(), hitboxSize(), knockback(), offset(), this.<StoneFreeEntity>hitMove(), grabDuration(), grabOffset())
                    .apply(instance, applyAttackExtras(CocoonAttack::new));
        }
    }
    public void updateGrabbedEntityPosition(LivingEntity attacker, LivingEntity victim, float grabOffset) {
        Vec3 lookVec = attacker.getLookAngle();
        double targetX = attacker.getX() + lookVec.x * grabOffset;
        double targetY = attacker.getY() + (attacker.getEyeHeight() - 0.5) + lookVec.y * grabOffset;
        double targetZ = attacker.getZ() + lookVec.z * grabOffset;

        victim.setPos(targetX, targetY, targetZ);
        victim.fallDistance = 0;
    }
}