package de.tuda.dmdb.access.exercise;

import de.tuda.dmdb.access.AbstractTable;
import de.tuda.dmdb.access.UniqueBPlusTreeBase;
import de.tuda.dmdb.access.AbstractIndexElement;
import de.tuda.dmdb.storage.AbstractRecord;
import de.tuda.dmdb.storage.types.AbstractSQLValue;
import de.tuda.dmdb.storage.types.exercise.SQLInteger;

import java.util.ArrayList;

/**
 * Unique B+-Tree implementation 
 * @author cbinnig
 *
 * @param <T>
 */

public class UniqueBPlusTree<T extends AbstractSQLValue> extends UniqueBPlusTreeBase<T> {
	static final int LEFT_NODE = 0;
	static final int RIGHT_NODE = 1;
	
	/**
	 * Constructor of B+-Tree with user-defined fil-grade
	 * @param table Table to be indexed
	 * @param keyColumnNumber Number of unique column which should be indexed
	 * @param fillGrade fill grade of index
	 */
	public UniqueBPlusTree(AbstractTable table, int keyColumnNumber, int fillGrade) {
		super(table, keyColumnNumber, fillGrade);
	} 
	
	/**
	 * Constructor for B+-tree with default fill grade
	 * @param table table to be indexed 
	 * @param keyColumnNumber Number of unique column which should be indexed
	 */
	public UniqueBPlusTree(AbstractTable table, int keyColumnNumber) {
		this(table, keyColumnNumber, DEFAULT_FILL_GRADE);
	}

	/**
	 * @param record record to be inserted
	 * @return true if insertion was correct, false otherwise
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	public boolean insert(AbstractRecord record) {
		//insert record
		T key = (T) record.getValue(this.keyColumnNumber);
		boolean inserted = this.root.insert(key, record);
		if (inserted && this.root.isFull()) {
			AbstractIndexElement<T> indexElement1 = this.root.createInstance();
			AbstractIndexElement<T> indexElement2 = this.root.createInstance();
			this.root.split(indexElement1, indexElement2);
			this.root = new Node(this);
			AbstractRecord nodeRecord = this.nodeRecPrototype.clone();
			nodeRecord.setValue(0, indexElement1.getMaxKey());
			nodeRecord.setValue(1, new SQLInteger(indexElement1.getPageNumber()));
			this.root.getIndexPage().insert(nodeRecord);
			nodeRecord.setValue(0, indexElement2.getMaxKey());
			nodeRecord.setValue(1, new SQLInteger(indexElement2.getPageNumber()));
			this.root.getIndexPage().insert(nodeRecord);
		}

		return inserted;


	}

	/**
	 * @param key key of the record to find
	 * @return if found, returns the record, otherwise null
	 */
	@Override
	public AbstractRecord lookup(T key) {
		AbstractRecord myRec = root.lookup(key);
		return myRec;
	}

}
