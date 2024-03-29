package com.tr.hsyn.telefonrehberi.main.call.activity.random.cast;


import androidx.annotation.NonNull;

import java.util.function.Consumer;


public interface GenerationRuler {

    void startGeneration(@NonNull final GenerationArgs args);

    void stopGeneration();

    boolean isInGeneration();

    void onMissionCompleted(Consumer<Integer> consumer);
}
