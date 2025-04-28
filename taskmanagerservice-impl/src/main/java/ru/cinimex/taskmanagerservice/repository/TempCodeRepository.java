package ru.cinimex.taskmanagerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cinimex.taskmanagerservice.domain.TempCodeEntity;
import ru.cinimex.taskmanagerservice.domain.UserEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TempCodeRepository extends JpaRepository<TempCodeEntity, UUID> {
    public Optional<TempCodeEntity> findByUser(UserEntity user);

    Optional<TempCodeEntity> findByUser(Optional<UserEntity> userEntity);
}
