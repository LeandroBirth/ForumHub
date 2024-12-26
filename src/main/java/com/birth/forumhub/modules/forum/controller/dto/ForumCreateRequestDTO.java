package com.birth.forumhub.modules.forum.controller.dto;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record ForumCreateRequestDTO(@NotBlank
                                    @Length(min = 3, max = 50)
                                    String name,

                                    @NotBlank
                                    @Length(min = 3, max = 50)
                                    String description) {
}