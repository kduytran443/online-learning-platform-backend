package com.kduytran.classservice.utils;

import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@UtilityClass
public class TypeUtils {

    public static Collection<UUID> stringsToUUIDs(Collection<String> stringIds) {
        return stringIds.stream().map(UUID::fromString).collect(Collectors.toList());
    }

}
