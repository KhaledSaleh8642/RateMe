package de.hs_kl.rateme.aspect;

import de.hs_kl.rateme.api.dtos.LogInDtoIn;
import de.hs_kl.rateme.api.dtos.RegisterDtoIn;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("within(de.hs_kl.rateme.api.controllers..*) || within(de.hs_kl.rateme.model.dbaccess..*)")
    public Object logControllerAndDbAccessCalls(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        String methodName = signature.getDeclaringType().getSimpleName() + "." + signature.getName();
        String parameters = getParameters(joinPoint);

        log.info("START {} with {}", methodName, parameters);

        try {
            Object result = joinPoint.proceed();
            log.info("SUCCESS {}", methodName);
            return result;
        } catch (Throwable throwable) {
            log.error("ERROR {} - {}", methodName, throwable.getMessage());
            throw throwable;
        }
    }

    private String getParameters(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        if (args.length == 0) {
            return "no parameters";
        }

        String[] parameterNames = new String[args.length];

        if (joinPoint.getSignature() instanceof CodeSignature codeSignature) {
            parameterNames = codeSignature.getParameterNames();
        }

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < args.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }

            String parameterName = parameterNames[i] != null ? parameterNames[i] : "arg" + i;
            builder.append(parameterName).append("=").append(sanitizeParameter(parameterName, args[i]));
        }

        return builder.toString();
    }

    private String sanitizeParameter(String parameterName, Object value) {
        if (value == null) {
            return "null";
        }

        String lowerParameterName = parameterName.toLowerCase();

        if (lowerParameterName.contains("password")
                || lowerParameterName.contains("hash")
                || lowerParameterName.contains("salt")
                || lowerParameterName.contains("token")) {
            return "***";
        }

        return switch (value) {
            case LogInDtoIn loginDtoIn -> "LogInDtoIn[username=" + loginDtoIn.username() + ", password=***]";

            case RegisterDtoIn registerDtoIn -> "RegisterDtoIn[username=" + registerDtoIn.username()
                    + ", email=" + registerDtoIn.email()
                    + ", firstname=" + registerDtoIn.firstname()
                    + ", lastname=" + registerDtoIn.lastname()
                    + ", password=***]";

            case MultipartFile file -> "MultipartFile[filename=" + file.getOriginalFilename()
                    + ", size=" + file.getSize()
                    + ", contentType=" + file.getContentType() + "]";

            case byte[] bytes -> "byte[" + bytes.length + "]";

            default -> value.toString();
        };

    }
}