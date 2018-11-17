package de.tuda.dmdb.access.exercise;

import de.tuda.dmdb.storage.AbstractRecord;
import de.tuda.dmdb.access.RecordIdentifier;
import de.tuda.dmdb.storage.PageManager;
import de.tuda.dmdb.access.HeapTableBase;

public class HeapTable extends HeapTableBase {

	/**
	 * 
	 * Constructs table from record prototype
	 * @param prototypeRecord
	 */
	public HeapTable(AbstractRecord prototypeRecord) {
		super(prototypeRecord);
	}

	@Override
	public RecordIdentifier insert(AbstractRecord record) {
		//TODO: Insert your own implementation from exercise02
		return null;
	}

	@Override
	public AbstractRecord lookup(int pageNumber, int slotNumber) {
		//TODO: Insert your own implementation from exercise02
		return null;
	}

}
