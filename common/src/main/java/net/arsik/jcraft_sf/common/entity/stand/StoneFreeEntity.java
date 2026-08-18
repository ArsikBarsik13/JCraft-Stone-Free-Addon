package net.arsik.jcraft_sf.common.entity.stand;

import net.arna.jcraft.JCraft;
import net.arna.jcraft.api.registry.JStatusRegistry;
import net.arna.jcraft.api.stand.SummonData;
import net.arna.jcraft.common.attack.moves.shared.NoOpMove;
import net.arna.jcraft.common.attack.moves.shared.SimpleUppercutAttack;
import net.arna.jcraft.platform.JComponentPlatformUtils;
import net.arsik.jcraft_sf.StoneFree;
import net.arsik.jcraft_sf.common.attack.CocoonAttack;
import net.arsik.jcraft_sf.common.attack.LowGrappleAttack;
import net.arsik.jcraft_sf.common.register.SFSoundRegistry;
import net.arsik.jcraft_sf.common.register.SFStandTypeRegistry;
import mod.azure.azurelib.animation.dispatch.command.AzCommand;
import mod.azure.azurelib.animation.play_behavior.AzPlayBehaviors;
import net.arna.jcraft.api.attack.MoveMap;
import net.arna.jcraft.api.attack.MoveSet;
import net.arna.jcraft.api.attack.MoveSetManager;
import net.arna.jcraft.api.attack.enums.MoveClass;
import net.arna.jcraft.api.registry.JSoundRegistry;
import net.arna.jcraft.api.stand.StandData;
import net.arna.jcraft.api.stand.StandEntity;
import net.arna.jcraft.api.stand.StandInfo;
import net.arna.jcraft.common.attack.moves.shared.MainBarrageAttack;
import net.arna.jcraft.common.attack.moves.shared.SimpleAttack;
import net.arna.jcraft.common.util.StandAnimationState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class StoneFreeEntity extends StandEntity<StoneFreeEntity, StoneFreeEntity.State> {
    public static final MoveSet<StoneFreeEntity, State> MOVE_SET = MoveSetManager.create(SFStandTypeRegistry.STONE_FREE,
            StoneFreeEntity::registerMoves, StoneFreeEntity.class, State.class);
    public static final StandData DATA = StandData.builder()
            .idleDistance(1.25f)
            .idleRotation(-45f)
            .info(StandInfo.builder()
                    .name(Component.translatable("entity.jcraft_sf.stone_free"))
                    .proCount(3)
                    .conCount(3)
                    .freeSpace(Component.literal(""" 
                            B&Bs
                            (Web Pre-Planted) > Barrage > (Dash Back) > S2 (hit opponent into the web) > S3 web followup
                            Cr M1 ~ Cr M1 > M1 > Barrage
                            Cr M1 ~ Cr M1 > Cr S2"""
                    ))
                    .build())
            .summonData(SummonData.of(SFSoundRegistry.SF_SUMMON))
            .build();

    public static final SimpleAttack<StoneFreeEntity> LEFT_FOREFOOT_SMACK_FOLLOWUP = new SimpleAttack<StoneFreeEntity>(
            JCraft.LIGHT_COOLDOWN, 5, 8, 1.0f, 3f, 4, 1.2f, 0.2f, -0.1f)
            .withImpactSound(JSoundRegistry.IMPACT_3)
            .withInfo(
                    Component.literal("Left Forefoot Smack"),
                    Component.empty()
            );
    public static final SimpleAttack<StoneFreeEntity> LEFT_FOREFOOT_SMACK = new SimpleAttack<StoneFreeEntity>(
            JCraft.LIGHT_COOLDOWN, 3, 8, 0.9f, 2.3f, 8, 1.1f, 0.2f, -0.1f)
            .withFollowup(LEFT_FOREFOOT_SMACK_FOLLOWUP)
            .withImpactSound(JSoundRegistry.IMPACT_4)
            .withInfo(
                    Component.literal("Left Forefoot Smack"),
                    Component.literal("")
            );
    public static final SimpleUppercutAttack<StoneFreeEntity> LIGHT_FOLLOWUP = new SimpleUppercutAttack<StoneFreeEntity>(
            0, 4, 8, 0.8f, 2.4f, 2, 1.2f, 0.2f, 0.0f, 0.3f)
            .withImpactSound(JSoundRegistry.IMPACT_2)
            .withInfo(
                    Component.literal("Light Followup"),
                    Component.empty()
            );
    public static final SimpleAttack<StoneFreeEntity> LIGHT = new SimpleAttack<StoneFreeEntity>(
            JCraft.LIGHT_COOLDOWN, 3, 8, 0.75f, 2f, 7, 1.2f, 0.2f, 0.0f)
            .withCrouchingVariant(LEFT_FOREFOOT_SMACK)
            .withFollowup(LIGHT_FOLLOWUP)
            .withImpactSound(JSoundRegistry.IMPACT_1)
            .withInfo(
                    Component.literal("Punch"),
                    Component.literal("Quick combo starter/extender")
            );
    public static final SimpleAttack<StoneFreeEntity> HEAVY = new SimpleAttack<StoneFreeEntity>(
            60, 5, 8, 1.25f, 4f, 9, 1.0f, 0.5f, 0.0f)
            .withArmor(1)
            .withImpactSound(JSoundRegistry.IMPACT_6)
            .withInfo(
                    Component.literal("Heavy"),
                    Component.literal("Extended range heavy, 1 point of armour, if the opponent is too close you will miss them")
            );
    public static final MainBarrageAttack<StoneFreeEntity> BARRAGE = new MainBarrageAttack<StoneFreeEntity>(
            280, 0, 40, 0.75f, 1f, 26, 2f, 0.25f, 0f, 3, Blocks.OBSIDIAN.defaultDestroyTime())
            .withSound(SFSoundRegistry.SF_BARRAGE)
            .withInfo(
                    Component.translatable("jcraft.generic.barrage"),
                    Component.literal("Strong generic barrage")
            );

    public static final LowGrappleAttack<StoneFreeEntity> LOW_GRAPPLE = new LowGrappleAttack<StoneFreeEntity>(50 * 20, 14, 30,
            0f, 4f, 10, 2.5f, 0.3f, 0)
            .withInfo(
                    Component.literal("Perfect Freeze"),
                    Component.literal("""
                            freezes all nearby enemies
                            summons 3 ice branches to chase opponents
                            stops all nearby projectiles""")
            )
            .withSound(JSoundRegistry.HORUS_PlACE_CREEPING_ICE);

    public static final NoOpMove<StoneFreeEntity> COCOON_HOLD =
            new NoOpMove<>(0, 100, 0f);

    public static final CocoonAttack COCOON = new CocoonAttack(
            100, 10, 13, 1.25f, 2f, 60, 1.0f, 0.5f, 0.0f, COCOON_HOLD, 100, 2.3f)
            .withImpactSound(JSoundRegistry.IMPACT_1)
            .withCrouchingVariant(LOW_GRAPPLE)
            .withInfo(
                    Component.literal("Cocoon"),
                    Component.literal("Wraps one arm around the target, locking them down while stunned")
            );

    private LivingEntity cocoonTarget;

    public void setCocoonTarget(@Nullable LivingEntity target) {
        this.cocoonTarget = target;
    }

    private LivingEntity grappleReelTarget;

    public void onGrappleCatch(final LivingEntity target) {
        target.addEffect(new MobEffectInstance(JStatusRegistry.KNOCKDOWN.get(), 40, 0, true, false));
        grappleReelTarget = target;
    }

    public StoneFreeEntity(final Level world) {
        super(SFStandTypeRegistry.STONE_FREE.get(), world);

        auraColors = new Vector3f[] {
                new Vector3f(0.459f,0.667f,1.f),
                new Vector3f(0.369f,1.f,0.945f),
                new Vector3f(0.f,0.698f,1.f),
                new Vector3f(0.486f,0.776f,1.f),
        };
    }

    private static void registerMoves(MoveMap<StoneFreeEntity, State> moveMap) {
        moveMap.register(MoveClass.LIGHT, LIGHT, State.LIGHT)
                .withFollowup(State.LIGHT);

        moveMap.register(MoveClass.HEAVY, HEAVY, State.LIGHT);

        moveMap.register(MoveClass.BARRAGE, BARRAGE, State.BARRAGE);

        moveMap.register(MoveClass.SPECIAL2, COCOON, State.COCOON)
                .withCrouchingVariant(State.LOW_GRAPPLE);
    }

    @Override
    public boolean initMove(final MoveClass moveClass) {
        if (tryFollowUp(moveClass, MoveClass.LIGHT)) return true;
        return super.initMove(moveClass);
    }

    @Override
    public void tick() {
        super.tick();

        if (cocoonTarget != null && !level().isClientSide) {
            final boolean stillStunned = cocoonTarget.isAlive()
                    && cocoonTarget.getEffect(JStatusRegistry.DAZED.get()) != null;

            if (!stillStunned) {
                breakCocoon();
            }
        }
    }

    private void breakCocoon() {
        if (cocoonTarget.isAlive()) {
            JComponentPlatformUtils.getGrab(cocoonTarget).endGrab();
            cocoonTarget.addEffect(new MobEffectInstance(JStatusRegistry.KNOCKDOWN.get(), 20, 0, true, false));
        }
        cocoonTarget = null;
        cancelMove(); //returns to SF
    }

    @NotNull
    @Override
    public StoneFreeEntity getThis() {
        return this;
    }

    @Override
    protected State[] getStateValues() {
        return State.values();
    }

    @Override
    public State getBlockState() {
        return State.BLOCK;
    }

    public enum State implements StandAnimationState<StoneFreeEntity> {
        IDLE(AzCommand.create(StoneFree.BASE_CONTROLLER, "idle", AzPlayBehaviors.LOOP)),
        LIGHT(AzCommand.create(StoneFree.BASE_CONTROLLER, "light", AzPlayBehaviors.HOLD_ON_LAST_FRAME)),
        BLOCK(AzCommand.create(StoneFree.BASE_CONTROLLER, "block", AzPlayBehaviors.LOOP)),
        BARRAGE(AzCommand.create(StoneFree.BASE_CONTROLLER, "barrage", AzPlayBehaviors.LOOP)),
        COCOON(AzCommand.create(StoneFree.BASE_CONTROLLER, "cocoon", AzPlayBehaviors.LOOP)),
        LOW_GRAPPLE(AzCommand.create(StoneFree.BASE_CONTROLLER, "reach", AzPlayBehaviors.HOLD_ON_LAST_FRAME));

        private final AzCommand animator;

        State(AzCommand animator) {
            this.animator = animator;
        }

        @Override
        public void playAnimation(StoneFreeEntity attacker) {
            animator.sendForEntity(attacker);
        }
    }
}
