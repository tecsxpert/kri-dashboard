package com.internship.tool.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KriRecordRequest {

    @NotBlank(message = "Title must not be blank")
    private String title;

    private String description;

    @NotBlank(message = "Category must not be blank")
    private String category;

    @NotBlank(message = "Status must not be blank")
    private String status;

    private Double score;

    private LocalDate dueDate;
}
