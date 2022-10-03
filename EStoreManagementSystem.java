import java.util.*;
import java.util.stream.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;

public class EStoreManagementSystem{

   public static void main(String ...args)throws Exception{
        System.out.println("please enter files(electronics, cloths) path [Drive:/path-to-files-folder/]");
	String filePath = new Scanner(System.in).nextLine().trim();
	String electronicsFile = "electronics";
	String clothsFile = "cloths";
        var users = Arrays.asList(new User(1, "ahmed", "ali", "ahmed123", "letmein", "ahmed@example.com", User.UserType.Customer), new User(2, "fatma", "hassan", "fatma123", "openforme", "fatma@example.com", User.UserType.Admin));
	new LoginGui(users, filePath, electronicsFile, clothsFile);
   }

   
}

class LoginGui{

   private JFrame frame;
   private JLabel headerLabel, usernameLabel, passwordLabel, logoLabel, loginStatusLabel;
   private JButton loginButton;
   private JTextField usernameField;
   private JPasswordField passwordField;
   private String filePath, electronicsFile, clothsFile;
   private java.util.List<User> users;

   public LoginGui(java.util.List<User> users, String filePath, String electronicsFile, String clothsFile){
      this.users = users;
      this.filePath = filePath;
      this.electronicsFile = electronicsFile;
      this.clothsFile = clothsFile;
      this.prepareGUI();
   }

   private void prepareGUI(){
      frame = new JFrame("E-Store Management System (Login Screen)");
      frame.setSize(600,350);
      frame.setResizable(false);
      frame.setLocationRelativeTo(null);
      frame.setLayout(null);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      headerLabel = new JLabel("E-Store Management System (EMS)",JLabel.CENTER );
      headerLabel.setForeground(Color.blue);
      headerLabel.setOpaque(true);
      headerLabel.setFont(new Font("TimesRoman", Font.BOLD, 20)); 
      headerLabel.setBounds(120, 20, 360, 50);

      usernameLabel = new JLabel("Username",JLabel.LEFT );
      usernameLabel.setFont(new Font("TimesRoman", Font.BOLD, 16)); 
      usernameLabel.setBounds(50, 90, 100, 40);
      usernameField = new JTextField("");
      usernameField.setFont(new Font("TimesRoman", Font.BOLD, 16));
      usernameField.setBounds(160, 90, 220, 40);
      passwordLabel = new JLabel("Password",JLabel.LEFT );
      passwordLabel.setFont(new Font("TimesRoman", Font.BOLD, 16)); 
      passwordLabel.setBounds(50, 150, 100, 40);
      passwordField = new JPasswordField("");
      passwordField.setFont(new Font("TimesRoman", Font.BOLD, 16));
      passwordField.setBounds(160, 150, 220, 40);
      loginButton = new JButton("Login");
      loginButton.setBounds(50, 210, 110, 40);
      loginButton.setFont(new Font("TimesRoman", Font.BOLD, 16));
      loginButton.addActionListener((event) -> {
         Optional<User> user = users.stream().filter(userElement -> userElement.getUsername().equals(usernameField.getText().trim()) && userElement.getPassword().equals(passwordField.getText().trim()))
			.findFirst();
         if(user.isPresent()){
	     switch(user.get().getUserType()){
		case Customer: { 
				frame.setVisible(false);
				new CustomerGui(this.filePath, this.electronicsFile, this.clothsFile);
			        break;
			}
		case Admin: {
				frame.setVisible(false);
				new AdminGui(this.filePath, this.electronicsFile, this.clothsFile);
			        break;
			}
	     }
	 }else{
             loginStatusLabel.setText("invalid username/password");
	 }
      });
      logoLabel = new JLabel("Shop", JLabel.CENTER);
      logoLabel.setFont(new Font("TimesRoman", Font.BOLD, 26));
      logoLabel.setBounds(390, 90, 150, 160);
      logoLabel.setForeground(Color.white);
      logoLabel.setBackground(Color.blue);
      logoLabel.setOpaque(true);

      loginStatusLabel = new JLabel("",JLabel.CENTER );
      loginStatusLabel.setForeground(Color.red);
      loginStatusLabel.setOpaque(true);
      loginStatusLabel.setFont(new Font("TimesRoman", Font.BOLD, 14)); 
      loginStatusLabel.setBounds(150, 260, 300, 40);

      frame.add(headerLabel);
      frame.add(usernameLabel);
      frame.add(usernameField);
      frame.add(passwordLabel);
      frame.add(passwordField);
      frame.add(loginButton);
      frame.add(logoLabel);
      frame.add(loginStatusLabel);
      frame.setVisible(true);
   }
}

class ConsoleLogger{

   public static void log(String filePath, String electronicsFile, String clothsFile) throws Exception{
	 var cfdh = new ClothsFileDataHandler();
	 System.out.println("\n\n**************\t electronics\t***************\n\n");
	 System.out.println("itemID, itemName, brand, model, year, price");
         cfdh.readFileData(filePath,electronicsFile).forEach(System.out::println);
	 System.out.println("\n\n**************\t   cloths   \t***************\n\n");
	 System.out.println("itemID, itemName, color, department, size, price");
         var efdh = new ElectronicsFileDataHandler();
         efdh.readFileData(filePath,clothsFile).forEach(System.out::println);
   }
}

class AdminGui{
   private JFrame frame, addElectronicsFrame, addClothsFrame, deleteElectronicsFrame, deleteClothsFrame;
   private JLabel headerLabel, itemIdLabel, itemNameLabel, brandLabel, modelLabel, sizeLabel, yearLabel, colorLabel, departmentLabel, priceLabel, addStatusLabel, deleteStatusLabel;
   private JButton addButton, deleteButton, exitButton, itemAdd, itemBack, deleteElectronics, deleteCloths;
   private JTextField itemIdField, itemNameField, brandField, modelField, sizeField, yearField, colorField, departmentField, priceField;
   private JPanel panel, gridPanel;
   private String filePath, electronicsFile, clothsFile;
   private LinkedList<Cloths> clothsList;
   private LinkedList<Electronics> electronicsList;
	
   public AdminGui(String filePath, String electronicsFile, String clothsFile){
      this.filePath = filePath;
      this.electronicsFile = electronicsFile;
      this.clothsFile = clothsFile;
      try{
         ConsoleLogger.log(filePath, electronicsFile, clothsFile);
         this.prepareGUI();
         this.clothsList = new ClothsFileDataHandler().readFileData(filePath, clothsFile);
         this.electronicsList = new ElectronicsFileDataHandler().readFileData(filePath, electronicsFile);
      }catch(Exception e){e.printStackTrace();}
   }

   private void prepareDeleteElectronics(){
      deleteElectronicsFrame = new JFrame("Deleting Electronics Screen");
      deleteElectronicsFrame.setSize(600,300);
      deleteElectronicsFrame.setResizable(false);
      deleteElectronicsFrame.setLocationRelativeTo(null);
      deleteElectronicsFrame.setLayout(null);
      deleteElectronicsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      headerLabel = new JLabel("Electronics Item Delete Form",JLabel.CENTER );
      headerLabel.setForeground(Color.blue);
      headerLabel.setOpaque(true);
      headerLabel.setFont(new Font("TimesRoman", Font.BOLD, 18)); 
      headerLabel.setBounds(150, 20, 300, 50);

      itemIdLabel = new JLabel("Item ID:",JLabel.LEFT );
      itemIdLabel.setFont(new Font("TimesRoman", Font.BOLD, 16)); 
      itemIdLabel.setBounds(50, 90, 100, 30);
      itemIdField = new JTextField("");
      itemIdField.setFont(new Font("TimesRoman", Font.BOLD, 16));
      itemIdField.setBounds(160, 90, 240, 30);
      deleteElectronics = new JButton("Delete");
      deleteElectronics.setBounds(410, 90, 140, 30);
      deleteElectronics.setFont(new Font("TimesRoman", Font.BOLD, 16));
      deleteElectronics.addActionListener((event) -> {
	 try{
            var deleted = new ElectronicsFileDataHandler().deleteItem(Integer.parseInt(itemIdField.getText().trim()), filePath, electronicsFile);
            deleteStatusLabel.setText("successfully deleted item {"+deleted.toString()+"}");
	    ConsoleLogger.log(filePath, electronicsFile, clothsFile);
	 }catch(Exception e){deleteStatusLabel.setText(e.getMessage());}
      });
      itemBack = new JButton("Go Back");
      itemBack.setFont(new Font("TimesRoman", Font.BOLD, 18));
      itemBack.setBounds(245, 150, 110, 40);
      itemBack.addActionListener((event) -> {
	 deleteElectronicsFrame.setVisible(false);
         prepareGUI();
      });

      deleteStatusLabel = new JLabel("Delete Status",JLabel.CENTER );
      deleteStatusLabel.setForeground(Color.red);
      deleteStatusLabel.setOpaque(true);
      deleteStatusLabel.setFont(new Font("TimesRoman", Font.BOLD, 14)); 
      deleteStatusLabel.setBounds(150, 210, 300, 50);

      deleteElectronicsFrame.add(headerLabel);
      deleteElectronicsFrame.add(itemIdLabel);
      deleteElectronicsFrame.add(itemIdField);
      deleteElectronicsFrame.add(deleteElectronics);
      deleteElectronicsFrame.add(itemBack);
      deleteElectronicsFrame.add(deleteStatusLabel);
      deleteElectronicsFrame.setVisible(true);
   }

   private void prepareDeleteCloths(){
      deleteClothsFrame = new JFrame("Deleting Electronics Screen");
      deleteClothsFrame.setSize(600,300);
      deleteClothsFrame.setResizable(false);
      deleteClothsFrame.setLocationRelativeTo(null);
      deleteClothsFrame.setLayout(null);
      deleteClothsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      headerLabel = new JLabel("Cloths Item Delete Form",JLabel.CENTER );
      headerLabel.setForeground(Color.blue);
      headerLabel.setOpaque(true);
      headerLabel.setFont(new Font("TimesRoman", Font.BOLD, 18)); 
      headerLabel.setBounds(150, 20, 300, 50);

      itemIdLabel = new JLabel("Item ID:",JLabel.LEFT );
      itemIdLabel.setFont(new Font("TimesRoman", Font.BOLD, 16)); 
      itemIdLabel.setBounds(50, 90, 100, 30);
      itemIdField = new JTextField("");
      itemIdField.setFont(new Font("TimesRoman", Font.BOLD, 16));
      itemIdField.setBounds(160, 90, 240, 30);
      deleteCloths = new JButton("Delete");
      deleteCloths.setBounds(410, 90, 140, 30);
      deleteCloths.setFont(new Font("TimesRoman", Font.BOLD, 16));
      deleteCloths.addActionListener((event) -> {
	 try{
            var deleted = new ClothsFileDataHandler().deleteItem(Integer.parseInt(itemIdField.getText().trim()), filePath, clothsFile);
            deleteStatusLabel.setText("successfully deleted item {"+deleted.toString()+"}");
	    ConsoleLogger.log(filePath, electronicsFile, clothsFile);
	 }catch(Exception e){deleteStatusLabel.setText(e.getMessage());}
      });
      itemBack = new JButton("Go Back");
      itemBack.setFont(new Font("TimesRoman", Font.BOLD, 18));
      itemBack.setBounds(245, 150, 110, 40);
      itemBack.addActionListener((event) -> {
	 deleteClothsFrame.setVisible(false);
         prepareGUI();
      });

      deleteStatusLabel = new JLabel("Delete Status",JLabel.CENTER );
      deleteStatusLabel.setForeground(Color.red);
      deleteStatusLabel.setOpaque(true);
      deleteStatusLabel.setFont(new Font("TimesRoman", Font.BOLD, 14)); 
      deleteStatusLabel.setBounds(150, 210, 300, 50);

      deleteClothsFrame.add(headerLabel);
      deleteClothsFrame.add(itemIdLabel);
      deleteClothsFrame.add(itemIdField);
      deleteClothsFrame.add(deleteCloths);
      deleteClothsFrame.add(itemBack);
      deleteClothsFrame.add(deleteStatusLabel);
      deleteClothsFrame.setVisible(true);
   }

   private void prepareAddElectronics(){
      addElectronicsFrame = new JFrame("Adding Electronics Screen");
      addElectronicsFrame.setSize(600,550);
      addElectronicsFrame.setResizable(false);
      addElectronicsFrame.setLocationRelativeTo(null);
      addElectronicsFrame.setLayout(null);
      addElectronicsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      headerLabel = new JLabel("Electronics Item Add Form",JLabel.CENTER );
      headerLabel.setForeground(Color.blue);
      headerLabel.setOpaque(true);
      headerLabel.setFont(new Font("TimesRoman", Font.BOLD, 18)); 
      headerLabel.setBounds(150, 20, 300, 50);

      itemIdLabel = new JLabel("Item ID:",JLabel.LEFT );
      itemIdLabel.setFont(new Font("TimesRoman", Font.BOLD, 16)); 
      itemIdLabel.setBounds(100, 90, 100, 30);
      itemIdField = new JTextField("");
      itemIdField.setFont(new Font("TimesRoman", Font.BOLD, 16));
      itemIdField.setBounds(210, 90, 290, 30);

      itemNameLabel = new JLabel("Item Name:",JLabel.LEFT );
      itemNameLabel.setFont(new Font("TimesRoman", Font.BOLD, 16)); 
      itemNameLabel.setBounds(100, 130, 100, 30);
      itemNameField = new JTextField("");
      itemNameField.setFont(new Font("TimesRoman", Font.BOLD, 16));
      itemNameField.setBounds(210, 130, 290, 30);

      brandLabel = new JLabel("Brand:",JLabel.LEFT );
      brandLabel.setFont(new Font("TimesRoman", Font.BOLD, 16)); 
      brandLabel.setBounds(100, 170, 100, 30);
      brandField = new JTextField("");
      brandField.setFont(new Font("TimesRoman", Font.BOLD, 16));
      brandField.setBounds(210, 170, 290, 30);
    
      modelLabel = new JLabel("Model:",JLabel.LEFT );
      modelLabel.setFont(new Font("TimesRoman", Font.BOLD, 16)); 
      modelLabel.setBounds(100, 210, 100, 30);
      modelField = new JTextField("");
      modelField.setFont(new Font("TimesRoman", Font.BOLD, 16));
      modelField.setBounds(210, 210, 290, 30);

      yearLabel = new JLabel("Year:",JLabel.LEFT );
      yearLabel.setFont(new Font("TimesRoman", Font.BOLD, 16)); 
      yearLabel.setBounds(100, 250, 100, 30);
      yearField = new JTextField("");
      yearField.setFont(new Font("TimesRoman", Font.BOLD, 16));
      yearField.setBounds(210, 250, 290, 30);

      priceLabel = new JLabel("Price:",JLabel.LEFT );
      priceLabel.setFont(new Font("TimesRoman", Font.BOLD, 16)); 
      priceLabel.setBounds(100, 290, 100, 30);
      priceField = new JTextField("");
      priceField.setFont(new Font("TimesRoman", Font.BOLD, 16));
      priceField.setBounds(210, 290, 290, 30);

      itemAdd = new JButton("Add Item");
      itemAdd.setFont(new Font("TimesRoman", Font.BOLD, 18));
      itemAdd.setBounds(185, 350, 110, 50);
      itemAdd.addActionListener((event) -> {
	 try{
	    var electronicsItem = new Electronics(Integer.parseInt(itemIdField.getText().trim()), itemNameField.getText().trim(),
		brandField.getText().trim(), modelField.getText().trim(), Integer.parseInt(yearField.getText().trim()), Integer.parseInt(priceField.getText().trim()));
            var added = new ElectronicsFileDataHandler().addItem(electronicsItem, filePath, electronicsFile);
            addStatusLabel.setText("successfully added item {"+added.toString()+"}");
	    ConsoleLogger.log(filePath, electronicsFile, clothsFile);
	 }catch(Exception e){addStatusLabel.setText(e.getMessage());}
      });

      itemBack = new JButton("Go Back");
      itemBack.setFont(new Font("TimesRoman", Font.BOLD, 18));
      itemBack.setBounds(305, 350, 110, 50);
      itemBack.addActionListener((event) -> {
	 addElectronicsFrame.setVisible(false);
         prepareGUI();
      });

      addStatusLabel = new JLabel("Add Status",JLabel.CENTER );
      addStatusLabel.setForeground(Color.red);
      addStatusLabel.setOpaque(true);
      addStatusLabel.setFont(new Font("TimesRoman", Font.BOLD, 14)); 
      addStatusLabel.setBounds(100, 440, 400, 50);
      

      addElectronicsFrame.add(headerLabel);
      addElectronicsFrame.add(itemIdLabel);
      addElectronicsFrame.add(itemIdField);
      addElectronicsFrame.add(itemNameLabel);
      addElectronicsFrame.add(itemNameField);
      addElectronicsFrame.add(brandLabel);
      addElectronicsFrame.add(brandField);
      addElectronicsFrame.add(modelLabel);
      addElectronicsFrame.add(modelField);
      addElectronicsFrame.add(yearLabel);
      addElectronicsFrame.add(yearField);
      addElectronicsFrame.add(priceLabel);
      addElectronicsFrame.add(priceField);
      addElectronicsFrame.add(itemAdd);
      addElectronicsFrame.add(itemBack);    
      addElectronicsFrame.add(addStatusLabel); 
      addElectronicsFrame.setVisible(true);
   }

   private void prepareAddCloths(){
      addClothsFrame = new JFrame("Adding Cloths Screen");
      addClothsFrame.setSize(600,550);
      addClothsFrame.setResizable(false);
      addClothsFrame.setLocationRelativeTo(null);
      addClothsFrame.setLayout(null);
      addClothsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      headerLabel = new JLabel("Cloths Item Add Form",JLabel.CENTER );
      headerLabel.setForeground(Color.blue);
      headerLabel.setOpaque(true);
      headerLabel.setFont(new Font("TimesRoman", Font.BOLD, 18)); 
      headerLabel.setBounds(150, 20, 300, 50);

      itemIdLabel = new JLabel("Item ID:",JLabel.LEFT );
      itemIdLabel.setFont(new Font("TimesRoman", Font.BOLD, 16)); 
      itemIdLabel.setBounds(100, 90, 100, 30);
      itemIdField = new JTextField("");
      itemIdField.setFont(new Font("TimesRoman", Font.BOLD, 16));
      itemIdField.setBounds(210, 90, 290, 30);

      itemNameLabel = new JLabel("Item Name:",JLabel.LEFT );
      itemNameLabel.setFont(new Font("TimesRoman", Font.BOLD, 16)); 
      itemNameLabel.setBounds(100, 130, 100, 30);
      itemNameField = new JTextField("");
      itemNameField.setFont(new Font("TimesRoman", Font.BOLD, 16));
      itemNameField.setBounds(210, 130, 290, 30);

      colorLabel = new JLabel("Color:",JLabel.LEFT );
      colorLabel.setFont(new Font("TimesRoman", Font.BOLD, 16)); 
      colorLabel.setBounds(100, 170, 100, 30);
      colorField = new JTextField("");
      colorField.setFont(new Font("TimesRoman", Font.BOLD, 16));
      colorField.setBounds(210, 170, 290, 30);
    
      departmentLabel = new JLabel("Department:",JLabel.LEFT );
      departmentLabel.setFont(new Font("TimesRoman", Font.BOLD, 16)); 
      departmentLabel.setBounds(100, 210, 100, 30);
      departmentField = new JTextField("");
      departmentField.setFont(new Font("TimesRoman", Font.BOLD, 16));
      departmentField.setBounds(210, 210, 290, 30);

      sizeLabel = new JLabel("Size:",JLabel.LEFT );
      sizeLabel.setFont(new Font("TimesRoman", Font.BOLD, 16)); 
      sizeLabel.setBounds(100, 250, 100, 30);
      sizeField = new JTextField("");
      sizeField.setFont(new Font("TimesRoman", Font.BOLD, 16));
      sizeField.setBounds(210, 250, 290, 30);

      priceLabel = new JLabel("Price:",JLabel.LEFT );
      priceLabel.setFont(new Font("TimesRoman", Font.BOLD, 16)); 
      priceLabel.setBounds(100, 290, 100, 30);
      priceField = new JTextField("");
      priceField.setFont(new Font("TimesRoman", Font.BOLD, 16));
      priceField.setBounds(210, 290, 290, 30);

      itemAdd = new JButton("Add Item");
      itemAdd.setFont(new Font("TimesRoman", Font.BOLD, 18));
      itemAdd.setBounds(185, 350, 110, 50);
      itemAdd.addActionListener((event) -> {
	 try{
	    var clothsItem = new Cloths(Integer.parseInt(itemIdField.getText().trim()), itemNameField.getText().trim(),
		colorField.getText().trim(), departmentField.getText().trim(), Integer.parseInt(sizeField.getText().trim()), Integer.parseInt(priceField.getText().trim()));
            var added = new ClothsFileDataHandler().addItem(clothsItem, filePath, clothsFile);
            addStatusLabel.setText("successfully added item {"+added.toString()+"}");
	    ConsoleLogger.log(filePath, electronicsFile, clothsFile);
	 }catch(Exception e){addStatusLabel.setText(e.getMessage());}
      });

      itemBack = new JButton("Go Back");
      itemBack.setFont(new Font("TimesRoman", Font.BOLD, 18));
      itemBack.setBounds(305, 350, 110, 50);
      itemBack.addActionListener((event) -> {
	 addClothsFrame.setVisible(false);
         prepareGUI();
      });

      addStatusLabel = new JLabel("Add Status",JLabel.CENTER );
      addStatusLabel.setForeground(Color.red);
      addStatusLabel.setOpaque(true);
      addStatusLabel.setFont(new Font("TimesRoman", Font.BOLD, 14)); 
      addStatusLabel.setBounds(100, 440, 400, 50);
      

      addClothsFrame.add(headerLabel);
      addClothsFrame.add(itemIdLabel);
      addClothsFrame.add(itemIdField);
      addClothsFrame.add(itemNameLabel);
      addClothsFrame.add(itemNameField);
      addClothsFrame.add(colorLabel);
      addClothsFrame.add(colorField);
      addClothsFrame.add(departmentLabel);
      addClothsFrame.add(departmentField);
      addClothsFrame.add(sizeLabel);
      addClothsFrame.add(sizeField);
      addClothsFrame.add(priceLabel);
      addClothsFrame.add(priceField);
      addClothsFrame.add(itemAdd);
      addClothsFrame.add(itemBack);    
      addClothsFrame.add(addStatusLabel);
      addClothsFrame.setVisible(true);
   }

   private void prepareGUI(){
      frame = new JFrame("Admin Screen");
      frame.setSize(400,440);
      frame.setResizable(false);
      frame.setLocationRelativeTo(null);
      frame.setLayout(null);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      headerLabel = new JLabel("Admin Menu",JLabel.CENTER );
      headerLabel.setForeground(Color.blue);
      headerLabel.setOpaque(true);
      headerLabel.setFont(new Font("TimesRoman", Font.BOLD, 18)); 
      headerLabel.setBounds(50, 20, frame.getWidth() - 110, 50);  
      addButton = new JButton("Add a New Item");
      addButton.setFont(new Font("TimesRoman", Font.BOLD, 18));
      addButton.setBounds(50, 100, frame.getWidth() - 110, 80);
      deleteButton = new JButton("Delete an Item");
      deleteButton.setFont(new Font("TimesRoman", Font.BOLD, 18));
      deleteButton.setBounds(50, 190, frame.getWidth() - 110, 80);
      exitButton = new JButton("Exit");   
      exitButton.setFont(new Font("TimesRoman", Font.BOLD, 18));
      exitButton.setBounds(50, 280, frame.getWidth() - 110, 80);

      addButton.addActionListener((event) -> {
	  frame.setVisible(false);
          var choice = Integer.parseInt(JOptionPane.showInputDialog("Choose where to Add\n1. Electronics\n2. Cloths"));
          switch(choice){
	     case 1: prepareAddElectronics();
                     break;
             case 2: prepareAddCloths();
                     break;
             default:frame.setVisible(true);
	  }
      });
      deleteButton.addActionListener((event) -> {
	  frame.setVisible(false);
          var choice = Integer.parseInt(JOptionPane.showInputDialog("Choose where to Add\n1. Electronics\n2. Cloths"));
          switch(choice){
	     case 1: prepareDeleteElectronics();
                     break;
             case 2: prepareDeleteCloths();
                     break;
             default:frame.setVisible(true);
	  }
      });
      exitButton.addActionListener((event) -> {
	 System.exit(0);
      });
      
      frame.add(headerLabel);
      frame.add(addButton);
      frame.add(deleteButton);
      frame.add(exitButton);
      frame.setVisible(true);  
   }
}

class CustomerGui{
   private JFrame frame, purchaseElectronicsFrame, purchaseClothsFrame;
   private JLabel headerLabel, itemIdLabel, itemNameLabel, brandLabel, modelLabel, quantityLabel, colorLabel, departmentLabel, purchaseStatusLabel;
   private JButton clothsButton, electronicsButton, exitButton, purchaseBuy, purchaseExit;
   private JTextField itemIdField, itemNameField, brandField, modelField, quantityField, colorField, departmentField;
   private JPanel panel, gridPanel;
   private String filePath, electronicsFile, clothsFile;
   private LinkedList<Cloths> clothsList;
   private LinkedList<Electronics> electronicsList;
	
   public CustomerGui(String filePath, String electronicsFile, String clothsFile){
      this.filePath = filePath;
      this.electronicsFile = electronicsFile;
      this.clothsFile = clothsFile;
      try{
         ConsoleLogger.log(filePath, electronicsFile, clothsFile);
         this.prepareGUI();
         this.clothsList = new ClothsFileDataHandler().readFileData(filePath, clothsFile);
         this.electronicsList = new ElectronicsFileDataHandler().readFileData(filePath, electronicsFile);
      }catch(Exception e){e.printStackTrace();}
   }

   private String orderSummary(int itemId, String itemName, int quantity, String category){
       Item item = null;
       if(category.equals("cloths"))
	   item = clothsList.stream().filter(i -> i.getItemID() == itemId && i.getItemName().equals(itemName)).findFirst().get();
       else
	   item = electronicsList.stream().filter(i -> i.getItemID() == itemId && i.getItemName().equals(itemName)).findFirst().get();
       return "Purchased item: "+itemId+", QTY="+quantity+", Total Price="+item.getPrice()*quantity;
   }
   
   private void prepareBuyElectronics(){
      purchaseElectronicsFrame = new JFrame("Buying Electronics Screen");
      purchaseElectronicsFrame.setSize(600,500);
      purchaseElectronicsFrame.setResizable(false);
      purchaseElectronicsFrame.setLocationRelativeTo(null);
      purchaseElectronicsFrame.setLayout(null);
      purchaseElectronicsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      headerLabel = new JLabel("Electronic Item Purchase Form",JLabel.CENTER );
      headerLabel.setForeground(Color.blue);
      headerLabel.setOpaque(true);
      headerLabel.setFont(new Font("TimesRoman", Font.BOLD, 18)); 
      headerLabel.setBounds(150, 20, 300, 50);

      itemIdLabel = new JLabel("Item ID*:",JLabel.LEFT );
      itemIdLabel.setFont(new Font("TimesRoman", Font.BOLD, 16)); 
      itemIdLabel.setBounds(100, 90, 100, 30);
      itemIdField = new JTextField("");
      itemIdField.setFont(new Font("TimesRoman", Font.BOLD, 16));
      itemIdField.setBounds(210, 90, 290, 30);

      itemNameLabel = new JLabel("Item Name*:",JLabel.LEFT );
      itemNameLabel.setFont(new Font("TimesRoman", Font.BOLD, 16)); 
      itemNameLabel.setBounds(100, 130, 100, 30);
      itemNameField = new JTextField("");
      itemNameField.setFont(new Font("TimesRoman", Font.BOLD, 16));
      itemNameField.setBounds(210, 130, 290, 30);

      brandLabel = new JLabel("Brand:",JLabel.LEFT );
      brandLabel.setFont(new Font("TimesRoman", Font.BOLD, 16)); 
      brandLabel.setBounds(100, 170, 100, 30);
      brandField = new JTextField("");
      brandField.setFont(new Font("TimesRoman", Font.BOLD, 16));
      brandField.setBounds(210, 170, 290, 30);
    
      modelLabel = new JLabel("Model:",JLabel.LEFT );
      modelLabel.setFont(new Font("TimesRoman", Font.BOLD, 16)); 
      modelLabel.setBounds(100, 210, 100, 30);
      modelField = new JTextField("");
      modelField.setFont(new Font("TimesRoman", Font.BOLD, 16));
      modelField.setBounds(210, 210, 290, 30);

      quantityLabel = new JLabel("Quantity:",JLabel.LEFT );
      quantityLabel.setFont(new Font("TimesRoman", Font.BOLD, 16)); 
      quantityLabel.setBounds(100, 250, 100, 30);
      quantityField = new JTextField("");
      quantityField.setFont(new Font("TimesRoman", Font.BOLD, 16));
      quantityField.setBounds(210, 250, 290, 30);

      purchaseBuy = new JButton("Buy");
      purchaseBuy.setFont(new Font("TimesRoman", Font.BOLD, 18));
      purchaseBuy.setBounds(200, 310, 95, 50);
      purchaseBuy.addActionListener((event) -> {
	  try{
	     if(!itemIdField.getText().trim().equals("") && !itemNameField.getText().trim().equals("")){
		var quantity = quantityField.getText().trim().equals("")? 1: Integer.parseInt(quantityField.getText().trim());
		purchaseStatusLabel.setText(orderSummary(Integer.parseInt(itemIdField.getText().trim()), itemNameField.getText().trim(), quantity, "electronics"));
             }else{
	        purchaseStatusLabel.setText("itemID & itemName fields must not be empty");
             }
          }catch(Exception e){purchaseStatusLabel.setText(e.getMessage());}
      });

      purchaseExit = new JButton("Exit");
      purchaseExit.setFont(new Font("TimesRoman", Font.BOLD, 18));
      purchaseExit.setBounds(305, 310, 95, 50);
      purchaseExit.addActionListener((event) -> {
	 System.exit(0);
      });

      purchaseStatusLabel = new JLabel("Purchase Status",JLabel.CENTER );
      purchaseStatusLabel.setForeground(Color.red);
      purchaseStatusLabel.setOpaque(true);
      purchaseStatusLabel.setFont(new Font("TimesRoman", Font.BOLD, 14)); 
      purchaseStatusLabel.setBounds(150, 400, 300, 50);
      

      purchaseElectronicsFrame.add(headerLabel);
      purchaseElectronicsFrame.add(itemIdLabel);
      purchaseElectronicsFrame.add(itemIdField);
      purchaseElectronicsFrame.add(itemNameLabel);
      purchaseElectronicsFrame.add(itemNameField);
      purchaseElectronicsFrame.add(brandLabel);
      purchaseElectronicsFrame.add(brandField);
      purchaseElectronicsFrame.add(modelLabel);
      purchaseElectronicsFrame.add(modelField);
      purchaseElectronicsFrame.add(quantityLabel);
      purchaseElectronicsFrame.add(quantityField);
      purchaseElectronicsFrame.add(purchaseBuy);
      purchaseElectronicsFrame.add(purchaseExit);    
      purchaseElectronicsFrame.add(purchaseStatusLabel); 
      purchaseElectronicsFrame.setVisible(true);
   }

   private void prepareBuyCloths(){
      purchaseClothsFrame = new JFrame("Buying Cloths Screen");
      purchaseClothsFrame.setSize(600,500);
      purchaseClothsFrame.setResizable(false);
      purchaseClothsFrame.setLocationRelativeTo(null);
      purchaseClothsFrame.setLayout(null);
      purchaseClothsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      headerLabel = new JLabel("Cloths Item Purchase Form",JLabel.CENTER );
      headerLabel.setForeground(Color.blue);
      headerLabel.setOpaque(true);
      headerLabel.setFont(new Font("TimesRoman", Font.BOLD, 18)); 
      headerLabel.setBounds(150, 20, 300, 50);

      itemIdLabel = new JLabel("Item ID*:",JLabel.LEFT );
      itemIdLabel.setFont(new Font("TimesRoman", Font.BOLD, 16)); 
      itemIdLabel.setBounds(100, 90, 100, 30);
      itemIdField = new JTextField("");
      itemIdField.setFont(new Font("TimesRoman", Font.BOLD, 16));
      itemIdField.setBounds(210, 90, 290, 30);

      itemNameLabel = new JLabel("Item Name*:",JLabel.LEFT );
      itemNameLabel.setFont(new Font("TimesRoman", Font.BOLD, 16)); 
      itemNameLabel.setBounds(100, 130, 100, 30);
      itemNameField = new JTextField("");
      itemNameField.setFont(new Font("TimesRoman", Font.BOLD, 16));
      itemNameField.setBounds(210, 130, 290, 30);

      colorLabel = new JLabel("Color:",JLabel.LEFT );
      colorLabel.setFont(new Font("TimesRoman", Font.BOLD, 16)); 
      colorLabel.setBounds(100, 170, 100, 30);
      colorField = new JTextField("");
      colorField.setFont(new Font("TimesRoman", Font.BOLD, 16));
      colorField.setBounds(210, 170, 290, 30);
    
      departmentLabel = new JLabel("Department:",JLabel.LEFT );
      departmentLabel.setFont(new Font("TimesRoman", Font.BOLD, 16)); 
      departmentLabel.setBounds(100, 210, 100, 30);
      departmentField = new JTextField("");
      departmentField.setFont(new Font("TimesRoman", Font.BOLD, 16));
      departmentField.setBounds(210, 210, 290, 30);

      quantityLabel = new JLabel("Quantity:",JLabel.LEFT );
      quantityLabel.setFont(new Font("TimesRoman", Font.BOLD, 16)); 
      quantityLabel.setBounds(100, 250, 100, 30);
      quantityField = new JTextField("");
      quantityField.setFont(new Font("TimesRoman", Font.BOLD, 16));
      quantityField.setBounds(210, 250, 290, 30);

      purchaseBuy = new JButton("Buy");
      purchaseBuy.setFont(new Font("TimesRoman", Font.BOLD, 18));
      purchaseBuy.setBounds(200, 310, 95, 50);
      purchaseBuy.addActionListener((event) -> {
	  try{
	     if(!itemIdField.getText().trim().equals("") && !itemNameField.getText().trim().equals("")){
		var quantity = quantityField.getText().trim().equals("")? 1: Integer.parseInt(quantityField.getText().trim());
		purchaseStatusLabel.setText(orderSummary(Integer.parseInt(itemIdField.getText().trim()), itemNameField.getText().trim(), quantity, "cloths"));
             }else{
	        purchaseStatusLabel.setText("itemID & itemName fields must not be empty");
             }
          }catch(Exception e){purchaseStatusLabel.setText(e.getMessage());}
      });

      purchaseExit = new JButton("Exit");
      purchaseExit.setFont(new Font("TimesRoman", Font.BOLD, 18));
      purchaseExit.setBounds(305, 310, 95, 50);
      purchaseExit.addActionListener((event) -> {
	 System.exit(0);
      });

      purchaseStatusLabel = new JLabel("Purchase Status",JLabel.CENTER );
      purchaseStatusLabel.setForeground(Color.red);
      purchaseStatusLabel.setOpaque(true);
      purchaseStatusLabel.setFont(new Font("TimesRoman", Font.BOLD, 14)); 
      purchaseStatusLabel.setBounds(150, 400, 300, 50);
      

      purchaseClothsFrame.add(headerLabel);
      purchaseClothsFrame.add(itemIdLabel);
      purchaseClothsFrame.add(itemIdField);
      purchaseClothsFrame.add(itemNameLabel);
      purchaseClothsFrame.add(itemNameField);
      purchaseClothsFrame.add(colorLabel);
      purchaseClothsFrame.add(colorField);
      purchaseClothsFrame.add(departmentLabel);
      purchaseClothsFrame.add(departmentField);
      purchaseClothsFrame.add(quantityLabel);
      purchaseClothsFrame.add(quantityField);
      purchaseClothsFrame.add(purchaseBuy);
      purchaseClothsFrame.add(purchaseExit);    
      purchaseClothsFrame.add(purchaseStatusLabel); 
      purchaseClothsFrame.setVisible(true);
   }

   private void prepareGUI(){
      frame = new JFrame("Customer Menu Screen");
      frame.setSize(400,440);
      frame.setResizable(false);
      frame.setLocationRelativeTo(null);
      frame.setLayout(null);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      headerLabel = new JLabel("Customer Main Menu",JLabel.CENTER );
      headerLabel.setForeground(Color.blue);
      headerLabel.setOpaque(true);
      headerLabel.setFont(new Font("TimesRoman", Font.BOLD, 18)); 
      headerLabel.setBounds(50, 20, frame.getWidth() - 110, 50);  
      clothsButton = new JButton("Cloths");
      clothsButton.setFont(new Font("TimesRoman", Font.BOLD, 18));
      clothsButton.setBounds(50, 100, frame.getWidth() - 110, 80);
      electronicsButton = new JButton("Electronics");
      electronicsButton.setFont(new Font("TimesRoman", Font.BOLD, 18));
      electronicsButton.setBounds(50, 190, frame.getWidth() - 110, 80);
      exitButton = new JButton("Exit");   
      exitButton.setFont(new Font("TimesRoman", Font.BOLD, 18));
      exitButton.setBounds(50, 280, frame.getWidth() - 110, 80);
   
      electronicsButton.addActionListener((event) -> {
	  frame.setVisible(false);
          prepareBuyElectronics();
      });
      clothsButton.addActionListener((event) -> {
	  frame.setVisible(false);
          prepareBuyCloths();
      });
      exitButton.addActionListener((event) -> {
	 System.exit(0);
      });
      
      frame.add(headerLabel);
      frame.add(clothsButton);
      frame.add(electronicsButton);
      frame.add(exitButton);
      frame.setVisible(true);  
   }
}

interface FileDataHandler<T extends Item>{
	LinkedList<T> readFileData(String filePath, String file) throws Exception;

	T addItem(T t, String filePath, String file)throws Exception;

	T deleteItem(int itemID, String filePath, String file)throws Exception;
}

class ClothsFileDataHandler implements FileDataHandler<Cloths>{

   @Override
   public LinkedList<Cloths> readFileData(String filePath, String file)throws Exception{
        var items = new LinkedList<Cloths>();
	String path = filePath+file+".txt";
	try{
	Scanner lineScanner = new Scanner(new File(path));
	while(lineScanner.hasNextLine()){
	      String line = lineScanner.nextLine();
	      Scanner sc = new Scanner(line);
	      sc.useDelimiter(",");
	      LinkedList<String> list = new LinkedList<>();
	      while(sc.hasNext()){
	         list.add(sc.next().trim());
	      }
	      items.add(new Cloths(Integer.parseInt(list.get(0)), list.get(1), list.get(2), list.get(3), Integer.parseInt(list.get(4)), Integer.parseInt(list.get(5))));
	   }
        }catch(Exception e){}
      	Collections.sort(items, (i1, i2) -> {
	    if(i1.getItemID() > i2.getItemID())	
	        return 1;
	    else if(i1.getItemID() < i2.getItemID())
	        return -1;
	    else 
	        return 0;
        });
        return items;
   }

   @Override
   public Cloths addItem(Cloths cloths, String filePath, String file)throws Exception{
	String path = filePath+file+".txt";
	try{
	    var items = readFileData(filePath, file);
	    items.add(cloths);
	    PrintWriter writer = new PrintWriter(new File(path));
	    for(var item: items)
	    	writer.println(item.toString());
	    writer.flush();
	    writer.close();
	}catch(Exception e){}
	return cloths;
   }

   @Override
   public Cloths deleteItem(int itemID, String filePath, String file)throws Exception{
	String path = filePath+file+".txt";
	Optional<Cloths> found = null;
	try{
	   var items = this.readFileData(filePath, file);
	   found = items.stream().filter(item -> item.itemID == itemID).findFirst();
	   if(!found.isPresent())
		return null;
	   var filteredItems = items.stream().filter(item -> item.itemID != itemID).collect(Collectors.toList());
	   PrintWriter writer = new PrintWriter(new File(path));
	   for(var item: filteredItems)
		writer.println(item.toString());
	   writer.flush();
	   writer.close();
        }catch(Exception e){}
	return found.get();
   }

}

class ElectronicsFileDataHandler implements FileDataHandler<Electronics>{

   @Override
   public LinkedList<Electronics> readFileData(String filePath, String file)throws Exception{
        var items = new LinkedList<Electronics>();
	String path = filePath+file+".txt";
	try{
	Scanner lineScanner = new Scanner(new File(path));
	while(lineScanner.hasNextLine()){
	      String line = lineScanner.nextLine();
	      Scanner sc = new Scanner(line);
	      sc.useDelimiter(",");
	      LinkedList<String> list = new LinkedList<>();
	      while(sc.hasNext()){
	         list.add(sc.next().trim());
	      }
	      items.add(new Electronics(Integer.parseInt(list.get(0)), list.get(1), list.get(2), list.get(3), Integer.parseInt(list.get(4)), Integer.parseInt(list.get(5))));
	   }
        }catch(Exception e){}
      	Collections.sort(items, (i1, i2) -> {
	    if(i1.getItemID() > i2.getItemID())	
	   	return 1;
	    else if(i1.getItemID() < i2.getItemID())
	   	return -1;
	    else 
	   	return 0;
      	});
        return items;
   }

   @Override
   public Electronics addItem(Electronics electronics, String filePath, String file)throws Exception{
	String path = filePath+file+".txt";
	try{
	    var items = readFileData(filePath, file);
	    items.add(electronics);
	    PrintWriter writer = new PrintWriter(new File(path));
	    for(var item: items)
	    	writer.println(item.toString());
	    writer.flush();
	    writer.close();
	}catch(Exception e){}
	return electronics;
   }

   @Override
   public Electronics deleteItem(int itemID, String filePath, String file)throws Exception{
	String path = filePath+file+".txt";
	Optional<Electronics> found = null;
	try{
	   var items = this.readFileData(filePath, file);
	   found = items.stream().filter(item -> item.itemID == itemID).findFirst();
	   if(!found.isPresent())
		return null;
	   var filteredItems = items.stream().filter(item -> item.itemID != itemID).collect(Collectors.toList());
	   PrintWriter writer = new PrintWriter(new File(path));
	   for(var item: filteredItems)
		writer.println(item.toString());
	   writer.flush();
	   writer.close();
        }catch(Exception e){}
	return found.get();
   }

}

class User{
	private int userID;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private String email;
	private UserType userType;

	public static enum UserType{Customer, Admin}

	public User(){}

	public User(int userID, String firstName, String lastName, String username, String password, String email, UserType userType){
	   this.userID = userID;
	   this.firstName = firstName;
	   this.lastName = lastName;
	   this.username = username;
	   this.password = password;
	   this.email = email;
	   this.userType = userType;
	}

	//accessors
	public int getUserID(){return this.userID;}
	public void setUserID(int userID){this.userID = userID;}

	public String getFirstName(){return this.firstName;}
	public void setFirstName(String firstName){this.firstName = firstName;}

	public String getLastName(){return this.lastName;}
	public void setLastName(String lastName){this.lastName = lastName;}

	public String getUsername(){return this.username;}
	public void setUsername(String username){this.username = username;}

	public String getPassword(){return this.password;}
	public void setPassword(String password){this.password = password;}

	public String getEmail(){return this.email;}
	public void setEmail(String email){this.email = email;}

	public UserType getUserType(){return this.userType;}
	public void setUserType(UserType userType){this.userType = userType;}

	@Override
	public String toString(){return this.userID+", "+this.firstName+", "+this.lastName+", "+this.username+", "+this.password+", "+this.email+", "+this.userType;}
}

class Electronics extends Item{
	private String brand;
	private String model;
        private int year;

	public Electronics(int itemID, String itemName, int price){
	   super(itemID, itemName, price);
	}

	public Electronics(int itemID, String itemName, String brand, String model, int year, int price){
	   super(itemID, itemName, price);
	   this.brand = brand;
	   this.model = model;
	   this.year = year;
	}

	public String getBrand(){return this.brand;}
	public void setBrand(String brand){this.brand = brand;}

	public String getModel(){return this.model;}
	public void setModel(String model){this.model = model;}

	public int getYear(){return this.year;}
	public void setYear(int year){this.year = year;}

	@Override
	public int getItemID(){return super.itemID;}
	@Override
	public void setItemID(int itemID){super.itemID = itemID;}

	@Override
	public String getItemName(){return super.itemName;}
	@Override
	public void setItemName(String itemName){super.itemName = itemName;}

	@Override
	public int getPrice(){return super.price;}
	@Override
	public void setPrice(int price){super.price = price;}

	@Override
	public String toString(){
	   return super.itemID+", "+super.itemName+", "+this.brand+", "+this.model+", "+this.year+", "+super.price;
	}
}

class Cloths extends Item{
	private String color;
	private String department;
        private int size;

	public Cloths(int itemID, String itemName, int price){
	   super(itemID, itemName, price);
	}

	public Cloths(int itemID, String itemName, String color, String department, int size, int price){
	   super(itemID, itemName, price);
	   this.color = color;
	   this.department = department;
	   this.size = size;
	}

	public String getColor(){return this.color;}
	public void setColor(String color){this.color = color;}

	public String getDepartment(){return this.department;}
	public void setDepartment(String department){this.department = department;}

	public int getSize(){return this.size;}
	public void setSize(int size){this.size = size;}

	@Override
	public int getItemID(){return super.itemID;}
	@Override
	public void setItemID(int itemID){super.itemID = itemID;}

	@Override
	public String getItemName(){return super.itemName;}
	@Override
	public void setItemName(String itemName){super.itemName = itemName;}

	@Override
	public int getPrice(){return super.price;}
	@Override
	public void setPrice(int price){super.price = price;}

	@Override
	public String toString(){
	   return super.itemID+", "+super.itemName+", "+this.color+", "+this.department+", "+this.size+", "+super.price;
	}
}

abstract class Item{
	protected int itemID;
	protected String itemName;
	protected int price;

	Item(int itemID, String itemName, int price){
	   this.itemID = itemID;
	   this.itemName = itemName;
	   this.price = price;
	}

	abstract int getItemID();
	abstract void setItemID(int itemID);
	abstract String getItemName();
	abstract void setItemName(String itemName);
	abstract int getPrice();
	abstract void setPrice(int price);
}