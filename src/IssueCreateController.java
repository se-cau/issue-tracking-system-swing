import its.model.LoginResponse;
import its.model.ProjectResponse;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IssueCreateController extends JFrame{
    private JTextField issueTitleTextField;
    private JComboBox comboBox1;
    private JTextArea descriptionTextArea;
    private JButton createIssueButton;
    private JButton goBackToIssueListButton;
    private JPanel IssueCreatePanel;
    private JLabel titleLabel;
    private JLabel reporterNameLabel;

    private LoginResponse userInfo;
    private ProjectResponse projectInfo;

    public IssueCreateController(LoginResponse userInfo, ProjectResponse projectInfo) {
        setContentPane(IssueCreatePanel);
        this.userInfo = userInfo;
        this.projectInfo = projectInfo;

        initSettings();
        initializeComponents();
        setVisible(true);
    }

    private void initSettings() {
        setTitle("Issue Tracking System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 700);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void initializeComponents() {
        configureTitleLabel();
        goBackToIssueListButton.addActionListener(goBackToIssueListButtonListener);
    }

    private void configureTitleLabel() {
        titleLabel.setFont(titleLabel.getFont().deriveFont(titleLabel.getFont().getSize() * 2.0f));
    }

    ActionListener goBackToIssueListButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            new IssueMainController(userInfo, projectInfo);
            dispose();
        }
    };

}
