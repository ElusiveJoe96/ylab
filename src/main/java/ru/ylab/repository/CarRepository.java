package ru.ylab.repository;

import ru.ylab.config.DatabaseConfig;
import ru.ylab.domain.model.Car;
import ru.ylab.domain.enums.CarStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarRepository {
    private final DatabaseConfig databaseConfig;

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
        String query = "INSERT INTO car_shop_schema.cars (brand, model, year, price, status, description) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, car.getBrand());
            statement.setString(2, car.getModel());
            statement.setInt(3, car.getYear());
            statement.setDouble(4, car.getPrice());
            statement.setString(5, car.getStatus().name());
            statement.setString(6, car.getDescription());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        car.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting car: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void update(Car car) {
        String query = "UPDATE car_shop_schema.cars " +
                "SET brand = ?, model = ?, year = ?, price = ?, status = ?, description = ? WHERE id = ?";

        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, car.getBrand());
            statement.setString(2, car.getModel());
            statement.setInt(3, car.getYear());
            statement.setDouble(4, car.getPrice());
            statement.setString(5, car.getStatus().name());
            statement.setString(6, car.getDescription());
            statement.setInt(7, car.getId());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("No car found with ID: " + car.getId());
            }
        } catch (SQLException e) {
            System.err.println("Error updating car: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void delete(int carId) {
        String query = "DELETE FROM car_shop_schema.cars WHERE id = ?";

        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, carId);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("No car found with ID: " + carId);
            }
        } catch (SQLException e) {
            System.err.println("Error deleting car: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Car findById(int carId) {
        String query = "SELECT id, brand, model, year, price, status, description " +
                "FROM car_shop_schema.cars WHERE id = ?";

        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, carId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToCar(resultSet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding car by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<Car> findAll() {
        String query = "SELECT id, brand, model, year, price, status, description " +
                "FROM car_shop_schema.cars";
        List<Car> cars = new ArrayList<>();

        try (Connection connection = databaseConfig.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                cars.add(mapRowToCar(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all cars: " + e.getMessage());
            e.printStackTrace();
        }
        return cars;
    }

    private List<Car> findByField(String fieldName, Object value) {
        String query = "SELECT id, brand, model, year, price, status, description " +
                "FROM car_shop_schema.cars WHERE " + fieldName + " = ?";

        List<Car> cars = new ArrayList<>();

        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

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
            System.err.println("Error finding cars by field: " + e.getMessage());
            e.printStackTrace();
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
