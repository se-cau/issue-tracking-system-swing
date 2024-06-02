import its.model.*;
import its.network.NetworkManager;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
    private JLabel userNameLabel;
    private JTextArea commentTextArea;
    private JButton editStateButton;
    private JTextArea commentMessageTextArea;
    private JLabel selectedCommentUserNameLabel;
    private JLabel selectedCommentUserRoleLabel;
    private JLabel selectedCommentDateLabel;
    private JButton deleteButton;
    private JScrollPane mainScorllPane;
    private JButton changeStateButton;

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
        commentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainScorllPane.getVerticalScrollBar().setValue(0);
            }
        });
    }
    private void initializeComponents() {
        configureTitleLabel();
        setIssueDetailLabels();
        setDataCommentsTable();
        setEditStateButton();
        deleteButton.setVisible(false);
        editStateButton.addActionListener(editStateButtonListener);
        logoutButton.addActionListener(logoutButtonListener);
        goBackButton.addActionListener(goBackButtonListener);
        addCommentButton.addActionListener(addCommentButtonListener);
        commentsTable.getSelectionModel().addListSelectionListener(commentsTableSelectionListioner);
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
        titleLabel.setText(issueInfo.getTitle());
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

    private void setEditStateButton() {
        Status status = issueInfo.getStatus();
        String userRole = userInfo.getRole();
        String assignee = issueInfo.getAssignee();
        String username = userInfo.getUsername();

        if (status == Status.ASSIGNED && username.equals(assignee)) {
            editStateButton.setText("Change to Fixed");
        } else if (status == Status.FIXED && userRole.equals("Tester")) {
            editStateButton.setText("Change to Resolved");

        } else if (userRole.equals("PL")) {
            if (status == Status.NEW) {
                editStateButton.setText("Assignee 배정하기");

            } else if (status == Status.RESOLVED || status == Status.REOPENED) {
                editStateButton.setText("Close the Issue");

            } else if (status == Status.CLOSE) {
                editStateButton.setText("Reopen the Issue");

            } else {
                editStateButton.setVisible(false);
                return;
            }
        } else {
            editStateButton.setVisible(false);
            return;
        }

        editStateButton.setVisible(true);
    }

    private void setDataCommentsTable() {
        model.setColumnIdentifiers(tableColumnNames);
        int index = 0;
        model.setRowCount(0);
        for (CommentResponse comment : comments) {
            model.addRow(new Object[0]);
            model.setValueAt(comment.getUsername(), index, 0);
            model.setValueAt(comment.getRole(), index, 1);
            model.setValueAt(comment.getMessage(), index, 2);
            index++;
        }
        commentsTable.setModel(model);
    }

    private void updateSelectedCommentLabels() {
        int selectedIndex = commentsTable.getSelectedRow();
        if (selectedIndex != -1) {
            CommentResponse selectedComment = comments.get(selectedIndex);
            // TODO 삭제 버튼 활성화
//            System.out.println(selectedComment.getId());
//            if (userInfo.getUserId() == selectedComment.getAuthorId()) {
//                deleteButton.setVisible(true);
//            } else {
//                deleteButton.setVisible(false);
//            }

            selectedCommentUserNameLabel.setText(selectedComment.getUsername());
            selectedCommentUserRoleLabel.setText(selectedComment.getRole());
            selectedCommentDateLabel.setText(selectedComment.getCreatedAt());
            commentMessageTextArea.setText(selectedComment.getMessage());
        } else {
            selectedCommentUserNameLabel.setText("-");
            selectedCommentUserRoleLabel.setText("-");
            selectedCommentDateLabel.setText("-");
            descriptionTextArea.setText("-");
        }
    }

    ListSelectionListener commentsTableSelectionListioner = new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting())
                return;
            updateSelectedCommentLabels();
        }
    };

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

    ActionListener addCommentButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (commentTextArea.getText().trim().isEmpty() || commentTextArea.getText().equals("댓글을 입력하세요")) {
                JOptionPane.showMessageDialog(null, "댓글을 올바르게 입력하세요");
                return;
            }

            String message = commentTextArea.getText();
            CommentRequest comment = new CommentRequest(message, userInfo.getUserId());

            try {
                comments = NetworkManager.postComment(comment, issueInfo.getId());
                setDataCommentsTable();
                commentTextArea.setText("댓글을 입력하세요");
                commentTextArea.setForeground(new Color(153, 153, 153));
                commentMessageTextArea.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "댓글 달기를 실패했습니다 : \n" + ex.getMessage());
            }
        }
    };

    ActionListener editStateButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Status status = issueInfo.getStatus();
            String userRole = userInfo.getRole();
            String assignee = issueInfo.getAssignee();
            String username = userInfo.getUsername();
            IssueRequest issueRequest = new IssueRequest(issueInfo.getTitle(), issueInfo.getDescription(), 0, issueInfo.getStatus(), issueInfo.getPriority(), userInfo.getUserId());

            if (status == Status.ASSIGNED && username.equals(assignee)) {
                try {
                    issueInfo = NetworkManager.updateIssueStatus(issueInfo.getId(), issueRequest);
                    setEditStateButton();
                    setIssueDetailLabels();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Change to Fixed 실패했습니다 \n 확인후 다시 시도하세요 \n Error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            } else if (status == Status.FIXED && userRole.equals("Tester")) {
                try {
                    issueInfo = NetworkManager.updateIssueStatus(issueInfo.getId(), issueRequest);
                    setEditStateButton();
                    setIssueDetailLabels();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Change to Resolved 실패했습니다 \n 확인후 다시 시도하세요 \n Error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            } else if (userRole.equals("PL")) {
                if (status == Status.NEW) {
                    new AssigneeSelectionController(userInfo, projectInfo, issueInfo);
                    dispose();
                } else if (status == Status.RESOLVED || status == Status.REOPENED) {
                    try {
                        issueInfo = NetworkManager.updateIssueStatus(issueInfo.getId(), issueRequest);
                        setEditStateButton();
                        setIssueDetailLabels();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Close the Issue 실패했습니다 \n 확인후 다시 시도하세요 \n Error: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                } else if (status == Status.CLOSE) {
                    try {
                        issueInfo = NetworkManager.updateIssueStatus(issueInfo.getId(), issueRequest);
                        setEditStateButton();
                        setIssueDetailLabels();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Reopen the Issue 실패했습니다 \n 확인후 다시 시도하세요 \n Error: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    };


    ActionListener deleteButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = commentsTable.getSelectedRow();
            if (selectedIndex != -1) {
                CommentResponse selectedComment = comments.get(selectedIndex);
                CommentRequest commentRequest = new CommentRequest(selectedComment.getMessage(), userInfo.getUserId());
                try {
                    comments = NetworkManager.deleteComment(selectedComment.getId(), commentRequest);
                    setDataCommentsTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "댓글 삭제 실패했습니다 \n 확인후 다시 시도하세요 \n Error: " + ex.getMessage());
                }
            }
        }
    };
    public static void main(String[] args) {
        new IssueDetailController(
                new LoginResponse(5, "민섭 테스트 PL", "PL"),
                new ProjectResponse(1, "민섭 Admin Project 1", "민섭 테스트 Admin", new ArrayList<>()),
                new IssueResponse(1, "이슈 첫번째", "첫번째 이유입니다", "민섭 테스트 Tester", null, null, Status.NEW, Priority.BLOCKER, "2024-05-28T12:30:03.34454", null));
    }
}
