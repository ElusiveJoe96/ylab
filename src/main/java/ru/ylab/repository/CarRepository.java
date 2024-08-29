package ru.ylab.repository;

import org.springframework.stereotype.Repository;
import ru.ylab.domain.model.Car;
import ru.ylab.domain.enums.CarStatus;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CarRepository {
    private final DataSource dataSource;

    private static final String INSERT_CAR_QUERY = "INSERT INTO car_shop_schema.cars " +
            "(brand, model, year, price, status, description) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_CAR_QUERY = "UPDATE car_shop_schema.cars " +
            "SET brand = ?, model = ?, year = ?, price = ?, status = ?, description = ? " +
            "WHERE id = ?";
    private static final String DELETE_CAR_QUERY = "DELETE FROM car_shop_schema.cars WHERE id = ?";
    private static final String FIND_BY_ID_QUERY = "SELECT id, brand, model, year, price, status, description " +
            "FROM car_shop_schema.cars WHERE id = ?";
    private static final String FIND_BY_MODEL_AND_YEAR_QUERY = "SELECT id, brand, model, year, price, status, description " +
            "FROM car_shop_schema.cars WHERE model = ? AND year = ?";
    private static final String FIND_ALL_QUERY = "SELECT id, brand, model, year, price, status, description " +
            "FROM car_shop_schema.cars";

    public CarRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(Car car) {
        if (car.getId() > 0) {
            update(car);
        } else {
            insert(car);
        }
    }

    private void setCarParameters(PreparedStatement statement, Car car, boolean isUpdate) throws SQLException {
        statement.setString(1, car.getBrand());
        statement.setString(2, car.getModel());
        statement.setInt(3, car.getYear());
        statement.setDouble(4, car.getPrice());
        statement.setString(5, car.getStatus().name());
        statement.setString(6, car.getDescription());

        if (isUpdate) {
            statement.setInt(7, car.getId());
        }
    }

    private void insert(Car car) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_CAR_QUERY,
                     Statement.RETURN_GENERATED_KEYS)) {

            setCarParameters(statement, car, false);

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    car.setId(generatedKeys.getInt(1));
                }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting car", e);
        }
    }

    private void update(Car car) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_CAR_QUERY)) {

            setCarParameters(statement, car, true);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating car", e);
        }
    }

    public void delete(int carId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_CAR_QUERY)) {

            statement.setInt(1, carId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting car", e);
        }
    }

    public Optional<Car> findById(int carId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {

            statement.setInt(1, carId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapRowToCar(resultSet));
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding car by ID", e);
        }
    }

    public Optional<Car> findByModelAndYear(String model, int year) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_MODEL_AND_YEAR_QUERY)) {

            statement.setString(1, model);
            statement.setInt(2, year);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapRowToCar(resultSet));
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding car by model and year", e);
        }
    }

    public List<Car> findAll() {
        List<Car> cars = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
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
