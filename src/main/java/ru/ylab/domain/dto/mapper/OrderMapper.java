package ru.ylab.domain.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.ylab.domain.dto.OrderDTO;
import ru.ylab.domain.model.Order;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderDTO toDTO(Order order);

    Order toEntity(OrderDTO orderDTO);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(OrderDTO orderDTO, @MappingTarget Order order);
}
