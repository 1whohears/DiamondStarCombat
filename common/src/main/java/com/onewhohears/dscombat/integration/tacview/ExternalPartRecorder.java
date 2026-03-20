package com.onewhohears.dscombat.integration.tacview;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.entity.parts.EntityPart;
import com.onewhohears.tacview.client.core.ClientPlayback;
import com.onewhohears.tacview.common.core.EntityKeyframe;
import com.onewhohears.tacview.common.core.EntityRecorder;
import com.onewhohears.tacview.common.core.KeyframeValue;
import com.onewhohears.tacview.common.core.RecordingSession;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.BiFunction;

public class ExternalPartRecorder extends EntityRecorder<EntityKeyframe<EntityPart>,EntityPart> {

    public static final BiFunction<ServerLevel,UUID,EntityPart> DEFAULT_PART_GETTER =
            (level, uuid) -> level.getEntity(uuid) instanceof EntityPart part ? part : null;

    public final KeyframeValue.StringV<EntityPart> preset = registerStringValue("preset", EntityPart::getStatsId, EntityPart::setStatsId);

    public ExternalPartRecorder(@NotNull EntityPart entity, int recordRate) {
        super(entity, recordRate, DEFAULT_PART_GETTER);
    }

    public ExternalPartRecorder(@NotNull JsonObject data, @NotNull RecordingSession session) {
        super(data, DEFAULT_PART_GETTER, session);
    }

    @Override
    public void onPlaybackEntitySetup(@NotNull EntityPart entity, @NotNull ClientPlayback playback) {
        super.onPlaybackEntitySetup(entity, playback);
        entity.setPreset(preset.get());
        entity.updateClientStatsHolder();
    }

    @Override
    protected @Nullable EntityKeyframe<EntityPart> readKeyframe(@NotNull JsonObject jsonObject) {
        return new EntityKeyframe<>(jsonObject);
    }

    @Override
    protected EntityKeyframe<EntityPart> newKeyframe(@NotNull EntityPart entityPart) {
        return new EntityKeyframe<>(entityPart);
    }

    @Override
    protected EntityKeyframe<EntityPart> emptyKeyframe() {
        return new EntityKeyframe<>();
    }
}
