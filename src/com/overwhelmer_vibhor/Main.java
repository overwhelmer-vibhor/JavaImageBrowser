package com.overwhelmer_vibhor;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


public class Main extends JFrame implements ActionListener {

    public static void main(String[] args) {
        Main obj = new Main();
        obj.drawFrame();
    }

    JButton[] buttons = new JButton[5];
    JLabel imageArea;
    JLabel imageCount;
    JTextField[] textFields = new JTextField[2];
    String directoryName = "";
    String filename = "";
    File fileDirectory;
    int imageIndex = 0;
    String[] imageList;
    FileDialog fileDialog;
    boolean startButtonPressed;

    private void drawFrame() {
        super.setBounds(0, 0, 750, 600);
        super.setResizable(false);
        super.setTitle("Image Browser by Vibhor");

        imageArea = new JLabel();
        imageArea.setBounds(50, 50, 400, 400);
        imageArea.setIcon(getResizedImageIcon(new ImageIcon(directoryName + filename), 400, 400));
        imageArea.setHorizontalAlignment(SwingConstants.CENTER);
        Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
        imageArea.setBorder(border);
        super.add(imageArea);

        buttons[0] = new JButton();//Left Arrow Button
        buttons[0].setBounds(50, 470, 30, 30);
        buttons[0].setText("<");
        //buttons[0].setIcon(getResizedImageIcon(new ImageIcon("C:\\Users\\Vibhor\\Desktop\\ImageBrowserSources\\Left.png"),30,30));
        buttons[0].addActionListener(this);
        super.add(buttons[0]);

        buttons[1] = new JButton();//Home Button
        buttons[1].setBounds(160, 470, 180, 30);
        buttons[1].setText("Home");
        buttons[1].addActionListener(this);
        super.add(buttons[1]);

        buttons[2] = new JButton();//Right Arrow Button
        buttons[2].setBounds(420, 470, 30, 30);
        buttons[2].setText(">");
        //buttons[2].setIcon(getResizedImageIcon(new ImageIcon("C:\\Users\\Vibhor\\Desktop\\ImageBrowserSources\\Right.png"),30,30));
        buttons[2].addActionListener(this);
        super.add(buttons[2]);

        buttons[3] = new JButton();//Browse Button
        buttons[3].setBounds(470, 50, 250, 30);
        buttons[3].setText("Browse");
        buttons[3].addActionListener(this);
        super.add(buttons[3]);


        textFields[0] = new JTextField();//For File Name
        textFields[0].setBounds(470, 100, 250, 30);
        textFields[0].setEditable(false);
        textFields[0].setHorizontalAlignment(SwingConstants.CENTER);
        super.add(textFields[0]);

        textFields[1] = new JTextField();//For File Size
        textFields[1].setBounds(470, 150, 250, 30);
        textFields[1].setEditable(false);
        textFields[1].setHorizontalAlignment(SwingConstants.CENTER);
        super.add(textFields[1]);

        buttons[4] = new JButton();//Start Button
        buttons[4].setBounds(470, 200, 250, 30);
        buttons[4].setText("Start");
        buttons[4].addActionListener(this);
        super.add(buttons[4]);

        imageCount = new JLabel();//Image Count Label
        imageCount.setBounds(470, 250, 250, 30);
        imageCount.setHorizontalAlignment(SwingConstants.CENTER);
        imageCount.setFont(new Font("", Font.BOLD, 15));
        super.add(imageCount);

        super.setLocationRelativeTo(null);
        super.setLayout(null);
        super.setVisible(true);
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public ImageIcon getResizedImageIcon(ImageIcon imageIcon, int x, int y) {
        x = imageIcon.getIconWidth() < imageIcon.getIconHeight() ? -1 : x;
        y = imageIcon.getIconWidth() > imageIcon.getIconHeight() ? -1 : y;
        return new ImageIcon(imageIcon.getImage().getScaledInstance(x, y, Image.SCALE_SMOOTH));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttons[3])//Browse
        {
            clearImage();
            fileDialog = new FileDialog(this, "Open Image", FileDialog.LOAD);
            fileDialog.setFile("*.jpg;*.jpeg;*.png");
            fileDialog.setVisible(true);

            setDirectoryName(fileDialog.getDirectory());
            setFilename(fileDialog.getFile());

            setFileDirectory(fileDialog.getDirectory());

            setUpImageList();

            startButtonPressed = false;
        }
        //----------------------------------------------------------------------------------------------------------------------------------------
        if (e.getSource() == buttons[4])//Start
        {
            if (getDirectoryName().equals(""))
                imageArea.setText("No Image Selected");
            else {
                for (int i = 0; i < getImageList().length; i++) {
                    if (getImageList()[i].equals(getFilename())) {
                        setImageIndex(i);
                        break;
                    }
                }
                loadImage(imageIndex);
                startButtonPressed = true;
            }
        }
        //----------------------------------------------------------------------------------------------------------------------------------------
        if (e.getSource() == buttons[2])//Right
        {
            if (getDirectoryName().equals(""))
                imageArea.setText("No Image Selected");
            else if (startButtonPressed) {
                imageIndex = (imageIndex + 1) % getImageList().length;
                loadImage(imageIndex);
                if (imageIndex + 1 == getImageList().length)
                    JOptionPane.showMessageDialog(this, "Reached Last Image", "Warning", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        //----------------------------------------------------------------------------------------------------------------------------------------
        if (e.getSource() == buttons[0])//Left
        {
            if (getDirectoryName().equals(""))
                imageArea.setText("No Image Selected");
            else if (startButtonPressed) {
                if (imageIndex == 0)
                    imageIndex = getImageList().length - 1;
                else
                    imageIndex = (imageIndex - 1) % getImageList().length;

                loadImage(imageIndex);
                if (imageIndex == 0)
                    JOptionPane.showMessageDialog(this, "Reached First Image", "Warning", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        //----------------------------------------------------------------------------------------------------------------------------------------
        if (e.getSource() == buttons[1])//Home
        {
            if (getDirectoryName().equals(""))
                imageArea.setText("No Image Selected");
            else if (startButtonPressed) {
                loadImage(0);
                setImageIndex(0);
            }
        }
    }

    public void loadImage(int index) //sets image,image name, image size and  image count
    {
        this.imageArea.setText("");
        this.imageCount.setText("" + (index + 1) + "/" + getImageList().length);
        this.imageArea.setIcon(getResizedImageIcon(new ImageIcon(getDirectoryName() + "\\" + getImageList()[index]), 400, 400));
        this.textFields[0].setText(getImageList()[index]);
        this.textFields[1].setText((new File(getDirectoryName() + "\\" + getImageList()[index]).length() / 1024) + " KB");
    }

    public void clearImage()//for clearing old images
    {
        setDirectoryName("");
        this.imageArea.setText("");
        this.imageCount.setText("");
        this.imageArea.setIcon(getResizedImageIcon(new ImageIcon(""), 400, 400));
        this.textFields[0].setText("");
        this.textFields[1].setText("");
    }


    //Setters and Getters////////////////////////////////////////////////////////////
    public void setUpImageList() {
        this.imageList = getFileDirectory().list((dir, name) -> name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".PNG") || name.endsWith(".JPG") || name.endsWith(".JPEG"));

    }

    public File getFileDirectory() {
        return fileDirectory;
    }

    public void setFileDirectory(String fileDirectory) {
        this.fileDirectory = new File(fileDirectory);
    }

    public String[] getImageList() {
        return imageList;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }
}
