import its.model.*;
import its.network.NetworkManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;

public class IssueDetailController extends JFrame{
    private JPanel issueDetailPane;
    private JLabel titleLabel;
    private JButton goBackButton;
    private JButton logoutButton;
    private JTextArea descriptionTextArea;
    private JButton addCommentButton;
    private JTable commentsTable;
    private JLabel stateLabel;
    private JLabel priorityLabel;
    private JLabel reporterLabel;
    private JLabel createdDateLabel;
    private JLabel assigneeLabel;
    private JLabel fixerLabel;
    private JPanel buttonsPanel;
    private JLabel userNameLabel;
    private JTextArea commentTextArea;

    private ProjectResponse projectInfo;
    private LoginResponse userInfo;
    private IssueResponse issueInfo;
    private List<CommentResponse> comments = new ArrayList<CommentResponse>();
    private final String[] tableColumnNames = {"Name", "Role", "Message"};
    private DefaultTableModel model = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public IssueDetailController(LoginResponse userInfo, ProjectResponse projectInfo, IssueResponse issueInfo) {
        this.userInfo = userInfo;
        this.issueInfo = issueInfo;
        this.projectInfo = projectInfo;
        commentsTable.setModel(model);
        commentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        model.setColumnIdentifiers(tableColumnNames);
        descriptionTextArea.setEditable(false);


        initSettings();
        fetchData();
        initializeComponents();

        setContentPane(issueDetailPane);
        setVisible(true);
        commentTextArea.setForeground(new Color(153, 153, 153));
        commentTextArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (commentTextArea.getText().equals("댓글을 입력하세요")) {
                    commentTextArea.setText("");
                    commentTextArea.setForeground(new Color(0, 0, 0));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (commentTextArea.getText().equals("")) {
                    commentTextArea.setText("댓글을 입력하세요");
                    commentTextArea.setForeground(new Color(153, 153, 153));
                }
            }
        });
    }
    private void initializeComponents() {
        configureTitleLabel();
        setIssueDetailLabels();
        setDataCommentsTable();

        logoutButton.addActionListener(logoutButtonListener);
        goBackButton.addActionListener(goBackButtonListener);
    }
    private void initSettings() {
        setTitle("Issue Tracking System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 700);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void fetchData() {
        try {
            issueInfo = NetworkManager.getIssueDetailByIssueId(issueInfo.getId());
            comments = NetworkManager.getCommentsByIssueId(issueInfo.getId());
        } catch (Exception e) {
            //TODO fetch 못한 경우 오류 처리
            e.printStackTrace();
        }
    }

    private void configureTitleLabel() {
        titleLabel.setFont(titleLabel.getFont().deriveFont(titleLabel.getFont().getSize() * 2.0f));
        userNameLabel.setText(userInfo.getUsername());
    }

    private void setIssueDetailLabels() {
        stateLabel.setText(issueInfo.getStatus().toString());
        priorityLabel.setText(issueInfo.getPriority().toString());
        reporterLabel.setText(issueInfo.getReporter());
        createdDateLabel.setText(issueInfo.getCreatedAt());
        descriptionTextArea.setText(issueInfo.getDescription());
        String assignee = issueInfo.getAssignee();
        if (assignee == null) {
            assignee = "-";
        }
        assigneeLabel.setText(assignee);
        String fixer = issueInfo.getFixer();
        if (fixer == null) {
            fixer = "-";
        }
        fixerLabel.setText(fixer);
    }

    private void setDataCommentsTable() {
        int index = 0;
        for (CommentResponse comment : comments) {
            model.addRow(new Object[0]);
            model.setValueAt(comment.getUsername(), index, 0);
            model.setValueAt(comment.getRole(), index, 1);
            model.setValueAt(comment.getMessage(), index, 2);
            index++;
        }
    }

    ActionListener logoutButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            new LoginController();
            dispose();
        }
    };

    ActionListener goBackButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            new IssueMainController(userInfo, projectInfo);
            dispose();
        }
    };

    public static void main(String[] args) {
        new IssueDetailController(
                new LoginResponse(6, "민섭 테스트 Dev", "Dev"),
                new ProjectResponse(1, "민섭 Admin Project 1", "민섭 테스트 Admin", new ArrayList<>()),
                new IssueResponse(1, "이슈 첫번째", "첫번째 이유입니다", "민섭 테스트 Tester", null, null, Status.NEW, Priority.BLOCKER, "2024-05-28T12:30:03.34454", null));
    }
}
