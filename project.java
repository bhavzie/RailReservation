import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.Collections;
import java.awt.Desktop;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import java.sql.*;

class JComboBoxAutoCompletador extends PlainDocument implements FocusListener, KeyListener, PropertyChangeListener
{
	private JComboBox comboBox;
	private ComboBoxModel model;
	private JTextComponent editor;
	private boolean hidePopupOnFocusLoss;
	//

	//
	public JComboBoxAutoCompletador()
	{
		// Bug 5100422 on Java 1.5: Editable JComboBox won’t hide popup when tabbing out
		hidePopupOnFocusLoss = System.getProperty("java.version").startsWith("1.5");
	}

	public JComboBoxAutoCompletador(JComboBox jcb)
	{
		this();
		registraComboBox(jcb);
	}
	
	public void registraComboBox(JComboBox jcb)
	{
		desregistraComboBox();
		this.comboBox = jcb;
		comboBox.setEditable(true);
		model = comboBox.getModel();
		editor = (JTextComponent) comboBox.getEditor().getEditorComponent();
		editor.setDocument(this);
		// Highlight whole text when focus gets lost
		editor.addFocusListener(this);
		// Highlight whole text when user hits enter
		editor.addKeyListener(this);
		comboBox.addPropertyChangeListener(this);
		// Handle initially selected object
		Object selected = comboBox.getSelectedItem();
		if(selected != null)
		editor.setText(selected.toString());
		else
		editor.setText("");
	}

	public void desregistraComboBox()
	{
		if(comboBox != null)
		{
			comboBox.getEditor().getEditorComponent().removeFocusListener(this);
			comboBox.getEditor().getEditorComponent().removeKeyListener(this);
			comboBox.removePropertyChangeListener(this);
			comboBox.setSelectedItem(null);
			comboBox = null;
		}
	}
	//

	//
	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
	{
	// construct the resulting string
		String currentText = getText(0, getLength());
		String beforeOffset = currentText.substring(0, offs);
		String afterOffset = currentText.substring(offs, currentText.length());
		String futureText = beforeOffset + str + afterOffset;
		// lookup and select a matching item
		Object item = lookupItem(futureText);
		if (item != null)
			comboBox.setSelectedItem(item);
		else
		{
			// keep old item selected if there is no match
			item = comboBox.getSelectedItem();
			// imitate no insert (later on offs will be incremented by str.length(): selection won’t move forward)
			offs = offs-str.length();
			// provide feedback to the user that his input has been received but can not be accepted
			comboBox.getToolkit().beep(); // when available use: UIManager.getLookAndFeel().provideErrorFeedback(comboBox);
		}

	// remove all text and insert the completed string
		super.remove(0, getLength());
		super.insertString(0, item.toString(), a);

		// if the user selects an item via mouse the the whole string will be inserted.
		// highlight the entire text if this happens.
		if (item.toString().equals(str) && offs == 0)
		highlightCompletedText(0);
		else
		{
			highlightCompletedText(offs + str.length());
			// show popup when the user types
			if(comboBox.isShowing() && comboBox.isFocusOwner())
			comboBox.setPopupVisible(true);
		}
	}

	private void highlightCompletedText(int start)
	{
		editor.setCaretPosition(getLength());
		editor.moveCaretPosition(start);
	}

	private Object lookupItem(String pattern)
	{
		Object selectedItem = model.getSelectedItem();
		// only search for a different item if the currently selected does not match
		if (selectedItem != null && startsWithIgnoreCase(selectedItem.toString(), pattern))
			return selectedItem;
		else
		{
		// iterate over all items
			for (int i = 0, n = model.getSize(); i < n; i++)
			{
				Object currentItem = model.getElementAt(i);
				// current item starts with the pattern?
				if(startsWithIgnoreCase(currentItem.toString(), pattern))
					return currentItem;
			}
		}
		// no item starts with the pattern => return null
		return null;
	}

	private boolean startsWithIgnoreCase(String str1, String str2)
	{
		return str1.toUpperCase().startsWith(str2.toUpperCase());
	}
	//

	//
	@Override
	public void focusGained(FocusEvent e) { }

	// focus lost on clicking\

	@Override
	public void focusLost(FocusEvent e)
	{
		highlightCompletedText(0);
		// Workaround for Bug 5100422 – Hide Popup on focus loss
		if(hidePopupOnFocusLoss)
			comboBox.setPopupVisible(false);
	}
	//

	//
	@Override
	public void keyTyped(KeyEvent e) { }

	@Override
	public void keyReleased(KeyEvent e) { }

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			highlightCompletedText(0);
		else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			comboBox.setSelectedIndex(0);
			editor.setText(comboBox.getSelectedItem().toString());
			highlightCompletedText(0);
		}
	}
	//

	//
	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		if(evt.getPropertyName().equals("model"))
			registraComboBox(comboBox);
	}
	//

}

/*
	Book page 
	
*/


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
	static JButton exit1 = new JButton("Exit ");
	static JButton viewrules = new JButton("View Rules ");
	static GridLayout glloginpage = new GridLayout(4,2,5,5);	

	
	//SignupPage
	static JFrame jfsignup = new JFrame("Signup Page");
	static JLabel jlusernamesignup = new JLabel("Username :");
	static JLabel jlpasswordsignup = new JLabel("Password :");
	static JLabel jladhar = new JLabel("Aadhar Number :");
	static JTextField jtfusernamesignup = new JTextField(15);
	static JTextField jtfpasswordsignup = new JTextField(15);
	static JTextField jtfadhar = new JTextField(15);
	static JButton register = new JButton("Register");
	static JButton exit2 = new JButton("Exit ");
	static GridLayout glsignuppage = new GridLayout(4,2,5,5);
		
	//MainFrame
	static JFrame jfmain = new JFrame("Main Page");
	static JLabel jlfrom = new JLabel("From :");
	static JComboBox jcbfrom = new JComboBox();
	static JComboBox jcbto = new JComboBox();
	static JLabel jlto = new JLabel("To :"); 
	static JButton findtrains = new JButton("Find Trains");
	static JButton checkstatus = new JButton("PNR STATUS");
	static JLabel date = new JLabel("Date : dd/mm/yyyy");
	static JTextField dateday = new JTextField();
	static JTextField datemonth = new JTextField();
	static JTextField dateyear = new JTextField();
	static JButton exit3 = new JButton("Exit ");   
	static GridLayout glmainpage = new GridLayout(6,2,5,5);
	
	//TrainList
	static JFrame jftrainlist = new JFrame("Train List");
	static JButton jbtrain[];
	
	//BookingPage
	static JFrame jfbook = new JFrame("Book Ticket");
	static JLabel jlpassname1 = new JLabel("Passenger Name :");
	static JTextField jtfpassname1 = new JTextField();
	static JLabel jlpassage = new JLabel("Age :");
	static JTextField jtfagepass1 = new JTextField();
	static JLabel jlpassgender = new JLabel("Gender :");
	static JRadioButton jrbpass1a = new JRadioButton("A) Male");
	static JRadioButton jrbpass1b = new JRadioButton("B) Female");
	static JButton jbbookticket = new JButton("Book ");
	static JButton jbbookexit = new JButton("Exit ");
	static ButtonGroup jbgpass1 = new ButtonGroup();
	
	//Pnr status
	static JFrame jfpnr = new JFrame("Check PNR Status");
	static JLabel jlpnr = new JLabel("Enter your PNR ");
	static JTextField jtfpnr = new JTextField();
	static JButton jbpnr = new JButton("Check Status");
	
	//Book Page
	static JFrame jfbooked = new JFrame("Booked");
	static JLabel jlpassname2 = new JLabel("Passenger Name :");
	static JLabel jtfpassname2 = new JLabel("");
	static JLabel jpnr = new JLabel("Ticket PNR :");
	static JLabel jpnr1 = new JLabel("");
	static JLabel jdate = new JLabel("Date :");
	static JLabel jdate1 = new JLabel("");
	static JLabel jtrainname = new JLabel("Train Name :");
	static JLabel jtrainname1 = new JLabel("");
	static JLabel jto = new JLabel("To :");
	static JLabel jto1 = new JLabel("");
	static JLabel jfrom = new JLabel("From :");
	static JLabel jfrom1 = new JLabel("");
	static JButton jfbooked1 = new JButton("Exit");
		
	
	
	
	// trainid trainname from to book
	// jscrollpane
	
	
	static FileWriter fw;
	static FileReader fr;
	
	public static boolean verify(String input)	// not already taken
	{
		try
		{
			BufferedReader br=new BufferedReader(new FileReader("Logincsv"));
			String abc;
			while((abc=br.readLine())!=null)
			{
				int endindex = abc.indexOf(",");
				String substr;
				if(endindex!=-1)
				{
					substr = abc.substring(0,endindex);
					if(substr.equals(input))
						return false;
				}
			}								
		}
		catch(Exception Le)
		{
		
		}
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
		exit1.setFont(new Font("Monaco",Font.BOLD,40));		
		jflogin.setSize(1000,500);
		jflogin.getRootPane().setDefaultButton(signin);
		viewrules.setFont(new Font("Monaco",Font.BOLD,40));
		jflogin.setLayout(glloginpage);

		//SignupPage
		jlusernamesignup.setFont(new Font("Monaco",Font.BOLD,40));
		register.setFont(new Font("Monaco",Font.BOLD,40));
		jtfadhar.setFont(new Font("Monaco",Font.BOLD,40));
		jtfpasswordsignup.setFont(new Font("Monaco",Font.BOLD,40));
		jtfusernamesignup.setFont(new Font("Monaco",Font.BOLD,40));
		jladhar.setFont(new Font("Monaco",Font.BOLD,40));
		exit2.setFont(new Font("Monaco",Font.BOLD,40));
		jlpasswordsignup.setFont(new Font("Monaco",Font.BOLD,40));
		jfsignup.setSize(800,500);
		jfsignup.getRootPane().setDefaultButton(register);
		jfsignup.setLayout(glsignuppage);
		
		
		//MainPage
		jlfrom.setFont(new Font("Monaco",Font.BOLD,40));
		jlto.setFont(new Font("Monaco",Font.BOLD,40));
		jcbfrom.setFont(new Font("Monaco",Font.BOLD,40));
		jcbto.setFont(new Font("Monaco",Font.BOLD,40));
		new JComboBoxAutoCompletador(jcbfrom);
		new JComboBoxAutoCompletador(jcbto);
		jcbfrom.setEditable(true);
		jcbfrom.setEnabled(true);
		jcbto.setEditable(true);
		jcbto.setEnabled(true);
		findtrains.setFont(new Font("Monaco",Font.BOLD,40));
		checkstatus.setFont(new Font("Monaco",Font.BOLD,20));
		date.setFont(new Font("Monaco",Font.BOLD,40));
		dateday.setFont(new Font("Monaco",Font.BOLD,40));
		datemonth.setFont(new Font("Monaco",Font.BOLD,40));
		dateyear.setFont(new Font("Monaco",Font.BOLD,40));
		exit3.setFont(new Font("Monaco",Font.BOLD,40));
		Calendar c = Calendar.getInstance();
		dateday.setText("" + c.get(Calendar.DAY_OF_MONTH));
		datemonth.setText("" + (c.get(Calendar.MONTH)+1));
		dateyear.setText("" + c.get(Calendar.YEAR));
		jfmain.getRootPane().setDefaultButton(findtrains);
		jfmain.setSize(1500,1000);
		jfmain.setLayout(glmainpage);
		
		//trainlist
		jftrainlist.setSize(1500,1000);
		jftrainlist.setLayout(new GridLayout(0,1,5,5));
		
		//bookingpage
		jfbook.setSize(1500,1000);
		jfbook.setLayout(new GridLayout(4,2,5,5));	
		jlpassname1.setFont(new Font("Monaco",Font.BOLD,40));
		jtfpassname1.setFont(new Font("Monaco",Font.BOLD,40));
		jtfagepass1.setFont(new Font("Monaco",Font.BOLD,40));
		jrbpass1a.setFont(new Font("Monaco",Font.BOLD,40));
		jrbpass1b.setFont(new Font("Monaco",Font.BOLD,40));
		jbbookticket.setFont(new Font("Monaco",Font.BOLD,40));
		jlpassgender.setFont(new Font("Monaco",Font.BOLD,40));
		jlpassage.setFont(new Font("Monaco",Font.BOLD,40));
		jbgpass1.add(jrbpass1a);
		jbgpass1.add(jrbpass1b);
		jbbookexit.setFont(new Font("Monaco",Font.BOLD,40));
		
		//pnrStatus
		jfpnr.setSize(1000,600);
		jfpnr.getRootPane().setDefaultButton(jbpnr);
		jlpnr.setFont(new Font("Monaco",Font.BOLD,40));
		jtfpnr.setFont(new Font("Monaco",Font.BOLD,40));
		jbpnr.setFont(new Font("Monaco",Font.BOLD,40));
		jfpnr.setLayout(new GridLayout(2,1,5,5));
		
		//book page
		jfbooked.setSize(1000,600);
		jfbooked.setLayout(new GridLayout(7,2,5,5));
		jlpassname2.setFont(new Font("Monaco",Font.BOLD,40));
		jtfpassname2.setFont(new Font("Monaco",Font.BOLD,40));
		jpnr.setFont(new Font("Monaco",Font.BOLD,40));
		jpnr1.setFont(new Font("Monaco",Font.BOLD,40));
		jdate.setFont(new Font("Monaco",Font.BOLD,40));
		jdate1.setFont(new Font("Monaco",Font.BOLD,40));
		jtrainname.setFont(new Font("Monaco",Font.BOLD,40));
		jtrainname1.setFont(new Font("Monaco",Font.BOLD,40));
		jto.setFont(new Font("Monaco",Font.BOLD,40));
		jto1.setFont(new Font("Monaco",Font.BOLD,40));
		jfrom.setFont(new Font("Monaco",Font.BOLD,40));
		jfrom1.setFont(new Font("Monaco",Font.BOLD,40));
		jfbooked1.setFont(new Font("Monaco",Font.BOLD,40));
		
		
		  
		// jcb allIndia trains
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("All_Indian_Trains.csv"));
			String abc;
			HashSet<String> h1 = new HashSet<String>();
			HashSet<String> b1 = new HashSet<String>();
			while((abc=br.readLine())!=null)
			{
				String dataarray[] = abc.split(",");
				h1.add(dataarray[3]);
				b1.add(dataarray[4]);				
			}
			List<String> sh1 = new ArrayList<String>(h1);
			List<String> sb1 = new ArrayList<String>(b1);
			Collections.sort(sh1);
			Collections.sort(sb1);
			Iterator<String> i = h1.iterator();
			Iterator<String> j = b1.iterator();
			for(int x=0;x < sh1.size();x++) 
			{
			    jcbfrom.addItem(sh1.get(x)); 
			}
			for(int x=0;x < sb1.size();x++) 
			{
			    jcbto.addItem(sb1.get(x)); 
			}
					
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
		jflogin.add(viewrules);
		jflogin.add(exit1);
		
		//SignupPage
		jfsignup.add(jlusernamesignup);
		jfsignup.add(jtfusernamesignup);
		jfsignup.add(jlpasswordsignup);
		jfsignup.add(jtfpasswordsignup);		
		jfsignup.add(jladhar);
		jfsignup.add(jtfadhar);
		jfsignup.add(register);
		jfsignup.add(exit2);

		//MainPage
		jfmain.add(jlfrom);	
		jfmain.add(jcbfrom);
		jfmain.add(new JLabel());
		jfmain.add(new JLabel());
		jfmain.add(jlto);
		jfmain.add(jcbto);
		jfmain.add(new JLabel());
		jfmain.add(new JLabel());
		jfmain.add(date);
		JPanel jptemp = new JPanel();
		jptemp.setLayout(new GridLayout(1,3,5,5));
		jptemp.add(dateday);
		jptemp.add(datemonth);
		jptemp.add(dateyear);
		jfmain.add(jptemp);		
		jfmain.add(findtrains);
		JPanel jpmag = new JPanel();
		jpmag.setLayout(new GridLayout(1,2,5,5));
		jpmag.add(checkstatus);
		jpmag.add(exit3);
		jfmain.add(jpmag);

		
		//BookingPage
		jfbook.add(jlpassname1);
		jfbook.add(jtfpassname1);
		jfbook.add(jlpassage);
		jfbook.add(jtfagepass1);
		jfbook.add(jlpassgender);
		JPanel jpnish = new JPanel();
		jpnish.setLayout(new GridLayout(1,2,5,5));
		jpnish.add(jrbpass1a);
		jpnish.add(jrbpass1b);
		jfbook.add(jpnish);
		jfbook.add(jbbookticket);
		jfbook.add(jbbookexit);
		
		
		
		//pnrstatus
		JPanel jpdi = new JPanel();
		jpdi.setLayout(new GridLayout(1,2,5,5));
		jpdi.add(jlpnr);
		jpdi.add(jtfpnr);
		jfpnr.add(jpdi);
		jfpnr.add(jbpnr);
		
		//bookedPage
		jfbooked.add(jlpassname2);
		jfbooked.add(jtfpassname2);
		jfbooked.add(jpnr);
		jfbooked.add(jpnr1);
		jfbooked.add(jdate);
		jfbooked.add(jdate1);
		jfbooked.add(jtrainname);
		jfbooked.add(jtrainname1);
		jfbooked.add(jto);
		jfbooked.add(jto1);
		jfbooked.add(jfrom);
		jfbooked.add(jfrom1);
		jfbooked.add(jfbooked1);
		
		
		
		HashMap<String, String> map = new HashMap<>();
		//insert into hashmap also used in pnr
		try
		{
		
			BufferedReader br=new BufferedReader(new FileReader("bookt"));
			String abc;
			
			while((abc=br.readLine())!=null)
			{
				String dataarray[] = abc.split(",");
				map.put(dataarray[0],dataarray[1]);
			}
		}catch(Exception dishr)
		{
			
		}
		
		

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
				String adharentered = jtfadhar.getText();
				
				if(!usernameentered.isEmpty() && !passwordentered.isEmpty() &&!adharentered.isEmpty()&&verify(usernameentered))
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
					JLabel jlinvaliduser = new JLabel();
					if(usernameentered.isEmpty())
						jlinvaliduser = new JLabel("Username already taken or Empty: ");
					else if(passwordentered.isEmpty())
						jlinvaliduser = new JLabel("Password Empty: ");
					else if(adharentered.isEmpty())
						jlinvaliduser = new JLabel("Adhar already taken or Empty: ");
					
					
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
				jfmain.setVisible(false);
				jfpnr.setVisible(true);
			}
		});
		
		jbpnr.addActionListener(new ActionListener() {		// check for pnr
			public void actionPerformed(ActionEvent e)
			{
				String input = jtfpnr.getText();
				
				if(!map.containsKey(input))
				{
					JFrame jferror = new JFrame("Error");
					JLabel jlinvaliduser = new JLabel("Invalid PNR ");
					jlinvaliduser.setFont(new Font("Monaco",Font.BOLD,40));
					jferror.setSize(1000,300);
					jferror.add(jlinvaliduser);
					jferror.setVisible(true);
				}
				else
				{
					String passengername = map.get(input);
					String dateoftrain = input.substring(0,2) + "/" + input.substring(2,4) + "/20";
					dateoftrain = dateoftrain + input.substring(4,6); 
					String seatid = input.substring(6,8);
					String trainid = input.substring(8);
					String fromtrain = "";
					String totrain = "";
					String trainname = "";
					
					try
					{
						BufferedReader br=new BufferedReader(new FileReader("All_Indian_Trains.csv"));
						String dishantj;
							
						while((dishantj=br.readLine())!=null)
						{
							String dataarray[] = dishantj.split(",");
							
							if(dataarray[1].equals(trainid))
							{
								trainname = dataarray[2];
								fromtrain = dataarray[3];
								totrain = dataarray[4];
								break;
							}
						}
						
					}catch(Exception dj)
					{
					
					}
					
					JFrame jfdisplayinfo = new JFrame("Display Information");
					JLabel passdisplayname = new JLabel("Passenger name :");
					passdisplayname.setFont(new Font("Monaco",Font.BOLD,40));
					JLabel passdisplayname1 = new JLabel(passengername);
					passdisplayname1.setFont(new Font("Monaco",Font.BOLD,40));
					JLabel trainiddisplay = new JLabel("Train ID");
					trainiddisplay.setFont(new Font("Monaco",Font.BOLD,40));
					JLabel trainiddisplay1 = new JLabel(trainid);
					trainiddisplay1.setFont(new Font("Monaco",Font.BOLD,40));
					JLabel trainnamedisplay = new JLabel("Train Name");
					trainnamedisplay.setFont(new Font("Monaco",Font.BOLD,40));
					JLabel trainnamedisplay1 = new JLabel(trainname);
					trainnamedisplay1.setFont(new Font("Monaco",Font.BOLD,40));
					JLabel dateoftraindisplay = new JLabel("Date of Journey");
					dateoftraindisplay.setFont(new Font("Monaco",Font.BOLD,40));
					JLabel dateoftraindisplay1 = new JLabel(dateoftrain);
					dateoftraindisplay1.setFont(new Font("Monaco",Font.BOLD,40));
					JLabel seatiddisplay = new JLabel("Seat Number");
					seatiddisplay.setFont(new Font("Monaco",Font.BOLD,40));
					JLabel seatiddisplay1 = new JLabel(seatid);
					seatiddisplay1.setFont(new Font("Monaco",Font.BOLD,40));
					JLabel fromdisplay = new JLabel("From");
					fromdisplay.setFont(new Font("Monaco",Font.BOLD,40));
					JLabel todisplay = new JLabel("To");
					todisplay.setFont(new Font("Monaco",Font.BOLD,40));
					JLabel fromdisplay1 = new JLabel(fromtrain);
					fromdisplay1.setFont(new Font("Monaco",Font.BOLD,40));
					JLabel todisplay1 = new JLabel(totrain);
					todisplay1.setFont(new Font("Monaco",Font.BOLD,40));
					JButton jbcanceldisplay = new JButton("Cancel");
					jbcanceldisplay.setFont(new Font("Monaco",Font.BOLD,40));
					JButton jbexitdisplay = new JButton("Exit");
					jbexitdisplay.setFont(new Font("Monaco",Font.BOLD,40));
					
					jfdisplayinfo.setLayout(new GridLayout(0,2,5,5));
					jfdisplayinfo.setSize(1500,1500);
					jfdisplayinfo.add(passdisplayname);
					jfdisplayinfo.add(passdisplayname1);
					jfdisplayinfo.add(trainiddisplay);
					jfdisplayinfo.add(trainiddisplay1);
					jfdisplayinfo.add(trainnamedisplay);
					jfdisplayinfo.add(trainnamedisplay1);
					jfdisplayinfo.add(dateoftraindisplay);
					jfdisplayinfo.add(dateoftraindisplay1);
					jfdisplayinfo.add(seatiddisplay);
					jfdisplayinfo.add(seatiddisplay1);
					jfdisplayinfo.add(fromdisplay);
					jfdisplayinfo.add(fromdisplay1);
					jfdisplayinfo.add(todisplay);
					jfdisplayinfo.add(todisplay1);
					jfdisplayinfo.add(jbcanceldisplay);
					jfdisplayinfo.add(jbexitdisplay);
					
					jbexitdisplay.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e)
						{
							System.exit(0);
						}
					});
					
					
					jbcanceldisplay.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e)
						{
							String input5 = jtfpnr.getText();
							String removedpassname = (String)map.remove(input);
							
							ArrayList<String> al = new ArrayList<String>(); 
							
							try
							{
								BufferedReader br=new BufferedReader(new FileReader("bookt"));
								String input;
								while((input=br.readLine())!=null)
								{
									String dataarray[] = input.split(",");
						
									if(!dataarray[0].equals(input5))
									{
										al.add(dataarray[0]+","+dataarray[1]);
									}	
								}
								
								 Iterator it = al.iterator(); 
  
								PrintWriter writer = new PrintWriter("bookt");
								writer.print("");
								writer.close();
								
								fw = new FileWriter("bookt",true);
								
								
								while (it.hasNext()) 
								{
									    fw.write(it.next().toString());
									    fw.write("\n");
								}
								fw.close();
								
							}catch(Exception emrpos)
							{
							}
							
							JFrame jfcancelled = new JFrame("Cancelled");
							JLabel jlcancelled = new JLabel("Cancelled :)");
							JLabel jlpnrcan = new JLabel("Passenger Name :");
							JLabel jlpnrcancelled = new JLabel(input);
							JLabel jlpnrnamecan = new JLabel("PNR :");
							JLabel jlnamecancelled = new JLabel(removedpassname);
							JButton jbcancelledexit = new JButton("Exit");
						
							jlcancelled.setFont(new Font("Monaco",Font.BOLD,80));
							jlnamecancelled.setFont(new Font("Monaco",Font.BOLD,40));
							jlpnrnamecan.setFont(new Font("Monaco",Font.BOLD,40));
							jlpnrcancelled.setFont(new Font("Monaco",Font.BOLD,40));
							jlpnrcan.setFont(new Font("Monaco",Font.BOLD,40));
							jlnamecancelled.setFont(new Font("Monaco",Font.BOLD,40));
							jbcancelledexit.setFont(new Font("Monaco",Font.BOLD,40));
					
							jfcancelled.setLayout(new GridLayout(4,1,5,5));
							jfcancelled.setSize(1000,1000);
							
							
							jfcancelled.add(jlcancelled);
							JPanel jppop = new JPanel();
							jppop.setLayout(new GridLayout(1,2,5,5));		
							jppop.add(jlpnrnamecan);
							jppop.add(jlpnrcancelled);
							jfcancelled.add(jppop);
							
							JPanel jppiss = new JPanel();
							jppiss.setLayout(new GridLayout(1,2,5,5));
							jppiss.add(jlpnrcan);
							jppiss.add(jlnamecancelled);
							jfcancelled.add(jppiss);
												
							jfcancelled.add(jbcancelledexit);
							
							
							
							jfmain.setVisible(false);
							jfpnr.setVisible(false);
							jfdisplayinfo.setVisible(false);	
							jfcancelled.setVisible(true);
							
							jbcancelledexit.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e)
								{
									System.exit(0);
								}
							});
						}
					});
		

					jfmain.setVisible(false);
					jfpnr.setVisible(false);
					jfdisplayinfo.setVisible(true);
					// cancel button
					
					
				}
			}
		});
		
		findtrains.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
			String pnrb = "";
						String info1 = "";
				String d1 = dateday.getText();
				String d2 = datemonth.getText();
				String d3 = dateyear.getText();
				
				if(d1.isEmpty() || d2.isEmpty() || d3.isEmpty() || d1.length()>2 || d2.length()>2 || d3.length()>5 || Integer.parseInt(d1)>31 || Integer.parseInt(d2)>12)
				{
					JFrame jferror = new JFrame("Error");
					JLabel jlinvaliduser = new JLabel("Invalid Date entered");
					jlinvaliduser.setFont(new Font("Monaco",Font.BOLD,40));
					jferror.setSize(1000,300);
					jferror.add(jlinvaliduser);
					jferror.setVisible(true);
				}
				else
				{
				try
				{
				BufferedReader br=new BufferedReader(new FileReader("All_Indian_Trains.csv"));
				String input;
				int count = 0;
				boolean flag=true;
				String frominput = jcbfrom.getSelectedItem().toString();
				String toinput = jcbto.getSelectedItem().toString();
				Vector tempstring = new Vector();
							
				while((input=br.readLine())!=null)
				{
					String dataarray[] = input.split(",");
					
					if(frominput.equals(dataarray[3]) && toinput.equals(dataarray[4]))
					{
						
						flag = false;
						count++;
						tempstring.add(dataarray[1]+"  "+dataarray[2]);
					}	
				}
				if(flag)
				{
					JFrame jferror = new JFrame("Error");
					JLabel jlinvaliduser = new JLabel("Train not found ");
					jlinvaliduser.setFont(new Font("Monaco",Font.BOLD,40));
					jferror.setSize(500,300);
					jferror.add(jlinvaliduser);
					jferror.setVisible(true);
					jtfusername.setText("");
					jtfpassword.setText("");
				}
					
				if(!flag)
				{
					jbtrain = new JButton[count];
					
					for(int i=0; i<count; i++)
					{
						jbtrain[i] = new JButton(tempstring.get(i).toString());
						jbtrain[i].setFont(new Font("Monaco",Font.BOLD,40));
						jftrainlist.add(jbtrain[i]);
					}
					
					for(int i=0; i<count; i++)
					{
						jbtrain[i].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e)
						{
								// hash logic
						jfbook.setVisible(true);
						final String xmr = e.getActionCommand(); 
						jbbookticket.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e)
						{
							try
							{
								// pnr = date+seatid+trainid,passname
							String datedayentered = dateday.getText();
							String datemonthentered = datemonth.getText();
							String dateyearentered = dateyear.getText();
			
							if(datedayentered.length()==1)
							{
								datedayentered = "0".concat(datedayentered);
							}
							if(datemonthentered.length()==1)
							{
								datemonthentered = "0".concat(datemonthentered);
							}
							dateyearentered = dateyearentered.valueOf(Integer.parseInt(dateyearentered)%100);
							String datefinalformat = datedayentered+datemonthentered+dateyearentered;
							String passnameentered = jtfpassname1.getText();
			
							String traininfoentered = xmr.substring(0,xmr.indexOf(" "));
							//info1 = traininfoentered;
							
							int i = 1;
							String temppnr = "";
							String finalpnr = "";
			
							while(i<=99)
							{
								String xde = temppnr.valueOf(i);
								if(i<10)
								xde = "0".concat(xde);
								
								temppnr = datefinalformat+xde+traininfoentered;
								
								if(!map.containsKey(temppnr))
								{
									finalpnr = temppnr;
									pnrb = pnrb.concat(finalpnr);
									break;
								}
								i++;
							}							
						
						
							map.put(finalpnr,passnameentered);
		
							try
							{							
							fw = new FileWriter("bookt",true);
							fw.write(finalpnr+","+passnameentered);
							fw.write('\n');
							fw.close();
							}catch(Exception em)
							{
							}
						}catch(Exception em)
						{
						}
			       			}
						});
						}
						});
						}
		
						jftrainlist.setVisible(true);	
					}
				}
				catch(Exception Le)
				{
				}
				}
				
				
				jbbookticket.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e)
					{
						
				
						String asd = d1.concat(d2);
						asd = asd.concat(d3);
						String passnameentered = jtfpassname1.getText();
						jtfpassname2.setText(passnameentered);
						jpnr1.setText(pnrb);
						jdate1.setText(d3);
						jtrainname1.setText(info1);
						//	jto1 jfrom1
						jfbooked.setVisible(true);
					}
				});
				
				
			}			
		});
		
		
		
		jfbooked1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		
		exit1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		
		exit2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		
		exit3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		
		jbbookexit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});

	
		//display
		jflogin.setVisible(true);			

		
	
		viewrules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				 try
				 {

					File pdfFile = new File("Rules & Regulations for the Agents.pdf");
					if (pdfFile.exists())
					{

						if (Desktop.isDesktopSupported())
						{
							Desktop.getDesktop().open(pdfFile);
						}
						else
						{
							System.out.println("Awt Desktop is not supported!");
						}
					}
					else 
					{
						System.out.println("File is not exists!");
					}

					System.out.println("Done");

				  }
				  catch (Exception ex)
				  {
					ex.printStackTrace();
		  		  }
		  	}
		
		});
	
	}
	
	public void actionPerformed(ActionEvent e)
	{
		//@override
	}
}
