package jasbro.util.enemyEditor;

import java.awt.Font;

import jasbro.game.world.customContent.npc.ComplexEnemyTemplate;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

@SuppressWarnings("serial")
public class EnemySexPanel extends JPanel {

	private ComplexEnemyTemplate complexEnemyTemplate;
	
	private JTextField rapeFemaleField = new JTextField();
	private JTextField reverseRapeFemaleField = new JTextField();
	private JTextField reverseRapeFemaleCaptureField = new JTextField();
	private JTextField submitFemaleField = new JTextField();
	private JTextField rapeMaleField = new JTextField();
	private JTextField reverseRapeMaleField = new JTextField();
	private JTextField reverseRapeMaleCaptureField = new JTextField();
	private JTextField submitMaleField = new JTextField();
	private JTextField encounterField = new JTextField();
	private JTextField summoningField = new JTextField();
	private JTextField captureField = new JTextField();
	
	public EnemySexPanel() {
		this.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		rapeFemaleField = new JTextField();
		reverseRapeFemaleField = new JTextField();
		reverseRapeFemaleCaptureField = new JTextField();
		submitFemaleField = new JTextField();
		rapeMaleField = new JTextField();
		reverseRapeMaleField = new JTextField();
		reverseRapeMaleCaptureField = new JTextField();
		submitMaleField = new JTextField();
		encounterField = new JTextField();
		summoningField = new JTextField();
		captureField = new JTextField();
		
		JLabel jLabel1 = new JLabel("Those fields are for monster on female scenes.");
		this.add(jLabel1, "1,1");
		jLabel1.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JLabel jLabel2 = new JLabel("Submit:");
		this.add(jLabel2, "1,2");
		
		JLabel jLabel3 = new JLabel("Rape:");
		this.add(jLabel3, "1,3");
		
		JLabel jLabel4 = new JLabel("Reverse-Rape:");
		this.add(jLabel4, "1,4");
		
		JLabel jLabel5 = new JLabel("Reverse-Rape + Capture:");
		this.add(jLabel5, "1,5");
		
		JLabel jLabel6 = new JLabel("Those fields are for monster on male scenes.");
		this.add(jLabel6, "1,6");
		jLabel6.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JLabel jLabel7 = new JLabel("Submit:");
		this.add(jLabel7, "1,7");
		
		JLabel jLabel8 = new JLabel("Rape:");
		this.add(jLabel8, "1,8");
		
		JLabel jLabel9 = new JLabel("Reverse-Rape:");
		this.add(jLabel9, "1,9");
		
		JLabel jLabel10 = new JLabel("Reverse-Rape + Capture:");
		this.add(jLabel10, "1,10");
		
		JLabel jLabel11 = new JLabel("Those fields are for generic/non-sex scenes.");
		this.add(jLabel11, "1,11");
		jLabel11.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JLabel jLabel12 = new JLabel("Encounter:");
		this.add(jLabel12, "1,12");
		
		JLabel jLabel13 = new JLabel("Summoning:");
		this.add(jLabel13, "1,13");
		
		JLabel jLabel14 = new JLabel("Capture:");
		this.add(jLabel14, "1,14");
		
		this.add(submitFemaleField, "2,2");
		submitFemaleField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				complexEnemyTemplate.setFemaleSubmit(submitFemaleField.getText());
			}
			
			public void removeUpdate(DocumentEvent e) {
				complexEnemyTemplate.setFemaleSubmit(submitFemaleField.getText());
			}
			
			public void changedUpdate(DocumentEvent e) {
				complexEnemyTemplate.setFemaleSubmit(submitFemaleField.getText());
			}
		});
		
		this.add(rapeFemaleField, "2,3");
		rapeFemaleField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				complexEnemyTemplate.setFemaleRape(rapeFemaleField.getText());
			}
			
			public void removeUpdate(DocumentEvent e) {
				complexEnemyTemplate.setFemaleRape(rapeFemaleField.getText());
			}
			
			public void changedUpdate(DocumentEvent e) {
				complexEnemyTemplate.setFemaleRape(rapeFemaleField.getText());
			}
		});
		
		this.add(reverseRapeFemaleField, "2,4");
		reverseRapeFemaleField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				complexEnemyTemplate.setReverseFemaleRape(reverseRapeFemaleField.getText());
			}
			
			public void removeUpdate(DocumentEvent e) {
				complexEnemyTemplate.setReverseFemaleRape(reverseRapeFemaleField.getText());
			}
			
			public void changedUpdate(DocumentEvent e) {
				complexEnemyTemplate.setReverseFemaleRape(reverseRapeFemaleField.getText());
			}
		});		
		
		this.add(reverseRapeFemaleCaptureField, "2,5");
		reverseRapeFemaleCaptureField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				complexEnemyTemplate.setReverseFemaleRapeCapture(reverseRapeFemaleCaptureField.getText());
			}
			
			public void removeUpdate(DocumentEvent e) {
				complexEnemyTemplate.setReverseFemaleRapeCapture(reverseRapeFemaleCaptureField.getText());
			}
			
			public void changedUpdate(DocumentEvent e) {
				complexEnemyTemplate.setReverseFemaleRapeCapture(reverseRapeFemaleCaptureField.getText());
			}
		});
		
		this.add(submitMaleField, "2,7");
		submitMaleField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				complexEnemyTemplate.setMaleSubmit(submitMaleField.getText());
			}
			
			public void removeUpdate(DocumentEvent e) {
				complexEnemyTemplate.setMaleSubmit(submitMaleField.getText());
			}
			
			public void changedUpdate(DocumentEvent e) {
				complexEnemyTemplate.setMaleSubmit(submitMaleField.getText());
			}
		});
		
		this.add(rapeMaleField, "2,8");
		rapeMaleField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				complexEnemyTemplate.setMaleRape(rapeMaleField.getText());
			}
			
			public void removeUpdate(DocumentEvent e) {
				complexEnemyTemplate.setMaleRape(rapeMaleField.getText());
			}
			
			public void changedUpdate(DocumentEvent e) {
				complexEnemyTemplate.setMaleRape(rapeMaleField.getText());
			}
		});
		
		this.add(reverseRapeMaleField, "2,9");
		reverseRapeMaleField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				complexEnemyTemplate.setReverseMaleRape(reverseRapeMaleField.getText());
			}
			
			public void removeUpdate(DocumentEvent e) {
				complexEnemyTemplate.setReverseMaleRape(reverseRapeMaleField.getText());
			}
			
			public void changedUpdate(DocumentEvent e) {
				complexEnemyTemplate.setReverseMaleRape(reverseRapeMaleField.getText());
			}
		});
		
		this.add(reverseRapeMaleCaptureField, "2,10");
		reverseRapeMaleCaptureField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				complexEnemyTemplate.setReverseMaleRapeCapture(reverseRapeMaleCaptureField.getText());
			}
			
			public void removeUpdate(DocumentEvent e) {
				complexEnemyTemplate.setReverseMaleRapeCapture(reverseRapeMaleCaptureField.getText());
			}
			
			public void changedUpdate(DocumentEvent e) {
				complexEnemyTemplate.setReverseMaleRapeCapture(reverseRapeMaleCaptureField.getText());
			}
		});
		
		this.add(encounterField, "2,12");
		encounterField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				complexEnemyTemplate.setTextEncounter(encounterField.getText());
			}
			
			public void removeUpdate(DocumentEvent e) {
				complexEnemyTemplate.setTextEncounter(encounterField.getText());
			}
			
			public void changedUpdate(DocumentEvent e) {
				complexEnemyTemplate.setTextEncounter(encounterField.getText());
			}
		});
		
		this.add(summoningField, "2,13");
		summoningField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				complexEnemyTemplate.setTextSummoning(summoningField.getText());
			}
			
			public void removeUpdate(DocumentEvent e) {
				complexEnemyTemplate.setTextSummoning(summoningField.getText());
			}
			
			public void changedUpdate(DocumentEvent e) {
				complexEnemyTemplate.setTextSummoning(summoningField.getText());
			}
		});
		
		this.add(captureField, "2,14");
		captureField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				complexEnemyTemplate.setTextCapture(captureField.getText());
			}
			
			public void removeUpdate(DocumentEvent e) {
				complexEnemyTemplate.setTextCapture(captureField.getText());
			}
			
			public void changedUpdate(DocumentEvent e) {
				complexEnemyTemplate.setTextCapture(captureField.getText());
			}
		});
	}
	
	public void setComplexEnemyTemplate(ComplexEnemyTemplate enemy) {
		this.complexEnemyTemplate = enemy;
		submitFemaleField.setText(enemy.getFemaleSubmit());
		submitMaleField.setText(enemy.getMaleSubmit());
		rapeFemaleField.setText(enemy.getFemaleRape());
		rapeMaleField.setText(enemy.getMaleRape());
		reverseRapeFemaleField.setText(enemy.getReverseFemaleRape());
		reverseRapeMaleField.setText(enemy.getReverseMaleRape());
		reverseRapeMaleCaptureField.setText(enemy.getReverseMaleRapeCapture());
		reverseRapeFemaleCaptureField.setText(enemy.getReverseFemaleRapeCapture());
		encounterField.setText(enemy.getTextEncounter());
		summoningField.setText(enemy.getTextSummoning());
		captureField.setText(enemy.getTextCapture());
		repaint();
	}
}

