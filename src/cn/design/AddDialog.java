package cn.design;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//增
public class AddDialog extends JDialog {
    private JLabel nameLabel = new JLabel("姓名:");
    private JLabel phoneNumberLabel = new JLabel("电话:");
    private JLabel addressLabel = new JLabel("地址：");
    private JLabel emailAddressLabel = new JLabel("邮件地址:");
    private JLabel remarksLable = new JLabel("备注:");
    protected JTextField nameText = new JTextField(6);
    protected JTextField phoneNumberText = new JTextField(6);
    protected JTextField addressText = new JTextField(6);
    protected JTextField emailAddressText = new JTextField(6);
    protected JTextField remarksText = new JTextField(6);

    private JButton submitBtn = new JButton("提交数据");

    public AddDialog() {
        this(null, true);
    }

    public AddDialog(Frame owner, boolean modal) {
        super(owner, modal);
        this.init();// 初始化操作
        this.addComponent();// 添加组件
        this.addListener();// 添加监听器
    }

    // 初始化操作
    private void init() {
        this.setTitle("添加数据");
        this.setSize(300, 250);
        GUITools.center(this);//设置窗口在屏幕上的位置
        this.setResizable(false);// 窗体大小固定
    }

    private void addComponent() {
        this.setLayout(null);//取消布局！！！！
        nameLabel.setBounds(60, 10, 60, 25);
        phoneNumberLabel.setBounds(60, 40, 60, 25);
        addressLabel.setBounds(60, 70, 60, 25);
        emailAddressLabel.setBounds(60, 100, 60, 25);
        remarksLable.setBounds(60, 130, 60, 25);

        nameText.setBounds(125, 10, 80, 25);
        phoneNumberText.setBounds(125, 40, 80, 25);
        addressText.setBounds(125, 70, 80, 25);
        emailAddressText.setBounds(125, 100, 80, 25);
        remarksText.setBounds(125, 130, 80, 25);

        submitBtn.setBounds(105, 170, 90, 25);
        this.add(nameLabel);
        this.add(phoneNumberLabel);
        this.add(addressLabel);
        this.add(emailAddressLabel);
        this.add(remarksLable);

        this.add(nameText);
        this.add(phoneNumberText);
        this.add(addressText);
        this.add(emailAddressText);
        this.add(remarksText);

        this.add(submitBtn);
    }

    private void addListener() {
        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameText.getText();
                String phoneNumber = phoneNumberText.getText();
                String address = addressText.getText();
                String emailAddress = emailAddressText.getText();
                String remarks = remarksText.getText();
                AddressBook addressData = new AddressBook();
                addressData.setName(name);
                addressData.setPhoneNumber(phoneNumber);
                addressData.setAddress(address);
                addressData.setEmailAddress(emailAddress);
                addressData.setRemarks(remarks);

                int sign = addAddressItem(addressData);
                if (sign == 1) {
                    AddDialog.this.dispose();
                }
            }
        });
    }

    private int addAddressItem(AddressBook addressBookItem) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        int sign = 0;
        try {
            conn = JDBCUtils.getConnection();
            stmt = conn.createStatement();
            if (!CharTool.isEmpty(addressBookItem.getName())) {
                if(!CharTool.isABC(addressBookItem.getPhoneNumber())) {
                    String sql = "INSERT INTO address_book(name, phone_number, address, email_address, remarks)"
                            + "VALUES('" + addressBookItem.getName() + "','" + addressBookItem.getPhoneNumber()
                            + "','" + addressBookItem.getAddress() + "','" + addressBookItem.getEmailAddress()
                            + "','" + addressBookItem.getRemarks() + "')";
                    sign = stmt.executeUpdate(sql);
                    if (sign > 0) {
                        JOptionPane.showMessageDialog(this, "数据添加成功");
                    }
                }else{
                    JOptionPane.showMessageDialog(this, "电话字段不能出现字母");
                }
            } else {
                JOptionPane.showMessageDialog(this, "姓名字段不能为空或空格");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.release(rs, stmt, conn);
            if (sign == 0)
                return 0;
            else
                return 1;
        }
    }

}
