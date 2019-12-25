package sample.model;

public class User {
    public Integer id;
    public String username;
    public String passwordHex;
    public UserType type;

    @Override
    public String toString() {
        return username + " : " + type;
    }

    public User(Integer id, String username, String passwordHex, UserType type) {
        this.id = id;
        this.username = username;
        this.passwordHex = passwordHex;
        this.type = type;
    }
}
