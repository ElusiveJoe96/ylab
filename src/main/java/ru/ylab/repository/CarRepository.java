package ru.ylab.repository;

import ru.ylab.config.DatabaseConfig;
import ru.ylab.domain.model.Car;
import ru.ylab.domain.enums.CarStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarRepository {
    private final DatabaseConfig databaseConfig;

    private static final String INSERT_CAR_QUERY = "INSERT INTO car_shop_schema.cars " +
            "(brand, model, year, price, status, description) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_CAR_QUERY = "UPDATE car_shop_schema.cars " +
            "SET brand = ?, model = ?, year = ?, price = ?, status = ?, description = ? WHERE id = ?";
    private static final String DELETE_CAR_QUERY = "DELETE FROM car_shop_schema.cars WHERE id = ?";
    private static final String FIND_BY_ID_QUERY = "SELECT id, brand, model, year, price, status, description " +
            "FROM car_shop_schema.cars WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT id, brand, model, year, price, status, description " +
            "FROM car_shop_schema.cars";
    private static final String FIND_BY_FIELD_QUERY = "SELECT id, brand, model, year, price, status, description " +
            "FROM car_shop_schema.cars WHERE %s = ?";

    public CarRepository(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    public void save(Car car) {
        if (car.getId() > 0) {
            update(car);
        } else {
            insert(car);
        }
    }

    private void insert(Car car) {
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_CAR_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, car.getBrand());
            statement.setString(2, car.getModel());
            statement.setInt(3, car.getYear());
            statement.setDouble(4, car.getPrice());
            statement.setString(5, car.getStatus().name());
            statement.setString(6, car.getDescription());

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        car.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting car", e);
        }
    }

    private void update(Car car) {
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_CAR_QUERY)) {

            statement.setString(1, car.getBrand());
            statement.setString(2, car.getModel());
            statement.setInt(3, car.getYear());
            statement.setDouble(4, car.getPrice());
            statement.setString(5, car.getStatus().name());
            statement.setString(6, car.getDescription());
            statement.setInt(7, car.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating car", e);
        }
    }

    public void delete(int carId) {
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_CAR_QUERY)) {

            statement.setInt(1, carId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting car", e);
        }
    }

    public Car findById(int carId) {
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {

            statement.setInt(1, carId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToCar(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding car by ID", e);
        }
        return null;
    }

    public List<Car> findAll() {
        List<Car> cars = new ArrayList<>();

        try (Connection connection = databaseConfig.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL_QUERY)) {

            while (resultSet.next()) {
                cars.add(mapRowToCar(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all cars", e);
        }
        return cars;
    }

    private List<Car> findByField(String fieldName, Object value) {
        List<Car> cars = new ArrayList<>();



        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(String.format(FIND_BY_FIELD_QUERY, fieldName))) {
            //TODO А почему не сделать switch с fieldName?
            if (value instanceof String) {
                statement.setString(1, (String) value);
            } else if (value instanceof Integer) {
                statement.setInt(1, (Integer) value);
            } else if (value instanceof Double) {
                statement.setDouble(1, (Double) value);
            } else if (value instanceof CarStatus) {
                statement.setString(1, ((CarStatus) value).name());
            } else {
                throw new IllegalArgumentException("Unsupported field type: " + value.getClass().getName());
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    cars.add(mapRowToCar(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding cars by field", e);
        }

        return cars;
    }

    public List<Car> findByBrand(String brand) {
        return findByField("brand", brand);
    }

    public List<Car> findByModel(String model) {
        return findByField("model", model);
    }

    public List<Car> findByYear(int year) {
        return findByField("year", year);
    }

    public List<Car> findByPrice(double price) {
        return findByField("price", price);
    }

    public List<Car> findByStatus(CarStatus status) {
        return findByField("status", status);
    }

    private Car mapRowToCar(ResultSet resultSet) throws SQLException {
        Car car = new Car();
        car.setId(resultSet.getInt("id"));
        car.setBrand(resultSet.getString("brand"));
        car.setModel(resultSet.getString("model"));
        car.setYear(resultSet.getInt("year"));
        car.setPrice(resultSet.getDouble("price"));
        car.setStatus(CarStatus.valueOf(resultSet.getString("status")));
        car.setDescription(resultSet.getString("description"));
        return car;
    }
}
