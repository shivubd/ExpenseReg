import java.awt.Toolkit;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.sql.*;
import com.toedter.calendar.JDateChooser;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JButton;
import java.awt.event.*;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.util.Date;


public class mainUI {
	private JFrame frame;
	private JTextField desctf;
	private JTextField amttf;
	private JTextField Descrecordtf;
	private JTextField amtrecordtf;
	private Connection conn=null;
	private Statement stmt=null;
	private CallableStatement cstmt=null;
	private CallableStatement cstmt2=null;
	private ResultSet rs=null;
	private JTable maintable;
	private String user;
	private JComboBox<Object> catrecordcb;
	private JDateChooser daterecorddc;
	private JComboBox<Object> categorycb;
	private JDateChooser addDatedc;
	private JLabel addstatuslabel;
	private JCheckBox categorychkbox;
	private JCheckBox amtchkbox;
	private JCheckBox timechkbox;
	private JComboBox<Object> categorysrchcb;
	private JComboBox<Object> amtrangecb;
	private JYearChooser timeyearsrch;
	private JMonthChooser timemonthsrch;
	private JLabel lbltotal;
	private JLabel lblFullName;
	private JLabel lblMailID;
	private JLabel lblPhone;
	private JLabel lblBudget;
	private JLabel lblBgtIndi;
	private JButton btnPieChartMonth;
	private JPanel percchpanel;
	private JButton btnPieChart;
	public mainUI(String user) {
		try {
			connecttodbms();
			this.user=user;
			Image mainicon=new ImageIcon(this.getClass().getResource("/mainicon.png")).getImage();
			frame=new JFrame("Expense Register");
			frame.setIconImage(mainicon);
			frame.setBackground(Color.WHITE);
			Toolkit tk=Toolkit.getDefaultToolkit();
			int x=(int)tk.getScreenSize().getWidth();
			int y=(int)tk.getScreenSize().getHeight();
			frame.setLocation((x/2)-540, (y/2)-360);
			frame.getContentPane().setLayout(null);
			frame.getContentPane().addMouseListener(new frameclickAction());
			
			JPanel userpanel = new JPanel();
			userpanel.setLayout(null);
			userpanel.setBackground(new Color(0, 51, 102));
			userpanel.setBounds(0, 0, 1074, 100);
			frame.getContentPane().add(userpanel);
			JLabel logopic = new JLabel();
			Image imglogo=new ImageIcon(this.getClass().getResource("/Desc.png")).getImage();
			logopic.setIcon(new ImageIcon(imglogo));
			logopic.setBounds(836, 12, 228, 75);
			userpanel.add(logopic);
			
			JLabel usericonlabel = new JLabel();
			usericonlabel.setHorizontalAlignment(SwingConstants.CENTER);
			Image img=new ImageIcon(this.getClass().getResource("/usericon.png")).getImage();
			usericonlabel.setIcon(new ImageIcon(img));
			usericonlabel.setBounds(10, 11, 78, 78);
			userpanel.add(usericonlabel);
			
			JLabel lblUsername = new JLabel(user);
			lblUsername.setForeground(Color.LIGHT_GRAY);
			lblUsername.setFont(new Font("Coolvetica Rg", Font.PLAIN, 20));
			lblUsername.setBounds(85, 35, 140, 30);
			userpanel.add(lblUsername);
			
			lblFullName = new JLabel();
			lblFullName.setForeground(Color.WHITE);
			lblFullName.setBounds(231, 30, 350, 14);
			userpanel.add(lblFullName);
			
			lblMailID = new JLabel();
			lblMailID.setForeground(Color.WHITE);
			lblMailID.setBounds(231, 45, 350, 14);
			userpanel.add(lblMailID);
			
			lblPhone = new JLabel();
			lblPhone.setForeground(Color.WHITE);
			lblPhone.setBounds(231, 60, 350, 14);
			userpanel.add(lblPhone);
			
			lblBudget = new JLabel();
			lblBudget.setForeground(Color.WHITE);
			lblBudget.setBounds(231, 75, 350, 14);
			userpanel.add(lblBudget);
			
			JLabel lblAccountDetails = new JLabel("ACCOUNT DETAILS");
			lblAccountDetails.setFont(new Font("Tahoma", Font.BOLD, 11));
			lblAccountDetails.setForeground(Color.LIGHT_GRAY);
			lblAccountDetails.setBounds(231, 12, 140, 14);
			userpanel.add(lblAccountDetails);
			
			JButton btnEdit = new JButton("Edit");
			btnEdit.setBounds(364, 6, 70, 20);
			userpanel.add(btnEdit);
			btnEdit.addActionListener(new aceditAction());
			
			JButton btnDeleteAccount = new JButton("DELETE ACCOUNT");
			btnDeleteAccount.setBounds(444, 6, 160, 20);
			userpanel.add(btnDeleteAccount);
			btnDeleteAccount.addActionListener(new deleteaccountAction());
			
			
			try {
				rs=stmt.executeQuery("Select * from acdetails where user_name='"+user+"';");
				while(rs.next()) {
					lblFullName.setText("Full name: "+rs.getString("Fname"));
					lblMailID.setText("Mail ID: "+rs.getString("mailID"));
					lblPhone.setText("Phone: "+rs.getString("phone"));
					if(rs.getInt("bgt")<0) {
						lblBudget.setText("Monthly Budget: Not set");
					}
					else {
						lblBudget.setText("Monthly Budget: Rs"+rs.getInt("bgt"));
					}
				}
			}
			catch(SQLException sac) {}
			
			JPanel searchpanel = new JPanel();
			searchpanel.setLayout(null);
			searchpanel.setBackground(Color.LIGHT_GRAY);
			searchpanel.setBounds(0, 413, 251, 279);
			frame.getContentPane().add(searchpanel);
			
			JLabel searchlabel = new JLabel("Search");
			searchlabel.setForeground(new Color(0, 51, 102));
			searchlabel.setFont(new Font("Coolvetica Rg", Font.PLAIN, 15));
			searchlabel.setBounds(10, 11, 80, 14);
			searchpanel.add(searchlabel);
			
			JLabel sortbylabel = new JLabel("Sort by:");
			sortbylabel.setBounds(10, 36, 46, 14);
			searchpanel.add(sortbylabel);
			
			
			JPanel addpanel = new JPanel();
			addpanel.setLayout(null);
			addpanel.setBackground(Color.LIGHT_GRAY);
			addpanel.setBounds(0, 111, 251, 299);
			frame.getContentPane().add(addpanel);
			
			String category[]= {"Education","Food","Transport","Payment","Medical&Healthcare","Utilities","Travel"};
			categorycb = new JComboBox<Object>(category);
			categorycb.setBounds(10, 97, 220, 20);
			addpanel.add(categorycb);
			
			categorysrchcb = new JComboBox<Object>(category);
			categorysrchcb.setEnabled(false);
			categorysrchcb.setBounds(37, 81, 204, 20);
			searchpanel.add(categorysrchcb);
			
			
			categorychkbox = new JCheckBox();
			categorychkbox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange()==ItemEvent.SELECTED) {
						categorysrchcb.setEnabled(true);
					}
					else {
						categorysrchcb.setEnabled(false);
						categorysrchcb.setSelectedIndex(0);
					}
				}
			});
			categorychkbox.setBounds(10, 81, 21, 20);
			searchpanel.add(categorychkbox);
			
			JPanel tablebgpanel = new JPanel();
			tablebgpanel.setLayout(null);
			tablebgpanel.setBounds(255, 111, 819, 495);
			frame.getContentPane().add(tablebgpanel);
			tablebgpanel.setLayout(null);
			
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane.setBounds(10, 0, 799, 441);
			tablebgpanel.add(scrollPane);
			
			maintable = new JTable();
			maintable.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Id no.", "Description", "Category", "Date", "Amount"
				}
			) {
				private static final long serialVersionUID = 1L;
				boolean[] columnEditables = new boolean[] {
					false, false, false, false, false
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			});
			maintable.getColumnModel().getColumn(0).setPreferredWidth(30);
			maintable.getColumnModel().getColumn(1).setPreferredWidth(200);
			maintable.getColumnModel().getColumn(2).setPreferredWidth(90);
			maintable.getColumnModel().getColumn(3).setPreferredWidth(50);
			maintable.getColumnModel().getColumn(4).setPreferredWidth(70);
			scrollPane.setViewportView(maintable);
			
			JLabel lblTotalExpense = new JLabel("Total Expense: ");
			lblTotalExpense.setBounds(10, 448, 95, 14);
			tablebgpanel.add(lblTotalExpense);
			
			lbltotal = new JLabel("");
			lbltotal.setFont(new Font("Tahoma", Font.PLAIN, 13));
			lbltotal.setBounds(115, 448, 110, 14);
			tablebgpanel.add(lbltotal);
			
			Image rsimg=new ImageIcon(this.getClass().getResource("/Rs.png")).getImage();
			
			JLabel rslbl = new JLabel();
			rslbl.setIcon(new ImageIcon(rsimg));
			rslbl.setBounds(100, 448, 15, 15);
			tablebgpanel.add(rslbl);
			
			JButton btnExport = new JButton("Export");
			btnExport.setBounds(720, 444, 89, 23);
			tablebgpanel.add(btnExport);
			btnExport.addActionListener(new exportAction());
			
			JLabel lblDownloadThisTable = new JLabel("Download this table into Excel file");
			lblDownloadThisTable.setBounds(520, 448, 200, 14);
			tablebgpanel.add(lblDownloadThisTable);
			
			lblBgtIndi = new JLabel();
			lblBgtIndi.setFont(new Font("Tahoma", Font.BOLD, 11));
			lblBgtIndi.setBounds(235, 448, 151, 14);
			tablebgpanel.add(lblBgtIndi);
			
			percchpanel = new JPanel();
			percchpanel.setBounds(10, 462, 250, 25);
			tablebgpanel.add(percchpanel);
			percchpanel.setLayout(new GridLayout(0, 1, 0, 0));
			
			btnPieChart = new JButton("Percentage of Expenses");
			percchpanel.add(btnPieChart);
			btnPieChart.addActionListener(new piechartAction());
			
			btnPieChartMonth = new JButton("Percentage of Expenses (Monthly)");
			btnPieChartMonth.addActionListener(new piechartActionMonthly());
			
			/*JButton btnUndo = new JButton("Undo delete");
			btnUndo.setBounds(699, 472, 110, 23);
			//tablebgpanel.add(btnUndo);
			btnUndo.addActionListener(new undoAction());*/
			
			/*JLabel lblNewLabel = new JLabel("        undos avalaible: 5");
			lblNewLabel.setBounds(563, 476, 126, 14);
			//tablebgpanel.add(lblNewLabel);*/
			
			showtable();
			maintable.addMouseListener(new tableListener());
			
			
			JLabel lblDescription = new JLabel("Description");
			lblDescription.setBounds(10, 36, 80, 14);
			addpanel.add(lblDescription);
			
			desctf = new JTextField();
			desctf.setBackground(Color.WHITE);
			desctf.setBounds(10, 50, 220, 20);
			addpanel.add(desctf);
			desctf.setColumns(10);
			
			JLabel lblCategory = new JLabel("Category");
			lblCategory.setBounds(10, 81, 80, 14);
			addpanel.add(lblCategory);
		
			JLabel categorysrchlabel = new JLabel("Category");
			categorysrchlabel.setBounds(10, 63, 80, 14);
			searchpanel.add(categorysrchlabel);
			
			JLabel amtsrchlabel = new JLabel("Amount");
			amtsrchlabel.setBounds(10, 112, 80, 14);
			searchpanel.add(amtsrchlabel);
			

			String amtrange[]={"<1000","1000-3000","3000-6000","6000-10,000","10000-20000","20000-30000",">30000"};
			amtrangecb = new JComboBox<Object>(amtrange);
			amtrangecb.setEnabled(false);
			amtrangecb.setBounds(37, 127, 189, 20);
			searchpanel.add(amtrangecb);
			
			amtchkbox = new JCheckBox("");
			amtchkbox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange()==ItemEvent.SELECTED) {
						amtrangecb.setEnabled(true);
					}
					else {
						amtrangecb.setEnabled(false);
						amtrangecb.setSelectedIndex(0);
					}
				}
			});
			amtchkbox.setBounds(10, 127, 21, 20);
			searchpanel.add(amtchkbox);
			
			
			JLabel timelabel = new JLabel("Time");
			timelabel.setBounds(10, 158, 80, 14);
			searchpanel.add(timelabel);
			
			
			timemonthsrch = new JMonthChooser();	
			timemonthsrch.getSpinner().setEnabled(false);
			timemonthsrch.setMonth(0);
			timemonthsrch.setBounds(37, 171, 114, 20);
			searchpanel.add(timemonthsrch);
			
			timeyearsrch = new JYearChooser();
			timeyearsrch.getSpinner().setEnabled(false);
			timeyearsrch.setYear(2020);
			timeyearsrch.setBounds(156, 171, 85, 20);
			searchpanel.add(timeyearsrch);
			
			
			timechkbox = new JCheckBox();
			timechkbox.setBounds(10, 171, 21, 20);
			searchpanel.add(timechkbox);
			timechkbox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange()==ItemEvent.SELECTED) {
						timemonthsrch.getSpinner().setEnabled(true);
						timeyearsrch.getSpinner().setEnabled(true);
					}
					else {
						timemonthsrch.getSpinner().setEnabled(false);
						timeyearsrch.getSpinner().setEnabled(false);
					}
				}
			});

			JButton btnSearch = new JButton("Search");
			btnSearch.setBounds(10, 221, 89, 23);
			searchpanel.add(btnSearch);
			btnSearch.addActionListener(new searchaction());
			
			JButton btnCancel = new JButton("Cancel");
			btnCancel.setBounds(109, 221, 89, 23);
			searchpanel.add(btnCancel);
			btnCancel.addActionListener(new srchcancelaction());
			
			JLabel rupeesrchlbl = new JLabel();
			rupeesrchlbl.setIcon(new ImageIcon(rsimg));
			rupeesrchlbl.setBounds(226, 130, 15, 15);
			searchpanel.add(rupeesrchlbl);
			
			JLabel lblmonthlyBudgetCan = new JLabel("*Monthly Budget can checked here");
			lblmonthlyBudgetCan.setForeground(Color.GRAY);
			lblmonthlyBudgetCan.setBounds(10, 196, 231, 14);
			searchpanel.add(lblmonthlyBudgetCan);
			
			JLabel lblAmount = new JLabel("Amount");
			lblAmount.setBounds(10, 128, 65, 14);
			addpanel.add(lblAmount);
			
			amttf = new JTextField();
			amttf.setBounds(10, 141, 194, 20);
			addpanel.add(amttf);
			amttf.setColumns(10);
			
			JLabel lblDate = new JLabel("Date");
			lblDate.setBounds(10, 172, 46, 14);
			addpanel.add(lblDate);
			
			JPanel recordpanel = new JPanel();
			recordpanel.setLayout(null);
			recordpanel.setBackground(Color.LIGHT_GRAY);
			recordpanel.setBounds(261, 617, 819, 75);
			
			addDatedc=new JDateChooser();
			addDatedc.setDateFormatString("dd-MM-yyyy");
			addDatedc.setBounds(10,197,220,20);
			addpanel.add(addDatedc);

			JLabel rupeelabel = new JLabel();
			rupeelabel.setIcon(new ImageIcon(rsimg));
			rupeelabel.setBounds(205, 144, 15, 15);
			addpanel.add(rupeelabel);
			
			JButton btnEnter = new JButton("Enter");
			btnEnter.setBounds(10, 230, 89, 23);
			addpanel.add(btnEnter);
			btnEnter.addActionListener(new enterAction());
			
			addstatuslabel = new JLabel();
			addstatuslabel.setForeground(new Color(0, 51, 102));
			addstatuslabel.setBounds(10, 264, 194, 14);
			addpanel.add(addstatuslabel);
			
			JButton btnClear = new JButton("Clear");
			btnClear.setBounds(109, 230, 89, 23);
			addpanel.add(btnClear);
			btnClear.addActionListener(new clearAction());
			
			JLabel addrecordlabel = new JLabel("Add Record");
			addrecordlabel.setForeground(new Color(0, 51, 102));
			addrecordlabel.setFont(new Font("Coolvetica Rg", Font.PLAIN, 15));
			addrecordlabel.setBounds(10, 11, 80, 14);
			addpanel.add(addrecordlabel);
			
			frame.getContentPane().add(recordpanel);
			
			JLabel recordDesclbl = new JLabel("Description");
			recordDesclbl.setBounds(10, 11, 77, 14);
			recordpanel.add(recordDesclbl);
			
			Descrecordtf = new JTextField();
			Descrecordtf.setBounds(10, 30, 180, 20);
			recordpanel.add(Descrecordtf);
			Descrecordtf.setColumns(10);
			
			JLabel recordcatlbl = new JLabel("Category");
			recordcatlbl.setBounds(200, 11, 70, 14);
			recordpanel.add(recordcatlbl);
			
			catrecordcb = new JComboBox<>(category);
			catrecordcb.setBounds(200, 30, 180, 20);
			recordpanel.add(catrecordcb);
			
			JLabel lblAmount_1 = new JLabel("Amount");
			
			lblAmount_1.setBounds(390, 11, 46, 14);
			recordpanel.add(lblAmount_1);
			
			amtrecordtf = new JTextField();
			amtrecordtf.setBounds(390, 30, 100, 20);
			recordpanel.add(amtrecordtf);
			amtrecordtf.setColumns(10);
			
			JLabel lblDate_1 = new JLabel("Date");
			lblDate_1.setBounds(525, 11, 46, 14);
			recordpanel.add(lblDate_1);
			
			daterecorddc =  new JDateChooser();
			daterecorddc.setDateFormatString("dd-MM-yyyy");
			daterecorddc.setBounds(525, 30, 140, 20);
			recordpanel.add(daterecorddc);
			
			JButton btnUpdate = new JButton("Update");
			btnUpdate.addActionListener(new updateAction());
			btnUpdate.setBounds(704, 7, 89, 23);
			recordpanel.add(btnUpdate);
			
			JButton btnDelete = new JButton("Delete");
			btnDelete.setBounds(704, 39, 89, 23);
			recordpanel.add(btnDelete);
			btnDelete.addActionListener(new deleteAction());
			
			JLabel rupeerecordlbl = new JLabel();
			rupeerecordlbl.setIcon(new ImageIcon(rsimg));
			rupeerecordlbl.setBounds(489, 33, 15, 15);
			recordpanel.add(rupeerecordlbl);
			
			JLabel lblToClearThis = new JLabel("To clear this record click anywhere in the window");
			lblToClearThis.setFont(new Font("Tahoma", Font.PLAIN, 10));
			lblToClearThis.setBounds(10, 48, 260, 14);
			recordpanel.add(lblToClearThis);
			frame.setResizable(false);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
			frame.setSize(1080,720);
		}
		catch(Exception e) {}
		finally {}
	}
	public ArrayList<exp> getexpList(){
		ArrayList<exp> expenseList=new ArrayList<>();
		try {
			rs=stmt.executeQuery("select * from maintable where user_name='"+user+"';");/////////////////////////////////EDITED
			exp expobj;
			while(rs.next()){
				expobj=new exp(rs.getInt("id"),rs.getString("descp"),rs.getString("cat"),String.valueOf(rs.getDate("dat")),rs.getInt("amt"));
				expenseList.add(expobj);
			}
		}
		catch(Exception e) {}
		return expenseList;
	}
	public void connecttodbms() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/users","root","root");
			stmt=conn.createStatement();
			cstmt=conn.prepareCall("{CALL cal_catperc(?)}");
			cstmt2=conn.prepareCall("{CALL cal_catpercmonthly(?,?,?,?,?,?,?,?,?)}");
		}
		catch(Exception se) {}
		finally {}
	}
	public void showtable() {
		ArrayList<exp> list=getexpList();
		DefaultTableModel model=(DefaultTableModel)maintable.getModel();
		Object row[]=new Object[5];
		Double sum=0.0;
		for(int i=0;i<list.size();i++) {
			row[0]=list.get(i).getid();
			row[1]=list.get(i).getdescp();
			row[2]=list.get(i).getcat();
			row[3]=list.get(i).getdate();
			row[4]=list.get(i).getamt();
			sum+=list.get(i).getamt();
			model.addRow(row);
		}
		lbltotal.setText(sum.toString());
	}
	public void showsearchtable(ArrayList<exp> list) {
		DefaultTableModel model=(DefaultTableModel)maintable.getModel();
		Object row[]=new Object[5];
		Double sum=0.0;
		for(int i=0;i<list.size();i++) {
			row[0]=list.get(i).getid();
			row[1]=list.get(i).getdescp();
			row[2]=list.get(i).getcat();
			row[3]=list.get(i).getdate();
			row[4]=list.get(i).getamt();
			sum+=list.get(i).getamt();
			model.addRow(row);
		}
		lbltotal.setText(sum.toString());
	}
	public class tableListener implements MouseListener{
		public void mouseClicked(MouseEvent me) {
			int i=maintable.getSelectedRow();
			TableModel model=maintable.getModel();
			Descrecordtf.setText(model.getValueAt(i, 1).toString());
			String Category=model.getValueAt(i,2).toString();
			switch(Category) {
			case "Education":catrecordcb.setSelectedIndex(0);
			break;
			case "Food":catrecordcb.setSelectedIndex(1);
			break;
			case "Transport":catrecordcb.setSelectedIndex(2);
			break;
			case "Payment":catrecordcb.setSelectedIndex(3);
			break;
			case "Medical&Healthcare":catrecordcb.setSelectedIndex(4);
			break;
			case "Utilities":catrecordcb.setSelectedIndex(5);
			break;
			case "Travel":catrecordcb.setSelectedIndex(6);
			break;	
			}
			try {
				DefaultTableModel defmodel=(DefaultTableModel)maintable.getModel();
				int srow=maintable.getSelectedRow();
				Date date=new SimpleDateFormat("yyyy-MM-dd").parse((String)defmodel.getValueAt(srow,3));
				daterecorddc.setDate(date);
			}
			catch(Exception e) {System.out.println("no date");}
			amtrecordtf.setText(model.getValueAt(i, 4).toString());
		}
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
	}
	public class enterAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			try {
				addstatuslabel.setText("");
				String des=desctf.getText();
				String cat=(String)categorycb.getSelectedItem();
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				String date=sdf.format(addDatedc.getDate());
				Double amt=Double.valueOf(amttf.getText());
				stmt.execute("insert into maintable values('"+user+"',null,'"+des+"','"+cat+"','"+date+"',"+amt+");");////////////////////////EDITED
				DefaultTableModel model=(DefaultTableModel)maintable.getModel();
				model.setRowCount(0);
				showtable();
				JOptionPane.showMessageDialog(frame,"Data entered sucessfully");
			}
			catch(SQLException sqe) {
				JOptionPane.showMessageDialog(frame,"Details are entered incorrectly");
			}
			catch(NullPointerException npe) {
				JOptionPane.showMessageDialog(frame,"Enter all fields");
			}
			catch(NumberFormatException ne) {
				JOptionPane.showMessageDialog(frame,"Enter amount without comma and don't enter text in amount field");
			}
		}
	}
	public class clearAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			clearedthread ct=new clearedthread();
			desctf.setText("");
			categorycb.setSelectedIndex(0);
			addDatedc.setDate(null);
			amttf.setText("");
			addstatuslabel.setForeground(new Color(0, 51, 102));
			ct.start();
		}
	}
	public class clearedthread extends Thread{
		public void run() {
			try {
				addstatuslabel.setText("Cleared");
				sleep(4000);
				addstatuslabel.setText("");
			}
			catch(Exception e) {}
		}
	}
	public class updateAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			try{
				int row=maintable.getSelectedRow();
				String idno=maintable.getModel().getValueAt(row,0).toString();
				String des=Descrecordtf.getText();
				String cat=(String)catrecordcb.getSelectedItem();
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				String date=sdf.format(daterecorddc.getDate());
				Double amt=Double.valueOf(amtrecordtf.getText());
				stmt.execute("update maintable set descp='"+des+"',cat='"+cat+"',dat='"+date+"',amt="+amt+" where user_name='"+user+"' and id="+idno+";");///////////////EDITED
				Descrecordtf.setText("");
				catrecordcb.setSelectedIndex(0);
				daterecorddc.setDate(null);
				amtrecordtf.setText("");
				DefaultTableModel model=(DefaultTableModel)maintable.getModel();
				model.setRowCount(0);
				showtable();
				JOptionPane.showMessageDialog(frame,"Updated Sucessfully");
			}
			catch(SQLException sqe) {
				JOptionPane.showMessageDialog(frame,"Details are entered incorrectly");
			}
			catch(NullPointerException npe) {
				JOptionPane.showMessageDialog(frame,"Fiels cannot be empty");
			}
			catch(NumberFormatException ne) {
				JOptionPane.showMessageDialog(frame,"Enter amount without comma and don't enter text in amount field");
			}
		}
	}
	public class deleteAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			try {
				int row=maintable.getSelectedRow();
				String idno=maintable.getModel().getValueAt(row,0).toString();
				stmt.execute("delete from maintable where id="+idno+" and user_name='"+user+"';");///////////////////////EDITED
				Descrecordtf.setText("");
				catrecordcb.setSelectedIndex(0);
				daterecorddc.setDate(null);
				amtrecordtf.setText("");
				DefaultTableModel model=(DefaultTableModel)maintable.getModel();
				model.setRowCount(0);
				showtable();
				JOptionPane.showMessageDialog(frame,"Deleted sucessfully");
			}
			catch(SQLException se) {}
		}
	}
	public class searchaction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(categorychkbox.isSelected()&&!(amtchkbox.isSelected())&&!(timechkbox.isSelected())){
				ArrayList<exp> expenseList=new ArrayList<>();
				try {
					rs=stmt.executeQuery("select * from maintable where user_name='"+user+"'and cat='"+categorysrchcb.getSelectedItem()+"';");////////////EDITED
					exp expobj;
					while(rs.next()){
						expobj=new exp(rs.getInt("id"),rs.getString("descp"),rs.getString("cat"),String.valueOf(rs.getDate("dat")),rs.getInt("amt"));
						expenseList.add(expobj);
					}
				}
				catch(Exception ex) {}
				DefaultTableModel model=(DefaultTableModel)maintable.getModel();
				model.setRowCount(0);
				showsearchtable(expenseList);
			}
			else if(!(categorychkbox.isSelected())&&amtchkbox.isSelected()&&!(timechkbox.isSelected())) {
				ArrayList<exp> expenseList=new ArrayList<>();
				if(amtrangecb.getSelectedItem().equals("<1000")) {
					try {
						rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and amt<1000;");/////////////EDITED V2
						exp expobj;
						while(rs.next()){
							expobj=new exp(rs.getInt("id"),rs.getString("descp"),rs.getString("cat"),String.valueOf(rs.getDate("dat")),rs.getInt("amt"));
							expenseList.add(expobj);
						}
					}
					catch(Exception ex) {}
				}
				else if(amtrangecb.getSelectedItem().equals("1000-3000")) {
					try {
						rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and amt>=1000 and amt<3000;");///////////EDITED v2
						exp expobj;
						while(rs.next()){
							expobj=new exp(rs.getInt("id"),rs.getString("descp"),rs.getString("cat"),String.valueOf(rs.getDate("dat")),rs.getInt("amt"));
							expenseList.add(expobj);
						}
					}
					catch(Exception ex) {}
				}
				else if(amtrangecb.getSelectedItem().equals("3000-6000")) {
					try {
						rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and amt>=3000 and amt<6000;");/////////////EDITED V2
						exp expobj;
						while(rs.next()){
							expobj=new exp(rs.getInt("id"),rs.getString("descp"),rs.getString("cat"),String.valueOf(rs.getDate("dat")),rs.getInt("amt"));
							expenseList.add(expobj);
						}
					}
					catch(Exception ex) {}
				}
				else if(amtrangecb.getSelectedItem().equals("6000-10000")) {
					try {
						rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and amt>=6000 and amt<10000;");///////////EDITED v2
						exp expobj;
						while(rs.next()){
							expobj=new exp(rs.getInt("id"),rs.getString("descp"),rs.getString("cat"),String.valueOf(rs.getDate("dat")),rs.getInt("amt"));
							expenseList.add(expobj);
						}
					}
					catch(Exception ex) {}
				}
				else if(amtrangecb.getSelectedItem().equals("10000-20000")) {
					try {
						rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and amt>=10000 and amt<20000;");////////////EDITED v2
						exp expobj;
						while(rs.next()){
							expobj=new exp(rs.getInt("id"),rs.getString("descp"),rs.getString("cat"),String.valueOf(rs.getDate("dat")),rs.getInt("amt"));
							expenseList.add(expobj);
						}
					}
					catch(Exception ex) {}
				}
				else if(amtrangecb.getSelectedItem().equals("20000-30000")) {
					try {
						rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and amt>=20000 and amt<30000;");/////////EDITED v2
						exp expobj;
						while(rs.next()){
							expobj=new exp(rs.getInt("id"),rs.getString("descp"),rs.getString("cat"),String.valueOf(rs.getDate("dat")),rs.getInt("amt"));
							expenseList.add(expobj);
						}
					}
					catch(Exception ex) {}
				}
				else {
					try {
						rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and amt>=30000;");//////////////EDITED v2
						exp expobj;
						while(rs.next()){
							expobj=new exp(rs.getInt("id"),rs.getString("descp"),rs.getString("cat"),String.valueOf(rs.getDate("dat")),rs.getInt("amt"));
							expenseList.add(expobj);
						}
					}
					catch(Exception ex) {}
				}
				DefaultTableModel model=(DefaultTableModel)maintable.getModel();
				model.setRowCount(0);
				showsearchtable(expenseList);
			}
			else if(!(categorychkbox.isSelected())&&!(amtchkbox.isSelected())&&timechkbox.isSelected()) {
				int bgt=0;
				Double t=0.0,te=0.0,tf=0.0,tt=0.0,tp=0.0,tu=0.0,ttr=0.0,tm=0.0;
				ArrayList<exp> expenseList=new ArrayList<>();
				try {
					if(timemonthsrch.getMonth()<9) {
						rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and dat like '%-0"+(timemonthsrch.getMonth()+1)+"-%' and dat like '%"+timeyearsrch.getYear()+"%';");
					}/////////EDITED v2
					else {
						rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and dat like '%-"+(timemonthsrch.getMonth()+1)+"-%' and dat like '%"+timeyearsrch.getYear()+"-%';");
					}///////EDITED v2
					exp expobj;
					while(rs.next()){
						expobj=new exp(rs.getInt("id"),rs.getString("descp"),rs.getString("cat"),String.valueOf(rs.getDate("dat")),rs.getInt("amt"));
						expenseList.add(expobj);
						t+=rs.getInt("amt");
						switch(rs.getString("cat")) {
						case "Education":te+=rs.getInt("amt");
						break;
						case "Food":tf+=rs.getInt("amt");
						break;
						case "Transport":tt+=rs.getInt("amt");
						break;
						case "Payment":tp+=rs.getInt("amt");
						break;
						case "Medical&Healthcare":tm+=rs.getInt("amt");
						break;
						case "Utilities":tu+=rs.getInt("amt");
						break;
						case "Travel":ttr+=rs.getInt("amt");
						break;
						}
					}
					cstmt2.setString(1,user);
					cstmt2.setDouble(2,t);
					cstmt2.setDouble(3,te);
					cstmt2.setDouble(4,tf);
					cstmt2.setDouble(5,tt);
					cstmt2.setDouble(6,tp);
					cstmt2.setDouble(7,tm);
					cstmt2.setDouble(8,tu);
					cstmt2.setDouble(9,ttr);
					cstmt2.execute();
					rs=stmt.executeQuery("select bgt from acdetails where user_name='"+user+"';");
					while(rs.next()) {
						bgt=rs.getInt("bgt");
					}	
				}
				catch(Exception ex) {}
				DefaultTableModel model=(DefaultTableModel)maintable.getModel();
				model.setRowCount(0);
				showsearchtable(expenseList);
				if(bgt!=-1) {
					if(bgt < Double.parseDouble(lbltotal.getText())) {
						lblBgtIndi.setForeground(Color.RED);
						lblBgtIndi.setText("Rs. "+((Double.parseDouble(lbltotal.getText()))-bgt)+" (>Budget)");
					}
					else if(bgt > Double.parseDouble(lbltotal.getText())) {
						lblBgtIndi.setForeground(new Color(50, 205, 50));
						lblBgtIndi.setText("Rs. "+(bgt-Double.parseDouble(lbltotal.getText()))+" (<Budget)");
					}
					else {
						lblBgtIndi.setForeground(Color.GRAY);
						lblBgtIndi.setText("Budget is over");
					}
				}
				else {
					lblBgtIndi.setForeground(Color.GRAY);
					lblBgtIndi.setText("Budget not set");
				}
				percchpanel.removeAll();
				percchpanel.add(btnPieChartMonth);
				percchpanel.updateUI();
			}
			else if(categorychkbox.isSelected()&&amtchkbox.isSelected()&&!(timechkbox.isSelected())) {
				ArrayList<exp> expenseList=new ArrayList<>();
				if(amtrangecb.getSelectedItem().equals("<1000")) {
					try {
						rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and amt<1000 and cat='"+categorysrchcb.getSelectedItem()+"';");//////////EDITED v2
						exp expobj;
						while(rs.next()){
							expobj=new exp(rs.getInt("id"),rs.getString("descp"),rs.getString("cat"),String.valueOf(rs.getDate("dat")),rs.getInt("amt"));
							expenseList.add(expobj);
						}
					}
					catch(Exception ex) {}
				}
				else if(amtrangecb.getSelectedItem().equals("1000-3000")) {
					try {
						rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and amt>=1000 and amt<3000 and cat='"+categorysrchcb.getSelectedItem()+"';");
						exp expobj;/////////////EDITED v2
						while(rs.next()){
							expobj=new exp(rs.getInt("id"),rs.getString("descp"),rs.getString("cat"),String.valueOf(rs.getDate("dat")),rs.getInt("amt"));
							expenseList.add(expobj);
						}
					}
					catch(Exception ex) {}
				}
				else if(amtrangecb.getSelectedItem().equals("3000-6000")) {
					try {
						rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and amt>=3000 and amt<6000 and cat='"+categorysrchcb.getSelectedItem()+"';");
						exp expobj;/////////////EDITED v2
						while(rs.next()){
							expobj=new exp(rs.getInt("id"),rs.getString("descp"),rs.getString("cat"),String.valueOf(rs.getDate("dat")),rs.getInt("amt"));
							expenseList.add(expobj);
						}
					}
					catch(Exception ex) {}
				}
				else if(amtrangecb.getSelectedItem().equals("6000-10000")) {
					try {
						rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and amt>=6000 and amt<10000 and cat='"+categorysrchcb.getSelectedItem()+"';");
						exp expobj;
						while(rs.next()){
							expobj=new exp(rs.getInt("id"),rs.getString("descp"),rs.getString("cat"),String.valueOf(rs.getDate("dat")),rs.getInt("amt"));
							expenseList.add(expobj);
						}
					}
					catch(Exception ex) {}
				}
				else if(amtrangecb.getSelectedItem().equals("10000-20000")) {
					try {
						rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and amt>=10000 and amt<20000 and cat='"+categorysrchcb.getSelectedItem()+"';");
						exp expobj;////EDITED v2
						while(rs.next()){
							expobj=new exp(rs.getInt("id"),rs.getString("descp"),rs.getString("cat"),String.valueOf(rs.getDate("dat")),rs.getInt("amt"));
							expenseList.add(expobj);
						}
					}
					catch(Exception ex) {}
				}
				else if(amtrangecb.getSelectedItem().equals("20000-30000")) {
					try {
						rs=stmt.executeQuery("select * from maintable where user_name="+user+"' and amt>=20000 and amt<30000 and cat='"+categorysrchcb.getSelectedItem()+"';");
						exp expobj;/////EDITED v2
						while(rs.next()){
							expobj=new exp(rs.getInt("id"),rs.getString("descp"),rs.getString("cat"),String.valueOf(rs.getDate("dat")),rs.getInt("amt"));
							expenseList.add(expobj);
						}
					}
					catch(Exception ex) {}
				}
				else {
					try {
						rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and amt>=30000 and cat='"+categorysrchcb.getSelectedItem()+"';");
						exp expobj;/////EDITED v2
						while(rs.next()){
							expobj=new exp(rs.getInt("id"),rs.getString("descp"),rs.getString("cat"),String.valueOf(rs.getDate("dat")),rs.getInt("amt"));
							expenseList.add(expobj);
						}
					}
					catch(Exception ex) {}
				}
				DefaultTableModel model=(DefaultTableModel)maintable.getModel();
				model.setRowCount(0);
				showsearchtable(expenseList);
				
			}
			else if(categorychkbox.isSelected()&&!(amtchkbox.isSelected())&&timechkbox.isSelected()) {
				ArrayList<exp> expenseList=new ArrayList<>();
				try {
					if(timemonthsrch.getMonth()<9) {
						rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and dat like '%-0"+(timemonthsrch.getMonth()+1)+"-%' and dat like '%"+timeyearsrch.getYear()+"%' and cat='"+categorysrchcb.getSelectedItem()+"';");
					}/////////EDITED v2
					else {
						rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and dat like '%-"+(timemonthsrch.getMonth()+1)+"-%' and dat like '%"+timeyearsrch.getYear()+"-%' and cat='"+categorysrchcb.getSelectedItem()+"';");
					}////////EDITED v2
					exp expobj;
					while(rs.next()){
						expobj=new exp(rs.getInt("id"),rs.getString("descp"),rs.getString("cat"),String.valueOf(rs.getDate("dat")),rs.getInt("amt"));
						expenseList.add(expobj);
					}
				}
				catch(Exception ex) {}
				DefaultTableModel model=(DefaultTableModel)maintable.getModel();
				model.setRowCount(0);
				showsearchtable(expenseList);
				
			}
			else if(!(categorychkbox.isSelected())&&amtchkbox.isSelected()&&timechkbox.isSelected()) {
				ArrayList<exp> expenseList=new ArrayList<>();
				if(amtrangecb.getSelectedItem().equals("<1000")) {
					try {
						if(timemonthsrch.getMonth()<9) {
							rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and dat like '%-0"+(timemonthsrch.getMonth()+1)+"-%' and dat like '%"+timeyearsrch.getYear()+"%' and amt<1000;");
						}//////EDITED v2
						else {
							rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and dat like '%-"+(timemonthsrch.getMonth()+1)+"-%' and dat like '%"+timeyearsrch.getYear()+"-%' and amt<1000;");
						}//////EDITED v2
						exp expobj;
						while(rs.next()){
							expobj=new exp(rs.getInt("id"),rs.getString("descp"),rs.getString("cat"),String.valueOf(rs.getDate("dat")),rs.getInt("amt"));
							expenseList.add(expobj);
						}
					}
					catch(Exception ex) {}
				}
				else if(amtrangecb.getSelectedItem().equals("1000-3000")) {
					try {
						if(timemonthsrch.getMonth()<9) {
							rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and dat like '%-0"+(timemonthsrch.getMonth()+1)+"-%' and dat like '%"+timeyearsrch.getYear()+"%' and amt>=1000 and amt<3000;");
						}
						else {
							rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and dat like '%-"+(timemonthsrch.getMonth()+1)+"-%' and dat like '%"+timeyearsrch.getYear()+"-%' and amt>=1000 and amt<3000;");
						}/////////EDITED v2
						exp expobj;
						while(rs.next()){
							expobj=new exp(rs.getInt("id"),rs.getString("descp"),rs.getString("cat"),String.valueOf(rs.getDate("dat")),rs.getInt("amt"));
							expenseList.add(expobj);
						}
					}
					catch(Exception ex) {}
				}
				else if(amtrangecb.getSelectedItem().equals("3000-6000")) {
					try {
						if(timemonthsrch.getMonth()<9) {
							rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and dat like '%-0"+(timemonthsrch.getMonth()+1)+"-%' and dat like '%"+timeyearsrch.getYear()+"%' and amt>=3000 and amt<6000;");
						}
						else {
							rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and dat like '%-"+(timemonthsrch.getMonth()+1)+"-%' and dat like '%"+timeyearsrch.getYear()+"-%' and amt>=3000 and amt<6000;");
						}///////////EDITED v2
						exp expobj;
						while(rs.next()){
							expobj=new exp(rs.getInt("id"),rs.getString("descp"),rs.getString("cat"),String.valueOf(rs.getDate("dat")),rs.getInt("amt"));
							expenseList.add(expobj);
						}
					}
					catch(Exception ex) {}
				}
				else if(amtrangecb.getSelectedItem().equals("6000-10000")) {
					try {
						if(timemonthsrch.getMonth()<9) {
							rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and dat like '%-0"+(timemonthsrch.getMonth()+1)+"-%' and dat like '%"+timeyearsrch.getYear()+"%' and amt>=6000 and amt<10000;");
						}
						else {
							rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and dat like '%-"+(timemonthsrch.getMonth()+1)+"-%' and dat like '%"+timeyearsrch.getYear()+"-%' and amt>=6000 and amt<10000;");
						}/////////////EDITED v2
						exp expobj;
						while(rs.next()){
							expobj=new exp(rs.getInt("id"),rs.getString("descp"),rs.getString("cat"),String.valueOf(rs.getDate("dat")),rs.getInt("amt"));
							expenseList.add(expobj);
						}
					}
					catch(Exception ex) {}
				}
				else if(amtrangecb.getSelectedItem().equals("10000-20000")) {
					try {
						if(timemonthsrch.getMonth()<9) {
							rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and dat like '%-0"+(timemonthsrch.getMonth()+1)+"-%' and dat like '%"+timeyearsrch.getYear()+"%' and amt>=10000 and amt<20000;");
						}
						else {
							rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and dat like '%-"+(timemonthsrch.getMonth()+1)+"-%' and dat like '%"+timeyearsrch.getYear()+"-%' and amt>=10000 and amt<20000;");
						}/////////////EDITED v2
						exp expobj;
						while(rs.next()){
							expobj=new exp(rs.getInt("id"),rs.getString("descp"),rs.getString("cat"),String.valueOf(rs.getDate("dat")),rs.getInt("amt"));
							expenseList.add(expobj);
						}
					}
					catch(Exception ex) {}
				}
				else if(amtrangecb.getSelectedItem().equals("20000-30000")) {
					try {
						if(timemonthsrch.getMonth()<9) {
							rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and dat like '%-0"+(timemonthsrch.getMonth()+1)+"-%' and dat like '%"+timeyearsrch.getYear()+"%' and amt>=20000 and amt<30000;");
						}
						else {
							rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and dat like '%-"+(timemonthsrch.getMonth()+1)+"-%' and dat like '%"+timeyearsrch.getYear()+"-%' and amt>=20000 and amt<30000;");
						}////////////EDITED v2
						exp expobj;
						while(rs.next()){
							expobj=new exp(rs.getInt("id"),rs.getString("descp"),rs.getString("cat"),String.valueOf(rs.getDate("dat")),rs.getInt("amt"));
							expenseList.add(expobj);
						}
					}
					catch(Exception ex) {}
				}
				else {
					try {
						if(timemonthsrch.getMonth()<9) {
							rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and dat like '%-0"+(timemonthsrch.getMonth()+1)+"-%' and dat like '%"+timeyearsrch.getYear()+"%' and amt>=30000;");
						}
						else {
							rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and dat like '%-"+(timemonthsrch.getMonth()+1)+"-%' and dat like '%"+timeyearsrch.getYear()+"-%' and amt>=30000;");
						}/////////EDITED v2
						exp expobj;
						while(rs.next()){
							expobj=new exp(rs.getInt("id"),rs.getString("descp"),rs.getString("cat"),String.valueOf(rs.getDate("dat")),rs.getInt("amt"));
							expenseList.add(expobj);
						}
					}
					catch(Exception ex) {}
				}
				DefaultTableModel model=(DefaultTableModel)maintable.getModel();
				model.setRowCount(0);
				showsearchtable(expenseList);
			}
			else if(categorychkbox.isSelected()&&amtchkbox.isSelected()&&timechkbox.isSelected()) {
				ArrayList<exp> expenseList=new ArrayList<>();
				if(amtrangecb.getSelectedItem().equals("<1000")) {
					try {
						if(timemonthsrch.getMonth()<9) {
							rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and dat like '%-0"+(timemonthsrch.getMonth()+1)+"-%' and dat like '%"+timeyearsrch.getYear()+"%' and amt<1000 and cat='"+categorysrchcb.getSelectedItem()+"';");
						}
						else {
							rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and dat like '%-"+(timemonthsrch.getMonth()+1)+"-%' and dat like '%"+timeyearsrch.getYear()+"-%' and amt<1000 and cat='"+categorysrchcb.getSelectedItem()+"';");
						}//////////////EDITED v2
						exp expobj;
						while(rs.next()){
							expobj=new exp(rs.getInt("id"),rs.getString("descp"),rs.getString("cat"),String.valueOf(rs.getDate("dat")),rs.getInt("amt"));
							expenseList.add(expobj);
						}
					}
					catch(Exception ex) {}
				}
				else if(amtrangecb.getSelectedItem().equals("1000-3000")) {
					try {
						if(timemonthsrch.getMonth()<9) {
							rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and dat like '%-0"+(timemonthsrch.getMonth()+1)+"-%' and dat like '%"+timeyearsrch.getYear()+"%' and amt>=1000 and amt<3000 and cat='"+categorysrchcb.getSelectedItem()+"';");
						}
						else {
							rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and dat like '%-"+(timemonthsrch.getMonth()+1)+"-%' and dat like '%"+timeyearsrch.getYear()+"-%' and amt>=1000 and amt<3000 and cat='"+categorysrchcb.getSelectedItem()+"';");
						}
						exp expobj;
						while(rs.next()){
							expobj=new exp(rs.getInt("id"),rs.getString("descp"),rs.getString("cat"),String.valueOf(rs.getDate("dat")),rs.getInt("amt"));
							expenseList.add(expobj);
						}
					}
					catch(Exception ex) {}
				}
				else if(amtrangecb.getSelectedItem().equals("3000-6000")) {
					try {
						if(timemonthsrch.getMonth()<9) {
							rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and dat like '%-0"+(timemonthsrch.getMonth()+1)+"-%' and dat like '%"+timeyearsrch.getYear()+"%' and amt>=3000 and amt<6000 and cat='"+categorysrchcb.getSelectedItem()+"';");
						}
						else {
							rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and dat like '%-"+(timemonthsrch.getMonth()+1)+"-%' and dat like '%"+timeyearsrch.getYear()+"-%' and amt>=3000 and amt<6000 and cat='"+categorysrchcb.getSelectedItem()+"';");
						}///////////////////EDITED v2
						exp expobj;
						while(rs.next()){
							expobj=new exp(rs.getInt("id"),rs.getString("descp"),rs.getString("cat"),String.valueOf(rs.getDate("dat")),rs.getInt("amt"));
							expenseList.add(expobj);
						}
					}
					catch(Exception ex) {}
				}
				else if(amtrangecb.getSelectedItem().equals("6000-10000")) {
					try {
						if(timemonthsrch.getMonth()<9) {
							rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and dat like '%-0"+(timemonthsrch.getMonth()+1)+"-%' and dat like '%"+timeyearsrch.getYear()+"%' and amt>=6000 and amt<10000 and cat='"+categorysrchcb.getSelectedItem()+"';");
						}
						else {
							rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and dat like '%-"+(timemonthsrch.getMonth()+1)+"-%' and dat like '%"+timeyearsrch.getYear()+"-%' and amt>=6000 and amt<10000 and cat='"+categorysrchcb.getSelectedItem()+"';");
						}///////////////EDITED v2
						exp expobj;
						while(rs.next()){
							expobj=new exp(rs.getInt("id"),rs.getString("descp"),rs.getString("cat"),String.valueOf(rs.getDate("dat")),rs.getInt("amt"));
							expenseList.add(expobj);
						}
					}
					catch(Exception ex) {}
				}
				else if(amtrangecb.getSelectedItem().equals("10000-20000")) {
					try {
						if(timemonthsrch.getMonth()<9) {
							rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and dat like '%-0"+(timemonthsrch.getMonth()+1)+"-%' and dat like '%"+timeyearsrch.getYear()+"%' and amt>=10000 and amt<20000 and cat='"+categorysrchcb.getSelectedItem()+"';");
						}
						else {
							rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and dat like '%-"+(timemonthsrch.getMonth()+1)+"-%' and dat like '%"+timeyearsrch.getYear()+"-%' and amt>=10000 and amt<20000 and cat='"+categorysrchcb.getSelectedItem()+"';");
						}////////////EDITED v2
						exp expobj;
						while(rs.next()){
							expobj=new exp(rs.getInt("id"),rs.getString("descp"),rs.getString("cat"),String.valueOf(rs.getDate("dat")),rs.getInt("amt"));
							expenseList.add(expobj);
						}
					}
					catch(Exception ex) {}
				}
				else if(amtrangecb.getSelectedItem().equals("20000-30000")) {
					try {
						if(timemonthsrch.getMonth()<9) {
							rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and dat like '%-0"+(timemonthsrch.getMonth()+1)+"-%' and dat like '%"+timeyearsrch.getYear()+"%' and amt>=20000 and amt<30000 and cat='"+categorysrchcb.getSelectedItem()+"';");
						}
						else {
							rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and dat like '%-"+(timemonthsrch.getMonth()+1)+"-%' and dat like '%"+timeyearsrch.getYear()+"-%' and amt>=20000 and amt<30000 and cat='"+categorysrchcb.getSelectedItem()+"';");
						}/////////////////EDITED v2
						exp expobj;
						while(rs.next()){
							expobj=new exp(rs.getInt("id"),rs.getString("descp"),rs.getString("cat"),String.valueOf(rs.getDate("dat")),rs.getInt("amt"));
							expenseList.add(expobj);
						}
					}
					catch(Exception ex) {}
				}
				else {
					try {
						if(timemonthsrch.getMonth()<9) {
							rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and dat like '%-0"+(timemonthsrch.getMonth()+1)+"-%' and dat like '%"+timeyearsrch.getYear()+"%' and and amt>=30000 and cat='"+categorysrchcb.getSelectedItem()+"';");
						}
						else {
							rs=stmt.executeQuery("select * from maintable where user_name='"+user+"' and dat like '%-"+(timemonthsrch.getMonth()+1)+"-%' and dat like '%"+timeyearsrch.getYear()+"-%' and amt>=30000 and cat='"+categorysrchcb.getSelectedItem()+"';");
						}////////////////EDITED v2
						exp expobj;
						while(rs.next()){
							expobj=new exp(rs.getInt("id"),rs.getString("descp"),rs.getString("cat"),String.valueOf(rs.getDate("dat")),rs.getInt("amt"));
							expenseList.add(expobj);
						}
					}
					catch(Exception ex) {}
				}
				DefaultTableModel model=(DefaultTableModel)maintable.getModel();
				model.setRowCount(0);
				showsearchtable(expenseList);
			}
			else {
				JOptionPane.showMessageDialog(frame,"Select atleast one field");
			}
		}		
	}
	public class srchcancelaction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			categorychkbox.setSelected(false);
			amtchkbox.setSelected(false);
			timemonthsrch.setMonth(0);
			timeyearsrch.setYear(2020);
			timechkbox.setSelected(false);
			DefaultTableModel model=(DefaultTableModel)maintable.getModel();
			model.setRowCount(0);
			showtable();
			lblBgtIndi.setText("");
			percchpanel.removeAll();
			percchpanel.add(btnPieChart);
			percchpanel.updateUI();
		}
	}
	public class frameclickAction implements MouseListener{
		public void mouseClicked(MouseEvent me) {
			Descrecordtf.setText("");
			catrecordcb.setSelectedIndex(0);
			amtrecordtf.setText("");
			daterecorddc.setDate(null);
		}
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
	}
	public class exportAction implements ActionListener{
		XSSFWorkbook excelJTableExporter=null;
		FileOutputStream fos=null;
		BufferedOutputStream bos=null;
		TableModel model;
		public void actionPerformed(ActionEvent e) {
			model=maintable.getModel();
			JFileChooser excelFileChooser=new JFileChooser("C:\\Users\\admin\\Desktop");
			excelFileChooser.setDialogTitle("Save As");
			FileNameExtensionFilter fileext=new FileNameExtensionFilter("EXCEL FILES","xls","xlsx","xlsm");
			excelFileChooser.setFileFilter(fileext);
			int excelchooser=excelFileChooser.showSaveDialog(null);
			if(excelchooser==JFileChooser.APPROVE_OPTION) {
				try {
					excelJTableExporter = new XSSFWorkbook();
					XSSFSheet excelSheet = excelJTableExporter.createSheet("JTable Sheet");
					XSSFRow eR=excelSheet.createRow(0);
					XSSFCell eC=eR.createCell(0);
					eC.setCellValue("idno");
					eC=eR.createCell(1);
					eC.setCellValue("Description");
					eC=eR.createCell(2);
					eC.setCellValue("Category");
					eC=eR.createCell(3);
					eC.setCellValue("Date");
					eC=eR.createCell(4);
					eC.setCellValue("Amount(in Rs)");
					for(int i=0;i<model.getRowCount();i++) {
						XSSFRow excelRow=excelSheet.createRow(i+1);
						for(int j=0;j<model.getColumnCount();j++) {
							XSSFCell excelCell=excelRow.createCell(j);
							excelCell.setCellValue(model.getValueAt(i,j).toString());
						}
					}
					fos=new FileOutputStream(excelFileChooser.getSelectedFile()+".xlsx");
					bos=new BufferedOutputStream(fos);
					excelJTableExporter.write(bos);
		
					JOptionPane.showMessageDialog(frame, "Saved sucessfully");
				}
				catch (FileNotFoundException e1) {} 
				catch (IOException e1) {}
				finally {
					try {
						if(bos!=null) {
							bos.close();
						}
						if(fos!=null) {
							fos.close();
						}
						if(excelJTableExporter!=null) {
							excelJTableExporter.close();
						}
					}
					catch(IOException ioe) {}
				}
			}
		}
	}
	public class piechartAction implements ActionListener{
		String pe,pf,pt,pp,pm,pu,ptr;
		String spe,spf,spt,spp,spm,spu,sptr;
		public void actionPerformed(ActionEvent e) {
			try{
				cstmt.setString(1,user);
				cstmt.execute();
				rs=stmt.executeQuery("select * from percstore where user_name='"+user+"';");
				while(rs.next()) {
					pe=String.format("%.2f",rs.getFloat("educ"));
					pf=String.format("%.2f",rs.getFloat("food"));
					pt=String.format("%.2f",rs.getFloat("trans"));
					pp=String.format("%.2f",rs.getFloat("paym"));
					pm=String.format("%.2f",rs.getFloat("medi"));
					pu=String.format("%.2f",rs.getFloat("util"));
					ptr=String.format("%.2f",rs.getFloat("trav"));
					spe=String.format("%.2f",rs.getFloat("educ_sum"));
					spf=String.format("%.2f",rs.getFloat("food_sum"));
					spt=String.format("%.2f",rs.getFloat("trans_sum"));
					spp=String.format("%.2f",rs.getFloat("paym_sum"));
					spm=String.format("%.2f",rs.getFloat("medi_sum"));
					spu=String.format("%.2f",rs.getFloat("util_sum"));
					sptr=String.format("%.2f",rs.getFloat("trav_sum"));
					JOptionPane.showMessageDialog(frame," Education----- "+pe+"% (Rs."+spe+")\n Food----- "+pf+"% (Rs."+spf+")\n Transport----- "+pt+"% (Rs."+spt+")\n Payment----- "+pp+"% (Rs."+spp+")\n Medical & Healthcare----- "+pm+"% (Rs."+spm+")\n Utilities----- "+pu+"% (Rs."+spu+")\n Travel----- "+ptr+"% (Rs."+sptr+")","Percentages",JOptionPane.PLAIN_MESSAGE);
				}
			}
			catch(Exception pe) {
			}
		}
	}
	/*public class undoAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			
		}
	}*/
	public class aceditAction implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			JPanel p=new JPanel();
			p.setLayout(new GridLayout(6,2,5,10));
			p.setBounds(0,0,100,90);
			JLabel fn=new JLabel("Enter your Full name ");
			fn.setSize(30,20);
			JLabel mi=new JLabel("Enter your Mail ID ");
			mi.setSize(30,20);
			JLabel ph=new JLabel("Enter your Phone no ");
			ph.setSize(30,20);
			JLabel bgt=new JLabel("Enter your Monthly Budget ");
			bgt.setSize(30,20);
			JLabel info1=new JLabel("*enter the only fields you want to edit");
			ph.setSize(30,20);
			JLabel emp=new JLabel(" ");
			emp.setSize(30,20);
			JLabel info2=new JLabel("*to delete all details leave all the fields empty");
			ph.setSize(30,20);
			JTextField tfn=new JTextField(15);
			JTextField tmi=new JTextField(15);
			JTextField tph=new JTextField(15);
			JTextField tbgt=new JTextField(15);
			p.add(fn);
			p.add(tfn);
			p.add(mi);
			p.add(tmi);
			p.add(ph);
			p.add(tph);
			p.add(bgt);
			p.add(tbgt);
			p.add(info1);
			p.add(emp);
			p.add(info2);
			JOptionPane.showMessageDialog(frame,p,"Edit account details",JOptionPane.PLAIN_MESSAGE);
			try {
				if(!((tfn.getText()).length()==0) && !((tmi.getText()).length()==0) && !((tph.getText()).length()==0)) {
					stmt.execute("update acdetails set Fname='"+tfn.getText()+"', mailID='"+tmi.getText()+"', phone='"+tph.getText()+"' where user_name='"+user+"';");
				}
				else if((tfn.getText().length()==0)&&!(tmi.getText().length()==0)&&!(tph.getText().length()==0)) {
					stmt.execute("update acdetails set mailID='"+tmi.getText()+"', phone='"+tph.getText()+"' where user_name='"+user+"';");
				}
				else if(!(tfn.getText().length()==0)&&(tmi.getText().length()==0)&&!(tph.getText().length()==0)) {
					stmt.execute("update acdetails set Fname='"+tfn.getText()+"', phone='"+tph.getText()+"' where user_name='"+user+"';");
				}
				else if(!(tfn.getText().length()==0)&&!(tmi.getText().length()==0)&&(tph.getText().length()==0)) {
					stmt.execute("update acdetails set Fname='"+tfn.getText()+"', mailID='"+tmi.getText()+"' where user_name='"+user+"';");
				}
				else if((tfn.getText().length()==0)&&(tmi.getText().length()==0)&&!(tph.getText().length()==0)) {
					stmt.execute("update acdetails set phone='"+tph.getText()+"' where user_name='"+user+"';");
				}
				else if(!(tfn.getText().length()==0)&&(tmi.getText().length()==0)&&(tph.getText().length()==0)) {
					stmt.execute("update acdetails set Fname='"+tfn.getText()+"' where user_name='"+user+"';");
				}
				else if((tfn.getText().length()==0)&&!(tmi.getText().length()==0)&&(tph.getText().length()==0)) {
					stmt.execute("update acdetails set mailID='"+tmi.getText()+"' where user_name='"+user+"';");
				}
				else if(tfn.getText().length()==0 && (tmi.getText()).length()==0 && (tph.getText()).length()==0 && (tbgt.getText()).length()==0){
					int input=JOptionPane.showConfirmDialog(frame, "Delete your account details?","Delete",JOptionPane.YES_NO_OPTION);
				    if(input==0) {
				    	stmt.execute("update acdetails set Fname=default,mailID=default,phone=default,bgt=default where user_name='"+user+"';");
				    }
				}
				else {}
				if(!(tbgt.getText().length()==0)) {
					try {
						if(Integer.parseInt(tbgt.getText())<0) {
							JOptionPane.showMessageDialog(frame,"Budget should not be less than 0","Incorrect Monthly Budget",JOptionPane.ERROR_MESSAGE);
						}
						else {
							stmt.execute("update acdetails set bgt="+Integer.parseInt(tbgt.getText())+" where user_name='"+user+"';");
						}
					}
					catch(NumberFormatException nfe) {
						JOptionPane.showMessageDialog(frame,"Budget should be a number\n Or should not contain special characters","Incorrect Monthly Budget",JOptionPane.ERROR_MESSAGE);
					}
				}
				rs=stmt.executeQuery("Select * from acdetails where user_name='"+user+"';");
				while(rs.next()) {
					lblFullName.setText("Full name: "+rs.getString("Fname"));
					lblMailID.setText("Mail ID: "+rs.getString("mailID"));
					lblPhone.setText("Phone: "+rs.getString("phone"));
					if(rs.getInt("bgt")>=0) {
						lblBudget.setText("Monthly Budget: "+rs.getInt("bgt"));
					}
					else {
						lblBudget.setText("Monthly Budget: Not set");
					}
				}
			}
			catch(SQLException ace) {}
		}
	}
	public class deleteaccountAction implements ActionListener{
		public void actionPerformed(ActionEvent ae) {
			try {
				int input=JOptionPane.showConfirmDialog(frame,"This will delete all your data\nAre you sure?","Delete Account",JOptionPane.YES_NO_OPTION);
				if(input==0) {
					stmt.execute("delete from logindetails where username='"+user+"';");
					JOptionPane.showMessageDialog(frame,"Account Deleted Sucessfully!","Expense Register",JOptionPane.OK_OPTION);
					frame.setVisible(false);
				}
			}
			catch(SQLException se) {
			}
		}
	}
	public class piechartActionMonthly implements ActionListener{
		public void actionPerformed(ActionEvent ae) {
			try {
				String pe,pf,pt,pp,pm,pu,ptr;
				String spe,spf,spt,spp,spm,spu,sptr;
				rs=stmt.executeQuery("select * from percstore where user_name='"+user+"';");
				while(rs.next()) {
					pe=String.format("%.2f",rs.getFloat("educ"));
					pf=String.format("%.2f",rs.getFloat("food"));
					pt=String.format("%.2f",rs.getFloat("trans"));
					pp=String.format("%.2f",rs.getFloat("paym"));
					pm=String.format("%.2f",rs.getFloat("medi"));
					pu=String.format("%.2f",rs.getFloat("util"));
					ptr=String.format("%.2f",rs.getFloat("trav"));
					spe=String.format("%.2f",rs.getFloat("educ_sum"));
					spf=String.format("%.2f",rs.getFloat("food_sum"));
					spt=String.format("%.2f",rs.getFloat("trans_sum"));
					spp=String.format("%.2f",rs.getFloat("paym_sum"));
					spm=String.format("%.2f",rs.getFloat("medi_sum"));
					spu=String.format("%.2f",rs.getFloat("util_sum"));
					sptr=String.format("%.2f",rs.getFloat("trav_sum"));
					JOptionPane.showMessageDialog(frame," Education----- "+pe+"% (Rs."+spe+")\n Food----- "+pf+"% (Rs."+spf+")\n Transport----- "+pt+"% (Rs."+spt+")\n Payment----- "+pp+"% (Rs."+spp+")\n Medical & Healthcare----- "+pm+"% (Rs."+spm+")\n Utilities----- "+pu+"% (Rs."+spu+")\n Travel----- "+ptr+"% (Rs."+sptr+")","Percentages",JOptionPane.PLAIN_MESSAGE);
				}
			}
			catch(SQLException sqe) {
			}
		}
			
	}
}
