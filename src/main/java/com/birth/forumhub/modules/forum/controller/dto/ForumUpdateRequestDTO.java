package com.birth.forumhub.modules.forum.controller.dto;
import jakarta.validation.constraints.Size;

public record ForumUpdateRequestDTO(@Size(max = 50)
                                    String name,

                                    @Size(max = 50)
                                    String description) {
}