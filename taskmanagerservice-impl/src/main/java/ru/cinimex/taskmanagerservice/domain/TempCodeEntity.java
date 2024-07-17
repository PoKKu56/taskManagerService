package ru.cinimex.taskmanagerservice.domain;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "temp_code")
@Setter
@Getter
@RequiredArgsConstructor
public class TempCodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    private String code;

    @Override
    public String toString() {
        return "TempCodeEntity{" +
                "id=" + id +
                ", user=" + user.getUsername() +
                ", code='" + code + '\'' +
                '}';
    }
}
