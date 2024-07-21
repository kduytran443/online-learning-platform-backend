package com.kduytran.classresourceservice.audit;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditAwareImpl implements AuditorAware<String> {

    // @Autowired
    private HttpServletRequest request;

    /**
     * Returns the current auditor of the application.
     *
     * @return the current auditor.
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        String currentAuditor = request != null ? request.getHeader("CurrentAuditor") : null;
        return Optional.of(currentAuditor == null ? "CLASS_RESOURCE_MS" : currentAuditor);
    }

}
