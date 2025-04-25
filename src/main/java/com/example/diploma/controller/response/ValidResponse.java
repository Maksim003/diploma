package com.example.diploma.controller.response;

import java.util.List;

public record ValidResponse(
          String code,
          String message,
          List<String> details
) {
}
