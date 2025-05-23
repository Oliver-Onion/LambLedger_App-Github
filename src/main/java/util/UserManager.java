package util;

import model.User;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static final String USERS_FILE = "users.csv";

    public static boolean saveUser(User user) {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                User existingUser = parseUserFromCSV(line);
                users.add(existingUser);
            }
        } catch (IOException e) {
            // 如果文件不存在，则创建新文件
            if (e.getMessage().contains("No such file or directory")) {
                // 忽略，继续创建文件
            } else {
                e.printStackTrace();
                return false;
            }
        }

        // 更新用户信息
        boolean userFound = false;
        for (User existingUser : users) {
            if (existingUser.getId() == user.getId()) {
                existingUser.setUsername(user.getUsername());
                existingUser.setPassword(user.getPassword());
                existingUser.setEmail(user.getEmail());
                existingUser.setPhone(user.getPhone());
                userFound = true;
                break;
            }
        }

        // 如果用户是新用户，则添加到列表中
        if (!userFound) {
            user.setId(users.isEmpty() ? 1 : users.get(users.size() - 1).getId() + 1);
            users.add(user);
        }

        try (FileWriter writer = new FileWriter(USERS_FILE)) {
            for (User u : users) {
                writer.write(String.format("%d,%s,%s,%s,%s,%s,%s\n",
                        u.getId(),
                        u.getUsername(),
                        u.getPassword(),
                        u.getEmail(),
                        u.getPhone()));
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static User getUserById(int userId) {
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                User user = parseUserFromCSV(line);
                if (user.getId() == userId) {
                    return user;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static User parseUserFromCSV(String line) {
        String[] data = line.split(",");
        User user = new User();
        user.setId(Integer.parseInt(data[0]));
        user.setUsername(data[1]);
        user.setPassword(data[2]);
        user.setEmail(data[3]);
        user.setPhone(data[4]);
        return user;
    }
}