import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.*;


public class project extends JFrame implements ActionListener
{
	//Components
	//LoginPage
	static JFrame jflogin = new JFrame("Login Page");
	static JLabel jlusername = new JLabel("Username :");
	static JLabel jlpassword = new JLabel("Password :");
	static JTextField jtfusername = new JTextField(15);
	static JTextField jtfpassword = new JTextField(15);
	static JButton signin = new JButton("Sign In");
	static JButton signup = new JButton("Sign up");	
	static GridLayout glloginpage = new GridLayout(3,2,5,5);	
	
	//SignupPage
	static JFrame jfsignup = new JFrame("Signup Page");
	static JLabel jlusernamesignup = new JLabel("Username :");
	static JLabel jlpasswordsignup = new JLabel("Password :");
	static JLabel jladhar = new JLabel("Aadhar Number :");
	static JTextField jtfusernamesignup = new JTextField(15);
	static JTextField jtfpasswordsignup = new JTextField(15);
	static JTextField jtfadhar = new JTextField(15);
	static JButton register = new JButton("Register");
	static GridLayout glsignuppage = new GridLayout(4,2,5,5);
		
	//MainFrame
	static JFrame jfmain = new JFrame("Main Page");
	static JLabel jlfrom = new JLabel("From :");
	static JTextField jtffrom = new JTextField(15);
	static JTextField jtfto = new JTextField(15);
	static JLabel jlto = new JLabel("To :"); 
	static JButton findtrains = new JButton("Find Trains");
	static JButton checkstatus = new JButton("PNR STATUS");
	static JLabel date = new JLabel("Date : dd/mm/yyyy");
	static JTextField dateday = new JTextField();
	static JTextField datemonth = new JTextField();
	static JTextField dateyear = new JTextField();   
	static GridLayout glmainpage = new GridLayout(4,2,5,5);
	
	static FileWriter fw;
	static FileReader fr;
	
	public static boolean verify(String input)
	{
		if(input.isEmpty())
		return false;
		return true;
	} 

	public static void main(String args[])
	{
		//Component adjustment
		//LoginPage
		signup.setFont(new Font("Monaco",Font.BOLD,40));
		jlusername.setFont(new Font("Monaco",Font.BOLD,40));
		jlpassword.setFont(new Font("Monaco",Font.BOLD,40));	
		signin.setFont(new Font("Monaco",Font.BOLD,40));
		jtfusername.setFont(new Font("Monaco",Font.BOLD,40));
		jtfpassword.setFont(new Font("Monaco",Font.BOLD,40));		
		jflogin.setSize(1000,500);
		jflogin.setLayout(glloginpage);

		//SignupPage
		jlusernamesignup.setFont(new Font("Monaco",Font.BOLD,40));
		register.setFont(new Font("Monaco",Font.BOLD,40));
		jtfadhar.setFont(new Font("Monaco",Font.BOLD,40));
		jtfpasswordsignup.setFont(new Font("Monaco",Font.BOLD,40));
		jtfusernamesignup.setFont(new Font("Monaco",Font.BOLD,40));
		jladhar.setFont(new Font("Monaco",Font.BOLD,40));
		jlpasswordsignup.setFont(new Font("Monaco",Font.BOLD,40));
		jfsignup.setSize(800,500);
		jfsignup.setLayout(glsignuppage);
		
		
		//MainPage
		jlfrom.setFont(new Font("Monaco",Font.BOLD,40));
		jlto.setFont(new Font("Monaco",Font.BOLD,40));
		jtffrom.setFont(new Font("Monaco",Font.BOLD,40));
		jtfto.setFont(new Font("Monaco",Font.BOLD,40));
		findtrains.setFont(new Font("Monaco",Font.BOLD,40));
		checkstatus.setFont(new Font("Monaco",Font.BOLD,40));
		date.setFont(new Font("Monaco",Font.BOLD,40));
		dateday.setFont(new Font("Monaco",Font.BOLD,40));
		datemonth.setFont(new Font("Monaco",Font.BOLD,40));
		dateyear.setFont(new Font("Monaco",Font.BOLD,40));
		jfmain.setSize(1000,800);
		jfmain.setLayout(glmainpage);
		
		try
		{
			fw = new FileWriter("Logincsv",true);
			fr = new FileReader("Logincsv");
		}catch(Exception emerald)
		{
		
		}
		
		//Component added
		//LoginPage
		jflogin.add(jlusername);
		jflogin.add(jtfusername);
		jflogin.add(jlpassword);
		jflogin.add(jtfpassword);
		jflogin.add(signin);
		jflogin.add(signup);
		
		//SignupPage
		jfsignup.add(jlusernamesignup);
		jfsignup.add(jtfusernamesignup);
		jfsignup.add(jlpasswordsignup);
		jfsignup.add(jtfpasswordsignup);		
		jfsignup.add(jladhar);
		jfsignup.add(jtfadhar);
		jfsignup.add(new JLabel());
		jfsignup.add(register);

		//MainPage
		jfmain.add(jlfrom);	
		jfmain.add(jtffrom);
		jfmain.add(jlto);
		jfmain.add(jtfto);
		jfmain.add(date);
		JPanel jptemp = new JPanel();
		jptemp.setLayout(new GridLayout(1,3,5,5));
		jptemp.add(dateday);
		jptemp.add(datemonth);
		jptemp.add(dateyear);
		jfmain.add(jptemp);		
		jfmain.add(findtrains);
		jfmain.add(checkstatus);
		

		//ButtonEvents
		signin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				//Database Check
				String username = jtfusername.getText();
				String password = jtfpassword.getText();
				String app = new String(username);
				app=app.concat(",");
				app=app.concat(password); 	
				// verify() username
				try
				{
					BufferedReader br=new BufferedReader(new FileReader("Logincsv"));
					String abc;
					boolean flag=true;
					while((abc=br.readLine())!=null)
					{
						if(abc.equals(app))
						{
							//System.out.println("SUCCESS");
							jflogin.setVisible(false);
							jfmain.setVisible(true);
							flag=false;
							break;		
						}
					}
					if(flag)
					{
						JFrame jferror = new JFrame("Error");
						JLabel jlinvaliduser = new JLabel("Invalid Username or Password");
						jlinvaliduser.setFont(new Font("Monaco",Font.BOLD,40));
						jferror.setSize(1000,300);
						jferror.add(jlinvaliduser);
						jferror.setVisible(true);
						jtfusername.setText("");
						jtfpassword.setText("");
					}
					
				}
				catch(Exception Le)
				{
				}
				
			}					
		});

		signup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				jflogin.setVisible(false);
				jfsignup.setVisible(true);
			}
		});

		register.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				// verify() username
				// Add to Database
				String usernameentered = jtfusernamesignup.getText();
				String passwordentered = jtfpasswordsignup.getText();
				
				if(verify(usernameentered) && verify(passwordentered))
				{
					try
					{					
						fw = new FileWriter("Logincsv",true);
						fw.write(usernameentered+","+passwordentered);
						fw.write('\n');
						fw.close();
						jfsignup.setVisible(false);
						jfmain.setVisible(true);
					}catch(Exception em)
					{
					}
				}
				else
				{
					JFrame jferror = new JFrame("Error");
					JLabel jlinvaliduser = new JLabel("Invalid Username or Password");
					jlinvaliduser.setFont(new Font("Monaco",Font.BOLD,40));
					jferror.setSize(1000,300);
					jferror.add(jlinvaliduser);
					jferror.setVisible(true);
					jtfusernamesignup.setText("");
					jtfpasswordsignup.setText("");
					jtfadhar.setText("");
				}
				
				
				
			}
		});

		checkstatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				//pnr
			}
		});

		findtrains.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				//find trains
			}
		});

		//display
		jflogin.setVisible(true);			

	}
	
	public void actionPerformed(ActionEvent e)
	{
		//@override
	}
}
