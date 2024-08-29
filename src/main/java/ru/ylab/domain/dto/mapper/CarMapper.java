package ru.ylab.domain.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.ylab.domain.dto.CarDTO;
import ru.ylab.domain.model.Car;

/**
 * Mapper interface for converting between {@link Car} and {@link CarDTO}.
 * <p>
 * This interface uses MapStruct to handle the mapping between the {@link Car} entity
 * and the {@link CarDTO} data transfer object. The {@link CarMapper} provides methods to
 * convert a {@link Car} instance to {@link CarDTO} and vice versa. It also provides
 * a method to update an existing {@link Car} entity with data from a {@link CarDTO}.
 * </p>
 *
 * <p>
 * The {@link CarMapper} instance can be accessed via {@link #INSTANCE}.
 * </p>
 *
 * @see Car
 * @see CarDTO
 */
@Mapper
public interface CarMapper {

    /**
     * Singleton instance of {@link CarMapper}.
     */
    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

    /**
     * Converts a {@link Car} entity to a {@link CarDTO}.
     *
     * @param car the {@link Car} entity to convert
     * @return the corresponding {@link CarDTO} data transfer object
     */
    CarDTO toDTO(Car car);

    /**
     * Converts a {@link CarDTO} to a {@link Car} entity.
     *
     * @param carDTO the {@link CarDTO} to convert
     * @return the corresponding {@link Car} entity
     */
    @Mapping(target = "id", ignore = true)
    Car toEntity(CarDTO carDTO);

    /**
     * Updates the provided {@link Car} entity with data from the given {@link CarDTO}.
     * <p>
     * The {@link CarDTO} properties will overwrite the existing properties in the {@link Car}
     * entity except for the {@code id} field, which will be ignored during the update.
     * </p>
     *
     * @param carDTO the {@link CarDTO} with updated data
     * @param car the {@link Car} entity to update
     */
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(CarDTO carDTO, @MappingTarget Car car);
}