import its.model.RegisterRequest;
import its.network.NetworkManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterController extends JFrame {
    private JTextField useridTextField;
    private JPasswordField passwordTextField;
    private JButton registerButton;
    private JPanel registerPanel;
    private JLabel registerTitle;
    private JComboBox roleComboBox;
    private JButton goBackToLoginButton;

    public RegisterController() {
        setContentPane(registerPanel);
        setTitle("Issue Tracking System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 700);
        setResizable(false);
        setLocationRelativeTo(null);
        registerTitle.setFont(registerTitle.getFont().deriveFont(registerTitle.getFont().getSize() * 2.0f));

        registerButton.addActionListener(registerButtonListener);
        goBackToLoginButton.addActionListener(goBackToLoginButtonListener);

        setVisible(true);
    }

    ActionListener registerButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Loginbutton 클릭");

            String id = useridTextField.getText();
            String password = passwordTextField.getText();
            String role = roleComboBox.getSelectedItem().toString();

            if (id.isEmpty() || password.isEmpty() || roleComboBox.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(null, "아이디/비밀번호/Role을 올바르게 입력하세요");
                return;
            }

            try {
                NetworkManager.resister(new RegisterRequest(id, password, role));
                JOptionPane.showMessageDialog(null, id + "님 회원 가입 성공! \n 로그인 페이지로 돌아가서 로그인 하세요!");
                System.out.println("회원 가입 성공!"); 

                clearInputs();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
    };

    ActionListener goBackToLoginButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Go Back to Login 버튼 클릭");

            new LoginController();
            dispose();

        }
    };

    public void clearInputs() {
        useridTextField.setText("");
        passwordTextField.setText("");
        roleComboBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        new RegisterController();
    }
}
