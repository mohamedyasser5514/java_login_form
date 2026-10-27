import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

class User {

    // Fields
    private String username;
    private String password;
    private String role;

    User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}

class WelcomePage extends Frame {

    WelcomePage(User user) {
        super("Welcome");

        setLayout(new GridLayout(2, 1));

        add(new Label("Hello, " + user.getUsername(), Label.CENTER));
        add(new Label("Role: " + user.getRole(), Label.CENTER));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setSize(300, 150);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

public class LoginFormTask extends Frame {

    private static final int MAX_ATTEMPTS = 3;

    private List<User> users = new ArrayList<>();
    private int failedAttempts = 0;

    public LoginFormTask() {
        super("Login");
        loadUsers();

        setLayout(new GridBagLayout());
        Panel content = new Panel(new BorderLayout(10, 15));

        Label title = new Label("Welcome back", Label.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        content.add(title, BorderLayout.NORTH);

        Panel form = new Panel(new GridLayout(3, 2, 8, 8));
        form.add(new Label("Username:", Label.RIGHT));
        TextField username = new TextField(18);
        form.add(username);

        form.add(new Label("Password:", Label.RIGHT));
        TextField password = new TextField(18);
        password.setEchoChar('*');
        form.add(password);

        form.add(new Label("Role:", Label.RIGHT));
        Choice role = new Choice();
        role.add("Student");
        role.add("TA");
        role.add("Professor");
        form.add(role);

        content.add(form, BorderLayout.CENTER);

        Panel buttons = new Panel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        Button cancel = new Button("Cancel");
        Button login = new Button("Login");
        buttons.add(cancel);
        buttons.add(login);
        content.add(buttons, BorderLayout.SOUTH);

        add(content);

        login.addActionListener(e -> {
            String name = username.getText().trim();
            String pass = password.getText();
            String selectedRole = role.getSelectedItem();

            if (name.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please fill in all fields!"
                );
                return;
            }

            User user = findUser(name);

            if (user != null
                    && user.getPassword().equals(pass)
                    && user.getRole().equals(selectedRole)) {

                new WelcomePage(user);
                dispose();

            } else {
                failedAttempts++;

                if (failedAttempts >= MAX_ATTEMPTS) {
                    login.setEnabled(false);
                    JOptionPane.showMessageDialog(
                            this,
                            "Too many failed attempts. Login is now disabled."
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Invalid username, password, or role!\n"
                            + "Attempts left: " + (MAX_ATTEMPTS - failedAttempts)
                    );
                }
            }
        });

        cancel.addActionListener(e -> {
            dispose();
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setSize(420, 340);
        setLocationRelativeTo(null);
    }

    private void loadUsers() {
        users.add(new User("Ahmed", "1234", "Student"));
        users.add(new User("Ali", "5678", "TA"));
        users.add(new User("Mohamed", "9999", "Professor"));
    }

    private User findUser(String name) {
        for (User user : users) {
            if (user.getUsername().equals(name)) {
                return user;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        new LoginFormTask().setVisible(true);
    }
}