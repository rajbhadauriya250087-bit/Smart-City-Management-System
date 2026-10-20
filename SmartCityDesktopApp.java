package edu.project.smartcity.ui;

import edu.project.smartcity.exception.CityDataException;
import edu.project.smartcity.model.CityIssue;
import edu.project.smartcity.repository.JdbcCityIssueRepository;
import edu.project.smartcity.service.CityIssueService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SmartCityDesktopApp extends JFrame {
    private final CityIssueService service = new CityIssueService(new JdbcCityIssueRepository());
    private final JTextField title=new JTextField(12), location=new JTextField(12), reporter=new JTextField(10), search=new JTextField(12);
    private final JComboBox<String> category=new JComboBox<>(new String[]{"Pothole","Garbage","Streetlight","Water Supply","Drainage","Traffic","Other"});
    private final JTextArea description=new JTextArea(2,20);
    private final DefaultTableModel model=new DefaultTableModel(new String[]{"ID","Title","Category","Location","Reporter","Status"},0) {
        @Override public boolean isCellEditable(int r,int c){return false;}
    };
    private final JTable table=new JTable(model);
    public SmartCityDesktopApp() {
        super("Smart City Management — Civic Issue Tracker");
        setDefaultCloseOperation(EXIT_ON_CLOSE); setSize(1100,650); setLocationRelativeTo(null);
        JPanel form=new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.add(new JLabel("Issue"));form.add(title);form.add(new JLabel("Category"));form.add(category);
        form.add(new JLabel("Location"));form.add(location);form.add(new JLabel("Reported by"));form.add(reporter);
        form.add(new JLabel("Description"));form.add(new JScrollPane(description));
        JButton add=new JButton("Report Issue");add.addActionListener(e->report());
        JButton refresh=new JButton("Refresh");refresh.addActionListener(e->load(""));
        JButton find=new JButton("Search");find.addActionListener(e->load(search.getText()));
        JButton status=new JButton("Update Status");status.addActionListener(e->updateStatus());
        JButton delete=new JButton("Delete Selected");delete.addActionListener(e->deleteSelected());
        form.add(add);form.add(new JLabel("Search"));form.add(search);form.add(find);form.add(refresh);form.add(status);form.add(delete);
        add(form,BorderLayout.NORTH);add(new JScrollPane(table),BorderLayout.CENTER);load("");
    }
    private void report() {
        try {
            service.report(title.getText(),(String)category.getSelectedItem(),location.getText(),description.getText(),reporter.getText());
            title.setText("");location.setText("");reporter.setText("");description.setText("");
            load("");JOptionPane.showMessageDialog(this,"Civic issue reported successfully.");
        } catch (IllegalArgumentException|CityDataException ex) { error(ex.getMessage()); }
    }
    private long selectedId() {
        int row=table.getSelectedRow(); if(row<0) throw new IllegalArgumentException("Select an issue first.");
        return ((Number)model.getValueAt(row,0)).longValue();
    }
    private void updateStatus() {
        try {
            long id=selectedId();
            String[] options={"REPORTED","IN_PROGRESS","RESOLVED"};
            String status=(String)JOptionPane.showInputDialog(this,"Choose status:","Update issue",
              JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
            if(status!=null){service.updateStatus(id,status);load(search.getText());}
        } catch(RuntimeException ex){error(ex.getMessage());}
    }
    private void deleteSelected() {
        try {
            long id=selectedId();
            if(JOptionPane.showConfirmDialog(this,"Delete issue #"+id+"?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                service.delete(id);load(search.getText());
            }
        } catch(RuntimeException ex){error(ex.getMessage());}
    }
    private void load(String q) {
        new SwingWorker<List<CityIssue>,Void>() {
            protected List<CityIssue> doInBackground(){return service.list(q);}
            protected void done(){
                try {model.setRowCount(0);for(CityIssue i:get())model.addRow(new Object[]{i.getId(),i.getTitle(),i.getCategory(),i.getLocation(),i.getReportedBy(),i.getStatus()});}
                catch(InterruptedException e){Thread.currentThread().interrupt();error("Refresh interrupted.");}
                catch(ExecutionException e){error(e.getCause().getMessage());}
            }
        }.execute();
    }
    private void error(String msg){JOptionPane.showMessageDialog(this,msg,"Smart City",JOptionPane.ERROR_MESSAGE);}
    public static void main(String[] args){SwingUtilities.invokeLater(()->new SmartCityDesktopApp().setVisible(true));}
}