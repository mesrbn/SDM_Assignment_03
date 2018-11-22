package de.tuda.dmdb.access.exercise;

import de.tuda.dmdb.access.AbstractIndexElement;
import de.tuda.dmdb.access.LeafBase;
import de.tuda.dmdb.access.RecordIdentifier;
import de.tuda.dmdb.access.UniqueBPlusTreeBase;
import de.tuda.dmdb.storage.AbstractRecord;
import de.tuda.dmdb.storage.types.AbstractSQLValue;
import de.tuda.dmdb.storage.types.exercise.SQLInteger;

/**
 * Index leaf
 * @author cbinnig
 */
public class Leaf<T extends AbstractSQLValue> extends LeafBase<T>{

	/**
	 * Leaf constructor
	 * @param uniqueBPlusTree TODO
	 */
	public Leaf(UniqueBPlusTreeBase<T> uniqueBPlusTree){
		super(uniqueBPlusTree);
	}

	/**
	 * @param key key of the record to find
	 * @return if found, returns the record, otherwise null
	 */
	@Override
	public AbstractRecord lookup(T key) {
		//TODO: implement this method
		int slotNumber = this.binarySearch(key);
		AbstractRecord leafRecord = this.uniqueBPlusTree.getLeafRecPrototype().clone();
		if (slotNumber < this.indexPage.getNumRecords()) {
			this.indexPage.read(slotNumber, leafRecord);
			SQLInteger tablePage = (SQLInteger)leafRecord.getValue(1);
			SQLInteger tableSlot = (SQLInteger)leafRecord.getValue(2);
			AbstractRecord retrieved = this.uniqueBPlusTree.getTable().lookup(tablePage.getValue(), tableSlot.getValue());
			if (slotNumber != 0) {
				return retrieved;
			}

			if (key.equals(retrieved.getValue(this.uniqueBPlusTree.getKeyColumnNumber()))) {
				return retrieved;
			}
		}

		return null;
	}

	/**
	 * @param key key of the record
	 * @param record record to be inserted
	 * @return true if insertion was correct, false otherwise
	 */
	@Override
	public boolean insert(T key, AbstractRecord record){
		//TODO: implement this method
		//search for key and return false if existing
		int slotNumber = 0;
		AbstractRecord leafRecord = this.uniqueBPlusTree.getLeafRecPrototype().clone();
		if (this.indexPage.getNumRecords() > 0) {
			slotNumber = this.binarySearch(key);
			if (slotNumber < this.indexPage.getNumRecords()) {
				this.indexPage.read(slotNumber, leafRecord);
				if (leafRecord.getValue(this.uniqueBPlusTree.getKeyColumnNumber()).compareTo(key) == 0) {
					return false;
				}
			}
		}

		RecordIdentifier rid = this.uniqueBPlusTree.getTable().insert(record);
		leafRecord.setValue(0, key);
		leafRecord.setValue(1, new SQLInteger(rid.getPageNumber()));
		leafRecord.setValue(2, new SQLInteger(rid.getSlotNumber()));
		this.indexPage.insert(slotNumber, leafRecord, true);
		return true;
	}
	
	@Override
	public AbstractIndexElement<T> createInstance() {
		return new Leaf<T>(this.uniqueBPlusTree);
	}
}