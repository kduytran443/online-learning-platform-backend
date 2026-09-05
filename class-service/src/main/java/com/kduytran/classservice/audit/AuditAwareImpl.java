package com.kduytran.classservice.audit;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditAwareImpl implements AuditorAware<String> {

    /**
     * Returns the current auditor of the application.
     *
     * @return the current auditor.
     */
    @NotNull
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("CLASS_SERVICE");
    }
}
