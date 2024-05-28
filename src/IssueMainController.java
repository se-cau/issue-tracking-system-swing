import its.model.IssueResponse;
import its.model.LoginResponse;
import its.model.ProjectResponse;
import its.network.NetworkManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class IssueMainController extends JFrame {

    private JPanel IssueMainPanel;
    private JButton createNewIssueButton;
    private JButton fetchIssuesButton;
    private JLabel projectTitleLabel;
    private JTable issuesTable;
    private JButton moveToSelectedIssueButton;
    private JButton logoutButton;
    private JButton goBackButton;
    private JLabel issueTitleLabel;
    private JLabel reporterNameLabel;
    private JLabel reportedDateLabel;
    private JTextArea textArea1;
    private JLabel titleLabel;
    private JLabel userNameLabel;
    private LoginResponse userInfo;
    private ProjectResponse projectInfo;

    private List<IssueResponse> issues = new ArrayList<IssueResponse>();

    private final String[] tableColumnNames = {"이슈 이름", "State", "Reporter", "Assignee"};
    private DefaultTableModel model = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public IssueMainController(LoginResponse userInfo, ProjectResponse projectInfo) {
        setContentPane(IssueMainPanel);

        initSettings();

        this.userInfo = userInfo;
        this.projectInfo = projectInfo;

        fetchData();
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

    private void fetchData() {
        try {
            issues = NetworkManager.getIssuesByProjectId(projectInfo.getProjectId());
        } catch (Exception e) {
            //TODO fetch 못한 경우 오류 처리
            e.printStackTrace();
        }
    }

    private void initializeComponents() {
        configureTitleLabel();
        setDataIssuesTable();
        issuesTable.setModel(model);

        logoutButton.addActionListener(logoutButtonListener);
        goBackButton.addActionListener(goBackButtonListener);
        fetchIssuesButton.addActionListener(fetchIssuesButtonListener);
    }

    private void configureTitleLabel() {
        titleLabel.setFont(titleLabel.getFont().deriveFont(titleLabel.getFont().getSize() * 2.0f));
        projectTitleLabel.setText(projectInfo.getTitle());
        projectTitleLabel.setFont(projectTitleLabel.getFont().deriveFont(projectTitleLabel.getFont().getSize() * 2.0f));
        userNameLabel.setText(userInfo.getUsername());
    }

    private void setDataIssuesTable() {
        model.setColumnIdentifiers(tableColumnNames);
        int index = 0;
        for (IssueResponse issue : issues) {
            model.addRow(new Object[0]);
            model.setValueAt(issue.getTitle(), index, 0);
            model.setValueAt(issue.getStatus().toString(), index, 1);
            model.setValueAt(issue.getReporter(), index, 2);
            model.setValueAt(issue.getAssignee() == null ? "-" : issue.getAssignee(), index, 3);
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
            new ProjectMainController(userInfo);
            dispose();
        }
    };

    ActionListener fetchIssuesButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            issuesTable.clearSelection();
            fetchData();
            setDataIssuesTable();
        }
    };

    public static void main(String[] args) {
        List<String> Object2 = new ArrayList<>();
        new IssueMainController(new LoginResponse(24, "msl3", "Admin"), new ProjectResponse(3, "더미 프로젝트", "adminName", Object2));

    }
}
