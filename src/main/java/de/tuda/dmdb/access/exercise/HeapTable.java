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
		if (!this.lastPage.recordFitsIntoPage(record)) {
			this.lastPage = PageManager.createDefaultPage(this.prototype.getFixedLength());
			this.addPage(this.lastPage);
		}

		int slotNumber = this.lastPage.insert(record);
		++this.recordCount;
		RecordIdentifier recordID = new RecordIdentifier(this.lastPage.getPageNumber(), slotNumber);
		this.recordIDMapping.add(recordID);
		return recordID;
	}

	@Override
	public AbstractRecord lookup(int pageNumber, int slotNumber) {
		//TODO: Insert your own implementation from exercise02
		AbstractRecord record = this.prototype.clone();
		this.getPage(pageNumber).read(slotNumber, record);
		return record;
	}

}
