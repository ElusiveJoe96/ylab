package ru.ylab.domain.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.ylab.domain.dto.CarDTO;
import ru.ylab.domain.model.Car;

@Mapper
public interface CarMapper {
    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

    CarDTO toDTO(Car car);

    Car toEntity(CarDTO carDTO);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(CarDTO carDTO, @MappingTarget Car car);
}
