/*
 * Created on May 14, 2006 by wyatt
 */
package org.homeunix.drummer.view.logic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.Date;

import org.homeunix.drummer.Const;
import org.homeunix.drummer.Translate;
import org.homeunix.drummer.TranslateKeys;
import org.homeunix.drummer.controller.DataInstance;
import org.homeunix.drummer.model.DataModel;
import org.homeunix.drummer.model.Schedule;
import org.homeunix.drummer.model.Transaction;
import org.homeunix.drummer.util.DateUtil;
import org.homeunix.drummer.util.Log;
import org.homeunix.drummer.view.AbstractBudgetDialog;
import org.homeunix.drummer.view.layout.ModifyScheduleDialogLayout;

public class ModifyScheduleDialog extends ModifyScheduleDialogLayout {
	public static final long serialVersionUID = 0;
	
	private final Schedule schedule;

	public ModifyScheduleDialog(Schedule schedule){
		super(MainBuddiFrame.getInstance());
		
		this.schedule = schedule;
		initContent();
		updateSchedulePulldown();
		transaction.updateContent();
		
		loadSchedule(schedule);
	}

	protected String getType(){
		return Translate.getInstance().get(TranslateKeys.ACCOUNT);
	}
		
	@Override
	protected AbstractBudgetDialog initActions() {
		okButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				ModifyScheduleDialog.this.saveSchedule();
				ModifyScheduleDialog.this.setVisible(false);
				ModifyScheduleDialog.this.dispose();
			}
		});
		
		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				ModifyScheduleDialog.this.setVisible(false);
			}
		});
		
		frequencyPulldown.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				ModifyScheduleDialog.this.updateSchedulePulldown();
			}
		});
		
		return this;
	}

	@Override
	protected AbstractBudgetDialog initContent() {
		updateContent();
		
		
		return this;
	}

	public AbstractBudgetDialog updateContent(){
		
		return this;
	}
	
	private void updateSchedulePulldown(){
		if (getFrequencyType().equals(TranslateKeys.WEEK.toString())){
			scheduleModel.removeAllElements();
			for (String day : Const.DAYS_IN_WEEK) {
				scheduleModel.addElement(day);	
			}
		}
		else if (getFrequencyType().equals(TranslateKeys.MONTH.toString())){
			scheduleModel.removeAllElements();
			Calendar c = Calendar.getInstance();
			c.setTime(DateUtil.getEndOfMonth(new Date(), 0));
			int daysInMonth = c.get(Calendar.DAY_OF_MONTH);
			for(int i = 1; i <= daysInMonth; i++){
				scheduleModel.addElement(i);
			}
		}
		else{
			Log.error("Unknown frequency type: " + getFrequencyType());
		}
	}
	
	private void saveSchedule(){
		if (this.schedule == null){
			Transaction t = DataInstance.getInstance().getDataModelFactory().createTransaction();
			t.setAmount(transaction.getAmount());
			t.setDescription(transaction.getDescription());
			t.setNumber(transaction.getNumber());
			t.setMemo(transaction.getMemo());
			t.setTo(transaction.getTo());
			t.setFrom(transaction.getFrom());

			DataInstance.getInstance().addSchedule(scheduleName.getText(), startDateChooser.getDate(), null, getFrequencyType(), getScheduleDay(), t);
		}
		else{
			schedule.setScheduleName(scheduleName.getText());
			schedule.setAmount(transaction.getAmount());
			schedule.setDescription(transaction.getDescription());
			schedule.setNumber(transaction.getNumber());
			schedule.setMemo(transaction.getMemo());
			schedule.setTo(transaction.getTo());
			schedule.setFrom(transaction.getFrom());
			schedule.setStartDate(startDateChooser.getDate());
			schedule.setFrequencyType(getFrequencyType());
			schedule.setScheduleDay(getScheduleDay());
			
			DataInstance.getInstance().saveDataModel();
		}
	}
	
	private void loadSchedule(Schedule s){
		if (s != null){
			transaction.updateContent();
			updateSchedulePulldown();
			
			startDateChooser.setDate(s.getStartDate());
			frequencyPulldown.setSelectedItem(s.getFrequencyType());
			schedulePulldown.setSelectedIndex(s.getScheduleDay());
			Transaction t = DataInstance.getInstance().getDataModelFactory().createTransaction();
			t.setAmount(s.getAmount());
			t.setDescription(s.getDescription());
			t.setNumber(s.getNumber());
			t.setMemo(s.getMemo());
			t.setTo(s.getTo());
			t.setFrom(s.getFrom());
			Log.debug("Transaction to load: " + t);
			transaction.setTransaction(t, true);
		}
	}
	
	private String getFrequencyType(){
		Object o = frequencyPulldown.getSelectedItem();
		
		return o.toString(); 
	}
	
	private Integer getScheduleDay(){
		Object o = schedulePulldown.getSelectedItem();
		
		if (o instanceof Integer){
			return ((Integer) o) - 1;
		}
		else {
			for (int i = 0; i < Const.DAYS_IN_WEEK.length; i++) {
				if (Const.DAYS_IN_WEEK[i].equals(o))
					return i;
			}
			
			Log.debug("Unknown object when getting schedule day: " + o);
			return -1;
		}		
	}
}