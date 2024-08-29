package ru.ylab.domain.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.ylab.domain.dto.OrderDTO;
import ru.ylab.domain.model.Order;

/**
 * Mapper interface for converting between {@link Order} and {@link OrderDTO}.
 * <p>
 * This interface uses MapStruct to handle the mapping between the {@link Order} entity
 * and the {@link OrderDTO} data transfer object. The {@link OrderMapper} provides methods to
 * convert an {@link Order} instance to {@link OrderDTO} and vice versa. It also provides
 * a method to update an existing {@link Order} entity with data from an {@link OrderDTO}.
 * </p>
 *
 * <p>
 * The {@link OrderMapper} instance can be accessed via {@link #INSTANCE}.
 * </p>
 *
 * @see Order
 * @see OrderDTO
 */
@Mapper
public interface OrderMapper {

    /**
     * Singleton instance of {@link OrderMapper}.
     */
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    /**
     * Converts an {@link Order} entity to an {@link OrderDTO}.
     *
     * @param order the {@link Order} entity to convert
     * @return the corresponding {@link OrderDTO} data transfer object
     */
    OrderDTO toDTO(Order order);

    /**
     * Converts an {@link OrderDTO} to an {@link Order} entity.
     *
     * @param orderDTO the {@link OrderDTO} to convert
     * @return the corresponding {@link Order} entity
     */
    @Mapping(target = "id", ignore = true)
    Order toEntity(OrderDTO orderDTO);

    /**
     * Updates the provided {@link Order} entity with data from the given {@link OrderDTO}.
     * <p>
     * The {@link OrderDTO} properties will overwrite the existing properties in the {@link Order}
     * entity except for the {@code id} field, which will be ignored during the update.
     * </p>
     *
     * @param orderDTO the {@link OrderDTO} with updated data
     * @param order the {@link Order} entity to update
     */
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(OrderDTO orderDTO, @MappingTarget Order order);
}