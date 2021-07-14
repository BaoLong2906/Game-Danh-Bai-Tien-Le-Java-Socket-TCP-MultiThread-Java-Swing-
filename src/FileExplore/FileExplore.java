package FileExplore;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import Client.ClientGameplay;
import DataPacket.DataPacket;
import DataPacket.RequestSendFileData;
import DataPacket.RequestSendMessenger;
import Storage.Save;
import Storage.UserInfor;


import javax.swing.JTree;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JScrollPane;
import java.awt.Font;
import javax.swing.JTabbedPane;
import javax.swing.JRadioButton;

public class FileExplore extends JFrame {

	private JPanel contentPane;
	private ScrollPane scrollPane;
	private JScrollPane jScrollPane;
	
	public static ClientGameplay clientGameplay;
	public ClientGameplay getClientGameplay() {
		return clientGameplay;
	}
	public void setClientGameplay(ClientGameplay clientGameplay) {
		this.clientGameplay = clientGameplay;
	}
	
	private JTabbedPane jtabbedPane;
	private JPanel subJPanelOpenFunction = new JPanel(), subJPanelEditFunction = new JPanel(),
	subJPanelPrintFunction = new JPanel(), subJPanelNewFunction = new JPanel(),
	subJPanelCopyFunction = new JPanel(), subJPanelCutFunction = new JPanel(),
	subJPanelRenameFunction = new JPanel(), subJPanelDeleteFunction = new JPanel(),
	subJPanelCompressFunction = new JPanel(), subJPanelExtractFunction = new JPanel();
	
	private JTable jtable;
	private JTree jtree;
	private JButton btnTypeLayout, btnBack, btnForward, btnSearch, btnGoTo;
	private JButton btnOpen, btnRename, btnDelete, btnCopy, btnCut, btnCompress, btnExtract;
	
	private File fileRoot;
	
	//----------------------
	private boolean isListLayout = true;
	//private String resourcePath = "F:\\ecilpse-workspace\\GiuaKyJavaNangCao\\Resource\\";
	
	//private String resourcePath = "F:"+File.separator+"ecilpse-workspace"+File.separator+"GiuaKyJavaNangCao"+File.separator+"Resource"+File.separator;
	private String resourcePath = "Resource"+File.separator;
	//----------------------
	//private String startRootPath = "C:\\Users\\Acer\\Desktop";
	//private String startRootPath = "C:"+File.separator+"Users"+File.separator+"Acer"+File.separator+"Desktop";
	private String startRootPath = System.getProperty("user.home")+File.separator+"Desktop";
	public String getStartRootPath() {
		return startRootPath;
	}
	
	public void setStartRootPath(String startRootPath) {
		this.startRootPath = startRootPath;
	}
	//----------------------
	// presentPath là đường dẫn con trỏ đang trỏ đến
	private String presentPath;
	private boolean isExistPresentPath = false;
	
	private Stack<String> stackOfForwardPath = new Stack<String>();
	private Stack<String> stackOfBackPath = new Stack<String>();
	
	public Stack<String> getStackOfForwardPath() {
		return stackOfForwardPath;
	}
	
	public void setStackOfForwardPath(Stack<String> stackOfForwardPath) {
		this.stackOfForwardPath = stackOfForwardPath;
	}
	
	public Stack<String> getStackOfBackPath() {
		return stackOfBackPath;
	}
	
	public void setStackOfPath(Stack<String> stackOfBackPath) {
		this.stackOfBackPath = stackOfBackPath;
	}
	//----------------------
	// 2 nút lui và tới
	private boolean isBackCanClick;
	private boolean isForwardCanClick;
	
	private JTextField textFieldPresentPath;
	private JTextField textFieldSearchPath;

	public boolean getIsBackCanClick() {
		return isBackCanClick;
	}

	public void setIsBackCanClick(boolean isBackCanClick) {
		this.isBackCanClick = isBackCanClick;
	}

	public boolean getIsForwardCanClick() {
		return isForwardCanClick;
	}

	public void setIsForwardCanClick(boolean isForwardCanClick) {
		this.isForwardCanClick = isForwardCanClick;
	}
	//----------------------
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FileExplore frame = new FileExplore();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FileExplore() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1188, 709);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textFieldPresentPath = new JTextField();
		textFieldPresentPath.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textFieldPresentPath.setBounds(87, 6, 733, 33);
		contentPane.add(textFieldPresentPath);
		textFieldPresentPath.setColumns(10);
		
		textFieldSearchPath = new JTextField();
		textFieldSearchPath.setBounds(889, 6, 170, 33);
		textFieldSearchPath.setText("Tìm kiếm trong thư mục này");
		contentPane.add(textFieldSearchPath);
		textFieldSearchPath.setColumns(10);
		
		// đối tượng file gốc
		//fileRoot = new File(startRootPath);
		
		// tạo ra root mother
		jtree = configureJTree(startRootPath);
		presentPath = new String(startRootPath);
		textFieldPresentPath.setText(presentPath);
		jtree.setBounds(10, 60, 221, 601);
		contentPane.add(jtree);
		
		//jtable = new JTable();
		jtable = configureJTable(startRootPath);
		jtable.setBounds(241, 61, 923, 464);
		contentPane.add(jtable);
		
		JLabel lblBack = new JLabel("");
		lblBack.setBounds(10, 6, 45, 13);
		contentPane.add(lblBack);
		
		JLabel lblForward = new JLabel("");
		lblForward.setBounds(65, 6, 45, 13);
		contentPane.add(lblForward);
		
		/////////////////////////////////////////
		scrollPane = new ScrollPane();
		scrollPane.setBounds(10, 60, 221, 601);
		
		// Disable this, if you want to use design mode
		//scrollPane.add(jtree);
		contentPane.add(scrollPane);
		
		/////////////////////////////////////////
		jScrollPane = new JScrollPane(jtable);
		jScrollPane.setBounds(241, 61, 923, 464);
		jtable.setFillsViewportHeight(true);
		contentPane.add(jScrollPane);
		
		////////////////////////////////////////
		
		btnTypeLayout = new JButton("");
		btnTypeLayout.setBounds(1119, 6, 45, 45);
		btnTypeLayout.setIcon(createFormatImageIcon(resourcePath + "listicon.png", btnTypeLayout.getWidth(), btnTypeLayout.getHeight()));
		btnTypeLayout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (isListLayout == true) {
					isListLayout = false;
					btnTypeLayout.setIcon(createFormatImageIcon(resourcePath + "gridicon.png", btnTypeLayout.getWidth(), btnTypeLayout.getHeight()));
					System.out.println(stackOfBackPath.toString());
					System.out.println(stackOfForwardPath.toString());
				} else {
					isListLayout = true;
					btnTypeLayout.setIcon(createFormatImageIcon(resourcePath + "listicon.png", btnTypeLayout.getWidth(), btnTypeLayout.getHeight()));
					System.out.println(stackOfBackPath.toString());
					System.out.println(stackOfForwardPath.toString());
				}
			}
		});
		contentPane.add(btnTypeLayout);
		
		btnBack = new JButton("");
		btnBack.setBounds(10, 5, 33, 34);
		btnBack.setIcon(createFormatImageIcon(resourcePath + "backoff.png", btnTypeLayout.getWidth(), btnTypeLayout.getHeight()));
		btnBack.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (isBackCanClick == true) {
					
					
					if (!stackOfBackPath.empty()) {
						// nếu stack không rỗng
						
						// push element to stackOfForwardPath
						stackOfForwardPath.push(presentPath);
						//System.out.println("back say " + presentPath);
						// điều chỉnh icon btForward sang trạng thái on
						btnForward.setIcon(createFormatImageIcon(resourcePath + "forwardon.png", btnForward.getWidth(), btnForward.getHeight()));
						isForwardCanClick = true;
						
						
						// pop element out from stackOfBackPath, show to present textfield
						presentPath = stackOfBackPath.pop();
						textFieldPresentPath.setText(presentPath);
						
						if (stackOfBackPath.empty()) {
							isBackCanClick = false;
							btnBack.setIcon(createFormatImageIcon(resourcePath + "backoff.png", btnBack.getWidth(), btnBack.getHeight()));
						}
						
						repaintTable(presentPath);
						repaintTree(presentPath);
					} 
				}
			}
		});
		contentPane.add(btnBack);
		
		btnForward = new JButton("");
		btnForward.setBounds(44, 5, 33, 34);
		btnForward.setIcon(createFormatImageIcon(resourcePath + "forwardoff.png", btnTypeLayout.getWidth(), btnTypeLayout.getHeight()));
		btnForward.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (isForwardCanClick == true) {
					
					if (!stackOfForwardPath.empty()) {
						// nếu stack không rỗng
						// pop element out from stackOfBackPath, show to present textfield
						stackOfBackPath.push(presentPath);
						btnBack.setIcon(createFormatImageIcon(resourcePath + "backon.png", btnBack.getWidth(), btnBack.getHeight()));
						isBackCanClick = true;
							
						presentPath = stackOfForwardPath.pop();
						textFieldPresentPath.setText(presentPath);
						
						//System.out.println("forward say " + presentPath);
						if (stackOfForwardPath.empty()) {
							isForwardCanClick = false;
							//System.out.println("forward say " + presentPath);
							// điều chỉnh icon btnForward sang trạng thái off
							btnForward.setIcon(createFormatImageIcon(resourcePath + "forwardoff.png", btnForward.getWidth(), btnForward.getHeight()));
						}
						
						repaintTable(presentPath);
						repaintTree(presentPath);
						
					}
				}
			}
		});
		contentPane.add(btnForward);
		
		btnSearch = new JButton("");
		btnSearch.setBounds(1069, 6, 33, 33);
		btnSearch.setIcon(createFormatImageIcon(resourcePath + "searchicon.png", btnSearch.getWidth(), btnSearch.getHeight()));
		btnSearch.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String fileNameNeedToSearch = null;
				if (tempDirectoryPath == null) {
					fileNameNeedToSearch = startRootPath + textFieldSearchPath.getText().trim();
				} else {
					fileNameNeedToSearch = tempDirectoryPath + textFieldSearchPath.getText().trim();
				}
				File file = new File(fileNameNeedToSearch);
				
			}
		});
		contentPane.add(btnSearch);
		
		btnGoTo = new JButton("");
		btnGoTo.setBounds(830, 6, 49, 33);
		btnGoTo.setIcon(createFormatImageIcon(resourcePath + "goicon.png", btnGoTo.getWidth() - 10, btnGoTo.getHeight() - 10));
		btnGoTo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(!new File(textFieldPresentPath.getText().trim()).exists()) {
					System.out.println("đường dẫn sai");
					return;
				}
				
				// có thể sử dụng nút này để open file, nếu đường dẫn đến một file.
				if (new File(textFieldPresentPath.getText().trim()).isFile()) {
					// nếu đường dẫn người dùng nhập vào là một đường dẫn của file
					
					//textFieldPresentPath.setText(presentPath);
				} else {
					if (!textFieldPresentPath.getText().trim().equals(tempPath)) {
						// nếu đường dẫn người dùng nhập vào khác với đường dẫn trước đó (lưu ý ở đây đường dẫn trước đó l đường dân của thư mục)
						System.out.println("textFieldPresentPath: " + textFieldPresentPath.getText().trim());
						if (!stackOfBackPath.isEmpty()) {
							System.out.println(stackOfBackPath.peek());
						}
						
						if (stackOfBackPath.isEmpty()) {
							isBackCanClick = true;
							btnBack.setIcon(createFormatImageIcon(resourcePath+"backon.png", btnBack.getWidth(), btnBack.getHeight()));
							
							stackOfBackPath.push(startRootPath);
							presentPath = textFieldPresentPath.getText().trim();
							
							stackOfForwardPath.clear();
							isForwardCanClick = false;
							btnForward.setIcon(createFormatImageIcon(resourcePath+"forwardoff.png", btnForward.getWidth(), btnForward.getHeight()));
							
							repaintTable(textFieldPresentPath.getText().trim());

							// lưu ý chỉ vẽ lại tree khi nhập một đường gẫn mother khác hoàn toàn
							// nếu chỉ khác ở phần con thì vẽ lại tree với đường dẫn mother ban đầu.
							repaintTree(textFieldPresentPath.getText().trim());
							
							// hiển thị đường dẫn thư mục
							textFieldPresentPath.setText(textFieldPresentPath.getText().trim());
							return;
						}
						
						stackOfForwardPath.clear();
						isForwardCanClick = false;
						btnForward.setIcon(createFormatImageIcon(resourcePath + "forwardoff.png", btnForward.getWidth(), btnForward.getHeight()));
						
						stackOfBackPath.push(tempPath);
						System.out.println("tempPath: " + tempPath);
						System.out.println(presentPath);
						tempPath          = new String(presentPath);
						tempDirectoryPath = new String(tempPath);
						presentPath       = textFieldPresentPath.getText().trim();
						
						repaintTable(textFieldPresentPath.getText().trim());
						
						// lưu ý chỉ vẽ lại tree khi nhập một đường gẫn mother khác hoàn toàn
						// nếu chỉ khác ở phần con thì vẽ lại tree với đường dẫn mother ban đầu.
						repaintTree(textFieldPresentPath.getText().trim());
						
						// hiển thị đường dẫn thư mục
						textFieldPresentPath.setText(textFieldPresentPath.getText().trim());
					} else {
						System.out.println("go there");
						
					}
					
				}
				
			}
		});
		contentPane.add(btnGoTo);
		
		
		jtabbedPane = new JTabbedPane(JTabbedPane.TOP);
		jtabbedPane.setBounds(241, 531, 923, 130);
		
		jtabbedPane.add("Open", subJPanelOpenFunction);
		subJPanelOpenFunction.setLayout(null);
		setUpJPanelOpen();
		
		
		jtabbedPane.add("Edit", subJPanelEditFunction);
		subJPanelEditFunction.setLayout(null);
		
		jtabbedPane.add("Print", subJPanelPrintFunction);
		subJPanelPrintFunction.setLayout(null);
		
		jtabbedPane.add("New", subJPanelNewFunction);
		subJPanelNewFunction.setLayout(null);
		setUpJPanelNew();
		
		
		
		jtabbedPane.add("Copy", subJPanelCopyFunction);
		subJPanelCopyFunction.setLayout(null);
		setUpJPanelCopy();
		
		
		
		jtabbedPane.add("Cut", subJPanelCutFunction);
		subJPanelCutFunction.setLayout(null);
		setUpJPanelCut();
		
		
		
		jtabbedPane.add("Rename", subJPanelRenameFunction);
		subJPanelRenameFunction.setLayout(null);
		setUpJPanelRename();
		
		
		
		jtabbedPane.add("Delete", subJPanelDeleteFunction);
		subJPanelDeleteFunction.setLayout(null);
		setUpJPanelDelete();
		
		
		jtabbedPane.add("Compress", subJPanelCompressFunction);
		subJPanelCompressFunction.setLayout(null);
		setUpJPanelCompress();
		
		
		
		jtabbedPane.add("Extract", subJPanelExtractFunction);
		subJPanelExtractFunction.setLayout(null);
		setUpJPanelExtract();
			
	}
	
	// --Thành phần component cho subtab
	public void setUpJPanelOpen() {
		JLabel lblNewLabel = new JLabel("Đường dẫn hiện tại:");
		lblNewLabel.setBounds(10, 13, 128, 13);
		subJPanelOpenFunction.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Loại file");
		lblNewLabel_1.setBounds(10, 36, 45, 13);
		subJPanelOpenFunction.add(lblNewLabel_1);
		
		btnOpen = new JButton("OPEN");
		btnOpen.setBounds(10, 72, 85, 21);
		btnOpen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (textFieldSubJPanelOpenLinkPath.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(FileExplore.this, "Đường dẫn bị trống");
				} else {
					// open this file
					File file = new File(textFieldSubJPanelOpenLinkPath.getText().trim());
					if (file.exists()) {
						if(!Desktop.isDesktopSupported()){
				            System.out.println("Desktop is not supported");
				        }
				    	Desktop desktop = Desktop.getDesktop();
				        try {
							desktop.open(file);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {JOptionPane.showMessageDialog(FileExplore.this, "File không tồn tại với đường dẫn đã cho");}
				}
			}
		});
		subJPanelOpenFunction.add(btnOpen);
		
		lblSubJPanelOpenTypeFile = new JLabel("");
		lblSubJPanelOpenTypeFile.setBounds(150, 36, 223, 13);
		subJPanelOpenFunction.add(lblSubJPanelOpenTypeFile);
		
		textFieldSubJPanelOpenLinkPath = new JTextField();
		textFieldSubJPanelOpenLinkPath.setBounds(150, 10, 484, 19);
		subJPanelOpenFunction.add(textFieldSubJPanelOpenLinkPath);
		textFieldSubJPanelOpenLinkPath.setColumns(10);
		
		lblNewLabel_2 = new JLabel("Hướng dẫn:");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_2.setBounds(644, 5, 75, 24);
		subJPanelOpenFunction.add(lblNewLabel_2);
		
		lblNewLabel_3 = new JLabel("*Nhấn chuột trái 2 lần để mở file");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_3.setBounds(663, 34, 223, 13);
		subJPanelOpenFunction.add(lblNewLabel_3);
		
		lblNewLabel_4 = new JLabel("hoặc vào tab open để sử dụng");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_4.setBounds(673, 50, 223, 13);
		subJPanelOpenFunction.add(lblNewLabel_4);
		
		lblNewLabel_5 = new JLabel("chức năng open file.");
		lblNewLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_5.setBounds(673, 66, 137, 13);
		subJPanelOpenFunction.add(lblNewLabel_5);
		
		btnGetPath = new JButton("Upload file này");
		btnGetPath.setBounds(115, 72, 148, 21);
		btnGetPath.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Save.chosingImagePath = textFieldSubJPanelOpenLinkPath.getText().trim();
				File file = new File(Save.chosingImagePath);
				
				//storeHouseActivity.textFieldAddUrl.setText(Save.chosingImagePath);
				//storeHouseActivity.textFieldProductURLToFix.setText(Save.chosingImagePath);
				
				// gửi thông tin về file.
				RequestSendMessenger requestSendMessenger = new RequestSendMessenger(clientGameplay.accountName, "đã gửi đến bạn file " + file.getName());
				DataPacket dataPacket = (DataPacket) requestSendMessenger;
				
				// gửi data của file.
				RequestSendFileData requestSendFileData = new RequestSendFileData(Save.chosingImagePath);
				DataPacket dataPacket2 = (DataPacket) requestSendFileData;
				
				clientGameplay.clientThread.doWrite(dataPacket);
				clientGameplay.clientThread.doWrite(dataPacket2);
				
				String string = "Bạn đã gửi một file: " + file.getName() + " \n";
				
				clientGameplay.logMessenger.append(string);
				
				clientGameplay.textPaneDisplayChat.setText(clientGameplay.logMessenger.toString());
				clientGameplay.textFieldInputMessenger.setText("");
				dispose();
			}
		});
		subJPanelOpenFunction.add(btnGetPath);
	}
	
	public void setUpJPanelEdit() {
		
	}
	
	public void setUpJPanelPrint() {
		
	}
	
	// 1 là lưu dạng file, 0 là lưu dạng folder
	int typeToNew = 1;
	public void setUpJPanelNew() {
		lblHngDn = new JLabel("Hướng dẫn:");
		lblHngDn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblHngDn.setBounds(530, 0, 75, 23);
		subJPanelNewFunction.add(lblHngDn);
		
		lblCo = new JLabel("*Nhấn chuột phải chọn chức năng new");
		lblCo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCo.setBounds(614, 5, 278, 13);
		subJPanelNewFunction.add(lblCo);
		
		lblHocVoTab = new JLabel("hoặc vào tab new để sử dụng chức năng new.");
		lblHocVoTab.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblHocVoTab.setBounds(614, 28, 294, 13);
		subJPanelNewFunction.add(lblHocVoTab);
		
		lblNewLabel_6 = new JLabel("Đường dẫn nơi lưu:");
		lblNewLabel_6.setBounds(10, 7, 134, 13);
		subJPanelNewFunction.add(lblNewLabel_6);
		
		textFieldSubJPanelNewLinkPath = new JTextField();
		textFieldSubJPanelNewLinkPath.setText("Ví dụ: C:\\Users\\Acer\\Desktop");
		textFieldSubJPanelNewLinkPath.setBounds(154, 4, 362, 19);
		subJPanelNewFunction.add(textFieldSubJPanelNewLinkPath);
		textFieldSubJPanelNewLinkPath.setColumns(10);
		
		lblNewLabel_7 = new JLabel("Tạo File hay Folder");
		lblNewLabel_7.setBounds(10, 30, 122, 13);
		subJPanelNewFunction.add(lblNewLabel_7);
		
		btnButtonChoseNewAsFileOrDir = new JButton("Lưu dạng file");
		btnButtonChoseNewAsFileOrDir.setBounds(154, 26, 134, 21);
		btnButtonChoseNewAsFileOrDir.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (typeToNew == 1) {
					typeToNew = 0;
					btnButtonChoseNewAsFileOrDir.setText("Lưu dạng folder");
				} else {
					typeToNew = 1;
					btnButtonChoseNewAsFileOrDir.setText("Lưu đạng file");
				}
			}
		});
		subJPanelNewFunction.add(btnButtonChoseNewAsFileOrDir);
		
		lblNewLabel_8 = new JLabel("Loại File muốn tạo");
		lblNewLabel_8.setBounds(10, 54, 122, 13);
		subJPanelNewFunction.add(lblNewLabel_8);
		
		textFieldSubJPanelNewFileType = new JTextField();
		textFieldSubJPanelNewFileType.setText("Ví dụ: doc, txt        (Nếu lưu folder, phần này có thể để trống)");
		textFieldSubJPanelNewFileType.setBounds(154, 51, 402, 19);
		subJPanelNewFunction.add(textFieldSubJPanelNewFileType);
		textFieldSubJPanelNewFileType.setColumns(10);
		
		lblNewLabel_9 = new JLabel("nhấn để chọn lưu kiểu file hay folder");
		lblNewLabel_9.setBounds(296, 30, 260, 13);
		subJPanelNewFunction.add(lblNewLabel_9);
		
		lblNewLabel_10 = new JLabel("Tên lưu của file/folder");
		lblNewLabel_10.setBounds(10, 77, 142, 13);
		subJPanelNewFunction.add(lblNewLabel_10);
		
		textFieldSubJPanelNewNameÒf = new JTextField();
		textFieldSubJPanelNewNameÒf.setText("Ví dụ: myword, chỉ ghi tên (myword) không ghi kiểu file (.doc) ở dòng này");
		textFieldSubJPanelNewNameÒf.setBounds(154, 74, 402, 19);
		subJPanelNewFunction.add(textFieldSubJPanelNewNameÒf);
		textFieldSubJPanelNewNameÒf.setColumns(10);
		
		btnNew = new JButton("New");
		btnNew.setBounds(565, 72, 75, 23);
		btnNew.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String path = textFieldSubJPanelNewLinkPath.getText().trim() + File.separator;
				if (typeToNew == 1) {
					path += textFieldSubJPanelNewNameÒf.getText().trim() + "." + textFieldSubJPanelNewFileType.getText().trim();
					createFile(path);
				} else {
					path += textFieldSubJPanelNewNameÒf.getText().trim();
					createDir(path);
				}
				repaintTable(presentPath);
				repaintTree(presentPath);
				System.out.println("path: " + path);
				
			}
		});
		subJPanelNewFunction.add(btnNew);
	}
	
	public void setUpJPanelCopy() {
		JLabel lblNewLabel_6_1 = new JLabel("Đường dẫn File/Folder gốc:");
		lblNewLabel_6_1.setBounds(10, 10, 176, 13);
		subJPanelCopyFunction.add(lblNewLabel_6_1);
		
		textFieldSubJPanelCopyOriginLinkPath = new JTextField();
		textFieldSubJPanelCopyOriginLinkPath.setText("Ví dụ: C:\\Users\\Acer\\Desktop\\MyWord.doc");
		textFieldSubJPanelCopyOriginLinkPath.setColumns(10);
		textFieldSubJPanelCopyOriginLinkPath.setBounds(297, 7, 362, 19);
		subJPanelCopyFunction.add(textFieldSubJPanelCopyOriginLinkPath);
		
		JLabel lblNewLabel_6_1_1 = new JLabel("Đường dẫn folder nơi lưu File/Folder bản copy:");
		lblNewLabel_6_1_1.setBounds(10, 29, 277, 13);
		subJPanelCopyFunction.add(lblNewLabel_6_1_1);
		
		textFieldSubJPanelCopyNewLinkPath = new JTextField();
		textFieldSubJPanelCopyNewLinkPath.setText("Ví dụ: C:\\Users\\Acer\\Desktop\\MyWordFolder");
		textFieldSubJPanelCopyNewLinkPath.setColumns(10);
		textFieldSubJPanelCopyNewLinkPath.setBounds(297, 26, 362, 19);
		subJPanelCopyFunction.add(textFieldSubJPanelCopyNewLinkPath);
		
		btnCopy = new JButton("Copy");
		btnCopy.setBounds(10, 55, 85, 21);
		btnCopy.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String originLinkPath = textFieldSubJPanelCopyOriginLinkPath.getText().trim();
				String newLinkPath    = textFieldSubJPanelCopyNewLinkPath.getText().trim();
				
				String resultOriginLink = getExtension(originLinkPath);
				String resultNewLink = getExtension(newLinkPath);
				
				if (new File(originLinkPath).exists()) {
					if (new File(originLinkPath).isFile()) {
						// nếu bản gốc là file, kiểm tra tiếp đường dẫn ở bản sao.
						if (new File(newLinkPath).exists() && new File(newLinkPath).isFile()) {
							copyFile(originLinkPath, newLinkPath);
							repaintTable(presentPath);
							repaintTree(presentPath);
						} else if (new File(newLinkPath).exists() && new File(newLinkPath).isDirectory()) {
							if (deleteLastSymbol(newLinkPath, File.separator) != "-1") {
								newLinkPath = new String(deleteLastSymbol(newLinkPath, File.separator));
							}
							newLinkPath += File.separator + "New " + new File(originLinkPath).getName();
							System.out.println("newLinkPath: " + newLinkPath);
							copyFile(originLinkPath, newLinkPath);
							repaintTable(presentPath);
							repaintTree(presentPath);
						} else {
							System.out.println("đường dẫn bản sao không hợp lệ hoặc không đúng");
							JOptionPane.showMessageDialog(FileExplore.this, "đường dẫn bản sao không hợp lệ hoặc không đúng");
						}
					} else {
						// nếu bản gốc là folder, kiểm tra tiếp đường dẫn ở bản sao.
						if (new File(newLinkPath).exists() && new File(newLinkPath).isFile()) {
							System.out.println("bản gốc là folder, bản sao không thể là dạng file");
							JOptionPane.showMessageDialog(FileExplore.this, "bản gốc là folder, bản sao không thể là dạng file");
						}
						else if (new File(newLinkPath).exists() && new File(newLinkPath).isDirectory()) {
							// folder bản sao tồn tại,vậy có nghĩa người dùng muốn
							// sao chép folder bản sao tại đây, với từ khóa phân biệt là new.
							System.out.println("thích thì tên với từ new");
							newLinkPath += File.separator + "New " + new File(originLinkPath).getName();
							System.out.println("newLinkPath: " + newLinkPath);
							try {
								copyDir(originLinkPath, newLinkPath);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							repaintTable(presentPath);
							repaintTree(presentPath);
						}
						else if (new File(newLinkPath).exists() == false && new File(new File(newLinkPath).getParent()).isDirectory()) {
							// folder không tồn tại, nhưng đường dẫn cha lại tồn tại, 
							// chứng tỏ đây là tên mà người dùng muốn đặt cho folder bản sao.
							System.out.println("ok có rồi");
							System.out.println("newLinkPath: " + newLinkPath);
							try {
								copyDir(originLinkPath, newLinkPath);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							repaintTable(presentPath);
							repaintTree(presentPath);
						} else {
							System.out.println("đường dẫn bản sao không tồn tại hoặc không hợp lệ");
							JOptionPane.showMessageDialog(FileExplore.this, "đường dẫn bản sao không tồn tại hoặc không hợp lệ");
						}
					}
				} else {
					System.out.println("đường dẫn bản gốc không tồn tại hoặc không hợp lệ");
					JOptionPane.showMessageDialog(FileExplore.this, "đường dẫn bản gốc không tồn tại hoặc không hợp lệ");
				}

			}
		});
		subJPanelCopyFunction.add(btnCopy);
		
		JLabel lblNewLabel_2_1 = new JLabel("Hướng dẫn:");
		lblNewLabel_2_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_2_1.setBounds(752, 23, 75, 24);
		subJPanelCopyFunction.add(lblNewLabel_2_1);
		
		JLabel lblnhnChutPhi = new JLabel("*Nhấn chuột phải chọn chức năng copy");
		lblnhnChutPhi.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblnhnChutPhi.setBounds(614, 57, 278, 13);
		subJPanelCopyFunction.add(lblnhnChutPhi);
		
		JLabel lblHocVoTab_4 = new JLabel("hoặc vào tab new để sử dụng chức năng copy");
		lblHocVoTab_4.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblHocVoTab_4.setBounds(614, 80, 294, 13);
		subJPanelCopyFunction.add(lblHocVoTab_4);
	}
	
	public void setUpJPanelCut() {
		JLabel lblNewLabel_6_1 = new JLabel("Đường dẫn File/Folder gốc:");
		lblNewLabel_6_1.setBounds(10, 10, 176, 13);
		subJPanelCutFunction.add(lblNewLabel_6_1);
		
		JLabel lblNewLabel_6_1_1 = new JLabel("Đường dẫn folder nơi lưu File/Folder bản cut");
		lblNewLabel_6_1_1.setBounds(10, 27, 277, 13);
		subJPanelCutFunction.add(lblNewLabel_6_1_1);
		
		textFieldSubJPanelCutOriginLinkPath = new JTextField();
		textFieldSubJPanelCutOriginLinkPath.setText("Ví dụ: C:\\Users\\Acer\\Desktop\\MyWord.doc");
		textFieldSubJPanelCutOriginLinkPath.setColumns(10);
		textFieldSubJPanelCutOriginLinkPath.setBounds(294, 7, 362, 19);
		subJPanelCutFunction.add(textFieldSubJPanelCutOriginLinkPath);
		
		textFieldSubJPanelCutNewLinkPath = new JTextField();
		textFieldSubJPanelCutNewLinkPath.setText("Ví dụ: C:\\Users\\Acer\\Desktop\\MyWordFolder");
		textFieldSubJPanelCutNewLinkPath.setColumns(10);
		textFieldSubJPanelCutNewLinkPath.setBounds(294, 24, 362, 19);
		subJPanelCutFunction.add(textFieldSubJPanelCutNewLinkPath);
		
		btnCut = new JButton("Cut");
		btnCut.setBounds(10, 56, 85, 21);
		btnCut.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String originLinkPath = textFieldSubJPanelCutOriginLinkPath.getText().trim();
				String newLinkPath    = textFieldSubJPanelCutNewLinkPath.getText().trim();
				
				String resultOriginLink = getExtension(originLinkPath);
				String resultNewLink = getExtension(newLinkPath);
				
				if (new File(originLinkPath).exists()) {
					if (new File(originLinkPath).isFile()) {
						// nếu bản gốc là file, kiểm tra tiếp đường dẫn ở bản sao.
						if (new File(newLinkPath).exists() && new File(newLinkPath).isFile()) {
							copyFile(originLinkPath, newLinkPath);
							deleteFile(originLinkPath);
							repaintTable(presentPath);
							repaintTree(presentPath);
						} else if (new File(newLinkPath).exists() && new File(newLinkPath).isDirectory()) {
							if (deleteLastSymbol(newLinkPath, File.separator) != "-1") {
								newLinkPath = new String(deleteLastSymbol(newLinkPath, File.separator));
							}
							newLinkPath += File.separator + "New " + new File(originLinkPath).getName();
							System.out.println("newLinkPath: " + newLinkPath);
							copyFile(originLinkPath, newLinkPath);
							deleteFile(originLinkPath);
							repaintTable(presentPath);
							repaintTree(presentPath);
						} else {
							System.out.println("đường dẫn bản Cut không hợp lệ hoặc không đúng");
							JOptionPane.showMessageDialog(FileExplore.this, "đường dẫn bản Cut không hợp lệ hoặc không đúng");
						}
					} else {
						// nếu bản gốc là folder, kiểm tra tiếp đường dẫn ở bản sao.
						if (new File(newLinkPath).exists() && new File(newLinkPath).isFile()) {
							System.out.println("bản gốc là folder, bản Cut không thể là dạng file");
							JOptionPane.showMessageDialog(FileExplore.this, "bản gốc là folder, bản Cut không thể là dạng file");
						}
						else if (new File(newLinkPath).exists() && new File(newLinkPath).isDirectory()) {
							// folder bản sao tồn tại,vậy có nghĩa người dùng muốn
							// sao chép folder bản sao tại đây, với từ khóa phân biệt là new.
							System.out.println("thích thì tên với từ new");
							newLinkPath += File.separator + "New " + new File(originLinkPath).getName();
							System.out.println("newLinkPath: " + newLinkPath);
							try {
								copyDir(originLinkPath, newLinkPath);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							deleteDir(originLinkPath);
							repaintTable(presentPath);
							repaintTree(presentPath);
						}
						else if (new File(newLinkPath).exists() == false && new File(new File(newLinkPath).getParent()).isDirectory()) {
							// folder không tồn tại, nhưng đường dẫn cha lại tồn tại, 
							// chứng tỏ đây là tên mà người dùng muốn đặt cho folder bản sao.
							System.out.println("ok có rồi");
							System.out.println("newLinkPath: " + newLinkPath);
							try {
								copyDir(originLinkPath, newLinkPath);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							deleteDir(originLinkPath);
							repaintTable(presentPath);
							repaintTree(presentPath);
						} else {
							System.out.println("đường dẫn bản Cut không tồn tại hoặc không hợp lệ");
							JOptionPane.showMessageDialog(FileExplore.this, "đường dẫn bản Cut không tồn tại hoặc không hợp lệ");
						}
					}
				} else {
					System.out.println("đường dẫn bản Cut không tồn tại hoặc không hợp lệ");
					JOptionPane.showMessageDialog(FileExplore.this, "đường dẫn bản Cut không tồn tại hoặc không hợp lệ");
				}
			}
		});
		subJPanelCutFunction.add(btnCut);
		
		JLabel lblNewLabel_2_1 = new JLabel("Hướng dẫn:");
		lblNewLabel_2_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_2_1.setBounds(744, 27, 75, 24);
		subJPanelCutFunction.add(lblNewLabel_2_1);
		
		JLabel lblnhnChutPhi_1 = new JLabel("*Nhấn chuột phải chọn chức năng cut");
		lblnhnChutPhi_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblnhnChutPhi_1.setBounds(630, 53, 278, 13);
		subJPanelCutFunction.add(lblnhnChutPhi_1);
		
		JLabel lblHocVoTab_4 = new JLabel("hoặc vào tab new để sử dụng chức năng cut");
		lblHocVoTab_4.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblHocVoTab_4.setBounds(614, 76, 294, 13);
		subJPanelCutFunction.add(lblHocVoTab_4);
	}
	
	// 1 là file càn đổi tên là dạng file, 0 là file cần đổi tên là dạng folder
	int typeToRename = 1;
	public void setUpJPanelRename() {
		JLabel lblNewLabel_11 = new JLabel("Đường dẫn:");
		lblNewLabel_11.setBounds(10, 10, 152, 13);
		subJPanelRenameFunction.add(lblNewLabel_11);
		
		textFieldSubJPanelRenameLinkPath = new JTextField();
		textFieldSubJPanelRenameLinkPath.setText("Ví dụ: C:\\Users\\Acer\\Desktop");
		textFieldSubJPanelRenameLinkPath.setBounds(172, 7, 335, 19);
		subJPanelRenameFunction.add(textFieldSubJPanelRenameLinkPath);
		textFieldSubJPanelRenameLinkPath.setColumns(10);
		
		JLabel lblNewLabel_12 = new JLabel("Loại file:");
		lblNewLabel_12.setBounds(10, 33, 152, 13);
		subJPanelRenameFunction.add(lblNewLabel_12);
		
		lblSubJPanelRenameTypeFile = new JLabel("");
		lblSubJPanelRenameTypeFile.setBounds(172, 33, 335, 13);
		subJPanelRenameFunction.add(lblSubJPanelRenameTypeFile);
		
		lblNewLabel_14 = new JLabel("Tên mới của File/Folder:");
		lblNewLabel_14.setBounds(10, 56, 152, 13);
		subJPanelRenameFunction.add(lblNewLabel_14);
		
		textFieldSubJPanelRenameNewName = new JTextField();
		textFieldSubJPanelRenameNewName.setBounds(172, 53, 335, 19);
		subJPanelRenameFunction.add(textFieldSubJPanelRenameNewName);
		textFieldSubJPanelRenameNewName.setColumns(10);
		
		lblHngDn_1 = new JLabel("Hướng dẫn:");
		lblHngDn_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblHngDn_1.setBounds(535, 3, 75, 23);
		subJPanelRenameFunction.add(lblHngDn_1);
		
		lblCo_1 = new JLabel("*Nhấn chuột phải chọn chức năng rename");
		lblCo_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCo_1.setBounds(573, 31, 278, 13);
		subJPanelRenameFunction.add(lblCo_1);
		
		lblHocVoTab_2 = new JLabel("hoặc vào tab rename để sử dụng chức năng rename.");
		lblHocVoTab_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblHocVoTab_2.setBounds(583, 54, 335, 13);
		subJPanelRenameFunction.add(lblHocVoTab_2);
		
		btnRename = new JButton("Rename");
		btnRename.setBounds(404, 84, 103, 19);
		btnRename.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String path = textFieldSubJPanelRenameLinkPath.getText().trim();
				File file = new File(path);
				if (file.exists()) {
					if (file.isFile()) {
						String extension = "";

	            		int i = file.getName().lastIndexOf('.');
	            		if (i > 0) {
	            		    extension = file.getName().substring(i+1);
	            		}
	            		
	            		String pathOfFileAfterRename = file.getParent() + File.separator + textFieldSubJPanelRenameNewName.getText().trim() +"."+ extension;
	            		rename(pathOfFileAfterRename);
					} else {
						String pathOfFileAfterRename = file.getParent() + File.separator + textFieldSubJPanelRenameNewName.getText().trim();
						rename(pathOfFileAfterRename);
					}
				}
				
			}
		});
		subJPanelRenameFunction.add(btnRename);
	}
	
	public void setUpJPanelDelete() {
		JLabel lblNewLabel_11 = new JLabel("Đường dẫn:");
		lblNewLabel_11.setBounds(10, 10, 152, 13);
		subJPanelDeleteFunction.add(lblNewLabel_11);
		
		textFieldSubJPanelDeleteLinkPath = new JTextField();
		textFieldSubJPanelDeleteLinkPath.setText("Ví dụ: C:\\Users\\Acer\\Desktop");
		textFieldSubJPanelDeleteLinkPath.setBounds(176, 7, 346, 19);
		subJPanelDeleteFunction.add(textFieldSubJPanelDeleteLinkPath);
		textFieldSubJPanelDeleteLinkPath.setColumns(10);
		
		JLabel lblNewLabel_12 = new JLabel("Loại file:");
		lblNewLabel_12.setBounds(10, 33, 152, 13);
		subJPanelDeleteFunction.add(lblNewLabel_12);
		
		lblSubJPanelDeleteTypeFile = new JLabel("");
		lblSubJPanelDeleteTypeFile.setBounds(176, 36, 346, 13);
		subJPanelDeleteFunction.add(lblSubJPanelDeleteTypeFile);
		
		btnDelete = new JButton("Delete");
		btnDelete.setBounds(10, 56, 118, 37);
		btnDelete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (new File(textFieldSubJPanelDeleteLinkPath.getText().trim()).isFile()) {
					deleteFile(textFieldSubJPanelDeleteLinkPath.getText().trim());
					textFieldSubJPanelDeleteLinkPath.setText("");
					repaintTable(presentPath);
					repaintTree(presentPath);
				} else {
					deleteDir(textFieldSubJPanelDeleteLinkPath.getText().trim());
					textFieldSubJPanelDeleteLinkPath.setText("");
					repaintTable(presentPath);
					repaintTree(presentPath);
				}
			}
		});
		subJPanelDeleteFunction.add(btnDelete);
		
		lblHngDn_2 = new JLabel("Hướng dẫn:");
		lblHngDn_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblHngDn_2.setBounds(545, 7, 75, 23);
		subJPanelDeleteFunction.add(lblHngDn_2);
		
		lblCo_2 = new JLabel("*Nhấn chuột phải chọn chức năng delete");
		lblCo_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCo_2.setBounds(555, 33, 278, 13);
		subJPanelDeleteFunction.add(lblCo_2);
		
		lblHocVoTab_1 = new JLabel("hoặc vào tab delete để sử dụng chức năng delete.");
		lblHocVoTab_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblHocVoTab_1.setBounds(565, 50, 335, 13);
		subJPanelDeleteFunction.add(lblHocVoTab_1);
	}
	
	int typeOfCompress = 0;
	public void setUpJPanelCompress() {
		
		ButtonGroup group = new ButtonGroup();
		
		JRadioButton rdbtnZipCompress = new JRadioButton("Zip");
		rdbtnZipCompress.setBounds(6, 59, 57, 21);
		subJPanelCompressFunction.add(rdbtnZipCompress);
		
		JRadioButton rdbtnJarCompress = new JRadioButton("Jar");
		rdbtnJarCompress.setBounds(65, 59, 51, 21);
		subJPanelCompressFunction.add(rdbtnJarCompress);
		
		JRadioButton rdbtn7ZipCompress = new JRadioButton("7Zip");
		rdbtn7ZipCompress.setBounds(118, 59, 57, 21);
		subJPanelCompressFunction.add(rdbtn7ZipCompress);
		
		JRadioButton rdbtnRarCompress = new JRadioButton("Rar");
		rdbtnRarCompress.setBounds(177, 59, 57, 21);
		subJPanelCompressFunction.add(rdbtnRarCompress);
		
		group.add(rdbtnZipCompress);
		group.add(rdbtnJarCompress);
		group.add(rdbtn7ZipCompress);
		group.add(rdbtnRarCompress);
		
		ActionListener actionListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (e.getSource() == rdbtnZipCompress) {
                    typeOfCompress = 1;
                }
                if (e.getSource() == rdbtnJarCompress) {
                	typeOfCompress = 2;
                }
                if (e.getSource() == rdbtn7ZipCompress) {
                	typeOfCompress = 3;
                }
                if (e.getSource() == rdbtnRarCompress) {
                    typeOfCompress = 4;
                }
			}
		};
		
		rdbtnZipCompress.addActionListener(actionListener);
		rdbtnJarCompress.addActionListener(actionListener);
		rdbtn7ZipCompress.addActionListener(actionListener);
		rdbtnRarCompress.addActionListener(actionListener);
		
		
		JLabel lblngDnFilefolder = new JLabel("Đường dẫn File/Folder:");
		lblngDnFilefolder.setBounds(6, 10, 154, 13);
		subJPanelCompressFunction.add(lblngDnFilefolder);
		
		textFieldSubJPanelCompressLinkPath = new JTextField();
		textFieldSubJPanelCompressLinkPath.setText("Ví dụ: C:\\Users\\Acer\\Desktop\\MyWord.doc");
		textFieldSubJPanelCompressLinkPath.setColumns(10);
		textFieldSubJPanelCompressLinkPath.setBounds(170, 7, 449, 19);
		subJPanelCompressFunction.add(textFieldSubJPanelCompressLinkPath);
		
		JLabel lblnhDngMun = new JLabel("Định dạng muốn nén là:   (File nén sẽ được lưu vào cùng thư mục với File/Folder được nén )");
		lblnhDngMun.setBounds(6, 40, 613, 13);
		subJPanelCompressFunction.add(lblnhDngMun);
		
		btnCompress = new JButton("Nén");
		btnCompress.setBounds(253, 59, 85, 21);
		btnCompress.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String path = textFieldSubJPanelCompressLinkPath.getText().trim();
				switch (typeOfCompress) {
				case 0:
					JOptionPane.showMessageDialog(FileExplore.this, "Chưa chọn định dạng nén");
					break;
				case 1:
					if (new File(path).exists()) {
						if (new File(path).isFile()) {
							// zip
							try {
								compressFileToZip(path);
								
								repaintTable(presentPath);
								repaintTree(presentPath);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							try {
								compressDirToZip(path);

								repaintTable(presentPath);
								repaintTree(presentPath);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					} else {JOptionPane.showMessageDialog(FileExplore.this, "File/Folder không tồn tại, hoặc đường dẫn không chính xác");}
					break;
				case 2:
					if (new File(path).exists()) {
						if (new File(path).isFile()) {
							
						} else {
							
						}
					} else {JOptionPane.showMessageDialog(FileExplore.this, "File/Folder không tồn tại, hoặc đường dẫn không chính xác");}
					break;
				case 3:
					if (new File(path).exists()) {
						if (new File(path).isFile()) {
							
						} else {
							
						}
					} else {JOptionPane.showMessageDialog(FileExplore.this, "File/Folder không tồn tại, hoặc đường dẫn không chính xác");}
					break;
				case 4:
					if (new File(path).exists()) {
//						if (new File(path).isFile()) {
//							
//						} else {
//							
//						}
						try {
							compressFileToRAR(path);
						} catch (Exception e) {
							// TODO: handle exception
						}
						
						repaintTable(presentPath);
						repaintTree(presentPath);
						
					} else {JOptionPane.showMessageDialog(FileExplore.this, "File/Folder không tồn tại, hoặc đường dẫn không chính xác");}
					break;
				}
			}
		});
		subJPanelCompressFunction.add(btnCompress);
	}
	
	public void setUpJPanelExtract() {
		JLabel lblngDnFilefolder = new JLabel("Đường dẫn File/Folder:");
		lblngDnFilefolder.setBounds(10, 10, 154, 13);
		subJPanelExtractFunction.add(lblngDnFilefolder);
		
		textFieldSubJPanelExtractLinkPath = new JTextField();
		textFieldSubJPanelExtractLinkPath.setText("Ví dụ: C:\\Users\\Acer\\Desktop\\MyWord.doc");
		textFieldSubJPanelExtractLinkPath.setColumns(10);
		textFieldSubJPanelExtractLinkPath.setBounds(173, 7, 362, 19);
		subJPanelExtractFunction.add(textFieldSubJPanelExtractLinkPath);
		
		JLabel lblnhDngMun = new JLabel("File/Folder sau khi giải nén sẽ được lưu vào cùng thư mục với File/Folder được giải nén");
		lblnhDngMun.setBounds(10, 52, 580, 13);
		subJPanelExtractFunction.add(lblnhDngMun);
		
		JLabel lblNewLabel_12 = new JLabel("Loại file:");
		lblNewLabel_12.setBounds(10, 29, 152, 13);
		subJPanelExtractFunction.add(lblNewLabel_12);
		
		lblSubJPanelExtractTypeFile = new JLabel("");
		lblSubJPanelExtractTypeFile.setBounds(173, 29, 335, 13);
		subJPanelExtractFunction.add(lblSubJPanelExtractTypeFile);
		
		btnExtract = new JButton("Giải nén");
		btnExtract.setBounds(10, 72, 104, 21);
		btnExtract.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					String extension = getExtension(textFieldSubJPanelExtractLinkPath.getText().trim());
					
//					if (extension.equals("-1") || extension.equals("zip") == false || extension.equals("jar") == false || extension.equals("7zip") == false || extension.equals("rar") == false) {
//						JOptionPane.showMessageDialog(MainActivity.this, "Không phải dạng file nén, hoặc đường dẫn không chính xác");
//					}
					
					if (extension.trim().equals("zip")) {
						if (new File(textFieldSubJPanelExtractLinkPath.getText().trim()).exists()) {
							Path source = Paths.get(textFieldSubJPanelExtractLinkPath.getText().trim());
							
							File file = new File(textFieldSubJPanelExtractLinkPath.getText().trim());
					        Path target = Paths.get(file.getParent());
					        
					        //System.out.println("asdasdaasd: " + file.getParentFile() + File.separator + file.getName());
							unzipFolder(source, target);
							
							
							repaintTable(presentPath);
							repaintTree(presentPath);
						} else {JOptionPane.showMessageDialog(FileExplore.this, "Không tồn tại file nén zip như vậy, kiểm tra lại đường dần");}
					}
					
					if (extension.trim().equals("jar")) {
						if (new File(textFieldSubJPanelExtractLinkPath.getText().trim()).exists()) {
							
							repaintTable(presentPath);
							repaintTree(presentPath);
						} else {JOptionPane.showMessageDialog(FileExplore.this, "Không tồn tại file nén jar như vậy, kiểm tra lại đường dần");}
					}
					
					if (extension.trim().equals("7z")) {
						if (new File(textFieldSubJPanelExtractLinkPath.getText().trim()).exists()) {
							extract7Zip(textFieldSubJPanelExtractLinkPath.getText().trim());
							
							repaintTable(presentPath);
							repaintTree(presentPath);
						} else {JOptionPane.showMessageDialog(FileExplore.this, "Không tồn tại file nén 7zip như vậy, kiểm tra lại đường dần");}
					}
					
					if (extension.trim().equals("rar")) {
						if (new File(textFieldSubJPanelExtractLinkPath.getText().trim()).exists()) {
							if (new File(textFieldSubJPanelExtractLinkPath.getText().trim()).exists()) {
								extractRAR(textFieldSubJPanelExtractLinkPath.getText().trim());
								
								repaintTable(presentPath);
								repaintTree(presentPath);
							} else {
								JOptionPane.showMessageDialog(FileExplore.this, "Không tồn tại file nén rar như vậy, kiểm tra lại đường dần");
							}
						} else {JOptionPane.showMessageDialog(FileExplore.this, "Không tồn tại file nén rar như vậy, kiểm tra lại đường dần");}
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		subJPanelExtractFunction.add(btnExtract);
		contentPane.add(jtabbedPane);
	}
	
	
	// --------------------------------------------------
	// cập nhật lại Tree mỗi khi có thay đổi như thêm, xóa file, thư mục
	
	public void repaintTree(String path) {
		try {
			scrollPane.remove(jtree);
			contentPane.remove(jtree);
			contentPane.remove(scrollPane);
			//fileRoot = null;
			// đối tượng file gốc
			//fileRoot = new File(startRootPath);
			// tạo ra root mother
			jtree = configureJTree(path);
			//presentPath = new String(startRootPath);
			//textFieldPresentPath.setText(presentPath);
			jtree.setBounds(10, 60, 221, 601);
			contentPane.add(jtree);
			
			scrollPane = new ScrollPane();
			scrollPane.setBounds(10, 60, 221, 601);
			
			// disable this, if you want to use design mode
			
			contentPane.add(scrollPane);
			scrollPane.add(jtree);
			scrollPane.repaint();
			contentPane.repaint();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	// cập nhật lại Table mỗi khi có thay đổi như thêm, xóa file, thư mục
	// tham số path ở đâu là để lấy đường dẫn hiện tại, phục vụ cho việc liệt kê lại file, nhằm update
	
	public void repaintTable(String path) {
		try {
			contentPane.remove(jScrollPane);
			fileViewModelList.clear();
			jtable = null;
			jScrollPane = null;
			
//			fileViewModelList = getFileViewModel(path);
//			CustomFileTableModel customFileTableModel = new CustomFileTableModel(fileViewModelList);
//			jtable = new JTable(customFileTableModel);
//			jtable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
			
			jtable = configureJTable(path);
			
			jScrollPane = new JScrollPane(jtable);
			jScrollPane.setBounds(241, 61, 923, 464);
			jtable.setFillsViewportHeight(true);
			contentPane.add(jScrollPane);
			contentPane.repaint();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	// --------------------------------------------------------------------
	public ArrayList<FileViewModel> fileViewModelList = new ArrayList<FileViewModel>();
	
	public JTable configureJTable(String path) {
		//DefaultTableModel defaultTableModel = new DefaultTableModel();
		
		fileViewModelList = getFileViewModel(path);
		CustomFileTableModel customFileTableModel = new CustomFileTableModel(fileViewModelList);
		//JTable jtable = new JTable(data, header);
		JTable jtable = new JTable(customFileTableModel);
		jtable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
		jtable.setRowHeight(15);
		
		int[] columnsWidth = {
	            20, 200, 400, 75, 75, 75
	    };
		for (int i = 0; i < jtable.getColumnCount(); i++) {
			TableColumn tableColumn = jtable.getColumnModel().getColumn(i);
			tableColumn.setWidth(columnsWidth[i]);
			tableColumn.setPreferredWidth(columnsWidth[i]);
		}
		
		jtable.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				// TODO Auto-generated method stub
				
				// nhấp chuột trái 2 lần
				if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton() == MouseEvent.BUTTON1) {
				    System.out.println("double left clicked");
				    int columnNumber = jtable.getSelectedColumn();
				    int rowNumber = jtable.getSelectedRow();
				    
				    FileViewModel fileViewModel = fileViewModelList.get(rowNumber);
				    System.out.println("path: " + fileViewModel.getPath());
				    
				    doStackJTableStuff(fileViewModel);
				}
				
				// nhấn chuột trái 1 lần
				if (mouseEvent.getClickCount() == 1 && mouseEvent.getButton() == MouseEvent.BUTTON1) {
					System.out.println("one left clicked");
					int columnNumber = jtable.getSelectedColumn();
				    int rowNumber = jtable.getSelectedRow();
				    
				    FileViewModel fileViewModel = fileViewModelList.get(rowNumber);
				    System.out.println("path: " + fileViewModel.getPath());
				    
				    if (new File(fileViewModel.getPath()).isFile()) {
				    	textFieldSubJPanelOpenLinkPath.setText(fileViewModel.getPath());
				    	textFieldSubJPanelRenameLinkPath.setText(fileViewModel.getPath());
				    	textFieldSubJPanelDeleteLinkPath.setText(fileViewModel.getPath());
				    	textFieldSubJPanelCopyOriginLinkPath.setText(fileViewModel.getPath());
				    	textFieldSubJPanelCutOriginLinkPath.setText(fileViewModel.getPath());
				    	textFieldSubJPanelCompressLinkPath.setText(fileViewModel.getPath());
				    	textFieldSubJPanelExtractLinkPath.setText(fileViewModel.getPath());
				    	String extension = "";
	            		int i = fileViewModel.getFile().getName().lastIndexOf('.');
	            		if (i > 0) {
	            		    extension = fileViewModel.getFile().getName().substring(i+1);
	            		}
					    lblSubJPanelOpenTypeFile.setText("File " + extension);
					    lblSubJPanelRenameTypeFile.setText("File " + extension);
					    lblSubJPanelDeleteTypeFile.setText("File " + extension);
					    lblSubJPanelExtractTypeFile.setText("File " + extension);
					    
					    
					    
				    } else {
				    	// nếu là folder
				    	textFieldSubJPanelRenameLinkPath.setText(fileViewModel.getPath());
				    	textFieldSubJPanelDeleteLinkPath.setText(fileViewModel.getPath());
				    	textFieldSubJPanelCopyOriginLinkPath.setText(fileViewModel.getPath());
				    	textFieldSubJPanelCutOriginLinkPath.setText(fileViewModel.getPath());
				    	textFieldSubJPanelCompressLinkPath.setText(fileViewModel.getPath());
				    	textFieldSubJPanelExtractLinkPath.setText(fileViewModel.getPath());
				    	
				    	lblSubJPanelRenameTypeFile.setText("Đây là Folder");
				    	lblSubJPanelDeleteTypeFile.setText("Đây là folder");
				    	lblSubJPanelExtractTypeFile.setText("Đây là Folder");
				    }
				}  
				
				// nhấn chuột phải 1 lần, hiện popup
				if (mouseEvent.getClickCount() == 1 && mouseEvent.getButton() == MouseEvent.BUTTON3) {
				    System.out.println("one right clicked");
				}
			}
		});
		return jtable;
	}
	
	public void doStackJTableStuff(FileViewModel fileViewModel) {
		File file = new File(fileViewModel.getPath()) ;
	    if (file.isDirectory()) {
	    	
	    	if (stackOfBackPath.empty()) {
	    		// trường hợp chương trình mới khởi chạy
	    		isBackCanClick = true;
	    		btnBack.setIcon(createFormatImageIcon(resourcePath+"backon.png", btnBack.getWidth() , btnBack.getHeight()));
	    		
	    		stackOfBackPath.push(startRootPath);
	    		presentPath = fileViewModel.getPath();
	    		textFieldPresentPath.setText(fileViewModel.getPath().trim());
	    		
	    		repaintTable(fileViewModel.getPath());
				repaintTree(fileViewModel.getPath());
	    	} else {
	    		System.out.println("presentPath: " + presentPath);
	    		stackOfBackPath.push(presentPath);
	    		presentPath = fileViewModel.getPath();
	    		textFieldPresentPath.setText(fileViewModel.getPath().trim());
	    		
	    		repaintTable(fileViewModel.getPath());
	    		repaintTree(fileViewModel.getPath());
	    	}
	    	
	    }
	    
	    if (file.isFile()) {
	    	// open this file
	    	if(!Desktop.isDesktopSupported()){
	            System.out.println("Desktop is not supported");
	        }
	    	Desktop desktop = Desktop.getDesktop();
	        try {
				desktop.open(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	
//  ---------------------------------------------------------------------------	
	// lưu đường dẫn hiện tại trước đó (có thể là file hoặc directory), phục vụ cho việc lưu
	// trung gian giữa 2 stack.
	private String tempPath;
	
	// lưu đường dẫn directory trước đó
	private String tempDirectoryPath;
	
	
	private JLabel lblSubJPanelOpenTypeFile;
	private JTextField textFieldSubJPanelOpenLinkPath;
	private JLabel lblHngDn;
	private JLabel lblCo;
	private JLabel lblHocVoTab;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;
	private JLabel lblNewLabel_4;
	private JLabel lblNewLabel_5;
	private JLabel lblNewLabel_6;
	private JTextField textFieldSubJPanelNewLinkPath;
	private JLabel lblNewLabel_7;
	private JButton btnButtonChoseNewAsFileOrDir;
	private JLabel lblNewLabel_8;
	private JTextField textFieldSubJPanelNewFileType;
	private JLabel lblNewLabel_9;
	private JLabel lblNewLabel_10;
	private JTextField textFieldSubJPanelNewNameÒf;
	private JButton btnNew;
	private JTextField textFieldSubJPanelRenameLinkPath;
	private JLabel lblSubJPanelRenameTypeFile;
	private JLabel lblNewLabel_14;
	private JTextField textFieldSubJPanelRenameNewName;
	private JLabel lblHngDn_1;
	private JLabel lblCo_1;
	private JLabel lblHocVoTab_2;
	private JTextField textFieldSubJPanelDeleteLinkPath;
	private JLabel lblSubJPanelDeleteTypeFile;
	private JLabel lblHngDn_2;
	private JLabel lblCo_2;
	private JLabel lblHocVoTab_1;
	private JLabel lblSubJPanelExtractTypeFile;
	private JTextField textFieldSubJPanelCopyOriginLinkPath;
	private JTextField textFieldSubJPanelCopyNewLinkPath;
	private JTextField textFieldSubJPanelCutOriginLinkPath;
	private JTextField textFieldSubJPanelCutNewLinkPath;
	private JTextField textFieldSubJPanelCompressLinkPath;
	private JTextField textFieldSubJPanelExtractLinkPath;
	private JButton btnGetPath;
//  ---------------------------------------------------------------------------
	
	public JTree configureJTree(String path) {
		File fileRoot = new File(path);
		DefaultMutableTreeNode defaultMutableTreeNodeTop1 = new DefaultMutableTreeNode(fileRoot);
		insertTreeNode(defaultMutableTreeNodeTop1, fileRoot);
		JTree jtree = new JTree(defaultMutableTreeNodeTop1);
		// để render hình ảnh tùy ý
		//jtree.setCellRenderer(new MyTreeCellRenderer());
		
		jtree.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
				// TODO Auto-generated method stub
				System.out.println("Array: " + treeSelectionEvent.getPath());
				
				// đường dẫn hiện tại
				presentPath = treeSelectionEvent.getPath().getPathComponent(0).toString();
				for (int i = 1; i < treeSelectionEvent.getPath().getPathCount(); i++) {
					presentPath += "\\" + treeSelectionEvent.getPath().getPathComponent(i).toString();
				}
				
				// lưu trữ đường dẫn
				doStackJtreeStuff(presentPath, treeSelectionEvent);
				
			}
		});
		return jtree;
	}
	
	public void doStackJtreeStuff(String presentPath, TreeSelectionEvent treeSelectionEvent) {
		// đẩy đường dẫn cũ vào stack
		// nếu là directory thì nạp path vào stackBack
		if (new File(presentPath).isDirectory()) {
			
			System.out.println("hello");
			if (stackOfBackPath.empty()) {
				// trong trường hợp khởi động ứng dụng lần đàu
				// lúc chương trình mới chạy, này đường dẫn cũ đó là startRootPath
	    		// lúc này startRootPath cũng chính là presentPath
				tempPath          = new String(presentPath);
				tempDirectoryPath = new String(tempPath);
				stackOfBackPath.push(startRootPath);
				isBackCanClick = true;
				btnBack.setIcon(createFormatImageIcon(resourcePath + "backon.png", btnBack.getWidth(), btnBack.getHeight()));
				// hiển thị đường dẫn thư mục
				textFieldPresentPath.setText(presentPath);
				repaintTable(presentPath);
			} else {
				// thực hiện thêm element vào stack
				stackOfBackPath.push(tempPath);
				tempPath          = new String(presentPath);
				tempDirectoryPath = new String(tempPath);
				// hiển thị đường dẫn thư mục
				textFieldPresentPath.setText(presentPath);
				repaintTable(presentPath);
			}
		} else {
			// nếu là dạng file, thì check xem cái file đó, có nằm trong folder đang xem không
			// nếu có thì dữ nguyên đường dẫn thư mục
			// nếu không thì lấy đường dẫn thư mục đó ra để lưu
			
			System.out.println("hi");
			String presentDirectoryPath = treeSelectionEvent.getPath().getPathComponent(0).toString();
			for (int i = 1; i < treeSelectionEvent.getPath().getPathCount() - 1; i++) {
				presentDirectoryPath += "\\" + treeSelectionEvent.getPath().getPathComponent(i).toString();
			}
			System.out.println(presentDirectoryPath);
			
			if (tempDirectoryPath == null) {
				// nếu null là do chương trình mới khởi chạy
				tempDirectoryPath = new String(presentDirectoryPath);
				textFieldPresentPath.setText(presentDirectoryPath);
				repaintTable(presentDirectoryPath);
			} else {
				if (!presentDirectoryPath.equals(tempDirectoryPath)) {
					// nếu đường dẫn thư mục hiện dại khác với đường dẫn thư mục trước đó.
					System.out.println("presentDirectoryPath: " + presentDirectoryPath);
					System.out.println("tempDirectoryPath: " + tempDirectoryPath);
					
					if (stackOfBackPath.empty()) {
						// trong trường hợp khởi động ứng dụng lần đàu
						// lúc chương trình mới chạy, này đường dẫn cũ đó là startRootPath
						tempPath          = new String(presentPath);
						tempDirectoryPath = new String(tempPath);
						stackOfBackPath.push(startRootPath);
						isBackCanClick = true;
						btnBack.setIcon(createFormatImageIcon(resourcePath + "backon.png", btnBack.getWidth(), btnBack.getHeight()));
						// hiển thị đường dẫn thư mục
						//textFieldPresentPath.setText(presentPath);
						textFieldPresentPath.setText(presentDirectoryPath);
						repaintTable(presentDirectoryPath);
					} else {
						// thực hiện thêm element vào stack
						stackOfBackPath.push(tempPath);
						tempPath          = new String(presentPath);
						tempDirectoryPath = new String(tempPath);
						// hiển thị đường dẫn thư mục
						//textFieldPresentPath.setText(presentPath);
						textFieldPresentPath.setText(presentDirectoryPath);
						repaintTable(presentDirectoryPath);
					}
				}
			}
			
		}
		// END
	}
	
	public void insertTreeNode(DefaultMutableTreeNode motherTreeNode ,File fileRoot) {
		File[] fileList = fileRoot.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].isFile()) {
				motherTreeNode.add(new DefaultMutableTreeNode(fileList[i].getName()));
				//System.out.println(fileList[i].getAbsolutePath());
			}
			
			if (fileList[i].isDirectory() && fileList.length != 0) {
				DefaultMutableTreeNode bornNewTreeNode = new DefaultMutableTreeNode(fileList[i].getName());
				motherTreeNode.add(bornNewTreeNode);
				//System.out.println(fileList[i].getAbsoluteFile());
				//insertTreeNode(bornNewTreeNode, fileList[i].getAbsoluteFile());
				try {
					insertTreeNode(bornNewTreeNode, fileList[i].getAbsoluteFile());
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
		
	}
	
	public ArrayList<FileViewModel> getFileViewModel(String path) {
		File file = new File(path);
		File[] files = file.listFiles();
		
		ArrayList<FileViewModel> fileViewModelList = new ArrayList<FileViewModel>();
		for (File inFile : files) {
			String url = "";
			if (inFile.isFile()) {
				url = resourcePath + "fileicon.png";
			} else {
				url = resourcePath + "foldericon.png";
			}
			Date date = new Date(inFile.lastModified());
			
			fileViewModelList.add(new FileViewModel(url, 
													inFile.getName(), 
													inFile.getAbsolutePath(), 
													date.toString(), 
													inFile.isFile(), 
													getFileSizeKiloBytes(inFile), 
													inFile));
		}
		
		return fileViewModelList;
	}
	
	private static String getFileSizeMegaBytes(File file) {
		return (double) file.length() / (1024 * 1024) + " mb";
	}
	
	private static String getFileSizeKiloBytes(File file) {
		return (double) file.length() / 1024 + "  kb";
	}

	private static String getFileSizeBytes(File file) {
		return file.length() + " bytes";
	}
	
	// Image Engine ----------------------------------
	public ImageIcon createFormatImageIcon(String url, int width, int height) {
		BufferedImage bufferdImage;
		try {
			bufferdImage = ImageIO.read(new File(url));
			Image imageScaled = bufferdImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			imageScaled.flush();
			return new ImageIcon(imageScaled);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	// NEW file hoặc folder
	public void createDir(String pathString) {
		Path path = Paths.get(pathString);
		System.out.println("path inside: " + path.getRoot());
		try {
			Files.createDirectory(path);
			// có thể lỗi khi gọi hàm này lúc chương trình mới khởi tạo, có thể presentPath sẽ null
			// think about using textfield thay thế.
//			repaintTable(presentPath);
//			repaintTree(presentPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//System.out.println(e.getMessage());
			System.out.println("Folder đã tồn tại từ trước");
			JOptionPane.showMessageDialog(FileExplore.this, "Folder này đã tồn tại");
		}
	}
	
	public void createFile(String pathString) {
		Path newFilePath = Paths.get(pathString);
        try {
			Files.createFile(newFilePath);
			// có thể lỗi khi gọi hàm này lúc chương trình mới khởi tạo, có thể presentPath sẽ null
//			repaintTable(presentPath);
//			repaintTree(presentPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(FileExplore.this, "File này đã tồn tại");
		}
	}
	
	// đổi tên file hoặc thư mục
	public void rename(String pathString) {
//		Path fileToMovePath = Paths.get(textFieldSubJPanelRenameNewName.getText().trim());
//	    Path targetPath = Paths.get(pathString);
//	    
//	    try {
//			Files.move(fileToMovePath, targetPath);
//			repaintTable(presentPath);
//			repaintTree(presentPath);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		System.out.println(textFieldSubJPanelRenameLinkPath.getText().trim());
		System.out.println(pathString);
		File oldName = new File(textFieldSubJPanelRenameLinkPath.getText().trim());
		File newName = new File(pathString);
		
		if(oldName.renameTo(newName)) {
			System.out.println("renamed");
			textFieldSubJPanelRenameLinkPath.setText(newName.getAbsolutePath());
			repaintTable(presentPath);
			repaintTree(presentPath);
	    } else {
	        System.out.println("Error");
	    }
	}
	
	// xóa file hoặc thư mục
	public void deleteFile(String path) {
		Path fileToDeletePath = Paths.get(path);
	    try {
			Files.delete(fileToDeletePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteDir(String path) {
		File fileDir = new File(path);
		try {
			if (fileDir.listFiles().length == 0) {
				boolean result = fileDir.delete();
				if (result == true) {
					System.out.println("xóa thư mục rỗng thành công");
				} else {
					System.out.println("xóa thư mục rỗng không thành công");
				}
			} else {
				File[] allFileInDir = fileDir.listFiles();
				for (File inFile : allFileInDir) {
					if (inFile.isFile()) {
					    deleteFile(inFile.getAbsolutePath());
					} else {
						deleteDir(inFile.getAbsolutePath());
					}
					
				}
				Path dirToDeletePath = Paths.get(fileDir.getAbsolutePath());
			    try {
					Files.delete(dirToDeletePath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("phương án delete thư mục dự phòng");
			//FileUtils.deleteDirectory(new File(destination));
			contingencyPlanForDelete(path);
		}
	}
	// dự phòng
	public void contingencyPlanForDelete(String path) {
		Path directory = Paths.get(path);
		try {
			Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
					Files.delete(file); // this will work because it's always a File
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					Files.delete(dir); //this will work because Files in the directory are already deleted
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void copyFile(String originLinkPath, String newLinkPath) {
		InputStream inStream = null;
        OutputStream outStream = null;
         
        try {
            inStream = new FileInputStream(new File(originLinkPath));
            outStream = new FileOutputStream(new File(newLinkPath));
 
            int length;
            byte[] buffer = new byte[1024];
 
            // copy the file content in bytes
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
            System.out.println("File is copied successful!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
				inStream.close();
				outStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
//		Path fileToCopyPath  = Paths.get(path);
//		Files.copy(fileToCopyPath.get, arg1);
        
//        repaintTable(presentPath);
//        repaintTree(presentPath);
	}
	
	public static void copyDir(String sourceDirectoryLocation, String destinationDirectoryLocation) 
			  throws IOException {
		Files.walk(Paths.get(sourceDirectoryLocation))
		.forEach(source -> {
			Path destination = Paths.get(destinationDirectoryLocation, source.toString()
							   .substring(sourceDirectoryLocation.length()));
			try {
				Files.copy(source, destination);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	
	// hiển thị đuôi mở rộng.
	public String getExtension(String path) {
		String extension = "";

		int i = new File(path).getName().lastIndexOf('.');
		if (i > 0) {
		    extension = new File(path).getName().substring(i+1);
		    return extension;
		}
		return "-1";
	}
	
	// xóa dấu gạch dư.
	public String deleteLastSymbol(String string, String symbol) {
		StringBuilder stringBuilder = new StringBuilder(string);
		String s = String.valueOf(stringBuilder.charAt(stringBuilder.length() - 1));
		if (s.equals(File.separator)) {
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);
			return stringBuilder.toString();
		} else {
			return "-1";
		}
	}
	
	public void compressFileToRAR(String path) {
		try {
			String arg1 = new String(path);
			String arg2 = new File(arg1).getParent() + File.separator;
			System.out.println("arg1 " + arg1);
			System.out.println("arg2 " + arg2);
			//String arg2 = "C:\\Users\\Acer\\Desktop";
			ProcessBuilder pb = new ProcessBuilder("python","Resource" + File.separator + "compressRAR.py",""+arg1,""+arg2);
			
			//ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c","Resource" + File.separator + "extractRAR.exe",""+arg1,""+arg2);
			
			Process p = pb.start();
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void extractRAR(String path) {
		try {
			String arg1 = new String(path);
			String arg2 = new File(arg1).getParent();
			//String arg2 = "C:\\Users\\Acer\\Desktop";
			ProcessBuilder pb = new ProcessBuilder("python","Resource" + File.separator + "extractRAR.py",""+arg1,""+arg2);
			
			//ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c","Resource" + File.separator + "extractRAR.exe",""+arg1,""+arg2);
			
			Process p = pb.start();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
	
	//////////////////////////////////////////// compress Zip
	public void compressFileToZip(String filePath) throws IOException{
		try {
            File file = new File(filePath);
            String zipFileName = file.getParent() + File.separator +file.getName().concat(".zip");
            
            FileOutputStream fos = new FileOutputStream(zipFileName);
            ZipOutputStream zos = new ZipOutputStream(fos);
 
            zos.putNextEntry(new ZipEntry(file.getName()));
 
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            zos.write(bytes, 0, bytes.length);
            zos.closeEntry();
            zos.close();
 
        } catch (FileNotFoundException ex) {
            System.err.format("The file %s does not exist", filePath);
        } catch (IOException ex) {
            System.err.println("I/O error: " + ex);
        }
	}
	
	public void compressDirToZip(String filePath) throws IOException {
		//String sourceFile = "zipTest";
		String sourceFile = new String(filePath);
		File fileToZip = new File(sourceFile);
        //FileOutputStream fos = new FileOutputStream("dirCompressed.zip");
		FileOutputStream fos = new FileOutputStream(fileToZip.getParent() + File.separator + fileToZip.getName().concat(".zip"));
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        
        //File fileToZip = new File(sourceFile);

        zipFile(fileToZip, fileToZip.getName(), zipOut);
        zipOut.close();
        fos.close();
	}
	
	private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith(File.separator)) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + File.separator));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + File.separator + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }
	
	private static void unzip(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                System.out.println("Unzipping to "+newFile.getAbsolutePath());
                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
	////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////// extract zip
	// protect zip slip attack
    public static Path zipSlipProtect(ZipEntry zipEntry, Path targetDir)
        throws IOException {

        // test zip slip vulnerability
        // Path targetDirResolved = targetDir.resolve("../../" + zipEntry.getName());

        Path targetDirResolved = targetDir.resolve(zipEntry.getName());

        // make sure normalized file still has targetDir as its prefix
        // else throws exception
        Path normalizePath = targetDirResolved.normalize();
        if (!normalizePath.startsWith(targetDir)) {
            throw new IOException("Bad zip entry: " + zipEntry.getName());
        }

        return normalizePath;
    }
	
	public static void unzipFolder(Path source, Path target) throws IOException {

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(source.toFile()))) {

            // list files in zip
            ZipEntry zipEntry = zis.getNextEntry();

            while (zipEntry != null) {

                boolean isDirectory = false;
                // example 1.1
                // some zip stored files and folders separately
                // e.g data/
                //     data/folder/
                //     data/folder/file.txt
                if (zipEntry.getName().endsWith(File.separator)) {
                    isDirectory = true;
                }

                Path newPath = zipSlipProtect(zipEntry, target);

                if (isDirectory) {
                    Files.createDirectories(newPath);
                } else {

                    // example 1.2
                    // some zip stored file path only, need create parent directories
                    // e.g data/folder/file.txt
                    if (newPath.getParent() != null) {
                        if (Files.notExists(newPath.getParent())) {
                            Files.createDirectories(newPath.getParent());
                        }
                    }

                    // copy files, nio
                    Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);

                    // copy files, classic
                    /*try (FileOutputStream fos = new FileOutputStream(newPath.toFile())) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }*/
                }

                zipEntry = zis.getNextEntry();

            }
            zis.closeEntry();

        }

    }
	
	public void extract7Zip(String path) {
		try {
			String arg1 = new String(path);
			String arg2 = new File(arg1).getParent();
			//String arg2 = "C:\\Users\\Acer\\Desktop";
			ProcessBuilder pb = new ProcessBuilder("python","Resource" + File.separator + "extract7Zip.py",""+arg1,""+arg2);
			
			//ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c","Resource" + File.separator + "extractRAR.exe",""+arg1,""+arg2);
			
			Process p = pb.start();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//------------------------------------------------
	class CustomFileTableModel extends AbstractTableModel {
		
		private ArrayList<FileViewModel> fileViewModelList;
		//public File[] file;
		//public FileSystemView fileSystemView = FileSystemView.getFileSystemView();
		//String[] column = {"", "Name", "Data Modified", "Type", "Size"};
		
		public CustomFileTableModel(ArrayList<FileViewModel> fileViewModelList) {
			super();
			this.fileViewModelList = fileViewModelList;
		}
		
		

		public ArrayList<FileViewModel> getFileViewModelList() {
			return fileViewModelList;
		}



		public void setFileViewModelList(ArrayList<FileViewModel> fileViewModelList) {
			this.fileViewModelList = fileViewModelList;
		}
		
		@Override
		public int getColumnCount() {
			// TODO Auto-generated method stub
			return header.length;
		}

		@Override
		public int getRowCount() {
			// TODO Auto-generated method stub
			return fileViewModelList.size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			// TODO Auto-generated method stub
			FileViewModel fileViewModel = fileViewModelList.get(rowIndex);
	        switch(columnIndex){
	            case 0: return fileViewModel.getUrl();
	            case 1: return fileViewModel.getFileName();
	            case 2: return fileViewModel.getPath();
	            case 3: return fileViewModel.getDataModified();
	            case 4: 
	            	if (!fileViewModel.getIsFile()) {
	            		return "Folder directory";
	            	} else {
	            		String extension = "";

	            		int i = fileViewModel.getFile().getName().lastIndexOf('.');
	            		if (i > 0) {
	            		    extension = fileViewModel.getFile().getName().substring(i+1);
	            		}
	            		return "File " + extension;
	            	}
	            	
	            case 5: return fileViewModel.getSize();
	            default: return null;
	        }
		}
		
		String[] header = {"Icon", "Name", "Path", "Data Modified", "Type", "Size"};
		@Override
		public String getColumnName(int columnNumber) {
			// TODO Auto-generated method stub
			switch(columnNumber){
            	case 0: return "Icon";
            	case 1: return "Name";
            	case 2: return "Path";
            	case 3: return "Data Modified";
            	case 4: return "Type";
            	case 5: return "Size";
            	default: return "ahihi";
			}
			//return super.getColumnName(arg0);
		}
		
	}
///////////////////////////////////////////////////////////////////////////////
	
	class CustomTableCellRenderer implements TableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable jtable, Object value, boolean isSelected, boolean hasFocus, int rowNumber,
				int columnNumber) {
			// TODO Auto-generated method stub
			JLabel jlabelCell = new JLabel();
	        // lệnh render hình ảnh icon tại cột số 0
	        if(columnNumber == 0){
	            //URL flagUrl = CountryCellRenderer.class.getResource("flags/" + value.toString() + ".png");
	            //URL flagUrl = CustomTableCellRenderer.class.getResource("flags/" + object.toString() + ".png");
	            
	        	//FileViewModel fileViewModel = (FileViewModel) value;
	        	String url = (String) value;
	            jlabelCell.setIcon(createFormatImageIcon(url, 20, 20));
	        }else{            
	        	jlabelCell.setText(value.toString());            
	        }
	        return jlabelCell;
		}
		
	}
///////////////////////////////////////////////////////////////////////////////
	class MyTreeCellRenderer extends DefaultTreeCellRenderer {

		  @Override
		public Component getTreeCellRendererComponent(JTree arg0, Object arg1, boolean arg2, boolean arg3, boolean arg4,
				int arg5, boolean arg6) {
			// TODO Auto-generated method stub
			
			return super.getTreeCellRendererComponent(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
		}
	}
}
