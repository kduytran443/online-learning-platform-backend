package com.hudsontran.courseservice.extrafields;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class ExtraFieldsHandler<F extends Enum<F>, S, T> {

    private final Map<F, BiConsumer<S, T>> handlers;

    public ExtraFieldsHandler(
            Class<F> enumType
    ) {
        this.handlers = new EnumMap<>(enumType);
    }

    public ExtraFieldsHandler<F, S, T> setExtraValue(
            F field,
            BiConsumer<S, T> handler
    ) {
        handlers.put(
                field,
                handler
        );

        return this;
    }

    public void execute(
            Set<F> fields,
            S source,
            T target
    ) {
        fields.forEach(field -> {

            BiConsumer<S, T> handler =
                    handlers.get(field);

            if (handler != null) {
                handler.accept(
                        source,
                        target
                );
            }
        });
    }

    public void executeList(
            Set<F> fields,
            List<S> sources,
            List<T> targets
    ) {
        fields.forEach(field -> {

            BiConsumer<S, T> handler =
                    handlers.get(field);
            for (int i = 0; i < sources.size(); i++) {
                S source = sources.get(i);
                T target = targets.get(i);
                if (handler != null) {
                    handler.accept(
                            source,
                            target
                    );
                }
            }
        });
    }
}
