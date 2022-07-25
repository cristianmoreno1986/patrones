/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oscarblancarte.ipd.templetemethod.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import oscarblancarte.ipd.templetemethod.util.OnMemoryDataBase;

/**
 *
 * @author crist
 */
public class BankFileProcess extends AbstractFileProcessTemplete {
    private String log = "";
    
    public BankFileProcess(File file, String logPath, String movePath){
        super(file, logPath, movePath);
    }
    
    @Override
    protected void validateName() throws Exception {
        String fileName = file.getName();
        if (!fileName.endsWith(".xml")) {
            throw new Exception("Invalid file name"
                    + ", must end with .xml");
        }

        if (fileName.length() != 10) {
            throw new Exception("Invalid document format");
        }
    }

    @Override
    protected void processFile() throws Exception {
        
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            document.getDocumentElement().normalize();
            //System.out.println("Root Element :" + document.getDocumentElement().getNodeName());
            NodeList nodeList = document.getElementsByTagName("payment");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                //System.out.println("\nCurrent Element :" + node.getNodeName());
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String id = element.getAttribute("id");
                    String customer = element.getElementsByTagName("customer").item(0).getTextContent();
                    double amount = Double.parseDouble(element.getElementsByTagName("amount").item(0).getTextContent());
                    String date = element.getElementsByTagName("date").item(0).getTextContent();
                    boolean exist = OnMemoryDataBase.customerExist(
                            Integer.parseInt(customer));
                    if (!exist) {
                    log += id + " E" + customer + "\t\t" + date
                            + " Customer not exist\n";
                    } else if (amount > 200) {
                        log += id + " E" + customer + "\t\t" + date
                                + " The amount exceeds the maximum\n";
                    } else {

                        log += id + " E" + customer + "\t\t" + date
                                + " Successfully applied\n";
                    }
                }
            }
        } catch(IOException e) {
            System.out.println(e);
        } 
    }

    @Override
    protected void createLog() throws Exception {
        FileOutputStream out = null;
        try {
            File outFile = new File(logPath + "/" + file.getName());
            if (!outFile.exists()) {
                outFile.createNewFile();
            }
            out = new FileOutputStream(outFile, false);
            out.write(log.getBytes());
            out.flush();
        } finally {
            out.close();
        }
    }
}
