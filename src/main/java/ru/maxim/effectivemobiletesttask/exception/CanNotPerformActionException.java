package ru.maxim.effectivemobiletesttask.exception;

import ru.maxim.effectivemobiletesttask.dto.ApiResponse;

public class CanNotPerformActionException extends RuntimeException {

    private final transient ApiResponse apiResponse;

    public CanNotPerformActionException(String message) {
        super();
        apiResponse = new ApiResponse(Boolean.FALSE, message);
    }

    public ApiResponse getApiResponse() {
        return apiResponse;
    }

}
