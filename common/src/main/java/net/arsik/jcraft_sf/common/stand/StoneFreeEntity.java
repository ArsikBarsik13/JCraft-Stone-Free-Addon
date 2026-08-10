package net.arsik.jcraft_sf.common.stand;

import net.arna.jcraft.JCraft;
import net.arna.jcraft.api.stand.SummonData;
import net.arna.jcraft.common.attack.moves.shared.SimpleUppercutAttack;
import net.arsik.jcraft_sf.StoneFree;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class StoneFreeEntity extends StandEntity<StoneFreeEntity, StoneFreeEntity.State> {
    public static final MoveSet<StoneFreeEntity, State> MOVE_SET = MoveSetManager.create(SFStandTypeRegistry.STONE_FREE,
            StoneFreeEntity::registerMoves, StoneFreeEntity.class, State.class);
    public static final StandData DATA = StandData.builder()
            .info(StandInfo.builder()
                    .name(Component.literal("Stone Free"))
                    .proCount(3)
                    .conCount(3)
                    .freeSpace(Component.literal(""" 
                            B&Bs
                            (Web Pre-Planted) > Barrage > (Dash Back) > S2 (hit opponent into the web) > S3 web followup
                            Cr M1 ~ Cr M1 > M1 > Barrage
                            Cr M1 ~ Cr M1 > Cr S2"""
                    ))
                    .build())
            .summonData(SummonData.of(JSoundRegistry.STAR_PLATINUM_SUMMON))
            .build();

    /*public static final SimpleAttack<StoneFreeEntity> LEFT_FOREFOOT_SMACK = new SimpleAttack<StoneFreeEntity>(
            JCraft.LIGHT_COOLDOWN, 5, 8, 0.75f, 5f, 7, 1.5f, 0.2f, -0.1f)
            .withImpactSound(JSoundRegistry.IMPACT_1)
            .withInfo(
                    Component.literal("Punch"),
                    Component.literal("An example punch attack")
            ); */
    public static final SimpleUppercutAttack<StoneFreeEntity> LIGHT_FOLLOWUP = new SimpleUppercutAttack<StoneFreeEntity>(
            0, 4, 8, 0.8f, 2.4f, 9, 1.2f, 0.2f, 0.0f, 0.3f)
            .withImpactSound(JSoundRegistry.IMPACT_1)
            .withInfo(
                    Component.literal("Light Followup"),
                    Component.empty()
            );
    public static final SimpleAttack<StoneFreeEntity> LIGHT = new SimpleAttack<StoneFreeEntity>(
            JCraft.LIGHT_COOLDOWN, 5, 8, 0.75f, 2f, 7, 1.2f, 0.2f, 0.0f)
            //.withCrouchingVariant(LEFT_FOREFOOT_SMACK)
            .withFollowup(LIGHT_FOLLOWUP)
            .withImpactSound(JSoundRegistry.IMPACT_1)
            .withInfo(
                    Component.literal("Punch"),
                    Component.literal("Quick combo starter/extender")
            );
    public static final SimpleAttack<StoneFreeEntity> HEAVY = new SimpleAttack<StoneFreeEntity>(
            60, 5, 8, 1.25f, 4f, 9, 1.0f, 0.5f, 0.0f)
            .withArmor(1)
            .withImpactSound(JSoundRegistry.IMPACT_1)
            .withInfo(
                    Component.literal("Heavy"),
                    Component.literal("Simple heavy, 1 point of armour, if the opponent is too close you will miss them")
            );
    public static final MainBarrageAttack<StoneFreeEntity> BARRAGE = new MainBarrageAttack<StoneFreeEntity>(
            280, 0, 40, 0.75f, 1f, 26, 2f, 0.25f, 0f, 3, Blocks.OBSIDIAN.defaultDestroyTime())
            .withSound(JSoundRegistry.STAR_PLATINUM_BARRAGE)
            .withInfo(
                    Component.translatable("jcraft.generic.barrage"),
                    Component.literal("Simple barrage")
            );

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
        moveMap.register(MoveClass.LIGHT, LIGHT, null).withFollowup(null);

        moveMap.register(MoveClass.HEAVY, HEAVY, null);

        moveMap.register(MoveClass.BARRAGE, BARRAGE, null);
    }

    @Override
    public boolean initMove(final MoveClass moveClass) {
        if (tryFollowUp(moveClass, MoveClass.LIGHT)) return true;
        return super.initMove(moveClass);
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
        BARRAGE(AzCommand.create(StoneFree.BASE_CONTROLLER, "barrage", AzPlayBehaviors.LOOP));

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
