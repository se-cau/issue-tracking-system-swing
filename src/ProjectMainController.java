import its.model.LoginResponse;
import its.model.ProjectResponse;
import its.network.NetworkManager;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ProjectMainController extends JFrame {

    private JTable projectTable;
    private JPanel ProjectMainPanel;
    private JLabel titleLabel;
    private JButton createNewProjectButton;
    private JButton fetchProjectsButton;
    private JButton moveToSelectedProjectButton;
    private JList contributorList;
    private JButton logoutButton;
    private JLabel projectTitleLabel;
    private JLabel adminNameLable;
    private JLabel userNameLabel;
    private LoginResponse userInfo;
    private List<ProjectResponse> projects = new ArrayList<>();

    private final String[] tableColumnNames = {"프로젝트 이름", "Contributors"};
    private String[][] tableContents = new String[][]{};
    private DefaultTableModel model = new DefaultTableModel(tableContents, tableColumnNames){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public ProjectMainController(LoginResponse loginResponse) {
        setContentPane(ProjectMainPanel);

        initSettings();
        this.userInfo = loginResponse;

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
            projects = NetworkManager.getProjectsByUserId(userInfo.getUserId());
        } catch (Exception e) {
            //TODO fetch 못한 경우 오류 처리
            e.printStackTrace();
        }
    }

    private void initializeComponents() {
        configureTitleLabel();
        userNameLabel.setText(userInfo.getUsername());

        setDataProjectTable();
        projectTable.setModel(model);
        projectTable.getSelectionModel().addListSelectionListener(tableListSelectionListioner);

        moveToSelectedProjectButton.addActionListener(moveToSelectedProjectButtonListener);

        logoutButton.addActionListener(logoutButtonListener);
        fetchProjectsButton.addActionListener(fetchProjectsButtonListener);

        if (!userInfo.getRole().equals("Admin")) {
            createNewProjectButton.setVisible(false);
        } else {
            createNewProjectButton.addActionListener(creatNewProjectButtonListener);
        }
    }

    private void setDataProjectTable() {
        tableContents = new String[projects.size()+1][2];
        int index = 0;
        for (ProjectResponse project : projects) {
            tableContents[index] = project.getProjectContent();
            index++;
        }
        model.setDataVector(tableContents, tableColumnNames);
    }

    private void configureTitleLabel() {
        titleLabel.setFont(titleLabel.getFont().deriveFont(titleLabel.getFont().getSize() * 2.0f));
    }

    ActionListener creatNewProjectButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            new ProjectCreateController(userInfo);
            dispose();
        }
    };

    ActionListener logoutButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            new LoginController();
            dispose();
        }
    };

    ActionListener fetchProjectsButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            projectTable.clearSelection();
            fetchData();
            setDataProjectTable();
        }
    };

    ActionListener moveToSelectedProjectButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = projectTable.getSelectedRow();
            if (selectedIndex != -1) {
                ProjectResponse selectedProject = projects.get(selectedIndex);
                System.out.println(selectedProject.getProjectId());
                System.out.println(selectedProject.getTitle());

                new IssueMainController(userInfo, selectedProject);
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Project를 선택하세요");
                return;
            }
        }
    };

    ListSelectionListener tableListSelectionListioner = new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting())
                return;

            updateSelectedProjectLabels();
        }
    };

    private void updateSelectedProjectLabels() {

        int selectedIndex = projectTable.getSelectedRow();

        if (selectedIndex != -1) {
            ProjectResponse selectedProject = projects.get(selectedIndex);
            projectTitleLabel.setText(selectedProject.getTitle());
            adminNameLable.setText(selectedProject.getAdminName());

            DefaultListModel<String> listModel = new DefaultListModel<>();
            for (String item : selectedProject.getContributorNames()) {
                listModel.addElement(item);
            }
            contributorList.setModel(listModel);

        } else {
            projectTitleLabel.setText("-");
            adminNameLable.setText("-");
            contributorList.setModel(new DefaultListModel());
        }
    }

    public static void main(String[] args) {
        new ProjectMainController(new LoginResponse(12, "pl01", "PL"));
    }
}
