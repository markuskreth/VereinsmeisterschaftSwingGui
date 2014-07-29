package de.kreth.vereinsmeisterschaft.gui.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.*;

import de.kreth.vereinsmeisterschaftprog.business.CompetitionBusiness;
import de.kreth.vereinsmeisterschaftprog.data.*;
import de.kreth.vereinsmeisterschaftprog.views.CompetitionView;

public class WettkampfPanel extends JPanel implements CompetitionView {

   private static final long serialVersionUID = -257839817852907002L;

   private CompetitionBusiness business;
   private JTable table;
   private MyTableModel tableModel;
   final JComboBox<Durchgang> comboBox_Durchgang = new JComboBox<>();

   private WertenDialog wertenDialog;

   /**
    * Create the panel.
    * 
    * @param wettkampfBusiness
    */
   public WettkampfPanel(final CompetitionBusiness wettkampfBusiness) {

      tableModel = new MyTableModel();
      table = new JTable();
      table.setModel(tableModel);
      
      this.business = wettkampfBusiness;
      this.wertenDialog = new WertenDialog(wettkampfBusiness);
      setLayout(new BorderLayout(0, 0));
      
      JPanel panel = new JPanel();
      add(panel, BorderLayout.NORTH);
      
      JButton btnNeuerStarter = new JButton("Neuer Starter");
      btnNeuerStarter.addActionListener(new ActionListener() {
         
         @Override
         public void actionPerformed(ActionEvent e) {
            String starterName = JOptionPane.showInputDialog(WettkampfPanel.this, "Name des Starters", "Neuer Starter", JOptionPane.QUESTION_MESSAGE);
            business.newStarter(starterName);
         }
      });
      panel.add(btnNeuerStarter);
      
      JLabel lblDurchgang = new JLabel("Durchgang");
      panel.add(lblDurchgang);
      
      comboBox_Durchgang.setModel(new DefaultComboBoxModel<Durchgang>(Durchgang.values()));
      panel.add(comboBox_Durchgang);

      JLabel lblSortierung = new JLabel("Sortierung");
      panel.add(lblSortierung);
      
      final JComboBox<Sortierung> comboBox = new JComboBox<>();
      comboBox.setModel(new DefaultComboBoxModel<Sortierung>(Sortierung.values()));
      comboBox.addActionListener(new ActionListener() {
         
         @Override
         public void actionPerformed(ActionEvent e) {
            Sortierung sort = (Sortierung) comboBox.getSelectedItem();
            tableModel.setSortierung(sort );
         }
      });
      
      panel.add(comboBox);
      
      TableColumnModel columnModel = table.getColumnModel();
      TableColumn btnColumn = columnModel.getColumn(5);
      int width = 50;
      for(int i=0; i<columnModel.getColumnCount(); i++) {
         if(i==0)
            columnModel.getColumn(i).setPreferredWidth(width*3);
         else if(i==5)
            columnModel.getColumn(i).setPreferredWidth((int) (width*1.5));
         else
            columnModel.getColumn(i).setPreferredWidth(width);
      }
      btnColumn.setCellRenderer(new ButtonRenderer());
      btnColumn.setCellEditor(new ButtonEditor());
      JScrollPane scroller = new JScrollPane(table);
      add(scroller, BorderLayout.CENTER);
      
      this.business.setView(this);   
   }

   private class ButtonRenderer implements TableCellRenderer {

      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
         JButton btn = (JButton) value;
         if (isSelected) {
            btn.setForeground(table.getSelectionForeground());
            btn.setBackground(table.getSelectionBackground());
         } else {
            btn.setForeground(table.getForeground());
            btn.setBackground(UIManager.getColor("Button.background"));
         }
         return btn;
      }

   }

   private class ButtonEditor extends AbstractCellEditor implements ActionListener, TableCellEditor {
      
      private static final long serialVersionUID = -3269232034263059218L;
      private JButton button;

      public ButtonEditor() {

         button = new JButton();
         button.addActionListener(this);
         button.setBorderPainted(false);
      }
      
      @Override
      public Object getCellEditorValue() {
         return null;
      }

      @Override
      public void actionPerformed(ActionEvent ev) {
         fireEditingStopped();
      }

      @Override
      public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
         return (JButton) value;
      }
      
   }
   
   private class MyTableModel extends AbstractTableModel {

      private static final long serialVersionUID = 2910124315519583475L;
      private String[] columnNames = { "Starter", "Pflicht", "Kür", "Gesamt", "Platz", "" };
      private List<Ergebnis> data = new ArrayList<>();
      private List<JButton> editButtons = new ArrayList<>();

      DecimalFormat df = new DecimalFormat("0.0##");

      public void setSortierung(Sortierung sort) {
         business.setSortierung(sort);
         fireTableDataChanged();
      }

      public void addElement(final Ergebnis e) {
         data.add(e);
         JButton b = new JButton("Werten");
         b.addActionListener(new ActionListener() {
            int index = data.size()-1;
            @Override
            public void actionPerformed(ActionEvent ev) {
               business.werteErgebnis(data.get(index), (Durchgang) comboBox_Durchgang.getSelectedItem());
            }
         });
         editButtons.add(b);
         fireTableDataChanged();
      }

      public void removeAllElements() {
         data.clear();
         editButtons.clear();
         fireTableDataChanged();
      }

      @Override
      public String getColumnName(int column) {
         return columnNames[column];
      }

      @Override
      public int getRowCount() {
         return data.size();
      }

      @Override
      public boolean isCellEditable(int rowIndex, int columnIndex) {
         if(columnIndex==5)
            return true;
         return false;
      }

      @Override
      public int getColumnCount() {
         return columnNames.length;
      }

      @Override
      public Class<?> getColumnClass(int columnIndex) {
         if (columnIndex == 5)
            return JComponent.class;

         return String.class;
      }

      @Override
      public Object getValueAt(int rowIndex, int columnIndex) {
         Ergebnis ergebnis = data.get(rowIndex);
         Object value;
         switch (columnIndex) {
            case 0:
               value = ergebnis.getStarterName();
               break;

            case 1:
               value = df.format(ergebnis.getPflicht().getErgebnis());
               break;

            case 2:
               value = df.format(ergebnis.getKuer().getErgebnis());
               break;

            case 3:
               value = df.format(ergebnis.getErgebnis());
               break;
            case 4:
               value = ergebnis.getPlatz() + ".";
               break;
            case 5:
               value = editButtons.get(rowIndex);
               break;
            default:
               value = "";
               break;
         }

         return value;
      }
   }

   @Override
   public void showWertung(String starterName, Wertung wertung) {
      wertenDialog.setWertung(starterName, wertung);
      wertenDialog.setVisible(true);
   }

   @Override
   public void setCompetition(Competition wettkampf) {

      tableModel.removeAllElements();
      
      if(wettkampf != null) {
         
         for (Ergebnis e : wettkampf.getErgebnisse()) {
            tableModel.addElement(e);
         }
         
      }
   }
}
