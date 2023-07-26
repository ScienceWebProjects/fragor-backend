package com.filament.measurement.Authentication.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.filament.measurement.Authentication.Permission.TokenType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "user")
public class Token {
    @Id
    @GeneratedValue
    private Long id;

    private String token;
    @Enumerated(value = EnumType.STRING)
    private TokenType tokenType;

    private boolean expired;

    private boolean revoked;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
