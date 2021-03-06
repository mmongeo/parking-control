package parkingControl;

import jpos.*;
import jpos.events.*;
import jpos.util.JposPropertiesConst;
import jp.co.epson.upos.UPOSConst;

import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;

public class PosPrinter implements StatusUpdateListener,
OutputCompleteListener, ErrorListener{
	POSPrinterControl114 ptr;
	boolean buttonStateByCover = true;
	boolean buttonStateByPaper = true;
	boolean bCoverSensor = false;
	SimpleDateFormat tf;
	SimpleDateFormat df;

//	A maximum of 2 line widths will be considered
	public final int MAX_LINE_WIDTHS = 2;
	
	private void printHeader() throws JposException{
		ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, "\u001b|1C   Parqueo Shalom     \n");
		ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, "\u001b|1C   Shalom Aleichem SAPAJ S.A.\n");
		ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, "\u001b|1C   Cedula juridica: 3-101-670604\n");	
		ptr.setRecLineSpacing(ptr.getRecLineHeight());
	}
	
	public PosPrinter(){
		ptr = (POSPrinterControl114)new POSPrinter();
		tf = new SimpleDateFormat("hh:mm a");
		df = new SimpleDateFormat("dd/MM/yyyy");
		// JavaPOS's code for Step10
		// Set StatusUpdateEvent listener
		ptr.addStatusUpdateListener(this);
		// Set ErrorEvent listener
		ptr.addErrorListener(this);
		// JavaPOS's code for Step10--END

		// JavaPOS's code for Step7
		// Set OutputCompleteEvent listener
		ptr.addOutputCompleteListener(this);
		// JavaPOS's code for Step7--END

		// JavaPOS's code for Step10
		try {
			//Open the device.
			//Use the name of the device that connected with your computer.
			ptr.open("POSPrinter");

		}
		catch(JposException ex){
			JOptionPane.showMessageDialog(null, "Error de impresora."
					+ "\n Revise que este conectada.",
					"",JOptionPane.WARNING_MESSAGE);
			//Nothing can be used.
			return;
		}

		try {
			//Get the exclusive control right for the opened device.
			//Then the device is disable from other application.
			ptr.claim(1000);
			bCoverSensor = ptr.getCapCoverSensor();
		}
		catch(JposException ex){
			JOptionPane.showMessageDialog(null, "Solo puede abrir una aplicacion a la vez.\n"
					+ "Cierre todas las ventanas e intente de nuevo. \n"
					+ "Asegurese que la impresora este conectada para iniciar la aplicacion.",
					"",JOptionPane.WARNING_MESSAGE);
			System.exit(1);
			//Nothing can be used.
			return;
		}

		try {
			//Enable the device.
			ptr.setDeviceEnabled(true);
		}
		catch(JposException ex){
			JOptionPane.showMessageDialog(null, "Interrupcion del dispositivo impresora",
					"",JOptionPane.WARNING_MESSAGE);
			//Nothing can be used.
			return;
		}
	}
		
	
	public void printReceipt(ParkedVehicleHistory v){

		// JavaPOS's code for Step10
		try{
			// keep a current LineSpacing.
			int orgSpacing = ptr.getRecLineSpacing();
			if (ptr.getCapRecPresent() == true){

				while (true){
					try{
						//Batch processing mode
						ptr.transactionPrint(POSPrinterConst.PTR_S_RECEIPT, POSPrinterConst.PTR_TP_TRANSACTION);
						
						//Enter the sideway mode.
						//ptr.rotatePrint(POSPrinterConst.PTR_S_RECEIPT, POSPrinterConst.PTR_RP_LEFT90);

						this.printHeader();
						ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, "\u001b|2C Placa: \n \u001b|2C" + v.getPlate() + "\n\n");
						ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, "\u001b|2C Cobro (CRC): \n \u001b|2C" + v.getCharge() + "\n");
						ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, "       \n");
						ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, "\u001b|1C   Fecha: " + df.format(v.getEntryTime()) + "\n");
						ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, "\u001b|1C   Hora entrada: " + tf.format(v.getEntryTime()) + "\n");
						ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, "\u001b|1C   Hora salida:  " + tf.format(v.getDepartureTime()) + "\n");
						break;
					}
					catch(JposException ex){
						// When error occurs, display a message to ask the user whether retry or not.
						int selectedValue = JOptionPane.showConfirmDialog(null, "La impresion fallo.\n\nReintentar?"
								, "", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
						if (selectedValue == JOptionPane.CANCEL_OPTION){
							try{
								// Clear the buffered data since the buffer retains print data when an error occurs during printing.
								ptr.clearOutput();
							}
							catch(JposException ex1){
							}
							return;
						}
						else if (selectedValue == JOptionPane.NO_OPTION){
							break;
						}
						try{
							// Clear the buffered data since the buffer retains print data when an error occurs during printing.
							ptr.clearOutput();
						}
						catch(JposException ex1){
						}
						continue;
					}
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Error de impresora",
						"",JOptionPane.WARNING_MESSAGE);
				return;
			}

			ptr.rotatePrint(POSPrinterConst.PTR_S_RECEIPT, POSPrinterConst.PTR_RP_NORMAL);

			//Feed the receipt to the cutter position automatically, and cut.
			//   ESC|#fP = Line Feed and Paper cut
			ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, "\u001b|fP");

			while(ptr.getState() != JposConst.JPOS_S_IDLE){
				try{
					Thread.sleep(100);
				}
				catch(Exception ex){
				}
			}
			//print all the buffer data. and exit the batch processing mode.
			ptr.transactionPrint(POSPrinterConst.PTR_S_RECEIPT, POSPrinterConst.PTR_TP_NORMAL);

			ptr.setRecLineSpacing(orgSpacing);
		}
		catch(JposException jex){
			JOptionPane.showMessageDialog(null, "Revise la impresora.\n"
					+ "No se pudo iniciar" ,
					"Printed receipt",JOptionPane.WARNING_MESSAGE);
		}
		// JavaPOS's code for Step10--END		
	}
	
	public void printEntryTicket(ParkedVehicle v){

		// JavaPOS's code for Step10
		try{
			// keep a current LineSpacing.
			int orgSpacing = ptr.getRecLineSpacing();
			if (ptr.getCapRecPresent() == true){

				while (true){
					try{
						//Batch processing mode
						ptr.transactionPrint(POSPrinterConst.PTR_S_RECEIPT, POSPrinterConst.PTR_TP_TRANSACTION);

						//Enter the sideway mode.
						//ptr.rotatePrint(POSPrinterConst.PTR_S_RECEIPT, POSPrinterConst.PTR_RP_LEFT90);
						this.printHeader();
						ptr.setRecLineSpacing(ptr.getRecLineHeight());
						ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, "       \n");
						ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, "\u001b|2C Placa: " + v.getPlate() + "\n\n");
						ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, "\u001b|1C   Fecha: " + df.format(v.getEntryTime()) + "\n");
						ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, "\u001b|1C   Hora entrada: " + tf.format(v.getEntryTime()) + "\n\n\n");
						break;
					}
					catch(JposException ex){
						// When error occurs, display a message to ask the user whether retry or not.
						int selectedValue = JOptionPane.showConfirmDialog(null, "La impresion fallo.\n\nReintentar?"
								, "", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
						if (selectedValue == JOptionPane.CANCEL_OPTION){
							try{
								// Clear the buffered data since the buffer retains print data when an error occurs during printing.
								ptr.clearOutput();
							}
							catch(JposException ex1){
							}
							return;
						}
						else if (selectedValue == JOptionPane.NO_OPTION){
							break;
						}
						try{
							// Clear the buffered data since the buffer retains print data when an error occurs during printing.
							ptr.clearOutput();
						}
						catch(JposException ex1){
						}
						continue;
					}
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Error de impresora",
						"",JOptionPane.WARNING_MESSAGE);
				return;
			}

			ptr.rotatePrint(POSPrinterConst.PTR_S_RECEIPT, POSPrinterConst.PTR_RP_NORMAL);

			//Feed the receipt to the cutter position automatically, and cut.
			//   ESC|#fP = Line Feed and Paper cut
			ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, "\u001b|fP");

			while(ptr.getState() != JposConst.JPOS_S_IDLE){
				try{
					Thread.sleep(100);
				}
				catch(Exception ex){
				}
			}
			//print all the buffer data. and exit the batch processing mode.
			ptr.transactionPrint(POSPrinterConst.PTR_S_RECEIPT, POSPrinterConst.PTR_TP_NORMAL);

			ptr.setRecLineSpacing(orgSpacing);
		}
		catch(JposException jex){
			JOptionPane.showMessageDialog(null, "Impresora no encontrada\n" ,
					"Printed receipt",JOptionPane.WARNING_MESSAGE);
		}
		// JavaPOS's code for Step10--END		
	}

	@Override
	public void errorOccurred(ErrorEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void outputCompleteOccurred(OutputCompleteEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void statusUpdateOccurred(StatusUpdateEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
