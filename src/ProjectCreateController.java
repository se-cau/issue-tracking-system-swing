import its.model.LoginResponse;

import javax.swing.*;

public class ProjectCreateController extends JFrame {
    private JPanel ProjectCreatePanel;
    private JLabel testLabel;

    private LoginResponse userInfo;

    public ProjectCreateController(LoginResponse loginResponse) {
        setContentPane(ProjectCreatePanel);

        initSettings();
        this.userInfo = loginResponse;

        setVisible(true);
    }

    private void initSettings() {
        setTitle("Issue Tracking System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 700);
        setResizable(false);
        setLocationRelativeTo(null);
    }
}
