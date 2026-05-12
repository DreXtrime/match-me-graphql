package com.matchme.server.exception;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import org.jspecify.annotations.NonNull;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

@Component
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, @NonNull DataFetchingEnvironment env) {
        return switch (ex) {
            case NotFoundException e -> GraphQLError.newError()
                    .errorType(ErrorType.DataFetchingException)
                    .message(e.getMessage())
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .build();
            case BadRequestException e -> GraphQLError.newError()
                    .errorType(ErrorType.ValidationError)
                    .message(e.getMessage())
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .build();
            case IllegalArgumentException e -> GraphQLError.newError()
                    .errorType(ErrorType.ValidationError)
                    .message("Invalid value: " + e.getMessage())
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .build();
            default -> GraphQLError.newError()
                    .errorType(ErrorType.DataFetchingException)
                    .message(ex.getMessage())
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .build();
        };
    }
}