import its.model.*;
import its.network.NetworkManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class IssueCreateController extends JFrame{
    private JTextField issueTitleTextField;
    private JComboBox priorityComboBox;
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
        createIssueButton.addActionListener(createIssueButtonListener);
    }

    private void configureTitleLabel() {
        titleLabel.setFont(titleLabel.getFont().deriveFont(titleLabel.getFont().getSize() * 2.0f));
        reporterNameLabel.setText(userInfo.getUsername());
    }

    ActionListener goBackToIssueListButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            new IssueMainController(userInfo, projectInfo);
            dispose();
        }
    };

    ActionListener createIssueButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String title = issueTitleTextField.getText();
            Priority priority = Priority.forValue(priorityComboBox.getSelectedItem().toString());
            String description = descriptionTextArea.getText();

            if (title == null || title.trim().isEmpty() || description == null || description.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "이슈의 정보를 올바르게 작성하세요");
                return;
            }
            IssueRequest issueRequest = new IssueRequest(title, description, 0, Status.NEW, priority, userInfo.getUserId());

            try {
                NetworkManager.createIssuesByProjectId(issueRequest, projectInfo.getProjectId());
                JOptionPane.showMessageDialog(null, "이슈를 성공적으로 생성했습니다.");
                clearInputs();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
    };

    private void clearInputs() {
        issueTitleTextField.setText("");
        priorityComboBox.setSelectedIndex(0);
        descriptionTextArea.setText("");
    }
    public static void main(String[] args) {
        List<String> Object2 = new ArrayList<>();
        new IssueCreateController(new LoginResponse(4, "민섭 테스트 Tester", "Tester"), new ProjectResponse(1, "민섭 Admin Project 1", "민섭 테스트 Admin", Object2));
    }

}
