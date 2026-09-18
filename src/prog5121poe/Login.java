package prog5121poe;

public class Login {

    private Registration registeredUser;

    public Login(Registration registeredUser) {
        this.registeredUser = registeredUser;
    }

    // ----- LOGIN CHECK -----
    // Verifies entered username and password against the registered ones
    public boolean loginUser(String username, String password) {
        return username.equals(registeredUser.getUsername())
                && password.equals(registeredUser.getPassword());
    }

    // ----- LOGIN MESSAGE -----
    // Returns an appropriate success or failure message
    public String returnLoginStatus(String username, String password) {
        if (loginUser(username, password)) {
            return "Welcome " + registeredUser.getUsername() + ", it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}