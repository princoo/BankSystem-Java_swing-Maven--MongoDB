package com.example.GUI;

import javax.swing.DefaultListModel;

import com.example.DatabaseUtil;
import com.example.Data.FileIO;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.bson.Document;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JList;
import java.awt.SystemColor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DisplayList extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    JScrollPane scrollPane = new JScrollPane();

    /**
     * Create the frame.
     */
    @SuppressWarnings({})
    public DisplayList() {

        setTitle("Account List");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 649, 474);
        contentPane = new JPanel();
        contentPane.setBackground(SystemColor.activeCaption);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblAccountList = new JLabel("Account List");
        lblAccountList.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblAccountList.setHorizontalAlignment(SwingConstants.CENTER);
        lblAccountList.setBounds(0, 11, 623, 31);
        contentPane.add(lblAccountList);

        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(10, 66, 613, 358);
        contentPane.add(scrollPane);
        updateList();
        // try {
        //     DatabaseUtil.openConnection();
        //     List<String> accountDataList = fetchDataFromMongoDB("SavingAccounts");
        //     // Convert the list to an array
        //     String[] accountDataArray = accountDataList.toArray(new String[0]);
        //     // Create a JList and set its data
        //     JList<String> list = new JList<String>(accountDataArray);
        //     // Create a JScrollPane and set its view to the JList
        //     System.out.println("ok");
        //     scrollPane.setViewportView(list);
        // } catch (SQLException e1) {
        //     e1.printStackTrace();
        // }
        // finally{
        //     DatabaseUtil.closeConnection();
        // }
        
    }
        public void updateList(){
            try {
                DatabaseUtil.openConnection();
                List<String> accountDataList = fetchDataFromMongoDB("SavingAccounts");
                // Convert the list to an array
                String[] accountDataArray = accountDataList.toArray(new String[0]);
                // Create a JList and set its data
                JList<String> list = new JList<String>(accountDataArray);
                // Create a JScrollPane and set its view to the JList
                scrollPane.setViewportView(list);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            finally{
                DatabaseUtil.closeConnection();
            }
        }
        public static List<String> fetchDataFromMongoDB(String collectionName) {
        List<String> accountDataList = new ArrayList<>();

        try {
            MongoDatabase database = DatabaseUtil.database;
            MongoCollection<Document> collection = database.getCollection(collectionName);

            // Finding all documents in the collection
            FindIterable<Document> documents = collection.find();

            for (Document document : documents) {
                // Format the document fields as needed and add to the list
                String formattedData = String.format(
                        "Id: %s,Name: %s, Balance: %.2f, Account Type: %s",
                        document.getObjectId("_id").toString(),
                        document.getString("name"),
                        document.getDouble("balance"),
                        document.getString("type")
                );
                accountDataList.add(formattedData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return accountDataList;
    }
}
