package cn.design;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

//改
public class AlterDialog extends JDialog {
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

    private String addressData;//接收主界面的查询字段

    public AlterDialog(String addressData) {
        this(null, true, addressData);


    }

    public AlterDialog(Frame owner, boolean modal, String addressData) {
        super(owner, modal);
        this.addressData = addressData;//注意传参的过程
        this.init();// 初始化操作
        this.addComponent();// 添加组件
        setAlterDialogText();
        this.addListener();// 添加监听器

    }

    // 初始化操作
    private void init() {
        this.setTitle("修改数据");
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
                AddressBook addressBook = new AddressBook();
                addressBook.setName(nameText.getText());
                addressBook.setPhoneNumber(phoneNumberText.getText());
                addressBook.setAddress(addressText.getText());
                addressBook.setEmailAddress(emailAddressText.getText());
                addressBook.setRemarks(remarksText.getText());
                int sign = alterData(addressBook);
                if (sign == 1) {
                    AlterDialog.this.dispose();
                }
            }
        });
    }


    private void setAlterDialogText() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        AddressBook addressBook = new AddressBook();
        try {
            conn = JDBCUtils.getConnection();
            stmt = conn.createStatement();
            if (!CharTool.isNumeric(addressData)) {
                String sql = "select * from address_book where name = '" + addressData + "'";
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    nameText.setText(rs.getString("name"));
                    phoneNumberText.setText(rs.getString("phone_number"));
                    addressText.setText(rs.getString("address"));
                    emailAddressText.setText(rs.getString("email_address"));
                    remarksText.setText(rs.getString("remarks"));
                }
            } else {
                String sql = "select * from address_book where phone_number = '" + addressData + "'";
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    nameText.setText(rs.getString("name"));
                    phoneNumberText.setText(rs.getString("phone_number"));
                    addressText.setText(rs.getString("address"));
                    emailAddressText.setText(rs.getString("email_address"));
                    remarksText.setText(rs.getString("remarks"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.release(rs, stmt, conn);
        }
    }

    private int alterData(AddressBook AlterData) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        int sign = 0;
        String nameData;
        try {
            conn = JDBCUtils.getConnection();
            stmt = conn.createStatement();
            nameData = AlterData.getName();
            if (!CharTool.isEmpty(nameData)) {
                if (!CharTool.isNumeric(addressData)) {
                    if(!CharTool.isABC(AlterData.getPhoneNumber())){
                        String sql = "update address_book set name = '" + AlterData.getName() + "', phone_number = '"
                                + AlterData.getPhoneNumber() + "', address = '" + AlterData.getAddress() + "', email_address = '"
                                + AlterData.getEmailAddress() + "', remarks = '" + AlterData.getRemarks() + "'"
                                + "where name = '" + addressData + "'";//注意字符串要用单引号
                        sign = stmt.executeUpdate(sql);
                        if (sign > 0) {
                            JOptionPane.showMessageDialog(this, "数据修改成功");
                        }
                    }else{
                        isABCDialog();
                    }
                }else{
                    String sql = "update address_book set name = '" + AlterData.getName() + "', phone_number = '"
                            + AlterData.getPhoneNumber() + "', address = '" + AlterData.getAddress() + "', email_address = '"
                            + AlterData.getEmailAddress() + "', remarks = '" + AlterData.getRemarks() + "'"
                            + "where phone_number = '" + addressData + "'";//注意字符串要用单引号
                    sign = stmt.executeUpdate(sql);
                    if (sign > 0) {
                        JOptionPane.showMessageDialog(this, "数据修改成功");
                    }
                }
            } else {
                isEmptyDialog();
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

    public void isEmptyDialog() {
        JOptionPane.showMessageDialog(this, "该字段不能为空或空格");
    }

    public void isABCDialog() {
        JOptionPane.showMessageDialog(this, "电话字段不能包含字母");
    }


}
