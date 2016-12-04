package parkingControl;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.awt.event.ActionEvent;

public class DialogInsertVehicle extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtPlate;
	private ParkedVehicle v = null;
	/**
	 * Launch the application.
	 */
	public ParkedVehicle showDialog() {
		try {
			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			this.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.v;
	}
	

	/**
	 * Create the dialog.
	 */
	public DialogInsertVehicle() {
		setModal(true);
		setTitle("Agregar nuevo vehiculo");
		setBounds(100, 100, 557, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JRadioButton rdbtnLight = new JRadioButton("liviano");
		rdbtnLight.setBounds(8, 173, 149, 23);
		contentPanel.add(rdbtnLight);
		rdbtnLight.setSelected(true);
		
		JRadioButton rdbtnMotorcycle = new JRadioButton("moto");
		rdbtnMotorcycle.setBounds(191, 173, 149, 23);
		contentPanel.add(rdbtnMotorcycle);
		
		JRadioButton rdbtnHeavy = new JRadioButton("pesado");
		rdbtnHeavy.setBounds(373, 173, 149, 23);
		contentPanel.add(rdbtnHeavy);
		ButtonGroup types = new ButtonGroup();
		types.add(rdbtnHeavy);
		types.add(rdbtnLight);
		types.add(rdbtnMotorcycle);
		
		JLabel lblNewLabel = new JLabel("Seleccione tipo de vehiculo");
		lblNewLabel.setBounds(22, 150, 454, 15);
		contentPanel.add(lblNewLabel);
		
		txtPlate = new JTextField();
		txtPlate.setBounds(171, 49, 169, 39);
		contentPanel.add(txtPlate);
		txtPlate.setColumns(10);
		
		JLabel lblPlaca = new JLabel("Placa");
		lblPlaca.setBounds(22, 55, 119, 27);
		contentPanel.add(lblPlaca);
		
		JLabel lblHora = new JLabel("Hora:");
		lblHora.setBounds(22, 123, 70, 15);
		contentPanel.add(lblHora);
		
		JLabel lblTime = new JLabel("New label");
		lblTime.setBounds(104, 123, 70, 15);
		contentPanel.add(lblTime);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Guardar");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if(!txtPlate.getText().isEmpty()){
							v = new ParkedVehicle();
							v.setPlate(txtPlate.getText());
							java.util.Date date = new java.util.Date();
							Timestamp timestamp = new Timestamp(date.getTime());
							v.setEntryTime(timestamp);
							for (Enumeration<AbstractButton> buttons = types.getElements(); buttons.hasMoreElements();) {
								AbstractButton button = buttons.nextElement();
								if (button.isSelected()) {
									v.setVehicleType(button.getText());
								}
							}
							setVisible(false);
						}
						else{
							JOptionPane.showMessageDialog(null, "Inserte el numero de placa");
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancelar");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
