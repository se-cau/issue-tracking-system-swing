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
    private int userId;
    private List<ProjectResponse> projects = new ArrayList<>();

    public ProjectMainController(int userId) {
        setContentPane(ProjectMainPanel);
        initSettings();
        this.userId = userId;

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
            projects = NetworkManager.getProjectsByUserId(userId);
        } catch (Exception e) {
            //TODO fetch 못한 경우 오류 처리
            e.printStackTrace();
        }
    }

    private void initializeComponents() {
        configureTitleLabel();
        String[] columnNames = {"프로젝트 이름", "Contributors"};
        String[][] contents = new String[projects.size()+1][2];
        int index = 0;
        System.out.println("여기" + projects);
        for (ProjectResponse project : projects) {
            contents[index] = project.getProjectContent();
            index++;
        }
        DefaultTableModel model = new DefaultTableModel(contents, columnNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        projectTable.setModel(model);
        projectTable.getSelectionModel().addListSelectionListener(tableListSelectionListioner);

        moveToSelectedProjectButton.addActionListener(moveToSelectedProjectButtonListener);
    }

    private void configureTitleLabel() {
        titleLabel.setFont(titleLabel.getFont().deriveFont(titleLabel.getFont().getSize() * 2.0f));
    }

    ActionListener creatNewProjectButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
        }
    };

    ActionListener moveToSelectedProjectButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = projectTable.getSelectedRow();
            if (selectedIndex != -1) {
                ProjectResponse selectedProject = projects.get(selectedIndex);
                new IssueMainController(selectedProject.getProjectId());
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
            if (e.getValueIsAdjusting()) return;

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
            projectTitleLabel.setText("");
        }
    }


    public static void main(String[] args) {
        new ProjectMainController(2);
    }
}
