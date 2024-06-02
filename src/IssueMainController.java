import its.model.IssueResponse;
import its.model.LoginResponse;
import its.model.ProjectResponse;
import its.model.StatisticsResponse;
import its.network.NetworkManager;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private JTextArea descriptionTextArea;
    private JLabel titleLabel;
    private JLabel userNameLabel;
    private JComboBox filterStateComboBox;
    private JButton applyButton;
    private JList statisticsList1;
    private JList statisticsList2;
    private JList statisticsList3;
    private JList statisticsList4;
    private LoginResponse userInfo;
    private ProjectResponse projectInfo;

    private StatisticsResponse statisticsInfo;

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
            statisticsInfo = NetworkManager.getStatistics(projectInfo.getProjectId());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Fetch data 실패 \n확인 후 다시 시도해주세요" + e.getMessage());
        }
    }

    private void initializeComponents() {
        configureTitleLabel();
        setDataIssuesTable();
        setDataStatisticsLists();
        issuesTable.setModel(model);
        issuesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        descriptionTextArea.setEditable(false);

        logoutButton.addActionListener(logoutButtonListener);
        goBackButton.addActionListener(goBackButtonListener);
        applyButton.addActionListener(applyButtonListener);

        fetchIssuesButton.addActionListener(fetchIssuesButtonListener);
        issuesTable.getSelectionModel().addListSelectionListener(issueTableSelectionListioner);
        moveToSelectedIssueButton.addActionListener(moveToSelectedIssueButtonListener);

        if (!userInfo.getRole().equals("Tester")) {
            createNewIssueButton.setVisible(false);
        } else {
            createNewIssueButton.addActionListener(createNewIssueButtonListener);
        }

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
        model.setRowCount(0);
        for (IssueResponse issue : issues) {
            model.addRow(new Object[0]);
            model.setValueAt(issue.getTitle(), index, 0);
            model.setValueAt(issue.getStatus().toString(), index, 1);
            model.setValueAt(issue.getReporter(), index, 2);
            model.setValueAt(issue.getAssignee() == null ? "-" : issue.getAssignee(), index, 3);
            index++;
        }
    }

    private void setDataStatisticsLists() {

        Map<String, Integer> statusDistribution = statisticsInfo.getStatusDistribution();
        Map<String, Integer> reporterDistribution = statisticsInfo.getReporterDistribution();
        Map<String, Integer> assigneeDistribution = statisticsInfo.getAssigneeDistribution();
        List<String> topCommentedIssues = statisticsInfo.getTopCommentedIssues();

        DefaultListModel<String> listModel1 = new DefaultListModel<>();
        if (statusDistribution == null) {
            listModel1.addElement("-");
            statisticsList1.setModel(listModel1);
        } else {
            for (Map.Entry<String, Integer> entry : statusDistribution.entrySet()) {
                listModel1.addElement(entry.getKey() + ": " + entry.getValue());
            }
            statisticsList1.setModel(listModel1);
        }

        DefaultListModel<String> listModel2 = new DefaultListModel<>();
        if (reporterDistribution == null) {
            listModel2.addElement("-");
            statisticsList2.setModel(listModel2);
        } else {
            for (Map.Entry<String, Integer> entry : reporterDistribution.entrySet()) {
                listModel2.addElement(entry.getKey() + ": " + entry.getValue());
            }
            statisticsList2.setModel(listModel2);
        }

        DefaultListModel<String> listModel3 = new DefaultListModel<>();
        if (reporterDistribution == null) {
            listModel3.addElement("-");
            statisticsList3.setModel(listModel3);
        } else {
            for (Map.Entry<String, Integer> entry : assigneeDistribution.entrySet()) {
                listModel3.addElement(entry.getKey() + ": " + entry.getValue());
            }
            statisticsList3.setModel(listModel3);
        }

        DefaultListModel<String> listModel4 = new DefaultListModel<>();
        if (reporterDistribution == null) {
            listModel4.addElement("-");
            statisticsList4.setModel(listModel4);
        } else {
            for (String issueName: topCommentedIssues) {
                listModel4.addElement(issueName);
            }
            statisticsList4.setModel(listModel4);
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
            setDataStatisticsLists();
        }
    };

    ActionListener createNewIssueButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            new IssueCreateController(userInfo, projectInfo);
            dispose();
        }
    };

    ListSelectionListener issueTableSelectionListioner = new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting())
                return;
            updateSelectedIssueLabels();
        }
    };
    ActionListener applyButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String filterState = filterStateComboBox.getSelectedItem().toString();
            try {
                issues = NetworkManager.getIssuesByStatus(filterState, projectInfo.getProjectId());
                setDataIssuesTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }

        }
    };

    ActionListener moveToSelectedIssueButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            int selectedIndex = issuesTable.getSelectedRow();
            if (selectedIndex != -1) {
                IssueResponse selectedIssue = issues.get(selectedIndex);
                new IssueDetailController(userInfo, projectInfo, selectedIssue);
            } else {
                JOptionPane.showMessageDialog(null, "이슈를 선택하세요.");
            }
        }
    };


    private void updateSelectedIssueLabels() {
        int selectedIndex = issuesTable.getSelectedRow();
        if (selectedIndex != -1) {
            IssueResponse selectedIssue = issues.get(selectedIndex);
            issueTitleLabel.setText(selectedIssue.getTitle());
            reporterNameLabel.setText(selectedIssue.getReporter());
            reportedDateLabel.setText(selectedIssue.getCreatedAt());
            descriptionTextArea.setText(selectedIssue.getDescription());
        } else {
            issueTitleLabel.setText("-");
            reporterNameLabel.setText("-");
            reportedDateLabel.setText("-");
            descriptionTextArea.setText("-");
        }
    }

    public static void main(String[] args) {
        List<String> Object2 = new ArrayList<>();
        new IssueMainController(new LoginResponse(6, "민섭 테스트 Dev", "Dev"), new ProjectResponse(1, "민섭 Admin Project 1", "민섭 테스트 Admin", Object2));

    }
}
