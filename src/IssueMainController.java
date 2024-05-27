import its.model.LoginResponse;
import its.model.ProjectRequest;
import its.model.ProjectResponse;
import its.model.UserResponse;

import javax.swing.*;

public class IssueMainController extends JFrame {

    private JLabel testLabel;
    private JPanel IssueMainPanel;

    private LoginResponse userInfo;
    private ProjectResponse projectInfo;

    public IssueMainController(LoginResponse userInfo, ProjectResponse projectInfo) {
        setContentPane(IssueMainPanel);
        initSettings();

        this.userInfo = userInfo;
        this.projectInfo = projectInfo;

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
