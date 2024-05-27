import its.model.LoginResponse;
import its.model.UserResponse;
import its.network.NetworkManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class ProjectCreateController extends JFrame {
    private JPanel ProjectCreatePanel;
    private JTextField titleTextField;
    private JLabel titleLabel;
    private JTable contributorsTable;
    private JButton createProjectButton;
    private JButton goBackToProjectButton;

    private LoginResponse userInfo;

    private List<UserResponse> users;

    private final String[] tableColumnNames = {"Selected","Name", "Role"};
    private DefaultTableModel model = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 0;
        }
        public Class<?> getColumnClass(int column)
        {
            switch(column)
            {
                case 0:
                    return Boolean.class;
                case 1:
                    return String.class;
                case 2:
                    return String.class;
                default:
                    return String.class;
            }
        }
    };

    public ProjectCreateController(LoginResponse loginResponse) {
        setContentPane(ProjectCreatePanel);

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
            users = NetworkManager.getUsers();
            for (UserResponse user : users) {
                System.out.println(user.getUsername());
            }
        } catch (Exception e) {
            //TODO fetch 못한 경우 오류 처리
            users = new ArrayList<UserResponse>();
            e.printStackTrace();
        }
    }

    private void initializeComponents() {
        configureTitleLabel();
        setDataProjectTable();
        contributorsTable.setModel(model);
    }

    private void configureTitleLabel() {
        titleLabel.setFont(titleLabel.getFont().deriveFont(titleLabel.getFont().getSize() * 2.0f));
    }

    private void setDataProjectTable() {
        model.setColumnIdentifiers(tableColumnNames);
        int index = 0;
        for (UserResponse user : users) {
            model.addRow(new Object[0]);
            model.setValueAt(false, index, 0);
            model.setValueAt(user.getUsername(), index, 1);
            model.setValueAt(user.getRole(), index, 2);
            index++;
        }
    }

    public static void main(String[] args) {
        new ProjectCreateController(new LoginResponse(24, "msl3", "Admin"));
    }

}
