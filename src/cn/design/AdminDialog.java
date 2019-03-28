package cn.design;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class AdminDialog extends JFrame {
    //定义界面使用到的组件作为成员变量
    private Container c = getContentPane();
    private JPanel jp = new JPanel();
    private JLabel tableLabel = new JLabel("通讯录列表");
    private JScrollPane tablePane = new JScrollPane();
    protected JTable table = new JTable();//表

    private JButton alterBtn = new JButton("修改数据");
    private JButton addBtn = new JButton("添加数据");
    private JButton delBtn = new JButton("删除数据");
    private JButton searchBtn = new JButton("查询");
    private JButton searchAllBtn = new JButton("查询全部");
    private JButton sortBtn = new JButton("姓氏排序");//按首字母排序
    private JButton sortBtnByTime = new JButton("时间排序");


    private JLabel delNameLable = new JLabel("联系人姓名");
    private JTextField delNameText = new JTextField();
    private JLabel searchLable = new JLabel("联系人姓名/电话");
    private JTextField searchText = new JTextField();
    private JLabel alterLable = new JLabel("联系人姓名/电话");
    private JTextField alterText = new JTextField();


    public AdminDialog() {
        this.init();// 初始化操作
        this.addComponent();// 添加组件
        this.addListener();// 添加监听器
        displayData(loadData("SELECT * FROM address_book"));
    }

    // 初始化操作
    private void init() {
        this.setTitle("通讯录管理系统");
        this.setSize(600, 400);
        GUITools.center(this);//设置窗口在屏幕上的位置
        this.setResizable(false);// 窗体大小固定
    }

    private void addComponent() {
        // 添加组件
        //取消布局
        jp.setLayout(null);
        //表格标题
        tableLabel.setBounds(265, 20, 70, 25);
        jp.add(tableLabel);
        //表格
        table.getTableHeader().setReorderingAllowed(false);    //列不能移动
        table.getTableHeader().setResizingAllowed(false);    //不可拉动表格
        table.setEnabled(false);                            //不可更改数据
        tablePane.setBounds(50, 50, 500, 200);
        tablePane.setViewportView(table);                    //视口装入表格
        jp.add(tablePane);
        addBtn.setBounds(60, 300, 90, 25);
        alterBtn.setBounds(155, 300, 90, 25);
        delBtn.setBounds(250, 300, 90, 25);
        searchBtn.setBounds(345, 300, 90, 25);
        searchAllBtn.setBounds(345, 330, 90, 25);
        sortBtn.setBounds(440, 300, 90, 25);
//        sortBtnByTime.setBounds(440,330,90,25);
        jp.add(addBtn);
        jp.add(alterBtn);
        jp.add(delBtn);
        jp.add(searchBtn);
        jp.add(searchAllBtn);
        jp.add(sortBtn);
//        this.add(sortBtnByTime);
        delNameLable.setBounds(260, 250, 90, 25);
        delNameText.setBounds(250, 275, 90, 25);
        jp.add(delNameLable);
        jp.add(delNameText);
        searchLable.setBounds(340, 250, 100, 25);
        searchText.setBounds(345, 275, 90, 25);
        jp.add(searchLable);
        jp.add(searchText);
        alterLable.setBounds(150, 250, 100, 25);
        alterText.setBounds(155, 275, 90, 25);
        jp.add(alterLable);
        jp.add(alterText);
        c.add(jp);
    }

    public ArrayList<AddressBook> loadData(String sql) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;//数据库给我们返回一个set字典
        ArrayList<AddressBook> list = new ArrayList<AddressBook>();
        //JDBC基操
        try {
            conn = JDBCUtils.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                AddressBook addressBook = new AddressBook();
                addressBook.setName(rs.getString("name"));
                addressBook.setPhoneNumber(rs.getString("phone_number"));
                addressBook.setAddress(rs.getString("address"));
                addressBook.setEmailAddress(rs.getString("email_address"));
                addressBook.setRemarks(rs.getString("remarks"));
                list.add(addressBook);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.release(rs, stmt, conn);
        }
        return null;
    }

    public void displayData(ArrayList<AddressBook> dataList) {
        String[] title = new String[]{"姓名", "电话号码", "地址", "邮件地址", "备注"};
        String[][] toStringArrayData = listToArray(dataList);
        TableModel dataModel = new DefaultTableModel(toStringArrayData, title);
        table.setModel(dataModel);
    }

    public String[][] listToArray(ArrayList<AddressBook> list) {
        String[][] toStringArrayData = new String[list.size()][5];
        for (int i = 0; i < list.size(); i++) {
            AddressBook addressBook = list.get(i);
            toStringArrayData[i][0] = addressBook.getName();
            toStringArrayData[i][1] = addressBook.getPhoneNumber();
            toStringArrayData[i][2] = addressBook.getAddress();
            toStringArrayData[i][3] = addressBook.getEmailAddress();
            toStringArrayData[i][4] = addressBook.getRemarks();
        }
        return toStringArrayData;
    }

    private void addListener() {
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddDialog().setVisible(true);
            }
        });
        alterBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String alterNamePhone = alterText.getText();
                if (CharTool.isEmpty(alterNamePhone)) {
                    isEmptyDialog();
                } else if (!SearchTool.hasData(alterNamePhone)) {
                    noHasData();
                } else {
                    new AlterDialog(alterNamePhone).setVisible(true);
                }
            }
        });
        delBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String delName = delNameText.getText();
                if (CharTool.isEmpty(delName)) {
                    isEmptyDialog();
                } else if (!SearchTool.hasData(delName)) {
                    noHasData();
                } else {
                    delAddressItem(delName);
                }
            }
        });
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchData = searchText.getText();
                if (CharTool.isEmpty(searchData)) {
                    isEmptyDialog();
                } else if (!SearchTool.hasData(searchData)) {
                    noHasData();
                } else {
                    searchAddressItem(searchData);
                }
            }
        });
        searchAllBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayData(loadData("SELECT * FROM address_book"));
            }
        });
        sortBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<AddressBook> sortData = loadData("SELECT * FROM address_book");
                Collections.sort(sortData);
                String[][] sortStringData = listToArray(sortData);
                String[] title = new String[]{"姓名", "电话号码", "地址", "邮件地址", "备注"};
                TableModel dataModel = new DefaultTableModel(sortStringData, title);
                table.setModel(dataModel);
            }
        });
//        sortBtnByTime.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                ArrayList<AddressBook> sortData = loadData("SELECT * FROM address_book");
//                String[][] sortStringData = listToArray(sortData);
//                String[] title = new String[]{"姓名", "电话号码", "地址", "邮件地址", "备注"};
//                TableModel dataModel = new DefaultTableModel(sortAddressItemByTime(sortStringData), title);
//                table.setModel(dataModel);
//            }
//        });
    }

    private void noHasData() {
        JOptionPane.showMessageDialog(this, "没有所输入字段的数据,操作失败");
    }

    private void isEmptyDialog() {
        JOptionPane.showMessageDialog(this, "该字段不能为空或空格");
    }

    private void delAddressItem(String name) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        int sign;
        try {
            conn = JDBCUtils.getConnection();
            stmt = conn.createStatement();
            if (!CharTool.isEmpty(name)) {
                String sql = "delete from address_book where name =" + "'" + name + "'";//注意字符串要用单引号
                sign = stmt.executeUpdate(sql);
                if (sign > 0) {
                    JOptionPane.showMessageDialog(this, "数据删除成功");
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
        }
    }

    private void searchAddressItem(String namePhoneData) {
        if (!CharTool.isNumeric(namePhoneData)) {
            String sql = "select * from address_book where name = '" + namePhoneData + "'";
            displayData(loadData(sql));
        } else {
            String sql = "select * from address_book where phone_number = '" + namePhoneData + "'";
            displayData(loadData(sql));
        }
    }

//    private String[][] sortAddressItemByAlphabet(){
//
//    }

//    private String[][] sortAddressItemByTime(String[][] strData){
//
//    }
}