package ru.ylab.domain.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.ylab.domain.dto.UserDTO;
import ru.ylab.domain.model.User;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO toDTO(User user);

    User toEntity(UserDTO userDTO);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(UserDTO userDTO, @MappingTarget User user);
}
