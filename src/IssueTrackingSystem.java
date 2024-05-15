import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class IssueTrackingSystem extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);
    private JPanel landingPanel = new JPanel();
    private JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    private JPanel signupPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    private JPanel projectPanel = new JPanel(new BorderLayout());
    private JPanel newProjectPanel = new JPanel(new GridLayout(4, 2, 10, 10));  // Increased to 4 rows for layout
    private JScrollPane scrollPane;
    private JPanel memberSelectionPanel;

    private JTextField idFieldLogin = new JTextField(10);
    private JPasswordField pwFieldLogin = new JPasswordField(10);
    private JTextField idFieldSignup = new JTextField(10);
    private JPasswordField pwFieldSignup = new JPasswordField(10);
    private JComboBox<String> roleBox = new JComboBox<>(new String[]{"Admin", "Dev", "PL", "Tester"});
    private DefaultListModel<String> projectListModel = new DefaultListModel<>();
    private JTextField projectNameField = new JTextField(10);
    private DatabaseManager dbManager;

    public IssueTrackingSystem() {
        setTitle("Issue Tracking System");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        dbManager = new DatabaseManager(); // Initialize the database manager

        setupLandingPanel();
        setupLoginPanel();
        setupSignupPanel();
        setupProjectPanel();
        setupNewProjectPanel();

        cardPanel.add(landingPanel, "Landing");
        cardPanel.add(loginPanel, "로그인");
        cardPanel.add(signupPanel, "회원가입");
        cardPanel.add(projectPanel, "Projects");
        cardPanel.add(newProjectPanel, "New Project");

        add(cardPanel);
        cardLayout.show(cardPanel, "Landing");
    }

    private void setupLandingPanel() {
        landingPanel.setLayout(new BoxLayout(landingPanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel("Issue Tracking System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton loginButton = new JButton("로그인");
        JButton signupButton = new JButton("회원가입");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signupButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginButton.addActionListener(e -> cardLayout.show(cardPanel, "로그인"));
        signupButton.addActionListener(e -> cardLayout.show(cardPanel, "회원가입"));

        landingPanel.add(Box.createVerticalStrut(30));
        landingPanel.add(titleLabel);
        landingPanel.add(Box.createVerticalStrut(20));
        landingPanel.add(loginButton);
        landingPanel.add(Box.createVerticalStrut(10));
        landingPanel.add(signupButton);
    }

    private void setupLoginPanel() {
        JButton loginButton = new JButton("로그인");
        JButton backButton = new JButton("뒤로 가기");

        loginPanel.add(new JLabel("ID:"));
        loginPanel.add(idFieldLogin);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(pwFieldLogin);
        loginPanel.add(loginButton);
        loginPanel.add(backButton);

        loginButton.addActionListener(e -> {
            String id = idFieldLogin.getText();
            String password = new String(pwFieldLogin.getPassword());
            if (dbManager.verifyUser(id, password)) {
                cardLayout.show(cardPanel, "Projects");
            } else {
                JOptionPane.showMessageDialog(this, "Login failed. Incorrect ID or password.");
            }
        });
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Landing"));
    }

    private void setupSignupPanel() {
        JButton signupButton = new JButton("회원가입");
        JButton backButton = new JButton("뒤로 가기");

        signupPanel.add(new JLabel("ID:"));
        signupPanel.add(idFieldSignup);
        signupPanel.add(new JLabel("Password:"));
        signupPanel.add(pwFieldSignup);
        signupPanel.add(new JLabel("Role:"));
        signupPanel.add(roleBox);
        signupPanel.add(signupButton);
        signupPanel.add(backButton);

        signupButton.addActionListener(e -> {
            String id = idFieldSignup.getText();
            String password = new String(pwFieldSignup.getPassword());
            String role = (String) roleBox.getSelectedItem();
            if (dbManager.userExists(id)) {
                JOptionPane.showMessageDialog(this, "Error: This ID already exists.");
            } else {
                if (dbManager.addUser(id, password, role)) {
                    JOptionPane.showMessageDialog(this, "Signed up successfully with ID: " + id);
                    clearSignupFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Error: Could not create user.");
                }
            }
        });
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Landing"));
    }

    private void setupProjectPanel() {
        JLabel infoLabel = new JLabel("Welcome to the Projects Page!");
        JButton newProjectButton = new JButton("New Project");
        JButton logoutButton = new JButton("로그아웃");

        projectPanel.add(infoLabel, BorderLayout.NORTH);
        projectPanel.add(new JScrollPane(new JList<>(projectListModel)), BorderLayout.CENTER);
        JPanel southPanel = new JPanel(new FlowLayout());
        southPanel.add(newProjectButton);
        southPanel.add(logoutButton);
        projectPanel.add(southPanel, BorderLayout.SOUTH);

        newProjectButton.addActionListener(e -> {
            updateMemberList();  // Update member list whenever the panel is about to be shown
            cardLayout.show(cardPanel, "New Project");
        });
        logoutButton.addActionListener(e -> cardLayout.show(cardPanel, "Landing"));
    }

    private void setupNewProjectPanel() {
        JLabel nameLabel = new JLabel("Project Name:");
        JLabel membersLabel = new JLabel("Select Members:");
        projectNameField = new JTextField(10);
        memberSelectionPanel = new JPanel();
        memberSelectionPanel.setLayout(new BoxLayout(memberSelectionPanel, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(memberSelectionPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JButton createButton = new JButton("Create");
        JButton cancelButton = new JButton("Cancel");

        newProjectPanel.add(nameLabel);
        newProjectPanel.add(projectNameField);
        newProjectPanel.add(membersLabel);
        newProjectPanel.add(scrollPane);
        newProjectPanel.add(createButton);
        newProjectPanel.add(cancelButton);

        createButton.addActionListener(e -> {
            String projectName = projectNameField.getText();
            if (!projectName.isEmpty()) {
                StringBuilder members = new StringBuilder();
                for (Component comp : memberSelectionPanel.getComponents()) {
                    if (comp instanceof JCheckBox && ((JCheckBox) comp).isSelected()) {
                        members.append(((JCheckBox) comp).getText()).append(", ");
                    }
                }
                if (members.length() > 0) {
                    members.setLength(members.length() - 2);  // Remove trailing comma and space
                }
                projectListModel.addElement(projectName + " - Members: " + members.toString());
                projectNameField.setText("");
                clearMemberSelections();
                cardLayout.show(cardPanel, "Projects");
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a project name.");
            }
        });
        cancelButton.addActionListener(e -> cardLayout.show(cardPanel, "Projects"));
    }

    private void updateMemberList() {
        memberSelectionPanel.removeAll();  // Clear previous entries
        try {
            ResultSet rs = dbManager.getAllUsers();
            while (rs.next()) {
                String id = rs.getString("username");
                String role = rs.getString("role");
                JCheckBox checkBox = new JCheckBox(id + " - " + role);
                memberSelectionPanel.add(checkBox);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        memberSelectionPanel.revalidate();
        memberSelectionPanel.repaint();
    }

    private void clearLoginFields() {
        idFieldLogin.setText("");
        pwFieldLogin.setText("");
    }

    private void clearSignupFields() {
        idFieldSignup.setText("");
        pwFieldSignup.setText("");
        roleBox.setSelectedIndex(0);
    }

    private void clearMemberSelections() {
        for (Component comp : memberSelectionPanel.getComponents()) {
            if (comp instanceof JCheckBox) {
                ((JCheckBox) comp).setSelected(false);
            }
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new IssueTrackingSystem().setVisible(true));
    }
}

class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:userdata.db";

    public DatabaseManager() {
        try {
            Class.forName("org.sqlite.JDBC");
            createDatabase();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void createDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL," +
                    "role TEXT NOT NULL)";
            stmt.executeUpdate(sql);
            System.out.println("Database created successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean addUser(String username, String password, String role) {
        String sql = "INSERT INTO users(username, password, role) VALUES(?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean verifyUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean userExists(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet getAllUsers() throws SQLException {
        String sql = "SELECT username, role FROM users";
        Connection conn = DriverManager.getConnection(DB_URL);
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(sql);
    }
}
