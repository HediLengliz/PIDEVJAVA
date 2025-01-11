package org.example.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.models.User;
import org.example.utils.CurrentUser;
import org.example.utils.EmailSender;
import org.example.utils.MyDatabase;
import org.apache.commons.lang3.RandomStringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserService implements org.example.services.IService<User> {

    public Connection getConnection() {
        return connection;
    }

    private Connection connection;

    public UserService() {
        connection = MyDatabase.getInstance().getConnection();
    }
//    private void saveProfilePicture(ImageView i) {
//        if (connection != null && i.getImage() != null) {
//            try {
//                String imagePath = i.getImage().ge; // Get the image path from the ImageView
//                // Update the profile picture path in the database
//                String updateQuery = "UPDATE user_profile SET profile_pic_path = ? WHERE user_id = ?";
//                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
//                preparedStatement.setString(1, imagePath);
//                preparedStatement.setInt(2, 1); // Set the user ID or use a parameter
//                int rowsUpdated = preparedStatement.executeUpdate();
//
//            } catch (SQLException e) {
//                e.printStackTrace();
//
//            }
//        }
//    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<String> getRolesFromResultSet(ResultSet rs) throws SQLException {
        try {
            String rolesJson = rs.getString("roles");
            String[] rolesArray = objectMapper.readValue(rolesJson, String[].class);
            return Arrays.asList(rolesArray);
        } catch (Exception e)
        {
            throw new SQLException("Error deserializing roles from JSON: " + e.getMessage());
        }
    }
    public User getUserByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String rolesString = resultSet.getString("roles");
                    List<String> rolesList = parseRolesString(rolesString);
                    String password = resultSet.getString("password");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String cin = resultSet.getString("cin");
                    String address = resultSet.getString("address");
                    String phoneNumber = resultSet.getString("phone_number");
                    String image = resultSet.getString("image_file_name");
                    String claims = resultSet.getString("claims");
                    return new User(id, email, rolesList, password, firstName, lastName, cin, address, phoneNumber,image,claims);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving user by email: " + e.getMessage());
        }
        return null; // User not found
    }

    private List<String> parseRolesString(String rolesString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(rolesString, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            System.out.println("Error parsing roles string: " + e.getMessage());
            return null;
        }
    }

    public void blockUserById(int userId) throws SQLException {
        // SQL query to update JSON column
        String sql = "UPDATE user SET claims = JSON_SET(COALESCE(claims, '{}'), '$.blocked', true) WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("User with ID " + userId + " not found.");
            }
        }
    }


    public void unblockUserById(int userId) throws SQLException {
        // SQL query to update JSON column
        String sql = "UPDATE user SET claims = JSON_SET(claims, '$.blocked', false) WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("User with ID " + userId + " not found.");
            }
        }
    }


    public boolean login(String email, String password) throws SQLException {
        String sql = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String hashedPassword = resultSet.getString("password");
                    // Check if the provided password matches the hashed password in the database
                    if (BCrypt.checkpw(password, hashedPassword)) {
                        return true; // Login successful
                    } else {
                        throw new SQLException("Incorrect password");
                    }
                } else {
                    throw new SQLException("User not found");
                }
            } catch (IllegalArgumentException e) {
                // Handle the "Invalid salt revision" error
                System.err.println("Error during login: Invalid salt revision");
                e.printStackTrace();
            }
        }
        return false; // Login failed
    }
    @Override
    public void add(User user) throws SQLException {

        if (user == null) {
            throw new IllegalArgumentException("User object is null.");
        }
        // Check if user with the given ID exists


        // Check if user with the same email exists (excluding the current user's ID)
        if (userExists(user.getEmail())) {
            throw new SQLException("User with email " + user.getEmail() + " already exists.");
        }

        // Check if user with the same CIN exists (excluding the current user's ID)
        if (userExistsWithCIN2(user.getCin())) {
            throw new SQLException("User with CIN " + user.getCin() + " already exists.");
        }

        // Check if user with the same phone number exists (excluding the current user's ID)
        if (userExistsWithPhoneNumber2(user.getPhoneNumber())) {
            throw new SQLException("User with phone number " + user.getPhoneNumber() + " already exists.");
        }

        String rolesJson;
        try {
            rolesJson = objectMapper.writeValueAsString(user.getRoles());
        } catch (Exception e) {
            throw new SQLException("Error serializing roles to JSON: " + e.getMessage());
        }
        String randomPassword = "123";
        String hashedPassword = BCrypt.hashpw(randomPassword, BCrypt.gensalt());
        String sql = "INSERT INTO user (email, roles, password, first_name, last_name, cin, address, phone_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, rolesJson);
            preparedStatement.setString(3, user.getPassword()); // Store the hashed password
            preparedStatement.setString(4, user.getFirstName());
            preparedStatement.setString(5, user.getLastName());
            preparedStatement.setString(6, user.getCin());
            preparedStatement.setString(7, user.getAddress());
            preparedStatement.setString(8, user.getPhoneNumber());
            preparedStatement.executeUpdate();
        }
    }

    private boolean userExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM user WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0;
                }
            }
        }
        return false;
    }

    public void register(User user) throws SQLException {
        String rolesJson;
        try {
            rolesJson = objectMapper.writeValueAsString(user.getRoles());
        } catch (Exception e) {
            throw new SQLException("Error serializing roles to JSON: " + e.getMessage());
        }
        String randomPassword = user.getPassword();
        String hashedPassword = BCrypt.hashpw(randomPassword, BCrypt.gensalt());
        String sql = "INSERT INTO user (email, roles, password, first_name, last_name, cin, address, phone_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, rolesJson);
            preparedStatement.setString(3, hashedPassword); // Store the hashed password
            preparedStatement.setString(4, user.getFirstName());
            preparedStatement.setString(5, user.getLastName());
            preparedStatement.setString(6, user.getCin());
            preparedStatement.setString(7, user.getAddress());
            preparedStatement.setString(8, user.getPhoneNumber());
            preparedStatement.executeUpdate();
        }
    }


















    private String generateRandomPassword() {
        // Generate a random alphanumeric password of length 8
        return RandomStringUtils.randomAlphanumeric(8);
    }

    @Override
    public void update(User user) throws SQLException {
        // Validate user ID
        if (user.getId() <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        // Check if user with the given ID exists
        if (!userExists(user.getId())) {
            throw new SQLException("User not found with ID: " + user.getId());
        }

        // Check if user with the same email exists (excluding the current user's ID)
        if (userExistsWithEmail(user.getEmail(), user.getId())) {
            throw new SQLException("User with email " + user.getEmail() + " already exists.");
        }

        // Check if user with the same CIN exists (excluding the current user's ID)
        if (userExistsWithCIN(user.getCin(), user.getId())) {
            throw new SQLException("User with CIN " + user.getCin() + " already exists.");
        }

        // Check if user with the same phone number exists (excluding the current user's ID)
        if (userExistsWithPhoneNumber(user.getPhoneNumber(), user.getId())) {
            throw new SQLException("User with phone number " + user.getPhoneNumber() + " already exists.");
        }

        String rolesJson;
        try {
            rolesJson = objectMapper.writeValueAsString(user.getRoles());
        } catch (Exception e) {
            throw new SQLException("Error serializing roles to JSON: " + e.getMessage());
        }

        // Update query
        String sql = "UPDATE user SET email = ?, roles = ?, first_name = ?, last_name = ?, cin = ?, address = ?, phone_number = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, rolesJson);

            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setString(5, user.getCin());
            preparedStatement.setString(6, user.getAddress());
            preparedStatement.setString(7, user.getPhoneNumber());
            preparedStatement.setInt(8, user.getId());
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("Failed to update user with ID: " + user.getId());
            }
        }
    }
    public void updatePasswordByEmail(String email) throws SQLException {
        // Validate email and password
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }


        // Check if user with the given email exists
        User user = getUserByEmail(email);
        if (user == null) {
            throw new SQLException("User not found with email: " + email);
        }
        String plainPassword =generateRandomPassword();
        String encryptedPassword =BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        ;

        // Update query
        String sql = "UPDATE user SET password = ? WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, encryptedPassword);
            preparedStatement.setString(2, email);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("Failed to update password for user with email: " + email);
            }
        }
        finally {
            String emailBody = "Reset Password \nPassword: "+ plainPassword;

            EmailSender.sendEmail(email,"Reset password",emailBody);        }
    }

    public void updatePassword(String currentPwd, String newPwd) throws SQLException {
        // Validate inputs (you can add more specific validations as needed)
        if (currentPwd == null || currentPwd.isEmpty() || newPwd == null || newPwd.isEmpty()) {
            throw new IllegalArgumentException("Invalid input: Passwords cannot be null or empty.");
        }

        // Check if user is logged in and get the current user
        User user = CurrentUser.getCurrentUser();
        if (user == null) {
            throw new IllegalStateException("User not logged in.");
        }

        // Check if current password matches the stored password
        if (!BCrypt.checkpw(currentPwd, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }

        // Encrypt the new password
        String encryptedPassword = BCrypt.hashpw(newPwd, BCrypt.gensalt());

        // Update the password in the database
        String sql = "UPDATE user SET password = ? WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, encryptedPassword);
            preparedStatement.setString(2, user.getEmail());
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("Failed to update password for user with email: " + user.getEmail());
            }
        }
    }


    private boolean userExists(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM user WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0;
                }
            }
        }
        return false;
    }

    private boolean userExistsWithEmail(String email, int excludeUserId) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM user WHERE email = ? AND id <> ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            preparedStatement.setInt(2, excludeUserId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0;
                }
            }
        }
        return false;
    }
    private boolean userExistsWithEmail2(String email) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM user WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0;
                }
            }
        }
        return false;
    }


    private boolean userExistsWithCIN(String cin, int excludeUserId) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM user WHERE cin = ? AND id <> ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, cin);
            preparedStatement.setInt(2, excludeUserId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0;
                }
            }
        }
        return false;
    }
    private boolean userExistsWithCIN2(String cin) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM user WHERE cin = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, cin);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0;
                }
            }
        }
        return false;
    }


    private boolean userExistsWithPhoneNumber(String phoneNumber, int excludeUserId) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM user WHERE phone_number = ? AND id <> ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, phoneNumber);
            preparedStatement.setInt(2, excludeUserId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0;
                }
            }
        }
        return false;
    }
    private boolean userExistsWithPhoneNumber2(String phoneNumber) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM user WHERE phone_number = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, phoneNumber);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0;
                }
            }
        }
        return false;
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "delete from user where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }


    @Override
    public List<User> getAll() throws SQLException {
        String sql = "SELECT * FROM user";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            User u = new User();
            u.setId(rs.getInt("id"));
            u.setEmail(rs.getString("email"));
            String[] rolesString = rs.getString("roles").split(",");
            String[] rolesArray = rolesString;
            List<String> rolesList = List.of(rolesArray);
            u.setRoles(rolesList);
            u.setPassword(rs.getString("password"));
            u.setFirstName(rs.getString("first_name"));
            u.setLastName(rs.getString("last_name"));
            u.setCin(rs.getString("cin"));
            u.setAddress(rs.getString("address"));
            u.setPhoneNumber(rs.getString("phone_number"));
            u.setClaims(rs.getString("claims"));
            users.add(u);
        }
        return users;
    }



    @Override
    public User getById(int idUser) throws SQLException {
        String sql = "SELECT `email`, `roles`, `password`, `first_name`, `last_name`, `cin`, `address`, `phone_number`, `image_file_name`, `claims` FROM `user` WHERE `id` = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idUser);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            String email = resultSet.getString("email");
            String[] roles = resultSet.getString("roles").split(","); // Assuming roles are comma-separated
            String password = resultSet.getString("password");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String cin = resultSet.getString("cin");
            String address = resultSet.getString("address");
            String phoneNumber = resultSet.getString("phone_number");
            String image=resultSet.getString("image_file_name");
            String claims=resultSet.getString("claims");
            return new User(idUser,email, List.of(roles), password, firstName, lastName, cin, address, phoneNumber,image,claims);
        } else {
            return null;
        }
    }

}