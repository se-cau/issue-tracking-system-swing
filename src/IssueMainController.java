import its.model.LoginResponse;
import its.model.ProjectResponse;

import javax.swing.*;

public class IssueMainController extends JFrame {

    private JPanel IssueMainPanel;
    private JButton createNewIssueButton;
    private JButton fetchIssuesButton;
    private JLabel projectTitleLabel;
    private JTable table1;
    private JButton moveToSelectedIssueButton;
    private JButton logoutButton;
    private JButton goBackButton;
    private JLabel issueTitleLabel;
    private JLabel reporterNameLabel;
    private JLabel reportedDateLabel;
    private JTextArea textArea1;

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

    public static void main(String[] args) {
        new IssueMainController(new LoginResponse(24, "msl3", "Admin"), new ProjectResponse());
    }
}
