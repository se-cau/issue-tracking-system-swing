import its.model.*;
import its.network.NetworkManager;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AssigneeSelectionController extends JFrame{
    private JPanel AssigneeSelectionPanel;
    private JList devUserList;
    private JLabel selectedNameLabel;
    private JButton applyButton;
    private JButton goBackToIssueButton;
    private JLabel titleLabel;
    private JLabel suggestedUserLabel;
    private LoginResponse userInfo;
    private ProjectResponse projectInfo;
    private IssueResponse issueInfo;

    UserResponse candidate = null;

    List<UserResponse> devUsers = new ArrayList<UserResponse>();

    UserResponse selectedUser = null;

    public AssigneeSelectionController(LoginResponse userInfo, ProjectResponse projectInfo, IssueResponse issueInfo) {
        this.userInfo = userInfo;
        this.issueInfo = issueInfo;
        this.projectInfo = projectInfo;

        initSettings();
        fetchData();
        initializeComponents();

        setContentPane(AssigneeSelectionPanel);
        setVisible(true);
    }

    private void initSettings() {
        setTitle("Issue Tracking System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void fetchData() {
        try {
            candidate = NetworkManager.getCandidate(issueInfo.getId());
            devUsers = NetworkManager.getDevUsersByProjectId(projectInfo.getProjectId());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Fetch data 실패 \n확인 후 다시 시도해주세요" + e.getMessage());
        }
    }

    private void initializeComponents() {
        configureTitleLabel();
        setDataLists();
        devUserList.addListSelectionListener(devUserListSelectionListioner);
        applyButton.addActionListener(applyButtonListener);
        goBackToIssueButton.addActionListener(goBackToIssueButtonListener);
    }

    private void setDataLists() {
        if (candidate != null) {
            suggestedUserLabel.setText(candidate.getUsername());
        }

        if (devUsers != null) {
            DefaultListModel<String> listModel = new DefaultListModel<>();
            for (UserResponse devUser : devUsers) {
                listModel.addElement(devUser.getUsername());
            }
            devUserList.setModel(listModel);
        }

    }

    private void configureTitleLabel() {
        titleLabel.setFont(titleLabel.getFont().deriveFont(titleLabel.getFont().getSize() * 2.0f));
    }


    ListSelectionListener devUserListSelectionListioner = new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting())
                return;
            int index = devUserList.getSelectedIndex();
            selectedUser = devUsers.get(index);
            selectedNameLabel.setText(selectedUser.getUsername());
        }
    };

    ActionListener applyButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (selectedUser != null) {
                    IssueRequest issueRequest = new IssueRequest(issueInfo.getTitle(), issueInfo.getDescription(), selectedUser.getUserId(), issueInfo.getStatus(), issueInfo.getPriority(), userInfo.getUserId());
                    NetworkManager.applyAssignee(issueInfo.getId(), issueRequest);
                    dispose();
                    new IssueDetailController(userInfo, projectInfo, issueInfo);
                } else {
                    JOptionPane.showMessageDialog(null, "담당자를 선택하세요");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "배정하기를 실패했습니다 : \n" + ex.getMessage());
            }
        }
    };

    ActionListener goBackToIssueButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            new IssueDetailController(userInfo, projectInfo, issueInfo);
            dispose();
        }
    };

    public static void main(String[] args) {
        new AssigneeSelectionController(
                new LoginResponse(5, "민섭 테스트 PL", "PL"),
                new ProjectResponse(1, "민섭 Admin Project 1", "민섭 테스트 Admin", new ArrayList<>()),
                new IssueResponse(1, "이슈 첫번째", "첫번째 이유입니다", "민섭 테스트 Tester", null, null, Status.NEW, Priority.BLOCKER, "2024-05-28T12:30:03.34454", null));
    }

}
