package ru.ylab.domain.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.ylab.domain.dto.UserDTO;
import ru.ylab.domain.model.User;

/**
 * Mapper interface for converting between {@link User} and {@link UserDTO}.
 * <p>
 * This interface uses MapStruct to handle the mapping between the {@link User} entity
 * and the {@link UserDTO} data transfer object. The {@link UserMapper} provides methods to
 * convert a {@link User} instance to {@link UserDTO} and vice versa. It also provides
 * a method to update an existing {@link User} entity with data from a {@link UserDTO}.
 * </p>
 *
 * <p>
 * The {@link UserMapper} instance can be accessed via {@link #INSTANCE}.
 * </p>
 *
 * @see User
 * @see UserDTO
 */
@Mapper
public interface UserMapper {

    /**
     * Singleton instance of {@link UserMapper}.
     */
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * Converts a {@link User} entity to a {@link UserDTO}.
     *
     * @param user the {@link User} entity to convert
     * @return the corresponding {@link UserDTO} data transfer object
     */
    @Mapping(target = "token", ignore = true)
    UserDTO toDTO(User user);

    /**
     * Converts a {@link UserDTO} to a {@link User} entity.
     *
     * @param userDTO the {@link UserDTO} to convert
     * @return the corresponding {@link User} entity
     */
    @Mapping(target = "id", ignore = true)
    User toEntity(UserDTO userDTO);

    /**
     * Updates the provided {@link User} entity with data from the given {@link UserDTO}.
     * <p>
     * The {@link UserDTO} properties will overwrite the existing properties in the {@link User}
     * entity except for the {@code id} field, which will be ignored during the update.
     * </p>
     *
     * @param userDTO the {@link UserDTO} with updated data
     * @param user the {@link User} entity to update
     */
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(UserDTO userDTO, @MappingTarget User user);
}