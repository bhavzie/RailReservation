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
	Database	
	Show Avail in trainlist
	Pnr
	Cancel Res	
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
				//jcbfrom.addItem(makeObj(dataarray[3]));
				//jcbto.addItem(makeObj(dataarray[4]));				
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
				
				if(!usernameentered.isEmpty() && !passwordentered.isEmpty() && verify(usernameentered))
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
					JLabel jlinvaliduser = new JLabel("Username already taken or Empty: ");
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
