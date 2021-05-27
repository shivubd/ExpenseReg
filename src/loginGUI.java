import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Image;

import javax.swing.border.MatteBorder;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.*;
import java.sql.Connection;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;


public class loginGUI {
	private JFrame mainf;
	private JTextField ltusername;
	private JPasswordField lppassword;
	private JTextField stusername;
	private JPasswordField sppassword;
	private JPasswordField spconfirm;
	private String username;
	private String password;
	private Connection conn=null;
	private Statement stmt=null;
	private ResultSet rs=null;
	private JLabel lerror;
	private JLabel serror;
	private JPanel bgpanel;
	private JPanel signin;
	private JPanel login;
	public static void main(String args[]) {
		new loginGUI();
	}
	public loginGUI() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/users","root","root");
			stmt=conn.createStatement();
			Image mainicon=new ImageIcon(this.getClass().getResource("/mainicon.png")).getImage();
			mainf = new JFrame();
			mainf.setIconImage(mainicon);
			mainf.setBackground(Color.WHITE);
			mainf.setSize(600,400);
			mainf.setUndecorated(true);
			Toolkit tk=Toolkit.getDefaultToolkit();
			int x=(int)tk.getScreenSize().getWidth();
			int y=(int)tk.getScreenSize().getHeight();
			mainf.setLocation(((x/2)-300),((y/2)-200));
			mainf.getContentPane().setLayout(null);
		
			JPanel mainpanel = new JPanel();//This is the main panel on which "Welcome!" is there
			mainpanel.setLayout(null);
			mainpanel.setBackground(new Color(0, 51, 102));
			mainpanel.setBounds(6, 6, 588, 388);
		
			mainf.getContentPane().add(mainpanel);
		
			bgpanel = new JPanel();//This is a panel on which login and Signup panels are switched
			bgpanel.setLayout(null);
			bgpanel.setBackground(new Color(0, 51, 102));
			bgpanel.setBounds(237, 22, 341, 366);
			mainpanel.add(bgpanel);
		
		/*******************THIS IS SIGNUP PANEL DESIGN**************************/
			signin= new JPanel();
			signin.setBounds(10, 24, 321, 331);
			signin.setLayout(null);
			signin.setForeground(Color.LIGHT_GRAY);
			signin.setBorder(new MatteBorder(4, 4, 4, 4, (Color) Color.LIGHT_GRAY));
			signin.setBackground(new Color(0, 51, 102));
		
			JLabel slabel = new JLabel("Create new account");               //label Create new account
			slabel.setHorizontalAlignment(SwingConstants.CENTER);
			slabel.setForeground(Color.LIGHT_GRAY);
			slabel.setFont(new Font("Coolvetica Rg", Font.PLAIN, 20));
			slabel.setBounds(57, 34, 207, 35);
			signin.add(slabel);
			
			JLabel susername = new JLabel("Username");                      //label username
			susername.setForeground(Color.WHITE);
			susername.setBounds(57, 92, 76, 14);
			signin.add(susername);
			
			stusername = new JTextField();                                  //textfield for user name
			stusername.setForeground(Color.WHITE);
			stusername.setBackground(new Color(0, 51, 102));
			stusername.setBounds(57, 105, 207, 20);
			signin.add(stusername);
			stusername.setColumns(10);
			
			JLabel spassword = new JLabel("Password");                      //label password
			spassword.setForeground(Color.WHITE);
			spassword.setBounds(57, 136, 76, 14);
			signin.add(spassword);
		
			JLabel sconfirm = new JLabel("Confirm Password");               //label confirm passwrod
			sconfirm.setForeground(Color.WHITE);
			sconfirm.setBounds(57, 179, 107, 14);
			signin.add(sconfirm);
			
			sppassword = new JPasswordField();                             //JPasswordField for passwrod
			sppassword.setForeground(Color.WHITE);
			sppassword.setBackground(new Color(0, 51, 102));
			sppassword.setBounds(57, 148, 207, 20);
			signin.add(sppassword);
			
			spconfirm = new JPasswordField();                               //JPasswordField for confirm passwrod
			spconfirm.setBackground(new Color(0, 51, 102));
			spconfirm.setForeground(Color.WHITE);
			spconfirm.setBounds(57, 193, 207, 20);
			signin.add(spconfirm);
			
			JButton btncreate = new JButton("Create account");
			btncreate.setForeground(new Color(0, 51, 102));                 //button for create account
			btncreate.setFont(new Font("Tahoma", Font.BOLD, 11));
			btncreate.setBounds(57, 244, 139, 23);
			signin.add(btncreate);
			btncreate.addActionListener(new signupaction());
			
			JButton btncancel = new JButton("Cancel");  
			btncancel.setForeground(new Color(0, 51, 102));                 //cancel button, this switches back to login panel
			btncancel.setFont(new Font("Tahoma", Font.BOLD, 11));
			btncancel.setBounds(57, 278, 71, 23); 
			signin.add(btncancel);
			
			serror = new JLabel();
			serror.setForeground(Color.RED);
			serror.setBounds(57, 219, 207, 14);
			signin.add(serror);
			
			
			/****************THIS IS LOGIN PANEL DESIGN************************/
			login = new JPanel();
			login.setBounds(10, 24, 321, 331);
			bgpanel.add(login);
			login.setForeground(Color.LIGHT_GRAY);
			login.setBorder(new MatteBorder(4, 4, 4, 4, (Color) Color.LIGHT_GRAY));
			login.setLayout(null);
			login.setBackground(new Color(0, 51, 102));
			
			JLabel llabel = new JLabel("Log In");                            //Log In label
			llabel.setForeground(Color.LIGHT_GRAY);
			llabel.setHorizontalAlignment(SwingConstants.CENTER);
			llabel.setFont(new Font("Coolvetica Rg", Font.PLAIN, 20));
			llabel.setBounds(107, 34, 107, 35);
			login.add(llabel);
			
			ltusername = new JTextField();                                   //username textfield
			ltusername.setForeground(Color.WHITE);
			ltusername.setBackground(new Color(0, 51, 102));
			ltusername.setBounds(58, 117, 204, 20);
			login.add(ltusername);
			ltusername.setColumns(10);
			
			JLabel lusername = new JLabel("Username");                       //label username
			lusername.setForeground(Color.WHITE);
			lusername.setBounds(58, 103, 87, 14);
			login.add(lusername);
			
			JLabel lpassword = new JLabel("Password");                       //label password
			lpassword.setForeground(Color.WHITE);
			lpassword.setBounds(58, 152, 87, 14);
			login.add(lpassword);
			
			lppassword = new JPasswordField();                               //JPassword for password
			lppassword.setForeground(Color.WHITE);
			lppassword.setBackground(new Color(0, 51, 102));
			lppassword.setBounds(58, 166, 204, 20);
			login.add(lppassword);
			
			JButton btnlogin = new JButton("Log In");                        //login button
			btnlogin.setForeground(new Color(0, 51, 102));
			btnlogin.setFont(new Font("Tahoma", Font.BOLD, 11));
			btnlogin.setBounds(58, 197, 89, 23);
			login.add(btnlogin);
			btnlogin.addActionListener(new loginaction());
			
			JButton btnsignup = new JButton("Sign Up");                     //sign up button to switch to signup panel
			btnsignup.setForeground(new Color(0, 51, 102));
			btnsignup.setFont(new Font("Tahoma", Font.BOLD, 11));
			btnsignup.setBounds(56, 271, 89, 23);
			login.add(btnsignup);
			btnsignup.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					lerror.setText("");
					ltusername.setText("");
					lppassword.setText("");
					bgpanel.removeAll();
					bgpanel.add(signin);
					bgpanel.updateUI();
				}
			});
		
			JLabel lnewuser = new JLabel("New user?");                      //label New User?
			lnewuser.setForeground(Color.WHITE);
			lnewuser.setBounds(58, 257, 87, 14);
			login.add(lnewuser);
			
			lerror = new JLabel();                   //label for error print  in red color
			lerror.setForeground(Color.RED);
			lerror.setBounds(58, 232, 204, 14);
			login.add(lerror);
			
			btncancel.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					serror.setText("");
					stusername.setText("");
					sppassword.setText("");
					spconfirm.setText("");
					bgpanel.removeAll();
					bgpanel.add(login);
					bgpanel.updateUI();
				}
			});
		
			JLabel logopic = new JLabel();
			Image imglogo=new ImageIcon(this.getClass().getResource("/Desc.png")).getImage();
			logopic.setIcon(new ImageIcon(imglogo));
			logopic.setBounds(10, 11, 228, 75);
			mainpanel.add(logopic);
			
			JTextPane taDesc = new JTextPane();
			taDesc.setFont(new Font("Tahoma", Font.PLAIN, 11));
			taDesc.setForeground(Color.WHITE);
			taDesc.setText("Welcome to Expense Register Software.\r\nIn this software you can store your expense records and fetch them in future, and use many more features. \r\n\r\nExpense Register\r\nAn Open Source project\r\nDeveloped By:  Shivaraj D and  Amit T\r\n\r\n\r\nDBMS:  Oracle MySQL\r\nProgrammed using:");
			taDesc.setEditable(false);
			taDesc.setBackground(new Color(0, 51, 102));
			taDesc.setBounds(10, 97, 228, 174);
			mainpanel.add(taDesc);
			
			JLabel javalogo = new JLabel();
			javalogo.setHorizontalAlignment(SwingConstants.CENTER);
			Image imgjava = new ImageIcon(this.getClass().getResource("/java.png")).getImage();
			javalogo.setIcon(new ImageIcon(imgjava));
			javalogo.setBounds(10, 267, 59, 63);
			mainpanel.add(javalogo);
			
			JButton closebtn = new JButton();
			Image closeimg=new ImageIcon(this.getClass().getResource("/closebtn.png")).getImage();
			closebtn.setIcon(new ImageIcon(closeimg));
			closebtn.setForeground(new Color(0, 51, 102));
			closebtn.setBackground(new Color(0, 51, 102));
			closebtn.setBounds(572, 0, 16, 16);
			mainpanel.add(closebtn);
			closebtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(conn!=null){
						try {
							conn.close();
						}
						catch(SQLException se) {}
					}
					mainf.setVisible(false);
				}
			});
			mainf.setResizable(false);
			mainf.setVisible(true);
			mainf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		catch(Exception e) {}
		finally {}
	}
	class loginaction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			try {
				username=ltusername.getText();
				password=String.valueOf((lppassword.getPassword()));
				stmt.execute("select * from logindetails where username='"+username+"'and password='"+password+"';");
				rs=stmt.getResultSet();
				if(rs.next()) {
					ltusername.setText("");
					lppassword.setText("");
					JOptionPane.showMessageDialog(mainf,"Login sucessfull");
					mainf.setVisible(false);
					new mainUI(username);
				}
				else {
					ltusername.setText("");
					lppassword.setText("");
					lerrorthread lt=new lerrorthread();
					lt.start();
				}
			}
			catch(Exception se) {			
			}
			finally {}
		}
	}
	class lerrorthread extends Thread{
		public void run() {
			try {
				lerror.setText("username or password wrong");
				sleep(4000);
				lerror.setText("");
			} catch (InterruptedException e) {}
		}
	}
	class signupaction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			try {
				if((stusername.getText().length()!=0)&&(String.valueOf(sppassword.getPassword())).length()!=0 && (String.valueOf(spconfirm.getPassword())).length()!=0) {
					if(isallwhitespace(stusername.getText())&&isallwhitespace(String.valueOf(sppassword.getPassword()))&&isallwhitespace(String.valueOf(spconfirm.getPassword()))&&(stusername.getText().indexOf(' ')==-1)&&(String.valueOf(sppassword.getPassword()).indexOf(' ')==-1)&&(String.valueOf(spconfirm.getPassword()).indexOf(' ')==-1)) {
						if(String.valueOf(sppassword.getPassword()).length()>3){
							if(String.valueOf(sppassword.getPassword()).equals(String.valueOf(spconfirm.getPassword()))){
								stmt.execute("insert into logindetails values('"+stusername.getText()+"','"+String.valueOf(spconfirm.getPassword())+"');");
								stmt.execute("insert into percstore values('"+stusername.getText()+"',0,0,0,0,0,0,0);");/////////////EDITED v2
								stmt.execute("insert into acdetails values('"+stusername.getText()+"',default,default,default);");
								JOptionPane.showMessageDialog(mainf,"Account created sucessfully");
								bgpanel.removeAll();
								bgpanel.add(login);
								bgpanel.updateUI();
							}
							else {
								stusername.setText("");
								sppassword.setText("");
								spconfirm.setText("");
								Thread ce=new Thread(){
									public void run() {
										try {
											serror.setText("confirm password is not matching");
											sleep(4000);
											serror.setText("");
										}
										catch (InterruptedException e) {}
									}
								};
								ce.start();
							}
						}
						else {
							stusername.setText("");
							sppassword.setText("");
							spconfirm.setText("");
							Thread pl=new Thread() {
								public void run() {
									try {
										serror.setText("password should be >3 characters");
										sleep(4000);
										serror.setText("");
									}
									catch (InterruptedException e) {}
								}
							};
							pl.start();
						}
					}
					else {
						stusername.setText("");
						sppassword.setText("");
						spconfirm.setText("");
						Thread fs=new Thread() {
							public void run() {
								try {
									serror.setText("fields cannot contain spaces");
									sleep(4000);
									serror.setText("");
								}
								catch(InterruptedException e) {}
							}
						};
						fs.start();
					}
				}
				else {
					stusername.setText("");
					sppassword.setText("");
					spconfirm.setText("");
					Thread ef=new Thread() {
						public void run() {
							try {
								serror.setText("Enter all fields");
								sleep(4000);
								serror.setText("");
							}
							catch(InterruptedException e) {}
						}
					};
					ef.start();
				}
			}
			catch(SQLException se) {
				stusername.setText("");
				sppassword.setText("");
				spconfirm.setText("");
				Thread un=new Thread(){
					public void run() {
						try {
							serror.setText("username already in use");
							sleep(4000);
							serror.setText("");
						}
						catch(InterruptedException e) {}
					}
				};
				un.start();
			}
			finally {}
		}
		public boolean isallwhitespace(String str) {
			if(str.trim().isEmpty())
				return false;
			else
				return true;
		}
	}
	public String getUser() {
		return username;
	}
}

