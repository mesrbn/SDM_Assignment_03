package de.tuda.dmdb.storage.exercise;

import de.tuda.dmdb.storage.AbstractPage;
import de.tuda.dmdb.storage.AbstractRecord;
import de.tuda.dmdb.storage.types.AbstractSQLValue;
import de.tuda.dmdb.storage.types.exercise.SQLInteger;


public class RowPage extends AbstractPage {

	/**
	 * Constructor for a row page with a given (fixed) slot size
	 * @param slotSize
	 */
	public RowPage(int slotSize) {
		super(slotSize);
	}
	
	@Override
	public void insert(int slotNumber, AbstractRecord record, boolean doInsert) {
		//TODO: Insert your own implementation from exercise02

	}
	
	@Override
	public int insert(AbstractRecord record){
		//TODO: Insert your own implementation from exercise02
		return 0;
	}

	
	@Override
	public void read(int slotNumber, AbstractRecord record){
		//TODO: Insert your own implementation from exercise02

	}
}
