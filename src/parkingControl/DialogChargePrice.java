package parkingControl;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;

public class DialogChargePrice extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JLabel lblCharge;
	private JLabel lblPlate;
	private JLabel lblType;
	private JLabel lblEntryTime;
	private JLabel lblDate;
	private JLabel lblDepartureTime;
	private JLabel lblElapsedTime; 
	private int result = -1;

	/**
	 * Create the dialog.
	 */
	public DialogChargePrice() {
		setTitle("Cobro");
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 405, 237);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("Placa:");
			lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 23));
			lblNewLabel.setBounds(89, 8, 71, 24);
			contentPanel.add(lblNewLabel);
		}
		
		JLabel lblNewLabel_1 = new JLabel("Tipo");
		lblNewLabel_1.setBounds(36, 49, 46, 14);
		contentPanel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Hora entrada");
		lblNewLabel_2.setBounds(36, 70, 94, 14);
		contentPanel.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Hora salida");
		lblNewLabel_3.setBounds(36, 95, 94, 14);
		contentPanel.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("Cobro");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_4.setBounds(269, 61, 57, 24);
		contentPanel.add(lblNewLabel_4);
		
		lblCharge = new JLabel();
		lblCharge.setFont(new Font("Tahoma", Font.PLAIN, 23));
		lblCharge.setBounds(269, 95, 94, 28);
		contentPanel.add(lblCharge);
		
		lblPlate = new JLabel("New label");
		lblPlate.setFont(new Font("Tahoma", Font.PLAIN, 23));
		lblPlate.setBounds(170, 11, 133, 19);
		contentPanel.add(lblPlate);
		
		lblType = new JLabel("New label");
		lblType.setBounds(154, 49, 71, 14);
		contentPanel.add(lblType);
		
		lblEntryTime = new JLabel("New label");
		lblEntryTime.setBounds(154, 70, 71, 14);
		contentPanel.add(lblEntryTime);
		
		lblDepartureTime = new JLabel("New label");
		lblDepartureTime.setBounds(154, 95, 71, 14);
		contentPanel.add(lblDepartureTime);
		
		JLabel lbl_A = new JLabel("Fecha");
		lbl_A.setBounds(36, 141, 71, 14);
		contentPanel.add(lbl_A);
		
		lblDate = new JLabel("New label");
		lblDate.setBounds(154, 141, 71, 14);
		contentPanel.add(lblDate);
		
		JLabel lblNewLabel_5 = new JLabel("Transcurrido");
		lblNewLabel_5.setBounds(36, 120, 94, 14);
		contentPanel.add(lblNewLabel_5);
		
		lblElapsedTime = new JLabel("New label");
		lblElapsedTime.setBounds(154, 116, 71, 14);
		contentPanel.add(lblElapsedTime);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Imprimir Factura");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						result = 1;
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancelar");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						result = -1;
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public int showDialog() {
		try {
			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			this.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public DialogChargePrice(ParkedVehicleHistory hv){
		this();
		SimpleDateFormat tf = new SimpleDateFormat("hh:mm");
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		lblCharge.setText(hv.getCharge() + "");
		lblPlate.setText(hv.getPlate());
		lblType.setText(hv.getVehicleType());
		lblDate.setText(df.format(hv.getDepartureTime()));
		lblDepartureTime.setText(tf.format(hv.getDepartureTime()));
		lblEntryTime.setText(tf.format(hv.getEntryTime()));
		lblElapsedTime.setText(ChargeCalculator.getVehicleTimeDifference(hv));
	}

}
