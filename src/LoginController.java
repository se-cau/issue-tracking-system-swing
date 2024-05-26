import its.model.LoginRequest;
import its.model.LoginResponse;
import its.network.NetworkManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController extends JFrame {
    private JTextField useridTextField;
    private JTextField passwordTextField;
    private JButton loginButton;
    private JPanel loginPanel;
    private JLabel loginLabel;
    private JButton registerButton;

    public LoginController() {
        setContentPane(loginPanel);
        setTitle("Issue Tracking System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 700);
        setResizable(false);
        setLocationRelativeTo(null);
        loginLabel.setFont(loginLabel.getFont().deriveFont(loginLabel.getFont().getSize() * 2.0f));

        loginButton.addActionListener(loginButtonListener);
        registerButton.addActionListener(registerButtonListener);

        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginController();
    }

    ActionListener loginButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Loginbutton 클릭");

            String id = useridTextField.getText();
            String password = passwordTextField.getText();

            if (id.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "아이디/비밀번호를 올바르게 입력하세요");
                return;
            }

            try {
                LoginResponse loginResponse = NetworkManager.login(new LoginRequest(id, password));
                System.out.println("로그인 성공");
                new ProjectMainController(loginResponse);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
    };

    ActionListener registerButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Register 버튼 클릭");

            new RegisterController();
            dispose();

        }
    };



}
