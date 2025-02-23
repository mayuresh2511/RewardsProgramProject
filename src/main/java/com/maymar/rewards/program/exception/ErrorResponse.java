package com.maymar.rewards.program.exception;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime timeStamp,
                            String statusCode,
                            String details,
                            String message) {

}
