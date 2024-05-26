import its.model.ProjectResponse;
import its.network.NetworkManager;

import javax.swing.*;
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
    private int userId;
    private List<ProjectResponse> projects = new ArrayList<>();

    public ProjectMainController(int userId) {
        setContentPane(ProjectMainPanel);
        setTitle("Issue Tracking System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 700);
        setResizable(false);
        setLocationRelativeTo(null);

        this.userId = userId;

        fetchData();
        initializeComponents();

        setVisible(true);

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

        createNewProjectButton.setVisible(false);
        DefaultTableModel model = new DefaultTableModel(contents, columnNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        projectTable.setModel(model);
    }

    private void configureTitleLabel() {
        titleLabel.setFont(titleLabel.getFont().deriveFont(titleLabel.getFont().getSize() * 2.0f));
    }

    ActionListener creatNewProjectButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
        }
    };

    public static void main(String[] args) {
        new ProjectMainController(2);
    }
}
