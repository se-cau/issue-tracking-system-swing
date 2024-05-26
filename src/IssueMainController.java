import javax.swing.*;

public class IssueMainController extends JFrame {

    private JLabel testLabel;
    private JPanel IssueMainPanel;

    public IssueMainController(int projectId) {
        setContentPane(IssueMainPanel);
        initSettings();
        testLabel.setText(""+projectId);
        setVisible(true);
    }
    private void initSettings() {
        setTitle("Issue Tracking System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 700);
        setResizable(false);
        setLocationRelativeTo(null);
    }

}
