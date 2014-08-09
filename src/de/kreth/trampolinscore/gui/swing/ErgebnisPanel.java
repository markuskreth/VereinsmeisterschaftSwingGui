package de.kreth.trampolinscore.gui.swing;

import java.awt.Color;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;

import de.kreth.trampolinscore.business.CompetitionBusiness;
import de.kreth.trampolinscore.data.RoutineType;
import de.kreth.trampolinscore.data.Result;
import de.kreth.trampolinscore.data.Routine;


public class ErgebnisPanel extends JPanel {

   private static final long serialVersionUID = -126921232179715189L;
   private Result ergebnis;
   private JLabel lblStarterName;
   private JLabel lblPflicht;
   private JLabel lblKuer;
   private JLabel lblErgebnis;
   private JLabel lblPlatz;
   private RoutineType durchgang;
   private WertenDialog dlg;
   private DecimalFormat df;
   
   /**
    * Create the panel.
    * @param durchgang 
    * @param business 
    */
   public ErgebnisPanel(final Result ergebnis, final RoutineType durchgang, CompetitionBusiness business) {
      this.ergebnis = ergebnis;
      FlowLayout flowLayout = (FlowLayout) getLayout();
      flowLayout.setHgap(15);
      this.durchgang = durchgang;

      df = new DecimalFormat("#.0");
      
      lblStarterName = new JLabel("<StarterName>");
      add(lblStarterName);
      
      lblPflicht = new JLabel("<Pflicht>");
      add(lblPflicht);
      
      lblKuer = new JLabel("<Kür>");
      add(lblKuer);
      
      lblErgebnis = new JLabel("<Ergebnis>");
      add(lblErgebnis);
      
      lblPlatz = new JLabel("<Platz>");
      add(lblPlatz);

      dlg = new WertenDialog(business);
      ergebnis.addPropertyChangeListener(dlg);
      
      ergebnis.addPropertyChangeListener(new PCL());
      updateValues();
   }

   public void setColor(Color backgrnd) {
      lblStarterName.setBackground(backgrnd);
   }
   
   private void updateValues() {
      lblStarterName.setText(ergebnis.getStarterName());
      lblPflicht.setText(df.format(ergebnis.getPflicht().getResult()));
      lblKuer.setText(df.format(ergebnis.getKuer().getResult()));
      lblErgebnis.setText(df.format(ergebnis.getResult()));
      lblPlatz.setText(df.format(ergebnis.getPlatz()));
   }
   
   private class PCL implements PropertyChangeListener {

      @Override
      public void propertyChange(PropertyChangeEvent evt) {
         updateValues();
      }
      
   }

   public void performButtonClick() {

      Routine wertung = null;
      switch (durchgang) {
         case VOLUNTARY:
            wertung = ergebnis.getKuer();
            break;
         case COMPULSORY:
            wertung = ergebnis.getPflicht();
            break;
      }
      
      dlg.setWertung(ergebnis.getStarterName(), wertung);
      dlg.setVisible(true);
   }
}
