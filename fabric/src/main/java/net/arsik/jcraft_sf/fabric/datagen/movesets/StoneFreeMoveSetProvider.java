package net.arsik.jcraft_sf.fabric.datagen.movesets;

import net.arsik.jcraft_sf.common.register.SFStandTypeRegistry;
import net.arsik.jcraft_sf.common.stand.StoneFreeEntity;
import net.arna.jcraft.api.attack.MoveMap;
import net.arna.jcraft.api.attack.MoveSet;
import net.arna.jcraft.api.datagen.JCraftMoveSetProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

import java.util.function.Consumer;

public class StoneFreeMoveSetProvider extends JCraftMoveSetProvider<StoneFreeEntity, StoneFreeEntity.State> {
    public StoneFreeMoveSetProvider(FabricDataOutput dataOutput) {
        super(dataOutput, MoveMap.Entry.codecFor(StoneFreeEntity.State.class), SFStandTypeRegistry.STONE_FREE.getId());
    }

    @Override
    protected void configureMoveSets(Consumer<MoveSet<StoneFreeEntity, StoneFreeEntity.State>> provider) {
        provider.accept(StoneFreeEntity.MOVE_SET);
    }
}
