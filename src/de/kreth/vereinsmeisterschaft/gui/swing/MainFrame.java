package de.kreth.vereinsmeisterschaft.gui.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.kreth.vereinsmeisterschaftprog.business.MainFrameBusiness;
import de.kreth.vereinsmeisterschaftprog.data.Gruppe;
import de.kreth.vereinsmeisterschaftprog.views.MainView;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame implements MainView {

   private static final long serialVersionUID = 5118057573440157488L;
   private JPanel contentPane;
   private MainFrameBusiness business;
   private DefaultListModel<Gruppe> model;

   /**
    * Create the frame.
    */
   public MainFrame() {
      setTitle("Vereinsmeisterschaften 2014");

      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(100, 100, 736, 300);
      contentPane = new JPanel();
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      contentPane.setLayout(new BorderLayout(0, 0));
      setContentPane(contentPane);

      model = new DefaultListModel<Gruppe>() {

         private static final long serialVersionUID = -3809219187518599443L;

         /**
          * Löscht alle Einträge und Initialisiert mit "Hinzufügen"-Eintrag.
          */
         @Override
         public void clear() {
            super.clear();
            addElement(new Gruppe(-1, "Hinzufügen", "Kein Eintrag! Neu erstellen, wenn gewählt!"));
         }
      };

      JPanel panel = new JPanel();
      contentPane.add(panel, BorderLayout.WEST);
      panel.setLayout(new BorderLayout(0, 0));

      final JList<Gruppe> pflichtenView = new JList<Gruppe>();
      panel.add(pflichtenView, BorderLayout.CENTER);
      
      pflichtenView.setFont(new Font("Dialog", Font.BOLD, 14));
      pflichtenView.setPreferredSize(new Dimension(100, 0));
      pflichtenView.setMinimumSize(new Dimension(150, 150));
      pflichtenView.setSelectedIndex(1);
      pflichtenView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

      pflichtenView.setModel(model);

      pflichtenView.addListSelectionListener(new ListSelectionListener() {

         @Override
         public void valueChanged(ListSelectionEvent e) {

            if (!e.getValueIsAdjusting()) {

               Gruppe selection = pflichtenView.getSelectedValue();
               if(selection != null)
                  business.pflichtChange(selection);
            }

         }
      });
      
      if(pflichtenView.getModel().getSize()>1)
         pflichtenView.setSelectedIndex(1);
      else
         pflichtenView.setSelectedIndex(0);

      JButton btnExport = new JButton("Exportieren");
      btnExport.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
            business.doExport();
         }
      });
      
      panel.add(btnExport, BorderLayout.SOUTH);

      business = new MainFrameBusiness(this);
      WettkampfPanel wkPanel = new WettkampfPanel(business.getWettkampfBusiness());
      contentPane.add(wkPanel, BorderLayout.CENTER);

      refreshGroups();

   }

   @Override
   public void showNewGruppeDialog() {
      String groupName = JOptionPane.showInputDialog(contentPane, "Name der Gruppe", "Neue Gruppe anlegen", JOptionPane.QUESTION_MESSAGE);
      
      if(groupName != null && groupName.length()>0) {
         String groupDescription = JOptionPane.showInputDialog(contentPane, "Beschreibung (Optional)", "Gruppe " + groupName, JOptionPane.QUESTION_MESSAGE);
         if(groupDescription == null)
            groupDescription = "";
         business.createGroup(groupName, groupDescription);      
      }
      refreshGroups();
   }

   private void refreshGroups() {
      model.clear();
      
      for (Gruppe p : business.getGruppen())
         model.addElement(p);

   }

}